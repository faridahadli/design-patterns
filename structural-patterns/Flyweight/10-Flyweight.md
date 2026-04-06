# 1 Core Concept

The primary goal is to **conserve memory** when your application needs to generate a massive number of similar objects. Instead of each object carrying all its data, you split the data into two parts:

- **Intrinsic State:** Data that is constant, shared, and independent of the context (e.g., the shape of a character in a font).
    
- **Extrinsic State:** Data that varies depending on the context (e.g., the position or color of that character on a specific page).
    

By storing the intrinsic state once and passing the extrinsic state in when needed, you can represent thousands of objects 

The pattern typically involves four key components:

1. **Flyweight:** An interface or abstract class that defines how the flyweight receives and uses the extrinsic state.
    
2. **Concrete Flyweight:** Implements the Flyweight interface and stores the **intrinsic state**. ***This object must be shareable and immutable.***
    
3. **Flyweight Factory:** Manages the flyweight objects. It ensures that flyweights are shared properly; when a client requests a flyweight, the factory either returns an existing one or creates a new one if it doesn't exist.
    
4. **Client:** Maintains the **extrinsic state** and calculates or stores where and when to apply the flyweights.

# 2 Structure

![[Pasted image 20260406141112.png]]

1. The Flyweight pattern is merely an optimization. Before applying it, make sure your program does have the RAM consumption problem related to having a massive number of similar objects in memory at the same time. Make sure that this problem can’t be solved in any other meaningful way.
    
2. The **Flyweight** class contains the portion of the original object’s state that can be shared between multiple objects. The same flyweight object can be used in many different contexts. The state stored inside a flyweight is called _intrinsic._ The state passed to the flyweight’s methods is called _extrinsic._
    
3. The **Context** class contains the extrinsic state, unique across all original objects. When a context is paired with one of the flyweight objects, it represents the full state of the original object.
    
4. Usually, the behavior of the original object remains in the flyweight class. In this case, whoever calls a flyweight’s method must also pass appropriate bits of the extrinsic state into the method’s parameters. On the other hand, the behavior can be moved to the context class, which would use the linked flyweight merely as a data object.
    
5. The **Client** calculates or stores the extrinsic state of flyweights. From the client’s perspective, a flyweight is a template object which can be configured at runtime by passing some contextual data into parameters of its methods.
    
6. The **Flyweight Factory** manages a pool of existing flyweights. With the factory, clients don’t create flyweights directly. Instead, they call the factory, passing it bits of the intrinsic state of the desired flyweight. The factory looks over previously created flyweights and either returns an existing one that matches search criteria or creates a new one if nothing is found.
# 3 Applicability

 Use the Flyweight pattern only when your program must support a huge number of objects which barely fit into available RAM.

 The benefit of applying the pattern depends heavily on how and where it’s used. It’s most useful when:

- an application needs to spawn a huge number of similar objects
- this drains all available RAM on a target device
- the objects contain duplicate states which can be extracted and shared between multiple objects

# 4  How to Implement

1. Divide fields of a class that will become a flyweight into two parts:
    - the intrinsic state: the fields that contain unchanging data duplicated across many objects
    - the extrinsic state: the fields that contain contextual data unique to each object
2. Leave the fields that represent the intrinsic state in the class, but make sure they’re immutable. They should take their initial values only inside the constructor.
    
3. Go over methods that use fields of the extrinsic state. For each field used in the method, introduce a new parameter and use it instead of the field.
    
4. Optionally, create a factory class to manage the pool of flyweights. It should check for an existing flyweight before creating a new one. Once the factory is in place, clients must only request flyweights through it. They should describe the desired flyweight by passing its intrinsic state to the factory.
    
5. The client must store or calculate values of the extrinsic state (context) to be able to call methods of flyweight objects. For the sake of convenience, the extrinsic state along with the flyweight-referencing field may be moved to a separate context class.
    

# 5  Pros and Cons

Pros:
-  You can save lots of RAM, assuming your program has tons of similar objects.
Cons:
-  You might be trading RAM over CPU cycles when some of the context data needs to be recalculated each time somebody calls a flyweight method.
-  The code becomes much more complicated. New team members will always be wondering why the state of an entity was separated in such a way.

## 5.1  Relations with Other Patterns

- You can implement shared leaf nodes of the [Composite](https://refactoring.guru/design-patterns/composite) tree as [Flyweights](https://refactoring.guru/design-patterns/flyweight) to save some RAM.
    
- [Flyweight](https://refactoring.guru/design-patterns/flyweight) shows how to make lots of little objects, whereas [Facade](https://refactoring.guru/design-patterns/facade) shows how to make a single object that represents an entire subsystem.
    
- [Flyweight](https://refactoring.guru/design-patterns/flyweight) would resemble [Singleton](https://refactoring.guru/design-patterns/singleton) if you somehow managed to reduce all shared states of the objects to just one flyweight object. But there are two fundamental differences between these patterns:
    1. There should be only one Singleton instance, whereas a _Flyweight_ class can have multiple instances with different intrinsic states.
    2. The _Singleton_ object can be mutable. Flyweight objects are immutable.

# 6 Examples in Java

### 6.1.1 **Java Standard Library (JDK)**

Java uses the Flyweight pattern in several core areas to prevent unnecessary object allocation:

- **`java.lang.Integer` (and other Wrappers):** The `Integer.valueOf(int)` method caches integers in the range of **-128 to 127**. When you call `Integer a = 10;`, Java returns a cached instance rather than creating a new object on the heap.
    
- **`java.lang.String`:** The **String Constant Pool** is a classic Flyweight implementation. When you define a string literal like `"Hello"`, the JVM checks the pool. If it exists, it returns a reference to the existing object.
    
- **`java.math.BigDecimal`:** Common values like `BigDecimal.ZERO`, `ONE`, and `TEN` are pre-instantiated and reused.
    

### 6.1.2 **Spring Framework**

Spring applies this pattern to handle configuration and infrastructure efficiency:

- **Bean Definitions:** While individual bean _instances_ can be many, the `BeanDefinition` objects (the metadata describing how to create a bean) act as flyweights. Spring doesn't recreate the metadata every time it needs to inject a prototype bean; it refers to a shared definition.
    
- **Cache Management:** Spring’s `@Cacheable` abstraction often uses flyweight-like behavior by ensuring that frequently accessed data is stored once and shared across multiple requests or sessions, rather than re-calculating or re-fetching it.

|**Feature**|**L1 Cache**|**L2 Cache**|
|---|---|---|
|**Scope**|Session (Single Thread)|SessionFactory (Application-wide)|
|**Availability**|Enabled by default (Mandatory)|Disabled by default (Optional)|
|**Flyweight Nature**|Shares object references within a unit of work.|Shares raw data across the entire application.|
|**Lifecycle**|Dies when `session.close()` is called.|Lives as long as the application is running.|

## 6.2 Java Code

In this example, the **Flyweight** pattern helps to reduce memory usage when rendering millions of tree objects on a canvas.

![Flyweight pattern example](https://refactoring.guru/images/patterns/diagrams/flyweight/example.png?id=0818d078c1a79f373e96397f37b7ee06)

The pattern extracts the repeating intrinsic state from a main `Tree` class and moves it into the flyweight class `TreeType`.

Now instead of storing the same data in multiple objects, it’s kept in just a few flyweight objects and linked to appropriate `Tree` objects which act as contexts. The client code creates new tree objects using the flyweight factory, which encapsulates the complexity of searching for the right object and reusing it if needed.

## 6.3 `Tree` class (Context)

```Java
public class Tree {  
    private int x;  
    private int y;  
    private TreeType type;  
  
    public Tree(int x, int y, TreeType type) {  
        this.x = x;  
        this.y = y;  
        this.type = type;  
    }  
  
    public void draw(Graphics g) {  
        type.draw(g, x, y);  
    }  
}
```

## 6.4 `TreeType` class (Flyweight)

needs to be immutable

```Java
public class TreeType {  
    private final String name;  
    private final Color color;  
    private final String otherTreeData;  
  
    public TreeType(String name, Color color, String otherTreeData) {  
        this.name = name;  
        this.color = color;  
        this.otherTreeData = otherTreeData;  
    }  
  
    public void draw(Graphics g, int x, int y) {  
        g.setColor(Color.BLACK);  
        g.fillRect(x - 1, y, 3, 5);  
        g.setColor(color);  
        g.fillOval(x - 5, y - 10, 10, 10);  
    }  
}
```

## 6.5 `TreeTypeFactory`  class (Flyweight factory)

```Java
static Map<String, TreeType> treeTypes = new HashMap<>();
    public static TreeType getTreeType(String name, Color color, String otherTreeData) {
        TreeType result = treeTypes.get(name);
        if (result == null) {
            result = new TreeType(name, color, otherTreeData);
            treeTypes.put(name, result);
        }
        return result;
    }
```

## 6.6 `Forest` class (Client)

```Java
public class Forest extends JFrame {  
    private List<Tree> trees = new ArrayList<>();  
  
    public void plantTree(int x, int y, String name, Color color, String otherTreeData) {  
        TreeType type = TreeTypeFactory.getTreeType(name, color, otherTreeData);  
        Tree tree = new Tree(x, y, type);  
        trees.add(tree);  
    }  
  
    @Override  
    public void paint(Graphics graphics) {  
        for (Tree tree : trees) {  
            tree.draw(graphics);  
        }  
    }  
}
```

