package ca.griis.ontorela.util.jdd;

import ca.griis.monto.api.OntoAxiomAssociationI.OntoAxiomType;
import ca.griis.monto.api.OntoAxiomI.OntoAxiomOrigin;
import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoClassI.OntoClassOrigin;
import ca.griis.monto.api.OntoEntityCollectionI;
import ca.griis.monto.api.ParticipationI.ParticipationType;
import ca.griis.monto.model.*;

/**
 * Des jeux de données pour MOnto.
 * Il s'agit d'une instanciation des composants de MRel pour effectuer des tests unitaires.
 * 
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-01-19 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2019-01-19
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */
public class MOntoJdd {
  // **************************************************************************
  // Attributs spécifiques
  //
  private OntoIri oIri;
  private OntoIri d00Iri;
  private OntoIri op00Iri;
  private OntoIri op01Iri;
  private OntoIri op001Iri;
  private OntoIri op02Iri;
  private OntoIri op03Iri;
  private OntoIri dp00Iri;
  private OntoIri c00Iri;
  private OntoIri c000Iri;
  private OntoIri c01Iri;
  private OntoIri c001Iri;
  private OntoIri c02Iri;
  private OntoIri c03Iri;
  private OntoIri thingiri;
  private Ontology o0;
  private OntoDatatype d00;
  private OntoObjectProperty op00;
  private OntoObjectProperty op000;
  private OntoObjectProperty op01;
  private OntoObjectProperty op001;
  private OntoObjectProperty op02;
  private OntoObjectProperty op03;
  private OntoDataProperty dp00;
  private OntoClass c00;
  private OntoClass c000;
  private OntoClass c01;
  private OntoClass c001;
  private OntoClass c02;
  private OntoClass c03;
  private OntoAxiomClassAssociation ac0;
  private OntoAxiomClassAssociation ac00;
  private OntoAxiomClassAssociation acMerged;
  private OntoAxiomClassAssociation ac1;
  private OntoAxiomClassAssociation ac2;
  private OntoAxiomDataAssociation ad0;
  private OntoAxiomDataAssociation ad1;
  private OntoAxiomClassInheritance isa0;
  private OntoAxiomClassInheritance isa1;
  private OntoAxiomClassInheritance isa2;
  private OntoAxiomClassInheritance isa3;
  private OntoAxiomClassInheritance isa4;
  private OntoIri op000Iri;

  // **************************************************************************
  // Constructeurs
  //
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
  public OntoAxiomClassAssociation getAc0() {
    return ac0;
  }

  public OntoAxiomClassAssociation getAc00() {
    return ac00;
  }

  public OntoAxiomClassAssociation getAc1() {
    return ac1;
  }

  public OntoAxiomClassAssociation getAcMerged() {
    return acMerged;
  }

  public OntoAxiomClassAssociation getAc2() {
    return ac2;
  }

  public OntoAxiomDataAssociation getAd0() {
    return ad0;
  }

  public OntoAxiomDataAssociation getAd1() {
    return ad1;
  }

  public OntoClass getC00() {
    return c00;
  }

  public OntoClass getC01() {
    return c01;
  }

  public OntoClass getC02() {
    return c02;
  }

  public OntoClass getC03() {
    return c03;
  }

  public OntoIri getC00Iri() {
    return c00Iri;
  }

  public OntoIri getC01Iri() {
    return c01Iri;
  }

  public OntoIri getC001Iri() {
    return c001Iri;
  }

  public OntoIri getC000Iri() {
    return c000Iri;
  }

  public OntoIri getC02Iri() {
    return c02Iri;
  }

  public OntoIri getC03Iri() {
    return c03Iri;
  }

  public OntoIri getD00Iri() {
    return d00Iri;
  }

  public OntoIri getOp00Iri() {
    return op00Iri;
  }

  public OntoIri getOp02Iri() {
    return op02Iri;
  }

  public OntoIri getOp03Iri() {
    return op03Iri;
  }

  public OntoIri getOp000Iri() {
    return op000Iri;
  }

  public OntoIri getOp001Iri() {
    return op001Iri;
  }

  public OntoIri getOp01Iri() {
    return op01Iri;
  }

  public OntoDatatype getD00() {
    return d00;
  }

  public OntoIri getThingiri() {
    return thingiri;
  }

  public Ontology buildO0() {
    oIri = new OntoIri("OntoRela/ut/ontology0");
    o0 = new Ontology(oIri);
    // ================ DataType
    d00Iri = new OntoIri("OntoRela/ut/d00");
    d00 = new OntoDatatype(d00Iri);
    o0.addOntoDatatype(d00);
    // ================ Classes
    c00Iri = new OntoIri("OntoRela/ut/C000");
    c00 = new OntoClass(OntoClassOrigin.DECLARED, c00Iri);
    o0.addOntoClass(c00);
    // ================ Classes
    c01Iri = new OntoIri("OntoRela/ut/C001");
    c01 = new OntoClass(OntoClassOrigin.DECLARED, c01Iri);
    o0.addOntoClass(c01);
    // ================ Classes
    c02Iri = new OntoIri("OntoRela/ut/C002");
    c02 = new OntoClass(OntoClassOrigin.DECLARED, c02Iri);
    o0.addOntoClass(c02);
    // ================ Properties
    op00Iri = new OntoIri("OntoRela/ut/op00");
    op00 = new OntoObjectProperty(op00Iri, false, false, c00, c01);
    o0.addOntoObjectProperty(op00);

    op01Iri = new OntoIri("OntoRela/ut/op01");
    op01 = new OntoObjectProperty(op01Iri, false, false, c01, c00);
    o0.addOntoObjectProperty(op01);
    // ================ Data properties
    dp00Iri = new OntoIri("OntoRela/ut/dp00");
    dp00 = new OntoDataProperty(dp00Iri, false);
    o0.addOntoDataProperty(dp00);
    // ================ Axioms
    // ==== Class
    ac0 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, op00,
        c00, new Participation(ParticipationType.MIN, 2), c01);
    o0.addOntoAxiom(ac0);
    ac1 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, op00,
        c00, new Participation(ParticipationType.SOME), c01);
    o0.addOntoAxiom(ac1);
    // isA
    isa0 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c02, c01);
    o0.addOntoAxiom(isa0);
    // ==== Data
    ad0 = new OntoAxiomDataAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, dp00,
        c01, new Participation(ParticipationType.EXACT, 1),
        BuiltInOwlDatatypeSet.OwlDatatype.XSD_STRING.getType());
    o0.addOntoAxiom(ad0);
    ad1 = new OntoAxiomDataAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, dp00,
        c01, new Participation(ParticipationType.SOME), d00);
    o0.addOntoAxiom(ad1);
    return o0;
  }

  /**
   * 
   * @return C00 op00 [2..*] C01; op01 [1..*] C02; dp00 [1..1] String
   */
  public Ontology buildO1() {
    oIri = new OntoIri("OntoRela/ut/ontology1");
    o0 = new Ontology(oIri);
    // ================ DataType
    d00Iri = new OntoIri("OntoRela/ut/d00");
    d00 = new OntoDatatype(d00Iri);
    o0.addOntoDatatype(d00);
    // ================ Classes
    c00Iri = new OntoIri("OntoRela/ut/C00");
    c00 = new OntoClass(OntoClassOrigin.DECLARED, c00Iri);
    o0.addOntoClass(c00);
    c01Iri = new OntoIri("OntoRela/ut/C01");
    c01 = new OntoClass(OntoClassOrigin.DECLARED, c01Iri);
    o0.addOntoClass(c01);
    c02Iri = new OntoIri("OntoRela/ut/C02");
    c02 = new OntoClass(OntoClassOrigin.DECLARED, c02Iri);
    o0.addOntoClass(c02);
    c03Iri = new OntoIri("OntoRela/ut/C03");
    c03 = new OntoClass(OntoClassOrigin.DECLARED, c03Iri);
    o0.addOntoClass(c03);
    // ================ Properties
    op00Iri = new OntoIri("OntoRela/ut/op00");
    op00 = new OntoObjectProperty(op00Iri, false, false, c00, c01);
    o0.addOntoObjectProperty(op00);
    op01Iri = new OntoIri("OntoRela/ut/op01");
    op01 = new OntoObjectProperty(op01Iri, false, false);
    o0.addOntoObjectProperty(op01);
    dp00Iri = new OntoIri("OntoRela/ut/dp00");
    dp00 = new OntoDataProperty(dp00Iri, false);
    o0.addOntoDataProperty(dp00);
    // ================ Axioms
    // ==== Class
    ac0 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, op00,
        c00, new Participation(ParticipationType.MIN, 2), c01);
    o0.addOntoAxiom(ac0);
    ac1 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, op01,
        c00, new Participation(ParticipationType.SOME), c02);
    o0.addOntoAxiom(ac1);
    ac2 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, op01,
        c02, new Participation(ParticipationType.SOME), c03);
    o0.addOntoAxiom(ac2);
    // ==== Data
    ad0 = new OntoAxiomDataAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, dp00,
        c00, new Participation(ParticipationType.EXACT, 1),
        BuiltInOwlDatatypeSet.OwlDatatype.XSD_STRING.getType());
    o0.addOntoAxiom(ad0);
    ad1 = new OntoAxiomDataAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, dp00,
        c00, new Participation(ParticipationType.MAX, 1), d00);
    o0.addOntoAxiom(ad1);
    return o0;
  }

  /**
   *
   * @return C00 op00 [1..*] C01; COO op01 [1..*] C02; COO dp00 [1..1] String
   */
  public Ontology buildO2() {
    oIri = new OntoIri("OntoRela/ut/ontology2");
    o0 = new Ontology(oIri);
    // ================ DataType
    d00Iri = new OntoIri("OntoRela/ut/d00");
    d00 = new OntoDatatype(d00Iri);
    o0.addOntoDatatype(d00);
    // =================Class
    c00Iri = new OntoIri("OntoRela/ut/C00");
    c00 = new OntoClass(OntoClassOrigin.DECLARED, c00Iri);
    o0.addOntoClass(c00);
    c01Iri = new OntoIri("OntoRela/ut/C01");
    c01 = new OntoClass(OntoClassOrigin.DECLARED, c01Iri);
    o0.addOntoClass(c01);
    c02Iri = new OntoIri("OntoRela/ut/C02");
    c02 = new OntoClass(OntoClassOrigin.DECLARED, c02Iri);
    o0.addOntoClass(c02);
    c03Iri = new OntoIri("OntoRela/ut/C03");
    c03 = new OntoClass(OntoClassOrigin.DECLARED, c03Iri);
    o0.addOntoClass(c03);
    // ================ Properties
    op00Iri = new OntoIri("OntoRela/ut/op00");
    op00 = new OntoObjectProperty(op00Iri, false, false, c00, c01);
    o0.addOntoObjectProperty(op00);
    // === op sans domaine et range
    op01Iri = new OntoIri("OntoRela/ut/op01");
    op01 = new OntoObjectProperty(op01Iri, false, false);
    o0.addOntoObjectProperty(op01);
    // === op sans range
    op02Iri = new OntoIri("OntoRela/ut/op02");
    OntoEntityCollectionI<OntoClassI> ontoClassIs = new OntoEntitySet<>();
    ontoClassIs.add(c02);
    op02 = new OntoObjectProperty(op02Iri, false, false, ontoClassIs);
    o0.addOntoObjectProperty(op02);
    // =============== Data properties
    dp00Iri = new OntoIri("OntoRela/ut/dp00");
    dp00 = new OntoDataProperty(dp00Iri, false);
    o0.addOntoDataProperty(dp00);
    // =============== Axiom
    // c00-op00-c01
    ac0 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, op00,
        c02, new Participation(ParticipationType.SOME), c01);
    o0.addOntoAxiom(ac0);
    // c00-op01-c02
    ac1 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, op01,
        c00, new Participation(ParticipationType.SOME), c02);
    o0.addOntoAxiom(ac1);
    // ============= Data
    // c00 dp00 1 String
    ad0 = new OntoAxiomDataAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, dp00,
        c00, new Participation(ParticipationType.EXACT, 1),
        BuiltInOwlDatatypeSet.OwlDatatype.XSD_STRING.getType());
    o0.addOntoAxiom(ad0);
    return o0;
  }

  /**
   *
   * @return une ontologie avec C0,C1,C2,C3 C2 isA C0, C3 isA C1 , C3 isA C0
   */

  public Ontology build03() {
    oIri = new OntoIri("ontorela/test/ontology");
    o0 = new Ontology(oIri);
    // Top class
    thingiri = new OntoIri("ontorela/test/Thing");
    // ================Class
    // C00
    c00Iri = new OntoIri("ontorela/test/C00");
    c00 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c00Iri);
    o0.addOntoClass(c00);
    // C000
    c000Iri = new OntoIri("ontorela/test/C000");
    c000 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c000Iri);
    o0.addOntoClass(c000);
    // C01
    c01Iri = new OntoIri("ontorela/test/C01");
    c01 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c01Iri);
    o0.addOntoClass(c01);
    // C001
    c001Iri = new OntoIri("ontorela/test/C001");
    c001 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c001Iri);
    o0.addOntoClass(c001);
    // C02
    c02Iri = new OntoIri("ontorela/test/C02");
    c02 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c02Iri);
    o0.addOntoClass(c02);
    // C03
    c03Iri = new OntoIri("ontorela/test/C03");
    c03 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c03Iri);
    o0.addOntoClass(c03);
    // C02 isA C00
    isa0 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c02, c00);
    o0.addOntoAxiom(isa0);
    // C03 isA C00
    isa1 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c03, c00);
    o0.addOntoAxiom(isa1);
    // C03 isA C01
    isa2 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c03, c01);
    o0.addOntoAxiom(isa2);
    // C000 isA C00
    isa3 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c000, c00);
    o0.addOntoAxiom(isa3);
    // C001 isA C01
    isa4 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c001, c01);
    o0.addOntoAxiom(isa4);

    // =============== Data properties
    dp00Iri = new OntoIri("OntoRela/ut/dp00");
    dp00 = new OntoDataProperty(dp00Iri, false);
    o0.addOntoDataProperty(dp00);

    // ============= Data Axiom
    // c00 dp00 1 String
    ad0 = new OntoAxiomDataAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, dp00,
        c00, new Participation(ParticipationType.EXACT, 1),
        BuiltInOwlDatatypeSet.OwlDatatype.XSD_STRING.getType());
    o0.addOntoAxiom(ad0);

    // ================ Properties
    // OP00 : domain c00 range c01
    op00Iri = new OntoIri("OntoRela/ut/op00");
    op00 = new OntoObjectProperty(op00Iri, false, false, c00, c01);
    o0.addOntoObjectProperty(op00);
    // OP000 : domain c000 range c001
    op000Iri = new OntoIri("OntoRela/ut/op000");
    op000 = new OntoObjectProperty(op000Iri, false, false, c000, c001);
    o0.addOntoObjectProperty(op000);
    // OP01 : domain S.O range:S.O
    op01Iri = new OntoIri("OntoRela/ut/op01");
    op01 = new OntoObjectProperty(op01Iri, false, false);
    o0.addOntoObjectProperty(op01);
    // OP001 : domain S.O range:S.O
    op001Iri = new OntoIri("OntoRela/ut/op001");
    op001 = new OntoObjectProperty(op001Iri, false, false);
    o0.addOntoObjectProperty(op001);

    // OP02: domaine C02 range S.O
    op02Iri = new OntoIri("OntoRela/ut/op02");
    OntoEntityCollectionI<OntoClassI> ontoClassopo2 = new OntoEntitySet<>();
    ontoClassopo2.add(c02);
    op02 = new OntoObjectProperty(op02Iri, false, false, ontoClassopo2);
    o0.addOntoObjectProperty(op02);
    // OP03 : domaine:S.O range C03
    op03Iri = new OntoIri("OntoRela/ut/op03");
    OntoEntityCollectionI<OntoClassI> ontoClassop31 = new OntoEntitySet<>();
    OntoEntityCollectionI<OntoClassI> ontoClassop3 = new OntoEntitySet<>();
    ontoClassop3.add(c03);
    op03 = new OntoObjectProperty(op03Iri, false, false, ontoClassop31, ontoClassop3);
    o0.addOntoObjectProperty(op03);
    // OP000 isA OP00
    op000.addSuperObjectProperty(op00);
    // op001 isA OP01
    op001.addSuperObjectProperty(op01);

    // Axiom
    // C00-op00-C01
    ac0 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, op00,
        c00, new Participation(ParticipationType.SOME), c01);
    o0.addOntoAxiom(ac0);

    // C000-op000-C001
    ac00 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION,
        op000, c000, new Participation(ParticipationType.SOME), c001);
    o0.addOntoAxiom(ac00);

    // C03-op0-C01
    acMerged = new OntoAxiomClassAssociation(OntoAxiomOrigin.MERGED_AXIOM,
        OntoAxiomType.ASSOCIATION, op00, c03, new Participation(ParticipationType.SOME), c01);
    o0.addOntoAxiom(acMerged);
    return o0;
  }

  public Ontology build04() {
    oIri = new OntoIri("ontorela/test/ontology");
    o0 = new Ontology(oIri);
    // ================Class
    // C00
    c00Iri = new OntoIri("ontorela/test/C00");
    c00 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c00Iri);
    o0.addOntoClass(c00);
    // C000
    c000Iri = new OntoIri("ontorela/test/C000");
    c000 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c000Iri);
    o0.addOntoClass(c000);
    // C01
    c01Iri = new OntoIri("ontorela/test/C01");
    c01 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c01Iri);
    o0.addOntoClass(c01);
    // C001
    c001Iri = new OntoIri("ontorela/test/C001");
    c001 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c001Iri);
    o0.addOntoClass(c001);
    // C02
    c02Iri = new OntoIri("ontorela/test/C02");
    c02 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c02Iri);
    o0.addOntoClass(c02);
    // C03
    c03Iri = new OntoIri("ontorela/test/C03");
    c03 = new OntoClass(OntoClassI.OntoClassOrigin.DECLARED, c03Iri);
    o0.addOntoClass(c03);
    // C02 isA C00
    isa0 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c02, c00);
    o0.addOntoAxiom(isa0);
    // C03 isA C00
    isa1 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c03, c00);
    o0.addOntoAxiom(isa1);
    // C03 isA C01
    isa2 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c03, c01);
    o0.addOntoAxiom(isa2);
    // C000 isA C00
    isa3 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c000, c00);
    o0.addOntoAxiom(isa3);
    // C001 isA C01
    isa4 = new OntoAxiomClassInheritance(OntoAxiomOrigin.DECLARED, c001, c01);
    o0.addOntoAxiom(isa4);

    // =============== Data properties
    dp00Iri = new OntoIri("OntoRela/ut/dp00");
    dp00 = new OntoDataProperty(dp00Iri, false);
    o0.addOntoDataProperty(dp00);

    // ============= Data Axiom
    // c00 dp00 1 String
    ad0 = new OntoAxiomDataAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, dp00,
        c00, new Participation(ParticipationType.EXACT, 1),
        BuiltInOwlDatatypeSet.OwlDatatype.XSD_STRING.getType());
    o0.addOntoAxiom(ad0);

    // ================ Properties
    // OP00 : domain c00 range c01
    op00Iri = new OntoIri("OntoRela/ut/op00");
    op00 = new OntoObjectProperty(op00Iri, false, false, c00, c01);
    o0.addOntoObjectProperty(op00);
    // OP000 : domain c000 range c001
    op000Iri = new OntoIri("OntoRela/ut/op000");
    op000 = new OntoObjectProperty(op000Iri, false, false, c000, c001);
    o0.addOntoObjectProperty(op000);
    // OP01 : domain S.O range:S.O
    op01Iri = new OntoIri("OntoRela/ut/op01");
    op01 = new OntoObjectProperty(op01Iri, false, false);
    o0.addOntoObjectProperty(op01);
    // OP001 : domain S.O range:S.O
    op001Iri = new OntoIri("OntoRela/ut/op001");
    op001 = new OntoObjectProperty(op001Iri, false, false);
    o0.addOntoObjectProperty(op001);

    // OP02: domaine C02 range S.O
    op02Iri = new OntoIri("OntoRela/ut/op02");
    OntoEntityCollectionI<OntoClassI> ontoClassopo2 = new OntoEntitySet<>();
    ontoClassopo2.add(c02);
    op02 = new OntoObjectProperty(op02Iri, false, false, ontoClassopo2);
    o0.addOntoObjectProperty(op02);
    // OP03 : domaine:S.O range C03
    op03Iri = new OntoIri("OntoRela/ut/op03");
    OntoEntityCollectionI<OntoClassI> ontoClassop31 = new OntoEntitySet<>();
    OntoEntityCollectionI<OntoClassI> ontoClassop3 = new OntoEntitySet<>();
    ontoClassop3.add(c03);
    op03 = new OntoObjectProperty(op03Iri, false, false, ontoClassop31, ontoClassop3);
    o0.addOntoObjectProperty(op03);
    // OP000 isA OP00
    op000.addSuperObjectProperty(op00);
    // op001 isA OP01
    op001.addSuperObjectProperty(op01);

    // Axiom
    // C00-op00-C01
    ac0 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION, op00,
        c00, new Participation(ParticipationType.SOME), c01);
    o0.addOntoAxiom(ac0);

    // C000-op000-C001
    ac00 = new OntoAxiomClassAssociation(OntoAxiomOrigin.DECLARED, OntoAxiomType.ASSOCIATION,
        op000, c000, new Participation(ParticipationType.SOME), c001);
    o0.addOntoAxiom(ac00);

    // C03-op00-C01
    acMerged = new OntoAxiomClassAssociation(OntoAxiomOrigin.MERGED_AXIOM,
        OntoAxiomType.ASSOCIATION, op00, c03, new Participation(ParticipationType.SOME), c01);
    o0.addOntoAxiom(acMerged);
    return o0;
  }
}
