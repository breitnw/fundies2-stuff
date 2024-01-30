import tester.Tester;

// Represents a drink you might get at a coffee shop
interface IBeverage {
  
  // Returns true if this IBeverage is decaf, otherwise returns false.
  boolean isDecaf();
  
  // Returns true if this IBeverage contains a given ingredient, otherwise returns false.
  boolean containsIngredient(String ingredient);
  
  // Produces a String representing how this IBeverage might appear on a menu.
  String format();
  
  // Produces a String representing how the ingredients of this IBeverage might appear on a menu.
  // Ingredients are comma-separated and parenthesized, and if there are no ingredients, the
  // String "(without mixins)" is returned instead.
  String formatIngredients();
}

abstract class ABeverage implements IBeverage {
  ILoString ingredients;
  
  ABeverage(ILoString ingredients) {
    this.ingredients = ingredients;
  }
  /* TEMPLATE:
  FIELDS:
  ... this.ingredients ...                        -- ILoString
  METHODS:
  ... containsIngredient(String ingredient) ...   -- boolean
  ... formatIngredients() ...                     -- String
  METHODS ON FIELDS:
  ... this.ingredients.contains(ingredient) ...   -- boolean
  ... this.ingredients.commaFormat() ...          -- String
   */
  
  // Returns true if this ABeverage contains a given ingredient, otherwise returns false.
  public boolean containsIngredient(String ingredient) {
    /* TEMPLATE:
    PARAMETERS:
    ... ingredient ...   -- String
     */
    return this.ingredients.contains(ingredient);
  }
  
  // Produces a String representing how the ingredients of this IBeverage might appear on a menu.
  // Ingredients are comma-separated and parenthesized, and if there are no ingredients, the
  // String "(without mixins)" is returned instead.
  public String formatIngredients() {
    String formattedMixins = this.ingredients.commaFormat();
    if (formattedMixins.isEmpty()) {
      return "(without mixins)";
    } else {
      return  "(with " + formattedMixins + ")";
    }
  }
}

// Represents a bubble-tea drink, with various mixins
class BubbleTea extends ABeverage {
  
  String variety; // Black tea, Oolong, Green tea, etc.
  
  int size; // in ounces
  
  BubbleTea(String variety, ILoString mixins, int size) {
    super(mixins);
    this.variety = variety;
    this.size = size;
  }
  /* TEMPLATE:
  ... this.variety ...                 -- String
  ... this.size ...                    -- int
  METHODS:
  ... isDecaf() ...                    -- boolean
  ... format() ...                     -- String
  METHODS ON FIELDS:
  ... this.variety.equals("Rooibos")   -- boolean
   */
  
  // Returns true if this BubbleTea is decaf, otherwise returns false. Only Rooibos tea is decaf;
  // all others are caffeinated.
  public boolean isDecaf() {
    return this.variety.equals("Rooibos");
  }
  
  // Produces a String representing how this BubbleTea might appear on a menu. Includes the size
  // of the drink, the variety, and the (formatted) ingredients.
  public String format() {
    return this.size + "oz " + this.variety + " " + this.formatIngredients();
  }
}

// Represents any coffee-based drink
class Coffee extends ABeverage {
  
  String variety; // Arabica, Robusta, Excelsa or Liberica
  
  String style; // americano, demitasse, espresso, etc.
  
  boolean isIced; // whether it's cold or hot
  
  Coffee(String variety, ILoString mixins, String style, boolean isIced) {
    super(mixins);
    this.variety = variety;
    this.style = style;
    this.isIced = isIced;
  }
  /* TEMPLATE:
  FIELDS:
  ... this.variety ...   -- String
  ... this.style ...     -- String
  ... this.isIced ...    -- boolean
  METHODS:
  ... isDecaf() ...      -- boolean
  ... icedOrHot() ...    -- String
  ... format() ...       -- String
   */
  
  // Always returns false, since no Coffee is decaf.
  public boolean isDecaf() {
    return false;
  }
  
  // Returns a String representing whether this Coffee is iced or hot. If it is iced, returns
  // "Iced", otherwise returns "Hot".
  public String icedOrHot() {
    if (this.isIced) {
      return "Iced";
    } else {
      return "Hot";
    }
  }
  
  // Produces a String representing how this Coffee might appear on a menu. Includes a
  // String representing whether the drink is iced or hot, the variety of the drink, the
  // style of the drink, and the (formatted) ingredients.
  public String format() {
    return icedOrHot() + " " + this.variety + " " + this.style + " " + this.formatIngredients();
  }
}

// Represents an ice-cream-based blended drink
class Milkshake extends ABeverage {
  
  String flavor;  // vanilla, mint-chip, strawberry, etc.
  
  String brandName; // Ben&Jerrys, JPLicks, etc.
  
  int size; // in ounces
  
  Milkshake(String flavor, ILoString toppings, String brandName, int size) {
    super(toppings);
    this.flavor = flavor;
    this.brandName = brandName;
    this.size = size;
  }
  /* TEMPLATE:
  ... this.flavor ...      -- String
  ... this.brandName ...   -- String
  ... this.size ...        -- int
  METHODS:
  ... isDecaf() ...        -- boolean
  ... format() ...         -- String
   */
  
  // Always returns true, since no Milkshake is caffeinated.
  public boolean isDecaf() {
    return true;
  }
  
  // Produces a String representing how this Milkshake might appear on a menu. Includes the size
  // of the drink, the brand name, the flavor, and the (formatted) ingredients.
  public String format() {
    return this.size + "oz " + this.brandName + " " + this.flavor + " " + this.formatIngredients();
  }
}


// lists of strings
interface ILoString {
  // Checks whether this ILoString contains val.
  boolean contains(String val);
  
  // Formats this ILoString as a String, with elements separated by a comma and a space.
  String commaFormat();
  
  // Formats this ILoString as a String, prepended by acc, with elements separated by a comma and a
  // space.
  String commaFormatPrepended(String acc);
}

// an empty list of strings
class MtLoString implements ILoString {
  /* TEMPLATE:
  METHODS:
  ... contains(String val) ...             -- boolean
  ... commaFormat() ...                    -- String
  ... commaFormatPrepended(String acc) ...    -- String
   */
  
  // Checks whether this MtLoString contains val. Since it is empty, this will always return false.
  public boolean contains(String val) {
    /* TEMPLATE:
    PARAMETERS:
    ... val ...   -- String
     */
    return false;
  }
  
  // Formats this MtLoString as a String. Since it is empty, an empty string is returned.
  public String commaFormat() {
    return "";
  }
  
  // Formats this MtLoString as a String, with elements separated by a comma and a space. Since it
  // is empty, simply returns acc.
  public String commaFormatPrepended(String acc) {
    /* TEMPLATE:
    PARAMETERS:
    ... acc ...   -- String
     */
    return acc;
  }
}

// a non-empty list of strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;
  
  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }
  /* TEMPLATE:
  ... this.first ...                                -- String
  ... this.rest ...                                 -- ILoString
  METHODS:
  ... contains(String val) ...                      -- boolean
  ... commaFormat() ...                             -- String
  ... commaFormatPrepended() ...                       -- String
  METHODS ON FIELDS:
  ... this.first.equals(val) ...                    -- boolean
  ... this.rest.contains(val) ...                   -- boolean
  ... this.rest.commaFormatPrepended(String acc) ...   -- String
   */
  
  // Checks whether this ConsLoString contains val. This is true if the first element of the
  // ConsLoString is equal to val, or the rest of the ConsLoString contains val.
  public boolean contains(String val) {
    /* TEMPLATE:
    PARAMETERS:
    ... val ...   -- String
     */
    return this.first.equals(val) || this.rest.contains(val);
  }
  
  // Formats this ILoString as a String, with elements separated by a comma and a space.
  public String commaFormat() {
    return this.commaFormatPrepended("");
  }
  
  // Formats this ILoString as a String, prepended by acc, with elements separated by a comma and a
  // space. If acc is non-empty, a comma and a space are added between acc and the first element
  // of this.
  public String commaFormatPrepended(String acc) {
    /* TEMPLATE:
    PARAMETERS:
    ... acc ...   -- String
     */
    if (acc.isEmpty()) {
      return this.rest.commaFormatPrepended(this.first);
    } else {
      return this.rest.commaFormatPrepended(acc + ", " + this.first);
    }
  }
}

class ExamplesBeverages {
  // Beverage examples
  BubbleTea black = new BubbleTea("Black Tea",
      new ConsLoString("boba",
          new MtLoString()),
      20);
  
  BubbleTea rooibos = new BubbleTea("Rooibos",
      new ConsLoString("extra sugar",
          new ConsLoString("extra ice", new MtLoString())),
      16);
  
  Coffee frappucino = new Coffee("Excelsa",
      new ConsLoString("extra sugar",
          new ConsLoString("cinnamon", new MtLoString())),
      "demitasse", false);
  
  Coffee americano = new Coffee("Arabica",
      new ConsLoString("cream",
          new ConsLoString("sugar",
              new ConsLoString("hazelnut syrup", new MtLoString()))),
      "americano", true);
      
  Milkshake vanilla = new Milkshake("vanilla",
      new ConsLoString("sprinkles",
          new ConsLoString("cherry",
              new ConsLoString("chocolate syrup",
                  new MtLoString()))),
      "Ben&Jerrys", 24);
  
  Milkshake mintChip = new Milkshake("mint-chip", new MtLoString(), "JPLicks", 8);
  
  // ILoString examples
  ILoString three =
      new ConsLoString("cream",
          new ConsLoString("sugar",
              new ConsLoString("hazelnut syrup", new MtLoString())));
  ILoString one =
      new ConsLoString("cream",
          new MtLoString());
  ILoString mt = new MtLoString();
  
  boolean testIsDecaf(Tester t) {
    return t.checkExpect(black.isDecaf(), false)
        && t.checkExpect(rooibos.isDecaf(), true)
        && t.checkExpect(frappucino.isDecaf(), false)
        && t.checkExpect(vanilla.isDecaf(), true);
  }
  
  boolean testContainsIngredient(Tester t) {
    return t.checkExpect(black.containsIngredient("boba"), true)
        && t.checkExpect(rooibos.containsIngredient("boba"), false)
        && t.checkExpect(americano.containsIngredient("sugar"), true)
        && t.checkExpect(mintChip.containsIngredient("chocolate syrup"), false);
  }
  
  boolean testFormat(Tester t) {
    return t.checkExpect(black.format(), "20oz Black Tea (with boba)")
        && t.checkExpect(rooibos.format(), "16oz Rooibos (with extra sugar, extra ice)")
        && t.checkExpect(frappucino.format(), "Hot Excelsa demitasse (with extra sugar, cinnamon)")
        && t.checkExpect(americano.format(), "Iced Arabica americano (with cream, sugar, hazelnut"
                                                 + " syrup)")
        && t.checkExpect(vanilla.format(), "24oz Ben&Jerrys vanilla (with sprinkles, cherry, " 
                                               + "chocolate syrup)")
        && t.checkExpect(mintChip.format(), "8oz JPLicks mint-chip (without mixins)");
  }
  
  boolean testFormatIngredients(Tester t) {
    return t.checkExpect(black.formatIngredients(), "(with boba)")
        && t.checkExpect(americano.formatIngredients(), "(with cream, sugar, hazelnut syrup)")
        && t.checkExpect(mintChip.formatIngredients(), "(without mixins)");
  }
  
  boolean testIcedOrHot(Tester t) {
    return t.checkExpect(frappucino.icedOrHot(), "Hot")
        && t.checkExpect(americano.icedOrHot(), "Iced");
  }
  
  boolean testContains(Tester t) {
    return t.checkExpect(three.contains("sugar"), true)
        && t.checkExpect(three.contains("ice"), false)
        && t.checkExpect(mt.contains("sugar"), false);
  }
  
  boolean testCommaFormat(Tester t) {
    return t.checkExpect(mt.commaFormat(), "")
        && t.checkExpect(three.commaFormat(), "cream, sugar, hazelnut syrup")
        && t.checkExpect(one.commaFormat(), "cream");
  }
  
  boolean testcommaFormatPrepended(Tester t) {
    return t.checkExpect(mt.commaFormatPrepended(""), "")
        && t.checkExpect(three.commaFormatPrepended(""), "cream, sugar, hazelnut syrup")
        && t.checkExpect(one.commaFormatPrepended(""), "cream")
        && t.checkExpect(one.commaFormatPrepended("sugar"), "sugar, cream");
  }
}
