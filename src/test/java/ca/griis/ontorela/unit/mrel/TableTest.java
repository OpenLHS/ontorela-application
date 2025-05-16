package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.model.OntoIri;
import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.CandidateKey;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.Table.TableOrigin;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests unitaires d'une table
 * 
 * <b>Tâches projetées</b><br>
 * TODO 2019-11-20 CK : création OntoIRI ou null ?<br>
 *
 * <b>Tâches réalisées</b><br>
 * <i>2017-XX-XX (0.2.0) [XX] ... </i> <br>
 * <i>2017-11-13 (0.1.0) [CK] Mise en oeuvre initiale. </i> <br>
 * 
 * <p>
 * <b>Copyright</b> 2014-2016, Μῆτις (http://info.usherbrooke.ca/llavoie/) <br>
 * <b>Copyright</b> 2017, GRIIS (http://griis.ca/) <br>
 * 
 * <p>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé) <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke <br>
 * Sherbrooke (Québec) J1K 2R1 CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 * 
 * @since 2018-06-12
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */
public class TableTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  private Table t0;
  private Table t1;
  private Table t2;
  //
  private Attribute a0;
  private Attribute a1;
  private Attribute a2;
  //
  private OntoIri c00Iri;
  private OntoIri c01Iri;
  private OntoIri c02Iri;

  // **************************************************************************
  // Constructeurs
  //
  /**
   * Initialisation des tests.
   */
  @BeforeEach
  public void initTest() {
    // les IRI de classes
    c00Iri = new OntoIri("OntoRela/ut/C001");
    c01Iri = new OntoIri("OntoRela/ut/C002");
    c02Iri = new OntoIri("OntoRela/ut/C003");
    // les attributs
    a0 = new Attribute("a0", "T001_00", "INT");
    a1 = new Attribute("a1", "T001_01", "INT");
    a2 = new Attribute("a2", "T001_02", "INT");
    // les tables
    t0 = new Table(TableOrigin.ONTOCLASS, c00Iri, "s0", "onto_t0");
    t1 = new Table(TableOrigin.ONTOCLASS, c01Iri, "s0", "onto_t1");
    t2 = new Table(TableOrigin.ONTOCLASS, c02Iri, "s0", "onto_t2");
  }

  // **************************************************************************
  // Cas de test
  //
  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#
   * addLabel(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testAddLabel() {
    assertTrue(t1.getLabels().isEmpty());
    //
    t1.addLabel("fr", "test unitaire");
    t1.addLabel("en", "unit test");
    //
    assertFalse(t1.getLabels().isEmpty());
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#
   * addDescription(java.lang.String, java.lang.String)}.
   */
  @Test
  public void testAddDescription() {
    assertTrue(t1.getDescription().isEmpty());
    //
    t1.addDescription("fr", "test unitaire");
    t1.addDescription("en", "unit test");
    //
    assertFalse(t1.getDescription().isEmpty());
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#
   * addLabels(java.util.Map)}.
   */
  @Test
  public void testAddLabels() {
    assertTrue(t1.getLabels().isEmpty());
    //
    Map<String, String> labels = new HashMap<>();
    labels.put("fr", "test unitaire");
    labels.put("en", "unit test");
    t1.addLabels(labels);
    //
    assertFalse(t1.getLabels().isEmpty());
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#
   * addDescriptions(java.util.Map)}.
   */
  @Test
  public void testAddDefinitions() {
    assertTrue(t1.getDescription().isEmpty());
    //
    Map<String, String> defs = new HashMap<>();
    defs.put("fr", "test unitaire");
    defs.put("en", "unit test");
    t1.addDescriptions(defs);
    //
    assertFalse(t1.getDescription().isEmpty());
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#
   * addAttribute(ca.griis.ontorela.mrel.Attribute)}.
   */
  @Test
  public void testAddAttribute() {
    assertTrue(t1.getAttributeSet().size() == 0);
    assertTrue(t2.getAttributeSet().size() == 0);
    //
    t0.addAttribute(a0);
    t1.addAttribute(a1);
    t1.addAttribute(a2);
    //
    assertTrue(t0.getAttributeSet().size() == 1);
    assertTrue(t1.getAttributeSet().size() == 2);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#
   * addKey(ca.griis.ontorela.mrel.Attribute, boolean)}.
   */
  @Test
  public void testAddKeyAttributeBoolean() {
    assertTrue(t1.getKeyAttributeSet().size() == 0);
    //
    t1.addKey(a2, true);
    //
    assertTrue(t1.getKeyAttributeSet().size() == 1);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#
   * addKey(ca.griis.ontorela.mrel.CandidateKey)}.
   */
  @Test
  public void testAddKeyCandidateKey() {
    assertTrue(t1.getKeyAttributeSet().size() == 0);
    //
    Set<Attribute> keyAtt1 = new LinkedHashSet<>();
    keyAtt1.add(a0);
    CandidateKey cc1 = new CandidateKey("cc1", keyAtt1, true);
    t1.addKey(cc1);
    Set<Attribute> keyAtt2 = new LinkedHashSet<>();
    keyAtt2.add(a0);
    keyAtt2.add(a1);
    CandidateKey cc2 = new CandidateKey("cc2", keyAtt2, false);
    t1.addKey(cc2);
    //
    assertTrue(t1.getKeyAttributeSet().size() == 2);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#
   * addKey(java.util.Set, boolean)}.
   */
  @Test
  public void testAddKeySetOfAttributeBoolean() {
    assertTrue(t1.getKeyAttributeSet().size() == 0);
    //
    Set<Attribute> keyAtt1 = new LinkedHashSet<>();
    keyAtt1.add(a0);
    t1.addKey(keyAtt1, true);
    Set<Attribute> keyAtt2 = new LinkedHashSet<>();
    keyAtt2.add(a0);
    keyAtt2.add(a1);
    t1.addKey(keyAtt2, false);
    //
    assertTrue(t1.getKeyAttributeSet().size() == 2);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getSchemaId()}.
   */
  @Test
  public void testGetSchemaId() {
    assertTrue(t1.getSchemaId().equals("s0"));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getIri()}.
   */
  @Test
  public void testGetTableIri() {
    assertTrue(t1.getIri().equals("onto_t1"));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getPrefixedIri()}.
   */
  @Test
  public void testGetPrefixedTableIri() {
    assertTrue(t1.getPrefixedIri().equals(t1.getIdentifier().getValue() + "_onto_t1"),
        t1.getPrefixedIri());
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getAttributeSet()}.
   */
  @Test
  public void testGetAttSet() {
    assertTrue(t1.getAttributeSet().size() == 0);
    //
    t1.addAttribute(a0);
    t1.addKey(a1, true);
    //
    assertTrue(t1.getAttributeSet().size() == 2);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getAttributeTypePaires()}.
   */
  @Test
  public void testGetAttTypePaires() {
    assertTrue(t1.getAttributeTypePaires().size() == 0);
    //
    t1.addAttribute(a0);
    //
    assertTrue(t1.getAttributeTypePaires().size() == 1);
    assertTrue(t1.getAttributeTypePaires().keySet().contains("a0"));
    assertTrue(t1.getAttributeTypePaires().values().contains("INT"));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getKeyAttributeSet()}.
   */
  @Test
  public void testGetKeyAttributeSet() {
    assertTrue(t1.getKeyAttributeSet().size() == 0);
    //
    t1.addAttribute(a0);
    t1.addKey(a1, true);
    //
    assertTrue(t1.getKeyAttributeSet().size() == 1);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getNoKeyAttributeSet()}.
   */
  @Test
  public void testGetNoKeyAttributeSet() {
    assertTrue(t1.getNoKeyAttributeSet().size() == 0);
    //
    t1.addAttribute(a0);
    t1.addAttribute(a2);
    t1.addKey(a1, true);
    //
    System.out.println(t1.getPrimaryKeyAttributeSet());
    System.out.println(t1.getNoKeyAttributeSet());
    assertTrue(t1.getNoKeyAttributeSet().size() == 2);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getKeyAttributeSet()}.
   */
  @Test
  public void testGetPrimaryKeyAttSet() {
    assertTrue(t1.getKeyAttributeSet().size() == 0);
    //
    Set<Attribute> keyAtt1 = new LinkedHashSet<>();
    keyAtt1.add(a0);
    CandidateKey cc1 = new CandidateKey("cc1", keyAtt1, true);
    t1.addKey(cc1);
    Set<Attribute> keyAtt2 = new LinkedHashSet<>();
    keyAtt2.add(a0);
    keyAtt2.add(a1);
    CandidateKey cc2 = new CandidateKey("cc2", keyAtt2, false);
    t1.addKey(cc2);
    //
    assertTrue(t1.getPrimaryKeyAttributeSet().size() == 1);
    assertTrue(t1.getPrimaryKeyAttributeSet().equals(keyAtt1));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#toString()}.
   */
  @Test
  public void testGetTableString() {
    assertNotNull(t1.toString());
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getTableDeclarationString()}.
   */
  @Test
  public void testGetTableDeclarationString() {
    assertNotNull(t1.toString());
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getTableOrigin()}.
   */
  public void testGetTableOrigin() {
    assertTrue(t0.getTableOrigin().equals(TableOrigin.ONTOCLASS));
    assertTrue(t1.getTableOrigin().equals(TableOrigin.ONTOCLASS));
    assertTrue(t2.getTableOrigin().equals(TableOrigin.ONTOCLASS));
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
}
