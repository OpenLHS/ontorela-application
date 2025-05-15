package ca.griis.ontorela.catalog;

import ca.griis.ontorela.mrel.catalog.MrelCatalog;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

/**
 * Lecteur d'un catalogue OntoRel.
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-11-21 (0.1.0) [CK] Mise en oeuvre initiale. <br>
 *
 * <p>
 * <b>Copyright</b> 2016-2017, GRIIS (https://griis.ca/) <br>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé)
 * <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke (Québec) J1K 2R1 <br>
 * CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 * </p>
 *
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2019-11-21
 */
public class OntoRelCatLoader {
  // **************************************************************************
  // Attributs spécifiques
  //
  // **************************************************************************
  // Constructeurs
  //

  /**
   *
   */
  public OntoRelCatLoader() {
    // TODO Auto-generated constructor stub
  }

  // **************************************************************************
  // Opérations propres
  //
  private static String getShortIri(String fullIri) {
    String shortIri = fullIri;
    shortIri = fullIri.substring(fullIri.lastIndexOf('/') + 1);
    if (shortIri.contains("#")) {
      shortIri = shortIri.substring(shortIri.lastIndexOf('#') + 1);
    }
    return shortIri;
  }

  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Chargement d'un OntoRelCat JSON. Permet d'initialiser le catalogue MRel.
   *
   * @param ontoRelCatPath : le chemin complet de l'emplacement du fichier
   *        OntoRelCat.
   */
  public static void loadOntoRelCat(String ontoRelCatPath) {
    if (ontoRelCatPath != null) {
      File ontoRelCatFile = new File(ontoRelCatPath);
      byte[] data;
      try {
        data = Files.readAllBytes(ontoRelCatFile.toPath());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(data);
        // ==== Récupérer les classes
        JsonNode classes = root.path("Classes");
        Iterator<JsonNode> defSet = classes.elements();
        while (defSet.hasNext()) {
          JsonNode ontoClass = defSet.next();
          //
          String shortIri = getShortIri(ontoClass.path("iri").asText());
          //
          String tableId = ontoClass.path("tableId").asText();
          MrelCatalog.getClassIriTableId().put(shortIri, tableId);
        }
        // ==== Récupérer les types
        JsonNode typeSetRoot = root.path("Types");
        Iterator<JsonNode> typesSet = typeSetRoot.elements();
        while (typesSet.hasNext()) {
          JsonNode ontoAxiom = typesSet.next();
          //
          String shortIri = getShortIri(ontoAxiom.path("iri").asText());
          String tableId = ontoAxiom.path("tableId").asText();
          //
          MrelCatalog.getClassIriTableId().put(shortIri, tableId);
        }
        // ==== Récupérer les axiomes de classes
        JsonNode classAxiomesRoot = root.path("ClassAxioms");
        Iterator<JsonNode> classAxiomesSet = classAxiomesRoot.elements();
        while (classAxiomesSet.hasNext()) {
          JsonNode ontoAxiom = classAxiomesSet.next();
          //
          String tableId = ontoAxiom.path("tableId").asText();
          String domainClassIri = getShortIri(ontoAxiom.path("domainClassIri").asText());
          String rangeClassIri = getShortIri(ontoAxiom.path("rangeClassIri").asText());
          String propertyIri = getShortIri(ontoAxiom.path("propertyIri").asText());
          //
          // System.out.println(tableId + "=" + ontoAssociationMap.get(tableId));
          OntoAxiomRelvarEntry classAxiomEntry =
              new OntoAxiomRelvarEntry(tableId, domainClassIri, rangeClassIri,
                  propertyIri);
          MrelCatalog.getOntoAxiomRelvarMap().put(tableId, classAxiomEntry);
        }
      } catch (IOException e) {
        System.err.println(e.getMessage() + " OntoRelCat file not found.");
      }
    }
  }
}
