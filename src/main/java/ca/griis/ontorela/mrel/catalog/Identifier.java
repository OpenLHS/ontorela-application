package ca.griis.ontorela.mrel.catalog;

/**
 * Un identifiant unique.
 * Les identifiants doivent être générés oar le catalogue deds identifiants (idCatalog).
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * <i>...</i> <br>
 *
 * <b>Tâches réalisées</b><br>
 * <i>2017-XX-XX (0.2.0) [XX] ... </i> <br>
 * <i>2017-11-13 (0.1.0) [CK] Mise en oeuvre initiale. </i> <br>
 *
 * <p>
 * <b>Copyright</b> 2014-2016, Μῆτις (http://info.usherbrooke.ca/llavoie/) <br>
 * <b>Copyright</b> 2017, GRIIS (http://griis.ca/) <br>
 *
 * <p>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé) <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke <br>
 * Sherbrooke (Québec) J1K 2R1 CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 *
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2018-02-26
 */
public class Identifier {
  // **************************************************************************
  // Attributs spécifiques
  //
  private final String id;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * @param id
   */
  protected Identifier(String id) {
    super();
    this.id = id;
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Obtenir la valeur de l'identifier.
   *
   * @return la valeur de l'identifier.
   */
  public String getValue() {
    return this.id;
  }

  // **************************************************************************
  // Opérations equals/hashCode/toStrung
  //
  /*
   * (non-Javadoc)
   * 
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
    Identifier other = (Identifier) obj;
    if (this.id == null) {
      return other.id == null;
    } else {
      return this.id.equals(other.id);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Identifier [" + this.id + "]";
  }
}
