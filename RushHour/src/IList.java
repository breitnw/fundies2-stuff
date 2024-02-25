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
  
  // Like an ormap, but also includes rest in the call to func.
  boolean restOrmap(BiFunction<T, IList<T>, Boolean> func);
  
  // Applies a function to the elements of one or more lists. Combines values in an arbitrary
  // way that is determined by func. The innermost value is combined with base, and subsequent
  // values are combined with previous return values.
  <U> U foldr(BiFunction<T, U, U> func, U base);
  
  // Maps each value in the list to a different value in an arbitrary way defined by func.
  <U> IList<U> map(Function<T, U> func);
}

// Represents a list of elements of type T with a first element and a list of remaining elements
class Cons<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  Cons(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.first ...                                          -- T
  ... this.rest ...                                           -- IList<T>
  METHODS:
  ... andmap(Function<T, Boolean> func) ...                   -- boolean
  ... ormap(Function<T, Boolean> func) ...                    -- boolean
  ... restOrmap(BiFunction<T, IList<T>, Boolean> func) ...    -- boolean
  ... foldr(BiFunction<T, U, U> func, U base) ...             -- U
  ... map(Function<T, U> func) ...                            -- IList<U>
  METHODS ON FIELDS:
  ... this.rest.andmap(Function<T, Boolean> func) ...                   -- boolean
  ... this.rest.ormap(Function<T, Boolean> func) ...                    -- boolean
  ... this.rest.restOrmap(BiFunction<T, IList<T>, Boolean> func) ...    -- boolean
  ... this.rest.foldr(BiFunction<T, U, U> func, U base) ...             -- U
  ... this.rest.map(Function<T, U> func) ...                            -- IList<U>
   */
  
  // Iterates over all elements of this Cons, returning true only if the provided predicate is
  // satisfied on each.
  @Override
  public boolean andmap(Function<T, Boolean> func) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...            -- Function<T, Boolean>
    METHODS ON PARAMETERS:
    ... func.apply(T) ...   -- boolean
     */
    return func.apply(this.first) && this.rest.andmap(func);
  }
  
  // Iterates over all elements of this Cons, returning true if the provided predicate is
  // satisfied on any one element.
  @Override
  public boolean ormap(Function<T, Boolean> func) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...            -- Function<T, Boolean>
    METHODS ON PARAMETERS:
    ... func.apply(T) ...   -- boolean
     */
    return func.apply(this.first) || this.rest.ormap(func);
  }
  
  // Like ormap, but also includes rest in the call to func.
  @Override
  public boolean restOrmap(BiFunction<T, IList<T>, Boolean> func) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...                      -- BiFunction<T, IList<T>, Boolean>
    METHODS ON PARAMETERS:
    ... func.apply(T, IList<T>) ...   -- Boolean
     */
    return func.apply(this.first, this.rest) || this.rest.restOrmap(func);
  }
  
  // Applies a function to the elements of one or more lists. Combines values in an arbitrary
  // way that is determined by func. The innermost value is combined with base, and subsequent
  // values are combined with previous return values.
  @Override
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...               -- BiFunction<T, U, U>
    ... base ...               -- U
    METHODS ON PARAMETERS:
    ... func.apply(T, U) ...   -- U
     */
    return func.apply(this.first, this.rest.foldr(func, base));
  }
  
  // Maps each value in the list to a different value in an arbitrary way defined by func.
  @Override
  public <U> IList<U> map(Function<T, U> func) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...            -- Function<T, U>
    METHODS ON PARAMETERS:
    ... func.apply(T) ...   -- U
     */
    return new Cons<U>(func.apply(this.first), this.rest.map(func));
  }
}

// Represents an empty list of elements of type T.
class Mt<T> implements IList<T> {
  /* TEMPLATE
  METHODS:
  ... andmap(Function<T, Boolean> func) ...                   -- boolean
  ... ormap(Function<T, Boolean> func) ...                    -- boolean
  ... restOrmap(BiFunction<T, IList<T>, Boolean> func) ...    -- boolean
  ... foldr(BiFunction<T, U, U> func, U base) ...             -- U
  ... map(Function<T, U> func) ...                            -- IList<U>
   */
  
  // Iterates over all elements of this Mt, returning true only if the provided predicate is
  // satisfied on each.
  @Override
  public boolean andmap(Function<T, Boolean> func) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...   -- Function<T, Boolean>
     */
    return true;
  }
  
  // Iterates over all elements of this Mt, returning true if the provided predicate is
  // satisfied on any one element.
  @Override
  public boolean ormap(Function<T, Boolean> func) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...   -- Function<T, Boolean>
     */
    return false;
  }
  
  // Like ormap, but also includes rest in the call to func.
  @Override
  public boolean restOrmap(BiFunction<T, IList<T>, Boolean> func) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...               -- BiFunction<T, IList<T>, Boolean>
    METHODS ON PARAMETERS:
    ... func.apply(T, IList<T>) ...   -- Boolean
     */
    return false;
  }
  
  // Applies a function to the elements of one or more lists. Combines values in an arbitrary
  // way that is determined by func. The innermost value is combined with base, and subsequent
  // values are combined with previous return values.
  @Override
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...   -- BiFunction<T, U, U>
    ... base ...   -- U
     */
    return base;
  }
  
  // Maps each value in the list to a different value in an arbitrary way defined by func.
  
  @Override
  public <U> IList<U> map(Function<T, U> func) {
    /* TEMPLATE
    PARAMETERS:
    ... func ...   -- Function<T, U>
     */
    return new Mt<U>();
  }
}