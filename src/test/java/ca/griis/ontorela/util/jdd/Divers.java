package ca.griis.ontorela.util.jdd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Décrire la classe ici.
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui | non (pourquoi).</li>
 * <li>Clonabilité : oui | non (pourquoi).</li>
 * <li>Modifiabilité : oui | non (pourquoi).</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2018-XX-XX (0.2.0) [XX] ... <br>
 * 2019-05-30 (0.1.0) [CK] Mise en oeuvre initiale. <br>
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
 * @since 2019-05-30
 * @version 0.1.0
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @author [LL] Luc.Lavoie@USherbrooke.ca
 */
public class Divers {
  @Test
  public void testMapSetValue() {
    Map<Integer, Set<String>> reductionResult = new LinkedHashMap<>();
    Integer i0 = 0;
    Set<String> reducedAxiomSet = new LinkedHashSet<>();
    reductionResult.put(i0, reducedAxiomSet);
    assertTrue(reductionResult.get(i0).isEmpty());
    System.out.println("Vide : " + reductionResult);
    String truc = "s1";
    reducedAxiomSet.add(truc);
    reductionResult.get(i0).add(truc);
    assertTrue(reductionResult.get(i0).size() == 1);
    System.out.println("Un : " + reductionResult);
    Set<String> choses = new LinkedHashSet<>();
    choses.add("s2");
    choses.add("s3");
    reductionResult.get(i0).addAll(choses);
    System.out.println("Trois : " + reductionResult);
    assertTrue(reductionResult.get(i0).size() == 3);
    choses.remove("s3");
    System.out.println("Deux : " + reductionResult);
    assertTrue(reductionResult.get(i0).size() == 3);
  }
  // **************************************************************************
  // Attributs spécifiques
  //
  // **************************************************************************
  // Constructeurs
  //
  // **************************************************************************
  // Opérations propres
  //
  // **************************************************************************
  // Opérations publiques
  //
}
