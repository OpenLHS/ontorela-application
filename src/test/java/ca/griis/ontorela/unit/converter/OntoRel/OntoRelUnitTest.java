package ca.griis.ontorela.unit.converter.OntoRel;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.monto.api.*;
import ca.griis.monto.model.OntoEntitySet;
import ca.griis.monto.model.Ontology;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.util.jdd.MOntoJdd;
import ca.griis.ontorela.util.jdd.MRelJdd;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire du convertisseur OntoRel (MOnto -> MRel).
 * Attention : OntRel est construit manuellement.
 *
 * <b>Tâches projetées</b><br>
 * TODO 2019-10-07 CK : faire un test unitaire par règle de création. Et ici tester le build
 * complet.<br>
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
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2018-09-06
 */
public class OntoRelUnitTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  protected static final Descriptor d = new Descriptor();
  private static Database db0;
  private static Ontology o0;
  private static Ontology o1;
  private static OntoRel ontoRel0;
  private static OntoRel ontoRel1;
  private static OntoRel ontoRel2;

  // **************************************************************************
  // Constructeurs
  //
  @BeforeAll
  public static void initTest() throws OntorelCreationException {
    d.titre("Test unitaire " + OntoRelUnitTest.class.getName());
    //
    MRelJdd mRelJdd = new MRelJdd();
    DatabaseConfiguration db0Config = ConfigurationLoader.loadDefaultDatabaseConfiguration();
    db0 = mRelJdd.buildDB0();
    assertNotNull(db0);
    MOntoJdd mOntoJdd = new MOntoJdd();
    o0 = mOntoJdd.buildO0();
    ontoRel0 = new OntoRel(o0, db0Config);
    // config avec RemoveThingTable =true
    o1 = mOntoJdd.buildO2();
    Path path1 = Paths.get("test-data/Integration/J0/ABC-database_config.yml");
    DatabaseConfiguration db1Config = ConfigurationLoader.loadDatabaseConfiguration(path1.toFile());
    ontoRel1 = new OntoRel(o1, db1Config);
    Path path = Paths.get("test-data/Integration/J1/ABC-database_config.yml");
    DatabaseConfiguration db2Config = ConfigurationLoader.loadDatabaseConfiguration(path.toFile());
    ontoRel2 = new OntoRel(o1, db2Config);
  }

  @AfterAll
  public static void finTest() {
    // ==== Traces
    d.ajouterListe("Liste des tables de ontorel0",
        ontoRel0.getDatabase().getBaseSchema().getTableSet().stream());
    d.ajouterListe("Liste des FK ",
        ontoRel0.getDatabase().getBaseSchema().getForeignKeySet().stream());
    d.ajouterMap("OntoRel class-table Dic", ontoRel0.getOntoRelDic().getClassTableCatalog());
    d.ajouterMap("OntoRel type-table Dic", ontoRel0.getOntoRelDic().getDataTableCatalog());
    d.ajouterMap("OntoRel dataAxiom-table Dic",
        ontoRel0.getOntoRelDic().getDataAxiomTableCatalog());
    System.out.println(d);
    // ===== Traces ontoRel1
    d.ajouterListe("Liste des tables de ontorel1",
        ontoRel1.getDatabase().getBaseSchema().getTableSet().stream());
    d.ajouterListe("Liste des FK ",
        ontoRel1.getDatabase().getBaseSchema().getForeignKeySet().stream());
    d.ajouterMap("OntoRel class-table Dic", ontoRel1.getOntoRelDic().getClassTableCatalog());
    d.ajouterMap("OntoRel type-table Dic", ontoRel1.getOntoRelDic().getDataTableCatalog());
    d.ajouterMap("OntoRel dataAxiom-table Dic",
        ontoRel1.getOntoRelDic().getDataAxiomTableCatalog());
    System.out.println(d);
    // ===== Traces ontoRel2
    d.ajouterListe("Liste des tables de ontorel2",
        ontoRel2.getDatabase().getBaseSchema().getTableSet().stream());
    d.ajouterListe("Liste des FK ",
        ontoRel2.getDatabase().getBaseSchema().getForeignKeySet().stream());
    d.ajouterMap("OntoRel class-table Dic", ontoRel2.getOntoRelDic().getClassTableCatalog());
    d.ajouterMap("OntoRel type-table Dic", ontoRel2.getOntoRelDic().getDataTableCatalog());
    d.ajouterMap("OntoRel dataAxiom-table Dic",
        ontoRel2.getOntoRelDic().getDataAxiomTableCatalog());
    System.out.println(d);
  }
  // **************************************************************************
  // Cas de test
  //

  @Test
  public void testGetDatabase() {
    assertNotNull(ontoRel0.getDatabase());
    assertNotNull(ontoRel1.getDatabase());
    assertNotNull(ontoRel2.getDatabase());

    assertEquals(1, ontoRel0.getDatabase().getSchemaSet().size());
    assertEquals(1, ontoRel1.getDatabase().getSchemaSet().size());
    assertEquals(1, ontoRel2.getDatabase().getSchemaSet().size());
    assertNotNull(ontoRel0.getDatabase().getBaseSchema());
    assertNotNull(ontoRel1.getDatabase().getBaseSchema());
    assertNotNull(ontoRel2.getDatabase().getBaseSchema());

    d.ajouter(ontoRel0.getDatabase().toString());
    d.ajouter(ontoRel1.getDatabase().toString());
    d.ajouter(ontoRel2.getDatabase().toString());
  }

  @Test
  public void testGetOntology() {
    assertNotNull(ontoRel0.getOntology());
    assertNotNull(ontoRel1.getOntology());
    assertNotNull(ontoRel2.getOntology());

    d.ajouter(ontoRel0.getOntology().getIri().toString());
    d.ajouter(ontoRel1.getOntology().getIri().toString());
    d.ajouter(ontoRel2.getOntology().getIri().toString());
  }

  @Test
  public void testGetDic() {
    assertNotNull(ontoRel0.getOntoRelDic());
    assertNotNull(ontoRel1.getOntoRelDic());
    assertNotNull(ontoRel2.getOntoRelDic());
  }

  @Test
  public void testCreateDomainSet() {
    // =================== Test ontorel0
    OntoEntitySet<OntoDatatypeI> dSet =
        (OntoEntitySet<OntoDatatypeI>) ontoRel0.getOntology().getOntoDatatypeSet();
    ontoRel0.createDomainSet(dSet, ontoRel0.getDatabase().getBaseSchema());
    d.ajouterListe("Liste des domaines ontoRel0",
        ontoRel0.getDatabase().getBaseSchema().getTypeSet().stream());
    assertEquals(dSet.size(), ontoRel0.getDatabase().getBaseSchema().getTypeSet().size() - 2);
    // =================== Test ontorel1
    OntoEntitySet<OntoDatatypeI> dSet1 =
        (OntoEntitySet<OntoDatatypeI>) ontoRel1.getOntology().getOntoDatatypeSet();
    ontoRel1.createDomainSet(dSet, ontoRel1.getDatabase().getBaseSchema());
    d.ajouterListe("Liste des domaines ontoRel1",
        ontoRel1.getDatabase().getBaseSchema().getTypeSet().stream());
    assertEquals(dSet1.size(), ontoRel1.getDatabase().getBaseSchema().getTypeSet().size() - 2);
  }

  @Test
  public void testCreateClassTableSet() {
    // ================ CAS 0
    assertEquals(1, ontoRel0.getDatabase().getBaseSchema().getTableSet().size());
    d.ajouter("ontoRel0 table set " + ontoRel0.getDatabase().getBaseSchema().getTableSet());
    OntoEntitySet<OntoClassI> cSet =
        (OntoEntitySet<OntoClassI>) ontoRel0.getOntology().getOntoClassSet();
    Set<OntoAxiomClassInheritanceI> isaSet =
        ontoRel0.getOntology().getOntoAxiomSet().getClassInheritenceAxioms();
    ontoRel0.createClassTableSet(cSet, isaSet, ontoRel0.getDatabase().getBaseSchema());
    assertEquals(cSet.size(), ontoRel0.getDatabase().getBaseSchema().getTableSet().size());
    // FK C00-Thing, Fk C01-Thing, Fk C01-C02
    assertEquals(isaSet.size() + 2,
        ontoRel0.getDatabase().getBaseSchema().getForeignKeySet().size());
    // ================ CAS 1 Thing n est pas crée
    assertEquals(0, ontoRel1.getDatabase().getBaseSchema().getTableSet().size());
    OntoEntitySet<OntoClassI> cSet1 =
        (OntoEntitySet<OntoClassI>) ontoRel1.getOntology().getOntoClassSet();
    Set<OntoAxiomClassInheritanceI> isaSet1 =
        ontoRel1.getOntology().getOntoAxiomSet().getClassInheritenceAxioms();
    ontoRel1.createClassTableSet(cSet1, isaSet1, ontoRel1.getDatabase().getBaseSchema());
    ontoRel2.createClassTableSet(cSet1, isaSet1, ontoRel2.getDatabase().getBaseSchema());
    assertEquals(cSet1.size(), ontoRel1.getDatabase().getBaseSchema().getTableSet().size());
    assertEquals(isaSet1.size(), ontoRel1.getDatabase().getBaseSchema().getForeignKeySet().size());
  }

  @Test
  public void testCreateClassAxiomTableSet() {
    // CAS0 - Table Thing est crée
    OntoEntitySet<OntoClassI> cSet =
        (OntoEntitySet<OntoClassI>) ontoRel0.getOntology().getOntoClassSet();

    int nbTableBefore = ontoRel0.getDatabase().getBaseSchema().getTableSet().size();
    int nbFKBefore = ontoRel0.getDatabase().getBaseSchema().getForeignKeySet().size();
    System.out.println("before");
    System.out.println(ontoRel0.getDatabase().getBaseSchema().getTableSet());

    ontoRel0.createObjectPropertyTableSet(ontoRel0.getOntology().getOntoObjectPropertieSet(),
        ontoRel0.getDatabase().getBaseSchema());
    ontoRel0.createClassAxiomTableSet(cSet, ontoRel0.getDatabase().getBaseSchema());

    int nbTableAfter = ontoRel0.getDatabase().getBaseSchema().getTableSet().size();
    int nbFKAfter = ontoRel0.getDatabase().getBaseSchema().getForeignKeySet().size();
    assertEquals(ontoRel0.getOntology().getOntoAxiomSet().getClassAxioms().size() + nbTableBefore
        + ontoRel0.getOntology().getOntoObjectPropertieSet().size() - 1, nbTableAfter);
    assertTrue(nbFKBefore < nbFKAfter);
    // CAS1 - Table Thing n est pas crée
    OntoEntitySet<OntoClassI> cSet1 =
        (OntoEntitySet<OntoClassI>) ontoRel1.getOntology().getOntoClassSet();
    d.ajouterListe("Liste des tables de avant ",
        ontoRel1.getDatabase().getBaseSchema().getTableSet().stream());
    ontoRel1.createObjectPropertyTableSet(ontoRel1.getOntology().getOntoObjectPropertieSet(),
        ontoRel1.getDatabase().getBaseSchema());
    ontoRel2.createObjectPropertyTableSet(ontoRel2.getOntology().getOntoObjectPropertieSet(),
        ontoRel2.getDatabase().getBaseSchema());
    ontoRel1.createClassAxiomTableSet(cSet1, ontoRel1.getDatabase().getBaseSchema());
    ontoRel2.createClassAxiomTableSet(cSet1, ontoRel2.getDatabase().getBaseSchema());
    int nbFKAfter1 = ontoRel1.getDatabase().getBaseSchema().getForeignKeySet().size();
    assertEquals(7, nbFKAfter1);
  }

  @Test
  public void testCreateDataAxiomTableSet() {
    // CAS 0 removeThingTable = false
    Set<OntoAxiomDataAssociationI> dataAxiomSet =
        ontoRel0.getOntology().getOntoAxiomSet().getDataAxioms();

    int nbTableBefore = ontoRel0.getDatabase().getBaseSchema().getTableSet().size();
    int nbFKBefore = ontoRel0.getDatabase().getBaseSchema().getForeignKeySet().size();

    ontoRel0.createDataAxiomTableSet(dataAxiomSet, ontoRel0.getDatabase().getBaseSchema());

    int nbTableAfter = ontoRel0.getDatabase().getBaseSchema().getTableSet().size();
    int nbFKAfter = ontoRel0.getDatabase().getBaseSchema().getForeignKeySet().size();

    assertTrue(dataAxiomSet.size() + nbTableBefore <= nbTableAfter);
    assertTrue(nbFKBefore < nbFKAfter);
    // CAS 1 removeThingTable = true et normalizeDataType= false
    Set<OntoAxiomDataAssociationI> dataAxiomSet1 =
        ontoRel1.getOntology().getOntoAxiomSet().getDataAxioms();

    int nbTableBefore1 = ontoRel1.getDatabase().getBaseSchema().getTableSet().size();
    int nbFKBefore1 = ontoRel1.getDatabase().getBaseSchema().getForeignKeySet().size();

    ontoRel1.createDataAxiomTableSet(dataAxiomSet1, ontoRel1.getDatabase().getBaseSchema());

    int nbTableAfter1 = ontoRel1.getDatabase().getBaseSchema().getTableSet().size();
    int nbFKAfter1 = ontoRel1.getDatabase().getBaseSchema().getForeignKeySet().size();

    assertEquals(dataAxiomSet1.size() + nbTableBefore1, nbTableAfter1);
    assertEquals(nbFKBefore1 + dataAxiomSet1.size(), nbFKAfter1);
    // CAS 2 removeThingTable = true et normalizeDataType= true
    ontoRel2.createDataAxiomTableSet(dataAxiomSet1, ontoRel2.getDatabase().getBaseSchema());

    int nbTableAfter2 = ontoRel2.getDatabase().getBaseSchema().getTableSet().size();
    int nbFKAfter2 = ontoRel2.getDatabase().getBaseSchema().getForeignKeySet().size();

    assertEquals(2 * dataAxiomSet1.size() + nbTableBefore1, nbTableAfter2);
    assertEquals(nbFKBefore1 + 2 * dataAxiomSet1.size(), nbFKAfter2);
  }

  // **************************************************************************
  // Opérations publiques
  //
}
