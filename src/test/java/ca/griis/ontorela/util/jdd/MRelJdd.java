package ca.griis.ontorela.util.jdd;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.ParticipationI.ParticipationType;
import ca.griis.monto.model.BuiltInOwlDatatypeSet;
import ca.griis.monto.model.Participation;
import ca.griis.ontorela.mrel.*;
import ca.griis.ontorela.mrel.ForeignKey.ForeignKeyType;
import ca.griis.ontorela.mrel.Schema.SchemaType;
import ca.griis.ontorela.mrel.Table.TableOrigin;
import java.util.*;

/**
 * Des jeux de données pour MRel.
 * Il s'agit d'une instanciation des composants de MRel pour effectuer des tests unitaires.
 *
 * <b>Tâches projetées</b><br>
 * TODO 2019-11-20 CK : création OntoIRI ou null ?<br>
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
public class MRelJdd {
  // **************************************************************************
  // Attributs spécifiques
  //
  // === jdd0 ===
  private Database db0;
  private Schema s00;
  private Attribute thingAtt;
  private Attribute a000;
  private Attribute a001;
  private Attribute a002;
  private Attribute a003;
  private Attribute adk0;
  private Attribute adv0;
  private Attribute adk1;
  private Attribute adv1;
  private Table thingTable;
  private Table t000;
  private Table t001;
  private Table t002;
  private Table t003;
  // private Table tOp0;
  // private Table tOp1;
  // private Table tOp2;
  // private Table tOp3;
  private Table ta00;
  private Table ta01;
  private Table ta02;
  private Table ta03;
  private Table ta04;
  private Table td00;
  private Table td01;
  // private ForeignKey fk0_c0;
  private MOntoJdd mOntoJdd;
  private Participation qt;

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
  public Table getT001() {
    return t001;
  }

  public Table getT002() {
    return t002;
  }

  public Table getT003() {
    return t003;
  }

  public Table getTa00() {
    return ta00;
  }

  public Table getTa01() {
    return ta01;
  }

  public Table getTa02() {
    return ta02;
  }

  public Table getTa03() {
    return ta03;
  }

  public Table getTa04() {
    return ta04;
  }

  public Table getTd00() {
    return td00;
  }

  public Table getTd01() {
    return td01;
  }

  public Database buildDB0() {
    // Initialiser MOnto pour récupérer les IRI
    mOntoJdd = new MOntoJdd();
    mOntoJdd.buildO0();
    // ================
    a000 = new Attribute("A0", ":A0", "String");
    a001 = new Attribute("A1", ":A1", "String");
    a002 = new Attribute("A2", ":A2", "String");
    a003 = new Attribute("A3", ":A3", "String");
    // ================
    s00 = new Schema("s00", SchemaType.BASE);
    // ================
    t000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC00Iri(), "s00", ":C000");
    t000.addAttribute(a000);
    t000.addKey(a000, true);
    s00.addTable(t000);
    // ====
    t001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC01Iri(), "s00", ":C001");
    t001.addAttribute(a001);
    t001.addKey(a001, true);
    s00.addTable(t001);
    // ====
    t002 = new Table(TableOrigin.CLASSAXIOM, mOntoJdd.getAc0(), "s00", "C000_op00_C001");
    t002.addAttribute(a002);
    t002.addAttribute(a003);
    Set<Attribute> attKeySet = new LinkedHashSet<>();
    attKeySet.add(a002);
    attKeySet.add(a003);
    t002.addKey(attKeySet, true);
    s00.addTable(t002);
    // ================
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, t002, t000, a002);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, t002, t001, a003);
    // ====
    db0 = new Database("DB0", s00);
    db0.addSchema(s00);
    return db0;
  }

  public Database buildDB1() {
    // Initialiser MOnto pour récupérer les IRI
    MOntoJdd mOntoJdd = new MOntoJdd();
    mOntoJdd.buildO1();
    // ================
    a000 = new Attribute("T00_uid", ":T00_uid", "UUID");
    a001 = new Attribute("T01_uid", ":T01_uid", "UUID");
    a002 = new Attribute("T02_uid", ":T02_uid", "UUID");
    a003 = new Attribute("T03_uid", ":T03_uid", "UUID");
    adk0 = new Attribute("D00_uid", ":D00_uid", "INT");
    adv0 = new Attribute("value", ":value", "STRING");
    adk1 = new Attribute("D01_uid", ":D01_uid", "INT");
    adv1 = new Attribute("value", ":value", "INT");
    // ================
    s00 = new Schema("s00", SchemaType.BASE);
    // ================ Type - Table
    td00 = new Table(TableOrigin.ONTOTYPE,
        BuiltInOwlDatatypeSet.OwlDatatype.XSD_STRING.getType().getIri(), "s00", ":string");
    td00.addKey(adk0, true);
    td00.addAttribute(adv0);
    s00.addTable(td00);
    //
    td01 = new Table(TableOrigin.ONTOTYPE, mOntoJdd.getD00Iri(), "s00", ":D00");
    td01.addKey(adk1, true);
    td01.addAttribute(adv1);
    s00.addTable(td01);
    // ================ Classe - Table
    t000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC00Iri(), "s00", ":T00");
    t000.addKey(a000, true);
    s00.addTable(t000);
    // ====
    t001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC01Iri(), "s00", ":T01");
    t001.addKey(a001, true);
    s00.addTable(t001);
    // ====
    t002 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC02Iri(), "s00", ":T02");
    t002.addKey(a002, true);
    s00.addTable(t002);
    // ====
    t003 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC03Iri(), "s00", ":T03");
    t003.addKey(a003, true);
    s00.addTable(t002);
    // ================ Axiome - Table
    ta00 =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc0(), "s00", "C00_op00_C01", t000, t001,
            new Participation(ParticipationType.MIN, 2));
    Set<Attribute> attKeySet00 = new LinkedHashSet<>();
    attKeySet00.add(a000);
    attKeySet00.add(a001);
    ta00.addKey(attKeySet00, true);
    s00.addTable(ta00);
    // ==
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t000, a000);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t001, a001);
    // ====
    ta01 =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc1(), "s00", "C00_op01_C02", t000, t002,
            new Participation(ParticipationType.SOME));
    Set<Attribute> attKeySet01 = new LinkedHashSet<>();
    attKeySet01.add(a000);
    attKeySet01.add(a002);
    ta01.addKey(attKeySet01, true);
    s00.addTable(ta01);
    // ==
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t000, a000);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t002, a002);
    // ====
    ta02 = new Table(TableOrigin.DATAAXIOM, mOntoJdd.getAd0(), "s00", "C00_dp00_String");

    Set<Attribute> attKeySet02 = new LinkedHashSet<>();
    attKeySet02.add(a000);
    attKeySet02.add(adk0);
    ta02.addKey(attKeySet02, true);
    s00.addTable(ta02);
    // ==
    s00.addFkForDefaultKey(ForeignKeyType.DATAPROPERTY, ta02, t000, a000);
    s00.addFkForDefaultKey(ForeignKeyType.DATAPROPERTY, ta02, t001, adk0);
    // ====
    ta04 = new Table(TableOrigin.DATAAXIOM, mOntoJdd.getAd1(), "s00", "C00_dp00_Int");
    Set<Attribute> attKeySet04 = new LinkedHashSet<>();
    attKeySet04.add(a001);
    attKeySet04.add(adk1);
    ta04.addKey(attKeySet04, true);
    s00.addTable(ta04);
    // ==
    s00.addFkForDefaultKey(ForeignKeyType.DATAPROPERTY, ta02, t000, a000);
    s00.addFkForDefaultKey(ForeignKeyType.DATAPROPERTY, ta02, t001, adk0);
    // ====
    ta03 =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc2(), "s00", "C02_op01_C03", t002, t003,
            new Participation(ParticipationType.SOME));
    Set<Attribute> attKeySet03 = new LinkedHashSet<>();
    attKeySet01.add(a002);
    attKeySet01.add(a003);
    ta03.addKey(attKeySet03, true);
    s00.addTable(ta03);
    // ==
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta03, t002, a002);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta03, t003, a003);

    // ================ BD
    db0 = new Database("DB0", s00);
    db0.addSchema(s00);
    return db0;
  }

  /**
   *
   * @return une Database de l'ontologie résultante de build03 de MontoJdd
   *         Jdd pour les test du converterWithThing table, ontoClass,objectProperty, axioms, Fk
   *         iSa, Fk op ,
   *         la config:
   *         generateOpTable=True
   *         removeThingTable=false
   */
  public Database buildDB3() {
    // Initialiser MOnto pour récupérer les IRI
    mOntoJdd = new MOntoJdd();
    mOntoJdd.build03();
    // ================

    thingAtt = new Attribute("Thing_uid", "Thing_uid", "uid_domain");
    a000 = new Attribute("C00_uid", "C00_uid", "uid_domain");
    a001 = new Attribute("C01_uid", "C01_uid", "uid_domain");
    a002 = new Attribute("C02_uid", "C02_uid", "uid_domain");
    a003 = new Attribute("C03_uid", "C03_uid", "uid_domain");
    // ================
    s00 = new Schema("s00", SchemaType.BASE);
    // =================
    thingTable = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getThingiri(), "s00", "Thing");
    thingTable.addAttribute(thingAtt);
    thingTable.addKey(thingAtt, true);
    s00.addTable(thingTable);
    // ================= Class
    t000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC00Iri(), "s00", "C00");
    t000.addAttribute(a000);
    t000.addKey(a000, true);
    s00.addTable(t000);
    // ====
    t001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC01Iri(), "s00", "C01");
    t001.addAttribute(a001);
    t001.addKey(a001, true);
    s00.addTable(t001);
    // ====
    t002 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC02Iri(), "s00", "C02");
    t002.addAttribute(a002);
    t002.addKey(a002, true);
    s00.addTable(t002);
    // ====
    t003 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC03Iri(), "s00", "C03");
    t003.addAttribute(a003);
    t003.addKey(a003, true);
    s00.addTable(t003);
    // ====
    Table t0000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC000Iri(), "s00", "C000");
    Attribute a0000 = new Attribute("C000_uid", "C000_uid", "uid_domain");
    t0000.addAttribute(a0000);
    t0000.addKey(a0000, true);
    s00.addTable(t0000);
    // ====
    Table t0001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC001Iri(), "s00", "C001");
    Attribute a0001 = new Attribute("C001_uid", "C001_uid", "uid_domain");
    t0001.addAttribute(a0001);
    t0001.addKey(a0001, true);
    s00.addTable(t0001);
    // Fk vers table mere
    // ==== t000 isA Thing
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t000, thingTable,
        t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t000 isA Thing
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t000, thingTable,
        t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t001 isA Thing
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t001, thingTable,
        t001.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t003 isA t001
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t003, t001,
        t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t003 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t003, t000,
        t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t002 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t002, t000,
        t002.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t0000 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t0000, t000,
        t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t0001 isA t001
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t0001, t001,
        t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // =================Object property
    // participation pour les op
    qt = new Participation(ParticipationType.ONLY);

    // === table op00
    Map<Attribute, Attribute> joinAtt0 = new LinkedHashMap<>();
    Attribute domainop0 = new Attribute("domain_C00_uid", "domain_C00_uid", "uid_domain");
    joinAtt0.put(domainop0, t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeop0 = new Attribute("range_C01_uid", "range_C01_uid", "uid_domain");
    joinAtt0.put(rangeop0, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Table tOp0 = new TableJoin(TableOrigin.OBJECTPROPERTY, mOntoJdd.getOp00Iri(), "s00", "op00",
        t000, t001, qt, joinAtt0);
    Set<Attribute> aop00 = new LinkedHashSet<>();
    aop00.add(domainop0);
    aop00.add(rangeop0);
    tOp0.addKey(aop00, true);
    s00.addTable(tOp0);
    // FK domain c00 range c01
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp0, t000, domainop0);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp0, t001, rangeop0);

    // ===table op000
    Map<Attribute, Attribute> joinAtt = new LinkedHashMap<>();
    Attribute domainop00 = new Attribute("domain_C000_uid", "domain_C000_uid", "uid_domain");
    joinAtt.put(domainop00, t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeop00 = new Attribute("range_C001_uid", "range_C001_uid", "uid_domain");
    joinAtt.put(rangeop00, t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Table tOp00 = new TableJoin(TableOrigin.OBJECTPROPERTY, mOntoJdd.getOp000Iri(), "s00", "op000",
        t0000, t0001, qt, joinAtt);
    Set<Attribute> aop000 = new LinkedHashSet<>();
    aop000.add(domainop00);
    aop000.add(rangeop00);
    tOp00.addKey(aop000, true);
    s00.addTable(tOp00);
    // FK op000 isA op00
    s00.addFk(ForeignKeyType.ISA, tOp00, tOp0, tOp00.getPrimaryKeyAttributeSet(),
        tOp0.getPrimaryKeyAttributeSet());

    // ===table op01
    Map<Attribute, Attribute> joinAttop01 = new LinkedHashMap<>();
    Attribute domainop01 = new Attribute("domain_Thing_uid", "domain_Thing_uid", "uid_domain");
    joinAttop01.put(domainop01, thingTable.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeop01 = new Attribute("range_Thing_uid", "range_Thing_uid", "uid_domain");
    joinAttop01.put(rangeop01, thingTable.getPrimaryKeyAttributeSet().stream().findFirst().get());

    Table tOp01 = new TableJoin(TableOrigin.OBJECTPROPERTY, mOntoJdd.getOp01Iri(), "s00", "op01",
        thingTable, thingTable, qt, joinAttop01);
    Set<Attribute> aop01 = new LinkedHashSet<>();
    aop01.add(domainop01);
    aop01.add(rangeop01);
    tOp01.addKey(aop01, true);
    s00.addTable(tOp01);
    // FK domain thing range thing
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp01, thingTable, domainop01);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp01, thingTable, rangeop01);


    // ===table op001
    Map<Attribute, Attribute> joinAttop001 = new LinkedHashMap<>();
    Attribute domainop001 = new Attribute("domain_Thing_uid", "domain_Thing_uid", "uid_domain");
    joinAttop001.put(domainop001,
        thingTable.getPrimaryKeyAttributeSet().stream().findFirst().get());

    Attribute rangeop001 = new Attribute("range_Thing_uid", "range_Thing_uid", "uid_domain");
    joinAttop001.put(rangeop001, thingTable.getPrimaryKeyAttributeSet().stream().findFirst().get());

    Table tOp001 = new TableJoin(TableOrigin.OBJECTPROPERTY, mOntoJdd.getOp001Iri(), "s00", "op001",
        thingTable, thingTable, qt, joinAttop001);
    Set<Attribute> aop001 = new LinkedHashSet<>();
    aop001.add(domainop001);
    aop001.add(rangeop001);
    tOp001.addKey(aop001, true);
    s00.addTable(tOp001);
    // FK op001 iSA op1
    s00.addFk(ForeignKeyType.ISA, tOp001, tOp01, tOp001.getPrimaryKeyAttributeSet(),
        tOp01.getPrimaryKeyAttributeSet());

    // ===table de op02
    Map<Attribute, Attribute> joinAttop02 = new LinkedHashMap<>();
    Attribute domainop02 = new Attribute("domain_C02_uid", "domain_C02_uid", "uid_domain");
    joinAttop02.put(domainop02, t002.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeop02 = new Attribute("range_Thing_uid", "range_Thing_uid", "uid_domain");
    joinAttop02.put(rangeop02, thingTable.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Table tOp02 = new TableJoin(TableOrigin.OBJECTPROPERTY, mOntoJdd.getOp02Iri(), "s00", "op02",
        t002, thingTable, qt, joinAttop02);
    Set<Attribute> aop02 = new LinkedHashSet<>();
    aop02.add(domainop02);
    aop02.add(rangeop02);
    tOp02.addKey(aop02, true);
    s00.addTable(tOp02);

    // FK domain C02 range thing
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp02, t002, domainop02);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp02, thingTable, rangeop02);

    // ===table OP3
    Map<Attribute, Attribute> joinAttop03 = new LinkedHashMap<>();
    Attribute domainop03 = new Attribute("domain_Thing_uid", "domain_Thing_uid", "uid_domain");
    joinAttop03.put(domainop03, thingTable.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeop03 = new Attribute("range_C03_uid", "range_C03_uid", "uid_domain");
    joinAttop03.put(rangeop03, t003.getPrimaryKeyAttributeSet().stream().findFirst().get());

    Table tOp03 = new TableJoin(TableOrigin.OBJECTPROPERTY, mOntoJdd.getOp03Iri(), "s00", "op03",
        thingTable, t003, qt, joinAttop03);
    Set<Attribute> aop03 = new LinkedHashSet<>();
    aop03.add(domainop03);
    aop03.add(rangeop03);
    tOp03.addKey(aop03, true);
    s00.addTable(tOp03);
    // FK OP3-> thing (domain thing)
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp03, thingTable, domainop03);
    // FK OP3-> C03 (range C03)
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp03, t003, rangeop03);

    // ================ Axiome - Table
    // === C00-Op00-C01
    Map<Attribute, Attribute> joinAttopa0 = new LinkedHashMap<>();
    Attribute domainopa0 = new Attribute("C00_uid", "C00_uid", "uid_domain");
    joinAttopa0.put(domainopa0, t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeopa0 = new Attribute("C01_uid", "C01_uid", "uid_domain");
    joinAttopa0.put(rangeopa0, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    ta00 =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc0(), "s00", "C00_op00_C01", t000, t001,
            new Participation(ParticipationType.SOME), joinAttopa0);
    Set<Attribute> attKeySet00 = new LinkedHashSet<>();
    attKeySet00.add(a000);
    attKeySet00.add(a001);
    ta00.addKey(attKeySet00, true);
    s00.addTable(ta00);
    // ==
    // C00_op00_C01 -> C00
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t000, a000);
    // C00_op00_C01 -> C01
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t001, a001);
    // C00_op00_C01 -> op00
    s00.addFk(ForeignKeyType.ISA, ta00, tOp0,
        ta00.getPrimaryKeyAttributeSet(), tOp0.getPrimaryKeyAttributeSet());

    // === C000-Op000-C001
    Map<Attribute, Attribute> joinAttopa1 = new LinkedHashMap<>();
    Attribute domainopa1 = new Attribute("C000_uid", "C000_uid", "uid_domain");
    joinAttopa1.put(domainopa1, t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeopa1 = new Attribute("C001_uid", "C001_uid", "uid_domain");
    joinAttopa1.put(rangeopa1, t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    ta01 = new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc00(),
        "s00", "C000_op000_C001", t0000, t0001, new Participation(ParticipationType.SOME),
        joinAttopa1);
    Set<Attribute> attKeySet01 = new LinkedHashSet<>();
    attKeySet01.add(a0000);
    attKeySet01.add(a0001);
    ta01.addKey(attKeySet01, true);
    s00.addTable(ta01);
    //
    // C000_op000_C001 -> C000
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t0000, a0000);
    // C000_op000_C001 -> C001
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t0001, a0001);
    // C000_op000_C001 -> op000
    s00.addFk(ForeignKeyType.ISA, ta01, tOp00,
        ta01.getPrimaryKeyAttributeSet(), tOp00.getPrimaryKeyAttributeSet());

    // ==C03_op00_C01
    Map<Attribute, Attribute> joinAttmr = new LinkedHashMap<>();
    Attribute domainOpMr = new Attribute("C03_uid", "C03_uid", "uid_domain");
    joinAttmr.put(domainOpMr, t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeOpMr = new Attribute("C01_uid", "C01_uid", "uid_domain");
    joinAttmr.put(rangeOpMr, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    Table tmerged =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAcMerged(), "s00", "C03_op00_C01",
            t003, t001, new Participation(ParticipationType.SOME), joinAttmr);
    Set<Attribute> attKeySetmr = new LinkedHashSet<>();
    attKeySetmr.add(a003);
    attKeySetmr.add(a001);
    tmerged.addKey(attKeySetmr, true);
    s00.addTable(tmerged);
    // C03_op00_C01 -> C03
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tmerged, t003, a003);
    // C03_op00_C01 -> C01
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tmerged, t001, a001);
    // C03_op00_C01 -> op00
    s00.addFk(ForeignKeyType.ISA, tmerged, tOp0,
        tmerged.getPrimaryKeyAttributeSet(), tOp0.getPrimaryKeyAttributeSet());

    db0 = new Database("DB0", s00);
    db0.addSchema(s00);

    return db0;
  }

  public Database build13(boolean normalize) {
    mOntoJdd = new MOntoJdd();
    mOntoJdd.build03();
    // ================
    a000 = new Attribute("C00_uid", "C00_uid", "uid_domain");
    // ================
    s00 = new Schema("s00", SchemaType.BASE);
    // ================
    t000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC00Iri(), "s00", "C00");
    t000.addAttribute(a000);
    t000.addKey(a000, true);
    s00.addTable(t000);
    // ================ Type - Table
    if (normalize) {
      adk0 = new Attribute("string_uid", "string_uid", "uid_domain");
      adv0 = new Attribute("string", "string", "string_domain");
      td00 = new Table(TableOrigin.ONTOTYPE,
          BuiltInOwlDatatypeSet.OwlDatatype.XSD_STRING.getType().getIri(), "s00", "string");


      td00.addKey(adk0, true);
      td00.addKey(adv0, false);
      // td00.addAttribute(adv0);
      s00.addTable(td00);
      // ================Axiome - DataAxiom
      Map<Attribute, Attribute> joinAttd0 = new LinkedHashMap<>();
      Attribute domaind0 = new Attribute("C00_uid", "C00_uid", "uid_domain");
      joinAttd0.put(domaind0, t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
      Attribute ranged0 = new Attribute("string_uid", "string_uid", "uid_domain");
      joinAttd0.put(ranged0, td00.getPrimaryKeyAttributeSet().stream().findFirst().get());

      ta02 = new TableJoin(TableOrigin.DATAAXIOM, (OntoAxiomAssociationI) mOntoJdd.getAd0(), "s00",
          "C00_dp00_string", t000, td00, (Participation) mOntoJdd.getAd0().getParticipation(),
          joinAttd0);
      Set<Attribute> attKeySet02 = new LinkedHashSet<>();
      attKeySet02.add(domaind0);
      attKeySet02.add(ranged0);
      ta02.addKey(attKeySet02, true);
      s00.addTable(ta02);
      // ==
      s00.addFkForDefaultKey(ForeignKeyType.DATAPROPERTY, ta02, t000, a000);
      s00.addFkForDefaultKey(ForeignKeyType.DATAPROPERTY, ta02, td00, adk0);
    } else {
      ta02 = new Table(TableOrigin.DATAAXIOM, (OntoAxiomAssociationI) mOntoJdd.getAd0(), "s00",
          "C00_dp00_string");
      Set<Attribute> attKeySet02 = new LinkedHashSet<>();
      attKeySet02.addAll(t000.getPrimaryKeyAttributeSet());
      adv0 = new Attribute("C00_dp00_string_dp00", "C00_dp00_string_dp00", "string_domain");
      ta02.addAttribute(adv0);
      ta02.addKey(attKeySet02, true);
      s00.addTable(ta02);
      s00.addFkForDefaultKey(ForeignKeyType.DATAPROPERTY, ta02, t000, a000);
    }
    db0 = new Database("DB0", s00);
    db0.addSchema(s00);
    return db0;
  }

  /**
   *
   * @return une Database de l'ontologie résultante de build03 de MontoJdd
   *         Jdd pour les test du converterWithThing table, ontoClass,objectProperty, axioms, Fk
   *         iSa, Fk op ,
   *         la config:
   *         generateOpTable=True
   *         removeThingTable=true
   */
  public Database buildDB4() {
    // Initialiser MOnto pour récupérer les IRI
    mOntoJdd = new MOntoJdd();
    mOntoJdd.build04();
    // ================

    a000 = new Attribute("C00_uid", "C00_uid", "uid_domain");
    a001 = new Attribute("C01_uid", "C01_uid", "uid_domain");
    a002 = new Attribute("C02_uid", "C02_uid", "uid_domain");
    a003 = new Attribute("C03_uid", "C03_uid", "uid_domain");
    // ================
    s00 = new Schema("s00", SchemaType.BASE);

    // ================= Class
    t000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC00Iri(), "s00", "C00");
    t000.addAttribute(a000);
    t000.addKey(a000, true);
    s00.addTable(t000);
    // ====
    t001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC01Iri(), "s00", "C01");
    t001.addAttribute(a001);
    t001.addKey(a001, true);
    s00.addTable(t001);
    // ====
    t002 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC02Iri(), "s00", "C02");
    t002.addAttribute(a002);
    t002.addKey(a002, true);
    s00.addTable(t002);
    // ====
    t003 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC03Iri(), "s00", "C03");
    t003.addAttribute(a003);
    t003.addKey(a003, true);
    s00.addTable(t003);
    // ====
    Table t0000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC000Iri(), "s00", "C000");
    Attribute a0000 = new Attribute("C000_uid", "C000_uid", "uid_domain");
    t0000.addAttribute(a0000);
    t0000.addKey(a0000, true);
    s00.addTable(t0000);
    // ====
    Table t0001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC001Iri(), "s00", "C001");
    Attribute a0001 = new Attribute("C001_uid", "C001_uid", "uid_domain");
    t0001.addAttribute(a0001);
    t0001.addKey(a0001, true);
    s00.addTable(t0001);
    // ==== t003 isA t001
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t003, t001,
        t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t003 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t003, t000,
        t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t002 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t002, t000,
        t002.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t0000 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t0000, t000,
        t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t0001 isA t001
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t0001, t001,
        t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // =================Object property
    // participation pour les op
    qt = new Participation(ParticipationType.ONLY);

    // === table op00
    Map<Attribute, Attribute> joinAtt0 = new LinkedHashMap<>();
    Attribute domainop0 = new Attribute("domain_C00_uid", "domain_C00_uid", "uid_domain");
    joinAtt0.put(domainop0, t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeop0 = new Attribute("range_C01_uid", "range_C01_uid", "uid_domain");
    joinAtt0.put(rangeop0, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Table tOp0 = new TableJoin(TableOrigin.OBJECTPROPERTY, mOntoJdd.getOp00Iri(), "s00", "op00",
        t000, t001, qt, joinAtt0);
    Set<Attribute> aop00 = new LinkedHashSet<>();
    aop00.add(domainop0);
    aop00.add(rangeop0);
    tOp0.addKey(aop00, true);
    s00.addTable(tOp0);
    // FK domain c00 range c01
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp0, t000, domainop0);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tOp0, t001, rangeop0);

    // ===table op000
    Map<Attribute, Attribute> joinAtt = new LinkedHashMap<>();
    Attribute domainop00 = new Attribute("domain_C000_uid", "domain_C000_uid", "uid_domain");
    joinAtt.put(domainop00, t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeop00 = new Attribute("range_C001_uid", "range_C001_uid", "uid_domain");
    joinAtt.put(rangeop00, t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Table tOp00 = new TableJoin(TableOrigin.OBJECTPROPERTY, mOntoJdd.getOp000Iri(), "s00", "op000",
        t0000, t0001, qt, joinAtt);
    Set<Attribute> aop000 = new LinkedHashSet<>();
    aop000.add(domainop00);
    aop000.add(rangeop00);
    tOp00.addKey(aop000, true);
    s00.addTable(tOp00);
    // FK op000 isA op00
    s00.addFk(ForeignKeyType.ISA, tOp00, tOp0, tOp00.getPrimaryKeyAttributeSet(),
        tOp0.getPrimaryKeyAttributeSet());


    // ================ Axiome - Table
    // === C00-Op00-C01
    Map<Attribute, Attribute> joinAttopa0 = new LinkedHashMap<>();
    Attribute domainopa0 = new Attribute("C00_uid", "C00_uid", "uid_domain");
    joinAttopa0.put(domainopa0, t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeopa0 = new Attribute("C01_uid", "C01_uid", "uid_domain");
    joinAttopa0.put(rangeopa0, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    ta00 =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc0(), "s00", "C00_op00_C01", t000, t001,
            new Participation(ParticipationType.SOME), joinAttopa0);
    Set<Attribute> attKeySet00 = new LinkedHashSet<>();
    attKeySet00.add(a000);
    attKeySet00.add(a001);
    ta00.addKey(attKeySet00, true);
    s00.addTable(ta00);
    // ==
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t000, a000);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t001, a001);
    s00.addFk(ForeignKeyType.ISA, ta00, tOp0,
        ta00.getPrimaryKeyAttributeSet(), tOp0.getPrimaryKeyAttributeSet());

    // === C000-Op000-C001
    Map<Attribute, Attribute> joinAttopa1 = new LinkedHashMap<>();
    Attribute domainopa1 = new Attribute("C000_uid", "C000_uid", "uid_domain");
    joinAttopa1.put(domainopa1, t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeopa1 = new Attribute("C001_uid", "C001_uid", "uid_domain");
    joinAttopa1.put(rangeopa1, t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    ta01 = new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc00(),
        "s00", "C000_op000_C001", t0000, t0001, new Participation(ParticipationType.SOME),
        joinAttopa1);
    Set<Attribute> attKeySet01 = new LinkedHashSet<>();
    attKeySet01.add(a0000);
    attKeySet01.add(a0001);
    ta01.addKey(attKeySet01, true);
    s00.addTable(ta01);
    //

    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t0000, a0000);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t0001, a0001);
    s00.addFk(ForeignKeyType.ISA, ta01, tOp00,
        ta01.getPrimaryKeyAttributeSet(), tOp00.getPrimaryKeyAttributeSet());

    // ==C03_op00_C01 doit etre crée par algo de reduction d'axiome (pour le moment est dans le
    // convertisseur)
    Map<Attribute, Attribute> joinAttmr = new LinkedHashMap<>();
    Attribute domainOpMr = new Attribute("C03_uid", "C03_uid", "uid_domain");
    joinAttmr.put(domainOpMr, t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeOpMr = new Attribute("C01_uid", "C01_uid", "uid_domain");
    joinAttmr.put(rangeOpMr, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    Table tmerged =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAcMerged(), "s00", "C03_op00_C01",
            t003, t001, new Participation(ParticipationType.SOME), joinAttmr);
    Set<Attribute> attKeySetmr = new LinkedHashSet<>();
    attKeySetmr.add(a003);
    attKeySetmr.add(a001);
    tmerged.addKey(attKeySetmr, true);
    s00.addTable(tmerged);

    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tmerged, t003, a003);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tmerged, t001, a001);
    s00.addFk(ForeignKeyType.ISA, tmerged, tOp0,
        tmerged.getPrimaryKeyAttributeSet(), tOp0.getPrimaryKeyAttributeSet());

    db0 = new Database("DB0", s00);
    db0.addSchema(s00);

    return db0;
  }

  /**
   *
   * @return une Database de l'ontologie résultante de build03 de MontoJdd
   *         Jdd pour les test du converterWithThing table, ontoClass,objectProperty, axioms, Fk
   *         iSa, Fk op ,
   *         la config:
   *         generateOpTable=flase
   *         removeThingTable=false
   */
  public Database buildDB5() {
    // Initialiser MOnto pour récupérer les IRI
    mOntoJdd = new MOntoJdd();
    mOntoJdd.build04();
    // ================

    a000 = new Attribute("C00_uid", "C00_uid", "uid_domain");
    a001 = new Attribute("C01_uid", "C01_uid", "uid_domain");
    a002 = new Attribute("C02_uid", "C02_uid", "uid_domain");
    a003 = new Attribute("C03_uid", "C03_uid", "uid_domain");
    // ================
    s00 = new Schema("s00", SchemaType.BASE);

    // ================= Class
    t000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC00Iri(), "s00", "C00");
    t000.addAttribute(a000);
    t000.addKey(a000, true);
    s00.addTable(t000);
    // ====
    t001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC01Iri(), "s00", "C01");
    t001.addAttribute(a001);
    t001.addKey(a001, true);
    s00.addTable(t001);
    // ====
    t002 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC02Iri(), "s00", "C02");
    t002.addAttribute(a002);
    t002.addKey(a002, true);
    s00.addTable(t002);
    // ====
    t003 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC03Iri(), "s00", "C03");
    t003.addAttribute(a003);
    t003.addKey(a003, true);
    s00.addTable(t003);
    // ====
    Table t0000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC000Iri(), "s00", "C000");
    Attribute a0000 = new Attribute("C000_uid", "C000_uid", "uid_domain");
    t0000.addAttribute(a0000);
    t0000.addKey(a0000, true);
    s00.addTable(t0000);
    // ====
    Table t0001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC001Iri(), "s00", "C001");
    Attribute a0001 = new Attribute("C001_uid", "C001_uid", "uid_domain");
    t0001.addAttribute(a0001);
    t0001.addKey(a0001, true);
    s00.addTable(t0001);
    // ==== t003 isA t001
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t003, t001,
        t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t003 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t003, t000,
        t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t002 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t002, t000,
        t002.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t0000 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t0000, t000,
        t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t0001 isA t001
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t0001, t001,
        t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    // ================ Axiome - Table
    // === C00-Op00-C01
    Map<Attribute, Attribute> joinAttopa0 = new LinkedHashMap<>();
    Attribute domainopa0 = new Attribute("C00_uid", "C00_uid", "uid_domain");
    joinAttopa0.put(domainopa0, t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeopa0 = new Attribute("C01_uid", "C01_uid", "uid_domain");
    joinAttopa0.put(rangeopa0, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    ta00 =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc0(), "s00", "C00_op00_C01", t000, t001,
            new Participation(ParticipationType.SOME), joinAttopa0);
    Set<Attribute> attKeySet00 = new LinkedHashSet<>();
    attKeySet00.add(a000);
    attKeySet00.add(a001);
    ta00.addKey(attKeySet00, true);
    s00.addTable(ta00);
    // ==
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t000, a000);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t001, a001);

    // === C000-Op000-C001
    Map<Attribute, Attribute> joinAttopa1 = new LinkedHashMap<>();
    Attribute domainopa1 = new Attribute("C000_uid", "C000_uid", "uid_domain");
    joinAttopa1.put(domainopa1, t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeopa1 = new Attribute("C001_uid", "C001_uid", "uid_domain");
    joinAttopa1.put(rangeopa1, t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    ta01 = new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc00(),
        "s00", "C000_op000_C001", t0000, t0001, new Participation(ParticipationType.SOME),
        joinAttopa1);
    Set<Attribute> attKeySet01 = new LinkedHashSet<>();
    attKeySet01.add(a0000);
    attKeySet01.add(a0001);
    ta01.addKey(attKeySet01, true);
    s00.addTable(ta01);
    //

    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t0000, a0000);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t0001, a0001);

    Map<Attribute, Attribute> joinAttmr = new LinkedHashMap<>();
    Attribute domainOpMr = new Attribute("C03_uid", "C03_uid", "uid_domain");
    joinAttmr.put(domainOpMr, t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeOpMr = new Attribute("C01_uid", "C01_uid", "uid_domain");
    joinAttmr.put(rangeOpMr, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    Table tmerged =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAcMerged(), "s00", "C03_op00_C01",
            t003, t001, new Participation(ParticipationType.SOME), joinAttmr);
    Set<Attribute> attKeySetmr = new LinkedHashSet<>();
    attKeySetmr.add(a003);
    attKeySetmr.add(a001);
    tmerged.addKey(attKeySetmr, true);
    s00.addTable(tmerged);

    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tmerged, t003, a003);
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tmerged, t001, a001);

    db0 = new Database("DB0", s00);
    db0.addSchema(s00);

    return db0;
  }

  /**
   *
   * @return une Database de l'ontologie résultante de build03 de MontoJdd
   *         Jdd pour les test du converterWithThing table, ontoClass,objectProperty, axioms, Fk
   *         iSa, Fk op ,
   *         la config:
   *         generateOpTable=false
   *         removeThingTable=true
   */
  public Database buildDB6() {
    // Initialiser MOnto pour récupérer les IRI
    mOntoJdd = new MOntoJdd();
    mOntoJdd.build03();
    // ================

    thingAtt = new Attribute("Thing_uid", "Thing_uid", "uid_domain");
    a000 = new Attribute("C00_uid", "C00_uid", "uid_domain");
    a001 = new Attribute("C01_uid", "C01_uid", "uid_domain");
    a002 = new Attribute("C02_uid", "C02_uid", "uid_domain");
    a003 = new Attribute("C03_uid", "C03_uid", "uid_domain");
    // ================
    s00 = new Schema("s00", SchemaType.BASE);
    // =================
    thingTable = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getThingiri(), "s00", "Thing");
    thingTable.addAttribute(thingAtt);
    thingTable.addKey(thingAtt, true);
    s00.addTable(thingTable);
    // ================= Class
    t000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC00Iri(), "s00", "C00");
    t000.addAttribute(a000);
    t000.addKey(a000, true);
    s00.addTable(t000);
    // ====
    t001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC01Iri(), "s00", "C01");
    t001.addAttribute(a001);
    t001.addKey(a001, true);
    s00.addTable(t001);
    // ====
    t002 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC02Iri(), "s00", "C02");
    t002.addAttribute(a002);
    t002.addKey(a002, true);
    s00.addTable(t002);
    // ====
    t003 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC03Iri(), "s00", "C03");
    t003.addAttribute(a003);
    t003.addKey(a003, true);
    s00.addTable(t003);
    // ====
    Table t0000 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC000Iri(), "s00", "C000");
    Attribute a0000 = new Attribute("C000_uid", "C000_uid", "uid_domain");
    t0000.addAttribute(a0000);
    t0000.addKey(a0000, true);
    s00.addTable(t0000);
    // ====
    Table t0001 = new Table(TableOrigin.ONTOCLASS, mOntoJdd.getC001Iri(), "s00", "C001");
    Attribute a0001 = new Attribute("C001_uid", "C001_uid", "uid_domain");
    t0001.addAttribute(a0001);
    t0001.addKey(a0001, true);
    s00.addTable(t0001);
    // Fk vers table mere
    // ==== t000 isA Thing
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t000, thingTable,
        t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t000 isA Thing
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t000, thingTable,
        t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t001 isA Thing
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t001, thingTable,
        t001.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t003 isA t001
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t003, t001,
        t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t003 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t003, t000,
        t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t002 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t002, t000,
        t002.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t0000 isA t000
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t0000, t000,
        t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    // ==== t0001 isA t001
    s00.addFkForDefaultKey(ForeignKeyType.ISA, t0001, t001,
        t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    // ================ Axiome - Table
    // === C00-Op00-C01
    Map<Attribute, Attribute> joinAttopa0 = new LinkedHashMap<>();
    Attribute domainopa0 = new Attribute("C00_uid", "C00_uid", "uid_domain");
    joinAttopa0.put(domainopa0, t000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeopa0 = new Attribute("C01_uid", "C01_uid", "uid_domain");
    joinAttopa0.put(rangeopa0, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    ta00 =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc0(), "s00", "C00_op00_C01", t000, t001,
            new Participation(ParticipationType.SOME), joinAttopa0);
    Set<Attribute> attKeySet00 = new LinkedHashSet<>();
    attKeySet00.add(a000);
    attKeySet00.add(a001);
    ta00.addKey(attKeySet00, true);
    s00.addTable(ta00);
    // ==
    // C00_op00_C01 -> C00
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t000, a000);
    // C00_op00_C01 -> C01
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta00, t001, a001);

    // === C000-Op000-C001
    Map<Attribute, Attribute> joinAttopa1 = new LinkedHashMap<>();
    Attribute domainopa1 = new Attribute("C000_uid", "C000_uid", "uid_domain");
    joinAttopa1.put(domainopa1, t0000.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeopa1 = new Attribute("C001_uid", "C001_uid", "uid_domain");
    joinAttopa1.put(rangeopa1, t0001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    ta01 = new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAc00(),
        "s00", "C000_op000_C001", t0000, t0001, new Participation(ParticipationType.SOME),
        joinAttopa1);
    Set<Attribute> attKeySet01 = new LinkedHashSet<>();
    attKeySet01.add(a0000);
    attKeySet01.add(a0001);
    ta01.addKey(attKeySet01, true);
    s00.addTable(ta01);
    //
    // C000_op000_C001 -> C000
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t0000, a0000);
    // C000_op000_C001 -> C001
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, ta01, t0001, a0001);

    // ==C03_op00_C01 doit etre crée par algo de reduction d'axiome (pour le moment est dans le
    // convertisseur)
    Map<Attribute, Attribute> joinAttmr = new LinkedHashMap<>();
    Attribute domainOpMr = new Attribute("C03_uid", "C03_uid", "uid_domain");
    joinAttmr.put(domainOpMr, t003.getPrimaryKeyAttributeSet().stream().findFirst().get());
    Attribute rangeOpMr = new Attribute("C01_uid", "C01_uid", "uid_domain");
    joinAttmr.put(rangeOpMr, t001.getPrimaryKeyAttributeSet().stream().findFirst().get());

    Table tmerged =
        new TableJoin(TableOrigin.CLASSAXIOM, mOntoJdd.getAcMerged(), "s00", "C03_op00_C01",
            t003, t001, new Participation(ParticipationType.SOME), joinAttmr);
    Set<Attribute> attKeySetmr = new LinkedHashSet<>();
    attKeySetmr.add(a003);
    attKeySetmr.add(a001);
    tmerged.addKey(attKeySetmr, true);
    s00.addTable(tmerged);
    // C03_op00_C01 -> C03
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tmerged, t003, a003);
    // C03_op00_C01 -> C01
    s00.addFkForDefaultKey(ForeignKeyType.OBJECTPROPERTY, tmerged, t001, a001);

    db0 = new Database("DB0", s00);
    db0.addSchema(s00);

    return db0;
  }
}
