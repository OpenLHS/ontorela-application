package ca.griis.ontorela.unit.catalog;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.griis.monto.api.*;
import ca.griis.monto.builder.owlapi.OntologyOwlApiBuilder;
import ca.griis.monto.model.OntoEntitySet;
import ca.griis.monto.model.OntoIri;
import ca.griis.monto.model.OntoObjectProperty;
import ca.griis.monto.model.Participation;
import ca.griis.monto.util.Descriptor;
import ca.griis.ontorela.catalog.OntoRelCat;
import ca.griis.ontorela.configuration.ConfigurationLoader;
import ca.griis.ontorela.configuration.DatabaseConfiguration;
import ca.griis.ontorela.converter.OntoRel;
import ca.griis.ontorela.exception.OntorelCreationException;
import ca.griis.ontorela.mrel.*;
import ca.griis.ontorela.mrel.catalog.Identifier;
import ca.griis.ontorela.util.jdd.ConstructeurJdd;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.junit.jupiter.api.*;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/**
 * Tests unitaires pour OntoRelCat
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-01-30 (0.1.0) [CK] Mise en oeuvre initiale. <br>
 *
 * <p>
 * <b>Copyright</b> 2016-2017, GRIIS (https://griis.ca/) <br>
 * GRIIS (Groupe de recherche interdisciplinaire en informatique de la santé) <br>
 * Faculté des sciences et Faculté de médecine et sciences de la santé <br>
 * Université de Sherbrooke (Québec) J1K 2R1 <br>
 * CANADA <br>
 * [CC-BY-NC-3.0 (http://creativecommons.org/licenses/by-nc/3.0)]
 * </p>
 *
 * @since 2019-01-30
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */
public class OntoRelCatTest {
  // **************************************************************************
  // Attributs spécifiques
  //
  protected static final Descriptor d = new Descriptor();
  private static final Set<OntoRel> ontoRelSet = new HashSet<>();

  private OntoRelCat ontoRelCat;
  private OntologyI ontologyMock;
  private Database dbMock;
  // **************************************************************************
  // Constructeurs
  //


  @BeforeEach
  void setUp() {

    ontoRelCat = new OntoRelCat();
    ontologyMock = mock(OntologyI.class);
    dbMock = mock(Database.class);
  }

  @BeforeAll
  public static void initJdd() throws OntorelCreationException, OWLOntologyCreationException {
    d.titre("Test unitaire " + OntoRelCatTest.class.getName());
    //
    String repo = "test-data/ontoRel/";
    for (File jdd : ConstructeurJdd.creationJddParRepo(repo)) {
      List<File> yamlFiles = ConstructeurJdd.getYamlConfigurationFiles(jdd);
      if (yamlFiles.size() == 2) {
        OntologyOwlApiBuilder ontoBuilder =
            new OntologyOwlApiBuilder(ConstructeurJdd.getOntologyConfigurationFiles(jdd).get(0));
        DatabaseConfiguration dbConfig = ConfigurationLoader
            .loadDatabaseConfiguration(ConstructeurJdd.getDatabaseConfigurationFiles(jdd).get(0));
        OntoRel ontoRel = new OntoRel(ontoBuilder.getOntology(), dbConfig);
        ontoRel.buildOntoRel();
        assertNotNull(ontoRel);
        ontoRelSet.add(ontoRel);
      } else {
        System.err.println("DataSet directory : " + jdd + " must contain 2 yamlFile");
      }
    }
  }

  @AfterAll
  public static void finTest() {
    System.out.println(d);
  }

  @AfterEach
  public void cleanUpGeneratedFiles() {
    for (OntoRel ontoRel : ontoRelSet) {
      String directoryPath =
          "build/test-results/ontoRelDic/" + ontoRel.getOntology().getAlias() + "/";
      File reportFile = new File(directoryPath + "OntoRelCat");
      File jsonFile = new File(directoryPath + "OntoRelDic.json");
      deleteFile(reportFile);
      deleteFile(jsonFile);
    }
  }

  private void deleteFile(File file) {
    if (file.exists()) {
      boolean deleted = file.delete();
      if (!deleted) {
        System.err.println(" Impossible de supprimer le fichier : " + file.getAbsolutePath());
      }
    }
  }


  // **************************************************************************
  // Cas de test
  //
  @Test
  public void testGenerateOntoRelJson() throws IOException {
    for (OntoRel ontoRel : ontoRelSet) {
      d.soustitre("Json for: " + ontoRel.getOntology().getIri().getFullIri());
      assertNotNull(ontoRel.getOntoRelDic());
      assertTrue(ontoRel.getOntoRelDic().getClassTableCatalog().size() > 1);
      ontoRel.getOntoRelDic().generateJson(ontoRel.getOntology(), ontoRel.getDatabase(),
          "build/test-results/ontoRelDic/" + ontoRel.getOntology().getAlias() + "/");
    }
  }

  @Test
  public void testGenerateOntoRelCatReport() {
    for (OntoRel ontoRel : ontoRelSet) {
      d.soustitre("Json for: " + ontoRel.getOntology().getIri().getFullIri());
      assertNotNull(ontoRel.getOntoRelDic());
      assertTrue(ontoRel.getOntoRelDic().getClassTableCatalog().size() > 1);
      ontoRel.getOntoRelDic().generateOntoRelCatReport(
          "build/test-results/ontoRelDic/" + ontoRel.getOntology().getAlias() + "/");
    }
  }

  @Test
  public void testGenerateOntoRelCatReport_NullDirectoryPath() {
    for (OntoRel ontoRel : ontoRelSet) {
      d.soustitre("Json for: " + ontoRel.getOntology().getIri().getFullIri());
      assertNotNull(ontoRel.getOntoRelDic());
      assertTrue(ontoRel.getOntoRelDic().getClassTableCatalog().size() > 1);
      assertThrows(NullPointerException.class,
          () -> ontoRel.getOntoRelDic().generateOntoRelCatReport(null));
    }
  }



  @Test
  public void testGenerateOntoRelCatReport_DirectoryDoesNotExist() {
    for (OntoRel ontoRel : ontoRelSet) {
      d.soustitre("Json for: " + ontoRel.getOntology().getIri().getFullIri());
      assertNotNull(ontoRel.getOntoRelDic());
      assertTrue(ontoRel.getOntoRelDic().getClassTableCatalog().size() > 1);
      String directoryPath = "build/test-results/nonExistentDir/";
      File directory = new File(directoryPath);
      if (directory.exists()) {
        directory.delete();
      }
      ontoRel.getOntoRelDic().generateOntoRelCatReport(directoryPath);
      assertTrue(directory.exists(), "Le dossier n'a pas été créé");
    }
  }

  @Test
  public void testGenerateOntoRelCatReport_CreatesFile() {
    OntoRelCat ontoRelCat = new OntoRelCat();
    String directoryPath = "build/test-results/ontoRelDic/";
    File file = ontoRelCat.generateOntoRelCatReport(directoryPath);
    assertNotNull(file, "Le fichier ne doit pas être null");
    assertTrue(file.exists(), "Le fichier généré doit exister");
  }

  @Test
  public void testGetAxiomTableCatalog() {
    OntoRelCat ontoRelCat = new OntoRelCat();
    OntoAxiomAssociationI mockAxiom = mock(OntoAxiomAssociationI.class);
    Table mockTable = mock(Table.class);
    ontoRelCat.addAxiomTableCatalogEntry(mockAxiom, mockTable);
    assertFalse(ontoRelCat.getAxiomTableCatalog().isEmpty(),
        "getAxiomTableCatalog doit paas être vide");
  }

  @Test
  public void testGetClassAxiomSetRelStructure() {
    OntoRelCat ontoRelCat = new OntoRelCat();
    OntoClassI domainClass = mock(OntoClassI.class);
    OntoAxiomClassAssociationI mockAxiom = mock(OntoAxiomClassAssociationI.class);
    Table mockTable = mock(Table.class);
    when(mockAxiom.getOntoDeterminant()).thenReturn(domainClass);
    ontoRelCat.addAxiomTableCatalogEntry(mockAxiom, mockTable);
    assertFalse(ontoRelCat.getClassAxiomSetRelStructure(domainClass).isEmpty(),
        "getClassAxiomSetRelStructure ne doit pas être vide");
  }

  @Test
  public void testGetDataAxiomSetRelStructure() {
    OntoRelCat ontoRelCat = new OntoRelCat();
    OntoClassI domainClass = mock(OntoClassI.class);
    OntoAxiomDataAssociationI mockAxiom = mock(OntoAxiomDataAssociationI.class);
    Table mockTable = mock(Table.class);
    when(mockAxiom.getOntoDeterminant()).thenReturn(domainClass);
    ontoRelCat.addAxiomTableCatalogEntry(mockAxiom, mockTable);
    assertFalse(ontoRelCat.getDataAxiomSetRelStructure(domainClass).isEmpty(),
        "getDataAxiomSetRelStructure ne doit pas être vide");
  }

  @Test
  public void testGenerateOntoRelCatReport_FileExists() {
    String directoryPath = "build/test-results/ontoRelDic/";
    File reportFile = new File(directoryPath + "OntoRelCat.txt");

    try {
      reportFile.getParentFile().mkdirs();
      reportFile.createNewFile();
    } catch (IOException e) {
      fail("Impossible de créer un fichier de test");
    }
    File generatedFile = ontoRelCat.generateOntoRelCatReport(directoryPath);
    assertNotNull(generatedFile);
    assertTrue(generatedFile.exists());
    assertTrue(generatedFile.length() > 0);
  }

  @Test
  public void testGenerateOntoRelCatReport_FileExistsIT() {
    for (OntoRel ontoRel : ontoRelSet) {
      d.soustitre("Json for: " + ontoRel.getOntology().getIri().getFullIri());
      assertNotNull(ontoRel.getOntoRelDic());
      assertTrue(ontoRel.getOntoRelDic().getClassTableCatalog().size() > 1);
      String directoryPath =
          "build/test-results/ontoRelDic/" + ontoRel.getOntology().getAlias() + "/";
      File reportFile = new File(directoryPath + "OntoRelCat.txt");
      ontoRel.getOntoRelDic().generateOntoRelCatReport(directoryPath);
      assertTrue(reportFile.exists(), "Le fichier n'a pas été généré");
      assertTrue(reportFile.length() > 0, "Le fichier généré est vide");
    }
  }

  @Test
  public void testGenerateJson_CreatesFile() throws IOException, InvocationTargetException,
      NoSuchMethodException, IllegalAccessException, InstantiationException {
    OntoRelCat ontoRelCat = new OntoRelCat();
    OntologyI mockOntology = mock(OntologyI.class);
    Database mockDatabase = mock(Database.class);
    OntoClassI mockClass = mock(OntoClassI.class);
    OntoClassI mockClass1 = mock(OntoClassI.class);
    OntoClassI domainClass = mock(OntoClassI.class);
    OntoClassI rangeClass = mock(OntoClassI.class);
    OntoDatatypeI mockDatatype = mock(OntoDatatypeI.class);
    OntoIri mockOntoIri = mock(OntoIri.class);
    OntoIri mockOntoIridp = mock(OntoIri.class);
    OntoIri mockIri = mock(OntoIri.class);
    OntoObjectProperty mocObjectProperty = mock(OntoObjectProperty.class);
    OntoAxiomClassAssociationI mockClassAxiom = mock(OntoAxiomClassAssociationI.class);

    Table mocktable = mock(Table.class);
    Table mocktable1 = mock(Table.class);
    Table mockdptable = mock(Table.class);
    Table mockDomaintable = mock(Table.class);
    Table mockRangetable = mock(Table.class);
    TableJoin mockTableJoin = mock(TableJoin.class);
    TableJoin mockTableAxiom = mock(TableJoin.class);

    OntoAnnotationCollectionI mockAnnotations = mock(OntoAnnotationCollectionI.class);
    OntoEntityCollectionI<OntoDataPropertyI> mockDataProperties = mock(OntoEntityCollectionI.class);
    OntoDataPropertyI mockDataProperty = mock(OntoDataPropertyI.class);
    OntoAnnotationCollectionI mockDataAnnotations = mock(OntoAnnotationCollectionI.class);
    OntoEntityCollectionI<OntoClassI> mockDomainCollection = new OntoEntitySet<>();
    OntoEntityCollectionI<OntoClassI> mockRangeClassCollection = new OntoEntitySet<>();
    OntoEntityCollectionI<OntoDatatypeI> mockRangeCollection = new OntoEntitySet<>();


    String directoryPath = "build/tmp/ontoRelDic/";
    Schema mockSchema = mock(Schema.class);
    when(mockSchema.getName()).thenReturn("ABC");
    when(mockOntology.getIri()).thenReturn(mock(OntoIri.class));
    when(mockOntology.getIri().getFullIri()).thenReturn("http://ABC");
    when(mockOntology.getAnnotations()).thenReturn(mockAnnotations);
    when(mockDatabase.getBaseSchema()).thenReturn(mockSchema);
    // mock class
    when(mockClass.getIri()).thenReturn(mockOntoIri);
    when(mockOntoIri.getFullIri()).thenReturn("http://ABC/C00");
    when(mockClass.getOntoClassOrigin()).thenReturn(OntoClassI.OntoClassOrigin.DECLARED);
    when(mockClass.getAnnotations()).thenReturn(mockAnnotations);
    when(mocktable.getIdentifier()).thenReturn(createIdentifier("T001"));
    ontoRelCat.addClassTableCatalogEntry(mockClass, mocktable);
    // mock class1
    when(mockClass1.getIri()).thenReturn(mockOntoIri);
    when(mockOntoIri.getFullIri()).thenReturn("http://ABC/C01");
    when(mockClass1.getOntoClassOrigin()).thenReturn(OntoClassI.OntoClassOrigin.DECLARED);
    when(mockClass1.getAnnotations()).thenReturn(mockAnnotations);
    when(mocktable1.getIdentifier()).thenReturn(createIdentifier("T002"));
    ontoRelCat.addClassTableCatalogEntry(mockClass1, mocktable1);
    // mock data prop
    when(mockOntology.getOntoDataPropertieSet()).thenReturn(mockDataProperties);
    when(mockDataProperties.iterator()).thenReturn(List.of(mockDataProperty).iterator());
    when(mockDataProperty.getIri()).thenReturn(mockIri);
    when(mockIri.getFullIri()).thenReturn("http://ABC/dp");
    when(mockDataProperty.getAnnotations()).thenReturn(mockDataAnnotations);
    when(mockdptable.getIdentifier()).thenReturn(createIdentifier("T002"));
    // mock object prop
    when(mocObjectProperty.getIri()).thenReturn(new OntoIri("http://ABC/op001"));
    when(mocObjectProperty.getAnnotations()).thenReturn(mockAnnotations);
    when(domainClass.getIri()).thenReturn(new OntoIri("http://ABC/C01"));
    when(rangeClass.getIri()).thenReturn(new OntoIri("http://ABC/C02"));
    mockDomainCollection.add(domainClass);
    mockRangeClassCollection.add(rangeClass);
    when(mocObjectProperty.getDomain()).thenReturn(mockDomainCollection);
    when(mocObjectProperty.getRange()).thenReturn(mockRangeClassCollection);
    when(mockTableJoin.getIdentifier()).thenReturn(createIdentifier("T003"));
    ontoRelCat.addOntoObjectPropertyTableCatalogEntry(mocObjectProperty, mockTableJoin);
    when(mockClass.getIri()).thenReturn(mockOntoIri);
    mockDomainCollection.add(mockClass);
    when(mockDataProperty.getDomain()).thenReturn(mockDomainCollection);

    when(domainClass.getOntoClassOrigin()).thenReturn(OntoClassI.OntoClassOrigin.DECLARED);
    when(domainClass.getAnnotations()).thenReturn(mockAnnotations);
    ontoRelCat.addClassTableCatalogEntry(domainClass, mockDomaintable);

    when(rangeClass.getOntoClassOrigin()).thenReturn(OntoClassI.OntoClassOrigin.DECLARED);
    when(rangeClass.getAnnotations()).thenReturn(mockAnnotations);
    ontoRelCat.addClassTableCatalogEntry(rangeClass, mockRangetable);

    // mock Axiome
    when(mockClassAxiom.getOntoAxiomString()).thenReturn("C01::OP01::C02");
    when(mockClassAxiom.getAxiomOrigin()).thenReturn(OntoAxiomI.OntoAxiomOrigin.DECLARED);
    when(mockClassAxiom.getOntoDeterminant()).thenReturn(domainClass);
    when(mockClassAxiom.getOntoDependent()).thenReturn(rangeClass);
    when(mockClassAxiom.getProperty()).thenReturn(mocObjectProperty);
    when(mockClassAxiom.getParticipation()).thenReturn(new Participation(1, 5));

    when(mockTableAxiom.getIdentifier()).thenReturn(createIdentifier("T005"));

    when(mockDomaintable.getIdentifier()).thenReturn(createIdentifier("T001"));
    when(mockRangetable.getIdentifier()).thenReturn(createIdentifier("T002"));
    when(mockTableAxiom.getLeftTable()).thenReturn(mockDomaintable);
    when(mockTableAxiom.getRightTable()).thenReturn(mockRangetable);
    ontoRelCat.addAxiomTableCatalogEntry(mockClassAxiom, mockTableAxiom);

    mockRangeCollection.add(mockDatatype);
    when(mockDatatype.getIri()).thenReturn(mockOntoIridp);
    when(mockOntoIridp.getFullIri()).thenReturn("http://ABC/dtype");
    when(mockDataProperty.getRange()).thenReturn(mockRangeCollection);
    when(mockOntology.getOntoDataPropertieSet()).thenReturn(mockDataProperties);
    when(mockDataProperties.iterator()).thenReturn(List.of(mockDataProperty).iterator());

    File file = ontoRelCat.generateJson(mockOntology, mockDatabase, directoryPath);

    assertNotNull(file, "Le fichier JSON ne doit pas être null");
    assertTrue(file.exists(), "Le fichier JSON généré doit exister");

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(file);
    assertEquals("http://ABC", jsonNode.get("OntoRel").get("ontologyIri").asText());
    assertEquals("ABC", jsonNode.get("OntoRel").get("dbBaseSchemaId").asText());
    assertTrue(jsonNode.get("OntoRel").has("label"));
    JsonNode labelArray = jsonNode.get("label");
    assertNull(labelArray);
    // assert Class
    assertTrue(jsonNode.has("Classes"));
    JsonNode classesArray = jsonNode.get("Classes");
    assertEquals(4, classesArray.size());
    JsonNode classNode = classesArray.get(0);
    assertEquals("http://ABC/C01", classNode.get("iri").asText());
    assertEquals("DECLARED", classNode.get("origin").asText());
    assertEquals("T001", classNode.get("tableId").asText());
    // assert DataProperties
    assertTrue(jsonNode.has("DataProperties"));
    JsonNode dataPropertiesArray = jsonNode.get("DataProperties");
    assertEquals(1, dataPropertiesArray.size());
    JsonNode dpNode = dataPropertiesArray.get(0);
    assertEquals("http://ABC/dp", dpNode.get("iri").asText());
    assertEquals("http://ABC/dtype", dpNode.get("rangeDataTypeIri").asText());
    assertEquals("http://ABC/C01", dpNode.get("domainClassIri").get(0).asText());
    assertEquals("http://ABC/C01", dpNode.get("domainClassIri").get(1).asText());
    // assert ObjectProperties
    assertTrue(jsonNode.has("ObjectProperties"));
    JsonNode objectPropertyArray = jsonNode.get("ObjectProperties");
    assertEquals(1, objectPropertyArray.size());
    JsonNode opNode = objectPropertyArray.get(0);
    assertEquals("http://ABC/op001", opNode.get("iri").asText());
    assertEquals("T003", opNode.get("tableId").asText());
    assertTrue(opNode.has("domainClassIri"));
    JsonNode domainNode = opNode.get("domainClassIri");
    assertEquals("http://ABC/C01", domainNode.get(0).asText());
    assertEquals("http://ABC/C01", domainNode.get(1).asText());
    JsonNode rangeNode = opNode.get("rangeClassIri");
    assertEquals("http://ABC/C02", rangeNode.get(0).asText());
    // axiome d'heritage
    ForeignKey mockIsa = mock(ForeignKey.class);
    when(mockIsa.getForeignKeyType()).thenReturn(ForeignKey.ForeignKeyType.ISA);
    when(mockIsa.getOrigin()).thenReturn(mocktable);
    when(mockIsa.getDestination()).thenReturn(mocktable1);
    // association de classe

  }

  @Test
  void testGenerateJson_Exception() {
    assertThrows(IllegalArgumentException.class,
        () -> ontoRelCat.generateJson(null, dbMock, "build/test-results/ontoRelDic/"));
    assertThrows(IllegalArgumentException.class,
        () -> ontoRelCat.generateJson(ontologyMock, null, "build/test-results/ontoRelDic/"));
    assertThrows(IllegalArgumentException.class,
        () -> ontoRelCat.generateJson(ontologyMock, dbMock, null));
  }

  @Test
  public void testToString() {
    String toStringResult = ontoRelCat.toString();
    assertNotNull(toStringResult);
    assertFalse(toStringResult.isEmpty());
    assertTrue(toStringResult.contains("OntoRelCat"));
    assertTrue(toStringResult.contains("# type-table:"));
    assertTrue(toStringResult.contains("# class-table:"));
    assertTrue(toStringResult.contains("# dataAxiom-table:"));
    assertTrue(toStringResult.contains("# classAxiom-table:"));
  }



  // **************************************************************************
  // Opérations propres
  //
  private Identifier createIdentifier(String value)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
      InstantiationException, InvocationTargetException {
    Constructor<Identifier> constructor = Identifier.class.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    return constructor.newInstance(value);
  }
  // **************************************************************************
  // Opérations publiques
  //
}
