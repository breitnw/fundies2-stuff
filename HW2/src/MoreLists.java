import tester.Tester;

interface ILoString {
  ILoString reverse();
  ILoString normalize();
  ILoString scanConcat();
//  ILoString addToEnd(ILoString that);
}

class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoString reverse() {
    return new SnocLoString(this.rest.reverse(), this.first);
  }

  public ILoString normalize() {
    return new ConsLoString(this.first, this.rest.normalize());
  }

  public ILoString scanConcat() { return null; }
  
//  public ILoString addToEnd(ILoString that) {
//    return new ConsLoString(this.first, this.rest.addToEnd(that));
//  }
}

class MtLoString implements ILoString {
  public ILoString reverse() { return this; }


  public ILoString normalize() {
    return this;
  }
  
  public ILoString scanConcat() { return null; }

  public ILoString addToEnd(ILoString that) {
    return that;
  }
}

class SnocLoString implements ILoString {
  ILoString rest;
  String last;

  SnocLoString(ILoString rest, String last) {
    this.rest = rest;
    this.last = last;
  }
  public ILoString reverse() {
    return new ConsLoString(this.last, this.rest.reverse());
  }
  
  // TODO: I have made a mistake...
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
  public ILoString reverse() {
    return new AppendLoString(this.list2.reverse(), this.list1.reverse());
  }
  
  // TODO: I have made many a mistake...
  public ILoString normalize() {
    return new AppendLoString(this.list1.normalize(), this.list2.normalize());
  }
  
  public ILoString scanConcat() { return null; }
}

class ExamplesLoString {
  ILoString empty = new MtLoString();
  ILoString addDoc = new SnocLoString(empty, "Doc");
  ILoString addBashful = new ConsLoString("Bashful", addDoc);
  ILoString addDopey = new SnocLoString(addBashful, "Dopey");
  
  ILoString grumpy = new ConsLoString("Grumpy", empty);
  
  ILoString addSleepy = new ConsLoString("Sleepy", empty);
  ILoString addHappy = new ConsLoString("Happy", addSleepy);
  ILoString addSneezy = new SnocLoString(addHappy, "Sneezy");
  
  ILoString dwarves = new AppendLoString(new AppendLoString(addDopey, grumpy), addSneezy);
  
  ILoString everybody = new ConsLoString("Snow White", dwarves);
  
  boolean testReverse(Tester t) {
    ILoString rAddDoc = new ConsLoString("Doc", empty);
    ILoString rAddBashful = new SnocLoString(rAddDoc, "Bashful");
    ILoString rAddDopey = new ConsLoString("Dopey", rAddBashful);
    
    ILoString rGrumpy = new SnocLoString(empty, "Grumpy");
    
    ILoString rAddSleepy = new SnocLoString(empty, "Sleepy");
    ILoString rAddHappy = new SnocLoString(rAddSleepy, "Happy");
    ILoString rAddSneezy = new ConsLoString("Sneezy", rAddHappy);
    
    ILoString rDwarves = new AppendLoString(rAddSneezy, new AppendLoString(rGrumpy, rAddDopey));
    ILoString rEverybody = new SnocLoString(rDwarves, "Snow White");
    
    return t.checkExpect(addBashful.reverse().normalize(), rAddBashful.normalize())
             && t.checkExpect(everybody.reverse().normalize(), rEverybody.normalize());
  }
  
  boolean testNormalize(Tester t) {
    ILoString nEverybody = new ConsLoString("Snow White",
      new ConsLoString("Bashful",
        new ConsLoString("Doc",
          new ConsLoString("Dopey",
            new ConsLoString("Grumpy",
              new ConsLoString("Happy",
                new ConsLoString("Sleepy",
                  new ConsLoString("Sneezy",
                    empty
                  )
                )
              )
            )
          )
        )
      )
    );
    
    return t.checkExpect(everybody.normalize(), nEverybody);
  }
  
  boolean testScanConcat(Tester t) {
    ILoString empty = new MtLoString();
    ILoString sAddDoc = new SnocLoString(empty, "Snow WhiteBashfulDoc");
    ILoString sAddBashful = new ConsLoString("Snow WhiteBashful", sAddDoc);
    ILoString sAddDopey = new SnocLoString(sAddBashful, "Snow WhiteBashfulDocDopey");
    
    ILoString sGrumpy = new ConsLoString("Snow WhiteBashfulDocDopeyGrumpy", empty);
    
    ILoString sAddSleepy = new ConsLoString("Snow WhiteBashfulDocDopeyGrumpyHappySleepy", empty);
    ILoString sAddHappy = new ConsLoString("Snow WhiteBashfulDocDopeyGrumpyHappy", sAddSleepy);
    ILoString sAddSneezy = new SnocLoString(sAddHappy, "Snow WhiteBashfulDocDopeyGrumpyHappySleepySneezy");
    
    ILoString sDwarves = new AppendLoString(new AppendLoString(sAddDopey, sGrumpy), sAddSneezy);
    
    ILoString sEverybody = new ConsLoString("Snow White", sDwarves);
    
    ILoString oneList = new SnocLoString(new SnocLoString(empty, "one"), "two");
    
    return t.checkExpect(everybody.scanConcat(), sEverybody.normalize())
             && t.checkExpect(empty.scanConcat(), empty)
             && t.checkExpect(oneList.scanConcat(), new ConsLoString("one", new ConsLoString("onetwo", empty)));
  }
}
