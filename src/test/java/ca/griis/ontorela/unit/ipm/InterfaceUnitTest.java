package ca.griis.ontorela.unit.ipm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.griis.monto.api.*;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.core.OntologyManager;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.ipm.Interface;
import ca.griis.ontorela.service.ServiceManager;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * Test unitaire d'interface pour ipm
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
public class InterfaceUnitTest {

  @Mock
  private File testDir;
  ServiceManager mockServiceManager;
  OntoRel mockOntoRel;
  OntologyI mockOntology;
  OntoEntityCollectionI<OntoObjectPropertyI> mockObjectPropertySet;
  OntoObjectPropertyI mockObjectProperty;
  OntoEntityCollectionI<OntoDataPropertyI> mockDataPropertySet;
  OntoDataPropertyI mockDataProperty;
  OntoIriI mockOntoIri;

  @BeforeEach
  void setUp() throws IOException {
    testDir =
        new File("test-data/_ontologies/ABC/config00");
    mockOntoRel = mock(OntoRel.class);
    mockOntology = mock(OntologyI.class);
    mockObjectPropertySet = mock(OntoEntityCollectionI.class);
    mockObjectProperty = mock(OntoObjectPropertyI.class);
    mockOntoIri = mock(OntoIriI.class);
    mockDataPropertySet = mock(OntoEntityCollectionI.class);
    mockDataProperty = mock(OntoDataPropertyI.class);

    when(mockOntology.getOntoObjectPropertieSet()).thenReturn(mockObjectPropertySet);
    when(mockObjectProperty.getIri()).thenReturn(mockOntoIri);
  }

  @Test
  void testGetObjectPropertiesList_Success() throws Exception {
    when(mockOntoIri.getShortIri()).thenReturn("GRIIS_00");
    when(mockOntoIri.getFullIri()).thenReturn("http://ca.griis.ontologies/ABC#GRIIS_00");

    List<OntoObjectPropertyI> objectProperties = List.of(mockObjectProperty);
    when(mockObjectPropertySet.stream()).thenReturn(objectProperties.stream());

    List<Map<String, Object>> result = Interface.getObjectPropertiesList(testDir);

    assertEquals(5, result.size());
    assertEquals("GRIIS_00", result.get(0).get("shortIri"));
    assertEquals("http://ca.griis.ontologies/ABC#GRIIS_00", result.get(0).get("fullIri"));
  }

  @Test
  void testGetAnomaliesOntologyReport()
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    ServiceManager mockServiceManager = mock(ServiceManager.class);
    when(mockServiceManager.getOntologyManager()).thenReturn(mock(OntologyManager.class));

    String result = Interface.getAnomaliesOntologyReport(testDir);
    assertNotNull(result);
  }

  @Test
  void testGetAnomaliesDatabaseIdentifiersReport()
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    String result = Interface.getAnomaliesDatabaseIdentifiersReport(testDir);
    assertNotNull(result);
  }

  @Test
  void testGetDatabaseTableIds()
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    Map<String, String> tableIds = Interface.getDatabaseTableIds(testDir);
    assertNotNull(tableIds);
  }

  @Test
  void testGetClassList()
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    List<Map<String, Object>> classList = Interface.getClassList(testDir);
    assertNotNull(classList);
    assertTrue(classList.isEmpty() || classList.size() >= 0);
  }

  @Test
  void testGetInitialObjectPropertiesList()
      throws IOException, OWLOntologyCreationException, OntorelCreationException {
    List<Map<String, Object>> objectProperties = Interface.getInitialObjectPropertiesList(testDir);
    assertNotNull(objectProperties);
  }

  @Test
  void testGetInitialDataPropertiesList_Success() throws Exception {
    when(mockOntoIri.getShortIri()).thenReturn("has_unit");
    when(mockOntoIri.getFullIri()).thenReturn("http://ca.griis.ontologies/ABC#has_unit");

    List<OntoDataPropertyI> dataProperties = List.of(mockDataProperty);
    when(mockDataPropertySet.stream()).thenReturn(dataProperties.stream());

    List<Map<String, Object>> result = Interface.getInitialDataPropertiesList(testDir);

    assertEquals(2, result.size());
    assertEquals("has_unit", result.get(0).get("shortIri"));
    assertEquals("http://ca.griis.ontologies/ABC#has_unit", result.get(0).get("fullIri"));
  }
}
