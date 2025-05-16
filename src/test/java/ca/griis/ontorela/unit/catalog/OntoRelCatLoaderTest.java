package ca.griis.ontorela.unit.catalog;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.catalog.OntoRelCatLoader;
import ca.griis.ontorela.mrel.catalog.MrelCatalog;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire de chargement d'un OntoRelCat
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-11-20 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2019-11-20
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */
public class OntoRelCatLoaderTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static Descriptor d = new Descriptor();

  // **************************************************************************
  // Constructeurs
  //
  @BeforeAll
  public static void initJdd() {
    d.titre("Test unitaire " + OntoRelCatLoaderTest.class.getName());
  }

  @AfterAll
  public static void finTest() {
    System.out.println(d);
  }

  // **************************************************************************
  // Cas de test
  //
  @Test
  public void testLoadOntoRelDicWithoutType() {
    //
    assertTrue(MrelCatalog.getClassIriTableId().isEmpty());
    assertTrue(MrelCatalog.getOntoAxiomRelvarMap().isEmpty());
    //
    String ontoRelFile = "test-data/ontoRelDic/OntoRelDic_wType.json";
    OntoRelCatLoader.loadOntoRelCat(ontoRelFile);
    d.ajouterMap("IRI-TableId", MrelCatalog.getClassIriTableId());
    d.ajouterMap("Axiom-TableId", MrelCatalog.getOntoAxiomRelvarMap());
    //
    assertFalse(MrelCatalog.getClassIriTableId().isEmpty());
    assertFalse(MrelCatalog.getOntoAxiomRelvarMap().isEmpty());
    // System.out.println(d);
  }
  // **************************************************************************
  // Opérations publiques
  //
}
