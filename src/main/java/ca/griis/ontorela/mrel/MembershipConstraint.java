package ca.griis.ontorela.mrel;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contrainte pour vérifier l'appartenance et la participation des valeurs d'un attribut dans une
 * table spécifique.
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
public class MembershipConstraint extends Constraint {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static final String defaultName = "_checkMembership";
  private final Table source;
  private final Attribute sourceAtt;
  private final Set<Table> targetSet;
  // private final Participation participation;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * Constructeur principale.
   *
   * @param source : la table à vérifier.
   * @param sourceAtt : l'attribute de la table à vérifier.
   * @param targetSet : les tables utilisées pour la vérification.
   */
  public MembershipConstraint(Table source, Attribute sourceAtt, Set<Table> targetSet) {
    super(source.getIdentifier().getValue() + defaultName);
    this.source = source;
    this.sourceAtt = sourceAtt;
    this.targetSet = targetSet;
  }

  /**
   * Constructeur avec un table cible.
   *
   * @param source : la table à vérifier.
   * @param sourceAtt : l'attribute de la table à vérifier.
   * @param target : la table cible utilisées pour la vérification.
   */
  public MembershipConstraint(Table source, Attribute sourceAtt, Table target) {
    super(source.getIdentifier().getValue() + defaultName);
    this.source = source;
    this.sourceAtt = sourceAtt;
    this.targetSet = new LinkedHashSet<>();
    this.targetSet.add(target);
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * @return source : la table à vérifier.
   */
  public Table getSource() {
    return this.source;
  }

  /**
   * @return sourceAtt : l'attribute de la table à vérifier.
   */
  public Attribute getSourceAtt() {
    return sourceAtt;
  }

  /**
   * @return target : l'ensemble des tables utilisées pour la vérification.
   */
  public Set<Table> getTargetSet() {
    return this.targetSet;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MembershipConstraint [source=" + this.source + ", targetSet=" + this.targetSet + "]";
  }
}
