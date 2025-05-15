package ca.griis.ontorela.integration;

import org.testcontainers.containers.PostgreSQLContainer;

/**
 * "Description brève du composant (classe, interface, ...)"
 *
 * <h3>Historique</h3>
 * <p>
 * 2025-04-01 [AS] - Implémentation initiale<br>
 * </p>
 *
 * <h3>Tâches</h3>
 * S.O.
 *
 * @author AS
 * @since
 */


public class ContainerUtil {
  public static PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>("postgres:latest")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

}
