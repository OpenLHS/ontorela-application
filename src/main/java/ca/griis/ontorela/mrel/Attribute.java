package ca.griis.ontorela.mrel;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Un attribut est défini par un identifiant de base de données, un identifiant ontologique
 * (un iri), un type OWL, un ensemble d'étiquettes et un ensemble de définitions.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * TODO 2020-01-23 CK : utiliser la classe Type <br>
 * TODO 2019-10-01 CK : catégoriser les attributs : domaine ou range <br>
 * TODO 2018-07-12 CK : stocker le type OWL de l'attribut pour simplifier la génération
 * de script pour différent SGBD. <br>
 * TODO 2018-07-12 CK : utiliser la classe annotationDB pour les étiquettes et les
 * définitions <br>
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
public class Attribute implements Cloneable {
  // **************************************************************************
  // Attributs spécifiques
  //
  private String attIri;
  private final String attType;
  private final TemporalPartitionType temporalPartition;
  private String attId;
  /**
   * Map between Language -> Label.
   */
  private Map<String, String> attLabels;
  /**
   * Map between Language -> Definition.
   */
  private final Map<String, String> attDefs;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * Main constructor of an attribute.
   *
   * @param temporalPartition : attribut temporal partition type.
   * @param attId : attribute identifier (name).
   *        TODO 2019-01-19 CK : Retirer : @param attIri : attribute owl short iri.
   * @param attIri : attribute iri.
   * @param attType : attribute owl type.
   */
  public Attribute(TemporalPartitionType temporalPartition, String attId, String attIri,
      String attType) {
    this.temporalPartition = temporalPartition;
    this.attId = attId;
    this.attIri = attIri;
    this.attType = attType;
    this.attLabels = new LinkedHashMap<>();
    this.attDefs = new LinkedHashMap<>();
  }

  /**
   * Atemporal Attribute constructor .
   *
   * @param attId : attribute identifier (name).
   * @param attIri : attribute iri.
   * @param attType : attribute owl type.
   */
  public Attribute(String attId, String attIri, String attType) {
    this(TemporalPartitionType.NT, attId, attIri, attType);
  }

  // **************************************************************************
  // Opérations propres
  //

  /**
   * Add a label to the attribute.
   *
   * @param language : the language of the label.
   * @param label : the label string.
   */
  public void addLabel(String language, String label) {
    if (label != null) {
      this.attLabels.put(language, label);
    }
  }

  /**
   * Add a definition to the attribut.
   *
   * @param language : the language of the definition.
   * @param def : the definition string.
   */
  public void addDefinition(String language, String def) {
    if (def != null) {
      this.attDefs.put(language, def);
    }
  }

  /**
   * TODO 2017-11-21 CK : si l'étiquette est nulle ne pas l'ajouter.
   * Add labels to the attribute.
   *
   * @param lables : pairs of the language of the label and the label string.
   */
  protected void addLabels(Map<String, String> lables) {
    this.attLabels.putAll(lables);
  }

  /**
   * TODO 2017-11-21 CK : si la définition est nulle ne pas l'ajouter.
   * Add definitions to the attribute.
   *
   * @param defs : pairs of the language of the definition and the definition string.
   */
  protected void addDefinitions(Map<String, String> defs) {
    this.attDefs.putAll(defs);
  }

  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Get the label for a specific language.
   *
   * @param language : the language of the label.
   * @return The label for the specified language.
   *         If not exists or it is empty return attribute id suffixed by the language.
   */
  public String getLabel(String language) {
    String l = this.attLabels.get(language);
    String i = getAttId() + "_" + language;
    return l == null || l.isEmpty() ? i : l;
  }

  /**
   * Get the definition for a specific language.
   *
   * @param language : the language of the definition.
   * @return The definition for the specified language.
   *         If not exists or it is empty return null.
   */
  public String getDefinition(String language) {
    String l = this.attDefs.get(language);
    return l == null || l.isEmpty() ? null : l;
  }

  /**
   * @return Temporal partition type of the attribute.
   */
  public TemporalPartitionType getTemporalPartition() {
    return temporalPartition;
  }

  /**
   * Get the attribute ID.
   *
   * @return The attribute id.
   */
  public String getAttId() {
    return this.attId;
  }

  /**
   * Change the attribute ID.
   *
   * @param attId : a new attribute ID
   */
  public void setAttId(String attId) {
    this.attId = attId;
  }

  /**
   * Get the attribute iri.
   *
   * @return The attribute iri.
   */
  public String getAttIri() {
    return this.attIri;
  }

  /**
   * Change the attribute IRI.
   *
   * @param attIri : a new attribute IRI
   */
  public void setAttIri(String attIri) {
    this.attIri = attIri;
  }

  /**
   * Get the attribute type.
   *
   * @return The attribute type.
   */
  public String getAttType() {
    return this.attType;
  }

  /**
   * Get the attribute labels.
   *
   * @return The pairs of all attribute labels.
   */
  public Map<String, String> getAttLabels() {
    return this.attLabels;
  }

  /**
   * A string representation of an attribute with the attribute id and the type.
   *
   * @param withId : the string with id.
   * @param withType : the string with the type name.
   * @return Attribute string representation with or without the type or the name.
   */
  public String getAttString(boolean withId, boolean withType) {
    StringBuilder s = new StringBuilder();
    s.append(getAttIri());
    if (withId) {
      s.insert(0, getAttId() + "::");
    }
    if (withType) {
      s.insert(s.length(), " " + getAttType());
    }
    return s.toString();
  }

  // **************************************************************************
  // Opérations equals/hashCode/toString et clone
  //

  /**
   * Contruire une hashcode à partir de l'identifiant, du iri et du type.
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.attId == null) ? 0 : this.attId.hashCode());
    result = prime * result + ((this.attIri == null) ? 0 : this.attIri.hashCode());
    result = prime * result + ((this.attType == null) ? 0 : this.attType.hashCode());
    return result;
  }

  /**
   * Vérifier l'égalité entre deux attributs à partir de l'identifiant, du iri et du type.
   *
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
    Attribute other = (Attribute) obj;

    return new EqualsBuilder()
        .append(attId, other.attId)
        .append(attIri, other.attIri)
        .append(attType, other.attType)
        .isEquals();
  }


  /**
   * Effectuer une copie d'un attribut avec les étiquettes.
   *
   * @see java.lang.Object#clone()
   */
  @Override
  public Attribute clone() {
    Attribute copy = null;
    try {
      copy = (Attribute) super.clone();
      copy.attLabels = this.attLabels.entrySet().stream()
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    } catch (CloneNotSupportedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return copy;
  }

  /**
   * TODO 2017-11-21 CK : vérifier l'utilisation et sinon utiliser la définition standard.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getAttString(true, true);
  }
}
