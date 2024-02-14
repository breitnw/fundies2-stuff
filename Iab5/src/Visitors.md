Sometimes, it is necessary to invoke appropriate methods on pre-defined objects when the class of the object is unknown. For example, we may have an `IList<IShape>`, where an `IShape` might be a circle or square. By utilizing this pattern, library authors can allow for extensibility by those who do not have access to the code. 

```java
interface IShape {
	<T> T accept(IShapeVisitor<T> func);
}

// the interface has to extend IFunc so that we can use it in other 
// places where IFuncs are necessary
interface IShapeVisitor<R> extends IFunc<IShape, R> {
	R visitCircle(Circle c);
	R visitSquare(Square s);
}
```

Then, when the classes are defined, each will call the corresponding function of the provided `IShapeFunc`:

```java
class Circle implements IShape {
	public <T> T accept(IShapeVisitor<T> func) {
		return func.visitCircle(this);
	}
}
```

Finally, we can implement IShapeVisitor on our classes in order to use them in [[Mapping|mappers]] or the like:

```java
class ShapePerim implements IShapeVisitor<Double> {
	// Everything from the IShapeVisitor interface
	// ...
	public Double visitCircle(Circle c) { 
		return Math.PI * c.radius * c.radius; 
	}
    // ...
    
	// Everything from the IFunc interface
	public Double apply(IShape that) {
		return that.accept(this);
	}
}
```