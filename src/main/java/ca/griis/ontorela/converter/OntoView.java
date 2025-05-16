package ca.griis.ontorela.converter;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoAxiomDataAssociationI;
import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoDatatypeI;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.Table;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Vue ontologique. Une vue ontologique est une requête relationnelle qui peut être utilisée pour
 * définir une vue relationnelle à partir d'un ou de plusieurs composants ontologiques. <br>
 * Cette version d'OntoRela inclut
 * OntoClassView : une vue ontologique de classe est une vue pour une classe et ses axiomes
 * d'associations au premier niveau. Il existe deux types de vue :
 * vue propre (proper view - _pView)
 * formée des axiomes avec participation [1..1] sont utilisés;
 * vue de l'ensemble complet (complet subset view - _csView)
 * formée des axiomes avec [1..1] et [1..*];
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : non.</li>
 * <li>Clonabilité : oui | non (pourquoi).</li>
 * <li>Modifiabilité : oui | non (pourquoi).</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * TODO 2019-11-15 CK : redéfinir l'utilisation de l'ensemble des clés référentielles ?? <br>
 * OPT 2019-11-18 CK : définir des vues pour détecter les données absentes ?? <br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-11-13 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2019-11-13
 */
public class OntoView {
  // **************************************************************************
  // Attributs spécifiques
  //
  private final String viewId;
  private final OntoViewType ontoViewType;
  private final OntoClassI classSource;
  private final Table tableSource;
  private final Set<ForeignKey> axiomForeignKeySet;
  private final Map<OntoAxiomAssociationI, Table> axiomTableMap;
  private final Map<OntoDatatypeI, Table> typeTableMap;

  // **************************************************************************
  // Constructeurs
  //
  public enum OntoViewType {
    PROPERVIEW("_p"), COMPLETESUBSETVIEW("_c");

    private final String viewSuffix;

    OntoViewType(String viewSuffix) {
      this.viewSuffix = viewSuffix;
    }

    private String getViewSuffix() {
      return this.viewSuffix;
    }
  }

  /**
   * Constructeur d'une vue ontologique pour une classe à partir des axiomes d'associations propres
   * et les clés référentielles entrants.
   *
   * @param ontoViewType : le type de la vue ontologique
   * @param classSource : une classe ontologique
   * @param tableSource : la table correspondante à la classe ontologique
   * @param axiomTableMap : l'ensemble de paires axiome d'association propres-table
   * @param axiomForeignKeySet : l'ensemble des des clés référentielles entrants
   */
  public OntoView(OntoViewType ontoViewType, OntoClassI classSource, Table tableSource,
      Map<OntoAxiomAssociationI, Table> axiomTableMap, Map<OntoDatatypeI, Table> typeTableMap,
      Set<ForeignKey> axiomForeignKeySet) {
    super();
    this.ontoViewType = ontoViewType;
    this.viewId = classSource.getIri().getShortIri() + ontoViewType.getViewSuffix();
    this.classSource = classSource;
    this.tableSource = tableSource;
    this.axiomTableMap = axiomTableMap;
    this.typeTableMap = typeTableMap;
    this.axiomForeignKeySet = axiomForeignKeySet;
  }

  /**
   * Constructeur d'une vue ontologique pour une classe à partir des axiomes d'associations propres.
   *
   * @param ontoViewType : le type de la vue ontologique
   * @param classSource : une classe ontologique
   * @param tableSource : la table correspondante à la classe ontologique
   * @param axiomTableMap : l'ensemble de paires axiome d'association propres-table
   */
  protected OntoView(OntoViewType ontoViewType, OntoClassI classSource, Table tableSource,
      Map<OntoAxiomAssociationI, Table> axiomTableMap, Map<OntoDatatypeI, Table> typeTableMap) {
    this(ontoViewType, classSource, tableSource, axiomTableMap, typeTableMap,
        new LinkedHashSet<>());
  }

  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //

  /**
   * Construire une vue ontologique d'une classe à partir d'une paire d'axiomes
   * d'associations-tables.
   * Les paires axiome-table sont filtrées pour récépérer uniquement les axiomes d'associations
   * propres à la classe sources. Pour une vue propre la selection est définie en joignant les
   * tables
   * des axiomes ayant une participation [1..1] seulement. Pour un vue est de l'ensemble complet la
   * selection est définie en joignant les tables des axiomes ayant une participation [1..1] et
   * [1..n].
   *
   * @param ontoViewType : le type de la vue ontologique
   * @param classSource : une classe ontologique
   * @param tableSource : la table correspondante à la classe ontologique
   * @param axiomTableMap : l'ensemble de paires axiome d'association-table
   * @return une vue ontologique de la classe spécifiée.
   */
  public static OntoView buildOntoView(OntoViewType ontoViewType, OntoClassI classSource,
      Table tableSource, Map<OntoAxiomAssociationI, Table> axiomTableMap) {
    // ==== Vérifier si la classe possède des axiomes
    Map<OntoAxiomAssociationI, Table> axiomTableMapFiltered = axiomTableMap.entrySet().stream()
        .filter(a -> a.getKey().getOntoDeterminant().equals(classSource))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    if (axiomTableMapFiltered.size() == 0) {
      return null;
    }
    //
    Map<OntoAxiomAssociationI, Table> axiomTableExactOne = new LinkedHashMap<>();
    Map<OntoAxiomAssociationI, Table> axiomTableMin1 = new LinkedHashMap<>();
    //
    classifyAxioms(axiomTableMapFiltered, axiomTableExactOne, axiomTableMin1);
    //
    // ==== Si PROPERVIEW construire un vue avec les axiomes [1..1]
    if (ontoViewType.equals(OntoViewType.PROPERVIEW)) {
      if (!axiomTableExactOne.isEmpty()) {
        return new OntoView(ontoViewType, classSource, tableSource, axiomTableExactOne,
            new LinkedHashMap<>());
      } else {
        return null;
      }
      // ==== Si COMPLETESUBSETVIEW construire un vue avec les axiomes [1..1] et [1..*]
    } else if (ontoViewType.equals(OntoViewType.COMPLETESUBSETVIEW)) {
      if (!axiomTableMin1.isEmpty()) {
        return new OntoView(ontoViewType, classSource, tableSource, axiomTableMin1,
            new LinkedHashMap<>());
      } else {
        return null;
      }
    }
    return null;
  }

  public static OntoView buildOntoView(OntoViewType ontoViewType, OntoClassI classSource,
      OntoRelCat ontoRelDic) {
    // ==== Vérifier si la classe possède des axiomes
    Map<OntoAxiomAssociationI, Table> axiomTableMapFiltered = ontoRelDic.getAxiomTableCatalog()
        .entrySet().stream().filter(a -> a.getKey().getOntoDeterminant().equals(classSource))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    if (axiomTableMapFiltered.size() == 0) {
      return null;
    }
    //
    Map<OntoAxiomAssociationI, Table> axiomTableExactOne = new LinkedHashMap<>();
    Map<OntoAxiomAssociationI, Table> axiomTableMin1 = new LinkedHashMap<>();
    Map<OntoDatatypeI, Table> typeTable = new LinkedHashMap<>();
    //
    classifyAxioms(axiomTableMapFiltered, axiomTableExactOne, axiomTableMin1, typeTable,
        ontoRelDic);
    Table tableSource = ontoRelDic.getClassTableCatalog().get(classSource);
    // ==== Si PROPERVIEW construire un vue avec les axiomes [1..1]
    if (ontoViewType.equals(OntoViewType.PROPERVIEW)) {
      if (!axiomTableExactOne.isEmpty()) {
        return new OntoView(ontoViewType, classSource, tableSource, axiomTableExactOne, typeTable);
      } else {
        return null;
      }
      // ==== Si COMPLETESUBSETVIEW construire un vue avec les axiomes [1..1] et [1..*]
    } else if (ontoViewType.equals(OntoViewType.COMPLETESUBSETVIEW)) {
      if (!axiomTableMin1.isEmpty()) {
        return new OntoView(ontoViewType, classSource, tableSource, axiomTableMin1, typeTable);
      } else {
        return null;
      }
    }
    return null;
  }

  private static void classifyAxioms(Map<OntoAxiomAssociationI, Table> axiomTableMapFiltered,
      Map<OntoAxiomAssociationI, Table> axiomTableExactOne,
      Map<OntoAxiomAssociationI, Table> axiomTableMin1) {
    for (Entry<OntoAxiomAssociationI, Table> e : axiomTableMapFiltered.entrySet()) {
      // Filtre [1..1]
      if (e.getKey().getParticipation().getMin() == 1
          && e.getKey().getParticipation().getMax() == 1) {
        axiomTableExactOne.put(e.getKey(), e.getValue());
      }
      // Filtre [1..*]
      if (e.getKey().getParticipation().getMin() >= 1) {
        axiomTableMin1.put(e.getKey(), e.getValue());
      }
    }
  }

  private static void classifyAxioms(Map<OntoAxiomAssociationI, Table> axiomTableMapFiltered,
      Map<OntoAxiomAssociationI, Table> axiomTableExactOne,
      Map<OntoAxiomAssociationI, Table> axiomTableMin1, Map<OntoDatatypeI, Table> typeTable,
      OntoRelCat ontoRelDic) {
    for (Entry<OntoAxiomAssociationI, Table> e : axiomTableMapFiltered.entrySet()) {
      // Filtre [1..1]
      if (e.getKey().getParticipation().getMin() == 1
          && e.getKey().getParticipation().getMax() == 1) {
        axiomTableExactOne.put(e.getKey(), e.getValue());
        //
        if (e.getKey() instanceof OntoAxiomDataAssociationI
            /* && normalized */ && !ontoRelDic.getDataTableCatalog().isEmpty()) {
          typeTable.put(((OntoAxiomDataAssociationI) e.getKey()).getOntoDependent(),
              ontoRelDic.getDataTableCatalog()
                  .get(((OntoAxiomDataAssociationI) e.getKey()).getOntoDependent()));
        }
      }
      // Filtre [1..*]
      if (e.getKey().getParticipation().getMin() >= 1) {
        axiomTableMin1.put(e.getKey(), e.getValue());
      }
    }
  }

  /**
   * Get the definition for a specific language.
   *
   * @param language : the language of the definition
   * @return The definition for the specified language, if not exists or is empty return null.
   */
  public String getDescription(String language) {
    StringBuilder l = new StringBuilder(this.tableSource.getDescription(language));
    for (Table t : getAxiomTableSet()) {
      l.append(" " + t.getDescription(language));
    }
    return l == null || l.isEmpty() ? null : l.toString();
  }

  /**
   * @return le type de la vue ontologique.
   */
  public OntoViewType getOntoViewType() {
    return ontoViewType;
  }

  /**
   * @return l'identifiant de la vue. L'identifiant est l'IRI court de la classe source.
   */
  public String getViewId() {
    return this.viewId;
  }

  /**
   * @return la classe source.
   */
  public OntoClassI getClassSource() {
    return this.classSource;
  }

  /**
   * @return la table source correspondante à la classe source.
   */
  public Table getTableSource() {
    return this.tableSource;
  }

  /**
   * @return l'ensemble de paires axiome d'association-table propres à la classes sources.
   */
  public Map<OntoAxiomAssociationI, Table> getAxiomTableMap() {
    return this.axiomTableMap;
  }

  /**
   * @return typeTableMap
   */
  public Map<OntoDatatypeI, Table> getTypeTableMap() {
    return this.typeTableMap;
  }

  /**
   * @return l'ensemble des tables utilisées pour construire la vue.
   */
  public Set<Table> getAxiomTableSet() {
    Set<Table> allTables = new LinkedHashSet<>();
    allTables.addAll(this.axiomTableMap.values());
    allTables.addAll(this.typeTableMap.values());
    return allTables;
  }

  /**
   * @return l'ensemble des clés référentielles entrants de la table source.
   */
  public Set<ForeignKey> getAxiomForeignKeySet() {
    return this.axiomForeignKeySet;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "OntoView [classSource=" + this.classSource + ", tableSource=" + this.tableSource + "\n"
        + ", joinTables=" + getAxiomTableSet() + "]";
  }
}
