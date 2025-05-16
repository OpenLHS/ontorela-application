package ca.griis.ontorela.unit.converter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.griis.monto.builder.MontoFilter;
import ca.griis.monto.builder.owlapi.OntologyOwlApiBuilder;
import ca.griis.monto.model.Ontology;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.util.jdd.ConstructeurJdd;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * Test unitaire pour l'algorithme de traitement des axiomes de données manquants.
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2018-11-11 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-11-11
 */
public class AddValueAttributTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  protected static final Descriptor d = new Descriptor();
  private static final Set<OntoRel> ontoRelSet = new HashSet<>();

  // **************************************************************************
  // Constructeurs
  //
  // **************************************************************************
  // Constructeurs
  //
  @BeforeAll
  public static void initJdd() throws OntorelCreationException, OWLOntologyCreationException {
    d.titre("Test unitaire " + AddValueAttributTest.class.getName());
    //
    String repo = "test-data/ontoRel/";
    for (File jdd : ConstructeurJdd.creationJddParRepo(repo)) {
      List<File> yamlFiles = ConstructeurJdd.getYamlConfigurationFiles(jdd);
      if (yamlFiles.size() == 2) {
        System.out.println(ConstructeurJdd.getOntologyConfigurationFiles(jdd));
        OntologyOwlApiBuilder ontoBuilder =
            new OntologyOwlApiBuilder(ConstructeurJdd.getOntologyConfigurationFiles(jdd).get(0));
        DatabaseConfiguration dbConfig = ConfigurationLoader
            .loadDatabaseConfiguration(ConstructeurJdd.getDatabaseConfigurationFiles(jdd).get(0));
        //
        Ontology ontoFiltered = MontoFilter.filterOntology(ontoBuilder.getOntology(),
            ontoBuilder.getOwlApiOntologyConfiguration());
        OntoRel ontoRel = new OntoRel(ontoFiltered, dbConfig);
        assertNotNull(ontoRel);
        ontoRelSet.add(ontoRel);
      } else {
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
  public void test() {
    for (OntoRel ontoRel : ontoRelSet) {
      assertNotNull(ontoRel.getOntology());
      d.ajouter(ontoRel.getOntology().getIri().toString());
      //
      // ontoRel.getOntoGraph().createDotFile("jeux/ontoRel/filtre.dot", null);
      d.ajouterListe(ontoRel.getOntoGraph().getAllNodesWithoutData().stream());
    }
  }
}
