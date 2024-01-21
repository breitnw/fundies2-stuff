import lightning.*;
import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
// and predefined colors (Color.RED, Color.GRAY, etc.)

public class ExamplesLightning {
  ILightningBolt tip = new Tip();
  ILightningBolt bolt1 = new Fork(33, 30, 10, 25, 135, 40, new Tip(), new Tip());
  ILightningBolt bolt2 = new Fork(30, 22, 30, 10, 115, 65, new Tip(), new Tip());
  ILightningBolt seg1 = new Segment(40, 20, 130, bolt1); // not plausible!
  ILightningBolt seg2 = new Segment(50, 30, 77, bolt2); // plausible!

  ILightningBolt forked = new Fork(40, 50, 50, 40, 150, 30, seg1, seg2);

  boolean testIsPhysicallyPossible(Tester t) {
    return t.checkExpect(seg1.isPhysicallyPossible(), false)
            && t.checkExpect(seg2.isPhysicallyPossible(), true)
            && t.checkExpect(forked.isPhysicallyPossible(), false);
            // && t.checkExpect(bolt1.isPhysicallyPosibble(), true)
            // && t.checkExpect(tip.isPhysicallyPossible(), true);
  }

  boolean testCombine(Tester t) {
    int lAngleOffset = 150 - 90;
    int rAngleOffset = 30 - 90;

    ILightningBolt combined = seg1.combine(40, 50, 50, 40, 150, 30, seg2);

    return t.checkExpect(combined,
            new Fork(40, 50, 50, 40, 150, 30,
                    new Segment(40, 20, 130 + lAngleOffset,
                            new Fork(33, 30, 10, 25, 135 + lAngleOffset, 40 + lAngleOffset, new Tip(), new Tip())),
                    new Segment(50, 30, 77 + rAngleOffset,
                            new Fork(30, 22, 30, 10, 115 + rAngleOffset, 65 + rAngleOffset, new Tip(), new Tip()))));
  }

//  boolean testGetWidth(Tester t) {
//    return t.checkExpect(seg1.getWidth(), )
//  }

}

