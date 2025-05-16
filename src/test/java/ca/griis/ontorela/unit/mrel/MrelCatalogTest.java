package ca.griis.ontorela.unit.mrel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.griis.ontorela.mrel.MrelEntity;
import ca.griis.ontorela.mrel.catalog.Identifier;
import ca.griis.ontorela.mrel.catalog.MrelCatalog;
import java.util.*;
import org.junit.jupiter.api.*;

/**
 * Test unitaire du catalogue Mrel
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
class MrelCatalogTest {

  @BeforeEach
  void setUp() {
    MrelCatalog.getCatalog().clear();
    MrelCatalog.getClassIriTableId().clear();
    MrelCatalog.getOntoAxiomRelvarMap().clear();
  }

  @Test
  void testGetCatalog() {
    assertNotNull(MrelCatalog.getCatalog());
    assertTrue(MrelCatalog.getCatalog().isEmpty());
  }

  @Test
  void testGetClassIriTableId() {
    assertNotNull(MrelCatalog.getClassIriTableId());
    assertTrue(MrelCatalog.getClassIriTableId().isEmpty());
  }

  @Test
  void testGetOntoAxiomRelvarMap() {
    assertNotNull(MrelCatalog.getOntoAxiomRelvarMap());
    assertTrue(MrelCatalog.getOntoAxiomRelvarMap().isEmpty());
  }

  @Test
  void testGetEntityCatalog() {
    MrelEntity mockEntity = mock(MrelEntity.class);
    MrelCatalog.getCatalog().put(new TestIdentifier("C001"), mockEntity);

    Set<MrelEntity> entitySet = MrelCatalog.getEntityCatalog();
    assertEquals(1, entitySet.size());
    assertTrue(entitySet.contains(mockEntity));
  }

  @Test
  void testGetIdCatalog() {
    Identifier id1 = new TestIdentifier("C001");
    MrelCatalog.getCatalog().put(id1, mock(MrelEntity.class));

    Set<Identifier> idSet = MrelCatalog.getIdCatalog();
    assertEquals(1, idSet.size());
    assertTrue(idSet.contains(id1));
  }

  @Test
  void testPrintCatalog() {
    MrelEntity mockEntity = mock(MrelEntity.class);
    Identifier id1 = new TestIdentifier("C001");
    when(mockEntity.toString()).thenReturn("t001");
    MrelCatalog.getCatalog().put(id1, mockEntity);
    String output = MrelCatalog.printCatalog();
    assertTrue(output.contains("[C001]::t001"));
  }
}
