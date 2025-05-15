package ca.griis.ontorela.ipm;

import static ca.griis.monto.MOntoAnomalies.generateAxiomAnomalies;
import static ca.griis.ontorela.Ontorela.main;
import static ca.griis.ontorela.mrel.MRelAnomalies.generateIdentifierAnomalies;

import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoDataPropertyI;
import ca.griis.monto.api.OntoEntityCollectionI;
import ca.griis.monto.api.OntoEntityI;
import ca.griis.monto.api.OntoObjectPropertyI;
import ca.griis.monto.api.OntoPropertyI;
import ca.griis.monto.api.OntologyI;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.core.ConfigurationsSarter;
import ca.griis.ontorela.core.ExecutionDescriptor;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.mrel.Schema;
import ca.griis.ontorela.service.ServiceManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;



/**
 * Interface de communication avec l'interface personne machine.
 *
 * <p>
 * <b>Copyright</b> 2016-2017, GRIIS (https://griis.ca/) <br>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé)
 * <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke
 * (Québec) J1K 2R1 <br>
 * CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 * </p>
 *
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2018-09-06
 */
public class Interface {
  private static Descriptor msg = new Descriptor();
  private static Date date = new Date();
  private static String dateString = new SimpleDateFormat("yyyyMMdd-HHmm").format(date);

  /**
   * Obtiens les anomalies d'une ontologie à partir de son fichier de configuration
   *
   * @param dir : a directory.
   * @return chaine de caractère des anomalies trouvées.
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  public static String getAnomaliesOntologyReport(File dir)
      throws IOException, OWLOntologyCreationException, OntorelCreationException {

    ServiceManager manager = new ServiceManager(dir.getPath());
    String[] args = {"--repoPath", dir.getAbsolutePath(), "--type", "ontology"};
    manager.execute("generate-anomalies-reports", args);
    return generateAxiomAnomalies(manager.getOntologyManager().getNormalizedGraph());

  }

  /**
   * Obtiens les anomalies de la base de données à partir de son fichier de configuration
   *
   * @param dir : a directory. Vers les fichiers de configurations
   * @return chaine de caractère des anomalies trouvées.
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  public static String getAnomaliesDatabaseIdentifiersReport(File dir)
      throws IOException, OntorelCreationException, OWLOntologyCreationException {

    ServiceManager manager = new ServiceManager(dir.getPath());
    String[] args = {"--repoPath", dir.getAbsolutePath(), "--type", "db"};
    manager.execute("generate-anomalies-reports", args);

    return generateIdentifierAnomalies(manager.getOntorelManager().getOntoRel().getDatabase(),
        manager.getOntorelManager().getOntoRel().getDatabaseConfiguration()
            .getMaxIdentifierLength());
  }

  /**
   * Obtiens le mapping entre les tables ID et les classes IRI
   *
   * @param dir : a directory. Vers les fichiers de configurations
   * @return mapping entre table id et iri
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  public static Map<String, String> getDatabaseTableIds(File dir)
      throws IOException, OntorelCreationException, OWLOntologyCreationException {

    OntoRel ontoRel = getOntoRel(dir);
    Map<String, String> mapTableIdIri = new HashMap<>();
    for (Schema s : ontoRel.getDatabase().getSchemaSet()) {
      s.getTableSet().forEach(n -> {
        mapTableIdIri.put(n.getIri(), n.getIdentifier().getValue());
      });
    }

    return mapTableIdIri;
  }

  /**
   * Obtiens la liste des classes dans l'ontologie
   *
   * @param dir : a directory. Vers les fichiers de configurations
   * @return liste de mapping entre short IRI et full IRI
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  public static List<Map<String, Object>> getClassList(File dir)
      throws IOException, OntorelCreationException, OWLOntologyCreationException {

    OntoRel ontoRel = getOntoRel(dir);
    OntologyI reducedOntology = ontoRel.getOntology();
    OntoEntityCollectionI<OntoClassI> classes = reducedOntology.getOntoClassSet();

    List<Map<String, Object>> data = new ArrayList<>();
    classes.stream().map(OntoEntityI::getIri).forEach(iri -> {
      Map<String, Object> entry = new HashMap<>();
      entry.put("shortIri", iri.getShortIri());
      entry.put("fullIri", iri.getFullIri());
      data.add(entry);
    });
    return data;
  }

  /**
   * Obtiens l'OntoRel
   *
   * @param dir : a directory. Vers les fichiers de configurations
   * @return OntoRel
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  private static OntoRel getOntoRel(File dir)
      throws IOException, OntorelCreationException, OWLOntologyCreationException {
    ServiceManager manager = new ServiceManager(dir.getPath());
    String[] args = {"--repoPath", dir.getAbsolutePath()};
    manager.execute("generate-ontorel", args);

    return manager.getOntorelManager().getOntoRel();

  }

  /**
   * Obtiens l'OntoRel
   *
   * @param dir : a directory. Vers les fichiers de configurations
   * @return OntoRel
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  private static OntologyI getOntology(File dir)
      throws IOException, OntorelCreationException, OWLOntologyCreationException {

    ServiceManager manager = new ServiceManager(dir.getPath());
    String[] args = {"--repoPath", dir.getAbsolutePath()};
    manager.execute("generate-ontorel", args);
    return manager.getOntologyManager().getOntoBuilder().getOntology();

  }

  /**
   * Obtiens la liste des classes dans l'ontologie avec hiérarchisation
   *
   * @param dir : a directory. Vers les fichiers de configurations
   * @return liste de mapping des classes
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  public static List<SubIriModel> getSubClassList(File dir)
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    OntoEntityCollectionI<OntoClassI> classSet = getOntology(dir).getOntoClassSet();
    List<SubIriModel> listClasses = new ArrayList<>();
    classSet.forEach(ontoClassI -> {
      OntoEntityCollectionI<OntoClassI> ontoSubClass;
      try {
        ontoSubClass = getOntology(dir).getSubClassSet(ontoClassI, true);
      } catch (IOException | OntorelCreationException | OWLOntologyCreationException e) {
        throw new RuntimeException(e);
      }
      if (!ontoSubClass.isEmpty()) {
        IriModel upperClass = new IriModel();
        upperClass.setFullIri(ontoClassI.getIri().getFullIri());
        upperClass.setShortIri(ontoClassI.getIri().getShortIri());
        upperClass.setId(
            ontoClassI.getIri().getShortIri() + "-" + ontoClassI.getIri().getFullIri());

        List<IriModel> subClassOntoIriList = new ArrayList<>();
        ontoSubClass.forEach(ontoClass -> {
          IriModel subClass = new IriModel();
          subClass.setFullIri(ontoClass.getIri().getFullIri());
          subClass.setShortIri(ontoClass.getIri().getShortIri());
          subClass.setId(ontoClassI.getIri().getShortIri() + "-" + ontoClass.getIri().getFullIri());
          subClassOntoIriList.add(subClass);
        });

        SubIriModel subIri = new SubIriModel();
        subIri.setUpperClass(upperClass);
        subIri.setSubClasses(subClassOntoIriList);

        listClasses.add(subIri);
      }
    });

    return listClasses;
  }

  /**
   * Obtiens la liste des object properties dans l'ontologie initiale
   *
   * @param dir : a directory. Vers les fichiers de configurations
   * @return liste de mapping des object properties avec leur short IRI et full IRI
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  public static List<Map<String, Object>> getInitialObjectPropertiesList(File dir)
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    OntoEntityCollectionI<OntoObjectPropertyI> objectProperties =
        getOntology(dir).getOntoObjectPropertieSet();

    List<Map<String, Object>> data = new ArrayList<>();
    objectProperties.stream().map(OntoPropertyI::getIri).forEach(properties -> {
      Map<String, Object> entry = new HashMap<>();
      entry.put("shortIri", properties.getShortIri());
      entry.put("fullIri", properties.getFullIri());
      data.add(entry);
    });
    return data;
  }

  /**
   * Obtiens la liste des object properties dans l'ontologie réduite
   *
   * @param dir : a directory. Vers les fichiers de configurations
   * @return liste de mapping des object properties avec leur short IRI et full IRI
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  public static List<Map<String, Object>> getObjectPropertiesList(File dir)
      throws IOException, OntorelCreationException, OWLOntologyCreationException {
    OntoRel ontoRel = getOntoRel(dir);
    OntologyI reducedOntology = ontoRel.getOntology();
    OntoEntityCollectionI<OntoObjectPropertyI> objectProperties =
        reducedOntology.getOntoObjectPropertieSet();
    List<Map<String, Object>> data = new ArrayList<>();
    objectProperties.stream().map(OntoPropertyI::getIri).forEach(properties -> {
      Map<String, Object> entry = new HashMap<>();
      entry.put("shortIri", properties.getShortIri());
      entry.put("fullIri", properties.getFullIri());
      data.add(entry);
    });
    return data;
  }

  /**
   * Obtiens la liste des data properties dans l'ontologie initiale
   *
   * @param dir : a directory. Vers les fichiers de configurations
   * @return liste de mapping des data properties avec leur short IRI et full IRI
   * @throws FileNotFoundException les fichiers de configuration sont introuvables.
   */
  public static List<Map<String, Object>> getInitialDataPropertiesList(File dir)
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    OntoEntityCollectionI<OntoDataPropertyI> dataProperties =
        getOntology(dir).getOntoDataPropertieSet();
    List<Map<String, Object>> data = new ArrayList<>();
    dataProperties.stream().map(OntoPropertyI::getIri).forEach(properties -> {
      Map<String, Object> entry = new HashMap<>();
      entry.put("shortIri", properties.getShortIri());
      entry.put("fullIri", properties.getFullIri());
      data.add(entry);
    });
    return data;
  }

  /**
   * Lance le processus main de OntoRelA
   *
   * @param args : le chemin vers un dossier qui contient les fichiers de configuration.
   */
  public static void launchMainProcess(String[] args)
      throws Exception {
    main(args);

    File repo = new File(args[1]);
    ExecutionDescriptor executionDescriptor = new ExecutionDescriptor(msg, dateString);
    ConfigurationsSarter configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    File ontologyConfigFile = configurationsSarter.getOntologyConfigurationFile(repo);
    Path ontologyConfigFilePath = ontologyConfigFile.toPath();

    File databaseConfig = configurationsSarter.getDatabaseConfigurationFile(repo);
    Path databaseConfigFilePath = databaseConfig.toPath();

    String outputPath = repo.getAbsolutePath() + "/" + dateString + "/Configs/";
    Path destinationDir = Paths.get(outputPath);

    if (!Files.exists(destinationDir)) {
      Files.createDirectories(destinationDir);
    }

    Files.copy(ontologyConfigFilePath, destinationDir.resolve(ontologyConfigFilePath.getFileName()),
        StandardCopyOption.REPLACE_EXISTING);
    Files.copy(databaseConfigFilePath, destinationDir.resolve(databaseConfigFilePath.getFileName()),
        StandardCopyOption.REPLACE_EXISTING);
  }
}
