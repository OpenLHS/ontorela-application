package ca.griis.ontorela.mrel.catalog;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoAxiomClassAssociationI;
import ca.griis.monto.api.OntoAxiomDataAssociationI;
import ca.griis.monto.api.OntoIriI;
import ca.griis.ontorela.configuration.ConfigurationLoader;

/**
 * Le catalogue des identifiants internes de MRel.
 * Toutes les entités relationnelles sont associées à un identifiant interne unique au sein
 * de l'application.
 * <br>
 * Patron de conception : singleton.
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ... <br>
 *
 * <b>Tâches réalisées</b><br>
 * 2017-XX-XX (0.2.0) [XX] ... <br>
 * 2018-02-26 (0.1.0) [CK] Mise en œuvre initiale. <br>
 *
 * <p>
 * <b>Copyright</b> 2016-2017,<a href="https://griis.ca/">GRIIS</a> <br>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé) <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke (Québec) J1K 2R1 <br>
 * CANADA <br>
 * [(<a href="http://creativecommons.org/licenses/by-nc/3.0">CC-BY-NC-3.0 </a>)]
 * </p>
 *
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2018-02-26
 */
public class IdGenerator {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static final IdGenerator instance = new IdGenerator();

  // **************************************************************************
  // Constructeurs
  //
  private IdGenerator() {}
  // **************************************************************************
  // Opérations propres
  //

  /**
   * @return instance
   */
  public static IdGenerator getInstance() {
    return instance;
  }

  /**
   * Construire un identifiant en héxadécimal de longueur 29.
   * Attention plusieurs riques possible : hashcode peut varier selon le OS et collision.
   * <a href=
   * "https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#hashCode()">Java
   * 17 hashCode</a>
   * TODO 2023-03-02 [CK] : étudier le risque de collision et modifier l'algorithme au besoin.
   *
   * @param s : une chaîne de caractère.
   * @return un identifiant d'une entité OntoRel en hexadécimal de longueur 29.
   */
  private static String computeHash(String s) {
    String id = Integer.toHexString(s.hashCode());
    return String.format("%1$-" + 10 + "s", id).replace(' ', '0').substring(0, 10);
  }

  private static String getHash(OntoIriI iri) {
    return computeHash(iri.getFullIri());
  }

  private static String getHash(OntoAxiomAssociationI axiom) {
    return computeHash(getAxiomIri(axiom));
  }

  // **************************************************************************
  // Opérations publiques
  //
  public static String getAxiomIri(OntoAxiomAssociationI axiom) {
    String iri = "";
    if (axiom instanceof OntoAxiomClassAssociationI) {
      iri = String.join("_", axiom.getOntoDeterminant().getIri().getFullIri(),
          axiom.getProperty().getIri().getFullIri(),
          ((OntoAxiomClassAssociationI) axiom).getOntoDependent().getIri().getFullIri());
    } else if (axiom instanceof OntoAxiomDataAssociationI) {
      iri = String.join("_", axiom.getOntoDeterminant().getIri().getFullIri(),
          axiom.getProperty().getIri().getFullIri(),
          ((OntoAxiomDataAssociationI) axiom).getOntoDependent().getIri().getFullIri());
    }
    return iri;
  }

  public static String getAxiomShortIri(OntoAxiomAssociationI axiom) {
    String iri = "";
    if (axiom instanceof OntoAxiomClassAssociationI) {
      iri = String.join("_", axiom.getOntoDeterminant().getIri().getShortIri(),
          axiom.getProperty().getIri().getShortIri(),
          ((OntoAxiomClassAssociationI) axiom).getOntoDependent().getIri().getShortIri());
    } else if (axiom instanceof OntoAxiomDataAssociationI) {
      iri = String.join("_", axiom.getOntoDeterminant().getIri().getShortIri(),
          axiom.getProperty().getIri().getShortIri(),
          ((OntoAxiomDataAssociationI) axiom).getOntoDependent().getIri().getShortIri());
    }
    return iri;
  }

  /**
   * Obtenir un nouvel identifiant selon le type de l'entité
   *
   * @param type : le type de l'entité.
   * @param iri : le IRI de l'entité.
   * @return un nouvel identifant.
   */
  protected Identifier getNewIdentifier(MrelEntityType type, OntoIriI iri) {
    String p;
    if (ConfigurationLoader.getDatabaseConfiguration().getuseIriAsTableId()) {
      p = iri.getShortIri();
    } else {
      p = getHash(iri);
      p = switch (type) {
        // Composants ontologiques
        case TABLE -> "T" + p;
        case TTABLE -> "D" + p;
        case PTABLE -> "P" + p;
        case VIEW -> "V" + p;
        case SCHEMA -> "S" + p;
      };
    }
    return new Identifier(p);
  }

  /**
   * Obtenir un nouvel identifiant selon le type de l'entité et du IRI de l'axiome
   *
   * @param type : le type de l'entité.
   * @param axiom : axiome.
   * @return un nouvel identifant.
   */
  protected Identifier getNewIdentifier(MrelEntityType type, OntoAxiomAssociationI axiom) {
    String p;
    if (ConfigurationLoader.getDatabaseConfiguration().getuseIriAsTableId()) {
      p = getAxiomShortIri(axiom);
    } else {
      p = getHash(axiom);
      if (type == MrelEntityType.TABLE) {
        p = "T" + p;
      } else {
        p = "X" + p;
      }
    }
    return new Identifier(p);
  }
}
