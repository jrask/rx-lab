## Background to Reactive Programming

#### [ Async Programming Done Right ]

---

## About me

* Java programmer since ...
* Enjoy other langs (as well)
* Devops:ish + Cloud + Infra + Observability
* Highly Opinionated - but try to be open and "discussable"

---

## Interactive Presentation

* Discussions
* "Whiteboarding"
* Everyone must understand

---

## Agenda

* Background (Why are we here)
* Reactive introduction
* Code

---

## Goals

* Understand Javas default imperative programming model and its implications
* Understand what Reactive programming is and when to use it
* Understand where you are today and where you want to go

---

## Concepts

* Performance - Scalability
* Blocking - Non-Blocking (later)
* Throughput - Latency
* Reactive Systems - Reactive Programming
* Simple - Easy

Note: Latency is the time required to perform some action or to produce some result. Latency is measured in units of time -- hours, minutes, seconds, nanoseconds or clock periods. Throughput is the number of such actions executed or results produced per unit of time.

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

## Servlets

* Servlet 2.0 - 1997 (JDK 1.1) 
* ….
* Servlet 3.0 - 2009 - Async Servlet
* Servlet 3.1 - 2013 - Nio

---

## Current Programming Model

* Request-per-thread
    * Since < 1997
    * Good - Bad ?

Note: Simple since it is easy to reason about. Good for cpu intensive. Bad since it is very hard to make it efficient. Hard to parallelize. Blocking!

---

## Threads and concurrency

* Blocking
* Non-blocking

Note: Explain and discuss what this means so we we are on the same level
---

### "Default" Java vs NodeJS

Java = Imperative & blocking
```java
String response = httpClient
        .get("http://google.com").asString();        
System.out.println(res)

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

---

## Blocking & non-blocking operations

* REST ?
* SOAP ?
* JDBC ?
* Files ?
* More ? 

Note: How can we make blocking operations non-blocking:ish? JDBC and transactions? JDBC and transactions over multiple methods?

---

## Available tools for non-blocking

* What tools do we have to prevent calling thread to block

Note: Futures always block. Completable Future

---

#### Wrap a blocking call

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

---

### Rx - (finally....)

```java
Observable.fromCallable(() 
    -> new LongRunningTask().runFor(1_000));
        .map( s -> "Sleep was " + s)
        .subscribe(this::print)
```

What about threads?

Note: Goto intellij and run samples.
Add subscribeOn and observeOn and check which thread it runs on

---
