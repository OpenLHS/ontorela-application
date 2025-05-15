package ca.griis.ontorela.generator;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoAxiomClassAssociationI;
import ca.griis.monto.api.OntoAxiomDataAssociationI;
import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoDataPropertyI;
import ca.griis.monto.api.OntoDatatypeI;
import ca.griis.monto.api.OntoObjectPropertyI;
import ca.griis.monto.api.OntologyI;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.TableJoin;
import ca.griis.ontorela.mrel.Type;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Générateur d'instruction de définition de composants SQL (procédures stockées) afin d'alimenter
 * la base de
 * données du dictionnaie OntoRelCat.
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : non.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : Oui.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 *
 * <b>Tâches réalisées</b><br>
 * 2024-04-30 (0.3.0) [CK] Correction génération des tuples pour les propriétés d'objets. <br>
 * 2022-08-30 (0.1.0) [AMG] Mise en oeuvre initiale. <br>
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
 * @version 0.3.0
 * @since 2018-09-07
 */
public class OntoRelCatDatabaseGenerator extends SqlGenerator {

  // Génération de la clé UUID afin d'identifier chaque execution d'OntoRela
  private static final String uniqueKey = UUID.randomUUID().toString();
  // **************************************************************************
  // Attributs spécifiques
  //
  private final SqlTemplate sqlGen;
  private final OntologyI ontology;
  private final OntoRelCat ontodic;
  private final DatabaseConfiguration dbConfig;
  private final File owlFile;

  // **************************************************************************
  // Constructeurs
  //
  public OntoRelCatDatabaseGenerator(Database database, DatabaseConfiguration dbConfig,
      OntologyI ontology,
      OntoRelCat ontodic, File owlFile) {
    super(database, dbConfig);
    this.sqlGen = new SqlTemplate();
    this.dbConfig = dbConfig;
    this.ontology = ontology;
    this.ontodic = ontodic;
    this.owlFile = owlFile;
  }

  // **************************************************************************
  // Création des procédures de génération
  //
  /**
   * Générer l'appel de la procédure : call ontorel_ins
   */
  private String generateOntorel() {
    return sqlGen.generateCallOntorelIns(uniqueKey, "1.2.2",
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now()));
  }

  /**
   * Générer l'appel de la procédure : call onto_config_db_ins
   */
  private String generateOntoConfigdb(DatabaseConfiguration databaseConfiguration) {
    return sqlGen.generateCallConfigdbIns(uniqueKey, databaseConfiguration.getDefaultKeyName(),
        databaseConfiguration.getDefaultKeyDomainName(), databaseConfiguration.getDefaultKeyType(),
        databaseConfiguration.getDefaultAttributeName(),
        databaseConfiguration.getDefaultAttributeDomaineName(),
        databaseConfiguration.getDefaultAttributeType(),
        databaseConfiguration.getMaxIdentifierLength(),
        databaseConfiguration.getuseIriAsTableId(),
        databaseConfiguration.getNormalizeDatatype(),
        databaseConfiguration.getRemoveThingTable(),
        databaseConfiguration.getGenerateOpTable());

  }

  /**
   * Générer l'appel de la procédure : call ontorel_ins
   * TODO 2022-08-30: Récupérer la définition des Schémas qui sont SqlGenerator
   */
  //
  private String generateOntoSchema() {
    StringBuilder s = new StringBuilder();
    getDatabase().getSchemaSet()
        .forEach((x) -> s.append(sqlGen.generateCallSchemaIns(uniqueKey, x.getName(),
            "en", String.valueOf(x.getSchemaType()))));
    return s.toString();
  }

  /**
   * Générer l'appel de la procédure : call Ontology_ins
   */
  private String generateOntology(OntologyI ontology, File owlFile) {
    StringBuilder s = new StringBuilder();
    s.append(sqlGen.generateCallOntologyIns(uniqueKey, ontology.getIri()
        .getFullIri(), owlFile.getName(), ontology.getAlias(), ontology.getVersion(),
        getCreatedOntologyDate(owlFile).creationTime()
            .toString()));
    ontology.getAnnotations().getLabelAnnotation()
        .forEach((a, x) -> s.append(sqlGen.generateOntologyLabelIns(uniqueKey, ontology.getIri()
            .getFullIri(), a.getLanguage(), x)));
    return s.toString();
  }

  /**
   * Générer l'appel de la procédure : call onto_data_type_ins
   */
  private String generateOntoDataType(OntologyI ontology) {
    StringBuilder s = new StringBuilder();

    for (OntoDatatypeI ontoType : ontology.getOntoDatatypeSet()) {
      String schemaDomain = ontoType.getIri().getShortIri() + "_domain";

      Optional<Type> matchingDomain = getDatabase().getBaseSchema().getTypeSet().stream()
          .filter(type -> type.getId().equals(schemaDomain))
          .findFirst();
      if (matchingDomain.isPresent()) {
        s.append(sqlGen.generateCallDataTypeIns(uniqueKey, ontoType.getIri().getFullIri(), null,
            generateSchemaDomainQuote(matchingDomain.get().getId())));
        ontoType.getAnnotations().getLabelAnnotation()
            .forEach((a, x) -> s.append(sqlGen.generateCallLabelIns(uniqueKey,
                ontoType.getIri().getFullIri(), a.getLanguage(), x)));
      }
    }
    return s.toString();
  }

  /**
   * Générer l'appel de la procédure : call onto_data_type_sql_ins
   */
  private String generateOntoDataTypeSql(OntologyI ontology) {
    StringBuilder s = new StringBuilder();

    for (Type e : getDatabase().getBaseSchema().getTypeSet()) {
      Optional<OntoDatatypeI> matchingOntoType = ontology.getOntoDatatypeSet().stream()
          .filter(ontoType -> (ontoType.getIri().getShortIri() + "_domain").equals(e.getId()))
          .findFirst();

      String iri = matchingOntoType.map(ontoDatatypeI -> ontoDatatypeI.getIri().getFullIri())
          .orElse(null);

      s.append(sqlGen.generateCallDataTypeSqlIns(uniqueKey, iri, e.getId(), e.getDomain()));
    }

    return s.toString();
  }

  /**
   * Générer l'appel de la procédure : call onto_class_ins
   */
  private String generateOntoClass(OntoRelCat ontodic) {
    StringBuilder s = new StringBuilder();
    for (Map.Entry<OntoClassI, Table> e : ontodic.getClassTableCatalog().entrySet()) {
      s.append(sqlGen.generateCallClassIns(uniqueKey, e.getKey().getIri().getFullIri(), e.getValue()
          .getIdentifier().getValue(), e.getKey().getOntoClassOrigin().name()));
      e.getKey().getAnnotations().getLabelAnnotation()
          .forEach((a, x) -> s.append(sqlGen.generateCallLabelIns(uniqueKey, e.getKey().getIri()
              .getFullIri(), a.getLanguage(), x)));
      e.getKey().getAnnotations().getDefinitionAnnotation()
          .forEach((a, x) -> s.append(sqlGen.generateCallDefinitionIns(uniqueKey, e.getKey()
              .getIri().getFullIri(), a.getLanguage(), x)));
    }
    return s.toString();
  }

  /**
   * Générer l'appel des procédures : call onto_object_properties_ins
   * & call onto_object_properties_domain_ins &
   * onto_object_properties_range_ins
   */
  private String generateOntoObjectProperties(OntologyI ontology, OntoRelCat ontodic) {
    StringBuilder s = new StringBuilder();
    for (OntoObjectPropertyI opSet : ontology.getOntoObjectPropertieSet()) {
      // Générer une instance pour la propriété
      Optional<Map.Entry<OntoObjectPropertyI, TableJoin>> opTable =
          ontodic.getObjectPropertyTableCatalog().entrySet().stream()
              .filter(op -> op.getKey().equals(opSet)).findFirst();
      String tableId = null;
      if (opTable.isPresent()) {
        tableId = opTable.get().getValue().getIdentifier().getValue();
      }
      s.append(sqlGen.generateCallObjectPropertiesIns(uniqueKey, opSet.getIri()
          .getFullIri(), tableId));
      // Trouver les annotations
      opSet.getAnnotations().getLabelAnnotation()
          .forEach((a, x) -> s.append(sqlGen.generateCallLabelIns(uniqueKey, opSet.getIri()
              .getFullIri(), a.getLanguage(), x)));
      // Générer une instance pour les domaines de la propriété.
      opSet.getDomain().forEach((a) -> {
        s.append(sqlGen.generateObjectPeropertiesDomainIns(uniqueKey, a.getIri()
            .getFullIri(), opSet.getIri().getFullIri()));
      });
      // Générer une instance pour les codomaines (range) de la propriété.
      opSet.getRange().forEach((a) -> {
        s.append(sqlGen.generateCallObjectPropertiesRangeIns(uniqueKey, a.getIri()
            .getFullIri(), opSet.getIri().getFullIri()));
      });
    }
    return s.toString();
  }

  /**
   * Générer l'appel des procédure : call onto_data_properties_ins
   * & call onto_data_properties_domain_ins &
   * onto_data_properties_range_ins
   */
  private String generateOntoDataProperties(OntologyI ontology, DatabaseConfiguration dbConfig) {
    StringBuilder s = new StringBuilder();
    for (OntoDataPropertyI dpSet : ontology.getOntoDataPropertieSet()) {
      s.append(sqlGen.generateCallDataPropertiesIns(uniqueKey, dpSet.getIri().getFullIri()));
      dpSet.getAnnotations().getLabelAnnotation()
          .forEach((a, x) -> s.append(sqlGen.generateCallLabelIns(uniqueKey, dpSet.getIri()
              .getFullIri(), a.getLanguage(), x)));
      dpSet.getDomain().forEach((a) -> {
        s.append(sqlGen.generateDataPropertiesDomainIns(uniqueKey, a.getIri()
            .getFullIri(), dpSet.getIri().getFullIri()));
      });
      dpSet.getRange().forEach((a) -> {
        s.append(sqlGen.generateCallDataPropertiesRangeIns(uniqueKey, a.getIri()
            .getFullIri(), dpSet.getIri().getFullIri(),
            dbConfig.getOwlSqlTypeMapper()
                .getSqlValue(a.getIri()
                    .getShortIri())));
      });
    }
    return s.toString();
  }

  /**
   * Récupération d'Object Properties en utilisant la table correspandate à l'objet
   */
  public OntoObjectPropertyI getObjectProperty(Table value) {
    return ontodic.getObjectPropertyTableCatalog().entrySet().stream()
        .filter(table -> table != null && table.getValue().equals(value)).map(Map.Entry::getKey)
        .findFirst().orElse(null);
  }

  /**
   * Récupération d'objet OntoClass en utilisant la table correspandate à l'objet
   */
  public OntoClassI getOntoClass(Table value) {
    return ontodic.getClassTableCatalog().entrySet().stream()
        .filter(table -> table != null && table.getValue().equals(value)).map(Map.Entry::getKey)
        .findFirst().orElse(null);
  }

  /**
   * Générer l'appel des procédure : call onto_class_inheritance_ins
   * TODO 2022-09-09 AMG: Récupérer l'origin du foriegn key depuis l'axiom d'héritage pour onto
   */
  private String generateOntoClassInheritance(Database database) {
    StringBuilder s = new StringBuilder();
    for (ForeignKey fk : database.getBaseSchema().getForeignKeySet().stream()
        .filter(fk -> fk.getForeignKeyType().equals(ForeignKey.ForeignKeyType.ISA))
        .collect(Collectors.toSet())) {
      if (fk.getOrigin().getTableOrigin().equals(Table.TableOrigin.ONTOCLASS)
          && fk.getDestination().getTableOrigin().equals(Table.TableOrigin.ONTOCLASS)) {
        s.append(
            sqlGen.generateCallClassInherIns(uniqueKey, getOntoClass(fk.getDestination()).getIri()
                .getFullIri(), getOntoClass(fk.getOrigin()).getIri().getFullIri()));
      }
    }
    return s.toString();
  }

  /**
   * Générer l'appel des procédure : call onto_object_property_inheritance_ins
   * NOTE: Actuellement on prend pas en considération la top table en héritage
   */
  private String generateOntoObjectPropertyInheritance(OntologyI ontology) {
    StringBuilder s = new StringBuilder();
    ontology.getOntoObjectPropertieSet()
        .forEach(objectProperty -> {
          String objectPropertyIri = objectProperty.getIri().getFullIri();
          if (!objectProperty.getIri().getShortIri().equals("topObjectProperty")) {
            objectProperty.getSuperObjectProperty().stream()
                .filter(Objects::nonNull)
                .filter(superProperty -> !superProperty.getIri().getShortIri()
                    .equals("topObjectProperty"))
                .map(superProperty -> superProperty.getIri().getFullIri())
                .forEach(superPropertyIri -> {
                  s.append(sqlGen.generateCallObjectPropInherIns(uniqueKey, objectPropertyIri,
                      superPropertyIri));
                });
          }
        });
    return s.toString();
  }

  /**
   * Générer l'appel de la procédure : call onto_class_axiom_ins
   */
  private String generateOntoClassAxiom(OntoRelCat ontodic) {
    StringBuilder s = new StringBuilder();
    for (Map.Entry<OntoAxiomAssociationI, Table> e : ontodic.getAxiomTableCatalog().entrySet()
        .stream().filter(a -> a.getKey() instanceof OntoAxiomClassAssociationI)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).entrySet()) {
      s.append(sqlGen.generateCallClassAxiomIns(uniqueKey, e.getKey().getOntoDeterminant().getIri()
          .getFullIri(),
          ((OntoAxiomClassAssociationI) e.getKey()).getOntoDependent().getIri()
              .getFullIri(),
          e.getKey().getProperty().getIri()
              .getFullIri(),
          "["
              + e.getKey().getParticipation().getMin()
              + ".."
              + (e.getKey().getParticipation().getMax() == Integer.MAX_VALUE ? "*"
                  : e.getKey()
                      .getParticipation().getMax())
              + "]",
          "["
              + (e.getKey().getProperty().isFunctional() ? "1" : "0")
              + ".. *]",
          e.getKey().getAxiomOrigin().name(), e.getValue().getIdentifier().getValue()));
    }
    return s.toString();
  }

  /**
   * Générer l'appel de la procédure : call onto_data_axiom_ins
   */
  private String generateOntoDataAxiom(OntoRelCat ontodic) {
    StringBuilder s = new StringBuilder();
    for (Map.Entry<OntoAxiomAssociationI, Table> e : ontodic.getAxiomTableCatalog().entrySet()
        .stream().filter(a -> a.getKey() instanceof OntoAxiomDataAssociationI)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).entrySet()) {
      s.append(sqlGen.generateCallDataAxiomIns(uniqueKey, e.getKey().getOntoDeterminant().getIri()
          .getFullIri(),
          ((OntoAxiomDataAssociationI) e.getKey()).getOntoDependent().getIri()
              .getFullIri(),
          e.getKey().getProperty().getIri()
              .getFullIri(),
          "[" + e.getKey().getParticipation().getMin()
              + ".."
              + (e.getKey().getParticipation().getMax() == Integer.MAX_VALUE ? "*"
                  : e.getKey()
                      .getParticipation().getMax())
              + "]",
          e.getKey().getAxiomOrigin()
              .name(),
          e.getValue().getIdentifier()
              .getValue()));
    }
    return s.toString();
  }

  /**
   * Récupération des attributs d'un fichier OWL
   */
  private BasicFileAttributes getCreatedOntologyDate(File owlFile) {
    BasicFileAttributes attr = null;
    try {
      attr = Files.readAttributes(Path.of(owlFile.getPath()), BasicFileAttributes.class);
    } catch (IOException ignored) {
      ignored.printStackTrace();
    }
    return attr;
  }


  /**
   * Méthode permettant de générer une chaîne de caractères formatée avec le schéma et le domaine.
   * 
   * @param domain Le domaine à utiliser dans la chaîne formatée.
   * @return La chaîne formatée comme "schema"."domain".
   */
  private String generateSchemaDomainQuote(String domain) {
    // Obtenir le nom du schéma de la base de données.
    String schema = getDatabase().getBaseSchema().getName();

    // Formater et retourner la chaîne comme "schema"."domain".
    return "\"" + schema + "\".\"" + domain + "\"";
  }


  // **************************************************************************
  // Opérations publiques
  //
  /**
   * Génération de contenu de script DML
   */
  public File generateOntoRelCatDml(String outDirPath, String version, String author) {
    Writer writer;
    File script = createSqLFile(outDirPath, "1003", "OntoRelCat_ins", version);
    try {
      writer = new OutputStreamWriter(new FileOutputStream(script, true), StandardCharsets.UTF_8);
      // Generate file header
      writer.write(sqlGen.genererEnteteFichierSql(this.getBaseSchemaId(), this.getCurrentDate(),
          author, version, "Call procedure created to insert data into OntoRelCat schema"));
      // Generate file content
      writer.write(generateOntorel());
      writer.write(generateOntoConfigdb(dbConfig));
      writer.write(generateOntoSchema());
      writer.write(generateOntology(ontology, owlFile));
      writer.write(generateOntoClass(ontodic));
      writer.write(generateOntoDataType(ontology));
      writer.write(generateOntoDataTypeSql(ontology));
      writer.write(generateOntoObjectProperties(ontology, ontodic));
      writer.write(generateOntoDataProperties(ontology, dbConfig));
      writer.write(generateOntoClassInheritance(getDatabase()));
      writer.write(generateOntoObjectPropertyInheritance(ontology));
      writer.write(generateOntoClassAxiom(ontodic));
      writer.write(generateOntoDataAxiom(ontodic));
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return script;
  }

  /**
   * Générer les scripts de définition du schéma OntoRelCat : la création des tables et des
   * procédures
   *
   * @param scriptRepo : l'emplacement du dossier qui va contenir les scripts.
   * @return les scripts de définition des tables et des procédures.
   */
  public List<File> genereteOntoRelCatDdlScripts(String scriptRepo) {
    List<File> scripts = new ArrayList<>();
    // Create the Insert file of the procedures
    File creCallProc = this.generateOntoRelCatDml(scriptRepo, "v0", "OntoRelA");
    scripts.add(creCallProc);
    return scripts;
  }
}
