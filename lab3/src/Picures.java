interface IPicture {
  double getWidth();
}


class Combo implements IPicture {
  String name;
  IOperation operation;
  
  Combo(String name, IOperation operation) {
    this.name = name;
    this.operation = operation;
  }
  
  double getWidth() {
    return this.operation.getWidth();
  }
}

interface IOperation {
  double getWidth();
}

class Scale implements IOperation {
  IPicture pic;
  
  Scale(IPicture pic) {
    this.pic = pic;
  }
  
  public double getWidth() {
    return this.pic.getWidth() * 2;
  }
}

class Beside implements IOperation {
  IPicture pic1;
  IPicture pic2;
  
  Beside(IPicture pic1, IPicture pic2) {
    this.pic1 = pic1;
    this.pic2 = pic2;
  }
  
  public double getWidth() {
    return this.pic1.getWidth() + this.pic2.getWidth();
  }
}

class Overlay implements IOperation {
  IPicture topPicture;
  IPicture bottomPicture;
  
  Overlay(IPicture topPicture, IPicture bottomPicture) {
    this.topPicture = topPicture;
    this.bottomPicture = bottomPicture;
  }
  
  public double getWidth() {
    return Math.max(this.topPicture.getWidth(), this.bottomPicture.getWidth());
  }
}


class Shape implements IPicture {
  String kind;
  double size;
  
  Shape(String kind, double size) {
    this.kind = kind;
    this.size = size;
  }
  
  public double getWidth() {
    return this.size;
  }
}

class ExamplesPicture {
  IPicture circle = new Shape("circle", 1);
  IPicture square = new Shape("square", 1);
  IPicture bigCircle = new Combo("Big Circle", new Scale(circle));
  IPicture squircle = new Combo("Squircle", new Overlay(square, bigCircle));
  IPicture geometry = new Combo("Geometry", new Beside(squircle, squircle));
}