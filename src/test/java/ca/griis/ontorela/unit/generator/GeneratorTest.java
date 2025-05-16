package ca.griis.ontorela.unit.generator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.*;

import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.generator.AbstactSqlGenerator;
import ca.griis.ontorela.generator.Generator;
import ca.griis.ontorela.mrel.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jgrapht.graph.DirectedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test unitaire de générateur sql
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

public class GeneratorTest {
  @Mock
  private Database database;

  @Mock
  private Schema baseSchema;
  @Mock
  private DatabaseConfiguration dbConfig;

  AbstactSqlGenerator sqlGenerator;
  private Generator generator;

  @Mock
  private Database mockDatabase;

  @Mock
  private Schema mockSchema;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    sqlGenerator = mock(AbstactSqlGenerator.class, CALLS_REAL_METHODS);
    when(sqlGenerator.getDatabase()).thenReturn(database);
    when(database.getBaseSchema()).thenReturn(baseSchema);
    when(baseSchema.getName()).thenReturn("ABC");
    when(sqlGenerator.getDatabase().getDbGraph())
        .thenReturn(new DirectedMultigraph<>(ForeignKey.class));
    MockitoAnnotations.openMocks(this);
    when(mockDatabase.getBaseSchema()).thenReturn(mockSchema);
    when(mockSchema.getName()).thenReturn("test_schema");

    generator = new Generator(mockDatabase) {
      @Override
      protected String generateCreateDomainStatements() {
        return null;
      }

      @Override
      protected String generateDropDomainStatements() {
        return null;
      }

      @Override
      protected String generateCreateTableStatements() {
        return null;
      }

      @Override
      protected String generateCreateFkStatements() {
        return null;
      }

      @Override
      protected String generateDropTableStatements() {
        return null;
      }

      @Override
      protected String generateDropSchemaStatements() {
        return null;
      }

      @Override
      protected String generateIriViews(String schemaView) {
        return null;
      }

      @Override
      protected String generateCheckMembership(MembershipConstraint constraint) {
        return null;
      }
    };

  }

  @Test
  void testCreateFile() throws IOException {
    File file = generator.createFile("build/test-results/generator/", "testFile", ".txt");
    assertNotNull(file);
    assertEquals("build/test-results/generator/testFile.txt", file.getPath());
  }

  @Test
  void testCreateSqlFile() {
    File file = generator.createSqLFile("build/test-results/generator", "001", "description", "v1");
    assertNotNull(file);
    assertTrue(file.getName().contains("test_schema_description_v1"));
    assertTrue(file.getName().endsWith(".sql"));
  }

  @Test
  void testCreateDocFile() {
    File file = generator.createDocFile("build/test-results/generator", "documentation", "v1");
    assertNotNull(file);
    assertTrue(file.getName().contains("test_schema_documentation_v1"));
    assertTrue(file.getName().endsWith(".txt"));
  }

  @Test
  void testCopyFile() throws IOException {
    File source = File.createTempFile("source", ".txt");
    File destination = File.createTempFile("destination", ".txt");

    try (FileWriter writer = new FileWriter(source)) {
      writer.write("test de copy");
    }

    File copiedFile = generator.copyFile(source, destination);

    assertNotNull(copiedFile);
    assertEquals(destination.getPath(), copiedFile.getPath());

    try (BufferedReader reader = new BufferedReader(new FileReader(destination))) {
      assertEquals("test de copy", reader.readLine());
    }
  }

  @Test
  void testGenerateCreateDomainStatements()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = AbstactSqlGenerator.class.getDeclaredMethod("generateCreateDomainStatements");
    method.setAccessible(true);
    String result = (String) method.invoke(sqlGenerator);
    assertNotNull(result);
    assertEquals("", result);

  }

  @Test
  void testGenerateDropDomainStatements()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = AbstactSqlGenerator.class.getDeclaredMethod("generateDropDomainStatements");
    method.setAccessible(true);
    String result = (String) method.invoke(sqlGenerator);
    assertNotNull(result);
    assertEquals("", result);

  }

  @Test
  void testGenerateDropTableStatements()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = AbstactSqlGenerator.class.getDeclaredMethod("generateDropTableStatements");
    method.setAccessible(true);
    String result = (String) method.invoke(sqlGenerator);
    assertNotNull(result);
    assertEquals("", result);

  }

  @Test
  void testGenerateDropSchemaStatements()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = AbstactSqlGenerator.class.getDeclaredMethod("generateDropSchemaStatements");
    method.setAccessible(true);
    String result = (String) method.invoke(sqlGenerator);
    assertNotNull(result);
  }

  @Test
  void testGenerateDeleteTableStatements()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = AbstactSqlGenerator.class.getDeclaredMethod("generateDeleteTableStatements");
    method.setAccessible(true);
    String result = (String) method.invoke(sqlGenerator);
    assertNotNull(result);
  }

  @Test
  void testGenerateIriViews()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = AbstactSqlGenerator.class.getDeclaredMethod("generateIriViews", String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(sqlGenerator, "ABC");
    assertNotNull(result);
  }

  @Test
  void testGenerateLAbelViews()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = AbstactSqlGenerator.class.getDeclaredMethod("generateLabelViews", String.class,
        int.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(sqlGenerator, "ABC", 20, "en");
    assertNotNull(result);
  }

  @Test
  void testGenerateOntoViews()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = AbstactSqlGenerator.class.getDeclaredMethod("generateOntoViews", String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(sqlGenerator, "ABC");
    assertNotNull(result);

  }

  @Test
  void testGenerateCheckParticipation()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = AbstactSqlGenerator.class.getDeclaredMethod("generateCheckParticipation",
        ParticipationConstraint.class);
    method.setAccessible(true);
    ParticipationConstraint constraint = mock(ParticipationConstraint.class);
    String result = (String) method.invoke(sqlGenerator, constraint);
    assertNotNull(result);
  }

}
