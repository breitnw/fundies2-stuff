import tester.Tester;

interface ILoString {
  // Produces a new list with its elements in reversed order from this list
  ILoString reverse();
  
  // Takes the current list and produces a list of the same items in the same order, but uses
  // only ConsLoString and MtLoString while simplifying away any instances of SnocLoString and
  // AppendLoString.
  
  ILoString normalize();
  
  // Normalizes this list according to the purpose statement for normalize(), appending tail at
  // the end.
  // The accumulator, tail, is updated each time normalizeHelper encounters a SnocLoString
  // (gaining a new ConsLoString that wraps the existing tail), as well as each time
  // normalizeHelper encounters an AppendLoString (swapping the existing tail with the
  // AppendLoString's back, itself normalized with the existing tail)
  ILoString normalizeHelper(ILoString tail);
  
  // left-scans across this list and concatenates all the strings, returning a normalized list
  // of the intermediate results at each step.
  ILoString scanConcat();
  
  // Left-scans across this list and concatenates all the strings, producing a list of the
  // results at each step. The accumulator, acc, represents a String prepended to each returned
  // result. Each time a ConsLoString is encountered, its first element is appended to the
  // accumulator.
  ILoString scanConcatHelper(String acc);
}

// Represents an element and the list of elements that follow it in a list of Strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }
  /* TEMPLATE:
  FIELDS:
  ... this.first ...                         -- String
  ... this.rest ...                          -- ILoString
  METHODS:
  ... reverse() ...                          -- ILoString
  ... normalize() ...                        -- ILoString
  ... normalizeHelper(ILoString tail) ...    -- ILoString
  ... scanConcat() ...                       -- ILoString
  ... scanConcatHelper(String acc) ...       -- ILoString
  METHODS ON FIELDS:
  ... this.rest.reverse() ...                -- ILoString
  ... this.rest.normalizeHelper() ...        -- ILoString
  ... this.rest.scanConcatHelper() ...       -- ILoString
   */
  
  // Produces a new list with its elements in reversed order by converting this ConsLoString to a
  // SnocLoString with its rest field reversed.
  public ILoString reverse() {
    return new SnocLoString(this.rest.reverse(), this.first);
  }
  
  // Takes the current list and produces a list of the same items in the same order, but uses
  // only ConsLoString and MtLoString.
  public ILoString normalize() {
    return this.normalizeHelper(new MtLoString());
  }
  
  // Takes the current list and produces a normalized list by creating a new ConsLoString with
  // the same first element and the rest of the elements normalized with the same tail.
  public ILoString normalizeHelper(ILoString tail) {
    /* TEMPLATE:
    PARAMETERS:
    ... tail ...   -- ILoString
     */
    return new ConsLoString(this.first, this.rest.normalizeHelper(tail));
  }
  
  // left-scans across this list and concatenates all the strings, returning a normalized list
  // of the intermediate results at each step.
  public ILoString scanConcat() {
    return this.scanConcatHelper("");
  }
  
  // Left-scans across this list and concatenates all the strings, producing a list of the
  // results at each step. The accumulator is prepended to each element of the list, and
  // this.first is appended to the accumulator for future iterations.
  public ILoString scanConcatHelper(String acc) {
    /* TEMPLATE:
    PARAMETERS:
    ... acc ...   -- String
     */
    return new ConsLoString(acc + this.first,
        this.rest.scanConcatHelper(acc + this.first));
  }
}

// Represents an empty list of Strings
class MtLoString implements ILoString {
  
  // Reverses this MtLoString by doing absolutely nothing and returning this.
  public ILoString reverse() {
    return this;
  }
  
  // Normalizes this MtLoString by returning this, since an MtLoString is a valid normalization.
  public ILoString normalize() {
    return this;
  }
  
  // Normalizes this MtLoString by substituting it with tail
  public ILoString normalizeHelper(ILoString tail) {
    /* TEMPLATE:
    PARAMETERS:
    ... tail ...   -- ILoString
     */
    return tail;
  }
  
  // left-scans across this list and concatenates all the strings, returning a normalized list
  // of the intermediate results at each step. Since this list is empty, the list itself is
  // returned.
  public ILoString scanConcat() {
    return this;
  }
  
  // Left-scans across this list and concatenates all the strings, producing a list of the
  // results at each step. Since this list is empty, the list itself is returned.
  public ILoString scanConcatHelper(String acc) {
    /* TEMPLATE:
    PARAMETERS:
    ... acc ...   -- String
     */
    return this;
  }
}

// Represents an element and the list of elements that precede it in a list of Strings
class SnocLoString implements ILoString {
  ILoString front;
  String last;

  SnocLoString(ILoString front, String last) {
    this.front = front;
    this.last = last;
  }
  /* TEMPLATE:
  FIELDS:
  ... this.front ...                          -- ILoString
  ... this.last ...                           -- String
  METHODS:
  ... reverse() ...                           -- ILoString
  ... normalize() ...                         -- ILoString
  ... normalizeHelper(ILoString tail) ...     -- ILoString
  ... scanConcat() ...                        -- ILoString
  ... scanConcatHelper(String acc) ...        -- ILoString
  METHODS ON FIELDS:
  ... this.front.reverse() ...                -- ILoString
  ... this.front.normalizeHelper() ...        -- ILoString
   */
  
  // Produces a new list with its elements in reversed order by converting this SnocLoString to a
  // ConsLoString with its rest field reversed.
  public ILoString reverse() {
    return new ConsLoString(this.last, this.front.reverse());
  }
  
  // Takes the current list and produces a list of the same items in the same order, but uses
  // only ConsLoString and MtLoString
  public ILoString normalize() {
    return this.normalizeHelper(new MtLoString());
  }
  
  // Takes the current list and produces a normalized list by normalizing the front of this
  // SnocLoString and appending the last to tail.
  public ILoString normalizeHelper(ILoString tail) {
    /* TEMPLATE:
    PARAMETERS:
    ... tail ...   -- ILoString
     */
    return this.front.normalizeHelper(new ConsLoString(this.last, tail));
  }
  
  // left-scans across this list and concatenates all the strings, returning a normalized list
  // of the intermediate results at each step.
  public ILoString scanConcat() {
    return this.scanConcatHelper("");
  }
  
  // Left-scans across this list and concatenates all the strings, producing a list of the
  // results at each step. To do so, simply calls scanConcatHelper() on a normalized version of
  // this list.
  public ILoString scanConcatHelper(String acc) {
    /* TEMPLATE:
    PARAMETERS:
    ... acc ...   -- String
     */
    return this.normalize().scanConcatHelper(acc);
  }
}

// Represents two lists of Strings that are concatenated side-by-side to form another list
class AppendLoString implements ILoString {
  ILoString front;
  ILoString back;

  AppendLoString(ILoString front, ILoString back) {
    this.front = front;
    this.back = back;
  }
  
  /* TEMPLATE:
  FIELDS:
  ... this.front ...                        -- ILoString
  ... this.back ...                         -- ILoString
  METHODS:
  ... reverse() ...                         -- ILoString
  ... normalize() ...                       -- ILoString
  ... normalizeHelper(ILoString tail) ...   -- ILoString
  ... scanConcat() ...                      -- ILoString
  ... scanConcatHelper(String acc) ...      -- ILoString
  METHODS ON FIELDS:
  ... this.back.reverse() ...               -- ILoString
  ... this.front.reverse() ...              -- ILoString
  ... this.front.normalizeHelper() ...      -- ILoString
  ... this.back.normalizeHelper() ...       -- ILoString
   */
  
  // Produces a new list with its elements in reversed order by swapping the order of this.front and
  // this.back and reversing each.
  public ILoString reverse() {
    return new AppendLoString(this.back.reverse(), this.front.reverse());
  }
  
  // Takes the current list and produces a list of the same items in the same order, but uses
  // only ConsLoString and MtLoString
  public ILoString normalize() {
    return this.normalizeHelper(new MtLoString());
  }
  
  // Takes the current list and produces a normalized list by normalizing this.front and swapping
  // the existing tail with this.back, itself normalized with the existing tail
  public ILoString normalizeHelper(ILoString tail) {
    /* TEMPLATE:
    PARAMETERS:
    ... tail ...   -- ILoString
     */
    return this.front.normalizeHelper(this.back.normalizeHelper(tail));
  }
  
  // left-scans across this list and concatenates all the strings, returning a normalized list
  // of the intermediate results at each step.
  public ILoString scanConcat() {
    return this.scanConcatHelper("");
  }
  
  // Left-scans across this list and concatenates all the strings, producing a list of the
  // results at each step. To do so, simply calls scanConcatHelper() on a normalized version of
  // this list.
  public ILoString scanConcatHelper(String acc) {
    /* TEMPLATE:
    PARAMETERS:
    ... acc ...   -- String
     */
    return this.normalize().scanConcatHelper(acc);
  }
}

class ExamplesLoString {
  ILoString empty = new MtLoString();
  ILoString addDoc = new SnocLoString(empty, "Doc");
  ILoString addBashful = new ConsLoString("Bashful", addDoc);
  ILoString addDopey = new SnocLoString(addBashful, "Dopey");
  
  ILoString grumpy = new ConsLoString("Grumpy", empty);
  
  ILoString addSleepy = new ConsLoString("Sleepy", empty);
  ILoString addHappy = new ConsLoString("Happy", addSleepy);
  ILoString addSneezy = new SnocLoString(addHappy, "Sneezy");
  
  ILoString dwarves = new AppendLoString(new AppendLoString(addDopey, grumpy), addSneezy);
  
  ILoString everybody = new ConsLoString("Snow White", dwarves);
  
  
  boolean testReverse(Tester t) {
    ILoString rAddDoc = new ConsLoString("Doc", empty);
    ILoString rAddBashful = new SnocLoString(rAddDoc, "Bashful");
    ILoString rAddDopey = new ConsLoString("Dopey", rAddBashful);
    
    ILoString rGrumpy = new SnocLoString(empty, "Grumpy");
    
    ILoString rAddSleepy = new SnocLoString(empty, "Sleepy");
    ILoString rAddHappy = new SnocLoString(rAddSleepy, "Happy");
    ILoString rAddSneezy = new ConsLoString("Sneezy", rAddHappy);
    
    ILoString rDwarves = new AppendLoString(rAddSneezy, new AppendLoString(rGrumpy, rAddDopey));
    ILoString rEverybody = new SnocLoString(rDwarves, "Snow White");
    
    return t.checkExpect(addBashful.reverse().normalize(), rAddBashful.normalize())
             && t.checkExpect(everybody.reverse().normalize(), rEverybody.normalize());
  }
  
  boolean testNormalize(Tester t) {
    return t.checkExpect(everybody.normalize(),
        new ConsLoString("Snow White",
            new ConsLoString("Bashful",
                new ConsLoString("Doc",
                    new ConsLoString("Dopey",
                        new ConsLoString("Grumpy",
                            new ConsLoString("Happy",
                                new ConsLoString("Sleepy",
                                    new ConsLoString("Sneezy",
                                        empty
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    );
  }
  
  boolean testNormalizeHelper(Tester t) {
    ILoString firstFour = new AppendLoString(addDopey, grumpy);
    return t.checkExpect(
        firstFour.normalizeHelper(new ConsLoString("secret dwarf", new MtLoString())),
        new ConsLoString("Bashful",
            new ConsLoString("Doc",
                new ConsLoString("Dopey",
                    new ConsLoString("Grumpy",
                        new ConsLoString("secret dwarf",
                            empty
                        )
                    )
                )
            )
        )
    );
  }
  
  boolean testScanConcat(Tester t) {
    ILoString empty = new MtLoString();
    ILoString sAddDoc = new SnocLoString(empty, "Snow WhiteBashfulDoc");
    ILoString sAddBashful = new ConsLoString("Snow WhiteBashful", sAddDoc);
    ILoString sAddDopey = new SnocLoString(sAddBashful, "Snow WhiteBashfulDocDopey");
    
    ILoString sGrumpy = new ConsLoString("Snow WhiteBashfulDocDopeyGrumpy", empty);
    
    ILoString sAddSleepy = new ConsLoString("Snow WhiteBashfulDocDopeyGrumpyHappySleepy", empty);
    ILoString sAddHappy = new ConsLoString("Snow WhiteBashfulDocDopeyGrumpyHappy", sAddSleepy);
    ILoString sAddSneezy = new SnocLoString(sAddHappy,
          "Snow WhiteBashfulDocDopeyGrumpyHappySleepySneezy");
    
    ILoString sDwarves = new AppendLoString(new AppendLoString(sAddDopey, sGrumpy), sAddSneezy);
    
    ILoString sEverybody = new ConsLoString("Snow White", sDwarves);
    
    ILoString oneList = new SnocLoString(new SnocLoString(empty, "one"), "two");
    
    return t.checkExpect(everybody.scanConcat(), sEverybody.normalize())
             && t.checkExpect(empty.scanConcat(), empty)
             && t.checkExpect(oneList.scanConcat(),
                    new ConsLoString("one", new ConsLoString("onetwo", empty)));
  }
  
  boolean testScanConcatHelper(Tester t) {
    ILoString firstFour = new AppendLoString(addDopey, grumpy);
    return t.checkExpect(
        firstFour.scanConcatHelper("secret dwarf"),
        new ConsLoString("secret dwarfBashful",
            new ConsLoString("secret dwarfBashfulDoc",
                new ConsLoString("secret dwarfBashfulDocDopey",
                    new ConsLoString("secret dwarfBashfulDocDopeyGrumpy",
                        empty
                    )
                )
            )
        )
    );
  }
}