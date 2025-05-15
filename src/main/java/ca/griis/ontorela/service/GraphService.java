package ca.griis.ontorela.service;

import ca.griis.monto.builder.owlapi.OntologyOwlApiBuilder;
import ca.griis.monto.model.OntoGraph;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.core.ConfigurationsSarter;
import ca.griis.ontorela.core.ExecutionDescriptor;
import ca.griis.ontorela.core.OntologyManager;
import ca.griis.ontorela.core.OntorelManager;
import java.io.File;
import java.util.Arrays;

/**
 * Construit un graphe d'ontologie, de base de données et/ou ontologique-relationnel
 * à partir d'ontorel.
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
 * @version 2.0.0
 * @since 2.0.0
 */
public class GraphService {

  private final OntologyManager ontologyManager;
  private final OntorelManager ontorelManager;
  private final ConfigurationsSarter configurationsSarter;
  private final ExecutionDescriptor executionDescriptor;

  public GraphService(OntologyManager ontologyManager, OntorelManager ontorelManager,
      ConfigurationsSarter configurationsSarter,
      ExecutionDescriptor executionDescriptor) {
    this.ontologyManager = ontologyManager;
    this.ontorelManager = ontorelManager;
    this.configurationsSarter = configurationsSarter;
    this.executionDescriptor = executionDescriptor;
  }

  /**
   * Construit un graphe d'ontologie, de base de données et/ou ontologique-relationnel selon
   * la demande
   *
   * @param repoPath le chemin d'accès des fichiers de configuration
   * @param args les arguments d'entrée
   */
  public void generateGraph(String repoPath, String[] args) {
    File repo = new File(repoPath);
    GraphType graphType = GraphType.all;
    boolean hasMoreArgs = args.length == 4 && args[2].equals("--type");
    if (hasMoreArgs) {
      try {
        graphType = GraphType.valueOf(args[3]);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(
            "Given type doesn't exist. Choices are " + Arrays.toString(GraphType.values()));
      }
    }
    executionDescriptor.getMsg().soustitre(" Building Graphs... ");
    // Obtenir les fichiers de configuration
    String outputPath = repo.getAbsolutePath() + "/" + executionDescriptor.getDateString() + "/";
    //
    String primaryLangue = configurationsSarter.getPrimaryLangue(repo);
    // Repertoire des graphes
    String graphRepo = outputPath + "Graphs/";
    //
    boolean generateInitial =
        graphType.equals(GraphType.all) || graphType.equals(GraphType.initial_onto);
    if (generateInitial) {
      generateInitialGraph(primaryLangue, graphRepo, this.ontologyManager.getOntoBuilder());
    }
    //
    boolean generateFiltered =
        graphType.equals(GraphType.all) || graphType.equals(GraphType.normalized_onto)
            || graphType.equals(GraphType.db) || graphType.equals(GraphType.ontorel);
    if (generateFiltered) {
      // les autre graphes
      generateNormalizedGraph(graphType, primaryLangue, graphRepo);
      if (graphType.equals(GraphType.all) || graphType.equals(GraphType.db)
          || graphType.equals(GraphType.ontorel)) {
        generateOntorelGraph(graphType, graphRepo, this.ontorelManager.getOntoRel());
        generateDatabaseGraph(graphType, graphRepo, this.ontorelManager.getOntoRel());
      }
    }
    // executionDescriptor.getMsg().creerFichier(outputPath + "README");
  }

  private void generateOntorelGraph(GraphType graphType, String graphRepo, OntoRel ontoRel) {
    boolean generateOntoRel =
        graphType.equals(GraphType.all) || graphType.equals(GraphType.ontorel);
    if (generateOntoRel) {
      executionDescriptor.getMsg().ajouter("> Generate OntoRel graph");
      File ontoRelGraphFile =
          ontoRel.getOntoRelGraph().createDotFile(graphRepo + "OntoRelGraph.dot");
      executionDescriptor.getMsg().ajouter("  OntoRel graph generated: \n    " + ontoRelGraphFile);

    }
  }

  private void generateDatabaseGraph(GraphType graphType, String graphRepo,
      OntoRel ontoRel) {
    boolean generateDb = graphType.equals(GraphType.all) || graphType.equals(GraphType.db);
    if (generateDb) {
      executionDescriptor.getMsg().ajouter("> Generate Database graph");
      File dbGraphFile = ontoRel.getDatabase().createDotFile(graphRepo + "RelGraph.dot");
      executionDescriptor.getMsg().ajouter("  Database graph generated: \n    " + dbGraphFile);
    }
  }

  private void generateNormalizedGraph(GraphType graphType, String primaryLangue,
      String graphRepo) {
    if (graphType.equals(GraphType.all) || graphType.equals(GraphType.normalized_onto)) {
      executionDescriptor.getMsg().ajouter("> Generate Ontology graph");
      OntoGraph filteredGraph = ontologyManager.getNormalizedGraph();
      File filteredGraphFile =
          filteredGraph.createDotFile(graphRepo + "OntoGraph.dot", primaryLangue);
      executionDescriptor.getMsg().ajouter(
          "  Filtered ontology graph generated: \n    " + filteredGraphFile.getAbsolutePath());
    }
  }

  private void generateInitialGraph(String primaryLangue, String graphRepo,
      OntologyOwlApiBuilder ontoBuilder) {
    executionDescriptor.getMsg().ajouter(">Generate initial Ontology graph");
    File ontologyGraph = new OntoGraph(ontoBuilder.getOntology())
        .createDotFile(graphRepo + "InitialOntoGraph.dot", primaryLangue);
    executionDescriptor.getMsg()
        .ajouter("  Initial Ontology graph generated: \n   " + ontologyGraph);
  }
}
