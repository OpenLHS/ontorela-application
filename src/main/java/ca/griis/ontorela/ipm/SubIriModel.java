package ca.griis.ontorela.ipm;

import java.util.List;

public class SubIriModel {
  private IriModel upperClass;
  private List<IriModel> subClasses;

  public IriModel getUpperClass() {
    return upperClass;
  }

  public void setUpperClass(IriModel upperClass) {
    this.upperClass = upperClass;
  }

  public List<IriModel> getSubClasses() {
    return subClasses;
  }

  public void setSubClasses(List<IriModel> subClasses) {
    this.subClasses = subClasses;
  }

}
