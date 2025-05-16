package ca.griis.ontorela.service;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.core.ConfigurationsSarter;
import ca.griis.ontorela.core.ExecutionDescriptor;
import ca.griis.ontorela.core.OntologyManager;
import ca.griis.ontorela.core.OntorelManager;
import ca.griis.ontorela.exception.OntorelCreationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;


/**
 * Gérer les services
 *
 * <h3>Historique</h3>
 * <p>
 * 2025-01-31 [AS] - Implémentation initiale <br>
 * </p>
 *
 * <h3>Tâches</h3>
 * S.O.
 *
 * @author [AS] Amnei.Souid@USherbrooke.ca
 * @version 2.0.0
 * @since 2.0.0
 */

public final class ServiceManager {
  private final ConfigurationsSarter configurationsSarter;
  private final OntologyManager ontologyManager;
  private final String configPath;
  private final DatabaseConfiguration databaseConfiguration;
  private final ExecutionDescriptor executionDescriptor;
  private OntorelManager ontorelManager;
  private GraphService graphService;
  private AnomaliesReportService reportService;
  private Descriptor msg = new Descriptor();
  private Date date = new Date();
  private String dateString = new SimpleDateFormat("yyyyMMdd-HHmm").format(date);

  public ServiceManager(String configPath) throws FileNotFoundException {
    executionDescriptor = new ExecutionDescriptor(msg, dateString);
    executionDescriptor.getMsg().titre("START OntoRelA Application ");
    this.configurationsSarter = new ConfigurationsSarter(executionDescriptor);
    configurationsSarter.startDatabaseConfiguration(new File(configPath));
    this.ontologyManager = new OntologyManager(
        configurationsSarter.getOntologyConfigurationFile(new File(configPath)), configPath,
        executionDescriptor);
    this.configPath = configPath;
    this.databaseConfiguration =
        configurationsSarter.getDatabaseConfiguration();
    //
  }

  public OntologyManager getOntologyManager() {
    return ontologyManager;
  }

  public OntorelManager getOntorelManager() {
    return ontorelManager;
  }

  public GraphService getGraphService() {
    return graphService;
  }

  public AnomaliesReportService getReportService() {
    return reportService;
  }

  public DatabaseConfiguration getDatabaseConfiguration() {
    return databaseConfiguration;
  }

  public ExecutionDescriptor getExecutionDescriptor() {
    return executionDescriptor;
  }

  public void execute(String processFunc, String[] args)
      throws OWLOntologyCreationException, OntorelCreationException, IOException {
    final long startTime = System.nanoTime();
    switch (processFunc) {
      case "generate-all" -> {
        ontologyManager.build();
        ontologyManager.generateNormalizedFiles();
        this.ontorelManager =
            new OntorelManager(configurationsSarter.getDatabaseConfiguration(),
                configPath, executionDescriptor, ontologyManager);
        this.ontorelManager.buildOntorel();
        this.ontorelManager.generateDatabaseScripts();
        // graphes
        graphService =
            new GraphService(ontologyManager, ontorelManager, configurationsSarter,
                executionDescriptor);
        graphService.generateGraph(configPath, args);
        // rapports
        reportService =
            new AnomaliesReportService(ontologyManager, ontorelManager, executionDescriptor);
        reportService.generateReport(configPath, args);
      }
      case "generate-ontorel" -> {
        ontologyManager.build();
        ontologyManager.generateNormalizedFiles();
        ontorelManager =
            new OntorelManager(configurationsSarter.getDatabaseConfiguration(),
                configPath, executionDescriptor, ontologyManager);
        ontorelManager.buildOntorel();
        ontorelManager.generateDatabaseScripts();
      }
      case "generate-graph" -> {
        ontologyManager.build();
        ontorelManager =
            new OntorelManager(configurationsSarter.getDatabaseConfiguration(),
                configPath, executionDescriptor, ontologyManager);
        ontorelManager.buildOntorel();
        // graphes
        graphService =
            new GraphService(ontologyManager, ontorelManager, configurationsSarter,
                executionDescriptor);
        graphService.generateGraph(configPath, args);
      }
      case "generate-anomalies-reports" -> {
        ontologyManager.build();
        ontorelManager =
            new OntorelManager(configurationsSarter.getDatabaseConfiguration(),
                configPath, executionDescriptor, ontologyManager);
        ontorelManager.buildOntorel();
        // rapports
        reportService =
            new AnomaliesReportService(ontologyManager, ontorelManager, executionDescriptor);
        reportService.generateReport(configPath, args);
      }
      default -> {
        throw new IllegalArgumentException(
            "Invalid process function. Accepted values:"
                + " generate-all, generate-ontorel, generate-graph, generate-anomalies-reports.");
      }
    }
    long endTime = System.nanoTime();
    long execTime = (endTime - startTime) / 1000000000;
    executionDescriptor.getMsg().titre(" END OntoRelA Application  " + execTime + " sec");
    System.out.println(executionDescriptor.getMsg());
    String outputPath = configPath + "/" + executionDescriptor.getDateString() + "/";

    executionDescriptor.getMsg().creerFichier(outputPath + "README");

  }
}
