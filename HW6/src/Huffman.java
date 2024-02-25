import tester.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

interface Maybe<T> {
  // Returns this Maybe if it contains a value, otherwise returns that
  Maybe<T> or(Maybe<T> that);
  
  // If this Maybe has contents, mutates those contents according to f; otherwise, does nothing.
  void mapMut(Consumer<T> f);
  
  // If this Maybe has contents, returns those contents; otherwise, throws ex.
  T expect(RuntimeException ex);
}

class Some<T> implements Maybe<T> {
  T t;
  
  Some(T t) {
    this.t = t;
  }
  
  // Returns this Maybe if it contains a value, otherwise returns that
  public Maybe<T> or(Maybe<T> that) {
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

class None<T> implements Maybe<T>  {
  
  // Returns this Maybe if it contains a value, otherwise returns that
  public Maybe<T> or(Maybe<T> that) {
    return that;
  }
  
  // If this Maybe has contents, mutates those contents according to f; otherwise, does nothing.
  public void mapMut(Consumer<T> f) { }
  
  // If this Maybe has contents, returns those contents; otherwise, throws ex.
  public T expect(RuntimeException ex) {
    throw ex;
  }
}

// Represents a Huffman tree constructed from a list of characters and their respective
// frequencies.
class Huffman {
  IHuffmanTree tree;

  Huffman(ArrayList<String> chars, ArrayList<Integer> frequencies) {
    
    // Validate that the lengths of the lists are equal
    int charSize = chars.size();
    int freqSize = frequencies.size();
    if (charSize != freqSize) {
      throw new IllegalArgumentException(
          "Expected two ArrayList of equal lengths, got "
          + charSize + " and " + freqSize + " instead"
      );
    }
    
    // Validate that the length of the list of strings is greater than or equal to 2
    if (charSize < 2) {
      throw new IllegalArgumentException(
          "The length of `chars` must be at least 2"
      );
    }
    
    // Zip chars and frequencies into a single list
    ArrayList<IHuffmanTree> trees = new ListUtils().zip(chars, frequencies, new HuffmanTreeZipper());
    
    // Condense all the IHuffmanTrees in the list into a single IHuffmanTree
    this.tree = new HuffmanTreeUtils().condense(trees);
  }
  
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

  public String decode(ArrayList<Boolean> code) {
    String message = "";
    while (!code.isEmpty()) {
      message = message + this.tree.decodeNext(code);
    }
    return message;
  }
}

// Represents a Huffman tree in tree form.
interface IHuffmanTree {
  // Gets the total frequency of this IHuffmanTree, calculated as the sum of the frequencies of
  // all the leaves in the tree.
  int frequency();
  
  Maybe<ArrayList<Boolean>> encodeChar(String c);
  
  // Decodes a single character from the given stream of Booleans `code`
  // EFFECT: removes the corresponding booleans within ArrayList<Boolean>
  String decodeNext(ArrayList<Boolean> code);
}

// Represents a leaf in a Huffman tree with a character and a frequency
class Leaf implements IHuffmanTree {
  String c;
  int freq;
  
  Leaf(String c, int freq) {
    this.c = c;
    this.freq = freq;
  }
  
  // Gets the total frequency of this Leaf, calculated as the sum of the frequencies of all the
  // leaves in the tree.
  public int frequency() {
    return this.freq;
  }
  
  public Maybe<ArrayList<Boolean>> encodeChar(String c) {
    if (this.c.equals(c)) {
      return new Some<>(new ArrayList<>());
    } else {
      return new None<>();
    }
  }
  
  public String decodeNext(ArrayList<Boolean> code) {
    return this.c;
  }
}

// Represents a node in a Huffman tree with left and right branches
class Node implements IHuffmanTree {
  IHuffmanTree left;
  IHuffmanTree right;
  
  Node(IHuffmanTree left, IHuffmanTree right) {
    this.left = left;
    this.right = right;
  }
  
  // Gets the total frequency of this Node, calculated as the sum of the frequencies of all the
  // leaves in the tree.
  public int frequency() {
    return this.left.frequency() + this.right.frequency();
  }
  
  public Maybe<ArrayList<Boolean>> encodeChar(String c) {
    Maybe<ArrayList<Boolean>> left = this.left.encodeChar(c);
    left.mapMut(new ArrayListPrepend<>(false));
    Maybe<ArrayList<Boolean>> right = this.right.encodeChar(c);
    right.mapMut(new ArrayListPrepend<>(true));
    
    return left.or(right);
  }
  
  public String decodeNext(ArrayList<Boolean> code) {
    if (code.isEmpty()) {
      return "?";
    }
    
    boolean codeAt = code.remove(0);
    if (codeAt) {
      return this.right.decodeNext(code);
    } else {
      return this.left.decodeNext(code);
    }
  }
  
}

// A Consumer that consumes a reference to an ArrayList and mutates it to add the T passed during
// construction to the start.
class ArrayListPrepend<T> implements Consumer<ArrayList<T>> {
  T t;
  
  ArrayListPrepend(T t) {
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
  public Leaf apply(String c, Integer freq) {
    return new Leaf(c, freq);
  }
}

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
  <T, U, V> ArrayList<V> zip(ArrayList<T> arr1, ArrayList<U> arr2, BiFunction<T, U, V> cmbFunc) {
    // TODO: do length validation here?
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
    ArrayList<IHuffmanTree> treesSorted = new ListUtils().sorted(trees, new HuffmanTreeComparator());
    
    int treesSize = treesSorted.size();
    // No matter the size of treesSorted, we will always need to make size-1 merges
    for (int i = 0; i < treesSize - 1; i += 1) {
      // The two elements we want to merge will always be the first two in the list
      IHuffmanTree first = treesSorted.remove(0);
      IHuffmanTree second = treesSorted.remove(0);
      // As specified in the problem, new nodes should be inserted as deep in the list as possible
      new ListUtils().insert(
          treesSorted,
          new Node(first, second),
          new HuffmanTreeComparator());
    }
    
    // At the end of the loop, there will only be one node containing all the leaves, so we can
    // access it at the first index.
    return treesSorted.get(0);
  }
}

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
  
  // When constructing a Huffman tree, the keys and frequencies should be sorted in the constructor
  void testConstructorSorting(Tester t) {
    t.checkExpect(hPairs, hPairsMixed);
  }
  
  // an unterminated sequence should result in an "/"
  // appended at the end of the decoded string
  void testUnterminatedDecode(Tester t) {
    t.checkExpect(
        hPairs.decode(new ArrayList<>(List.of(false))),
        "?");
  }
  
  void testConsistentEncodeDecode(Tester t) {
    t.checkExpect(
        hPairs.decode(hPairs.encode("abacd")),
        "abacd"
    );
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
  
  void testHuffmanTreeFrequency(Tester t) {
    IHuffmanTree leaf1 = new Leaf("a", 3);
    IHuffmanTree leaf2 = new Leaf("b", 2);
    IHuffmanTree node1 = new Node(leaf1, leaf2);
    t.checkExpect(leaf1.frequency(), 3);
    t.checkExpect(leaf2.frequency(), 2);
    t.checkExpect(node1.frequency(), 5);
  }
}