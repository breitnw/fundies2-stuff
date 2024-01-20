import tester.*;

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
  ... this.name ...                                 -- String
  ... this.content ...                              -- ILoContent
  METHODS:
  ... this.totalMegabytes() ...                     -- double
  ... this.totalCredits() ...                       -- int
  ... this.pictureInfo() ...                        -- String
  METHODS ON FIELDS:
  ... this.content.totalMegabytes() ...             -- double
  ... this.content.pictureInfo(String prevInfo) ... -- String
  */
  
  // Returns the total number of megabytes of all the Pictures reachable from this Webpage
  double totalMegabytes() {
    return this.content.totalMegabytes();
  }
  
  // Returns the total credit cost of this Webpage, calculated by calculating the total number of
  // megabytes of all the pictures, rounded up, and multiplying by 50.
  int totalCredits() {
    return (int) Math.ceil(this.totalMegabytes()) * 50;
  }
  
  // Returns the info of all the pictures reachable from this website, separated by commas.
  String pictureInfo() {
    return this.content.pictureInfo("");
  }
}

interface ILoContent {
  /*
  TEMPLATE:
  METHODS:
  ... totalMegabytes() ...               -- double
  ... pictureInfo(String prevInfo) ...   -- String
  */
  
  // Gets the total megabytes of all the pictures reachable from this ILoContent
  double totalMegabytes();
  
  //  Returns the info of all the pictures reachable from this ILoContent, separated by commas.
  String pictureInfo(String prevInfo);
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
  ... this.first ...                                                     -- IContent
  ... this.rest ...                                                      -- ILoContent
  METHODS:
  ... totalMegabytes() ...                                               -- double
  ... pictureInfo(String prevInfo) ...                                   -- String
  METHODS ON FIELDS:
  ... this.first.totalMegabytes() ...                                    -- double
  ... this.rest.totalMegabytes() ...                                     -- double
  ... this.rest.pictureInfo(this.first.pictureInfo(prevInfo)) ...        -- String
  ... this.first.pictureInfo(prevInfo) ...
  */
  
  // Gets the total megabytes of all the pictures reachable from this ConsLoContent
  public double totalMegabytes() {
    return this.first.totalMegabytes() + this.rest.totalMegabytes();
  }
  
  //  Returns prevInfo followed by the info of all the pictures reachable from this ConsLoContent,
  //  separated by commas.
  public String pictureInfo(String prevInfo) {
    /*
    TEMPLATE
    PARAMETERS:
    ... prevInfo ... -- String
     */
    return this.rest.pictureInfo(this.first.pictureInfo(prevInfo));
  }
}

class MtLoContent implements ILoContent {
  /* TEMPLATE:
  METHODS:
  ... totalMegabytes() ...               -- double
  ... pictureInfo(String prevInfo) ...   -- String
   */
  
  // Returns 0, the number of megabytes in an empty list
  public double totalMegabytes() {
    return 0;
  }
  
  // For an empty list, simply returns prevInfo
  public String pictureInfo(String prevInfo) {
    /*
    TEMPLATE
    PARAMETERS:
    ... prevInfo ... -- String
     */
    return prevInfo;
  }
}

interface IContent {
  /* TEMPLATE:
  METHODS:
  ... totalMegabytes() ...               -- double
  ... pictureInfo(String prevInfo) ...   -- String
   */
  
  // Returns the total number of megabytes of this piece of content
  double totalMegabytes();
  
  // Returns prevInfo, followed by the info of this piece of content if it is a Picture. If the
  // previous and new info are both non-empty, then separates them with a comma and a space
  String pictureInfo(String prevInfo);
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
  ... totalMegabytes() ...               -- double
  ... pictureInfo(String prevInfo) ...   -- String
   */
  
  // Returns 0, since text does not contribute toward the megabyte limit
  public double totalMegabytes() {
    return 0;
  }
  
  // Returns prevInfo, since text does not have Picture info.
  public String pictureInfo(String prevInfo) {
    /*
    TEMPLATE
    PARAMETERS:
    ... prevInfo ... -- String
     */
    return prevInfo;
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
  ... toString() ...                     -- String
  ... totalMegabytes() ...               -- double
  ... pictureInfo(String prevInfo) ...   -- String
   */
  
  // Returns a String containing this Picture's name, as well as its description in parentheses.
  public String toString() {
    return this.name + " (" + this.description + ")";
  }
  
  // Returns the number of megabytes it takes to store this image, since a picture contributes to
  // the megabyte limit.
  public double totalMegabytes() {
    return this.megabytes;
  }
  
  // Returns this picture's required information if it is the first picture found,
  // else, it appends this picture's required information to the given string.
  public String pictureInfo(String prevInfo) {
    /*
    TEMPLATE
    PARAMETERS:
    ... prevInfo ...           -- String
    METHODS ON PARAMETERS
    ... prevInfo.isEmpty() ... -- boolean
     */
    if (prevInfo.isEmpty()) {
      return this.toString();
    } else {
      return prevInfo + ", " + this;
    }
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
  ... this.text ...                           -- String
  ... this.destination ...                    -- Webpage
  METHODS:
  ... totalMegabytes() ...                    -- double
  ... pictureInfo(String prevInfo) ...        -- String
  METHODS ON FIELDS:
  ... this.destination.totalMegabytes() ...   -- double
  ... this.destination.pictureInfo() ...      -- String
   */
  
  // Returns the total number of megabytes of the pictures accessible from this Hyperlink
  public double totalMegabytes() {
    return this.destination.totalMegabytes();
  }
  
  // Returns prevInfo, joined with the pictureInfo (if any) of this Hyperlink's destination,
  // separated by a comma and a space if both are non-empty.
  public String pictureInfo(String prevInfo) {
    /* TEMPLATE:
    PARAMETERS:
    ... prevInfo ...             -- String
    METHODS ON PARAMETERS:
    ... prevInfo.isEmpty() ...   -- boolean
     */
    String destInfo = this.destination.pictureInfo();
    if (prevInfo.isEmpty() || destInfo.isEmpty()) {
      return prevInfo + destInfo;
    } else {
      return prevInfo + ", " + this.destination.pictureInfo();
    }
  }
}

class ExamplesWebpages {
  // Cats website examples ----------------------------
  
  // We have a webpage named "Cat Not Found" with the following content:
  // - A text section named "404: The cat you're looking for might not exist" with 1 line that is
  // not
  //   written in markdown
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
  //   that is 1.1 megabytes.
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
  
  // TODO: Do we make check-expects for our helper functions as well?
  boolean testTotalCredits(Tester t) {
    return t.checkExpect(roars.totalCredits(), 100)
             && t.checkExpect(catNotFound.totalCredits(), 0)
             && t.checkExpect(catsHome.totalCredits(), 150);
  }
  
  boolean testPictureInfo(Tester t) {
    return t.checkExpect(
      homepage.pictureInfo(),
      "Eclipse (Eclipse logo), Coding Background (digital rain from the Matrix), "
        + "Java (HD Java logo), Submission (submission screenshot), "
        + "Submission (submission screenshot)"
    ) && t.checkExpect(
      catsHome.pictureInfo(),
      "Tiger (A picture of a big cat in the wild), "
        + "Spot (The First Cat to preside over the Feline Council)"
    );
  }
  
  // The reason that some methods to double-count information is because of the presence of
  // multiple Hyperlinks to one webpage, in which case, the webpage is traversed in more than once,
  // thereby double-counting its information on that specific webpage.
}