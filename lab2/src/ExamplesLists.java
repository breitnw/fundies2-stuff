import lists.*;
import tester.*;

class ExamplesLists {
  ILoInt zero = new MtLoInt();
  ILoInt one = new ConsLoInt(1, zero);
  ILoInt two = new ConsLoInt(2, one);
  ILoInt three = new ConsLoInt(3, two);
  boolean testLength1(Tester t) {
    return t.checkExpect(three.length(), 3);
  }

  boolean testContains1(Tester t) {
    return t.checkExpect(zero.contains(22), false);
  }

  boolean testContains2(Tester t) {
    return t.checkExpect(one.contains(1), true);
  }

  boolean testContains3(Tester t) {
    return t.checkExpect(two.contains(3), false);
  }

  boolean testContains4(Tester t) {
    return t.checkExpect(two.contains(1), true);
  }
}