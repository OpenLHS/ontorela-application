package ca.griis.ontorela.mrel;

import ca.griis.monto.api.ParticipationI;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contrainte de vérification de la participation.
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
 * 2018-09-10 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-09-10
 */
public class ParticipationConstraint extends Constraint {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static final String default_name = "_checkParticipation";
  private final Table table;
  private final ParticipationI participation;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * Constructeur principal.
   *
   * @param table
   * @param participation
   */
  public ParticipationConstraint(Table table, ParticipationI participation) {
    super(table.getIdentifier().getValue() + default_name);
    this.table = table;
    this.participation = participation;
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * @return table
   */
  public Table getTable() {
    return this.table;
  }

  /**
   * @return participation
   */
  public ParticipationI getParticipation() {
    return this.participation;
  }

  /**
   * Obtenir la liste des attributs sur lesquels le compte de participation s'applique.
   *
   * @return la liste des attributs formant la clé primaire de la table du domaine (déterminante).
   *         TODO 2019-05-15 CK : rendre configurable.
   */
  public Set<Attribute> getSearchAttributeSet() {
    Set<Attribute> attSet = new LinkedHashSet<>();
    if (this.table instanceof TableJoin) {
      attSet.addAll(((TableJoin) this.table).getJoinAtt().keySet());
    } else {
      attSet.addAll(this.table.getPrimaryKeyAttributeSet());
    }
    return attSet;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ParticipationConstraint [table=" + this.table + ", participation=" + this.participation
        + "]";
  }
}
