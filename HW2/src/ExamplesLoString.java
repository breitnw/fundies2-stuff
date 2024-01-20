import lists.*;
import tester.Tester;

public class ExamplesLoString {
  ILoString empty = new MtLoString();
  ILoString addDoc = new SnocLoString(empty, "Doc");
  ILoString addBashful = new ConsLoString("Bashful", addDoc);
  ILoString addDopey = new SnocLoString(addBashful, "Dopey");
  
  ILoString grumpy = new ConsLoString("Grumpy", empty);
  
  ILoString addSleepy = new ConsLoString("Sleepy", empty);
  ILoString addHappy = new ConsLoString("Happy", addSleepy);
  ILoString addSneezy = new SnocLoString(addHappy, "Sneezy");
  
  ILoString dwarves = new AppendLoString(new AppendLoString(addDopey, grumpy), addSneezy);
  
  boolean testReverse(Tester t) {
    ILoString rAddDoc = new ConsLoString("Doc", empty);
    ILoString rAddBashful = new SnocLoString(rAddDoc, "Bashful");
    ILoString rAddDopey = new ConsLoString("Dopey", rAddBashful);
    
    ILoString rGrumpy = new SnocLoString(empty, "Grumpy");
    
    ILoString rAddSleepy = new SnocLoString(empty, "Sleepy");
    ILoString rAddHappy = new SnocLoString(rAddSleepy, "Happy");
    ILoString rAddSneezy = new ConsLoString("Sneezy", rAddHappy);
    
    ILoString rDwarves = new AppendLoString(rAddSneezy, new AppendLoString(rGrumpy, rAddDopey));
    
    return t.checkExpect(addBashful.reverse(), rAddBashful);
  }
  
  boolean testNormalize(Tester t) {
    ILoString nDwarves = new ConsLoString("Bashful",
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
    );
    
    return t.checkExpect(dwarves.normalize(), nDwarves);
  }
  
//  boolean testScanConcat(Tester t) {
//    ILoString empty = new MtLoString();
//    ILoString sAddDoc = new SnocLoString(empty, "BashfulDoc");
//    ILoString sAddBashful = new ConsLoString("Bashful", sAddDoc);
//    ILoString sAddDopey = new SnocLoString(sAddBashful, "BashfulDocDopey");
//
//    ILoString sGrumpy = new ConsLoString("BashfulDocDopeyGrumpy", empty);
//
//    ILoString sAddSleepy = new ConsLoString("BashfulDocDopeyGrumpyHappySleepy", empty);
//    ILoString sAddHappy = new ConsLoString("BashfulDocDopeyGrumpyHappy", sAddSleepy);
//    ILoString sAddSneezy = new SnocLoString(sAddHappy, "BashfulDocDopeyGrumpyHappySleepySneezy");
//
//    ILoString sDwarves = new AppendLoString(new AppendLoString(sAddDopey, sGrumpy), sAddSneezy);
//
//    return t.checkExpect(dwarves.scanConcat(), sDwarves);
//  }
}
