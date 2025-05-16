package ca.griis.ontorela.unit.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.core.ExecutionDescriptor;
import ca.griis.ontorela.core.OntologyManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * Test unitaire pour contruire l'ontologie initial et filtré pour les services.
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
public class OntologyManagerTest {
  @Mock
  private ExecutionDescriptor executionDescriptor;
  private String outputPath;
  private OntologyManager ontologyManager;

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
  void setUp() {
    MockitoAnnotations.openMocks(this);
    File ontologyConfigFile =
        new File(jdd01 + "/ABC/config00/ABC-ontology_config.yml");
    outputPath = "build/test-results/output";

    when(executionDescriptor.getDateString()).thenReturn("20250129-1200");
    when(executionDescriptor.getMsg()).thenReturn(new Descriptor());

    ontologyManager = new OntologyManager(ontologyConfigFile, outputPath, executionDescriptor);
  }

  @Test
  @DisplayName("Test de la construction de l'ontologie")
  void testBuildOntology() throws OWLOntologyCreationException {
    ontologyManager.build();

    assertNotNull(ontologyManager.getOntoBuilder(), "L'ontologie initiale n'a pas été construite.");
    assertNotNull(ontologyManager.getOntoFiltred(), "L'ontologie filtrée n'a pas été générée.");
    assertNotNull(ontologyManager.getNormalizedGraph(), "Le graphe normalisé n'a pas été créé.");
  }

  @Test
  @DisplayName("Test de la génération des fichiers normalisés")
  void testGenerateNormalizedFiles() throws OWLOntologyCreationException {

    ontologyManager.build();
    assertNotNull(ontologyManager.getOntoBuilder(), "L'ontologie initiale n'a pas été construite.");
    assertNotNull(ontologyManager.getOntoFiltred(), "L'ontologie filtrée n'a pas été générée.");

    ontologyManager.generateNormalizedFiles();

    File expectedNomalizedFile = new File(outputPath + "/20250129-1200/ABC_normalized.ttl");
    File expectedFile = new File(outputPath + "/20250129-1200/ABC_normalized.txt");

    assertTrue(expectedNomalizedFile.exists() || expectedFile.exists(),
        "Les fichiers normalisés ne sont pas générés.");
  }

  @Test
  @DisplayName("Test de l'échec de la construction de l'ontologie")
  void testBuildOntologyFailure() throws OWLOntologyCreationException {

    Exception exception = assertThrows(OWLOntologyCreationException.class, () -> {
      OntologyManager manager =
          new OntologyManager(new File("invalid-path"), outputPath, executionDescriptor);
      manager.build();
    }, "Une exception est supposé pour le type de args");
    assertTrue(exception.toString().contains("OWLOntologyCreationException"));

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
}
