package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ca.griis.ontorela.mrel.NoRedundancyConstraint;
import ca.griis.ontorela.mrel.TableJoin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire des Contraintes de redondance
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
public class NoRedundancyConstaintTest {

  private TableJoin joinExp;
  private NoRedundancyConstraint noRedundancyConstraint;

  @BeforeEach
  void setUp() {
    joinExp = mock(TableJoin.class);

    when(joinExp.getIri()).thenReturn("http://ABC/join");
    noRedundancyConstraint = new NoRedundancyConstraint(joinExp);
  }

  @Test
  void testGetJoinExp() {
    assertEquals(joinExp, noRedundancyConstraint.getJoinExp());
    verify(joinExp, times(1)).getIri();
  }

  @Test
  void testConstructorSetsCorrectName() {
    assertEquals("http://ABC/join_checkNonRedundancy", noRedundancyConstraint.getName());
  }
}
