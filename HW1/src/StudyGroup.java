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

interface IStudyGroup {
}

class Person implements IStudyGroup {
  String name;

  Person(String name) {
    this.name = name;
  }
  /* TEMPLATE
     FIELDS:
     ... this.name ...   -- String
   */
}

class StudyBuddy implements IStudyGroup {
  IStudyGroup connection;
  String name;

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