package ca.griis.ontorela.configuration;


import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;
import org.slf4j.Logger;

/**
 * Interface de la mise en correspondance entre les types OWL et les types SQL.
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
public interface OwlSqlDatatypes {
  //
  Logger LOGGER = getLogger(OwlSqlDatatypes.class);

  //

  /**
   * Obtenir la mise en correspondance entre un type OWL et SQL.
   * Un type OWL est représenté par son shortIRI et le type SQL par le nom du type.
   *
   * @return toutes les paires[owltype:sqlType] définies dans le fichier de configuration.
   */
  Map<String, String> getDatatypes();

  /**
   * Définir la mise en correspondance entre un type OWL et SQL.
   *
   * @param datatypes : toutes les paires[owltype:sqlType] définies dans le fichier de
   *        configuration.
   */
  void setDatatypes(Map<String, String> datatypes);

  /**
   * Obtenir le type SQL correspondant à un type OWL.
   *
   * @param owlDatatype : un shortIri correspondant à un type OWL
   * @return Le type SQL correspondant à un type OWL. Si le type OWL n'est pas défini,
   *         retourne un type par défault selon le SGBD.
   */
  String getSqlValue(String owlDatatype);
}
