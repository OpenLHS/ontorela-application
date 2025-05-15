package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.griis.ontorela.mrel.Attribute;
import ca.griis.ontorela.mrel.MembershipConstraint;
import ca.griis.ontorela.mrel.Table;
import ca.griis.ontorela.mrel.catalog.Identifier;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire des Contraintes d'appartenance
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
public class MembershipConstraintTest {
  private Table source;
  private Attribute sourceAtt;
  private Table target1;
  private Table target2;
  private Set<Table> targetSet;
  private MembershipConstraint membershipConstraint;
  private MembershipConstraint membershipConstraintSingleTarget;

  @BeforeEach
  public void setUp() {
    source = mock(Table.class);
    sourceAtt = mock(Attribute.class);
    target1 = mock(Table.class);
    target2 = mock(Table.class);
    Identifier identifierMock = mock(Identifier.class);
    when(source.getIdentifier()).thenReturn(identifierMock);
    when(identifierMock.getValue()).thenReturn("SourceTable");
    targetSet = new LinkedHashSet<>();
    targetSet.add(target1);
    targetSet.add(target2);
    membershipConstraint = new MembershipConstraint(source, sourceAtt, targetSet);
    membershipConstraintSingleTarget = new MembershipConstraint(source, sourceAtt, target1);
  }

  @Test
  public void testGetSource() {
    assertEquals(source, membershipConstraint.getSource());
    verify(source, times(2)).getIdentifier();
  }

  @Test
  public void testGetSourceAtt() {
    assertEquals(sourceAtt, membershipConstraint.getSourceAtt());
  }

  @Test
  void testGetTargetSet_MultipleTargets() {
    assertEquals(targetSet, membershipConstraint.getTargetSet());
  }

  @Test
  void testGetTargetSet_SingleTarget() {
    Set<Table> expectedSet = new LinkedHashSet<>();
    expectedSet.add(target1);
    assertEquals(expectedSet, membershipConstraintSingleTarget.getTargetSet());
  }

  @Test
  void testToString() {
    String expected = "MembershipConstraint [source=" + source + ", targetSet=" + targetSet + "]";
    assertEquals(expected, membershipConstraint.toString());
  }

  @Test
  void testSetsCorrectName() {
    assertEquals("SourceTable_checkMembership", membershipConstraint.getName());
  }
}
