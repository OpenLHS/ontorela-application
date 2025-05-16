package ca.griis.ontorela.unit.converter;

import static org.junit.jupiter.api.Assertions.*;

import ca.griis.ontorela.converter.OntoRelScriptGenerator;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OntorelSqlGeneratorTest {

  @Mock
  private OntoRelScriptGenerator generator;



  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateScript() {
    List<File> scripts = generator.generateScripts("build/test-results/generator");
    assertNotNull(scripts);
  }
}
