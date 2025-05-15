package ca.griis.ontorela.mrel;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.converter.OntoRel;
import java.io.File;
import java.util.Map.Entry;

/**
 * Ensemble des méthodes pour la détection et génération de rapport d'anomalies sur MRel.
 *
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
 * 2019-01-29 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2019-01-29
 */
public class MRelAnomalies {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static final Descriptor diag = new Descriptor();

  // **************************************************************************
  // Constructeurs
  //
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Générer un rapport complet des anomalies de la base de données de OntoRel.
   *
   * @param ontoRel : l'ontoRel.
   * @param maxSize : la longeur maximale possible d'un identifiant.
   * @param directoryPath : l'emplacement du dossier où le fichier doit être créé.
   * @return le fichier du rapport.
   */
  public static File generateAnomaliesReport(OntoRel ontoRel, int maxSize, String directoryPath) {
    generateIdentifierAnomalies(ontoRel.getDatabase(), maxSize);
    return diag.creerFichier(directoryPath + "DatabaseIdentifierAnomalies");
  }

  /**
   * Générer un rapport sur les identifiants ayant une longeur supérieure à la longeur spécifiée.
   * Les identifiants incluent les noms des views et leurs attributs dans toutes les langues
   * d'intérêts.
   *
   * @param db : la base de données ontorel.
   * @param maxSize : la longeur maximale d'un identifiant.
   * @return une chaine de caractère listant les anomalies.
   */
  public static String generateIdentifierAnomalies(Database db, int maxSize) {
    diag.titre("Long view identifiers anomalies");
    diag.ajouter("  Note : if the identifier is long, the IRI idenfiers will be used.");
    //
    diag.soustitre("Long view name and attributes identifiers");
    for (Table t : db.getBaseSchema().getTableSet()) {
      if (t.getIri().length() > maxSize) {
        diag.ajouter("  View name IRI " + t.getIri());
      }
      for (Entry<String, String> e : t.getLabels().entrySet()) {
        if (e.getValue().length() > maxSize) {
          diag.ajouter("  View name @" + e.getKey() + ": " + e.getValue());
        }
      }
      //
      for (Attribute a : t.getAttributeSet()) {
        if (a.getAttIri().length() > maxSize) {
          diag.ajouter("    View attribute IRI" + a.getAttIri());
        }
        for (Entry<String, String> e : a.getAttLabels().entrySet()) {
          if (e.getValue().length() > maxSize) {
            diag.ajouter("    View attribute @" + e.getKey() + ": " + e.getValue());
          }
        }
      }
      diag.sauterLigne();
    }
    //
    return diag.toString();
  }
}
