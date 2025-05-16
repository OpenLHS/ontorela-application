package ca.griis.ontorela.integration.ipm;

import static ca.griis.ontorela.ipm.Interface.getAnomaliesDatabaseIdentifiersReport;
import static ca.griis.ontorela.ipm.Interface.getAnomaliesOntologyReport;
import static ca.griis.ontorela.ipm.Interface.getClassList;
import static ca.griis.ontorela.ipm.Interface.getDatabaseTableIds;
import static ca.griis.ontorela.ipm.Interface.getInitialDataPropertiesList;
import static ca.griis.ontorela.ipm.Interface.getInitialObjectPropertiesList;
import static ca.griis.ontorela.ipm.Interface.getObjectPropertiesList;
import static ca.griis.ontorela.ipm.Interface.getSubClassList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.core.ConfigurationsSarter;
import ca.griis.ontorela.core.ExecutionDescriptor;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.ipm.SubIriModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;


/**
 * Test unitaire de la classe Interface (communication avec l'IPM)
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * ..<br>
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
 * @since 2019-03-16
 */
public class InterfaceTest {
  private Descriptor msg = new Descriptor();
  private Date date = new Date();
  private String dateString = new SimpleDateFormat("yyyyMMdd-HHmm").format(date);
  private ExecutionDescriptor executionDescriptor = new ExecutionDescriptor(msg, dateString);

  // **************************************************************************
  // Cas de test
  //
  @Test
  public void testGetAnomaliesOntologyReport()
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    File testDir = new File("test-data/_ontologies/MONDIAL/config00/");

    assertTrue(testDir.exists(), "Le répertoire de test doit exister");
    assertTrue(testDir.isDirectory(), "Le répertoire de test doit être un dossier");

    String result = getAnomaliesOntologyReport(testDir);

    assertNotNull(result, "Le rapport d'anomalies ne doit pas être null");
    assertFalse(result.isEmpty(), "Le rapport d'anomalies ne doit pas être vide");
  }

  @Test
  public void testGetAnomaliesDatabaseIdentifiersReport()
      throws IOException, OntorelCreationException, OWLOntologyCreationException {
    File testDir = new File("test-data/_ontologies/MONDIAL/config00/");

    assertTrue(testDir.exists(), "Le répertoire de test doit exister");
    assertTrue(testDir.isDirectory(), "Le répertoire de test doit être un dossier");

    String result = getAnomaliesDatabaseIdentifiersReport(testDir);

    assertNotNull(result, "Le rapport d'anomalies ne doit pas être null");
    assertFalse(result.isEmpty(), "Le rapport d'anomalies ne doit pas être vide");
  }

  @Test
  public void testGetDatabaseTableIds()
      throws IOException, OntorelCreationException, OWLOntologyCreationException {
    File testDir = new File("test-data/_ontologies/MONDIAL/config00/");

    assertTrue(testDir.exists(), "Le répertoire de test doit exister");
    assertTrue(testDir.isDirectory(), "Le répertoire de test doit être un dossier");

    Map<String, String> result = getDatabaseTableIds(testDir);

    assertNotNull(result, "Le mapping d'ID ne doit pas être null");
    assertFalse(result.isEmpty(), "Le mapping d'ID ne doit pas être vide");
  }

  @Test
  public void testGetClassList()
      throws IOException, OntorelCreationException, OWLOntologyCreationException {
    File testDir = new File("test-data/_ontologies/MONDIAL/config00/");

    assertTrue(testDir.exists(), "Le répertoire de test doit exister");
    assertTrue(testDir.isDirectory(), "Le répertoire de test doit être un dossier");

    List<Map<String, Object>> result = getClassList(testDir);

    assertNotNull(result, "La liste de classe ne doit pas être nulle");
    assertFalse(result.isEmpty(), "La liste de classe ne doit pas être vide");
  }

  @Test
  public void testGetSubClassList()
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    File testDir = new File("test-data/_ontologies/MONDIAL/config00/");

    assertTrue(testDir.exists(), "Le répertoire de test doit exister");
    assertTrue(testDir.isDirectory(), "Le répertoire de test doit être un dossier");

    List<SubIriModel> result = getSubClassList(testDir);

    assertNotNull(result, "La liste de classe ne doit pas être null");
    assertFalse(result.isEmpty(), "La liste de classe ne doit pas être vide");
  }

  @Test
  public void testGetInitialObjectPropertiesList()
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    File testDir = new File("test-data/_ontologies/MONDIAL/config00/");

    assertTrue(testDir.exists(), "Le répertoire de test doit exister");
    assertTrue(testDir.isDirectory(), "Le répertoire de test doit être un dossier");

    List<Map<String, Object>> result = getInitialObjectPropertiesList(testDir);

    assertNotNull(result, "La liste des object properties ne doit pas être null");
    assertFalse(result.isEmpty(), "La liste des object properties ne doit pas être vide");
  }

  @Test
  public void testGetObjectPropertiesList()
      throws IOException, OntorelCreationException, OWLOntologyCreationException {
    File testDir = new File("test-data/_ontologies/MONDIAL/config00/");

    assertTrue(testDir.exists(), "Le répertoire de test doit exister");
    assertTrue(testDir.isDirectory(), "Le répertoire de test doit être un dossier");

    List<Map<String, Object>> result = getObjectPropertiesList(testDir);

    assertNotNull(result, "La liste des object properties ne doit pas être null");
    assertFalse(result.isEmpty(), "La liste des object properties ne doit pas être vide");
  }

  @Test
  public void testGetDataPropertiesList()
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    File testDir = new File("test-data/_ontologies/MONDIAL/config00/");

    assertTrue(testDir.exists(), "Le répertoire de test doit exister");
    assertTrue(testDir.isDirectory(), "Le répertoire de test doit être un dossier");

    List<Map<String, Object>> result = getInitialDataPropertiesList(testDir);

    assertNotNull(result, "La liste des data properties ne doit pas être null");
    assertFalse(result.isEmpty(), "La liste des data properties ne doit pas être vide");
  }

  @Test
  public void testGetDatabaseConfigurationFile() throws FileNotFoundException {
    File testDir = new File("test-data/_ontologies/MONDIAL/config00/");

    assertTrue(testDir.exists(), "Le répertoire de test doit exister");
    assertTrue(testDir.isDirectory(), "Le répertoire de test doit être un dossier");
    ConfigurationsSarter configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    File result = configurationsSarter.getDatabaseConfigurationFile(testDir);

    assertNotNull(result,
        "Le fichier de configuration de la base de données ne doit pas être null");
  }
}
