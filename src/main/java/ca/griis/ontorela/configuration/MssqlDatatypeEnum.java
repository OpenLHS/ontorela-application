package ca.griis.ontorela.configuration;

/**
 * Enumeration des types MSSQL.<br>
 * References :
 * <ul>
 * <li>https://docs.microsoft.com/en-us/sql/t-sql/statements/create-type-transact-sql?view=sql-server-ver15</li>
 * <li>https://docs.microsoft.com/en-us/sql/t-sql/data-types/datetime-transact-sql?view=sql-server-ver15</li>
 * </ul>
 *
 * <p>
 * <b>Propriétés des objets</b>
 * <ul>
 * <li>Unicité : oui.</li>
 * <li>Clonabilité : non.</li>
 * <li>Modifiabilité : non.</li>
 * </ul>
 *
 * <b>Tâches projetées</b><br>
 * ..<br>
 *
 * <b>Tâches réalisées</b><br>
 * 2021-03-14 (0.1.0) [KB] : Création avec reference du fichier postgresql. <br>
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
 * @author [KB] Karen.Ivonne.Boisier.Aguayo@Usherbrooke.ca
 * @version 0.1.0
 * @since 2021-01-15
 */
public enum MssqlDatatypeEnum {
  /**
   * logical Boolean (true/false).
   * An integer data type that can take a value of 1, 0, or NULL.
   */
  BIT,
  // ===================================== Numbers
  /**
   * signed four-byte integer.
   */
  INT("int"),
  /**
   * signed two-byte integer.
   */
  SMALLINT("int2"),
  /**
   * signed eight-byte integer.
   */
  BIGINT("int8"),
  /**
   * double precision floating-point number (4 or 8 bytes).
   */
  FLOAT,
  /**
   * single precision floating-point number (4 bytes).
   */
  REAL,
  /**
   * exact numeric of selectable precision.
   */
  NUMERIC,
  /**
   * up to 131072 digits before the decimal point; up to 16383 digits after the decimal point
   */
  DECIMAL,
  /**
   * 15 decimal digits precision
   */
  DOUBLE_PRECISION,
  // ===================================== Text
  /**
   * fixed-length character string.
   */
  CHAR("character"),
  /**
   * variable-length character string.
   */
  VARCHAR("character varying"),
  /**
   * variable-length character string.
   */
  TEXT,
  // ===================================== Bits
  /**
   * autoincrementing two-byte integer.
   */
  SMALLSERIAL("serial2"),
  /**
   * autoincrementing four-byte integer.
   */
  SERIAL("serial4"),
  /**
   * autoincrementing eight-byte integer.
   */
  BIGSERIAL("serial8"),
  // ===================================== Date Time
  /**
   * calendar date (year, month, day).
   */
  DATE,
  /**
   * time of day (no time zone).
   */
  DATETIMEOFFSET,
  /**
   * date and time of day, including time zone.
   */
  DATETIME,
  /**
   * Defines a time of a day. The time is without time zone awareness and is based on a 24-hour
   * clock.
   */
  TIME,
  /**
   * Defines a date that is combined with a time of day that is based on 24-hour clock. datetime2
   * can
   * be considered as an extension of the existing datetime type that has a larger date range, a
   * larger default fractional precision, and optional user-specified precision.
   */
  DATETIME2,
  /**
   * date that is combined with a time of day. The time is based on a 24-hour day,
   * with seconds always zero (:00) and without fractional seconds..
   */
  SMALLDATETIME,

  // ===================================== Geo
  /**
   * collection of zero or more continuous circular arc segments.
   */
  CIRCULARSTRING,
  /**
   * collection of zero or more continuous CircularString or
   * LineString instances of either geometry or geography types
   */
  COMPOUNDCURVE,
  /**
   * one-dimensional object representing a sequence of points and the line segments connecting them.
   */
  LINESTRING,
  /**
   * collection of zero or more points.
   */
  MULTIPOINT,
  /**
   * collection of zero or more geometry or geographyLineString instances.
   */
  MULTILINESTRING,
  /**
   * geometric point on a plane.
   */
  POINT,
  /**
   * Topologically closed surface defined by an exterior bounding ring and zero or more interior
   * rings.
   */
  CURVEPOLYGON,
  /**
   * closed geometric path on a plane.
   */
  POLYGON,
  /**
   * currency amount of 4 bytes.
   */
  SMALLMONEY,
  /**
   * currency amount of 8 bytes.
   */
  MONEY,

  // ===================================== Other
  /**
   * textual JSON data.
   */
  JSON,
  /**
   * GUID
   */
  uniqueidentifier,
  /**
   * XML data.
   */
  XML("xml");

  private String alias;

  MssqlDatatypeEnum() {}

  MssqlDatatypeEnum(String alias) {
    this.alias = alias;
  }

  /**
   * Get the type alias.
   *
   * @return A string representing the type alias. The value can be null.
   */
  public String getAlias() {
    return alias;
  }
}
