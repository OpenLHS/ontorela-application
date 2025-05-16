package ca.griis.ontorela.converter.factory;

import ca.griis.monto.api.OntoAxiomClassAssociationI;
import ca.griis.monto.model.Participation;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRelGraph;
import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.ParticipationConstraint;
import ca.griis.ontorela.mrel.Schema;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.TableJoin;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * create table for ClassAxiomTable
 *
 * <h3>Historique</h3>
 * <p>
 * 2025-01-31 [AS] - Implémentation initiale <br>
 * </p>
 *
 * <h3>Tâches</h3>
 * S.O.
 *
 * @author [AS] Amnei.Souid@USherbrooke.ca
 * @version 2.0.1
 * @since 2.0.1
 */
public class ClassAxiomTableFactory extends TableFactory<OntoAxiomClassAssociationI> {
  public ClassAxiomTableFactory(OntoRelCat dic, OntoRelGraph ontoRelGraph, Schema baseSchema,
      DatabaseConfiguration dbConfig, Table topTable) {
    super(dic, ontoRelGraph, baseSchema, dbConfig, topTable);
  }

  /**
   * Création d'une table de jointure pour un axiome d'association de classe.
   * Si la table existe déjà elle sera retourner.
   * TODO 2018-09-17 CK : traiter les clés primaires ayant plusieurs attributs.
   *
   * @param axiom : un axiome d'association de classe.
   * @return Une table de jointure pour un axiome.
   */
  @Override
  public TableJoin createTable(OntoAxiomClassAssociationI axiom) {
    TableJoin joinTable = this.dic.getClassAxiomTableCatalog().get(axiom);
    // ======== Vérifier si l'axiome possède déjà une table de correspondance.
    if (joinTable == null) {
      Table determinant = this.dic.getClassTableCatalog().get(axiom.getOntoDeterminant());
      Table dependent = this.dic.getClassTableCatalog().get(axiom.getOntoDependent());
      assert determinant != null : "Table not found : " + axiom.getOntoDeterminant();
      // dependant peut etre null en axiome lorsque removeThingTable est true
      // exemple River hasEstuary exactly 1 owl:Thing
      if (dependent != null) {
        // si table Thing est ajouter creer les axiomes ayant dependant Thing
        // ==== Créer les table de jointure
        // // Récupérer l'attribut clé du déterminant.
        Map<Attribute, Attribute> joinAtt = new LinkedHashMap<>();
        Attribute determinantKey =
            determinant.getPrimaryKeyAttributeSet().stream().findFirst().get().clone();
        joinAtt.put(determinantKey,
            determinant.getPrimaryKeyAttributeSet().stream().findFirst().get());
        // Récupérer l'attribut clé du dépendent
        Attribute dependentKey =
            dependent.getPrimaryKeyAttributeSet().stream().findFirst().get().clone();
        joinAtt.put(dependentKey, dependent.getPrimaryKeyAttributeSet().stream().findFirst().get());
        // Gérér les boucles (ex. A has part 1 A)
        if (dependentKey.equals(determinantKey)) {
          determinantKey.setAttId(determinantKey.getAttId() + "_domain");
          dependentKey.setAttId(dependentKey.getAttId() + "_range");

          determinantKey.setAttIri(determinantKey.getAttIri() + "_domain");
          dependentKey.setAttIri(dependentKey.getAttIri() + "_range");

          determinantKey.getAttLabels().replaceAll((key, value) -> value + "_domain");
          dependentKey.getAttLabels().replaceAll((key, value) -> value + "_range");
        }
        String tableIri = determinant.getIri() + "_" + axiom.getProperty().getIri().getShortIri()
            + "_" + dependent.getIri();
        String schemaId = baseSchema.getName();
        joinTable =
            new TableJoin(Table.TableOrigin.CLASSAXIOM, axiom, schemaId, tableIri, determinant,
                dependent, (Participation) axiom.getParticipation(), joinAtt);
        // Ajouter la clé du déterminant
        Set<Attribute> attKeySet = new LinkedHashSet<>();
        attKeySet.add(determinantKey);
        if (axiom.getProperty().isFunctional()) {
          joinTable.addKey(attKeySet, true);
        } else {
          attKeySet.add(dependentKey);
          joinTable.addKey(attKeySet, true);
        }
        // // Ajouter la clé du dépendant
        joinTable.addAttribute(dependentKey);
        // // Ajouter les étiquettes et les définitions
        for (String l : this.dbConfig.getLanguages()) {
          String label = String.join(" ",
              axiom.getOntoDeterminant().getAnnotations().getLabelValues(new Locale(l)).stream()
                  .findFirst().orElse(axiom.getOntoDeterminant().getIri().getShortIri()),
              axiom.getProperty().getAnnotations().getLabelValues(new Locale(l)).stream()
                  .findFirst()
                  .orElse(axiom.getProperty().getIri().getShortIri()),
              axiom.getOntoDependent().getAnnotations().getLabelValues(new Locale(l)).stream()
                  .findFirst().orElse(axiom.getOntoDependent().getIri().getShortIri()));
          joinTable.addLabel(l, label);
          String def = String.join(" ",
              axiom.getOntoDeterminant().getAnnotations().getDefinitionValues(new Locale(l))
                  .stream()
                  .findFirst().orElse(null),
              axiom.getProperty().getAnnotations().getDefinitionValues(new Locale(l)).stream()
                  .findFirst().orElse(null),
              axiom.getOntoDependent().getAnnotations().getDefinitionValues(new Locale(l)).stream()
                  .findFirst().orElse(null));
          joinTable.addDescription(l, def);
        }
        // ======== Ajouter la table dans le schéma
        baseSchema.addTable(joinTable);
        baseSchema.addTemporalPartition(joinTable);
        ontoRelGraph.addNode(OntoRelGraph.NodeType.CLASSAXIOMNODE, joinTable);
        // ======== Ajouter les contraintes référentielles
        ForeignKey determinantFk =
            baseSchema.addFkForDefaultKey(ForeignKey.ForeignKeyType.OBJECTPROPERTY,
                joinTable, determinant, determinantKey);
        ontoRelGraph.addLink(OntoRelGraph.EdgeType.OPEDGE, determinant, joinTable, determinantFk);
        ForeignKey dependantFk =
            baseSchema.addFkForDefaultKey(ForeignKey.ForeignKeyType.OBJECTPROPERTY,
                joinTable, dependent, dependentKey);
        ontoRelGraph.addLink(OntoRelGraph.EdgeType.OPEDGE, joinTable, dependent, dependantFk);
        // ========= Ajouter FK
        // Si les propriétés de classes sont converties en table, ajouter un FK vers cette table.
        if (dbConfig.getGenerateOpTable()) {
          TableJoin propertyTable =
              this.dic.getObjectPropertyTableCatalog().get(axiom.getProperty());
          if (propertyTable != null) {
            Map<Attribute, Attribute> linkedAtt = new LinkedHashMap<>();
            Attribute[] propertyAtt = propertyTable.getJoinAtt().keySet()
                .toArray(new Attribute[propertyTable.getJoinAtt().values().size()]);
            linkedAtt.put(determinantKey, propertyAtt[0]);
            linkedAtt.put(dependentKey, propertyAtt[1]);
            ForeignKey propertyFk =
                baseSchema.addFk(ForeignKey.ForeignKeyType.ISA, joinTable, propertyTable,
                    linkedAtt);
            ontoRelGraph.addLink(OntoRelGraph.EdgeType.ISA, joinTable, propertyTable, propertyFk);
          }
        }
        // ======== Ajouter contrainte de participation
        baseSchema.addConstraint(new ParticipationConstraint(joinTable, axiom.getParticipation()));
        // ======== Ajouter au dictionnaire
        this.dic.addAxiomTableCatalogEntry(axiom, joinTable);
      }
    }
    return joinTable;
  }
}
