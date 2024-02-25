import huffman.*;
import tester.Tester;

import java.util.ArrayList;
import java.util.List;

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
//  void testConstructorSorting(Tester t) {
//    t.checkExpect(hPairs, hPairsMixed);
//  }
  
  // an unterminated sequence should result in an "/"
  // appended at the end of the decoded string
  void testUnterminatedDecode(Tester t) {
    t.checkExpect(
        hPairs.decode(new ArrayList<>(List.of(false))),
        "?");
  }
  
  void testOneCharEncode1(Tester t) {
    t.checkExpect(
        hTwo.encode("b"),
        new ArrayList<>(List.of(true)));
  }
  
  void testOneCharEncode2(Tester t) {
    t.checkExpect(hTwoDup.encode("b"),
        new ArrayList<>(List.of(true)));
  }
  
  void testOneCharEncode3(Tester t) {
    t.checkExpect(hThree.encode("c"),
        new ArrayList<>(List.of(false)));
  }
  
//  void testOneCharEncode4(Tester t) {
//    t.checkExpect(hPairs.encode("c"),
//        new ArrayList<>(List.of(true, false)));
//  }
//
//  void testOneCharEncode5(Tester t) {
//    t.checkExpect(hOneByOne.encode("c"),
//        new ArrayList<>(List.of(true, false)));
//  }
  
//  void testMultiCharEncode(Tester t) {
//    t.checkExpect(hThree.encode("cab"),
//        new ArrayList<>(List.of(false, true, false, true, true)));
//    t.checkExpect(hPairs.encode("cab"),
//        new ArrayList<>(List.of(true, false, false, false, false, true)));
//    t.checkExpect(hOneByOne.encode("cab"),
//        new ArrayList<>(List.of(true, false, true, true, false, true, true, true)));
//  }
}
