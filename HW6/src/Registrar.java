import tester.Tester;

import java.util.function.Predicate;

// Represents a course with a name, a professor, and students.
class Course {
  String name;
  Instructor prof;
  IList<Student> students;
  
  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    // A list of students is not provided upon construction because students enroll in a course
    // after the course is created.
    this.students = new MtList<>();
    
    this.prof.addCourse(this);
  }
  
  // Updates this course's list of enrolled students to include newStudent, throwing an exception
  // if newStudent is not enrolled in this course.
  // EFFECT: This course is mutated to contain newStudent in its list of students.
  void addStudent(Student newStudent) {
    if (!newStudent.courses.ormap(new SameInstancePred<>(this))) {
      throw new IllegalStateException("Student is not enrolled in this course");
    }
    this.students = new ConsList<>(newStudent, this.students);
  }
  
  // Determines whether this Course contains a student that matches the provided student
  boolean hasStudent(Student s) {
    return this.students.ormap(new SameStudentPred(s));
  }
}

// Represents a named instructor with a list of courses that they teach
class Instructor {
  String name;
  IList<Course> courses;
  
  Instructor(String name) {
    this.name = name;
    this.courses = new MtList<>();
  }
  
  // Determines whether the provided student is in more than one of this Instructor's courses
  boolean dejavu(Student s) {
    return this.courses.countTrue(new HasStudentPred(s)) >= 2;
  }
  
  // Adds the provided course to this instructor's list of courses, throwing an exception if the
  // course is not taught by this instructor.
  // EFFECT: c is added to the beginning of courses, unless it is not taught by this instructor
  void addCourse(Course c) {
    // We use intensional equality, since the course we're adding must point to this Instructor
    // object in particular.
    if (c.prof != this) {
      throw new IllegalStateException("Course is not taught by this instructor");
    }
    this.courses = new ConsList<>(c, this.courses);
  }
}

class Student {
  String name;
  int id;
  IList<Course> courses;
  
  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new MtList<>();
  }
  
  // Enrolls this student in the provided course.
  // EFFECT: Mutates this student to contain the provided course in its list of courses, and
  // updates the provided course to reflect this student's enrollment.
  void enroll(Course c) {
    this.courses = new ConsList<>(c, this.courses);
    c.addStudent(this);
  }
  
  // Determines if this student is the same as another student by checking if their student IDs
  // are the same.
  // JUSTIFICATION: Since two different students may have the same course list or name, that
  // information is essentially useless if our target is truly certain identification. However,
  // the nature of student IDs dictates that they are unique; hence, they can be used to alone
  // determine sameness among students.
  boolean sameStudent(Student that) {
    return this.id == that.id;
  }
  
  // Determines whether the given Student is in any of the same classes as this Student
  boolean classmates(Student s) {
    return this.courses.ormap(new HasStudentPred(s));
  }
}

// PREDICATES -------------------------------------------------------------------------------------

// A predicate that determines whether the provided student is enrolled in a course.
class HasStudentPred implements Predicate<Course> {
  Student s;
  
  HasStudentPred(Student s) {
    this.s = s;
  }
  
  // Determines whether the provided student is enrolled in a course.
  @Override
  public boolean test(Course that) {
    return that.hasStudent(this.s);
  }
}

// A predicate that determines whether a student is the same (by student ID) as the one provided
// during construction.
class SameStudentPred implements Predicate<Student> {
  Student s;
  
  SameStudentPred(Student s) {
    this.s = s;
  }
  
  // Determines whether a student is the same (by student ID) as the one provided during
  // construction.
  @Override
  public boolean test(Student that) {
    return this.s.sameStudent(that);
  }
}

// A predicate that determines whether this object is the same instance (using intensional
// equality) as that. Only used for validation in constructors to create properly cyclic data.
class SameInstancePred<T> implements Predicate<T> {
  T t;
  
  SameInstancePred(T t) {
    this.t = t;
  }
  
  // Determines whether this object is the same instance (using intensional equality) as that
  @Override
  public boolean test(T t) {
    return this.t == t;
  }
}

// LISTS ------------------------------------------------------------------------------------------

// Represents a list of items of type T.
interface IList<T> {
  // Applies pred to each of the elements of this IList<T> and determines if any of the
  // applications resulted in true.
  boolean ormap(Predicate<T> pred);
  
  // Applies pred to each of the elements of this IList<T>, and returns the number of times it
  // returned true.
  int countTrue(Predicate<T> pred);
}

// Represents a list of items of type T with a first item and a list of other items.
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  public ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
  // Applies pred to each of the elements of this Cons<T> and determines if any of the
  // applications resulted in true.
  public boolean ormap(Predicate<T> pred) {
    return pred.test(first) || this.rest.ormap(pred);
  }
  
  // Applies pred to each of the elements of this Cons<T>, and returns the number of times it
  // returned true.
  public int countTrue(Predicate<T> pred) {
    if (pred.test(this.first)) {
      return 1 + this.rest.countTrue(pred);
    } else {
      return this.rest.countTrue(pred);
    }
  }
}

// Represents an empty list of items of type T.
class MtList<T> implements IList<T> {
  // Applies pred to each of the elements of this Cons<T> and determines if any of the
  // applications resulted in true.
  public boolean ormap(Predicate<T> pred) {
    return false;
  }
  
  // Applies pred to each of the elements of this Mt<T>, and returns the number of times it
  // returned true.
  public int countTrue(Predicate<T> pred) {
    return 0;
  }
}

// EXAMPLES ---------------------------------------------------------------------------------------

class ExamplesRegistrar {
  Student s1;
  Student s2;
  Student s3;
  Student s4;
  Student s5;
  
  Instructor i1;
  Instructor i2;
  
  Course c1;
  Course c2;
  Course c3;
  Course c4;
  Course c5;
  
  void initExamples() {
    s1 = new Student("Harry", 19322);
    s2 = new Student("Ron", 92131);
    s3 = new Student("Hermione", 29931);
    s4 = new Student("Neville", 93042);
    s5 = new Student("Draco", 12384);
    
    i1 = new Instructor("Daniel Patterson");
    i2 = new Instructor("Benjamin Lerner");
    
    // students: s1, s3, s4, s5
    c1 = new Course("Fundamentals 1", i1);
    s1.enroll(c1);
    s3.enroll(c1);
    s4.enroll(c1);
    s5.enroll(c1);
    
    // students: s2
    c2 = new Course("Logic & Computation", i1);
    s2.enroll(c2);
    
    // students: s1, s4, s5
    c3 = new Course("Fundamentals 2", i2);
    s1.enroll(c3);
    s4.enroll(c3);
    s5.enroll(c3);
    
    // students: s1, s2
    c4 = new Course("Compilers", i2);
    s1.enroll(c4);
    s2.enroll(c4);
    
    // students: none
    c5 = new Course("Object-Oriented Design", i2);
  }
  
  // The constructor for course should have added all the courses to their corresponding
  // instructors.
  void testCourseConstructor(Tester t) {
    initExamples();
    t.checkExpect(i1.courses, new ConsList<>(c2, new ConsList<>(c1, new MtList<>())));
    t.checkExpect(i2.courses,
        new ConsList<>(c5, new ConsList<>(c4, new ConsList<>(c3, new MtList<>()))));
  }
  
  void testAddStudent(Tester t) {
    initExamples();
    // Create a new student and manually modify his courses to contain c2
    Student s6 = new Student("George", 23421);
    s6.courses = new ConsList<>(c2, new MtList<>());
    // Add the student to the course
    c2.addStudent(s6);
    // Check that the student was successfully added to the course
    t.checkExpect(c2.students, new ConsList<>(s6, new ConsList<>(s2, new MtList<>())));
    
    // Further modify s6's courses to contain c5
    s6.courses = new ConsList<>(c5, s6.courses);
    // Add the student to the course
    c5.addStudent(s6);
    // Check that the student was successfully added to the course
    t.checkExpect(c5.students, new ConsList<>(s6, new MtList<>()));
  }
  
  void testAddStudentEx(Tester t) {
    initExamples();
    t.checkException(
        new IllegalStateException("Student is not enrolled in this course"),
        c1,
        "addStudent",
        s2);
  }
  
  void testHasStudent(Tester t) {
    initExamples();
    t.checkExpect(c1.hasStudent(s4), true);
    t.checkExpect(c1.hasStudent(new Student("Harry", 19322)), true);
    t.checkExpect(c1.hasStudent(s2), false);
    t.checkExpect(c5.hasStudent(s1), false);
  }
  
  void testDejavu(Tester t) {
    initExamples();
    t.checkExpect(i1.dejavu(s1), false);
    t.checkExpect(i2.dejavu(s1), true);
    t.checkExpect(i2.dejavu(new Student("Harry", 19322)), true);
    t.checkExpect(i2.dejavu(s3), false);
  }
  
  void testAddCourse(Tester t) {
    initExamples();
    // Create a new course and manually modify it to be taught by i1
    Course c6 = new Course("Programming in PowerPoint", i2);
    c6.prof = i1;
    // Add the course to the instructor
    i1.addCourse(c6);
    // Check that the course was successfully added to the instructor
    t.checkExpect(i1.courses,
        new ConsList<>(c6, new ConsList<>(c2, new ConsList<>(c1, new MtList<>()))));
  }
  
  void testAddCourseEx(Tester t) {
    initExamples();
    t.checkException(
        new IllegalStateException("Course is not taught by this instructor"),
        i1,
        "addCourse",
        c4);
  }
  
  void testEnroll(Tester t) {
    initExamples();
    s1.enroll(c2);
    // check that both c2.students and s1.courses were updated
    t.checkExpect(c2.students, new ConsList<>(s1, new ConsList<Student>(s2, new MtList<>())));
    t.checkExpect(s1.courses,
        new ConsList<>(c2,
            new ConsList<>(c4,
                new ConsList<>(c3,
                    new ConsList<>(c1,
                        new MtList<>())))));
    
    s5.enroll(c5);
    // check that both c5.students and s5.courses were updated
    t.checkExpect(c5.students, new ConsList<>(s5, new MtList<>()));
    t.checkExpect(s5.courses,
        new ConsList<>(c5, new ConsList<>(c3, new ConsList<>(c1, new MtList<>()))));
  }
  
  void testSameStudent(Tester t) {
    initExamples();
    t.checkExpect(s1.sameStudent(s1), true);
    t.checkExpect(s1.sameStudent(s2), false);
    t.checkExpect(s1.sameStudent(new Student("Harry", 19322)), true);
    // This next test looks weird, but it's the desired behavior by our definition of sameness
    t.checkExpect(s1.sameStudent(new Student("Larry", 19322)), true);
    t.checkExpect(s1.sameStudent(new Student("Harry", 19324)), false);
    t.checkExpect(s2.sameStudent(new Student("Ron", 92131)), true);
  }
  
  void testClassmates(Tester t) {
    initExamples();
    t.checkExpect(s1.classmates(s2), true);
    t.checkExpect(s2.classmates(s1), true);
    t.checkExpect(s1.classmates(s1), true);
    t.checkExpect(s5.classmates(s2), false);
    t.checkExpect(s3.classmates(s2), false);
    // TODO: take a look at this behavior... sign that we should change notion of equality?
    t.checkExpect(s3.classmates(new Student("Harry", 19322)), true);
    t.checkExpect(new Student("Harry", 19322).classmates(s3), false);
  }
  
  void testHasStudentPred(Tester t) {
    initExamples();
    t.checkExpect(new HasStudentPred(s4).test(c1), true);
    t.checkExpect(new HasStudentPred(new Student("Harry", 19322)).test(c1), true);
    t.checkExpect(new HasStudentPred(s2).test(c1), false);
    t.checkExpect(new HasStudentPred(s1).test(c5), false);
  }
  
  void testSameStudentPred(Tester t) {
    initExamples();
    t.checkExpect(new SameStudentPred(s1).test(s1), true);
    t.checkExpect(new SameStudentPred(s2).test(s1), false);
    t.checkExpect(new SameStudentPred(new Student("Harry", 19322)).test(s1), true);
    t.checkExpect(new SameStudentPred(new Student("Larry", 19322)).test(s1), true);
    t.checkExpect(new SameStudentPred(new Student("Harry", 19324)).test(s1), false);
    t.checkExpect(new SameStudentPred(new Student("Ron", 92131)).test(s2), true);
  }
  
  void testSameInstancePred(Tester t) {
    initExamples();
    t.checkExpect(new SameInstancePred<>(s1).test(s1), true);
    t.checkExpect(new SameInstancePred<>(s2).test(s2), true);
    t.checkExpect(new SameInstancePred<>(s2).test(s1), false);
    t.checkExpect(new SameInstancePred<>(new Student("Harry", 19322))
                      .test(new Student("Harry", 19322)), false);
    t.checkExpect(new SameInstancePred<>(c1).test(c1), true);
    t.checkExpect(new SameInstancePred<>(i1).test(i1), true);
  }
  
  void testOrmap(Tester t) {
    initExamples();
    
    IList<Student> l1 = new MtList<>();
    IList<Student> l2 = new ConsList<>(s1,
        new ConsList<>(s2,
            new ConsList<>(s3,
                new ConsList<>(s4,
                    new MtList<>()))));
    
    t.checkExpect(l1.ormap(new SameStudentPred(s2)), false);
    t.checkExpect(l2.ormap(new SameStudentPred(new Student("Ron", 92131))), true);
    t.checkExpect(l2.ormap(new SameStudentPred(s5)), false);
    t.checkExpect(l1.ormap(new SameInstancePred<>(s2)), false);
    t.checkExpect(l2.ormap(new SameInstancePred<>(new Student("Ron", 92131))), false);
    t.checkExpect(l2.ormap(new SameInstancePred<>(s2)), true);
  }
  
  void testCountTrue(Tester t) {
    initExamples();
    
    IList<Student> l1 = new MtList<>();
    IList<Student> l2 = new ConsList<>(s1,
        new ConsList<>(s2,
            new ConsList<>(s3,
                new ConsList<>(new Student("Ron", 92131),
                    new MtList<>()))));
    
    t.checkExpect(l1.countTrue(new SameStudentPred(s1)), 0);
    t.checkExpect(l2.countTrue(new SameStudentPred(s2)), 2);
    t.checkExpect(l2.countTrue(new SameStudentPred(s1)), 1);
    t.checkExpect(l2.countTrue(new SameStudentPred(new Student("Ron", 92131))), 2);
    t.checkExpect(l1.countTrue(new SameInstancePred<>(s2)), 0);
    t.checkExpect(l2.countTrue(new SameInstancePred<>(s2)), 1);
  }
}