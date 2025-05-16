package ca.griis.ontorela.unit.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.core.ConfigurationsSarter;
import ca.griis.ontorela.core.ExecutionDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour chargement des Configurations
 *
 * <h3>Historique</h3>
 * <p>
 * 2025-01-31 [AS] - Implémentation initiale <br>
 * </p>
 *
 * <h3>Tâches</h3>
 * S.O.
 *
 * @author [AS] Amnei.Souid@USherbrooke.ca
 * @version 2.0.0
 * @since 2.0.0
 */

public class ConfigurationStarterTest {
  private Descriptor msg = new Descriptor();
  private Date date = new Date();
  private String dateString = new SimpleDateFormat("yyyyMMdd-HHmm").format(date);
  private ExecutionDescriptor executionDescriptor = new ExecutionDescriptor(msg, dateString);

  @Test
  void testGetOntologyConfigurationFile_nofile() {
    Path path = Paths.get("test-data/configurations/");
    File configJdd = path.toFile();
    ConfigurationsSarter configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    Exception exception = assertThrows(FileNotFoundException.class, () -> {
      configurationsSarter.getOntologyConfigurationFile(configJdd);
    });

    String expectedMessage =
        "Ontology configuration file not found in " + configJdd.getAbsolutePath();
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testGetOntologyConfigurationFile_correctfile() throws FileNotFoundException {
    Path path = Paths.get("test-data/_ontologies/ABC/config00/");
    File configJdd = path.toFile();
    ConfigurationsSarter configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    File ontofile = configurationsSarter.getOntologyConfigurationFile(configJdd);
    assertNotNull(ontofile);
  }

  @Test
  void testGetDatabaseConfigurationFile_nofile() {
    Path path = Paths.get("test-data/configurations/");
    File configJdd = path.toFile();
    ConfigurationsSarter configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    Exception exception = assertThrows(FileNotFoundException.class, () -> {
      configurationsSarter.getDatabaseConfigurationFile(configJdd);
    });

    String expectedMessage =
        "Database configuration file not found in " + configJdd.getAbsolutePath();
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void testGetDatabaseConfigurationFile() throws FileNotFoundException {
    Path path = Paths.get("test-data/_ontologies/ABC/config00/");
    File configJdd = path.toFile();
    ConfigurationsSarter configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    File ontofile = configurationsSarter.getDatabaseConfigurationFile(configJdd);
    assertNotNull(ontofile);
  }

  @Test
  void testGetDatabaseConfiguration_defaultConfig() throws FileNotFoundException {
    Path path = Paths.get("test-data/configurations/");
    File configJdd = path.toFile();
    ConfigurationsSarter configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    DatabaseConfiguration databaseConfig =
        configurationsSarter.startDatabaseConfiguration(configJdd);

    DatabaseConfiguration expected = ConfigurationLoader.loadDefaultDatabaseConfiguration();
    assertThat(databaseConfig).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void testGetDatabaseConfiguration() throws FileNotFoundException {
    Path path = Paths.get("test-data/_ontologies/ABC/config00/");
    File configJdd = path.toFile();
    ConfigurationsSarter configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    DatabaseConfiguration databaseConfig =
        configurationsSarter.startDatabaseConfiguration(configJdd);

    assertNotNull(databaseConfig);
  }
}
