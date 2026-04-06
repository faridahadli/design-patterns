# 1 Core Concept
**Proxy** is a structural design pattern that lets you provide a substitute or placeholder for another object. The primary goal is **controlled access**. You use a proxy when you want to add functionality to an object without changing its code. This is useful for:

- **Lazy Initialization (Virtual Proxy):** Creating a heavy object only when it's actually needed.
    
- **Access Control (Protection Proxy):** Checking if a client has the necessary permissions.
    
- **Local Execution of Remote Service (Remote Proxy):** Handling the "messy" networking code so a remote object looks local.
    
- **Logging/Caching:** Storing results or logging calls without bloating the business logic.

The pattern relies on the Proxy and the Real Subject implementing the same interface. This ensures the client doesn't even know it's talking to a proxy.

- **Subject Interface:** Defines the common operations for both the Real Subject and the Proxy.
    
- **Real Subject:** The class that contains the actual business logic.
    
- **Proxy:** Contains a reference to the Real Subject. it implements the same interface, performs its "extra" work (logging, caching, etc.), and then delegates the call to the Real Subject.

# 2 Structure

![Structure of the Proxy design pattern](https://refactoring.guru/images/patterns/diagrams/proxy/structure-indexed.png)

1. The **Service Interface** declares the interface of the Service. The proxy must follow this interface to be able to disguise itself as a service object.
    
2. The **Service** is a class that provides some useful business logic.
    
3. The **Proxy** class has a reference field that points to a service object. After the proxy finishes its processing (e.g., lazy initialization, logging, access control, caching, etc.), it passes the request to the service object.
    
    Usually, proxies manage the full lifecycle of their service objects.
    
1. The **Client** should work with both services and proxies via the same interface. This way you can pass a proxy into any code that expects a service object.

# 3 Applicability

- There are dozens of ways to utilize the Proxy pattern. Let’s go over the most popular uses.

 - Lazy initialization (virtual proxy). This is when you have a heavyweight service object that wastes system resources by being always up, even though you only need it from time to time.

 - Instead of creating the object when the app launches, you can delay the object’s initialization to a time when it’s really needed.

 - Access control (protection proxy). This is when you want only specific clients to be able to use the service object; for instance, when your objects are crucial parts of an operating system and clients are various launched applications (including malicious ones).

 - The proxy can pass the request to the service object only if the client’s credentials match some criteria.

 - Local execution of a remote service (remote proxy). This is when the service object is located on a remote server.

 - In this case, the proxy passes the client request over the network, handling all of the nasty details of working with the network.

 - Logging requests (logging proxy). This is when you want to keep a history of requests to the service object.

 - The proxy can log each request before passing it to the service.

 - Caching request results (caching proxy). This is when you need to cache results of client requests and manage the life cycle of this cache, especially if results are quite large.

 - The proxy can implement caching for recurring requests that always yield the same results. The proxy may use the parameters of requests as the cache keys.

 - Smart reference. This is when you need to be able to dismiss a heavyweight object once there are no clients that use it.

 - The proxy can keep track of clients that obtained a reference to the service object or its results. From time to time, the proxy may go over the clients and check whether they are still active. If the client list gets empty, the proxy might dismiss the service object and free the underlying system resources.

The proxy can also track whether the client had modified the service object. Then the unchanged objects may be reused by other clients.

# 4  How to Implement

1. If there’s no pre-existing service interface, create one to make proxy and service objects interchangeable. Extracting the interface from the service class isn’t always possible, because you’d need to change all of the service’s clients to use that interface. Plan B is to make the proxy a subclass of the service class, and this way it’ll inherit the interface of the service.
    
2. Create the proxy class. It should have a field for storing a reference to the service. Usually, proxies create and manage the whole life cycle of their services. On rare occasions, a service is passed to the proxy via a constructor by the client.
    
3. Implement the proxy methods according to their purposes. In most cases, after doing some work, the proxy should delegate the work to the service object.
    
4. Consider introducing a creation method that decides whether the client gets a proxy or a real service. This can be a simple static method in the proxy class or a full-blown factory method.
    
5. Consider implementing lazy initialization for the service object.
    

# 5  Pros and Cons
Pros
-  You can control the service object without clients knowing about it.
-  You can manage the lifecycle of the service object when clients don’t care about it.
-  The proxy works even if the service object isn’t ready or is not available.
-  _Open/Closed Principle_. You can introduce new proxies without changing the service or clients.
Cons
-  The code may become more complicated since you need to introduce a lot of new classes.
-  The response from the service might get delayed.

# 6  Relations with Other Patterns

- With [Adapter](https://refactoring.guru/design-patterns/adapter) you access an existing object via different interface. With [Proxy](https://refactoring.guru/design-patterns/proxy), the interface stays the same. With [Decorator](https://refactoring.guru/design-patterns/decorator) you access the object via an enhanced interface.
    
- [Facade](https://refactoring.guru/design-patterns/facade) is similar to [Proxy](https://refactoring.guru/design-patterns/proxy) in that both buffer a complex entity and initialize it on its own. Unlike _Facade_, _Proxy_ has the same interface as its service object, which makes them interchangeable.
    
- [Decorator](https://refactoring.guru/design-patterns/decorator) and [Proxy](https://refactoring.guru/design-patterns/proxy) have similar structures, but very different intents. Both patterns are built on the composition principle, where one object is supposed to delegate some of the work to another. The difference is that a _Proxy_ usually manages the life cycle of its service object on its own, whereas the composition of _Decorators_ is always controlled by the client.
# 7 Examples in Java

The Proxy pattern is arguably the most important pattern in the modern Java ecosystem, specifically because of how Spring handles "magic" behind the scenes.

 ***Java Standard Library (java.lang.reflect.Proxy)***

Java has built-in support for **Dynamic Proxies**. This allows you to create a proxy for any interface at runtime without writing a specific class.

- **Usage:** Used by many frameworks to intercept method calls. You provide an `InvocationHandler` that decides what to do when a method is called.
    

***Spring Framework (AOP)***

Spring is practically built on proxies. If you’ve ever wondered how adding an annotation makes things "just work," it's likely a proxy.

- **`@Transactional`:** Spring creates a proxy around your bean. When you call a method, the proxy starts a database transaction, calls your real method, and then commits or rolls back the transaction based on the result.
    
- **`@Cacheable`:** The proxy checks if the data is already in the cache. If it is, it returns the cached version; if not, it calls your real method and saves the result.
    
- **`@Async`:** The proxy intercepts the call and submits the task to an executor service instead of running it on the main thread.
    

 ***Hibernate (Lazy Loading)***

When you fetch an entity from the database that has a "Lazy" relationship, Hibernate doesn't fetch the related data immediately.

- **Usage:** It returns a **Proxy object**. Only when you call a getter like `user.getAddress()` does the proxy wake up, hit the database, and populate the data.

# 8 Java Code

This example illustrates how the **Proxy** pattern can help to ***introduce caching*** to a 3rd-party YouTube integration library.

![[Pasted image 20260406155015.png]]

The library provides us with the video downloading class. However, it’s very inefficient. If the client application requests the same video multiple times, the library just downloads it over and over, instead of caching and reusing the first downloaded file.

The proxy class implements the same interface as the original downloader and delegates it all the work. However, it keeps track of the downloaded files and returns the cached result when the app requests the same video multiple times.

## 8.1 `ThirdPartyYouTubeLib` interface (service interface)

```Java
public interface ThirdPartyYouTubeLib {  
    HashMap<String, Video> popularVideos();  
    Video getVideo(String videoId);  
}
```

## 8.2 `ThirdPartyYouTubeClass`  class (service)

```Java
public class ThirdPartyYouTubeClass implements ThirdPartyYouTubeLib {  
  
    @Override  
    public HashMap<String, Video> popularVideos() {  
        connectToServer("http://www.youtube.com");  
        return getRandomVideos();  
    }  
  
    @Override  
    public Video getVideo(String videoId) {  
        connectToServer("http://www.youtube.com/" + videoId);  
        return getSomeVideo(videoId);  
    }  
  
    // -----------------------------------------------------------------------  
    // Fake methods to simulate network activity. They as slow as a real life.  
    private int random(int min, int max) {  
        return min + (int) (Math.random() * ((max - min) + 1));  
    }  
  
    private void experienceNetworkLatency() {  
        int randomLatency = random(5, 10);  
        for (int i = 0; i < randomLatency; i++) {  
            try {  
                Thread.sleep(100);  
            } catch (InterruptedException ex) {  
                ex.printStackTrace();  
            }  
        }  
    }  
  
    private void connectToServer(String server) {  
        System.out.print("Connecting to " + server + "... ");  
        experienceNetworkLatency();  
        System.out.print("Connected!" + "\n");  
    }  
  
    private HashMap<String, Video> getRandomVideos() {  
        System.out.print("Downloading populars... ");  
  
        experienceNetworkLatency();  
        HashMap<String, Video> hmap = new HashMap<String, Video>();  
        hmap.put("catzzzzzzzzz", new Video("sadgahasgdas", "Catzzzz.avi"));  
        hmap.put("mkafksangasj", new Video("mkafksangasj", "Dog play with ball.mp4"));  
        hmap.put("dancesvideoo", new Video("asdfas3ffasd", "Dancing video.mpq"));  
        hmap.put("dlsdk5jfslaf", new Video("dlsdk5jfslaf", "Barcelona vs RealM.mov"));  
        hmap.put("3sdfgsd1j333", new Video("3sdfgsd1j333", "Programing lesson#1.avi"));  
  
        System.out.print("Done!" + "\n");  
        return hmap;  
    }  
  
    private Video getSomeVideo(String videoId) {  
        System.out.print("Downloading video... ");  
  
        experienceNetworkLatency();  
        Video video = new Video(videoId, "Some video title");  
  
        System.out.print("Done!" + "\n");  
        return video;  
    }  
  
}
```

## 8.3 `CachedYoutubeClass` class (proxy)

```Java
public class CachedYoutubeClass implements ThirdPartyYouTubeLib {  
    private ThirdPartyYouTubeLib youtubeService;  
    private HashMap<String, Video> cachePopular = new HashMap<String, Video>();  
    private HashMap<String, Video> cacheAll = new HashMap<String, Video>();  
  
    public CachedYoutubeClass() {  
        this.youtubeService = new ThirdPartyYouTubeClass();  
    }  
  
    @Override  
    public HashMap<String, Video> popularVideos() {  
        if (cachePopular.isEmpty()) {  
            cachePopular = youtubeService.popularVideos();  
        } else {  
            System.out.println("Retrieved list from cache.");  
        }  
        return cachePopular;  
    }  
  
    @Override  
    public Video getVideo(String videoId) {  
        Video video = cacheAll.get(videoId);  
        if (video == null) {  
            video = youtubeService.getVideo(videoId);  
            cacheAll.put(videoId, video);  
        } else {  
            System.out.println("Retrieved video '" + videoId + "' from cache.");  
        }  
        return video;  
    }  
  
    public void reset() {  
        cachePopular.clear();  
        cacheAll.clear();  
    }  
}
```

## 8.4 `Video` class (utility)
```Java
public class Video {  
    public String id;  
    public String title;  
    public String data;  
  
    public Video(String id, String title) {  
        this.id = id;  
        this.title = title;  
        this.data = "Random video.";  
    }  
}
```

## 8.5 `YouTubeDownloader` class (client)

```Java
public class YouTubeDownloader {  
    private ThirdPartyYouTubeLib api;  
  
    public YouTubeDownloader(ThirdPartyYouTubeLib api) {  
        this.api = api;  
    }  
  
    public void renderVideoPage(String videoId) {  
        Video video = api.getVideo(videoId);  
        System.out.println("\n-------------------------------");  
        System.out.println("Video page (imagine fancy HTML)");  
        System.out.println("ID: " + video.id);  
        System.out.println("Title: " + video.title);  
        System.out.println("Video: " + video.data);  
        System.out.println("-------------------------------\n");  
    }  
  
    public void renderPopularVideos() {  
        HashMap<String, Video> list = api.popularVideos();  
        System.out.println("\n-------------------------------");  
        System.out.println("Most popular videos on YouTube (imagine fancy HTML)");  
        for (Video video : list.values()) {  
            System.out.println("ID: " + video.id + " / Title: " + video.title);  
        }  
        System.out.println("-------------------------------\n");  
    }  
}
```

## 8.6 `MainProxy`  

```Java
public class MainProxy {  
    public static void main(String[] args) {  
        YouTubeDownloader naiveDownloader = new YouTubeDownloader(new ThirdPartyYouTubeClass());  
        YouTubeDownloader smartDownloader = new YouTubeDownloader(new CachedYoutubeClass());  
  
        long naive = test(naiveDownloader);  
        long smart = test(smartDownloader);  
        System.out.print("Time saved by caching proxy: " + (naive - smart) + "ms");  
  
    }  
  
    private static long test(YouTubeDownloader downloader) {  
        long startTime = System.currentTimeMillis();  
  
        // User behavior in our app:  
        downloader.renderPopularVideos();  
        downloader.renderVideoPage("catzzzzzzzzz");  
        downloader.renderPopularVideos();  
        downloader.renderVideoPage("dancesvideoo");  
        // Users might visit the same page quite often.  
        downloader.renderVideoPage("catzzzzzzzzz");  
        downloader.renderVideoPage("someothervid");  
  
        long estimatedTime = System.currentTimeMillis() - startTime;  
        System.out.print("Time elapsed: " + estimatedTime + "ms\n");  
        return estimatedTime;  
    }  
}
```