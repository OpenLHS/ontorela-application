package ca.griis.ontorela.unit.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.griis.monto.api.OntoObjectPropertyI;
import ca.griis.monto.model.Ontology;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.mrel.ForeignKey.ForeignKeyType;
import ca.griis.ontorela.mrel.MembershipConstraint;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.Table.TableOrigin;
import ca.griis.ontorela.util.jdd.MOntoJdd;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire pour la conversion des propriétés.
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-10-04 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @version 0.1.0
 * @since 2019-10-04
 */
public class PropertyConversionTest {
  /**
   *
   */
  public PropertyConversionTest() {
    // TODO Auto-generated constructor stub
  }

  // **************************************************************************
  // Attributs spécifiques
  //
  private static final String title = "Test unitaire " + PropertyConversionTest.class.getName();
  protected Descriptor d;
  private Ontology o0;
  private DatabaseConfiguration dbConfig;
  private OntoRel ontoRel0;
  private OntoRel ontoRel1;

  // **************************************************************************
  // Constructeurs
  //
  @BeforeAll
  public static void initJdd() {
    //
  }

  @AfterAll
  public static void finTest() {
    //
  }

  @BeforeEach
  public void initTest() throws OntorelCreationException {
    d = new Descriptor();
    MOntoJdd mOntoJdd = new MOntoJdd();
    o0 = mOntoJdd.buildO0();
    //
    dbConfig = ConfigurationLoader.loadDefaultDatabaseConfiguration();
    ontoRel0 = new OntoRel(o0, dbConfig);
    ontoRel1 = new OntoRel(o0, dbConfig);
    System.out.println(" Database config : " + dbConfig.toString());
  }

  // **************************************************************************
  // Cas de test
  //
  @Test
  public void testOntoRelBuilder() {
    // Test debault config
    int opCount = o0.getOntoObjectPropertieSet().size();
    int classCount = o0.getOntoClassSet().size();
    int classAxCount = o0.getOntoAxiomSet().getClassAxioms().size();
    int membershipConstraintCount = 0;
    for (OntoObjectPropertyI op : o0.getOntoObjectPropertieSet()) {
      if (!op.getDomain().isEmpty()) {
        membershipConstraintCount++;
      }
    }
    // ======== Test config avec generateOpTable: true
    ontoRel0.buildOntoRel();
    d.titre(title + "config with generateOpTable:" + dbConfig.getGenerateOpTable());
    printOntoRelDic(ontoRel0);
    List<Table> opTableSet = ontoRel0.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(t -> t.getTableOrigin().equals(TableOrigin.OBJECTPROPERTY))
        .toList();
    assertEquals(opTableSet.size(), opCount);
    //
    int opFk0 = (int) ontoRel0.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(fk -> fk.getForeignKeyType().equals(ForeignKeyType.OBJECTPROPERTY)).count();
    assertEquals(opFk0, opCount * 2L + (classAxCount - 1) * 2L);
    //
    int isaFk0 = (int) ontoRel0.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(fk -> fk.getForeignKeyType().equals(ForeignKeyType.ISA)).count();
    // Ne pas compter Thing et l'axiome réduit
    assertEquals(isaFk0, (classCount - 1) + (classAxCount - 1));
    //
    assertEquals(ontoRel0.getOntoRelDic().getObjectPropertyTableCatalog().size(), opCount);
    //
    assertEquals(ontoRel0.getDatabase().getBaseSchema().getConstraintSet(MembershipConstraint.class)
        .size(), membershipConstraintCount);
    //
    // ======== Test config avec generateOpTable: false
    dbConfig.setGenerateOpTable(false);
    ontoRel1.buildOntoRel();
    d.titre(title + "config with generateOpTable:" + dbConfig.getGenerateOpTable());
    printOntoRelDic(ontoRel1);
    //
    assertEquals(0, ontoRel1.getDatabase().getBaseSchema().getTableSet().stream()
        .filter(t -> t.getTableOrigin().equals(TableOrigin.OBJECTPROPERTY)).count());
    //
    int opFk1 = (int) ontoRel1.getDatabase().getBaseSchema().getForeignKeySet().stream()
        .filter(fk -> fk.getForeignKeyType().equals(ForeignKeyType.OBJECTPROPERTY)).count();
    assertEquals(opFk1, (classAxCount - 1) * 2);
    //
    assertEquals(0, ontoRel1.getOntoRelDic().getObjectPropertyTableCatalog().size());
    //
    assertEquals(0,
        ontoRel1.getDatabase().getBaseSchema().getConstraintSet(MembershipConstraint.class)
            .size());
  }

  // **************************************************************************
  // Opérations propres
  //
  private void printOntoRelDic(OntoRel ontoRel) {
    // ==== Traces
    d.ajouterListe("Liste des tables ",
        ontoRel.getDatabase().getBaseSchema().getTableSet().stream());
    d.ajouterListe("Liste des FK ",
        ontoRel.getDatabase().getBaseSchema().getForeignKeySet().stream());
    d.ajouterMap("OntoRel class-table Dic", ontoRel.getOntoRelDic().getClassTableCatalog());
    d.ajouterMap("OntoRel Op-table Dic", ontoRel.getOntoRelDic().getObjectPropertyTableCatalog());
    d.ajouterMap("OntoRel type-table Dic", ontoRel.getOntoRelDic().getDataTableCatalog());
    d.ajouterMap("OntoRel dataAxiom-table Dic", ontoRel.getOntoRelDic().getDataAxiomTableCatalog());
    System.out.println(d);
  }
}
