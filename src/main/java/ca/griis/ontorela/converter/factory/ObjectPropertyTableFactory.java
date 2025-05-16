package ca.griis.ontorela.converter.factory;

import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoObjectPropertyI;
import ca.griis.monto.api.ParticipationI;
import ca.griis.monto.model.Participation;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRelGraph;
import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.MembershipConstraint;
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
 * create table for ObjectPropertyTable
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

public class ObjectPropertyTableFactory extends TableFactory<OntoObjectPropertyI> {

  public ObjectPropertyTableFactory(OntoRelCat dic, OntoRelGraph ontoRelGraph, Schema baseSchema,
      DatabaseConfiguration dbconfig, Table topTable) {
    super(dic, ontoRelGraph, baseSchema, dbconfig, topTable);
  }

  /**
   * Création d'une table à partir d'une propriété de classe.
   * Une propriété de classe est convertie en une relvar comprenant deux attributs :
   * domain_tableId_dbid:dbid_type et range_tableId_dbid:dbid_type.
   * Les deux attributs sont dérivés de Thing
   * TODO 2019-10-03 CK : Définir les attributs à partir
   * La clé est composée des deux attributs.
   *
   * @param op : une propriété de classe.
   * @return une table.
   */

  @Override
  public Table createTable(OntoObjectPropertyI op) {

    TableJoin t = this.dic.getObjectPropertyTableCatalog().get(op);
    // Vérifier si la propriété possède déjà une table de correspondance.
    // Sinon, créer une table de jointure
    if (t == null) {
      // ==== Créer table de domain si op a un domain
      Table domainTable = createDomainTable(op);
      // === Créer table de range si op a un range
      Table rangeTable = createRangeTable(op);
      // ==== Créer les attributs
      Map<Attribute, Attribute> joinAtt = new LinkedHashMap<>();
      // Créer domainkey
      Attribute domainKey = createDomainKey(domainTable, joinAtt);
      // Créer rangekey
      Attribute rangeKey = createRangeKey(rangeTable, joinAtt);

      // la table jointure op
      t = createJoinTable(op, domainTable, rangeTable, domainKey, rangeKey, joinAtt);

      if (t != null) {
        t.addKey(joinAtt.keySet(), true);
        //
        for (String l : this.dbConfig.getLanguages()) {
          String label = op.getAnnotations().getLabelValues(new Locale(l)).stream().findFirst()
              .orElse(op.getIri().getShortIri());
          t.addLabel(l, label);
          String def = op.getAnnotations().getDefinitionValues(new Locale(l)).stream().findFirst()
              .orElse(null);
          t.addDescription(l, def);
        }
        // TODO rangeTable-10-01 CK : créer description des attributs.
        // ==== Ajouter une noeud au graph
        ontoRelGraph.addNode(OntoRelGraph.NodeType.TYPENODE, t);
        // ======== Ajouter la table dans le schéma
        baseSchema.addTable(t);
        baseSchema.addTemporalPartition(t);
        // ======== Ajouter contrainte de participation
        baseSchema.addConstraint(new ParticipationConstraint(t, t.getParticipation()));
        // ======== Ajouter contrainte d'appartenance
        Set<Table> targetSet = new LinkedHashSet<>();
        for (OntoClassI c : op.getDomain()) {
          Table target = this.dic.getClassTableCatalog().get(c);
          if (target != null) {
            targetSet.add(target);
          }
        }
        if (!targetSet.isEmpty()) {
          baseSchema.addConstraint(new MembershipConstraint(t, domainKey, targetSet));
        }
        // ==== Ajouter dans le dictionnaire
        this.dic.addOntoObjectPropertyTableCatalogEntry(op, t);
        // ======== Ajouter item dans le graph
        ontoRelGraph.addNode(OntoRelGraph.NodeType.PROPERTYNODE, t);
      }
    }
    return t;
  }

  /**
   * Création d'une table de jointure à partir d'une propriété de classe.
   *
   *
   *
   * @param op : une propriété de classe.
   * @param domainTable : Table de domaine.
   * @param rangeTable : Table de range.
   * @param domainKey : Attribut de domaine.
   * @param rangeKey : Attribut de range.
   * @param joinAtt : Attribut de jointure.
   * @return une table de jointure.
   */
  private TableJoin createJoinTable(OntoObjectPropertyI op, Table domainTable, Table rangeTable,
      Attribute domainKey, Attribute rangeKey, Map<Attribute, Attribute> joinAtt) {
    TableJoin t = null;
    // participation
    Participation qt = new Participation(ParticipationI.ParticipationType.ONLY);
    if (op.isFunctional()) {
      qt = new Participation(ParticipationI.ParticipationType.MAX, 1);
    }
    if (domainKey != null && rangeKey != null) {
      t = new TableJoin(Table.TableOrigin.OBJECTPROPERTY, op.getIri(), baseSchema.getName(),
          op.getIri().getShortIri(), domainTable, rangeTable, qt, joinAtt);
    } else {
      // todo
      System.out.println("WARNIIIIIIIG : Domain ou/et range manquants pour l'object proerty "
          + op.getIri().getShortIri());
      // DIAG.ajouter(
      // " Domain ou/et range manquants pour l'object proerty " + op.getIri().getShortIri());
    }
    return t;
  }

  /**
   * Création d'une RangeTable à partir d'une propriété de classe.
   *
   *
   *
   * @param op : une propriété de classe.
   * 
   * @return une table.
   */
  private Table createRangeTable(OntoObjectPropertyI op) {
    Table rangeTable = null;

    if (!op.getRange().isEmpty()) {
      rangeTable = this.dic.getClassTableCatalog().get(op.getRange().stream().findFirst().get());
    } else {
      if (!op.getSuperObjectProperty().isEmpty()
          && op.getSuperObjectProperty().stream().findFirst().get().getRange().stream()
              .findFirst()
              .isPresent()) {
        rangeTable = this.dic.getClassTableCatalog().get(op.getSuperObjectProperty().stream()
            .findFirst().get().getRange().stream().findFirst().get());
      } else {
        if (!dbConfig.getRemoveThingTable()) {
          rangeTable = this.topTable;
        }
      }
    }
    return rangeTable;
  }

  /**
   * Création d'une DomainTable à partir d'une propriété de classe.
   *
   *
   *
   * @param op : une propriété de classe.
   * 
   * @return une table.
   */
  private Table createDomainTable(OntoObjectPropertyI op) {
    Table domainTable = null;

    if (!op.getDomain().isEmpty()) {
      domainTable =
          this.dic.getClassTableCatalog().get(op.getDomain().stream().findFirst().get());
    } else {
      if (!op.getSuperObjectProperty().isEmpty()
          && op.getSuperObjectProperty().stream().findFirst().get().getDomain().stream()
              .findFirst()
              .isPresent()) {
        domainTable = this.dic.getClassTableCatalog().get(op.getSuperObjectProperty().stream()
            .findFirst().get().getDomain().stream().findFirst().get());
      } else {
        if (!dbConfig.getRemoveThingTable()) {
          domainTable = this.topTable;
        }
      }
    }
    return domainTable;
  }

  /**
   * Création d'attribut pour une table de domaine.
   *
   *
   * @param domainTable : table de domaine.
   * @param joinAtt : attribut de jointure.
   * 
   * @return une Attribut.
   */

  private Attribute createDomainKey(Table domainTable, Map<Attribute, Attribute> joinAtt) {
    Attribute domainKey = null;
    if (domainTable != null) {
      domainKey =
          domainTable.getPrimaryKeyAttributeSet().stream().findFirst().get().clone();
      domainKey.setAttId("domain_" + domainKey.getAttId());
      domainKey.setAttIri("domain_" + domainKey.getAttIri());
      domainKey.getAttLabels().replaceAll((key, value) -> "domain_" + value);
      joinAtt.put(domainKey, domainTable.getPrimaryKeyAttributeSet().stream().findFirst().get());
    }
    return domainKey;
  }

  /**
   * Création d'attribut pour une table de range.
   *
   *
   * @param rangeTable : table de range.
   * @param joinAtt : attribut de jointure.
   * 
   * @return une Attribut.
   */

  private Attribute createRangeKey(Table rangeTable, Map<Attribute, Attribute> joinAtt) {
    Attribute rangeKey = null;

    if (rangeTable != null) {
      rangeKey =
          rangeTable.getPrimaryKeyAttributeSet().stream().findFirst().get().clone();
      rangeKey.setAttId("range_" + rangeKey.getAttId());
      rangeKey.setAttIri("range_" + rangeKey.getAttIri());
      rangeKey.getAttLabels().replaceAll((key, value) -> "range_" + value);
      joinAtt.put(rangeKey, rangeTable.getPrimaryKeyAttributeSet().stream().findFirst().get());
    }
    return rangeKey;
  }

}
