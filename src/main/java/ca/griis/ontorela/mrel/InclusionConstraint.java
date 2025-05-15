package ca.griis.ontorela.mrel;

import java.util.Set;

/**
 * Contrainte pour vérifier si une table d'union contient tous les tuples des tables qui forment
 * les éléments de l'union.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : non.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2018-11-25 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-11-25
 */
public class InclusionConstraint extends Constraint {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static final String defaultName = "_checkInclusion";
  private final Table unionTable;
  private final Set<Table> elementSet;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * @param unionTable : la table d'union
   * @param elementSet : les table appartenant à la table d'union.
   */
  public InclusionConstraint(Table unionTable, Set<Table> elementSet) {
    super(unionTable.getIdentifier().getValue() + defaultName);
    this.unionTable = unionTable;
    this.elementSet = elementSet;
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * @return unionTable
   */
  public Table getUnionTable() {
    return this.unionTable;
  }

  /**
   * @return elements
   */
  public Set<Table> getElementSet() {
    return this.elementSet;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InclusionConstraint [unionTable=" + this.unionTable + ", elementSet=" + this.elementSet
        + "]";
  }
}
