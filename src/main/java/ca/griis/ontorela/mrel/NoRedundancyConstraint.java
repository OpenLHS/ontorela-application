package ca.griis.ontorela.mrel;

/**
 * Décrire la classe ici.
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui | non (pourquoi).</li>
 * <li>Clonabilité : oui | non (pourquoi).</li>
 * <li>Modifiabilité : oui | non (pourquoi).</li>
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
public class NoRedundancyConstraint extends Constraint {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static final String defaultName = "_checkNonRedundancy";
  private final TableJoin joinExp;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * Constructeur princiapel.
   *
   * @param joinExp
   */
  public NoRedundancyConstraint(TableJoin joinExp) {
    super(joinExp.getIri() + defaultName);
    this.joinExp = joinExp;
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * @return joinExp
   */
  public TableJoin getJoinExp() {
    return this.joinExp;
  }
}
