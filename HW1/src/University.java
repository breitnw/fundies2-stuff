// Represents academic, geographic, and demographic information about a university
class University {
  String name; // The name of the University
  String city; // The city the campus is located in
  int studentSize; // The number of students the University has this year
  String mostPopularMajor; // The most popular major at the University
  double averageGPA; // The mean student GPA
  boolean hasCoop; // Set to true if the University has a co-op program, otherwise false

  University(String name, String city, int studentSize, String mostPopularMajor, double averageGPA, boolean hasCoop) {
    this.name = name;
    this.city = city;
    this.studentSize = studentSize;
    this.mostPopularMajor = mostPopularMajor;
    this.averageGPA = averageGPA;
    this.hasCoop = hasCoop;
  }

  /* TEMPLATE
     FIELDS:
     ... this.name ...               -- String
     ... this.city ...               -- String
     ... this.studentSize ...        -- int
     ... this.mostPopularMajor ...   -- String
     ... this.averageGPA ...         -- double
     ... this.hasCoop ...            -- boolean
   */
}

class ExamplesUniversity {
  University yale = new University("Yale", "New Haven", 14567, "Economics", 4.14, false);
  University neu = new University("Northeastern", "Boston", 19940, "Engineering", 4.04, true);
  University emory = new University("Emory", "Atlanta", 8359,
          "Business Administration and Management", 3.8, true);
  University cornell = new University("Cornell", "Ithaca", 16071,
          "Computer and Information Sciences and Support Services", 4.07, true);
}

