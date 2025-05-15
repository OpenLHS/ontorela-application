package ca.griis.ontorela.unit.converter;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.monto.api.OntoAxiomClassAssociationI;
import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.builder.MontoFilter;
import ca.griis.monto.builder.owlapi.OntologyOwlApiBuilder;
import ca.griis.monto.model.Ontology;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.TableJoin;
import ca.griis.ontorela.mrel.catalog.MrelCatalog;
import java.io.File;
import java.util.Map.Entry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * Test unitaire évolution des identifiants ontoRel.
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
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2019-11-20
 */
public class OntoRelEvolutionTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static Descriptor d;
  private static File jddRepo;
  private static File outRepo;

  // **************************************************************************
  // Constructeurs
  //
  @BeforeAll
  public static void initJdd() {
    d = new Descriptor();
    d.titre("Test unitaire " + OntoRelEvolutionTest.class.getName());
    jddRepo = new File("test-data/evolution/ABC");
    outRepo = new File("build/test-results/" + d.obtenirDate() + "/OntoRelEvolutionTest");
    if (!outRepo.exists()) {
      boolean out = outRepo.mkdirs();
      if (!out) {
        System.err.println("Impossible de créer le fichier : " + outRepo.getAbsolutePath());
      }
    }
    d.ajouter(" Résultats des essais : " + outRepo.getAbsolutePath());
  }

  @AfterAll
  public static void finTest() {
    d.creerFichier(outRepo.getAbsolutePath());
    System.out.println(d);
  }

  // **************************************************************************
  // Cas de test
  // Stratégie :
  // 1) Vérifier la création d'objets OntoRel en tenant compte d'un OntoRelDic existant
  // spécifié dans les paramètres.
  // a) Sans ajout d'entités dans la configuration
  // b) Avec ajout d'entités dans la configuration
  // c) Avec retrait de classes dans la configuration
  // d) b) + avec ajout d'axiomes dans l'ontologie pour une ou plusieurs entités d'intérêt
  // e) c) + avec ajout d'axiomes dans l'ontologie pour une ou plusieurs entités d'intérêt
  // 2) Vérifier la création d'objets OntoRel sans OntoRelDic
  //

  /**
   * Méthode :
   * Créer un ontoRel avec un OntoRelDic.
   * Vérifier que les identifiant des tables sont associée à des objets identiques.
   */
  @Test
  public void testAConfigIdem() throws OntorelCreationException, OWLOntologyCreationException {
    d.soustitre(" Test evolution - config identiques ");
    //
    File ontoConfigFile = new File(jddRepo.getAbsoluteFile() + "/ontology_config.yml");
    File dbConfigFile = new File(jddRepo.getAbsoluteFile() + "/database_config-withDic.yml");
    OntoRel ontoRel = buildOntoRel(ontoConfigFile, dbConfigFile);
    assertNotNull(ontoRel);
    assertNotNull(ontoRel.getOntoRelDic());
    // ==== Imprimer le contenu du dictionnaire généré
    addOntoRelDescription(ontoRel);
    // ==== Vérifier le contenu des dictionnaires
    // == Vérifier les classes
    verifyClasseSet(ontoRel);
    // == Vérifier les axiomes
    verifyAxiomSet(ontoRel);
  }

  /**
   * Méthode :
   * Créer un ontoRel avec une configuration qui contient des classes qui ne sont pas dans
   * l'OntoRelDic passé en paramètre
   * Vérifier que les identifiant des tables sont associée à des objets identiques.
   */
  @Test
  public void testBAddEntity() throws OntorelCreationException, OWLOntologyCreationException {
    d.soustitre(" Test evolution - config ajout d'entités ");
    File ontoConfigFile = new File(jddRepo.getAbsoluteFile() + "/ontology_config-addEntities.yml");
    File dbConfigFile = new File(jddRepo.getAbsoluteFile() + "/database_config-withDic.yml");
    OntoRel ontoRel = buildOntoRel(ontoConfigFile, dbConfigFile);
    // ontoRel.genereteScripts(outRepo.getAbsolutePath());
    assertNotNull(ontoRel);
    assertNotNull(ontoRel.getOntoRelDic());
    // ==== Imprimer le contenu du dictionnaire généré
    addOntoRelDescription(ontoRel);
    // ==== Vérifier le contenu des dictionnaires
    // == Vérifier les classes
    verifyClasseSet(ontoRel);
    // == Vérifier les axiomes
    verifyAxiomSet(ontoRel);
  }

  /**
   * Méthode :
   * Créer un ontoRel avec une configuration qui contient des classes qui ne sont pas dans
   * l'OntoRelDic passé en paramètre
   * Vérifier que les identifiant des tables sont associée à des objets identiques.
   */
  @Test
  public void testCRemoveEntity() throws OntorelCreationException, OWLOntologyCreationException {
    d.soustitre(" Test evolution - config retrait entités ");
    File ontoConfigFile = new File(jddRepo.getAbsoluteFile() + "/ontology_config-remEntities.yml");
    File dbConfigFile = new File(jddRepo.getAbsoluteFile() + "/database_config-withDic.yml");
    OntoRel ontoRel = buildOntoRel(ontoConfigFile, dbConfigFile);
    assertNotNull(ontoRel);
    assertNotNull(ontoRel.getOntoRelDic());
    // ==== Imprimer le contenu du dictionnaire généré
    addOntoRelDescription(ontoRel);
    // ==== Vérifier le contenu des dictionnaires
    // == Vérifier les classes
    verifyClasseSet(ontoRel);
    // == Vérifier les axiomes
    verifyAxiomSet(ontoRel);
  }

  /**
   * Méthode :
   * Créer un ontoRel avec une configuration qui contient des classes qui ne sont pas dans
   * l'OntoRelDic passé en paramètre
   * Vérifier que les identifiant des tables sont associée à des objets identiques.
   */
  @Test
  public void testDAddAxiom() throws OntorelCreationException, OWLOntologyCreationException {
    d.soustitre(" Test evolution - onto ajout axiom et config idem " + jddRepo.getAbsolutePath());
    File ontoConfigFile = new File(jddRepo.getAbsoluteFile() + "/ontology_config-addAxiom.yml");
    File dbConfigFile = new File(jddRepo.getAbsoluteFile() + "/database_config-withThing.yml");
    File dbConfigFile1 = new File(jddRepo.getAbsoluteFile() + "/database_config-withoutThing.yml");
    OntoRel ontoRel0 = buildOntoRel(ontoConfigFile, dbConfigFile);
    OntoRel ontoRel1 = buildOntoRel(ontoConfigFile, dbConfigFile1);
    // ontoRel.genereteScripts(outRepo.getAbsolutePath());
    assertNotNull(ontoRel0);
    assertNotNull(ontoRel1);
    assertNotNull(ontoRel0.getOntoRelDic());
    assertNotNull(ontoRel1.getOntoRelDic());
    // ==== Imprimer le contenu du dictionnaire généré
    addOntoRelDescription(ontoRel0);
    addOntoRelDescription(ontoRel1);
    // ==== Vérifier le contenu des dictionnaires
    // == Vérifier les classes
    verifyClasseSet(ontoRel0);
    verifyClasseSet(ontoRel1);
    // == Vérifier les axiomes
    verifyAxiomSet(ontoRel0);
    verifyAxiomSet(ontoRel1);
  }

  // **************************************************************************
  // Opérations publiques
  //
  public void verifyClasseSet(OntoRel ontoRel) {
    for (Entry<OntoClassI, Table> e : ontoRel.getOntoRelDic().getClassTableCatalog().entrySet()) {
      // vérifier correspondance des identifiants
      if (MrelCatalog.getClassIriTableId().get(e.getKey().getIri().getShortIri()) != null) {
        assertEquals(MrelCatalog.getClassIriTableId().get(e.getKey().getIri().getShortIri()),
            e.getValue().getIdentifier().getValue());
      }
      // vérifier la définition de l'ensemble d'attributs des tables
      Table t = ontoRel.getOntoRelDic().getClassTableCatalog().get(e.getKey());
      assertEquals(t.getAttributeSet(), e.getValue().getAttributeSet());
      // vérifier correspondance des identifiants des axiomes
    }
  }

  public void verifyAxiomSet(OntoRel ontoRel) {
    for (Entry<OntoAxiomClassAssociationI, TableJoin> e : ontoRel.getOntoRelDic()
        .getClassAxiomTableCatalog().entrySet()) {
      // vérifier correspondance des identifiants
      // MrelCatalog.getOntoAxiomRelvarMap().
      assertTrue(ontoRel.getOntoRelDic().getClassAxiomTableCatalog().entrySet().contains(e));
      // vérifier la définition de l'ensemble d'attributs des tables
      Table t = ontoRel.getOntoRelDic().getClassAxiomTableCatalog().get(e.getKey());
      assertEquals(t.getAttributeSet(), e.getValue().getAttributeSet());
    }
  }

  public OntoRel buildOntoRel(File ontoConfigFile, File dbConfigFile)
      throws OntorelCreationException, OWLOntologyCreationException {
    DatabaseConfiguration dbConfig = ConfigurationLoader.loadDatabaseConfiguration(dbConfigFile);
    //
    OntoRel ontoRel = new OntoRel(loadOntology(ontoConfigFile), dbConfig);
    ontoRel.buildOntoRel();
    return ontoRel;
  }

  public Ontology loadOntology(File ontConfigFile) throws OWLOntologyCreationException {
    OntologyOwlApiBuilder ontoBuilder = new OntologyOwlApiBuilder(ontConfigFile);
    Ontology ontoFiltered = MontoFilter.filterOntology(ontoBuilder.getOntology(),
        ontoBuilder.getOwlApiOntologyConfiguration());
    return ontoFiltered;
  }

  public void addOntoRelDescription(OntoRel ontoRel) {
    d.ajouter("OntoRel ");
    for (Entry<OntoClassI, Table> e : ontoRel.getOntoRelDic().getClassTableCatalog().entrySet()) {
      d.ajouter(
          "  " + e.getValue().getIdentifier().getValue() + "=" + e.getKey().getIri().getFullIri());
    }
    for (Entry<OntoAxiomClassAssociationI, TableJoin> e : ontoRel.getOntoRelDic()
        .getClassAxiomTableCatalog().entrySet()) {
      d.ajouter("  " + e.getValue().getIdentifier().getValue() + "=" + "["
          + e.getKey().getOntoDeterminant().getIri().getShortIri() + ";"
          + e.getKey().getProperty().getIri().getShortIri() + ";"
          + e.getKey().getOntoDependent().getIri().getShortIri() + "]");
    }
  }
}
