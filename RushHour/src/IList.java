import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

// Represents a list of elements of type T
interface IList<T> {
  // Iterates over all elements of this IList, returning true only if the provided predicate is
  // satisfied on each.
  boolean andmap(Function<T, Boolean> func);
  
  // Iterates over all elements of this IList, returning true if the provided predicate is
  // satisfied on any one element.
  boolean ormap(Function<T, Boolean> func);
  
  // Like an ormap, but also includes rest in the call to func.
  boolean restOrmap(BiFunction<T, IList<T>, Boolean> func);
  
  // Produces a new list with the first instance of the provided element removed. If the provided
  // element is not in this list, does nothing. Uses intensional equality for comparison.
  IList<T> without(T that);

  // Executes the provided consumer for each element in this list
  // EFFECT: each element in the list is mutated according to the consumer
  void forEach(Consumer<T> c);
}

// Represents a list of elements of type T with a first element and a list of remaining elements
class Cons<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  Cons(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
  // Iterates over all elements of this Cons, returning true only if the provided predicate is
  // satisfied on each.
  @Override
  public boolean andmap(Function<T, Boolean> func) {
    return func.apply(this.first) && this.rest.andmap(func);
  }
  
  // Iterates over all elements of this Cons, returning true if the provided predicate is
  // satisfied on any one element.
  @Override
  public boolean ormap(Function<T, Boolean> func) {
    return func.apply(this.first) || this.rest.ormap(func);
  }
  
  // Like ormap, but also includes rest in the call to func.
  @Override
  public boolean restOrmap(BiFunction<T, IList<T>, Boolean> func) {
    return func.apply(this.first, this.rest) || this.rest.restOrmap(func);
  }
  
  // Produces a new list with the first instance of the provided element removed. If the provided
  // element is not in this list, does nothing. Uses intensional equality for comparison
  @Override
  public IList<T> without(T that) {
    if (this.first == that) {
      return this.rest;
    } else {
      return new Cons<>(this.first, this.rest.without(that));
    }
  }

  // Executes the provided consumer for each element in this list
  // EFFECT: each element in the list is mutated according to the consumer
  @Override
  public void forEach(Consumer<T> c) {
    c.accept(this.first);
    this.rest.forEach(c);
  }
}

// Represents an empty list of elements of type T.
class Mt<T> implements IList<T> {
  
  // Iterates over all elements of this Mt, returning true only if the provided predicate is
  // satisfied on each.
  @Override
  public boolean andmap(Function<T, Boolean> func) {
    return true;
  }
  
  // Iterates over all elements of this Mt, returning true if the provided predicate is
  // satisfied on any one element.
  @Override
  public boolean ormap(Function<T, Boolean> func) {
    return false;
  }
  
  // Like ormap, but also includes rest in the call to func.
  @Override
  public boolean restOrmap(BiFunction<T, IList<T>, Boolean> func) {
    return false;
  }
  
  // Produces a new list with the first instance of the provided element removed. If the provided
  // element is not in this list, does nothing. Uses double-equals (instance-wise) equality for
  // comparison.
  @Override
  public IList<T> without(T that) {
    return this;
  }

  // Executes the provided consumer for each element in this list
  // EFFECT: each element in the list is mutated according to the consumer
  @Override
  public void forEach(Consumer<T> c) {
    // Since the list is empty and we only run the consumer for non-empty lists, we don't need to
    // do anything here
  }
}