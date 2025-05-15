package ca.griis.ontorela.converter.factory;

import ca.griis.monto.api.OntoClassI;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRelGraph;
import ca.griis.ontorela.mrel.Schema;
import ca.griis.ontorela.mrel.Table;
import java.util.Locale;

/**
 * create table for classeTable
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

public class ClassTableFactory extends TableFactory<OntoClassI> {
  public ClassTableFactory(OntoRelCat dic, OntoRelGraph ontoRelGraph, Schema baseSchema,
      DatabaseConfiguration dbConfig, Table topTable) {
    super(dic, ontoRelGraph, baseSchema, dbConfig, topTable);
  }

  /**
   * Création d'une table à partir d'une classe ontologique dans un schéma.
   *
   * @param ontoClass : une classe ontologique.
   * @return une table.
   */

  @Override
  public Table createTable(OntoClassI ontoClass) {

    Table t = dic.getClassTableCatalog().get(ontoClass);

    if (t == null) {
      t = new Table(Table.TableOrigin.ONTOCLASS, ontoClass.getIri(), baseSchema.getName(),
          ontoClass.getIri().getShortIri());

      // Ajouter les étiquettes et les définitions
      for (String l : this.dbConfig.getLanguages()) {
        String label = ontoClass.getAnnotations().getLabelValues(new Locale(l)).stream().findFirst()
            .orElse(ontoClass.getIri().getShortIri());
        t.addLabel(l, label);
        String def = ontoClass.getAnnotations().getDefinitionValues(new Locale(l)).stream()
            .findFirst().orElse(null);
        t.addDescription(l, def);
      }
      // Ajouter les attributs
      t.addKey(buildGlobalKeyAttribute(t), true);
      // Ajouter dans le dictionnaire
      this.dic.addClassTableCatalogEntry(ontoClass, t);
      // Ajouter une noeud au graph
      ontoRelGraph.addNode(OntoRelGraph.NodeType.CLASSNODE, t);
      // Ajouter au schéma
      baseSchema.addTable(t);
      baseSchema.addTemporalPartition(t);
    }
    return t;
  }
}
