# 1 The Core Concept

The goal is to create a **class hierarchy** where "Part" objects and "Whole" objects (collections of parts) implement the same interface. This allows clients to ignore the difference between individual objects and compositions.

### 1.1.1 The Key Players

- **component:** An interface or abstract class that defines the operations common to both simple and complex objects (e.g., `execute()` or `getPrice()`).
    
- **Leaf:** The basic building block. It implements the component interface but has no "children." It does the actual work.
    
- **Composite:** A container that stores child components (Leaves or other Composites). It implements the component methods by typically delegating the work to its children.

# 2 Structure

![Structure of the Composite design pattern](https://refactoring.guru/images/patterns/diagrams/composite/structure-en.png)

1. The **component** interface describes operations that are common to both simple and complex elements of the tree.
    
2. The **Leaf** is a basic element of a tree that doesn’t have sub-elements.
    Usually, leaf components end up doing most of the real work, since they don’t have anyone to delegate the work to.
    
3. The **Container** (aka _composite_) is an element that has sub-elements: leaves or other containers. A container doesn’t know the concrete classes of its children. It works with all sub-elements only via the component interface. 
4. The **Client** works with all elements through the component interface. As a result, the client can work in the same way with both simple or complex elements of the tree.


# 3 How to Implement

1. Make sure that the core model of your app can be represented as a tree structure. Try to break it down into simple elements and containers. Remember that containers must be able to contain both simple elements and other containers.
    
2. Declare the component interface with a list of methods that make sense for both simple and complex components.
    
3. Create a leaf class to represent simple elements. A program may have multiple different leaf classes.
    
4. Create a container class to represent complex elements. In this class, provide an array field for storing references to sub-elements. The array must be able to store both leaves and containers, so make sure it’s declared with the component interface type.
    
    While implementing the methods of the component interface, remember that a container is supposed to be delegating most of the work to sub-elements.
    
5. Finally, define the methods for adding and removal of child elements in the container.
    
    Keep in mind that these operations can be declared in the component interface. This would violate the _Interface Segregation Principle_ because the methods will be empty in the leaf class. However, the client will be able to treat all the elements equally, even when composing the tree.

# 4 Applicability

 - Use the Composite pattern when you have to implement a tree-like object structure.

 - The Composite pattern provides you with two basic element types that share a common interface: simple leaves and complex containers. A container can be composed of both leaves and other containers. This lets you construct a nested recursive object structure that resembles a tree.

 - Use the pattern when you want the client code to treat both simple and complex elements uniformly.

 - All elements defined by the Composite pattern share a common interface. Using this interface, the client doesn’t have to worry about the concrete class of the objects it works with.

## 4.1 Pros and Cons

-  You can work with complex tree structures more conveniently: use polymorphism and recursion to your advantage.
-  _Open/Closed Principle_. You can introduce new element types into the app without breaking the existing code, which now works with the object tree.

-  It might be difficult to provide a common interface for classes whose functionality differs too much. In certain scenarios, you’d need to overgeneralize the component interface, making it harder to comprehend.

# 5  Relations with Other Patterns

- You can use [Builder](https://refactoring.guru/design-patterns/builder) when creating complex [Composite](https://refactoring.guru/design-patterns/composite) trees because you can program its construction steps to work recursively.
    
- [Chain of Responsibility](https://refactoring.guru/design-patterns/chain-of-responsibility) is often used in conjunction with [Composite](https://refactoring.guru/design-patterns/composite). In this case, when a leaf component gets a request, it may pass it through the chain of all of the parent components down to the root of the object tree.
    
- You can use [Iterators](https://refactoring.guru/design-patterns/iterator) to traverse [Composite](https://refactoring.guru/design-patterns/composite) trees.
    
- You can use [Visitor](https://refactoring.guru/design-patterns/visitor) to execute an operation over an entire [Composite](https://refactoring.guru/design-patterns/composite) tree.
    
- You can implement shared leaf nodes of the [Composite](https://refactoring.guru/design-patterns/composite) tree as [Flyweights](https://refactoring.guru/design-patterns/flyweight) to save some RAM.
    
- [Composite](https://refactoring.guru/design-patterns/composite) and [Decorator](https://refactoring.guru/design-patterns/decorator) have similar structure diagrams since both rely on recursive composition to organize an open-ended number of objects.
    
    A _Decorator_ is like a _Composite_ but only has one child component. There’s another significant difference: _Decorator_ adds additional responsibilities to the wrapped object, while _Composite_ just “sums up” its children’s results.
    
    However, the patterns can also cooperate: you can use _Decorator_ to extend the behavior of a specific object in the _Composite_ tree.
    
- Designs that make heavy use of [Composite](https://refactoring.guru/design-patterns/composite) and [Decorator](https://refactoring.guru/design-patterns/decorator) can often benefit from using [Prototype](https://refactoring.guru/design-patterns/prototype). Applying the pattern lets you clone complex structures instead of re-constructing them from scratch.

# 6 Java Code

![[Pasted image 20260401154633.png]]

## 6.1 The component Interface

This defines the contract for both simple and complex objects.

Java

```Java
import java.util.*;

// The component interface
interface Graphic {
    void move(int x, int y);
    void draw();
}
```

---

## 6.2 The Leaf Classes

These are the basic building blocks that do not have children.

Java

```Java
// Leaf class
class Dot implements Graphic {
    protected int x, y;

    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a dot at (" + x + ", " + y + ")");
    }
}

// Another Leaf class (extends Dot as per UML)
class Circle extends Dot {
    private int radius;

    public Circle(int x, int y, int radius) {
        super(x, y);
        this.radius = radius;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a circle at (" + x + ", " + y + ") with radius " + radius);
    }
}
```

---

## 6.3 The Composite Class

This class maintains a collection of children and delegates the work to them.

Java

```Java
// The Composite class
class CompoundGraphic implements Graphic {
    private List<Graphic> children = new ArrayList<>();

    public void add(Graphic child) {
        children.add(child);
    }

    public void remove(Graphic child) {
        children.remove(child);
    }

    @Override
    public void move(int x, int y) {
        for (Graphic child : children) {
            child.move(x, y);
        }
    }

    @Override
    public void draw() {
        System.out.println("--- Group Drawing Start ---");
        for (Graphic child : children) {
            child.draw();
        }
        System.out.println("--- Group Drawing End ---");
    }
}
```

---

## 6.4 The Client (ImageEditor)

The client works with all graphics through the `Graphic` interface, making it agnostic of whether it is dealing with a single dot or a complex group.

Java

```Java
class ImageEditor {
    public static void main(String[] args) {
        // Create individual shapes
        Graphic dot1 = new Dot(10, 10);
        Graphic circle1 = new Circle(20, 20, 5);

        // Create a compound graphic and add shapes
        CompoundGraphic group = new CompoundGraphic();
        group.add(dot1);
        group.add(circle1);

        // Create another compound to show nesting
        CompoundGraphic mainCanvas = new CompoundGraphic();
        mainCanvas.add(group);
        mainCanvas.add(new Dot(50, 50));

        // Move and Draw everything at once
        System.out.println("Initial Draw:");
        mainCanvas.draw();

        System.out.println("\nMoving all graphics by (10, 10)...");
        mainCanvas.move(10, 10);
        
        System.out.println("\nDraw after movement:");
        mainCanvas.draw();
    }
}
```