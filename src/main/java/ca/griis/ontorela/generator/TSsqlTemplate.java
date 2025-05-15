package ca.griis.ontorela.generator;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

/**
 * Décrire la classe ici.
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui | non (pourquoi).</li>
 * <li>Clonabilité : oui | non (pourquoi).</li>
 * <li>Modifiabilité : oui | non (pourquoi).</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-03-16 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2019-03-16
 */
public class TSsqlTemplate extends SqlTemplate {
  // **************************************************************************
  // Attributs spécifiques
  //
  // **************************************************************************
  // Constructeurs
  //
  public TSsqlTemplate() {
    String path = "TSSQL.stg";
    setTemplate(new STGroupFile(path));
    if (template == null) {
      System.err.println("Template not found " + path);
    }
  }
  // **************************************************************************
  // Opérations propres
  //

  // **************************************************************************
  // Opérations publiques
  //
  public String getPeriodFunctions(String schemaId) {
    ST vxeFunc = template.getInstanceOf("create_vxe_function");
    vxeFunc.add("schemaId", schemaId);
    ST vbxFunc = template.getInstanceOf("create_vbx_function");
    vbxFunc.add("schemaId", schemaId);
    //
    String s = "";
    s += vxeFunc.render();
    s += vbxFunc.render();
    return s;
  }

  /**
   * Obtenir une requête SQL simple d'une table avec une selection sur une table et une
   * projetion sur un ou plusieurs attributs.
   *
   * @param schemaId : le schéma de la table.
   * @param tableId : la table.
   * @param attMap : liste de paire d'attributs et leur alias (alias peut être null).
   * @param funcSig : liste de paire de signatures de function et leur alias.
   * @return une chaine de caractère SQL d'une requête temporel simple.
   */
  public String getSimpleQuery(String schemaId, String tableId, Map<String, String> attMap,
      Map<String, String> funcSig) {
    ST query = template.getInstanceOf("simple_query");
    query.add("schemaId", schemaId);
    query.add("tableId", tableId);
    for (Entry<String, String> e : attMap.entrySet()) {
      String id = e.getKey();
      String alias = e.getValue();
      query.addAggr("attSet.{id, alias}", id, alias);
    }
    for (Entry<String, String> e : funcSig.entrySet()) {
      String id = e.getKey();
      String alias = e.getValue();
      query.addAggr("funcSig.{id, alias}", id, alias);
    }
    return query.render();
  }

  /**
   * Obtenir une vue historique d'une table à partir 3 partitions : vxe, vbe et vbx.
   *
   * @param schemaId : le schéma de la table.
   * @param viewId : l'identidiant de la vue.
   * @param vxeTableId : l'identifiant de la table vxe.
   * @param vxeAttMap : une paire d'identifiant de l'attribut vxe et son alias
   * @param vbeTableId : l'identifiant de la table vbe.
   * @param vbeAttMap : une paire d'identifiant de l'attribut vbe et son alias
   * @param vbxTableId : l'identifiant de la table vbx.
   * @param vbxAttMap : une paire d'identifiant de l'attribut vbx et son alias.
   * @param attMap : liste de paires d'identifiant d'attribut et leur alias.
   * @return une chaine de caractère SQL de la vue historique d'une table.
   */
  public String getHistoryView(String schemaId, String viewId, String vxeTableId,
      Map<String, String> vxeAttMap, String vbeTableId, Map<String, String> vbeAttMap,
      String vbxTableId, Map<String, String> vbxAttMap, Map<String, String> attMap) {
    ST view = template.getInstanceOf("history_view");
    view.add("schemaId", schemaId);
    view.add("viewId", viewId);
    //
    view.add("vxeQuery", getSimpleQuery(schemaId, vxeTableId, attMap, vxeAttMap));
    view.add("vbeQuery", getSimpleQuery(schemaId, vbeTableId, attMap, vbeAttMap));
    view.add("vbxQuery", getSimpleQuery(schemaId, vbxTableId, attMap, vbxAttMap));
    return view.render();
  }

  /**
   * Obtenir une contrainte d'unicité temporelle pour une table.
   *
   * @param schemaId : le schéma.
   * @param constraintId : l'identificateur de la contrainte.
   * @param tableId : la table de base.
   * @param vxeTableId : l'identifiant de la table vxe.
   * @param vbeTableId : l'identifiant de la table vbe.
   * @param vbxTableId : l'identifiant de la table vbx.
   * @param vxeAttId : l'identifiant de l'attribut vxe.
   * @param vbeAttId : l'identifiant de l'attribut vbe.
   * @param vbxAttId : l'identifiant de l'attribut vbx.
   * @param keyMap : liste de paire identifiant de l'attribut et son type.
   * @return une chaine de caractère SQL de la contrainte d'unicité temporel.
   */
  // temporalUniqueness_check(schemaId, vxeTableId, vbeTableId, vbxTableId, vxeAttId, vbeAttId,
  // vbxAttId, keySet)
  public String getTemporalUniquenessCheck(String schemaId, String constraintId, String tableId,
      String vxeTableId, String vbeTableId, String vbxTableId, String vxeAttId, String vbeAttId,
      String vbxAttId, Map<String, String> keyMap) {
    ST check = template.getInstanceOf("temporalUniqueness_check");
    check.add("schemaId", schemaId);
    check.add("constraintId", constraintId);
    check.add("tableId", tableId);
    check.add("vxeTableId", vxeTableId);
    check.add("vbeTableId", vbeTableId);
    check.add("vbxTableId", vbxTableId);
    check.add("vxeAttId", vxeAttId);
    check.add("vbeAttId", vbeAttId);
    check.add("vbxAttId", vbxAttId);
    for (Entry<String, String> e : keyMap.entrySet()) {
      check.addAggr("keySet.{id, type}", e.getKey(), e.getValue());
    }
    return check.render();
  }

  /**
   * Obtenir la fonction d'insertion pour une table qui représente un axiome de données.
   *
   * @param schemaId : le schéma.
   * @param tableId : la table de base.
   * @param keyAttId
   * @param keyAttType
   * @param typeTableId
   * @param typeKeyAttId
   * @param typeValAttId
   * @param typeValAttType
   * @return une chaine de caractère SQL de la function d'insertion.
   */
  public String getInsertFunction(String schemaId, String tableId, String keyAttId,
      String keyAttType, String typeTableId, String typeKeyAttId, String typeValAttId,
      String typeValAttType) {
    ST insert = template.getInstanceOf("dataAxiom_insertion");
    insert.add("schemaId", schemaId);
    insert.add("tableId", tableId);
    insert.add("typeTableId", typeTableId);
    insert.add("keyAtt", new AttributeString(keyAttId, keyAttType));
    insert.add("typeValAtt", new AttributeString(typeValAttId, typeValAttType));
    insert.add("typeKeyAttId", typeKeyAttId);
    return insert.render();
  }

  /**
   * Obtenir la contrainte de non redondance temporelle en SQL.
   *
   * @param schemaId : le schéma.
   * @param tableId : la table de base.
   * @param keyAttId : l'ensemble des identifiants des attributs clés.
   * @param noKeyAttId : l'identifiant de l'attribut non clé (en considérant que la table est en
   *        6FN).
   * @param vtId : l'identifiant de l'attribut temporel.
   * @return une chaine de caractère SQL de la contrainte de non redondance.
   */
  public String getNoRedundancyConstraint(String schemaId, String tableId, Set<String> keyAttId,
      String noKeyAttId, String vtId) {
    ST check = template.getInstanceOf("noRedundancy");
    check.add("schemaId", schemaId);
    check.add("tableId", tableId);
    for (String a : keyAttId) {
      check.addAggr("keyAttSet.{id}", a);
    }
    check.add("noKeyAttId", noKeyAttId);
    check.add("vtId", vtId);
    return check.render();
  }

  /**
   * Obtenir la contrainte de non circomlocution temporelle en SQL.
   *
   * @param schemaId : le schéma.
   * @param tableId : la table de base.
   * @param keyAttId : l'ensemble des identifiants des attributs clés.
   * @param noKeyAttId : l'identifiant de l'attribut non clé (en considérant que la table est en
   *        6FN).
   * @param vtId : l'identifiant de l'attribut temporel.
   * @return une chaine de caractère SQL de la contrainte de non circomlocution.
   */
  public String getNoCircumlocutionConstraint(String schemaId, String tableId, Set<String> keyAttId,
      String noKeyAttId, String vtId) {
    ST check = template.getInstanceOf("noCircumlocution");
    check.add("schemaId", schemaId);
    check.add("tableId", tableId);
    for (String a : keyAttId) {
      check.addAggr("keyAttSet.{id}", a);
    }
    check.add("noKeyAttId", noKeyAttId);
    check.add("vtId", vtId);
    return check.render();
  }

  /**
   * Obtenir la contrainte de non contradiction temporelle en SQL.
   *
   * @param schemaId : le schéma.
   * @param tableId : la table de base.
   * @param keyAttId : l'ensemble des identifiants des attributs clés.
   * @param noKeyAttId : l'identifiant de l'attribut non clé (en considérant que la table est en
   *        6FN).
   * @param vtId : l'identifiant de l'attribut temporel.
   * @return une chaine de caractère SQL de la contrainte de non contradiction.
   */
  public String getNoContradictionConstraint(String schemaId, String tableId, Set<String> keyAttId,
      String noKeyAttId, String vtId) {
    ST check = template.getInstanceOf("noContradiction");
    check.add("schemaId", schemaId);
    check.add("tableId", tableId);
    for (String a : keyAttId) {
      check.addAggr("keyAttSet.{id}", a);
    }
    check.add("noKeyAttId", noKeyAttId);
    check.add("vtId", vtId);
    return check.render();
  }
}
