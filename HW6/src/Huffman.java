import tester.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

// Represents a Huffman tree constructed from a list of characters and their respective
// frequencies.
class Huffman {
  IHuffmanTree tree;

  Huffman(ArrayList<String> chars, ArrayList<Integer> frequencies) {
    // Validate that the length of the list of strings is greater than or equal to 2
    if (chars.size() < 2) {
      throw new IllegalArgumentException(
          "The length of `chars` must be at least 2"
      );
    }
    
    // Zip chars and frequencies into a single list (NOTE: this also performs the validation that
    // the list lengths are equal)
    ArrayList<IHuffmanTree> trees = new ListUtils().zip(
        chars, frequencies, new HuffmanTreeZipper());
    
    // Condense all the IHuffmanTrees in the list into a single IHuffmanTree
    this.tree = new HuffmanTreeUtils().condense(trees);
  }
  
  // Encodes the provided message as an ArrayList<Boolean> according to the structure of the
  // Huffman tree, where false represents a leftward movement down the tree (0) and true
  // represents a rightward movement (1)
  ArrayList<Boolean> encode(String message) {
    ArrayList<Boolean> code = new ArrayList<>();
    for (int i = 0; i < message.length(); i += 1) {
      String c = message.substring(i, i + 1);
      code.addAll(this.tree.encodeChar(c).expect(new IllegalArgumentException(
          "Tried to encode " + c + " but that is not part of the language.")
      ));
    }
    return code;
  }
  
  // Decodes the provided code to a String according to the structure of the Huffman tree, where
  // a false in the code represents a leftward movement down the tree (0) and a true represents
  // a rightward movement (1)
  public String decode(ArrayList<Boolean> code) {
    String message = "";
    IHuffmanTree t = this.tree;
    Iterator<Boolean> codeIter = code.iterator();
    
    while (codeIter.hasNext()) {
      message = message + this.tree.decodeNext(codeIter);
    }
    
    return message;
  }
}

// TREE STRUCTURE ---------------------------------------------------------------------------------

// Represents a Huffman tree in tree form.
interface IHuffmanTree {
  // Gets the total frequency of this IHuffmanTree, calculated as the sum of the frequencies of
  // all the leaves in the tree.
  int frequency();
  
  // Encodes a single character according to the structure of the Huffman tree, producing a
  // Some<ArrayList<Boolean>> with the corresponding code if the character exists in the tree and
  // a None<> if it does not.
  Maybe<ArrayList<Boolean>> encodeChar(String c);
  
  // Decodes a single character from the given Iterator of Booleans, or produces "?" if
  // the Iterator does not have enough elements left to decode a character.
  // EFFECT: moves the cursor of this codeIter the number of elements necessary to decode a
  // single character
  String decodeNext(Iterator<Boolean> codeIter);
}

// Represents a leaf in a Huffman tree with a character and a frequency
class HTLeaf implements IHuffmanTree {
  String c;
  int freq;
  
  HTLeaf(String c, int freq) {
    this.c = c;
    this.freq = freq;
  }
  
  // Gets the total frequency of this HTLeaf, calculated as the sum of the frequencies of all the
  // leaves in the tree.
  public int frequency() {
    return this.freq;
  }
  
  // Encodes a single character according to the structure of the Huffman tree, producing a
  // Some<ArrayList<Boolean>> with the corresponding code if the character exists in the tree and
  // a None<> if it does not.
  public Maybe<ArrayList<Boolean>> encodeChar(String c) {
    if (this.c.equals(c)) {
      return new Some<>(new ArrayList<>());
    } else {
      return new None<>();
    }
  }
  
  // Decodes a single character from the given Iterator of Booleans, or produces "?" if
  // the Iterator does not have enough elements left to decode a character.
  public String decodeNext(Iterator<Boolean> codeIter) {
    return this.c;
  }
}

// Represents a node in a Huffman tree with left and right branches
class HTNode implements IHuffmanTree {
  IHuffmanTree left;
  IHuffmanTree right;
  
  HTNode(IHuffmanTree left, IHuffmanTree right) {
    this.left = left;
    this.right = right;
  }
  
  // Gets the total frequency of this HTNode, calculated as the sum of the frequencies of all the
  // leaves in the tree.
  public int frequency() {
    return this.left.frequency() + this.right.frequency();
  }
  
  // Encodes a single character according to the structure of the Huffman tree, producing a
  // Some<ArrayList<Boolean>> with the corresponding code if the character exists in the tree and
  // a None<> if it does not.
  public Maybe<ArrayList<Boolean>> encodeChar(String c) {
    Maybe<ArrayList<Boolean>> left = this.left.encodeChar(c);
    Maybe<ArrayList<Boolean>> right = this.right.encodeChar(c);
    
    left.mapMut(new ArrPrepend<>(false));
    right.mapMut(new ArrPrepend<>(true));
    
    return left.orIfNone(right);
  }
  
  // Decodes a single character from the given Iterator of Booleans, or produces "?" if
  // the Iterator does not have enough elements left to decode a character.
  // EFFECT: moves the cursor of codeIter the number of elements necessary to decode a single
  // character
  public String decodeNext(Iterator<Boolean> codeIter) {
    if (codeIter.hasNext()) {
      boolean shouldGoRight = codeIter.next();
      if (shouldGoRight) {
        return this.right.decodeNext(codeIter);
      } else {
        return this.left.decodeNext(codeIter);
      }
    } else {
      return "?";
    }
  }
}

// OPTIONALS --------------------------------------------------------------------------------------

// Represents a value that may or may not exist
interface Maybe<T> {
  // Returns this Maybe if it contains a value, otherwise returns that
  Maybe<T> orIfNone(Maybe<T> that);
  
  // If this Maybe has contents, mutates those contents according to f; otherwise, does nothing.
  void mapMut(Consumer<T> f);
  
  // If this Maybe has contents, returns those contents; otherwise, throws ex.
  T expect(RuntimeException ex);
}

// Represents a value that exists
class Some<T> implements Maybe<T> {
  T t;
  
  Some(T t) {
    this.t = t;
  }
  
  // Returns this Maybe if it contains a value, otherwise returns that
  public Maybe<T> orIfNone(Maybe<T> that) {
    return this;
  }
  
  // If this Maybe has contents, mutates those contents according to f; otherwise, does nothing.
  public void mapMut(Consumer<T> f) {
    f.accept(this.t);
  }
  
  // If this Maybe has contents, returns those contents; otherwise, throws ex.
  public T expect(RuntimeException ex) {
    return this.t;
  }
}

// Represents a value that does not exist
class None<T> implements Maybe<T>  {
  // Returns this Maybe if it contains a value, otherwise returns that
  public Maybe<T> orIfNone(Maybe<T> that) {
    return that;
  }
  
  // If this Maybe has contents, mutates those contents according to f; otherwise, does nothing.
  public void mapMut(Consumer<T> f) {
    // This method does not do anything, since None has no value to mutate
  }
  
  // If this Maybe has contents, returns those contents; otherwise, throws ex.
  public T expect(RuntimeException ex) {
    throw ex;
  }
}

// FUNCTIONS --------------------------------------------------------------------------------------

// A Consumer that consumes a reference to an ArrayList and mutates it to add the T passed during
// construction to the start.
class ArrPrepend<T> implements Consumer<ArrayList<T>> {
  T t;
  
  ArrPrepend(T t) {
    this.t = t;
  }
  
  // A Consumer that consumes a reference to an ArrayList<T>, the T passed during construction to
  // the start.
  // EFFECT: adds the T passed during construction to the start of the provided ArrayList<T>.
  @Override
  public void accept(ArrayList<T> ts) {
    ts.add(0, t);
  }
}

// Comparator that compares two IHuffmanTrees based on the total frequency of each
class HuffmanTreeComparator implements Comparator<IHuffmanTree> {
  // Compares two IHuffmanTrees based on the total frequency of each, producing a negative number
  // if ht1 has a lower frequency, 0 if the frequencies are equal, and a positive number if ht1
  // has a greater frequency.
  @Override
  public int compare(IHuffmanTree ht1, IHuffmanTree ht2) {
    return ht1.frequency() - ht2.frequency();
  }
}

// Zipper that constructs an IHuffmanTree provided a String and an Integer. Intended for use on a
// list of Strings and a list of Integers via the zip() function on ListUtils.
class HuffmanTreeZipper implements BiFunction<String, Integer, IHuffmanTree> {
  // Constructs an IHuffmanTree provided a String and an Integer.
  @Override
  public HTLeaf apply(String c, Integer freq) {
    return new HTLeaf(c, freq);
  }
}

// UTILS ------------------------------------------------------------------------------------------

// Represents a suite of utilities for working with generic lists.
class ListUtils {
  // Sorts the provided ArrayList low-to-high via a stable insertion sort, provided a Comparator
  // cmp that compares any two elements of the ArrayList.
  <T> ArrayList<T> sorted(ArrayList<T> arr, Comparator<T> cmp) {
    ArrayList<T> sorted = new ArrayList<>();
    for (T t : arr) {
      this.insert(sorted, t, cmp);
    }
    return sorted;
  }
  
  // Provided a comparator cmp, inserts el into arr immediately before the first element
  // exceeding its value.
  // EFFECT: arr is mutated to contain el at an appropriate position, with all elements greater
  // than it shifted right one index.
  <T> void insert(ArrayList<T> arr, T el, Comparator<T> cmp) {
    for (int i = 0; i < arr.size(); i += 1) {
      if (cmp.compare(el, arr.get(i)) < 0) {
        arr.add(i, el);
        return;
      }
    }
    arr.add(el);
  }
  
  // Zips two ArrayLists of equal length into a single ArrayList of that same length. The
  // element at each index n in the produced ArrayList is the result of combining the nth element
  // of arr1 with the nth element of arr2 in an arbitrary way defined by cmbFunc.
  // If the lengths of the arrays are not equal, throws an IllegalArgumentException.
  <T, U, V> ArrayList<V> zip(ArrayList<T> arr1, ArrayList<U> arr2, BiFunction<T, U, V> cmbFunc) {
    // Validate that the lengths of the lists are equal
    int a1size = arr1.size();
    int a2size = arr2.size();
    if (a1size != a2size) {
      throw new IllegalArgumentException(
          "Expected two ArrayList of equal lengths, got " + a1size + " and " + a2size + " instead"
      );
    }
    
    ArrayList<V> zipped = new ArrayList<>();
    for (int i = 0; i < arr1.size(); i += 1) {
      zipped.add(cmbFunc.apply(arr1.get(i), arr2.get(i)));
    }
    return zipped;
  }
}

// Utilities for construction of a Huffman
class HuffmanTreeUtils {
  // Condenses a list of IHuffmanTrees into a single IHuffmanTree, combining the trees with the
  // lowest total frequencies bottom-up.
  IHuffmanTree condense(ArrayList<IHuffmanTree> trees) {
    ArrayList<IHuffmanTree> treesSorted = new ListUtils().sorted(
        trees, new HuffmanTreeComparator());
    
    int treesSize = treesSorted.size();
    // No matter the size of treesSorted, we will always need to make size-1 merges
    for (int i = 0; i < treesSize - 1; i += 1) {
      // The two elements we want to merge will always be the first two in the list
      IHuffmanTree first = treesSorted.remove(0);
      IHuffmanTree second = treesSorted.remove(0);
      // As specified in the problem, new nodes should be inserted as deep in the list as possible
      new ListUtils().insert(
          treesSorted,
          new HTNode(first, second),
          new HuffmanTreeComparator());
    }
    
    // At the end of the loop, there will only be one node containing all the leaves, so we can
    // access it at the first index.
    return treesSorted.get(0);
  }
}

// EXAMPLES ---------------------------------------------------------------------------------------

class ExamplesHuffman {
  /*
    f 3 t
     / \
    a   b
    1   2
  */
  Huffman hTwo = new Huffman(
      new ArrayList<>(List.of("a", "b")),
      new ArrayList<>(List.of(1, 2))
  );
  
  /*
    f 2 t
     / \
    a   b
    1   1
  */
  Huffman hTwoDup = new Huffman(
      new ArrayList<>(List.of("a", "b")),
      new ArrayList<>(List.of(1, 1))
  );
  
  /*
    f   6   t
       / \
      /   3
     /   / \
    c   a   b
    3   1   2
  */
  Huffman hThree = new Huffman(
      new ArrayList<>(List.of("a", "b", "c")),
      new ArrayList<>(List.of(1, 2, 3))
  );
  
  /*
  Connects c and d before connecting c with the a-b node, since the node is inserted as deep as
  possible in the list.
    f     9     t
         / \
        /   \
       /     \
      3       6
     / \     / \
    a   b   c   d
    1   2   3   3
  */
  Huffman hPairs = new Huffman(
      new ArrayList<>(List.of("a", "b", "c", "d")),
      new ArrayList<>(List.of(1, 2, 3, 3))
  );
  
  Huffman hPairsMixed = new Huffman(
      new ArrayList<>(List.of("b", "c", "a", "d")),
      new ArrayList<>(List.of(2, 3, 1, 3))
  );
  
  /*
  Connects c with the a-b node before connecting c with d, since the sum of c (3) and a-b (3)
  is less than the sum of d (4) with either of those two values.
    f     9     t
         / \
        /   6
       /   / \
      /   /   3
     /   /   / \
    d   c   a   b
    4   3   1   2
  */
  Huffman hOneByOne = new Huffman(
      new ArrayList<>(List.of("a", "b", "c", "d")),
      new ArrayList<>(List.of(1, 2, 3, 4))
  );
  
  // Methods on Huffman ---------------------------------------------------------------------------
  
  // When constructing a Huffman tree, the keys and frequencies should be sorted in the constructor
  void testConstructorSorting(Tester t) {
    t.checkExpect(hPairs, hPairsMixed);
  }
  
  // If the list of chars is less than length 2, we throw an exception
  void testConstructorExSufficientLength(Tester t) {
    t.checkConstructorException(
        new IllegalArgumentException(
            "The length of `chars` must be at least 2"),
        "Huffman",
        new ArrayList<>(List.of("a")),
        new ArrayList<>(List.of(1))
    );
    t.checkConstructorException(
        new IllegalArgumentException(
            "The length of `chars` must be at least 2"),
        "Huffman",
        new ArrayList<>(),
        new ArrayList<>()
    );
  }
  
  // If the lists don't have equal lengths, we throw an exception
  void testConstructorExEqualLengths(Tester t) {
    t.checkConstructorException(
        new IllegalArgumentException(
            "Expected two ArrayList of equal lengths, got 4 and 3 instead"),
        "Huffman",
        new ArrayList<>(List.of("a", "b", "c", "d")),
        new ArrayList<>(List.of(1, 2, 3))
    );
    t.checkConstructorException(
        new IllegalArgumentException(
            "Expected two ArrayList of equal lengths, got 3 and 4 instead"),
        "Huffman",
        new ArrayList<>(List.of("a", "b", "c")),
        new ArrayList<>(List.of(1, 2, 3, 4))
    );
  }
  
  
  // an unterminated sequence should result in a "?" appended at the end of the decoded string
  void testUnterminatedDecode(Tester t) {
    t.checkExpect(
        hPairs.decode(new ArrayList<>(List.of(false))),
        "?");
    t.checkExpect(
        hPairs.decode(new ArrayList<>(List.of(true, false, false))),
        "c?");
    t.checkExpect(
        hPairs.decode(new ArrayList<>(List.of(true, false, false, false, false, true, false))),
        "cab?");
  }

  void testOneCharEncode(Tester t) {
    t.checkExpect(
        hTwo.encode("b"),
        new ArrayList<>(List.of(true)));
    t.checkExpect(hTwoDup.encode("b"),
        new ArrayList<>(List.of(true)));
    t.checkExpect(hThree.encode("c"),
        new ArrayList<>(List.of(false)));
    t.checkExpect(hPairs.encode("c"),
        new ArrayList<>(List.of(true, false)));
    t.checkExpect(hOneByOne.encode("c"),
        new ArrayList<>(List.of(true, false)));
  }

  void testMultiCharEncode(Tester t) {
    t.checkExpect(hThree.encode("cab"),
        new ArrayList<>(List.of(false, true, false, true, true)));
    t.checkExpect(hPairs.encode("cab"),
        new ArrayList<>(List.of(true, false, false, false, false, true)));
    t.checkExpect(hOneByOne.encode("cab"),
        new ArrayList<>(List.of(true, false, true, true, false, true, true, true)));
  }
  
  void testEncodeEx(Tester t) {
    t.checkException(
        new IllegalArgumentException("Tried to encode e but that is not part of the language."),
        hTwo,
        "encode",
        "e"
    );
  }
  
  // We should be able to maintain a result by decoding then encoding, or encoding then decoding
  // (unless the result ends in a ? or an error is thrown)
  void testConsistentEncodeDecode(Tester t) {
    t.checkExpect(
        hPairs.decode(hPairs.encode("abacd")),
        "abacd"
    );
    t.checkExpect(
        hPairs.encode(
            hPairs.decode(new ArrayList<>(List.of(true, false, false, false, false, true)))),
        new ArrayList<>(List.of(true, false, false, false, false, true))
    );
  }
  
  // Methods on IHuffmanTree ----------------------------------------------------------------------
  
  void testFrequency(Tester t) {
    IHuffmanTree leaf1 = new HTLeaf("a", 3);
    IHuffmanTree leaf2 = new HTLeaf("b", 2);
    IHuffmanTree node1 = new HTNode(leaf1, leaf2);
    t.checkExpect(leaf1.frequency(), 3);
    t.checkExpect(leaf2.frequency(), 2);
    t.checkExpect(node1.frequency(), 5);
  }
  
  void testEncodeChar(Tester t) {
    t.checkExpect(hTwo.tree.encodeChar("a"),
        new Some<>(new ArrayList<>(List.of(false))));
    t.checkExpect(hPairs.tree.encodeChar("c"),
        new Some<>(new ArrayList<>(List.of(true, false))));
    t.checkExpect(hPairs.tree.encodeChar("s"),
        new None<>());
    t.checkExpect(new HTLeaf("a", 2).encodeChar("a"),
        new Some<>(new ArrayList<>()));
    t.checkExpect(new HTLeaf("a", 2).encodeChar("b"),
        new None<>());
  }
  
  void testDecodeNext(Tester t) {
    ArrayList<Boolean> code1 = new ArrayList<>(List.of(false, true, true));
    ArrayList<Boolean> code2 = new ArrayList<>(List.of(true));
    
    // Iterator extends beyond the end of the tree, and one element is used
    Iterator<Boolean> iter1 = code1.iterator();
    t.checkExpect(hTwo.tree.decodeNext(iter1), "a");
    // Check that the iterator is properly mutated
    Iterator<Boolean> iter1Check = code1.iterator();
    iter1Check.next();
    t.checkExpect(iter1, iter1Check);
    
    // Iterator extends beyond the end of this tree, and two elements are used
    Iterator<Boolean> iter2 = code1.iterator();
    t.checkExpect(hPairs.tree.decodeNext(iter2), "b");
    // Check that the iterator is properly mutated
    Iterator<Boolean> iter2Check = code1.iterator();
    iter2Check.next();
    iter2Check.next();
    t.checkExpect(iter2, iter2Check);
    
    // Iterator ends at the end of the tree
    Iterator<Boolean> iter3 = code2.iterator();
    t.checkExpect(hTwo.tree.decodeNext(iter3), "b");
    // Check that the iterator is properly mutated
    t.checkExpect(iter3.hasNext(), false);
    
    // Iterator ends before the end of the tree
    Iterator<Boolean> iter4 = code2.iterator();
    t.checkExpect(hPairs.tree.decodeNext(iter4), "?");
    // Check that the iterator is properly mutated
    t.checkExpect(iter4.hasNext(), false);
    
    // Tree is just a leaf
    Iterator<Boolean> iter5 = code2.iterator();
    t.checkExpect(new HTLeaf("a", 2).decodeNext(iter5), "a");
    // Check that the iterator is properly mutated
    t.checkExpect(iter5, code2.iterator());
  }
  
  // Methods on Maybe -----------------------------------------------------------------------------
  
  void testOr(Tester t) {
    t.checkExpect(new Some<>(1).orIfNone(new Some<>(2)), new Some<>(1));
    t.checkExpect(new Some<>(1).orIfNone(new None<>()), new Some<>(1));
    t.checkExpect(new None<>().orIfNone(new Some<>(2)), new Some<>(2));
    t.checkExpect(new None<>().orIfNone(new None<>()), new None<>());
  }
  
  void testMapMut(Tester t) {
    ArrayList<Integer> ints = new ArrayList<>(List.of(1, 2));
    Maybe<ArrayList<Integer>> someInts = new Some<>(ints);
    Maybe<ArrayList<Integer>> noInts = new None<>();
    
    // A Some should be mutated according to the consumer
    someInts.mapMut(new ArrPrepend<>(0));
    t.checkExpect(someInts, new Some<>(new ArrayList<>(List.of(0, 1, 2))));
    
    // A None should not be changed
    noInts.mapMut(new ArrPrepend<>(0));
    t.checkExpect(noInts, new None<>());
  }
  
  void testExpect(Tester t) {
    t.checkExpect(new Some<>(1).expect(new RuntimeException("oops")), 1);
  }
  
  void testExpectEx(Tester t) {
    t.checkException(
        new RuntimeException("oops"),
        new None<>(),
        "expect",
        new RuntimeException("oops"));
  }
  
  // Function objects -----------------------------------------------------------------------------
  
  void testArrPrepend(Tester t) {
    ArrayList<Integer> l1 = new ArrayList<>(List.of(1, 2));
    new ArrPrepend<>(0).accept(l1);
    t.checkExpect(l1, new ArrayList<>(List.of(0, 1, 2)));
    
    ArrayList<String> l2 = new ArrayList<>(List.of("a", "b"));
    new ArrPrepend<>("z").accept(l2);
    t.checkExpect(l2, new ArrayList<>(List.of("z", "a", "b")));
  }
  
  void testHuffmanTreeComparator(Tester t) {
    IHuffmanTree lFreq1 = new HTLeaf("a", 1);
    IHuffmanTree lFreq2 = new HTLeaf("b", 2);
    IHuffmanTree lFreq3 = new HTLeaf("c", 3);
    IHuffmanTree nFreq3 = new HTNode(lFreq2, lFreq1);
    IHuffmanTree nFreq5 = new HTNode(lFreq2, lFreq3);
    
    t.checkExpect(new HuffmanTreeComparator().compare(lFreq1, lFreq1), 0);
    t.checkExpect(new HuffmanTreeComparator().compare(lFreq1, lFreq2), -1);
    t.checkExpect(new HuffmanTreeComparator().compare(lFreq2, lFreq1), 1);
    t.checkExpect(new HuffmanTreeComparator().compare(lFreq1, lFreq3), -2);
    
    t.checkExpect(new HuffmanTreeComparator().compare(lFreq1, nFreq3), -2);
    t.checkExpect(new HuffmanTreeComparator().compare(nFreq3, lFreq1), 2);
    t.checkExpect(new HuffmanTreeComparator().compare(lFreq3, nFreq3), 0);
    
    t.checkExpect(new HuffmanTreeComparator().compare(nFreq3, nFreq5), -2);
    t.checkExpect(new HuffmanTreeComparator().compare(nFreq5, nFreq3), 2);
  }
  
  void testHuffmanTreeZipper(Tester t) {
    t.checkExpect(new HuffmanTreeZipper().apply("a", 2), new HTLeaf("a", 2));
    t.checkExpect(new HuffmanTreeZipper().apply("b", 5), new HTLeaf("b", 5));
  }
  
  // Functions on ListUtils -----------------------------------------------------------------------
  
  void testSorted(Tester t) {
    IHuffmanTree lFreq1 = new HTLeaf("a", 1);
    IHuffmanTree lFreq2b = new HTLeaf("b", 2);
    IHuffmanTree lFreq2c = new HTLeaf("c", 2);
    IHuffmanTree lFreq3 = new HTLeaf("d", 3);
    
    ArrayList<IHuffmanTree> mt = new ArrayList<>();
    ArrayList<IHuffmanTree> trees = new ArrayList<>(List.of(lFreq3, lFreq1, lFreq2b));
    ArrayList<IHuffmanTree> treesDup = new ArrayList<>(List.of(lFreq3, lFreq1, lFreq2c, lFreq2b));
    
    t.checkExpect(new ListUtils().sorted(mt, new HuffmanTreeComparator()), mt);
    t.checkExpect(new ListUtils().sorted(trees, new HuffmanTreeComparator()),
        new ArrayList<>(List.of(lFreq1, lFreq2b, lFreq3)));
    t.checkExpect(new ListUtils().sorted(treesDup, new HuffmanTreeComparator()),
        new ArrayList<>(List.of(lFreq1, lFreq2c, lFreq2b, lFreq3)));
  }
  
  void testInsert(Tester t) {
    IHuffmanTree lFreq1 = new HTLeaf("a", 1);
    IHuffmanTree lFreq2b = new HTLeaf("b", 2);
    IHuffmanTree lFreq2c = new HTLeaf("c", 2);
    IHuffmanTree lFreq3 = new HTLeaf("d", 3);
    
    ArrayList<IHuffmanTree> mt = new ArrayList<>();
    ArrayList<IHuffmanTree> trees1 = new ArrayList<>(List.of(lFreq1, lFreq3));
    ArrayList<IHuffmanTree> trees2 = new ArrayList<>(List.of(lFreq1, lFreq2b, lFreq3));
    
    new ListUtils().insert(mt, lFreq2c, new HuffmanTreeComparator());
    t.checkExpect(mt, new ArrayList<>(List.of(lFreq2c)));
    
    new ListUtils().insert(trees1, lFreq2c, new HuffmanTreeComparator());
    t.checkExpect(trees1, new ArrayList<>(List.of(lFreq1, lFreq2c, lFreq3)));
    
    new ListUtils().insert(trees2, lFreq2c, new HuffmanTreeComparator());
    t.checkExpect(trees2, new ArrayList<>(List.of(lFreq1, lFreq2b, lFreq2c, lFreq3)));
  }
  
  void testZip(Tester t) {
    t.checkExpect(new ListUtils().zip(
        new ArrayList<String>(),
        new ArrayList<Integer>(),
        new HuffmanTreeZipper()
    ), new ArrayList<IHuffmanTree>());
    
    t.checkExpect(new ListUtils().zip(
        new ArrayList<String>(List.of("a", "b", "c")),
        new ArrayList<Integer>(List.of(1, 3, 2)),
        new HuffmanTreeZipper()
    ), new ArrayList<IHuffmanTree>(List.of(
        new HTLeaf("a", 1),
        new HTLeaf("b", 3),
        new HTLeaf("c", 2)
    )));
  }
  
  // Functions on HuffmanTreeUtils ----------------------------------------------------------------
  
  void testCondense(Tester t) {
    IHuffmanTree lFreq1 = new HTLeaf("a", 1);
    IHuffmanTree lFreq2b = new HTLeaf("b", 2);
    IHuffmanTree lFreq2c = new HTLeaf("c", 2);
    IHuffmanTree lFreq3 = new HTLeaf("d", 3);
    
    IHuffmanTree nFreq3 = new HTNode(lFreq1, lFreq2b);
    IHuffmanTree nFreq4 = new HTNode(lFreq2b, lFreq2c);
    IHuffmanTree nFreq5 = new HTNode(lFreq2c, nFreq3);
    IHuffmanTree nFreq6 = new HTNode(lFreq3, nFreq3);
    
    // Test behavior with only one leaf
    t.checkExpect(new HuffmanTreeUtils().condense(
        new ArrayList<>(List.of(lFreq1))
    ), lFreq1);
    
    // Test behavior of merging two leaves
    t.checkExpect(new HuffmanTreeUtils().condense(
        new ArrayList<>(List.of(lFreq1, lFreq2b))
    ), nFreq3);
    
    // Test behavior of merging two leaves of matching frequency (they should be kept in order)
    t.checkExpect(new HuffmanTreeUtils().condense(
        new ArrayList<>(List.of(lFreq2b, lFreq2c))
    ), nFreq4);
    
    // Test behavior of inserting a node and then another node containing it (the leaf should
    // come first since the node has a higher value for frequency)
    t.checkExpect(new HuffmanTreeUtils().condense(
        new ArrayList<>(List.of(lFreq1, lFreq2b, lFreq2c))
    ), nFreq5);
    
    // Test behavior of merging a node and a leaf of matching frequency (the leaf should come first
    // since the node is inserted after it)
    t.checkExpect(new HuffmanTreeUtils().condense(
        new ArrayList<>(List.of(lFreq1, lFreq2b, lFreq3))
    ), nFreq6);
  }
}