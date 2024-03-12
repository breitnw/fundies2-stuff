import java.util.function.*;
import tester.*;

// Represents an arithmetic expression
interface IArith {
  // Accepts an IArithVisitor aCnd applies its according function to this IArith.
  <R> R accept(IArithVisitor<R> that);
}

// Represents a constant in an arithmetic expression
class Const implements IArith {
  double num;
  
  Const(double num) {
    this.num = num;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.num ...                        -- double
  METHODS:
  ... accept(IArithVisitor<R> that) ...   -- R
   */
  
  // Accepts an IArithVisitor and applies its according function to this IArith.
  public <R> R accept(IArithVisitor<R> that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                    -- IArithVisitor<R>
    METHODS ON PARAMETERS:
    ... that.visitConst(this) ...   -- R
     */
    return that.visitConst(this);
  }
}

// Represents a unary operation in an arithmetic expression
class UnaryFormula implements IArith {
  Function<Double, Double> func;
  String name;
  IArith child;
  
  /* TEMPLATE
  FIELDS:
  ... this.func ...                       -- Function<Double, Double>
  ... this.name ...                       -- String
  ... this.child ...                      -- IArith
  METHODS:
  ... accept(IArithVisitor<R> that) ...   -- R
   */
  
  // TODO: validate formula names?
  UnaryFormula(Function<Double, Double> func, String name, IArith child) {
    this.func = func;
    this.name = name;
    this.child = child;
  }
  
  // Accepts an IArithVisitor and applies its visitUnaryFormula function to this UnaryFormula.
  public <R> R accept(IArithVisitor<R> that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                           -- IArithVisitor<R>
    METHODS ON PARAMETERS:
    ... that.visitUnaryFormula(this) ...   -- R
     */
    return that.visitUnaryFormula(this);
  }
}

// Represents a binary operation in an arithmetic expression
class BinaryFormula implements IArith {
  BiFunction<Double, Double, Double> func;
  String name;
  IArith left;
  IArith right;
  
  BinaryFormula(BiFunction<Double, Double, Double> func, String name, IArith left, IArith right) {
    this.func = func;
    this.name = name;
    this.left = left;
    this.right = right;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.func ...                       -- BiFunction<Double, Double, Double>
  ... this.name ...                       -- String
  ... this.left ...                       -- IArith
  ... this.right ...                      -- IArith
  METHODS:
  ... accept(IArithVisitor<R> that) ...   -- R
   */
  
  // Accepts an IArithVisitor and applies its visitBinaryFormula function to this BinaryFormula.
  public <R> R accept(IArithVisitor<R> that) {
    /* TEMPLATE:
    PARAMETERS:
    ... that ...                            -- IArithVisitor<R>
    METHODS ON PARAMETERS:
    ... that.visitBinaryFormula(this) ...   -- R
     */
    return that.visitBinaryFormula(this);
  }
}

// Represents an addition operation between two Doubles
class Plus implements BiFunction<Double, Double, Double> {
  /* TEMPLATE
  METHODS:
  ... apply(Double aDouble, Double aDouble2) ...   -- Double
   */
  
  // Applies the addition operation to two Doubles
  public Double apply(Double aDouble, Double aDouble2) {
    /* TEMPLATE
    PARAMETERS:
    ... aDouble ...    -- Double
    ... aDouble2 ...   -- Double
     */
    return aDouble + aDouble2;
  }
}

// Represents a subtraction operation between two Doubles
class Minus implements BiFunction<Double, Double, Double> {
  /* TEMPLATE
  METHODS:
  ... apply(Double aDouble, Double aDouble2) ...   -- Double
   */
  
  // Applies the subtraction operation to two Doubles
  public Double apply(Double aDouble, Double aDouble2) {
    /* TEMPLATE
    PARAMETERS:
    ... aDouble ...    -- Double
    ... aDouble2 ...   -- Double
     */
    return aDouble - aDouble2;
  }
}

// Represents a multiplication operation between two Doubles
class Mul implements BiFunction<Double, Double, Double> {
  /* TEMPLATE
  METHODS:
  ... apply(Double aDouble, Double aDouble2) ...   -- Double
   */
  
  // Applies the multiplication operation to two Doubles
  public Double apply(Double aDouble, Double aDouble2) {
    /* TEMPLATE
    PARAMETERS:
    ... aDouble ...    -- Double
    ... aDouble2 ...   -- Double
     */
    return aDouble * aDouble2;
  }
}

// Represents a division operation between two Doubles
class Div implements BiFunction<Double, Double, Double> {
  /* TEMPLATE
  METHODS:
  ... apply(Double aDouble, Double aDouble2) ...   -- Double
   */
  
  // Applies the division operation to two Doubles
  public Double apply(Double aDouble, Double aDouble2) {
    /* TEMPLATE
    PARAMETERS:
    ... aDouble ...    -- Double
    ... aDouble2 ...   -- Double
     */
    return aDouble / aDouble2;
  }
}

// Represents a negation operation on a Double
class Neg implements Function<Double, Double> {
  /* TEMPLATE
  METHODS:
  ... apply(Double aDouble) ...   -- Double
   */
  
  // Applies the negation operation to a Double
  public Double apply(Double aDouble) {
    /* TEMPLATE
    PARAMETERS:
    ... aDouble ...   -- Double
     */
    return -aDouble;
  }
}

// Represents a squaring operation on a Double
class Sqr implements Function<Double, Double> {
  /* TEMPLATE
 METHODS:
 ... apply(Double aDouble) ...   -- Double
  */
  
  // Applies the squaring operation to a Double
  public Double apply(Double aDouble) {
    /* TEMPLATE
    PARAMETERS:
    ... aDouble ...   -- Double
     */
    return aDouble * aDouble;
  }
}

// Represents a visitor to the IArith class
interface IArithVisitor<R> extends Function<IArith, R> {
  // Applies this function to the provided Const
  R visitConst(Const that);
  
  // Applies this function to the provided UnaryFormula
  R visitUnaryFormula(UnaryFormula that);
  
  // Applies this function to the provided BinaryFormula
  R visitBinaryFormula(BinaryFormula that);
}

// Represents a visitor to IArith that evaluates the provided IArith to produce a Double
class EvalVisitor implements IArithVisitor<Double> {
  /* TEMPLATE
  METHODS:
  ... visitConst(Const that) ...                   -- Double
  ... visitUnaryFormula(UnaryFormula that) ...     -- Double
  ... visitBinaryFormula(BinaryFormula that) ...   -- Double
  ... apply(IArith that) ...                       -- Double
   */
  
  // Evaluates the provided Const to produce a Double
  public Double visitConst(Const that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- Const
    FIELDS ON PARAMETERS:
    ... that.num ...   -- double
     */
    return that.num;
  }
  
  // Evaluates the provided UnaryFormula to produce a Double
  public Double visitUnaryFormula(UnaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- UnaryFormula
    FIELDS ON PARAMETERS:
    ... that.func ...  -- Function<Double, Double>
    ... that.child ... -- IArith
     */
    return that.func.apply(this.apply(that.child));
  }
  
  // Evaluates the provided BinaryFormula to produce a Double
  public Double visitBinaryFormula(BinaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- BinaryFormula
    FIELDS ON PARAMETERS:
    ... that.func ...  -- BiFunction<Double, Double, Double>
    ... that.left ...  -- IArith
    ... that.right ... -- IArith
     */
    return that.func.apply(this.apply(that.left), this.apply(that.right));
  }
  
  // Applies this EvalVisitor to a provided IArith, evaluating the IArith to produce a Double
  public Double apply(IArith that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                -- IArith
    METHODS ON PARAMETERS:
    ... that.accept(IArithVisitor<R> that) ...  -- R
     */
    return that.accept(this);
  }
}

// Represents a visitor to IArith that produces a String showing the fully-parenthesized
// expression in Racket-like prefix notation
class PrintVisitor implements IArithVisitor<String> {
  /* TEMPLATE
  METHODS:
  ... visitConst(Const that) ...                   -- Double
  ... visitUnaryFormula(UnaryFormula that) ...     -- Double
  ... visitBinaryFormula(BinaryFormula that) ...   -- Double
  ... apply(IArith that) ...                       -- Double
   */
  
  // Produces a String showing the provided Const
  public String visitConst(Const that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- Const
    FIELDS ON PARAMETERS:
    ... that.num ...   -- double
     */
    return Double.toString(that.num);
  }
  
  // Produces a String showing the provided UnaryFormula in fully-parenthesized, Racket-like
  // prefix notation
  public String visitUnaryFormula(UnaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- UnaryFormula
    FIELDS ON PARAMETERS:
    ... that.func ...  -- Function<Double, Double>
    ... that.child ... -- IArith
     */
    return String.format("(%s %s)", that.name, this.apply(that.child));
  }
  
  // Produces a String showing the provided BinaryFormula in fully-parenthesized, Racket-like
  // prefix notation
  public String visitBinaryFormula(BinaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- BinaryFormula
    FIELDS ON PARAMETERS:
    ... that.name ...  -- String
    ... that.left ...  -- IArith
    ... that.right ... -- IArith
    */
    return String.format("(%s %s %s)", that.name, this.apply(that.left), this.apply(that.right));
  }
  
  // Applies this PrintVisitor to a provided IArith, producing a String showing the
  // fully-parenthesized expression in Racket-like prefix notation
  public String apply(IArith that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                -- IArith
    METHODS ON PARAMETERS:
    ... that.accept(IArithVisitor<R> that) ...  -- R
     */
    return that.accept(this);
  }
}

// Represents a visitor to IArith that and produces another IArith, where every Const in
// the tree has been doubled
class DoublerVisitor implements IArithVisitor<IArith> {
  /* TEMPLATE
  METHODS:
  ... visitConst(Const that) ...                   -- Double
  ... visitUnaryFormula(UnaryFormula that) ...     -- Double
  ... visitBinaryFormula(BinaryFormula that) ...   -- Double
  ... apply(IArith that) ...                       -- Double
   */
  
  // Visits a Const and produces another Const with a doubled value
  public IArith visitConst(Const that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- Const
    FIELDS ON PARAMETERS:
    ... that.num ...   -- double
     */
    return new Const(2 * that.num);
  }
  
  // Visits a UnaryFormula and produces another UnaryFormula where every constant in the tree has
  // been doubled.
  public IArith visitUnaryFormula(UnaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...         -- UnaryFormula
    FIELDS ON PARAMETERS:
    ... that.func ...    -- Function<Double, Double>
    ... that.name ...    -- String
    ... that.child ...   -- IArith
     */
    return new UnaryFormula(that.func, that.name, this.apply(that.child));
  }
  
  // Visits a BinaryFormula and produces another BinaryFormula where every constant in the tree has
  // been doubled.
  public IArith visitBinaryFormula(BinaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...         -- BinaryFormula
    FIELDS ON PARAMETERS:
    ... that.func ...    -- BiFunction<Double, Double, Double>
    ... that.name ...    -- String
    ... that.left ...    -- IArith
    ... that.right ...   -- IArith
     */
    return new BinaryFormula(that.func, that.name, this.apply(that.left), this.apply(that.right));
  }
  
  // Applies this DoublerVisitor to a provided IArith, producing another IArith, where every
  // Const in the tree has been doubled
  public IArith apply(IArith that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                -- IArith
    METHODS ON PARAMETERS:
    ... that.accept(IArithVisitor<R> that) ...  -- R
     */
    return that.accept(this);
  }
}

// Represents a visitor to IArith that produces a Boolean that is true if every constant in the
// tree is less than 10.
class AllSmallVisitor implements IArithVisitor<Boolean> {
  /* TEMPLATE
  METHODS:
  ... visitConst(Const that) ...                   -- Double
  ... visitUnaryFormula(UnaryFormula that) ...     -- Double
  ... visitBinaryFormula(BinaryFormula that) ...   -- Double
  ... apply(IArith that) ...                       -- Double
   */
  
  // Determines if the provided Const is less than 10.
  public Boolean visitConst(Const that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- Const
    FIELDS ON PARAMETERS:
    ... that.num ...   -- double
     */
    return that.num < 10;
  }
  
  // Visits a UnaryFormula and determines if every constant in its tree is less than 10.
  public Boolean visitUnaryFormula(UnaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...         -- UnaryFormula
    FIELDS ON PARAMETERS:
    ... that.child ...   -- IArith
     */
    return this.apply(that.child);
  }
  
  // Visits a BinaryFormula and determines if every constant in its tree is less than 10.
  public Boolean visitBinaryFormula(BinaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...         -- BinaryFormula
    FIELDS ON PARAMETERS:
    ... that.left ...    -- IArith
    ... that.right ...   -- IArith
     */
    return this.apply(that.left) && this.apply(that.right);
  }
  
  // Applies this AllSmallVisitor to a provided IArith, producing a Boolean that is true if every
  // constant in the tree is less than 10.
  public Boolean apply(IArith that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                -- IArith
    METHODS ON PARAMETERS:
    ... that.accept(IArithVisitor<R> that) ...  -- R
     */
    return that.accept(this);
  }
}

// Represents a visitor to IArith producing a Boolean that is true if anywhere there is a
// formula named "div", the right argument does not evaluate to roughly zero.
class NoDivBy0 implements IArithVisitor<Boolean> {
  /* TEMPLATE
  METHODS:
  ... visitConst(Const that) ...                   -- Double
  ... visitUnaryFormula(UnaryFormula that) ...     -- Double
  ... visitBinaryFormula(BinaryFormula that) ...   -- Double
  ... apply(IArith that) ...                       -- Double
   */
  
  // Returns true, since that Const does not have any formulas named "div" where the right
  // argument does not evaluate to roughly zero
  public Boolean visitConst(Const that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- Const
     */
    return true;
  }
  
  // Produces a Boolean that is true if anywhere there is a formula named "div" in that
  // UnaryFormula, the right argument does not evaluate to roughly zero.
  public Boolean visitUnaryFormula(UnaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- UnaryFormula
    FIELDS ON PARAMETERS:
    ... that.child ... -- IArith
     */
    return this.apply(that.child);
  }
  
  // Produces a Boolean that is true if anywhere there is a formula named "div" in that
  // BinaryFormula, the right argument does not evaluate to roughly zero.
  public Boolean visitBinaryFormula(BinaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- BinaryFormula
    FIELDS ON PARAMETERS:
    ... that.name ...   -- String
    ... that.right ... -- IArith
    ... that.left ...  -- IArith
     */
    if (that.name.equals("div") && Math.abs(new EvalVisitor().apply(that.right)) < 0.001) {
      return false;
    }
    return this.apply(that.left) && this.apply(that.right);
  }
  
  // Applies this NoDivBy0 to a provided IArith, producing a Boolean that is true if
  // anywhere there is a formula named "div", the right argument does not evaluate to roughly zero.
  public Boolean apply(IArith that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                -- IArith
    METHODS ON PARAMETERS:
    ... that.accept(IArithVisitor<R> that) ...  -- R
     */
    return that.accept(this);
  }
}

// Represents a visitor to IArith producing a Boolean that is true if a negative number is
// never encountered at any point during the evaluation of the IArith
class NoNegativeResults implements IArithVisitor<Boolean> {
  /* TEMPLATE
  METHODS:
  ... visitConst(Const that) ...                   -- Double
  ... visitUnaryFormula(UnaryFormula that) ...     -- Double
  ... visitBinaryFormula(BinaryFormula that) ...   -- Double
  ... apply(IArith that) ...                       -- Double
   */
  
  // Produces a Boolean that is true if a negative number is never encountered at any point
  // during the evaluation of this Const; i.e., the Const is non-negative.
  public Boolean visitConst(Const that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- Const
    FIELDS ON PARAMETERS:
    ... that.num ...   -- double
     */
    return that.num >= 0;
  }
  
  // Produces a Boolean that is true if a negative number is never encountered at any point
  // during the evaluation of this UnaryFormula
  public Boolean visitUnaryFormula(UnaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- UnaryFormula
    FIELDS ON PARAMETERS:
    ... that.child ... -- IArith
     */
    return new EvalVisitor().apply(that) >= 0
        && this.apply(that.child);
  }
  
  // Produces a Boolean that is true if a negative number is never encountered at any point
  // during the evaluation of this BinaryFormula
  public Boolean visitBinaryFormula(BinaryFormula that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...       -- BinaryFormula
    FIELDS ON PARAMETERS:
    ... that.left ...  -- IArith
    ... that.right ... -- IArith
     */
    return new EvalVisitor().apply(that) >= 0
        && this.apply(that.left)
        && this.apply(that.right);
  }
  
  // Applies this NoNegativeResults to a provided IArith, producing a Boolean that is true
  // if a negative number is never encountered at any point during the evaluation of the IArith,
  // _or_ the IArith cannot be evaluated.
  public Boolean apply(IArith that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                -- IArith
    METHODS ON PARAMETERS:
    ... that.accept(IArithVisitor<R> that) ...  -- R
     */
    return !(new NoDivBy0().apply(that)) || that.accept(this);
  }
}

class ExamplesIArith {
  // EXAMPLES =====================================================================================
  
  // Functions and BiFunctions
  BiFunction<Double, Double, Double> fPlus = new Plus();
  BiFunction<Double, Double, Double> fMinus = new Minus();
  BiFunction<Double, Double, Double> fMul = new Mul();
  BiFunction<Double, Double, Double> fDiv = new Div();
  Function<Double, Double> fSqr = new Sqr();
  Function<Double, Double> fNeg = new Neg();
  
  // Constants
  Const negativeThree = new Const(-3.);
  Const zero = new Const(0.);
  Const two = new Const(2.);
  Const three = new Const(3.);
  Const four = new Const(4.);
  Const six = new Const(6.);
  Const ten = new Const(10.);
  
  // Non-nested unary or binary formulas
  BinaryFormula plus = new BinaryFormula(fPlus, "plus", two, three);
  BinaryFormula plus2 = new BinaryFormula(fPlus, "plus", four, six);
  BinaryFormula plus3 = new BinaryFormula(fPlus, "plus", negativeThree, three);
  BinaryFormula minus = new BinaryFormula(fMinus, "minus", two, three);
  BinaryFormula mul = new BinaryFormula(fMul, "mul", two, three);
  BinaryFormula mul2 = new BinaryFormula(fMul, "mul", four, six);
  BinaryFormula div = new BinaryFormula(fDiv, "div", two, three);
  UnaryFormula twoSqr = new UnaryFormula(fSqr, "sqr", two);
  UnaryFormula fourSqr = new UnaryFormula(fSqr, "sqr", four);
  UnaryFormula zeroSqr = new UnaryFormula(fSqr, "sqr", zero);
  UnaryFormula negativeThreeSqr = new UnaryFormula(fSqr, "sqr", negativeThree);
  UnaryFormula negThree = new UnaryFormula(fNeg, "neg", three);
  UnaryFormula negSix = new UnaryFormula(fNeg, "neg", six);
  UnaryFormula tenSqr = new UnaryFormula(fSqr, "sqr", ten);
  BinaryFormula undefined = new BinaryFormula(fDiv, "div", ten, zero);
  BinaryFormula undefined2 = new BinaryFormula(fDiv, "div", ten, plus3);
  
  // Combinations of multiple operations
  BinaryFormula expr1 = new BinaryFormula(fMul, "mul", twoSqr, negThree); // -12
  UnaryFormula expr2 = new UnaryFormula(fNeg, "neg", plus); // -5
  BinaryFormula expr3 = new BinaryFormula(fMinus, "minus", expr1, expr2); // -7
  UnaryFormula expr4 = new UnaryFormula(fNeg, "neg", plus2);
  BinaryFormula expr5 = new BinaryFormula(fMul, "mul", fourSqr, negSix);
  BinaryFormula expr6 = new BinaryFormula(fMinus, "minus", expr5, expr4);
  UnaryFormula expr7 = new UnaryFormula(fSqr, "neg", undefined2);
  BinaryFormula expr8 = new BinaryFormula(fPlus, "plus", expr4, expr7);
  UnaryFormula expr9 = new UnaryFormula(fNeg, "neg", tenSqr);
  BinaryFormula expr10 = new BinaryFormula(fDiv, "div", tenSqr, undefined);
  
  // METHODS ======================================================================================
  
  // EvalVisitor ----------------------------------------------------------------------------------
  void testEvalConst(Tester t) {
    t.checkInexact(new EvalVisitor().visitConst(two), 2.0, 0.001);
    
    // Checking this ensures that a Const that accepts a visitor produces the same result as that
    // visitor visiting a Const. Thus, IArithVisitor.visitConst(Const) produces the same result as
    // Const.accept(IArithVisitor).
    t.checkInexact(new EvalVisitor().visitConst(two), two.accept(new EvalVisitor()), 0.001);
  }
  
  void testEvalUnary(Tester t) {
    t.checkInexact(new EvalVisitor().visitUnaryFormula(twoSqr), 4.0, 0.001);
    t.checkInexact(new EvalVisitor().visitUnaryFormula(expr2), -5.0, 0.001);
    
    // Checking this ensures that a UnaryFormula that accepts a visitor produces the same result as
    // that visitor visiting a UnaryFormula. Thus, IArithVisitor.visitUnaryFormula(UnaryFormula)
    // produces the same result as UnaryFormula.accept(IArithVisitor).
    t.checkInexact(new EvalVisitor().visitUnaryFormula(twoSqr),
        twoSqr.accept(new EvalVisitor()), 0.001);
    t.checkInexact(new EvalVisitor().visitUnaryFormula(expr2),
        expr2.accept(new EvalVisitor()), 0.001);
  }
  
  void testEvalBinary(Tester t) {
    t.checkInexact(new EvalVisitor().visitBinaryFormula(plus), 5.0, 0.001);
    t.checkInexact(new EvalVisitor().visitBinaryFormula(expr1), -12.0, 0.001);
    t.checkInexact(new EvalVisitor().visitBinaryFormula(expr3), -7.0, 0.001);
    
    // Checking this ensures that a BinaryFormula that accepts a visitor produces the same result
    // as that visitor visiting a BinaryFormula. Thus,
    // IArithVisitor.visitBinaryFormula(BinaryFormula) produces the same result as
    // BinaryFormula.accept(IArithVisitor).
    t.checkInexact(new EvalVisitor().visitBinaryFormula(plus),
        plus.accept(new EvalVisitor()), 0.001);
    t.checkInexact(new EvalVisitor().visitBinaryFormula(expr1),
        expr1.accept(new EvalVisitor()), 0.001);
    t.checkInexact(new EvalVisitor().visitBinaryFormula(expr3),
        expr3.accept(new EvalVisitor()), 0.001);
  }
  
  void testEvalApply(Tester t) {
    t.checkInexact(new EvalVisitor().apply(two), 2.0, 0.001);
    t.checkInexact(new EvalVisitor().apply(twoSqr), 4.0, 0.001);
    t.checkInexact(new EvalVisitor().apply(expr2), -5.0, 0.001);
    t.checkInexact(new EvalVisitor().apply(plus), 5.0, 0.001);
    t.checkInexact(new EvalVisitor().apply(expr1), -12.0, 0.001);
    t.checkInexact(new EvalVisitor().apply(expr3), -7.0, 0.001);
    
    // Checking this ensures that a IArith that accepts a visitor produces the same result as
    // that visitor applied to an IArith. Thus, IArithVisitor.apply(IArith) produces the same
    // result as IArith.accept(IArithVisitor).
    t.checkInexact(new EvalVisitor().apply(two), two.accept(new EvalVisitor()), 0.001);
    t.checkInexact(new EvalVisitor().apply(twoSqr), twoSqr.accept(new EvalVisitor()), 0.001);
    t.checkInexact(new EvalVisitor().apply(expr2), expr2.accept(new EvalVisitor()), 0.001);
    t.checkInexact(new EvalVisitor().apply(plus), plus.accept(new EvalVisitor()), 0.001);
    t.checkInexact(new EvalVisitor().apply(expr1), expr1.accept(new EvalVisitor()), 0.001);
    t.checkInexact(new EvalVisitor().apply(expr3), expr3.accept(new EvalVisitor()), 0.001);
  }
  
  /*
  From the cases above, it can be observed that the following operations yield the same result:
  - IArithVisitor.apply(IArith)
  - IArith.accept(IArithVisitor)
  - IArithVisitor.visit_(_), where '_' is substituted with Const, UnaryFormula, or BinaryFormula,
    according to the type of the parameter.
    
  Because of this, it is sufficient to test only IArithVisitor.apply(IArith) for the remaining
  visitors. If apply() is correct, then it can be assumed that visitConst(), visitUnaryFormula(),
  visitBinaryFormula(), and accept() are as well.
   */
  
  // PrintVisitor ---------------------------------------------------------------------------------
  void testPrintApply(Tester t) {
    t.checkExpect(new PrintVisitor().apply(three), "3.0");
    t.checkExpect(new PrintVisitor().apply(negThree), "(neg 3.0)");
    t.checkExpect(new PrintVisitor().apply(expr2), "(neg (plus 2.0 3.0))");
    t.checkExpect(new PrintVisitor().apply(minus), "(minus 2.0 3.0)");
    t.checkExpect(new PrintVisitor().apply(expr1), "(mul (sqr 2.0) (neg 3.0))");
    t.checkExpect(new PrintVisitor().apply(expr3),
        "(minus (mul (sqr 2.0) (neg 3.0)) (neg (plus 2.0 3.0)))");
  }
  
  // DoublerVisitor -------------------------------------------------------------------------------
  void testDoublerApply(Tester t) {
    t.checkExpect(new DoublerVisitor().apply(two), four);
    t.checkExpect(new DoublerVisitor().apply(twoSqr), fourSqr);
    t.checkExpect(new DoublerVisitor().apply(expr2), expr4);
    t.checkExpect(new DoublerVisitor().apply(mul), mul2);
    t.checkExpect(new DoublerVisitor().apply(expr1), expr5);
    t.checkExpect(new DoublerVisitor().apply(expr3), expr6);
  }
  
  // AllSmallVisitor ------------------------------------------------------------------------------
  void testAllSmallApply(Tester t) {
    t.checkExpect(new AllSmallVisitor().apply(two), true);
    t.checkExpect(new AllSmallVisitor().apply(ten), false);
    t.checkExpect(new AllSmallVisitor().apply(fourSqr), true);
    t.checkExpect(new AllSmallVisitor().apply(expr4), true);
    t.checkExpect(new AllSmallVisitor().apply(tenSqr), false);
    t.checkExpect(new AllSmallVisitor().apply(expr9), false);
    t.checkExpect(new AllSmallVisitor().apply(div), true);
    t.checkExpect(new AllSmallVisitor().apply(expr1), true);
    t.checkExpect(new AllSmallVisitor().apply(expr3), true);
    t.checkExpect(new AllSmallVisitor().apply(undefined), false);
    t.checkExpect(new AllSmallVisitor().apply(expr10), false);
    t.checkExpect(new AllSmallVisitor().apply(expr8), false);
  }
  
  // NoDivBy0 ------------------------------------------------------------------------------
  void testNoDivBy0Apply(Tester t) {
    t.checkExpect(new NoDivBy0().apply(three), true);
    t.checkExpect(new NoDivBy0().apply(fourSqr), true);
    t.checkExpect(new NoDivBy0().apply(expr4), true);
    t.checkExpect(new NoDivBy0().apply(expr7), false);
    t.checkExpect(new NoDivBy0().apply(mul2), true);
    t.checkExpect(new NoDivBy0().apply(expr5), true);
    t.checkExpect(new NoDivBy0().apply(expr6), true);
    t.checkExpect(new NoDivBy0().apply(undefined), false);
    t.checkExpect(new NoDivBy0().apply(expr8), false);
  }
  
  // NoNegativeResults ---------------------------------------------------------------------
  void testNoNegativeResultsApply(Tester t) {
    t.checkExpect(new NoNegativeResults().apply(negativeThree), false);
    t.checkExpect(new NoNegativeResults().apply(zero), true);
    t.checkExpect(new NoNegativeResults().apply(three), true);
    t.checkExpect(new NoNegativeResults().apply(twoSqr), true);
    t.checkExpect(new NoNegativeResults().apply(zeroSqr), true);
    t.checkExpect(new NoNegativeResults().apply(negativeThreeSqr), false);
    t.checkExpect(new NoNegativeResults().apply(negThree), false);
    t.checkExpect(new NoNegativeResults().apply(minus), false);
    t.checkExpect(new NoNegativeResults().apply(plus), true);
    t.checkExpect(new NoNegativeResults().apply(plus3), false);
  }
}