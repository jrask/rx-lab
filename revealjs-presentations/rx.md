## RxJava

Reactive Extensions for java

---

## What is it

Declarative - Functional 

---

## What will not be covered

* Focus
    * Async non-blocking aspects
    * Calling external services

* Not covered
    * Backpressure
    * Most operators (zip, join, etc)

## Components  

* Observer (0 .. n)
* Flowable (0 .. n + backpressure)
* Single   ( exactly one)
* Maybe    ( 0 or 1, like Optional)
* Completable 

---

## Sample

```java
Observable.just("Hello", "World")
    .map(s -> s + ":")
    .subscribe(System.out::println)

Hello:
World:
```

---

## Lazy
```java
Observable.just("Hello", "World")
    .map(s -> s + ":")
    .doOnNext(System.out::println)
```
Nothing is done until subscribe() is invoked, thats when items will begin to be emitted.

