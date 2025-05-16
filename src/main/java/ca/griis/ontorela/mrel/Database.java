package ca.griis.ontorela.mrel;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.mrel.Schema.SchemaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

/**
 * Une base de données contenant un ensemble de schéma.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : non.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : oui.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2018-09-06 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-09-06
 */
public class Database {
  // **************************************************************************
  // Attributs spécifiques
  //
  private String databaseId;
  private Set<Schema> schemaSet;
  public static final Descriptor msg = new Descriptor();

  // **************************************************************************
  // Constructeurs
  //
  public Database(String databaseId, Schema baseSchema) {
    this.databaseId = databaseId;
    this.schemaSet = new LinkedHashSet<>();
    this.schemaSet.add(baseSchema);
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
  // Références : https://jgrapht.org/guide/UserOverview#graph-structures

  /**
   * Créer un graph du schéma de base de la base de données.
   *
   * @return Un graphe orienté acyclique.
   */
  public DirectedMultigraph<Table, ForeignKey> getDbGraph() {
    DirectedMultigraph<Table, ForeignKey> g = new DirectedMultigraph<>(ForeignKey.class);
    for (Table t : this.getBaseSchema().getTableSet()) {
      g.addVertex(t);
      for (ForeignKey fk : this.getBaseSchema().getOriginForeignKeySet(t)) {
        if (!g.containsVertex(fk.getDestination())) {
          g.addVertex(fk.getDestination());
        }
        try {
          g.addEdge(t, fk.getDestination(), fk);
        } catch (IllegalArgumentException e) {
          if (e.toString().contains("loops not allowed")) {
            msg.ajouterErreur(
                String.format("[IGNORED] Ontology contained a loop from %s to %s (using fk '%s'). ",
                    t.getIri(), fk.getDestination().getIri(), fk.foreignKeyString()));
          } else {
            throw e;
          }
        }
      }
    }
    return g;
  }

  /**
   * Créer un fichier DOT du graph du schéma de base de la base de données.
   *
   * @param file : fichier .dot
   * @param lang : langue des étiquettes du graphe.
   */
  public void createDbGraphDotFile(File file, String lang) {
    DirectedMultigraph<Table, ForeignKey> dbGraph = getDbGraph();
    DOTExporter<Table, ForeignKey> exporter = new DOTExporter<>();
    // Définir les attributs des noeuds
    exporter.setVertexAttributeProvider(nodeAtt -> {
      Map<String, Attribute> map = new LinkedHashMap<>();
      // Définir l'étiquette
      String label = "\"" + nodeAtt.getLabel(lang) + "\"";
      map.put("label", DefaultAttribute.createAttribute(label));
      return map;
    });
    // Définir les attributs des arrêtes
    exporter.setEdgeAttributeProvider(edgeAtt -> {
      Map<String, Attribute> map = new LinkedHashMap<>();
      // Définir l'étiquette
      String label = "\"" + edgeAtt.getFkId() + "\"";
      map.put("label", DefaultAttribute.createAttribute(label));
      return map;
    });
    // Vérifier si le fichier existe sinon créer un
    try {
      if (!file.exists()) {
        boolean parentFile = file.getParentFile().mkdirs();
        boolean newFile = file.createNewFile();
        if (!parentFile && !newFile) {
          System.err.println("Impossible de créer le fichier : " + file.getAbsolutePath());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Remplir le fichier
    try (OutputStreamWriter writer =
        new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
      exporter.exportGraph(dbGraph, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * List of related components.
   *
   * @return List of table sets.
   */
  public List<Set<Table>> listeComposantsConnexes() {
    ConnectivityInspector<Table, ForeignKey> i = new ConnectivityInspector<>(getDbGraph());
    List<Set<Table>> components = i.connectedSets();
    return components;
  }

  /**
   * @return schemaSet
   */
  public Set<Schema> getSchemaSet() {
    return this.schemaSet;
  }

  /**
   * @param schemaSet --> schemaSet
   */
  public void setSchemaSet(Set<Schema> schemaSet) {
    this.schemaSet = schemaSet;
  }

  /**
   * Ajouter un schéma à la base de données.
   *
   * @param schema
   */
  public void addSchema(Schema schema) {
    this.schemaSet.add(schema);
  }

  /**
   * Obtenir le schéma de base. Une base de données OntoRel contient un seul schéma de base.
   *
   * @return Le schéma de base.
   */
  public Schema getBaseSchema() {
    return this.schemaSet.stream().filter(s -> s.getSchemaType().equals(SchemaType.BASE))
        .findFirst().get();
  }

  /**
   * @return databaseId
   */
  public String getDatabaseId() {
    return this.databaseId;
  }

  /**
   * @param databaseId --> databaseId
   */
  public void setDatabaseId(String databaseId) {
    this.databaseId = databaseId;
  }

  /**
   * Générer un rapport qui décrit le contenu de la base de données.
   *
   * @param directoryPath : l'emplacement du dossier contenant le rapport.
   * @return le fishier correspondant au rapport de la base de données.
   */
  public File generateDatabaseReport(String directoryPath) {
    Descriptor report = new Descriptor();
    //
    report.titre("Database Report");
    report.ajouter(this.getDatabaseId());
    report.ajouter("Schemas: " + this.getSchemaSet().size());
    report.ajouterListe(this.getSchemaSet().stream());
    //
    for (Schema s : this.getSchemaSet()) {
      report.soustitre(s.getName());
      report.ajouter("All Tables: " + s.getTableSet().size());
      report.ajouter("Join Tables: " + s.getJoinTableSet().size());
      report.ajouter("Types: " + s.getTypeSet().size());
      report.ajouter("Foreign keys: " + s.getForeignKeySet().size());
      report.ajouter("Constraints: " + s.getConstraintSet().size());
      report.sauterLigne();
      report.ajouterListe("Tables:", s.getTableSet().stream());
      report.ajouterListe("Foreign keys: ", s.getForeignKeySet().stream());
    }
    //
    return report.creerFichier(directoryPath + "DatabaseReport");
  }

  /**
   * Générer un fichier au format .dot du graphe des tables du schéma de base.
   *
   * @param filePath : l'emplacement du fichier contenant le rapport.
   * @return Le fichier .dot du graph.
   */
  public File createDotFile(String filePath) {
    DOTExporter<Table, ForeignKey> exporter = new DOTExporter<>();
    // Définir les attributs du graphe
    exporter.setGraphAttributeProvider(() -> {
      Map<String, Attribute> map = new LinkedHashMap<>();
      map.put("splines", DefaultAttribute.createAttribute("polyline"));
      map.put("rankdir", DefaultAttribute.createAttribute("LR"));
      return map;
    });
    // Définition des identifiants et les attributs des noeuds
    exporter.setVertexIdProvider(nodeId -> "\"" + nodeId.getIdentifier().getValue() + "\"");
    exporter.setVertexAttributeProvider(nodeAtt -> {
      Map<String, Attribute> map = new LinkedHashMap<>();
      // Définir l'étiquette
      String label = nodeAtt.getIdentifier().getValue() + "::" + nodeAtt.getIri() + "::\n"
          + nodeAtt.getLabel("en");
      map.put("label", DefaultAttribute.createAttribute(label));
      // Définir la forme
      String shape = "box";
      if (nodeAtt.getTableOrigin().equals(Table.TableOrigin.ONTOTYPE)
          || nodeAtt.getTableOrigin().equals(Table.TableOrigin.DATAAXIOM)) {
        shape = "tab";
        map.put("penwidth", DefaultAttribute.createAttribute(4));
      }
      map.put("shape", DefaultAttribute.createAttribute(shape));
      //
      return map;
    });
    // Définir les attributs des arrêtes
    exporter.setEdgeAttributeProvider(edgeAtt -> {
      Map<String, Attribute> map = new LinkedHashMap<>();
      // Définir l'étiquette
      String label = "\"" + edgeAtt.getFkId() + "\"";
      map.put("label", DefaultAttribute.createAttribute(label));
      return map;
    });
    // Créer le fichier
    File dotFile = new File(filePath);
    try {
      if (!dotFile.exists()) {
        boolean parentFile = dotFile.getParentFile().mkdirs();
        boolean newFile = dotFile.createNewFile();
        if (!parentFile && !newFile) {
          System.err.println("Impossible de créer le fichier : " + dotFile.getAbsolutePath());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Remplir le fichier
    try (OutputStreamWriter writer =
        new OutputStreamWriter(new FileOutputStream(dotFile), StandardCharsets.UTF_8)) {
      exporter.exportGraph(this.getDbGraph(), writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dotFile;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Database [databaseId=" + this.databaseId + ", schemaSet=" + this.schemaSet + "]";
  }
}
