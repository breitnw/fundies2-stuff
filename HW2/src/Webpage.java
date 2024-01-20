import tester.*;

class Webpage {
  String name;
  ILoContent content;

  Webpage(String name, ILoContent content) {
    this.name = name;
    this.content = content;
  }

  double totalMegabytes() {
    return this.content.totalMegabytes();
  }

  int totalCredits() {
    return (int)Math.ceil(this.totalMegabytes()) * 50;
  }
}

interface ILoContent {
  double totalMegabytes();
}

class ConsLoContent implements ILoContent {
  IContent first;
  ILoContent rest;

  ConsLoContent(IContent first, ILoContent rest) {
    this.first = first;
    this.rest = rest;
  }

  public double totalMegabytes() {
    return this.first.totalMegabytes() + this.rest.totalMegabytes();
  }
}

class MtLoContent implements ILoContent {
  public double totalMegabytes() {
    return 0;
  }
}

interface IContent {
  double totalMegabytes();
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

  public double totalMegabytes() {
    return 0;
  }
}

class Picture implements IContent {
  String name;
  String description;
  double megabytes;

  Picture(String name, String description, double megabytes) {
    this.name = name;
    this.description = description;
    this.megabytes = megabytes;
  }

  public double totalMegabytes() {
    return this.megabytes;
  }
}

class Hyperlink implements IContent {
  String text;
  Webpage destination;

  Hyperlink(String text, Webpage destination) {
    this.text = text;
    this.destination = destination;
  }

  public double totalMegabytes() {
    return this.destination.totalMegabytes();
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
  // - A picture "Tiger", with description "A picture of a big cat in the wild." that is 1.6
  //   megabytes.
  // - A hyperlink "The Sphinx" to "Cat Not Found"
  // We have a webpage, named "Domesticated Felines" with the following content:
  // - A picture "Spot", with description "The First Cat to preside over the Feline Council."
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
  Picture tiger = new Picture("Tiger", "A picture of a big cat in the wild.", 1.6);
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
  Picture spot = new Picture("Spot",
  "The First Cat to preside over the Feline Council",
  0.6);
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
    "Course Syllabus",
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
  Text expectations = new Text("expectations", 15, false);
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
  Picture eclipse = new Picture("Eclipse", "Eclipse Logo", 0.13);
  Picture codingBackground = new Picture("Coding Background",
  "digital rain from the Matrix",
  30.2);
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
}