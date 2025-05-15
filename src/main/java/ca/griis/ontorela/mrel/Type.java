package ca.griis.ontorela.mrel;

/**
 * Un type est formé d'un identidiant et d'un domaine.
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
 * ..<br>
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
public class Type {
  // **************************************************************************
  // Attributs spécifiques
  //
  private final String id;
  private final String domain;

  // **************************************************************************
  // Constructeur

  /**
   * @param id : l'identifiant du type.
   * @param domain : le domaine du type.
   */
  public Type(String id, String domain) {
    super();
    this.id = id;
    this.domain = domain;
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Obtenir l'identifiant du domaine.
   *
   * @return id : l'identifiant du domaine.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Obtenir le domaine du type.
   *
   * @return type : le domaine du type.
   */
  public String getDomain() {
    return this.domain;
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
    result = prime * result + ((this.domain == null) ? 0 : this.domain.hashCode());
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
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Type other = (Type) obj;
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    if (this.domain == null) {
      return other.domain == null;
    } else {
      return this.domain.equals(other.domain);
    }
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Domain [id=" + this.id + ", type=" + this.domain + "]";
  }
}
