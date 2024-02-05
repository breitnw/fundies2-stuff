import tester.Tester;
import xml.*;

class ExamplesXml {
  // EXAMPLES =====================================================================================
  
  // Text -----------------------------------------------------------------------------------------
  // Simple, varying text examples to use in more complex data structures
  Text t0 = new Text("Long Story Short");
  Text t1 = new Text("lorem");
  Text t2 = new Text("ipsum");
  Text t3 = new Text("dolor");
  Text t4 = new Text("sit");
  Text t5 = new Text("amet");
  
  // A basic ConsLoXML containing only text
  ConsLoXML texts = new ConsLoXML(t1,
          new ConsLoXML(t2,
              new ConsLoXML(t3,
                  new ConsLoXML(t4,
                      new ConsLoXML(t5,
                          new MtLoXML())))));
  // A ConsLoXML containing all the same text as `texts`, but in a different order, in case a
  // chaff accidentally allows for different orderings of ILoXML instead of ILoAttr.
  ConsLoXML textsScrambled = new ConsLoXML(t4,
      new ConsLoXML(t3,
          new ConsLoXML(t1,
              new ConsLoXML(t2,
                  new ConsLoXML(t5,
                      new MtLoXML())))));
  // A ConsLoXML containing the first 4 values of `texts`, in case a chaff forgets to check the
  // rest of one list after the other terminates
  ConsLoXML textsSubset = new ConsLoXML(t1,
      new ConsLoXML(t2,
          new ConsLoXML(t3,
              new ConsLoXML(t4,
                  new MtLoXML()))));
  
  // Attr -----------------------------------------------------------------------------------------
  // Simple, varying text examples to use in more complex data structures
  Attr cssDisplayAttr1 = new Attr("display", "flex");
  Attr cssDisplayAttr2 = new Attr("display", "block");
  Attr cssBorderRadiusAttr = new Attr("border-radius", "5px");
  Attr cssColorAttr = new Attr("color", "orange");
  
  // A basic ILoAttr containing three different attributes.
  ILoAttr cssAttrs = new ConsLoAttr(cssDisplayAttr1,
      new ConsLoAttr(cssBorderRadiusAttr,
          new ConsLoAttr(cssColorAttr, new MtLoAttr())));
  // An ILoAttr with all the same attributes as `cssAttrs`, but with the value (not the name) of
  // one attribute changed. Useful in case a chaff forgets to check attribute values.
  ILoAttr cssAttrsValChanged = new ConsLoAttr(cssDisplayAttr2,
      new ConsLoAttr(cssBorderRadiusAttr,
          new ConsLoAttr(cssColorAttr, new MtLoAttr())));
  // An ILoAttr with all the same attributes as `cssAttrs`, just in a different order. Useful for
  // testing against cssAttrs in the sameXML() method, where attribute order doesn't matter.
  ILoAttr cssAttrsScrambled = new ConsLoAttr(cssColorAttr,
      new ConsLoAttr(cssBorderRadiusAttr,
          new ConsLoAttr(cssDisplayAttr1, new MtLoAttr())));
  
  // Tag ------------------------------------------------------------------------------------------
  // A basic Tag containing cssAttrs and one Text object.
  Tag evermoreTag1 = new Tag("evermore", cssAttrs, new ConsLoXML(t0, new MtLoXML()));
  // A Tag that is the same as evermoreTag1 with the only difference being that the attributes
  // are in a different order. Useful for checking order-agnosticism in the sameXML() method
  Tag evermoreTag2 = new Tag("evermore", cssAttrsScrambled, new ConsLoXML(t0, new MtLoXML()));
  // A Tag that is the same as evermoreTag1 with the only difference being that it only has a
  // subset of the original's attributes. Useful for checking that subsets are not considered the
  // same list as supersets, even in sameXML().
  Tag evermoreTag3 = new Tag("evermore",
      new ConsLoAttr(cssDisplayAttr1, new MtLoAttr()),
      new ConsLoXML(t0, new MtLoXML()));
  // A Tag that is the same as evermoreTag1 but with the only difference being that the value of
  // one of the attributes is changed. This is useful for checking that the sameness methods
  // correctly check attribute values.
  Tag evermoreTag4 = new Tag("evermore", cssAttrsValChanged, new ConsLoXML(t0, new MtLoXML()));
  // A Tag that is the same as evermoreTag1 with the only difference being that the name of the
  // tag is changed. This is useful for showing that sameDocument and sameXML correctly check for
  // tag names, and that sameText does not.
  Tag folkloreTag = new Tag("folklore", cssAttrs, new ConsLoXML(t0, new MtLoXML()));
  // A Tag that contains another Tag, each with the same attributes in a different order. This is
  // useful for ensuring that nested tags _can_ have the same attributes, even though one tag
  // tag _cannot_ have multiple of the same attribute.
  Tag novoAmorTag = new Tag("birthplace", cssAttrs,
      new ConsLoXML(new Tag("bathing beach", cssAttrsScrambled, new MtLoXML()),
          new MtLoXML()));
  
  // A basic Tag containing a list of five Text objects
  Tag loremTag1 = new Tag("lorems", new MtLoAttr(), texts);
  // A Tag containing all the same Text objects as loremTag1, but in a different order, in case a
  // chaff accidentally allows for different orderings of ILoXML instead of ILoAttr.
  Tag loremTag2 = new Tag("lorems", new MtLoAttr(), textsScrambled);
  // A Tag containing the first 4 Text objects of LoremTag1, in case a chaff forgets to check the
  // rest of one list after the other terminates
  Tag loremTag3 = new Tag("lorems", new MtLoAttr(), textsSubset);
  // A Tag containing all the same Text objects as loremTag1, but nested within two different Tag
  // objects. Useful for testing whether sameText() properly compares only the contents of Text
  // objects.
  Tag loremTag4 = new Tag("lorems", new MtLoAttr(),
      new ConsLoXML(new Tag("lorems1", new MtLoAttr(),
          new ConsLoXML(t1,
              new ConsLoXML(t2,
                  new MtLoXML()))),
          new ConsLoXML(new Tag("lorems2", new MtLoAttr(),
              new ConsLoXML(t3,
                  new ConsLoXML(t4,
                      new ConsLoXML(t5,
                          new MtLoXML())))), new MtLoXML())));
  
  // A Tag whose contents are a list containing both Tag and Text objects, in case a chaff
  // encounters errors when the two types are placed as direct children of the same Tag.
  Tag mixture = new Tag("mixture", cssAttrs,
      new ConsLoXML(t0,
          new ConsLoXML(evermoreTag2,
              new ConsLoXML(loremTag1, new MtLoXML()))));
  
  // METHODS ======================================================================================
  
//  boolean testSameDocument1(Tester t) {
//      // Everything about the document is the same except for the content, which is in a
//      // scrambled order.
//      return t.checkExpect(loremTag.sameDocument(loremTag2), false);
//  }
//
//  boolean testSameDocument2(Tester t) {
//    // A document should always be the same as itself.
//    return t.checkExpect(novoAmorTag.sameDocument(novoAmorTag), true);
//  }
  
  // sameDocument
  boolean testSameDocument3(Tester t) {
    // Everything about the document is the same except for the attributes, which are scrambled
    return t.checkExpect(evermoreTag1.sameDocument(evermoreTag2), false);
  }
  
  boolean testSameDocument4(Tester t) {
    // Everything about the document is the same except for the name of the tag, which is different
    return t.checkExpect(folkloreTag.sameDocument(evermoreTag1), false);
  }
  
  boolean testSameDocument5(Tester t) {
    // Everything about the document is the same except for the value of one of the attributes
    return t.checkExpect(evermoreTag1.sameDocument(evermoreTag4), false);
  }
  
  boolean testSameDocument6(Tester t) {
    // Different instances of text with the same content cannot simply be checked with the =
    // operator, but they should still be viewed as the same document.
    return t.checkExpect(t1.sameDocument(new Text("lorem")), true);
  }
  
  // sameXml
  boolean testSameXML1(Tester t) {
    // The given document has a rearranged set of contents, but all the attributes are the same.
    // Since the order of contents still matters, this is invalid.
    return t.checkExpect(loremTag1.sameXML(loremTag2), false);
  }
  
  boolean testSameXML2(Tester t) {
    // The given tag has a rearranged set of attributes, but all other fields are the same. The
    // unordered _set_ of attributes is the same, so this is valid.
    return t.checkExpect(evermoreTag1.sameXML(evermoreTag2), true);
  }
  
  boolean testSameXML3(Tester t) {
    // The given tag has attributes that are a subset of this tag's attributes. The lists of
    // attributes are different, so this is invalid.
    return t.checkExpect(evermoreTag1.sameXML(evermoreTag3), false);
  }
  
  boolean testSameXML4(Tester t) {
    // The given tag has attributes that are a superset of this tag's attributes. The lists of
    // attributes are different, so this is invalid.
    return t.checkExpect(evermoreTag3.sameXML(evermoreTag1), false);
  }
  
  // sameText
  boolean testSameText1(Tester t) {
    // The given tag has all the same fields as this one, except for name. Both still have the
    // same text, so this is valid.
    return t.checkExpect(evermoreTag1.sameText(folkloreTag), true);
  }
  
//  boolean testSameText2(Tester t) {
//    // This tag contains only the given text, but they have different structures.
//    return t.checkExpect(evermoreTag1.sameText(t0), true);
//  }
  
//  boolean testSameText3(Tester t) {
//    // The given tag has all the same text as this one, but nested in two separate sub-tags
//    // rather than in one flat list
//    return t.checkExpect(loremTag1.sameText(loremTag4), true);
//  }
  
  // CONSTRUCTORS
  boolean testSameAttrName(Tester t) {
    // Attribute names within an individual tag must be unique. Since they are not in this
    // example, an exception will be thrown when a Tag is instantiated.
    return t.checkConstructorExceptionType(IllegalArgumentException.class, "Tag",
        "Lorem Ipsum",
        new ConsLoAttr(new Attr("a1", "false"),
            new ConsLoAttr(new Attr("a1", "true"),
                new MtLoAttr())),
        new MtLoXML());
  }
  
  boolean testInstantiation(Tester t) {
    // A tag may contain two of the same tag within its ILoXML, so no exception will be thrown
    // when it is instantiated.
    Tag a = new Tag("a", cssAttrs,
        new ConsLoXML(t0,
            new ConsLoXML(t0,
                new MtLoXML())));
    
    return t.checkConstructorNoException("testInstantiation", "Tag", "b", cssAttrs,
        new ConsLoXML(a,
            new ConsLoXML(a,
                new MtLoXML())));
  }
}