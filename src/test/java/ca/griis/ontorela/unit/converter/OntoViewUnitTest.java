package ca.griis.ontorela.unit.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.griis.monto.api.OntoAxiomAssociationI;
import ca.griis.monto.api.OntoClassI;
import ca.griis.monto.api.OntoDatatypeI;
import ca.griis.monto.model.OntoIri;
import ca.griis.ontorela.converter.OntoView;
import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.Table;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * "Description brève du composant (classe, interface, ...)"
 *
 * <h3>Historique</h3>
 * <p>
 * 2025-04-04 [AS] - Implémentation initiale<br>
 * </p>
 *
 * <h3>Tâches</h3>
 * S.O.
 *
 * @author AS
 * @since
 */
public class OntoViewUnitTest {

  private OntoClassI mockClass;
  private Table mockTable;
  private OntoAxiomAssociationI mockAxiom;
  private OntoDatatypeI mockDatatype;
  private Table mockDatatypeTable;
  private ForeignKey mockForeignKey;

  @BeforeEach
  public void setUp() {
    mockClass = mock(OntoClassI.class);
    mockTable = mock(Table.class);
    mockAxiom = mock(OntoAxiomAssociationI.class);
    mockDatatype = mock(OntoDatatypeI.class);
    mockDatatypeTable = mock(Table.class);
    mockForeignKey = mock(ForeignKey.class);

    OntoIri mockIRI = mock(OntoIri.class);

    when(mockClass.getIri()).thenReturn(mockIRI);
    when(mockIRI.getShortIri()).thenReturn("PDRO_0000133");
  }

  @Test
  public void testConstructor() {
    Map<OntoAxiomAssociationI, Table> axiomMap = new HashMap<>();
    axiomMap.put(mockAxiom, mockTable);

    Map<OntoDatatypeI, Table> datatypeMap = new HashMap<>();
    datatypeMap.put(mockDatatype, mockDatatypeTable);

    Set<ForeignKey> fkSet = new HashSet<>();
    fkSet.add(mockForeignKey);

    OntoView view = new OntoView(OntoView.OntoViewType.PROPERVIEW, mockClass, mockTable, axiomMap,
        datatypeMap, fkSet);

    assertEquals("PDRO_0000133_p", view.getViewId());
    assertEquals(OntoView.OntoViewType.PROPERVIEW, view.getOntoViewType());
    assertEquals(mockClass, view.getClassSource());
    assertEquals(mockTable, view.getTableSource());
    assertEquals(axiomMap, view.getAxiomTableMap());
    assertEquals(datatypeMap, view.getTypeTableMap());
    assertEquals(fkSet, view.getAxiomForeignKeySet());
  }

  @Test
  public void testGetDescription() {
    Map<OntoAxiomAssociationI, Table> axiomMap = new HashMap<>();
    axiomMap.put(mockAxiom, mockTable);

    Map<OntoDatatypeI, Table> datatypeMap = new HashMap<>();
    datatypeMap.put(mockDatatype, mockDatatypeTable);

    when(mockTable.getDescription("fr")).thenReturn("la table PDRO_0000133");
    when(mockDatatypeTable.getDescription("fr")).thenReturn("Datatype");

    OntoView view = new OntoView(OntoView.OntoViewType.PROPERVIEW, mockClass, mockTable, axiomMap,
        datatypeMap, new HashSet<>());
    String description = view.getDescription("fr");

    assertTrue(description.contains("PDRO_0000133"));
    assertTrue(description.contains("Datatype"));
  }

  @Test
  public void testToString() {
    Map<OntoAxiomAssociationI, Table> axiomMap = new HashMap<>();
    axiomMap.put(mockAxiom, mockTable);

    Map<OntoDatatypeI, Table> datatypeMap = new HashMap<>();
    datatypeMap.put(mockDatatype, mockDatatypeTable);

    OntoView view = new OntoView(OntoView.OntoViewType.PROPERVIEW, mockClass, mockTable, axiomMap,
        datatypeMap, new HashSet<>());
    String result = view.toString();

    assertTrue(result.contains("classSource="));
    assertTrue(result.contains("tableSource="));
    assertTrue(result.contains("joinTables="));
  }
}
