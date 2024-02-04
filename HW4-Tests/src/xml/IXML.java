package xml;

public interface IXML {
  boolean sameDocument(IXML doc);

  boolean sameXML(IXML doc);

  boolean sameText(IXML doc);
}
