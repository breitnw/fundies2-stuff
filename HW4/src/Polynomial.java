import tester.Tester;

class Monomial {
  int coefficient;
  int degree;
  
  // TODO: needs tests (exception)
  Monomial(int coefficient, int degree) {
    if (degree < 0) {
      throw new IllegalArgumentException("the degree of a monomial must a be non-negative integer");
    }
    this.degree = degree;
    this.coefficient = coefficient;
  }
  
  // Determines if the coefficient of this monomial is 0
  // TODO: needs tests
  boolean isZero() {
    return this.coefficient == 0;
  }
  
  // Evaluates this Monomial for the given value of x
  // TODO: needs tests
  int evaluate(int x) {
    return (int) (this.coefficient * (Math.pow(x, this.degree)));
  }
  
  // Compares this monomial with that. Returns a negative number if this Monomial's degree is
  // less than the degree of that, zero if the degrees are equal, and a positive number this
  // Monomial's degree is greater.
  // TODO: needs tests
  int compareTo(Monomial that) {
    return this.degree - that.degree;
  }
  
  // Returns a new Monomial representing the sum of this and that. If the degrees of the
  // monomials do not match, throws an exception.
  // TODO: needs tests
  // TODO: needs tests (exception)
  Monomial add(Monomial that) {
    if (this.degree != that.degree) {
      throw new RuntimeException("cannot add monomials if their degrees do not match!");
    }
    return new Monomial(this.coefficient + that.coefficient, this.degree);
  }
}

class Polynomial {
  ILoMonomial monomials;
  
  // TODO: needs tests (exception)
  Polynomial(ILoMonomial monomials) {
    if (monomials.hasDuplicateDegree()) {
      throw new IllegalArgumentException("no two monomials may have the same degree");
    }
    this.monomials = monomials.normalize();
  }
  
  Polynomial() {
    this(new MtLoMonomial());
  }
  
  // Evaluates a polynomial at a given value of x.
  // TODO: needs tests
  int evaluate(int x) {
    return this.monomials.evaluate(x);
  }
  
  // Mathematically adds another Polynomial to this one, one monomial at a time. If the degrees
  // of two monomials are the same, their coefficients are added; otherwise, both monomials are
  // added separately.
  Polynomial add(Polynomial that) {
    return new Polynomial(this.monomials.addILoMonomial(that.monomials));
  }
  
  // Determines if this Polynomial and that Polynomial represent the same polynomial
  // TODO: needs tests
  boolean samePolynomial(Polynomial that) {
    // TODO: potential solution: make a method to multiply by a scalar, multiply that by -1 and
    //  add to this, expect new Polynomial() [i.e., 0]
    throw new RuntimeException("not done");
  }
  
  // Multiplies this Polynomial with that Polynomial, returning a new Polynomial representing the
  // result.
  // TODO: needs tests
  Polynomial multiply(Polynomial that) {
    throw new RuntimeException("not done");
  }
}

interface ILoMonomial {
  // Checks if the same degree appears twice in this ILoMonomial
  // TODO: needs tests
  boolean hasDuplicateDegree();
  
  // Checks if this ILoMonomial has a term with the same degree as the provided term
  // TODO: needs tests
  boolean hasDegreeOf(Monomial that);
  
  // Normalizes this ILoMonomial by removing all terms with coefficient 0 and sorting by degree,
  // from lowest to highest.
  
  ILoMonomial normalize();
  
  // Insertion-sorts this ILoMonomial from lowest to highest degree
  // TODO: needs tests
  ILoMonomial insertionSort();
  
  // Inserts that Monomial into this ILoMonomial immediately before the first Monomial that
  // exceeds its degree
  // TODO: needs tests
  ILoMonomial insert(Monomial that);
  
  // Removes all Monomials in this ILoMonomial with a coefficient of 0
  // TODO: needs tests
  ILoMonomial removeZeroes();
  
  // Evaluates all the Monomials in this ILoMonomial and finds their sum
  int evaluate(int x);
  
  // Mathematically adds another ILoMonomial to this one, one monomial at a time. If the degrees
  // of two monomials are the same, their coefficients are added; otherwise, both monomials are
  // added separately.
  // TODO: needs tests
  ILoMonomial addILoMonomial(ILoMonomial that);
  
  // Mathematically adds a single monomial to this list of monomials, preserving normalization. If
  // it matches the degree of an existing monomial, their coefficients are added; otherwise, it
  // is added separately at an appropriate location.
  ILoMonomial addMonomial(Monomial that);
}

class ConsLoMonomial implements ILoMonomial {
  Monomial first;
  ILoMonomial rest;
  
  ConsLoMonomial(Monomial first, ILoMonomial rest) {
    this.first = first;
    this.rest = rest;
  }
  
  // Checks if the same degree appears twice in this ConsLoMonomial
  public boolean hasDuplicateDegree() {
    return this.rest.hasDegreeOf(this.first) || this.rest.hasDuplicateDegree();
  }
  
  // Checks if this ConsLoMonomial has a term with the same degree as the provided term
  public boolean hasDegreeOf(Monomial that) {
    return this.first.compareTo(that) == 0 || this.rest.hasDegreeOf(that);
  }
  
  // Normalizes this ConsLoMonomial by removing all terms with coefficient 0 and sorting by degree,
  // from lowest to highest.
  public ILoMonomial normalize() {
    return this.insertionSort().removeZeroes();
  }
  
  // Insertion-sorts this ConsLoMonomial from lowest to highest degree
  public ILoMonomial insertionSort() {
    return this.rest.insertionSort().insert(this.first);
  }
  
  // Inserts that Monomial into this ConsLoMonomial immediately before the first Monomial that
  // exceeds its degree
  public ILoMonomial insert(Monomial that) {
    if (that.compareTo(this.first) > 0) {
      return new ConsLoMonomial(this.first, this.rest.insert(that));
    } else {
      return new ConsLoMonomial(that, this);
    }
  }
  
  // Removes all Monomials in this ConsLoMonomial with a coefficient of 0
  public ILoMonomial removeZeroes() {
    if (this.first.isZero()) {
      return this.rest.removeZeroes();
    } else {
      return new ConsLoMonomial(this.first, this.rest.removeZeroes());
    }
  }
  
  // Evaluates all the Monomials in this ILoMonomial and finds their sum
  public int evaluate(int x) {
    return this.first.evaluate(x) + this.rest.evaluate(x);
  }
  
  // Mathematically adds another ILoMonomial to this ConsLoMonomial, one monomial at a time. If the
  // degrees of two monomials are the same, their coefficients are added; otherwise, both
  // monomials are added separately.
  public ILoMonomial addILoMonomial(ILoMonomial that) {
    return that.addILoMonomial(this.rest).addMonomial(this.first);
  }
  
  // Mathematically adds a single monomial to this ConsLoMonomial, preserving normalization. If
  // it matches the degree of an existing monomial, their coefficients are added; otherwise, it
  // is added separately at an appropriate location.
  public ILoMonomial addMonomial(Monomial that) {
    if (that.compareTo(this.first) == 0) {
      return new ConsLoMonomial(this.first.add(that), this.rest);
    } else if (that.compareTo(this.first) > 0) {
      return new ConsLoMonomial(this.first, this.rest.addMonomial(that));
    } else {
      return new ConsLoMonomial(that, this);
    }
  }
}

class MtLoMonomial implements ILoMonomial {
  // Checks if the same degree appears twice in this MtLoMonomial
  public boolean hasDuplicateDegree() {
    return false;
  }
  
  // Checks if this MtLoMonomial has a term with the same degree as the provided term
  public boolean hasDegreeOf(Monomial that) {
    return false;
  }
  
  // Normalizes this ConsLoMonomial by removing all terms with coefficient 0 and sorting by degree,
  // from lowest to highest.
  public ILoMonomial normalize() {
    return this;
  }
  
  // Insertion-sorts this MtLoMonomial from lowest to highest degree
  public ILoMonomial insertionSort() {
    return this;
  }
  
  // Inserts that Monomial into this ConsLoMonomial immediately before the first Monomial that
  // exceeds its degree
  public ILoMonomial insert(Monomial that) {
    return new ConsLoMonomial(that, new MtLoMonomial());
  }
  
  // Removes all Monomials in this ConsLoMonomial with a coefficient of 0
  public ILoMonomial removeZeroes() {
    return this;
  }
  
  // Evaluates all the Monomials in this ILoMonomial and finds their sum
  public int evaluate(int x) {
    return 0;
  }
  
  // Mathematically adds another ILoMonomial to this MtLoMonomial.
  public ILoMonomial addILoMonomial(ILoMonomial that) {
    return that;
  }
  
  // Mathematically adds a single monomial to this MtLoMonomial, preserving normalization.
  public ILoMonomial addMonomial(Monomial that) {
    return new ConsLoMonomial(that, new MtLoMonomial());
  }
}

class ExamplesPolynomials {
  // == 0
  ILoMonomial lMt = new MtLoMonomial();
  // == 2
  ILoMonomial lTwo = new ConsLoMonomial(
      new Monomial(2, 0),
      new MtLoMonomial());
  // == 3x^2 + 2
  ILoMonomial lBi = new ConsLoMonomial(
      new Monomial(3, 2),
      lTwo);
  // == -2x^4 + 3x^2 + 2
  ILoMonomial lTri = new ConsLoMonomial(
      new Monomial(-2, 4),
      lBi);
  // == 3x^2 + 0x^5 + 2 - 2x^4
  ILoMonomial lTriRedundant = new ConsLoMonomial(
      new Monomial(3, 2),
      new ConsLoMonomial(
          new Monomial(0, 5),
          new ConsLoMonomial(
              new Monomial(2, 0),
              new ConsLoMonomial(
                  new Monomial(-2, 4),
                  new MtLoMonomial()
              )
          )
      )
  );
  // == 2 + 3x^2 - 2x^4
  ILoMonomial lTriNorm = new ConsLoMonomial(
      new Monomial(2, 0),
      new ConsLoMonomial(
          new Monomial(3, 2),
          new ConsLoMonomial(
              new Monomial(-2, 4),
              new MtLoMonomial()
          )
      )
  );
  // == 5x - x^2 - 6x^5
  ILoMonomial lTri2 = new ConsLoMonomial(
      new Monomial(5, 1),
      new ConsLoMonomial(
          new Monomial(-1, 2),
          new ConsLoMonomial(
              new Monomial(-6, 5),
              new MtLoMonomial()
          )
      )
  );
  
  Polynomial mt = new Polynomial();
  Polynomial two = new Polynomial(lTwo);
  Polynomial bi = new Polynomial(lBi);
  Polynomial tri = new Polynomial(lTri);
  Polynomial triRedundant = new Polynomial(lTriRedundant);
  Polynomial triNorm = new Polynomial(lTriNorm);
  Polynomial tri2 = new Polynomial(lTri2);
  
  boolean testEvaluate(Tester t) {
    return t.checkExpect(lMt.evaluate(22), 0)
        && t.checkExpect(lTwo.evaluate(3), 2)
        && t.checkExpect(lTri.evaluate(3), -133);
  }
  
  boolean testNormalize(Tester t) {
    return t.checkExpect(lTri.normalize(), lTriNorm)
        && t.checkExpect(lTriRedundant.normalize(), lTriNorm)
        && t.checkExpect(lTriNorm.normalize(), lTriNorm)
        && t.checkExpect(lMt.normalize(), lMt);
  }
  
  boolean testAdd(Tester t) {
    return t.checkExpect(mt.add(mt), mt)
        && t.checkExpect(two.add(two), new Polynomial(
            new ConsLoMonomial(
                new Monomial(4, 0),
                new MtLoMonomial())))
        && t.checkExpect(tri.add(tri2), new Polynomial(
            new ConsLoMonomial(
                new Monomial(2, 0),
                new ConsLoMonomial(
                    new Monomial(5, 1),
                    new ConsLoMonomial(
                        new Monomial(2, 2),
                        new ConsLoMonomial(
                            new Monomial(-2, 4),
                            new ConsLoMonomial(
                                new Monomial(-6, 5),
                                new MtLoMonomial())))))))
        && t.checkExpect(
            two.add(new Polynomial(
                new ConsLoMonomial(
                    new Monomial(-2, 0),
                    new MtLoMonomial()))),
            new Polynomial());
  }
  
  boolean testAddMonomial(Tester t) {
    return t.checkExpect(lMt.addMonomial(new Monomial(2, 4)),
            new ConsLoMonomial(
                new Monomial(2, 4),
                new MtLoMonomial()))
        && t.checkExpect(lTriNorm.addMonomial(new Monomial(-1, 2)),
            new ConsLoMonomial(
                new Monomial(2, 0),
                new ConsLoMonomial(
                    new Monomial(2, 2),
                    new ConsLoMonomial(
                        new Monomial(-2, 4),
                        new MtLoMonomial()))));
  }
}