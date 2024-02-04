import tester.Tester;

// SAMENESS =======================================================================================

// To a user, a list is an ordered set of numbers.
// It would not matter to them in what kind of data structure this list is stored.
// Hence, when checking for "sameness", irrespective of structures

// ================================================================================================

interface ILoInt {
  
  // Takes the current list and produces a list of the same items in the same order, but uses
  // only ConsLoInt and MtLoInt while simplifying away any instances of SnocLoInt and
  // AppendLoInt.
  ILoInt normalize();
  
  // Normalizes this list according to the purpose statement for normalize(), appending tail at
  // the end.
  // The accumulator, tail, is updated each time normalizeHelper encounters a SnocLoInt
  // (gaining a new ConsLoInt that wraps the existing tail), as well as each time
  // normalizeHelper encounters an AppendLoInt (swapping the existing tail with the
  // AppendLoInt's back, itself normalized with the existing tail)
  ILoInt normalizeHelper(ILoInt tail);
  
  boolean sameLoInt(ILoInt that);
  
  boolean sameConsLoInt(ConsLoInt that);
  
  boolean sameMtLoInt(MtLoInt that);
}

// Represents an element and the list of elements that follow it in a list of ints
class ConsLoInt implements ILoInt {
  int first;
  ILoInt rest;
  
  ConsLoInt(int first, ILoInt rest) {
    this.first = first;
    this.rest = rest;
  }
  /* TEMPLATE:
  FIELDS:
  ... this.first ...                             -- int
  ... this.rest ...                              -- ILoInt
  METHODS:
  ... normalize() ...                            -- ILoInt
  ... normalizeHelper(ILoInt tail) ...           -- ILoInt
  METHODS ON FIELDS:
  ... this.rest.normalizeHelper(tail) ...        -- ILoInt
   */
  
  // Takes the current list and produces a list of the same items in the same order, but uses
  // only ConsLoInt and MtLoInt.
  public ILoInt normalize() {
    return this.normalizeHelper(new MtLoInt());
  }
  
  // Takes the current list and produces a normalized list by creating a new ConsLoInt with
  // the same first element and the rest of the elements normalized with the same tail.
  public ILoInt normalizeHelper(ILoInt tail) {
    /* TEMPLATE:
    PARAMETERS:
    ... tail ...   -- ILoInt
     */
    return new ConsLoInt(this.first, this.rest.normalizeHelper(tail));
  }
  
  public boolean sameLoInt(ILoInt that) {
    return that.normalize().sameConsLoInt(this);
  }
  
  public boolean sameConsLoInt(ConsLoInt that) {
    return this.first == that.first && this.rest.sameLoInt(that.rest.normalize());
  }
  
  public boolean sameMtLoInt(MtLoInt that) {
    return false;
  }
}

// Represents an empty list of ints
class MtLoInt implements ILoInt {
  // Normalizes this MtLoInt by returning this, since an MtLoInt is a valid normalization.
  public ILoInt normalize() {
    return this;
  }
  
  // Normalizes this MtLoInt by substituting it with tail
  public ILoInt normalizeHelper(ILoInt tail) {
    /* TEMPLATE:
    PARAMETERS:
    ... tail ...   -- ILoInt
     */
    return tail;
  }
  
  public boolean sameLoInt(ILoInt that) {
    return that.sameMtLoInt(this);
  }
  
  public boolean sameConsLoInt(ConsLoInt that) {
    return false;
  }
  
  public boolean sameMtLoInt(MtLoInt that) {
    return true;
  }
}

// Represents an element and the list of elements that precede it in a list of ints
class SnocLoInt implements ILoInt {
  ILoInt front;
  int last;
  
  SnocLoInt(ILoInt front, int last) {
    this.front = front;
    this.last = last;
  }
  /* TEMPLATE:
  FIELDS:
  ... this.front ...                          -- ILoInt
  ... this.last ...                           -- int
  METHODS:
  ... normalize() ...                         -- ILoInt
  ... normalizeHelper(ILoInt tail) ...     -- ILoInt
  METHODS ON FIELDS:
  ... this.front.normalizeHelper() ...        -- ILoInt
   */
  
  // Takes the current list and produces a list of the same items in the same order, but uses
  // only ConsLoInt and MtLoInt
  public ILoInt normalize() {
    return this.normalizeHelper(new MtLoInt());
  }
  
  // Takes the current list and produces a normalized list by normalizing the front of this
  // SnocLoInt and appending the last to tail.
  public ILoInt normalizeHelper(ILoInt tail) {
    /* TEMPLATE:
    PARAMETERS:
    ... tail ...   -- ILoInt
     */
    return this.front.normalizeHelper(new ConsLoInt(this.last, tail));
  }
  
  public boolean sameLoInt(ILoInt that) {
    return this.normalize().sameLoInt(that);
  }
  
  public boolean sameConsLoInt(ConsLoInt that) {
    return false;
  }
  
  public boolean sameMtLoInt(MtLoInt that) {
    return false;
  }
}

// Represents two lists of ints that are concatenated side-by-side to form another list
class AppendLoInt implements ILoInt {
  ILoInt front;
  ILoInt back;
  
  AppendLoInt(ILoInt front, ILoInt back) {
    this.front = front;
    this.back = back;
  }
  
  /* TEMPLATE:
  FIELDS:
  ... this.front ...                        -- ILoInt
  ... this.back ...                         -- ILoInt
  METHODS:
  ... normalize() ...                       -- ILoInt
  ... normalizeHelper(ILoInt tail) ...      -- ILoInt
  METHODS ON FIELDS:
  ... this.front.normalizeHelper() ...      -- ILoInt
  ... this.back.normalizeHelper() ...       -- ILoInt
   */
  
  // Takes the current list and produces a list of the same items in the same order, but uses
  // only ConsLoInt and MtLoInt
  public ILoInt normalize() {
    return this.normalizeHelper(new MtLoInt());
  }
  
  // Takes the current list and produces a normalized list by normalizing this.front and swapping
  // the existing tail with this.back, itself normalized with the existing tail
  public ILoInt normalizeHelper(ILoInt tail) {
    /* TEMPLATE:
    PARAMETERS:
    ... tail ...   -- ILoInt
     */
    return this.front.normalizeHelper(this.back.normalizeHelper(tail));
  }
  
  public boolean sameLoInt(ILoInt that) {
    return this.normalize().sameLoInt(that);
  }
  
  public boolean sameConsLoInt(ConsLoInt that) {
    return false;
  }
  
  public boolean sameMtLoInt(MtLoInt that) {
    return false;
  }
}

class ExamplesLoInt {
  ILoInt empty = new MtLoInt();
  
  ILoInt zero = new SnocLoInt(new MtLoInt(), 0);
  
  ILoInt zeroes1 = new ConsLoInt(0, new ConsLoInt(0, new ConsLoInt(0, new ConsLoInt(0, empty))));
  
  ILoInt zeroes2 = new AppendLoInt(
      new ConsLoInt(0, new ConsLoInt(0, empty)),
      new ConsLoInt(0, new ConsLoInt(0, empty)));
  
  ILoInt zeroes3 = new AppendLoInt(
      new SnocLoInt(new ConsLoInt(0, empty), 0),
      new ConsLoInt(0, new ConsLoInt(0, empty))
  );
  
  ILoInt fibo1 = new ConsLoInt(1,
      new ConsLoInt(2,
          new SnocLoInt(
              new ConsLoInt(3,
                  new ConsLoInt(5,
                      new ConsLoInt(8,
                          empty))), 13)));
  
  ILoInt fibo2 = new ConsLoInt(1,
      new AppendLoInt(
          new ConsLoInt(2, new SnocLoInt(empty, 3)),
          new ConsLoInt(5, new AppendLoInt(
              new ConsLoInt(8, empty),
              new ConsLoInt(13, empty)))
      ));
  
  ILoInt list3 = new ConsLoInt(2,
      new ConsLoInt(1,
          new ConsLoInt(3,
              new ConsLoInt(4,
                  new ConsLoInt(7,
                      new ConsLoInt(11,
                          empty))))));
  
  
  ILoInt list4 = new ConsLoInt(1,
      new ConsLoInt(1,
          new ConsLoInt(0,
              new ConsLoInt(1,
                  new ConsLoInt(-1,
                      new ConsLoInt(2,
                          new ConsLoInt(-3,
                              new ConsLoInt(5,
                                  new ConsLoInt(-8,
                                      empty)))))))));
  
  ILoInt list5 = new ConsLoInt(13, new SnocLoInt(new AppendLoInt(list4, list3), 7));
  ILoInt list6 = new SnocLoInt(new ConsLoInt(13, new AppendLoInt(list4, list3)), 7);
  
  boolean testSameLoIntT(Tester t) {
    return t.checkExpect(empty.sameLoInt(empty), true)
        && t.checkExpect(zeroes1.sameLoInt(zeroes2), true)
        && t.checkExpect(zeroes2.sameLoInt(zeroes3), true)
        && t.checkExpect(zeroes1.sameLoInt(zeroes3), true);
  }
  
  boolean testSameLoIntF(Tester t) {
    return t.checkExpect(zero.sameLoInt(zeroes1), false)
        && t.checkExpect(zeroes1.sameLoInt(fibo1), false)
        && t.checkExpect(empty.sameLoInt(zero), false);
  }
}