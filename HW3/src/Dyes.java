import tester.*;

// Represents a perfect recipe for a dye.
class DyeRecipe {
  double red;
  double yellow;
  double blue;
  double black;
  
  DyeRecipe(double red, double yellow, double blue, double black) {
    // Throw an exception if any of the dyes are in negative amounts
    if (red < 0 || yellow < 0 || blue < 0 || black < 0) {
      String ex = String.format(
          "Negative dyes? Really? All dyes must have nonnegative weights. "
              + "(given %.2f, %.2f, %.2f, %.2f)",
          red, yellow, blue, black);
      throw new IllegalArgumentException(ex);
    }
    
    // Throw an exception if any black dye is being wasted
    if (new Utils().excessiveBlackToColors(red, yellow, blue, black)
            && new Utils().excessiveColorsToBlack(red, yellow, blue, black)) {
      throw new IllegalArgumentException(
          "Black dye exceeds 5% of the weight of the other three colors, and the weight of the "
              + "other three colors is more than 10% the weight of the black dye. For a perfect "
              + "mixture, at least one of these conditions must be false");
    }
    
    // Throw an exception if there is any yellow dye and too much blue dye relative to the amount
    // of yellow dye
    if (new Utils().excessiveBlueToYellow(yellow, blue)) {
      String ex = String.format(
          "The amount of yellow dye (%.2f) is nonzero, and blue dye (%.2f) exceeds one-tenth of "
              + "the yellow dye's weight.",
          yellow, blue);
      throw new IllegalArgumentException(ex);
    }
  
    double totalWeight = red + yellow + blue + black;
    
    this.red = red / totalWeight;
    this.yellow = yellow / totalWeight;
    this.blue = blue / totalWeight;
    this.black = black / totalWeight;
  }
  
  DyeRecipe(double red, double yellow, double blue) {
    // Construct the darkest perfect dye recipe possible given specified amounts of red, yellow,
    // and blue.
    this(red, yellow,
        new Utils().clampBlueByYellow(yellow, blue),
        0.05 * (red + yellow + new Utils().clampBlueByYellow(yellow, blue)));
  }
  
  DyeRecipe(DyeRecipe first, DyeRecipe second) {
    // Construct a perfect dye recipe that is a half-and-half mixture of two others, reducing
    // blue and black dye to the maximum allowable amount
    this(first.red + second.red,
        first.yellow + second.yellow,
        first.adjustBlueForCombination(second),
        new Utils().clampBlackByColors(
            first.red + second.red,
            first.yellow + second.yellow,
            first.adjustBlueForCombination(second),
            first.black + second.black));
  }
  
  /* TEMPLATE:
  FIELDS:
  ... this.red ...                                          -- double
  ... this.yellow ...                                       -- double
  ... this.blue ...                                         -- double
  ... this.black ...                                        -- double
  METHODS:
  ... this.adjustBlackForCombination(DyeRecipe other) ...   -- double
  ... this.adjustBlueForCombination(DyeRecipe other) ...    -- double
  ... this.sameRecipe(DyeRecipe other) ...                  -- double
   */
  
  
  // Produces a double representing the amount of blue dye that should go in a mixture of this
  // dye with other. The returned value is the result of clampBlueByYellow, with the color fields
  // of this and other summed for the corresponding inputs.
  double adjustBlueForCombination(DyeRecipe other) {
    /* TEMPLATE:
    PARAMETERS:
    ... other ...          -- DyeRecipe
    FIELDS ON PARAMETERS:
    ... other.yellow ...   -- double
    ... other.blue ...     -- double
     */
    return new Utils().clampBlueByYellow(
        this.yellow + other.yellow,
        this.blue + other.blue
    );
  }
  
  // Determines if the corresponding ingredients of this recipe and the other recipe have the
  // same weights within a tolerance of 0.001 grams.
  boolean sameRecipe(DyeRecipe other) {
    /* TEMPLATE:
    ... other ...          -- DyeRecipe
    FIELDS ON PARAMETERS:
    ... other.red ...      -- double
    ... other.yellow ...   -- double
    ... other.blue ...     -- double
    ... other.black ...    -- double
     */
    return Math.abs(other.red - this.red) < 0.001
        && Math.abs(other.yellow - this.yellow) < 0.001
        && Math.abs(other.blue - this.blue) < 0.001
        && Math.abs(other.black - this.black) < 0.001;
  }
}

// Represents a set of utilities for use in DyeRecipe constructors.
class Utils {
  // Returns true if the weight of the black pigment is more than 5% the weight of the other
  // colors combined, otherwise false.
  boolean excessiveBlackToColors(double red, double yellow, double blue, double black) {
    /* TEMPLATE:
    PARAMETERS:
    ... red ...      -- double
    ... yellow ...   -- double
    ... blue ...     -- double
    ... black ...    -- double
     */
    return black > this.maxBlackByColors(red, yellow, blue);
  }
  
  // Returns true if the weight of the other three colors is more than 10% the weight of the
  // black pigment, otherwise false.
  boolean excessiveColorsToBlack(double red, double yellow, double blue, double black) {
    /* TEMPLATE:
    PARAMETERS:
    ... red ...      -- double
    ... yellow ...   -- double
    ... blue ...     -- double
    ... black ...    -- double
     */
    return red + yellow + blue > 0.1 * black;
  }
  
  // Returns true if there is yellow pigment AND the amount of blue pigment is more than a tenth
  // of the amount of yellow. Otherwise, returns false.
  boolean excessiveBlueToYellow(double yellow, double blue) {
    /* TEMPLATE:
    PARAMETERS:
    ... yellow ...   -- double
    ... blue ...     -- double
     */
    return yellow > 0 && blue > 0.1 * yellow;
  }
  
  // Assuming that the amount of black dye must be less than the amount of the other three colors
  // combined, determines the maximum amount of black dye that may be included in the mixture
  // (within the supplied constraints). The value returned will be 5% of the sum of the other
  // three colors.
  double maxBlackByColors(double red, double yellow, double blue) {
    /* TEMPLATE:
    PARAMETERS:
    ... red ...      -- double
    ... yellow ...   -- double
    ... blue ...     -- double
     */
    return 0.05 * (red + yellow + blue);
  }
  
  // "Clamps" the provided amount of black dye to an amount that satisfies the constraints for a
  // perfect mixture. If the amount of colored dye is more than one-tenth of the black dye,
  // returns the minimum between the result of maxBlackByColors and the given amount of black dye.
  // Otherwise, simply returns the amount of black dye as-is.
  double clampBlackByColors(double red, double yellow, double blue, double black) {
    /* TEMPLATE:
    PARAMETERS:
    ... red ...      -- double
    ... yellow ...   -- double
    ... blue ...     -- double
    ... black ...    -- double
     */
    if (new Utils().excessiveColorsToBlack(red, yellow, blue, black)) {
      return Math.min(black, this.maxBlackByColors(red, yellow, blue));
    } else {
      return black;
    }
  }
  
  // "Clamps" the provided amount of blue dye to an amount that satisfies the constraints for a
  // perfect mixture. If the amount of yellow dye is greater than zero, returns the minimum between
  // the provided amount of blue dye and one-tenth of the amount of yellow dye. Otherwise,
  // returns the amount of blue dye as-is.
  double clampBlueByYellow(double yellow, double blue) {
    /* TEMPLATE:
    PARAMETERS:
    ... yellow ...   -- double
    ... blue ...     -- double
     */
    if (yellow > 0) {
      return Math.min(blue, yellow * 0.1);
    } else {
      return blue;
    }
  }
}

class ExamplesDyeRecipes {
  DyeRecipe red = new DyeRecipe(1., 0., 0., 0.);
  DyeRecipe blue = new DyeRecipe(0., 0., 1., 0.);
  DyeRecipe yellow = new DyeRecipe(0., 1., 0., 0.);
  DyeRecipe black = new DyeRecipe(0., 0., 0., 1.);
  DyeRecipe valid1 = new DyeRecipe(50., 100., 10., 8.);
  
  DyeRecipe conv1 = new DyeRecipe(100., 100., 6.);
  DyeRecipe conv2 = new DyeRecipe(20., 60., 5.);
  
  DyeRecipe purple = new DyeRecipe(red, blue);
  DyeRecipe blend2 = new DyeRecipe(conv1, conv2);
  
  boolean testConstructor1Valid(Tester t) {
    return t.checkConstructorNoException("lowerThanExThreshold", "DyeRecipe", 50., 100., 10., 8.)
        && t.checkConstructorNoException("higherThanExThreshold", "DyeRecipe", 5., 10., 1., 160.);
  }
  
  boolean testConstructor1Ex(Tester t) {
    return t.checkConstructorException(new IllegalArgumentException(
        "Black dye exceeds 5% of the weight of the other three colors, and the weight of the "
            + "other three colors is more than 10% the weight of the black dye. For a perfect "
            + "mixture, at least one of these conditions must be false"),
        "DyeRecipe", 50., 25., 25., 10.)
        && t.checkConstructorException(new IllegalArgumentException("Negative dyes? Really? "
            + "All dyes must have nonnegative weights. "
            + "(given -1.00, -1.00, 0.00, 0.00)"), "DyeRecipe", -1., -1., 0., 0.)
        && t.checkConstructorException(new IllegalArgumentException("The amount of yellow dye "
            + "(25.00) is nonzero, and blue dye (25.00) exceeds one-tenth of the yellow dye's "
            + "weight."), "DyeRecipe", 50., 25., 25., 0.);
  }
  
  boolean testConstructor1Eq(Tester t) {
    return t.checkExpect(valid1.sameRecipe(
        new DyeRecipe(5., 10., 1., 0.8)), true);
  }
  
  boolean testConstructor2(Tester t) {
    return t.checkExpect(
            new DyeRecipe(50.,100., 10.).sameRecipe(
                valid1), true)
        && t.checkExpect(
            new DyeRecipe(50.,100., 20.).sameRecipe(
                valid1), true)
        && t.checkExpect(
            new DyeRecipe(50.,0., 20.).sameRecipe(
                new DyeRecipe(50., 0., 20., 3.5)
            ), true);
  }
  
  boolean testConstructor3(Tester t) {
    return
        // Mixture elements are the same
        t.checkExpect(
            new DyeRecipe(
                red,
                red
            ).sameRecipe(
                new DyeRecipe(1., 0., 0., 0.)
            ), true)
        // Blue gets clamped down.
        && t.checkExpect(
            new DyeRecipe(
                new DyeRecipe(0.5, 0.5, 0., 0.),
                blue
            ).sameRecipe(
                new DyeRecipe(0.5, 0.5, 0.05, 0.)
            ), true)
        // Black gets clamped down.
        && t.checkExpect(
            new DyeRecipe(
                red,
                black
            ).sameRecipe(
                new DyeRecipe(20., 0., 0., 1.)
            ), true)
        // Black doesn't get clamped down.
        && t.checkExpect(
            new DyeRecipe(
                new DyeRecipe(1., 0., 0., 10.),
                black
            ).sameRecipe(
                new DyeRecipe(1., 0., 0., 21.)
            ), true)
        // Blue gets clamped down first, then black.
        && t.checkExpect(
            new DyeRecipe(
                new DyeRecipe(1., 1., 0.1, 0.),
                new DyeRecipe(0., 0., 0.1, 5.)
            ).sameRecipe(
                new DyeRecipe(0.5, 0.5, 0.05, 0.0525)
            ), true);
  }
  
  boolean testAdjustBlueForCombination(Tester t) {
    return t.checkInexact(yellow.adjustBlueForCombination(blue), 0.1, 0.001)
        && t.checkInexact(
            new DyeRecipe(0., 0., 1., 0.).adjustBlueForCombination(
                new DyeRecipe(0., 0., 1., 0.)), 2., 0.001);
  }
  
  boolean testSameRecipe(Tester t) {
    return t.checkExpect(new DyeRecipe(1., 1., 0., 0.1).sameRecipe(
            new DyeRecipe(1., 1., 0., 0.1)
        ), true)
        && t.checkExpect(new DyeRecipe(1., 1., 0., 0.1).sameRecipe(
            new DyeRecipe(10., 10., 0., 1.)
        ), true)
        && t.checkExpect(new DyeRecipe(1., 1., 0., 0.1).sameRecipe(
            new DyeRecipe(1., 1.1, 0., 0.1)
        ), false);
    
  }
  
  boolean testExcessiveBlackToColors(Tester t) {
    return t.checkExpect(new Utils().excessiveBlackToColors(1., 1., 0., 0.1), false)
        && t.checkExpect(new Utils().excessiveBlackToColors(1., 1. , 0, 0.11), true);
  }
  
  boolean testExcessiveColorsToBlack(Tester t) {
    return t.checkExpect(new Utils().excessiveColorsToBlack(0.5, 0.3, 0.2, 1.), true)
        && t.checkExpect(new Utils().excessiveColorsToBlack(0.5, 0.3, 0.2, 10.), false)
        && t.checkExpect(new Utils().excessiveColorsToBlack(0.5, 0.3, 0.2, 20.), false);
  }
  
  boolean testExcessiveBlueToYellow(Tester t) {
    return t.checkExpect(new Utils().excessiveBlueToYellow(1.0, 0.5), true)
        && t.checkExpect(new Utils().excessiveBlueToYellow(-1.0, 0.5), false)
        && t.checkExpect(new Utils().excessiveBlueToYellow(1.0, 0.05), false);
  }
  
  boolean testMaxBlackByColors(Tester t) {
    return t.checkInexact(new Utils().maxBlackByColors(0.1, 0.7, 0.2), 0.05, 0.001)
        && t.checkInexact(new Utils().maxBlackByColors(0.0, 0.0, 0.0), 0.0, 0.001);
  }
  
  boolean testClampBlackByColors(Tester t) {
    return t.checkInexact(new Utils().clampBlackByColors(0.5, 0.3, 0.2, 1.0), 0.05, 0.001)
        && t.checkInexact(new Utils().clampBlackByColors(0.5, 0.3, 0.2, 20.0), 20.0, 0.001);
  }
  
  boolean testClampBlueByYellow(Tester t) {
    return t.checkInexact(new Utils().clampBlueByYellow(1.0, 0.2), 0.1, 0.001)
        && t.checkInexact(new Utils().clampBlueByYellow(1.0, 0.05), 0.05, 0.001)
        && t.checkInexact(new Utils().clampBlueByYellow(0.0, 0.5), 0.5, 0.001);
  }
}