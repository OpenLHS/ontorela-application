package ca.griis.ontorela.unit.generator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.griis.ontorela.generator.SqlTemplate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 * Test unitaire de template sql
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

class SqlTemplateTest {

  private SqlTemplate sqlTemplate;

  @Mock
  private STGroup mockTemplate;

  @Mock
  private ST mockST;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    sqlTemplate = new SqlTemplate();
    sqlTemplate.setTemplate(mockTemplate);
  }

  @Test
  void testGenererSchema() {
    when(mockTemplate.getInstanceOf("schema")).thenReturn(mockST);
    when(mockST.render()).thenReturn("CREATE SCHEMA ABC;");

    String result = sqlTemplate.genererSchema("ABC");

    assertEquals("CREATE SCHEMA ABC;", result);
    verify(mockST).add("schema_id", "ABC");
  }

  @Test
  void testGenererSchemaDefinition() {
    when(mockTemplate.getInstanceOf("schema_def")).thenReturn(mockST);
    when(mockST.render()).thenReturn("COMMENT ON SCHEMA ABC IS 'Description';");

    String result = sqlTemplate.genererSchemaDefinition("ABC", "Description");

    assertEquals("COMMENT ON SCHEMA ABC IS 'Description';", result);
    verify(mockST).add("schema_id", "ABC");
    verify(mockST).add("def", "Description");
  }

  @Test
  void testGenererSuppressionSchema() {
    when(mockTemplate.getInstanceOf("sup_schema")).thenReturn(mockST);
    when(mockST.render()).thenReturn("DROP SCHEMA ABC CASCADE;");

    String result = sqlTemplate.genererSuppressionSchema("ABC", true);

    assertEquals("DROP SCHEMA ABC CASCADE;", result);
    verify(mockST).add("schema_id", "ABC");
    verify(mockST).add("enCascade", true);
  }

  @Test
  void testGenererOntoView() {
    when(mockTemplate.getInstanceOf("view_ontoClass")).thenReturn(mockST);
    when(mockST.render()).thenReturn("CREATE VIEW test_onto_view AS ...");

    Map<String, String> axiomTableDef = new HashMap<>();
    axiomTableDef.put("table1", "axiom1");

    Map<String, String> typeTableDef = new HashMap<>();
    typeTableDef.put("table2", "type2");

    String result = sqlTemplate.generateOntoView("schema1", "view1", "classIri", "sourceSchema",
        "sourceTable", "sourceKey", axiomTableDef, typeTableDef);

    assertEquals("CREATE VIEW test_onto_view AS ...", result);
    verify(mockST).add("view_schema", "schema1");
    verify(mockST).add("view_id", "view1");
    verify(mockST).add("class_iri", "classIri");
    verify(mockST).add("var_schema", "sourceSchema");
    verify(mockST).add("var_id", "sourceTable");
  }

  @Test
  void testGenererVueDefinition() {
    when(mockTemplate.getInstanceOf("vue_def")).thenReturn(mockST);
    when(mockST.render()).thenReturn("CREATE VIEW test_view AS ...");

    String result =
        sqlTemplate.genererVueDefinition("ABC", "test_view", "SELECT * FROM test_table;");

    assertEquals("CREATE VIEW test_view AS ...", result);
    verify(mockST).add("schema_id", "ABC");
    verify(mockST).add("vue_id", "test_view");
  }

  @Test
  void testGenerateCallImportIns() {
    when(mockTemplate.getInstanceOf("onto_import_ins")).thenReturn(mockST);
    when(mockST.render()).thenReturn("CALL onto_import_ins(...);");

    String result =
        sqlTemplate.generateCallImportIns("ontorel1", "file.json", "{data}", "2025-03-14");

    assertEquals("CALL onto_import_ins(...);", result);
    verify(mockST).add("ontorel_id", "ontorel1");
    verify(mockST).add("file_name", "file.json");
    verify(mockST).add("json", "{data}");
    verify(mockST).add("import_date", "2025-03-14");
  }

  @Test
  void testGenerateOntologyLabelIns() {
    when(mockTemplate.getInstanceOf("ontology_label_ins")).thenReturn(mockST);
    when(mockST.render()).thenReturn("CALL ontology_label_ins(...);");

    String result =
        sqlTemplate.generateOntologyLabelIns("ontorel1", "label1", "en", "Ontology Label");

    assertEquals("CALL ontology_label_ins(...);", result);
    verify(mockST).add("ontorel_id", "ontorel1");
    verify(mockST).add("iri", "label1");
    verify(mockST).add("code", "en");
    verify(mockST).add("value", "Ontology Label");
  }

  @Test
  void testGenerateViewTableDefinitionOntorelCalt() {
    when(mockTemplate.getInstanceOf("vue_table_def_ontorelcat")).thenReturn(mockST);
    when(mockST.render()).thenReturn("CREATE VIEW table_def_ontorelcat AS ...");

    String result = sqlTemplate.generateViewTableDefinitionOntorelCalt("view");

    assertEquals("CREATE VIEW table_def_ontorelcat AS ...", result);
    verify(mockST).add("code", "view");
  }
}
