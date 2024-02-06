import tester.Tester;

class Monomial {
  int coefficient;
  int degree;
  
  Monomial(int coefficient, int degree) {
    if (degree < 0) {
      throw new IllegalArgumentException("the degree of a monomial must a be non-negative integer");
    }
    this.degree = degree;
    this.coefficient = coefficient;
  }
  
  // Determines if the coefficient of this monomial is 0
  boolean isZero() {
    return this.coefficient == 0;
  }
  
  // Evaluates this Monomial for the given value of x
  int evaluate(int x) {
    return (int) (this.coefficient * (Math.pow(x, this.degree)));
  }
  
  // Compares this monomial with that. Returns a negative number if this Monomial's degree is
  // less than the degree of that, zero if the degrees are equal, and a positive number this
  // Monomial's degree is greater.
  int compareTo(Monomial that) {
    return this.degree - that.degree;
  }
  
  // Returns a new Monomial representing the sum of this and that. If the degrees of the
  // monomials do not match, throws an exception.
  Monomial add(Monomial that) {
    if (this.degree != that.degree) {
      throw new RuntimeException("cannot add monomials if their degrees do not match!");
    }
    return new Monomial(this.coefficient + that.coefficient, this.degree);
  }
  
  // Multiplies the coefficient of this Monomial by a scalar
  Monomial multiplyScalar(int scalar) {
    return new Monomial(scalar * this.coefficient, this.degree);
  }
  
  Monomial multiplyMonomial(Monomial that) {
    return new Monomial(this.coefficient * that.coefficient, this.degree + that.degree);
  }
}

class Polynomial {
  ILoMonomial monomials;
  
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
  int evaluate(int x) {
    return this.monomials.evaluate(x);
  }
  
  // Mathematically adds another Polynomial to this one, one monomial at a time. If the degrees
  // of two monomials are the same, their coefficients are added; otherwise, both monomials are
  // added separately.
  Polynomial add(Polynomial that) {
    return new Polynomial(this.monomials.addILoMonomial(that.monomials));
  }
  
  // Multiplies each term in this Polynomial by a provided scalar
  Polynomial multiplyScalar(int scalar) {
    return new Polynomial(this.monomials.multiplyScalar(scalar));
  }
  
  // Determines if the value of this polynomial is 0.
  boolean isZero() {
    return this.monomials.isZero();
  }
  
  // Determines if this Polynomial and that Polynomial represent the same polynomial
  boolean samePolynomial(Polynomial that) {
    // TODO: potential solution: make a method to multiply by a scalar, multiply that by -1 and
    //  add to this, expect new Polynomial() [i.e., 0]
    throw new RuntimeException("not done");
  }
  
  // Multiplies this Polynomial with that Polynomial, returning a new Polynomial representing the
  // result.
  Polynomial multiply(Polynomial that) {
    throw new RuntimeException("not done");
  }
}

interface ILoMonomial {
  // Checks if the same degree appears twice in this ILoMonomial
  boolean hasDuplicateDegree();
  
  // Checks if this ILoMonomial has a term with the same degree as the provided term
  boolean hasDegreeOf(Monomial that);
  
  // Normalizes this ILoMonomial by removing all terms with coefficient 0 and sorting by degree,
  // from lowest to highest.
  ILoMonomial normalize();
  
  // Insertion-sorts this ILoMonomial from lowest to highest degree
  ILoMonomial insertionSort();
  
  // Inserts that Monomial into this ILoMonomial immediately before the first Monomial that
  // exceeds its degree
  ILoMonomial insert(Monomial that);
  
  // Removes all Monomials in this ILoMonomial with a coefficient of 0
  ILoMonomial removeZeroes();
  
  // Evaluates all the Monomials in this ILoMonomial and finds their sum
  int evaluate(int x);
  
  // Mathematically adds another ILoMonomial to this one, one monomial at a time. If the degrees
  // of two monomials are the same, their coefficients are added; otherwise, both monomials are
  // added separately.
  ILoMonomial addILoMonomial(ILoMonomial that);
  
  // Mathematically adds a single monomial to this list of monomials, preserving normalization. If
  // it matches the degree of an existing monomial, their coefficients are added; otherwise, it
  // is added separately at an appropriate location.
  ILoMonomial addMonomial(Monomial that);
  
  // Multiplies the coefficient of each term in this ILoMonomial by scalar.
  ILoMonomial multiplyScalar(int scalar);
  
  // Determines if the value of this ILoMonomial is 0; i.e., all of its terms have a coefficient
  // of zero (or it has no terms)
  boolean isZero();
  
  // Multiplies all terms of this ConsLoMonomial by a Monomial.
  ILoMonomial multiplyMonomial(Monomial that);
  
  // Multiplies this ILoMonomial by another ILoMonomial, assuming that both are normalized.
  ILoMonomial multiplyILoMonomial(ILoMonomial that);
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
  // == -2x^4
  ILoMonomial lMon = new ConsLoMonomial(
      minusTwoX4,
      new MtLoMonomial());
  // == 2 - 2x^4
  ILoMonomial lBi = new ConsLoMonomial(
      new Monomial(3, 2),
      lTwo);
  // == -2x^4 + 3x^2 + 2
  ILoMonomial lTri = new ConsLoMonomial(
      new Monomial(-2, 4),
      lBi);
  
  // == 3x^2 + 0x^5 + 2 - 2x^4 + 0x^3
  ILoMonomial lTriRedundant = new ConsLoMonomial(
      threeX2,
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
              minusTwoX4,
              new MtLoMonomial())));
  
  // == 5x - x^2 - 6x^3
  ILoMonomial lTri2 = new ConsLoMonomial(
      new Monomial(5, 1),
      new ConsLoMonomial(
          new Monomial(-1, 2),
          new ConsLoMonomial(
              new Monomial(-6, 3),
              new MtLoMonomial())));
  
  // == -x^2 + 5x + 3x^2
  ILoMonomial lTriDup = new ConsLoMonomial(
      minusOneX2,
      new ConsLoMonomial(
          fiveX1,
          new ConsLoMonomial(
              threeX2,
              new MtLoMonomial())));
  
  Polynomial mt = new Polynomial();
  Polynomial two = new Polynomial(lTwo);
  Polynomial bi = new Polynomial(lBi);
  Polynomial tri = new Polynomial(lTri);
  Polynomial triRedundant = new Polynomial(lTriRedundant);
  Polynomial triNorm = new Polynomial(lTriNorm);
  Polynomial tri2 = new Polynomial(lTri2);
  
  // Tests ========================================================================================
  
  // Monomial -------------------------------------------------------------------------------------
  boolean testIsZeroM(Tester t) {
    return t.checkExpect(threeX2.isZero(), false)
        && t.checkExpect(twoX0.isZero(), false)
        && t.checkExpect(minusTwoX4.isZero(), false)
        && t.checkExpect(zeroX5.isZero(), true);
  }
  
  boolean testEvaluateM(Tester t) {
    return t.checkExpect(threeX2.evaluate(0), 0)
        && t.checkExpect(twoX0.evaluate(1), 2)
        && t.checkExpect(minusTwoX4.evaluate(2), -32)
        && t.checkExpect(fiveX1.evaluate(3), 15)
        && t.checkExpect(minusOneX2.evaluate(4), -16)
        && t.checkExpect(zeroX5.evaluate(5), 0);
  }
  
  boolean testCompareTo(Tester t) {
    return t.checkExpect(threeX2.compareTo(twoX0), 2)
        && t.checkExpect(minusTwoX4.compareTo(fiveX1), 3)
        && t.checkExpect(minusOneX2.compareTo(zeroX5), -3);
  }
  
  boolean testAddM(Tester t) {
    return t.checkExpect(threeX2.add(minusOneX2), new Monomial(2,2))
        && t.checkExpect(zeroX5.add(zeroX5), new Monomial(0,5));
  }
  
  boolean testAddMEx(Tester t) {
    return t.checkNoException(threeX2, "add", threeX2)
        && t.checkException(
            new RuntimeException("cannot add monomials if their degrees do not match!"),
            threeX2, "add", twoX0);
  }
  
  boolean testMultiplyScalarM(Tester t) {
    return t.checkExpect(threeX2.multiplyScalar(1), threeX2)
        && t.checkExpect(twoX0.multiplyScalar(0), new Monomial(0,0))
        && t.checkExpect(minusTwoX4.multiplyScalar(-1), new Monomial(2,4))
        && t.checkExpect(minusOneX2.multiplyScalar(2), new Monomial(-2,2));
  }
  
  boolean testMultiplyMonomialM(Tester t) {
    return t.checkExpect(threeX2.multiplyMonomial(minusOneX2), new Monomial(-3, 4))
        && t.checkExpect(minusTwoX4.multiplyMonomial(zeroX5), new Monomial(0, 9))
        && t.checkExpect(minusTwoX4.multiplyMonomial(minusOneX2), new Monomial(2, 6));
  }
  
  // ILoMonomial ----------------------------------------------------------------------------------
  
  boolean testHasDuplicateDegree(Tester t) {
    return t.checkExpect(lTri.hasDuplicateDegree(), false)
        && t.checkExpect(lTriRedundant.hasDuplicateDegree(), false)
        && t.checkExpect(lTriDup.hasDuplicateDegree(), true)
        && t.checkExpect(new MtLoMonomial().hasDuplicateDegree(), false);
  }
  
  boolean testHasDegreeOf(Tester t) {
    return t.checkExpect(lMt.hasDegreeOf(twoX0), false)
        && t.checkExpect(lBi.hasDegreeOf(minusTwoX4), true)
        && t.checkExpect(lTri.hasDegreeOf(minusOneX2), true);
  }
  
  boolean testNormalize(Tester t) {
    return t.checkExpect(lTri.normalize(), lTriNorm)
        && t.checkExpect(lTriRedundant.normalize(), lTriNorm)
        && t.checkExpect(lTriNorm.normalize(), lTriNorm)
        && t.checkExpect(lMt.normalize(), lMt);
  }
  
  boolean testInsertionSort(Tester t) {
    return t.checkExpect(lMt.insertionSort(), lMt)
        && t.checkExpect(lTriNorm.insertionSort(), lTriNorm)
        && t.checkExpect(lTriRedundant.insertionSort(), new ConsLoMonomial(
            twoX0,
            new ConsLoMonomial(
                threeX2,
                new ConsLoMonomial(
                    zeroX3,
                    new ConsLoMonomial(
                        minusTwoX4,
                        new ConsLoMonomial(
                            zeroX5,
                            new MtLoMonomial()))))));
  }
  
  boolean testInsert(Tester t) {
    return t.checkExpect(lMt.insert(twoX0), lTwo)
        && t.checkExpect(lTriNorm.insert(new Monomial(2, 3)),
        new ConsLoMonomial(
            twoX0,
            new ConsLoMonomial(
                threeX2,
                new ConsLoMonomial(
                    new Monomial(2, 3),
                    new ConsLoMonomial(
                        minusTwoX4,
                        new MtLoMonomial())))));
  }
  
  boolean testRemoveZeroes(Tester t) {
    return t.checkExpect(lTri.removeZeroes(), lTri)
        && t.checkExpect(lTriRedundant.removeZeroes(), lTri)
        && t.checkExpect(new MtLoMonomial().removeZeroes(), new MtLoMonomial());
  }
  
  boolean testEvaluateL(Tester t) {
    return t.checkExpect(lMt.evaluate(22), 0)
        && t.checkExpect(lTwo.evaluate(3), 2)
        && t.checkExpect(lTri.evaluate(3), -133);
  }
  
  boolean testAddILoMonomial(Tester t) {
    return t.checkExpect(lMt.addILoMonomial(lMt), lMt)
        && t.checkExpect(lTwo.addILoMonomial(lTwo), new ConsLoMonomial(
            new Monomial(4, 0),
            new MtLoMonomial()))
        && t.checkExpect(lTriNorm.addILoMonomial(lTri2), new ConsLoMonomial(
            new Monomial(2, 0),
            new ConsLoMonomial(
                new Monomial(5, 1),
                new ConsLoMonomial(
                    new Monomial(2, 2),
                    new ConsLoMonomial(
                        new Monomial(-6, 3),
                        new ConsLoMonomial(
                            new Monomial(-2, 4),
                            new MtLoMonomial()))))))
        && t.checkExpect(
            lTwo.addILoMonomial(new ConsLoMonomial(
                new Monomial(-2, 0),
                new MtLoMonomial())),
            new ConsLoMonomial(new Monomial(0, 0), new MtLoMonomial()));
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
  
  boolean testMultiplyScalarL(Tester t) {
    return t.checkExpect(lMt.multiplyScalar(5), lMt)
        && t.checkExpect(lTwo.multiplyScalar(-3),
            new ConsLoMonomial(new Monomial(-6, 0), new MtLoMonomial()))
        && t.checkExpect(lTri.multiplyScalar(-2),
            new ConsLoMonomial(
                new Monomial(-6, 2),
                new ConsLoMonomial(
                    new Monomial(-4, 0),
                    new ConsLoMonomial(
                        new Monomial(4, 4),
                        new MtLoMonomial()))));
  }
  
  boolean testMultiplyMonomialL(Tester t) {
    return t.checkExpect(lMt.multiplyMonomial(threeX2), new MtLoMonomial())
        && t.checkExpect(lTri.multiplyMonomial(threeX2), new ConsLoMonomial(
            new Monomial(9, 4),
            new ConsLoMonomial(
                new Monomial(6, 2),
                new ConsLoMonomial(
                    new Monomial(-6, 6),
                    new MtLoMonomial()))));
  }
  
  boolean testMultiplyILoMonomial(Tester t) {
    return t.checkExpect(lMt.multiplyILoMonomial(lMt), lMt)
        && t.checkExpect(lMt.multiplyILoMonomial(lTri), lMt)
        && t.checkExpect(lTri.multiplyILoMonomial(lMt), lMt)
        && t.checkExpect(lBi.multiplyILoMonomial(lBi), new ConsLoMonomial(
            new Monomial(4, 0),
            new ConsLoMonomial(
                new Monomial(-8, 4),
                new ConsLoMonomial(
                    new Monomial(4, 8),
                    new MtLoMonomial()))))
        && t.checkExpect(lTri2.multiplyILoMonomial(lBi), new ConsLoMonomial(
            new Monomial(10, 1),
            new ConsLoMonomial(
                new Monomial(-2, 2),
                new ConsLoMonomial(
                    new Monomial(-12, 3),
                    new ConsLoMonomial(
                        new Monomial(-10, 5),
                        new ConsLoMonomial(
                            new Monomial(2, 6),
                            new ConsLoMonomial(
                                new Monomial(12, 7),
                                new MtLoMonomial())))))));
  }
  
  // Polynomial -----------------------------------------------------------------------------------
  
  boolean testConstructor(Tester t) {
    return t.checkExpect(tri, triNorm)
        && t.checkExpect(tri, triRedundant);
  }
  
  boolean testEvaluateP(Tester t) {
    return t.checkExpect(mt.evaluate(22), 0)
        && t.checkExpect(two.evaluate(3), 2)
        && t.checkExpect(tri.evaluate(3), -133);
  }
  
  boolean testAddP(Tester t) {
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
                            new Monomial(-6, 3),
                            new ConsLoMonomial(
                                new Monomial(-2, 4),
                                new MtLoMonomial())))))))
        && t.checkExpect(
            two.add(new Polynomial(
                new ConsLoMonomial(
                    new Monomial(-2, 0),
                    new MtLoMonomial()))),
            new Polynomial());
  }
  
  boolean testMultiplyScalarP(Tester t) {
    return t.checkExpect(mt.multiplyScalar(5), mt)
        && t.checkExpect(two.multiplyScalar(-3),
            new Polynomial(new ConsLoMonomial(new Monomial(-6, 0), new MtLoMonomial())))
        && t.checkExpect(tri.multiplyScalar(-2),
            new Polynomial(
                new ConsLoMonomial(
                    new Monomial(-4, 0),
                    new ConsLoMonomial(
                        new Monomial(-6, 2),
                        new ConsLoMonomial(
                            new Monomial(4, 4),
                            new MtLoMonomial())))));
  }
  
  boolean testIsZeroP(Tester t) {
    return t.checkExpect(new Polynomial().isZero(), true)
        && t.checkExpect(new Polynomial(
            new ConsLoMonomial(minusOneX2, new MtLoMonomial())).isZero(), false)
        // Technically this third test is redundant since Polynomial is simplified in the
        // constructor, but we've included it for completeness
        && t.checkExpect(new Polynomial(
            new ConsLoMonomial(zeroX3,  new MtLoMonomial())).isZero(), true);
  }
  
  boolean testSamePolynomial(Tester t) {
    return t.checkExpect(new Polynomial().samePolynomial(new Polynomial()), true)
        && t.checkExpect(tri.samePolynomial(tri2), false)
        && t.checkExpect(tri.samePolynomial(triNorm), true)
        && t.checkExpect(tri.samePolynomial(triRedundant), true);
  }
  
  boolean testMultiply(Tester t) {
    return t.checkExpect(new Polynomial().multiply(new Polynomial()), new Polynomial())
        && t.checkExpect(new Polynomial().multiply(tri), new Polynomial())
        && t.checkExpect(tri.multiply(new Polynomial()), new Polynomial())
        && t.checkExpect(bi.multiply(bi), new Polynomial(
            new ConsLoMonomial(
                new Monomial(4, 0),
                new ConsLoMonomial(
                    new Monomial(-8, 4),
                    new ConsLoMonomial(
                        new Monomial(4, 8),
                        new MtLoMonomial())))))
        && t.checkExpect(tri2.multiply(bi), new Polynomial(
            new ConsLoMonomial(
                new Monomial(10, 1),
                new ConsLoMonomial(
                    new Monomial(-2, 2),
                    new ConsLoMonomial(
                        new Monomial(-12, 3),
                        new ConsLoMonomial(
                            new Monomial(-10, 5),
                            new ConsLoMonomial(
                                new Monomial(2, 6),
                                new ConsLoMonomial(
                                    new Monomial(12, 7),
                                    new MtLoMonomial()))))))));
  }
  
  // CONSTRUCTORS ---------------------------------------------------------------------------------
  
  boolean testPositiveDegreeValidation(Tester t) {
     return t.checkConstructorNoException("noNegDegree1", "Monomial", -5, 0)
         && t.checkConstructorNoException("noNegDegree2", "Monomial", 3, 6)
         && t.checkConstructorException(
             new IllegalArgumentException("the degree of a monomial must a be non-negative integer"),
             "Monomial", 4, -1);
  }

  boolean testNoDuplicatesValidation(Tester t) {
    return t.checkConstructorNoException("NoDuplicates1", "Polynomial", lTriRedundant)
        && t.checkConstructorNoException("NoDuplicates2", "Polynomial", lMt)
        && t.checkConstructorException(
            new IllegalArgumentException("no two monomials may have the same degree"),
            "Polynomial", lTriDup);
  }
}