package ca.griis.ontorela.unit.generator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.generator.SqlGenerator;
import ca.griis.ontorela.generator.SqlTemplate;
import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.mrel.Schema;
import java.io.File;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.stringtemplate.v4.ST;

/**
 * Test unitaire string template SSQL.
 * 
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
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
 * @since 2018-09-06
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */
public class SqlGeneratorTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static SqlTemplate gen = new SqlTemplate();

  // **************************************************************************
  // Constructeurs
  //
  private SqlGenerator sqlGenerator;

  @Mock
  private Database database;

  @Mock
  private Schema baseSchema;

  @Mock
  private DatabaseConfiguration dbConfig;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(database.getBaseSchema()).thenReturn(baseSchema);
    when(baseSchema.getName()).thenReturn("ABC");
    when(dbConfig.getLanguages()).thenReturn(Arrays.asList("en", "fr"));
    when(dbConfig.getuseIriAsTableId()).thenReturn(false);
    when(dbConfig.getRdbmsName()).thenReturn(Arrays.asList("mssql"));
    sqlGenerator = new SqlGenerator(database, dbConfig);
    assertNotNull(sqlGenerator);
  }

  // **************************************************************************
  // Cas de test
  //


  @Test
  public void test() {
    String schema = gen.genererSchema("1").replaceAll("\\r\\n|\\r|\\n", "");
    assertTrue(schema.equals("CREATE SCHEMA IF NOT EXISTS \"1\";"));
    String schemaDescription =
        gen.genererSchemaDefinition("1", "description").replaceAll("\\r\\n|\\r|\\n", "");
    assertTrue(schemaDescription.equals("COMMENT ON SCHEMA \"1\" IS 'description';"));
    String schemaSuppressionTrue =
        gen.genererSuppressionSchema("1", true).replaceAll("\\r\\n|\\r|\\n", "");
    assertTrue(schemaSuppressionTrue.equals("DROP SCHEMA \"1\" CASCADE;"));
    String schemaSuppressionFalse =
        gen.genererSuppressionSchema("1", false).replaceAll("\\r\\n|\\r|\\n", "");
    assertTrue(schemaSuppressionFalse.equals("DROP SCHEMA \"1\" ;"));
  }

  @Test
  public void testGenererVerificationParticipation() {
    ST participationFctMin = gen.getTemplate().getInstanceOf("verificationParticipationMin");
    assertNotNull(participationFctMin);
    ST participationFctMax = gen.getTemplate().getInstanceOf("verificationParticipationMax");
    assertNotNull(participationFctMax);
  }

  @Test
  public void testCheckUnionAxiom() {
    ST checkUnionAxiom = gen.getTemplate().getInstanceOf("checkUnionAxiom");
    assertNotNull(checkUnionAxiom);
    //
    checkUnionAxiom.add("contrainte_id", "testConstrainte");
    checkUnionAxiom.add("schema_id", "testSchema");
    checkUnionAxiom.add("unionTable_id", "union");
    checkUnionAxiom.addAggr("unionTable_keySet.{id}", "k1");
    int n = 1;
    for (int i = 0; i <= n; i++) {
      ST selectKeys = gen.getTemplate().getInstanceOf("selectKeys");
      selectKeys.add("schema_id", "test");
      selectKeys.add("table_id", "t" + i);
      selectKeys.addAggr("keySet.{id}", "k" + i);
      checkUnionAxiom.addAggr("elementSet.{exp}", selectKeys.render());
      assertNotNull(selectKeys);
    }
  }

  @Test
  public void testGenerateCheckMembership() {
    ST checkMembership = gen.getTemplate().getInstanceOf("checkMembership");
    assertNotNull(checkMembership);
    //
    checkMembership.add("contrainte_id", "testConstrainte");
    checkMembership.add("schema_id", "testSchema");
    checkMembership.add("sourceTable_id", "TS");
    checkMembership.add("sourceAtt", "TS_uid");
    Map<String, String> targetTableMap = new HashMap<>();
    targetTableMap.put("TT0", "TT0_uid");
    targetTableMap.put("TT1", "TT1_uid");
    for (Map.Entry<String, String> e : targetTableMap.entrySet()) {
      checkMembership.addAggr("targetTableMap.{table_id, targetAtt}", e.getKey(), e.getValue());
    }
    System.out.println(checkMembership.render());
  }

  @Test
  void testGetCommentLanguage() {
    assertEquals("en", sqlGenerator.getCommentLanguage());
  }


  @Test
  void testGenerateCreateTableDdL() {
    File file = sqlGenerator.generateCreateTableDdL("build/test-results/generator", "v1", "author");
    assertNotNull(file);
    assertTrue(file.getPath().contains("cre-table"));
  }

  @Test
  void testGenerateDropTableDdL() {
    File file = sqlGenerator.generateDropTableDdL("build/test-results/generator", "v1", "author");
    assertNotNull(file);
    assertTrue(file.getPath().contains("drp-table"));
  }

  @Test
  void testGenerateDropSchemaDdl() {
    File file = sqlGenerator.generateDropSchemaDdl("build/test-results/generator", "v1", "author");
    assertNotNull(file);
    assertTrue(file.getPath().contains("drp-table"));
    assertTrue(file.getPath().contains("910"));
  }

  // @Test
  // void testGenerateDeleteDdl(){
  // File file = sqlGenerator.generateDeleteDdL("build/test-results/generator", "v1", "author");
  // // mocker le graph dbGraph pour générer les statemetn de delete, la fonction est arevoir et
  // modifier..
  // assertNotNull(file);
  // assertTrue(file.getPath().contains("del-table"));
  // assertTrue(file.getPath().contains("800"));
  // }

  @Test
  void testGeneratePaticipationFctDdl() {
    File file =
        sqlGenerator.generateParticipatioFctDdl("build/test-results/generator", "v1", "author");
    assertNotNull(file);
    assertTrue(file.getPath().contains("cre-participationCheck-fct"));
    assertTrue(file.getPath().contains("110"));
  }

  @Test
  void testGenerateCheckUnionAxiomFctDdl() {
    File file =
        sqlGenerator.generateCheckUnionAxiomFctDdl("build/test-results/generator", "v1", "author");
    assertNotNull(file);
    assertTrue(file.getPath().contains("cre-unionAxiomCheck-fct"));
    assertTrue(file.getPath().contains("120"));
  }

  @Test
  void testGenerateCheckMembershipFctDdl() {
    File file =
        sqlGenerator.generateCheckMembershipFctDdl("build/test-results/generator", "v1", "author");
    assertNotNull(file);
    assertTrue(file.getPath().contains("cre-membershipCheck-fct"));
    assertTrue(file.getPath().contains("130"));
  }

  @Test
  void testGenerateCreateIriViewDdL() {
    File file =
        sqlGenerator.generateCreateIriViewDdL("build/test-results/generator", "v1", "author");
    assertNotNull(file);
    assertTrue(file.getPath().contains("cre-view-iri"));
    assertTrue(file.getPath().contains("200"));
  }

  @Test
  void testGenerateCreateLabelViewDdL() {
    File file = sqlGenerator.generateCreateLabelViewDdL("build/test-results/generator", "v1",
        "author", 20, "en");
    assertNotNull(file);
    assertTrue(file.getPath().contains("cre-view"));
    assertTrue(file.getPath().contains("210"));
  }

  @Test
  void testGenerateCreateOntoViewDdL() {
    File file =
        sqlGenerator.generateCreateOntoViewDdL("build/test-results/generator", "v1", "author");
    assertNotNull(file);
    assertTrue(file.getPath().contains("cre-view-onto"));
    assertTrue(file.getPath().contains("220"));
  }

  @Test
  void testProduceSqlDoc() {
    File file = new File("build/test-results/generator/sqlDocumentation.txt");
    sqlGenerator.produceSqlDoc(file, "author", "v0", "20250313");
    assertNotNull(file);
  }

  @Test
  void testProduceSqlDiagram() {
    File file = new File("build/test-results/generator/sqlDocumentation");
    sqlGenerator.produceSqlDiagram(file, "author", "v0", "20250313");
    assertNotNull(file);
  }


  // **************************************************************************
  // Opérations propres
  //
}
