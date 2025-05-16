package ca.griis.ontorela.unit.converter.essais;

import static ca.griis.ontorela.unit.converter.essais.Util.comparerKey;
import static ca.griis.ontorela.unit.converter.essais.Util.comparerTable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoDatatypeI;
import ca.griis.monto.model.OntoClass;
import ca.griis.monto.model.OntoEntitySet;
import ca.griis.monto.model.OntoIri;
import ca.griis.monto.model.Ontology;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.mrel.*;
import ca.griis.ontorela.util.jdd.MOntoJdd;
import ca.griis.ontorela.util.jdd.MRelJdd;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire conversion en table (MOnto -> MRel).
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2025-01-17 (2.0.0) [AS] Mise en oeuvre initiale. <br>
 *
 * <p>
 * <b>Copyright</b> 2016-2017, GRIIS (<a href="https://griis.ca/">...</a>) <br>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé) <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke (Québec) J1K 2R1 <br>
 * CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 * </p>
 *
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 2.0.0
 * @since 2018-09-06
 */
public class ConverterEssais10Test {
  protected static final Descriptor d = new Descriptor();
  private static Database db0;
  private static Database db1;
  private static Database db2;
  private static Ontology o3;
  private static DatabaseConfiguration db2Config;

  @BeforeAll
  public static void initTest() throws OntorelCreationException {
    d.titre("Essais Convertisseur avec removeThing= false et generateOpTable=true"
        + ConverterEssais10Test.class.getName());
    //
    db2Config = ConfigurationLoader.loadDefaultDatabaseConfiguration();
    db2Config.setuseIriAsTableId(true);
    MOntoJdd mOntoJdd = new MOntoJdd();
    o3 = mOntoJdd.build03();
    MRelJdd mRelJdd = new MRelJdd();
    db0 = mRelJdd.buildDB3();
    db1 = mRelJdd.build13(false);
    db2 = mRelJdd.build13(true);
  }

  @Test
  @DisplayName("E11-Test create TopTable- Convertir Thing en Table")
  void testCreateTopTable() {
    // ontologie avec une seule classe Thing
    OntoIri oIri = new OntoIri("ontorela/test/ontology");
    Ontology ontology = new Ontology(oIri);
    OntoIri iri = new OntoIri("ontorela/test/Thing");
    OntoClassI topClass = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, iri);
    ontology.addOntoClass(topClass);
    // remove Thing est false
    db2Config.setRemoveThingTable(true);
    OntoRel ontoRel = new OntoRel(ontology, db2Config);
    d.ajouter(ontoRel.getDatabase().getBaseSchema().getTableSet() + "get top set");
    // Assert
    assertEquals(ontoRel.getDatabase().getBaseSchema().getTableSet().size(), 0);
    db2Config.setRemoveThingTable(false);
    OntoRel ontoRelAvecThing = new OntoRel(ontology, db2Config);
    Table t000 = new Table(Table.TableOrigin.ONTOCLASS, iri, "s00", "Thing");
    assertEquals(
        ontoRelAvecThing.getDatabase().getBaseSchema().getTableSet().stream().findAny().get()
            .getIri(),
        t000.getIri());
  }

  @Test
  @DisplayName("E12- Convertir ontoClass en table et ontoAxiomClassInheritence en FK avec removeThing= false ")
  void TestCreateOntoClassTable() {
    OntoRel ontoRel = new OntoRel(o3, db2Config);
    ontoRel.createClassTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms(),
        ontoRel.getDatabase().getBaseSchema());
    // Etape--01 verifier que les classes sont converties en table
    Set<Table> tableOntoclass = ontoRel.getDatabase().getBaseSchema().getTableSet();
    // tables attendu des ontoclass seuleument
    Set<Table> tableAttenduOntoClass = db0.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.ONTOCLASS))
        .collect(Collectors.toSet());
    assertEquals(tableOntoclass.size(), tableAttenduOntoClass.size());
    assertTrue(comparerTable(tableOntoclass, tableAttenduOntoClass));
    // fin
  }

  @Test
  @DisplayName("E13-Convertir les ClassInheritenceAxioms en  FK de type isA avec removeThing= false et generateOpTable=true")
  void TestCreateFkIsA() {
    db2Config.setGenerateOpTable(true);
    OntoRel ontoRel = new OntoRel(o3, db2Config);
    // isA des Ontoclass
    ontoRel.createClassTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms(),
        ontoRel.getDatabase().getBaseSchema());
    // isA des op: op000 isA op00
    ontoRel.createObjectPropertyTableSet(ontoRel.getOntology().getOntoObjectPropertieSet(),
        ontoRel.getDatabase().getBaseSchema());
    // isA axiomes de classe EXp: C02-Op00-C01 iSA Op00
    ontoRel.createClassAxiomTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getDatabase().getBaseSchema());
    // Etape 02- test des Fk vers la table thing et des ontoAxiomClassInheritance
    Set<ForeignKey> fkOntorel = ontoRel.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType().equals(ForeignKey.ForeignKeyType.ISA))
        .collect(Collectors.toSet());
    Set<ForeignKey> fkAttendu = db0.getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType().equals(ForeignKey.ForeignKeyType.ISA))
        .collect(Collectors.toSet());

    assertEquals(fkAttendu.size(), fkOntorel.size());
    assertTrue(comparerKey(fkOntorel, fkAttendu));
  }

  @Test
  @DisplayName("E14- convertir les objectproperty en table avec removeThing= false et generateOpTable=true")
  void TestCreateOPTable() {
    db2Config.setGenerateOpTable(true);
    OntoRel ontoRel = new OntoRel(o3, db2Config);
    ontoRel.createClassTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createObjectPropertyTableSet(ontoRel.getOntology().getOntoObjectPropertieSet(),
        ontoRel.getDatabase().getBaseSchema());

    // Etape 1 verfiier que les op sont convertie en table
    Set<Table> tableOntorel1 = ontoRel.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.OBJECTPROPERTY))
        .collect(Collectors.toSet());
    // tables attendu des op seuleument
    Set<Table> tableAttendu1 = db0.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.OBJECTPROPERTY))
        .collect(Collectors.toSet());
    assertEquals(tableAttendu1.size(), tableOntorel1.size());
    assertTrue(comparerTable(tableOntorel1, tableAttendu1));
    //
  }

  @Test
  @DisplayName("E15- Création des Fk de type objectProperty avec removeThing= false et generateOpTable=true")
  void TestCreateFkObjectProperty() {
    db2Config.setGenerateOpTable(true);
    OntoRel ontoRel = new OntoRel(o3, db2Config);
    ontoRel.createClassTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createObjectPropertyTableSet(ontoRel.getOntology().getOntoObjectPropertieSet(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createClassAxiomTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getDatabase().getBaseSchema());

    Set<ForeignKey> fkOp = ontoRel.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType()
            .equals(ForeignKey.ForeignKeyType.OBJECTPROPERTY))
        .collect(Collectors.toSet());

    Set<ForeignKey> fkAattendu = db0.getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType()
            .equals(ForeignKey.ForeignKeyType.OBJECTPROPERTY))
        .collect(Collectors.toSet());
    assertEquals(fkAattendu.size(), fkOp.size());
    assertTrue(comparerKey(fkOp, fkAattendu));
  }


  @Test
  @DisplayName("E16- Création des table  des axiomes d'associations  avec removeThing= false et generateOpTable=true")
  void TestCreateAxiomClassAssociation() {
    db2Config.setGenerateOpTable(true);
    OntoRel ontoRel = new OntoRel(o3, db2Config);

    ontoRel.createClassTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createObjectPropertyTableSet(ontoRel.getOntology().getOntoObjectPropertieSet(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createClassAxiomTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getDatabase().getBaseSchema());

    // table de class d'association de ontorel
    Set<Table> axiomOntorel = ontoRel.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.CLASSAXIOM))
        .collect(Collectors.toSet());

    // table de class d'association attendu
    Set<Table> axiomAttendu = db0.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.CLASSAXIOM))
        .collect(Collectors.toSet());
    // pour quoi laxiome merged n a pas pas été convertis ??
    System.out.println("attendu ");
    System.out.println(axiomAttendu);
    System.out.println("Ontorel ");
    System.out.println(axiomOntorel);
    assertEquals(axiomAttendu.size(), axiomOntorel.size());
    assertTrue(comparerTable(axiomOntorel, axiomAttendu));
  }

  @Test
  @DisplayName("E17- Création des table des axiomes de données avec removeThing= false et normalizeDatatype=false")
  void TestCreateDataAxiom() {
    MRelJdd mRelJdd = new MRelJdd();
    db1 = mRelJdd.build13(false);

    db2Config.setNormalizeDatatype(false);
    OntoRel ontoRel = new OntoRel(o3, db2Config);
    ontoRel.createDomainSet(
        (OntoEntitySet<OntoDatatypeI>) ontoRel.getOntology().getOntoDatatypeSet(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createClassTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createDataAxiomTableSet(ontoRel.getOntology().getOntoAxiomSet().getDataAxioms(),
        ontoRel.getDatabase().getBaseSchema());

    Set<Table> tableOntorel = ontoRel.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.DATAAXIOM))
        .collect(Collectors.toSet());
    Set<Table> tableAttendu = db1.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.DATAAXIOM))
        .collect(Collectors.toSet());

    assertEquals(tableAttendu.size(), tableOntorel.size());
    assertTrue(comparerTable(tableOntorel, tableAttendu));

    Set<ForeignKey> fkontorel = ontoRel.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType()
            .equals(ForeignKey.ForeignKeyType.DATAPROPERTY))
        .collect(Collectors.toSet());

    Set<ForeignKey> fkAattendu = db1.getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType()
            .equals(ForeignKey.ForeignKeyType.DATAPROPERTY))
        .collect(Collectors.toSet());
    assertEquals(fkAattendu.size(), fkontorel.size());
    assertTrue(comparerKey(fkontorel, fkAattendu));
  }

  @Test
  @DisplayName("E18-Création des table des axiomes de données avec removeThing= false et normalizeDatatype=true")
  void TestCreateDataAxiomNormalized() {
    MRelJdd mRelJdd = new MRelJdd();
    db2 = mRelJdd.build13(true);
    db2Config.setNormalizeDatatype(true);

    OntoRel ontoRel = new OntoRel(o3, db2Config);
    ontoRel.createDomainSet(
        (OntoEntitySet<OntoDatatypeI>) ontoRel.getOntology().getOntoDatatypeSet(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createClassTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createDataAxiomTableSet(ontoRel.getOntology().getOntoAxiomSet().getDataAxioms(),
        ontoRel.getDatabase().getBaseSchema());

    Set<Table> tableOntorel = ontoRel.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.ONTOTYPE))
        .collect(Collectors.toSet());
    Set<Table> tableAttendu = db2.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.ONTOTYPE))
        .collect(Collectors.toSet());
    assertEquals(tableAttendu.size(), tableOntorel.size());
    assertTrue(comparerTable(tableOntorel, tableAttendu));

    Set<Table> axiomOntorel = ontoRel.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.DATAAXIOM))
        .collect(Collectors.toSet());
    Set<Table> axiomAttendu = db2.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.DATAAXIOM))
        .collect(Collectors.toSet());
    assertEquals(axiomAttendu, axiomOntorel);
    assertTrue(comparerTable(tableOntorel, tableAttendu));

    Set<ForeignKey> fkontorel = ontoRel.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType()
            .equals(ForeignKey.ForeignKeyType.DATAPROPERTY))
        .collect(Collectors.toSet());

    Set<ForeignKey> fkAattendu = db2.getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType()
            .equals(ForeignKey.ForeignKeyType.DATAPROPERTY))
        .collect(Collectors.toSet());
    assertEquals(fkontorel.size(), fkAattendu.size());
    assertTrue(comparerKey(fkontorel, fkAattendu));
  }

}
