import tester.*;

public class ExamplesLightning {
  ILightningBolt tip = new Tip();
  ILightningBolt fork1 = new Fork(33, 30, 10, 25, 135, 40, new Tip(), new Tip());
  ILightningBolt fork2 = new Fork(30, 22, 20, 10, 115, 65, new Tip(), new Tip());
  ILightningBolt seg1 = new Segment(40, 20, 130, fork1); // Not plausible!
  ILightningBolt seg2 = new Segment(50, 30, 77, fork2); // Plausible!
  ILightningBolt seg3 = new Segment(50, 29, 77, fork2); // Not plausible!

  ILightningBolt forked = new Fork(40, 50, 30, 35, 150, 30, seg2, seg2);

  boolean testIsPhysicallyPossible1(Tester t) {
    return t.checkExpect(seg1.isPhysicallyPossible(), false);
  }
  
  boolean testIsPhysicallyPossible2(Tester t) {
    return t.checkExpect(seg2.isPhysicallyPossible(), true);
  }
  
  boolean testIsPhysicallyPossible3(Tester t) {
    return t.checkExpect(forked.isPhysicallyPossible(), true);
  }
  
  boolean testIsPhysicallyPossible4(Tester t) {
    return t.checkExpect(seg3.isPhysicallyPossible(), false);
  }

  boolean testCombine1(Tester t) {
    int lAngleOffset = 150 - 90;
    int rAngleOffset = 30 - 90;

    ILightningBolt combined = seg1.combine(40, 50, 50, 40, 150, 30, seg2);

    return t.checkExpect(combined,
      new Fork(40, 50, 50, 40, 150, 30,
        new Segment(40, 20, 130 + lAngleOffset,
          new Fork(33, 30, 10, 25, 135 + lAngleOffset, 40 + lAngleOffset, new Tip(), new Tip())),
        new Segment(50, 30, 77 + rAngleOffset,
          new Fork(30, 22, 20, 10, 115 + rAngleOffset, 65 + rAngleOffset,
            new Tip(), new Tip()))));
  }
  
  boolean testCombine2(Tester t) {
    ILightningBolt shortSeg1 = new Segment(20, 49, 22, new Tip());
    ILightningBolt shortSeg2 = new Tip();
    ILightningBolt combined = shortSeg1.combine(40, 50, 50, 40, 90, 90, shortSeg2);
    
    return t.checkExpect(combined,
      new Fork(40, 50, 50, 40, 90, 90,
        shortSeg1,
        shortSeg2));
  }
  
  boolean testCombine3(Tester t) {
    int lAngleOffset = 150 - 90;
    int rAngleOffset = 30 - 90;
    
    ILightningBolt combined = fork1.combine(40, 50, 50, 40, 150, 30, fork2);
    
    return t.checkExpect(combined,
      new Fork(40, 50, 50, 40, 150, 30,
        new Fork(33, 30, 10, 25, 135 + lAngleOffset, 40 + lAngleOffset,
          new Tip(),
          new Tip()),
        new Fork(30, 22, 20, 10, 115 + rAngleOffset, 65 + rAngleOffset,
          new Tip(),
          new Tip())));
  }
  
  boolean testCombine4(Tester t) {
    ILightningBolt combined = fork1.combine(40, 50, 50, 40, 90, 90, fork2);

    return t.checkExpect(combined,
      new Fork(40, 50, 50, 40, 90, 90,
        new Fork(33, 30, 10, 25, 135, 40,
          new Tip(),
          new Tip()),
        new Fork(30, 22, 20, 10, 115, 65,
          new Tip(),
          new Tip())));
  }

//  boolean testGetWidth(Tester t) {
//    return t.checkExpect(seg1.getWidth(), )
//  }

}

