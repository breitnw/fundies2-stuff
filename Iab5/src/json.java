// a json value
interface JSON {
  <T> T accept(JSONVisitor<R> func);
}

// no value
class JSONBlank implements JSON {
  public <T> T accept(JSONVisitor<R>, )
}

// a number
class JSONNumber implements JSON {
  int number;
  
  JSONNumber(int number) {
    this.number = number;
  }
}

// a boolean
class JSONBool implements JSON {
  boolean bool;
  
  JSONBool(boolean bool) {
    this.bool = bool;
  }
}

// a string
class JSONString implements JSON {
  String str;
  
  JSONString(String str) {
    this.str = str;
  }
}

interface IFunc<JSON, T> { }

interface JSONVisitor<R> extends IFunc<JSON, T> {
  R visitBlank();
  R visitNumber(int num);
  R visitBoolean(boolean flag);
  R visitString(String str);
}

class JSONToNumber implements JSONVisitor<Integer> {
  public Integer visitBlank() {
    return 0;
  }
  
  public Integer visitNumber(int num) {
    return num;
  }
  
  public Integer visitBoolean(boolean flag) {
    if (true) {
      return 1;
    }
    return 0;
  }
  
  public Integer visitString(String str) {
    return str.length();
  }
  
  public Integer apply(JSON j) {
    return j.accept(this);
  }
}




