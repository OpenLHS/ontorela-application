package ca.griis.ontorela.configuration;

/**
 * Enumeration des types PostgreSQL.<br>
 * References :
 * <ul>
 * <li>https://www.postgresql.org/docs/9.6/static/datatype.html</li>
 * <li>https://www.postgresql.org/docs/9.6/static/rangetypes.html</li>
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
 * 2018-07-12 (0.1.1) [CK] : Revue et documentation. <br>
 * 2018-03-14 (0.1.0) [BK] : Création. <br>
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
 * @author [BK] Blaisie.Kampire@USherbrooke.ca
 * @author [CK] Christina.Khnaisser@USherbrooke.ca
 * @version 0.1.0
 * @since 2018-03-14
 */
public enum PostgresDatatypeEnum {
  /**
   * logical Boolean (true/false).
   */
  BOOLEAN,
  // ===================================== Numbers
  /**
   * signed four-byte integer.
   */
  INTEGER("int"),
  /**
   * signed two-byte integer.
   */
  SMALLINT("int2"),
  /**
   * signed eight-byte integer.
   */
  BIGINT("int8"),
  /**
   * double precision floating-point number (8 bytes).
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
   * fixed-length bit string.
   */
  BIT,
  /**
   * variable-length bit string.
   */
  VARBIT("bit varying"),
  /**
   * binary data ("byte array").
   */
  BYTEA,
  // ===================================== Serial
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
  TIME,
  /**
   * time of day, including time zone.
   */
  TIMEZ,
  /**
   * date and time (no time zone).
   */
  TIMESTAMP,
  /**
   * date and time, including time zone.
   */
  TIMESTAMPZ,
  /**
   * time span.
   */
  INTERVAL,
  // ===================================== Geo
  /**
   * circle on a plane.
   */
  CIRCLE,
  /**
   * rectangular box on a plane.
   */
  BOX,
  /**
   * infinite line on a plane.
   */
  LINE,
  /**
   * geometric path on a plane.
   */
  PATH,
  /**
   * PostgreSQL Log Sequence Number.
   */
  PG_LSN,
  /**
   * geometric point on a plane.
   */
  POINT,
  /**
   * line segment on a plane.
   */
  LSEG,
  /**
   * closed geometric path on a plane.
   */
  POLYGON,
  /**
   * currency amount.
   */
  MONEY,
  /**
   * MAC (Media Access Control) address.
   */
  MACADDR,
  /**
   * text search query.
   */
  TSQUERY,
  /**
   * text search document.
   */
  TSVECTOR,
  // ===================================== Intervals
  /**
   * Range of integer.
   */
  INT4RANGE("int4range "),
  /**
   * Range of bigint.
   */
  INT8RANGE("int8range"),
  /**
   * Range of numeric.
   */
  NUMRANG("numrang"),
  /**
   * Range of timestamp without time zone.
   */
  TSRANGE("tsrange"),
  /**
   * Range of timestamp with time zone.
   */
  TSTZRANGE("tstzrange"),
  /**
   * Range of date.
   */
  DATERANGE("daterange"),
  // ===================================== Other
  /**
   * textual JSON data.
   */
  JSON,
  /**
   * binary JSON data, decomposed.
   */
  JSONB,
  /**
   * universally unique identifier.
   */
  UUID,
  /**
   * XML data.
   */
  XML("xml"),
  /**
   * IPv4 or IPv6 network address.
   */
  CIDR,
  /**
   * IPv4 or IPv6 host address.
   */
  INET;

  private String alias;

  PostgresDatatypeEnum() {}

  PostgresDatatypeEnum(String alias) {
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
