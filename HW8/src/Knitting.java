import tester.Tester;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

// Knitted fabric ---------------------------------------------------------------------------------

class KnittedFabric {
  ArrayList<KnittedRow> rows;

  KnittedFabric(ArrayList<KnittedRow> rows) {
    this.rows = rows;
  }

  // Convenience constructor to create an empty KnittedFabric.
  KnittedFabric() {
    this(new ArrayList<>());
  }

  // Renders the fabric as a String, with each row separated by a newline character.
  String renderFabric() {
    String fabric = "";
    for (KnittedRow row : this.rows) {
      fabric = fabric + row.renderRow() + "\n";
    }
    return fabric;
  }

  // Adds a row containing the stitches from rowIter to this fabric, and returns this fabric so
  // that more rows can be added.
  // EFFECT: adds a row containing the stitches from the provided iterator to the knitted fabric
  KnittedFabric addRow(Iterator<IStitch> rowIter) {
    this.rows.add(new KnittedRow(rowIter));
    return this;
  }

  // returns a new KnittedFabric flipped over
  KnittedFabric reversed() {
    ArrayList<KnittedRow> newRows = new ArrayList<>();
    for (KnittedRow row : this.rows) {
      newRows.add(row.reversed());
    }
    return new KnittedFabric(newRows);
  }

  // Determines if all the rows in this KnittedFabric have the same contents as the rows of that
  // KnittedFabric
  boolean sameRows(KnittedFabric other) {
    for (int i = 0; i < this.rows.size(); i++) {
      if (!this.rows.get(i).sameRow(other.rows.get(i))) {
        return false;
      }
    }
    return true;
  }

  // Determines if this fabric looked the same as the other fabric (in both its original direction
  // _and_ flipped over)
  public boolean sameFabric(KnittedFabric other) {
    if (this.rows.size() != other.rows.size()) {
      return false;
    }
    // because the fabric could be flipped over
    return this.sameRows(other) || this.reversed().sameRows(other);
  }
}

// Knitted rows -----------------------------------------------------------------------------------

// Represents a row of stitches
class KnittedRow {
  ArrayList<IStitch> stitches; // where true represents a knit and false represents a purl

  KnittedRow(ArrayList<IStitch> stitches) {
    this.stitches = stitches;
  }

  // Convenience constructor to create a knitted row from an iterator of stitches.
  KnittedRow(Iterator<IStitch> stitchIter) {
    this.stitches = new ArrayList<>();
    while (stitchIter.hasNext()) {
      this.stitches.add(stitchIter.next());
    }
  }

  // returns a string representation of this row of stitches
  String renderRow() {
    String result = "";
    for (IStitch stitch : stitches) {
      result += stitch.renderStitch();
    }
    return result;
  }

  // Reverses the order of this KnittedRow's stitches, and flips each individual stitch
  KnittedRow reversed() {
    ArrayList<IStitch> reversedOrder = new ArrayUtils().reverse(this.stitches);
    ArrayList<IStitch> reversedStitches = new ArrayList<>();
    for (IStitch stitch : reversedOrder) {
      reversedStitches.add(stitch.reversed());
    }
    return new KnittedRow(reversedStitches);
  }

  // determines if the stitches of this row are exactly the same as the other row
  public boolean sameRow(KnittedRow other) {
    for (int i = 0; i < this.stitches.size(); i++) {
      if (!this.stitches.get(i).sameStitch(other.stitches.get(i))) {
        return false;
      }
    }
    return true;
  }
}

// Knits ------------------------------------------------------------------------------------------

// Represents a single stitch in a knitted fabric
interface IStitch {
  // Renders this stitch as a one-character string
  String renderStitch();

  // Reverses this stitch; i.e., determines what it would be if looked at from the back
  IStitch reversed();

  // Determines if the other IStitch is the same as this IStitch
  boolean sameStitch(IStitch other);

  // Determines if this stitch and other are both Knits
  boolean sameKnit(Knit other);

  // Determines if this stitch and other are both Purls
  boolean samePurl(Purl other);
}

// Represents a single knit in a knitted fabric
class Knit implements IStitch {
  // Renders this knit as a one-character string
  @Override
  public String renderStitch() {
    return "V";
  }

  // Reverses this stitch; i.e., determines what it would be if looked at from the back
  @Override
  public IStitch reversed() {
    return new Purl();
  }

  // Determines if this is the same stitch as other
  @Override
  public boolean sameStitch(IStitch other) {
    return other.sameKnit(this);
  }

  // Determines if this stitch and other are both Knits
  @Override
  public boolean sameKnit(Knit other) {
    return true;
  }

  // Determines if this stitch and other are both Purls
  @Override
  public boolean samePurl(Purl other) {
    return false;
  }
}

// Represents a single purl in a knitted fabric
class Purl implements IStitch {
  // Renders this purl as a one-character string
  @Override
  public String renderStitch() {
    return "-";
  }

  // Reverses this stitch; i.e., determines what it would be if looked at from the back
  @Override
  public IStitch reversed() {
    return new Knit();
  }

  // Determines if the other IStitch is the same as this IStitch
  @Override
  public boolean sameStitch(IStitch other) {
    return other.samePurl(this);
  }

  // Determines if this stitch and other are both Knits
  @Override
  public boolean sameKnit(Knit other) {
    return false;
  }

  // Determines if this stitch and other are both Purls
  @Override
  public boolean samePurl(Purl other) {
    return true;
  }
}

// Instructions -----------------------------------------------------------------------------------

class KnitFabricInstructions {
  ArrayList<KnitRowInstructions> rowInstructions;

  KnitFabricInstructions addRow(Iterator<IInstruction> instructions) {
    // TODO
  }

  KnittedFabric makeFabric() {
    /*
    boolean isRev = false;
    for (int i = 0; i < ; i++) {

    }

     */
  }

  boolean sameInstructions(KnitFabricInstructions other) {
    KnittedFabric thisFabric = this.makeFabric();
    KnittedFabric otherFabric = other.makeFabric();

    return thisFabric.sameFabric(otherFabric);
  }

}

class KnitRowInstructions {
  ArrayList<IInstruction> instructions;
}

interface IInstruction extends Iterable<IStitch> {

}

abstract class AInstruction implements IInstruction {
  int numRepetitions;

  AInstruction(int numRepetitions) {
    this.numRepetitions = numRepetitions;
  }
}

class KnitInstruction extends AInstruction {
  KnitInstruction(int numRepetitions) {
    super(numRepetitions);
  }
}

// instruction classes

class MtIter<T> implements Iterator<T> {
  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public T next() {
    throw new RuntimeException("Iterator does not have a next element");
  }
}

class ComposedIter<T> implements Iterator<T> {
  Iterator<Iterator<T>> nestedIter;
  Iterator<T> current;

  ComposedIter(Iterator<Iterator<T>> nestedIter) {
    this.nestedIter = nestedIter;
    this.current = new MtIter<>();
  }

  @Override
  public boolean hasNext() {
    return current.hasNext() || nestedIter.hasNext();
  }

  @Override
  public T next() {
    if (!this.hasNext()) {
      throw new RuntimeException("Iterator does not have a next element");
    }
    while (!this.current.hasNext()) {
      if (!this.nestedIter.hasNext()) {
        throw new RuntimeException("Iterator does not have a next element");
      }
      this.current = this.nestedIter.next();
    }
    return this.current.next();
  }
}

// Utils ------------------------------------------------------------------------------------------

class ArrayUtils {
  // returns a new ArrayList with the order of the items of the passed in ArrayList reversed
  public <T> ArrayList<T> reverse(ArrayList<T> arr) {
    ArrayList<T> newArr = new ArrayList<T>();
    for (int i = arr.size() - 1; i >= 0; i -= 1) {
      newArr.add(arr.get(i));
    }
    return newArr;
  }
}

// Examples ---------------------------------------------------------------------------------------

class ExamplesKnitting {
  IStitch k = new Knit();
  IStitch p = new Purl();

  KnittedRow krMt = new KnittedRow(new ArrayList<>());
  KnittedRow kr1 = new KnittedRow(
      new ArrayList<>(List.of(k, k, k, p, p, p, p, k))
  );
  KnittedRow kr2 = new KnittedRow(
      new ArrayList<>(List.of(p, k, p, p, k, k, p, p))
  );
  KnittedRow kr1Rev = new KnittedRow(
      new ArrayList<>(List.of(p, k, k, k, k, p, p, p))
  );
  KnittedRow kr2Rev = new KnittedRow(
      new ArrayList<>(List.of(k, k, p, p, k, k, p, k))
  );

  KnittedFabric kfMt = new KnittedFabric(new ArrayList<>());
  KnittedFabric kf1 = new KnittedFabric(
      new ArrayList<>(List.of(kr1))
  );
  KnittedFabric kf2 = new KnittedFabric(
      new ArrayList<>(List.of(kr1, kr2))
  );
  KnittedFabric kf3 = new KnittedFabric(
      new ArrayList<>(List.of(kr2, kr1))
  );
  KnittedFabric kf1Rev = new KnittedFabric(
      new ArrayList<>(List.of(kr1Rev))
  );
  KnittedFabric kf2Rev = new KnittedFabric(
      new ArrayList<>(List.of(kr1Rev, kr2Rev))
  );

  // KnittedFabric tests --------------------------------------------------------------------------

  void testKnittedFabricConstructor(Tester t) {
    // empty constructor
    t.checkExpect(new KnittedFabric(), kfMt);
  }

  void testKnittedFabricAddRow(Tester t) {
    KnittedFabric kf = new KnittedFabric();
    t.checkExpect(
        kf,
        kfMt
    );
    kf.addRow(List.of(k, k, k, p, p, p, p, k).iterator());
    t.checkExpect(
        kf,
        kf1
    );
    kf.addRow(List.of(p, k, p, p, k, k, p, p).iterator());
    t.checkExpect(
        kf,
        kf2
    );

    // chained `addRow`s
    t.checkExpect(
        new KnittedFabric()
            .addRow(List.of(k, k, k, p, p, p, p, k).iterator())
            .addRow(List.of(p, k, p, p, k, k, p, p).iterator()),
        kf2
    );
  }

  void testKnittedFabricReversed(Tester t) {
    t.checkExpect(
        kfMt.reversed(),
        kfMt
    );
    t.checkExpect(
        kf1.reversed(),
        kf1Rev
    );
    t.checkExpect(
        kf2.reversed(),
        kf2Rev
    );
    t.checkExpect(kf1.reversed().reversed(), kf1);
  }

  void testKnittedFabricsSameRows(Tester t) {
    t.checkExpect(kfMt.sameRows(kfMt), true);
    t.checkExpect(kf1.sameRows(kf1), true);
    t.checkExpect(kf1.sameRows(
        new KnittedFabric(
            new ArrayList<>(List.of(kr1, kr2))
        )
    ), true);
    t.checkExpect(kf1.sameRows(kf1Rev), false);
    t.checkExpect(kf2.sameRows(kf2Rev), false);
  }

  void testKnittedFabricSameFabric(Tester t) {
    t.checkExpect(kfMt.sameFabric(kfMt), true);
    t.checkExpect(kf1.sameFabric(kf1), true);
    t.checkExpect(kf1.sameFabric(kf1Rev), true);
    t.checkExpect(kf1.sameFabric(kf2), false);
    t.checkExpect(kf2.sameFabric(kf1), false);
    t.checkExpect(kf2.sameFabric(kf2Rev), true);
    t.checkExpect(kf2Rev.sameFabric(kf2), true);
  }

  void testKnittedFabricRenderFabric(Tester t) {
    t.checkExpect(kfMt.renderFabric(), "");
    t.checkExpect(
        kf1.renderFabric(),
        "VVV----V\n"
    );
    t.checkExpect(
        kf2.renderFabric(),
        "VVV----V\n"
        + "-V--VV--\n"
    );
  }

  // KnittedRow tests -----------------------------------------------------------------------------

  void testKnittedRowConstructor(Tester t) {
    t.checkExpect(new KnittedRow(new MtIter<>()), krMt);
    t.checkExpect(new KnittedRow(List.of(k, k, k, p, p, p, p, k).iterator()), kr1);
  }

  void testKnittedRowRenderRow(Tester t) {
    t.checkExpect(krMt.renderRow(), "");
    t.checkExpect(kr1.renderRow(), "VVV----V");
    t.checkExpect(kr2.renderRow(), "-V--VV--");
  }

  void testKnittedRowReversed(Tester t) {
    t.checkExpect(krMt.reversed(), krMt);
    t.checkExpect(kr1.reversed(), kr1Rev);
    t.checkExpect(kr2.reversed(), kr2Rev);
  }

  void testKnittedRowSameRow(Tester t) {
    t.checkExpect(krMt.sameRow(krMt), true);
    t.checkExpect(kr1.sameRow(kr1), true);
    t.checkExpect(kr1.sameRow(
        new KnittedRow(List.of(k, k, k, p, p, p, p, k).iterator())
    ), true);
    t.checkExpect(kr1.sameRow(kr2), false);
    // sameRow does not consider a row the same as its flipped version
    t.checkExpect(kr1.sameRow(kr1Rev), false);
  }

  // IStitch tests --------------------------------------------------------------------------------

  void testRenderStitch(Tester t) {
    t.checkExpect(k.renderStitch(), "V");
    t.checkExpect(p.renderStitch(), "-");
  }

  void testReversed(Tester t) {
    t.checkExpect(k.reversed(), p);
    t.checkExpect(p.reversed(), k);
  }

  void testSameStitch(Tester t) {
    t.checkExpect(k.sameStitch(k), true);
    t.checkExpect(k.sameStitch(new Knit()), true);
    t.checkExpect(k.sameStitch(p), false);
    t.checkExpect(p.sameStitch(p), true);
    t.checkExpect(p.sameStitch(new Purl()), true);
    t.checkExpect(p.sameStitch(k), false);
  }

  // ArrayUtils tests -----------------------------------------------------------------------------

  void testReverse(Tester t) {
    ArrayUtils utils = new ArrayUtils();
    ArrayList<Integer> mt = new ArrayList<>();
    ArrayList<Integer> nums = new ArrayList<>(List.of(1, 2, 3));
    t.checkExpect(utils.reverse(nums), new ArrayList<>(List.of(3, 2, 1)));
    t.checkExpect(utils.reverse(mt), mt);
  }
}