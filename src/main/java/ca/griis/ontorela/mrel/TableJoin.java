package ca.griis.ontorela.mrel;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoIriI;
import ca.griis.monto.model.Participation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Une jointure de table est une définition de jointure entre deux tables.
 * Une jointure est définie entre deux tables et un ensemble de paires d'attributs
 * formant la condition de la jointure.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : oui.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * TODO 2018-07-12 CK : utiliser la classe annotationDB pour les étiquettes et les définitions <br>
 * TODO 2018-07-15 CK : adapter les fonctions de création des fk avec ceux de la classe Schema. <br>
 * TODO 2018-07-15 CK : développer une fonction de vérification des attributs de jointure.<br>
 * TODO 2018-09-18 CK : étendre pour supporter plusieurs table.<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-07-15 (0.1.0) [CK] : Création. <br>
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
 * @since 2018-07-15
 */
public final class TableJoin extends Table {
  // **************************************************************************
  // Attributs spécifiques
  //
  private Table leftTable;
  private Table rightTable;
  private final Participation participation;
  private Map<Attribute, Attribute> joinAtt;
  // **************************************************************************
  // Constructeurs
  //

  /**
   * Main constructor.
   * TODO 2019-11-20 CK : création OntoIRI ou null ?
   *
   * @param origin : the table origin.
   * @param schemaId : the schema id in which the table is contained.
   * @param tableIri : the table owl short iri.
   * @param leftTable : a table.
   * @param rightTable : a table.
   * @param participation : a participation
   * @param joinAtt : list of pairs attributes to join.
   */
  public TableJoin(TableOrigin origin, OntoAxiomAssociationI ontoAxiom, String schemaId,
      String tableIri, Table leftTable, Table rightTable, Participation participation,
      Map<Attribute, Attribute> joinAtt) {
    super(origin, ontoAxiom, schemaId, tableIri);
    //
    this.leftTable = leftTable;
    this.rightTable = rightTable;
    this.participation = participation;
    this.joinAtt = joinAtt;
  }

  /**
   * Build a join table with an empty join attributes (to be added later).
   *
   * @param origin : the table origin.
   * @param schemaId : the schema id in which the table is contained.
   * @param tableIri : the table owl short iri.
   * @param leftTable : a table.
   * @param rightTable : a table.
   * @param participation : a participation
   */
  public TableJoin(TableOrigin origin, OntoAxiomAssociationI ontoAxiom, String schemaId,
      String tableIri, Table leftTable, Table rightTable, Participation participation) {
    super(origin, ontoAxiom, schemaId, tableIri);
    //
    this.leftTable = leftTable;
    this.rightTable = rightTable;
    this.participation = participation;
    this.joinAtt = new LinkedHashMap<>();
  }

  /**
   * Build a join table with an empty join attributes (to be added later).
   *
   * @param origin : the table origin.
   * @param schemaId : the schema id in which the table is contained.
   * @param tableIri : the table owl short iri.
   * @param leftTable : a table.
   * @param rightTable : a table.
   * @param participation : a participation
   */
  public TableJoin(TableOrigin origin, OntoIriI ontoIri, String schemaId, String tableIri,
      Table leftTable, Table rightTable, Participation participation,
      Map<Attribute, Attribute> joinAtt) {
    super(origin, ontoIri, schemaId, tableIri);
    //
    this.leftTable = leftTable;
    this.rightTable = rightTable;
    this.participation = participation;
    if (joinAtt.isEmpty()) {
      throw new IllegalArgumentException(
          "L'ensemble des attributs de jointure ne doit pas être vide ");
    } else {
      this.joinAtt = joinAtt;
    }
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * @return participation
   */
  public Participation getParticipation() {
    return participation;
  }

  /**
   * Get all join attribute conditions.
   *
   * @return All join attibute conditions.
   */
  public Map<Attribute, Attribute> getJoinAtt() {
    return this.joinAtt;
  }

  /**
   * Add a join attribute equal condition.
   *
   * @param leftAtt : an attribute of the left table.
   * @param rightAtt : an attribute of the right table.
   */
  public void addJoinAtt(Attribute leftAtt, Attribute rightAtt) {
    this.joinAtt.put(leftAtt, rightAtt);
  }

  /**
   * Définir les attributs de jointures.
   *
   * @param joinAtt : les paires d'attributs de jointure.
   */
  public void setJoinAtt(Map<Attribute, Attribute> joinAtt) {
    this.joinAtt = joinAtt;
  }

  /**
   * Get the left table of the join.
   * Note that for the a natural, full and inner join the position of the table in the
   * join is useless.
   *
   * @return The left table of the join.
   */
  public Table getLeftTable() {
    return this.leftTable;
  }

  /**
   * Get the right table of the join.
   * Note that for the a natural, full and inner join the position of the table in the
   * join is useless.
   *
   * @return The left table of the join.
   */
  public Table getRightTable() {
    return this.rightTable;
  }

  /**
   * TODO 2017-11-21 CK : à refaire avec la syntaxe Discipulus ?
   * Get the table declaration.
   *
   * @return the string of the table definition.
   */
  public String getTableDeclarationString() {
    StringBuffer sb = new StringBuffer();
    sb.append("JOINTABLE " + getTableIriPrefixed(true) + "::" + getLabels() + "\n");
    sb.append(getRightTable() + " JOIN " + getLeftTable() + "\n  {\n");
    for (Attribute a : getAttributeSet()) {
      sb.append("    " + a.toString() + "::" + a.getAttLabels() + "\n");
    }
    sb.append("  } \n");
    // Keys
    for (CandidateKey cc : getKeyAttributeSet()) {
      ArrayList<String> keyIds = new ArrayList<>();
      cc.getKeyAttSet().stream().forEach(a -> keyIds.add(a.getAttId()));
      sb.append("KEY {" + String.join(", ", keyIds) + "} \n");
    }
    return sb.toString();
  }

  // **************************************************************************
  // Opérations : polymophisme avec Table
  //

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.joinAtt == null) ? 0 : this.joinAtt.hashCode());
    result = prime * result + ((this.leftTable == null) ? 0 : this.leftTable.hashCode());
    result = prime * result + ((this.rightTable == null) ? 0 : this.rightTable.hashCode());
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
    TableJoin other = (TableJoin) obj;
    return new EqualsBuilder()
        .append(joinAtt, other.joinAtt)
        .append(leftTable, other.leftTable)
        .append(rightTable, other.rightTable)
        .isEquals();
  }


  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getTableIriPrefixed(true);
  }
}
