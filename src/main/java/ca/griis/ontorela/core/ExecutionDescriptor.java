package ca.griis.ontorela.core;

import ca.griis.monto.util.Descriptor;

/**
 * Fournis les logs du descripteur pour le README généré
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

public class ExecutionDescriptor {
  private final Descriptor msg;
  private final String dateString;

  public ExecutionDescriptor(Descriptor msg, String dateString) {
    this.msg = msg;
    this.dateString = dateString;
  }

  public Descriptor getMsg() {
    return msg;
  }

  public String getDateString() {
    return dateString;
  }
}
