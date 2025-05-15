package ca.griis.ontorela.converter;

import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.generator.SqlGenerator;
import ca.griis.ontorela.mrel.Database;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * générateur de script pour ontorel
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
 * @version 2.0.1
 * @since 2.0.1
 */

public class OntoRelScriptGenerator {
  private final Database database;
  private final DatabaseConfiguration dbConfig;

  public OntoRelScriptGenerator(Database database, DatabaseConfiguration dbConfig) {
    this.database = database;
    this.dbConfig = dbConfig;
  }

  /**
   * Générer tous les scripts de la base de données OntoRel.
   *
   * @param scriptRepo : l'emplacement du dossier qui va contenir les scripts.
   * @return Tous les scripts de la base de données OntoRel dans le dossier spécifié.
   */
  public List<File> generateScripts(String scriptRepo) {
    List<File> scripts = new ArrayList<>();
    SqlGenerator sqlGen = new SqlGenerator(this.database, this.dbConfig);
    // ========== CREATE TABLE
    File creTable = sqlGen.generateCreateTableDdL(scriptRepo, "v0", "OntoRelA");
    scripts.add(creTable);
    // ========== DROP TABLE
    File drpTable = sqlGen.generateDropTableDdL(scriptRepo, "v0", "OntoRelA");
    scripts.add(drpTable);
    // ========== CREATE FUNCTION
    // Functions for participation check
    File checkParticipationFct = sqlGen.generateParticipatioFctDdl(scriptRepo, "v0", "OntoRelA");
    scripts.add(checkParticipationFct);
    // Fonctions for union axiom check
    File checkUnionAxiomFct = sqlGen.generateCheckUnionAxiomFctDdl(scriptRepo, "v0", "OntoRelA");
    scripts.add(checkUnionAxiomFct);
    // Fonctions for memberchip check
    File checkMembershipFct = sqlGen.generateCheckMembershipFctDdl(scriptRepo, "v0", "OntoRelA");
    scripts.add(checkMembershipFct);
    // ========== CREATE VIEW
    File creIriView = sqlGen.generateCreateIriViewDdL(scriptRepo, "v0", "OntoRelA");
    scripts.add(creIriView);
    // Désactivation de creOntoView
    // File creOntoView = sqlGen.generateCreateOntoViewDdL(scriptRepo, "v0", "OntoRelA");
    // scripts.add(creOntoView);
    for (String ln : this.dbConfig.getLanguages()) {
      File crelnView = sqlGen.generateCreateLabelViewDdL(scriptRepo, "v0", "OntoRelA",
          this.dbConfig.getMaxIdentifierLength(), ln);
      scripts.add(crelnView);
    }
    // ========== DELETE
    File delTable = sqlGen.generateDeleteDdL(scriptRepo, "v0", "OntoRelA");
    scripts.add(delTable);
    return scripts;
  }

}
