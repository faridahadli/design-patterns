The **Bridge Pattern** is a structural design pattern that lets you split a large class or a set of closely related classes into two separate hierarchies—**abstraction** and **implementation**—which can be developed independently of each other.

It is primarily used to "prefer composition over inheritance," helping to avoid an exponential explosion of class subclasses when a class varies in multiple dimensions.

---

# 1 Core Components

The pattern identifies four key roles:

1. **Abstraction:** The high-level control layer. It contains a reference to an object of the implementation type and delegates the actual work to it.
    
2. **Refined Abstraction:** Precise variants of the control layer (e.g., a "Remote Control" vs. an "Advanced Remote Control").
    
3. **Implementor:** An interface common to all concrete implementations. It defines the primitive operations that the abstraction can use.
    
4. **Concrete Implementors:** The platform-specific or detail-specific code (e.g., "Sony TV" vs. "Samsung TV").


# 2 Structure

![Bridge design pattern](https://refactoring.guru/images/patterns/diagrams/bridge/structure-en.png)

1. The **Abstraction** provides high-level control logic. It relies on the implementation object to do the actual low-level work.
    
2. The **Implementation** declares the interface that’s common for all concrete implementations. An abstraction can only communicate with an implementation object via methods that are declared here.
    
    The abstraction may list the same methods as the implementation, but usually the abstraction declares some complex behaviors that rely on a wide variety of primitive operations declared by the implementation.
    
3. **Concrete Implementations** contain platform-specific code.
    
4. **Refined Abstractions** provide variants of control logic. Like their parent, they work with different implementations via the general implementation interface.

# 3 How to Implement

1. Identify the orthogonal dimensions in your classes. These independent concepts could be: abstraction/platform, domain/infrastructure, front-end/back-end, or interface/implementation.
    
2. See what operations the client needs and define them in the base abstraction class.
    
3. Determine the operations available on all platforms. Declare the ones that the abstraction needs in the general implementation interface.
    
4. For all platforms in your domain create concrete implementation classes, but make sure they all follow the implementation interface.
    
5. Inside the abstraction class, add a reference field for the implementation type. The abstraction delegates most of the work to the implementation object that’s referenced in that field.
    
6. If you have several variants of high-level logic, create refined abstractions for each variant by extending the base abstraction class.
    
7. The client code should pass an implementation object to the abstraction’s constructor to associate one with the other. After that, the client can forget about the implementation and work only with the abstraction object.
    

# 4  Pros and Cons

-  You can create platform-independent classes and apps.
-  The client code works with high-level abstractions. It isn’t exposed to the platform details.
-  _Open/Closed Principle_. You can introduce new abstractions and implementations independently from each other.
-  _Single Responsibility Principle_. You can focus on high-level logic in the abstraction and on platform details in the implementation.

-  You might make the code more complicated by applying the pattern to a highly cohesive class.

# 5  Relations with Other Patterns

- [Bridge](https://refactoring.guru/design-patterns/bridge) is usually designed up-front, letting you develop parts of an application independently of each other. On the other hand, [Adapter](https://refactoring.guru/design-patterns/adapter) is commonly used with an existing app to make some otherwise-incompatible classes work together nicely.
    
- [Bridge](https://refactoring.guru/design-patterns/bridge), [State](https://refactoring.guru/design-patterns/state), [Strategy](https://refactoring.guru/design-patterns/strategy) (and to some degree [Adapter](https://refactoring.guru/design-patterns/adapter)) have very similar structures. Indeed, all of these patterns are based on composition, which is delegating work to other objects. However, they all solve different problems. A pattern isn’t just a recipe for structuring your code in a specific way. It can also communicate to other developers the problem the pattern solves.
    
- You can use [Abstract Factory](https://refactoring.guru/design-patterns/abstract-factory) along with [Bridge](https://refactoring.guru/design-patterns/bridge). This pairing is useful when some abstractions defined by _Bridge_ can only work with specific implementations. In this case, _Abstract Factory_ can encapsulate these relations and hide the complexity from the client code.
    
- You can combine [Builder](https://refactoring.guru/design-patterns/builder) with [Bridge](https://refactoring.guru/design-patterns/bridge): the director class plays the role of the abstraction, while different builders act as implementations.

# 6 Java Code

## 6.1 Remote and Device Problem
This example illustrates how the **Bridge** pattern can help divide the monolithic code of an app that manages devices and their remote controls. The `Device` classes act as the implementation, whereas the `Remote`s act as the abstraction.

![Structure of the Bridge pattern example](https://refactoring.guru/images/patterns/diagrams/bridge/example-en.png)

The original class hierarchy is divided into two parts: devices and remote controls.

The base remote control class declares a reference field that links it with a device object. All remotes work with the devices via the general device interface, which lets the same remote support multiple device types.

You can develop the remote control classes independently from the device classes. All that’s needed is to create a new remote subclass. For example, a basic remote control might only have two buttons, but you could extend it with additional features, such as an extra battery or a touchscreen.

The client code links the desired type of remote control with a specific device object via the remote’s constructor.

### 6.1.1 `Device` interface
```Java
public interface Device {  
  
        boolean isEnabled();  
  
        void enable();  
  
        void disable();  
  
        int getVolume();  
  
        void setVolume(int percent);  
  
        int getChannel();  
  
        void setChannel(int channel);  
  
        void printStatus();  
    }
```

### 6.1.2 `Radio` class

```Java
public class Radio implements Device {  
        private boolean on = false;  
        private int volume = 30;  
        private int channel = 1;  
      
        @Override  
        public boolean isEnabled() {  
            return on;  
        }  
      
        @Override  
        public void enable() {  
            on = true;  
        }  
      
        @Override  
        public void disable() {  
            on = false;  
        }  
      
        @Override  
        public int getVolume() {  
            return volume;  
        }  
      
        @Override  
        public void setVolume(int volume) {  
            if (volume > 100) {  
                this.volume = 100;  
            } else if (volume < 0) {  
                this.volume = 0;  
            } else {  
                this.volume = volume;  
            }  
        }  
      
        @Override  
        public int getChannel() {  
            return channel;  
        }  
      
        @Override  
        public void setChannel(int channel) {  
            this.channel = channel;  
        }  
      
        @Override  
        public void printStatus() {  
            System.out.println("------------------------------------");  
            System.out.println("| I'm radio.");  
            System.out.println("| I'm " + (on ? "enabled" : "disabled"));  
            System.out.println("| Current volume is " + volume + "%");  
            System.out.println("| Current channel is " + channel);  
            System.out.println("------------------------------------\n");  
        }  
}
```

### 6.1.3 `Tv` class

```Java
public class Tv implements Device{  
    private boolean on = false;  
    private int volume = 30;  
    private int channel = 1;  
  
    @Override  
    public boolean isEnabled() {  
        return on;  
    }  
  
    @Override  
    public void enable() {  
        on = true;  
    }  
  
    @Override  
    public void disable() {  
        on = false;  
    }  
  
    @Override  
    public int getVolume() {  
        return volume;  
    }  
  
    @Override  
    public void setVolume(int volume) {  
        if (volume > 100) {  
            this.volume = 100;  
        } else if (volume < 0) {  
            this.volume = 0;  
        } else {  
            this.volume = volume;  
        }  
    }  
  
    @Override  
    public int getChannel() {  
        return channel;  
    }  
  
    @Override  
    public void setChannel(int channel) {  
        this.channel = channel;  
    }  
  
    @Override  
    public void printStatus() {  
        System.out.println("------------------------------------");  
        System.out.println("| I'm TV set.");  
        System.out.println("| I'm " + (on ? "enabled" : "disabled"));  
        System.out.println("| Current volume is " + volume + "%");  
        System.out.println("| Current channel is " + channel);  
        System.out.println("------------------------------------\n");  
    }  
}
```

### 6.1.4 `Remote` interface

```Java
public interface Remote {  
    void power();  
  
    void volumeDown();  
  
    void volumeUp();  
  
    void channelDown();  
  
    void channelUp();  
}
```
### 6.1.5 `BasicRemote` class

```Java
public class BasicRemote implements Remote {  
    protected Device device;  
  
    public BasicRemote() {}  
  
    public BasicRemote(Device device) {  
        this.device = device;  
    }  
  
    @Override  
    public void power() {  
        System.out.println("Remote: power toggle");  
        if (device.isEnabled()) {  
            device.disable();  
        } else {  
            device.enable();  
        }  
    }  
  
    @Override  
    public void volumeDown() {  
        System.out.println("Remote: volume down");  
        device.setVolume(device.getVolume() - 10);  
    }  
  
    @Override  
    public void volumeUp() {  
        System.out.println("Remote: volume up");  
        device.setVolume(device.getVolume() + 10);  
    }  
  
    @Override  
    public void channelDown() {  
        System.out.println("Remote: channel down");  
        device.setChannel(device.getChannel() - 1);  
    }  
  
    @Override  
    public void channelUp() {  
        System.out.println("Remote: channel up");  
        device.setChannel(device.getChannel() + 1);  
    }  
}
```

### 6.1.6 `AdvancedRemote` class

```Java
public class AdvancedRemote extends BasicRemote {  
    public AdvancedRemote(Device device) {  
        super.device = device;  
    }  
  
    public void mute() {  
        System.out.println("Remote: mute");  
        device.setVolume(0);  
    }  
}	
```
