// This represents a university.
// TODO: come back to this
class University {
  String name; // The name of the University
  String city; // Location of campus.
  int studentSize; // The number of students the University has this year
  String mostPopularMajor; // The most popular major at the University
  double averageGPA; // Average student GPA.
  boolean hasCoop; // Represents whether the university offers a co-op program or not.

  University(String name, String city, int studentSize, String mostPopularMajor, double averageGPA,
             boolean hasCoop) {
    this.name = name;
    this.city = city;
    this.studentSize = studentSize;
    this.mostPopularMajor = mostPopularMajor;
    this.averageGPA = averageGPA;
    this.hasCoop = hasCoop;
  }
}

class ExamplesUniversity {
  University yale = new University("Yale", "New Haven", 14567, "Economics", 4.14, false);
  University neu = new University("Northeastern", "Boston", 19940, "Engineering", 4.04, true);
  University emory = new University("Emory", "Atlanta", 8359,
          "Business Administration and Management", 3.8, true);
  University cornell = new University("Cornell", "Ithaca", 16071,
          "Computer and Information Sciences and Support Services", 4.07, true);
}

