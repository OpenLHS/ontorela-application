package ca.griis.ontorela.mrel;

/**
 * Une contrainte, est une expression boolléenne.
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
 * ..<br>
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
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2018-09-18
 */
public abstract class Constraint {
  // **************************************************************************
  // Attributs spécifiques
  //
  private final String name;
  // **************************************************************************
  // Constructeurs
  //

  /**
   * @param name
   */
  public Constraint(String name) {
    super();
    this.name = name;
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * @return name
   */
  public String getName() {
    return this.name;
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
    result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
    Constraint other = (Constraint) obj;
    if (this.name == null) {
      return other.name == null;
    } else {
      return this.name.equals(other.name);
    }
  }
}
