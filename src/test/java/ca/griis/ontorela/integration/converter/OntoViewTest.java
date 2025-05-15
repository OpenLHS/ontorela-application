package ca.griis.ontorela.integration.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoDatatypeI;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.converter.OntoView;
import ca.griis.ontorela.converter.OntoView.OntoViewType;
import ca.griis.ontorela.generator.SqlTemplate;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.util.jdd.OntoRelJdd;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.graph.DirectedMultigraph;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;

/**
 * Test unitaire pour la création des vues ontologiques.
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-11-13 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2019-11-13
 */
public class OntoViewTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  protected static final Descriptor d = new Descriptor();
  private Set<OntoView> ontoViewSet;
  private static OntoRelCat ontoRel;
  private static DirectedMultigraph<Table, ForeignKey> dbGraph;

  // **************************************************************************
  // Constructeurs
  //
  @BeforeAll
  public static void initJdd() {
    d.titre("Test unitaire " + OntoViewTest.class.getName());
    //
    OntoRelJdd ontoRelJdd = new OntoRelJdd();
    ontoRel = ontoRelJdd.buildOntoRel0();
    assertNotNull(ontoRel);
    assertTrue(ontoRelJdd.getDb1().getBaseSchema().getTableSet().size() > 0);
    assertTrue(ontoRelJdd.getDb1().getBaseSchema().getForeignKeySet().size() > 0);
    //
    dbGraph = ontoRelJdd.getDb1().getDbGraph();
    assertNotNull(dbGraph);
    assertTrue(dbGraph.vertexSet().size() > 0);
  }

  @BeforeEach
  public void initTest() {
    ontoViewSet = new LinkedHashSet<>();
  }

  @AfterAll
  public static void finTest() {
    System.out.println(d);
  }

  // **************************************************************************
  // Cas de test
  //
  @Test
  public void testOntoViewConstructors() {
    d.soustitre(" Test constructeur ");
    //
    for (Entry<OntoClassI, Table> e : ontoRel.getClassTableCatalog().entrySet()) {
      Map<OntoAxiomAssociationI, Table> axiomTableMap = ontoRel.getAxiomTableCatalog().entrySet()
          .stream().filter(a -> a.getKey().getOntoDeterminant().equals(e.getKey()))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      if (axiomTableMap.size() > 0) {
        Set<Table> axiomTableSet = new LinkedHashSet<>();
        dbGraph.outgoingEdgesOf(e.getValue()).stream()
            .forEach(fk -> axiomTableSet.add(fk.getDestination()));
        OntoView v = new OntoView(OntoViewType.PROPERVIEW, e.getKey(), e.getValue(), axiomTableMap,
            new LinkedHashMap<>(), dbGraph.incomingEdgesOf(e.getValue()));
        ontoViewSet.add(v);
        d.ajouter(v.toString());
        d.ajouter(generateOntoViewStatement(v));
      }
    }
  }

  @Test
  public void testBuildOntoProperView() {
    d.soustitre(" Test builder PROPERVIEW");
    for (Entry<OntoClassI, Table> e : ontoRel.getClassTableCatalog().entrySet()) {
      OntoView v = OntoView.buildOntoView(OntoViewType.PROPERVIEW, e.getKey(), e.getValue(),
          ontoRel.getAxiomTableCatalog());
      if (v != null) {
        ontoViewSet.add(v);
        d.ajouter(v.toString());
        Map<OntoAxiomAssociationI, Table> axiomTableMap = new LinkedHashMap<>();
        for (Entry<OntoAxiomAssociationI, Table> a : ontoRel.getAxiomTableCatalog().entrySet()) {
          // Filtre [1..1]
          if (a.getKey().getOntoDeterminant().equals(e.getKey())
              && a.getKey().getParticipation().getMin() == 1
              && a.getKey().getParticipation().getMax() == 1) {
            axiomTableMap.put(a.getKey(), e.getValue());
          }
        }
        assertNotNull(v.getViewId());
        assertEquals(v.getOntoViewType(), OntoViewType.PROPERVIEW);
        assertEquals(v.getClassSource(), e.getKey());
        assertEquals(v.getTableSource(), e.getValue());
        assertEquals(v.getAxiomTableMap().size(), axiomTableMap.size());
        assertEquals(v.getAxiomTableSet().size(), axiomTableMap.size());
        d.ajouter(generateOntoViewStatement(v));
      }
    }
  }

  @Test
  public void testBuildOntoCompleteSubSetView() {
    d.soustitre(" Test builder COMPLETESUBSETVIEW ");
    for (Entry<OntoClassI, Table> e : ontoRel.getClassTableCatalog().entrySet()) {
      OntoView v = OntoView.buildOntoView(OntoViewType.COMPLETESUBSETVIEW, e.getKey(), e.getValue(),
          ontoRel.getAxiomTableCatalog());
      if (v != null) {
        ontoViewSet.add(v);
        d.ajouter(v.toString());
        Map<OntoAxiomAssociationI, Table> axiomTableMap = new LinkedHashMap<>();
        for (Entry<OntoAxiomAssociationI, Table> a : ontoRel.getAxiomTableCatalog().entrySet()) {
          // Filtre [1..*]
          if (a.getKey().getOntoDeterminant().equals(e.getKey())
              && a.getKey().getParticipation().getMin() >= 1) {
            axiomTableMap.put(a.getKey(), e.getValue());
          }
        }
        assertNotNull(v.getViewId());
        assertEquals(v.getOntoViewType(), OntoViewType.COMPLETESUBSETVIEW);
        assertEquals(v.getClassSource(), e.getKey());
        assertEquals(v.getTableSource(), e.getValue());
        assertEquals(v.getAxiomTableMap().size() + axiomTableMap.size(),
            v.getAxiomTableSet().size(), axiomTableMap.size());
        assertEquals(v.getAxiomTableSet().size(), axiomTableMap.size());
        d.ajouter(generateOntoViewStatement(v));
      }
    }
  }

  @Test
  public void testBuildOntoViewOntoRelDicProperView() {
    d.soustitre(" Test builder OntoRelDic P");
    for (Entry<OntoClassI, Table> e : ontoRel.getClassTableCatalog().entrySet()) {
      OntoView v = OntoView.buildOntoView(OntoViewType.PROPERVIEW, e.getKey(), ontoRel);
      if (v != null) {
        ontoViewSet.add(v);
        d.ajouter(v.toString());
        Map<OntoAxiomAssociationI, Table> axiomTableMap = new LinkedHashMap<>();
        for (Entry<OntoAxiomAssociationI, Table> a : ontoRel.getAxiomTableCatalog().entrySet()) {
          // Filtre [1..1]
          if (a.getKey().getOntoDeterminant().equals(e.getKey())
              && a.getKey().getParticipation().getMin() == 1
              && a.getKey().getParticipation().getMax() == 1) {
            axiomTableMap.put(a.getKey(), e.getValue());
          }
        }
        assertNotNull(v.getViewId());
        assertEquals(v.getOntoViewType(), OntoViewType.PROPERVIEW);
        assertEquals(v.getClassSource(), e.getKey());
        assertEquals(v.getTableSource(), e.getValue());
        assertEquals(v.getAxiomTableMap().size(), axiomTableMap.size());
        assertTrue(v.getAxiomTableSet().size() >= axiomTableMap.size());
        d.ajouter(generateOntoViewStatement(v));
      }
    }
  }

  @Test
  public void testBuildOntoViewOntoRelDicCSView() {
    d.soustitre(" Test builder OntoRelDic CS");
    for (Entry<OntoClassI, Table> e : ontoRel.getClassTableCatalog().entrySet()) {
      OntoView v = OntoView.buildOntoView(OntoViewType.COMPLETESUBSETVIEW, e.getKey(), ontoRel);
      if (v != null) {
        ontoViewSet.add(v);
        d.ajouter(v.toString());
        Map<OntoAxiomAssociationI, Table> axiomTableMap = new LinkedHashMap<>();
        for (Entry<OntoAxiomAssociationI, Table> a : ontoRel.getAxiomTableCatalog().entrySet()) {
          // Filtre [1..1]
          if (a.getKey().getOntoDeterminant().equals(e.getKey())
              && a.getKey().getParticipation().getMin() >= 1) {
            axiomTableMap.put(a.getKey(), e.getValue());
          }
        }
        assertNotNull(v.getViewId());
        assertEquals(v.getOntoViewType(), OntoViewType.COMPLETESUBSETVIEW);
        assertEquals(v.getClassSource(), e.getKey());
        assertEquals(v.getTableSource(), e.getValue());
        assertEquals(v.getAxiomTableMap().size(), axiomTableMap.size());
        assertTrue(v.getAxiomTableSet().size() >= axiomTableMap.size());
        d.ajouter(generateOntoViewStatement(v));
      }
    }
  }

  @Test
  public void testSQLGenerator() {
    SqlTemplate gen = new SqlTemplate();
    ST ontoView = gen.getTemplate().getInstanceOf("view_ontoClass");
    assertNotNull(ontoView);
    ontoView.add("view_schema", "S0_onto");
    ontoView.add("view_id", "v0");
    ontoView.add("class_iri", "C0_iri");
    ontoView.add("var_schema", "S0");
    ontoView.add("var_id", "T0");
    for (int i = 1; i <= 4; i++) {
      ontoView.addAggr("ensTable.{id, var_key}", "T" + i, "T0_uid");
    }
    assertFalse(ontoView.render().isEmpty());
    System.out.println(ontoView.render());
  }

  // **************************************************************************
  // Opérations publiques
  //
  public String generateOntoViewStatementWithON(OntoView v) {
    SqlTemplate gen = new SqlTemplate();
    ST ontoView = gen.getTemplate().getInstanceOf("view_ontoClass_test");
    ontoView.add("schema_id", "S0");
    ontoView.add("vue_id", v.getViewId());
    ontoView.add("class_iri", v.getClassSource().getIri().getShortIri());
    ontoView.add("table_id", v.getTableSource().getIdentifier().getValue());
    String sourceKey =
        v.getTableSource().getPrimaryKeyAttributeSet().stream().findFirst().get().getAttId();
    for (ForeignKey fk : v.getAxiomForeignKeySet()) {
      ontoView.addAggr("ensTable.{id, sourceKey, targetKey}",
          fk.getOrigin().getIdentifier().getValue(), sourceKey,
          fk.getAttOrigin().stream().findFirst().get().getAttId());
    }
    return ontoView.render();
  }

  public String generateOntoViewStatement(OntoView v) {
    SqlTemplate gen = new SqlTemplate();
    ST ontoView = gen.getTemplate().getInstanceOf("view_ontoClass");
    ontoView.add("view_schema", "S0_onto");
    ontoView.add("view_id", v.getViewId());
    ontoView.add("class_iri", v.getClassSource().getIri().getShortIri());
    ontoView.add("var_schema", v.getTableSource().getSchemaId());
    ontoView.add("var_id", v.getTableSource().getIdentifier().getValue());
    String source_key =
        v.getTableSource().getPrimaryKeyAttributeSet().stream().findFirst().get().getAttId();
    for (Entry<OntoAxiomAssociationI, Table> link : v.getAxiomTableMap().entrySet()) {
      ontoView.addAggr("ensTable.{id, var_key, axiomString}",
          link.getValue().getIdentifier().getValue(), source_key,
          link.getKey().getOntoAxiomString());
    }
    for (Entry<OntoDatatypeI, Table> link : v.getTypeTableMap().entrySet()) {
      String type_key =
          link.getValue().getPrimaryKeyAttributeSet().stream().findFirst().get().getAttId();
      ontoView.addAggr("ensTable.{id, var_key, axiomString}",
          link.getValue().getIdentifier().getValue(), type_key,
          link.getKey().getIri().getShortIri());
    }
    return ontoView.render();
  }

}
