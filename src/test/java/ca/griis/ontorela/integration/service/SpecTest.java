package ca.griis.ontorela.integration.service;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.monto.api.OntoAxiomI;
import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.model.OntoAxiomClassInheritance;
import ca.griis.monto.model.OntoClass;
import ca.griis.monto.model.OntoIri;
import ca.griis.monto.model.Ontology;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.core.OntorelManager;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.service.ServiceManager;
import ca.griis.ontorela.util.jdd.ConstructeurJdd;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * essais pour cli
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
public class SpecTest {


  private static final String jdd01 = "build/test-results/service/validJdd";
  private static final String jdd02 = "build/test-results/service/invalidJdd";

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
  @DisplayName("E1 - CT_001: le fichier de configuration sql et ontologique sont correct.")
  public void essai1() throws Exception {
    // tester avec MONDIAL, HDRN et PDRO-P3

    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      for (File config : folder.listFiles()) {
        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        manager.execute("generate-ontorel",
            new String[] {"--repoPath", config.getAbsolutePath()});

        DatabaseConfiguration dbConfig = manager.getDatabaseConfiguration();

        Path pathoutput = Paths.get(config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/");
        File outJdd = pathoutput.toFile();

        assertNotNull(outJdd, "Le répertoire est null. ");
        File[] files = outJdd.listFiles();
        assertNotNull(files, "La liste des fichiers est null.");
        assertTrue(files.length > 0, "Le répertoire est vide.");
        // test pour les fichiers ontologiques
        assertTrue(Arrays.stream(Objects.requireNonNull(outJdd.list()))
            .anyMatch(fileName -> fileName.contains("_normalized.ttl")),
            "Le fichier attendu n'existe pas ");
        assertTrue(Arrays.stream(Objects.requireNonNull(outJdd.list()))
            .anyMatch(fileName -> fileName.contains("_normalized.txt")),
            "Le fichier attendu n'existe pas ");
        // vérifier si les fichier de scripts sint correcte
        for (File jdd : Objects.requireNonNull(outJdd.listFiles())) {

          if (jdd.getName().equals("DatabaseScripts")) {
            String[] expectedFiles = {
                "100-<nom_ontology>_cre-table_<version>_<date>.sql",
                "110-<nom_ontology>_cre-participationCheck-fct_<version>_<date>.sql",
                "120-<nom_ontology>_cre-unionAxiomCheck-fct_<version>_<date>.sql",
                "130-<nom_ontology>_cre-membershipCheck-fct_<version>_<date>.sql",
                "200-<nom_ontology>_cre-view-iri_<version>_<date>.sql",
                "210-<nom_ontology>_cre-view-en_<version>_<date>.sql",
                "210-<nom_ontology>_cre-view-fr_<version>_<date>.sql",
                "800-<nom_ontology>_del-table_<version>_<date>.sql",
                "920-<nom_ontology>_drp-table_<version>_<date>.sql",
                "1003-<nom_ontology>_OntoRelCat_ins_<version>_<date>.sql"
            };

            // Vérification du nombre de fichiers
            assertEquals(expectedFiles.length, jdd.list().length);
            for (String expectedFile : expectedFiles) {
              String resolvedFileName = expectedFile
                  .replace("<nom_ontology>", dbConfig.getSchemaName())
                  .replace("<version>", "v0")
                  .replace("<date>", manager.getExecutionDescriptor().getDateString());
              boolean fileExists = Arrays.stream(jdd.list())
                  .anyMatch(fileName -> fileName.contains(resolvedFileName));
              assertTrue(fileExists, "Le fichier attendu n'existe pas : " + resolvedFileName);

              if (fileExists) {
                File file = new File(jdd.getPath() + "/" + resolvedFileName);
                assertTrue(file.length() > 0, "Le fichier " + resolvedFileName + " est vide.");
              }
            }
          }
        }
      }
    }
  }

  @Test
  @DisplayName("E2 - CT_002: les fichiers de configuration sql et ontologique sont introuvable.")
  public void essai2() throws Exception {
    // Dans le cas ou le fichier de configuration ontologique existe, et celui de base de donnée
    // n'est pas fournis,
    // un fichier de configuration database par défaut (resources/default_database_config.yaml)

    for (File jdd : ConstructeurJdd.creationJddParRepo(jdd02 + "/essai02")) {
      Exception exception = assertThrows(FileNotFoundException.class, () -> {

        ServiceManager manager = new ServiceManager(jdd.getAbsolutePath());
        manager.execute("generate-ontorel",
            new String[] {"--repoPath", jdd.getAbsolutePath()});

      }, "Une exception est supposé pour le " + jdd.getName());
      assertTrue(exception.toString().contains("FileNotFoundException"));
    }
  }

  @Test
  @DisplayName("E3 - CT_003A : Echec de la génération de l'ontologie")
  public void essai3A() throws Exception {
    // case 1 le fichier de l'ontologie n'existe pas dans le dossier :
    // test-data/essais/essai03/case01
    // case 2 url de l'ontologie né,est pas bon : test-data/essais/essai03/case02
    // case 3 erreur dans l'IRI de l'ontologie (Ontology IRI non existant dans le fichier de
    // l'ontologie):test-data/essais/essai03/case03
    for (File jdd : ConstructeurJdd.creationJddParRepo(jdd02 + "/essai03")) {
      Exception exception = assertThrows(OWLOntologyCreationException.class, () -> {
        ServiceManager manager = new ServiceManager(jdd.getAbsolutePath());
        manager.execute("generate-ontorel",
            new String[] {"--repoPath", jdd.getAbsolutePath()});
      }, "Une exception est supposé pour le " + jdd.getName());
      assertTrue(exception.toString().contains("OWLOntologyCreationException"));
    }
  }

  @Test
  @DisplayName("E4 - CT_003B : Echec de la génération de ontorel- ontologie filtré invalide")
  public void essai4() throws Exception {
    // simuler une entrée invalide pour generateOntorel, plusieur option pour avoir une ontologie
    // filtré non valide existe..
    // c00 isa c01 , co00 n'existe pas dans l'ontologie

    String repoPath = jdd02 + "/essai04";
    File dbConfigFile = new File(repoPath);

    // ontology
    Ontology invalidOntology = new Ontology(new OntoIri("http:/ABC"));
    // classes
    OntoIri c00Iri = new OntoIri("OntoRela/ut/C00");
    OntoClass c00 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c00Iri);
    OntoIri c01Iri = new OntoIri("OntoRela/ut/C01");
    OntoClass c01 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c01Iri);
    // axiome
    OntoAxiomClassInheritance isa0 =
        new OntoAxiomClassInheritance(OntoAxiomI.OntoAxiomOrigin.DECLARED, c00, c01);
    // ajout de classe c00 et axiome isa0 à l'ontologie, la classe c01 ceci cause une erreur lors
    // de la création de table de jointure..
    invalidOntology.addOntoClass(c01);
    invalidOntology.addOntoAxiom(isa0);

    ServiceManager manager = new ServiceManager(dbConfigFile.getAbsolutePath());
    DatabaseConfiguration validDbConfig = manager.getDatabaseConfiguration();
    // changer l'ontologie
    Exception exception = assertThrows(OntorelCreationException.class, () -> {
      manager.execute("generate-ontorel",
          new String[] {"--repoPath", repoPath});
      manager.getOntologyManager().setOntoFiltred(invalidOntology);
      OntorelManager ontorelManager = new OntorelManager(validDbConfig, repoPath,
          manager.getExecutionDescriptor(), manager.getOntologyManager());
      ontorelManager.buildOntorel();
    }, "OntorelCreationException must be thrown ");
    assertTrue(exception.toString().contains("OntorelCreationException"));

  }

  @Test
  @DisplayName("E5 - CT_004 : Vérifier que la construction d’un graphe ontologique-relationnel est correcte.")
  public void essai5() throws Exception {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      assertNotNull(folder.listFiles(), "Le répertoire est null. ");
      for (File config : folder.listFiles()) {

        String[] args = {"--repoPath", config.getAbsolutePath(), "--type", "ontorel"};

        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        manager.execute("generate-graph", args);

        String outputPath = config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/";
        File outJdd = Paths.get(outputPath).toFile();
        for (File jdd : outJdd.listFiles()) {

          if (jdd.getName().equals("Graphs")) {
            String[] expectedFiles = {
                "OntoRelGraph.dot",
            };
            verifFiles(jdd, expectedFiles);
          }
        }
      }
    }
  }

  @Test
  @DisplayName("E6 - CT_005 : Vérifier que la construction d’un graphe de base de donnée à partir d’ontorel est correcte.")
  public void essai6() throws Exception {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      assertNotNull(folder.listFiles(), "Le répertoire est null. ");
      for (File config : folder.listFiles()) {
        String[] args = {"--repoPath", config.getAbsolutePath(), "--type", "db"};

        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        manager.execute("generate-graph", args);

        String outputPath = config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/";
        File outJdd = Paths.get(outputPath).toFile();
        for (File jdd : outJdd.listFiles()) {

          if (jdd.getName().equals("Graphs")) {
            String[] expectedFiles = {
                "RelGraph.dot",
            };
            verifFiles(jdd, expectedFiles);
          }
        }
      }
    }
  }

  @Test
  @DisplayName("E7 - CT_006 : Vérifier que la construction d’un graphe d’ontologie normalisé est correcte.")
  public void essai7() throws Exception {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      assertNotNull(folder.listFiles(), "Le répertoire est null. ");
      for (File config : folder.listFiles()) {
        String[] args = {"--repoPath", config.getAbsolutePath(), "--type", "normalized_onto"};

        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        manager.execute("generate-graph", args);

        String outputPath = config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/";
        File outJdd = Paths.get(outputPath).toFile();
        for (File jdd : outJdd.listFiles()) {

          if (jdd.getName().equals("Graphs")) {
            String[] expectedFiles = {
                "OntoGraph.dot",
            };
            verifFiles(jdd, expectedFiles);
          }
        }
      }
    }
  }

  @Test
  @DisplayName("E8 - CT_007 : Vérifier que la construction d’un graphe d’ontologie initiale est correcte.")
  public void essai8()
      throws OntorelCreationException, IOException, OWLOntologyCreationException {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      assertNotNull(folder.listFiles(), "Le répertoire est null. ");
      for (File config : folder.listFiles()) {
        String[] args = {"--repoPath", config.getAbsolutePath(), "--type", "initial_onto"};

        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        manager.execute("generate-graph", args);

        String outputPath = config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/";
        File outJdd = Paths.get(outputPath).toFile();
        for (File jdd : outJdd.listFiles()) {

          if (jdd.getName().equals("Graphs")) {
            String[] expectedFiles = {
                "InitialOntoGraph.dot",
            };
            verifFiles(jdd, expectedFiles);
          }
        }
      }
    }
  }

  @Test
  @DisplayName("E9 - CT_008 : Vérifier que la construction de tous les graphes à partir d’un ontorel est correcte.")
  public void essai9()
      throws OntorelCreationException, IOException, OWLOntologyCreationException {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      assertNotNull(folder.listFiles(), "Le répertoire est null. ");
      for (File config : folder.listFiles()) {

        String[] args = {"--repoPath", config.getAbsolutePath(), "--type", "all"};
        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        manager.execute("generate-graph", args);

        String outputPath = config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/";
        File outJdd = Paths.get(outputPath).toFile();

        // verifFiles(outJdd, expectedFiles);
        for (File jdd : outJdd.listFiles()) {

          if (jdd.getName().equals("Graphs")) {
            String[] expectedFiles = {
                "InitialOntoGraph.dot",
                "OntoGraph.dot",
                "OntoRelGraph.dot",
                "RelGraph.dot",
            };
            verifFiles(jdd, expectedFiles);
          }
        }
      }
    }
  }

  @Test
  @DisplayName("E10 - CT_009 :Vérifier que lorsque les fichiers de configuration ne sont pas fournis, une exception FileNotFound nous parvient")
  public void essai10() throws OntorelCreationException, FileNotFoundException {


    for (File jdd : ConstructeurJdd.creationJddParRepo(jdd02 + "/essai02")) {

      String[] args = {"--repoPath", jdd.getName(), "--type", "all"};
      Exception exception = assertThrows(FileNotFoundException.class, () -> {

        ServiceManager manager = new ServiceManager(jdd.getAbsolutePath());
        manager.execute("generate-graph", args);
      }, "Une exception est supposé pour le " + jdd.getName());

      assertTrue(exception.toString().contains("FileNotFoundException"));
    }
  }

  @Test
  @DisplayName("E11 - CT_010 : Vérifier l'exception IllegalArgument nous parvient.")
  public void essai11() throws OntorelCreationException, FileNotFoundException {
    Path path = Paths.get(jdd01 + "/HDRN/config00");
    String validRepoPath = path.toString();
    String[] args = {"--repoPath", validRepoPath, "--type", " typenon"};

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {

      ServiceManager manager = new ServiceManager(validRepoPath);
      manager.execute("generate-graph", args);

    }, "Une exception est supposé pour le type de args");

    assertTrue(exception.toString().contains("IllegalArgumentException"));


  }

  @Test
  @DisplayName("E12 - CT_011 :Vérifier que la génération des anomalies de l’ontologie fonctionne.")
  public void essai12()
      throws OntorelCreationException, IOException, OWLOntologyCreationException {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      assertNotNull(folder.listFiles(), "Le répertoire est null. ");
      for (File config : folder.listFiles()) {
        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        String[] args = {"--repoPath", config.getAbsolutePath(), "--type", "ontology"};
        manager.execute("generate-anomalies-reports", args);
        String outputPath = config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/";
        File outJdd = Paths.get(outputPath).toFile();

        String[] expectedFiles = {
            "MOnto-summary.txt",
            "OntologyAnnotationDiagnostics.txt",
            "OntologyAnomalies.txt",
            "OntologyDetails.txt"
        };
        verifFiles(outJdd, expectedFiles);
      }
    }
  }

  @Test
  @DisplayName("E13 - CT_012 :Vérifier que la génération des anomalies de la base de données fonctionne.")
  public void essai13()
      throws OntorelCreationException, IOException, OWLOntologyCreationException {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      assertNotNull(folder.listFiles(), "Le répertoire est null. ");
      for (File config : folder.listFiles()) {
        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        String[] args = {"--repoPath", config.getAbsolutePath(), "--type", "db"};
        manager.execute("generate-anomalies-reports", args);
        String outputPath = config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/";
        File outJdd = Paths.get(outputPath).toFile();

        String[] expectedFiles = {
            "DatabaseIdentifierAnomalies.txt",
            "DatabaseReport.txt",
            "OntoRelA-summary.txt",
            "OntoRelDiagnostics.txt"
        };
        verifFiles(outJdd, expectedFiles);
      }
    }
  }

  @Test
  @DisplayName("E14 - CT_013 :Vérifier que la génération de toutes les anomalies fonctionne.")
  public void essai14()
      throws OntorelCreationException, IOException, OWLOntologyCreationException {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      assertNotNull(folder.listFiles(), "Le répertoire est null. ");
      for (File config : folder.listFiles()) {
        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        String[] args = {"--repoPath", config.getAbsolutePath(), "--type", "all"};
        manager.execute("generate-anomalies-reports", args);

        String outputPath = config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/";
        File outJdd = Paths.get(outputPath).toFile();

        String[] expectedFiles = {
            "DatabaseIdentifierAnomalies.txt",
            "DatabaseReport.txt",
            "OntoRelA-summary.txt",
            "OntoRelDiagnostics.txt",
            "MOnto-summary.txt",
            "OntologyAnnotationDiagnostics.txt",
            "OntologyAnomalies.txt",
            "OntologyDetails.txt"
        };
        verifFiles(outJdd, expectedFiles);
      }
    }
  }

  @Test
  @DisplayName("E14 - CT_013 :Vérifier que la génération de toutes les anomalies fonctionne.")
  public void essai14_()
      throws OntorelCreationException, IOException, OWLOntologyCreationException {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      assertNotNull(folder.listFiles(), "Le répertoire est null. ");
      for (File config : folder.listFiles()) {
        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        String[] args = {"--repoPath", config.getAbsolutePath(), "--type", "all"};
        manager.execute("generate-anomalies-reports", args);

        String outputPath = config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/";
        File outJdd = Paths.get(outputPath).toFile();

        String[] expectedFiles = {
            "DatabaseIdentifierAnomalies.txt",
            "DatabaseReport.txt",
            "OntoRelA-summary.txt",
            "OntoRelDiagnostics.txt",
            "MOnto-summary.txt",
            "OntologyAnnotationDiagnostics.txt",
            "OntologyAnomalies.txt",
            "OntologyDetails.txt"
        };
        verifFiles(outJdd, expectedFiles);
      }
    }
  }

  @Test
  @DisplayName("E15 - CT_014 : Vérifier que lorsque les fichiers de configuration sont non présent, alors une exception FileNotFound est présenté.")
  public void essai15() throws OntorelCreationException, FileNotFoundException {
    String repoPath = jdd02 + "/essai02/case01";

    Exception exception = assertThrows(FileNotFoundException.class, () -> {
      ServiceManager manager = new ServiceManager(repoPath);
      String[] args = {"--repoPath", repoPath, "--type", "all"};
      manager.execute("generate-anomalies-reports", args);

    }, "Une exception est supposé pour le " + repoPath);
    assertTrue(exception.toString().contains("FileNotFoundException"));
  }

  @Test
  @DisplayName("E16 - CT_015 : Vérifier l'exception IllegalArgument nous parvient.")
  public void essai16() throws OntorelCreationException, FileNotFoundException {
    Path path = Paths.get(jdd01 + "/HDRN/config00");
    String validRepoPath = path.toString();

    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      ServiceManager manager = new ServiceManager(validRepoPath);
      String[] args = {"--repoPath", validRepoPath, "--type", "typenon"};
      manager.execute("generate-anomalies-reports", args);

    }, "Une exception est supposé pour le type de args");
    assertTrue(exception.toString().contains("IllegalArgumentException"));
  }

  @Test
  @DisplayName("E17 - CT_016: Vérifier l’exécution complète.")
  public void essai17() throws Exception {
    for (File folder : ConstructeurJdd.creationJddParRepo(jdd01)) {
      for (File config : folder.listFiles()) {
        // System.out.println(config.getAbsolutePath());

        ServiceManager manager = new ServiceManager(config.getAbsolutePath());
        manager.execute("generate-all",
            new String[] {"--repoPath", config.getAbsolutePath()});

        DatabaseConfiguration dbConfig = manager.getDatabaseConfiguration();

        Path pathoutput = Paths.get(config.getAbsolutePath() + "/"
            + manager.getExecutionDescriptor().getDateString() + "/");
        File outJdd = pathoutput.toFile();

        assertNotNull(outJdd, "Le répertoire est null. ");
        File[] files = outJdd.listFiles();
        assertNotNull(files, "La liste des fichiers est null.");
        assertTrue(files.length > 0, "Le répertoire est vide.");
        // test pour les fichiers ontologiques
        assertTrue(Arrays.stream(outJdd.list())
            .anyMatch(fileName -> fileName.contains("_normalized.ttl")),
            "Le fichier attendu n'existe pas ");
        assertTrue(Arrays.stream(outJdd.list())
            .anyMatch(fileName -> fileName.contains("_normalized.txt")),
            "Le fichier attendu n'existe pas ");
        // vérifier si les fichier de scripts sint correcte
        for (File jdd : outJdd.listFiles()) {
          // System.out.println(" " + jdd.getName());

          if (jdd.getName().equals("DatabaseScripts")) {
            String[] expectedFiles = {
                "100-<nom_ontology>_cre-table_<version>_<date>.sql",
                "110-<nom_ontology>_cre-participationCheck-fct_<version>_<date>.sql",
                "120-<nom_ontology>_cre-unionAxiomCheck-fct_<version>_<date>.sql",
                "130-<nom_ontology>_cre-membershipCheck-fct_<version>_<date>.sql",
                "200-<nom_ontology>_cre-view-iri_<version>_<date>.sql",
                "210-<nom_ontology>_cre-view-en_<version>_<date>.sql",
                "210-<nom_ontology>_cre-view-fr_<version>_<date>.sql",
                "800-<nom_ontology>_del-table_<version>_<date>.sql",
                "920-<nom_ontology>_drp-table_<version>_<date>.sql",
                "1003-<nom_ontology>_OntoRelCat_ins_<version>_<date>.sql"
            };

            // Vérification du nombre de fichiers
            assertEquals(expectedFiles.length, jdd.list().length);
            for (String expectedFile : expectedFiles) {
              String resolvedFileName = expectedFile
                  .replace("<nom_ontology>", dbConfig.getSchemaName())
                  .replace("<version>", "v0")
                  .replace("<date>", manager.getExecutionDescriptor().getDateString());
              boolean fileExists = Arrays.stream(jdd.list())
                  .anyMatch(fileName -> fileName.contains(resolvedFileName));
              assertTrue(fileExists, "Le fichier attendu n'existe pas : " + resolvedFileName);

              if (fileExists) {
                File file = new File(jdd.getPath() + "/" + resolvedFileName);
                assertTrue(file.length() > 0, "Le fichier " + resolvedFileName + " est vide.");
              }
            }
          }
          if (jdd.getName().equals("Graphs")) {
            String[] expectedFiles = {
                "InitialOntoGraph.dot",
                "OntoGraph.dot",
                "OntoRelGraph.dot",
                "RelGraph.dot",
            };
            verifFiles(jdd, expectedFiles);
          }
          String[] expectedAnomaliesFiles = {
              "DatabaseIdentifierAnomalies.txt",
              "DatabaseReport.txt",
              "OntoRelA-summary.txt",
              "OntoRelDiagnostics.txt",
              "MOnto-summary.txt",
              "OntologyAnnotationDiagnostics.txt",
              "OntologyAnomalies.txt",
              "OntologyDetails.txt"
          };
          verifFiles(outJdd, expectedAnomaliesFiles);
        }
      }
    }
  }

  private void verifFiles(File outJdd, String[] expectedFiles) {
    for (String fileName : expectedFiles) {
      assertTrue(Arrays.stream(outJdd.list())
          .anyMatch(name -> name.equals(fileName)),
          "Le fichier attendu n'existe pas : " + fileName);

      File file = new File(outJdd.getPath() + "/" + fileName);
      assertTrue(file.length() > 0, "Le fichier " + fileName + " est vide.");
    }
  }
}
