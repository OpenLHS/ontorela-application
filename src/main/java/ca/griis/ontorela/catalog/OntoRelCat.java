package ca.griis.ontorela.catalog;


import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoAxiomClassAssociationI;
import ca.griis.monto.api.OntoAxiomDataAssociationI;
import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoDataPropertyI;
import ca.griis.monto.api.OntoDatatypeI;
import ca.griis.monto.api.OntoObjectPropertyI;
import ca.griis.monto.api.OntologyI;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.ForeignKey.ForeignKeyType;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.TableJoin;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Catalogue (dictionnaire de données) de OntoRel.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : oui.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2022-08-01 (1.2.1) [MAG] Ajouter les étiquettes et les définitions des entités en plusieurs
 * langues dans le catalogue de données. <br>
 * 2019-10-01 (0.2.0) [CK] Ajouter le catalogue objectProperty-Table. <br>
 * 2018-09-06 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2018-09-06
 */
public class OntoRelCat {
  // **************************************************************************
  // Attributs spécifiques
  //
  private final Map<OntoClassI, Table> classTableCatalog;
  //
  private final Map<OntoDatatypeI, Table> dataTableCatalog;
  //
  private final Map<OntoObjectPropertyI, TableJoin> objectPropertyTableCatalog;
  //
  private final Map<OntoAxiomAssociationI, Table> axiomTableCatalog;
  //
  // **************************************************************************
  // Constructeurs
  //

  /**
   * Contructeur d'un catalogue OntoRel. Initilise tous les catalogues.
   */
  public OntoRelCat() {
    super();
    this.classTableCatalog = new LinkedHashMap<>();
    //
    this.dataTableCatalog = new LinkedHashMap<>();
    this.objectPropertyTableCatalog = new LinkedHashMap<>();
    //
    this.axiomTableCatalog = new LinkedHashMap<>();
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Obtenir les paires Classe-Table.
   *
   * @return catalogue Classe-Table.
   */
  public Map<OntoClassI, Table> getClassTableCatalog() {
    return this.classTableCatalog;
  }

  /**
   * Obtenir les paires Type-Table.
   *
   * @return catalogue Type-Table.
   */
  public Map<OntoDatatypeI, Table> getDataTableCatalog() {
    return this.dataTableCatalog;
  }

  /**
   * Obtenir les paires ObjectProperty-Table.
   *
   * @return catalogue ObjectProperty-Table.
   */
  public Map<OntoObjectPropertyI, TableJoin> getObjectPropertyTableCatalog() {
    return this.objectPropertyTableCatalog;
  }

  /**
   * Obtenir les paires Axiome-Table.
   *
   * @return catalogue Axiome-Table.
   */
  public Map<OntoAxiomAssociationI, Table> getAxiomTableCatalog() {
    return this.axiomTableCatalog;
  }

  /**
   * Obtenir les paires ClassAxiom-TableJoin
   *
   * @return catalogue ClassAxiom-TableJoin
   */
  public Map<OntoAxiomClassAssociationI, TableJoin> getClassAxiomTableCatalog() {
    return this.axiomTableCatalog.entrySet().stream()
        .filter(e -> e.getKey() instanceof OntoAxiomClassAssociationI
            && e.getValue() instanceof TableJoin)
        .collect(Collectors.toMap(e -> (OntoAxiomClassAssociationI) e.getKey(),
            e -> (TableJoin) e.getValue()));
  }

  /**
   * Obtenir les paires DataAxiom-Table
   *
   * @return catalogue DataAxiom-Table
   */
  public Map<OntoAxiomDataAssociationI, Table> getDataAxiomTableCatalog() {
    return this.axiomTableCatalog.entrySet().stream()
        .filter(e -> e.getKey() instanceof OntoAxiomDataAssociationI)
        .collect(Collectors.toMap(e -> (OntoAxiomDataAssociationI) e.getKey(), e -> e.getValue()));
  }


  /**
   * Ajouter une entrée dans le catalogue de correspondance classe-table.
   *
   * @param ontoClass : une classe.
   * @param table : la table correspondante.
   */
  public void addClassTableCatalogEntry(OntoClassI ontoClass, Table table) {
    this.classTableCatalog.put(ontoClass, table);
  }

  /**
   * Ajouter une entrée dans le catalogue de correspondance type-table.
   *
   * @param type : un type ontologique.
   * @param table : la table correspondante.
   */
  public void addDataTableCatalogEntry(OntoDatatypeI type, Table table) {
    this.dataTableCatalog.put(type, table);
  }

  /**
   * Ajouter une entrée dans le catalogue de correspondance objectProperty-table.
   *
   * @param op : une propriété de classe.
   * @param table : la table correspondante.
   */
  public void addOntoObjectPropertyTableCatalogEntry(OntoObjectPropertyI op, TableJoin table) {
    this.objectPropertyTableCatalog.put(op, table);
  }

  /**
   * Récupérer la structure relationnelle définissant les axiomes de classes d'une
   * classe.
   *
   * @param domainClass : une classe ayant le role d'un domaine.
   * @return la structure relationnelle définissant les axiomes de classes d'une
   *         classe.
   */
  public Set<Table> getClassAxiomSetRelStructure(OntoClassI domainClass) {
    Set<Table> res = new LinkedHashSet<>();
    for (OntoAxiomAssociationI a : this.axiomTableCatalog.keySet().stream()
        .filter(a -> a.getOntoDeterminant().equals(domainClass)
            && a instanceof OntoAxiomClassAssociationI)
        .collect(Collectors.toSet())) {
      res.add(this.axiomTableCatalog.get(a));
    }
    return res;
  }

  /**
   * Récupérer la structure relationnelle définissant les axiomes de données d'une
   * classe.
   *
   * @param domainClass : une classe ayant le role d'un domaine.
   * @return la structure relationnelle définissant les axiomes de classes d'une
   *         classe.
   */
  public Set<Table> getDataAxiomSetRelStructure(OntoClassI domainClass) {
    Set<Table> res = new LinkedHashSet<>();
    for (OntoAxiomAssociationI a : this.axiomTableCatalog.keySet().stream()
        .filter(a -> a.getOntoDeterminant().equals(domainClass)
            && a instanceof OntoAxiomDataAssociationI)
        .collect(Collectors.toSet())) {
      res.add(this.axiomTableCatalog.get(a));
    }
    return res;
  }

  /**
   * Ajouter une entrée dans le catalogue de correspondance axiome-table.
   *
   * @param ontoAxiom : un axiome d'association.
   * @param table : la table correspondante.
   */
  public void addAxiomTableCatalogEntry(OntoAxiomAssociationI ontoAxiom, Table table) {
    this.axiomTableCatalog.put(ontoAxiom, table);
  }



  /**
   * Obtenir un fichier contenant du catalogue OntoRel.
   *
   * @param directoryPath : l'emplacement du dossier dans lequel le fichier doit
   *        être crée.
   * @return OntoRelCat Report file.
   */
  public File generateOntoRelCatReport(String directoryPath) {
    Descriptor report = new Descriptor();
    report.titre("Database Report");
    //
    report.soustitre("Class-Table Catalog");
    for (Entry<OntoClassI, Table> e : this.classTableCatalog.entrySet()) {
      report.ajouter(
          e.getKey().getIri().getFullIri() + " -> " + e.getValue().getIdentifier().getValue() + "::"
              + e.getValue().getIri() + "::" + e.getValue().getLabel("en"));
    }
    //
    report.soustitre("Type-Table Catalog");
    for (Entry<OntoDatatypeI, Table> e : this.dataTableCatalog.entrySet()) {
      report.ajouter(
          e.getKey().getIri().getFullIri() + " -> " + e.getValue().getIdentifier().getValue() + "::"
              + e.getValue().getIri() + "::" + e.getValue().getLabel("en"));
    }
    //
    report.soustitre("ObjectProperty-Table Catalog");
    for (Entry<OntoObjectPropertyI, TableJoin> e : this.objectPropertyTableCatalog.entrySet()) {
      report.ajouter(
          e.getKey().getIri().getFullIri() + " -> " + e.getValue().getIdentifier().getValue() + "::"
              + e.getValue().getIri() + "::" + e.getValue().getLabel("en"));
    }
    //
    report.soustitre("Axiom-Table Catalog");
    for (Entry<OntoAxiomAssociationI, Table> e : this.axiomTableCatalog.entrySet()) {
      report.ajouter(
          e.getKey().getOntoAxiomString() + " -> " + e.getValue().getIdentifier().getValue() + "::"
              + e.getValue().getLabel("en"));
    }
    //
    return report.creerFichier(directoryPath + "OntoRelCat");
  }

  /**
   * Générer OntoRelCat en format Json.
   *
   * @param db : la base de données OntoRel.
   * @param directoryPath l'emplacement du dossier dans lequel le fichier doit
   *        être crée.
   * @return OntoRelCat en format Json.
   */
  public File generateJson(OntologyI ontology, Database db, String directoryPath)
      throws IOException {
    if (ontology == null || db == null || directoryPath == null) {
      throw new IllegalArgumentException("Ontology, Database, or directoryPath must not be null");
    }
    JsonFactory factory = new JsonFactory();
    DatabaseConfiguration dbConfig = ConfigurationLoader.loadDefaultDatabaseConfiguration();
    //
    File jsonFile = createDirJsonFile(directoryPath);
    try {
      JsonGenerator gen = factory.createGenerator(jsonFile, JsonEncoding.UTF8);
      gen.writeStartObject();
      addJsonBody(gen, ontology, db, dbConfig);
      gen.writeEndArray();
      gen.writeEndObject();
      gen.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return jsonFile;
  }

  /**
   * Ajouter Json.
   *
   * @param db : la base de données OntoRel.
   * @param gen générateur json
   * @param ontology ontology
   *
   */
  private void addJsonBody(JsonGenerator gen, OntologyI ontology, Database db,
      DatabaseConfiguration dbConfig) throws IOException {
    writeOntoRel(gen, ontology, db);
    writeClasses(gen);
    writeTypes(gen, dbConfig);
    writeObjectProperties(gen);
    writeDataProperties(gen, ontology);
    writeIsaAxioms(gen, db);
    writeClassAxioms(gen);
    writeDataAxioms(gen);

  }

  /**
   * créer le fichier Json.
   *
   * @param directoryPath l'emplacement du dossier dans lequel le fichier doit
   *        être crée.
   * @return File json file
   */
  private File createDirJsonFile(String directoryPath) throws IOException {
    File directory = new File(directoryPath);
    File jsonFile = new File(directoryPath, "OntoRelCat.json");

    if (!directory.exists() && !directory.mkdirs()) {
      throw new IOException("Unable to create directory: " + directoryPath);
    }
    if (!jsonFile.exists() && !jsonFile.createNewFile()) {
      throw new IOException("Unable to create JSON file: " + jsonFile.getAbsolutePath());
    }

    return jsonFile;
  }

  /**
   * Ajouter dataAxiom au Json.
   *
   * @param gen générateur json
   *
   */
  private void writeDataAxioms(JsonGenerator gen) throws IOException {
    // ==== Les axiomes d'association de données
    gen.writeArrayFieldStart("DataAxioms");
    for (Entry<OntoAxiomAssociationI, Table> e : this.axiomTableCatalog.entrySet().stream()
        .filter(a -> a.getKey() instanceof OntoAxiomDataAssociationI)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).entrySet()) {
      gen.writeStartObject();
      gen.writeStringField("expression", e.getKey().getOntoAxiomString());
      gen.writeStringField("tableId", e.getValue().getIdentifier().getValue());
      gen.writeStringField("domainClassIri",
          e.getKey().getOntoDeterminant().getIri().getFullIri());
      gen.writeStringField("domainTableId",
          this.classTableCatalog.get(e.getKey().getOntoDeterminant()).getIdentifier().getValue());
      gen.writeStringField("rangeClassIri",
          ((OntoAxiomDataAssociationI) e.getKey()).getOntoDependent().getIri().getFullIri());
      // Si les types sont convertis en table récupérer l'identifiant, sinon mettre un
      // null
      Table rangeTable =
          this.dataTableCatalog.get(((OntoAxiomDataAssociationI) e.getKey()).getOntoDependent());
      gen.writeStringField("rangeTableId",
          rangeTable == null ? null : rangeTable.getIdentifier().getValue());
      gen.writeStringField("propertyIri", e.getKey().getProperty().getIri().getFullIri());
      // domainParticipation
      gen.writeFieldName("domainParticipation");
      gen.writeStartObject();
      gen.writeStringField("min", String.valueOf(e.getKey().getParticipation().getMin()));
      gen.writeStringField("max",
          String.valueOf(e.getKey().getParticipation().getMax() == Integer.MAX_VALUE ? "*"
              : e.getKey().getParticipation().getMax()));
      gen.writeEndObject();
      gen.writeEndObject();
    }

  }

  /**
   * Ajouter classAxiom au Json.
   *
   * @param gen générateur json
   *
   */
  private void writeClassAxioms(JsonGenerator gen) throws IOException {
    // ==== Les axiomes d'association de classes
    gen.writeArrayFieldStart("ClassAxioms");
    for (Entry<OntoAxiomAssociationI, Table> e : this.axiomTableCatalog.entrySet().stream()
        .filter(a -> a.getKey() instanceof OntoAxiomClassAssociationI)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).entrySet()) {
      gen.writeStartObject();
      gen.writeStringField("expression", e.getKey().getOntoAxiomString());
      gen.writeStringField("origin", e.getKey().getAxiomOrigin().name());
      gen.writeStringField("tableId", e.getValue().getIdentifier().getValue());
      gen.writeStringField("domainClassIri",
          e.getKey().getOntoDeterminant().getIri().getFullIri());
      gen.writeStringField("domainTableId",
          this.classTableCatalog.get(e.getKey().getOntoDeterminant()).getIdentifier().getValue());
      gen.writeStringField("rangeClassIri",
          ((OntoAxiomClassAssociationI) e.getKey()).getOntoDependent().getIri().getFullIri());
      gen.writeStringField("rangeTableId", this.classTableCatalog
          .get(((OntoAxiomClassAssociationI) e.getKey()).getOntoDependent()).getIdentifier()
          .getValue());
      gen.writeStringField("propertyIri", e.getKey().getProperty().getIri().getFullIri());
      // domainParticipation
      gen.writeFieldName("domainParticipation");
      gen.writeStartObject();
      gen.writeStringField("min", String.valueOf(e.getKey().getParticipation().getMin()));
      gen.writeStringField("max",
          String.valueOf(e.getKey().getParticipation().getMax() == Integer.MAX_VALUE ? "*"
              : e.getKey().getParticipation().getMax()));
      gen.writeEndObject();
      gen.writeFieldName("rangeParticipation");
      gen.writeStartObject();
      gen.writeStringField("min", e.getKey().getProperty().isFunctional() ? "1" : "0");
      gen.writeStringField("max", "*");
      gen.writeEndObject();
      gen.writeEndObject();
    }
    gen.writeEndArray();

  }

  /**
   * Ajouter IsaAxiom au Json.
   *
   * @param gen générateur json
   *
   */
  private void writeIsaAxioms(JsonGenerator gen, Database db) throws IOException {
    // ==== Les axiomes d'héritage
    gen.writeArrayFieldStart("IsaAxioms");
    for (ForeignKey isa : db.getBaseSchema().getForeignKeySet().stream()
        .filter(fk -> fk.getForeignKeyType().equals(ForeignKeyType.ISA))
        .collect(Collectors.toSet())) {
      gen.writeStartObject();
      gen.writeStringField("subClassIri", isa.getOrigin().getIri());
      gen.writeStringField("subClassTableId", isa.getOrigin().getIdentifier().getValue());
      gen.writeStringField("superClassIri", isa.getDestination().getIri());
      gen.writeStringField("superClassTableId", isa.getDestination().getIdentifier().getValue());
      gen.writeEndObject();
    }
    gen.writeEndArray();

  }

  /**
   * Ajouter DataProperties au Json.
   *
   * @param gen générateur json
   *
   */
  private void writeDataProperties(JsonGenerator gen, OntologyI ontology) throws IOException {
    // ==== Les propriété de données
    gen.writeArrayFieldStart("DataProperties");
    for (OntoDataPropertyI dpSet : ontology.getOntoDataPropertieSet()) {
      gen.writeStartObject();
      //
      gen.writeStringField("iri", dpSet.getIri().getFullIri());
      gen.writeFieldName("label");
      gen.writeStartObject();
      gen.writeStringField("fr",
          dpSet.getAnnotations().getLabelValues(Locale.FRENCH).stream().findFirst().orElse(null));
      gen.writeStringField("en", dpSet.getAnnotations().getLabelValues(Locale.ENGLISH).stream()
          .findFirst().orElse(null));
      gen.writeEndObject();
      gen.writeArrayFieldStart("domainClassIri");
      for (OntoClassI dpDomain : dpSet.getDomain()) {
        gen.writeString(dpDomain.getIri().getFullIri());
      }
      gen.writeEndArray();
      gen.writeStringField("rangeDataTypeIri",
          dpSet.getRange().stream().map(x -> x.getIri().getFullIri()).findFirst().orElse(null));
      //
      gen.writeEndObject();
    }
    gen.writeEndArray();

  }

  /**
   * Ajouter ObjectProperties au Json.
   *
   * @param gen générateur json
   *
   */
  private void writeObjectProperties(JsonGenerator gen) throws IOException {
    // ==== Les propriété de classe
    gen.writeArrayFieldStart("ObjectProperties");
    for (Entry<OntoObjectPropertyI, TableJoin> e : this.objectPropertyTableCatalog.entrySet()) {
      gen.writeStartObject();
      //
      gen.writeStringField("iri", e.getKey().getIri().getFullIri());
      gen.writeStringField("tableId", e.getValue().getIdentifier().getValue());
      gen.writeFieldName("label");
      gen.writeStartObject();
      gen.writeStringField("fr", e.getKey().getAnnotations().getLabelValues(Locale.FRENCH)
          .stream().findFirst().orElse(null));
      gen.writeStringField("en", e.getKey().getAnnotations().getLabelValues(Locale.ENGLISH)
          .stream().findFirst().orElse(null));
      gen.writeEndObject();
      gen.writeArrayFieldStart("domainClassIri");
      for (OntoClassI opDomain : e.getKey().getDomain()) {
        gen.writeString(opDomain.getIri().getFullIri());
      }
      gen.writeEndArray();
      gen.writeArrayFieldStart("rangeClassIri");
      for (OntoClassI opRange : e.getKey().getRange()) {
        gen.writeString(opRange.getIri().getFullIri());
      }
      gen.writeEndArray();
      //
      gen.writeEndObject();
    }
    gen.writeEndArray();

  }

  /**
   * Ajouter Type au Json.
   *
   * @param gen générateur json
   *
   */
  private void writeTypes(JsonGenerator gen, DatabaseConfiguration dbConfig) throws IOException {
    // ==== Les types
    gen.writeArrayFieldStart("Types");
    for (Entry<OntoDatatypeI, Table> e : this.dataTableCatalog.entrySet()) {
      gen.writeStartObject();
      //
      gen.writeStringField("iri", e.getKey().getIri().getFullIri());
      gen.writeStringField("tableId", e.getValue().getIdentifier().getValue());
      gen.writeFieldName("label");
      gen.writeStartObject();
      gen.writeStringField("fr", e.getKey().getAnnotations().getLabelValues(Locale.FRENCH)
          .stream().findFirst().orElse(null));
      gen.writeStringField("en", e.getKey().getAnnotations().getLabelValues(Locale.ENGLISH)
          .stream().findFirst().orElse(null));
      gen.writeEndObject();
      //
      gen.writeStringField("sqlType",
          dbConfig.getOwlSqlTypeMapper().getSqlValue(e.getKey().getIri().getShortIri()));
      gen.writeEndObject();
    }
    gen.writeEndArray();

  }

  /**
   * Ajouter class au Json.
   *
   * @param gen générateur json
   *
   */
  private void writeClasses(JsonGenerator gen) throws IOException {
    // ==== Les classes
    gen.writeArrayFieldStart("Classes");
    for (Entry<OntoClassI, Table> e : this.classTableCatalog.entrySet()) {
      gen.writeStartObject();
      //
      gen.writeStringField("iri", e.getKey().getIri().getFullIri());
      gen.writeStringField("origin", e.getKey().getOntoClassOrigin().name());
      gen.writeStringField("tableId", e.getValue().getIdentifier().getValue());
      gen.writeFieldName("label");
      gen.writeStartObject();
      gen.writeStringField("fr", e.getKey().getAnnotations().getLabelValues(Locale.FRENCH)
          .stream().findFirst().orElse(null));
      gen.writeStringField("en", e.getKey().getAnnotations().getLabelValues(Locale.ENGLISH)
          .stream().findFirst().orElse(null));
      gen.writeEndObject();
      gen.writeArrayFieldStart("dataAxiomTables");
      for (Table t : this.getDataAxiomSetRelStructure(e.getKey())) {
        gen.writeString(t.getIdentifier().getValue());
      }
      gen.writeEndArray();
      gen.writeArrayFieldStart("classAxiomTables");
      for (Table t : this.getClassAxiomSetRelStructure(e.getKey())) {
        gen.writeString(t.getIdentifier().getValue());
      }
      gen.writeEndArray();
      //
      gen.writeEndObject();
    }
    gen.writeEndArray();

  }

  /**
   * Ajouter ontorel au Json.
   *
   * @param gen générateur json
   *
   */
  private void writeOntoRel(JsonGenerator gen, OntologyI ontology, Database db) throws IOException {
    // ==== Un ontoRel
    gen.writeObjectFieldStart("OntoRel");
    gen.writeStringField("ontologyIri", ontology.getIri().getFullIri());
    gen.writeFieldName("label");
    gen.writeStartObject();
    gen.writeStringField("fr", ontology.getAnnotations().getLabelValues(Locale.FRENCH).stream()
        .findFirst().orElse(null));
    gen.writeStringField("en", ontology.getAnnotations().getLabelValues(Locale.ENGLISH).stream()
        .findFirst().orElse(null));
    gen.writeEndObject();
    gen.writeStringField("dbBaseSchemaId", db.getBaseSchema().getName());
    gen.writeStringField("ontoRelversion", null);
    gen.writeStringField("creationDate",
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()));
    gen.writeEndObject();

  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    Descriptor d = new Descriptor();
    d.ajouter("OntoRelCat");
    d.ajouterMap("  # type-table:", getDataTableCatalog());
    d.ajouterMap("  # class-table:", getClassTableCatalog());
    d.ajouterMap("  # dataAxiom-table:", getDataAxiomTableCatalog());
    d.ajouterMap("  # classAxiom-table:", getClassAxiomTableCatalog());
    return d.toString();
  }
}
