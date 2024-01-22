interface ILoString {
  ILoString reverse();
  ILoString normalize();
  ILoString scanConcat();
}

class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

   public ILoString reverse() { return null; }

  public ILoString normalize() {
    return new ConsLoString(this.first, this.rest.normalize());
  }

   public ILoString scanConcat() { return null; }
}

class MtLoString implements ILoString {
  public ILoString reverse() { return null; }


  public ILoString normalize() {
    return this;
  }
  
  public ILoString scanConcat() { return null; }

}

class SnocLoString implements ILoString {
  ILoString rest;
  String last;

  SnocLoString(ILoString rest, String last) {
    this.rest = rest;
    this.last = last;
  }
  public ILoString reverse() { return null; }


  public ILoString normalize() {
    return new AppendLoString(this.rest.normalize(), new ConsLoString(this.last, new MtLoString()));
  }
  
  public ILoString scanConcat() { return null; }
}

class AppendLoString implements ILoString {
  ILoString list1;
  ILoString list2;

  AppendLoString(ILoString list1, ILoString list2) {
    this.list1 = list1;
    this.list2 = list2;
  }
  public ILoString reverse() { return null; }


  public ILoString normalize() {
    return new AppendLoString(this.list1.normalize(), this.list2.normalize());
  }
  
  public ILoString scanConcat() { return null; }
}

// Given Auto-generated Code: public class MoreLists { }
