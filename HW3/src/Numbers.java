import tester.Tester;

// Represents a list of integers.
interface ILoInt {
  // Determines if the sequence is Fibonacci-like; i.e., it follows a rule where each
  // 𝐹(𝑛+1)=𝐹(𝑛−1)+𝐹(𝑛)
  boolean isFibLike();
  
  // Determines if the sequence is Pell-like; i.e., it follows a rule where each
  // 𝑃(𝑛+1)=𝑃(𝑛−1)+2*𝑃(𝑛)
  boolean isPellLike();
  
  // Determines if the sequence is Nega-Fibonacci-like; i.e., it follows a rule where each
  // 𝑁(𝑛+1)=𝑁(𝑛−1)−𝑁𝑛
  boolean isNegaFibLike();
  
  // Determines if the sequence is Jacobsthal-like; i.e., it follows a rule where each
  // 𝐽(𝑛+1)=2*𝐽(𝑛−1)+𝐽(𝑛)
  boolean isJacobsthalLike();
  
  // Returns the second-greatest number in the list of integers (Repeated Numbers counted as
  // individual terms). If there are less than two numbers in the list, throws a RuntimeException.
  int secondLargestNum();
  
  // Returns the fifth-greatest number in the list of integers (Repeated Numbers counted as
  // individual terms). If there are less than five numbers in the list, throws a RuntimeException.
  int fifthLargestNum();
  
  // Returns the most common number in the list of integers. If the two most common numbers have
  // the same frequency, returns only one of them. If the list is empty, throws an exception.
  int mostCommonNum();
  
  // Returns the third most common number in the list of integers. If the frequency of numbers is
  // tied for this position, one of the numbers is returned. Throws an exception if the list has
  // less than three distinct numbers.
  int thirdMostCommonNum();
  
  // HELPERS --------------------------------------------------------------------------------------
  
  // Determines if each set of three consecutive elements in the sequence satisfies the condition
  // defined by a provided SequenceCmp.
  boolean isLike(SequenceCmp type);
  
  // Determines if each set of three consecutive elements in the sequence satisfies the condition
  // defined by a provided SequenceCmp. Assumes that the previous element before the sequence was
  // nMinusOne.
  boolean isLike1Prev(SequenceCmp type, int nMinusOne);
  
  // Determines if each set of three consecutive elements in the sequence satisfies the condition
  // defined by a provided SequenceCmp. Assumes that the previous element before the sequence was
  // nMinusOne, and the element before that was nMinusTwo.
  boolean isLike2Prev(SequenceCmp type, int nMinusTwo, int nMinusOne);
  
  // Returns the single largest number in this ILoInt, throwing an exception if the ILoInt is empty.
  int largestNum();
  
  // Continues a largestNum call by finding the largest number in this ILoInt, given that
  // curLargest is the largest element so far.
  int largestNumCont(int curLargest);
  
  // Removes the first appearance of val in this and returns the result. If val does not appear
  // in this, throws an exception.
  ILoInt removeOne(int val);
  
  // Insertion-sorts this ILoInt.
  ILoInt insertionSort();
  
  // Inserts val in this ILoInt, immediately before the first element that exceeds its value. If
  // no element exceeds its value, inserts it at the end.
  ILoInt insert(int val);
  
  // Removes all appearances of val in this ILoInt and returns the result.
  ILoInt removeAll(int val);
  
  // Continues a mostCommonNum call, returning the most common number in this ILoInt given that
  // the most common value is commonVal (appearing commonCount times), the last seen element is
  // lastVal (appearing lastCount times), and the rest of this ILoInt is sorted.
  int mostCommonNumCont(int commonVal, int commonCount, int lastVal, int lastCount);
}

// Represents a list of integers with a first element and a list of other elements.
class ConsLoInt implements ILoInt {
  int first;
  ILoInt rest;
  
  ConsLoInt(int first, ILoInt rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /* TEMPLATE:
  FIELDS:
  ... this.first ...                                                      -- int
  ... this.rest ...                                                       -- ILoInt
  METHODS:
  ... isFibLike() ...                                                     -- boolean
  ... isPellLike() ...                                                    -- boolean
  ... isNegaFibLike() ...                                                 -- boolean
  ... isJacobsthalLike() ...                                              -- boolean
  ... secondLargestNum() ...                                              -- int
  ... fifthLargestNum() ...                                               -- int
  ... mostCommonNum() ...                                                 -- int
  ... thirdMostCommonNum() ...                                            -- int
  ... isLike(SequenceCmp type) ...                                        -- boolean
  ... IsLike1Prev(SequenceCmp type, int nMinusOne) ...                    -- boolean
  ... IsLike2Prev(SequenceCmp type, int nMinusTwo, int nMinusOne) ...     -- boolean
  ... largestNum() ...                                                    -- int
  ... largestNumCont(int curLargest) ...                                  -- int
  ... removeOne(int val) ...                                              -- ILoInt
  ... insertionSort() ...                                                 -- ILoInt
  ... insert(int val) ...                                                 -- ILoInt
  ... removeAll(int val) ...                                              -- ILoInt
  ... mostCommonNumCont(int commonVal, int commonCount, int lastVal, int lastCount) ...  -- int
  METHODS ON FIELDS:
  ... this.rest.IsLike1Prev(type, this.first) ...                         -- boolean
  ... this.rest.IsLike2Prev(type, nMinusOne, this.first) ...              -- boolean
  ... this.rest.largestNumCont(Math.max(curLargest, this.first)) ...      -- int
  ... this.rest.removeOne(val) ...                                        -- ILoInt
  ... this.rest.insertionSort().insert(this.first) ...                    -- ILoInt
  ... this.rest.insert(val) ...                                           -- ILoInt
  ... this.rest.removeAll(val) ...                                        -- ILoInt
  ... this.rest.mostCommonNumCont(newCommonVal, newCommonCount, this.first, curCount) ...-- int
   */
  
  // Determines if the sequence is Fibonacci-like; i.e., it follows a rule where each
  // 𝐹(𝑛+1)=𝐹(𝑛−1)+𝐹(𝑛)
  public boolean isFibLike() {
    return this.isLike(new FibCmp());
  }
  
  // Determines if the sequence is Pell-like; i.e., it follows a rule where each
  // 𝑃(𝑛+1)=𝑃(𝑛−1)+2*𝑃(𝑛)
  public boolean isPellLike() {
    return this.isLike(new PellCmp());
  }
  
  // Determines if the sequence is Nega-Fibonacci-like; i.e., it follows a rule where each
  // 𝑁(𝑛+1)=𝑁(𝑛−1)−𝑁𝑛
  public boolean isNegaFibLike() {
    return this.isLike(new NegaFibCmp());
  }
  
  // Determines if the sequence is Jacobsthal-like; i.e., it follows a rule where each
  // 𝐽(𝑛+1)=2*𝐽(𝑛−1)+𝐽(𝑛)
  public boolean isJacobsthalLike() {
    return this.isLike(new JacobsthalCmp());
  }
  
  // Returns the second-greatest number in the list of integers (Repeated numbers counted as
  // individual terms). If there are less than two numbers in the list, throws a RuntimeException.
  public int secondLargestNum() {
    int firstLargest = this.largestNum();
    return this.removeOne(firstLargest).largestNum();
  }
  
  // Returns the fifth-greatest number in the list of integers (Repeated numbers counted as
  // individual terms). If there are less than five numbers in the list, throws a RuntimeException.
  public int fifthLargestNum() {
    int firstLargest = this.largestNum();
    ILoInt remFirst = this.removeOne(firstLargest);
    int secondLargest = remFirst.largestNum();
    ILoInt remSecond = remFirst.removeOne(secondLargest);
    int thirdLargest = remSecond.largestNum();
    ILoInt remThird = remSecond.removeOne(thirdLargest);
    int fourthLargest = remThird.largestNum();
    ILoInt remFourth = remThird.removeOne(fourthLargest);
    
    return remFourth.largestNum();
  }
  
  // Returns the most common number in the list of integers. If the two most common numbers have
  // the same frequency, returns only one of them. If the list is empty, throws an exception.
  public int mostCommonNum() {
    return this.insertionSort().mostCommonNumCont(0, 0, 0, 0);
  }
  
  // Returns the third most common number in the list of integers. If the frequency of numbers is
  // tied for this position, one of the numbers is returned. Throws an exception if the list has
  // less than three distinct numbers.
  public int thirdMostCommonNum() {
    int firstMostCommon = this.mostCommonNum();
    ILoInt remFirst = this.removeAll(firstMostCommon);
    int secondMostCommon = remFirst.mostCommonNum();
    ILoInt remSecond = remFirst.removeAll(secondMostCommon);
    
    return remSecond.mostCommonNum();
  }
  
  // HELPERS --------------------------------------------------------------------------------------
  
  // Determines if each set of three consecutive elements in the sequence satisfies the condition
  // defined by a provided SequenceCmp.
  public boolean isLike(SequenceCmp type) {
    /* TEMPLATE:
    PARAMETERS:
    ... type ...   -- SequenceCmp
     */
    return this.rest.isLike1Prev(type, this.first);
  }
  
  // Determines if each set of three consecutive elements in the sequence satisfies the condition
  // defined by a provided SequenceCmp. Assumes that the previous element before the sequence was
  // nMinusOne.
  public boolean isLike1Prev(SequenceCmp type, int nMinusOne) {
    /* TEMPLATE:
    PARAMETERS:
    ... type ...        -- SequenceCmp
    ... nMinusOne ...   -- int
     */
    return this.rest.isLike2Prev(type, nMinusOne, this.first);
  }
  
  // Determines if each set of three consecutive elements in the sequence satisfies the condition
  // defined by a provided SequenceCmp. Assumes that the previous element before the sequence was
  // nMinusOne, and the element before that was nMinusTwo.
  public boolean isLike2Prev(SequenceCmp type, int nMinusTwo, int nMinusOne) {
    /* TEMPLATE:
    PARAMETERS:
    ... type ...                                             -- SequenceCmp
    ... nMinusTwo ...                                        -- int
    ... nMinusOne ...                                        -- int
    METHODS ON PARAMETERS:
    ... type.compare(nMinusTwo, nMinusOne, this.first) ...   -- boolean
     */
    return type.compare(nMinusTwo, nMinusOne, this.first)
        && this.rest.isLike2Prev(type, nMinusOne, this.first);
  }
  
  // Returns the single largest number in this ConsLoInt.
  public int largestNum() {
    return this.largestNumCont(this.first);
  }
  
  // Given that curLargest is the largest so far, finds the largest number in this ConsLoInt.
  public int largestNumCont(int curLargest) {
    /* TEMPLATE:
    PARAMETERS:
    ... curLargest ...   -- int
     */
    return this.rest.largestNumCont(Math.max(curLargest, this.first));
  }
  
  // Removes the first appearance of val in this ConsLoInt. If val does not appear in this,
  // throws an exception.
  public ILoInt removeOne(int val) {
    /* TEMPLATE:
    PARAMETERS:
    ... val ...   -- int
     */
    if (val == this.first) {
      return this.rest;
    } else {
      return new ConsLoInt(this.first, this.rest.removeOne(val));
    }
  }
  
  // Insertion-sorts this ConsLoInt.
  public ILoInt insertionSort() {
    return this.rest.insertionSort().insert(this.first);
  }
  
  // Inserts val in this ILoInt, immediately before the first element that exceeds its value. If
  // no element exceeds its value, inserts it at the end.
  public ILoInt insert(int val) {
    /* TEMPLATE:
    PARAMETERS:
    ... val ...   -- int
     */
    if (val < this.first) {
      return new ConsLoInt(val, this);
    } else {
      return new ConsLoInt(this.first, this.rest.insert(val));
    }
  }
  
  // Removes all appearances of val in this ConsLoInt and returns the result.
  public ILoInt removeAll(int val) {
    /* TEMPLATE:
    PARAMETERS:
    ... val ...   -- int
     */
    if (val == this.first) {
      return this.rest.removeAll(val);
    } else {
      return new ConsLoInt(this.first, this.rest.removeAll(val));
    }
  }
  
  // Given that the most common value is commonVal (appearing commonCount times), the last
  // seen element is lastVal (appearing lastCount times), and the rest of this ILoInt is sorted,
  // returns the most common number in this ConsLoInt.
  public int mostCommonNumCont(int commonVal, int commonCount, int lastVal, int lastCount) {
    /* TEMPLATE:
    PARAMETERS:
    ... commonVal ...     -- int
    ... commonCount ...   -- int
    ... lastVal ...       -- int
    ... lastCount ...     -- int
     */
    int curCount;
    
    if (this.first == lastVal) {
      curCount = lastCount + 1;
    } else {
      curCount = 1;
    }
    
    int newCommonVal;
    int newCommonCount;
    
    if (curCount > commonCount) {
      newCommonVal = this.first;
      newCommonCount = curCount;
    } else {
      newCommonVal = commonVal;
      newCommonCount = commonCount;
    }
    
    return this.rest.mostCommonNumCont(newCommonVal, newCommonCount, this.first, curCount);
  }
}

// Returns an empty list of integers.
class MtLoInt implements ILoInt {
  
  /* TEMPLATE:
  METHODS:
  ... isFibLike() ...                                                     -- boolean
  ... isPellLike() ...                                                    -- boolean
  ... isNegaFibLike() ...                                                 -- boolean
  ... isJacobsthalLike() ...                                              -- boolean
  ... secondLargestNum() ...                                              -- int
  ... fifthLargestNum() ...                                               -- int
  ... mostCommonNum() ...                                                 -- int
  ... thirdMostCommonNum() ...                                            -- int
  ... isLike(SequenceCmp type) ...                                        -- boolean
  ... IsLike1Prev(SequenceCmp type, int nMinusOne) ...                    -- boolean
  ... IsLike2Prev(SequenceCmp type, int nMinusTwo, int nMinusOne) ...     -- boolean
  ... largestNum() ...                                                    -- int
  ... largestNumCont(int curLargest) ...                                  -- int
  ... removeOne(int val) ...                                              -- ILoInt
  ... insertionSort() ...                                                 -- ILoInt
  ... insert(int val) ...                                                 -- ILoInt
  ... removeAll(int val) ...                                              -- ILoInt
  ... mostCommonNumCont(int commonVal, int commonCount, int lastVal, int lastCount) ...  -- int
   */
  
  // Determines if the sequence is Fibonacci-like; i.e., it follows a rule where each
  // 𝐹(𝑛+1)=𝐹(𝑛−1)+𝐹(𝑛)
  public boolean isFibLike() {
    return true;
  }
  
  // Determines if the sequence is Pell-like; i.e., it follows a rule where each
  // 𝑃(𝑛+1)=𝑃(𝑛−1)+2*𝑃(𝑛)
  public boolean isPellLike() {
    return true;
  }
  
  // Determines if the sequence is Nega-Fibonacci-like; i.e., it follows a rule where each
  // 𝑁(𝑛+1)=𝑁(𝑛−1)−𝑁𝑛
  public boolean isNegaFibLike() {
    return true;
  }
  
  // Determines if the sequence is Jacobsthal-like; i.e., it follows a rule where each
  // 𝐽(𝑛+1)=2*𝐽(𝑛−1)+𝐽(𝑛)
  public boolean isJacobsthalLike() {
    return true;
  }
  
  // Returns the second-greatest number in the list of integers. Since there are less than two
  // numbers in the list, throws a RuntimeException.
  public int secondLargestNum() {
    throw new RuntimeException("Passed a empty list, which may not have a second largest number");
  }
  
  // Returns the fifth-greatest number in the list of integers. Since there are less than five
  // numbers in the list, throws a RuntimeException.
  public int fifthLargestNum() {
    throw new RuntimeException("Passed an empty list, which may not have a fifth largest number");
  }
  
  // Returns the most common number in the list of integers. Since the list is empty, throws an
  // exception.
  public int mostCommonNum() {
    throw new RuntimeException("Passed an empty list, which may hot have a most common number");
  }
  
  // Returns the third most common number in the list of integers. Throws an exception, since the
  // list has less than three distinct numbers.
  public int thirdMostCommonNum() {
    throw new RuntimeException("Passed an empty list, which may not have a third most "
                                   + "common number");
  }
  
  // HELPERS --------------------------------------------------------------------------------------
  
  // Determines if each set of three consecutive elements in the sequence satisfies the condition
  // defined by a provided SequenceCmp.
  public boolean isLike(SequenceCmp type) {
    /* TEMPLATE:
    PARAMETERS:
    ... type ...   -- SequenceCmp
     */
    return true;
  }
  
  // Determines if each set of three consecutive elements in the sequence satisfies the condition
  // defined by a provided SequenceCmp. Assumes that the previous element before the sequence was
  // nMinusOne.
  public boolean isLike1Prev(SequenceCmp type, int nMinusOne) {
    /* TEMPLATE:
    PARAMETERS:
    ... type ...        -- Sequence
    ... nMinusOne ...   -- int
     */
    return true;
  }
  
  // Determines if each set of three consecutive elements in the sequence satisfies the condition
  // defined by a provided SequenceCmp. Assumes that the previous element before the sequence was
  // nMinusOne, and the element before that was nMinusTwo.
  public boolean isLike2Prev(SequenceCmp type, int nMinusTwo, int nMinusOne) {
    /* TEMPLATE:
    PARAMETERS:
    ... type ...        -- SequenceCmp
    ... nMinusTwo ...   -- int
    ... nMinusOne ...   -- int
     */
    return true;
  }
  
  // Returns the single largest number in the MtLoInt, throwing an exception since it is empty.
  public int largestNum() {
    throw new RuntimeException("Empty list has no maximum");
  }
  
  // Given that curLargest is the largest element so far, finds the largest number in this list
  // of integers.
  public int largestNumCont(int curLargest) {
    /* TEMPLATE:
    PARAMETERS:
    ... curLargest ...   -- int
     */
    return curLargest;
  }
  
  // Removes the first appearance of val in this and returns the result. Since val does not appear
  // in this, throws an exception.
  public ILoInt removeOne(int val) {
    /* TEMPLATE:
    PARAMETERS:
    ... val ...   -- int
     */
    throw new RuntimeException("Provided value not present in list, so cannot be removed");
  }
  
  // Insertion-sorts this MtLoInt.
  public ILoInt insertionSort() {
    return this;
  }
  
  // Inserts val in this MtLoInt, immediately before the first element that exceeds its value.
  // Since no element exceeds its value, inserts it at the end.
  public ILoInt insert(int val) {
    /* TEMPLATE:
    PARAMETERS:
    ... val ...   -- int
     */
    return new ConsLoInt(val, this);
  }
  
  // Removes all appearances of val in this ILoInt and returns the result.
  public ILoInt removeAll(int val) {
    /* TEMPLATE:
    PARAMETERS:
    ... val ...   -- int
     */
    return this;
  }
  
  // Given that the most common value so far is commonVal (appearing commonCount times) and the
  // last seen element is lastVal (appearing lastCount times), returns the most common number in
  // this list of integers.
  public int mostCommonNumCont(int commonVal, int commonCount, int lastVal, int lastCount) {
    /* TEMPLATE:
    PARAMETERS:
    ... commonVal ...     -- int
    ... commonCount ...   -- int
    ... lastVal ...       -- int
    ... lastCount ...     -- int
     */
    return commonVal;
  }
}

// SEQUENCE ELEMENT COMPARISON --------------------------------------------------------------------

// Represents the structure of a given type of sequence; i.e., the rules of how its elements
// compare to each other.
interface SequenceCmp {
  // Returns true if three specified elements of a sequence satisfy this SequenceCmp's "rule"
  boolean compare(int nMinusTwo, int nMinusOne, int n);
}

// Represents the comparison rule of a Fibonacci sequence.
class FibCmp implements SequenceCmp {
  /* TEMPLATE:
  METHODS:
  ... compare(int nMinusTwo, int nMinusOne, int n) ...   -- boolean
   */
  
  // Determines if three elements satisfy the comparison rule of a Fibonacci sequence:
  // n is equal to nMinusTwo + nMinusOne
  public boolean compare(int nMinusTwo, int nMinusOne, int n) {
    /* TEMPLATE:
    PARAMETERS:
    ... nMinusTwo ...   -- int
    ... nMinusOne ...   -- int
    ... n ...           -- int
     */
    return nMinusTwo + nMinusOne == n;
  }
}

// Represents the comparison rule of a Pell sequence.
class PellCmp implements SequenceCmp {
  /* TEMPLATE:
  METHODS:
  ... compare(int nMinusTwo, int nMinusOne, int n) ...   -- boolean
   */
  
  // Determines if three elements satisfy the comparison rule of a Fibonacci sequence:
  // n is equal to nMinusTwo + 2 * nMinusOne
  public boolean compare(int nMinusTwo, int nMinusOne, int n) {
    /* TEMPLATE:
    PARAMETERS:
    ... nMinusTwo ...   -- int
    ... nMinusOne ...   -- int
    ... n ...           -- int
     */
    return nMinusTwo + 2 * nMinusOne == n;
  }
}

// Represents the comparison rule of a Nega-Fibonacci sequence.
class NegaFibCmp implements SequenceCmp {
  /* TEMPLATE:
  METHODS:
  ... compare(int nMinusTwo, int nMinusOne, int n) ...   -- boolean
   */
  
  // Determines if three elements satisfy the comparison rule of a Fibonacci sequence:
  // n is equal to nMinusTwo - nMinusOne
  public boolean compare(int nMinusTwo, int nMinusOne, int n) {
    /* TEMPLATE:
    PARAMETERS:
    ... nMinusTwo ...   -- int
    ... nMinusOne ...   -- int
    ... n ...           -- int
     */
    return nMinusTwo - nMinusOne == n;
  }
}

// Represents the comparison rule of a Jacobsthal sequence.
class JacobsthalCmp implements SequenceCmp {
  /* TEMPLATE:
  METHODS:
  ... compare(int nMinusTwo, int nMinusOne, int n) ...   -- boolean
   */
  
  // Determines if three elements satisfy the comparison rule of a Fibonacci sequence:
  // n is equal to 2 * nMinusTwo + nMinusOne
  public boolean compare(int nMinusTwo, int nMinusOne, int n) {
    /* TEMPLATE:
    PARAMETERS:
    ... nMinusTwo ...   -- int
    ... nMinusOne ...   -- int
    ... n ...           -- int
     */
    return 2 * nMinusTwo + nMinusOne == n;
  }
}

class ExamplesLoInt {
  
  // EXAMPLES -------------------------------------------------------------------------------------
  
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
  
  // TESTS ----------------------------------------------------------------------------------------
  
  // "is_Like" methods
  boolean testIsFibLike(Tester t) {
    return t.checkExpect(empty.isFibLike(), true)
        && t.checkExpect(zeroes.isFibLike(), true)
        && t.checkExpect(fibo.isFibLike(), true)
        && t.checkExpect(lucas.isFibLike(), true)
        && t.checkExpect(pell.isFibLike(), false);
  }

  boolean testIsPellLike(Tester t) {
    return t.checkExpect(empty.isPellLike(), true)
        && t.checkExpect(zeroes.isPellLike(), true)
        && t.checkExpect(pell.isPellLike(), true)
        && t.checkExpect(lucas.isPellLike(), false);
  }

  boolean testIsNegaFibLike(Tester t) {
    return t.checkExpect(empty.isNegaFibLike(), true)
        && t.checkExpect(zeroes.isNegaFibLike(), true)
        && t.checkExpect(negaFibo.isNegaFibLike(), true)
        && t.checkExpect(pell.isNegaFibLike(), false);
  }

  boolean testIsJacobsthalLike(Tester t) {
    return t.checkExpect(empty.isJacobsthalLike(), true)
        && t.checkExpect(zeroes.isJacobsthalLike(), true)
        && t.checkExpect(jacobsthal.isJacobsthalLike(), true)
        && t.checkExpect(negaFibo.isJacobsthalLike(), false);
  }
  
  // secondLargestNum
  boolean testSecondLargestNum(Tester t) {
    return
        // All zeroes
        t.checkExpect(zeroes.secondLargestNum(), 0)
        // Strictly increasing
        && t.checkExpect(fibo.secondLargestNum(), 8)
        // Increasing and decreasing
        && t.checkExpect(negaFibo.secondLargestNum(), 2);
  }
  
  boolean testSecondLargestNumEx(Tester t) {
    // Less than two numbers
    return t.checkException(new RuntimeException(
        "Passed a empty list, which may not have a second largest number"),
            empty, "secondLargestNum")
        && t.checkException(new RuntimeException("Empty list has no maximum"),
            zero, "secondLargestNum");
  }

  // fifthLargestNum
  boolean testFifthLargestNum(Tester t) {
    return t.checkExpect(fibo.fifthLargestNum(), 2)
        && t.checkExpect(lucas.fifthLargestNum(), 2)
        && t.checkExpect(pell.fifthLargestNum(), 2)
        && t.checkExpect(jacobsthal.fifthLargestNum(), 5);
  }

  boolean testFifthLargestNumEx(Tester t) {
    return t.checkException(
        new RuntimeException("Passed an empty list, which may not have a fifth largest number"),
            empty, "fifthLargestNum")
        && t.checkException(
            new RuntimeException("Empty list has no maximum"), zeroes, "fifthLargestNum");
  }

  // mostCommonNum
  boolean testMostCommonNum(Tester t) {
    ILoInt test = new ConsLoInt(3,
        new ConsLoInt(2,
            new ConsLoInt(3,
                new ConsLoInt(1,
                    new ConsLoInt(5,
                        new ConsLoInt(1,
                            new MtLoInt()))))));
    
    return t.checkExpect(zero.mostCommonNum(), 0)
        && t.checkExpect(zeroes.mostCommonNum(), 0)
        && t.checkOneOf(fibo.mostCommonNum(), 1, 2, 3, 5, 8, 13)
        && t.checkExpect(negaFibo.mostCommonNum(), 1)
        && t.checkOneOf(test.mostCommonNum(), 1, 3);
    
  }

  // thirdMostCommonNum
  boolean testThirdMostCommonNum(Tester t) {
    ILoInt test1 = new ConsLoInt(3,
        new ConsLoInt(2,
            new ConsLoInt(3,
                new ConsLoInt(1,
                    new ConsLoInt(5,
                        new ConsLoInt(1,
                            new MtLoInt()))))));
    
    ILoInt test2 = new ConsLoInt(3,
        new ConsLoInt(2,
            new ConsLoInt(3,
                new ConsLoInt(1,
                    new ConsLoInt(1,
                        new ConsLoInt(3,
                            new ConsLoInt(2,
                                new MtLoInt())))))));
    
    return t.checkOneOf(test1.thirdMostCommonNum(), 2, 5)
        && t.checkOneOf(test2.thirdMostCommonNum(), 1, 2);
  }
  
  // HELPER TESTS ---------------------------------------------------------------------------------
  
  boolean testIsLike(Tester t) {
    return t.checkExpect(empty.isLike(new FibCmp()), true)
        && t.checkExpect(empty.isLike(new PellCmp()), true)
        && t.checkExpect(fibo.isLike(new FibCmp()), true)
        && t.checkExpect(pell.isLike(new PellCmp()), true)
        && t.checkExpect(negaFibo.isLike(new NegaFibCmp()), true)
        && t.checkExpect(jacobsthal.isLike(new JacobsthalCmp()), true)
        && t.checkExpect(fibo.isLike(new PellCmp()), false);
  }
  
  boolean testIsLike1Prev(Tester t) {
    return t.checkExpect(empty.isLike1Prev(new FibCmp(), 0), true)
        && t.checkExpect(fibo.isLike1Prev(new FibCmp(), 1), true)
        && t.checkExpect(fibo.isLike1Prev(new FibCmp(), 0), false)
        && t.checkExpect(fibo.isLike1Prev(new PellCmp(), 1), false);
  }
  
  boolean testIsLike2Prev(Tester t) {
    return t.checkExpect(empty.isLike2Prev(new FibCmp(), 8, 3), true)
        && t.checkExpect(fibo.isLike2Prev(new FibCmp(), 0, 1), true)
        && t.checkExpect(fibo.isLike2Prev(new FibCmp(), 0, 0), false)
        && t.checkExpect(fibo.isLike2Prev(new PellCmp(), 0, 1), false);
  }
  
  boolean testLargestNum(Tester t) {
    return t.checkException(new RuntimeException("Empty list has no maximum"), empty, "largestNum")
        && t.checkExpect(zeroes.largestNum(), 0)
        && t.checkExpect(fibo.largestNum(), 13)
        && t.checkExpect(lucas.largestNum(), 11)
        && t.checkExpect(pell.largestNum(), 70)
        && t.checkExpect(negaFibo.largestNum(), 5)
        && t.checkExpect(jacobsthal.largestNum(), 85);
  }
  
  boolean testLargestNumCont(Tester t) {
    return t.checkExpect(zeroes.largestNumCont(0), 0)
        && t.checkExpect(empty.largestNumCont(0), 0)
        && t.checkExpect(fibo.largestNumCont(0), 13)
        && t.checkExpect(negaFibo.largestNumCont(8), 8)
        && t.checkExpect(negaFibo.largestNumCont(0), 5);
  }
  
  boolean testRemoveOne(Tester t) {
    return t.checkException(new RuntimeException(
        "Provided value not present in list, so cannot be removed"), empty, "removeOne", 0)
        && t.checkExpect(zero.removeOne(0), new MtLoInt())
        && t.checkExpect(zeroes.removeOne(0), new ConsLoInt(0,
            new ConsLoInt(0,
                new ConsLoInt(0,
                    empty))))
        && t.checkExpect(jacobsthal.removeOne(5), new ConsLoInt(0,
            new ConsLoInt(1,
                new ConsLoInt(1,
                    new ConsLoInt(3,
                        new ConsLoInt(11,
                            new ConsLoInt(21,
                                new ConsLoInt(43,
                                    new ConsLoInt(85,
                                        empty)))))))));
  }
  
  boolean testInsertionSort(Tester t) {
    return t.checkExpect(negaFibo.insertionSort(),
        new ConsLoInt(-8,
            new ConsLoInt(-3,
                new ConsLoInt(-1,
                    new ConsLoInt(0,
                        new ConsLoInt(1,
                            new ConsLoInt(1,
                                new ConsLoInt(1,
                                    new ConsLoInt(2,
                                        new ConsLoInt(5, empty))))))))));
  }
  
  boolean testInsert(Tester t) {
    return t.checkExpect(fibo.insert(4),
        new ConsLoInt(1,
            new ConsLoInt(2,
                new ConsLoInt(3,
                    new ConsLoInt(4,
                        new ConsLoInt(5,
                            new ConsLoInt(8,
                                new ConsLoInt(13,
                                    empty))))))))
        && t.checkExpect(fibo.insert(8),
        new ConsLoInt(1,
            new ConsLoInt(2,
                new ConsLoInt(3,
                    new ConsLoInt(5,
                        new ConsLoInt(8,
                            new ConsLoInt(8,
                                new ConsLoInt(13,
                                    empty))))))));
  }
  
  boolean testRemoveAll(Tester t) {
    return t.checkExpect(negaFibo.removeAll(1),
        new ConsLoInt(0,
            new ConsLoInt(-1,
                new ConsLoInt(2,
                    new ConsLoInt(-3,
                        new ConsLoInt(5,
                            new ConsLoInt(-8, empty)))))))
        && t.checkExpect(zeroes.removeAll(0),
            new MtLoInt())
        && t.checkExpect(zeroes.removeAll(1),
            zeroes);
  }
  
  boolean testMostCommonNumCont(Tester t) {
    ILoInt test = new ConsLoInt(1,
        new ConsLoInt(1,
            new ConsLoInt(2,
                new ConsLoInt(3,
                    new ConsLoInt(3,
                        new ConsLoInt(5,
                            new MtLoInt()))))));
    
    return t.checkExpect(empty.mostCommonNumCont(1, 3, 2, 1 ), 1)
        && t.checkOneOf(fibo.mostCommonNumCont(0, 0, 0, 0), 1, 2, 3, 5, 8, 13)
        && t.checkExpect(jacobsthal.mostCommonNumCont(0, 0, 0, 0), 1)
        && t.checkExpect(test.mostCommonNumCont(2, 3, 1, 1), 2)
        && t.checkExpect(test.mostCommonNumCont(2, 2, 1, 2), 1);
  }
  
  boolean testCompare(Tester t) {
    SequenceCmp fibCmp = new FibCmp();
    SequenceCmp pellCmp = new PellCmp();
    SequenceCmp negaFibCmp = new NegaFibCmp();
    SequenceCmp jacobsthalCmp = new JacobsthalCmp();
    
    return t.checkExpect(fibCmp.compare(3, 5, 8), true)
        && t.checkExpect(fibCmp.compare(3, 5, 7), false)
        && t.checkExpect(pellCmp.compare(5, 12, 29), true)
        && t.checkExpect(pellCmp.compare(3, 5, 8), false)
        && t.checkExpect(negaFibCmp.compare(1, 0, 1), true)
        && t.checkExpect(negaFibCmp.compare(3, 5, 8), false)
        && t.checkExpect(jacobsthalCmp.compare(5, 11, 21), true)
        && t.checkExpect(jacobsthalCmp.compare(3, 5, 8), false);
        
  }
}