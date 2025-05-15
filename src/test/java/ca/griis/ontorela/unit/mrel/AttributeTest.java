package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.TemporalPartitionType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire sur la classe table.
 * 
 * <p>
 * <b>Copyright</b> 2016-2017, GRIIS (https://griis.ca/) <br>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé)
 * <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke (Québec) J1K 2R1 <br>
 * CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 * </p>
 *
 * <b>Future Tasks</b><br>
 * 
 * <br>
 * <b>Realized Tasks</b><br>
 * 2017-11-19 (0.0.1) [CK] - Creation.<br>
 * <br>
 * 
 * @since 2018-03-02
 * @version 0.0.1
 * @author [FO] Francis.Ouellet@USherbrooke.ca
 */
public class AttributeTest {

  // **************************************************************************
  // Constructeurs
  //
  private Attribute attribute;

  /**
   * Initialisation des tests.
   */
  @BeforeEach
  public void initTest() {
    attribute = new Attribute("att1", "http://ABC/att1", "integer");
  }

  // **************************************************************************
  // Cas de test
  //
  @Test
  public void test() {
    String id = "V001";
    String iri = "http://www.w3.org/2002/07/owl";
    String type = "INTEGER";
    Attribute attribute = new Attribute(id, iri, type);
    assertTrue(attribute.getAttId().equals(id));
    assertTrue(attribute.getAttIri().equals(iri));
    assertTrue(attribute.getAttType().equals(type));
    assertTrue(
        attribute.getAttString(true, true).equals("V001::http://www.w3.org/2002/07/owl INTEGER"));
    assertTrue(attribute.getAttString(false, true).equals("http://www.w3.org/2002/07/owl INTEGER"));
    assertTrue(attribute.getAttString(true, false).equals("V001::http://www.w3.org/2002/07/owl"));
    assertTrue(attribute.getAttString(false, false).equals("http://www.w3.org/2002/07/owl"));
  }

  @Test
  void testWithTemporalPartition() {
    Attribute attr = new Attribute(TemporalPartitionType.TT, "att2",
        "http://www.w3.org/2002/07/owl/att2", "INTEGER");
    assertEquals("att2", attr.getAttId());
    assertEquals("http://www.w3.org/2002/07/owl/att2", attr.getAttIri());
    assertEquals("INTEGER", attr.getAttType());
    assertEquals(TemporalPartitionType.TT, attr.getTemporalPartition());
  }

  @Test
  void testWithoutTemporalPartition() {
    assertEquals("att1", attribute.getAttId());
    assertEquals("http://ABC/att1", attribute.getAttIri());
    assertEquals("integer", attribute.getAttType());
    assertEquals(TemporalPartitionType.NT, attribute.getTemporalPartition());
  }

  @Test
  void testAddDefinitions()
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    Map<String, String> defs = new LinkedHashMap<>();
    defs.put("en", "English Definition");
    defs.put("fr", "Fr Definition");
    Method method = Attribute.class.getDeclaredMethod("addDefinitions", Map.class);
    method.setAccessible(true);
    method.invoke(attribute, defs);
    assertEquals("English Definition", attribute.getDefinition("en"));
    assertEquals("Fr Definition", attribute.getDefinition("fr"));
  }

  @Test
  void testAddLabels()
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    Map<String, String> labels = new LinkedHashMap<>();
    labels.put("en", "English label");
    labels.put("fr", "label en fr");
    Method method = Attribute.class.getDeclaredMethod("addLabels", Map.class);
    method.setAccessible(true);
    method.invoke(attribute, labels);
    assertEquals("English label", attribute.getLabel("en"));
    assertEquals("label en fr", attribute.getLabel("fr"));
  }

}
