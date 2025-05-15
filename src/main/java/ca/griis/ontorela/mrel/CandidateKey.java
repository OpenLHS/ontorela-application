package ca.griis.ontorela.mrel;

import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Une clé candidate est une contrainte définie par un ensemble attributs qui doivent être
 * unique au sein d'une table.
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
 * TODO 2020-01-23 CK : Dériver de Constraint ? <br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2018-09-18 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-05-16
 */
public class CandidateKey {
  // **************************************************************************
  // Attributs spécifiques
  //
  private String id;
  private Set<Attribute> keyAttSet;
  private boolean primary;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * Main constructor of an candidate key.
   *
   * @param id : candidate key constraint identifier (name).
   * @param keyAttSet : set of attributes that form the key.
   * @param primary : is the candidate key a primary key ?
   */
  public CandidateKey(String id, Set<Attribute> keyAttSet, boolean primary) {
    super();
    this.id = id;
    this.keyAttSet = keyAttSet;
    this.primary = primary;
  }

  /**
   * Construct a candidate key with one attribute.
   *
   * @param id : candidate key constraint name.
   * @param keyAtt : the attribute that form the key.
   * @param primary : is the candidate key a primary key ?
   */
  public CandidateKey(String id, Attribute keyAtt, boolean primary) {
    this.id = id;
    this.keyAttSet = new LinkedHashSet<>();
    this.keyAttSet.add(keyAtt);
    this.primary = primary;
  }
  // **************************************************************************
  // Opérations propres
  //

  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Replace the candidate key contraint identifier.
   *
   * @param id : a candidate key contraint identifier.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Add a key to the candidate key.
   *
   * @param keyAtt : the key attribut to add.
   */
  public void addKey(Attribute keyAtt) {
    this.keyAttSet.add(keyAtt);
  }

  /**
   * Change the candidate key type.
   *
   * @param primary : set the key as a primary key (true) or not (false).
   */
  public void setPrimary(boolean primary) {
    this.primary = primary;
  }

  /**
   * Get the candidate key constraint identifier.
   *
   * @return the candidate key constraint identifier.
   */
  public String getId() {
    return this.id;
  }

  public Set<String> getKeyIri() {
    Set<String> ids = new LinkedHashSet<>();
    this.getKeyAttSet().stream().forEach(k -> ids.add(k.getAttIri()));
    return ids;
  }

  /**
   * Get the set of attributs forming the candidate key.
   *
   * @return the set of key attributes.
   */
  public Set<Attribute> getKeyAttSet() {
    return this.keyAttSet;
  }

  /**
   * Replace the set of key attributes.
   *
   * @param keyAttSet : a set of key attributes.
   */
  public void setKeyAttSet(Set<Attribute> keyAttSet) {
    this.keyAttSet = keyAttSet;
  }

  /**
   * Get the set of key attributs ids.
   *
   * @return The string set of key attribute ids.
   */
  public Set<String> getKeyAttIds() {
    Set<String> ids = new LinkedHashSet<>();
    this.getKeyAttSet().stream().forEach(k -> ids.add(k.getAttId()));
    return ids;
  }

  /**
   * Get the set od key attributes string id:Type.
   *
   * @param withId : return a string with attribute id.
   * @param withType : return a string with the type name.
   * @return The string set of key attributes.
   */
  public Set<String> getKeyAttString(boolean withId, boolean withType) {
    Set<String> string = new LinkedHashSet<>();
    this.getKeyAttSet().stream().forEach(k -> string.add(k.getAttString(withId, withType)));
    return string;
  }

  /**
   * Is candidate key a primary key?.
   *
   * @return True if the candidate key is a primary key.
   */
  public boolean isPrimary() {
    return this.primary;
  }
  // **************************************************************************
  // Opérations equals/hashCode/toString
  //

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
    result = prime * result + ((this.keyAttSet == null) ? 0 : this.keyAttSet.hashCode());
    result = prime * result + (this.primary ? 1231 : 1237);
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
    CandidateKey other = (CandidateKey) obj;
    return new EqualsBuilder()
        .append(id, other.id)
        .append(keyAttSet, other.keyAttSet)
        .append(primary, other.primary)
        .isEquals();
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "CandidateKey [id=" + this.id + ", keyAttSet=" + this.keyAttSet + "]";
  }
}
