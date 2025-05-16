package ca.griis.ontorela.util.jdd;
/**
 * Des jeux de données internes pour OntoRel.
 * Contient des définition manuelles de plusieurs ontoRel.
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-11-14 (0.1.0) [CK] Mise en oeuvre initiale. <br>
 * 
 * <p><b>Copyright</b> 2016-2017, GRIIS (https://griis.ca/) <br>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé) <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke (Québec) J1K 2R1 <br>
 * CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 * </p>
 * 
 * @since 2019-11-14
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.monto.model.BuiltInOwlDatatypeSet;
import ca.griis.monto.model.Ontology;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.mrel.Database;

public class OntoRelJdd {
  // **************************************************************************
  // Attributs spécifiques
  //
  protected static Descriptor d = new Descriptor();
  private static MRelJdd mRelJdd = new MRelJdd();
  private Database db1;
  private static MOntoJdd mOntoJdd = new MOntoJdd();
  private Ontology o1;

  //
  // **************************************************************************
  // Constructeurs
  //
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
  public Database getDb1() {
    return db1;
  }

  public OntoRelCat buildOntoRel0() {
    //
    db1 = mRelJdd.buildDB1();
    assertNotNull(db1);
    assertTrue(db1.getBaseSchema().getTableSet().size() > 0);
    assertTrue(db1.getBaseSchema().getForeignKeySet().size() > 0);
    o1 = mOntoJdd.buildO1();
    assertNotNull(o1);
    //
    OntoRelCat ontoRel = new OntoRelCat();
    // ================ Type - Table
    ontoRel.addDataTableCatalogEntry(BuiltInOwlDatatypeSet.OwlDatatype.XSD_STRING.getType(),
        mRelJdd.getTd00());
    ontoRel.addDataTableCatalogEntry(mOntoJdd.getD00(), mRelJdd.getTd01());
    // ================ Classe - Table
    ontoRel.addClassTableCatalogEntry(mOntoJdd.getC00(), mRelJdd.getT001());
    ontoRel.addClassTableCatalogEntry(mOntoJdd.getC01(), mRelJdd.getT001());
    ontoRel.addClassTableCatalogEntry(mOntoJdd.getC02(), mRelJdd.getT002());
    ontoRel.addClassTableCatalogEntry(mOntoJdd.getC03(), mRelJdd.getT003());
    // ================ Axiome - Table
    ontoRel.addAxiomTableCatalogEntry(mOntoJdd.getAc0(), mRelJdd.getTa00());
    ontoRel.addAxiomTableCatalogEntry(mOntoJdd.getAc1(), mRelJdd.getTa01());
    ontoRel.addAxiomTableCatalogEntry(mOntoJdd.getAc2(), mRelJdd.getTa03());
    ontoRel.addAxiomTableCatalogEntry(mOntoJdd.getAd0(), mRelJdd.getTa02());
    ontoRel.addAxiomTableCatalogEntry(mOntoJdd.getAd1(), mRelJdd.getTa04());
    return ontoRel;
  }
}
