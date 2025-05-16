package ca.griis.ontorela.generator;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * Facade de StringTemplate.
 * <p>
 * Propriétés des objets
 * </p>
 * <ul>
 * <li>Unicité : oui.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
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
 * <b>Tâches projetées</b><br>
 * TODO 2018-09-07 CK : singleton ? <br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2017-07-24 (0.1.0) [CK] Mise en oeuvre initiale. <br>
 *
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 * @version 0.1.0
 * @since 2017-07-24
 */
public class SqlTemplate {
  // *************************************************************************
  // Attributs spécifiques
  //
  protected STGroup template;

  // **************************************************************************
  // Constructeur
  //
  public SqlTemplate() {
    String path = "SSQL.stg";
    template = new STGroupFile(path);
  }

  public STGroup getTemplate() {
    return template;
  }
  // **************************************************************************
  // Opérations propres
  //

  public void setTemplate(STGroup template) {
    this.template = template;
  }

  /**
   * Définition d'un schéma selon la syntaxe de postgreSQL.
   *
   * @param schemaId : l'identifiant du schéma
   * @return la chaine de caractère de définition d'un schéma,
   */
  public String genererSchema(String schemaId) {
    ST schema = template.getInstanceOf("schema");
    schema.add("schema_id", schemaId);
    return schema.render();
  }
  // **************************************************************************
  // Opérations publiques - DÉFINITION DU SCHÉMA
  //

  /**
   * Définition d'un commentaire sur un schéma.
   *
   * @param schemaId : l'identifiant du schéma
   * @param def : la définition du schéma
   * @return la chaine de caractère de définition d'un commentaire de schéma.
   */
  public String genererSchemaDefinition(String schemaId, String def) {
    ST schema = template.getInstanceOf("schema_def");
    schema.add("schema_id", schemaId);
    // Ajouter une caractère d'echappement pour l'apostrophe
    def = def.replace("'", "''");
    schema.add("def", def);
    return schema.render();
  }

  /**
   * Suppresion d'un schéma.
   *
   * @param schemaId : L'identifiant du schéma à supprimer
   * @param enCascade : suppression en cascade ou pas.
   * @return la chaine de caractère de suppresion d'un schéma.
   */
  public String genererSuppressionSchema(String schemaId, boolean enCascade) {
    ST schema = template.getInstanceOf("sup_schema");
    schema.add("schema_id", schemaId);
    schema.add("enCascade", enCascade);
    return schema.render();
  }

  // **************************************************************************
  // Opérations publiques - DÉFINITION DES TYPES
  //
  // TODO 2017-08-08 [CK] : générer CREATE TYPE pour les types défini

  /**
   * Définition du gabarit d'un type prédéfini NON ANONYME.
   *
   * @param typeId l'identifiant du type prédéfini
   * @param domaineId l'identifiant du domaine
   * @param annulable Si annulable
   * @return la chaine de caractère de définition d'un type prédéfini NON ANONYME selon le langage
   *         cible.
   */
  public String genererTypePredefini(String typeId, String domaineId, boolean annulable) {
    ST type = template.getInstanceOf("type_predefini");
    type.add("type_id", typeId);
    type.add("domaine_id", domaineId);
    type.add("annulable", annulable);
    return type.render();
  }
  // **************************************************************************
  // Opérations publiques - DÉFINITION DES TYPES
  //
  // TODO 2017-08-08 [CK] : générer CREATE TYPE pour les types défini

  /**
   * Generate domain for rdbms data types.
   *
   * @param schemaId {@link String}
   * @param typeId {@link String} : OWl datatype
   * @param domainId {@link String} : Corresponding rdbms data type
   * @return {@link String}
   */
  public String genererDomaine(String schemaId, String typeId, String domainId) {
    ST domaineStr = template.getInstanceOf("domaine");
    domaineStr.add("schema_id", schemaId);
    domaineStr.add("type_id", typeId);
    domaineStr.add("domaine_id", domainId);
    return domaineStr.render();
  }

  /**
   * Définition d'une instruction de suppression d'un domaine.
   *
   * @param schemaId l'identifiant du schéma auquel appartient le domaine.
   * @param domaineId l'identifiant du domaine à supprimer
   * @return la chaine de caractère de suppression d'un domaine selon le langage.
   */
  public String genererSuppressionDomaine(String schemaId, String domaineId) {
    ST drp = template.getInstanceOf("suppression_domaine");
    drp.add("schema_id", schemaId);
    drp.add("domaine_id", domaineId);
    return drp.render();
  }

  /**
   * Définition d'un commentaire sur une relvar.
   *
   * @param schemaId : l'identifiant du schéma.
   * @param varId : l'identifiant de la relvar.
   * @param def : la définition de la relvar.
   * @return la chaine de caractère de définition d'un commentaire de la relvar.
   */
  public String genererRelvarDefinition(String schemaId, String varId, String def) {
    ST relvar = template.getInstanceOf("relvar_def");
    relvar.add("schema_id", schemaId);
    relvar.add("var_id", varId);
    def = def.replace("'", "''");
    relvar.add("def", def);
    return relvar.render();
  }
  // **************************************************************************
  // Opérations publiques - DÉFINITION D'UNE RELATION
  //

  /**
   * Définition d'un commentaire sur une vue.
   *
   * @param schemaId : l'identifiant du schéma
   * @param vueId : l'identifiant de la vue
   * @param def : la définition de la vue
   * @return la chaine de caractère de définition d'un commentaire de la vue.
   */
  public String genererVueDefinition(String schemaId, String vueId, String def) {
    ST relvar = template.getInstanceOf("vue_def");
    relvar.add("schema_id", schemaId);
    relvar.add("vue_id", vueId);
    // Ajouter une caractère d'echappement pour l'apostrophe
    def = def.replace("'", "''");
    relvar.add("def", def);
    return relvar.render();
  }

  /**
   * Définition d'un commentaire sur un attribut d'une relvar.
   *
   * @param schemaId : l'identifiant du schéma
   * @param varId : l'identifiant de la relvar
   * @param attId : l'identifiant de l'attribut
   * @param def : la définition de l'attribut
   * @return la chaine de caractère de définition d'un commentaire de l'attribut.
   */
  public String genererAttributDefinition(String schemaId, String varId, String attId, String def) {
    ST relvar = template.getInstanceOf("attribut_def");
    relvar.add("schema_id", schemaId);
    relvar.add("var_id", varId);
    relvar.add("att_id", attId);
    // Ajouter une caractère d'echappement pour l'apostrophe
    def = def.replace("'", "''");
    relvar.add("def", def);
    return relvar.render();
  }

  /**
   * Définition du gabarit d'une variable de relation.
   *
   * @param schemaId : l'identifiant du schéma auquel appartient la table
   * @param varId : l'identifiant de la table
   * @param ensAtt : les chaines de caractères des définitions des attributs (id, type)
   * @param contrainteId : l'identifiant de la contrainte clé
   * @param attCleP : les identifiants des attributs formant la clé primaire
   * @return la chaine de caractère de définition d'une variable de relation selon le langage cible.
   */
  public String genererRelvar(String schemaId, String varId, Map<String, String> ensAtt,
      String contrainteId, Set<String> attCleP) {
    ST relvar = template.getInstanceOf("relvar");
    relvar.add("schema_id", schemaId);
    relvar.add("var_id", varId);
    // Générer les attributs
    for (Map.Entry<String, String> e : ensAtt.entrySet()) {
      relvar.addAggr("ensAtt.{id, type}", e.getKey(), e.getValue());
    }
    genererContrainteClePrimaire(relvar, contrainteId, attCleP);
    return relvar.render();
  }

  /**
   * Définition d'une vue de renommage. La vue représente une variable de relation avec un autre
   * nom.
   *
   * @param vueSchema : le schéma dans lequel il faut créer la vue
   * @param vueId : l'identifiant de la vue
   * @param varSchema : le schéma de la table d'origine
   * @param varId : l'identifiant de la variable de relation
   * @param ensAtt : l'ensemble d'attributs
   * @return la chaine de caractère de définition de la vue.
   */
  public String genererVueDeRenommage(String vueSchema, String vueId, String varSchema,
      String varId, Map<String, String> ensAtt) {
    ST vue = template.getInstanceOf("vue_renommage");
    vue.add("vue_schema", vueSchema);
    vue.add("vue_id", vueId);
    vue.add("var_schema", varSchema);
    vue.add("var_id", varId);
    for (Entry<String, String> a : ensAtt.entrySet()) {
      vue.addAggr("ensAtt.{id, alias}", a.getKey(), a.getValue());
    }
    return vue.render();
  }

  /**
   * Définition d'une vue ontologique d'une classe. Le USING est utilisé pour conditionner la
   * jointure.
   *
   * @param viewSchemaId : l'identifiant du schéma dans lequel il faut créer la vue
   * @param viewId : l'identifiant de la vue
   * @param classIri : l'iri court de la classe source
   * @param sourceTableSchemaId : l'identifiant du schéma de la relvar source
   * @param sourceTableId : l'identifiant de la relvar source
   * @param sourceTableKeyId : l'identifiant de l'attribut clé de la relvar source
   * @param axiomTableDef : l'ensemble de paires identifiant table et decription d'un axiome
   *        (ex. getOntoAxiomString()) utilisées pour la contruction de la jointure.
   * @return la chaine de caractère de définition d'une vue ontologique d'une classe.
   */
  public String generateOntoView(String viewSchemaId, String viewId, String classIri,
      String sourceTableSchemaId, String sourceTableId, String sourceTableKeyId,
      Map<String, String> axiomTableDef, Map<String, String> typeTableDef) {
    ST ontoView = getTemplate().getInstanceOf("view_ontoClass");
    //
    ontoView.add("view_schema", viewSchemaId);
    ontoView.add("view_id", viewId);
    ontoView.add("class_iri", classIri);
    ontoView.add("var_schema", sourceTableSchemaId);
    ontoView.add("var_id", sourceTableId);
    for (Entry<String, String> axiomLink : axiomTableDef.entrySet()) {
      ontoView.addAggr("ensTable.{id, var_key, axiomString}",
          axiomLink.getKey(), sourceTableKeyId, axiomLink.getValue());
    }
    for (Entry<String, String> typeLink : typeTableDef.entrySet()) {
      ontoView.addAggr("ensTable.{id, var_key, axiomString}",
          typeLink.getKey(), typeLink.getValue(), typeLink.getKey());
    }
    return ontoView.render();
  }

  public String genererSuppressionRelVar(String schemaId, String varId) {
    ST drp = template.getInstanceOf("suppression_relvar");
    drp.add("schema_id", schemaId);
    drp.add("var_id", varId);
    drp.add("enCascade", true);
    return drp.render();
  }

  /**
   * Définition du gabarit d'une contrainte de clé primaire. La contrainte clé primaire est générée
   * à l'intérieur d'une définition de table. NOTE de mise en oeuvre : la construction de template
   * s'effectue à partir du template "mère". Pas besoin d'instancier le template en question pour
   * générer la syntaxe.
   *
   * @param relvar le template de la variable de relation en cours de traitement
   * @param contrainteId l'identifiant de la contrainte clé
   * @param attCle les identifiants des attributs formant la clé
   * @return la chaine de caractère de définition d'une clé primiaire selon le langage cible.
   */
  protected String genererContrainteClePrimaire(ST relvar, String contrainteId,
      Set<String> attCle) {
    relvar.add("contrainte_id", contrainteId);
    for (String a : attCle) {
      relvar.addAggr("attCle.{id}", a);
    }
    return relvar.render();
  }
  // **************************************************************************
  // Opérations publiques - DÉFINITION DE CONTRAINTES
  //

  /**
   * Définition du gabarit d'une contrainte de clé secondaire.
   *
   * @param schemaId l'identifiant du schéma auquel appartient la table
   * @param varId l'identifiant de la relvar
   * @param contrainteId l'identifiant de la contrainte clé
   * @param attCle les identifiants des attributs formant la clé
   * @return la chaine de caractère de définition d'une clé secondaire selon le langage cible.
   */
  public String genererContrainteCleSecondaire(String schemaId, String varId,
      String contrainteId, ArrayList<String> attCle) {
    ST cle = template.getInstanceOf("contrainte_cleSecondaire");
    cle.add("var_id", varId);
    cle.add("contrainte_id", contrainteId);
    for (String a : attCle) {
      cle.addAggr("attCle.{id}", a);
    }
    return cle.render();
  }

  /**
   * Définition SQL d'une contrainte référentielle.
   *
   * @param varOrigineSchema l'identifiant du schéma de la variable d'origine auquel appartient la
   *        table
   * @param contrainteId l'identifiant de la contrainte
   * @param varOrigineId l'identifiant de la relvar d'origine
   * @param ensAttOrigine les identifiants des attributs de la relvar d'origine formant la clé
   * @param varDestinationSchema l'identifiant du schéma de la variable de destination auquel
   *        appartient la table
   * @param varDestinationId l'identifiant de la relvar de destination
   * @param ensAttDestination les identifiants des attributs de la relvar destination formant la clé
   * @return la chaine de caractère de définition de contrainte référentielle selon le langage
   *         cible.
   */
  public String genererContrainteReferentielle(String varOrigineSchema, String contrainteId,
      String varOrigineId, Set<String> ensAttOrigine, String varDestinationSchema,
      String varDestinationId, Set<String> ensAttDestination) {
    ST ref = template.getInstanceOf("contrainte_referentielle");
    // TODO 2017-08-07 [CK] : ref.add("schema_id", schema_id);
    ref.add("contrainte_id", contrainteId);
    // Générer les attributs origine
    ref.add("varOrigine_schema", varOrigineSchema);
    ref.add("varOrigine_id", varOrigineId);
    ensAttOrigine.stream().forEach(a -> ref.addAggr("ensAttOrigine.{id}", a));
    // Générer les attributs destination
    ref.add("varDestination_schema", varDestinationSchema);
    ref.add("varDestination_id", varDestinationId);
    ensAttDestination.stream().forEach(a -> ref.addAggr("ensAttDestination.{id}", a));
    return ref.render();
  }

  /**
   * Définition SQL d'une contrainte référentielle avec un seul attribut. Version simplifiée.
   *
   * @param varOrigineSchema l'identifiant du schéma de la variable d'origine auquel appartient
   *        la table
   * @param contrainteId l'identifiant de la contrainte
   * @param varOrigineId l'identifiant de la relvar d'origine
   * @param attOrigine l'identifiant de l'attribut de la relvar d'origine formant la clé
   * @param varDestinationSchema l'identifiant du schéma de la variable de destination auquel
   *        appartient la table
   * @param varDestinationId l'identifiant de la relvar de destination
   * @param attDestination l'identifiant de l'attribut de la relvar destination formant la clé
   * @return la chaine de caractère de définition de contrainte référentielle selon le langage
   *         cible.
   */
  public String genererContrainteReferentielle(String varOrigineSchema, String contrainteId,
      String varOrigineId, String attOrigine, String varDestinationSchema, String varDestinationId,
      String attDestination) {
    ST ref = template.getInstanceOf("contrainte_referentielle");
    ref.add("varOrigine_schema", varOrigineSchema);
    ref.add("varOrigine_id", varOrigineId);
    ref.add("contrainte_id", contrainteId);
    // Générer les attributs origine
    ref.addAggr("ensAttOrigine.{id}", attOrigine);
    // Générer les attributs destination
    ref.add("varDestination_schema", varDestinationSchema);
    ref.add("varDestination_id", varDestinationId);
    ref.addAggr("ensAttDestination.{id}", attDestination);
    return ref.render();
  }

  /**
   * Définition d'un commentaire sur une contrainte de clé référentielle.
   *
   * @param schemaId : l'identifiant du schéma
   * @param varId : l'identifiant de la relvar
   * @param contrainteId : l'identifiant de la contrainte
   * @param def : la définition de la contrainte
   * @return la chaine de caractère de définition d'un commentaire de la clé référentielle
   */
  public String genererContRefDefinition(String schemaId, String varId,
      String contrainteId, String def) {
    ST cleRef = template.getInstanceOf("cleRef_def");
    cleRef.add("schema_id", schemaId);
    cleRef.add("var_id", varId);
    cleRef.add("contrainte_id", contrainteId);
    // Suffixer les apostrophes par un caractère d'echappement.
    def = def.replace("'", "''");
    cleRef.add("def", def);
    return cleRef.render();
  }

  /**
   * Creer la contrainte unique sur la table.
   *
   * @param schema : schema de la table.
   * @param relvar : table dans laquelle il faut creer la contrainte.
   * @param contrainteId : Identifiant de la containte.
   * @param ensAtt : Attribut sur lesquels appliquer la contarinte.
   * @return {@link String}
   */
  public String genererContrainteUnique(String schema, String relvar, String contrainteId,
      Set<String> ensAtt) {
    ST uniqueConstraint = template.getInstanceOf("contrainte_unique");
    uniqueConstraint.add("varOrigine_schema", schema);
    uniqueConstraint.add("varOrigine_table", relvar);
    uniqueConstraint.add("contrainte_id", contrainteId);
    for (String r : ensAtt) {
      uniqueConstraint.addAggr("ensAttOrigine.{id}", r);
    }
    return uniqueConstraint.render();
  }

  /**
   * Générer une fonction de vérification d'un participation minimale d'un attribut.
   *
   * @param constraintId : identifiant de la contrainte.
   * @param schemaId : identifiant du shéma.
   * @param tableId : identifiant de la table.
   * @param domainKeyId : identifiant de la clé primaire de la table de domaine (déterminante).
   * @param domainKeyType : type de la clé primaire de la table de domaine.
   * @param min : participation minimale
   * @return : chaine de caractère de définition d'une fonction de vérification d'une
   *         participation minimale.
   */
  public String genererVerificationParticipationMin(String constraintId, String schemaId,
      String tableId, String domainKeyId, String domainKeyType, int min) {
    ST fct = template.getInstanceOf("verificationParticipationMin");
    fct.add("table_id", tableId);
    fct.add("domaine_cle", new AttributeString(domainKeyId, domainKeyType));
    fct.add("contrainte_id", constraintId + "Min");
    fct.add("schema_id", schemaId);
    fct.add("min", min);
    return fct.render();
  }

  /**
   * Générer une fonction de vérification d'un participation maximale d'un attribut.
   *
   * @param constraintId : identifiant de la contrainte.
   * @param schemaId : identifiant du shéma.
   * @param tableId : identifiant de la table.
   * @param domainKeyId : identifiant de la clé primaire de la table de domaine (déterminante).
   * @param domainKeyType : type de la clé primaire de la table de domaine.
   * @param max : participation maximale
   * @return : chaine de caractère de définition d'une fonction de vérification d'une
   *         participation maximale.
   */
  public String genererVerificationParticipationMax(String constraintId, String schemaId,
      String tableId, String domainKeyId, String domainKeyType, int max) {
    ST fct = template.getInstanceOf("verificationParticipationMax");
    fct.add("table_id", tableId);
    fct.add("domaine_cle", new AttributeString(domainKeyId, domainKeyType));
    fct.add("contrainte_id", constraintId + "Max");
    fct.add("schema_id", schemaId);
    fct.add("max", max);
    return fct.render();
  }

  /**
   * Générer une fonction de vérification de la une contrainte de d'union.
   * La contrainte d'union vérifier que tous les tuples de la table d'union sont les tuples
   * provenant
   * de l'union des tuples des tables éléments de l'union.
   *
   * @param constraintId : identifiant de la contrainte.
   * @param schemaId : identifiant du shéma.
   * @param uniontableId : identifiant de la table d'union.
   * @param unionElementSet : les pairs de l'identifiant de table éléments de l'union et son
   *        attribut clé.
   * @return : chaine de caractère de la définition d'une fonction de vérification de l'union.
   */
  public String generateCheckUnionAxiom(String constraintId, String schemaId, String uniontableId,
      Map<String, String> unionElementSet) {
    ST checkUnionAxiom = template.getInstanceOf("checkUnionAxiom");
    checkUnionAxiom.add("contrainte_id", constraintId);
    checkUnionAxiom.add("schema_id", schemaId);
    checkUnionAxiom.add("unionTable_id", uniontableId);
    for (Map.Entry<String, String> e : unionElementSet.entrySet()) {
      ST selectKeys = template.getInstanceOf("selectKeys");
      selectKeys.add("schema_id", schemaId);
      selectKeys.add("table_id", e.getKey());
      selectKeys.addAggr("keySet.{id}", e.getValue());
      checkUnionAxiom.addAggr("elementSet.{exp}", selectKeys.render());
    }
    return checkUnionAxiom.render();
  }

  /**
   * Générer une fonction devérification d'une contrainte d'appartenance.
   *
   * @param constraintId : identifiant de la contrainte.
   * @param schemaId : identifiant du schéma.
   * @param sourceTableId : identifiant de la table à vérifier.
   * @param sourceAttId : identifiant de l'attribut de la table à vérifier.
   * @param targetTableMap : les paires (targetTableId, targetAttId) identifiant de la table et de
   *        son
   *        attribut utilisés pour la vérification.
   * @return : chaine de caractère de la définition de la fonction de vérification de
   *         l'appartenance.
   */
  public String generateCheckMembership(String constraintId, String schemaId, String sourceTableId,
      String sourceAttId, Map<String, String> targetTableMap) {
    ST checkMembership = template.getInstanceOf("checkMembership");
    checkMembership.add("contrainte_id", constraintId);
    checkMembership.add("schema_id", schemaId);
    checkMembership.add("sourceTable_id", sourceTableId);
    checkMembership.add("sourceAtt", sourceAttId);
    for (Map.Entry<String, String> e : targetTableMap.entrySet()) {
      checkMembership.addAggr("targetTableMap.{table_id, targetAtt}", e.getKey(),
          e.getValue());
    }
    return checkMembership.render();
  }

  /**
   * Générer une expression.
   *
   * @param attributs {@link ArrayList}
   * @param relvars {@link ArrayList}
   * @return Retourne l'expression.
   */
  public String genererExpression(ArrayList<String> attributs, ArrayList<String> relvars) {
    ST requete = template.getInstanceOf("requete");
    for (String a : attributs) {
      requete.addAggr("projection.{exp}", a);
    }
    for (String r : relvars) {
      requete.addAggr("selection.{exp}", r);
    }
    return requete.render();
  }
  // ***********************************************************************************************
  // Opérations publiques - MANIPULATION DU SCHÉMA
  //

  /**
   * Définition d'une instruction de suppression d'une variable de relation.
   *
   * @param schemaId l'identifiant du schéma auquel appartient la table
   * @param varId l'identifiant de la relvar à supprimer
   * @return la chaine de caractère de suppression d'une variable de relation selon le langage.
   */
  public String genererSuppressionDonnees(String schemaId, String varId) {
    ST d = template.getInstanceOf("suppression_donnees");
    d.add("schema_id", schemaId);
    d.add("var_id", varId);
    return d.render();
  }

  /**
   * Définition du gabarit de l'entête d'un script SQL.
   *
   * @param schemaId l'identifiant du schéma auquel appartient la table
   * @param dateCreation la date de création du script
   * @param responsable le responsable du script
   * @param version la version courante du script
   * @param objet l'objet du script
   * @return la chaine de caractère de l'entête.
   */
  public String genererEnteteFichierSql(String schemaId, String dateCreation, String responsable,
      String version, String objet) {
    ST enteteF = template.getInstanceOf("enteteFichierSQL");
    enteteF.add("schema_id", schemaId);
    enteteF.add("date_creation", dateCreation);
    enteteF.add("responsable", responsable);
    enteteF.add("version", version);
    enteteF.add("objet", objet);
    return enteteF.render();
  }
  // ***********************************************************************************************
  // Opérations publiques - Documentation script
  //

  /**
   * Définition du gabarit de l'entête d'un fichier de documentation.
   *
   * @param schemaId l'identifiant du schéma auquel appartient la table
   * @param dateCreation la date de création du script
   * @param responsable le responsable du script
   * @param version la version courante du script
   * @param objet l'objet du script
   * @return la chaine de caractère de l'entête.
   */
  public String genererEnteteFichierDoc(String schemaId, String dateCreation,
      String responsable, String version, String objet) {
    ST enteteF = template.getInstanceOf("enteteFichierDoc");
    enteteF.add("schema_id", schemaId);
    enteteF.add("date_creation", dateCreation);
    enteteF.add("responsable", responsable);
    enteteF.add("version", version);
    enteteF.add("objet", objet);
    return enteteF.render();
  }

  /**
   * Générer l'appel de la procédure : call ontorel_ins
   *
   * @param ontorelId {@link String}
   * @param version {@link String}
   * @param importDate {@link String}
   * @return {@link String}
   */
  public String generateCallOntorelIns(String ontorelId, String version, String importDate) {
    ST ontorelStr = template.getInstanceOf("ontorel_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("version", version);
    ontorelStr.add("import_date", importDate);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure: call onto_config_db_ins
   *
   * @param ontorelId {@link String}
   * @param defaultKeyNameOntorelcat {@link String}
   * @param defaultKeyDomainNameOntorelcat {@link String}
   * @param defaultKeyTypeOntorelcat {@link String}
   * @param defaultAttributeNameOntorelcat {@link String}
   * @param defaultAttributeDomaineNameOntorelcat {@link String}
   * @param defaultAttributeType {@link String}
   * @param maxIdentifierLengthOntorelcat {@link int}
   * @param useIriAsTableIdOntorelcat {@link boolean}
   * @param normalizeDatatypeOntorelcat {@link boolean}
   * @param generateOpTable {@link boolean}
   * @return {@link String}
   * 
   */
  public String generateCallConfigdbIns(String ontorelId, String defaultKeyNameOntorelcat,
      String defaultKeyDomainNameOntorelcat, String defaultKeyTypeOntorelcat,
      String defaultAttributeNameOntorelcat, String defaultAttributeDomaineNameOntorelcat,
      String defaultAttributeType, int maxIdentifierLengthOntorelcat,
      boolean useIriAsTableIdOntorelcat, boolean normalizeDatatypeOntorelcat,
      boolean removeThingTable, boolean generateOpTable) {
    ST ontorelStr = template.getInstanceOf("onto_config_db_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("DefaultKeyNameOntorelcat", defaultKeyNameOntorelcat);
    ontorelStr.add("DefaultKeyDomainNameOntorelcat", defaultKeyDomainNameOntorelcat);
    ontorelStr.add("DefaultKeyTypeOntorelcat", defaultKeyTypeOntorelcat);
    ontorelStr.add("DefaultAttributeNameOntorelcat", defaultAttributeNameOntorelcat);
    ontorelStr.add("DefaultAttributeDomaineNameOntorelcat", defaultAttributeDomaineNameOntorelcat);
    ontorelStr.add("DefaultAttributeType", defaultAttributeType);
    ontorelStr.add("MaxIdentifierLengthOntorelcat", maxIdentifierLengthOntorelcat);
    ontorelStr.add("UseIriAsTableIdOntorelcat", useIriAsTableIdOntorelcat);
    ontorelStr.add("NormalizeDatatypeOntorelcat", normalizeDatatypeOntorelcat);
    ontorelStr.add("RemoveThingTable", removeThingTable);
    ontorelStr.add("GenerateOpTable", generateOpTable);
    return ontorelStr.render();
  }


  /**
   * Générer l'appel de la procédure : call onto_schema_ins
   *
   * @param ontorelId {@link String}
   * @param schemaName {@link String}
   * @param code {@link String}
   * @param value {@link String}
   * @return {@link String}
   */
  public String generateCallSchemaIns(String ontorelId, String schemaName, String code,
      String value) {
    ST ontorelStr = template.getInstanceOf("onto_schema_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("schema_name", schemaName);
    ontorelStr.add("code", code);
    value = value.replace("'", "''");
    ontorelStr.add("value", value);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_import_ins
   *
   * @param ontorelId {@link String}
   * @param fileName {@link String}
   * @param json {@link String}
   * @param importDate {@link String}
   * @return {@link String}
   */
  public String generateCallImportIns(String ontorelId, String fileName, String json,
      String importDate) {
    ST ontorelStr = template.getInstanceOf("onto_import_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("file_name", fileName);
    json = json.replace("'", "''");
    ontorelStr.add("json", json);
    ontorelStr.add("import_date", importDate);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call ontology_ins
   *
   * @param ontorelId {@link String}
   * @param iri {@link String}
   * @param fileName {@link String}
   * @param alias {@link String}
   * @param version {@link String}
   * @param createDate {@link String}
   * @return {@link String}
   */
  public String generateCallOntologyIns(String ontorelId, String iri, String fileName,
      String alias, String version, String createDate) {
    ST ontorelStr = template.getInstanceOf("ontology_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("iri", iri);
    ontorelStr.add("file_name", fileName);
    ontorelStr.add("alias", alias);
    ontorelStr.add("version", version);
    ontorelStr.add("create_date", createDate);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure: call onto_class_ins
   *
   * @param ontorelId {@link String}
   * @param iri {@link String}
   * @param tableId {@link String}
   * @param originClass {@link String}
   * @return {@link String}
   */
  public String generateCallClassIns(String ontorelId, String iri, String tableId,
      String originClass) {
    ST ontorelStr = template.getInstanceOf("onto_class_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("iri", iri);
    ontorelStr.add("table_id", tableId);
    ontorelStr.add("origin_class", originClass);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure: call onto_data_type_ins
   *
   * @param ontorelId {@link String}
   * @param iri {@link String}
   * @param tableId {@link String}
   * @param owlsqlType {@link String}
   * @return {@link String}
   */
  public String generateCallDataTypeIns(String ontorelId, String iri, String tableId,
      String owlsqlType) {
    ST ontorelStr = template.getInstanceOf("onto_data_type_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("iri", iri);
    ontorelStr.add("table_id", tableId);
    ontorelStr.add("owlsql_type", owlsqlType);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure: call onto_data_type_ins
   *
   * @param ontorelId {@link String}
   * @param iri {@link String}
   * @param owlsqlType {@link String}
   * @param postgresqlType {@link String}
   * @return {@link String}
   */
  public String generateCallDataTypeSqlIns(String ontorelId, String iri, String owlsqlType,
      String postgresqlType) {
    ST ontorelStr = template.getInstanceOf("onto_data_type_sql_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("iri", iri);
    ontorelStr.add("owlsql_type", owlsqlType);
    ontorelStr.add("postgresql_type", postgresqlType);
    return ontorelStr.render();
  }


  /**
   * Générer l'appel de la procédure : call onto_object_properties_ins
   *
   * @param ontorelId {@link String}
   * @param iri {@link String}
   * @param tableId {@link String}
   * @return {@link String}
   */
  public String generateCallObjectPropertiesIns(String ontorelId, String iri, String tableId) {
    ST ontorelStr = template.getInstanceOf("onto_object_properties_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("iri", iri);
    ontorelStr.add("table_id", tableId);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_object_properties_domain_ins
   *
   * @param ontorelId {@link String}
   * @param classIri {@link String}
   * @param propertyIri {@link String}
   * @return {@link String}
   */
  public String generateObjectPeropertiesDomainIns(String ontorelId, String classIri,
      String propertyIri) {
    ST ontorelStr = template.getInstanceOf("onto_object_properties_domain_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("class_iri", classIri);
    ontorelStr.add("property_iri", propertyIri);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_object_properties_range_ins
   *
   * @param ontorelId {@link String}
   * @param classIri {@link String}
   * @param propertyIri {@link String}
   * @return {@link String}
   */
  public String generateCallObjectPropertiesRangeIns(String ontorelId, String classIri,
      String propertyIri) {
    ST ontorelStr = template.getInstanceOf("onto_object_properties_range_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("class_iri", classIri);
    ontorelStr.add("property_iri", propertyIri);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_data_properties_ins
   *
   * @param ontorelId {@link String}
   * @param iri {@link String}
   * @return {@link String}
   */
  public String generateCallDataPropertiesIns(String ontorelId, String iri) {
    ST ontorelStr = template.getInstanceOf("onto_data_properties_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("iri", iri);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel du procédure: call onto_data_properties_domain_ins
   *
   * @param ontorelId {@link String}
   * @param classIri {@link String}
   * @param propertyIri {@link String}
   * @return {@link String}
   */
  public String generateDataPropertiesDomainIns(String ontorelId, String classIri,
      String propertyIri) {
    ST ontorelStr = template.getInstanceOf("onto_data_properties_domain_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("class_iri", classIri);
    ontorelStr.add("property_iri", propertyIri);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel du procédure: call onto_data_properties_range_ins
   *
   * @param ontorelId {@link String}
   * @param datatypeIri {@link String}
   * @param propertyIri {@link String}
   * @param sqlType {@link String}
   * @return {@link String}
   */
  public String generateCallDataPropertiesRangeIns(String ontorelId, String datatypeIri,
      String propertyIri, String sqlType) {
    ST ontorelStr = template.getInstanceOf("onto_data_properties_range_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("datatype_iri", datatypeIri);
    ontorelStr.add("property_iri", propertyIri);
    ontorelStr.add("sql_type", sqlType);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_object_property_inheritance_ins
   *
   * @param ontorelId {@link String}
   * @param superpropertyIri {@link String}
   * @param subpropertyIri {@link String}
   * @return {@link String}
   */
  public String generateCallObjectPropInherIns(String ontorelId, String superpropertyIri,
      String subpropertyIri) {
    ST ontorelStr = template.getInstanceOf("onto_object_property_inheritance_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("superproperty_iri", superpropertyIri);
    ontorelStr.add("subproperty_iri", subpropertyIri);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_class_inheritance_ins
   *
   * @param ontorelId {@link String}
   * @param superclassIri {@link String}
   * @param subclassIri {@link String}
   * @return {@link String}
   */
  public String generateCallClassInherIns(String ontorelId, String superclassIri,
      String subclassIri) {
    ST ontorelStr = template.getInstanceOf("onto_class_inheritance_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("superclass_iri", superclassIri);
    ontorelStr.add("subclass_iri", subclassIri);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_class_axiom_ins
   *
   * @param ontorelId {@link String}
   * @param domainIri {@link String}
   * @param rangeIri {@link String}
   * @param propertyIri {@link String}
   * @param domainCard {@link String}
   * @param rangeCard {@link String}
   * @param origin {@link String}
   * @param tableId {@link String}
   * @return {@link String}
   */
  public String generateCallClassAxiomIns(String ontorelId, String domainIri, String rangeIri,
      String propertyIri, String domainCard, String rangeCard, String origin, String tableId) {
    ST ontorelStr = template.getInstanceOf("onto_class_axiom_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("domain_iri", domainIri);
    ontorelStr.add("range_iri", rangeIri);
    ontorelStr.add("property_iri", propertyIri);
    ontorelStr.add("domain_card", domainCard);
    ontorelStr.add("range_card", rangeCard);
    ontorelStr.add("origin", origin);
    ontorelStr.add("table_id", tableId);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_data_axiom_ins
   *
   * @param ontorelId {@link String}
   * @param domainIri {@link String}
   * @param rangeIri {@link String}
   * @param propertyIri {@link String}
   * @param domainCard {@link String}
   * @param origin {@link String}
   * @param tableId {@link String}
   * @return {@link String}
   */
  public String generateCallDataAxiomIns(String ontorelId, String domainIri, String rangeIri,
      String propertyIri, String domainCard, String origin, String tableId) {
    ST ontorelStr = template.getInstanceOf("onto_data_axiom_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("domain_iri", domainIri);
    ontorelStr.add("range_iri", rangeIri);
    ontorelStr.add("property_iri", propertyIri);
    ontorelStr.add("domain_card", domainCard);
    ontorelStr.add("origin", origin);
    ontorelStr.add("table_id", tableId);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call ontology_label_ins
   *
   * @param ontorelId {@link String}
   * @param iri {@link String}
   * @param code {@link String}
   * @param value {@link String}
   * @return {@link String}
   */
  public String generateOntologyLabelIns(String ontorelId, String iri, String code, String value) {
    ST ontorelStr = template.getInstanceOf("ontology_label_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("iri", iri);
    ontorelStr.add("code", code);
    value = value.replace("'", "''");
    ontorelStr.add("value", value);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_label_ins
   *
   * @param ontorelId {@link String}
   * @param iri {@link String}
   * @param code {@link String}
   * @param value {@link String}
   * @return {@link String}
   */
  public String generateCallLabelIns(String ontorelId, String iri, String code, String value) {
    ST ontorelStr = template.getInstanceOf("onto_label_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("iri", iri);
    ontorelStr.add("code", code);
    value = value.replace("'", "''");
    ontorelStr.add("value", value);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : call onto_definition_ins
   *
   * @param ontorelId {@link String}
   * @param iri {@link String}
   * @param code {@link String}
   * @param value {@link String}
   * @return {@link String}
   */
  public String generateCallDefinitionIns(String ontorelId, String iri, String code, String value) {
    ST ontorelStr = template.getInstanceOf("onto_definition_ins");
    ontorelStr.add("ontorel_id", ontorelId);
    ontorelStr.add("iri", iri);
    ontorelStr.add("code", code);
    value = value.replace("'", "''");
    ontorelStr.add("value", value);
    return ontorelStr.render();
  }

  /**
   * Générer l'appel de la procédure : generate view of table definition OnotRelCat
   *
   * @param code {@link String}
   * @return {@link String}
   */
  public String generateViewTableDefinitionOntorelCalt(String code) {
    ST ontorelStr = template.getInstanceOf("vue_table_def_ontorelcat");
    ontorelStr.add("code", code);
    return ontorelStr.render();
  }

  protected static class AttributeString {
    String id;
    String type;

    /**
     * @param id
     * @param type
     */
    public AttributeString(String id, String type) {
      super();
      this.id = id;
      this.type = type;
    }

    public String getId() {
      return id;
    }

    public String getType() {
      return type;
    }
  }
}
