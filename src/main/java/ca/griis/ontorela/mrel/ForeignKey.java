package ca.griis.ontorela.mrel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Une clé référentielle est définie par une identifiant unique au sein de la base de données,
 * avec deux tables : une table d'origine (srouce) et une table destination (cible) et
 * les attributs qui participent à la définition de la clé.
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
 * TODO 2020-01-23 CK : Dériver de Constraint ? <br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2017-11-20 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2017-11-20
 */
public class ForeignKey {
  // **************************************************************************
  // Attributs spécifiques
  //
  public enum ForeignKeyType {
    ISA("isa"), OBJECTPROPERTY("op"), DATAPROPERTY("dp");

    //
    String string;

    ForeignKeyType(String string) {
      this.string = string;
    }

    public String getString() {
      return this.string;
    }
  }

  private final ForeignKeyType foreignKeyType;
  private final String fkId;
  private final Table origin;
  private final Table destination;
  private final Map<Attribute, Attribute> linkedAtt;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * TODO 2018-06-12 CK : vérifier que les attributs de destination forment une clés candidates.
   * Main constructor.
   *
   * @param fkId : the contrainte identifier (name).
   * @param origin : the origin table.
   * @param destination : the destination table.
   * @param linkedAtt : a pair of attribute from the origin table and a attibute from the
   *        destination table.
   */
  public ForeignKey(ForeignKeyType foreignKeyType, String fkId, Table origin, Table destination,
      Map<Attribute, Attribute> linkedAtt) {
    super();
    this.foreignKeyType = foreignKeyType;
    this.fkId = fkId;
    this.origin = origin;
    this.destination = destination;
    this.linkedAtt = linkedAtt;
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * @return foreignKeyType
   */
  public ForeignKeyType getForeignKeyType() {
    return foreignKeyType;
  }

  /**
   * Get the foreign key id (name).
   *
   * @return The foreign key id (name).
   */
  public String getFkId() {
    return this.fkId;
  }

  /**
   * Get the origin table.
   *
   * @return The origin table.
   */
  public Table getOrigin() {
    return this.origin;
  }

  /**
   * Get the destination table.
   *
   * @return The destination table.
   */
  public Table getDestination() {
    return this.destination;
  }

  /**
   * TODO 2018-06-12 CK : vérifier que l'ensemble d'attribut existe dans la table d'origine.
   * Get the origin table attributes forming the foreign key.
   *
   * @return The origine table attributes forming the foreign key.
   */
  public Set<Attribute> getAttOrigin() {
    return this.linkedAtt.keySet();
  }

  /**
   * Get the origin table attributes ids forming the foreign key.
   *
   * @return The string set of id of the attributes forming the foreign key.
   */
  public Set<String> getAttOrigineId() {
    Set<String> attIDs = new LinkedHashSet<>();
    getAttOrigin().stream().forEach(a -> attIDs.add(a.getAttId()));
    return attIDs;
  }

  public Set<String> getAttOrigineId(boolean useIriAsTableId) {
    Set<String> attIDs = new LinkedHashSet<>();
    getAttOrigin().stream()
        .forEach(a -> attIDs.add(useIriAsTableId ? a.getAttIri() : a.getAttId()));
    return attIDs;
  }

  /**
   * Get the destination table attributes forming the foreign key.
   *
   * @return The destination table attributes forming the foreign key.
   */
  public Collection<Attribute> getAttDestination() {
    return this.linkedAtt.values();
  }

  /**
   * Get the destination table attributes ids forming the foreign key.
   *
   * @return The string set of id of the attributes forming the foreign key.
   */
  public Set<String> getAttDestinationId() {
    Set<String> attIds = new LinkedHashSet<>();
    getAttDestination().forEach(a -> attIds.add(a.getAttId()));
    return attIds;
  }

  public Set<String> getAttDestinationId(boolean useIriAsTableId) {
    Set<String> attIds = new LinkedHashSet<>();
    getAttDestination().forEach(a -> attIds.add(useIriAsTableId ? a.getAttIri() : a.getAttId()));
    return attIds;
  }

  /**
   * Get the pair of attributes (attOrgin, attDestination) forming the foreign key.
   *
   * @return The pair of attributes (attOrgin, attDestination) forming the foreign key.
   */
  public Map<Attribute, Attribute> getLinkedAtt() {
    return this.linkedAtt;
  }

  /**
   * Description textuelle de la contrainte référentielle.
   *
   * @return une description textuelle de la contrainte référentielle.
   */
  public String foreignKeyString() {
    String s = "";
    ArrayList<String> aoSet = new ArrayList<>();
    getAttOrigin().stream().forEach(ao -> aoSet.add(ao.getAttString(true, false)));
    ArrayList<String> adSet = new ArrayList<>();
    getAttDestination().forEach(ad -> adSet.add(ad.getAttString(true, false)));
    s += getDestination().getTableIriPrefixed(true) + "{" + String.join(", ", aoSet) + "}" + " -> "
        + getDestination().getTableIriPrefixed(true) + "{" + String.join(", ", adSet) + "}";
    return s;
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
    result = prime * result + ((this.destination == null) ? 0 : this.destination.hashCode());
    result = prime * result + ((this.linkedAtt == null) ? 0 : this.linkedAtt.hashCode());
    result = prime * result + ((this.origin == null) ? 0 : this.origin.hashCode());
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

    ForeignKey other = (ForeignKey) obj;

    return new EqualsBuilder()
        .append(destination, other.destination)
        .append(linkedAtt, other.linkedAtt)
        .append(origin, other.origin)
        .isEquals();
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ForeignKey [foreignKeyType=" + this.foreignKeyType + ", fkId=" + this.fkId + ", origin="
        + this.origin + ", destination=" + this.destination + ", linkedAtt=" + this.linkedAtt + "]";
  }
}
