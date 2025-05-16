package ca.griis.ontorela.unit.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.core.ConfigurationsSarter;
import ca.griis.ontorela.core.ExecutionDescriptor;
import ca.griis.ontorela.core.OntologyManager;
import ca.griis.ontorela.core.OntorelManager;
import ca.griis.ontorela.exception.OntorelCreationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.*;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * Test unitaire pour OntoRel manager
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
public class OntorelManagerTest {
  private Descriptor msg = new Descriptor();
  private Date date = new Date();
  private String dateString = new SimpleDateFormat("yyyyMMdd-HHmm").format(date);
  private ExecutionDescriptor executionDescriptor = new ExecutionDescriptor(msg, dateString);
  private OntorelManager ontorelManager;
  private String outputPath;

  private static final String jdd01 = "build/test-results/service/validJdd";

  @BeforeAll
  public static void prepareTestDir() {

    Path sourceDirectory = Paths.get("test-data/service");
    Path targetDirectory = Paths.get("build/test-results/service");

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

  @BeforeEach
  void setUp() throws OWLOntologyCreationException {
    ConfigurationsSarter configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    DatabaseConfiguration databaseConfiguration = configurationsSarter.startDatabaseConfiguration(
        new File(jdd01 + "/ABC/config00"));
    File ontologyConfigFile =
        new File(jdd01 + "/ABC/config00/ABC-ontology_config.yml");
    outputPath = "build/test-results/output";

    OntologyManager ontologyManager =
        new OntologyManager(ontologyConfigFile, outputPath, executionDescriptor);
    ontologyManager.build();
    ontorelManager = new OntorelManager(databaseConfiguration, outputPath, executionDescriptor,
        ontologyManager);

  }

  @AfterEach
  void cleanUp() {

    Path directory = Paths.get(outputPath = "build/test-results/output/20250129-1200");

    if (Files.exists(directory)) {
      try {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
              throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
          }
        });
      } catch (IOException e) {
        System.err.println("impossible de supprimer le dossier" + e.getMessage());
      }
    }
  }

  @Test
  @DisplayName("Test de la construction de l'ontorel")
  void testBuildOntorel() throws OntorelCreationException, IOException {
    ontorelManager.buildOntorel();
    assertNotNull(ontorelManager.getOntoRel(), "L'ontorel n'a pas été construit.");

    ontorelManager.generateDatabaseScripts();
    Path scriptsFolder = Paths.get(outputPath + "/" + dateString + "/DatabaseScripts/");
    assertTrue(Files.exists(scriptsFolder), "Le dossier des scripts SQL doit être créé");

  }
}
