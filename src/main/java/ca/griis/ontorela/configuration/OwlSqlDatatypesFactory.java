package ca.griis.ontorela.configuration;

/**
 * Structure générique de la configuration de la mise en correspondance entre les types
 * SQL et OWL.
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
 * 2018-07-12 (0.1.1) [CK] : Revue et documentation. <br>
 * 2018-02-22 (0.1.0) [FO] : Création. <br>
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
 * @author [FO] Francis.Ouellet@USherbrooke.ca
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @version 0.1.0
 * @since 2018-02-22
 */
public class OwlSqlDatatypesFactory {
  /**
   * Obtenir la correspondance entre les types OWL et SQL d'un SGBD.
   *
   * @param rdbmsName : une configuration de base de données.
   * @return Owl and SQL data types from a specific SGBD.
   */
  public static OwlSqlDatatypes getDatatypes(String rdbmsName) {
    OwlSqlDatatypes owlsqlDatatypes = null;
    if ("postgresql".equalsIgnoreCase(rdbmsName)) {
      owlsqlDatatypes = ConfigurationLoader.loadDefaultPostgresqlDatatypes();
    } else if ("mssql".equalsIgnoreCase(rdbmsName)) {
      owlsqlDatatypes = ConfigurationLoader.loadDefaultMssqlDatatypes();
    } else {
      System.err.println(rdbmsName + "RDBMS not supporte yet");
    }
    return owlsqlDatatypes;
  }
}
