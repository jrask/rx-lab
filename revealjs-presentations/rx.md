## RxJava

Reactive Extensions for java

---

## What is it

* Declarative - Functional 
* Observer Pattern - On Steroids
* Streams - On Sterioids

---

### What will be covered

* Focus
    * Async & non-blocking aspects
    * flatMap() is best your friend
    * Simple scenarios - request/response
    * Calling external services

* Not covered
    * Backpressure
    * Hot/Cold observables

---

## Components  

* Observable  (0 .. n) (Focus of today)
* Flowable    (0 .. n + backpressure)
* Single      ( exactly one)
* Maybe       ( 0 or 1, like Optional)
* Completable 


Note: Samples like getCustomers=Observable/Flowable, getCustomer=Maybe/Single

---

## Stream vs Observable
```java
    Optional<Integer> bytes = asList("Hello","World")
            .stream()
            .map(String::length)
            .reduce((int1, int2) -> int1 + int2);

    Maybe<Integer> bytes = 
        Observable.fromIterable(asList("Hello","World"))
            .map(String::length);
            .reduce((int1, int2) -> int1 + int2);
```

Note: Rarely used, more common to use subscribe() function

---

### Creating observables

```java
Observable.just("Johan");
Observable.just("Johan","Rask");
Observable.fromIterable(anyIterable);
Observable.fromCallable(callable); # Great for wrapping async stuff (later)
```
Note: When using fromCallable allows us to configure on which thread it runs later

---

### Custom creation

```java    
public static <T> Observable<T> fromIterable(Iterable<T> iterable) {
    return Observable.create(emitter -> {

        for (T t : iterable) {
            emitter.onNext(t);
        }
        emitter.onComplete();
    });
}
```

---

### Subscribing to observables

There are three methods/callbacks that you can register in the subscription

* onNext(T t) - Invoked for each item in the stream
* onError(Throwable t) - Invoked when there is an error.
* onComplete() - Stream is finished and no more items will appear

Note: Think a reactive webapp, write in onNext() flush/end in onError and onComplete()
onError is instead of catch() we cannot catch in async mode

---

## Subscribe sample

```java
Observable.just("Hello", "World")
    .subscribe(
        System.out::println,
        Throwable::printStackTrace,
        ()-> System.out.println("Done!"));
```

---

## Lazy
```java
Observable.just("Hello", "World")
    .map(s -> s + ":")
    .doOnNext(System.out::println)
    //.subscribe() must be invoked
```
Nothing is done until subscribe() is invoked, thats when items will begin to be emitted.

---

