package ca.griis.ontorela.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.ontorela.service.ServiceManager;
import ca.griis.ontorela.util.jdd.ConstructeurJdd;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * test d'intégration execution des scripts
 *
 * <h3>Historique</h3>
 * <p>
 * 2025-04-01 [AS] - Implémentation initiale<br>
 * </p>
 *
 * <h3>Tâches</h3>
 * S.O.
 *
 * @author AS
 * @since
 */

public class IntegrationTest {

  private static final String jdd01 = "build/test-results/integration";

  @BeforeAll
  public static void prepareTestDir() {

    Path sourceDirectory = Paths.get("test-data/postgresIntegration");
    Path targetDirectory = Paths.get("build/test-results/integration");

    try {
      Files.walk(sourceDirectory).forEach(sourcePath -> {
        try {
          Path targetPath = targetDirectory.resolve(sourceDirectory.relativize(sourcePath));
          if (Files.isDirectory(sourcePath)) {
            if (!Files.exists(targetPath)) {
              Files.createDirectory(targetPath);
            }
          } else {
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
          }
        } catch (IOException e) {
          System.err.println("Erreur lors de la creation de la dir de result : " + sourcePath
              + " -> " + e.getMessage());
        }
      });
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }


  @Test
  @DisplayName("Test d'intégration -Exécution des scripts générés ")
  public void IT_ex_scripts() throws Exception {
    // tester avec MONDIAL, HDRN et ABC

    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      System.out.println(folder.getAbsolutePath());
      for (File config : Objects.requireNonNull(folder.listFiles())) {
        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        manager.execute("generate-ontorel",
            new String[] {"--repoPath", config.getAbsolutePath()});
        System.out.println("test des scripts généré par " + config.getAbsolutePath());

        Path pathoutput = Paths.get(config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/");
        File outJdd = pathoutput.toFile();

        assertNotNull(outJdd, "Le répertoire est null. ");
        File[] files = outJdd.listFiles();
        assertNotNull(files, "La liste des fichiers est null.");
        assertTrue(files.length > 0, "Le répertoire est vide.");

        for (File jdd : Objects.requireNonNull(outJdd.listFiles())) {
          if (jdd.getName().equals("DatabaseScripts")) {
            File[] dbScripts = jdd.listFiles();
            if (dbScripts == null)
              continue;

            Arrays.sort(dbScripts, Comparator.comparing(File::getName));
            assertDoesNotThrow(() -> {
              List<String> failedScripts = new ArrayList<>();
              ContainerUtil.postgresContainer.start();
              String jdbcUrl = ContainerUtil.postgresContainer.getJdbcUrl();
              String username = ContainerUtil.postgresContainer.getUsername();
              String password = ContainerUtil.postgresContainer.getPassword();

              try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                  Statement stmt = connection.createStatement()) {

                for (File script : dbScripts) {
                  if (script.getName().endsWith(".sql")
                      && !script.getName().contains("OntoRelCat")) {
                    String sql = new String(Files.readAllBytes(script.toPath()));
                    try {
                      stmt.execute(sql);
                      System.out.println("Executed: " + script.getName());
                    } catch (SQLException e) {
                      System.err.println("Erreur lors de l'exécution de : " + script.getName());
                      failedScripts.add(script.getName());
                      e.printStackTrace();

                    }
                  }
                }
              } finally {
                ContainerUtil.postgresContainer.stop();
              }
              if (!failedScripts.isEmpty()) {
                System.err.println("Fichiers non exécutés avec succès :");
                for (String failed : failedScripts) {
                  System.err.println(" - " + failed);
                }
              }
              assertTrue(failedScripts.isEmpty(),
                  "Certains scripts SQL ont échoué");

            });
          }
        }
      }
    }
  }
}

// @Test
// @DisplayName("Test d'intégration -Exécution des scripts générés ")
// public void IT_ex_scripts() throws Exception {
// // tester avec MONDIAL, HDRN et ABC
//
// for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
// System.out.println(folder.getAbsolutePath());
// for (File config : folder.listFiles()) {
// ServiceManager manager = new ServiceManager(config.getAbsolutePath());
// manager.execute("generate-ontorel",
// new String[] {"--repoPath", config.getAbsolutePath()});
//
// Path pathoutput = Paths.get(config.getAbsolutePath() + "/"
// + manager.getExecutionDescriptor().getDateString() + "/");
// File outJdd = pathoutput.toFile();
//
// assertNotNull(outJdd, "Le répertoire est null. ");
// File[] files = outJdd.listFiles();
// assertNotNull(files, "La liste des fichiers est null.");
// assertTrue(files.length > 0, "Le répertoire est vide.");
//
// for (File jdd : Objects.requireNonNull(outJdd.listFiles())) {
// if (jdd.getName().equals("DatabaseScripts")) {
// File[] dbScripts = jdd.listFiles();
// if (dbScripts == null)
// continue;
//
// Arrays.sort(dbScripts, Comparator.comparing(File::getName));
// assertDoesNotThrow(() -> {
// List<String> failedScripts = new ArrayList<>();
// ContainerUtil.postgresContainer.start();
// String jdbcUrl = ContainerUtil.postgresContainer.getJdbcUrl();
// String username = ContainerUtil.postgresContainer.getUsername();
// String password = ContainerUtil.postgresContainer.getPassword();
//
// try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
// Statement stmt = connection.createStatement()) {
//
// for (File script : dbScripts) {
// if (script.getName().endsWith(".sql")&& !script.getName().contains("OntoRelCat")) {
// String sql = new String(Files.readAllBytes(script.toPath()));
// try {
// stmt.execute(sql);
// System.out.println("Exécuté : " + script.getName());
// } catch (SQLException e) {
// System.err.println("Échec exécution : " + script.getName());
// failedScripts.add(script.getName());
// e.printStackTrace();
// }
// }
// }
// } finally {
// ContainerUtil.postgresContainer.stop();
// }
// if (!failedScripts.isEmpty()) {
// System.err.println("Fichiers non exécutés avec succès :");
// for (String failed : failedScripts) {
// System.err.println(" - " + failed);
// }
// }
// assertTrue(failedScripts.isEmpty(),
// "Certains scripts SQL ont échoué");
//
// });
// }
// }
// }
// }
// }
// }
