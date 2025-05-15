package ca.griis.ontorela.generator;

import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.InclusionConstraint;
import ca.griis.ontorela.mrel.MembershipConstraint;
import ca.griis.ontorela.mrel.ParticipationConstraint;
import ca.griis.ontorela.mrel.Table;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Générateur d'instruction de définition de composants SQL pour une base de
 * données.
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
 * 2018-09-07 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-09-07
 */
public class SqlGenerator extends AbstactSqlGenerator {
  // **************************************************************************
  // Attributs spécifiques
  //
  private final List<String> languages;
  private final SqlTemplate sqlTemplate;
  private final String commentLanguage;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * @param database
   * @param dbConfig
   */
  public SqlGenerator(Database database, DatabaseConfiguration dbConfig) {
    super(database, dbConfig, new SqlTemplate());
    this.languages = dbConfig.getLanguages();
    this.commentLanguage = this.languages.get(0);
    if (dbConfig.getRdbmsName().get(0).equals("mssql")) {
      this.sqlTemplate = new MssqlTemplate();
    } else {
      this.sqlTemplate = new SqlTemplate();
    }
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques générales
  //

  /**
   * @return commentLanguage
   */
  public String getCommentLanguage() {
    return this.commentLanguage;
  }

  // **************************************************************************
  // Opérations : DDL files generation
  //

  /**
   * Produce the DDL file for the creation of the tables.
   *
   * @param outDirPath : a directory path in which the script will be stored.
   * @param version : a schema version to be used to define the file name
   * @param author : a author name to be used in the file header
   * @return SQL File containing the create table and create domain statements.
   */
  public File generateCreateTableDdL(String outDirPath, String version, String author) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "100", "cre-table", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      // Generate file header
      writer
          .write(sqlTemplate.genererEnteteFichierSql(this.getBaseSchemaId(), this.getCurrentDate(),
              author, version,
              "Create domains and tables"));
      // Generate schema
      String desc = "Schéma " + this.getBaseSchemaId() + " créé le " + this.getCurrentDate();
      writer.write(sqlTemplate.genererSchema(this.getBaseSchemaId()));
      writer.write(sqlTemplate.genererSchemaDefinition(this.getBaseSchemaId(), desc));
      // Generate Create domain
      writer.write(generateCreateDomainStatements());
      // Generate tables
      writer.write(generateCreateTableStatements());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produce the DDL file for drop statements for tables and domains. All drop
   * statements are ON CASCADE.
   *
   * @param outDirPath : a directory path in which the script will be stored.
   * @param version : a schema version to be used to define the file name
   * @param author : a author name to be used in the file header
   * @return SQL File containing the drop table and drop domain statements.
   */
  public File generateDropTableDdL(String outDirPath, String version, String author) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "920", "drp-table", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      // Generate file header
      writer
          .write(sqlTemplate.genererEnteteFichierSql(this.getBaseSchemaId(), this.getCurrentDate(),
              author, version,
              "drop tables"));
      // Generate drop statements for all tables
      writer.write(generateDropTableStatements());
      // Generate drop statements for all domains
      writer.write(generateDropDomainStatements());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produce the DDL file for drop statements for schemes. All drop statements are
   * ON CASCADE.
   *
   * @param outDirPath : a directory path in which the script will be stored.
   * @param version : a schema version to be used to define the file name
   * @param author : a author name to be used in the file header
   * @return SQL File containing the drop schema statements.
   */
  public File generateDropSchemaDdl(String outDirPath, String version, String author) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "910", "drp-table", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      // Generate file header
      writer
          .write(sqlTemplate.genererEnteteFichierSql(this.getBaseSchemaId(), this.getCurrentDate(),
              author, version,
              "drop tables"));
      // Generate drop statements for all schemas
      writer.write(generateDropSchemaStatements());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produce the DDL file for delete statements.
   *
   * @param outDirPath : a directory path in which the script will be stored.
   * @param version : a schema version to be used to define the file name
   * @param author : a author name to be used in the file header
   * @return SQL File containing the delete schema statements.
   */
  public File generateDeleteDdL(String outDirPath, String version, String author) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "800", "del-table", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      // Generate file header
      writer
          .write(sqlTemplate.genererEnteteFichierSql(this.getBaseSchemaId(), this.getCurrentDate(),
              author, version,
              "delete tables"));
      // Generate delete statements for all tables
      writer.write(generateDeleteTableStatements());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produce the DDL file for participation check functions.
   *
   * @param outDirPath : a directory path in which the script will be stored.
   * @param version : a schema version to be used to define the file name
   * @param author : a author name to be used in the file header
   * @return SQL File containing the create functions.
   */
  public File generateParticipatioFctDdl(String outDirPath, String version, String author) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "110", "cre-participationCheck-fct", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      // Generate file header
      writer
          .write(sqlTemplate.genererEnteteFichierSql(this.getBaseSchemaId(), this.getCurrentDate(),
              author, version,
              "Create check participation functions"));
      // Generate functions for all the tables
      for (ParticipationConstraint t : this.getDatabase().getBaseSchema()
          .getConstraintSet(ParticipationConstraint.class)) {
        writer.write(generateCheckParticipation(t));
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produce the DDL file for union axiom check functions.
   *
   * @param outDirPath : a directory path in which the script will be stored.
   * @param version : a schema version to be used to define the file name
   * @param author : a author name to be used in the file header
   * @return SQL File containing the create fonctions.
   */
  public File generateCheckUnionAxiomFctDdl(String outDirPath, String version, String author) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "120", "cre-unionAxiomCheck-fct", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      // Generate file header
      writer
          .write(sqlTemplate.genererEnteteFichierSql(this.getBaseSchemaId(), this.getCurrentDate(),
              author, version,
              "Create check union axiom functions"));
      // Generate functions for all the contraintes
      for (InclusionConstraint c : this.getDatabase().getBaseSchema()
          .getConstraintSet(InclusionConstraint.class)) {
        writer.write(generateCheckUnionAxiom(c));
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produce the DDL file for membership check functions.
   *
   * @param outDirPath : a directory path in which the script will be stored.
   * @param version : a schema version to be used to define the file name
   * @param author : a author name to be used in the file header
   * @return SQL File containing the create fonctions.
   */
  public File generateCheckMembershipFctDdl(String outDirPath, String version, String author) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "130", "cre-membershipCheck-fct", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      // Generate file header
      writer
          .write(sqlTemplate.genererEnteteFichierSql(this.getBaseSchemaId(), this.getCurrentDate(),
              author, version,
              "Create check membership functions"));
      // Generate functions for all the tables
      for (MembershipConstraint c : this.getDatabase().getBaseSchema()
          .getConstraintSet(MembershipConstraint.class)) {
        writer.write(generateCheckMembership(c));
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produce the DDL file for the creation of the iri views.
   *
   * @param outDirPath : a directory path in which the script will be stored.
   * @param version : a schema version to be used to define the file name
   * @param author : a author name to be used in the file header
   * @return SQL File containing the create table and create domain statements.
   */
  public File generateCreateIriViewDdL(String outDirPath, String version, String author) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "200", "cre-view-iri", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      String schemaId = this.getBaseSchemaId() + "_iri";
      String desc = "Create views with short IRI of " + schemaId;
      // Generate file header
      writer.write(
          sqlTemplate.genererEnteteFichierSql(schemaId, this.getCurrentDate(), author, version,
              desc));
      // Generate schema
      writer.write(sqlTemplate.genererSchema(schemaId));
      writer
          .write(sqlTemplate.genererSchemaDefinition(schemaId, desc + " " + this.getCurrentDate()));
      // Generate iri view statement for all tables
      writer.write(generateIriViews(schemaId));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produce the DDL file for the creation of the label views for a langauge.
   *
   * @param outDirPath : a directory path in which the script will be stored.
   * @param version : a schema version to be used to define the file name
   * @param author : a author name to be used in the file header
   * @param maxIdLength : maximum identifier length
   * @param ln : the language of the labels used in views
   * @return SQL File containing the create view.
   */
  public File generateCreateLabelViewDdL(String outDirPath, String version, String author,
      int maxIdLength, String ln) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "210", "cre-view-" + ln, version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      String schemaId = this.getBaseSchemaId() + "_" + ln;
      String desc = "Create views in " + ln + " of " + this.getBaseSchemaId();
      if (ln.equals("fr")) {
        desc = "Création des vue en " + ln + " de " + this.getBaseSchemaId();
      }
      // Generate file header
      writer.write(
          sqlTemplate.genererEnteteFichierSql(schemaId, this.getCurrentDate(), author, version,
              desc));
      // Generate schema
      writer.write(sqlTemplate.genererSchema(schemaId));
      writer
          .write(sqlTemplate.genererSchemaDefinition(schemaId, desc + " " + this.getCurrentDate()));
      // Generate create view instructions for all tables
      writer.write(generateLabelViews(schemaId, maxIdLength, ln));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produire le fichier DDL pour la création des vues ontologiques.
   *
   * @param outDirPath : le dossier qui va contenir le fichier
   * @param version : la version du schéma
   * @param author : l'auteur
   * @return Le fichier SQL File contenant les instructions CREATE VIEW.
   */
  public File generateCreateOntoViewDdL(String outDirPath, String version, String author) {
    Writer writer = null;
    File script = createSqLFile(outDirPath, "220", "cre-view-onto", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      String schemaId = this.getBaseSchemaId() + "_ontoView";
      String desc = "Create views with short IRI of " + schemaId;
      // Generate file header
      writer.write(
          sqlTemplate.genererEnteteFichierSql(schemaId, this.getCurrentDate(), author, version,
              desc));
      // Generate schema
      writer.write(sqlTemplate.genererSchema(schemaId));
      writer
          .write(sqlTemplate.genererSchemaDefinition(schemaId, desc + " " + this.getCurrentDate()));
      // Generate iri view statement for all tables
      writer.write(generateOntoViews(schemaId));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Produce documentation file for the SQL schema..
   *
   * @param f : the file in which the definitions will be stored
   * @param author : the author name to be used in the file header
   * @param version : the schema version to be used to define the file name
   * @param dateTime : the date and time of the produced file
   */
  public void produceSqlDoc(File f, String author, String version, String dateTime) {
    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(f, true), StandardCharsets.UTF_8);
      // Generate file header
      writer.write(
          sqlTemplate.genererEnteteFichierDoc(this.getBaseSchemaId(), dateTime, author, version,
              "SQL schema description for " + this.getBaseSchemaId()));
      // Generate global definition
      StringBuilder s = new StringBuilder();
      s.append("=================================== \n");
      s.append("Summary" + "\n");
      s.append("=================================== \n");
      int nbT = this.getDatabase().getBaseSchema().getTableSet().size();
      s.append("Number of tables : " + nbT + "\n");
      s.append("=================================== \n\n");
      writer.write(s.toString());
      // Generate description for all tables
      for (Table t : this.getDatabase().getBaseSchema().getTableSet()) {
        writer.write(t.getTableDeclarationString() + "\n\n");
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Produce diagram file for the SQL schema..
   *
   * @param f : the file in which the definitions will be stored
   * @param author : the author name to be used in the file header
   * @param version : the schema version to be used to define the file name
   * @param dateTime : the date and time of the produced file
   */
  public void produceSqlDiagram(File f, String author, String version, String dateTime) {
    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(f, true), StandardCharsets.UTF_8);
      // Generate file header
      writer.write(
          sqlTemplate.genererEnteteFichierDoc(this.getBaseSchemaId(), dateTime, author, version,
              "SQL schema description for " + this.getBaseSchemaId()));
      //
      StringBuilder s = new StringBuilder();
      // Generate global definition
      s.append("{- \n=================================== \n");
      s.append("Summary" + "\n");
      s.append("=================================== \n");
      s.append(
          "Number of tables : " + this.getDatabase().getBaseSchema().getTableSet().size() + "\n");
      s.append("=================================== \n-} \n\n");
      // Generate description for all tables
      for (Table t : this.getDatabase().getBaseSchema().getTableSet()) {
        s.append(quote(t.getPrefixedIri()) + "::" + quote(t.getLabel("en")) + "\n");
        for (ForeignKey fk : this.getDatabase().getBaseSchema().getOriginForeignKeySet(t)) {
          s.append("  " + quote(fk.getFkId()) + "::" + quote("") + " "
              + quote(fk.getDestination().getPrefixedIri())
              + "::" + quote(fk.getDestination().getLabel("en")) + "\n");
        }
        s.append("\n");
      }
      writer.write(s.toString());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
