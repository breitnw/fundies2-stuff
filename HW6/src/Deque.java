import tester.Tester;

import java.util.function.Predicate;

// Represents a generic list that can be traversed in either direction, from the front or from
// the back.
class Deque<T> {
  Sentinel<T> header;
  
  Deque() {
    this(new Sentinel<>());
  }
  
  Deque(Sentinel<T> s) {
    this.header = s;
  }
  
  // Counts the number of nodes in this Deque, not including the header node.
  int size() {
    return this.header.size();
  }
  
  // Consumes a value of type T and inserts it at the front of the list.
  // EFFECT: updates the references of the header and its next element to refer to a new
  // element between them.
  void addAtHead(T that) {
    this.insert(that, 0);
  }
  
  // Consumes a value of type T and inserts it at the tail of the list.
  // EFFECT: updates the references of the header and its previous element to refer to a new
  // element between them.
  void addAtTail(T that) {
    this.insert(that, -1);
  }
  
  // Inserts a new node with the specified data at index i of the list, with natural indices
  // inserting ahead of the header element and negative indices inserting behind it. For
  // instance, i = 0 inserts at the head of the list, and i = -1 inserts at the tail.
  // EFFECT: mutates the list to insert the provided element at index i.
  void insert(T that, int i) {
    this.header.insert(that, i);
  }
  
  // Removes the first node from this Deque and returns its data, throwing an exception if this
  // Deque is empty
  // EFFECT: The first node is removed from this Deque and the references are corrected
  // accordingly.
  T removeFromHead() {
    return this.remove(1);
  }
  
  // Removes the last node from this Deque and returns its data, throwing an exception if this
  // Deque is empty
  // EFFECT: The last node is removed from this Deque and the references are corrected accordingly.
  T removeFromTail() {
    return this.remove(-1);
  }
  
  // Removes the node at index i of the list, with positive indices removing ahead of the header,
  // and negative indices inserting behind it. Throws an exception if i = 0 or another index
  // that refers to the header (for example, in an empty list, i = 1 will also refer to the header)
  // Otherwise, returns the data from that index.
  // EFFECT: mutates the list to remove the element at index i.
  T remove(int i) {
    return this.header.remove(i);
  }
  
  // Produces the first node in this Deque for which the given predicate returns true. If the
  // predicate never returns true for any value in this Deque, returns the header node in this
  // Deque.
  ANode<T> find(Predicate<T> pred) {
    return this.header.find(pred);
  }
  
  // Removes the provided node from the Deque. If the provided node is a Sentinel, does nothing.
  // NOTE: Does not need to return anything, since the user already must already have access to
  // the node in question in order to pass it to the method.
  void removeNode(ANode<T> node) {
    node.selfRemove();
  }
}

// Represents a node or sentinel in a generic list that can be traversed in either direction, from
// the front or from the back.
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;
  
  // Replaces the previous element of this ANode with that, and updates the next element of that
  // ANode accordingly.
  // EFFECT: updates the previous element of this and the next element of that according to the
  // purpose statement.
  void replacePrev(ANode<T> that) {
    this.prev = that;
    that.updateNext(this);
  }
  
  // Replaces the next element of this ANode with that, and updates the previous element of that
  // ANode accordingly.
  // EFFECT: updates the next element of this and the previous element of that according to the
  // purpose statement.
  void replaceNext(ANode<T> that) {
    that.replacePrev(this);
  }
  
  // Updates the next element of this node to that, throwing an exception if the node preceding
  // that node is not this.
  // EFFECT: updates the next element of this according to the purpose statement.
  void updateNext(ANode<T> that) {
    if (that.prev != this) {
      throw new IllegalStateException("This node is not the previous before the provided node");
    }
    this.next = that;
  }
  
  // Gets the total number of nodes (including this one, if it is a node) until the next sentinel
  // in the list.
  abstract int sizeToSentinel();
  
  // Inserts a new node with the specified data at index i of the list, with natural indices
  // inserting ahead of this element and negative indices inserting behind it. For instance, i = 0
  // inserts at the head of the list, and i = -1 inserts at the tail.
  // EFFECT: mutates the list to insert the provided element at index i.
  void insert(T that, int i) {
    if (i > 0) {
      this.next.insert(that, i - 1);
    } else if (i < 0) {
      this.prev.insert(that, i + 1);
    } else {
      new Node<T>(that, this.next, this);
    }
  }
  
  // Removes the node at index i of the list, with positive indices removing ahead of the header,
  // and negative indices inserting behind it. Throws an exception if i = 0 or another index
  // that refers to the header (for example, in an empty list, i = 1 will also refer to the header)
  // Otherwise, returns the data from that index.
  // EFFECT: mutates the list to remove the element at index i.
  T remove(int i) {
    if (i > 0) {
      return this.next.remove(i - 1);
    } else if (i < 0) {
      return this.prev.remove(i + 1);
    } else {
      this.selfRemove();
      return this.getData();
    }
  }
  
  // Removes this node from its list by modifying its previous and next elements to refer to each
  // other. If this is a Sentinel, does nothing.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  abstract void selfRemove();
  
  // Gets the data contained by this ANode, throwing a RuntimeException if there is none.
  abstract T getData();
  
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
  
  // Counts the number of nodes following this Sentinel before arriving back at the Sentinel.
  int size() {
    return this.next.sizeToSentinel();
  }
  
  // Gets the total number of nodes (including this one, if it is a node) until the next sentinel
  // in the list.
  @Override
  int sizeToSentinel() {
    return 0;
  }
  
  // Removes this from its list by modifying its previous and next elements to refer to each
  // other. If this is a Sentinel, does nothing.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  @Override
  void selfRemove() {
    this.prev.replaceNext(this.next);
  }
  
  // Gets the data contained by this ANode, throwing a RuntimeException if there is none.
  @Override
  T getData() {
    throw new RuntimeException("Attempted to access data on a sentinel");
  }
  
  // Produces the first node after this Sentinel for which the given predicate returns true. If the
  // predicate never returns true for any value after this Sentinel, returns the header node.
  ANode<T> find(Predicate<T> pred) {
    return this.next.findToSentinel(pred);
  }
  
  // Finds the first occurrence of a Node that satisfies pred before the next Sentinel. If no
  // such node is found, returns the next Sentinel.
  @Override
  ANode<T> findToSentinel(Predicate<T> pred) {
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
  
  Node(T data, ANode<T> next, ANode<T> prev) {
    this(data);
    
    if (this.next == null || this.prev == null) {
      throw new IllegalArgumentException(
          "One or both of the ANodes provided to the Node constructor is null");
    }
    
    this.replaceNext(next);
    this.replacePrev(prev);
  }
  
  // Gets the total number of nodes (including this one, if it is a node) until the next sentinel
  // in the list.
  @Override
  int sizeToSentinel() {
    return 1 + this.next.sizeToSentinel();
  }
  
  // Removes this node from its list by modifying its previous and next elements to refer to each
  // other. If this is a Sentinel, does nothing.
  // EFFECT: if this is a node, modifies the previous and next elements in this list to refer to
  // each other.
  @Override
  void selfRemove() { }
  
  // Gets the data contained by this ANode, throwing a RuntimeException if there is none.
  @Override
  T getData() {
    return data;
  }
  
  // Finds the first occurrence of a Node that satisfies pred before the next Sentinel. If no
  // such node is found, returns the next Sentinel.
  @Override
  ANode<T> findToSentinel(Predicate<T> pred) {
    if (pred.test(this.data)) {
      return this;
    } else {
      return this.next.findToSentinel(pred);
    }
  }
}

class ExamplesDeque {
  Deque<String> deque1 = new Deque<>();
  Deque<String> deque2 = new Deque<>();
  Deque<String> deque3 = new Deque<>();
  
  void initDeques() {
    new Node<String>("abc",
        new Node<String>("bcd",
            new Node<String>("cde",
                new Node<String>("def",
                    deque2.header,
                    deque2.header),
                deque2.header),
            deque2.header),
        deque2.header);
    
    new Node<String>("foobar",
        new Node<String>("barfoo",
            new Node<String>("bazbar",
                new Node<String>("foobaz",
                    new Node<String>("barbar",
                        deque3.header,
                        deque3.header),
                    deque3.header),
                deque3.header),
            deque3.header),
        deque3.header);
  }
  
  void testSize(Tester t) {
    initDeques();
    t.checkExpect(deque1.size(), 0);
    t.checkExpect(deque2.size(), 4);
    t.checkExpect(deque3.size(), 5);
  }
}