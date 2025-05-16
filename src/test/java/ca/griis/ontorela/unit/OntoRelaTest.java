package ca.griis.ontorela.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.griis.ontorela.Ontorela;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.service.ServiceManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * Test unitaire de la classe ontorela
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
@ExtendWith(MockitoExtension.class)
public class OntoRelaTest {
  private final PrintStream out = System.out;
  private final PrintStream err = System.err;
  private ByteArrayOutputStream errStream;


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
    errStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));
  }

  @AfterEach
  void fin() {
    System.setOut(out);
    System.setErr(err);
  }


  @Test
  void execute_test() throws OWLOntologyCreationException, OntorelCreationException, IOException,
      OWLOntologyStorageException {

    String[] args = {"generate-all", jdd01 + "/ABC/config00"};
    try (MockedConstruction<ServiceManager> mocked = mockConstruction(ServiceManager.class,
        (mock, context) -> {
          doNothing().when(mock).execute(anyString(), any());
        })) {

      Ontorela.main(args);
      ServiceManager mockManager = mocked.constructed().get(0);

      verify(mockManager, times(1)).execute("generate-all", args);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void missingArgument_test() throws OWLOntologyCreationException, OntorelCreationException,
      IOException, OWLOntologyStorageException {

    String[] args = {"invalid"};
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      Ontorela.main(args);
    });
    assertTrue(exception.getMessage().contains("Missing argument"));
  }

  @Test
  void validArgument_test() throws OWLOntologyCreationException, OntorelCreationException,
      IOException, OWLOntologyStorageException {
    String[] args = {"generate-all", jdd01 + "/ABC/config00"};
    try {
      Ontorela.main(args);
    } catch (Exception e) {
      fail("Exception should not have been thrown for valid arguments.");
    }
    assertTrue(errStream.toString().isEmpty());
  }

  @Test
  void exception_test() throws OWLOntologyCreationException, OntorelCreationException, IOException,
      OWLOntologyStorageException {
    String[] args = {"generate-all", jdd01 + "/ABC/config00"};
    try (MockedConstruction<ServiceManager> mocked = mockConstruction(ServiceManager.class,
        (mock, context) -> {
          doThrow(new RuntimeException("err")).when(mock).execute(anyString(), any());
        })) {
      Exception exception = assertThrows(Exception.class, () -> Ontorela.main(args));
      assertEquals("Error when execute service", exception.getMessage());
    }
  }
}
