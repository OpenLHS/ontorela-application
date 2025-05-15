package ca.griis.ontorela.unit.ipm;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.ontorela.ipm.IriModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire iri model pour ipm
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
public class IriModelTest {

  private IriModel iriModel;

  @BeforeEach
  void setUp() {
    iriModel = new IriModel();
  }

  @Test
  void testSetAndGetShortIri() {
    String shortIri = "PDRO_0000133";
    iriModel.setShortIri(shortIri);
    assertEquals(shortIri, iriModel.getShortIri());
  }

  @Test
  void testSetAndGetFullIri() {
    String fullIri = "http://purl.obolibrary.org/obo/PDRO_0000133";
    iriModel.setFullIri(fullIri);
    assertEquals(fullIri, iriModel.getFullIri());
  }

  @Test
  void testSetAndGetId() {
    String id = "http://purl.obolibrary.org/obo/PDRO_0000133";
    iriModel.setId(id);
    assertEquals(id, iriModel.getId());
  }

  @Test
  void testDefaultValues() {
    assertNull(iriModel.getShortIri(), "Le short IRI doit être null par défaut.");
    assertNull(iriModel.getFullIri(), "Le full IRI doit être null par défaut.");
    assertNull(iriModel.getId(), "L'ID doit être null par défaut.");
  }
}
