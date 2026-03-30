# 1 Intent

**Adapter** is a structural design pattern that allows objects with incompatible interfaces to collaborate.

# 2 Problem 

Imagine that you’re creating a stock market monitoring app. The app downloads the stock data from multiple sources in XML format and then displays nice-looking charts and diagrams for the user.

At some point, you decide to improve the app by integrating a smart 3rd-party analytics library. But there’s a catch: the analytics library only works with data in JSON format.

![[Pasted image 20260327132525.png|779]]

# 3 Solution

You can create an _adapter_. This is a special object that converts the interface of one object so that another object can understand it.

An adapter wraps one of the objects to hide the complexity of conversion happening behind the scenes. The wrapped object isn’t even aware of the adapter. For example, you can wrap an object that operates in meters and kilometers with an adapter that converts all of the data to imperial units such as feet and miles.

Adapters can not only convert data into various formats but can also help objects with different interfaces collaborate. Here’s how it works:

1. The adapter gets an interface, compatible with one of the existing objects.
2. Using this interface, the existing object can safely call the adapter’s methods.
3. Upon receiving a call, the adapter passes the request to the second object, but in a format and order that the second object expects.
Sometimes it’s even possible to create a two-way adapter that can convert the calls in both directions.

![[Pasted image 20260327133020.png|709]]

Let’s get back to our stock market app. To solve the dilemma of incompatible formats, you can create XML-to-JSON adapters for every class of the analytics library that your code works with directly. Then you adjust your code to communicate with the library only via these adapters. When an adapter receives a call, it translates the incoming XML data into a JSON structure and passes the call to the appropriate methods of a wrapped analytics object.

# 4 Structure


## 4.1 Object adapter

This implementation uses the object composition principle: the adapter implements the interface of one object and wraps the other one. It can be implemented in all popular programming languages.

![Structure of the Adapter design pattern (the object adapter)](https://refactoring.guru/images/patterns/diagrams/adapter/structure-object-adapter.png)

1. The **Client** is a class that contains the existing business logic of the program.
    
2. The **Client Interface** describes a protocol that other classes must follow to be able to collaborate with the client code.
    
3. The **Service** is some useful class (usually 3rd-party or legacy). The client can’t use this class directly because it has an incompatible interface.
    
4. The **Adapter** is a class that’s able to work with both the client and the service: it implements the client interface, while wrapping the service object. The adapter receives calls from the client via the client interface and translates them into calls to the wrapped service object in a format it can understand.
    
5. The client code doesn’t get coupled to the concrete adapter class as long as it works with the adapter via the client interface. Thanks to this, you can introduce new types of adapters into the program without breaking the existing client code. This can be useful when the interface of the service class gets changed or replaced: you can just create a new adapter class without changing the client code.
    

## 4.2 Class adapter

This implementation uses inheritance: the adapter inherits interfaces from both objects at the same time. Note that this approach can only be implemented in programming languages that support multiple inheritance, such as C++.

![Adapter design pattern (class adapter)](https://refactoring.guru/images/patterns/diagrams/adapter/structure-class-adapter.png)

1. The **Class Adapter** doesn’t need to wrap any objects because it inherits behaviors from both the client and the service. The adaptation happens within the overridden methods. The resulting adapter can be used in place of an existing client class.

# 5 Applicability

- Use the Adapter class when you want to use some existing class, but its interface isn’t compatible with the rest of your code.

 - The Adapter pattern lets you create a middle-layer class that serves as a translator between your code and a legacy class, a 3rd-party class or any other class with a weird interface.

 - Use the pattern when you want to reuse several existing subclasses that lack some common functionality that can’t be added to the superclass.

 - You could extend each subclass and put the missing functionality into new child classes. However, you’ll need to duplicate the code across all of these new classes, which [smells really bad](https://refactoring.guru/smells/duplicate-code).

- The much more elegant solution would be to put the missing functionality into an adapter class. Then you would wrap objects with missing features inside the adapter, gaining needed features dynamically. For this to work, the target classes must have a common interface, and the adapter’s field should follow that interface. This approach looks very similar to the [Decorator](https://refactoring.guru/design-patterns/decorator) pattern.

# 6 How to Implement

1. Make sure that you have at least two classes with incompatible interfaces:
    - A useful _service_ class, which you can’t change (often 3rd-party, legacy or with lots of existing dependencies).
    - One or several _client_ classes that would benefit from using the service class.
    
2. Declare the client interface and describe how clients communicate with the service.
    
3. Create the adapter class and make it follow the client interface. Leave all the methods empty for now.
    
4. Add a field to the adapter class to store a reference to the service object. The common practice is to initialize this field via the constructor, but sometimes it’s more convenient to pass it to the adapter when calling its methods.
    
5. One by one, implement all methods of the client interface in the adapter class. The adapter should delegate most of the real work to the service object, handling only the interface or data format conversion.
    
6. Clients should use the adapter via the client interface. This will let you change or extend the adapters without affecting the client code.

# 7 Pros and Cons

-  _Single Responsibility Principle_. You can separate the interface or data conversion code from the primary business logic of the program.
-  _Open/Closed Principle_. You can introduce new types of adapters into the program without breaking the existing client code, as long as they work with the adapters through the client interface.

-  The overall complexity of the code increases because you need to introduce a set of new interfaces and classes. Sometimes it’s simpler just to change the service class so that it matches the rest of your code.

# 8 Relations with Other Patterns

- [Bridge](https://refactoring.guru/design-patterns/bridge) is usually designed up-front, letting you develop parts of an application independently of each other. On the other hand, [Adapter](https://refactoring.guru/design-patterns/adapter) is commonly used with an existing app to make some otherwise-incompatible classes work together nicely.
    
- [Adapter](https://refactoring.guru/design-patterns/adapter) provides a completely different interface for accessing an existing object. On the other hand, with the [Decorator](https://refactoring.guru/design-patterns/decorator) pattern the interface either stays the same or gets extended. In addition, _Decorator_ supports recursive composition, which isn’t possible when you use _Adapter_.
    
- With [Adapter](https://refactoring.guru/design-patterns/adapter) you access an existing object via different interface. With [Proxy](https://refactoring.guru/design-patterns/proxy), the interface stays the same. With [Decorator](https://refactoring.guru/design-patterns/decorator) you access the object via an enhanced interface.
    
- [Facade](https://refactoring.guru/design-patterns/facade) defines a new interface for existing objects, whereas [Adapter](https://refactoring.guru/design-patterns/adapter) tries to make the existing interface usable. _Adapter_ usually wraps just one object, while _Facade_ works with an entire subsystem of objects.
    
- [Bridge](https://refactoring.guru/design-patterns/bridge), [State](https://refactoring.guru/design-patterns/state), [Strategy](https://refactoring.guru/design-patterns/strategy) (and to some degree [Adapter](https://refactoring.guru/design-patterns/adapter)) have very similar structures. Indeed, all of these patterns are based on composition, which is delegating work to other objects. However, they all solve different problems. A pattern isn’t just a recipe for structuring your code in a specific way. It can also communicate to other developers the problem the pattern solves.

# 9 Java Code

## 9.1 Some adapters in Java

- [`java.util.Arrays#asList()`](https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#asList-T...-)
- [`java.util.Collections#list()`](https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#list-java.util.Enumeration-)
- [`java.util.Collections#enumeration()`](https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#enumeration-java.util.Collection-)
- [`java.io.InputStreamReader(InputStream)`](https://docs.oracle.com/javase/8/docs/api/java/io/InputStreamReader.html#InputStreamReader-java.io.InputStream-) (returns a `Reader` object)
- [`java.io.OutputStreamWriter(OutputStream)`](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStreamWriter.html#OutputStreamWriter-java.io.OutputStream-) (returns a `Writer` object)
- [`javax.xml.bind.annotation.adapters.XmlAdapter#marshal()`](https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/annotation/adapters/XmlAdapter.html#marshal-BoundType-) and `#unmarshal()`

## 9.2 Round Hole Round Peg example 
This example of the **Adapter** pattern is based on the classic conflict between square pegs and round holes.

![Structure of the Adapter pattern example](https://refactoring.guru/images/patterns/diagrams/adapter/example.png)



The Adapter pretends to be a round peg, with a radius equal to a half of the square’s diameter (in other words, the radius of the smallest circle that can accommodate the square peg).

### 9.2.1 `RoundPeg.java`

```Java
public class RoundPeg {  
    private double radius;  
  
    public RoundPeg() {}  
  
    public RoundPeg(double radius) {  
        this.radius = radius;  
    }  
  
    public double getRadius() {  
        return radius;  
    }  
}
```

### 9.2.2 `RoundHole.java`

```Java
public class RoundHole {  
    private double radius;  
  
    public RoundHole(double radius) {  
        this.radius = radius;  
    }  
  
    public double getRadius() {  
        return radius;  
    }  
  
    public boolean fits(RoundPeg peg) {  
        boolean result;  
        result = (this.getRadius() >= peg.getRadius());  
        return result;  
    }  
}
```

### 9.2.3 `SquarePeg.Java`

```Java
public class SquarePeg {  
    private double width;  
  
    public SquarePeg(double width) {  
        this.width = width;  
    }  
  
    public double getWidth() {  
        return width;  
    }  
  
    public double getSquare() {  
        double result;  
        result = Math.pow(this.width, 2);  
        return result;  
    }  
}
```

### 9.2.4 `SquarePegAdapter`

```Java
public class SquarePegAdapter extends RoundPeg {  
    private SquarePeg peg;  
  
    public SquarePegAdapter(SquarePeg peg) {  
        this.peg = peg;  
    }  
  
    @Override  
    public double getRadius() {  
        double result;  
        // Calculate a minimum circle radius, which can fit this peg.  
        result = (Math.sqrt(Math.pow((peg.getWidth() / 2), 2) * 2));  
        return result;  
    }  
}
```

