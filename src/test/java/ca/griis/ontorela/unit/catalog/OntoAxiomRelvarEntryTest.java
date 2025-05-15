package ca.griis.ontorela.unit.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import ca.griis.ontorela.catalog.OntoAxiomRelvarEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OntoAxiomRelvarEntryTest {

  private OntoAxiomRelvarEntry entry;

  @BeforeEach
  public void setUp() {
    entry = new OntoAxiomRelvarEntry("T001", "http://ABC/C00",
        "http://ABC/C01", "http://ABC/op1");
  }

  @Test
  public void testGetTableId() {
    assertEquals("T001", entry.getTableId());
  }

  @Test
  public void testGetDomainIri() {
    assertEquals("http://ABC/C00", entry.getDomainIri());
  }

  @Test
  public void testGetRangeIri() {
    assertEquals("http://ABC/C01", entry.getRangeIri());
  }

  @Test
  public void testGetPropertyIri() {
    assertEquals("http://ABC/op1", entry.getPropertyIri());
  }

  @Test
  public void testToString() {
    String expected = "OntoAxiomRelvarEntry [tableId=T001, domainIri=http://ABC/C00, " +
        "propertyIri=http://ABC/op1, rangeIri=http://ABC/C01]";
    assertEquals(expected, entry.toString());
  }

  @Test
  void testConstructorWithNullValues() {
    OntoAxiomRelvarEntry nullEntry = new OntoAxiomRelvarEntry(null, null, null, null);
    assertNull(nullEntry.getTableId());
    assertNull(nullEntry.getDomainIri());
    assertNull(nullEntry.getRangeIri());
    assertNull(nullEntry.getPropertyIri());
  }

  @Test
  void testConstructorWithEmptyStrings() {
    OntoAxiomRelvarEntry emptyEntry = new OntoAxiomRelvarEntry("", "", "", "");
    assertEquals("", emptyEntry.getTableId());
    assertEquals("", emptyEntry.getDomainIri());
    assertEquals("", emptyEntry.getRangeIri());
    assertEquals("", emptyEntry.getPropertyIri());
  }
}
