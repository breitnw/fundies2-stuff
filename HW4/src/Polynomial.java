import tester.Tester;

// Represents a monomial with a coefficient and a degree, of the form ax^b.
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
  
  /* TEMPLATE
  FIELDS:
  ... this.coefficient ...                       -- int
  ... this.degree ...                            -- int
  METHODS:
  ... this.isZero() ...                          -- boolean
  ... this.evaluate(int x) ...                   -- int
  ... this.compareTo(Monomial that) ...          -- int
  ... this.add(Monomial that) ...                -- Monomial
  ... this.multiplyScalar(int scalar) ...        -- Monomial
  ... this.multiplyMonomial(Monomial that) ...   -- Monomial
   */
  
  // Determines if the coefficient of this monomial is 0
  boolean isZero() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.coefficient == 0;
  }
  
  // Evaluates this Monomial for the given value of x
  int evaluate(int x) {
    /* TEMPLATE
    PARAMETERS:
    ... x ...  -- int
     */
    return (int) (this.coefficient * (Math.pow(x, this.degree)));
  }
  
  // Compares this monomial with that. Returns a negative number if this Monomial's degree is
  // less than the degree of that, zero if the degrees are equal, and a positive number this
  // Monomial's degree is greater.
  int compareTo(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...         -- Monomial
    FIELDS ON PARAMETERS:
    ... that.degree ...  -- int
     */
    return this.degree - that.degree;
  }
  
  // Returns a new Monomial representing the sum of this and that. If the degrees of the
  // monomials do not match, throws an exception.
  Monomial add(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...              -- Monomial
    FIELDS ON PARAMETERS:
    ... that.degree ...       -- int
    ... that.coefficient ...  -- int
     */
    if (this.degree != that.degree) {
      throw new RuntimeException("cannot add monomials if their degrees do not match!");
    }
    return new Monomial(this.coefficient + that.coefficient, this.degree);
  }
  
  // Multiplies the coefficient of this Monomial by a scalar
  Monomial multiplyScalar(int scalar) {
    /* TEMPLATE
    PARAMETERS:
    ... scalar ...  -- int
     */
    return new Monomial(scalar * this.coefficient, this.degree);
  }
  
  // Multiplies this Monomial by another Monomial
  Monomial multiplyMonomial(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...              -- Monomial
    FIELDS ON PARAMETERS
    ... that.coefficient ...  -- int
    ... that.degree ...       -- int
     */
    return new Monomial(this.coefficient * that.coefficient, this.degree + that.degree);
  }
}

// Represents a polynomial whose value is the sum of a list of provided monomials
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
  
  /* TEMPLATE
  FIELDS:
  ... this.monomials ...                                       -- ILoMonomial
  METHODS:
  ... evaluate(int x) ...                                      -- int
  ... add(Polynomial that) ...                                 -- Polynomial
  ... multiplyScalar(int scalar) ...                           -- Polynomial
  ... isZero() ...                                             -- boolean
  ... samePolynomial(Polynomial that) ...                      -- boolean
  ... multiply(Polynomial that) ...                            -- Polynomial
  METHODS ON FIELDS:
  ... this.monomials.evaluate(x) ...                           -- int
  ... this.monomials.addILoMonomial(that.monomials) ...        -- ILoMonomial
  ... this.monomials.multiplyScalar(scalar) ...                -- ILoMonomial
  ... this.monomials.isZero() ...                              -- boolean
  ... this.monomials.multiplyILoMonomial(that.monomials) ...   -- ILoMonomial
   */
  
  // Evaluates a polynomial at a given value of x.
  int evaluate(int x) {
    /* TEMPLATE
    PARAMETERS:
    ... x ...   -- int
     */
    return this.monomials.evaluate(x);
  }
  
  // Mathematically adds another Polynomial to this one, one monomial at a time. If the degrees
  // of two monomials are the same, their coefficients are added; otherwise, both monomials are
  // added separately.
  Polynomial add(Polynomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...             -- Polynomial
    FIELDS ON PARAMETERS:
    ... that.monomials ...   -- ILoMonomial
     */
    return new Polynomial(this.monomials.addILoMonomial(that.monomials));
  }
  
  // Multiplies each term in this Polynomial by a provided scalar
  Polynomial multiplyScalar(int scalar) {
    /* TEMPLATE
    PARAMETERS:
    ... scalar ...   -- int
     */
    return new Polynomial(this.monomials.multiplyScalar(scalar));
  }
  
  // Determines if the value of this polynomial is 0.
  boolean isZero() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.monomials.isZero();
  }
  
  // Determines if this Polynomial and that Polynomial represent the same polynomial
  boolean samePolynomial(Polynomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                      -- Polynomial
    METHODS ON PARAMETERS:
    ... that.multiplyScalar(-1) ...   -- Polynomial
     */
    return this.add(that.multiplyScalar(-1)).isZero();
  }
  
  // Multiplies this Polynomial with that Polynomial, returning a new Polynomial representing the
  // result.
  Polynomial multiply(Polynomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...             -- Polynomial
    FIELDS ON PARAMETERS:
    ... that.monomials ...   -- ILoMonomial
     */
    return new Polynomial(this.monomials.multiplyILoMonomial(that.monomials));
  }
}

// Represents a list of Monomials.
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

// Represents a list of Monomials with a first element and a list of other elements
class ConsLoMonomial implements ILoMonomial {
  Monomial first;
  ILoMonomial rest;
  
  ConsLoMonomial(Monomial first, ILoMonomial rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.first ...                                   -- Monomial
  ... this.rest ...                                    -- Monomial
  METHODS:
  ... this.hasDuplicateDegree() ...                    -- boolean
  ... this.hasDegreeOf(Monomial that) ...              -- boolean
  ... this.normalize() ...                             -- ILoMonomial
  ... this.insertionSort() ...                         -- ILoMonomial
  ... this.insert(Monomial that) ...                   -- ILoMonomial
  ... this.removeZeroes() ...                          -- ILoMonomial
  ... this.evaluate(int x) ...                         -- int
  ... this.addILoMonomial(ILoMonomial that) ...        -- ILoMonomial
  ... this.addMonomial(Monomial that) ...              -- ILoMonomial
  ... this.multiplyScalar(int scalar) ...              -- ILoMonomial
  ... this.isZero() ...                                -- boolean
  ... this.multiplyMonomial(Monomial that) ...         -- ILoMonomial
  ... this.multiplyILoMonomial(Monomial that) ...      -- ILoMonomial
  METHODS ON FIELDS:
  ... this.first.isZero() ...                          -- boolean
  ... this.first.evaluate(int x) ...                   -- int
  ... this.first.compareTo(Monomial that) ...          -- int
  ... this.first.add(Monomial that) ...                -- Monomial
  ... this.first.multiplyScalar(int scalar) ...        -- Monomial
  ... this.first.multiplyMonomial(Monomial that) ...   -- Monomial
  ... this.rest.hasDuplicateDegree() ...               -- boolean
  ... this.rest.hasDegreeOf(Monomial that) ...         -- boolean
  ... this.rest.normalize() ...                        -- ILoMonomial
  ... this.rest.insertionSort() ...                    -- ILoMonomial
  ... this.rest.insert(Monomial that) ...              -- ILoMonomial
  ... this.rest.removeZeroes() ...                     -- ILoMonomial
  ... this.rest.evaluate(int x) ...                    -- int
  ... this.rest.addILoMonomial(ILoMonomial that) ...   -- ILoMonomial
  ... this.rest.addMonomial(Monomial that) ...         -- ILoMonomial
  ... this.rest.multiplyScalar(int scalar) ...         -- ILoMonomial
  ... this.rest.isZero() ...                           -- boolean
  ... this.rest.multiplyMonomial(Monomial that) ...    -- ILoMonomial
  ... this.rest.multiplyILoMonomial(Monomial that) ... -- ILoMonomial
   */
  
  // Checks if the same degree appears twice in this ConsLoMonomial
  public boolean hasDuplicateDegree() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.rest.hasDegreeOf(this.first) || this.rest.hasDuplicateDegree();
  }
  
  // Checks if this ConsLoMonomial has a term with the same degree as the provided term
  public boolean hasDegreeOf(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- Monomial
     */
    return this.first.compareTo(that) == 0 || this.rest.hasDegreeOf(that);
  }
  
  // Normalizes this ConsLoMonomial by removing all terms with coefficient 0 and sorting by degree,
  // from lowest to highest.
  public ILoMonomial normalize() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.insertionSort().removeZeroes();
  }
  
  // Insertion-sorts this ConsLoMonomial from lowest to highest degree
  public ILoMonomial insertionSort() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.rest.insertionSort().insert(this.first);
  }
  
  // Inserts that Monomial into this ConsLoMonomial immediately before the first Monomial that
  // exceeds its degree
  public ILoMonomial insert(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                         -- Monomial
    METHODS ON PARAMETERS:
    ... that.compareTo(this.first) ...   -- int
     */
    if (that.compareTo(this.first) > 0) {
      return new ConsLoMonomial(this.first, this.rest.insert(that));
    } else {
      return new ConsLoMonomial(that, this);
    }
  }
  
  // Removes all Monomials in this ConsLoMonomial with a coefficient of 0
  public ILoMonomial removeZeroes() {
    /* TEMPLATE
    Template: Same as class template.
     */
    if (this.first.isZero()) {
      return this.rest.removeZeroes();
    } else {
      return new ConsLoMonomial(this.first, this.rest.removeZeroes());
    }
  }
  
  // Evaluates all the Monomials in this ILoMonomial and finds their sum
  public int evaluate(int x) {
    /* TEMPLATE
    PARAMETERS:
    ... x ...  -- int
     */
    return this.first.evaluate(x) + this.rest.evaluate(x);
  }
  
  // Mathematically adds another ILoMonomial to this ConsLoMonomial, one monomial at a time. If the
  // degrees of two monomials are the same, their coefficients are added; otherwise, both
  // monomials are added separately.
  public ILoMonomial addILoMonomial(ILoMonomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                   -- ILoMonomial
    METHODS ON FIELDS:
    ... that.addILoMonomial(ILoMonomial that) ...  -- ILoMonomial
     */
    return that.addILoMonomial(this.rest).addMonomial(this.first);
  }
  
  // Mathematically adds a single monomial to this ConsLoMonomial, preserving normalization. If
  // it matches the degree of an existing monomial, their coefficients are added; otherwise, it
  // is added separately at an appropriate location.
  public ILoMonomial addMonomial(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                         -- Monomial
    METHODS ON PARAMETERS:
    ... that.compareTo(this.first) ...   -- int
     */
    if (that.compareTo(this.first) == 0) {
      return new ConsLoMonomial(this.first.add(that), this.rest);
    } else if (that.compareTo(this.first) > 0) {
      return new ConsLoMonomial(this.first, this.rest.addMonomial(that));
    } else {
      return new ConsLoMonomial(that, this);
    }
  }
  
  // Multiplies all the terms in this ILoMonomial by a scalar
  public ILoMonomial multiplyScalar(int scalar) {
    /* TEMPLATE
    PARAMETERS
    ... scalar ...  -- int
     */
    return new ConsLoMonomial(this.first.multiplyScalar(scalar), this.rest.multiplyScalar(scalar));
  }
  
  // Determines if the value of this ConsLoMonomial is 0; i.e., all of its terms have a
  // coefficient of zero.
  public boolean isZero() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this.first.isZero() && this.rest.isZero();
  }
  
  // Multiplies all terms of this ConsLoMonomial by a Monomial.
  public ILoMonomial multiplyMonomial(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...  -- Monomial
     */
    return new ConsLoMonomial(this.first.multiplyMonomial(that), this.rest.multiplyMonomial(that));
  }
  
  // Multiplies this ConsLoMonomial by an ILoMonomial, assuming that both are normalized. 
  public ILoMonomial multiplyILoMonomial(ILoMonomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                  -- ILoMonomial
    METHODS ON PARAMETERS
    ... that.multiplyMonomial(Monomial that) ...  -- ILoMonomial
     */
    return that.multiplyMonomial(this.first).addILoMonomial(this.rest.multiplyILoMonomial(that));
  }
}

// Represents an empty list of Monomials
class MtLoMonomial implements ILoMonomial {
  
  /* TEMPLATE
  FIELDS:
  ... this.first ...                                   -- Monomial
  ... this.rest ...                                    -- Monomial
  METHODS:
  ... this.hasDuplicateDegree() ...                    -- boolean
  ... this.hasDegreeOf(Monomial that) ...              -- boolean
  ... this.normalize() ...                             -- ILoMonomial
  ... this.insertionSort() ...                         -- ILoMonomial
  ... this.insert(Monomial that) ...                   -- ILoMonomial
  ... this.removeZeroes() ...                          -- ILoMonomial
  ... this.evaluate(int x) ...                         -- int
  ... this.addILoMonomial(ILoMonomial that) ...        -- ILoMonomial
  ... this.addMonomial(Monomial that) ...              -- ILoMonomial
  ... this.multiplyScalar(int scalar) ...              -- ILoMonomial
  ... this.isZero() ...                                -- boolean
  ... this.multiplyMonomial(Monomial that) ...         -- ILoMonomial
  ... this.multiplyILoMonomial(Monomial that) ...      -- ILoMonomial
   */
  
  // Checks if the same degree appears twice in this MtLoMonomial
  public boolean hasDuplicateDegree() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return false;
  }
  
  // Checks if this MtLoMonomial has a term with the same degree as the provided term
  public boolean hasDegreeOf(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- Monomial
     */
    return false;
  }
  
  // Normalizes this ConsLoMonomial by removing all terms with coefficient 0 and sorting by degree,
  // from lowest to highest.
  public ILoMonomial normalize() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this;
  }
  
  // Insertion-sorts this MtLoMonomial from lowest to highest degree
  public ILoMonomial insertionSort() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this;
  }
  
  // Inserts that Monomial into this ConsLoMonomial immediately before the first Monomial that
  // exceeds its degree
  public ILoMonomial insert(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- Monomial
     */
    return new ConsLoMonomial(that, new MtLoMonomial());
  }
  
  // Removes all Monomials in this ConsLoMonomial with a coefficient of 0
  public ILoMonomial removeZeroes() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return this;
  }
  
  // Evaluates all the Monomials in this ILoMonomial and finds their sum
  public int evaluate(int x) {
    /* TEMPLATE
    PARAMETERS:
    ... x ...  -- int
     */
    return 0;
  }
  
  // Mathematically adds another ILoMonomial to this MtLoMonomial.
  public ILoMonomial addILoMonomial(ILoMonomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...  -- ILoMonomial
     */
    return that;
  }
  
  // Mathematically adds a single monomial to this MtLoMonomial, preserving normalization.
  public ILoMonomial addMonomial(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...  -- Monomial
     */
    return new ConsLoMonomial(that, new MtLoMonomial());
  }
  
  // Multiplies all the terms in this ILoMonomial by a scalar
  public ILoMonomial multiplyScalar(int scalar) {
    /* TEMPLATE
    PARAMETERS:
    ... scalar ...  -- int
     */
    return this;
  }
  
  // Determines if the value of this MtLoMonomial is zero.
  public boolean isZero() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return true;
  }
  
  // Multiplies this MtLoMonomial by a Monomial.
  public ILoMonomial multiplyMonomial(Monomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...  -- Monomial
     */
    return this;
  }
  
  // Multiplies this MtLoMonomial by an ILoMonomial.
  public ILoMonomial multiplyILoMonomial(ILoMonomial that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...  -- ILoMonomial
     */
    return new MtLoMonomial();
  }
}


class ExamplesPolynomials {
  // Examples =====================================================================================
  
  Monomial threeX2 = new Monomial(3, 2);
  Monomial twoX0 = new Monomial(2,0);
  Monomial minusTwoX4 = new Monomial(-2,4);
  Monomial fiveX1 = new Monomial(5,1);
  Monomial minusOneX2 = new Monomial(-1,2);
  Monomial zeroX5 = new Monomial(0,5);
  Monomial zeroX3 = new Monomial(0,3);
  
  // == 0
  ILoMonomial lMt = new MtLoMonomial();
  // == 2
  ILoMonomial lTwo = new ConsLoMonomial(
      twoX0,
      new MtLoMonomial());
  // == -2x^4
  ILoMonomial lMon = new ConsLoMonomial(
      minusTwoX4,
      new MtLoMonomial());
  // == 2 - 2x^4
  ILoMonomial lBi = new ConsLoMonomial(
      twoX0,
      lMon);
  // == 3x^2 + 2 - 2x^4
  ILoMonomial lTri = new ConsLoMonomial(
      threeX2,
      lBi);
  
  // == 3x^2 + 0x^5 + 2 - 2x^4 + 0x^3
  ILoMonomial lTriRedundant = new ConsLoMonomial(
      threeX2,
      new ConsLoMonomial(
          zeroX5,
          new ConsLoMonomial(
              twoX0,
              new ConsLoMonomial(
                  minusTwoX4,
                  new ConsLoMonomial(
                      zeroX3,
                      new MtLoMonomial())))));
  
  // == 2 + 3x^2 - 2x^4
  ILoMonomial lTriNorm = new ConsLoMonomial(
      twoX0,
      new ConsLoMonomial(
          threeX2,
          new ConsLoMonomial(
              minusTwoX4,
              new MtLoMonomial())));
  
  // == 5x - x^2 - 6x^3
  ILoMonomial lTri2 = new ConsLoMonomial(
      fiveX1,
      new ConsLoMonomial(
          minusOneX2,
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
            new IllegalArgumentException(
                "the degree of a monomial must a be non-negative integer"),
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