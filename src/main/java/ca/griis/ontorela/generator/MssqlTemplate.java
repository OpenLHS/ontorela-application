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
public class MssqlTemplate extends SqlTemplate {
  // *************************************************************************
  // Attributs spécifiques
  //
  protected STGroup mssqlTemplate;

  // **************************************************************************
  // Constructeur
  //
  public MssqlTemplate() {
    String path = "MSSQL.stg";
    mssqlTemplate = new STGroupFile(path);
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
  // **************************************************************************
  // Opérations propres
  //

  public STGroup getTemplate() {
    return mssqlTemplate;
  }

  public void setTemplate(STGroup template) {
    this.mssqlTemplate = template;
  }

  // **************************************************************************
  // Opérations publiques - DÉFINITION DU SCHÉMA
  //

  /**
   * Définition d'un schéma selon la syntaxe de postgreSQL.
   *
   * @param schemaId : l'identifiant du schéma
   * @return la chaine de caractère de définition d'un schéma,
   */
  public String genererSchema(String schemaId) {
    ST schema = mssqlTemplate.getInstanceOf("schema");
    schema.add("schema_id", schemaId);
    return schema.render();
  }

  /**
   * Définition d'un commentaire sur un schéma.
   *
   * @param schemaId : l'identifiant du schéma
   * @param def : la définition du schéma
   * @return la chaine de caractère de définition d'un commentaire de schéma.
   */
  public String genererSchemaDefinition(String schemaId, String def) {
    ST schema = mssqlTemplate.getInstanceOf("schema_def");
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
    ST schema = mssqlTemplate.getInstanceOf("sup_schema");
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
    ST type = mssqlTemplate.getInstanceOf("type_predefini");
    type.add("type_id", typeId);
    type.add("domaine_id", domaineId);
    type.add("annulable", annulable);
    return type.render();
  }

  /**
   * Generate domain for rdbms data types.
   *
   * @param schemaId {@link String}
   * @param typeId {@link String} : OWl datatype
   * @param domainId {@link String} : Corresponding rdbms data type
   * @return {@link String}
   *         TODO: Maintenant le type est cree avec le nom du schema, pour erreur de INT et Integer
   *         deja existant
   *         Utiliser les types OWL, changer la recolection de typen en Monto
   */
  public String genererDomaine(String schemaId, String typeId, String domainId) {
    ST domaineStr = mssqlTemplate.getInstanceOf("domaine");
    domaineStr.add("schema_id", schemaId);
    // FIXME 2021-05-04 : retirer après avoir changer dans les noms des types.
    // On doit le tirer ça quand on va préfixer tous les types avec owl_
    domaineStr.add("type_id", schemaId + typeId);
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
    ST drp = mssqlTemplate.getInstanceOf("suppression_domaine");
    drp.add("schema_id", schemaId);
    // FIXME 2021-05-04 : retirer après avoir changer dans les noms des types.
    // On doit le tirer ça quand on va préfixer tous les types avec owl_
    drp.add("domaine_id", schemaId + domaineId);
    return drp.render();
  }

  // **************************************************************************
  // Opérations publiques - DÉFINITION D'UNE RELATION
  //

  /**
   * Définition d'un commentaire sur une relvar.
   *
   * @param schemaId : l'identifiant du schéma.
   * @param varId : l'identifiant de la relvar.
   * @param def : la définition de la relvar.
   * @return la chaine de caractère de définition d'un commentaire de la relvar.
   */
  public String genererRelvarDefinition(String schemaId, String varId, String def) {
    ST relvar = mssqlTemplate.getInstanceOf("relvar_def");
    relvar.add("schema_id", schemaId);
    relvar.add("var_id", varId);
    def = def.replace("'", "''");
    relvar.add("def", def);
    return relvar.render();
  }

  /**
   * Définition d'un commentaire sur une vue.
   *
   * @param schemaId : l'identifiant du schéma
   * @param vueId : l'identifiant de la vue
   * @param def : la définition de la vue
   * @return la chaine de caractère de définition d'un commentaire de la vue.
   */
  public String genererVueDefinition(String schemaId, String vueId, String def) {
    ST relvar = mssqlTemplate.getInstanceOf("vue_def");
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
    ST relvar = mssqlTemplate.getInstanceOf("attribut_def");
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
    ST relvar = mssqlTemplate.getInstanceOf("relvar");
    relvar.add("schema_id", schemaId);
    relvar.add("var_id", varId);
    // Générer les attributs
    // FIXME 2021-05-04 : retirer après avoir changer dans les noms des types.
    // On doit le tirer ça quand on va préfixer tous les types avec owl_
    for (Map.Entry<String, String> e : ensAtt.entrySet()) {
      relvar.addAggr("ensAtt.{id, type}", e.getKey(), schemaId + e.getValue());
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
    ST vue = mssqlTemplate.getInstanceOf("vue_renommage");
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
      ontoView.addAggr("ensTable.{id, var_key, axiomString}", axiomLink.getKey(), sourceTableKeyId,
          axiomLink.getValue());
    }
    for (Entry<String, String> typeLink : typeTableDef.entrySet()) {
      ontoView.addAggr("ensTable.{id, var_key, axiomString}", typeLink.getKey(),
          typeLink.getValue(), typeLink.getKey());
    }
    return ontoView.render();
  }

  public String genererSuppressionRelVar(String schemaId, String varId) {
    ST drp = mssqlTemplate.getInstanceOf("suppression_relvar");
    drp.add("schema_id", schemaId);
    drp.add("var_id", varId);
    drp.add("enCascade", true);
    return drp.render();
  }

  // **************************************************************************
  // Opérations publiques - DÉFINITION DE CONTRAINTES
  //

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

  /**
   * Définition du gabarit d'une contrainte de clé secondaire.
   *
   * @param schemaId l'identifiant du schéma auquel appartient la table
   * @param varId l'identifiant de la relvar
   * @param contrainteId l'identifiant de la contrainte clé
   * @param attCle les identifiants des attributs formant la clé
   * @return la chaine de caractère de définition d'une clé secondaire selon le langage cible.
   */
  public String genererContrainteCleSecondaire(String schemaId, String varId, String contrainteId,
      ArrayList<String> attCle) {
    ST cle = mssqlTemplate.getInstanceOf("contrainte_cleSecondaire");
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
    ST ref = mssqlTemplate.getInstanceOf("contrainte_referentielle");
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
   * @param varOrigineSchema l'identifiant du schéma de la variable d'origine auquel appartient la
   *        table
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
    ST ref = mssqlTemplate.getInstanceOf("contrainte_referentielle");
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
  public String genererContRefDefinition(String schemaId, String varId, String contrainteId,
      String def) {
    ST cleRef = mssqlTemplate.getInstanceOf("cleRef_def");
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
    ST uniqueConstraint = mssqlTemplate.getInstanceOf("contrainte_unique");
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
    ST fct = mssqlTemplate.getInstanceOf("verificationParticipationMin");
    fct.add("table_id", tableId);
    fct.add("domaine_cle", new AttributeString(domainKeyId, domainKeyType));
    fct.add("contrainte_id", constraintId);
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
    ST fct = mssqlTemplate.getInstanceOf("verificationParticipationMax");
    fct.add("table_id", tableId);
    fct.add("domaine_cle", new AttributeString(domainKeyId, domainKeyType));
    fct.add("contrainte_id", constraintId);
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
    ST checkUnionAxiom = mssqlTemplate.getInstanceOf("checkUnionAxiom");
    checkUnionAxiom.add("contrainte_id", constraintId);
    checkUnionAxiom.add("schema_id", schemaId);
    checkUnionAxiom.add("unionTable_id", uniontableId);
    for (Map.Entry<String, String> e : unionElementSet.entrySet()) {
      ST selectKeys = mssqlTemplate.getInstanceOf("selectKeys");
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
    ST checkMembership = mssqlTemplate.getInstanceOf("checkMembership");
    checkMembership.add("contrainte_id", constraintId);
    checkMembership.add("schema_id", schemaId);
    checkMembership.add("sourceTable_id", sourceTableId);
    checkMembership.add("sourceAtt", sourceAttId);
    for (Map.Entry<String, String> e : targetTableMap.entrySet()) {
      checkMembership.addAggr("targetTableMap.{table_id, targetAtt}", e.getKey(), e.getValue());
    }
    return checkMembership.render();
  }

  // ***********************************************************************************************
  // Opérations publiques - MANIPULATION DU SCHÉMA
  //

  /**
   * Générer une expression.
   *
   * @param attributs {@link ArrayList}
   * @param relvars {@link ArrayList}
   * @return Retourne l'expression.
   */
  public String genererExpression(ArrayList<String> attributs, ArrayList<String> relvars) {
    ST requete = mssqlTemplate.getInstanceOf("requete");
    for (String a : attributs) {
      requete.addAggr("projection.{exp}", a);
    }
    for (String r : relvars) {
      requete.addAggr("selection.{exp}", r);
    }
    return requete.render();
  }

  /**
   * Définition d'une instruction de suppression d'une variable de relation.
   *
   * @param schemaId l'identifiant du schéma auquel appartient la table
   * @param varId l'identifiant de la relvar à supprimer
   * @return la chaine de caractère de suppression d'une variable de relation selon le langage.
   */
  public String genererSuppressionDonnees(String schemaId, String varId) {
    ST d = mssqlTemplate.getInstanceOf("suppression_donnees");
    d.add("schema_id", schemaId);
    d.add("var_id", varId);
    return d.render();
  }

  // ***********************************************************************************************
  // Opérations publiques - Documentation script
  //

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
    ST enteteF = mssqlTemplate.getInstanceOf("enteteFichierSQL");
    enteteF.add("schema_id", schemaId);
    enteteF.add("date_creation", dateCreation);
    enteteF.add("responsable", responsable);
    enteteF.add("version", version);
    enteteF.add("objet", objet);
    return enteteF.render();
  }

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
  public String genererEnteteFichierDoc(String schemaId, String dateCreation, String responsable,
      String version, String objet) {
    ST enteteF = mssqlTemplate.getInstanceOf("enteteFichierDoc");
    enteteF.add("schema_id", schemaId);
    enteteF.add("date_creation", dateCreation);
    enteteF.add("responsable", responsable);
    enteteF.add("version", version);
    enteteF.add("objet", objet);
    return enteteF.render();
  }
}
