import java.util.function.BiFunction;
import java.util.function.Function;

// Represents a list of elements of type T
interface IList<T> {
  // Iterates over all elements of this IList, returning true only if the provided predicate is
  // satisfied on each.
  boolean andmap(Function<T, Boolean> func);
  
  // Iterates over all elements of this IList, returning true if the provided predicate is
  // satisfied on any one element.
  boolean ormap(Function<T, Boolean> func);
  
  <U> U foldr(BiFunction<T, U, U> func, U base);
  
  <U> IList<U> map(Function<T, U> func);
}

class Cons<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  Cons(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
  @Override
  public boolean andmap(Function<T, Boolean> func) {
    return func.apply(this.first) && this.rest.andmap(func);
  }
  
  @Override
  public boolean ormap(Function<T, Boolean> func) {
    return func.apply(this.first) || this.rest.ormap(func);
  }
  
  @Override
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return func.apply(this.first, this.rest.foldr(func, base));
  }
  
  @Override
  public <U> IList<U> map(Function<T, U> func) {
    return new Cons<U>(func.apply(this.first), this.rest.map(func));
  }
}

class Mt<T> implements IList<T> {
  @Override
  public boolean andmap(Function<T, Boolean> func) {
    return true;
  }
  
  @Override
  public boolean ormap(Function<T, Boolean> func) {
    return false;
  }
  
  @Override
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return base;
  }
  
  @Override
  public <U> IList<U> map(Function<T, U> func) {
    return new Mt<U>();
  }
}