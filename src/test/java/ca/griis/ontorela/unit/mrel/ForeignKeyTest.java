package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.model.OntoIri;
import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.CandidateKey;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.ForeignKey.ForeignKeyType;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.Table.TableOrigin;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire des clés réfétentielles.
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
public class ForeignKeyTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  // TODO 2018-06-12 CK : test pour un fk invalides.
  private ForeignKey fk1;
  private ForeignKey fk2;
  private Table to;
  private Table td1;
  private Table td2;
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
    // Les tables
    Attribute a0 = new Attribute("a0", "T001_00", "INT");
    Set<Attribute> keyAtto1 = new LinkedHashSet<>();
    keyAtto1.add(a0);
    CandidateKey cc1 = new CandidateKey("cc1", keyAtto1, true);
    to = new Table(TableOrigin.ONTOCLASS, c00Iri, "s0", "t0");
    to.addKey(cc1);
    //
    td1 = new Table(TableOrigin.ONTOCLASS, c01Iri, "s0", "td1");
    td1.addKey(cc1);
    //
    Attribute a1 = new Attribute("a1", "T001_01", "INT");
    Set<Attribute> keyAttd = new LinkedHashSet<>();
    keyAttd.add(a0);
    keyAttd.add(a1);
    CandidateKey cc2 = new CandidateKey("cc2", keyAttd, false);
    //
    td2 = new Table(TableOrigin.ONTOCLASS, c02Iri, "s0", "td2");
    td2.addKey(cc2);
    //
    // FK avec un attribut
    Map<Attribute, Attribute> linkedAtt1 = new HashMap<>();
    linkedAtt1.put(a0, a0);
    fk1 = new ForeignKey(ForeignKeyType.OBJECTPROPERTY, "fk1", to, td1, linkedAtt1);
    // FK avec deux attributs
    to.addAttribute(a1);
    Map<Attribute, Attribute> linkedAtt2 = new HashMap<>();
    linkedAtt2.put(a0, a0);
    linkedAtt2.put(a1, a1);
    fk2 = new ForeignKey(ForeignKeyType.ISA, "fk2", to, td2, linkedAtt2);
  }

  // **************************************************************************
  // Cas de tests
  //
  /**
   * Test method for {@link ca.griis.ontorela.mrel.ForeignKey#getFkId()}.
   */
  @Test
  public void testGetFkId() {
    assertTrue(fk1.getFkId().equals("fk1"));
    assertTrue(fk2.getFkId().equals("fk2"));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.ForeignKey#getOrigin()}.
   */
  @Test
  public void testGetOrigine() {
    assertTrue(fk1.getOrigin().equals(to));
    assertTrue(fk2.getOrigin().equals(to));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.ForeignKey#getDestination()}.
   */
  @Test
  public void testGetDestination() {
    assertTrue(fk1.getDestination().equals(td1));
    assertTrue(fk2.getDestination().equals(td2));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.ForeignKey#getAttOrigin()}.
   */
  @Test
  public void testGetAttOrigine() {
    // Taille de l'ensemble
    assertTrue(fk1.getAttOrigin().size() == 1);
    assertTrue(fk2.getAttOrigin().size() == 2);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.ForeignKey#getAttOrigineId()}.
   */
  @Test
  public void testGetAttOrigineId() {
    assertNotNull(fk1.getAttOrigineId());
    assertNotNull(fk2.getAttOrigineId());
    //
    System.out.println(fk1.toString());
    System.out.println(fk2.toString());
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.ForeignKey#getAttDestination()}.
   */
  @Test
  public void testGetAttDestination() {
    // Taille de l'ensemble
    assertTrue(fk1.getAttDestination().size() == 1);
    assertTrue(fk2.getAttDestination().size() == 2);
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.ForeignKey#getAttDestinationId()}.
   */
  @Test
  public void testGetAttDestinationId() {
    assertNotNull(fk1.getAttDestinationId());
    assertNotNull(fk2.getAttDestinationId());
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
}
