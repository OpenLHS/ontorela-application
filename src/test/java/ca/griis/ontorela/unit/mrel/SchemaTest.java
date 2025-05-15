package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.api.ParticipationI.ParticipationType;
import ca.griis.monto.model.OntoIri;
import ca.griis.monto.model.Participation;
import ca.griis.ontorela.mrel.*;
import ca.griis.ontorela.mrel.ForeignKey.ForeignKeyType;
import ca.griis.ontorela.mrel.Schema.SchemaType;
import ca.griis.ontorela.mrel.Table.TableOrigin;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Décrire la classe ici.
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui | non (pourquoi).</li>
 * <li>Clonabilité : oui | non (pourquoi).</li>
 * <li>Modifiabilité : oui | non (pourquoi).</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * TODO 2019-11-20 CK : création OntoIRI ou null ?<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2018-09-07 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-09-07
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */
public class SchemaTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  private Schema s0;
  private Table t0;
  private Table t1;
  private Table t2;
  private Attribute a0;
  private Attribute a1;
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
    //
    // les IRI de classes
    c00Iri = new OntoIri("OntoRela/ut/C001");
    c01Iri = new OntoIri("OntoRela/ut/C002");
    c02Iri = new OntoIri("OntoRela/ut/C003");
    // les tables
    a0 = new Attribute("a0", "T001_00", "INT");
    a1 = new Attribute("a1", "T001_01", "INT");
    t0 = new Table(TableOrigin.ONTOCLASS, c00Iri, "s0", "t0");
    t1 = new Table(TableOrigin.ONTOCLASS, c01Iri, "s0", "t1");
    t2 = new Table(TableOrigin.ONTOCLASS, c02Iri, "s0", "t2");
    s0 = new Schema("s0", SchemaType.BASE);
  }

  // **************************************************************************
  // Cas de test
  //
  /**
   * Test method AddTable
   */
  @Test
  public void testAddTable() {
    assertTrue(s0.getTableSet().size() == 0);
    s0.addTable(t0);
    assertTrue(s0.getTableSet().size() == 1);
  }

  /**
   * Test method AddFk
   */
  @Test
  public void testAddFk() {
    s0.addTable(t0);
    s0.addTable(t2);
    Map<Attribute, Attribute> linkedAtt = new HashMap<>();
    linkedAtt.put(a0, a0);
    s0.addFk(ForeignKeyType.OBJECTPROPERTY, t0, t2, linkedAtt);
    assertTrue(s0.getForeignKeySet().size() == 1);
    assertTrue(s0.getOriginForeignKeySet(t0).size() == 1);
    assertTrue(s0.getOriginForeignKeySet(t2).size() == 0);
    for (ForeignKey fk : s0.getOriginForeignKeySet(t0)) {
      assertTrue(fk.getOrigin().equals(t0));
      assertTrue(fk.getAttOrigin().contains(a0));
      assertTrue(fk.getDestination().equals(t2));
      assertTrue(fk.getAttDestination().stream().findFirst().get().equals(a0));
    }
  }

  /**
   * Test method for AddFkForDefaultKey
   */
  @Test
  public void testAddFkForDefaultKey() {
    s0.addTable(t1);
    s0.addTable(t2);
    t1.addKey(a0, true);
    t2.addAttribute(a0);
    t2.addKey(a1, true);
    s0.addFkForDefaultKey(ForeignKeyType.ISA, t2, t1, a0);
    assertTrue(s0.getForeignKeySet().size() == 1);
    assertTrue(s0.getOriginForeignKeySet(t2).size() == 1);
    assertTrue(s0.getOriginForeignKeySet(t1).size() == 0);
    for (ForeignKey fk : s0.getOriginForeignKeySet(t2)) {
      assertTrue(fk.getOrigin().equals(t2));
      assertTrue(fk.getAttOrigin().contains(a0));
      assertTrue(fk.getDestination().equals(t1));
      assertTrue(fk.getAttDestination().stream().findFirst().get().equals(a0));
    }
  }

  @Test
  public void getConstraintSet() {
    ParticipationConstraint c0 =
        new ParticipationConstraint(t0, new Participation(ParticipationType.SOME));
    MembershipConstraint c1 = new MembershipConstraint(t1, a0, t0);
    s0.addConstraint(c0);
    s0.addConstraint(c1);
    assertTrue(s0.getConstraintSet().size() > 0);
    s0.getConstraintSet(ParticipationConstraint.class);
    System.out.println(s0.getConstraintSet());
    assertTrue(s0.getConstraintSet(ParticipationConstraint.class).size() == 1);
  }
}
