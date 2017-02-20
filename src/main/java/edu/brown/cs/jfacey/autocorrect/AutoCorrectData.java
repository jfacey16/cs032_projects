package edu.brown.cs.jfacey.autocorrect;

public class AutoCorrectData {

  private boolean iPrefix;
  private boolean iWhitespace;
  private boolean iSmart;
  private boolean iLed;

  public AutoCorrectData(boolean prefix, boolean whitespace, boolean smart,
      boolean led) {
    iPrefix = prefix;
    iWhitespace = whitespace;
    iSmart = smart;
    iLed = led;
  }

  public boolean getPrefix() {
    return iPrefix;
  }

  public void setPrefix(boolean prefix) {
    iPrefix = prefix;
  }

  public boolean getWhitespace() {
    return iWhitespace;
  }

  public void setWhitespace(boolean whitespace) {
    iWhitespace = whitespace;
  }

  public boolean getSmart() {
    return iSmart;
  }

  public void setSmart(boolean smart) {
    iSmart = smart;
  }

  public boolean getLed() {
    return iLed;
  }

  public void setLed(boolean led) {
    iLed = led;

  }
}
