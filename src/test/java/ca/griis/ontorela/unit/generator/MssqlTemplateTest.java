package ca.griis.ontorela.unit.generator;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.ontorela.generator.MssqlTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 * Test unitaire de template mssql
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

public class MssqlTemplateTest {

  private MssqlTemplate mssqlTemplate;
  private STGroup mockGroup;
  private ST mockST;

  @BeforeEach
  public void setUp() {
    mockGroup = Mockito.mock(STGroup.class);
    mssqlTemplate = new MssqlTemplate();
    mssqlTemplate.setTemplate(mockGroup);
    mockST = Mockito.mock(ST.class);

    mssqlTemplate = new MssqlTemplate();
    mssqlTemplate.setTemplate(mockGroup);


  }

  @Test
  public void testGetTemplate() {
    assertNotNull(mssqlTemplate.getTemplate());
  }

  @Test
  public void testGenererSchema() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("schema")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("CREATE SCHEMA test_schema;");
    String result = mssqlTemplate.genererSchema("test_schema");
    assertEquals("CREATE SCHEMA test_schema;", result);
  }

  @Test
  public void testGenererSchemaDefinition() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("schema_def")).thenReturn(mockST);
    Mockito.when(mockST.render())
        .thenReturn("COMMENT ON SCHEMA test_schema IS 'This is a test schema';");

    String result = mssqlTemplate.genererSchemaDefinition("test_schema", "This is a test schema");
    assertEquals("COMMENT ON SCHEMA test_schema IS 'This is a test schema';", result);
  }

  @Test
  public void testGenererSuppressionSchema() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("sup_schema")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("DROP SCHEMA test_schema CASCADE;");

    String result = mssqlTemplate.genererSuppressionSchema("test_schema", true);
    assertEquals("DROP SCHEMA test_schema CASCADE;", result);
  }

  @Test
  public void testGenererTypePredefini() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("type_predefini")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("CREATE TYPE my_type AS VARCHAR(255) NOT NULL;");

    String result = mssqlTemplate.genererTypePredefini("my_type", "VARCHAR(255)", false);
    assertEquals("CREATE TYPE my_type AS VARCHAR(255) NOT NULL;", result);
  }

  @Test
  public void testGenererDomaine() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("domaine")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("CREATE DOMAIN my_domain AS INT;");

    String result = mssqlTemplate.genererDomaine("public", "my_domain", "INT");
    assertEquals("CREATE DOMAIN my_domain AS INT;", result);
  }

  @Test
  public void testGenererSuppressionDomaine() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("suppression_domaine")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("DROP DOMAIN my_domain;");

    String result = mssqlTemplate.genererSuppressionDomaine("public", "my_domain");
    assertEquals("DROP DOMAIN my_domain;", result);
  }

  @Test
  public void testGenererRelvarDefinition() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("relvar_def")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("COMMENT ON TABLE public.my_table IS 'Test table';");

    String result = mssqlTemplate.genererRelvarDefinition("public", "my_table", "Test table");
    assertEquals("COMMENT ON TABLE public.my_table IS 'Test table';", result);
  }

  @Test
  public void testGenererContrainteCleSecondaire() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("contrainte_cleSecondaire")).thenReturn(mockST);
    Mockito.when(mockST.render())
        .thenReturn("ALTER TABLE my_table ADD CONSTRAINT sec_key UNIQUE (column1, column2);");

    ArrayList<String> attCle = new ArrayList<>();
    attCle.add("column1");
    attCle.add("column2");

    String result =
        mssqlTemplate.genererContrainteCleSecondaire("public", "my_table", "sec_key", attCle);
    assertEquals("ALTER TABLE my_table ADD CONSTRAINT sec_key UNIQUE (column1, column2);", result);
  }

  @Test
  public void testGenererSuppressionRelVar() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("suppression_relvar")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("DROP TABLE my_table CASCADE;");

    String result = mssqlTemplate.genererSuppressionRelVar("public", "my_table");
    assertEquals("DROP TABLE my_table CASCADE;", result);
  }

  @Test
  public void testGenererEnteteFichierSql() {
    ST mockST = Mockito.mock(ST.class);
    Mockito.when(mockGroup.getInstanceOf("enteteFichierSQL")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("-- SQL Header");

    String result = mssqlTemplate.genererEnteteFichierSql("public", "2025-03-03", "Admin", "1.0",
        "Schema Creation");
    System.out.println(result);
    assertEquals("-- SQL Header", result);
  }

  @Test
  public void testGenerateOntoView() {
    Mockito.when(mockGroup.getInstanceOf("view_ontoClass")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("GENERATED SQL VIEW");

    String viewSchemaId = "public";
    String viewId = "ontology_view";
    String classIri = "ontologyIRI";
    String sourceTableSchemaId = "public";
    String sourceTableId = "tableId";
    String sourceTableKeyId = "u_id";

    Map<String, String> axiomTableDef = new HashMap<>();
    axiomTableDef.put("axiom1", "C001op1C00");
    axiomTableDef.put("axiom2", "C02op2C03");

    Map<String, String> typeTableDef = new HashMap<>();
    typeTableDef.put("type1", "type1");
    typeTableDef.put("type2", "type2");

    String result = mssqlTemplate.generateOntoView(viewSchemaId, viewId, classIri,
        sourceTableSchemaId, sourceTableId, sourceTableKeyId,
        axiomTableDef, typeTableDef);

    Mockito.verify(mockST).add("view_schema", viewSchemaId);
    Mockito.verify(mockST).add("view_id", viewId);
    Mockito.verify(mockST).add("class_iri", classIri);
    Mockito.verify(mockST).add("var_schema", sourceTableSchemaId);
    Mockito.verify(mockST).add("var_id", sourceTableId);

    for (Map.Entry<String, String> axiomEntry : axiomTableDef.entrySet()) {
      Mockito.verify(mockST).addAggr("ensTable.{id, var_key, axiomString}",
          axiomEntry.getKey(), sourceTableKeyId, axiomEntry.getValue());
    }

    for (Map.Entry<String, String> typeEntry : typeTableDef.entrySet()) {
      Mockito.verify(mockST).addAggr("ensTable.{id, var_key, axiomString}",
          typeEntry.getKey(), typeEntry.getValue(), typeEntry.getKey());
    }

    assertEquals("GENERATED SQL VIEW", result);
  }

  @Test
  public void testGenerateOntoViewWithEmptyMaps() {
    Mockito.when(mockGroup.getInstanceOf("view_ontoClass")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("GENERATED SQL VIEW");

    String viewSchemaId = "public";
    String viewId = "ontology_view";
    String classIri = "ontologyIRI";
    String sourceTableSchemaId = "public";
    String sourceTableId = "tableId";
    String sourceTableKeyId = "u_id";
    Map<String, String> axiomTableDef = new HashMap<>();
    Map<String, String> typeTableDef = new HashMap<>();

    String result = mssqlTemplate.generateOntoView(viewSchemaId, viewId, classIri,
        sourceTableSchemaId, sourceTableId, sourceTableKeyId,
        axiomTableDef, typeTableDef);

    Mockito.verify(mockST).render();
    assertEquals("GENERATED SQL VIEW", result);
  }

  @Test
  public void testGenerateOntoViewWithNullValues() {
    Mockito.when(mockGroup.getInstanceOf("view_ontoClass")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("GENERATED SQL VIEW");

    String viewSchemaId = "public";
    String viewId = "ontology_view";
    String classIri = null;
    String sourceTableSchemaId = "public";
    String sourceTableId = "table_idd";
    String sourceTableKeyId = "u_id";

    String result = mssqlTemplate.generateOntoView(viewSchemaId, viewId, classIri,
        sourceTableSchemaId, sourceTableId, sourceTableKeyId,
        new HashMap<>(),
        new HashMap<>());

    Mockito.verify(mockST).render();
    assertEquals("GENERATED SQL VIEW", result);
  }

  @Test
  void testGenererVueDeRenommage() {
    Mockito.when(mockGroup.getInstanceOf("vue_renommage")).thenReturn(mockST);
    Mockito.when(mockST.render()).thenReturn("GENERATED SQL VIEW RENAME");

    String vueSchema = "public";
    String vueId = "renommage_view";
    String varSchema = "schema";
    String varId = "table_id";

    Map<String, String> ensAtt = new HashMap<>();
    ensAtt.put("name1", "name1");
    ensAtt.put("name2", "name2");
    String result = mssqlTemplate.genererVueDeRenommage(vueSchema, vueId, varSchema, varId, ensAtt);
    Mockito.verify(mockST).add("vue_schema", vueSchema);
    Mockito.verify(mockST).add("vue_id", vueId);
    Mockito.verify(mockST).add("var_schema", varSchema);
    Mockito.verify(mockST).add("var_id", varId);

    for (Map.Entry<String, String> entry : ensAtt.entrySet()) {
      Mockito.verify(mockST).addAggr("ensAtt.{id, alias}", entry.getKey(), entry.getValue());
    }
    assertEquals("GENERATED SQL VIEW RENAME", result);
  }
}
