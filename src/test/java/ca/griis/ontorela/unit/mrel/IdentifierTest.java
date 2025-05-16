package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.ontorela.mrel.catalog.Identifier;
import org.junit.jupiter.api.Test;

class TestIdentifier extends Identifier {
  public TestIdentifier(String id) {
    super(id);
  }
}


public class IdentifierTest {
  @Test
  void testcreateId() {
    TestIdentifier identifier = new TestIdentifier("test123");
    assertEquals("test123", identifier.getValue());
  }

  @Test
  void testequals() {
    TestIdentifier id1 = new TestIdentifier("test123");
    TestIdentifier id11 = new TestIdentifier("test123");
    TestIdentifier id2 = new TestIdentifier("test456");
    assertFalse(id1.equals(id2));
    assertTrue(id1.equals(id11));
    assertNotNull(id1.hashCode());
    assertNotNull(id1.toString());
    assertEquals("Identifier [test123]", id1.toString());

  }
}
