import java.util.ArrayDeque;

class Stack<T> {
  ArrayDeque<T> contents;
  void push(T item) {
    this.contents.addFirst(item);
  }
  boolean isEmpty() {
    return this.contents.isEmpty();
  }
  T pop() {
    return this.contents.removeFirst();
  }
}

class Queue<T> {
  ArrayDeque<T> contents;
  void push(T item) {
    this.contents.addFirst(item);
  }
  boolean isEmpty() {
    return this.contents.isEmpty();
  }
  T pop() {
    return this.contents.removeLast();
  }
}