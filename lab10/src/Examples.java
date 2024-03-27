import student.HeapSort;
import tester.Tester;

import java.util.ArrayList;
import java.util.Comparator;

class ExamplesMyHeap {
  void testHeapSort(Tester t) {
    ArrayList<Integer> test = new ArrayList<>();
    for (int i = 0; i <= 100; i += 1) {
      test.add((int) (Math.random() * 100));
    }
    new HeapSort<Integer>().heapsort(test, Comparator.naturalOrder());
    for (int i = 0; i <= 100; i += 1) {
      System.out.println(test.get(i));
    }
  }
}