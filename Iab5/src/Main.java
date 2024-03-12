// Represents functions of signature A -> R, for some argument type A and
// result type R
interface IFunc<A, R> {
  R apply(A input);
}

interface IFunc2<A1, A2, R> {
  R apply(A1 input1, A2 input2);
}

class Addition implements IFunc2<Integer, Integer, Integer> {
  public Integer apply(Integer input1, Integer input2) {
    return input1 + input2;
  }
}


// generic list
interface IList<T> {
  // map over a list, and produce a new list with a (possibly different)
  // element type
  <U> IList<U> map(IFunc<T, U> f);
  <U> U foldr(IFunc2<T, U, U> f, U base);
}

// empty generic list
class MtList<T> implements IList<T> {
  public <U> IList<U> map(IFunc<T, U> f) {
    return new MtList<U>();
  }
  
  public <U> U foldr(IFunc2<T, U, U> f, U base) {
    return base;
  }
}

// non-empty generic list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;
  
  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
  public <U> IList<U> map(IFunc<T, U> f) {
    return new ConsList<U>(f.apply(this.first), this.rest.map(f));
  }
  
  public <U> U foldr(IFunc2<T, U, U> f, U base) {
    return f.apply(this.first, this.rest.foldr(f, base));
  }
}