# 1 Core Concept

The **Chain of Responsibility** is a behavioral design pattern that allows you to pass requests along a chain of handlers. Upon receiving a request, each handler decides either to process the request or to pass it to the next handler in the chain.

It’s essentially the software equivalent of "calling customer support"—the first person tries to help you, and if they can't, they escalate you to a specialist, and so on.

The primary goal is **decoupling**.

- **Decouple Senders and Receivers:** The object that sends a request doesn't need to know which specific object will ultimately handle it.
    
- **Flexibility:** You can add, remove, or reorder handlers at runtime without changing the sender's code.
    
- **Single Responsibility:** Each handler focuses on one specific task or condition.

# 2 Structure 

![Structure of the Chain Of Responsibility design pattern](https://refactoring.guru/images/patterns/diagrams/chain-of-responsibility/structure-indexed.png)

1. The **Handler** declares the interface, common for all concrete handlers. It usually contains just a single method for handling requests, but sometimes it may also have another method for setting the next handler on the chain.
    
2. The **Base Handler** is an optional class where you can put the boilerplate code that’s common to all handler classes.
    
    Usually, this class defines a field for storing a reference to the next handler. The clients can build a chain by passing a handler to the constructor or setter of the previous handler. The class may also implement the default handling behavior: it can pass execution to the next handler after checking for its existence.
    
3. **Concrete Handlers** contain the actual code for processing requests. Upon receiving a request, each handler must decide whether to process it and, additionally, whether to pass it along the chain.
    
    Handlers are usually self-contained and immutable, accepting all necessary data just once via the constructor.
    
1. The **Client** may compose chains just once or compose them dynamically, depending on the application’s logic. Note that a request can be sent to any handler in the chain—it doesn’t have to be the first one.


 ***Pure Chain of Responsibility***

In a **Pure** CoR, the request is handled by **exactly one** object in the chain.

- **Mechanism:** Each handler checks if it can process the request. If it can, it processes it and **stops** the chain. The request never reaches the subsequent handlers.
    
- **Key Characteristic:** Only one "link" is responsible for the final outcome.
    

***Impure Chain of Responsibility***

In an **Impure** CoR, multiple handlers (or even all of them) can process the request as it moves down the line.

- **Mechanism:** A handler performs its specific task and then **explicitly forwards** the request to the next handler, regardless of whether it did something or not.
    
- **Key Characteristic:** The request is refined or transformed by multiple objects in the sequence.

# 3 Applicability

 - Use the Chain of Responsibility pattern when your program is expected to process different kinds of requests in various ways, but the exact types of requests and their sequences are unknown beforehand.

 - The pattern lets you link several handlers into one chain and, upon receiving a request, “ask” each handler whether it can process it. This way all handlers get a chance to process the request.

 - Use the pattern when it’s essential to execute several handlers in a particular order.

 - Since you can link the handlers in the chain in any order, all requests will get through the chain exactly as you planned.

 - Use the CoR pattern when the set of handlers and their order are supposed to change at runtime.

 - If you provide setters for a reference field inside the handler classes, you’ll be able to insert, remove or reorder handlers dynamically.

# 4  How to Implement

1. Declare the handler interface and describe the signature of a method for handling requests.
    
    Decide how the client will pass the request data into the method. The most flexible way is to convert the request into an object and pass it to the handling method as an argument.
    
2. To eliminate duplicate boilerplate code in concrete handlers, it might be worth creating an abstract base handler class, derived from the handler interface.
    
    This class should have a field for storing a reference to the next handler in the chain. Consider making the class immutable. However, if you plan to modify chains at runtime, you need to define a setter for altering the value of the reference field.
    
    You can also implement the convenient default behavior for the handling method, which is to forward the request to the next object unless there’s none left. Concrete handlers will be able to use this behavior by calling the parent method.
    
3. One by one create concrete handler subclasses and implement their handling methods. Each handler should make two decisions when receiving a request:
    
    - Whether it’ll process the request.
    - Whether it’ll pass the request along the chain.
4. The client may either assemble chains on its own or receive pre-built chains from other objects. In the latter case, you must implement some factory classes to build chains according to the configuration or environment settings.
    
5. The client may trigger any handler in the chain, not just the first one. The request will be passed along the chain until some handler refuses to pass it further or until it reaches the end of the chain.
    
6. Due to the dynamic nature of the chain, the client should be ready to handle the following scenarios:
    
    - The chain may consist of a single link.
    - Some requests may not reach the end of the chain.
    - Others may reach the end of the chain unhandled.

# 5  Pros and Cons

Pros
-  You can control the order of request handling.
-  _Single Responsibility Principle_. You can decouple classes that invoke operations from classes that perform operations.
-  _Open/Closed Principle_. You can introduce new handlers into the app without breaking the existing client code.
Cons
-  Some requests may end up unhandled.

# 6  Relations with Other Patterns

- [Chain of Responsibility](https://refactoring.guru/design-patterns/chain-of-responsibility), [Command](https://refactoring.guru/design-patterns/command), [Mediator](https://refactoring.guru/design-patterns/mediator) and [Observer](https://refactoring.guru/design-patterns/observer) address various ways of connecting senders and receivers of requests:
    
    - _Chain of Responsibility_ passes a request sequentially along a dynamic chain of potential receivers until one of them handles it.
    - _Command_ establishes unidirectional connections between senders and receivers.
    - _Mediator_ eliminates direct connections between senders and receivers, forcing them to communicate indirectly via a mediator object.
    - _Observer_ lets receivers dynamically subscribe to and unsubscribe from receiving requests.
- [Chain of Responsibility](https://refactoring.guru/design-patterns/chain-of-responsibility) is often used in conjunction with [Composite](https://refactoring.guru/design-patterns/composite). In this case, when a leaf component gets a request, it may pass it through the chain of all of the parent components down to the root of the object tree.
    
- Handlers in [Chain of Responsibility](https://refactoring.guru/design-patterns/chain-of-responsibility) can be implemented as [Commands](https://refactoring.guru/design-patterns/command). In this case, you can execute a lot of different operations over the same context object, represented by a request.
    
    However, there’s another approach, where the request itself is a _Command_ object. In this case, you can execute the same operation in a series of different contexts linked into a chain.
    
- [Chain of Responsibility](https://refactoring.guru/design-patterns/chain-of-responsibility) and [Decorator](https://refactoring.guru/design-patterns/decorator) have very similar class structures. Both patterns rely on recursive composition to pass the execution through a series of objects. However, there are several crucial differences.
    
    The _CoR_ handlers can execute arbitrary operations independently of each other. They can also stop passing the request further at any point. On the other hand, various _Decorators_ can extend the object’s behavior while keeping it consistent with the base interface. In addition, decorators aren’t allowed to break the flow of the request.

# 7 Examples in Java

You likely use this pattern every day without realizing it. It is foundational to how web requests and security are managed.

***A. Jakarta (Java) Servlet Filters***

This is the most "classic" example. When a request hits a web server (like Tomcat), it passes through a `FilterChain`.

- **How it works:** Each `Filter` can inspect the request, modify it, or block it (like a logging filter or an authentication filter). If the filter allows the request, it calls `chain.doFilter(request, response)`, passing it to the next filter.
    

 ***B. Spring Security Filter Chain***

Spring Security is essentially one giant, sophisticated Chain of Responsibility.

- **The Chain:** It uses a `SecurityFilterChain` made of many internal filters.
    
- **Example Handlers:** * `UsernamePasswordAuthenticationFilter`
    
    - `CsrfFilter`
        
    - `BasicAuthenticationFilter`
        
- If your request doesn't have a CSRF token, the `CsrfFilter` handles (rejects) it, and the request never reaches your Controller.
    

***C. Spring WebMvc Exception Handling***

Spring’s `HandlerExceptionResolver` works similarly. When an exception occurs, Spring iterates through a list of resolvers (like `AnnotationMethodHandlerExceptionResolver` or `DefaultHandlerExceptionResolver`) until one of them knows how to map that specific exception to a view or a response body.

***D. Logging (Log4j / Logback)***

Modern logging frameworks often use this pattern for **Appenders**. A log message is sent into a chain where different appenders decide if they should log it based on the level (INFO, ERROR, etc.) and where to send it (Console, File, or Remote Server).

| **Feature**        | **Description**                                                              |
| ------------------ | ---------------------------------------------------------------------------- |
| **Best Used When** | Multiple objects can handle a request, but the handler isn't known a priori. |
| **Java Example**   | `java.util.logging.Handler`, `javax.servlet.Filter`.                         |
| **Spring Example** | `SecurityFilterChain`, `HandlerInterceptor`.                                 |
| **Pros**           | Reduced coupling; dynamic handling.                                          |
| **Cons**           | A request might go unhandled if the chain isn't configured correctly.        |
# 8 Java Code

This example shows how a request containing user data passes a sequential chain of handlers that perform various things such as authentication, authorization, and validation.

This example is a bit different from the canonical version of the pattern given by various authors. Most of the pattern examples are built on the notion of looking for the right handler, launching it and exiting the chain after that. But here we execute every handler until there’s one that **can’t handle** a request. Be aware that this still is the Chain of Responsibility pattern, even though the flow is a bit different.

## 8.1 `Middleware` class (base handler)

```Java
public abstract class Middleware {
    private Middleware next;
  
    public static Middleware link(Middleware first, Middleware... chain) {
        Middleware head = first;
        for (Middleware nextInChain: chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(String email, String password);

    public boolean checkNext(String email, String password) {
        if (next == null) {
            return true;
        }
        return next.check(email, password);
    }
}
```


## 8.2 `RoleCheckMiddleware`  class (concrete handler)

```Java
public class RoleCheckMiddleware extends Middleware {  
    @Override
    public boolean check(String email, String password) {  
        if (email.equals("admin@example.com")) {  
            System.out.println("Hello, admin!");  
            return true;  
        }  
        System.out.println("Hello, user!");  
        return checkNext(email, password);  
    }  
}
```

## 8.3 `ThrottlingMiddleware`  class (concrete handler)

```Java
public class ThrottlingMiddleware extends Middleware {  
    private int requestPerMinute;  
    private int request;  
    private long currentTime;  
  
    public ThrottlingMiddleware(int requestPerMinute) {  
        this.requestPerMinute = requestPerMinute;  
        this.currentTime = System.currentTimeMillis();  
    }  
    @Override  
    public boolean check(String email, String password) {  
        if (System.currentTimeMillis() > currentTime + 60_000) {  
            request = 0;  
            currentTime = System.currentTimeMillis();  
        }  
  
        request++;  
          
        if (request > requestPerMinute) {  
            System.out.println("Request limit exceeded!");  
            Thread.currentThread().stop();  
        }  
        return checkNext(email, password);  
    }  
}
```

## 8.4 `UserExistsMiddleware` class (concrete class)

```Java
public class UserExistsMiddleware extends Middleware {  
    private Server server;  
  
    public UserExistsMiddleware(Server server) {  
        this.server = server;  
    }  
  
    public boolean check(String email, String password) {  
        if (!server.hasEmail(email)) {  
            System.out.println("This email is not registered!");  
            return false;  
        }  
        if (!server.isValidPassword(email, password)) {  
            System.out.println("Wrong password!");  
            return false;  
        }  
        return checkNext(email, password);  
    }  
}
```

## 8.5 `Server` class

```Java
public class Server {  
    private Map<String, String> users = new HashMap<>();  
    private Middleware middleware;  
    public void setMiddleware(Middleware middleware) {  
        this.middleware = middleware;  
    }  
    public boolean logIn(String email, String password) {  
        if (middleware.check(email, password)) {  
            System.out.println("Authorization have been successful!");  
            return true;  
        }  
        return false;  
    }  
  
    public void register(String email, String password) {  
        users.put(email, password);  
    }  
  
    public boolean hasEmail(String email) {  
        return users.containsKey(email);  
    }  
  
    public boolean isValidPassword(String email, String password) {  
        return users.get(email).equals(password);  
    }  
}
```

## 8.6 `MainCoR` class (client)

```Java
public class MainCoR {  
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
    private static Server server;  
  
    private static void init() {  
        server = new Server();  
        server.register("admin@example.com", "admin_pass");  
        server.register("user@example.com", "user_pass");  
        Middleware middleware = Middleware.link(  
            new ThrottlingMiddleware(2),  
            new UserExistsMiddleware(server),  
            new RoleCheckMiddleware()  
        );  
  
        server.setMiddleware(middleware);  
    }  
  
    public static void main(String[] args) throws IOException {  
        init();  
  
        boolean success;  
        do {  
            System.out.print("Enter email: ");  
            String email = reader.readLine();  
            System.out.print("Input password: ");  
            String password = reader.readLine();  
            success = server.logIn(email, password);  
        } while (!success);  
    }  
}
```