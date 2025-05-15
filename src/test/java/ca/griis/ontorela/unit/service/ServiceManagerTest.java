package ca.griis.ontorela.unit.service;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.ontorela.service.ServiceManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire du service manager
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
public class ServiceManagerTest {

  private ServiceManager manager;
  private final String jdd02 = "build/test-results/service/validJdd/ABC/config00";

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

  @Test
  void testGetDatabaseConfiguration() throws FileNotFoundException {
    manager = new ServiceManager(jdd02);
    assertNotNull(manager.getDatabaseConfiguration(),
        "La configuration de la base de données ne doit pas être null");
  }

  @Test
  void testGetExecutionDescriptor() throws FileNotFoundException {
    manager = new ServiceManager(jdd02);
    assertNotNull(manager.getExecutionDescriptor(),
        "Le Descriptor d'exécution ne doit pas être null");
  }

  @Test
  void testExecuteBuildOntorela() throws Exception {
    manager = new ServiceManager(jdd02);

    manager.execute("generate-all",
        new String[] {"generate-all", jdd02});

    assertNotNull(manager.getOntologyManager().getOntoBuilder());
    assertNotNull(manager.getOntologyManager().getOntoFiltred());
    assertNotNull(manager.getOntologyManager().getNormalizedGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelDic());
    assertNotNull(manager.getGraphService());
    assertNotNull(manager.getReportService());

    Path scriptsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/DatabaseScripts/");
    assertTrue(Files.exists(scriptsFolder), "Le dossier des scripts SQL doit être créé");

    Path graphsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/");
    assertTrue(Files.exists(graphsFolder), "Le dossier des graphes doit être créé");

    Path anomaliesFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/");
    assertTrue(Files.exists(anomaliesFolder.resolve("OntologyAnomalies.txt")),
        "Le fichier des anomalies d'ontologie doit être généré");
  }

  @BeforeEach
  void cleanUp() throws FileNotFoundException {
    manager = new ServiceManager(jdd02);
    String outputDir = jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString();
    Path directory = Paths.get(outputDir);

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
  void testExecutegenerateOntorel() throws Exception {

    manager.execute("generate-ontorel",
        new String[] {"generate-ontorel", jdd02});

    assertNotNull(manager.getOntologyManager().getOntoBuilder());
    assertNotNull(manager.getOntologyManager().getOntoFiltred());
    assertNotNull(manager.getOntologyManager().getNormalizedGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelDic());
    assertNull(manager.getGraphService());
    assertNull(manager.getReportService());

    Path scriptsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/DatabaseScripts/");
    assertTrue(Files.exists(scriptsFolder), "Le dossier des scripts SQL doit être créé");

    Path graphsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/");
    assertFalse(Files.exists(graphsFolder), "Le dossier des graphes doit être créé");

    Path anomaliesFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/");
    assertFalse(Files.exists(anomaliesFolder.resolve("OntologyAnomalies.txt")),
        "Le fichier des anomalies d'ontologie doit être généré");
  }

  @Test
  void testExecuteGenerateInitialGraph() throws Exception {

    manager.execute("generate-graph",
        new String[] {"generate-graph --type initial_onto", jdd02 + "/"});

    assertNotNull(manager.getOntologyManager().getOntoBuilder());
    assertNotNull(manager.getOntologyManager().getOntoFiltred());
    assertNotNull(manager.getOntologyManager().getNormalizedGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelDic());
    assertNotNull(manager.getGraphService());
    assertNull(manager.getReportService());

    Path scriptsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/DatabaseScripts/");
    assertFalse(Files.exists(scriptsFolder), "Le dossier des scripts SQL ne doit pas être créé");

    Path graphsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/");
    assertTrue(Files.exists(graphsFolder), "Le dossier des graphes doit être créé");
    Path graphsFile = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/InitialOntoGraph.dot");
    assertTrue(Files.exists(graphsFile), "Le fichier  doit être créé");

  }

  @Test
  void testExecuteGenerateNormalizedGraph() throws Exception {

    manager.execute("generate-graph",
        new String[] {"generate-graph --type normalized_onto", jdd02 + "/"});

    assertNotNull(manager.getOntologyManager().getOntoBuilder());
    assertNotNull(manager.getOntologyManager().getOntoFiltred());
    assertNotNull(manager.getOntologyManager().getNormalizedGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelDic());
    assertNotNull(manager.getGraphService());
    assertNull(manager.getReportService());

    Path scriptsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/DatabaseScripts/");
    assertFalse(Files.exists(scriptsFolder), "Le dossier des scripts SQL ne doit pas être créé");

    Path graphsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/");
    assertTrue(Files.exists(graphsFolder), "Le dossier des graphes doit être créé");
    Path graphsFile = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/OntoGraph.dot");
    assertTrue(Files.exists(graphsFile), "Le fichier  doit être créé");
  }

  @Test
  void testExecuteGenerateDbGraph() throws Exception {

    manager.execute("generate-graph",
        new String[] {"generate-graph --type db", jdd02 + "/"});

    assertNotNull(manager.getOntologyManager().getOntoBuilder());
    assertNotNull(manager.getOntologyManager().getOntoFiltred());
    assertNotNull(manager.getOntologyManager().getNormalizedGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelDic());
    assertNotNull(manager.getGraphService());
    assertNull(manager.getReportService());

    Path scriptsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/DatabaseScripts/");
    assertFalse(Files.exists(scriptsFolder), "Le dossier des scripts SQL ne doit pas être créé");

    Path graphsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/");
    assertTrue(Files.exists(graphsFolder), "Le dossier des graphes doit être créé");
    Path graphsFile = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/RelGraph.dot");
    assertTrue(Files.exists(graphsFile), "Le fichier  doit être créé");
  }

  @Test
  void testExecuteGenerateOntorelGraph() throws Exception {

    manager.execute("generate-graph",
        new String[] {"generate-graph --type ontorel", jdd02 + "/"});

    assertNotNull(manager.getOntologyManager().getOntoBuilder());
    assertNotNull(manager.getOntologyManager().getOntoFiltred());
    assertNotNull(manager.getOntologyManager().getNormalizedGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelDic());
    assertNotNull(manager.getGraphService());
    assertNull(manager.getReportService());

    Path scriptsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/DatabaseScripts/");
    assertFalse(Files.exists(scriptsFolder), "Le dossier des scripts SQL ne doit pas être créé");

    Path graphsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/");
    assertTrue(Files.exists(graphsFolder), "Le dossier des graphes doit être créé");
    Path graphsFile = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/OntoRelGraph.dot");
    assertTrue(Files.exists(graphsFile), "Le fichier  doit être créé");
  }

  @Test
  void testExecuteGenerateAllGraph() throws Exception {

    manager.execute("generate-graph",
        new String[] {"generate-graph ", jdd02 + "/"});

    assertNotNull(manager.getOntologyManager().getOntoBuilder());
    assertNotNull(manager.getOntologyManager().getOntoFiltred());
    assertNotNull(manager.getOntologyManager().getNormalizedGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelDic());
    assertNotNull(manager.getGraphService());
    assertNull(manager.getReportService());

    Path scriptsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/DatabaseScripts/");
    assertFalse(Files.exists(scriptsFolder), "Le dossier des scripts SQL ne doit pas être créé");

    Path graphsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/");
    assertTrue(Files.exists(graphsFolder), "Le dossier des graphes doit être créé");
  }

  @Test
  void testExecuteGenerateReports() throws Exception {

    manager.execute("generate-anomalies-reports",
        new String[] {"generate-anomalies-reports", jdd02});

    assertNotNull(manager.getOntologyManager().getOntoBuilder());
    assertNotNull(manager.getOntologyManager().getOntoFiltred());
    assertNotNull(manager.getOntologyManager().getNormalizedGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelGraph());
    assertNotNull(manager.getOntorelManager().getOntoRel().getOntoRelDic());
    assertNull(manager.getGraphService());
    assertNotNull(manager.getReportService());

    Path scriptsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/DatabaseScripts/");
    assertFalse(Files.exists(scriptsFolder), "Le dossier des scripts SQL ne doit pas être créé");

    Path graphsFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/Graphs/");
    assertFalse(Files.exists(graphsFolder), "Le dossier des graphes ne doit être pas créé");

    Path anomaliesFolder = Paths.get(jdd02 + "/"
        + manager.getExecutionDescriptor().getDateString() + "/");
    Path anomaliesFile = anomaliesFolder.resolve("OntologyAnomalies.txt");
    assertTrue(Files.size(anomaliesFile) > 0, "Le fichier des anomalies ne doit pas être vide");

  }

  @Test
  void testGenerateDatabaseScripts() throws Exception {
    manager.execute("generate-ontorel", new String[] {"generate-ontorel", jdd02});

    Path scriptsFolder = Paths
        .get(jdd02 + "/" + manager.getExecutionDescriptor().getDateString() + "/DatabaseScripts/");
    assertTrue(Files.exists(scriptsFolder), "Le dossier des scripts SQL doit être créé");
    assertTrue(Files.list(scriptsFolder).findAny().isPresent(),
        "Le dossier des scripts SQL ne doit pas être vide");
  }

}
