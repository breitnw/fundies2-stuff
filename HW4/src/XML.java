import tester.Tester;

// Represents an XML attribute present in a tag, with a name and a value
class Attr  {
  String name;
  String value;
  
  Attr(String name, String value) {
    this.name = name;
    this.value = value;
  }
  /* TEMPLATE
  FIELDS:
  ... this.name ...                       -- String
  ... this.value ...                      -- String
  METHODS:
  ... sameName(Attr that) ...             -- boolean
  ... sameAttr(Attr that) ...             -- boolean
  METHODS ON FIELDS:
  ... this.name.equals(that.name) ...     -- boolean
  ... this.value.equals(that.value) ...   -- boolean
   */
  
  // Determines if this Attr has the same name as that Attr
  boolean sameName(Attr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...        -- Attr
    FIELDS ON PARAMETERS:
    ... that.name ...   -- String
     */
    return this.name.equals(that.name);
  }
  
  // Determines if this Attr is the same as that Attr; i.e., the names and values of both are equal.
  boolean sameAttr(Attr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...         -- Attr
    FIELDS ON PARAMETERS:
    ... that.name ...    -- String
    ... that.value ...   -- String
     */
    return this.name.equals(that.name)
        && this.value.equals(that.value);
  }
}

// Represents a list of Attrs with zero or more elements
interface ILoAttr {
  // Determines if this ILoAttr contains two Attrs with the same name.
  boolean hasDuplicateName();
  
  // Determines if this ILoAttr contains an Attr with the same name as the provided Attr.
  boolean hasNameOf(Attr attr);
  
  // Determines if this ILoAttr and that ILoAttr have the same structure and contents.
  boolean sameILoAttr(ILoAttr that);
  
  // Determines if this ILoAttr and that ConsLoAttr have the same structure and contents.
  boolean sameConsLoAttr(ConsLoAttr that);
  
  // Determines if this ILoAttr and that MtLoAttr have the same structure and contents.
  boolean sameMtLoAttr(MtLoAttr that);
  
  // Determines if this ILoAttr has the same attributes as that ILoAttr, where order doesn't
  // matter, by checking that this and that are subsets of each other.
  boolean sameILoAttrUnordered(ILoAttr that);
  
  // Determines if this ILoAttr is a subset of that; i.e., every Attr in this is the same as an
  // Attr in that.
  boolean subsetOf(ILoAttr that);
  
  // Determines if this ILoAttr contains an element that matches the fields of the provided Attr.
  boolean hasAttr(Attr that);
}

// Represents a list of Attrs with a first element and a list of subsequent elements
class ConsLoAttr implements ILoAttr {
  Attr first;
  ILoAttr rest;
  
  ConsLoAttr(Attr first, ILoAttr rest) {
    this.first = first;
    this.rest = rest;
  }
  /* TEMPLATE
  FIELDS:
  ... this.first ...                           -- Attr
  ... this.rest ...                            -- ILoAttr
  METHODS:
  ... hasDuplicateName() ...                   -- boolean
  ... hasNameOf(Attr attr) ...                 -- boolean
  ... sameILoAttr(ILoAttr that) ...            -- boolean
  ... sameConsLoAttr(ConsLoAttr that) ...      -- boolean
  ... sameMtLoAttr(MtLoAttr that) ...          -- boolean
  ... sameILoAttrUnordered(ILoAttr that) ...   -- boolean
  ... subsetOf(ILoAttr that) ...               -- boolean
  ... hasAttr(Attr that) ...                   -- boolean
  METHODS ON FIELDS:
  ... this.rest.hasNameOf(this.first) ...      -- boolean
  ... this.rest.hasDuplicateName() ...         -- boolean
  ... this.first.sameName(attr) ...            -- boolean
  ... this.rest.hasNameOf(attr) ...            -- boolean
  ... this.rest.subsetOf(that) ...             -- boolean
  ... this.first.sameAttr(that) ...            -- boolean
  ... this.rest.hasAttr(that) ...              -- boolean
   */
  
  // Determines if this ConsLoAttr contains two Attrs with the same name.
  public boolean hasDuplicateName() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.rest.hasNameOf(this.first) || this.rest.hasDuplicateName();
  }
  
  // Determines if this ConsLoAttr contains an Attr with the same name as the provided Attr.
  public boolean hasNameOf(Attr attr) {
    /* TEMPLATE
    PARAMETERS:
    ... attr ...   -- Attr
     */
    return this.first.sameName(attr) || this.rest.hasNameOf(attr);
  }
  
  // Determines if this ConsLoAttr and that ILoAttr have the same structure and contents.
  public boolean sameILoAttr(ILoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                        -- ILoAttr
    METHODS ON PARAMETERS:
    ... that.sameConsLoAttr(this) ...   -- boolean
     */
    return that.sameConsLoAttr(this);
  }
  
  // Determines if this ConsLoAttr and that ConsLoAttr have the same structure and contents.
  public boolean sameConsLoAttr(ConsLoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- ConsLoAttr
    FIELDS ON PARAMETERS:
    ... that.first ...   -- Attr
    ... that.rest ...    -- ILoAttr
     */
    return that.first.sameAttr(this.first)
        && that.rest.sameILoAttr(this.rest);
  }
  
  // Determines if this ConsLoAttr and that MtLoAttr have the same structure and contents, always
  // returning false.
  public boolean sameMtLoAttr(MtLoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- MtLoAttr
     */
    return false;
  }
  
  // Determines if this ConsLoAttr has the same attributes as that ILoAttr, where order doesn't
  // matter, by checking that this and that are subsets of each other.
  public boolean sameILoAttrUnordered(ILoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                  -- ILoAttr
    METHODS ON PARAMETERS:
    ... that.subsetOf(this) ...   -- boolean
     */
    return this.subsetOf(that) && that.subsetOf(this);
  }
  
  // Determines if this ConsLoAttr is a subset of that; i.e., every Attr in this is the same as an
  // Attr in that.
  public boolean subsetOf(ILoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                       -- ILoAttr
    METHODS ON PARAMETERS:
    ... that.hasAttr(this.first) ...   -- boolean
     */
    return that.hasAttr(this.first) && this.rest.subsetOf(that);
  }
  
  // Determines if this ILoAttr contains an element that matches the fields of the provided Attr.
  public boolean hasAttr(Attr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- Attr
     */
    return this.first.sameAttr(that) || this.rest.hasAttr(that);
  }
}

// Represents an empty list of Attrs
class MtLoAttr implements ILoAttr {
  /* TEMPLATE
  METHODS:
  ... hasDuplicateName() ...                   -- boolean
  ... hasNameOf(Attr attr) ...                 -- boolean
  ... sameILoAttr(ILoAttr that) ...            -- boolean
  ... sameConsLoAttr(ConsLoAttr that) ...      -- boolean
  ... sameMtLoAttr(MtLoAttr that) ...          -- boolean
  ... sameILoAttrUnordered(ILoAttr that) ...   -- boolean
  ... subsetOf(ILoAttr that) ...               -- boolean
  ... hasAttr(Attr that) ...                   -- boolean
   */
  
  // Determines if this MtLoAttr contains two Attrs with the same name.
  public boolean hasDuplicateName() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return false;
  }
  
  // Determines if this MtLoAttr contains an Attr with the same name as the provided Attr.
  public boolean hasNameOf(Attr attr) {
    /* TEMPLATE
    PARAMETERS:
    ... attr ...   -- Attr
     */
    return false;
  }
  
  // Determines if this MtLoAttr and that ILoAttr have the same structure and contents.
  public boolean sameILoAttr(ILoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                      -- ILoAttr
    METHODS ON PARAMETERS:
    ... that.sameMtLoAttr(this) ...   -- boolean
     */
    return that.sameMtLoAttr(this);
  }
  
  // Determines if this MtLoAttr and that ConsLoAttr have the same structure and contents, always
  // returning false.
  public boolean sameConsLoAttr(ConsLoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- ConsLoAttr
     */
    return false;
  }
  
  // Determines if this MtLoAttr and that MtLoAttr have the same structure and contents, always
  // returning true.
  public boolean sameMtLoAttr(MtLoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- MtLoAttr
     */
    return true;
  }
  
  // Determines if this MtLoAttr has the same attributes as that ILoAttr, where order doesn't
  // matter, by checking that that is a subset of this.
  public boolean sameILoAttrUnordered(ILoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                  -- ILoAttr
    METHODS ON PARAMETERS:
    ... that.subsetOf(this) ...   -- boolean
     */
    return that.subsetOf(this);
  }
  
  // Determines if this MtLoAttr is a subset of that; i.e., every Attr in this is the same as an
  // Attr in that.
  public boolean subsetOf(ILoAttr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- ILoAttr
     */
    return true;
  }
  
  // Determines if this MtLoAttr contains an element that matches the fields of the provided Attr.
  public boolean hasAttr(Attr that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- Attr
     */
    return false;
  }
}

// Represents an XML document
interface IXML {
  // Determines if this IXML is the "same" as doc. To be the same, the documents must have the
  // same tags, tree-structure, and content, and any lists of attributes must satisfy attrsPred.
  boolean same(IXML doc, ISameAttrsPred attrsPred);
  
  // Determines whether this document and the given document have exactly the same shape: the
  // same tags, tree-structure, attributes (with the same order and values), and content.
  boolean sameDocument(IXML doc);
  
  // Determines if this IXML and that Tag are the same, including the same tags, tree-structure,
  // and content. Any lists of attributes must satisfy attrsPred.
  boolean sameTag(Tag that, ISameAttrsPred attrsPred);
  
  // Determines if this IXML and that Text are the same.
  boolean sameTextObj(Text that);
  
  // Determines if this XML document and the given document have the same shape, when the
  // ordering of attributes is irrelevant.
  boolean sameXML(IXML doc);

  // Determines if this document and the given document contain the same plain text content,
  // ignoring all tags and attributes.
  boolean sameText(IXML doc);
  
  // Converts this IXML to a String representation by concatenating the contents of all of its
  // contained Text objects and ignoring all other content.
  String toTextString();
}

// Represents an XML tag with a name, a list of attributes, and a list of contents.
class Tag implements IXML {
  String name;
  ILoAttr attrs;
  ILoXML content;
  
  Tag(String name, ILoAttr attrs, ILoXML content) {
    if (attrs.hasDuplicateName()) {
      throw
          new IllegalArgumentException("Two or more provided attributes have the same name. tag");
    }
    this.name = name;
    this.attrs = attrs;
    this.content = content;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.name ...                                          -- String
  ... this.attrs ...                                         -- ILoAttr
  ... this.content ...                                       -- ILoXML
  METHODS:
  ... same(IXML doc, ISameAttrsPred attrsPred) ...           -- boolean
  ... sameDocument(IXML doc) ...                             -- boolean
  ... sameTag(Tag that, ISameAttrsPred) ...                  -- boolean
  ... sameTextObj(Text that) ...                             -- boolean
  ... sameXML(IXML doc) ...                                  -- boolean
  ... sameText(IXML doc) ...                                 -- boolean
  ... toTextString() ...                                     -- String
  METHODS ON FIELDS:
  ... this.name.equals(that.name) ...                        -- boolean
  ... this.content.sameILoXML(that.content, attrsPred) ...   -- boolean
  ... this.content.toTextString() ...                        -- String
   */
  
  // Determines if this Tag is the "same" as doc. To be the same, the documents must have the
  // same tags, tree-structure, and content, and any lists of attributes must satisfy attrsPred.
  public boolean same(IXML doc, ISameAttrsPred attrsPred) {
    /* TEMPLATE:
    PARAMETERS:
    ... doc ...                            -- IXML
    ... attrsPred ...                      -- ISameAttrsPred
    METHODS ON PARAMETERS:
    ... doc.sameTag(this, attrsPred) ...   -- boolean
     */
    return doc.sameTag(this, attrsPred);
  }
  
  // Determines whether this Tag and the given document have exactly the same shape: the
  // same tags, tree-structure, attributes (with the same order and values), and content.
  public boolean sameDocument(IXML doc) {
    /* TEMPLATE
    PARAMETERS:
    ... doc ...   -- IXML
     */
    return this.same(doc, new SameListPred());
  }
  
  // Determines if this Tag and that Tag are the same, including the same tags, tree-structure,
  // and content. Any lists of attributes must satisfy attrsPred.
  public boolean sameTag(Tag that, ISameAttrsPred attrsPred) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                      -- Tag
    ... attrsPred ...                                 -- ISameAttrsPred
    FIELDS ON PARAMETERS:
    ... that.name ...                                 -- String
    ... that.attrs ...                                -- ILoAttr
    ... that.content ...                              -- ILoXML
    METHODS ON PARAMETERS:
    ... attrsPred.check(this.attrs, that.attrs) ...   -- boolean
     */
    return this.name.equals(that.name)
        && attrsPred.check(this.attrs, that.attrs)
        && this.content.sameILoXML(that.content, attrsPred);
  }
  
  // Determines if this Tag and that Text are the same.
  public boolean sameTextObj(Text that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- Text
     */
    return false;
  }
  
  // Determines if this Tag and the given document have the same shape, when the ordering of
  // attributes is irrelevant.
  public boolean sameXML(IXML doc) {
    /* TEMPLATE
    PARAMETERS:
    ... doc ...   -- IXML
     */
    return same(doc, new SameSetPred());
  }
  
  // Determines if this Tag and the given document contain the same plain text content,
  // ignoring all tags and attributes.
  public boolean sameText(IXML doc) {
    /* TEMPLATE
    PARAMETERS:
    ... doc ...                  -- IXML
    METHODS ON PARAMETERS:
    ... doc.toTextString() ...   -- String
     */
    return this.toTextString().equals(doc.toTextString());
  }
  
  // Converts this Tag to a String representation by concatenating the contents of all of its
  // contained Text objects and ignoring all other content.
  public String toTextString() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.content.toTextString();
  }
}

// Represents text in an XML document.
class Text implements IXML {
  String content;
  
  Text(String content) {
    this.content = content;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.content ...                               -- String
  METHODS:
  ... same(IXML doc, ISameAttrsPred attrsPred) ...   -- boolean
  ... sameDocument(IXML doc) ...                     -- boolean
  ... sameTag(Tag that, ISameAttrsPred) ...          -- boolean
  ... sameTextObj(Text that) ...                     -- boolean
  ... sameXML(IXML doc) ...                          -- boolean
  ... sameText(IXML doc) ...                         -- boolean
  ... toTextString() ...                             -- String
  METHODS ON FIELDS:
  ... this.content.equals(that.content) ...          -- boolean
  ... this.content.equals(doc.toTextString())        -- boolean
   */
  
  // Determines if this Text is the "same" as doc. To be the same, the documents must have the
  // same tags, tree-structure, and content, and any lists of attributes must satisfy attrsPred.
  public boolean same(IXML doc, ISameAttrsPred attrsPred) {
    /* TEMPLATE
    PARAMETERS:
    ... doc ...                     -- IXML
    ... attrsPred ...               -- ISameAttrsPred
    METHODS ON PARAMETERS:
    ... doc.sameTextObj(this) ...   -- boolean
     */
    return doc.sameTextObj(this);
  }
  
  // Determines whether this Text and the given document have exactly the same shape and content.
  public boolean sameDocument(IXML doc) {
    /* TEMPLATE
    PARAMETERS:
    ... doc ...   -- IXML
     */
    return same(doc, new SameListPred());
  }
  
  // Determines if this Text and that Tag are the same.
  public boolean sameTag(Tag that, ISameAttrsPred attrsPred) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...        -- Tag
    ... attrsPred ...   -- ISameAttrsPred
     */
    return false;
  }
  
  // Determines if this Text and that Text have the same content.
  public boolean sameTextObj(Text that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...           -- Text
    FIELDS ON PARAMETERS:
    ... that.content ...   -- String
     */
    return this.content.equals(that.content);
  }
  
  // Determines if this Text and the given document have the same shape, when the ordering of
  // attributes is irrelevant.
  public boolean sameXML(IXML doc) {
    /* TEMPLATE
    PARAMETERS:
    ... doc ...   -- IXML
     */
    return same(doc, new SameSetPred());
  }
  
  // Determines if this Text and the given document contain the same plain text content,
  // ignoring all tags and attributes.
  public boolean sameText(IXML doc) {
    /* TEMPLATE
    PARAMETERS:
    ... doc ...                  -- IXML
    METHODS ON PARAMETERS:
    ... doc.toTextString() ...   -- boolean
     */
    return this.content.equals(doc.toTextString());
  }
  
  // Converts this Text to a String representation by returning its contents.
  public String toTextString() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.content;
  }
}

// Represents a list of IXMLs with zero or more elements
interface ILoXML {
  // Determines if this ILoXML has the same contents as that ILoXML according to attrsPred.
  boolean sameILoXML(ILoXML that, ISameAttrsPred attrsPred);
  
  // Determines if this ILoXML has the same contents as that ConsLoXML; i.e., their first and
  // remaining IXML elements are all the same according to attrsPred.
  boolean sameConsLoXML(ConsLoXML that, ISameAttrsPred attrsPred);
  
  // Determines if this ILoXML has the same contents as that MtLoXML
  boolean sameMtLoXML(MtLoXML that);
  
  // Converts this ILoXML to a String representation by concatenating the contents of all of its
  // contained Text objects and ignoring all other content.
  String toTextString();
}

// Represents a list of IXMLs with a first element and a list of remaining elements
class ConsLoXML implements ILoXML {
  IXML first;
  ILoXML rest;
  
  ConsLoXML(IXML first, ILoXML rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.first ...                                                -- IXML
  ... this.rest ...                                                 -- ILoXML
  METHODS:
  ... sameILoXML(ILoXML that, ISameAttrsPred attrsPred) ...         -- boolean
  ... sameConsLoXML(ConsLoXML that, ISameAttrsPred attrsPred) ...   -- boolean
  ... sameMtLoXML(MtLoXML that) ...                                 -- boolean
  ... toTextString() ...                                            -- String
  METHODS ON FIELDS:
  ... this.first.sameDocument(that.first) ...                       -- boolean
  ... this.rest.sameILoXML(that.rest, attrsPred) ...                -- boolean
  ... this.first.toTextString() ...                                 -- String
  ... this.rest.toTextString() ...                                  -- String
   */
  
  // Determines if this ConsLoXML has the same contents as that ILoXML according to attrsPred.
  public boolean sameILoXML(ILoXML that, ISameAttrsPred attrsPred) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                  -- ILoXML
    ... attrsPred ...                             -- ISameAttrsPred
    METHODS ON PARAMETERS:
    ... that.sameConsLoXML(this, attrsPred) ...   -- boolean
     */
    return that.sameConsLoXML(this, attrsPred);
  }
  
  // Determines if this ConsLoXML has the same contents as that ConsLoXML; i.e., their first and
  // remaining IXML elements are all the same according to attrsPred.
  public boolean sameConsLoXML(ConsLoXML that, ISameAttrsPred attrsPred) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...        -- ConsLoXML
    ... attrsPred ...   -- ISameAttrsPred
    FIELDS ON PARAMETERS:
    ... that.first ...   -- IXML
    ... that.rest ...    -- ILoXML
     */
    return this.first.same(that.first, attrsPred)
        && this.rest.sameILoXML(that.rest, attrsPred);
  }
  
  // Determines if this ConsLoXML has the same contents as that MtLoXML
  public boolean sameMtLoXML(MtLoXML that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- MtLoXML
     */
    return false;
  }
  
  // Converts this ConsLoXML to a String representation by concatenating the contents of all of its
  // contained Text objects and ignoring all other content.
  public String toTextString() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.first.toTextString() + this.rest.toTextString();
  }
}

// Represents an empty list of IXMLs
class MtLoXML implements ILoXML {
  /* TEMPLATE
  METHODS:
  ... sameILoXML(ILoXML that, ISameAttrsPred attrsPred) ...         -- boolean
  ... sameConsLoXML(ConsLoXML that, ISameAttrsPred attrsPred) ...   -- boolean
  ... sameMtLoXML(MtLoXML that) ...                                 -- boolean
  ... toTextString() ...                                            -- String
   */
  
  // Determines if this MtLoXML has the same contents as that ILoXML according to attrsPred.
  public boolean sameILoXML(ILoXML that, ISameAttrsPred attrsPred) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                     -- ILoXML
    ... attrsPred ...                -- ISameAttrsPred
    METHODS ON PARAMETERS:
    ... that.sameMtLoXML(this) ...   -- boolean
     */
    return that.sameMtLoXML(this);
  }
  
  // Determines if this MtLoXML has the same contents as that ConsLoXML according to attrsPred.
  public boolean sameConsLoXML(ConsLoXML that, ISameAttrsPred attrsPred) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...        -- ConsLoXML
    ... attrsPred ...   -- ISameAttrsPred
     */
    return false;
  }
  
  // Determines if this MtLoXML has the same contents as that MtLoXML
  public boolean sameMtLoXML(MtLoXML that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- MtLoXML
     */
    return true;
  }
  
  // Converts this MtLoXML to a String representation by concatenating the contents of all of its
  // contained Text objects and ignoring all other content.
  public String toTextString() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return "";
  }
}

// A function interface representing a predicate to compare two ILoAttrs
interface ISameAttrsPred {
  // Checks that a1 is the same as a2
  boolean check(ILoAttr a1, ILoAttr a2);
}

// A predicate that compares two ILoAttrs with list equality
class SameListPred implements ISameAttrsPred {
  /* TEMPLATE
  METHODS:
  ... check(ILoAttr a1, ILoAttr a2) ...   -- boolean
   */
  
  // Checks that a1 is the same as a2 using list equality; i.e., all the elements and the
  // order of elements must be the same
  public boolean check(ILoAttr a1, ILoAttr a2) {
    /* TEMPLATE
    PARAMETERS:
    ... a1 ...                   -- ILoAttr
    ... a2 ...                   -- ILoAttr
    METHODS ON PARAMETERS:
    ... a1.sameILoAttr(a2) ...   -- boolean
     */
    return a1.sameILoAttr(a2);
  }
}

// A predicate that compares two ILoAttrs with set equality
class SameSetPred implements ISameAttrsPred {
  /* TEMPLATE
  METHODS:
  ... check(ILoAttr a1, ILoAttr a2) ...   -- boolean
   */
  
  // Checks that a1 is the same as a2 using set equality; i.e., only the elements themselves, not
  // the order, must be the same.
  public boolean check(ILoAttr a1, ILoAttr a2) {
    /* TEMPLATE
    PARAMETERS:
    ... a1 ...                            -- ILoAttr
    ... a2 ...                            -- ILoAttr
    METHODS ON PARAMETERS:
    ... a1.sameILoAttrUnordered(a2) ...   -- boolean
     */
    return a1.sameILoAttrUnordered(a2);
  }
}

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
  ConsLoAttr cssAttrs = new ConsLoAttr(cssDisplayAttr1,
      new ConsLoAttr(cssBorderRadiusAttr,
          new ConsLoAttr(cssColorAttr, new MtLoAttr())));
  // An ILoAttr containing a subset of the attributes represented in cssAttrs. Useful for testing
  // whether subsets/supersets are caught by sameness methods
  ConsLoAttr cssAttrsSubset = new ConsLoAttr(cssDisplayAttr1,
      new ConsLoAttr(cssColorAttr, new MtLoAttr()));
  // An ILoAttr with all the same attributes as `cssAttrs`, but with the value (not the name) of
  // one attribute changed. Useful in case a chaff forgets to check attribute values.
  ConsLoAttr cssAttrsValChanged = new ConsLoAttr(cssDisplayAttr2,
      new ConsLoAttr(cssBorderRadiusAttr,
          new ConsLoAttr(cssColorAttr, new MtLoAttr())));
  // An ILoAttr with all the same attributes as `cssAttrs`, just in a different order. Useful for
  // testing against cssAttrs in the sameXML() method, where attribute order doesn't matter.
  ConsLoAttr cssAttrsScrambled = new ConsLoAttr(cssColorAttr,
      new ConsLoAttr(cssBorderRadiusAttr,
          new ConsLoAttr(cssDisplayAttr1, new MtLoAttr())));
  // An ILoAttr containing the all the values of `cssAttrs` but with one duplicate Attr name.
  // Useful for testing whether a list of Attrs with a duplicate is successfully caught by the
  // hasDuplicateName() method.
  ConsLoAttr cssAttrsDuplicate = new ConsLoAttr(cssDisplayAttr1,
      new ConsLoAttr(cssBorderRadiusAttr,
          new ConsLoAttr(cssDisplayAttr2,
              new ConsLoAttr(cssColorAttr,
                  new MtLoAttr()))));
  
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
  
  // on Attr --------------------------------------------------------------------------------------
  boolean testSameName(Tester t) {
    return
        // different names and values
        t.checkExpect(new Attr("a1", "v1").sameName(new Attr("a2", "v2")), false)
        // same name but different value
        && t.checkExpect(new Attr("a1", "v1").sameName(new Attr("a1", "v2")), true)
        // different name but same value
        && t.checkExpect(new Attr("a1", "v1").sameName(new Attr("a2", "v1")), false)
        // same name and value
        && t.checkExpect(new Attr("a1", "v1").sameName(new Attr("a1", "v1")), true);
  }
  
  boolean testSameAttr(Tester t) {
    return
        // different names and values
        t.checkExpect(new Attr("a1", "v1").sameAttr(new Attr("a2", "v2")), false)
        // same name but different value
        && t.checkExpect(new Attr("a1", "v1").sameAttr(new Attr("a1", "v2")), false)
        // different name but same value
        && t.checkExpect(new Attr("a1", "v1").sameAttr(new Attr("a2", "v1")), false)
        // same name and value
        && t.checkExpect(new Attr("a1", "v1").sameAttr(new Attr("a1", "v1")), true);
  }
  
  // on ILoAttr -----------------------------------------------------------------------------------
  boolean testHasDuplicateName(Tester t) {
    return
        // all names are unique
        t.checkExpect(cssAttrs.hasDuplicateName(), false)
        // all values are unique, but there is a duplicate name
        && t.checkExpect(cssAttrsDuplicate.hasDuplicateName(), true)
        // an empty list of attributes does not have a duplicate name
        && t.checkExpect(new MtLoAttr().hasDuplicateName(), false);
  }
  
  boolean testHasNameOf(Tester t) {
    // The name is found in cssAttrs, but not the value
    Attr testAttr1 = new Attr("display", "inline-block");
    // The value is found in cssAttrs, but not the name
    Attr testAttr2 = new Attr("width", "5px");
    
    return
        // cssAttrs has the name of testAttr1 but not the value
        t.checkExpect(cssAttrs.hasNameOf(testAttr1), true)
        // cssAttrs has the value of testAttr1 but not the name
        && t.checkExpect(cssAttrs.hasNameOf(testAttr2), false)
        // an empty list of attributes does not have any name
        && t.checkExpect(new MtLoAttr().hasNameOf(testAttr1), false);
  }
  
  boolean testSameILoAttr(Tester t) {
    return
        // A list of Attrs is clearly the same as itself
        t.checkExpect(cssAttrs.sameILoAttr(cssAttrs), true)
        // Changing one value and no names yields a different ILoAttr
        && t.checkExpect(cssAttrs.sameILoAttr(cssAttrsValChanged), false)
        // Changing the order of the Attrs yields a different ILoAttr
        && t.checkExpect(cssAttrs.sameILoAttr(cssAttrsScrambled), false)
        // A list of Attrs is not the same as a superset
        && t.checkExpect(cssAttrsSubset.sameILoAttr(cssAttrs), false)
        // A list of Attrs is not the same as a subset
        && t.checkExpect(cssAttrs.sameILoAttr(cssAttrsSubset), false);
  }
  
  boolean testSameConsLoAttr(Tester t) {
    return
        // A list of Attrs is clearly the same as itself
        t.checkExpect(cssAttrs.sameConsLoAttr(cssAttrs), true)
        // Changing one value and no names yields a different ILoAttr
        && t.checkExpect(cssAttrs.sameConsLoAttr(cssAttrsValChanged), false)
        // Changing the order of the Attrs yields a different ILoAttr
        && t.checkExpect(cssAttrs.sameConsLoAttr(cssAttrsScrambled), false)
        // A list of Attrs is not the same as a superset
        && t.checkExpect(cssAttrsSubset.sameConsLoAttr(cssAttrs), false)
        // A list of Attrs is not the same as a subset
        && t.checkExpect(cssAttrs.sameConsLoAttr(cssAttrsSubset), false);
  }
  
  boolean testSameMtLoAttr(Tester t) {
    // An empty list of Attrs is always the same as an empty list of Attrs
    return t.checkExpect(new MtLoAttr().sameILoAttr(new MtLoAttr()), true);
  }
  
  boolean testSameILoAttrUnordered(Tester t) {
    return
        // A list of Attrs is clearly the same as itself
        t.checkExpect(cssAttrs.sameILoAttrUnordered(cssAttrs), true)
        // Changing one value and no names yields a different ILoAttr
        && t.checkExpect(cssAttrs.sameILoAttrUnordered(cssAttrsValChanged), false)
        // Changing the order of the Attrs still results in a true comparison
        && t.checkExpect(cssAttrs.sameILoAttrUnordered(cssAttrsScrambled), true)
        // A list of Attrs is not the same as a superset
        && t.checkExpect(cssAttrsSubset.sameILoAttrUnordered(cssAttrs), false)
        // A list of Attrs is not the same as a subset
        && t.checkExpect(cssAttrs.sameILoAttrUnordered(cssAttrsSubset), false);
  }
  
  boolean testSubsetOf(Tester t) {
    return
        // A list of Attrs is a subset of itself
        t.checkExpect(cssAttrs.subsetOf(cssAttrs), true)
        // A list of Attrs with one element removed is a subset of the original
        && t.checkExpect(cssAttrsSubset.subsetOf(cssAttrs), true)
        // A list of Attrs is not a subset of the same list with one element removed
        && t.checkExpect(cssAttrs.subsetOf(cssAttrsSubset), false)
        // An empty list is a subset of any list of Attrs
        && t.checkExpect(new MtLoAttr().subsetOf(cssAttrs), true)
        // An empty list is a subset of another empty list
        && t.checkExpect(new MtLoAttr().subsetOf(new MtLoAttr()), true);
  }
  
  boolean hasAttr(Tester t) {
    // The name is found in cssAttrs, but not the value
    Attr testAttr1 = new Attr("display", "inline-block");
    
    return
        // cssAttrs has the name of testAttr1 but not the value
        t.checkExpect(cssAttrs.hasAttr(testAttr1), false)
        // cssAttrs has an attr with the name and value of cssDisplayAttr1
        && t.checkExpect(cssAttrs.hasAttr(cssDisplayAttr1), true)
        // an empty list of attributes does not have any Attr
        && t.checkExpect(new MtLoAttr().hasAttr(cssDisplayAttr1), false);
  }
  
  // On IXML --------------------------------------------------------------------------------------
  boolean testSame(Tester t) {
    // These tests are the same as those used in testSameDocument_ and testSameXML_, just using
    // the `same` method with an instance of ISameAttrsPred instead. See the testSameDocument_
    // and testSameXML_ tests for justifications of each case.
    return t.checkExpect(loremTag1.same(loremTag2, new SameListPred()), false)
        && t.checkExpect(novoAmorTag.same(novoAmorTag, new SameListPred()), true)
        && t.checkExpect(evermoreTag1.same(evermoreTag2, new SameListPred()), false)
        && t.checkExpect(folkloreTag.same(evermoreTag1, new SameListPred()), false)
        && t.checkExpect(evermoreTag1.same(evermoreTag4, new SameListPred()), false)
        && t.checkExpect(loremTag1.same(loremTag2, new SameSetPred()), false)
        && t.checkExpect(evermoreTag1.same(evermoreTag2, new SameSetPred()), true)
        && t.checkExpect(evermoreTag1.same(evermoreTag3, new SameSetPred()), false)
        && t.checkExpect(evermoreTag3.same(evermoreTag1, new SameSetPred()), false)
        && t.checkExpect(new Text("test").same(evermoreTag1, new SameListPred()), false)
        && t.checkExpect(evermoreTag1.same(new Text("test"), new SameListPred()), false)
        && t.checkExpect(new Text("test").same(new Text("test"), new SameListPred()), true)
        && t.checkExpect(new Text("test").same(new Text("test2"), new SameListPred()), false);
    
  }
  
  boolean testSameTag(Tester t) {
    // These tests are the same as those used in testSameDocument_ and testSameXML_, just using
    // the `same` method with an instance of ISameAttrsPred instead. See the testSameDocument_
    // and testSameXML_ tests for justifications of each case.
    return t.checkExpect(loremTag1.sameTag(loremTag2, new SameListPred()), false)
        && t.checkExpect(novoAmorTag.sameTag(novoAmorTag, new SameListPred()), true)
        && t.checkExpect(evermoreTag1.sameTag(evermoreTag2, new SameListPred()), false)
        && t.checkExpect(folkloreTag.sameTag(evermoreTag1, new SameListPred()), false)
        && t.checkExpect(evermoreTag1.sameTag(evermoreTag4, new SameListPred()), false)
        && t.checkExpect(loremTag1.sameTag(loremTag2, new SameSetPred()), false)
        && t.checkExpect(evermoreTag1.sameTag(evermoreTag2, new SameSetPred()), true)
        && t.checkExpect(evermoreTag1.sameTag(evermoreTag3, new SameSetPred()), false)
        && t.checkExpect(evermoreTag3.sameTag(evermoreTag1, new SameSetPred()), false)
        && t.checkExpect(new Text("test").sameTag(evermoreTag1, new SameListPred()), false);
  }
  
  boolean testSameTextObj(Tester t) {
    return
        // A tag is clearly not the same as any text
        t.checkExpect(evermoreTag1.sameTextObj(new Text("test")), false)
        // A Text is the same as another Text instance with the same content
        && t.checkExpect(new Text("test").sameTextObj(new Text("test")), true)
        // A Text is not the same as another Text instance with different content
        && t.checkExpect(new Text("test").sameTextObj(new Text("test2")), false);
  }
  
  boolean testIXMLToTextString(Tester t) {
    return
        // A Text on its own produces its contents.
        t.checkExpect(t0.toTextString(), "Long Story Short")
        // A Tag containing one Text should produce the contents of that Text.
        && t.checkExpect(evermoreTag1.toTextString(), "Long Story Short")
        // A Tag containing a complex tree structure should produce all of its Text elements in
        // order with no space between them
        && t.checkExpect(mixture.toTextString(),
        "Long Story ShortLong Story Shortloremipsumdolorsitamet")
        // A Tag containing no Text elements returns an empty string
        && t.checkExpect(novoAmorTag.toTextString(), "");
  }
  
  boolean testSameDocument1(Tester t) {
    // Everything about the document is the same except for the content, which is in a
    // scrambled order.
    return t.checkExpect(loremTag1.sameDocument(loremTag2), false);
  }

  boolean testSameDocument2(Tester t) {
    // A document should always be the same as itself.
    return t.checkExpect(novoAmorTag.sameDocument(novoAmorTag), true);
  }
  
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
  
  // sameText -------------------------------------------------------------------------------------
  boolean testSameText1(Tester t) {
    // The given tag has all the same fields as this one, except for name. Both still have the
    // same text, so this is valid.
    return t.checkExpect(evermoreTag1.sameText(folkloreTag), true);
  }

  boolean testSameText2(Tester t) {
    // This tag contains only the given text, but they have different structures.
    return t.checkExpect(evermoreTag1.sameText(t0), true);
  }

  boolean testSameText3(Tester t) {
    // The given tag has all the same text as this one, but nested in two separate sub-tags
    // rather than in one flat list
    return t.checkExpect(loremTag1.sameText(loremTag4), true);
  }
  
  // on ILoXML ------------------------------------------------------------------------------------
  boolean testSameILoXML(Tester t) {
    // All the tests below are the same as those found in testSameConsLoXML and TestSameMtLoXML,
    // just using the SameILoXML method instead. See testSameConsLoXML and TestSameMtLoXML for
    // justifications for each test.
    return t.checkExpect(texts.sameILoXML(texts, new SameListPred()), true)
        && t.checkExpect(texts.sameILoXML(texts, new SameSetPred()), true)
        && t.checkExpect(texts.sameILoXML(textsScrambled, new SameListPred()), false)
        && t.checkExpect(texts.sameILoXML(textsScrambled, new SameSetPred()), false)
        && t.checkExpect(new ConsLoXML(evermoreTag1, new MtLoXML()).sameILoXML(
            new ConsLoXML(evermoreTag2, new MtLoXML()), new SameListPred()), false)
        && t.checkExpect(new ConsLoXML(evermoreTag1, new MtLoXML()).sameILoXML(
            new ConsLoXML(evermoreTag2, new MtLoXML()), new SameSetPred()), true)
        && t.checkExpect(new MtLoXML().sameILoXML(new MtLoXML(), new SameSetPred()), true)
        && t.checkExpect(texts.sameILoXML(new MtLoXML(), new SameSetPred()), false);
  }

  boolean testSameConsLoXML(Tester t) {
    return
        // an ILoXML should always be the same as itself with either predicate
        t.checkExpect(texts.sameConsLoXML(texts, new SameListPred()), true)
        && t.checkExpect(texts.sameConsLoXML(texts, new SameSetPred()), true)
        // an ILoXML should not be the same as an ILoXML with the same, but scrambled, contents
        // with either predicate
        && t.checkExpect(texts.sameConsLoXML(textsScrambled, new SameListPred()), false)
        && t.checkExpect(texts.sameConsLoXML(textsScrambled, new SameSetPred()), false)
        // an ILoXML containing one Tag should not be the same as an ILoXML containing the same
        // Tag with scrambled Attrs under the SameListPred
        && t.checkExpect(new ConsLoXML(evermoreTag1, new MtLoXML()).sameConsLoXML(
            new ConsLoXML(evermoreTag2, new MtLoXML()), new SameListPred()), false)
        // an ILoXML containing one Tag should be the same as an ILoXML containing the same
        // tag with scrambled Attrs under the SameSetPred
        && t.checkExpect(new ConsLoXML(evermoreTag1, new MtLoXML()).sameConsLoXML(
            new ConsLoXML(evermoreTag2, new MtLoXML()), new SameSetPred()), true);
  }

  boolean testSameMtLoXML(Tester t) {
    return
        // An MtLoXML should always be the same as another MtLoXML
        t.checkExpect(new MtLoXML().sameMtLoXML(new MtLoXML()), true)
        // A ConsLoXML should never be the same as an MtLoXML
        && t.checkExpect(texts.sameMtLoXML(new MtLoXML()), false);
  }
  
  boolean testILoXMLToTextString(Tester t) {
    return
        // An MtLoXML has no Texts, so it should produce an empty String.
        t.checkExpect(new MtLoXML().toTextString(), "")
        // An ILoXML with multiple Texts should produce a String with those Strings concatenated
        // in order, with no separators.
        && t.checkExpect(texts.toTextString(), "loremipsumdolorsitamet");
  }
  
  // on ISameAttrsPred ----------------------------------------------------------------------------
  boolean testCheck(Tester t) {
    return
        // A set of Attrs should be the same as itself under the SameListPred
        t.checkExpect(new SameListPred().check(cssAttrs, cssAttrs), true)
        // A set of Attrs should not be the same as a subset under the SameListPred
        && t.checkExpect(new SameListPred().check(cssAttrs, cssAttrsSubset), false)
        // A set of Attrs should not be the same as a scrambled set under the SameListPred
        && t.checkExpect(new SameListPred().check(cssAttrs, cssAttrsScrambled), false)
        // A set of Attrs should be the same as itself under the SameSetPred
        && t.checkExpect(new SameSetPred().check(cssAttrs, cssAttrs), true)
        // A set of Attrs should not be the same as a subset under the SameSetPred
        && t.checkExpect(new SameSetPred().check(cssAttrs, cssAttrsSubset), false)
        // A set of Attrs should be the same as a scrambled set under the SameSetPred
        && t.checkExpect(new SameSetPred().check(cssAttrs, cssAttrsScrambled), true);
  }
  
  // CONSTRUCTORS ---------------------------------------------------------------------------------
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

