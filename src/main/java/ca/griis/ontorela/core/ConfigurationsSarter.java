package ca.griis.ontorela.core;


import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Responsable de chargement des Configurations d'ontologie et de base de données
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

public class ConfigurationsSarter {
  private final ExecutionDescriptor executionDescriptor;
  private DatabaseConfiguration databaseConfiguration;

  public ConfigurationsSarter(ExecutionDescriptor executionDescriptor) {
    this.executionDescriptor = executionDescriptor;
  }


  /**
   * Obtenir le fichier de configuration de l'ontologie à partir d'un dossier.
   * Le fichier de configuration en YAML.
   *
   * @param dir : a directory.
   * @return le fichier de configuration de l'ontologie.
   * @throws FileNotFoundException Le fichier de configuration de l'ontologie est introuvable.
   */
  public File getOntologyConfigurationFile(File dir) throws FileNotFoundException {
    assert dir.isDirectory() : "Parameter must be a existing directory: "
        + dir.getAbsolutePath();
    List<File> yamlFiles = new ArrayList<>();
    for (File configFile : getYamlConfigurationFiles(dir)) {
      if (configFile.getName().contains("ontology")) {
        yamlFiles.add(configFile);
      }
    }
    if (yamlFiles.isEmpty()) {
      throw new FileNotFoundException(
          "Ontology configuration file not found in " + dir.getAbsolutePath());
    }
    executionDescriptor.getMsg()
        .ajouter("Ontology configuration file : " + yamlFiles.get(0).getAbsolutePath());
    return yamlFiles.get(0);
  }

  public DatabaseConfiguration getDatabaseConfiguration() {
    return this.databaseConfiguration;
  }

  /**
   * Obtenir le fichier de configuration de la base de données à partir d'un dossier.
   * Le fichier de configuration en YAML.
   *
   * @param dir : a directory.
   * @return le fichier de configuration de la base de données
   */
  public DatabaseConfiguration startDatabaseConfiguration(File dir) {
    assert dir.isDirectory() : "Jdd must be a directory " + dir.getAbsolutePath();
    List<File> yamlFiles = new ArrayList<>();
    for (File configFile : getYamlConfigurationFiles(dir)) {
      if (configFile.getName().contains("database")) {
        yamlFiles.add(configFile);
      }
    }
    if (yamlFiles.isEmpty()) {
      executionDescriptor.getMsg().ajouter(
          "Database configuration file not found in " + dir.getAbsolutePath()
              + " default configuration will be used.");
      databaseConfiguration = ConfigurationLoader.loadDefaultDatabaseConfiguration();
    } else {
      databaseConfiguration = ConfigurationLoader.loadDatabaseConfiguration(yamlFiles.get(0));
      executionDescriptor.getMsg()
          .ajouter("Database configuration file : " + yamlFiles.get(0).getAbsolutePath());
    }
    return databaseConfiguration;
  }

  /**
   * Obtenir le fichier de configuration de la base de données à partir d'un dossier.
   * Le fichier de configuration en YAML.
   *
   * @param dir : a directory.
   * @return le fichier de configuration de l'ontologie.
   * @throws FileNotFoundException Fichier de configuration de la base de données est introuvable
   */
  public File getDatabaseConfigurationFile(File dir) throws FileNotFoundException {
    assert dir.isDirectory() : "Parameter must be a existing directory: " + dir.getAbsolutePath();
    List<File> yamlFiles = new ArrayList<>();
    for (File configFile : getYamlConfigurationFiles(dir)) {
      if (configFile.getName().contains("database")) {
        yamlFiles.add(configFile);
      }
    }
    if (yamlFiles.isEmpty()) {
      throw new FileNotFoundException(
          "Database configuration file not found in " + dir.getAbsolutePath());
    }
    return yamlFiles.get(0);
  }

  /**
   * Get Yaml files from a directory.
   *
   * @param jdd : a directory.
   * @return List of Yaml files.
   */
  private List<File> getYamlConfigurationFiles(File jdd) {
    assert jdd.isDirectory() : "The input must be a directory " + jdd.getAbsolutePath();
    //
    if (!jdd.exists()) {
      System.err.println("Ontology configuration file not found in " + jdd.getAbsolutePath());
      throw new IllegalArgumentException("Ontology configuration file not found in");
    }
    //
    List<File> yamlFiles = new ArrayList<>();
    Collections.addAll(yamlFiles,
        Objects.requireNonNull(jdd.listFiles(pathname -> !pathname.isHidden()
            &&
            (pathname.getName()
                .endsWith(".yml")
                || pathname.getName()
                    .endsWith(".yaml")))));
    return yamlFiles;
  }

  /**
   * Get Yaml files from a directory.
   *
   * @param dir : a directory.
   * @return List of Yaml files.
   */
  public String getPrimaryLangue(File dir) {
    DatabaseConfiguration dbConfig = getDatabaseConfiguration();
    if (dbConfig != null && !dbConfig.getLanguages().isEmpty()) {
      return dbConfig.getLanguages().get(0);
    }
    return "en";
  }
}
