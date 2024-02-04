class Monomial {
  int degree;
  int coefficient;
  
  Monomial(int degree, int coefficient) {
    // TODO: validate non-negative
    this.degree = degree;
    this.coefficient = coefficient;
  }
  
  // Compares this monomial with that. Returns a negative number if this Monomial's degree is
  // less than the degree of that, zero if the degrees are equal, and a positive number this
  // Monomial's degree is greater.
  int compareTo(Monomial that) {
    throw new RuntimeException("not done");
  }
}

class Polynomial {
  ILoMonomial monomials;
  
  Polynomial(ILoMonomial monomials) {
    // TODO: validate none are the same degree
    // TODO: remember to normalize
    this.monomials = monomials;
  }
  
  Polynomial() {
    this(new MtLoMonomial());
  }
  
  // Evaluates a polynomial at a given value of x.
  int evaluate(int x) {
    throw new RuntimeException("not done");
  }
  
  // Mathematically adds another Polynomial to this one, one monomial at a time. If the degrees
  // of two monomials are the same, their coefficients are added; otherwise, both monomials are
  // added separately.
  Polynomial add(Polynomial that) {
    throw new RuntimeException("not done");
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
  // Normalizes this ILoMonomial by removing all terms with coefficient 0 and sorting by degree,
  // from lowest to highest.
  ILoMonomial normalize();
  
  // Mathematically adds another ILoMonomial to this one, one monomial at a time. If the degrees
  // of two monomials are the same, their coefficients are added; otherwise, both monomials are
  // added separately.
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
  
  // Normalizes this ConsLoMonomial by removing all terms with coefficient 0 and sorting by degree,
  // from lowest to highest.
  public ILoMonomial normalize() {
    throw new RuntimeException("not done");
  }
  
  // Mathematically adds another ILoMonomial to this ConsLoMonomial, one monomial at a time. If the
  // degrees of two monomials are the same, their coefficients are added; otherwise, both
  // monomials are added separately.
  public ILoMonomial addILoMonomial(ILoMonomial that) {
    // TODO: maybe?
    // if it works, its necessary to addILoMonomial before we addMonomial because we pull a
    // jiujitsu flip so we'd be infinitely sending elements back and forth otherwise
    return that.addILoMonomial(this.rest).addMonomial(this.first);
  }
  
  // Mathematically adds a single monomial to this ConsLoMonomial, preserving normalization. If
  // it matches the degree of an existing monomial, their coefficients are added; otherwise, it
  // is added separately at an appropriate location.
  public ILoMonomial addMonomial(Monomial that) {
    throw new RuntimeException("not done");
  }
}

class MtLoMonomial implements ILoMonomial {
  // Normalizes this ConsLoMonomial by removing all terms with coefficient 0 and sorting by degree,
  // from lowest to highest.
  public ILoMonomial normalize() {
    throw new RuntimeException("not done");
  }
  
  // Mathematically adds another ILoMonomial to this MtLoMonomial.
  public ILoMonomial addILoMonomial(ILoMonomial that) {
    throw new RuntimeException("not done");
  }
  
  // Mathematically adds a single monomial to this MtLoMonomial, preserving normalization.
  public ILoMonomial addMonomial(Monomial that) {
    throw new RuntimeException("not done");
  }
}

class ExamplesPolynomials {

}