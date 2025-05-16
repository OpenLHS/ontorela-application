package ca.griis.ontorela.unit.converter.OntoRel;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.monto.api.*;
import ca.griis.monto.builder.MontoFilter;
import ca.griis.monto.builder.MontoRedundancyReducer;
import ca.griis.monto.builder.owlapi.OntologyOwlApiBuilder;
import ca.griis.monto.model.OntoEntitySet;
import ca.griis.monto.model.Ontology;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.mrel.Table.TableOrigin;
import ca.griis.ontorela.util.jdd.ConstructeurJdd;
import java.io.File;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test d'intégration du convertisseur OntoRel (MOnto -> MRel).
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2024-04-05 (0.2.0) [AS] Adaptation des tests avec la configuration RemoveThingTable <br>
 * 2018-09-06 (0.1.0) [CK] Mise en oeuvre initiale. <br>
 *
 * <p>
 * <b>Copyright</b> 2016-2017, GRIIS (<a href="https://griis.ca/">...</a>) <br>
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
public class OntoRelTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  protected static final Descriptor d = new Descriptor();
  private static final String repo = "test-data/Integration";

  // **************************************************************************
  // Constructeurs
  //
  @BeforeAll
  public static void initDataSet() {
    d.titre("Test unitaire " + OntoRelTest.class.getName());
    // Vérifier que les jeux de données contiennent les fichiers de configurations
    for (File jdd : ConstructeurJdd.creationJddParRepo(repo)) {
      List<File> yamlFiles = ConstructeurJdd.getYamlConfigurationFiles(jdd);
      if (yamlFiles.size() < 2) {
        System.err.println("DataSet directory : " + jdd + " must contain 2 yamlFile");
      }
    }
  }

  @AfterAll
  public static void finTest() {
    System.out.println(d);
  }
  // **************************************************************************
  // Cas de test
  //

  @Test
  public void testGetDatabase() throws OntorelCreationException {
    for (File jdd : ConstructeurJdd.creationJddParRepo(repo)) {
      d.ajouter(" >> TestGetDatabase de " + jdd.getAbsolutePath());
      // ==================== Initialisation d'un OntoRel
      OntoRel ontoRel = getOntoRel(jdd);
      // ==================== BEGIN Cas de tests
      assertNotNull(ontoRel.getDatabase());
      assertEquals(1, ontoRel.getDatabase().getSchemaSet().size());
      assertNotNull(ontoRel.getDatabase().getBaseSchema());
      // ==================== END Cas de tests
      d.ajouter(ontoRel.getDatabase().toString());
    }
  }

  @Test
  public void testGetOntology() throws OntorelCreationException {
    for (File jdd : ConstructeurJdd.creationJddParRepo(repo)) {
      d.ajouter(" >> TestGetOntology de " + jdd.getAbsolutePath());
      // ==================== Initialisation d'un OntoRel
      OntoRel ontoRel = getOntoRel(jdd);
      // ==================== BEGIN Cas de tests
      assertNotNull(ontoRel.getOntology());
      // ==================== END Cas de tests
      d.ajouter(ontoRel.getOntology().getIri().toString());
    }
  }

  @Test
  public void testCreateDomainSet() throws OntorelCreationException {
    for (File jdd : ConstructeurJdd.creationJddParRepo(repo)) {
      d.ajouter(" >> TestCreateDomainSet de " + jdd.getAbsolutePath());
      // ==================== Initialisation d'un OntoRel
      OntoRel ontoRel = getOntoRel(jdd);
      // ==================== BEGIN Cas de tests
      OntoEntitySet<OntoDatatypeI> dSet =
          (OntoEntitySet<OntoDatatypeI>) ontoRel.getOntology().getOntoDatatypeSet();
      ontoRel.createDomainSet(dSet, ontoRel.getDatabase().getBaseSchema());
      /*
       * d.ajouterListe("Liste des domaines " + ontoRel.getOntology().getIri().getShortIri(),
       * ontoRel.getDatabase().getBaseSchema().getTypeSet().stream());
       */
      assertEquals(dSet.size(), ontoRel.getDatabase().getBaseSchema().getTypeSet().size() - 2);
      // ==================== END Cas de tests
    }
  }

  @Test
  public void testCreateClassTableSet() throws OntorelCreationException {
    for (File jdd : ConstructeurJdd.creationJddParRepo(repo)) {
      d.ajouter(" >> TestCreateClassTableSet de " + jdd.getAbsolutePath());
      // ==================== Initialisation d'un OntoRel
      OntoRel ontoRel = getOntoRel(jdd);
      d.ajouter("Configuration de RemoveThingTable = "
          + ontoRel.getDatabaseConfiguration().getRemoveThingTable());
      // ==================== BEGIN Cas de tests
      // Test sur création ou non de la table Thing
      if (ontoRel.getDatabaseConfiguration().getRemoveThingTable()) {
        // Thing non crée
        assertEquals(0, ontoRel.getDatabase().getBaseSchema().getTableSet().size());
      } else {
        // table thing est crée
        assertEquals(1, ontoRel.getDatabase().getBaseSchema().getTableSet().size());
      }
      //
      OntoEntitySet<OntoClassI> cSet =
          (OntoEntitySet<OntoClassI>) ontoRel.getOntology().getOntoClassSet();
      Set<OntoAxiomClassInheritanceI> isaSet =
          ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms();
      //
      ontoRel.createClassTableSet(cSet, isaSet, ontoRel.getDatabase().getBaseSchema());
      d.ajouterListe("Liste des tables de  " + ontoRel.getOntology().getIri().getShortIri(),
          ontoRel.getDatabase().getBaseSchema().getTableSet().stream());
      // test sur table Thing
      if (ontoRel.getDatabaseConfiguration().getRemoveThingTable()) {

        assertEquals(cSet.size(), ontoRel.getDatabase().getBaseSchema().getTableSet().size());
      } else {
        assertEquals(cSet.size() + 1, ontoRel.getDatabase().getBaseSchema().getTableSet().size());
      }
      // relation des classe isA
      d.ajouterListe("Liste des FK " + ontoRel.getOntology().getIri().getShortIri(),
          ontoRel.getDatabase().getBaseSchema().getForeignKeySet().stream());
      assertTrue(isaSet.size() <= ontoRel.getDatabase().getBaseSchema().getForeignKeySet().size());
      // ==================== END Cas de tests
    }
  }

  @Test
  public void testCreateDataAxiomTableSet() throws OntorelCreationException {
    for (File jdd : ConstructeurJdd.creationJddParRepo(repo)) {
      d.ajouter(" >> TestCreateDataAxiomTableSet de " + jdd.getAbsolutePath());
      // ==================== Initialisation d'un OntoRel
      OntoRel ontoRel = getOntoRel(jdd);
      // ==================== BEGIN Cas de tests
      OntoEntitySet<OntoClassI> cSet =
          (OntoEntitySet<OntoClassI>) ontoRel.getOntology().getOntoClassSet();
      Set<OntoAxiomClassInheritanceI> isaSet =
          ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms();
      Set<OntoAxiomDataAssociationI> dataAxiomSet =
          ontoRel.getOntology().getOntoAxiomSet().getDataAxioms();
      //
      ontoRel.createClassTableSet(cSet, isaSet, ontoRel.getDatabase().getBaseSchema());
      ontoRel.createDataAxiomTableSet(dataAxiomSet, ontoRel.getDatabase().getBaseSchema());

      d.ajouterListe("Liste des tables " + ontoRel.getOntology().getIri().getShortIri(),
          ontoRel.getDatabase().getBaseSchema().getTableSet().stream());
      d.ajouterListe("Liste des FK " + ontoRel.getOntology().getIri().getShortIri(),
          ontoRel.getDatabase().getBaseSchema().getForeignKeySet().stream());
      //
      if (ontoRel.getDatabaseConfiguration().getRemoveThingTable()) {
        if (ontoRel.getDatabaseConfiguration().getNormalizeDatatype()) {
          assertEquals(
              cSet.size() + 2L * dataAxiomSet.size(),
              ontoRel.getDatabase().getBaseSchema().getTableSet().size());
        }
        // normalizeDataType = false
        else {
          assertEquals(
              cSet.size() + dataAxiomSet.size(),
              ontoRel.getDatabase().getBaseSchema().getTableSet().size());
        }
        // table thing est crée
      } else {
        if (ontoRel.getDatabaseConfiguration().getNormalizeDatatype()) {
          assertEquals(
              cSet.size() + 2L * dataAxiomSet.size() + 1,
              ontoRel.getDatabase().getBaseSchema().getTableSet().size());
        }
        // normalizeDataType = false
        else {
          assertEquals(
              cSet.size() + 1 + dataAxiomSet.size(),
              ontoRel.getDatabase().getBaseSchema().getTableSet().size());
        }
      }
      assertTrue(isaSet.size() <= ontoRel.getDatabase().getBaseSchema().getForeignKeySet().size());
      // ==================== END Cas de tests
    }
  }

  @Test
  public void testCreateClassAxiomTableSet() throws OntorelCreationException {
    for (File jdd : ConstructeurJdd.creationJddParRepo(repo)) {
      d.ajouter(" >> TestCreateClassAxiomTableSet de " + jdd.getAbsolutePath());
      // ==================== Initialisation d'un OntoRel
      OntoRel ontoRel = getOntoRel(jdd);
      // ==================== BEGIN Cas de tests
      OntoEntitySet<OntoClassI> cSet =
          (OntoEntitySet<OntoClassI>) ontoRel.getOntology().getOntoClassSet();
      OntoEntityCollectionI<OntoObjectPropertyI> opSet =
          ontoRel.getOntology().getOntoObjectPropertieSet();
      Set<OntoAxiomClassInheritanceI> isaSet =
          ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms();
      Set<OntoAxiomClassAssociationI> classAxiomSet =
          ontoRel.getOntology().getOntoAxiomSet().getClassAxioms();
      //
      /*
       * d.ajouterListe("Liste des tables  sans op " + ontoRel.getOntology().getIri().getShortIri(),
       * ontoRel.getDatabase().getBaseSchema().getTableSet().stream());
       */
      ontoRel.createClassTableSet(cSet, isaSet, ontoRel.getDatabase().getBaseSchema());
      //
      /*
       * d.ajouterListe("Liste des tables  apres op " +
       * ontoRel.getOntology().getIri().getShortIri(),
       * ontoRel.getDatabase().getBaseSchema().getTableSet().stream());
       */
      if (ontoRel.getDatabaseConfiguration().getGenerateOpTable()) {
        int opSetDR = (int) opSet.parallelStream()
            .filter(o -> (!o.getRange().isEmpty() || !o.getSuperObjectProperty().isEmpty() &&
                o.getSuperObjectProperty().stream()
                    .anyMatch(sp -> sp.getRange().stream().findAny().isPresent()))
                && (!o.getDomain().isEmpty() || !o.getSuperObjectProperty().isEmpty() &&
                    o.getSuperObjectProperty().stream()
                        .anyMatch(sp -> sp.getDomain().stream().findAny().isPresent())))
            .count();
        ontoRel.createObjectPropertyTableSet(opSet, ontoRel.getDatabase().getBaseSchema());

        if (ontoRel.getDatabaseConfiguration().getRemoveThingTable()) {
          assertEquals(opSetDR, ontoRel.getDatabase().getBaseSchema().getTableSet().stream()
              .filter(t -> t.getTableOrigin().equals(TableOrigin.OBJECTPROPERTY)).count());

          /*
           * d.ajouter("op set   "+ opSet.size());
           * d.ajouterListe("Liste des tables  apres create op " +
           * ontoRel.getOntology().getIri().getShortIri(),
           * ontoRel.getDatabase().getBaseSchema().getTableSet().stream());
           */
        } else {
          /*
           * d.ajouter("op set   "+ opSet.size());
           * d.ajouter("Liste des tables  apres  op " +
           * ontoRel.getDatabase().getBaseSchema().getTableSet().stream().
           * filter(t -> t.getTableOrigin().equals(TableOrigin.OBJECTPROPERTY)).count());
           */
          assertEquals(opSet.size(), (int) ontoRel.getDatabase().getBaseSchema().getTableSet()
              .stream().filter(t -> t.getTableOrigin().equals(TableOrigin.OBJECTPROPERTY)).count());
        }
      }
      //
      ontoRel.createClassAxiomTableSet(cSet, ontoRel.getDatabase().getBaseSchema());

      d.ajouterListe(
          "Liste des tables  avec axiomes " + ontoRel.getOntology().getIri().getShortIri(),
          ontoRel.getDatabase().getBaseSchema().getTableSet().stream());
      d.ajouterListe("Liste des FK " + ontoRel.getOntology().getIri().getShortIri(),
          ontoRel.getDatabase().getBaseSchema().getForeignKeySet().stream());

      assertTrue(
          cSet.size() + classAxiomSet.size() >= ontoRel.getDatabase().getBaseSchema().getTableSet()
              .stream().filter(t -> t.getTableOrigin().equals(TableOrigin.CLASSAXIOM)).count());
      assertTrue(isaSet.size() <= ontoRel.getDatabase().getBaseSchema().getForeignKeySet().size());
      // ==================== END Cas de tests
    }
  }

  // **************************************************************************
  // Opérations propres
  //
  private OntoRel getOntoRel(File jdd) throws OntorelCreationException {
    OntologyOwlApiBuilder ontoBuilder =
        new OntologyOwlApiBuilder(ConstructeurJdd.getOntologyConfigurationFiles(jdd).get(0));
    DatabaseConfiguration dbConfig = ConfigurationLoader
        .loadDatabaseConfiguration(ConstructeurJdd.getDatabaseConfigurationFiles(jdd).get(0));
    //
    Ontology ontoFiltered = MontoFilter.filterOntology(ontoBuilder.getOntology(),
        ontoBuilder.getOwlApiOntologyConfiguration());
    MontoRedundancyReducer reducer =
        new MontoRedundancyReducer(ontoFiltered, ontoBuilder.getOwlApiOntologyConfiguration());
    Ontology ontoReduced = reducer.getReducedOntology();
    OntoRel ontoRel = new OntoRel(ontoReduced, dbConfig);
    assertNotNull(ontoRel);
    return ontoRel;
  }
}
