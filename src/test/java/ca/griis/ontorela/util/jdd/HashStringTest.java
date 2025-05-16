/*
 * Copyright (c) 2024. Metis
 */

package ca.griis.ontorela.util.jdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class HashStringTest {

  @Test
  public void stringHashTest() {
    String s1 = "BB";
    String s2 = "Aa";
    assertNotEquals(getHash(s1), getHash(s2));
    assertEquals(s1.hashCode(), s2.hashCode());
    String hasPart = "http://purl.obolibrary.org/obo/BFO_0000051";
    String hasSubPart = "http://purl.obolibrary.org/obo/BFO_0000052";
    String cityA = "f://m#CityAa";
    String cityB = "f://m#CityBB";
    assertNotEquals(getHash(cityA), getHash(cityB));
    assertNotEquals(getHash(hasPart), getHash(hasSubPart));
  }

  private String getHash(String s) {
    byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
    StringBuilder b = new StringBuilder();
    for (int i = bytes.length - 1; i >= 0; i--) {
      String hex = Integer.toHexString(bytes[i]);
      if (hex.length() == 1) {
        b.append('0');
      }
      b.append(hex);
    }
    return String.format("%1$-" + 29 + "s", b).replace(' ', '0').substring(0, 29);
  }
}
