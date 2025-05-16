package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ca.griis.ontorela.mrel.InclusionConstraint;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.catalog.Identifier;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire des Contraintes d'inclusion
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
public class InclusionConstrainTest {

  private Table unionTable;
  private Table t1;
  private Table t2;
  private Set<Table> tableSet;
  private InclusionConstraint inclusionConstraint;

  @BeforeEach
  void setUp() {
    unionTable = mock(Table.class);
    t1 = mock(Table.class);
    t2 = mock(Table.class);
    Identifier identifierMock = mock(Identifier.class);
    when(unionTable.getIdentifier()).thenReturn(identifierMock);
    when(identifierMock.getValue()).thenReturn("UnionTable");
    tableSet = new LinkedHashSet<>();
    tableSet.add(t1);
    tableSet.add(t2);
    inclusionConstraint = new InclusionConstraint(unionTable, tableSet);
  }

  @Test
  void testGetUnionTable() {
    assertEquals(unionTable, inclusionConstraint.getUnionTable());
    verify(unionTable, times(1)).getIdentifier();
  }

  @Test
  void testGetElementSet() {
    assertEquals(tableSet, inclusionConstraint.getElementSet());
  }

  @Test
  void testToString() {
    String expected =
        "InclusionConstraint [unionTable=" + unionTable + ", elementSet=" + tableSet + "]";
    assertEquals(expected, inclusionConstraint.toString());
  }

  @Test
  void testSetsCorrectName() {
    assertEquals("UnionTable_checkInclusion", inclusionConstraint.getName());
  }

  @Test
  void testEquals() {
    InclusionConstraint constraint = new InclusionConstraint(unionTable, tableSet);
    assertTrue(inclusionConstraint.equals(constraint));
  }

}
