package ca.griis.ontorela.unit.converter.essais;

import ca.griis.ontorela.mrel.ForeignKey;
import ca.griis.ontorela.mrel.Table;
import java.util.Set;

/**
 * méthodes utils pour les tests
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
public class Util {
  protected static boolean comparerTable(Set<Table> tableOntorel, Set<Table> tableAttendu) {
    if (tableOntorel.size() != tableAttendu.size()) {
      return false;
    }
    for (Table table : tableOntorel) {
      boolean existe = false;
      for (Table table1 : tableAttendu) {
        if (table1.equals(table)) {
          existe = true;
          break;
        }
      }
      if (!existe) {
        System.out.println("table manquante : " + table.getIri());
        return false;
      }
    }
    return true;
  }

  protected static boolean comparerKey(Set<ForeignKey> fkOntorel, Set<ForeignKey> fkAttendu) {
    if (fkOntorel.size() != fkAttendu.size()) {
      return false;
    }
    for (ForeignKey fk : fkOntorel) {
      boolean existe = false;
      for (ForeignKey fk1 : fkAttendu) {
        if (fk1.equals(fk)) {
          existe = true;
          break;
        }
      }
      if (!existe) {
        System.out.println("FK manquante : " + fk.getFkId());
        return false;
      }
    }
    return true;
  }

}
