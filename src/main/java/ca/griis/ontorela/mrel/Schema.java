package ca.griis.ontorela.mrel;

import ca.griis.ontorela.converter.OntoView;
import ca.griis.ontorela.mrel.ForeignKey.ForeignKeyType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Un schéma relationnel.
 * Un schéma relationnel est composé d'un ensemble de type, d'un ensemble de table,
 * d'un ensemble de contraintes et d'un ensemble de vue.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * TODO 2018-09-07 CK : vérifier que les tables de la FK existent.<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2018-09-06 (0.1.0) [CK] Mise en oeuvre initiale. <br>
 *
 * <p>
 * <b>Copyright</b> 2016-2017, GRIIS (https://griis.ca/) <br>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé) <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke (Québec) J1K 2R1 <br>
 * CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 * </p>
 *
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2018-09-06
 */
public class Schema {

  // **************************************************************************
  // Attributs spécifiques
  //
  public enum SchemaType {
    BASE, VIEW
  }

  private final String name;
  private final SchemaType schemaType;
  private Set<Table> tableSet;
  private Set<Type> typeSet;
  private Set<ForeignKey> foreignKeySet;
  private final Set<Constraint> constraintSet;
  private Set<OntoView> ontoViewSet;
  private Map<Table, Set<Table>> temporalPartitionMap;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * Contructeur principale.
   *
   * @param name : schema name.
   * @param schemaType : schema type.
   */
  public Schema(String name, SchemaType schemaType) {
    super();
    this.name = name;
    this.schemaType = schemaType;
    this.tableSet = new LinkedHashSet<>();
    this.typeSet = new LinkedHashSet<>();
    this.foreignKeySet = new LinkedHashSet<>();
    this.constraintSet = new LinkedHashSet<>();
    // this.viewSet = new LinkedHashSet<>();
    this.ontoViewSet = new LinkedHashSet<>();
    this.temporalPartitionMap = new LinkedHashMap<>();
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Obtenir le nom du schéma.
   *
   * @return id
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return schemaType
   */
  public SchemaType getSchemaType() {
    return this.schemaType;
  }

  // **************************************************************************
  // Opérations publiques : Domaines
  //

  /**
   * Obtenir l'ensemble des types.
   *
   * @return L'ensemble des types.
   */
  public Set<Type> getTypeSet() {
    return this.typeSet;
  }

  /**
   * Ajouter un ensemble de type au schéma.
   *
   * @param typeSet : un ensemble de type.
   */
  public void setTypeSet(Set<Type> typeSet) {
    this.typeSet = typeSet;
  }

  /**
   * Ajouter un type au schéma.
   *
   * @param type : un type.
   */
  public void addDomain(Type type) {
    this.typeSet.add(type);
  }

  // **************************************************************************
  // Opérations publiques : Tables
  //

  /**
   * Obtenir l'ensemble de toutes les tables.
   *
   * @return L'ensemble de toutes les tables.
   */
  public Set<Table> getTableSet() {
    return this.tableSet;
  }

  /**
   * Obtenir L'ensemble des tables de jointures.
   *
   * @return l'ensemble des tables de jointures.
   */
  public Set<TableJoin> getJoinTableSet() {
    Set<TableJoin> joinTableSet = new LinkedHashSet<>();
    this.tableSet.stream().filter(t -> t instanceof TableJoin)
        .forEach(j -> joinTableSet.add((TableJoin) j));
    return joinTableSet;
  }

  /**
   * @param tableSet --> tableSet
   */
  public void setTableSet(Set<Table> tableSet) {
    this.tableSet = tableSet;
  }

  /**
   * Ajouter une table au schéma.
   *
   * @param table : une table.
   */
  public void addTable(Table table) {
    this.tableSet.add(table);
  }

  /**
   * Ajouter un ensemble de table au schéma.
   *
   * @param set : un ensemble de table.
   */
  public void addTableJoin(Set<TableJoin> set) {
    this.tableSet.addAll(set);
  }

  // **************************************************************************
  // Opérations publiques : contrainte référentielles
  //

  /**
   * Add a foreign key to the table by giving the pairs of linked attributes.
   * The constrainte name will follow this pattern fk"nbFks"_"tableId".
   *
   * @param foreignKeyType : origin of the foreign key : isa axiom or property axiom.
   * @param origin : the origin table.
   * @param linkedAtt : a pair of attribute from the origin table and a attibute from the
   *        destination table (attOrign, attDestination).
   * @param destination : the destination table.
   * @return The constructed foreign key.
   */
  public ForeignKey addFk(ForeignKeyType foreignKeyType, Table origin, Table destination,
      Map<Attribute, Attribute> linkedAtt) {
    String fkId =
        "fk" + this.getOriginForeignKeySet(origin).size() + "_" + origin.getIdentifier().getValue();
    ForeignKey fk = new ForeignKey(foreignKeyType, fkId, origin, destination, linkedAtt);
    this.foreignKeySet.add(fk);
    return fk;
  }

  /**
   * Add a foreign key to the table by giving two sets of attributes.
   * The constrainte name will follow this pattern fk"nbFks"_"tableId".
   * TODO 2019-10-03 CK : à vérifier l'ordre des attributs !
   *
   * @param foreignKeyType : origin of the foreign key : isa axiom or property axiom.
   * @param origin : the origin table.
   * @param attOrigin : the set of attribute from the origin table.
   * @param attDestination : the of attribute from the destination table.
   * @param destination : the destination table.
   * @return The constructed foreign key.
   */
  public ForeignKey addFk(ForeignKeyType foreignKeyType, Table origin, Table destination,
      Set<Attribute> attOrigin, Set<Attribute> attDestination) {
    String fkId =
        "fk" + this.getOriginForeignKeySet(origin).size() + "_" + origin.getIdentifier().getValue();
    ArrayList<Attribute> attOriginList = new ArrayList<>(attOrigin);
    ArrayList<Attribute> attDestinationList = new ArrayList<>(attDestination);

    Map<Attribute, Attribute> linkedAtt = new HashMap<>();
    if (attDestinationList.size() != attOriginList.size()) {
      for (Attribute o : attOriginList) {
        for (Attribute d : attDestinationList) {
          linkedAtt.put(d, o);
        }
      }
    } else {
      linkedAtt = IntStream.range(0, attOrigin.size()).boxed()
          .collect(Collectors.toMap(attOriginList::get, attDestinationList::get));
    }
    ForeignKey fk = new ForeignKey(foreignKeyType, fkId, origin, destination, linkedAtt);
    this.foreignKeySet.add(fk);
    return fk;
  }

  /**
   * Add foreign key from a candidate key of this table to the default key (primary key)
   * of the the destination table.
   * The constrainte name will follow this pattern fk"nbFks"_"tableId".
   * TODO 2018-09-17 CK : traiter les clés primaires ayant plusieurs attributs.
   *
   * @param foreignKeyType : origin of the foreign key : isa axiom or property axiom.
   * @param origin : the origin table.
   * @param attOrigin : the key attribut of the origine table that must be used.
   * @param destination : the destination table.
   * @return The constructed foreign key.
   *         The default key of the destination talbe will be used to link both tables.
   */
  public ForeignKey addFkForDefaultKey(ForeignKeyType foreignKeyType, Table origin,
      Table destination, Attribute attOrigin) {
    ForeignKey fk = null;
    String fkId =
        "fk" + this.getOriginForeignKeySet(origin).size() + "_" + origin.getIdentifier().getValue();
    Map<Attribute, Attribute> linkedAtt = new LinkedHashMap<>();
    if (destination != null) {
      linkedAtt.put(attOrigin, destination.getPrimaryKeyAttributeSet().stream().findFirst().get());
      fk = new ForeignKey(foreignKeyType, fkId, origin, destination, linkedAtt);
      this.foreignKeySet.add(fk);
    }
    return fk;
  }

  /**
   * @param fks --> fks
   */
  public void setForeignKeySet(Set<ForeignKey> fks) {
    this.foreignKeySet = fks;
  }

  /**
   * @return fks
   */
  public Set<ForeignKey> getForeignKeySet() {
    return this.foreignKeySet;
  }

  /**
   * Obtenir les contraintes référentielles d'une table ayant comme rôle une table d'origine.
   *
   * @param table : une table d'origine.
   * @return Les contraintes référentielles d'une table d'origine.
   */
  public Set<ForeignKey> getOriginForeignKeySet(Table table) {
    return this.foreignKeySet.stream().filter(f -> f.getOrigin().equals(table))
        .collect(Collectors.toSet());
  }

  /**
   * Obtenir les contraintes référentielles d'une table ayant comme rôle une table de destination.
   *
   * @param table : une table de destination.
   * @return Les contraintes référentielles d'une table de destination.
   */
  public Set<ForeignKey> getDestinationForeignKeySet(Table table) {
    return this.foreignKeySet.stream().filter(f -> f.getDestination().equals(table))
        .collect(Collectors.toSet());
  }

  /**
   * Ajouter une contrainte au schéma.
   *
   * @param constraint : une contrainte.
   */
  public void addConstraint(Constraint constraint) {
    this.constraintSet.add(constraint);
    // TODO 2018-09-10 : sinon lancer une exception, laquelle ??
  }

  /**
   * Obtenir L'ensemble des contraintes.
   *
   * @return L'ensemble des contraintes.
   */
  public Set<Constraint> constraintSet() {
    return this.constraintSet;
  }

  /**
   * Obtenir l'ensemble des contraintes.
   *
   * @return L'ensemble des contraintes.
   */
  public Set<Constraint> getConstraintSet() {
    return this.constraintSet;
  }

  /**
   * Obtenir l'ensemble des contraintes selon la classe spécifiée.
   *
   * @param cls : une classe de contrainte.
   * @return L'ensemble des contraintes appartenant à la classe spécifiée.
   */
  @SuppressWarnings("unchecked")
  public <T extends Constraint> Set<T> getConstraintSet(Class<T> cls) {
    Set<T> set = new LinkedHashSet<>();
    this.constraintSet.stream()
        .filter(c -> c.getClass().getSimpleName().equals(cls.getSimpleName()))
        .forEach(c -> set.add((T) c));
    return set;
  }
  // **************************************************************************
  // Opérations publiques : vue
  //
  // /**
  // * Ajouter une vue au schéma.
  // * @param view : une vue.
  // */
  // public void addView(View view) {
  // this.viewSet.add(view);
  // }
  //
  // /**
  // * @param viewSet --> viewSet
  // */
  // public void setViewSet(Set<View> viewSet) {
  // this.viewSet = viewSet;
  // }
  //
  // /**
  // * @return viewSet
  // */
  // public Set<View> getViewSet() {
  // return this.viewSet;
  // }
  //
  // /**
  // * Obtenir l'ensemble de vue iri.
  // * @return L'ensemble de vue iri.
  // */
  // public Set<View> getIriViewSet() {
  // return this.viewSet.stream().filter(v -> v.getViewType().equals(ViewType.IRI))
  // .collect(Collectors.toSet());
  // }
  //
  // /**
  // * Obtenir l'ensemble de vue pour une langue.
  // * @param ln : une langue.
  // * @return L'ensemble de vue pour une langue.
  // */
  // public Set<View> getLabelViewSet(Locale ln) {
  // return this.viewSet.stream()
  // .filter(v -> v.getViewType().equals(ViewType.LABEL) && v.getLanguage().equals(ln))
  // .collect(Collectors.toSet());
  // }

  /**
   * Ajouter une vue ontologique au schéma.
   *
   * @param view : une vue
   */
  public void addOntoView(OntoView view) {
    this.ontoViewSet.add(view);
  }

  /**
   * @param viewSet : une ensemble de vues ontologiques
   */
  public void setOntoViewSet(Set<OntoView> viewSet) {
    this.ontoViewSet = viewSet;
  }

  /**
   * @return l'ensemble de vue ontologique du schéma.
   */
  public Set<OntoView> getOntoViewSet() {
    return this.ontoViewSet;
  }

  // **************************************************************************
  // Diver : toString, etc...
  //

  /**
   * @return temporalPartitionMap
   */
  public Map<Table, Set<Table>> getTemporalPartitionMap() {
    return temporalPartitionMap;
  }

  /**
   * Ajouter une partition temporelle pour une table a historicisée.
   * TODO 2019-03-23 CK : ajouter le type de partitions à créer.
   *
   * @param table : la table.
   * @param temporalTables : les partitions temporelles associées.
   */
  public void addTemporalPartition(Table table, Set<Table> temporalTables) {
    this.temporalPartitionMap.put(table, temporalTables);
  }

  /**
   * Ajouter une table a historicisée dans la liste des partitions avec une partition vide.
   * À remplir par le processus d'historicisation.
   *
   * @param table : la table.
   */
  public void addTemporalPartition(Table table) {
    this.temporalPartitionMap.put(table, new LinkedHashSet<>());
  }

  /**
   * @param temporalPartitionMap --> temporalPartitionMap
   */
  public void setTemporalPartitionMap(Map<Table, Set<Table>> temporalPartitionMap) {
    this.temporalPartitionMap = temporalPartitionMap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Schema [id=" + this.name + ", schemaType=" + this.schemaType + "]";
  }
}
