package ca.griis.ontorela.unit.converter.essais;


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

public class ConverterEssais30Test {
  protected static final Descriptor d = new Descriptor();
  private static Database db0;
  private static Database db1;
  private static Ontology o3;
  private static DatabaseConfiguration db2Config;
  private static OntoRel ontoRel;
  private static OntoRel ontoRel1;

  @BeforeAll
  public static void initTest() throws OntorelCreationException {
    d.titre(
        "Essais Convertisseur avec generateOpTable=false" + ConverterEssais30Test.class.getName());
    //
    MOntoJdd mOntoJdd = new MOntoJdd();
    o3 = mOntoJdd.build03();
    MRelJdd mRelJdd = new MRelJdd();
    db2Config = ConfigurationLoader.loadDefaultDatabaseConfiguration();
    db2Config.setuseIriAsTableId(true);
    db2Config.setGenerateOpTable(false);
    // case01
    db2Config.setRemoveThingTable(true);
    db0 = mRelJdd.buildDB5();
    ontoRel = new OntoRel(o3, db2Config);
    ontoRel.buildOntoRel();
    // case 02
    db2Config.setRemoveThingTable(false);
    db1 = mRelJdd.buildDB6();
    ontoRel1 = new OntoRel(o3, db2Config);
    ontoRel1.buildOntoRel();

  }

  @Test
  @DisplayName("E30-Convertir les ClassInheritenceAxioms en  FK de type isA avec removeThing= true et generateOpTable=false")
  void TestCreateFkIsACase1() {

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
  @DisplayName("E32-Convertir les ClassInheritenceAxioms en  FK de type isA avec removeThing= false et generateOpTable=false")
  void TestCreateFkIsACase2() {

    // Etape 02- test des Fk vers la table thing et des ontoAxiomClassInheritance
    Set<ForeignKey> fkOntorel = ontoRel1.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType().equals(ForeignKey.ForeignKeyType.ISA))
        .collect(Collectors.toSet());
    Set<ForeignKey> fkAttendu = db1.getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType().equals(ForeignKey.ForeignKeyType.ISA))
        .collect(Collectors.toSet());
    System.out.println("Ontorel");
    System.out.println(fkOntorel);
    System.out.println("Attendu");
    System.out.println(fkAttendu);
    assertEquals(fkAttendu.size(), fkOntorel.size());
    assertTrue(comparerKey(fkOntorel, fkAttendu));
  }

  @Test
  @DisplayName("E33- convertir les objectproperty en table avec removeThing= true et generateOpTable=false")
  void TestCreateOPTableCase01() {
    // Etape 1 verfiier que les op sont convertie en table
    Set<Table> tableOntorel = ontoRel.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.OBJECTPROPERTY))
        .collect(Collectors.toSet());
    // tables attendu des op seuleument
    Set<Table> tableAttendu = db0.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.OBJECTPROPERTY))
        .collect(Collectors.toSet());
    assertEquals(tableAttendu.size(), tableOntorel.size());
    assertTrue(comparerTable(tableOntorel, tableAttendu));
    //
  }

  @Test
  @DisplayName("E34- convertir les objectproperty en table avec removeThing= false et generateOpTable=false")
  void TestCreateOPTable() {
    // Etape 1 verfiier que les op sont convertie en table
    Set<Table> tableOntorel1 = ontoRel1.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.OBJECTPROPERTY))
        .collect(Collectors.toSet());
    // tables attendu des op seuleument
    Set<Table> tableAttendu1 = db1.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.OBJECTPROPERTY))
        .collect(Collectors.toSet());
    assertEquals(tableAttendu1.size(), tableOntorel1.size());
    assertTrue(comparerTable(tableOntorel1, tableAttendu1));
    //
  }

  @Test
  @DisplayName("E05- Création des Fk de type objectProperty avec removeThing= true et generateOpTable=false")
  void TestCreateFkObjectPropertyCase01() {
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
  @DisplayName("E05- Création des Fk de type objectProperty avec removeThing= false et generateOpTable=false")
  void TestCreateFkObjectPropertyCase02() {

    Set<ForeignKey> fkOp = ontoRel1.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType()
            .equals(ForeignKey.ForeignKeyType.OBJECTPROPERTY))
        .collect(Collectors.toSet());

    Set<ForeignKey> fkAattendu = db1.getBaseSchema().getForeignKeySet().stream()
        .filter(foreignKey -> foreignKey.getForeignKeyType()
            .equals(ForeignKey.ForeignKeyType.OBJECTPROPERTY))
        .collect(Collectors.toSet());
    assertEquals(fkAattendu.size(), fkOp.size());
    assertTrue(comparerKey(fkOp, fkAattendu));
  }


  @Test
  @DisplayName("E06- Création des table  des axiomes d'associations  avec removeThing= true et generateOpTable=false")
  void TestCreateAxiomClassAssociationCase01() {
    // table de class d'association de ontorel
    Set<Table> axiomOntorel = ontoRel.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.CLASSAXIOM))
        .collect(Collectors.toSet());

    // table de class d'association attendu
    Set<Table> axiomAttendu = db0.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.CLASSAXIOM))
        .collect(Collectors.toSet());

    assertEquals(axiomAttendu.size(), axiomOntorel.size());
    assertTrue(comparerTable(axiomOntorel, axiomAttendu));
  }

  @Test
  @DisplayName("E06- Création des table  des axiomes d'associations  avec removeThing= false et generateOpTable=false")
  void TestCreateAxiomClassAssociationCase02() {
    // table de class d'association de ontorel
    Set<Table> axiomOntorel = ontoRel1.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.CLASSAXIOM))
        .collect(Collectors.toSet());

    // table de class d'association attendu
    Set<Table> axiomAttendu = db1.getBaseSchema().getTableSet().stream()
        .filter(table -> table.getTableOrigin().equals(Table.TableOrigin.CLASSAXIOM))
        .collect(Collectors.toSet());
    System.out.println(axiomAttendu);
    System.out.println(axiomOntorel);
    assertEquals(axiomAttendu.size(), axiomOntorel.size());
    assertTrue(comparerTable(axiomOntorel, axiomAttendu));
  }


  private boolean comparerTable(Set<Table> tableOntorel, Set<Table> tableAttendu) {
    if (tableOntorel.size() != tableAttendu.size()) {
      return false;
    }
    for (Table table : tableOntorel) {
      boolean existe = false;
      for (Table table1 : tableAttendu) {
        if (table1.equals(table)) {
          existe = true;
          break;
        }
      }
      if (!existe) {
        System.out.println("table manquante : " + table.getIri());
        return false;
      }
    }
    return true;
  }

  private boolean comparerKey(Set<ForeignKey> fkOntorel, Set<ForeignKey> fkAttendu) {
    if (fkOntorel.size() != fkAttendu.size()) {
      return false;
    }
    for (ForeignKey fk : fkOntorel) {
      boolean existe = false;
      for (ForeignKey fk1 : fkAttendu) {
        if (fk1.equals(fk)) {
          existe = true;
          break;
        }
      }
      if (!existe) {
        System.out.println("FK manquante : " + fk.getFkId());
        return false;
      }
    }
    return true;
  }
}
