package ca.griis.ontorela.catalog;

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
 * 2019-11-21 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2019-11-21
 */
public class OntoAxiomRelvarEntry {
  // **************************************************************************
  // Attributs spécifiques
  //
  private final String tableId;
  private final String domainIri;
  private final String rangeIri;
  private final String propertyIri;

  // **************************************************************************
  // Constructeur

  /**
   * @param tableId
   * @param domainIri
   * @param rangeIri
   * @param propertyIri
   */
  public OntoAxiomRelvarEntry(String tableId, String domainIri, String rangeIri,
      String propertyIri) {
    super();
    this.tableId = tableId;
    this.domainIri = domainIri;
    this.rangeIri = rangeIri;
    this.propertyIri = propertyIri;
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * @return tableId
   */
  public String getTableId() {
    return this.tableId;
  }

  /**
   * @return domainIri
   */
  public String getDomainIri() {
    return this.domainIri;
  }

  /**
   * @return rangeIri
   */
  public String getRangeIri() {
    return this.rangeIri;
  }

  /**
   * @return propertyIri
   */
  public String getPropertyIri() {
    return this.propertyIri;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "OntoAxiomRelvarEntry [tableId=" + this.tableId + ", domainIri=" + this.domainIri
        + ", propertyIri=" + this.propertyIri + ", rangeIri=" + this.rangeIri + "]";
  }
}
