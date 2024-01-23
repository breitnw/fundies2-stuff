import tester.*;

// A page on a website with a given name and list of contents
class Webpage {
  String name;
  ILoContent content;
  
  Webpage(String name, ILoContent content) {
    this.name = name;
    this.content = content;
  }
  
  /*
  TEMPLATE
  FIELDS:
  ... this.name ...                                         -- String
  ... this.content ...                                      -- ILoContent
  METHODS:
  ... this.totalMegabytes() ...                             -- double
  ... this.totalMegabytesHelper(ILoContent traversed) ...   -- double
  ... this.totalCredits() ...                               -- int
  ... this.pictureInfo() ...                                -- String
  ... this.pictureInfoHelper(ILoContent traversed) ...      -- String
  ... this.containsPictureInfo(String info) ...             -- boolean
  METHODS ON FIELDS:
  ... this.content.totalMegabytes(new MtLoContent) ...      -- double
  ... this.content.totalMegabytes(traversed) ...            -- double
  ... this.content.pictureInfo(String traversed) ...        -- String
  ... this.content.pictureInfo("", new MtLoContent()) ...   -- String
  ... this.content.pictureInfo("", traversed) ...           -- String
  ... this.content.containsPictureInfo(info) ...            -- boolean
  */
  
  // Returns the total number of megabytes of all the Pictures reachable from this Webpage,
  // omitting double-counted Pictures.
  double totalMegabytes() {
    return this.content.totalMegabytes(new MtLoContent());
  }
  
  // Returns the total number of megabytes of all the Pictures reachable from this Webpage,
  // omitting double-counted Pictures or Pictures already present in traversed.
  double totalMegabytesHelper(ILoContent traversed) {
    /* TEMPLATE:
    PARAMETERS:
    ... traversed ...   -- ILoContent
     */
    return this.content.totalMegabytes(traversed);
  }
  
  // Returns the total credit cost of this Webpage, calculated by calculating the total number of
  // megabytes of all the pictures, rounded up, and multiplying by 50.
  int totalCredits() {
    return (int) Math.ceil(this.totalMegabytes()) * 50;
  }
  
  // Returns the info of all the pictures reachable from this website, separated by commas,
  // omitting double-counted pictures.
  String pictureInfo() {
    return this.content.pictureInfo("", new MtLoContent());
  }
  
  // Returns the info of all the pictures reachable from this website, separated by commas,
  // omitting double-counted pictures or those already present in traversed.
  String pictureInfoHelper(ILoContent traversed) {
    /* TEMPLATE:
    PARAMETERS:
    ... traversed ...   -- ILoContent
     */
    return this.content.pictureInfo("", traversed);
  }
  
  // Returns true if this Website's content contains a picture whose info evaluates to the given
  // String, or a Hyperlink leading to such a picture. Otherwise, returns false.
  boolean containsPictureInfo(String info) {
    /* TEMPLATE:
    PARAMETERS:
    ... info ...   -- String
     */
    return this.content.containsPictureInfo(info);
  }
}

interface ILoContent {
  /*
  TEMPLATE:
  METHODS:
  ... totalMegabytes(ILoContent traversed) ...                -- double
  ... pictureInfo(String accInfo, ILoContent traversed) ...   -- String
  ... containsPictureInfo(String info) ...                    -- boolean
  */
  
  // Gets the total megabytes of all the pictures reachable from this ILoContent, omitting
  // pictures already present in traversed.
  double totalMegabytes(ILoContent traversed);
  
  // Returns the info of all the pictures reachable from this ILoContent, separated by commas,
  // omitting double-counts and pictures already present in traversed.
  String pictureInfo(String accInfo, ILoContent traversed);
  
  // Returns true if this ILoContent contains a picture whose info evaluates to the given
  // String, or a Hyperlink leading to such a picture. Otherwise, returns false.
  boolean containsPictureInfo(String info);
}

class ConsLoContent implements ILoContent {
  IContent first;
  ILoContent rest;
  
  ConsLoContent(IContent first, ILoContent rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /* TEMPLATE:
  FIELDS:
  ... this.first ...                                                           -- IContent
  ... this.rest ...                                                            -- ILoContent
  METHODS:
  ... totalMegabytes(ILoContent traversed) ...                                 -- double
  ... pictureInfo(String accInfo, ILoContent traversed) ...                    -- String
  ... containsPictureInfo(String info) ...                                     -- boolean
  METHODS ON FIELDS:
  ... this.first.totalMegabytes(traversed) ...                                 -- double
  ... this.rest.totalMegabytes(new ConsLoContent(this.first, traversed)) ...   -- double
  ... this.rest.pictureInfo(this.first.pictureInfo(accInfo, traversed)) ...    -- String
  ... this.first.pictureInfo(accInfo, traversed) ...                           -- String
  ... this.first.matchesPictureInfo(info) ...                                  -- boolean
  ... this.rest.containsPictureInfo(info) ...                                  -- boolean
  */
  
  // Gets the total megabytes of all the pictures reachable from this ConsLoContent
  public double totalMegabytes(ILoContent traversed) {
    /* TEMPLATE:
    PARAMETERS:
    ... traversed ...   -- ILoContent
     */
    return this.first.totalMegabytes(traversed)
               + this.rest.totalMegabytes(new ConsLoContent(this.first, traversed));
  }
  
  // Returns accInfo followed by the info of all the pictures reachable from this ConsLoContent,
  // separated by commas.
  public String pictureInfo(String accInfo, ILoContent traversed) {
    /*
    TEMPLATE
    PARAMETERS:
    ... accInfo ...    -- String
    ... traversed...   -- ILoContent
     */
    return this.rest.pictureInfo(this.first.pictureInfo(accInfo, traversed),
        new ConsLoContent(this.first, traversed));
  }
  
  // Returns true if this ILoContent contains a picture whose info evaluates to the given
  // String, or a Hyperlink leading to such a picture. Otherwise, returns false.
  public boolean containsPictureInfo(String info) {
    /* TEMPLATE:
    PARAMETERS:
    ... info ...   -- String
     */
    return this.first.matchesPictureInfo(info) || this.rest.containsPictureInfo(info);
  }
}

class MtLoContent implements ILoContent {
  /* TEMPLATE:
  METHODS:
  ... totalMegabytes(ILoContent traversed) ...                -- double
  ... pictureInfo(String accInfo, ILoContent traversed) ...   -- String
  ... containsPictureInfo() ...                               -- boolean
   */
  
  // Returns 0, the number of megabytes in an empty list
  public double totalMegabytes(ILoContent traversed) {
    /* TEMPLATE:
    PARAMETERS:
    ... traversed ...   -- ILoContent
     */
    return 0;
  }
  
  // For an empty list, simply returns accInfo, the picture info accumulated throughout the
  // traversal
  public String pictureInfo(String accInfo, ILoContent traversed) {
    /*
    TEMPLATE
    PARAMETERS:
    ... accInfo ...     -- String
    ... traversed ...   -- ILoContent
     */
    return accInfo;
  }
  
  // Always returns false, since an MtLoString cannot contain a Picture
  public boolean containsPictureInfo(String info) {
    /* TEMPLATE:
    PARAMETERS:
    ... info ...   -- String
     */
    return false;
  }
}

interface IContent {
  /* TEMPLATE:
  METHODS:
  ... totalMegabytes(ILoContent traversed) ...                -- double
  ... pictureInfo(String accInfo, ILoContent traversed) ...   -- String
  ... matchesPictureInfo(String info) ...                     -- boolean
   */
  
  // Returns the total number of megabytes of this piece of content
  double totalMegabytes(ILoContent traversed);
  
  // Returns accInfo, followed by the info of this piece of content if it is a Picture. If the
  // previous and new info are both non-empty, then separates them with a comma and a space
  String pictureInfo(String accInfo, ILoContent traversed);
  
  // Returns true if this IContent is a Picture whose info (the resulting string when toString()
  // is invoked) matches info, or if this IContent is a Hyperlink that eventually leads to such a
  // Picture. Otherwise, returns false.
  boolean matchesPictureInfo(String info);
}

class Text implements IContent {
  String name;
  int numLines;
  boolean inMarkdown;
  
  Text(String name, int numLines, boolean inMarkdown) {
    this.name = name;
    this.numLines = numLines;
    this.inMarkdown = inMarkdown;
  }
  
  /* TEMPLATE:
  FIELDS:
  ... this.name ...                      -- String
  ... this.numLines ...                  -- int
  ... this.inMarkdown ...                -- boolean
  METHODS;
  ... totalMegabytes(ILoContent traversed) ...                -- double
  ... pictureInfo(String accInfo, ILoContent traversed) ...   -- String
  ... matchesPictureInfo(String info) ...                     -- boolean
   */
  
  // Returns 0, since text does not contribute toward the megabyte limit
  public double totalMegabytes(ILoContent traversed) {
    /* TEMPLATE:
    PARAMETERS:
    ... traversed ...   -- ILoContent
     */
    return 0;
  }
  
  // Returns traversed, since text does not have Picture info.
  public String pictureInfo(String accInfo, ILoContent traversed) {
    /*
    TEMPLATE
    PARAMETERS:
    ... accInfo ...     -- String
    ... traversed ...   -- ILoContent
     */
    return accInfo;
  }
  
  // Returns false, since a Text can never contain a Picture matching the given info.
  public boolean matchesPictureInfo(String info) {
    /* TEMPLATE:
    PARAMETERS:
    ... info ...   -- String
     */
    return false;
  }
}

class Picture implements IContent {
  String name; // Name of this image.
  String description; // Description about this image.
  double megabytes; // Amount of Megabytes it takes to store this image.
  
  Picture(String name, String description, double megabytes) {
    this.name = name;
    this.description = description;
    this.megabytes = megabytes;
  }
  /* TEMPLATE:
  FIELDS:
  ... this.name ...                      -- String
  ... this.description ...               -- String
  ... this.megabytes ...                 -- double
  METHODS:
  ... toString() ...                                          -- String
  ... totalMegabytes(ILoContent traversed) ...                -- double
  ... pictureInfo(String accInfo, ILoContent traversed) ...   -- String
  ... matchesPictureInfo(String info) ...                     -- boolean
   */
  
  // Returns a String containing this Picture's name, as well as its description in parentheses.
  public String toString() {
    return this.name + " (" + this.description + ")";
  }
  
  // Returns the number of megabytes it takes to store this image, since a picture contributes to
  // the megabyte limit.
  public double totalMegabytes(ILoContent traversed) {
    /* TEMPLATE:
    PARAMETERS:
    ... traversed ...                                        -- ILoContent
    METHODS ON PARAMETERS:
    ... traversed.containsPictureInfo(this.toString()) ...   -- boolean
     */
    if (traversed.containsPictureInfo(this.toString())) {
      return 0;
    } else {
      return this.megabytes;
    }
  }
  
  // If the list of traversed contents contains this picture's info, simply returns the
  // existing info. Otherwise, if no info has been accumulated, returns this picture's required
  // information, else this picture's required information appended to the given string.
  public String pictureInfo(String accInfo, ILoContent traversed) {
    /*
    TEMPLATE
    PARAMETERS:
    ... accInfo ...                                          -- String
    ... traversed ...                                        -- ILoContent
    METHODS ON PARAMETERS:
    ... traversed.containsPictureInfo(this.toString()) ...   -- boolean
    ... accInfo.isEmpty() ...                                -- boolean
     */
    if (traversed.containsPictureInfo(this.toString())) {
      return accInfo;
    } else if (accInfo.isEmpty()) {
      return this.toString();
    } else {
      return accInfo + ", " + this;
    }
  }
  
  // Returns true if this Picture's info (the resulting string when toString() is invoked)
  // matches info.
  public boolean matchesPictureInfo(String info) {
    /* TEMPLATE:
    PARAMETERS:
    ... info ...   -- String
     */
    return this.toString().equals(info);
  }
}

// Represents a Hyperlink as a content component on a Webpage.
class Hyperlink implements IContent {
  String text;
  Webpage destination;
  
  Hyperlink(String text, Webpage destination) {
    this.text = text;
    this.destination = destination;
  }
  /* TEMPLATE:
  FIELDS:
  ... this.text ...                                                  -- String
  ... this.destination ...                                           -- Webpage
  METHODS:
  ... totalMegabytes(ILoContent traversed) ...                       -- double
  ... pictureInfo(String accInfo, ILoContent traversed) ...          -- String
  ... matchesPictureInfo(String info) ...                            -- boolean
  METHODS ON FIELDS:
  ... this.destination.totalMegabytesHelper(traversed) ...           -- double
  ... this.destination.pictureInfoHelper(traversed) ...              -- String
  ... this.destination.containsPictureInfo(info) ...                 -- boolean
   */
  
  // Returns the total number of megabytes of the pictures accessible from this Hyperlink
  public double totalMegabytes(ILoContent traversed) {
    /* TEMPLATE:
    PARAMETERS:
    ... traversed ...   -- ILoContent
     */
    return this.destination.totalMegabytesHelper(traversed);
  }
  
  // Returns traversed, joined with the pictureInfo (if any) of this Hyperlink's destination,
  // separated by a comma and a space if both are non-empty.
  public String pictureInfo(String accInfo, ILoContent traversed) {
    /* TEMPLATE:
    PARAMETERS:
    ... accInfo ...             -- String
    ... traversed ...           -- ILoContent
    METHODS ON PARAMETERS:
    ... accInfo.isEmpty() ...   -- boolean
     */
    String destInfo = this.destination.pictureInfoHelper(traversed);
    if (accInfo.isEmpty() || destInfo.isEmpty()) {
      return accInfo + destInfo;
    } else {
      return accInfo + ", " + destInfo;
    }
  }
  
  // Returns true if this Hyperlink eventually leads to a Picture whose info matches the given
  // info. Otherwise, returns false.
  public boolean matchesPictureInfo(String info) {
    /* TEMPLATE:
    PARAMETERS:
    ... info ...   -- String
     */
    return this.destination.containsPictureInfo(info);
  }
}

class ExamplesWebpages {
  // Cats website examples ----------------------------
  
  // We have a webpage named "Cat Not Found" with the following content:
  // - A text section named "404: The cat you're looking for might not exist" with 1 line that is
  // not written in markdown
  // We have a webpage named "Cat's Home" with the following content:
  // - A hyperlink "The bigger ones" to "Big Cats"
  // - A hyperlink "The smaller ones" to "Domesticated Felines"
  // - A text section named "Cat-egories", with 16 lines that are written in Markdown.
  // We have a webpage, named "Big Cats" with the following content:
  // - A picture "Tiger", with description "A picture of a big cat in the wild" that is 1.6
  //   megabytes.
  // - A hyperlink "The Sphinx" to "Cat Not Found"
  // We have a webpage, named "Domesticated Felines" with the following content:
  // - A picture "Spot", with description "The First Cat to preside over the Feline Council"
  //   that is 0.6 megabytes.
  // - A hyperlink "Cheshire Cat" to "Cat Not Found"
  
  // 404 page
  Text notFoundText = new Text("The cat you're looking for might not exist", 1, false);
  Webpage catNotFound = new Webpage(
      "404",
      new ConsLoContent(
          notFoundText,
          new MtLoContent()
      )
  );
  
  // Big cats page
  Picture tiger = new Picture("Tiger", "A picture of a big cat in the wild", 1.6);
  Hyperlink sphinx = new Hyperlink("The Sphinx", catNotFound);
  
  Webpage roars = new Webpage(
      "Big Cats",
      new ConsLoContent(
          tiger,
          new ConsLoContent(
              sphinx,
              new MtLoContent()
          )
      )
  );
  
  // Domesticated felines page
  Picture spot = new Picture(
      "Spot",
      "The First Cat to preside over the Feline Council",
      0.6
  );
  Hyperlink cheshire = new Hyperlink("Cheshire Cat", catNotFound);
  
  Webpage mews = new Webpage(
      "Domesticated Felines",
      new ConsLoContent(
          spot,
          new ConsLoContent(
              cheshire,
              new MtLoContent()
          )
      )
  );
  
  // Home page
  Hyperlink bigCats = new Hyperlink("The Bigger Ones", roars);
  Hyperlink domesticatedFelines = new Hyperlink("The Smaller Ones", mews);
  Text categories = new Text("Cat-egories", 16, true);
  
  Webpage catsHome = new Webpage(
      "Cat's Home",
      new ConsLoContent(
          bigCats,
          new ConsLoContent(
              domesticatedFelines,
              new ConsLoContent(
                  categories,
                  new MtLoContent()
              )
          )
      )
  );
  
  // Fundies 2 website examples ----------------------------
  
  // Assignment 1 page
  Picture submission = new Picture("Submission", "submission screenshot", 13.7);
  
  Webpage assignment1 = new Webpage(
      "Assignment 1",
      new ConsLoContent(
          submission,
          new MtLoContent()
      )
  );
  
  // Syllabus page
  Picture java = new Picture("Java", "HD Java logo", 4.);
  Text week1 = new Text("Week 1", 10, true);
  Hyperlink firstAssignment = new Hyperlink("First Assignment", assignment1);
  
  Webpage syllabus = new Webpage(
      "Syllabus",
      new ConsLoContent(
          java,
          new ConsLoContent(
              week1,
              new ConsLoContent(
                  firstAssignment,
                  new MtLoContent()
              )
          )
      )
  );
  
  // Assignments page
  Text pairProgramming = new Text("Pair Programming", 10, false);
  Text expectations = new Text("Expectations", 15, false);
  // First assignment already defined
  
  Webpage assignments = new Webpage(
      "Assignments",
      new ConsLoContent(
          pairProgramming,
          new ConsLoContent(
              expectations,
              new ConsLoContent(
                  firstAssignment,
                  new MtLoContent()
              )
          )
      )
  );
  
  // Homepage
  Text courseGoals = new Text("Course Goals", 5, true);
  Text instructorContact = new Text("Instructor Contact", 1, false);
  Picture eclipse = new Picture("Eclipse", "Eclipse logo", 0.13);
  Picture codingBackground = new Picture(
      "Coding Background",
      "digital rain from the Matrix",
      30.2
  );
  Hyperlink courseSyllabus = new Hyperlink("Course Syllabus", syllabus);
  Hyperlink courseAssignments = new Hyperlink("Course Assignments", assignments);
  
  Webpage homepage = new Webpage(
      "Fundies 2 Homepage",
      new ConsLoContent(
          courseGoals,
          new ConsLoContent(
              instructorContact,
              new ConsLoContent(
                  eclipse,
                  new ConsLoContent(
                      codingBackground,
                      new ConsLoContent(
                          courseSyllabus,
                          new ConsLoContent(
                              courseAssignments,
                              new MtLoContent()
                          )
                      )
                  )
              )
          )
      )
  );
  
  boolean testTotalMegabytesWebpage(Tester t) {
    return t.checkInexact(catsHome.totalMegabytes(), 2.2, 0.001)
               && t.checkInexact(homepage.totalMegabytes(), 48.03, 0.001);
  }
  
  boolean testTotalMegabytesHelperWebpage(Tester t) {
    return t.checkInexact(catsHome.totalMegabytesHelper(new MtLoContent()), 2.2, 0.001)
        && t.checkInexact(homepage.totalMegabytesHelper(new MtLoContent()), 48.03, 0.001);
  }
  
  boolean testTotalCredits(Tester t) {
    return t.checkExpect(roars.totalCredits(), 100)
               && t.checkExpect(catNotFound.totalCredits(), 0)
               && t.checkExpect(catsHome.totalCredits(), 150)
               && t.checkExpect(homepage.totalCredits(), 2450);
  }
  
  boolean testPictureInfo(Tester t) {
    return t.checkExpect(
        homepage.pictureInfo(),
        "Eclipse (Eclipse logo), Coding Background (digital rain from the Matrix), "
            + "Java (HD Java logo), Submission (submission screenshot)"
    ) && t.checkExpect(
        catsHome.pictureInfo(),
        "Tiger (A picture of a big cat in the wild), "
            + "Spot (The First Cat to preside over the Feline Council)"
    );
  }
  
  boolean testPictureInfoHelperWebpage(Tester t) {
    return t.checkExpect(catsHome.pictureInfoHelper(new MtLoContent()),
        "Tiger (A picture of a big cat in the wild), Spot (The First Cat to preside over the Feline Council)")
        && t.checkExpect(homepage.pictureInfoHelper(new MtLoContent()), "");
  }
  
  boolean testTotalMegabytesILoContent(Tester t) {
    return t.checkInexact(catsHome.content.totalMegabytes(new MtLoContent()), 2.2, 0.001)
               && t.checkInexact(catNotFound.content.totalMegabytes(new MtLoContent()), 0.0, 0.001);
  }
  
  boolean testPictureInfoILoContent(Tester t) {
    return t.checkExpect(
        homepage.content.pictureInfo("", new MtLoContent()),
        "Eclipse (Eclipse logo), Coding Background (digital rain from the Matrix), "
            + "Java (HD Java logo), Submission (submission screenshot)")
               && t.checkExpect(catNotFound.content.pictureInfo("", new MtLoContent()), "");
  }
  
  boolean testTotalMegabytesIContent(Tester t) {
    return t.checkExpect(categories.totalMegabytes(new MtLoContent()), 0.0)
               && t.checkExpect(tiger.totalMegabytes(new MtLoContent()), 1.6)
               && t.checkExpect(sphinx.totalMegabytes(new MtLoContent()), 0.0);
  }
  
  boolean testPictureInfoIContent(Tester t) {
    return t.checkExpect(categories.pictureInfo("text", new MtLoContent()), "text")
               && t.checkExpect(tiger.pictureInfo("", new MtLoContent()),
          "Tiger (A picture of a big cat in the wild)")
               && t.checkExpect(tiger.pictureInfo("text", new MtLoContent()),
          "text, Tiger (A picture of a big cat in the wild)")
               && t.checkExpect(sphinx.pictureInfo("hyperlink", new MtLoContent()), "hyperlink")
               && t.checkExpect(domesticatedFelines.pictureInfo("hyperlink", new MtLoContent()),
          "hyperlink, Spot (The First Cat to preside over the Feline Council)");
  }
  
  boolean testToStringPicture(Tester t) {
    return t.checkExpect(tiger.toString(), "Tiger (A picture of a big cat in the wild)");
  }
  
  // DOUBLE-COUNTING ------------------------------------------------------------------------------
  
  // The reason that some methods to double-count information is because of the presence of
  // multiple Hyperlinks to one webpage, in which case, the webpage is traversed in more than once,
  // thereby double-counting its information on that specific webpage.
}