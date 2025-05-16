package ca.griis.ontorela.configuration;

import ca.griis.ontorela.catalog.OntoRelCatLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Gestionnaire de chargement des configurations.
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
 * 2018-09-06 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-09-06
 */
public class ConfigurationLoader {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static DatabaseConfiguration dbconfig;

  // **************************************************************************
  // Constructeurs
  //
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
  public static DatabaseConfiguration getDatabaseConfiguration() {
    return dbconfig;
  }

  /**
   * Charger le fichier de configuration.
   *
   * @param configFile : le fichier de configuration de la base de données.
   * @return Une configuration de la base de données.
   */
  public static DatabaseConfiguration loadDatabaseConfiguration(File configFile) {
    synchronized (ConfigurationLoader.class) {
      if (dbconfig == null
          || !Objects.equals(dbconfig.getConfigDirectoryPath(), configFile.getAbsolutePath())) {
        try (InputStream input = new FileInputStream(configFile);) {
          Constructor constructor =
              new Constructor(DatabaseConfiguration.class, new LoaderOptions());
          Yaml yaml = new Yaml(constructor);
          dbconfig = yaml.load(input);
          if (dbconfig.getConfigDirectoryPath() == null
              || dbconfig.getConfigDirectoryPath().isEmpty()) {
            dbconfig.setConfigDirectoryPath(configFile.getParent());
          }
          OntoRelCatLoader.loadOntoRelCat(dbconfig.getOntoRelDicFilePath());
        } catch (FileNotFoundException e) {
          loadDefaultDatabaseConfiguration();
          System.err.println(" Configuration file not found on :" + configFile.getAbsolutePath());
        } catch (IOException e) {
          // Gérer d'autres exceptions liées à la lecture du fichier
          e.printStackTrace();
        }
      }
    }
    return dbconfig;
  }

  /**
   * Charger le fichier de configuration par défault.
   * Les paramètres par défault sont ceux de PostgreSQL.
   *
   * @return La configuration le la base de données par défault.
   */
  public static DatabaseConfiguration loadDefaultDatabaseConfiguration() {
    InputStream input = ConfigurationLoader.class.getClassLoader()
        .getResourceAsStream("default_database_config.yaml");
    Constructor constructor = new Constructor(DatabaseConfiguration.class, new LoaderOptions());
    Yaml yaml = new Yaml(constructor);
    dbconfig = yaml.load(input);
    return dbconfig;
  }

  /**
   * Charger le fichier de correspondance OWL-SQL pour postgreSQL.
   *
   * @param configFile : le fichier de mise en correspondance OWLSQL pour postgreSQL.
   * @return Une configuration de mise en correspondance OWLSQL de postgresql.
   */
  public static PostgresqlDatatypes loadPostgresqlDatatypes(File configFile) {
    PostgresqlDatatypes config = null;
    try (InputStream input = new FileInputStream(configFile)) {
      Constructor constructor = new Constructor(PostgresqlDatatypes.class, new LoaderOptions());
      Yaml yaml = new Yaml(constructor);
      return yaml.load(input);
    } catch (FileNotFoundException e) {
      System.err.println(" Configuration file not found on :" + configFile.getAbsolutePath());
    } catch (IOException e) {
      // Gérer d'autres exceptions liées à la lecture du fichier
      e.printStackTrace();
    }
    return config;
  }

  /**
   * Charger le fichier de correspondance OWL-SQL par défault pour postgreSQL.
   *
   * @return La configuration par défault de mise en correspondance OWL-SQL de postgresql.
   */
  public static PostgresqlDatatypes loadDefaultPostgresqlDatatypes() {
    PostgresqlDatatypes config = null;
    InputStream input = ConfigurationLoader.class.getClassLoader()
        .getResourceAsStream("default_postgresql_owlsqltype_config.yaml");
    Constructor constructor = new Constructor(PostgresqlDatatypes.class, new LoaderOptions());
    Yaml yaml = new Yaml(constructor);
    config = yaml.load(input);
    return config;
  }

  /**
   * Charger le fichier de correspondance OWL-SQL pour MSSQL.
   *
   * @param configFile : le fichier de mise en correspondance OWLSQL pour MSSQL.
   * @return Une configuration de mise en correspondance OWLSQL de MSSQL.
   */
  public static MssqlDatatypes loadMssqlDatatypes(File configFile) {
    MssqlDatatypes config = null;
    try (InputStream input = new FileInputStream(configFile)) {
      Constructor constructor = new Constructor(MssqlDatatypes.class, new LoaderOptions());
      Yaml yaml = new Yaml(constructor);
      return config = yaml.load(input);
    } catch (FileNotFoundException e) {
      System.err.println(" Configuration file not found on :" + configFile.getAbsolutePath());
    } catch (IOException e) {
      // Gérer d'autres exceptions liées à la lecture du fichier
      e.printStackTrace();
    }
    return config;
  }

  /**
   * Charger le fichier de correspondance OWL-SQL par défault pour MSSQL.
   *
   * @return la configuration par défaut de mise en correspondance OWL-SQL de MSSQL.
   */
  public static MssqlDatatypes loadDefaultMssqlDatatypes() {
    MssqlDatatypes config = null;
    InputStream input = ConfigurationLoader.class.getClassLoader()
        .getResourceAsStream("default_mssql_owlsqltype_config.yaml");
    Constructor constructor = new Constructor(MssqlDatatypes.class, new LoaderOptions());
    Yaml yaml = new Yaml(constructor);
    config = yaml.load(input);
    return config;
  }
}
