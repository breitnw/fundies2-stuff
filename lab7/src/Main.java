import tester.Tester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

// Utilities for manipulating ArrayLists
class ListUtils {
  // Filters arr to only the elements that satisfy pred (if keepPassing is true) or only the
  // elements that fail pred (if keepPassing is false)
  <T> ArrayList<T> customFilter(ArrayList<T> arr, Predicate<T> pred, boolean keepPassing) {
    ArrayList<T> ret = new ArrayList<>();
    for (T val : arr) {
      if (pred.test(val) == keepPassing) {
        ret.add(val);
      }
    }
    return ret;
  }
  
  // Filters arr to an array containing only the elements that satisfy pred
  <T> ArrayList<T> filter(ArrayList<T> arr, Predicate<T> pred) {
    return this.customFilter(arr, pred, true);
  }
  
  // Filters arr to an array containing only the elements that do not satisfy pred
  <T> ArrayList<T> filterNot(ArrayList<T> arr, Predicate<T> pred) {
    return this.customFilter(arr, pred, false);
  }
  
  // Removes all the elements of arr that fail the predicate in-place
  <T> void removeFailing(ArrayList<T> arr, Predicate<T> pred) {
    for (int i = arr.size() - 1; i >= 0; i -= 1) {
      if (!pred.test(arr.get(i))) {
        arr.remove(i);
      }
    }
  }
}

class EvenPred implements Predicate<Integer> {
  public boolean test(Integer i) {
    return i % 2 == 0;
  }
}

class ExamplesListUtils {
  ListUtils lu = new ListUtils();
  ArrayList<Integer> mt = new ArrayList<>();
  ArrayList<Integer> ints;
  ArrayList<Integer> evens;
  ArrayList<Integer> odds;
  
  void initState() {
    ints = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    evens = new ArrayList<>(Arrays.asList(2, 4, 6, 8, 10));
    odds = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
  }
  
  void testCustomFilter(Tester t) {
    this.initState();
    t.checkExpect(lu.customFilter(ints, new EvenPred(), true), evens);
    t.checkExpect(lu.customFilter(ints, new EvenPred(), false), odds);
  }
  
  void testFilter(Tester t) {
    this.initState();
    t.checkExpect(lu.filter(ints, new EvenPred()), evens);
  }
  
  void testFilterNot(Tester t) {
    this.initState();
    t.checkExpect(lu.filterNot(ints, new EvenPred()), odds);
  }
  
  void testRemoveFailing(Tester t) {
    this.initState();
    lu.removeFailing(ints, new EvenPred());
    t.checkExpect(ints, evens);
    lu.removeFailing(odds, new EvenPred());
    t.checkExpect(odds, mt);
  }
}