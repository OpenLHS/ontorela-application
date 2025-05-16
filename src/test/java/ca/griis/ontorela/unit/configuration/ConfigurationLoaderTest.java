package ca.griis.ontorela.unit.configuration;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.configuration.MssqlDatatypes;
import ca.griis.ontorela.configuration.PostgresqlDatatypes;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire du lecteur de configuration de base de données.
 *
 * <b>Tâches projetées</b><br>
 * TODO 2021-03-10 [CK] : Ajouter un logger.<br>
 *
 * <b>Tâches réalisées</b><br>
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
public class ConfigurationLoaderTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  // **************************************************************************
  // Constructeurs
  //
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Test method for
   * {@link ca.griis.ontorela.configuration.ConfigurationLoader#loadDatabaseConfiguration(java.io.File)}.
   */
  @Test
  public void testLoadConfiguration() {
    Path path = Paths.get("test-data/configurations/");
    File configJdd = path.toFile();
    for (File jdd : configJdd.listFiles()) {
      System.out.println("Test: " + jdd.getAbsolutePath());
      for (File configFile : jdd.listFiles()) {
        System.out.println("  " + configFile.getAbsolutePath());
        if (configFile.getName().equals("database_config.yaml")) {
          DatabaseConfiguration config = ConfigurationLoader.loadDatabaseConfiguration(configFile);
          assertNotNull(config);
          assertNotNull(config.getSchemaName());
          assertNotNull(config.getRdbmsName());
          assertNotNull(config.getDefaultKeyName());
          assertNotNull(config.getDefaultKeyType());
          assertNotNull(config.getDefaultKeyDomainName());
          assertNotNull(config.getDefaultAttributeName());
          assertNotNull(config.getDefaultAttributeType());
          assertNotNull(config.getDefaultAttributeDomaineName());
          assertNotNull(config.getMaxIdentifierLength());
          assertNotNull(config.getuseIriAsTableId());
          assertNotNull(config.getNormalizeDatatype());
          assertNotNull(config.getGenerateOpTable());
          assertNotNull(config.getLanguages());
          assertNotNull(config.getOwlSqlTypeMapper());
          assertNotNull(config.getRemoveThingTable());
          // System.out.println(" owlSqlTypeMapperFilePath:" + config.getOwlSqlTypeMapper());
        } else if (configFile.getName().equals("owlsqltype_config.yaml")) {
          PostgresqlDatatypes config = ConfigurationLoader.loadPostgresqlDatatypes(configFile);
          assertNotNull(config);
          assertNotNull(config.getDatatypes());
        } else if (configFile.getName().equals("owlmssqltype_config.yaml")) {
          MssqlDatatypes config = ConfigurationLoader.loadMssqlDatatypes(configFile);
          assertNotNull(config);
          assertNotNull(config.getDatatypes());
        }
      }
    }
  }

  /**
   * Test method for
   * {@link ca.griis.ontorela.configuration.ConfigurationLoader#loadDefaultDatabaseConfiguration()}.
   */
  @Test
  public void testLoadDefaultConfiguration() {
    DatabaseConfiguration config = ConfigurationLoader.loadDefaultDatabaseConfiguration();
    assertNotNull(config);
    assertNotNull(config.getSchemaName());
    assertEquals("ONTORELA", config.getSchemaName());
    assertNotNull(config.getRdbmsName());
    assertEquals("postgresql", config.getRdbmsName().get(0));
    assertNotNull(config.getDefaultKeyName());
    assertEquals("uid", config.getDefaultKeyName());
    assertNotNull(config.getDefaultKeyDomainName());
    assertEquals("uid_domain", config.getDefaultKeyDomainName());
    assertNotNull(config.getDefaultKeyType());
    assertEquals("UUID", config.getDefaultKeyType());
    assertNotNull(config.getDefaultAttributeName());
    assertEquals("value", config.getDefaultAttributeName());
    assertNotNull(config.getDefaultAttributeDomaineName());
    assertEquals("value_domain", config.getDefaultAttributeDomaineName());
    assertNotNull(config.getDefaultAttributeType());
    assertEquals("TEXT", config.getDefaultAttributeType());
    assertNotNull(config.getMaxIdentifierLength());
    assertEquals(30, config.getMaxIdentifierLength());
    assertNotNull(config.getuseIriAsTableId());
    assertTrue(!config.getuseIriAsTableId());
    assertNotNull(config.getNormalizeDatatype());
    assertTrue(config.getNormalizeDatatype());
    assertNotNull(config.getGenerateOpTable());
    assertTrue(config.getGenerateOpTable());
    assertNotNull(config.getLanguages());
    assertEquals("en", config.getLanguages().get(0));
    assertNotNull(config.getOwlSqlTypeMapper());
  }

  /**
   * Test method for
   * {@link ca.griis.ontorela.configuration.ConfigurationLoader#loadDefaultPostgresqlDatatypes()}.
   */
  @Test
  public void testLoadDefaultPostgresqlDatatypes() {
    PostgresqlDatatypes config = ConfigurationLoader.loadDefaultPostgresqlDatatypes();
    assertNotNull(config);
    assertNotNull(config.getDatatypes());

  }

  /**
   * Test exception for
   * {@link ca.griis.ontorela.configuration.ConfigurationLoader#loadDatabaseConfiguration(java.io.File)}.
   */
  @Test
  public void testLoadConfigurationRdbmsExeption() throws Exception {
    Path path = Paths.get("test-data/configurationsErrors/config00");
    File configJdd = path.toFile();

    for (File jdd : configJdd.listFiles()) {
      System.out.println("Test for exception : " + jdd.getAbsolutePath());
      if (jdd.listFiles() != null) {
        for (File configFile : jdd.listFiles()) {
          System.out.println("  " + configFile.getAbsolutePath());
          if (configFile.getName().equals("database_config.yaml")) {
            assertThrows(Exception.class,
                () -> ConfigurationLoader.loadDatabaseConfiguration(configFile));
          }
        }
      }
    }
  }

  /**
   * Test NullPointerException for
   * {@link ca.griis.ontorela.configuration.ConfigurationLoader#loadDatabaseConfiguration(java.io.File)}.
   */
  @Test
  public void testLoadConfigurationRdbmsNullExeption() throws NullPointerException {
    Path path = Paths.get("test-data/configurationsErrors/config01");
    File configJdd = path.toFile();
    for (File jdd : configJdd.listFiles()) {
      System.out.println("Test for NullPointerException : " + jdd.getAbsolutePath());
      if (jdd.listFiles() != null) {
        for (File configFile : jdd.listFiles()) {
          System.out.println("  " + configFile.getAbsolutePath());
          if (configFile.getName().equals("database_config.yaml")) {
            assertThrows(NullPointerException.class,
                () -> ConfigurationLoader.loadDatabaseConfiguration(configFile));
          }
        }
      }
    }
  }

  @Test
  public void testLoadDefaultMssqlDatatypes() {
    MssqlDatatypes config = ConfigurationLoader.loadDefaultMssqlDatatypes();
    assertNotNull(config);
    assertNotNull(config.getDatatypes());
  }
}
