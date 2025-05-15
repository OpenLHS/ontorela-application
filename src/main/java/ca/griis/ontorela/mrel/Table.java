package ca.griis.ontorela.mrel;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoIriI;
import ca.griis.ontorela.mrel.catalog.MrelEntityType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Une table est définie par d'un nom du schéma, un identifiant de base de données, un identifiant
 * ontologique (un iri), un ensemble d'attribute, un ensemble de clé candidate, un ensemble
 * de clés référentielle, un ensemble d'étiquettes et un ensemble de définitions.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : oui.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * TODO 2018-07-12 CK : utiliser la classe annotationDB pour les étiquettes et les
 * définitions <br>
 * TODO 2018-07-15 CK : adapter les fonctions de création des fk avec ceux de la classe
 * RelationalSchema. <br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-07-12 (0.1.1) [CK] : Revue et documentation. <br>
 * 2017-11-19 (0.1.0) [CK] : Création. <br>
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
 * @version 0.1.0
 * @since 2017-11-19
 */
public class Table extends MrelEntity {
  // **************************************************************************
  // Attributs spécifiques
  //
  public enum TableOrigin {
    ONTOCLASS, ONTOTYPE, OBJECTPROPERTY, CLASSAXIOM, DATAAXIOM
  }

  private final TableOrigin tableOrigin;
  private TemporalPartitionType tablePartition;
  private final String schemaId;
  private final String tableIri;
  private final Set<Attribute> attSet;
  private final Set<CandidateKey> keyAttSet;
  /**
   * Map between Language -> Label.
   */
  private final Map<String, String> tableLabels;
  /**
   * Map between Language -> Definition.
   */
  private final Map<String, String> tableDefs;
  //
  // **************************************************************************
  // Constructeurs
  //

  /**
   * Main constructor of a table.
   * TODO 2019-01-19 CK : Fusionner MRelEntityType et TableOrigin ?
   *
   * @param type : relation type : base or virtual.
   * @param tableOrigin : table origin.
   * @param ontoIri : l'iri du composant ontologique associé.
   * @param tablePartition : table partition type.
   * @param schemaId : the schema id in which the table is contained.
   * @param tableIri : owlIri refering to this table.
   */
  public Table(MrelEntityType type, TableOrigin tableOrigin, OntoIriI ontoIri,
      TemporalPartitionType tablePartition, String schemaId, String tableIri) {
    super(type, ontoIri);
    //
    this.tableOrigin = tableOrigin;
    this.tablePartition = tablePartition;
    this.schemaId = schemaId;
    this.tableIri = tableIri;
    this.tableDefs = new LinkedHashMap<>();
    this.tableLabels = new LinkedHashMap<>();
    this.attSet = new LinkedHashSet<>();
    this.keyAttSet = new LinkedHashSet<>();
  }

  /**
   * Constructeur d'une table à partir d'un composant ontologique nommée (classe, propriété et
   * type).
   *
   * @param tableOrigin : table origin.
   * @param ontoIri : l'iri du composant ontologique associé.
   * @param schemaId : the schema id in which the table is contained.
   * @param tableIri : owlIri refering to this table.
   */
  public Table(TableOrigin tableOrigin, OntoIriI ontoIri, String schemaId, String tableIri) {
    super(MrelEntityType.TABLE, ontoIri);
    //
    this.tableOrigin = tableOrigin;
    this.tablePartition = TemporalPartitionType.NT;
    this.schemaId = schemaId;
    this.tableIri = tableIri;
    this.tableDefs = new LinkedHashMap<>();
    this.tableLabels = new LinkedHashMap<>();
    this.attSet = new LinkedHashSet<>();
    this.keyAttSet = new LinkedHashSet<>();
  }

  /**
   * Constructeur d'une table à partir d'un axiome d'association.
   *
   * @param tableOrigin : table origin.
   * @param ontoAxiom : l'axiome d'association associé.
   * @param schemaId : the schema id in which the table is contained.
   * @param tableIri : the table owl short iri.
   */
  public Table(TableOrigin tableOrigin, OntoAxiomAssociationI ontoAxiom, String schemaId,
      String tableIri) {
    super(MrelEntityType.TABLE, ontoAxiom);
    //
    this.tableOrigin = tableOrigin;
    this.tablePartition = TemporalPartitionType.NT;
    this.schemaId = schemaId;
    this.tableIri = tableIri;
    this.tableDefs = new LinkedHashMap<>();
    this.tableLabels = new LinkedHashMap<>();
    this.attSet = new LinkedHashSet<>();
    this.keyAttSet = new LinkedHashSet<>();
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Add a label definition to the table.
   *
   * @param language : the language of the label
   * @param label : the label string
   */
  public void addLabel(String language, String label) {
    if (label != null) {
      this.tableLabels.put(language, label);
    }
  }

  /**
   * Add a definition to the table.
   *
   * @param language : the language of the definition
   * @param def : the definition string
   */
  public void addDescription(String language, String def) {
    if (def != null) {
      this.tableDefs.put(language, def);
    }
  }

  /**
   * TODO 2017-11-21 CK : si l'étiquette est nulle ne pas l'ajouter.
   * Add labels to the table.
   *
   * @param lables : pairs of the language of the label and the label string.
   */
  public void addLabels(Map<String, String> lables) {
    this.tableLabels.putAll(lables);
  }

  /**
   * TODO 2017-11-21 CK : si la définition est nulle ne pas l'ajouter.
   * Add a definitions to the table.
   *
   * @param defs : pairs of the language of the definition and the definition string.
   */
  public void addDescriptions(Map<String, String> defs) {
    this.tableDefs.putAll(defs);
  }

  /**
   * Add an attribute to the table.
   *
   * @param a an attribute
   */
  public void addAttribute(Attribute a) {
    this.attSet.add(a);
  }

  /**
   * Add a set of no key attributes to the table.
   *
   * @param noKeyAttSet : set of no key attributes.
   */
  public void addNoKeyAttributeSet(Set<Attribute> noKeyAttSet) {
    this.attSet.addAll(noKeyAttSet);
  }

  /**
   * Add a key to the table. It will generate an new candidate key and will add the
   * attribute in the global attribute set.
   *
   * @param attKey : a key attribute.
   * @param primary : is the key primary ?
   */
  public void addKey(Attribute attKey, boolean primary) {
    CandidateKey cc = new CandidateKey("ck" + this.keyAttSet.size(), attKey, primary);
    this.attSet.add(attKey);
    this.keyAttSet.add(cc);
  }

  /**
   * Add a candidate key to the table. It will add the attributes in the global attribute set.
   *
   * @param ck : a candidate key.
   */
  public void addKey(CandidateKey ck) {
    this.attSet.addAll(ck.getKeyAttSet());
    this.keyAttSet.add(ck);
  }

  /**
   * Add a composite key to the table. It will generate an new candidate key and will add
   * the set of attribute in the global attribute set.
   *
   * @param attKeySet : set of a key attributes,
   * @param primary : is the key primary ?
   */
  public void addKey(Set<Attribute> attKeySet, boolean primary) {
    CandidateKey cc = new CandidateKey("ck" + this.keyAttSet.size(), attKeySet, primary);
    this.attSet.addAll(attKeySet);
    this.keyAttSet.add(cc);
  }

  /**
   * Get the table origin.
   *
   * @return The table origin.
   */
  public TableOrigin getTableOrigin() {
    return tableOrigin;
  }

  /**
   * Get table partition type.
   *
   * @return tablePartition
   */
  public TemporalPartitionType getTablePartition() {
    return tablePartition;
  }

  /**
   * Specify a the table partition type.
   *
   * @param tablePartition : a table partition type.
   */
  public void setTablePartition(TemporalPartitionType tablePartition) {
    this.tablePartition = tablePartition;
  }

  /**
   * Get the schema name in which the table is defined.
   *
   * @return The schema name in which the table is defined.
   */
  public String getSchemaId() {
    return this.schemaId;
  }

  /**
   * Get ontology entity IRI refering to this table.
   *
   * @return The IRI refering to this table.
   */
  public String getIri() {
    return this.tableIri;
  }

  /**
   * Get the table iri prefixed by the table.
   *
   * @return The table IRI prefixed by the table id.
   */
  public String getPrefixedIri() {
    return super.getIdentifier().getValue() + "_" + getIri();
  }

  /**
   * Get all table labels.
   *
   * @return The pairs of (language, labels).
   */
  public Map<String, String> getLabels() {
    return this.tableLabels;
  }

  /**
   * Get all table definitions.
   *
   * @return the pairs of (language, labels).
   */
  public Map<String, String> getDescription() {
    return this.tableDefs;
  }

  /**
   * Get the definition for a specific language.
   *
   * @param language : the language of the definition
   * @return The definition for the specified language, if not exists or is empty return null.
   */
  public String getDescription(String language) {
    String l = this.tableDefs.get(language);
    return l == null || l.isEmpty() ? null : l;
  }

  /**
   * Get the label for a specific language.
   *
   * @param language : the language of the label
   * @return The label for the specified language. If not exists or if is empty return
   *         the table iri suffixed by the language.
   */
  public String getLabel(String language) {
    String l = this.tableLabels.get(language);
    String i = getIri() + "_" + language;
    return l == null || l.isEmpty() ? i : l;
  }

  /**
   * Get labels for a specific language prefixed with the table id.
   *
   * @param language : the language of the label
   * @return The prefixed label for the specified language.
   */
  public String getPefixedLabel(String language) {
    return super.getIdentifier().getValue() + "_" + getLabel(language);
  }

  /**
   * Get table prefixed labels.
   *
   * @return Labels prefixed with the table prefix ID for all languages.
   */
  public Map<String, String> getPrefixedLabels() {
    Map<String, String> p = new LinkedHashMap<>();
    for (Entry<String, String> e : this.tableLabels.entrySet()) {
      p.put(e.getKey(), getPefixedLabel(e.getKey()));
    }
    return p;
  }

  /**
   * Get the key and no key attributes.
   *
   * @return All attributes: key and no key attributes.
   */
  public Set<Attribute> getAttributeSet() {
    return this.attSet;
  }

  /**
   * Get map of all attributes.
   *
   * @return Pairs(attId, Type) of all attributes.
   */
  public Map<String, String> getAttributeTypePaires() {
    Map<String, String> m = new LinkedHashMap<>();
    this.attSet.forEach(a -> m.put(a.getAttId(), a.getAttType()));
    return m;
  }

  public Map<String, String> getAttributeTypePaires(boolean useIriAsTableId) {
    Map<String, String> m = new LinkedHashMap<>();
    this.attSet.forEach(a -> m.put(useIriAsTableId ? a.getAttIri() : a.getAttId(), a.getAttType()));
    return m;
  }

  /**
   * Get the no key attributes.
   *
   * @return Set of no key attributes.
   */
  public Set<Attribute> getNoKeyAttributeSet() {
    return this.attSet.stream().filter(n -> !this.getPrimaryKeyAttributeSet().contains(n))
        .collect(Collectors.toSet());
  }

  /**
   * Get the set of candidate key.
   *
   * @return The set of key attributes only.
   */
  public Set<CandidateKey> getKeyAttributeSet() {
    return this.keyAttSet;
  }

  /**
   * Get the set of attributes coposing the primary key.
   *
   * @return The set of attributes coposing the primary key.
   */
  public Set<Attribute> getPrimaryKeyAttributeSet() {
    return getKeyAttributeSet().stream().filter(cc0 -> cc0.isPrimary()).findFirst()
        .map(CandidateKey::getKeyAttSet).get();
  }

  /**
   * TODO 2017-11-21 CK : vérifier l'utilisation est supprimer si inutile.
   * Get table id with or without the iri .
   *
   * @param withId : obtain a table iri prefixed with de table id or not
   * @return Table iri prefixed with the table id or not.
   */
  public String getTableIriPrefixed(boolean withId) {
    return withId ? super.getIdentifier().getValue() + "::" + getIri() : getIri();
  }

  /**
   * TODO 2017-11-21 CK : à refaire avec la syntaxe Discipulus ?
   * Get the table declaration.
   *
   * @return the string of the table definition.
   */
  public String getTableDeclarationString() {
    StringBuffer sb = new StringBuffer();
    sb.append("TABLE " + getTableIriPrefixed(true) + "::" + this.tableLabels + "\n  {\n");
    for (Attribute a : getAttributeSet()) {
      sb.append("    " + a.toString() + "::" + a.getAttLabels() + "\n");
    }
    sb.append("  } \n");
    // Keys
    for (CandidateKey cc : this.keyAttSet) {
      ArrayList<String> keyIds = new ArrayList<>();
      cc.getKeyAttSet().stream().forEach(a -> keyIds.add(a.getAttId()));
      sb.append("KEY {" + String.join(", ", keyIds) + "} \n");
    }
    return sb.toString();
  }
  // **************************************************************************
  // Opérations equals/hashCode/toStrung
  //

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.attSet == null) ? 0 : this.attSet.hashCode());
    result = prime * result + ((this.keyAttSet == null) ? 0 : this.keyAttSet.hashCode());
    result = prime * result + ((this.tableIri == null) ? 0 : this.tableIri.hashCode());
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Table other = (Table) obj;
    return new EqualsBuilder()
        .append(attSet, other.attSet)
        .append(keyAttSet, other.keyAttSet)
        .append(tableIri, other.tableIri)
        .isEquals();
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getTableIriPrefixed(true);
  }
}
