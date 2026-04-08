In technical terms, it’s a structural design pattern that lets you attach new behaviors to objects by placing these objects inside special wrapper objects that contain the behaviors.

Usually, when you want to alter an object's behavior, you think of **inheritance**. But inheritance has a few "gotchas":

- **It's static:** You can’t change behavior at runtime.
    
- **Class Explosion:** If you have many optional features, you end up with a mess of subclasses like `CoffeeWithMilkAndSugarAndCaramel`.
    

The Decorator pattern favors **composition** over inheritance, allowing you to stack behaviors like LEGO bricks.

# 1 Core Components


The pattern relies on four key components:

1. **Component (Interface):** The base interface for both the real object and the decorators.
    
2. **Concrete Component:** The basic object you’re adding stuff to (e.g., a Plain Coffee).
    
3. **Base Decorator:** A class that implements the Component interface and holds a reference to a Component object.
    
4. **Concrete Decorators:** The actual "wrappers" that add specific features (e.g., MilkDecorator, SugarDecorator).

# 2 Structure

![Structure of the Decorator design pattern](https://refactoring.guru/images/patterns/diagrams/decorator/structure-indexed.png)

1. The **Component** declares the common interface for both wrappers and wrapped objects.
    
2. **Concrete Component** is a class of objects being wrapped. It defines the basic behavior, which can be altered by decorators.
    
3. The **Base Decorator** class has a field for referencing a wrapped object. The field’s type should be declared as the component interface so it can contain both concrete components and decorators. The base decorator delegates all operations to the wrapped object.
    
4. **Concrete Decorators** define extra behaviors that can be added to components dynamically. Concrete decorators override methods of the base decorator and execute their behavior either before or after calling the parent method.
    
5. The **Client** can wrap components in multiple layers of decorators, as long as it works with all objects via the component interface.

# 3 Applicability

 Use the Decorator pattern when you need to be able to assign extra behaviors to objects at runtime without breaking the code that uses these objects.

 The Decorator lets you structure your business logic into layers, create a decorator for each layer and compose objects with various combinations of this logic at runtime. The client code can treat all these objects in the same way, since they all follow a common interface.

 Use the pattern when it’s awkward or not possible to extend an object’s behavior using inheritance.

 Many programming languages have the `final` keyword that can be used to prevent further extension of a class. For a final class, the only way to reuse the existing behavior would be to wrap the class with your own wrapper, using the Decorator pattern.

# 4  How to Implement

1. Make sure your business domain can be represented as a primary component with multiple optional layers over it.
    
2. Figure out what methods are common to both the primary component and the optional layers. Create a component interface and declare those methods there.
    
3. Create a concrete component class and define the base behavior in it.
    
4. Create a base decorator class. It should have a field for storing a reference to a wrapped object. The field should be declared with the component interface type to allow linking to concrete components as well as decorators. The base decorator must delegate all work to the wrapped object.
    
5. Make sure all classes implement the component interface.
    
6. Create concrete decorators by extending them from the base decorator. A concrete decorator must execute its behavior before or after the call to the parent method (which always delegates to the wrapped object).
    
7. The client code must be responsible for creating decorators and composing them in the way the client needs.
    

# 5  Pros and Cons

-  You can extend an object’s behavior without making a new subclass.
-  You can add or remove responsibilities from an object at runtime.
-  You can combine several behaviors by wrapping an object into multiple decorators.
-  _Single Responsibility Principle_. You can divide a monolithic class that implements many possible variants of behavior into several smaller classes.

-  It’s hard to remove a specific wrapper from the wrappers stack.
-  It’s hard to implement a decorator in such a way that its behavior doesn’t depend on the order in the decorators stack.
-  The initial configuration code of layers might look pretty ugly.

# 6  Relations with Other Patterns

- [Adapter](https://refactoring.guru/design-patterns/adapter) provides a completely different interface for accessing an existing object. On the other hand, with the [Decorator](https://refactoring.guru/design-patterns/decorator) pattern the interface either stays the same or gets extended. In addition, _Decorator_ supports recursive composition, which isn’t possible when you use _Adapter_.
    
- With [Adapter](https://refactoring.guru/design-patterns/adapter) you access an existing object via different interface. With [Proxy](https://refactoring.guru/design-patterns/proxy), the interface stays the same. With [Decorator](https://refactoring.guru/design-patterns/decorator) you access the object via an enhanced interface.
    
- [Chain of Responsibility](https://refactoring.guru/design-patterns/chain-of-responsibility) and [Decorator](https://refactoring.guru/design-patterns/decorator) have very similar class structures. Both patterns rely on recursive composition to pass the execution through a series of objects. However, there are several crucial differences.
    
    The _CoR_ handlers can execute arbitrary operations independently of each other. They can also stop passing the request further at any point. On the other hand, various _Decorators_ can extend the object’s behavior while keeping it consistent with the base interface. In addition, decorators aren’t allowed to break the flow of the request.
    
- [Composite](https://refactoring.guru/design-patterns/composite) and [Decorator](https://refactoring.guru/design-patterns/decorator) have similar structure diagrams since both rely on recursive composition to organize an open-ended number of objects.
    
    A _Decorator_ is like a _Composite_ but only has one child component. There’s another significant difference: _Decorator_ adds additional responsibilities to the wrapped object, while _Composite_ just “sums up” its children’s results.
    
    However, the patterns can also cooperate: you can use _Decorator_ to extend the behavior of a specific object in the _Composite_ tree.
    
- Designs that make heavy use of [Composite](https://refactoring.guru/design-patterns/composite) and [Decorator](https://refactoring.guru/design-patterns/decorator) can often benefit from using [Prototype](https://refactoring.guru/design-patterns/prototype). Applying the pattern lets you clone complex structures instead of re-constructing them from scratch.
    
- [Decorator](https://refactoring.guru/design-patterns/decorator) lets you change the skin of an object, while [Strategy](https://refactoring.guru/design-patterns/strategy) lets you change the guts.
    
- [Decorator](https://refactoring.guru/design-patterns/decorator) and [Proxy](https://refactoring.guru/design-patterns/proxy) have similar structures, but very different intents. Both patterns are built on the composition principle, where one object is supposed to delegate some of the work to another. The difference is that a _Proxy_ usually manages the life cycle of its service object on its own, whereas the composition of _Decorators_ is always controlled by the client.

# 7 Java Code

**Usage examples:** The Decorator is pretty standard in Java code, especially in code related to streams.

Here are some examples of Decorator in core Java libraries:

- All subclasses of [`java.io.InputStream`](http://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html), [`OutputStream`](http://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html), [`Reader`](http://docs.oracle.com/javase/8/docs/api/java/io/Reader.html) and [`Writer`](http://docs.oracle.com/javase/8/docs/api/java/io/Writer.html) have constructors that accept objects of their own type.
    
- [`java.util.Collections`](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html), methods [`checkedXXX()`](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#checkedCollection-java.util.Collection-java.lang.Class-), [`synchronizedXXX()`](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#synchronizedCollection-java.util.Collection-) and [`unmodifiableXXX()`](http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#unmodifiableCollection-java.util.Collection-).
    
- [`javax.servlet.http.HttpServletRequestWrapper`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServletRequestWrapper.html) and [`HttpServletResponseWrapper`](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServletResponseWrapper.html)
    

**Identification:** Decorator can be recognized by creation methods or constructors that accept objects of the same class or interface as a current class.

## 7.1 `DataSource` 

In this example, the **Decorator** pattern lets you compress and encrypt sensitive data independently from the code that actually uses this data.

![Structure of the Decorator pattern example](https://refactoring.guru/images/patterns/diagrams/decorator/example.png)

The encryption and compression decorators example.

### 7.1.1 `DataSource` interface (component)

```Java
public interface DataSource {
    void writeData(String data);

    String readData();
}
```

### 7.1.2 `FileDataSource` implementation (concrete component)

```Java
public class FileDataSource implements DataSource {
    private String name;

    public FileDataSource(String name) {
        this.name = name;
    }

    @Override
    public void writeData(String data) {
        File file = new File(name);
        try (OutputStream fos = new FileOutputStream(file)) {
            fos.write(data.getBytes(), 0, data.length());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String readData() {
        char[] buffer = null;
        File file = new File(name);
        try (FileReader reader = new FileReader(file)) {
            buffer = new char[(int) file.length()];
            reader.read(buffer);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return new String(buffer);
    }
}
```

### 7.1.3 `DataSourceDecorator` abstract class (Base Decorator)

```Java
package refactoring_guru.decorator.example.decorators;

public abstract class DataSourceDecorator implements DataSource {
    private DataSource wrappee;

    DataSourceDecorator(DataSource source) {
        this.wrappee = source;
    }

    @Override
    public void writeData(String data) {
        wrappee.writeData(data);
    }

    @Override
    public String readData() {
        return wrappee.readData();
    }
}
```

### 7.1.4 `EncryptionDecorator` (Concrete Decorator)

```Java
public class EncryptionDecorator extends DataSourceDecorator {

    public EncryptionDecorator(DataSource source) {
        super(source);
    }

    @Override
    public void writeData(String data) {
        super.writeData(encode(data));
    }

    @Override
    public String readData() {
        return decode(super.readData());
    }

    private String encode(String data) {
        byte[] result = data.getBytes();
        for (int i = 0; i < result.length; i++) {
            result[i] += (byte) 1;
        }
        return Base64.getEncoder().encodeToString(result);
    }

    private String decode(String data) {
        byte[] result = Base64.getDecoder().decode(data);
        for (int i = 0; i < result.length; i++) {
            result[i] -= (byte) 1;
        }
        return new String(result);
    }
}
```

### 7.1.5 `CompressionDecorator` (Concrete Decorator)

```Java
public class CompressionDecorator extends DataSourceDecorator {
    private int compLevel = 6;

    public CompressionDecorator(DataSource source) {
        super(source);
    }

    public int getCompressionLevel() {
        return compLevel;
    }

    public void setCompressionLevel(int value) {
        compLevel = value;
    }

    @Override
    public void writeData(String data) {
        super.writeData(compress(data));
    }

    @Override
    public String readData() {
        return decompress(super.readData());
    }

    private String compress(String stringData) {
        byte[] data = stringData.getBytes();
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
            DeflaterOutputStream dos = new DeflaterOutputStream(bout, new Deflater(compLevel));
            dos.write(data);
            dos.close();
            bout.close();
            return Base64.getEncoder().encodeToString(bout.toByteArray());
        } catch (IOException ex) {
            return null;
        }
    }

    private String decompress(String stringData) {
        byte[] data = Base64.getDecoder().decode(stringData);
        try {
            InputStream in = new ByteArrayInputStream(data);
            InflaterInputStream iin = new InflaterInputStream(in);
            ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
            int b;
            while ((b = iin.read()) != -1) {
                bout.write(b);
            }
            in.close();
            iin.close();
            bout.close();
            return new String(bout.toByteArray());
        } catch (IOException ex) {
            return null;
        }
    }
}
```

### 7.1.6 `Main`

```Java
public class MainCoR {
    public static void main(String[] args) {
        String salaryRecords = "Name,Salary\nJohn Smith,100000\nSteven Jobs,912000";
        DataSourceDecorator encoded = new CompressionDecorator(
                                         new EncryptionDecorator(
                                             new FileDataSource("out/OutputDemo.txt")));
        encoded.writeData(salaryRecords);
        DataSource plain = new FileDataSource("out/OutputDemo.txt");

        System.out.println("- Input ----------------");
        System.out.println(salaryRecords);
        System.out.println("- Encoded --------------");
        System.out.println(plain.readData());
        System.out.println("- Decoded --------------");
        System.out.println(encoded.readData());
    }
}
```