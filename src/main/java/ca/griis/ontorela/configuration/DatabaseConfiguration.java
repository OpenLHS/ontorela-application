package ca.griis.ontorela.configuration;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * La configuration pour la génération de la base de données de l'OntoRel.
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
 * 2021-03-10 [KB] : Modifier pour accepter plusieres rdbms names
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2021-03-10 (0.2.0) [CK] Ajout paramètre activation réduction de la redondance des axiomes.<br>
 * 2018-09-06 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @version 0.2.0
 * @since 2018-09-06
 */
public class DatabaseConfiguration {
  // **************************************************************************
  // Attributs spécifiques
  //
  private String schemaName;
  private List<String> rdbmsName;
  private String defaultKeyName;
  private String defaultKeyDomainName;
  private String defaultKeyType;
  private String defaultAttributeName;
  private String defaultAttributeDomaineName;
  private String defaultAttributeType;
  private String owlSqlTypeMapperFilePath;
  private String ontoRelDicFilePath;
  private int maxIdentifierLength;
  private boolean useIriAsTableId;
  private boolean normalizeDatatype;
  private boolean generateOpTable;
  private List<String> languages;
  private OwlSqlDatatypes owlSqlTypeMapper;
  private String configDirectoryPath;
  private boolean removeThingTable;

  // **************************************************************************
  // Constructeurs
  //
  protected DatabaseConfiguration() {
    super();
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //


  /**
   * Obtenir le nom du schéma de base a généré.
   *
   * @return Le nom du schéma de base.
   */
  public String getSchemaName() {
    return schemaName;
  }

  /**
   * Définir le nom du schéma de base a généré.
   *
   * @param schemaName : le nom du schéma de base.
   */
  public void setSchemaName(String schemaName) {
    this.schemaName = schemaName;
  }

  /**
   * Obtenir la liste des SGDB d'intérêts.
   *
   * @return la liste de noms des SGBD cibles.
   */
  public List<String> getRdbmsName() {
    return this.rdbmsName;
  }

  /**
   * Définir le(s) nom(s) du SGBD cible.
   * <p>
   * Si la liste rdbmsName n'est pas défini, le SGBD par défaut est postgresql.
   *
   * @param rdbmsName : le nom du SGBD cible.
   * @throws Exception
   */
  public void setRdbmsName(List<String> rdbmsName) throws Exception {
    this.rdbmsName = rdbmsName;
    // Gestion du param List null
    if (rdbmsName == null) {
      this.rdbmsName = new ArrayList<>();
      this.rdbmsName.add("postgresql");
    }

    // Verification des noms dans la liste
    List<String> rdbmsDispo = Arrays.asList("postgresql", "mssql");
    for (String sgbd : this.rdbmsName) {
      if (!rdbmsDispo.contains(sgbd)) {
        throw new Exception("Rdbms name :" + sgbd + " not recognized. ");
      }
    }
    // TODO 2021-05-04: il faut créer une OWLSQLMapper générique puis avoir des spécialisations.
    setOwlSqlTypeMapper(this.rdbmsName.get(0));
  }

  /**
   * Obtenir le nom de la clé artificielle.
   *
   * @return le nom de la clé artificielle.
   */
  public String getDefaultKeyName() {
    return defaultKeyName;
  }

  /**
   * Définir le nom de la clé artificielle.
   *
   * @param defaultKeyName : le nom de la clé artificielle.
   */
  public void setDefaultKeyName(String defaultKeyName) {
    this.defaultKeyName = defaultKeyName;
  }

  /**
   * Obtenir le nom du domaine de la clé artificielle.
   *
   * @return le nom du domaine de la clé artificielle.
   */
  public String getDefaultKeyDomainName() {
    return defaultKeyDomainName;
  }

  /**
   * Définir le nom du domaine de la clé artificielle.
   *
   * @param defaultKeyDomainName : le nom du domaine de la clé artificielle.
   */
  public void setDefaultKeyDomainName(String defaultKeyDomainName) {
    this.defaultKeyDomainName = defaultKeyDomainName;
  }

  /**
   * Obtenir le nom du type de la clé artificielle.
   *
   * @return le nom du type de la clé artificielle.
   */
  public String getDefaultKeyType() {
    return defaultKeyType;
  }

  /**
   * Définir le nom du type de la clé artificielle.
   *
   * @param defaultKeyType : le nom du type de la clé artificielle.
   */
  public void setDefaultKeyType(String defaultKeyType) {
    this.defaultKeyType = defaultKeyType;
  }

  /**
   * Obtenir le nom de l'attribut de valeur par défaut.
   *
   * @return Le nom de l'attribut de valeur par défaut.
   */
  public String getDefaultAttributeName() {
    return defaultAttributeName;
  }

  /**
   * Définir le nom de l'attribut de valeur par défaut.
   *
   * @param defaultAttributeName : le nom de l'attribut de valeur par défaut.
   */
  public void setDefaultAttributeName(String defaultAttributeName) {
    this.defaultAttributeName = defaultAttributeName;
  }

  /**
   * Obtenir le nom du domaine de l'attribut de valeur par défaut.
   *
   * @return Le nom du domaine de l'attribut de valeur par défaut.
   */
  public String getDefaultAttributeDomaineName() {
    return defaultAttributeDomaineName;
  }

  /**
   * Définir le nom du domaine de l'attribut de valeur par défaut.
   *
   * @param defaultAttributeDomaineName : le nom de du domaine l'attribut de valeur par défaut.
   */
  public void setDefaultAttributeDomaineName(String defaultAttributeDomaineName) {
    this.defaultAttributeDomaineName = defaultAttributeDomaineName;
  }

  /**
   * Obtenir le nom du type de l'attribut de valeur par défaut.
   *
   * @return Le nom du type de l'attribut de valeur par défaut.
   */
  public String getDefaultAttributeType() {
    return defaultAttributeType;
  }

  /**
   * Définir le nom du type de l'attribut de valeur par défaut.
   *
   * @param defaultAttributeType : le nom de du type l'attribut de valeur par défaut.
   */
  public void setDefaultAttributeType(String defaultAttributeType) {
    this.defaultAttributeType = defaultAttributeType;
  }

  /**
   * Obtenir la liste des langues d'intérêts. Si la liste n'est pas définit, la langue
   * par défaut est l'anglais.
   *
   * @return languages : la liste des langues d'intérêts.
   */
  public List<String> getLanguages() {
    if (languages == null) {
      this.languages = new ArrayList<>();
      this.languages.add("en");
    }
    return this.languages;
  }

  /**
   * @param languages --> languages
   */
  public void setLanguages(List<String> languages) {
    this.languages = languages;
  }

  /**
   * @return la longeur maximal des identifiants de la base de données. Si non définie, retourne 30.
   */
  public int getMaxIdentifierLength() {
    if (this.maxIdentifierLength == 0) {
      this.maxIdentifierLength = 30;

    }
    return this.maxIdentifierLength;
  }

  /**
   * Définir la longeur maximal des identifiants de la base de données.
   *
   * @param maxIdentifierLength la longeur maximal des identifiants de la base de données.
   */
  public void setMaxIdentifierLength(int maxIdentifierLength) {
    this.maxIdentifierLength = maxIdentifierLength;
  }

  /**
   * Définir l'emplacement du fichier de correspondance des types OWLSQL.
   *
   * @param owlSqlTypeMapperFilePath : l'emplacement du fichier de correspondance
   *        des types OWLSQL.
   */
  public void setOwlSqlTypeMapperFilePath(String owlSqlTypeMapperFilePath) {
    this.owlSqlTypeMapperFilePath = owlSqlTypeMapperFilePath;
  }

  /**
   * @return owlsqlTypeMapper
   */
  public OwlSqlDatatypes getOwlSqlTypeMapper() {
    if (this.owlSqlTypeMapperFilePath != null) {
      File owlSqlTypeMapperFile = new File(owlSqlTypeMapperFilePath);

      if (!owlSqlTypeMapperFile.exists() && this.configDirectoryPath != null) {
        owlSqlTypeMapperFile = Paths.get(this.configDirectoryPath, this.owlSqlTypeMapperFilePath)
            .toFile();
      }
      return ConfigurationLoader.loadPostgresqlDatatypes(owlSqlTypeMapperFile);
    }
    return this.owlSqlTypeMapper;
  }

  /**
   * @return ontoRelDicFilePath
   */
  public String getOntoRelDicFilePath() {
    return ontoRelDicFilePath;
  }

  /**
   * Définir l'emplacement du dictionnaire de données d'un ontoRel.
   *
   * @param ontoRelDicFilePath : l'emplacement du dictionnaire de données d'un ontoRel
   */
  public void setOntoRelDicFilePath(String ontoRelDicFilePath) {
    this.ontoRelDicFilePath = ontoRelDicFilePath;
  }

  /**
   * Définir le paramètre de nomalisation à faire pour les types.
   *
   * @param normalizeDatatype :
   *        Si true : chaque type est converti en une table (clé-valeur).
   *        Si false : chaque type est converti en attribut dans la table de jointure.
   */
  public void setNormalizeDatatype(boolean normalizeDatatype) {
    this.normalizeDatatype = normalizeDatatype;
  }


  /**
   * Obtenir le paramètre de nomalisation des types.
   *
   * @return normalizeDatatype
   *         Si true : le type est transformé en une table (clé-valeur).
   *         Si false : le type est transformé en attribut dans la table de jointure.
   */
  public boolean getNormalizeDatatype() {
    return this.normalizeDatatype;
  }

  /**
   * Définir le type de conversion pour les propriétés de classe.
   *
   * @param generateOpTable : type de conversion des propriétés de classe.
   *        Si true :une propriété de classes est convertie en table.
   *        Si false : une propriété de classe sont converties en vue.
   */
  public void setGenerateOpTable(boolean generateOpTable) {
    this.generateOpTable = generateOpTable;
  }

  /**
   * Obtenir le type de conversion pour les propriétés de classe.
   *
   * @return generateOpTable
   *         * Si true :une propriété de classes est convertie en table.
   *         Si false : une propriété de classe sont converties en vue.
   */
  public boolean getGenerateOpTable() {
    return this.generateOpTable;
  }

  /**
   * Définir la référence pour la création des identifiants des tables.
   *
   * @param useIriAsTableId : la référence pour la création des identifiants des tables
   *        Si true : l'identifiant des tables est construit en utilisant l'IRI locale (short IRI).
   *        Si false : l'identifiant des tables est construit en utilisant une séquence interne.
   */
  public void setuseIriAsTableId(boolean useIriAsTableId) {
    this.useIriAsTableId = useIriAsTableId;
  }

  /**
   * @return useIriAsTableId
   *         Si true : l'identifiant des tables est construit en utilisant l'IRI locale (short IRI).
   *         Si false : l'identifiant des tables est construit en utilisant une séquence interne.
   */
  public boolean getuseIriAsTableId() {
    return this.useIriAsTableId;
  }

  /**
   * Definir le sql type mapper avec le premier nom du rdbnsName
   * TODO : pour le moment on prendre seulement le premier SGBD de la liste
   * a faire de charger plusieres types de OWLSQLTypeMapper
   * Par default c'est postgresql
   *
   * @return void
   */
  public void setOwlSqlTypeMapper(String rdbmsName) {
    if (rdbmsName.equals("mssql")) {
      this.owlSqlTypeMapper = ConfigurationLoader.loadDefaultMssqlDatatypes();
    } else {
      this.owlSqlTypeMapper = ConfigurationLoader.loadDefaultPostgresqlDatatypes();
    }
  }

  /**
   * Définir la référence pour la création ou non de la table Thing.
   *
   * @param removeThingTable : la référence pour la création des identifiants des tables
   *        Si true : création de la table Thing.
   *        Si false : la table Thing ne sera pas créée.
   */
  public void setRemoveThingTable(boolean removeThingTable) {
    this.removeThingTable = removeThingTable;
  }

  /**
   * Définir la référence pour la création ou non de la table Thing.
   *
   * @return removeThingTable
   *         Si true : création de la table Thing.
   *         Si false : la table Thing ne sera pas créée.
   */
  public boolean getRemoveThingTable() {
    return this.removeThingTable;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "DatabaseConfiguration["
        + "schemaName='"
        + schemaName
        + '\''
        + ", rdbmsName="
        + rdbmsName
        + ", defaultKeyName='"
        + defaultKeyName
        + '\''
        + ", defaultKeyDomainName='"
        + defaultKeyDomainName
        + '\''
        + ", defaultKeyType='"
        + defaultKeyType
        + '\''
        + ", defaultAttributeName='"
        + defaultAttributeName
        + '\''
        + ", defaultAttributeDomaineName='"
        + defaultAttributeDomaineName
        + '\''
        + ", defaultAttributeType='"
        + defaultAttributeType
        + '\''
        + ", owlSqlTypeMapperFilePath='"
        + owlSqlTypeMapperFilePath
        + '\''
        + ", ontoRelDicFilePath='"
        + ontoRelDicFilePath
        + '\''
        + ", maxIdentifierLength="
        + maxIdentifierLength
        + ", useIriAsTableId="
        + useIriAsTableId
        + ", normalizeDatatype="
        + normalizeDatatype
        + ", generateOpTable="
        + generateOpTable
        + ", languages="
        + languages
        + ", owlSqlTypeMapper="
        + owlSqlTypeMapper
        + ", removeThingTable="
        + removeThingTable
        + ']';
  }

  /**
   * Obtenir le chemin du répertoire qui contient les fichiers de configuration.
   *
   * @return Le chemin du répertoire de configuration.
   */
  public String getConfigDirectoryPath() {
    return configDirectoryPath;
  }

  /**
   * Définir le chemin du répertoire qui contient les fichiers de configuration.
   *
   * @param configDirectoryPath : le chemin du répertoire de configuration.
   */
  public void setConfigDirectoryPath(String configDirectoryPath) {
    this.configDirectoryPath = configDirectoryPath;
  }
}
