package ca.griis.ontorela.generator;

import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.mrel.MembershipConstraint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Une interface pour créer et accéder à des générateur de code utilisant StringTemplate.
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui | non (pourquoi).</li>
 * <li>Clonabilité : oui | non (pourquoi).</li>
 * <li>Modifiabilité : oui | non (pourquoi).</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-03-21 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2019-03-21
 */
public abstract class Generator {
  // **************************************************************************
  // Attributs spécifiques
  //
  private final Database database;
  private final String baseSchema;
  private final String currentDate;

  // **************************************************************************
  // Constructeurs
  //

  /**
   * @param database
   */
  public Generator(Database database) {
    this.database = database;
    this.baseSchema = this.database.getBaseSchema().getName();
    //
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
    Date date = new Date();
    this.currentDate = dateFormat.format(date);
  }

  // **************************************************************************
  // Opérations propres
  //
  protected String quote(String s) {
    char quote = '"';
    return quote + s + quote;
  }

  /**
   * Creates a file.
   *
   * @param directoryPath : the path of the output directory
   * @param fileName : the file name
   * @param extension : the extension of the file
   * @return a file
   * @throws IOException Thow if the directory is not created.
   */
  public File createFile(String directoryPath, String fileName, String extension)
      throws IOException {
    File directory = new File(directoryPath);
    if (!directory.exists() && !directory.mkdirs()) {
      throw new IOException("Unable to create directory" + directoryPath);
    }
    File f = new File(directoryPath + fileName + extension);
    return f;
  }

  /**
   * Creates an SQL file.
   *
   * @param outDirPath : the path of the output directory
   * @param order : the number that defines the order of execution
   * @param desc : the small description that describes the file content
   * @param version : the schema version to be used to define the file name
   * @return SSQ DDL file as outDirPath/schemaName_suffix_version_currentDate.sql
   */
  public File createSqLFile(String outDirPath, String order, String desc, String version) {
    String fileName =
        order + "-" + String.join("_", this.baseSchema, desc, version, this.currentDate);
    String path = outDirPath.endsWith("/") ? outDirPath : outDirPath + "/";
    File file = null;
    try {
      file = createFile(path, fileName, ".sql");
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return file;
  }

  /**
   * Creates a documentation file.
   *
   * @param outDirPath : the path of the output directory
   * @param desc : description file content
   * @param version : the schema version to be used to define the file name
   * @return SSQ DDL file as outDirPath/schemaName_suffix_version_currentDate.sql
   */
  public File createDocFile(String outDirPath, String desc, String version) {
    String fileName = String.join("_", this.baseSchema, desc, version, this.currentDate);
    String path = outDirPath.endsWith("/") ? outDirPath : outDirPath + "/";
    File file = null;
    try {
      file = createFile(path, fileName, ".txt");
    } catch (IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return file;
  }

  /**
   * Copier un fichier.
   *
   * @param source : le fichier source.
   * @param destination : le fichier destination.
   */
  public File copyFile(File source, File destination) {
    try (InputStream is = new FileInputStream(source);
        OutputStream os = new FileOutputStream(destination)) {
      byte[] buffer = new byte[1024];
      int length;
      while ((length = is.read(buffer)) > 0) {
        os.write(buffer, 0, length);
      }
      return destination;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * @return database
   */
  public Database getDatabase() {
    return this.database;
  }

  /**
   * @return baseSchema
   */
  public String getBaseSchemaId() {
    return this.baseSchema;
  }

  /**
   * @return currentDate
   */
  public String getCurrentDate() {
    return this.currentDate;
  }

  protected abstract String generateCreateDomainStatements();

  protected abstract String generateDropDomainStatements();

  protected abstract String generateCreateTableStatements();

  protected abstract String generateCreateFkStatements();

  protected abstract String generateDropTableStatements();

  protected abstract String generateDropSchemaStatements();

  protected abstract String generateIriViews(String schemaView);

  protected abstract String generateCheckMembership(MembershipConstraint constraint);

}
