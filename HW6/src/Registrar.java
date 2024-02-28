import java.util.function.Predicate;

// Represents a course with a name, a professor, and students.
class Course {
  String name;
  Instructor prof;
  IList<Student> students;
  
  Course(String name, Instructor prof) {
    this.name = name;
    this.prof = prof;
    this.students = new Mt<>();
  }
  
  // Updates this course's list of enrolled students to include newStudent, throwing an exception
  // if newStudent is not enrolled in this course.
  // EFFECT: This course is mutated to contain newStudent in its list of students.
  void addStudent(Student newStudent) {
    if (!newStudent.courses.ormap(new SameInstancePred<>(this))) {
      throw new IllegalStateException("Student is not enrolled in this course");
    }
    this.students = new Cons<>(newStudent, this.students);
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
    this.courses = new Mt<>();
  }
  
  // Determines whether the provided student is in more than one of this Instructor's courses
  boolean dejaVu(Student s) {
    return this.courses.countTrue(new HasStudentPred(s)) >= 2;
  }
  
  // Adds the provided course to this instructor's list of courses, throwing an exception if the
  // course is not taught by this instructor.
  void addCourse(Course c) {
    // We use intensional equality, since the course we're adding must point to this Instructor
    // object in particular.
    if (c.prof != this) {
      throw new IllegalStateException("Course is not taught by this instructor");
    }
    this.courses = new Cons<>(c, this.courses);
  }
}

class Student {
  String name;
  int id;
  IList<Course> courses;
  
  Student(String name, int id) {
    this.name = name;
    this.id = id;
    this.courses = new Mt<>();
  }
  
  // Enrolls this student in the provided course.
  // EFFECT: Mutates this student to contain the provided course in its list of courses, and
  // updates the provided course to reflect this student's enrollment.
  void enroll(Course c) {
    this.courses = new Cons<>(c, this.courses);
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
    return this.s.sameStudent(s);
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
class Cons<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  public Cons(T first, IList<T> rest) {
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
class Mt<T> implements IList<T> {
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

}