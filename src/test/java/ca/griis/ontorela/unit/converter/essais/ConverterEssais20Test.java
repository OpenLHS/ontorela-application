package ca.griis.ontorela.unit.converter.essais;

import static ca.griis.ontorela.unit.converter.essais.Util.comparerKey;
import static ca.griis.ontorela.unit.converter.essais.Util.comparerTable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.model.Ontology;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.util.jdd.MOntoJdd;
import ca.griis.ontorela.util.jdd.MRelJdd;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * essais pour conversion en table (MOnto -> MRel)
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
 * @version 2.0.0
 * @since 2.0.0
 */
public class ConverterEssais20Test {
  protected static final Descriptor d = new Descriptor();
  private static Database db0;
  private static Ontology o3;
  private static DatabaseConfiguration db2Config;


  @BeforeAll
  public static void initTest() throws OntorelCreationException {
    d.titre("Essais convertisseur avec avec removeThing= true et generateOpTable=true"
        + ConverterEssais10Test.class.getName());
    //
    db2Config = ConfigurationLoader.loadDefaultDatabaseConfiguration();
    db2Config.setuseIriAsTableId(true);
    db2Config.setRemoveThingTable(true);
    MOntoJdd mOntoJdd = new MOntoJdd();
    o3 = mOntoJdd.build04();
    MRelJdd mRelJdd = new MRelJdd();
    db0 = mRelJdd.buildDB4();
  }

  @Test
  @DisplayName("E21- Convertir ontoClass en table et ontoAxiomClassInheritence en FK avec removeThing= true ")
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
  @DisplayName("E22-Convertir les ClassInheritenceAxioms en  FK de type isA avec removeThing= true et generateOpTable=true")
  void TestCreateFkIsA() {
    db2Config.setGenerateOpTable(true);
    OntoRel ontoRel = new OntoRel(o3, db2Config);
    ontoRel.createClassTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getOntology().getOntoAxiomSet().getClassInheritenceAxioms(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createObjectPropertyTableSet(ontoRel.getOntology().getOntoObjectPropertieSet(),
        ontoRel.getDatabase().getBaseSchema());
    ontoRel.createClassAxiomTableSet(ontoRel.getOntology().getOntoClassSet(),
        ontoRel.getDatabase().getBaseSchema());

    // Etape 02- test des Fk vers la table thing et des ontoAxiomClassInheritance
    Set<ForeignKey> fkOntorel = ontoRel.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType().equals(ForeignKey.ForeignKeyType.ISA))
        .collect(Collectors.toSet());
    Set<ForeignKey> fkAttendu = db0.getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType().equals(ForeignKey.ForeignKeyType.ISA))
        .collect(Collectors.toSet());
    assertEquals(fkOntorel.size(), fkAttendu.size());
    assertTrue(comparerKey(fkOntorel, fkAttendu));
  }

  @Test
  @DisplayName("E23- convertir les objectproperty en table avec removeThing= true et generateOpTable=true")
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
    assertEquals(tableOntorel1.size(), tableAttendu1.size());
    assertTrue(comparerTable(tableOntorel1, tableAttendu1));
  }

  @Test
  @DisplayName("E24- Création des Fk de type objectProperty avec removeThing= true et generateOpTable=true")
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
    assertEquals(fkOp.size(), fkAattendu.size());
    assertTrue(comparerKey(fkOp, fkAattendu));
  }


  @Test
  @DisplayName("E25-Création des table  des axiomes d'associations  avec removeThing= true et generateOpTable=true")
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
    System.out.println("attendu");
    System.out.println(axiomAttendu);
    System.out.println("ontorel");
    System.out.println(axiomOntorel);

    assertEquals(axiomAttendu.size(), axiomOntorel.size());
    assertTrue(comparerTable(axiomOntorel, axiomAttendu));
  }

}
