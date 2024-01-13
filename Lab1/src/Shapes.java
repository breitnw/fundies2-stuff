import tester.*;

// to represent a geometric shape
interface IShape { }

// to represent a circle
class Circle implements IShape {
    int x;
    int y;
    int rad;

    Circle(int x, int y, int rad) {
        this.x = x;
        this.y = y;
        this.rad = rad;
    }
}

// to represent a rectangle
class Rect implements IShape {
    int x;
    int y;
    int w;
    int h;

    Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}

// to represent a combined shape
class Combo implements IShape {
    IShape top;
    IShape bot;

    Combo(IShape top, IShape bot) {
        this.top = top;
        this.bot = bot;
    }
}


// to represent examples and tests for shapes
class ExamplesShapes {
    IShape circle = new Circle(50, 50, 50);
    IShape rleft = new Rect(20, 20, 20, 20);
    IShape rBot = new Rect(20, 60, 60, 20);

    IShape addMouth = new Combo(this.rBot, this.circle);
    IShape addLeftEye = new Combo(this.rleft, this.addMouth);
    IShape face = new Combo(new Rect(60, 20, 20, 20), this.addLeftEye);

    // Car example
    IShape background = new Rect(0, 0, 100, 100);
    IShape hood = new Rect(20, 40, 60, 20);
    IShape body = new Rect(0, 60, 100, 20);
    IShape leftWheel = new Circle(20, 90, 10);
    IShape rightWheel = new Circle(80, 90, 10);

    IShape placeHood = new Combo(hood, background);
    IShape placeBody = new Combo(body, placeHood);
    IShape placeL = new Combo(leftWheel, placeBody);
    IShape car = new Combo(rightWheel, placeL);
}