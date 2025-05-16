package ca.griis.ontorela.mrel.catalog;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoAxiomClassAssociationI;
import ca.griis.monto.api.OntoIriI;
import ca.griis.ontorela.catalog.OntoAxiomRelvarEntry;
import ca.griis.ontorela.mrel.MrelEntity;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Catalogue des composant d'OntoRel.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui | non (pourquoi).</li>
 * <li>Clonabilité : oui | non (pourquoi).</li>
 * <li>Modifiabilité : oui | non (pourquoi).</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * TODO 2018-07-19 CK : migrer dans OntoRel. <br>
 * TODO 2018-07-19 CK : créer un OntoRelCatalog par ontology. <br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2018-07-19 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-07-19
 */
public class MrelCatalog {
  private static final Map<Identifier, MrelEntity> catalog = new LinkedHashMap<>();
  private static final Map<String, String> classIRITableId = new LinkedHashMap<>();
  private static final Map<String, OntoAxiomRelvarEntry> ontoAxiomRelvarMap = new LinkedHashMap<>();

  // **************************************************************************
  // Constructeurs
  //
  private MrelCatalog() {}
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Ajout d'une entité relationnelle dans le catalogue MRel à partir d'un IRI.
   * Chaque entité est associée à un identifiant.
   *
   * @param type : le type de l'entité
   * @param entity : l'entité relationnelle à ajouter
   * @param ontoIri : l'iri du composant ontologique correspondant au composant relationnel
   * @return l'identifiant associé à l'entité relationnelle ajoutée au catalogue.
   */
  public static Identifier addEntity(MrelEntityType type, MrelEntity entity, OntoIriI ontoIri) {
    Identifier id = IdGenerator.getInstance().getNewIdentifier(type, ontoIri);
    return id;
  }

  /**
   * Ajout d'une entité relationnelle dans le catalogue MRel à partir d'un axiom.
   *
   * @param type : le type de l'entité
   * @param entity : l'entité relationnelle à ajouter
   * @param ontoAxiom : l'axiome d'association correspondant au composant relationnel
   * @return l'identifiant associé à l'entité relationnelle ajoutée au catalogue.
   */
  public static Identifier addEntity(MrelEntityType type, MrelEntity entity,
      OntoAxiomAssociationI ontoAxiom) {
    Identifier id = IdGenerator.getInstance().getNewIdentifier(type, ontoAxiom);
    //
    // TODO 2023-02-03 [CK] : réviser et documenter cette boucle.
    for (Entry<String, OntoAxiomRelvarEntry> e : ontoAxiomRelvarMap.entrySet()) {
      if (e.getValue().getPropertyIri().equals(ontoAxiom.getProperty().getIri().getShortIri())
          && e.getValue().getDomainIri()
              .equals(ontoAxiom.getOntoDeterminant().getIri().getShortIri())
          && ontoAxiom instanceof OntoAxiomClassAssociationI
          && e.getValue().getRangeIri().equals(((OntoAxiomClassAssociationI) ontoAxiom)
              .getOntoDependent().getIri().getShortIri())) {
        id = new Identifier(e.getKey());
      }
    }

    return id;
  }

  /**
   * Obtenir le catalogue de OntoRel.
   *
   * @return L'ensemble des paires identifiants-iri.
   */
  public static Map<Identifier, MrelEntity> getCatalog() {
    return catalog;
  }

  /**
   * @return L'ensemble des paires IRI classe et identifiant interne d'une table.
   */
  public static Map<String, String> getClassIriTableId() {
    return classIRITableId;
  }

  /**
   * @return OntoAxiomRelvarMap L'ensemble des paires identifiant interne d'une table et
   *         les identifant des composants de l'axiome associé.
   */
  public static Map<String, OntoAxiomRelvarEntry> getOntoAxiomRelvarMap() {
    return ontoAxiomRelvarMap;
  }

  /**
   * Obtenir le catalogue des entité ontorel.
   *
   * @return L'ensemble des entités ontorel.
   */
  public static Set<MrelEntity> getEntityCatalog() {
    return new HashSet<>(catalog.values());
  }

  /**
   * Obtenir le catalogue des identifiants ontorel.
   *
   * @return L'ensemble des identifiants des entités ontorel.
   */
  public static Set<Identifier> getIdCatalog() {
    return catalog.keySet();
  }

  public static String printCatalog() {
    StringBuilder s = new StringBuilder();
    s.append("\n======================================================= \n MRel Catalog"
        + "\n======================================================= \n");
    for (Entry<Identifier, MrelEntity> e : getCatalog().entrySet()) {
      s.append(e.getKey()).append("::").append(e.getValue()).append("\n");
    }
    return s.toString();
  }
}
