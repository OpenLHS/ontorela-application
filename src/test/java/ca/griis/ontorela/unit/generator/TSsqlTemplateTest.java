package ca.griis.ontorela.unit.generator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.generator.TSsqlTemplate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

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
 * @since 2019-03-16
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */
public class TSsqlTemplateTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  protected static final Descriptor d = new Descriptor();
  private static TSsqlTemplate sql = new TSsqlTemplate();

  // **************************************************************************
  // Constructeurs
  //
  @AfterAll
  public static void finTest() {
    System.out.println(d);
  }

  // **************************************************************************
  // Cas de test
  //
  @Test
  public void testTSsqlTemplate() {
    assertNotNull(sql.getTemplate());
    //
    d.ajouter(sql.getTemplate().getInstanceOf("schema").render());
    assertNotNull(sql.getTemplate().getInstanceOf("schema"));
    d.ajouter(sql.getTemplate().getInstanceOf("create_vxe_function").render());
    assertNotNull(sql.getTemplate().getInstanceOf("create_vxe_function"));
  }

  @Test
  public void testGenerateSimpleQuery() {
    Map<String, String> attMap = new HashMap<>();
    attMap.put("a0", null);
    attMap.put("vbx", "vt");
    String query = sql.getSimpleQuery("TDB", "T0", attMap, new LinkedHashMap<>());
    assertFalse(query.isEmpty());
    //
    d.ajouter(query);
  }

  @Test
  public void testGenerateHistoryView() {
    Map<String, String> attMap = new LinkedHashMap<>();
    attMap.put("k", null);
    Map<String, String> vxeAttMap = new LinkedHashMap<>();
    vxeAttMap.put("vxe", "vt");
    Map<String, String> vbeAttMap = new LinkedHashMap<>();
    vbeAttMap.put("vbe", "vt");
    Map<String, String> vbxAttMap = new LinkedHashMap<>();
    vbxAttMap.put("vbx", "vt");
    String view = sql.getHistoryView("TDB", "T0", "T0_vxe", vxeAttMap, "T0_vbe", vbeAttMap,
        "T0_vbx", vbxAttMap, attMap);
    assertFalse(view.isEmpty());
    //
    d.ajouter(view);
  }

  @Test
  // temporalUniqueness_check(schema_id, vxeTable_id, vbeTable_id, vbxTable_id, vxeAtt_id,
  // vbeAtt_id, vbxAtt_id, keySet)
  public void testGenerateTemporalUniquenessCheck() {
    Map<String, String> keyMap = new LinkedHashMap<>();
    keyMap.put("k0", "INT");
    keyMap.put("k1", "INT");
    String check = sql.getTemporalUniquenessCheck("TDB", "T0", "_checkTemporalUniqueness", "T0_vxe",
        "T0_vbe", "T0_vbx", "vxe", "vbe", "vbx", keyMap);
    assertFalse(check.isEmpty());
    //
    d.ajouter(check);
  }

  @Test
  public void testGenerateInsertFunction() {
    String insert = sql.getInsertFunction("TDB", "T0", "T0_dbid", "DBID", "CODE", "code_dbid",
        "value", "STRING");
    assertFalse(insert.isEmpty());
    //
    d.ajouter(insert);
  }

  @Test
  public void testGetNoRedundancyConstraint() {
    Set<String> keyAttId = new LinkedHashSet<>();
    keyAttId.add("k0");
    keyAttId.add("k1");
    String check = sql.getNoRedundancyConstraint("TDB", "T0", keyAttId, null, "during");
    d.ajouter(check);
  }

  @Test
  public void testGetNoCircumlocutionConstraint() {
    Set<String> keyAttId = new LinkedHashSet<>();
    keyAttId.add("k0");
    keyAttId.add("k1");
    String check = sql.getNoCircumlocutionConstraint("TDB", "T0", keyAttId, "a0", "during");
    d.ajouter(check);
  }

  @Test
  public void testGetnoContradictionConstraint() {
    Set<String> keyAttId = new LinkedHashSet<>();
    keyAttId.add("k0");
    keyAttId.add("k1");
    String check = sql.getNoContradictionConstraint("TDB", "T0", keyAttId, "a0", "during");
    d.ajouter(check);
  }
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
}
