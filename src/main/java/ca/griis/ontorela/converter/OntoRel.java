package ca.griis.ontorela.converter;


import ca.griis.monto.api.OntoAxiomClassAssociationI;
import ca.griis.monto.api.OntoAxiomClassInheritanceI;
import ca.griis.monto.api.OntoAxiomDataAssociationI;
import ca.griis.monto.api.OntoAxiomI.OntoAxiomOrigin;
import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoClassI.OntoClassOrigin;
import ca.griis.monto.api.OntoDatatypeI;
import ca.griis.monto.api.OntoObjectPropertyI;
import ca.griis.monto.api.OntologyI;
import ca.griis.monto.model.OntoEntitySet;
import ca.griis.monto.model.OntoGraph;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRelGraph.EdgeType;
import ca.griis.ontorela.converter.OntoRelGraph.NodeType;
import ca.griis.ontorela.converter.factory.ClassAxiomTableFactory;
import ca.griis.ontorela.converter.factory.ClassTableFactory;
import ca.griis.ontorela.converter.factory.DataAxiomTableFactory;
import ca.griis.ontorela.converter.factory.ObjectPropertyTableFactory;
import ca.griis.ontorela.converter.factory.TableFactory;
import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.ForeignKey.ForeignKeyType;
import ca.griis.ontorela.mrel.InclusionConstraint;
import ca.griis.ontorela.mrel.Schema;
import ca.griis.ontorela.mrel.Schema.SchemaType;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.Table.TableOrigin;
import ca.griis.ontorela.mrel.TableJoin;
import ca.griis.ontorela.mrel.Type;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Générateur d'un OntoRel.
 * OntoRel un modèle de données génénée à partir d'une ontologie et une configuration.
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
 * TODO 2022-02-20 [CK] Réduction redondance des axiomes de données. <br>
 * TODO 2021-03-10 [CK] Refactorisation majeure. <br>
 * TODO 2018-03-20 [CK] Génération des vues 5FN. <br>
 * TODO 2018-03-20 [CK] factoriser pour supporter plusieurs SGBD. <br>
 *
 *
 * <b>Tâches réalisées</b><br>
 * 2025-03-12 (2.1.1) [AS] - Refactorisation <br>
 * 2024-03-06 (0.3.0) [AS] - Creation ou non de la table thing et ses clés <br>
 * 2018-08-10 (0.2.0) [CK] - Refactorisation majeure. <br>
 * 2017-11-13 (0.1.1) [CK] - Creation. <br>
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
 * @version 0.1.8
 * @since 2017-11-13
 */
public class OntoRel {
  // **************************************************************************
  // Attributs spécifiques
  //
  private static final Logger LOGGER = LoggerFactory.getLogger(OntoRel.class);
  private static final Descriptor DIAG = new Descriptor();
  //
  private final DatabaseConfiguration dbConfig;
  private final OntologyI ontology;
  private final Database database;
  private final OntoRelCat dic;
  private final OntoGraph ontoGraph; // TODO 2019-02-26 CK : retirer ?
  private final OntoRelGraph ontoRelGraph;
  private Table topTable;

  // **************************************************************************
  // Constructeurs
  //
  public OntoRel(OntologyI ontology, DatabaseConfiguration dbConfig) {
    this.ontology = ontology;
    this.dbConfig = dbConfig;
    this.ontoGraph = new OntoGraph(ontology);
    this.ontoRelGraph = new OntoRelGraph();
    this.dic = new OntoRelCat();
    //
    Schema baseSchema = new Schema(this.dbConfig.getSchemaName(), SchemaType.BASE);
    this.database = new Database(this.ontology.getAlias(), baseSchema);
    if (!dbConfig.getRemoveThingTable()) {
      this.topTable = createTopTable(this.database.getBaseSchema());
    }
    // Diagnostiques :
    DIAG.titre("OntoRel Diagnostics");
    checkOwlTypesMapper();

  }

  // **************************************************************************
  // Opérations propres réutilisable
  //

  /**
   * Check if owl types are in type config file.
   *
   * @return {@link Boolean}
   */
  private boolean checkOwlTypesMapper() {
    // Check if owl types are in type config file
    DIAG.soustitre("OWL to SQL datatypes");
    boolean istypeInConf = false;
    for (OntoDatatypeI owlDatatype : this.ontology.getOntoDatatypeSet()) {
      if (!this.dbConfig.getOwlSqlTypeMapper().getDatatypes().containsKey(owlDatatype.getName())) {
        DIAG.ajouter(
            "OWL Type " + owlDatatype.getName() + " does not exist in config file. The type "
                + this.dbConfig.getOwlSqlTypeMapper().getSqlValue(owlDatatype.getName())
                + " will be used");
        istypeInConf = false;
      } else {
        istypeInConf = true;
      }
    }
    return istypeInConf;
  }

  /**
   * Build a key attribute with en english an french label. This attribute is used as a default
   * key for all tables. All labels have the same values for all languages.
   *
   * @return Default key attribute with an english an french label and definition.
   */
  private Attribute buildGlobalKeyAttribute(Table t) {
    Attribute a =
        new Attribute(t.getIdentifier().getValue() + "_" + this.dbConfig.getDefaultKeyName(),
            t.getIri() + "_" + this.dbConfig.getDefaultKeyName(),
            this.dbConfig.getDefaultKeyDomainName());
    a.addLabel("en", this.dbConfig.getDefaultKeyName() + " " + t.getLabel("en"));
    a.addLabel("fr", this.dbConfig.getDefaultKeyName() + " " + t.getLabel("fr"));
    a.addDefinition("en", "Default primary key" + " of " + t.getLabel("en"));
    a.addDefinition("fr", "Clé primaire par défaut" + " de " + t.getLabel("fr"));
    return a;
  }

  /**
   * Création de la table mère dans un schéma. La table mère correspondant à la classe mère.
   * Par défault la classe mère est la classe Thing.
   *
   * @param baseSchema : un schéma.
   * @return Un table. La table Thing.
   */
  protected Table createTopTable(Schema baseSchema) {
    if (this.topTable == null) {
      OntoClassI top = this.ontology.getTopClass();
      Table t = new Table(TableOrigin.ONTOCLASS, top.getIri(), baseSchema.getName(),
          top.getIri().getShortIri());
      topTable = t;
      // Ajouter les étiquettes et les définitions
      t.addLabel("fr", "Chose");
      t.addLabel("en", "Thing");
      t.addDescription("fr", "Table mère");
      t.addDescription("en", "Top table");
      // Ajouter les attributs
      t.addKey(buildGlobalKeyAttribute(t), true);
      // Ajouter dans le dictionnaire
      this.dic.addClassTableCatalogEntry(top, t);
      // Ajouter au schéma
      baseSchema.addTable(t);
      // Ajouter au graph
      ontoRelGraph.addNode(NodeType.CLASSNODE, t);
    }
    return this.topTable;
  }

  /**
   * Obtenir la table mère. Si elle existe pas la créer dans le schéma de base.
   *
   * @return la table mère.
   */
  private Table getTopTable() {
    return this.topTable;
  }

  // **************************************************************************
  // Opérations propres : Schéma de base, domaines et tables
  //

  /**
   * Créer les domaines pour les types ontologiques et les attributs par défault.
   *
   * @param dataSet : l'ensemble de type ontologique.
   * @param baseSchema : le schéma de base.
   * @return Le schéma de base contenant la définition des domaines.
   */
  public Schema createDomainSet(OntoEntitySet<OntoDatatypeI> dataSet, Schema baseSchema) {
    // création des domaines de UID and XID.
    baseSchema.addDomain(
        new Type(this.dbConfig.getDefaultKeyDomainName(), this.dbConfig.getDefaultKeyType()));
    baseSchema.addDomain(new Type(this.dbConfig.getDefaultAttributeDomaineName(),
        this.dbConfig.getDefaultAttributeType()));
    // création des domaines par type ontologique.
    for (OntoDatatypeI d : dataSet) {
      baseSchema.addDomain(new Type(d.getIri().getShortIri() + "_domain",
          this.dbConfig.getOwlSqlTypeMapper().getSqlValue(d.getIri().getShortIri())));
    }
    return baseSchema;
  }

  /**
   * Créer les tables dérivées des classes et les contraintes référentielles dérivées des axiomes
   * d’héritage de classes.
   *
   * @param classSet : un ensemble de classe ontologique.
   * @param baseSchema : le schéma de base.
   * @return Le schéma de base contenant la définition des tables.
   */
  public Schema createClassTableSet(Set<OntoClassI> classSet,
      Set<OntoAxiomClassInheritanceI> isaSet, Schema baseSchema) {
    // Créer d'une table par classe.
    TableFactory<OntoClassI> classFactory =
        new ClassTableFactory(dic, ontoRelGraph, baseSchema, dbConfig, topTable);

    for (OntoClassI c : classSet) {
      Table t = classFactory.createTable(c);
      // Ajouter un contrainte référentielle vers la table mère.
      if (dbConfig.getRemoveThingTable()) {
        if (getTopTable() != null) {
          ForeignKey fk = baseSchema.addFkForDefaultKey(ForeignKeyType.ISA, t, getTopTable(),
              t.getPrimaryKeyAttributeSet().stream().findFirst().get());
          ontoRelGraph.addLink(EdgeType.ISA, t, getTopTable(), fk);
        }
      } else {
        if (!c.equals(getOntology().getTopClass())
            && isaSet.stream().filter(a -> a.getOntoDeterminant().equals(c)).count() == 0) {
          ForeignKey fk = baseSchema.addFkForDefaultKey(ForeignKeyType.ISA, t, getTopTable(),
              t.getPrimaryKeyAttributeSet().stream().findFirst().get());
          ontoRelGraph.addLink(EdgeType.ISA, t, getTopTable(), fk);
        }
      }
    }
    // Créer une contrainte référentielle par axiome d'hériage.
    for (OntoAxiomClassInheritanceI isa : isaSet) {
      Table origin = dic.getClassTableCatalog().get(isa.getOntoDeterminant());
      Table destination = dic.getClassTableCatalog().get(isa.getOntoDependent());
      if (destination != null) {
        Attribute key = origin.getPrimaryKeyAttributeSet().stream().findFirst().get();
        ForeignKey fk = baseSchema.addFkForDefaultKey(ForeignKeyType.ISA, origin, destination, key);
        ontoRelGraph.addLink(EdgeType.ISA, origin, destination, fk);
      }
    }
    return baseSchema;
  }

  /**
   * Créer les tables dérivées des propriétés de classes et et les contraintes référentielles
   * dérivées
   * des axiomes d’héritage de propriétés
   *
   * @param opSet : un ensemble de proprités de classes.
   * @param baseSchema : le schéma de base.
   * @return Le schéma de base contenant la définition des tables.
   */
  public Schema createObjectPropertyTableSet(Set<OntoObjectPropertyI> opSet,
      Schema baseSchema) {
    TableFactory<OntoObjectPropertyI> tableFactory =
        new ObjectPropertyTableFactory(dic, ontoRelGraph, baseSchema, dbConfig, topTable);
    for (OntoObjectPropertyI op : opSet) {
      tableFactory.createTable(op);
    }
    // ======== Ajouter les contraintes référentielles
    for (OntoObjectPropertyI op : opSet) {
      addObjectPropertyConstraint(op, baseSchema);
    }
    return baseSchema;
  }

  private void addObjectPropertyConstraint(OntoObjectPropertyI op, Schema baseSchema) {
    TableJoin t = this.dic.getObjectPropertyTableCatalog().get(op);
    if (t != null) {
      List<Boolean> supNotFound = new ArrayList<>();
      if (!op.getSuperObjectProperty().isEmpty()) {
        for (OntoObjectPropertyI supOp : op.getSuperObjectProperty()) {
          // supOp est Thing
          TableJoin supT = this.dic.getObjectPropertyTableCatalog().get(supOp);
          if (supT != null) {
            supNotFound.add(false);
            ForeignKey fk = baseSchema.addFk(ForeignKeyType.ISA, t, supT,
                t.getPrimaryKeyAttributeSet(), supT.getPrimaryKeyAttributeSet());
            ontoRelGraph.addLink(EdgeType.ISA, t, supT, fk);
          } else {
            supNotFound.add(true);
          }
        }
      }
      if (supNotFound.isEmpty() || !supNotFound.contains(false)) {
        ArrayList<Attribute> keys = new ArrayList<>(t.getJoinAtt().keySet());
        if (t.getLeftTable() != null) {
          ForeignKey domainFk = baseSchema.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, t,
              t.getLeftTable(), keys.get(0));
          ontoRelGraph.addLink(EdgeType.OPEDGE, t, t.getLeftTable(), domainFk);
        }
        if (t.getRightTable() != null) {
          ForeignKey rangeFk = baseSchema.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, t,
              t.getRightTable(), keys.get(1));
          ontoRelGraph.addLink(EdgeType.OPEDGE, t, t.getRightTable(), rangeFk);
        }
      }
    }
  }

  /**
   * Créer les tables dérivées des axiomes d'association de données inclant les contraintes
   * référentielles et les contraintes de participations.
   *
   * @param dataAxiomSet : l'ensemble des axiomes d'association de données.
   * @param baseSchema : le schéma de base.
   * @return Le schéma de base contenant la définition des tables.
   */
  public Schema createDataAxiomTableSet(Set<OntoAxiomDataAssociationI> dataAxiomSet,
      Schema baseSchema) {
    // Création d'une table par axiome
    TableFactory<OntoAxiomDataAssociationI> tableFactory =
        new DataAxiomTableFactory(dic, ontoRelGraph, baseSchema, dbConfig, topTable);
    for (OntoAxiomDataAssociationI a : dataAxiomSet) {
      tableFactory.createTable(a);
    }
    return baseSchema;
  }

  /**
   * Créer les tables dérivées des axiomes d'association de classe inclant les contraintes
   * référentielles et les contraintes de participations.
   * La création des tables s'effectue en deux étapes : la réduction de redondance puis la création
   * des tables de jointures.
   *
   * @param baseSchema : le schéma de base.
   * @return Le schéma de base contenant la définition des tables.
   */
  public Schema createClassAxiomTableSet(Set<OntoClassI> classSet, Schema baseSchema) {
    // ==== Créer les tables de jointures
    TableFactory<OntoAxiomClassAssociationI> tableFactory =
        new ClassAxiomTableFactory(dic, ontoRelGraph, baseSchema, dbConfig, topTable);
    for (OntoClassI c : classSet) {
      for (OntoAxiomClassAssociationI a : this.ontology.getOntoAxiomSet()
          .getDeterminantEntityAxiomSet(c).getClassAxioms()) {
        LOGGER.debug(DIAG.ajouter("Creating join table for: " + a.getOntoAxiomString()));
        tableFactory.createTable(a);
      }
    }
    return baseSchema;
  }

  // **************************************************************************
  // Opérations propres : Vues
  //
  /**
   * Créer les contraintes d'inclusion pour les classes d'union.
   *
   * @param classSet : un ensemble de classe
   * @param isaSet : un ensemble d'axiome d'héritage.
   * @param baseSchema : le schéma de base.
   * @return Le schéma contenant les contraintes d'inclusion pour les classes d'union.
   */
  protected Schema createInclusionConstraintSet(Set<OntoClassI> classSet,
      Set<OntoAxiomClassInheritanceI> isaSet, Schema baseSchema) {
    for (OntoClassI unionClass : classSet.stream()
        .filter(c -> c.getOntoClassOrigin().equals(OntoClassOrigin.UNION_CLASS))
        .toList()) {
      Table unionTable = this.dic.getClassTableCatalog().get(unionClass);
      //
      Set<Table> elementSet = new LinkedHashSet<>();
      for (OntoAxiomClassInheritanceI unionIsa : isaSet.stream()
          .filter(a -> a.getAxiomOrigin().equals(OntoAxiomOrigin.UNION_AXIOM)
              && a.getOntoDependent().equals(unionClass))
          .toList()) {
        elementSet.add(this.dic.getClassTableCatalog().get(unionIsa.getOntoDeterminant()));
      }
      if (elementSet.size() > 1) {
        InclusionConstraint constraint = new InclusionConstraint(unionTable, elementSet);
        baseSchema.addConstraint(constraint);
      }
    }
    return baseSchema;
  }

  // **************************************************************************
  // Création de la BD
  //
  // **************************************************************************
  /**
   * Contruire tous les composants d'OntoRel.
   */
  public void buildOntoRel() {
    Schema baseSchema = this.database.getBaseSchema();
    // ========================================= Create domains
    OntoEntitySet<OntoDatatypeI> dataSet =
        (OntoEntitySet<OntoDatatypeI>) this.ontology.getOntoDatatypeSet();
    createDomainSet(dataSet, baseSchema);
    // ========================================= Create tables
    // Create class base tables
    // avoid ConcurrentModificationException
    Set<OntoClassI> classSet = new HashSet<>(this.ontology.getOntoClassSet());
    classSet.removeIf(c -> this.ontology.getTopClass().equals(c));

    Set<OntoAxiomClassInheritanceI> isaSet =
        this.ontology.getOntoAxiomSet().getClassInheritenceAxioms();
    createClassTableSet(classSet, isaSet, baseSchema);
    // Create property base tables
    if (dbConfig.getGenerateOpTable()) {
      createObjectPropertyTableSet(this.ontology.getOntoObjectPropertieSet(), baseSchema);
    }
    // Create data axiom base tables
    Set<OntoAxiomDataAssociationI> dataAxiomSet = this.ontology.getOntoAxiomSet().getDataAxioms();
    createDataAxiomTableSet(dataAxiomSet, baseSchema);
    // Create class axiom base tables
    createClassAxiomTableSet(classSet, baseSchema);
    // TODO CK : créer default value attribute createDefaultValueAttribute
    // ========================================= Create constraints
    // Create union axiom check (inclusion) constraints
    createInclusionConstraintSet(classSet, isaSet, baseSchema);
  }

  public File generateReport(String directoryPath) {
    return DIAG.creerFichier(directoryPath + "OntoRelDiagnostics");
  }

  // **************************************************************************
  // Getters
  //

  /**
   * Obtenir la base de données.
   *
   * @return La base de données de OntoRel
   */
  public Database getDatabase() {
    return this.database;
  }

  /**
   * Obtenir la configuration de la base de données.
   *
   * @return La configuration de la base de données de OntoRel.
   */
  public DatabaseConfiguration getDatabaseConfiguration() {
    return this.dbConfig;
  }

  /**
   * Obetnir l'ontologie.
   *
   * @return L'ontologie de OntoRel.
   */
  public OntologyI getOntology() {
    return this.ontology;
  }

  /**
   * Obtenir le graphe de l'ontologie.
   *
   * @return Le graphe de l'ontologie.
   */
  public OntoGraph getOntoGraph() {
    return this.ontoGraph;
  }

  /**
   * @return ontoRelGraph
   */
  public OntoRelGraph getOntoRelGraph() {
    return this.ontoRelGraph;
  }

  /**
   * Obtenir le dictionnaire des éléments OntoRel.
   *
   * @return Le dictionnaire des éléments OntoRel.
   */
  public OntoRelCat getOntoRelDic() {
    return this.dic;
  }
}
