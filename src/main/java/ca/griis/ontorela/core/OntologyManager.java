package ca.griis.ontorela.core;


import ca.griis.monto.builder.MontoFilter;
import ca.griis.monto.builder.MontoRedundancyReducer;
import ca.griis.monto.builder.owlapi.OntologyOwlApiBuilder;
import ca.griis.monto.generator.MicroOntoGenerator;
import ca.griis.monto.model.OntoGraph;
import ca.griis.monto.model.Ontology;
import java.io.File;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * Contruire l'ontologie initial et filtré pour les services
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

public class OntologyManager {
  private final File ontologyConfigFile;
  private final String outputPath;
  private final ExecutionDescriptor executionDescriptor;
  private OntologyOwlApiBuilder ontoBuilder;
  private Ontology ontoFiltred;
  private OntoGraph normalizedGraph;

  public OntologyManager(File ontologyConfigFile, String outputpath,
      ExecutionDescriptor executionDescriptor) {
    this.ontologyConfigFile = ontologyConfigFile;
    this.outputPath = outputpath;
    this.executionDescriptor = executionDescriptor;
  }

  public Ontology getOntoFiltred() {
    return ontoFiltred;
  }

  public void setOntoFiltred(Ontology ontoFiltred) {
    this.ontoFiltred = ontoFiltred;
  }

  public OntologyOwlApiBuilder getOntoBuilder() {
    return ontoBuilder;
  }

  public OntoGraph getNormalizedGraph() {
    return normalizedGraph;
  }

  /**
   * Construire l'ontologie normalisée puis filtrée.
   * 
   * @throws OWLOntologyCreationException : problème de création de l'ontologie.
   */
  public void build() throws OWLOntologyCreationException {
    executionDescriptor.getMsg().soustitre("Building Ontology...");
    try {
      // ============ l'ontologie initiale =============
      this.ontoBuilder = generateInitialOntology(ontologyConfigFile);
      // ============ l'ontologie filtrée ==============
      this.ontoFiltred = generateFilteredOntology(ontoBuilder);
      this.normalizedGraph = new OntoGraph(ontoFiltred);

    } catch (Exception e) {
      throw new OWLOntologyCreationException(e);
    }
  }

  public void generateNormalizedFiles() {
    String out = outputPath + "/" + executionDescriptor.getDateString() + "/";
    // ============ Générer l'ontologie normalisée en format monto
    File monto = MicroOntoGenerator.getInstance().generateMOnto(out, this.ontoFiltred,
        "OntoRela", "v0", executionDescriptor.getDateString());
    executionDescriptor.getMsg()
        .ajouter("  Normalized ontology generated (monto): \n    " + monto.getAbsolutePath());

    // ============ Générer l'ontologie normalisée en format owl
    File owlMonto = MicroOntoGenerator.getInstance().generateMOntoOwl(out, this.ontoFiltred);
    executionDescriptor.getMsg()
        .ajouter("  Normalized ontology generated (owl): \n    " + owlMonto.getAbsolutePath());
  }

  /**
   * Génère l'ontologie initial
   *
   */
  private OntologyOwlApiBuilder generateInitialOntology(File ontologyConfigFile)
      throws OWLOntologyCreationException {
    try {
      // ============ l'ontologie initiale =============
      OntologyOwlApiBuilder ontoBuilder = new OntologyOwlApiBuilder(ontologyConfigFile);
      executionDescriptor.getMsg().ajouter("Initial ontology has been built.");
      executionDescriptor.getMsg().ajouter("  " + ontoBuilder.getOntology().getIri());
      return ontoBuilder;
    } catch (Exception e) {
      throw new OWLOntologyCreationException(e);
    }
  }

  /**
   * Génère l'ontologie filtrée et réduite
   *
   * @param ontoBuilder le constructeur d'ontologie
   * @return une ontologie filtrée
   */
  private Ontology generateFilteredOntology(OntologyOwlApiBuilder ontoBuilder) {
    // ============ l'ontologie filtrée ==============
    /*
     * FIXME : PDRO CAST uk.ac.manchester.cs.owl.owlapi.OWLObjectInverseOfImplis
     * OwlOntologyMerger owlmerged = new
     * OwlOntologyMerger(ontoBuilder.getOwlApiOntologyConfiguration());
     * owlmerged.merge(); owlmerged.generateMergedFile(outputPath);
     */
    Ontology filteredOnto = MontoFilter.filterOntology(ontoBuilder.getOntology(),
        ontoBuilder.getOwlApiOntologyConfiguration());
    executionDescriptor.getMsg().ajouter("Filtered ontology has been built.");
    MontoRedundancyReducer reducer =
        new MontoRedundancyReducer(filteredOnto, ontoBuilder.getOwlApiOntologyConfiguration());
    Ontology ontoReduced = reducer.getReducedOntology();
    executionDescriptor.getMsg().ajouter("Filtered ontology has been reduced");
    return ontoReduced;
  }
}
