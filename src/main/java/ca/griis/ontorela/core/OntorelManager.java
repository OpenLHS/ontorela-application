package ca.griis.ontorela.core;


import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.converter.OntoRelScriptGenerator;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.generator.OntoRelCatDatabaseGenerator;
import java.io.IOException;


/**
 * Contruire l'OntoRel pour les services
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

public class OntorelManager {
  private final DatabaseConfiguration databaseConfigFile;
  private final String outputPath;
  // private final Ontology ontoFiltred;
  private final ExecutionDescriptor executionDescriptor;
  private OntoRel ontoRel;
  private final OntologyManager ontologyManager;

  public OntorelManager(DatabaseConfiguration databaseConfigFile, String outputPath,
      ExecutionDescriptor executionDescriptor, OntologyManager ontologyManager) {
    this.databaseConfigFile = databaseConfigFile;
    this.outputPath = outputPath;
    this.executionDescriptor = executionDescriptor;
    this.ontologyManager = ontologyManager;
  }

  public OntoRel getOntoRel() {
    return ontoRel;
  }

  /**
   * Génère l'OntoRel
   *
   */
  public void buildOntorel() throws OntorelCreationException {
    try {
      executionDescriptor.getMsg().soustitre("Building OntoRel...");
      executionDescriptor.getMsg().ajouter("> Generate OntoRel from filtered ontology...");
      ontoRel = new OntoRel(ontologyManager.getOntoFiltred(), databaseConfigFile);
      ontoRel.buildOntoRel();
      executionDescriptor.getMsg().ajouter(" " + ontoRel.getDatabase().toString());
      executionDescriptor.getMsg().ajouter("OntoRel generated successfully.");
    } catch (Exception e) {
      throw new OntorelCreationException(e);
    }
  }

  /**
   *
   * Génère les scripts de ontorel
   *
   */
  public void generateDatabaseScripts() throws IOException {
    executionDescriptor.getMsg().ajouter("> Generate OntoRel database scripts...");
    String scriptRepo =
        outputPath + "/" + executionDescriptor.getDateString() + "/DatabaseScripts/";
    OntoRelScriptGenerator scriptGenerator = new OntoRelScriptGenerator(
        ontoRel.getDatabase(), ontoRel.getDatabaseConfiguration());
    scriptGenerator.generateScripts(scriptRepo)
        .forEach(script -> executionDescriptor.getMsg()
            .ajouter("Generated script: " + script.getAbsolutePath()));
    executionDescriptor.getMsg().ajouter("Database scripts generated successfully.");
    generateOntorelCat();
  }

  /**
   * Génère le script d'insertion de données pour le catalogue OntoRel.
   * 
   * @throws IOException le fichier de l'ontologie est introuvable.
   */
  public void generateOntorelCat() throws IOException {
    executionDescriptor.getMsg().ajouter("> Generate OntoRel Catalog scripts...");
    String scriptRepo =
        outputPath + "/" + executionDescriptor.getDateString() + "/DatabaseScripts/";
    OntoRelCatDatabaseGenerator ontoproc;
    ontoproc =
        new OntoRelCatDatabaseGenerator(ontoRel.getDatabase(), databaseConfigFile,
            ontologyManager.getOntoFiltred(),
            ontoRel.getOntoRelDic(),
            ontologyManager.getOntoBuilder().getOwlApiOntologyConfiguration().getOwlFile());
    ontoproc.genereteOntoRelCatDdlScripts(scriptRepo)
        .forEach(f -> executionDescriptor.getMsg().ajouter("    " + f));
    executionDescriptor.getMsg().ajouter("OntoRel Catalog generated successfully.");
  }
}
