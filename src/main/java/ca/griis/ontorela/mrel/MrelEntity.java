package ca.griis.ontorela.mrel;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoIriI;
import ca.griis.ontorela.mrel.catalog.Identifier;
import ca.griis.ontorela.mrel.catalog.MrelCatalog;
import ca.griis.ontorela.mrel.catalog.MrelEntityType;

/**
 * Une entité MRel.
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
 * TODO 2018-07-19 CK : migrer dans OntoRel.<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2018-07-19 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-07-19
 */
public abstract class MrelEntity {
  // **************************************************************************
  // Attributs spécifiques
  //
  private final Identifier id;

  // **************************************************************************
  // Constructeurs
  //
  public MrelEntity(MrelEntityType type, OntoIriI iri) {
    this.id = MrelCatalog.addEntity(type, this, iri);
  }

  public MrelEntity(MrelEntityType type, OntoAxiomAssociationI ontoAxiom) {
    this.id = MrelCatalog.addEntity(type, this, ontoAxiom);
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
  public Identifier getIdentifier() {
    return this.id;
  }

  // **************************************************************************
  // Opérations equals/hashCode/toStrung
  //

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
    MrelEntity other = (MrelEntity) obj;
    if (this.id == null) {
      return other.id == null;
    } else {
      return this.id.equals(other.id);
    }
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
    return result;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MrelEntity [id=" + this.id + "]";
  }
}
