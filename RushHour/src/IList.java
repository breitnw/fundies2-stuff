import java.util.function.BiFunction;
import java.util.function.Function;

/*
class ListUtils {
  // Iterates over all elements of the given ArrayList, returning true only if the provided
  // predicate is satisfied on each.
  <T> boolean andmap(List<T> ls, Function<T, Boolean> func) {
    boolean res = true;
    for (T t : ls) {
      res &= func.apply(t);
    }
    return res;
  }

  // Iterates over all elements of this ArrayList, returning true if the provided predicate is
  // satisfied on any one element.
  <T> boolean ormap(List<T> ls, Function<T, Boolean> func) {
    boolean res = false;
    for (T t : ls) {
      res |= func.apply(t);
    }
    return res;
  }

  // Like an ormap, but also includes the rest of the list after this element in the call to func.
  <T> boolean restOrmap(List<T> ls, BiFunction<T, List<T>, Boolean> func) {
    boolean res = false;
    for (int i = 0; i < ls.size(); i += 1) {
      res |= func.apply(ls.get(i), ls.subList(i + 1, ls.size()));
    }
    return res;
  }

  // Applies a function to the elements of one or more lists. Combines values in an arbitrary
  // way that is determined by func. The first value is combined with base, and subsequent
  // values are combined with previous return values.
  <T, U> U foldl(List<T> ls, BiFunction<T, U, U> func, U base) {
    U acc = base;
    for (T t : ls) {
      acc = func.apply(t, base);
    }
    return acc;
  }
}
 */

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
  
  // Applies a function to the elements of one or more lists. Combines values in an arbitrary
  // way that is determined by func. The innermost value is combined with base, and subsequent
  // values are combined with previous return values.
  <U> U foldr(BiFunction<T, U, U> func, U base);
  
  // Maps each value in the list to a different value in an arbitrary way defined by func.
  <U> IList<U> map(Function<T, U> func);
  
  // Produces a new list with the first instance of the provided element removed. If the provided
  // element is not in this list, does nothing. Uses intensional equality for comparison.
  // TODO: needs tests
  IList<T> remove(T that);
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
  
  // Applies a function to the elements of one or more lists. Combines values in an arbitrary
  // way that is determined by func. The innermost value is combined with base, and subsequent
  // values are combined with previous return values.
  @Override
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return func.apply(this.first, this.rest.foldr(func, base));
  }
  
  // Maps each value in the list to a different value in an arbitrary way defined by func.
  @Override
  public <U> IList<U> map(Function<T, U> func) {
    return new Cons<U>(func.apply(this.first), this.rest.map(func));
  }
  
  // Produces a new list with the first instance of the provided element removed. If the provided
  // element is not in this list, does nothing. Uses double-equals (instance-wise) equality for
  // comparison.
  @Override
  public IList<T> remove(T that) {
    if (this.first == that) {
      return this.rest;
    } else {
      return new Cons<>(this.first, this.rest.remove(that));
    }
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
  
  // Applies a function to the elements of one or more lists. Combines values in an arbitrary
  // way that is determined by func. The innermost value is combined with base, and subsequent
  // values are combined with previous return values.
  @Override
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return base;
  }
  
  // Maps each value in the list to a different value in an arbitrary way defined by func.
  
  @Override
  public <U> IList<U> map(Function<T, U> func) {
    return new Mt<U>();
  }
  
  // Produces a new list with the first instance of the provided element removed. If the provided
  // element is not in this list, does nothing. Uses double-equals (instance-wise) equality for
  // comparison.
  @Override
  public IList<T> remove(T that) {
    return this;
  }
}