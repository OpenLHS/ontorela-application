package ca.griis.ontorela;

import ca.griis.ontorela.service.ServiceManager;


/**
 * Point d'entrée. <br>
 * Prend en entrée un dossier contenant une ontologie et deux fichiers de
 * configuration :
 * <ul>
 * <li>Un fichier de configuration pour l'ontologie.</li>
 * <li>Un fichier de configuration pour la base de données.</li>
 * </ul>
 * Génère :
 * <ul>
 * <li>Des scripts pour la création de la BD.</li>
 * <li>Le dictionnaire de données de OntoRel (OntoRelDic).</li>
 * <li>Des graphes : le graphe de l'ontologie, le graphe de l'ontologie
 * normalisée. le graphe de la base de données et le graphe d'OntoRel.</li>
 * <li>Des rapports descriptifs de l'ontologie et de la BD OntoRel.</li>
 * <li>Des rapports d'anomalies.</li>
 * </ul>
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
 * TODO 2021-03-17 KB : Attributs spécifiques (prive ou public)
 * TODO 2021-03-17 KB : Refactoring des methodes du main
 * TODO 2021-03-17 KB : Dependance horizontal avec Monto
 * TODO 2019-03-12 CK : gestion des excceptions.<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2018-09-06 (0.1.0) [CK] Mise en oeuvre initiale. <br>
 * 2024-12-20 (2.0.0) [SSC] Séparation des différentes fonctions en plusieurs appels possibles.
 * Migration dans des classes séparées du main.
 * 2024-01-31 (2.0.1) [AS] Migration des appels des fonctions dans service manger
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
 * @since 2018-09-06
 */
public class Ontorela {
  /**
   * @param args : le chemin vers un dossier qui contient les fichiers de configuration.
   */
  public static void main(String[] args)
      throws Exception {
    if (args.length < 2) {
      throw new IllegalArgumentException(
          "Missing argument. Should specify a function name followed by configuration directory.");
    }
    String processFunc = args[0];
    String repoPath = args[1];
    try {
      ServiceManager manager = new ServiceManager(repoPath);
      manager.execute(processFunc, args);
    } catch (Exception e) {
      throw new Exception("Error when execute service");
    }
  }
}
