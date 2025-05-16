package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.ontorela.mrel.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire d'un type
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
public class TypeTest {
  private Type type1;
  private Type type2;
  private Type type3;

  @BeforeEach
  public void init() {
    type1 = new Type("id1", "domain1");
    type2 = new Type("id1", "domain1");
    type3 = new Type("id2", "domain2");
  }

  @Test
  public void test() {
    assertEquals("id1", type1.getId());
    assertEquals("domain1", type1.getDomain());
    assertTrue(type1.equals(type1));
    assertTrue(type1.equals(type2));
    assertTrue(type2.equals(type1));
    assertFalse(type1.equals(type3));
    assertFalse(type1.equals(null));
    assertFalse(type1.equals(new Object()));
    String expected = "Domain [id=id1, type=domain1]";
    assertEquals(expected, type1.toString());
  }
}
