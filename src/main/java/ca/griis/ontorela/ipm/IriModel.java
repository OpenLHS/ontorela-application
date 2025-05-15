package ca.griis.ontorela.ipm;

public class IriModel {
  private String shortIri;
  private String fullIri;
  private String id;

  public String getFullIri() {
    return fullIri;
  }

  public String getShortIri() {
    return shortIri;
  }

  public void setShortIri(String shortIri) {
    this.shortIri = shortIri;
  }

  public void setFullIri(String fullIri) {
    this.fullIri = fullIri;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
