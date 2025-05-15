package ca.griis.ontorela.generator;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoDatatypeI;
import ca.griis.monto.api.ParticipationI;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoView;
import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.CandidateKey;
import ca.griis.ontorela.mrel.Database;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.InclusionConstraint;
import ca.griis.ontorela.mrel.MembershipConstraint;
import ca.griis.ontorela.mrel.ParticipationConstraint;
import ca.griis.ontorela.mrel.Schema;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.Type;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.traverse.TopologicalOrderIterator;

/**
 * générateur sql statements
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

public abstract class AbstactSqlGenerator extends Generator {
  private final List<String> languages;
  private final String commentLanguage;
  protected SqlTemplate sqlTemplate;
  protected final String currentDate;
  private final boolean useIriAsTableId;

  /**
   * @param database
   */
  public AbstactSqlGenerator(Database database, DatabaseConfiguration dbConfig,
      SqlTemplate sqlTemplate) {
    super(database);
    this.useIriAsTableId = dbConfig.getuseIriAsTableId();
    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmm");
    Date date = new Date();
    this.currentDate = dateFormat.format(date);

    this.languages = dbConfig.getLanguages();
    this.commentLanguage = this.languages.get(0);

    this.sqlTemplate = sqlTemplate;
  }

  /**
   * Generate create domain statements.
   *
   * @return Ddl of create domaines for all the domains as a string.
   */
  protected String generateCreateDomainStatements() {
    StringBuilder s = new StringBuilder();
    for (Type e : this.getDatabase().getBaseSchema().getTypeSet()) {
      s.append(sqlTemplate.genererDomaine(this.getBaseSchemaId(), e.getId(), e.getDomain()));
    }
    return s.toString();
  }

  /**
   * Generate drop statement for all domains.
   *
   * @return Ddl of drop for all the domains as a string.
   */
  protected String generateDropDomainStatements() {
    StringBuilder s = new StringBuilder();
    for (Type e : this.getDatabase().getBaseSchema().getTypeSet()) {
      s.append(sqlTemplate.genererSuppressionDomaine(this.getBaseSchemaId(), e.getId()));
    }
    return s.toString();
  }

  /**
   * Generate the DDL for the create table including the foreign key contraintes.
   * The tables IDs are internal identifier. For each table an definition is
   * generated the first langauge declared on the parameter file.
   *
   * @return DDL for the create table including the foreign key contraintes.
   */
  protected String generateCreateTableStatements() {
    // Generate the DDL
    StringBuilder tablesDdl = new StringBuilder();
    StringBuilder uniqueDdl = new StringBuilder();
    //
    for (Table t : this.getDatabase().getBaseSchema().getTableSet()) {
      Set<String> attPkSet = new LinkedHashSet<>();
      t.getPrimaryKeyAttributeSet().stream()
          .forEach(a -> attPkSet.add(useIriAsTableId ? a.getAttIri() : a.getAttId()));
      tablesDdl.append(sqlTemplate.genererRelvar(
          this.getBaseSchemaId(),
          useIriAsTableId ? t.getIri() : t.getIdentifier().getValue(),
          t.getAttributeTypePaires(useIriAsTableId),
          "key_" + (useIriAsTableId ? t.getIri() : t.getIdentifier().getValue()), attPkSet));
      // Generate the table comment
      tablesDdl.append(generateTableComments(t));
      // Generate unique keys
      for (CandidateKey uk : t.getKeyAttributeSet().stream().filter(k -> !k.isPrimary())
          .collect(Collectors.toSet())) {
        uniqueDdl.append(
            sqlTemplate.genererContrainteUnique(
                this.getBaseSchemaId(),
                useIriAsTableId ? t.getIri() : t.getIdentifier().getValue(),
                uk.getId() + "_" + (useIriAsTableId ? t.getIri() : t.getIdentifier().getValue()),
                useIriAsTableId ? uk.getKeyIri() : uk.getKeyAttIds()));
      }

    }
    tablesDdl.append(uniqueDdl);
    tablesDdl.append(generateCreateFkStatements());
    return tablesDdl.toString();
  }

  private String generateTableComments(Table t) {
    StringBuilder comments = new StringBuilder();
    for (Attribute a : t.getAttributeSet()) {
      comments.append(
          sqlTemplate.genererAttributDefinition(
              this.getBaseSchemaId(),
              useIriAsTableId ? t.getIri() : t.getIdentifier().getValue(),
              useIriAsTableId ? a.getAttIri() : a.getAttId(),
              a.getLabel(this.commentLanguage) + "::" + a.getDefinition(this.commentLanguage)));
    }
    return comments.toString();
  }

  /**
   * Generate the DDL for foreign key contraintes.
   *
   * @return DDL for the create foreign key contraintes.
   */
  protected String generateCreateFkStatements() {
    StringBuilder fkDdl = new StringBuilder();
    String lang = this.languages.get(0);
    for (ForeignKey f : this.getDatabase().getBaseSchema().getForeignKeySet()) {
      fkDdl.append(sqlTemplate.genererContrainteReferentielle(this.getBaseSchemaId(), f.getFkId(),
          useIriAsTableId ? f.getOrigin().getIri() : f.getOrigin().getIdentifier().getValue(),
          f.getAttOrigineId(useIriAsTableId), this.getBaseSchemaId(),
          useIriAsTableId ? f.getDestination().getIri()
              : f.getDestination().getIdentifier().getValue(),
          f.getAttDestinationId(useIriAsTableId)));
      // Generate FK comment
      fkDdl.append(sqlTemplate.genererContRefDefinition(this.getBaseSchemaId(),
          useIriAsTableId ? f.getOrigin().getIri() : f.getOrigin().getIdentifier().getValue(),
          f.getFkId(), f.getOrigin().getLabel(lang) + " -> " + f.getDestination().getLabel(lang)));
    }
    return fkDdl.toString();
  }

  /**
   * Generate drop table statements for all tables of the base schema.
   *
   * @return DDL of drop for all the tables as a string.
   */
  protected String generateDropTableStatements() {
    StringBuilder s = new StringBuilder();
    for (Table t : this.getDatabase().getBaseSchema().getTableSet()) {
      s.append(
          sqlTemplate.genererSuppressionRelVar(this.getBaseSchemaId(),
              useIriAsTableId ? t.getIri() : t.getIdentifier().getValue()));
    }
    return s.toString();
  }

  /**
   * Generate drop schema statements for all schema of the DB.
   *
   * @return DDL of drop for all schemas as a string.
   */
  protected String generateDropSchemaStatements() {
    StringBuilder s = new StringBuilder();
    for (Schema c : this.getDatabase().getSchemaSet()) {
      s.append(sqlTemplate.genererSuppressionSchema(c.getName(), true));
    }
    return s.toString();
  }

  /**
   * Generate of Delete table statements for all tables of the base schema.
   *
   * @return DDL of delete for all the tables as a string.
   */
  protected String generateDeleteTableStatements() {
    TopologicalOrderIterator<Table, ForeignKey> i =
        new TopologicalOrderIterator<>(this.getDatabase().getDbGraph());
    StringBuilder s = new StringBuilder();
    while (i.hasNext()) {
      s.append(sqlTemplate.genererSuppressionDonnees(this.getBaseSchemaId(),
          useIriAsTableId ? i.next().getIri() : i.next().getIdentifier().getValue()));
    }
    return s.toString();
  }

  // **************************************************************************
  // Opérations propres : VIEW
  //

  /**
   * TODO 2018-09-13 CK : Utiliser les l'ensemble de view du schéma ? Generation
   * of iri views are views over the base tables.
   *
   * @param schemaView : view schema name.
   * @return DDL of create views as a string.
   */
  protected String generateIriViews(String schemaView) {
    StringBuilder s = new StringBuilder();
    for (Table t : this.getDatabase().getBaseSchema().getTableSet()) {
      // <AttID, Alias>
      Map<String, String> ensAtt = new LinkedHashMap<>();
      for (Attribute a : t.getAttributeSet()) {
        ensAtt.put(useIriAsTableId ? a.getAttIri() : a.getAttId(), a.getAttIri());
      }
      s.append(
          sqlTemplate.genererVueDeRenommage(schemaView, t.getIri(), t.getSchemaId(),
              useIriAsTableId ? t.getIri() : t.getIdentifier().getValue(), ensAtt));
      // Generate comment for the first language
      if (t.getDescription(this.commentLanguage) != null) {
        s.append(sqlTemplate.genererVueDefinition(schemaView, t.getIri(),
            t.getLabel(this.commentLanguage) + "::" + t.getDescription(this.commentLanguage)));
      }
    }
    return s.toString();

  }

  /**
   * Generation of label views are views over the base tables with the specified
   * language.
   *
   * @param schemaView : schema name where the views must be created.
   * @param maxIdLength : maximum identifier length
   * @param language : label and comment language.
   * @return DDL of create views as a string.
   */
  protected String generateLabelViews(String schemaView, int maxIdLength, String language) {
    StringBuilder s = new StringBuilder();
    for (Table t : this.getDatabase().getBaseSchema().getTableSet()) {
      Map<String, String> ensAtt = buildAttribute(t, language, maxIdLength);

      String viewId = useIriAsTableId ? t.getLabel(language) : t.getPefixedLabel(language);
      if (viewId.length() > maxIdLength) {
        viewId = useIriAsTableId ? t.getIri() : t.getPrefixedIri();
      }
      //
      s.append(sqlTemplate.genererVueDeRenommage(schemaView, viewId, t.getSchemaId(),
          useIriAsTableId ? t.getIri() : t.getIdentifier().getValue(), ensAtt));
      // Generate comment for the specified language
      if (t.getDescription(language) != null) {
        s.append(sqlTemplate.genererVueDefinition(schemaView, viewId, t.getDescription(language)));
      }
    }
    return s.toString();
  }

  private Map<String, String> buildAttribute(Table t, String language, int maxIdLength) {
    Map<String, String> ensAtt = new LinkedHashMap<>();
    for (Attribute a : t.getAttributeSet()) {
      String label = a.getLabel(language);
      if (label.length() > maxIdLength) {
        label = useIriAsTableId ? a.getAttIri() : a.getAttId();
      }
      ensAtt.put(useIriAsTableId ? a.getAttIri() : a.getAttId(), label);
    }
    return ensAtt;
  }

  /**
   * Générer les définitions de vues ontologiques.
   *
   * @param schemaId : l'identifiant du schéma dans lequel les vues doivent être
   *        créées
   * @return l'ensemble des instructions CREATE VIEW.
   */
  protected String generateOntoViews(String schemaId) {
    StringBuilder s = new StringBuilder();
    for (OntoView v : this.getDatabase().getBaseSchema().getOntoViewSet()) {
      // === Contruire l'ensemble de paire identifiant table et description axiome
      Map<String, String> axiomTableDef = createAxiomtableDef(v);
      Map<String, String> typeTableDef = createTypetableDef(v);
      // === Générer le code de la view
      s.append(sqlTemplate.generateOntoView(schemaId, v.getViewId(),
          v.getClassSource().getIri().getShortIri(),
          v.getTableSource().getSchemaId(),
          useIriAsTableId ? v.getTableSource().getIri()
              : v.getTableSource().getIdentifier().getValue(),
          useIriAsTableId
              ? v.getTableSource().getPrimaryKeyAttributeSet().stream().findFirst().get()
                  .getAttIri()
              : v.getTableSource().getPrimaryKeyAttributeSet().stream().findFirst().get()
                  .getAttId(),
          axiomTableDef,
          typeTableDef));
      // Générer un commentaire avec la langue principale.
      if (v.getDescription(this.commentLanguage) != null) {
        s.append(sqlTemplate.genererVueDefinition(schemaId, v.getViewId(),
            v.getTableSource().getLabel(this.commentLanguage) + "::"
                + v.getDescription(this.commentLanguage)));
      }
    }
    return s.toString();
  }

  private Map<String, String> createTypetableDef(OntoView v) {
    // === Contruire l'ensemble de paire identifiant table du type et identifiant de
    // l'attribut clé
    Map<String, String> typeTableDef = new LinkedHashMap<>();
    for (Map.Entry<OntoDatatypeI, Table> link : v.getTypeTableMap().entrySet()) {
      typeTableDef.put(
          useIriAsTableId ? link.getValue().getIri() : link.getValue().getIdentifier().getValue(),
          useIriAsTableId
              ? link.getValue().getPrimaryKeyAttributeSet().stream().findFirst().get().getAttIri()
              : link.getValue().getPrimaryKeyAttributeSet().stream().findFirst().get()
                  .getAttId());
    }
    return typeTableDef;
  }

  private Map<String, String> createAxiomtableDef(OntoView v) {
    Map<String, String> axiomTableDef = new LinkedHashMap<>();
    for (Map.Entry<OntoAxiomAssociationI, Table> link : v.getAxiomTableMap().entrySet()) {
      axiomTableDef.put(
          useIriAsTableId ? link.getValue().getIri() : link.getValue().getIdentifier().getValue(),
          link.getKey().getOntoAxiomString());
    }
    return axiomTableDef;
  }

  // **************************************************************************
  // Opérations propres : CONSTRAINT
  //

  /**
   * Generate check Participation functions. The functions to check Participation
   * on property tables. When the participation is [1..1] no function is created.
   * When the participation is [0..n] only maximum participation function is
   * created. When the participation is [n.. ∞] only the minimum participation
   * function is created Otherwise the both minimum and maximum participation
   * function are created.
   *
   * @param constraint : a participation constraint.
   * @return Create function statement.
   */
  protected String generateCheckParticipation(ParticipationConstraint constraint) {
    StringBuilder s = new StringBuilder();
    //
    if (constraint.getSearchAttributeSet().stream().findFirst().isPresent()) {
      String domainKeyId = useIriAsTableId
          ? constraint.getSearchAttributeSet().stream().findFirst().get().getAttIri()
          : constraint.getSearchAttributeSet().stream().findFirst().get().getAttId();
      String domainKeyType =
          constraint.getSearchAttributeSet().stream().findFirst().get().getAttType();
      String tableId = useIriAsTableId ? constraint.getTable().getIri()
          : constraint.getTable().getIdentifier().getValue();

      //
      ParticipationI p = constraint.getParticipation();
      if (p.getMin() >= 1) {
        if (p.getMax() != Integer.MAX_VALUE) {
          s.append(
              sqlTemplate.genererVerificationParticipationMin(constraint.getName(),
                  this.getBaseSchemaId(),
                  tableId, domainKeyId, domainKeyType,
                  p.getMin()));
          s.append(
              sqlTemplate.genererVerificationParticipationMax(constraint.getName(),
                  this.getBaseSchemaId(),
                  tableId, domainKeyId, domainKeyType,
                  p.getMax()));
        } else if (p.getMax() == Integer.MAX_VALUE) {
          s.append(
              sqlTemplate.genererVerificationParticipationMin(constraint.getName(),
                  this.getBaseSchemaId(),
                  tableId, domainKeyId, domainKeyType,
                  p.getMin()));
        }
      } else if (p.getMax() != Integer.MAX_VALUE) {
        s.append(
            sqlTemplate.genererVerificationParticipationMax(constraint.getName(),
                this.getBaseSchemaId(),
                tableId, domainKeyId, domainKeyType,
                p.getMax()));
      }
    }

    return s.toString();
  }

  /**
   * Generate check union axiom function.
   *
   * @param constraint : a union axiom constraint.
   * @return Create function statement.
   */
  protected String generateCheckUnionAxiom(InclusionConstraint constraint) {
    StringBuilder s = new StringBuilder();
    Map<String, String> unionElementSet = new HashMap<>();
    for (Table e : constraint.getElementSet()) {
      unionElementSet.put(useIriAsTableId ? e.getIri() : e.getIdentifier().getValue(),
          useIriAsTableId ? e.getPrimaryKeyAttributeSet().stream().findFirst().get().getAttIri()
              : e.getPrimaryKeyAttributeSet().stream().findFirst().get().getAttId());
    }
    s.append(sqlTemplate.generateCheckUnionAxiom(constraint.getName(), this.getBaseSchemaId(),
        constraint.getUnionTable().getIdentifier().getValue(), unionElementSet));
    return s.toString();
  }

  /**
   * Generate check membership function.
   *
   * @param constraint : a membership constraint.
   * @return Create function statement.
   */
  protected String generateCheckMembership(MembershipConstraint constraint) {
    StringBuilder s = new StringBuilder();
    Map<String, String> targetTableMap = new HashMap<>();
    for (Table e : constraint.getTargetSet()) {
      targetTableMap.put(useIriAsTableId ? e.getIri() : e.getIdentifier().getValue(),
          useIriAsTableId ? e.getPrimaryKeyAttributeSet().stream().findFirst().get().getAttIri()
              : e.getPrimaryKeyAttributeSet().stream().findFirst().get().getAttId());
    }
    s.append(sqlTemplate.generateCheckMembership(constraint.getName(), this.getBaseSchemaId(),
        useIriAsTableId ? constraint.getSource().getIri()
            : constraint.getSource().getIdentifier().getValue(),
        constraint.getSourceAtt().getAttId(),
        targetTableMap));
    return s.toString();
  }

  public abstract File generateCreateTableDdL(String outDirPath, String version, String author);

  public abstract File generateDropTableDdL(String outDirPath, String version, String author);

  public abstract File generateDropSchemaDdl(String outDirPath, String version, String author);

  public abstract File generateDeleteDdL(String outDirPath, String version, String author);

  public abstract File generateParticipatioFctDdl(String outDirPath, String version, String author);

  public abstract File generateCheckUnionAxiomFctDdl(String outDirPath, String version,
      String author);

  public abstract File generateCheckMembershipFctDdl(String outDirPath, String version,
      String author);

  public abstract File generateCreateIriViewDdL(String outDirPath, String version, String author);

  public abstract File generateCreateLabelViewDdL(String outDirPath, String version, String author,
      int maxIdLength, String ln);

  public abstract File generateCreateOntoViewDdL(String outDirPath, String version, String author);
}
