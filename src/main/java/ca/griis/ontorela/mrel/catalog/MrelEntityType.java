package ca.griis.ontorela.mrel.catalog;

/**
 * Énumérateur des types de composants MRel pouvant avoir des identifiant interne.
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
 * ... <br>
 *
 * <b>Tâches réalisées</b><br>
 * 2017-XX-XX (0.2.0) [XX] ... <br>
 * 2017-11-13 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-07-17
 */
public enum MrelEntityType {
  /**
   * Schema
   */
  SCHEMA,
  /**
   * Type table
   */
  TTABLE,
  /**
   * Property table
   */
  PTABLE,
  /**
   * Association table
   */
  TABLE,
  /**
   * View
   */
  VIEW;

  //
  private final String type;

  MrelEntityType() {
    this.type = this.name();
  }

  public String getEntityType() {
    return this.type;
  }
}
