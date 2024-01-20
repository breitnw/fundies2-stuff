/*
      +-------------+
      | IStudyGroup |
      +-------------+
       /\         /\
      /__\       /__\
       ||         ||
       ||         ||
     Person   StudyBuddy
 */

// Represents the group of people committed together in a "study group".
interface IStudyGroup {
}

// A Person (which is a type of IStudyGroup) represents the primary person in a study group.
class Person implements IStudyGroup {
  String name; // Name of this Person.

  Person(String name) {
    this.name = name;
  }
  /* TEMPLATE
     FIELDS:
     ... this.name ...   -- String
   */
}

// A StudyBuddy (which is a type of IStudyGroup) represents a secondary member of the study
// group, as well as the rest of the study group.
class StudyBuddy implements IStudyGroup {
  IStudyGroup connection; // The rest of the study group who is with this StudyBuddy.
  String name; // The name of this StudyBuddy.

  StudyBuddy(IStudyGroup connection, String name) {
    this.connection = connection;
    this.name = name;
  }
  /* TEMPLATE
     FIELDS:
     ... this.connection ...   -- IStudyGroup
     ... this.name ...         -- String
   */
}

class ExamplesStudyGroup {
  IStudyGroup labs = new StudyBuddy(new Person("Margaryta"), "Regan");
  IStudyGroup largeGroup = new StudyBuddy(new StudyBuddy(new StudyBuddy(new Person("Ted"), "Liz"),
          "Jenny"), "Cornelius");
}