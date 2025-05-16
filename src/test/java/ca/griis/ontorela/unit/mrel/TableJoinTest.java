package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.api.ParticipationI.ParticipationType;
import ca.griis.monto.model.BuiltInOwlDatatypeSet;
import ca.griis.monto.model.Participation;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.Table.TableOrigin;
import ca.griis.ontorela.mrel.TableJoin;
import ca.griis.ontorela.util.jdd.MOntoJdd;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test untaire pour les tables de jointures.
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
public class TableJoinTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static Table t0;
  private static Table t1;
  private static Table t2;
  //
  private static TableJoin j0;
  private static TableJoin j1;
  private static TableJoin j2;
  //
  private static MOntoJdd mOntoJdd;

  // **************************************************************************
  // Constructeurs
  //
  /**
   * Initialisation des tests.
   */
  @BeforeAll
  public static void initTest() {
    // Initialiser jeux de données MOnto
    mOntoJdd = new MOntoJdd();
    mOntoJdd.buildO0();
    // les tables
    t0 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC00Iri(), "s0", "t0");
    t1 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC01Iri(), "s0", "t1");
    t2 = new Table(TableOrigin.ONTOTYPE,
        BuiltInOwlDatatypeSet.OwlDatatype.XSD_STRING.getType().getIri(), "s0", "t2");
    // les tables de jointure
    j0 = new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc0(), "S1", "t0-op0-t1", t0, t1,
        new Participation(ParticipationType.MIN, 2));
    j1 = new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc1(), "S1", "t0-op0-t1", t0, t1,
        new Participation(ParticipationType.SOME));
    j2 = new TableJoin(TableOrigin.DATAAXIOM, mOntoJdd.getAd0(), "S1", "t1-DP-t2", t1, t2,
        new Participation(ParticipationType.EXACT, 1));
  }

  // **************************************************************************
  // Cas de test
  //
  @Test
  public void testGetParticpation() {
    assertTrue(j0.getParticipation().getParticipationType().equals(ParticipationType.MIN));
    assertTrue(j1.getParticipation().getParticipationType().equals(ParticipationType.SOME));
    assertTrue(j2.getParticipation().getParticipationType().equals(ParticipationType.EXACT));
  }

  @Test
  public void testGetLeftTable() {
    assertTrue(j0.getLeftTable().equals(t0));
    assertTrue(j1.getLeftTable().equals(t0));
    assertTrue(j2.getLeftTable().equals(t1));
  }

  @Test
  public void testGetRightTable() {
    assertTrue(j0.getRightTable().equals(t1));
    assertTrue(j1.getRightTable().equals(t1));
    assertTrue(j2.getRightTable().equals(t2));
  }

  /**
   * Test method for {@link ca.griis.ontorela.mrel.Table#getTableOrigin()}.
   */
  public void testGetTableOrigin() {
    assertTrue(t0.getTableOrigin().equals(TableOrigin.ONTOCLASS));
    assertTrue(t1.getTableOrigin().equals(TableOrigin.ONTOCLASS));
    assertTrue(t2.getTableOrigin().equals(TableOrigin.ONTOTYPE));
    assertTrue(j0.getTableOrigin().equals(TableOrigin.CLASSAXIOM));
    assertTrue(j1.getTableOrigin().equals(TableOrigin.CLASSAXIOM));
    assertTrue(j2.getTableOrigin().equals(TableOrigin.DATAAXIOM));
  }
  // **************************************************************************
  // Opérations publiques
  //
}
