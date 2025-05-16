package ca.griis.ontorela.configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Structure de la mise en correspondance entre les types SQL de MSSQL et OWL.
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
 * 2021-03-14 (0.1.0) [KB] : Création avec la base du version postgresql. <br>
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
 * @author [KB] Karen.Ivonne.Boisier.Aguayo@Usherbrooke.ca
 * @version 0.1.0
 * @since 2021-01-15
 */
public class MssqlDatatypes implements OwlSqlDatatypes {
  // **************************************************************************
  // Attributs spécifiques
  //
  public static final String defaultSqlValue = "VARCHAR";
  private Map<String, String> datatypes;

  // **************************************************************************
  // Opérations propres
  //
  private void checkSqlDatatypeCompatibility() {
    Set<String> mssqlDatatypes = new HashSet<>();
    for (MssqlDatatypeEnum t : MssqlDatatypeEnum.values()) {
      mssqlDatatypes.add(t.name());
    }
    //
    Set<String> noCompatible = new HashSet<>();
    for (String s : this.datatypes.values()) {
      if (!mssqlDatatypes.contains(s)) {
        noCompatible.add(s);
      }
    }
    // Trace
    for (String s : noCompatible) {
      LOGGER.warn("MSSQL does not have the type " + s);
    }
  }

  // **************************************************************************
  // Opérations publiques
  //

  /**
   * See interface.
   *
   * @see ca.griis.ontorela.configuration.OwlSqlDatatypes#getDatatypes()
   */
  @Override
  public Map<String, String> getDatatypes() {
    return this.datatypes;
  }

  /**
   * See interface.
   *
   * @see ca.griis.ontorela.configuration.OwlSqlDatatypes#setDatatypes(java.util.Map)
   */
  @Override
  public void setDatatypes(Map<String, String> datatypes) {
    this.datatypes = datatypes;
    checkSqlDatatypeCompatibility();
  }

  /**
   * Type par défaut : TEXT.
   *
   * @see ca.griis.ontorela.configuration.OwlSqlDatatypes#getSqlValue(String)
   */
  @Override
  public String getSqlValue(String owlDatatype) {
    if (datatypes == null || !datatypes.containsKey(owlDatatype)) {
      return defaultSqlValue;
    }
    return datatypes.get(owlDatatype);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MssqlDatatypes [datatypes=" + this.datatypes + "]";
  }
}
