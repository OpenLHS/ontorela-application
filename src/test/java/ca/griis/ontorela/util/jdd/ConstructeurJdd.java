package ca.griis.ontorela.util.jdd;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import ca.griis.monto.ConfigurationLoader;
import ca.griis.monto.facade.owlapi.OwlApiOntology;
import ca.griis.monto.util.Descriptor;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Constructeur de jeux de données.
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : non.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * <i>...</i> <br>
 *
 * <b>Tâches réalisées</b><br>
 * <i>2017-XX-XX (0.2.0) [XX] ... </i> <br>
 * <i>2017-11-13 (0.1.0) [CK] Mise en oeuvre initiale. </i> <br>
 *
 * <p>
 * <b>Copyright</b> 2014-2016, Μῆτις (http://info.usherbrooke.ca/llavoie/) <br>
 * <b>Copyright</b> 2017, GRIIS (http://griis.ca/) <br>
 *
 * <p>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé) <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke <br>
 * Sherbrooke (Québec) J1K 2R1 CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 *
 * @since 2018-04-13
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */
public class ConstructeurJdd {
  // **************************************************************************
  // Attributs spécifiques
  //
  // **************************************************************************
  // Constructeurs
  //
  // **************************************************************************
  // Opérations propres
  //
  public static OwlApiOntology loadOntology(File repo) throws IOException {
    OwlApiOntology ontoJdd = null;
    for (File configFile : repo.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return !pathname.isHidden()
            && (pathname.getName().endsWith(".yml") || pathname.getName().endsWith(".yaml"));
      }
    })) {
      ontoJdd = new OwlApiOntology(ConfigurationLoader.loadConfiguration(configFile));
    }
    return ontoJdd;
  }

  /**
   * Get Yaml files from a directory.
   *
   * @param jdd : a directory.
   * @return List of Yaml files.
   */
  public static List<File> getYamlConfigurationFiles(File jdd) {
    assert jdd.isDirectory() == true : "Jdd must be a directory " + jdd.getAbsolutePath();
    List<File> yamlFiles = new ArrayList<>();
    for (File configFile : jdd.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return !pathname.isHidden()
            && (pathname.getName().endsWith(".yml") || pathname.getName().endsWith(".yaml"));
      }
    })) {
      yamlFiles.add(configFile);
    }
    return yamlFiles;
  }

  /**
   * Get Yaml files from a directory.
   *
   * @param jdd : a directory.
   * @return List of Yaml files.
   */
  public static List<File> getOntologyConfigurationFiles(File jdd) {
    assert jdd.isDirectory() : "Jdd must be a directory " + jdd.getAbsolutePath();
    List<File> yamlFiles = new ArrayList<>();
    for (File configFile : getYamlConfigurationFiles(jdd)) {
      if (configFile.getName().contains("ontology")) {
        yamlFiles.add(configFile);
      }
    }
    return yamlFiles;
  }

  /**
   * Get Yaml files from a directory.
   *
   * @param jdd : a directory.
   * @return List of Yaml files.
   */
  public static List<File> getDatabaseConfigurationFiles(File jdd) {
    assert jdd.isDirectory() : "Jdd must be a directory " + jdd.getAbsolutePath();
    List<File> yamlFiles = new ArrayList<>();
    for (File configFile : getYamlConfigurationFiles(jdd)) {
      if (configFile.getName().contains("database")) {
        yamlFiles.add(configFile);
      }
    }
    return yamlFiles;
  }

  /**
   * Création des jeux de données.
   * Méthode permettant de parcourir les sous-dossiers du dossier principal indiqué lors de
   * l'initialisation. Va créer les schémas dans la base de données et instancie les schémas
   * d'origine et de destination.
   *
   * @param cheminDossierEssaie : dossier contenant les dossiers des jeux de données.
   * @return L'ensemble de fichier de test par dossier.
   */
  public static File[] creationJddParRepo(String cheminDossierEssaie) {
    File dossierEssai = new File(cheminDossierEssaie);
    FileFilter filtreEss = new FileFilter() {
      @Override
      public boolean accept(File fichier) {
        return !fichier.isHidden() && fichier.isDirectory();
      }
    };
    File[] listeFichier = dossierEssai.listFiles(filtreEss);
    return listeFichier;
  }

  /**
   * Récupérer le fichier du jeu de données.
   *
   * @param repertoire
   * @return le fichier du jeu de données.
   */
  public static File obtenirScript(File repertoire) {
    FileFilter filtreEss = new FileFilter() {
      @Override
      public boolean accept(File fichier) {
        return !fichier.isHidden() && !fichier.isDirectory()
            && (fichier.getName().endsWith(".txt") || fichier.getName().endsWith(".onto"));
      }
    };
    File[] scripts = repertoire.listFiles(filtreEss);
    assertNotEquals(
        Float.parseFloat(
            "Aucun fichier est défini. " + "Le nom du script doit être <nom>.txt ou <nom>.onto"),
        0,
        scripts.length);
    assertFalse(scripts.length > 1, "Plusieurs fichiers sont définis. ");
    return scripts[0];
  }

  public static void genererTrace(String nomFichierTrace, String schema_id, Descriptor d) {
    String fichier = "build/test-results/" + "results_" + schema_id + "/" + nomFichierTrace;
    d.creerFichier(fichier, ".trace");
  }
}
