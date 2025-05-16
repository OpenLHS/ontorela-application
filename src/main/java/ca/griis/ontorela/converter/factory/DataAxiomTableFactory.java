package ca.griis.ontorela.converter.factory;

import ca.griis.monto.api.OntoAxiomDataAssociationI;
import ca.griis.monto.api.OntoDatatypeI;
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
 * create table for DataAxiomTable
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

public class DataAxiomTableFactory extends TableFactory<OntoAxiomDataAssociationI> {
  public DataAxiomTableFactory(OntoRelCat dic, OntoRelGraph ontoRelGraph, Schema baseSchema,
      DatabaseConfiguration dbConfig, Table topTable) {
    super(dic, ontoRelGraph, baseSchema, dbConfig, topTable);
  }

  /**
   * Création d'une table de jointure pour un axiome d'association de données.
   * Si la table existe déjà elle sera retourner.
   * TODO 2018-09-17 CK : traiter les clés primaires ayant plusieurs attributs.
   *
   * @param axiom : un axiome d'association de données.
   * @return Une table de jointure pour un axiome.
   */
  @Override
  public Table createTable(OntoAxiomDataAssociationI axiom) {
    boolean normalize = dbConfig.getNormalizeDatatype();

    Table dataTable = null;
    String schemaId = baseSchema.getName();
    // ============ Si les types sont normalisés
    if (normalize) {
      dataTable = this.dic.getDataAxiomTableCatalog().get(axiom);
      // Vérifier si l'axiome possède déjà une table de correspondance.
      if (dataTable == null) {
        // ==== Récupration des composants de la table de jointure.
        Table determinant = this.dic.getClassTableCatalog().get(axiom.getOntoDeterminant());
        Table dependent = this.dic.getDataTableCatalog().get(axiom.getOntoDependent());
        assert determinant != null : "Table not found : " + axiom.getOntoDeterminant();
        if (dependent == null) {
          dependent = createTable(axiom.getOntoDependent());
        }
        // ==== Créer les table de jointure
        String tableIri = determinant.getIri() + "_" + axiom.getProperty().getIri().getShortIri()
            + "_" + dependent.getIri();
        Map<Attribute, Attribute> joinAtt = new LinkedHashMap<>();
        Attribute determinantKey =
            determinant.getPrimaryKeyAttributeSet().stream().findFirst().get();
        joinAtt.put(determinantKey, determinantKey);
        Attribute dependentKey = dependent.getPrimaryKeyAttributeSet().stream().findFirst().get();
        joinAtt.put(dependentKey, dependentKey);
        dataTable =
            new TableJoin(Table.TableOrigin.DATAAXIOM, axiom, schemaId, tableIri, determinant,
                dependent, (Participation) axiom.getParticipation(), joinAtt);
        // Ajouter les attributs
        // // Ajouter la clé du déterminant et l'attribut par défault de valeur.
        Set<Attribute> attKeySet = new LinkedHashSet<>();
        attKeySet.add(determinantKey);
        if (axiom.getProperty().isFunctional()) {
          dataTable.addKey(attKeySet, true);
        } else {
          attKeySet.add(dependentKey);
          dataTable.addKey(attKeySet, true);
        }
        // // Ajouter la clé du dépendant
        dataTable.addAttribute(dependentKey);
        // Ajouter les étiquettes et les définitions
        for (String l : this.dbConfig.getLanguages()) {
          String label = String.join(" ",
              axiom.getOntoDeterminant().getAnnotations().getLabelValues(new Locale(l)).stream()
                  .findFirst().orElse(axiom.getOntoDeterminant().getIri().getShortIri()),
              axiom.getProperty().getAnnotations().getLabelValues(new Locale(l)).stream()
                  .findFirst().orElse(axiom.getProperty().getIri().getShortIri()),
              axiom.getOntoDependent().getAnnotations().getLabelValues(new Locale(l)).stream()
                  .findFirst().orElse(axiom.getOntoDependent().getIri().getShortIri()));
          dataTable.addLabel(l, label);
          String def = String.join(" ",
              axiom.getOntoDeterminant().getAnnotations().getDefinitionValues(new Locale(l))
                  .stream().findFirst().orElse(null),
              axiom.getProperty().getAnnotations().getDefinitionValues(new Locale(l)).stream()
                  .findFirst().orElse(null),
              axiom.getOntoDependent().getAnnotations().getDefinitionValues(new Locale(l)).stream()
                  .findFirst().orElse(null));
          dataTable.addDescription(l, def);
        }
        // Ajouter la table au schéma
        baseSchema.addTable(dataTable);
        baseSchema.addTemporalPartition(dataTable);
        // Ajouter item dans le graph
        ontoRelGraph.addNode(OntoRelGraph.NodeType.DATAXIOMNODE, dataTable);
        // Ajouter les contraintes référentielles
        ForeignKey determinantFk =
            baseSchema.addFkForDefaultKey(ForeignKey.ForeignKeyType.DATAPROPERTY,
                dataTable, determinant, determinantKey);
        // Ajouter item dans le graph
        ontoRelGraph.addLink(OntoRelGraph.EdgeType.DPEDGE, determinant, dataTable, determinantFk);
        // Ajouter les contraintes référentielles
        ForeignKey dependantFk =
            baseSchema.addFkForDefaultKey(ForeignKey.ForeignKeyType.DATAPROPERTY,
                dataTable, dependent, dependentKey);
        // Ajouter contrainte de participation
        baseSchema.addConstraint(new ParticipationConstraint(dataTable, axiom.getParticipation()));
        // Ajouter au dictionnaire
        this.dic.addAxiomTableCatalogEntry(axiom, dataTable);
        // Ajouter item dans le graph
        ontoRelGraph.addLink(OntoRelGraph.EdgeType.DPEDGE, dataTable, dependent, dependantFk);
      }
    } else {
      // ============ Si les types ne sont normalisés
      String tableIri = axiom.getOntoDeterminant().getIri().getShortIri() + "_"
          + axiom.getProperty().getIri().getShortIri() + "_" + axiom.getOntoDependent().getName();
      // TODO 2019-11-20 CK : création OntoIRI ou null ?
      dataTable = new Table(Table.TableOrigin.DATAAXIOM, axiom, schemaId, tableIri);
      // Ajouter les étiquettes et les définitions
      for (String ln : this.dbConfig.getLanguages()) {
        String label = String.join(" ",
            axiom.getOntoDeterminant().getAnnotations().getLabelValues(new Locale(ln)).stream()
                .findFirst().orElse(axiom.getOntoDeterminant().getIri().getShortIri()),
            axiom.getProperty().getAnnotations().getLabelValues(new Locale(ln)).stream().findFirst()
                .orElse(axiom.getProperty().getIri().getShortIri()),
            axiom.getOntoDependent().getAnnotations().getLabelValues(new Locale(ln)).stream()
                .findFirst()
                .orElse(axiom.getOntoDependent().getIri().getShortIri()));
        dataTable.addLabel(ln, label);
        String def = axiom.getAnnotations().getDefinitionValues(new Locale(ln)).stream().findFirst()
            .orElse(null);
        dataTable.addDescription(ln, def);
      }
      Table destination = dic.getClassTableCatalog().get(axiom.getOntoDeterminant());
      // Ajouter les attributs
      Set<Attribute> attKeySet = new LinkedHashSet<>();
      // TODO 2019-05-13 CK : XID attKeySet.add(this.buildDefaultValueKeyAttribute()); ?
      attKeySet.addAll(destination.getPrimaryKeyAttributeSet());
      Attribute dpAtt = new Attribute(
          dataTable.getIdentifier().getValue() + "_" + axiom.getProperty().getIri().getShortIri(),
          dataTable.getIdentifier().getValue() + "_" + axiom.getProperty().getIri().getShortIri(),
          axiom.getOntoDependent().getIri().getShortIri() + "_domain");
      for (String ln : this.dbConfig.getLanguages()) {
        dpAtt.addLabel(ln, axiom.getProperty().getAnnotations().getLabelValues(new Locale(ln))
            .stream().findFirst().orElse(axiom.getProperty().getIri().getShortIri()));
      }
      // Définir la clé
      if (axiom.getParticipation().getMax() > 1) {
        attKeySet.add(dpAtt);
      } else {
        dataTable.addAttribute(dpAtt);
      }
      dataTable.addKey(attKeySet, true);
      // Ajouter dans le dictionnaire
      this.dic.addAxiomTableCatalogEntry(axiom, dataTable);
      // Ajouter au schéma
      baseSchema.addTable(dataTable);
      // Ajouter contrainte référentielle
      Attribute destinationKey = destination.getPrimaryKeyAttributeSet().stream().findFirst().get();
      ForeignKey fk =
          baseSchema.addFkForDefaultKey(ForeignKey.ForeignKeyType.DATAPROPERTY, dataTable,
              destination, destinationKey);
      // Ajouter contrainte de participation
      baseSchema.addConstraint(new ParticipationConstraint(dataTable, axiom.getParticipation()));
      // Ajouter item dans le graph
      ontoRelGraph.addNode(OntoRelGraph.NodeType.DATAXIOMNODE, dataTable);
      ontoRelGraph.addLink(OntoRelGraph.EdgeType.DPEDGE, destination, dataTable, fk);
    }
    return dataTable;
  }

  /**
   * Création d'une table à partir d'un type de données ontologique dans un schéma.
   *
   * @param datatype : un type ontologique.
   * @return une table.
   */
  public Table createTable(OntoDatatypeI datatype) {
    Table t = this.dic.getDataTableCatalog().get(datatype);
    // Vérifier si la classe possède déjà une table de correspondance.
    if (t == null) {
      t = new Table(Table.TableOrigin.ONTOTYPE, datatype.getIri(), baseSchema.getName(),
          datatype.getIri().getShortIri());
      // ==== Ajouter les étiquettes et les définitions
      for (String l : this.dbConfig.getLanguages()) {
        String label = datatype.getAnnotations().getLabelValues(new Locale(l)).stream().findFirst()
            .orElse(datatype.getIri().getShortIri());
        t.addLabel(l, label);
        String def = datatype.getAnnotations().getDefinitionValues(new Locale(l)).stream()
            .findFirst().orElse(null);
        t.addDescription(l, def);
      }
      // ==== Ajouter les attributs
      t.addKey(buildGlobalKeyAttribute(t), true);
      Attribute value = new Attribute(datatype.getIri().getShortIri(),
          datatype.getIri().getShortIri(), datatype.getIri().getShortIri() + "_domain");
      for (String ln : this.dbConfig.getLanguages()) {
        value.addLabel(ln, datatype.getAnnotations().getLabelValues(new Locale(ln)).stream()
            .findFirst().orElse(datatype.getIri().getShortIri()));
      }
      t.addKey(value, false);
      // ==== Ajouter dans le dictionnaire
      this.dic.addDataTableCatalogEntry(datatype, t);
      // ==== Ajouter une noeud au graph
      ontoRelGraph.addNode(OntoRelGraph.NodeType.TYPENODE, t);
      // Ajouter au schéma
      baseSchema.addTable(t);
    }
    return t;
  }
}
