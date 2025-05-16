package ca.griis.ontorela.service;


import ca.griis.monto.MOntoAnomalies;
import ca.griis.monto.Monto;
import ca.griis.monto.api.OntologyI;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.core.ExecutionDescriptor;
import ca.griis.ontorela.core.OntologyManager;
import ca.griis.ontorela.core.OntorelManager;
import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.mrel.MRelAnomalies;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Génère un rapport d'anomalies pour l'ontologie et/ou la base de données.
 *
 * <h3>Historique</h3>
 * <p>
 * 2024-12-18 [SSC] - Implémentation initiale<br>
 * 2025-01-31 [AS] - Factirisation <br>
 * </p>
 *
 * <h3>Tâches</h3>
 * S.O.
 *
 * @author SSC
 * @author [AS] Amnei.Souid@USherbrooke.ca
 * @version 2.0.0
 * @since 2.0.0
 */
public class AnomaliesReportService {

  private final OntologyManager ontologyManager;
  private final OntorelManager ontorelManager;
  private final ExecutionDescriptor executionDescriptor;

  public AnomaliesReportService(OntologyManager ontologyManager, OntorelManager ontorelManager,
      ExecutionDescriptor executionDescriptor) {
    this.ontologyManager = ontologyManager;
    this.ontorelManager = ontorelManager;
    this.executionDescriptor = executionDescriptor;
  }

  /**
   * Génération des différents rapports à la demande
   *
   * @param repoPath Le chemin pour les fichiers de configurations
   * @param args Les arguments d'entrées dirigeant la génération
   */
  public void generateReport(String repoPath, String[] args) {
    File repo = new File(repoPath);
    AnomalyType anomalyType = AnomalyType.all;
    if (args.length == 4 && args[2].equals("--type")) {
      try {
        anomalyType = AnomalyType.valueOf(args[3]);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(
            "Given type doesn't exist. Choices are " + Arrays.toString(AnomalyType.values()));
      }
    }
    // Obtenir les fichiers de configuration
    String outputPath = repo.getAbsolutePath() + "/" + executionDescriptor.getDateString() + "/";
    executionDescriptor.getMsg().sauterLigne();
    // Rapport
    boolean generateOntology =
        anomalyType.equals(AnomalyType.all) || anomalyType.equals(AnomalyType.ontology);
    if (generateOntology) {
      executionDescriptor.getMsg().soustitre("Reports generations");
      executionDescriptor.getMsg().ajouter("  Ontology reports generated: ");
      this.ontologyManager.getOntoBuilder().generateReports(outputPath)
          .forEach(f -> executionDescriptor.getMsg().ajouter("    " + f.getAbsolutePath()));
      executionDescriptor.getMsg().ajouter("    "
          + MOntoAnomalies.generateAnomaliesReport(ontologyManager.getNormalizedGraph(), outputPath)
              .getAbsolutePath());
      Monto.montoSummary(ontologyManager.getOntoBuilder().getOntology(),
          ontologyManager.getOntoFiltred(), outputPath);
    }
    // == db
    boolean generateDb = anomalyType.equals(AnomalyType.all) || anomalyType.equals(AnomalyType.db);
    if (generateDb) {
      OntoRel ontoRel = ontorelManager.getOntoRel();
      executionDescriptor.getMsg().ajouter("  OntoRel anomalies generated: ");
      executionDescriptor.getMsg()
          .ajouter("    " + ontoRel.generateReport(outputPath).getAbsolutePath());

      executionDescriptor.getMsg().ajouter("  Database reports generated: ");
      File dbReportFile = ontoRel.getDatabase().generateDatabaseReport(outputPath);
      executionDescriptor.getMsg().ajouter("    " + dbReportFile);
      executionDescriptor.getMsg().ajouter("    " + MRelAnomalies
          .generateAnomaliesReport(ontoRel,
              ontoRel.getDatabaseConfiguration().getMaxIdentifierLength(), outputPath)
          .getAbsolutePath());
      ontoRelaSummary(ontoRel.getOntology(), ontoRel.getDatabase(),
          ontoRel.getOntoRelDic(), outputPath);
      /**
       * @todo [SSC] - À venir: Tables Without Primary Key, Orphan Foreign Keys et Duplicate Column
       *       Names.
       */
    }
  }

  /**
   * Gérer un rapport sommaire de l'exécution.
   *
   * @param reducedOntology : l'ontologie normalisée.
   * @param ontoRelDb : l'OntoRel.
   * @param ontoRelCat : le catalogue de mise en correspondance.
   * @param outputPath : l'emplacement du rapport.
   */
  public static void ontoRelaSummary(OntologyI reducedOntology, Database ontoRelDb,
      OntoRelCat ontoRelCat,
      String outputPath) {
    Descriptor msg = new Descriptor();
    msg.titre("OntoRelA summary");
    // Résumé quantitatif des éléments de l'ontologie générée
    msg.soustitre("Reduced Ontology (axiom redundancy reduction)");
    msg.ajouter(reducedOntology.getIri().getFullIri());
    msg.ajouter("Nb. classes          : " + reducedOntology.getOntoClassSet().size());
    msg.ajouter("Nb. data properties  : " + reducedOntology.getOntoDataPropertieSet().size());
    msg.ajouter("Nb. object properties: " + reducedOntology.getOntoObjectPropertieSet().size());
    // msg.ajouter("Nb. axioms: " + reducedOntology.getOntoAxiomSet().size());
    msg.ajouter("Nb. isa axioms       : "
        + reducedOntology.getOntoAxiomSet().getClassInheritenceAxioms().size());
    msg.ajouter(
        "Nb. data axioms      : " + reducedOntology.getOntoAxiomSet().getDataAxioms().size());
    msg.ajouter(
        "Nb. class axioms     : " + reducedOntology.getOntoAxiomSet().getClassAxioms().size());
    // Chiffres sur la BD
    msg.soustitre("OntoRel database");
    msg.ajouter("Nb. all tables          : " + ontoRelDb.getBaseSchema().getTableSet().size());
    msg.ajouter("Nb. association tables  : " + ontoRelDb.getBaseSchema().getJoinTableSet().size());
    msg.ajouter("Nb. fks                 : " + ontoRelDb.getBaseSchema().getForeignKeySet().size());
    msg.ajouter("Nb. constraint functions: " + ontoRelDb.getBaseSchema().getConstraintSet().size());
    //
    // Chiffres techniques
    msg.soustitre("Other");
    // msg.ajouter("Execution time: " + execTime + " sec");
    Path folder = Paths.get(outputPath);
    long size = getFolderSize(folder.toFile());
    msg.ajouter("Output total size: " + size / 1024 + " KB");
    //
    msg.creerFichier(outputPath + "OntoRelA-summary");
  }

  /**
   * Obtenir la taille du répertoire
   *
   * @param folder le répertoire
   * @return la taille
   */
  private static long getFolderSize(File folder) {
    long length = 0;
    File[] files = folder.listFiles();
    assert files != null;
    for (File file : files) {
      if (file.isFile()) {
        length += file.length();
      } else {
        length += getFolderSize(file);
      }
    }
    return length;
  }
}
