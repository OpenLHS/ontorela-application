package ca.griis.ontorela.exception;

/**
 * Exception de création d'ontorel
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

public class OntorelCreationException extends Exception {
  protected OntorelCreationException() {}

  public OntorelCreationException(String message) {
    super(message);
  }

  public OntorelCreationException(String message, Throwable cause) {
    super(message, cause);
  }

  public OntorelCreationException(Throwable cause) {
    super(cause);
  }
}
