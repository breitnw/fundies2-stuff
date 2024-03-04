import tester.*; // The tester library

import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.impworld.*;      // the abstract World class and the big-bang library for imperative worlds

import java.awt.Color;
// general colors (as triples of red,green,blue values)
// and predefined colors (Red, Green, Yellow, Blue, Black, White)
import java.util.ArrayList;
import java.util.List;


interface ICell {
    // gets the state of this ICell
    int getState();

    // render this ICell as an image of a rectangle with this width and height
//    WorldImage render(int width, int height);

    // produces the child cell of this ICell with the given left and right neighbors
    ICell childCell(ICell left, ICell right);
}

class InertCell implements ICell {
    public int getState() {
        return 0;
    }

    public ICell childCell(ICell left, ICell right) {
        return new InertCell();
    }
}

class Utils {
    // generates the index of a truth table as an ArrayList
    public ArrayList<ArrayList<Integer>> generateMatrix(int dimensions, int depth) {
        ArrayList<ArrayList<Integer>> mat = new ArrayList<>();

        for (int i = (int)(Math.pow(depth, dimensions) - 1); i >= 0; i -= 1) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = dimensions - 1; j >= 0; j -= 1) {
                int val = (i / (int)Math.pow(depth, j)) % depth;
                row.add(val);
            }
            mat.add(row);
        }

        return mat;
    }
}

// hardcoded dimension 3 depth 2 cell
abstract class ACell implements ICell {
    static final ArrayList<ArrayList<Integer>> ruleMatrix = new Utils().generateMatrix(3, 2);



    int state;
    ArrayList<Integer> ruleVec;

    ACell(int state, int ruleNum) {
        this.state = state;
        this.ruleVec = new ArrayList<>();

        for (int i = 7; i >= 0; i -= 1) {
            this.ruleVec.add((int) (ruleNum / (Math.pow(2, i))));
        }
    }


    public int getState() {
        return this.state;
    }

    public int nextState(int left, int right) {
        if (this.ruleMatrix.get(0).size() != 3) {
            throw new IllegalStateException("Invalid ruleMatrix");
        }
        ArrayList<Integer> vec = new ArrayList<>(List.of(left, this.getState(), right));
        for (int i = 0; i < this.ruleMatrix.size(); i += 1) {
            ArrayList<Integer> curVec = this.ruleMatrix.get(i);
            boolean same = true;
            for (int j = 0; j < curVec.size(); j += 1) {
                same = same && curVec.get(j).equals(vec.get(j));
            }
            if (same) {
                return this.ruleVec.get(i);
            }
        }
        throw new RuntimeException("Invalid state found");
    }


}

class Rule60 extends ACell {
    Rule60(int state) {
        super(state, 60);
    }
    public ICell childCell(ICell left, ICell right) {
        return new Rule60(this.nextState(left.getState(), right.getState()));
    }
}



