## Reactive Programming

### [Async Programming Done Right ]

Note: 

* At least at good as we can in java
* How many have used rx?
* How many has experience from other languages?

---

## Agenda

* Background (Why are we here)
* Reactive introduction
* Labs
    * Operators
    * Handling Errors
    * Concurrency
    * Convert app, Blocking -> Reactive

Note: Discussions, whiteboard, everyone must understand

---

## Goals

* **Sharing knowledge!**
* Understand Javas default imperative programming model, its advantages and its implications
* Understand what Reactive programming is and when to use it
* Understand where you are today and where you want to go

---

## Concepts

* Performance - Scalability
* Blocking - Non-Blocking
* Synchrounous vs Asynchrounous
* Throughput - Latency
* Reactive Systems - Reactive Programming
* Simple - Easy

Note: Latency is the time required to perform some action or to produce some result. Latency is measured in units of time -- hours, minutes, seconds, nanoseconds or clock periods. Throughput is the number of such actions executed or results produced per unit of time.

Asynchronous is defined by the Oxford Dictionary as “not existing or occurring at the same time”, which in this context means that the processing of a message or event is happening at some arbitrary time, possibly in the future.

---

## History

* Containers
* Servlets
* Programming Models

---

## Tomcat Connectors

* Bio 
* Nio + Nio2
* Apr

Note: Difference between Bio and Nio? Performance. Whiteboard!

---

### Servlets

* Servlet 2.0 - 1997 (JDK 1.1) 
* ….
* Servlet 3.0 - 2009 - Async Servlet
* Servlet 3.1 - 2013 - Nio
    * Most framweorks has some support for this

Note: Discuss, where are you today and why?
Spring, Jersey, DeferredResult, AsyncContext
---

### Current "http" Programming Model

* Request-per-thread
    * Since < 1997
    * Good - Bad ?

Note: 
* What does the request thread do most of the time?
* Simple since it is easy to reason about and debug
* Good for cpu intensive. 
* Bad since it is very hard to make it efficient. 
* Hard and complex to parallelize. 
* Blocking!
* What can we gain from a non-blocking model?

---

### Hmmm... lets discuss..

***Non-blocking IO***

* What does this mean?
* Is it better and faster than blocking IO?

Note: IO operations does not block calling thread. It scales better with many connections since it consumers fewer threads.

---

### "Default" Java (backend?) vs NodeJS

Java = Imperative & blocking
```java
String response = httpClient
        .get("http://google.com").asString();        

String contents = FileUtils.read("/tmp/file.txt")
```

NodeJS - Functional & non-blocking

```javascript
httpClient.get("http://google.com", func(err, res) {
    // Invoked on event-thread
    console.print(res.response);
});
```

Note: Java ALWAYS block, NodeJS NEVER Block. Android and Java Swing is not allowed to block the event-thread.
NodeJS has threadpools for cpu intensive work like crypto.

---



## Blocking & non-blocking operations

To make full advantage of rx we need API:s that are non-blocking.

* HTTP/REST clients ?
* SOAP ?
* JDBC ?
* Files ?
* More ? 

Note: Which of these provide non-blocking API:s? How can we make blocking operations non-blocking:ish? JDBC and transactions? JDBC and transactions over multiple methods?

---

## Available tools for non-blocking

* What tools do we have to prevent calling thread to block



Note: Futures always block. CompletableFuture. Callbacks. ThreadPools

---

#### Wrap a blocking call

If a missing non-blocking API exists, we can wrap a blocking call to at least prevent the
calling thread to block.

Consider this code, how can we wrap this in an async flow to prevent from blocking a calling thread?

```java
public String runFor(long ms) {
    Thread.sleep(ms);
    return String.valueOf(ms);
}

// blocking invocation
print(new LongRunningTask().runFor(1_000));
```

Note: Code example
Let go of context like servlets etc.

---

### Future

```java
Future<String> res = 
    executorService.submit(()
         -> new LongRunningTask().runFor(1_000));
print(res.get()); // blocks
```

Note: Have we really gained anything?

---

### CompletableFuture

```java
CompletableFuture.supplyAsync(() -> 
    new LongRunningTask().runFor(1_000))
        .thenApply(s -> "Sleep was " + s)
        .thenAccept(this::print);
```

Note: Single value only
Cannot declaratively define schedulers

### Discussions?

* Missing anything?