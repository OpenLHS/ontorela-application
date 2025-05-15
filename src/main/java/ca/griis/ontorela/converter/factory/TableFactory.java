package ca.griis.ontorela.converter.factory;

import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRelGraph;
import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.Schema;
import ca.griis.ontorela.mrel.Table;

/**
 * create table
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

public abstract class TableFactory<E> {
  protected final OntoRelCat dic;
  protected final OntoRelGraph ontoRelGraph;
  protected final Schema baseSchema;
  protected final DatabaseConfiguration dbConfig;
  protected final Table topTable;

  public TableFactory(OntoRelCat dic, OntoRelGraph ontoRelGraph, Schema baseSchema,
      DatabaseConfiguration dbConfig, Table topTable) {
    this.dic = dic;
    this.ontoRelGraph = ontoRelGraph;
    this.baseSchema = baseSchema;
    this.dbConfig = dbConfig;
    this.topTable = topTable;
  }

  /**
   * Création d'une table à partir d'une propriété de classe.
   *
   * @param entity : une entité ontologique
   * @return une table.
   */
  public abstract Table createTable(E entity);


  /**
   * Build a key attribute with en english an french label. This attribute is used as a default
   * key for all tables. All labels have the same values for all languages.
   *
   * @return Default key attribute with an english an french label and definition.
   */
  protected Attribute buildGlobalKeyAttribute(Table t) {
    Attribute a =
        new Attribute(t.getIdentifier().getValue() + "_" + this.dbConfig.getDefaultKeyName(),
            t.getIri() + "_" + this.dbConfig.getDefaultKeyName(),
            this.dbConfig.getDefaultKeyDomainName());
    a.addLabel("en", this.dbConfig.getDefaultKeyName() + " " + t.getLabel("en"));
    a.addLabel("fr", this.dbConfig.getDefaultKeyName() + " " + t.getLabel("fr"));
    a.addDefinition("en", "Default primary key" + " of " + t.getLabel("en"));
    a.addDefinition("fr", "Clé primaire par défaut" + " de " + t.getLabel("fr"));
    return a;
  }
}
