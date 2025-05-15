package ca.griis.ontorela.converter;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.ForeignKey.ForeignKeyType;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.Table.TableOrigin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;

/**
 * Les graphe de la base de données OntoRel.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : non.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-02-26 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2019-02-26
 */
public class OntoRelGraph {
  // **************************************************************************
  // Attributs spécifiques
  //
  public static final Descriptor msg = new Descriptor();

  public enum NodeType {
    CLASSNODE, TYPENODE, PROPERTYNODE, CLASSAXIOMNODE, DATAXIOMNODE
  }


  public enum EdgeType {
    ISA, OPEDGE, DPEDGE
  }


  private final DirectedMultigraph<Table, ForeignKey> ontoRelGraph;

  // **************************************************************************
  // Constructeurs
  //
  public OntoRelGraph() {
    super();
    this.ontoRelGraph = new DirectedMultigraph<>(ForeignKey.class);
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
  public void addNode(NodeType nodeType, Table table) {
    this.ontoRelGraph.addVertex(table);
  }

  public void addLink(EdgeType edgeType, Table source, Table target, ForeignKey joinKey) {
    try {
      this.ontoRelGraph.addEdge(source, target, joinKey);
    } catch (IllegalArgumentException e) {
      if (e.toString().contains("loops not allowed")) {
        msg.ajouterErreur(
            String.format("[IGNORED] Ontology contained a loop from %s to %s (using fk '%s'). ",
                source.getIri(), target.getIri(), joinKey.foreignKeyString()));
      } else {
        throw e;
      }
    }
  }

  /**
   * @return ontoRelGraph
   */
  public DirectedMultigraph<Table, ForeignKey> getOntoRelGraph() {
    return this.ontoRelGraph;
  }

  /**
   * Générer un fichier au format .dot du graphe OntoRel.
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
    exporter.setVertexIdProvider(node -> "\"" + node.getIdentifier().getValue() + "\"");
    exporter.setVertexAttributeProvider(node -> {
      Map<String, Attribute> map = new LinkedHashMap<>();
      // Définir l'étiquette
      String label = "\"" + node.getIdentifier().getValue() + "::\n" + node.getLabel(
          "en") + "\"";
      if (node.getTableOrigin().equals(TableOrigin.ONTOCLASS)
          || node.getTableOrigin().equals(TableOrigin.ONTOTYPE)
          || node.getTableOrigin().equals(TableOrigin.OBJECTPROPERTY)) {
        label =
            "\"" + node.getIri() + "::" + node.getIdentifier().getValue() + "::\n" + node.getLabel(
                "en") + "\"";
      }
      map.put("label", DefaultAttribute.createAttribute(label));
      // Définir la forme
      String shape = "box";
      if (node.getTableOrigin().equals(TableOrigin.OBJECTPROPERTY)) {
        shape = "Mdiamond";
      } else if (node.getTableOrigin().equals(TableOrigin.CLASSAXIOM)) {
        shape = "Mdiamond";
      } else if (node.getTableOrigin().equals(TableOrigin.DATAAXIOM)) {
        shape = "doubleoctagon";
      } else if (node.getTableOrigin().equals(TableOrigin.ONTOTYPE)) {
        shape = "circle";
      }
      map.put("shape", DefaultAttribute.createAttribute(shape));
      //
      return map;
    });
    // Définir les attributs des arrêtes
    // TODO 2019-10-08 CK : permettre d'afficher plusieurs attributs.
    exporter.setEdgeAttributeProvider(edge -> {
      Map<String, Attribute> map = new LinkedHashMap<>();
      // Définir l'étiquette
      String label = "\"" + edge.getForeignKeyType().getString() + ":";
      if (edge.getForeignKeyType().equals(ForeignKeyType.ISA)) {
        label += edge.getAttDestination().stream().map(e -> e.getAttId())
            .collect(Collectors.joining("\""));
      } else {
        label +=
            edge.getAttOrigin().stream().map(e -> e.getAttId()).collect(Collectors.joining("\""));
      }
      map.put("label", DefaultAttribute.createAttribute(label));
      //
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
      exporter.exportGraph(this.ontoRelGraph, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dotFile;
  }
}
