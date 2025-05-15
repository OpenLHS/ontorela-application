package ca.griis.ontorela.unit.ipm;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.ontorela.ipm.IriModel;
import ca.griis.ontorela.ipm.SubIriModel;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test unitaire d'subIri mpdel pour ipm
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
public class SubIriModelTest {

  private SubIriModel subIriModel;
  private IriModel upperClass;
  private List<IriModel> subClasses;

  @BeforeEach
  void setUp() {
    subIriModel = new SubIriModel();
    upperClass = new IriModel();
    upperClass.setShortIri("PDRO_0000133");
    upperClass.setFullIri("http://purl.obolibrary.org/obo/PDRO_0000133");
    upperClass.setId("PDRO_0000133-http://purl.obolibrary.org/obo/PDRO_0000133");

    subClasses = new ArrayList<>();
    IriModel subClass1 = new IriModel();
    subClass1.setShortIri("PDRO_0000134");
    subClass1.setFullIri("http://purl.obolibrary.org/obo/PDRO_0000134");
    subClass1.setId("PDRO_0000134-http://purl.obolibrary.org/obo/PDRO_0000134");

    IriModel subClass2 = new IriModel();
    subClass2.setShortIri("PDRO_0000135");
    subClass2.setFullIri("http://purl.obolibrary.org/obo/PDRO_0000135");
    subClass2.setId("PDRO_0000135-http://purl.obolibrary.org/obo/PDRO_0000135");

    subClasses.add(subClass1);
    subClasses.add(subClass2);
  }

  @Test
  void testSetAndGetUpperClass() {
    subIriModel.setUpperClass(upperClass);
    assertEquals(upperClass, subIriModel.getUpperClass(),
        "L'upper class doit être correctement définie et récupérée.");
  }

  @Test
  void testSetAndGetSubClasses() {
    subIriModel.setSubClasses(subClasses);
    assertEquals(subClasses, subIriModel.getSubClasses(),
        "Les sous-classes doivent être correctement définies et récupérées.");
    assertEquals(2, subIriModel.getSubClasses().size(),
        "Le nombre de sous-classes doit être correct.");
  }

  @Test
  void testDefaultValues() {
    assertNull(subIriModel.getUpperClass(), "L'upper class doit être null par défaut.");
    assertNull(subIriModel.getSubClasses(), "La liste des sous-classes doit être null par défaut.");
  }
}
