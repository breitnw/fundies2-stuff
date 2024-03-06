import tester.Tester;

import java.util.function.Predicate;

// Represents a generic list that can be traversed in either direction, from the front or from
// the back.
class Deque<T> {
  Sentinel<T> header;
  
  /*
    TEMPLATE for Deque
    Fields:
    ... this.header ... -- Sentinel<T>
    Methods:
    ... this.size() ... -- int
    ... this.addAtHead(T) ... -- void
    ... this.addAtTail(T) ... -- void
    ... this.removeFromHead() ... -- T
    ... this.removeFromTail() ... -- T
    ... this.find(Predicate<T>) ... -- ANode<T>
    ... this.removeNode(ANode<T>) ... -- void
    Methods on fields:
    ... this.header.replacePrev(ANode<T>) ...        -- void
    ... this.header.replaceNext(ANode<T>) ...        -- void
    ... this.header.updateNext(ANode<T>) ...         -- void
    ... this.header.sizeToSentinel() ...             -- int
    ... this.header.selfDelete() ...                 -- void
    ... this.header.selfRemove() ...                 -- T
    ... this.header.findToSentinel(Predicate<T>) ... -- T
    ... this.header.addAtHead(T) ...                 -- void
    ... this.header.addAtTail(T) ...                 -- void
    ... this.header.removeFromHead() ...             -- T
    ... this.header.removeFromTail() ...             -- T
    ... this.header.find(Predicate<T>) ...           -- ANode<T>
   */
  
  Deque() {
    this(new Sentinel<>());
  }
  
  Deque(Sentinel<T> s) {
    this.header = s;
  }
  
  // Counts the number of nodes in this Deque, not including the header node.
  int size() {
    /*
    TEMPLATE: Same as class template
     */
    return this.header.size();
  }
  
  // Consumes a value of type T and inserts it at the front of the list.
  // EFFECT: updates the references of the header and its next element to refer to a new
  // element between them.
  void addAtHead(T that) {
    /*
    TEMPLATE:
    Parameters:
    ... that ...  -- T
     */
    this.header.addAtHead(that);
  }
  
  // Consumes a value of type T and inserts it at the tail of the list.
  // EFFECT: updates the references of the header and its previous element to refer to a new
  // element between them.
  void addAtTail(T that) {
    /*
    TEMPLATE:
    Parameters:
    ... that ...  -- T
     */
    this.header.addAtTail(that);
  }
  
  // Removes the first node from this Deque and returns its data, throwing an exception if this
  // Deque is empty
  // EFFECT: The first node is removed from this Deque and the references are corrected
  // accordingly.
  T removeFromHead() {
    /*
    TEMPLATE: Same as class template
     */
    return this.header.removeFromHead();
  }
  
  // Removes the last node from this Deque and returns its data, throwing an exception if this
  // Deque is empty
  // EFFECT: The last node is removed from this Deque and the references are corrected accordingly.
  T removeFromTail() {
    /*
    TEMPLATE: Same as class template
     */
    return this.header.removeFromTail();
  }
  
  // Produces the first node in this Deque for which the given predicate returns true. If the
  // predicate never returns true for any value in this Deque, returns the header node in this
  // Deque.
  ANode<T> find(Predicate<T> pred) {
    /*
    TEMPLATE
    Parameters:
    ... pred ...         -- Predicate<T>
    Methods on parameters:
    ... pred.test(T) ... -- boolean
     */
    return this.header.find(pred);
  }
  
  // Removes the provided node from the Deque. If the provided node is a Sentinel, does nothing.
  // NOTE: Does not need to return anything, since the user already must already have access to
  // the node in question in order to pass it to the method.
  void removeNode(ANode<T> node) {
    /*
    TEMPLATE
    Parameters:
    ... node ...               -- ANode<T>
    Methods on parameters:
    ... node.selfDelete() ...  -- void
     */
    node.selfDelete();
  }
}

// Represents a node or sentinel in a generic list that can be traversed in either direction, from
// the front or from the back.
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;
  
  /*
    TEMPLATE for ANode
    Fields:
    ... this.next ...                         -- ANode<T>
    ... this.prev ...                         -- ANode<T>
    Methods:
    ... this.replacePrev(ANode<T>) ...        -- void
    ... this.replaceNext(ANode<T>) ...        -- void
    ... this.updateNext(ANode<T>) ...         -- void
    ... this.sizeToSentinel() ...             -- int
    ... this.selfDelete() ...                 -- void
    ... this.selfRemove() ...                 -- T
    ... this.findToSentinel(Predicate<T>) ... -- T
    Methods on fields:
    ... this.next.replacePrev(ANode<T>) ...        -- void
    ... this.next.replaceNext(ANode<T>) ...        -- void
    ... this.next.updateNext(ANode<T>) ...         -- void
    ... this.next.sizeToSentinel() ...             -- int
    ... this.next.selfDelete() ...                 -- void
    ... this.next.selfRemove() ...                 -- T
    ... this.next.findToSentinel(Predicate<T>) ... -- T
    
    ... this.prev.replacePrev(ANode<T>) ...        -- void
    ... this.prev.replaceNext(ANode<T>) ...        -- void
    ... this.prev.updateNext(ANode<T>) ...         -- void
    ... this.prev.sizeToSentinel() ...             -- int
    ... this.prev.selfDelete() ...                 -- void
    ... this.prev.selfRemove() ...                 -- T
    ... this.prev.findToSentinel(Predicate<T>) ... -- T
    
   */
  
  // Replaces the previous element of this ANode with that, and updates the next element of that
  // ANode accordingly.
  // EFFECT: updates the previous element of this and the next element of that according to the
  // purpose statement.
  void replacePrev(ANode<T> that) {
    /*
    TEMPLATE
    Parameters:
    ... that ...                       -- ANode<T>
    Methods on Params:
    ... that.updateNext(ANode<T>) ...  -- void
     */
    this.prev = that;
    that.updateNext(this);
  }
  
  // Replaces the next element of this ANode with that, and updates the previous element of that
  // ANode accordingly.
  // EFFECT: updates the next element of this and the previous element of that according to the
  // purpose statement.
  void replaceNext(ANode<T> that) {
    /*
    TEMPLATE
    Parameters:
    ... that ...                        -- ANode<T>
    Methods on Params:
    ... that.replacePrev(ANode<T>) ...  -- void
     */
    that.replacePrev(this);
  }
  
  // Updates the next element of this node to that, throwing an exception if the node preceding
  // that node is not this.
  // EFFECT: updates the next element of this according to the purpose statement.
  void updateNext(ANode<T> that) {
    /*
    TEMPLATE
    Parameters:
    ... that ...       -- ANode<T>
    Fields on parameters:
    ... that.prev ...  -- ANode<T>
     */
    if (that.prev != this) {
      throw new IllegalStateException("This node is not the previous before the provided node");
    }
    this.next = that;
  }
  
  // Gets the total number of nodes (including this one, if it is a node) until the next sentinel
  // in the list.
  abstract int sizeToSentinel();
  
  // Removes this node from its list by modifying its previous and next elements to refer to each
  // other. If this is a Sentinel, does nothing.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  abstract void selfDelete();
  
  // Removes this from its list by modifying its previous and next elements to refer to each
  // other, returning the data of this. If this is a Sentinel, throws an exception.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  abstract T selfRemove();
  
  // Finds the first occurrence of a Node that satisfies pred before the next Sentinel. If no
  // such node is found, returns the next Sentinel.
  abstract ANode<T> findToSentinel(Predicate<T> pred);
}

// Represents a sentinel in a generic list that can be traversed in either direction, from
// the front or from the back. Serves as a starting point and guard for access and mutation
// methods.
class Sentinel<T> extends ANode<T> {
  Sentinel() {
    this.next = this;
    this.prev = this;
  }
  
  /*
    TEMPLATE for Sentinel
    Fields:
    ... this.next ...                         -- ANode<T>
    ... this.prev ...                         -- ANode<T>
    Methods:
    ... this.replacePrev(ANode<T>) ...        -- void
    ... this.replaceNext(ANode<T>) ...        -- void
    ... this.updateNext(ANode<T>) ...         -- void
    ... this.sizeToSentinel() ...             -- int
    ... this.selfDelete() ...                 -- void
    ... this.selfRemove() ...                 -- T
    ... this.findToSentinel(Predicate<T>) ... -- T
    ... this.addAtHead(T) ...                 -- void
    ... this.addAtTail(T) ...                 -- void
    ... this.removeFromHead() ...             -- T
    ... this.removeFromTail() ...             -- T
    ... this.find(Predicate<T>) ...           -- ANode<T>
    Methods on fields:
    
    ... this.next.replacePrev(ANode<T>) ...        -- void
    ... this.next.replaceNext(ANode<T>) ...        -- void
    ... this.next.updateNext(ANode<T>) ...         -- void
    ... this.next.sizeToSentinel() ...             -- int
    ... this.next.selfDelete() ...                 -- void
    ... this.next.selfRemove() ...                 -- T
    ... this.next.findToSentinel(Predicate<T>) ... -- T
    
    ... this.prev.replacePrev(ANode<T>) ...        -- void
    ... this.prev.replaceNext(ANode<T>) ...        -- void
    ... this.prev.updateNext(ANode<T>) ...         -- void
    ... this.prev.sizeToSentinel() ...             -- int
    ... this.prev.selfDelete() ...                 -- void
    ... this.prev.selfRemove() ...                 -- T
    ... this.prev.findToSentinel(Predicate<T>) ... -- T
   */
  
  // Counts the number of nodes following this Sentinel before arriving back at the Sentinel.
  int size() {
    /*
    TEMPLATE: Same as class template
     */
    return this.next.sizeToSentinel();
  }
  
  // Gets the total number of nodes (including this one, if it is a node) until the next sentinel
  // in the list.
  @Override
  int sizeToSentinel() {
    /*
    TEMPLATE: Same as class template
     */
    return 0;
  }
  
  // Adds an element to the start of the list.
  // EFFECT: modifies the previous and next elements in this list to refer to
  // this element instead of referring to each other
  void addAtHead(T data) {
    /*
    TEMPLATE
    Parameters:
    ... data ... -- T
     */
    new Node<>(data, this.next, this);
  }
  
  // Adds an element to the end of the list.
  // EFFECT: modifies the previous and next elements in this list to refer to
  // this element instead of referring to each other
  void addAtTail(T data) {
    /*
    TEMPLATE
    Parameters:
    ... data ... -- T
     */
    new Node<>(data, this, this.prev);
  }
  
  // Removes the node at the start of the list, returning the contents of the corresponding
  // element.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  T removeFromHead() {
    /*
    TEMPLATE: Same as class template
     */
    return this.next.selfRemove();
  }
  
  // Removes the node at the end of the list, returning the contents of the corresponding
  // element.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  T removeFromTail() {
    /*
    TEMPLATE: Same as class template
     */
    return this.prev.selfRemove();
  }
  
  // Deletes this from its list by modifying its previous and next elements to refer to each
  // other. If this is a Sentinel, does nothing.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  @Override
  void selfDelete() {
    /*
    TEMPLATE: Same as class template
     */
    
    // The body for this method is empty, since we do not want to mutate the list if the element
    // to be deleted is a sentinel.
  }
  
  // Removes this from its list by modifying its previous and next elements to refer to each
  // other, returning the data of this. If this is a Sentinel, throws an exception.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  @Override
  T selfRemove() {
    /*
    TEMPLATE: Same as class template
     */
    throw new RuntimeException("Attempted to remove a sentinel");
  }
  
  // Produces the first node after this Sentinel for which the given predicate returns true. If the
  // predicate never returns true for any value after this Sentinel, returns the header node.
  ANode<T> find(Predicate<T> pred) {
    /*
    TEMPLATE
    Parameters:
    ... pred ... -- Predicate<T>
     */
    return this.next.findToSentinel(pred);
  }
  
  // Finds the first occurrence of a Node that satisfies pred before the next Sentinel. If no
  // such node is found, returns the next Sentinel.
  @Override
  ANode<T> findToSentinel(Predicate<T> pred) {
    /*
    TEMPLATE
    Parameters:
    ... pred ... -- Predicate<T>
     */
    return this;
  }
}

// Represents a node in a generic list that can be traversed in either direction, from the front
// or from the back.
class Node<T> extends ANode<T> {
  T data;
  
  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }
  
  /*
    TEMPLATE for Node
    Fields:
    ... this.next ...                         -- ANode<T>
    ... this.prev ...                         -- ANode<T>
    Methods:
    ... this.replacePrev(ANode<T>) ...        -- void
    ... this.replaceNext(ANode<T>) ...        -- void
    ... this.updateNext(ANode<T>) ...         -- void
    ... this.sizeToSentinel() ...             -- int
    ... this.selfDelete() ...                 -- void
    ... this.selfRemove() ...                 -- T
    ... this.findToSentinel(Predicate<T>) ... -- T
    Methods on fields:
    ... this.next.replacePrev(ANode<T>) ...        -- void
    ... this.next.replaceNext(ANode<T>) ...        -- void
    ... this.next.updateNext(ANode<T>) ...         -- void
    ... this.next.sizeToSentinel() ...             -- int
    ... this.next.selfDelete() ...                 -- void
    ... this.next.selfRemove() ...                 -- T
    ... this.next.findToSentinel(Predicate<T>) ... -- T
    
    ... this.prev.replacePrev(ANode<T>) ...        -- void
    ... this.prev.replaceNext(ANode<T>) ...        -- void
    ... this.prev.updateNext(ANode<T>) ...         -- void
    ... this.prev.sizeToSentinel() ...             -- int
    ... this.prev.selfDelete() ...                 -- void
    ... this.prev.selfRemove() ...                 -- T
    ... this.prev.findToSentinel(Predicate<T>) ... -- T
   */
  
  Node(T data, ANode<T> next, ANode<T> prev) {
    this(data);
    
    this.replaceNext(next);
    this.replacePrev(prev);
    
    if (this.next == null || this.prev == null) {
      throw new IllegalArgumentException(
          "One or both of the ANodes provided to the Node constructor is null");
    }
  }
  
  // Gets the total number of nodes (including this one, if it is a node) until the next sentinel
  // in the list.
  @Override
  int sizeToSentinel() {
    /*
    TEMPLATE: Same as class template
     */
    return 1 + this.next.sizeToSentinel();
  }
  
  // Deletes this node from its list by modifying its previous and next elements to refer to each
  // other. If this is a Sentinel, does nothing.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  @Override
  void selfDelete() {
    /*
    TEMPLATE: Same as class template
     */
    this.prev.replaceNext(this.next);
  }
  
  // Removes this from its list by modifying its previous and next elements to refer to each
  // other, returning the data of this. If this is a Sentinel, throws an exception.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  @Override
  T selfRemove() {
    /*
    TEMPLATE: Same as class template
     */
    this.selfDelete();
    return this.data;
  }
  
  // Finds the first occurrence of a Node that satisfies pred before the next Sentinel. If no
  // such node is found, returns the next Sentinel.
  @Override
  ANode<T> findToSentinel(Predicate<T> pred) {
    /*
    TEMPLATE
    Parameters:
    ... pred ...    -- Predicate<T>
    Methods:
    ... pred.test() -- boolean
     */
    if (pred.test(this.data)) {
      return this;
    } else {
      return this.next.findToSentinel(pred);
    }
  }
}

// PREDICATES FOR TESTING ------------------------------------------------------------------------

// A predicate to determine whether a String contains a substring provided upon construction
class StringContains implements Predicate<String> {
  String str;
  
  StringContains(String str) {
    this.str = str;
  }
  
  // Determines whether this String contains the substring provided upon construction
  @Override
  public boolean test(String s) {
    return s.contains(this.str);
  }
}

// A predicate to determine whether an Integer is greater than the int provided upon construction
class GreaterThan implements Predicate<Integer> {
  int value;
  
  GreaterThan(int value) {
    this.value = value;
  }
  
  // Determines whether an Integer is greater than the int provided upon construction
  @Override
  public boolean test(Integer that) {
    return that > value;
  }
}

// EXAMPLES ---------------------------------------------------------------------------------------

class ExamplesDeque {
  Sentinel<String> s1;
  Sentinel<Integer> s2;
  
  Node<String> abc;
  Node<String> bcd;
  Node<String> cde;
  Node<String> def;
  
  Node<Integer> i1;
  Node<Integer> i2;
  Node<Integer> i3;
  Node<Integer> i4;
  Node<Integer> i5;
  
  Deque<String> dq1;
  Deque<Integer> dq2;
  Deque<String> mtdq;
  
  
  void initDeques() {
    s1 = new Sentinel<>();
    s2 = new Sentinel<>();
    
    abc = new Node<>("abc", s1, s1);
    bcd = new Node<>("bcd", s1, abc);
    cde = new Node<>("cde", s1, bcd);
    def = new Node<>("def", s1, cde);
    
    i1 = new Node<>(4, s2, s2);
    i2 = new Node<>(2, s2, i1);
    i3 = new Node<>(5, s2, i2);
    i4 = new Node<>(8, s2, i3);
    i5 = new Node<>(3, s2, i4);
    
    dq1 = new Deque<>(s1);
    dq2 = new Deque<>(s2);
    mtdq = new Deque<>();
  }
  
  // Methods on Deque -----------------------------------------------------------------------------
  
  void testSize(Tester t) {
    initDeques();
    t.checkExpect(mtdq.size(), 0);
    t.checkExpect(dq1.size(), 4);
    t.checkExpect(dq2.size(), 5);
  }
  
  void testDequeAddAtHead(Tester t) {
    // Retrieve copies of the deques for testing against
    initDeques();
    Deque<String> dq1Copy = dq1;
    Deque<Integer> dq2Copy = dq2;
    Deque<String> mtdqCopy = mtdq;
    
    // Re-init the dequeues now that we have the references to the copies saved
    initDeques();
    
    // Adding a node to the head should be the same as instantiating a new node between the
    // sentinel and the first
    dq1Copy.addAtHead("aaa");
    new Node<>("aaa", abc, s1);
    t.checkExpect(dq1Copy, dq1);
    
    dq2Copy.addAtHead(1);
    new Node<>(1, i1, s2);
    t.checkExpect(dq2Copy, dq2);
    
    mtdqCopy.addAtHead("aaa");
    new Node<>("aaa", mtdq.header, mtdq.header);
    t.checkExpect(mtdqCopy, mtdq);
  }
  
  void testDequeAddAtTail(Tester t) {
    // Retrieve copies of the deques for testing against
    initDeques();
    Deque<String> dq1Copy = dq1;
    Deque<Integer> dq2Copy = dq2;
    Deque<String> mtdqCopy = mtdq;
    
    // Re-init the dequeues now that we have the references to the copies saved
    initDeques();
    
    // Adding a node to the tail should be the same as instantiating a new node between the
    // last and the sentinel
    dq1Copy.addAtTail("aaa");
    new Node<>("aaa", s1, def);
    t.checkExpect(dq1Copy, dq1);
    
    dq2Copy.addAtTail(1);
    new Node<>(1, s2, i5);
    t.checkExpect(dq2Copy, dq2);
    
    mtdqCopy.addAtTail("aaa");
    new Node<>("aaa", mtdq.header, mtdq.header);
    t.checkExpect(mtdqCopy, mtdq);
  }
  
  void testDequeRemoveFromHead(Tester t) {
    initDeques();
    
    t.checkExpect(dq1.removeFromHead(), "abc");
    t.checkExpect(s1.next, bcd);
    t.checkExpect(bcd.prev, s1);
    
    t.checkExpect(dq2.removeFromHead(), 4);
    t.checkExpect(s2.next, i2);
    t.checkExpect(i2.prev, s2);
  }
  
  void testDequeRemoveFromHeadEx(Tester t) {
    initDeques();
    t.checkException(
        new RuntimeException("Attempted to remove a sentinel"),
        mtdq,
        "removeFromHead");
  }
  
  void testDequeRemoveFromTail(Tester t) {
    initDeques();
    
    t.checkExpect(dq1.removeFromTail(), "def");
    t.checkExpect(s1.prev, cde);
    t.checkExpect(cde.next, s1);
    
    t.checkExpect(dq2.removeFromTail(), 3);
    t.checkExpect(s2.prev, i4);
    t.checkExpect(i4.next, s2);
  }
  
  void testDequeRemoveFromTailEx(Tester t) {
    initDeques();
    t.checkException(
        new RuntimeException("Attempted to remove a sentinel"),
        mtdq,
        "removeFromTail");
  }
  
  void testDequeFind(Tester t) {
    initDeques();
    StringContains pred1 = new StringContains("fgh");
    StringContains pred2 = new StringContains("cd");
    GreaterThan pred3 = new GreaterThan(5);
    
    t.checkExpect(dq1.find(pred1), s1);
    t.checkExpect(dq1.find(pred2), bcd);
    t.checkExpect(dq2.find(pred3), i4);
  }
  
  void testDequeRemoveNode(Tester t) {
    initDeques();
    t.checkNoException(dq1, "removeNode", s1);
    t.checkExpect(dq1.header, s1);
    t.checkExpect(dq1.size(), 4);
    
    t.checkNoException(dq2, "removeNode", i1);
    t.checkExpect(s2.next, i2);
    t.checkExpect(i2.prev, s2);
  }
  
  // Methods on ANode -----------------------------------------------------------------------------
  
  void testANodeReplaceNext(Tester t) {
    initDeques();
    i4.replaceNext(s2);
    t.checkExpect(i4.next, s2);
    t.checkExpect(s2.prev, i4);
  }
  
  void testANodeReplacePrev(Tester t) {
    initDeques();
    s2.replacePrev(i4);
    t.checkExpect(i4.next, s2);
    t.checkExpect(s2.prev, i4);
  }
  
  void testANodeUpdateNext(Tester t) {
    initDeques();
    t.checkException(
        new IllegalStateException("This node is not the previous before the provided node"),
        i5, "updateNext", i3);
    
    t.checkException(
        new IllegalStateException("This node is not the previous before the provided node"),
        i5, "updateNext", i4);
    
    i5.next = null;
    t.checkNoException(i5, "updateNext", s2);
    t.checkExpect(i5.next, s2);
  }
  
  void testANodeSizeToSentinel(Tester t) {
    initDeques();
    t.checkExpect(s1.sizeToSentinel(), 0);
    t.checkExpect(abc.sizeToSentinel(), 4);
    t.checkExpect(bcd.sizeToSentinel(), 3);
    t.checkExpect(cde.sizeToSentinel(), 2);
    t.checkExpect(def.sizeToSentinel(), 1);
  }
  
  void testANodeSelfDelete(Tester t) {
    initDeques();
    s1.selfDelete();
    t.checkExpect(dq1.header, s1);
    t.checkExpect(dq1.size(), 4);
    
    i1.selfDelete();
    t.checkExpect(s2.next, i2);
    t.checkExpect(i2.prev, s2);
  }
  
  void testANodeSelfRemove(Tester t) {
    initDeques();
    t.checkExpect(i1.selfRemove(), 4);
    t.checkExpect(s2.next, i2);
    t.checkExpect(i2.prev, s2);
  }
  
  void testANodeSelfRemoveEx(Tester t) {
    initDeques();
    t.checkException(
        new RuntimeException("Attempted to remove a sentinel"),
        s1,
        "selfRemove");
  }
  
  void testANodeFindToSentinel(Tester t) {
    initDeques();
    StringContains pred1 = new StringContains("fgh");
    StringContains pred2 = new StringContains("cd");
    
    t.checkExpect(abc.findToSentinel(pred1), s1);
    t.checkExpect(abc.findToSentinel(pred2), bcd);
    t.checkExpect(s1.findToSentinel(pred1), s1);
  }
  
  // Methods on Sentinel (exclusively) ------------------------------------------------------------
  
  void testSentinelSize(Tester t) {
    initDeques();
    t.checkExpect(s1.size(), 4);
    t.checkExpect(s2.size(), 5);
    t.checkExpect(new Sentinel<>().size(), 0);
  }
  
  void testSentinelAddAtHead(Tester t) {
    // Retrieve copies of s1 and s2 for testing against
    initDeques();
    Sentinel<String> s1Copy = s1;
    Sentinel<Integer> s2Copy = s2;
    
    // Re-init the dequeues now that we have the references to the copies saved
    initDeques();
    
    // Create empty sentinels for testing as well
    Sentinel<Integer> sMtCopy = new Sentinel<>();
    Sentinel<Integer> sMt = new Sentinel<>();
    
    // Adding a node to the head should be the same as instantiating a new node between the
    // sentinel and the first
    s1Copy.addAtHead("aaa");
    new Node<>("aaa", abc, s1);
    t.checkExpect(s1Copy, s1);
    
    s2Copy.addAtHead(1);
    new Node<>(1, i1, s2);
    t.checkExpect(s2Copy, s2);
    
    sMtCopy.addAtHead(1);
    new Node<>(1, sMt, sMt);
    t.checkExpect(sMtCopy, sMt);
  }
  
  void testSentinelAddAtTail(Tester t) {
    // Retrieve copies of s1 and s2 for testing against
    initDeques();
    Sentinel<String> s1Copy = s1;
    Sentinel<Integer> s2Copy = s2;
    
    // Re-init the dequeues now that we have the references to the copies saved
    initDeques();
    
    // Create empty sentinels for testing as well
    Sentinel<Integer> sMtCopy = new Sentinel<>();
    Sentinel<Integer> sMt = new Sentinel<>();
    
    // Adding a node to the tail should be the same as instantiating a new node between the
    // last and the sentinel
    s1Copy.addAtTail("aaa");
    new Node<>("aaa", s1, def);
    t.checkExpect(s1Copy, s1);
    
    s2Copy.addAtTail(1);
    new Node<>(1, s2, i5);
    t.checkExpect(s2Copy, s2);
    
    sMtCopy.addAtTail(1);
    new Node<>(1, sMt, sMt);
    t.checkExpect(sMtCopy, sMt);
  }
  
  void testSentinelRemoveFromHead(Tester t) {
    initDeques();
    
    t.checkExpect(s1.removeFromHead(), "abc");
    t.checkExpect(s1.next, bcd);
    t.checkExpect(bcd.prev, s1);
    
    t.checkExpect(s2.removeFromHead(), 4);
    t.checkExpect(s2.next, i2);
    t.checkExpect(i2.prev, s2);
  }
  
  void testSentinelRemoveFromHeadEx(Tester t) {
    initDeques();
    t.checkException(
        new RuntimeException("Attempted to remove a sentinel"),
        new Sentinel<>(),
        "removeFromHead");
  }
  
  void testSentinelRemoveFromTail(Tester t) {
    initDeques();
    
    t.checkExpect(s1.removeFromTail(), "def");
    t.checkExpect(s1.prev, cde);
    t.checkExpect(cde.next, s1);
    
    t.checkExpect(s2.removeFromTail(), 3);
    t.checkExpect(s2.prev, i4);
    t.checkExpect(i4.next, s2);
  }
  
  void testSentinelRemoveFromTailEx(Tester t) {
    initDeques();
    t.checkException(
        new RuntimeException("Attempted to remove a sentinel"),
        new Sentinel<>(),
        "removeFromTail");
  }
  
  // Methods on Node (exclusively) ----------------------------------------------------------------
  
  // creating new nodes should update its neighbor's references accordingly
  void testInsertNodeConstructor(Tester t) {
    initDeques();
    Node<String> str1 = new Node<>("str1", mtdq.header, mtdq.header);
    t.checkExpect(mtdq.size(), 1);
    t.checkExpect(mtdq.header.next, str1);
    t.checkExpect(mtdq.header.prev, str1);
    
    t.checkExpect(mtdq.header.next.next, mtdq.header);
    t.checkExpect(mtdq.header.prev.prev, mtdq.header);
    
    Node<Integer> i6 = new Node<>(9, s2, i5);
    t.checkExpect(dq2.size(), 6);
    t.checkExpect(i5.next, i6);
    t.checkExpect(s2.prev, i6);
    t.checkExpect(i6.next, s2);
    t.checkExpect(i6.prev, i5);
  }
  
}