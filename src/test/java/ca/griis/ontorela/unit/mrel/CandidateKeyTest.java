package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.CandidateKey;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaires pour les clés candidates.
 *
 * <b>Tâches projetées</b><br>
 * <i>...</i> <br>
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
public class CandidateKeyTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  private CandidateKey cc1;
  private CandidateKey cc2;
  private Attribute a0;
  private Attribute a1;
  private Set<Attribute> keyAtt1;

  // **************************************************************************
  // Constructeurs
  //
  /**
   * Initialisation des tests.
   */
  @BeforeEach
  public void initTest() {
    // Clé avec un attribut
    a0 = new Attribute("a0", "T001_00", "INT");
    keyAtt1 = new LinkedHashSet<>();
    keyAtt1.add(a0);
    cc1 = new CandidateKey("cc1", keyAtt1, true);
    // Clé avec deux attributs
    a1 = new Attribute("a1", "T001_01", "INT");
    Set<Attribute> keyAtt2 = new LinkedHashSet<>();
    keyAtt2.add(a0);
    keyAtt2.add(a1);
    cc2 = new CandidateKey("cc2", keyAtt2, false);
  }

  // **************************************************************************
  // Cas de tests
  //
  /**
   * Test method for {@link ca.griis.ontorela.mrel.CandidateKey#getId()}.
   */
  @Test
  public void testGetCkId() {
    assertTrue(cc1.getId().equals("cc1"));
    assertTrue(cc2.getId().equals("cc2"));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.CandidateKey#isPrimary()}.
   */
  @Test
  public void testGetPrimary() {
    assertTrue(cc1.isPrimary());
    assertFalse(cc2.isPrimary());
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.CandidateKey#getKeyAttSet()}.
   */
  @Test
  public void testGetKeyAttSet() {
    // Taille de l'ensemble
    assertTrue(cc1.getKeyAttSet().size() == 1);
    assertTrue(cc2.getKeyAttSet().size() == 2);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.CandidateKey#getKeyAttIds()}.
   */
  @Test
  public void testGetKeyAttIds() {
    // Taille de l'ensemble
    assertTrue(cc1.getKeyAttIds().size() == 1);
    assertTrue(cc2.getKeyAttIds().size() == 2);
    //
    System.out.println(cc1.toString());
    System.out.println(cc2.toString());
  }

  /**
   * Test method for
   * {@link ca.griis.ontorela.mrel.CandidateKey#addKey(ca.griis.ontorela.mrel.Attribute)}.
   */
  @Test
  public void testAddKey() {
    Attribute a2 = new Attribute("a2", "T001_02", "INT");
    cc1.addKey(a2);
    assertTrue(cc1.getKeyAttSet().size() == 2);
    cc2.addKey(a2);
    assertTrue(cc2.getKeyAttSet().size() == 3);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.CandidateKey#setId(java.lang.String)}.
   */
  @Test
  public void testSetCkId() {
    String id = "setCkId";
    cc1.setId(id);
    assertTrue(cc1.getId().equals(id));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.CandidateKey#setKeyAttSet(java.util.Set)}.
   */
  @Test
  public void testSetKeyAttSet() {
    Attribute a3 = new Attribute("a3", "T001_03", "INT");
    Set<Attribute> keyAtt = new LinkedHashSet<>();
    keyAtt.add(a3);
    cc2.setKeyAttSet(keyAtt);
    assertTrue(cc2.getKeyAttSet().size() == 1);
    assertTrue(cc2.getKeyAttSet().equals(keyAtt));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.CandidateKey#setPrimary(boolean)}.
   */
  @Test
  public void testSetPrimary() {
    boolean f = false;
    cc1.setPrimary(f);
    assertFalse(cc1.isPrimary());
    boolean t = true;
    cc2.setPrimary(t);
    assertTrue(cc2.isPrimary());
  }

  @Test
  public void testGetkeyAttString() {
    Set<String> expected = new LinkedHashSet<>();
    expected.add("a0::T001_00");
    assertEquals(expected, cc1.getKeyAttString(true, false));
  }

  @Test
  public void testGetkeyIri() {
    Set<String> expected = new LinkedHashSet<>();
    expected.add("T001_00");
    assertEquals(expected, cc1.getKeyIri());
  }

  @Test
  void testToString() {
    String expected = "CandidateKey [id=cc1, keyAttSet=" + keyAtt1 + "]";
    assertEquals(expected, cc1.toString());
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
}
