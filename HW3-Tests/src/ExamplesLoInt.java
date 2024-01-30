import lists.*;
import tester.Tester;

class ExamplesLoInt {
  // your examples go here
  
  // Valid for all
  ILoInt empty = new MtLoInt();
  
  ILoInt zero = new ConsLoInt(0, new MtLoInt());
  
  ILoInt zeroes = new ConsLoInt(0,
      new ConsLoInt(0,
          new ConsLoInt(0,
              new ConsLoInt(0,
                  empty))));
  
  // Fibonacci-like
  ILoInt fibo = new ConsLoInt(1,
      new ConsLoInt(2,
          new ConsLoInt(3,
              new ConsLoInt(5,
                  new ConsLoInt(8,
                      new ConsLoInt(13,
                          empty))))));
  
  ILoInt lucas = new ConsLoInt(2,
      new ConsLoInt(1,
          new ConsLoInt(3,
              new ConsLoInt(4,
                  new ConsLoInt(7,
                      new ConsLoInt(11,
                          empty))))));
  
  // Pell-like
  ILoInt pell = new ConsLoInt(1,
      new ConsLoInt(2,
          new ConsLoInt(5,
              new ConsLoInt(12,
                  new ConsLoInt(29,
                      new ConsLoInt(70,
                          empty))))));
  
  // nega-Fibonacci-like
  ILoInt negaFibo = new ConsLoInt(1,
      new ConsLoInt(1,
          new ConsLoInt(0,
              new ConsLoInt(1,
                  new ConsLoInt(-1,
                      new ConsLoInt(2,
                          new ConsLoInt(-3,
                              new ConsLoInt(5,
                                  new ConsLoInt(-8,
                                      empty)))))))));
  
  // Jacobsthal-like
  ILoInt jacobsthal = new ConsLoInt(0,
      new ConsLoInt(1,
          new ConsLoInt(1,
              new ConsLoInt(3,
                  new ConsLoInt(5,
                      new ConsLoInt(11,
                          new ConsLoInt(21,
                              new ConsLoInt(43,
                                  new ConsLoInt(85,
                                      empty)))))))));
  
  
//  boolean testIsFibLike(Tester t) {
//    t.checkExpect(empty.isFibLike(), true)
//        && t.checkExpect(zeroes.isFibLike(), true)
//        && t.checkExpect(fibo.isFibLike(), true)
//        && t.checkExpect(lucas.isFibLike(), true)
//        && t.checkExpect()
//  }
//
//  boolean testIsPellLike(Tester t) {
//
//  }
//
//  boolean testIsNegaFibLike(Tester t) {
//
//  }
//
//  boolean testIsJacobsthalLike(Tester t) {
//
//  }
  
  boolean testSecondLargestNum1(Tester t) {
    // All zeroes
    return t.checkExpect(zeroes.secondLargestNum(), 0);
  }
//
//  boolean testSecondLargestNum2(Tester t) {
//    // Strictly increasing
//    return t.checkExpect(fibo.secondLargestNum(), 8);
//  }
  
//  boolean testSecondLargestNum3(Tester t) {
//    // Increasing and decreasing
//    return t.checkExpect(negaFibo.secondLargestNum(), 2);
//  }
  
  //  boolean testSecondLargestNumEx(Tester t) {
//    // Less than two numbers
//    return t.checkException(new RuntimeException(), empty, "secondLargestNum")
//               && t.checkException(new RuntimeException(), zero, "secondLargestNum");
//  }
  
//  boolean testFifthLargestNum1(Tester t) {
//    return t.checkExpect(fibo.fifthLargestNum(), 2);
//  }
//
//  boolean testFifthLargestNum2(Tester t) {
//    return t.checkExpect(lucas.fifthLargestNum(), 2);
//  }
//
//  boolean testFifthLargestNum3(Tester t) {
//    return t.checkExpect(pell.fifthLargestNum(), 2);
//  }
  
  boolean testFifthLargestNum4(Tester t) {
    return t.checkExpect(negaFibo.fifthLargestNum(), 1);
  }
  
//  boolean testFifthLargestNum5(Tester t) {
//    return t.checkExpect(jacobsthal.fifthLargestNum(), 5);
//  }
  
//  boolean testFifthLargestNumEx(Tester t) {
//    return t.checkException(new RuntimeException(), empty, "fifthLargestNum")
//        && t.checkException(new RuntimeException(), zeroes, "fifthLargestNum");
//  }
  

//  boolean testMostCommonNum1(Tester t) {
//    return t.checkExpect(zero.mostCommonNum(), 0);
//  }
//
//  boolean testMostCommonNum2(Tester t) {
//    return t.checkExpect(zeroes.mostCommonNum(), 0);
//  }
  
  boolean testMostCommonNum3(Tester t) {
    return t.checkOneOf(fibo.mostCommonNum(), 1, 2, 3, 5, 8, 13);
  }
  
//  boolean testMostCommonNum4(Tester t) {
//    return t.checkExpect(negaFibo.mostCommonNum(), 1);
//  }
  
//  boolean testMostCommonNum5(Tester t) {
//    ILoInt test = new ConsLoInt(3,
//        new ConsLoInt(2,
//            new ConsLoInt(3,
//                new ConsLoInt(1,
//                    new ConsLoInt(5,
//                        new ConsLoInt(1,
//                            new MtLoInt()))))));
//    return t.checkOneOf(test.mostCommonNum(), 1, 3);
//  }
  
//  boolean testThirdMostCommonNum1 (Tester t) { // Doesn't work
//    return t.checkExpect(zeroes.thirdMostCommonNum(), 0);
//  }
//
//  boolean testThirdMostCommonNum2(Tester t) { // Doesn't work
//    ILoInt test = new ConsLoInt(3,
//        new ConsLoInt(2,
//            new ConsLoInt(3,
//                new ConsLoInt(1,
//                    new ConsLoInt(5,
//                        new ConsLoInt(1,
//                            new MtLoInt()))))));
//    return t.checkOneOf(test.thirdMostCommonNum(), 1, 3);
//  }
  
  boolean testThirdMostCommonNum3(Tester t) {
    ILoInt test = new ConsLoInt(3,
        new ConsLoInt(2,
            new ConsLoInt(3,
                new ConsLoInt(1,
                    new ConsLoInt(1,
                        new ConsLoInt(3,
                            new ConsLoInt(2,
                                new MtLoInt())))))));
    return t.checkOneOf(test.thirdMostCommonNum(), 1, 2);
  }
}