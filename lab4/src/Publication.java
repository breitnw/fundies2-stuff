import tester.*;

interface IPublication {
  boolean samePublication(IPublication that);
  boolean sameArticle(Article that);
  boolean sameBook(Book that);
  boolean sameWebpage(Webpage that);
  boolean samePressRelease(PressRelease that);
}

abstract class APublication implements IPublication {
  String title;
  Author author;
  
  APublication(String title, Author author) {
    this.title = title;
    this.author = author;
  }
  
  public boolean sameArticle(Article that) {
    return false;
  }
  
  public boolean sameBook(Book that) {
    return false;
  }
  
  public boolean sameWebpage(Webpage that) {
    return false;
  }
  
  public boolean samePressRelease(PressRelease that) {
    return false;
  }
}

class Article extends APublication {
  int yearPublished;
  
  Article(String title, Author author, int yearPublished) {
    super(title, author);
    this.yearPublished = yearPublished;
  }
  
  public boolean samePublication(IPublication that) {
    return that.sameArticle(this);
  }
  
  public boolean sameArticle(Article that) {
    return this.yearPublished == that.yearPublished
        && this.author.sameAuthor(that.author)
        && this.title.equals(that.title);
  }
}

class PressRelease extends Article {
  String companyName;
  
  PressRelease(String title, Author author, int yearPublished, String companyName) {
    super(title, author, yearPublished);
    this.companyName = companyName;
  }
  
  public boolean samePublication(IPublication that) {
    return that.samePressRelease(this);
  }
  
  public boolean sameArticle(Article that) {
    return false;
  }
  
  public boolean samePressRelease(PressRelease that) {
    return this.yearPublished == that.yearPublished
        && this.author.sameAuthor(that.author)
        && this.title.equals(that.title)
        && this.companyName.equals(that.companyName);
  }
}

class Book extends APublication {
  boolean isSeries;
  
  Book(String title, Author author, boolean isSeries) {
    super(title, author);
    this.isSeries = isSeries;
  }
  
  public boolean samePublication(IPublication that) {
    return that.sameBook(this);
  }
  
  public boolean sameBook(Book that) {
    return this.isSeries == that.isSeries
        && this.author.sameAuthor(that.author)
        && this.title.equals(that.title);
  }
}

class Webpage extends APublication {
  Webpage(String title, Author author) {
    super(title, author);
  }
  
  public boolean samePublication(IPublication that) {
    return that.sameWebpage(this);
  }
  
  public boolean sameWebpage(Webpage that) {
    return this.author.sameAuthor(that.author)
        && this.title.equals(that.title);
  }
}

class Author {
  String name;
  int yob;
  
  Author(String name, int yob) {
    this.name = name;
    this.yob = yob;
  }
  
  boolean sameAuthor(Author that) {
    return this.name.equals(that.name)
        && this.yob == that.yob;
  }
}

class ExamplesPublications {
  Author vRoth = new Author("Veronica Roth", 1988);
  Author jDoe = new Author("John Doe", 0);
  Author breitnw = new Author("Nick Breitling", 2004);
  
  Article a1 = new Article("article 1", jDoe, 2016);
  PressRelease pr1 = new PressRelease("Extended article 1", jDoe,2016, "LinkedIn");
  Book divergent = new Book("Divergent", vRoth, true);
  Webpage github = new Webpage("Github Repository", breitnw);
  
  boolean testSameAuthor(Tester t) {
    return t.checkExpect(vRoth.sameAuthor(vRoth), true)
        && t.checkExpect(jDoe.sameAuthor(vRoth), false);
  }
  
  boolean testSamePublication(Tester t) {
    return t.checkExpect(a1.samePublication(a1), true)
        && t.checkExpect(pr1.samePublication(pr1), true)
        && t.checkExpect(a1.samePublication(pr1), false)
        && t.checkExpect(pr1.samePublication(a1), false)
        && t.checkExpect(divergent.samePublication(divergent), true)
        && t.checkExpect(github.samePublication(github), true)
        && t.checkExpect(pr1.samePublication(a1), false);
  }
}