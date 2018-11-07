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

### More Creating observables

```java
// Single item stream
Observable<Long> stream = 
    Observable.timer(errorCount, TimeUnit.SECONDS)
    // Same as 
    Single.timer(errorCount, TimeUnit.SECONDS)

// One item every second
Observable<Long> stream = 
    Observable.interval(1, TimeUnit.SECONDS)
    .map(someSource::getStatus)

```

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

## Subscribe samples

```java
Observable.just("Hello", "World")
    .subscribe(
        System.out::println,
        Throwable::printStackTrace,
        ()-> System.out.println("Done!"));

Observable.just("Hello", "World")
    .subscribe();
```

---

### Multiple subscriptions

```java
Observable<String> johan = Observable.just("Johan")
    .map(name -> name.toUpperCase())
    .doOnNext(System.out::print);

johan.subscribe();
johan.subscribe();

> JOHAN
> JOHAN
```

---

## Lazy
```java
Observable<String> stream = Observable.just("Hello", "World")
    .map(s -> s + ":")
    .doOnNext(System.out::println);

// This is where it happens!
stream.subscribe() must be invoked

```
Note: Nothing is done until subscribe() is invoked, thats when items will begin to be emitted.

---

## Lab 1

**Rx Operators**

Open rx-lab/lab/src/test/java/se/flapsdown/rxlab/one/.
Some tests should simply print values, others should be fixed so that
the test passes

---

## Handling Errors

* Simply catch them and return error
* Return default value
* Return another Observable
* Retry
* Combination

---

### Catch exception

```java
Observable.just("Hello", "World")
    // potential error
    .subscribe(
        System.out::println,
        Throwable::printStackTrace,
        ()-> System.out.println("Done!"));
// vs
    .subscribe()
```

Note: If there is no onError() callback an onErrorNotImplementedException will be "thrown", well at least printed to Stderr to inform you that you are missing an exception handler. This would be where we send error response to http clients

### Return values

```java
Observable.just("Hello", "World")
    // potential error
    .onErrorReturnItem("GoodNight")

Observable.just("Hello", "World")
    // potential error
    .onErrorReturn( error ->
        if( error instanceof NullPointerException) {
            return "Empty"
        } else {
            throw error;
        }
    )
```

---

### Return a stream

```java
Observable.just("Hello", "World")
    // potential error
    .onErrorResumeNext(Observable.empty())
```
---

### Retry n times

```java
Observable.just("Hello", "World")
    // potential error
    .retry(5)
    .subscribe()
```
Note: Will retry 5 times regardless of error

---

### Retry with condition

```java
Observable.just("Hello", "World")
    // potential error
    .retry( (retryCnt, error) -> retryCnt < 5)
    .subscribe()
```
Note: We can retry depending on error and/or count

---

## Lab 2

Errors

---

## Concurrency

* flatMap() is **The** operator
* Schedulers

---

### flatMap 

flatMap()

* Flattens (flat) and transforms (map)
* Returns another Observable/Flowable/Single/etc..
    * and that is how it supports concurrency

---

### Basic flatMap() example

List -> Observable

```java
Observable.just(asList("Johan", "Rask"))
    .flatMap( list -> Observable.fromIterable(list))
    .subscribe(System.out::println)

> Johan
> Rask    
```

---
### 1. flatMap() and concurrency

Consider the following

```java
getUUIDs()
    .map( uuid -> getByUuid(uuid))
    .subscribe(System.out::println)
```
vs

```java
getUUIDs()
    .flatMap( uuid -> 
        Observable.fromCallable(() -> getByUUid(uuid)))
    .subscribe(System.out::println)
```

Note: These two will currently result in the exact same way but by using
flatMap we are prepared to declare concurrency.

---

### Schedulers



* Think threadpool
* Define **which** threadpool to use
* Define **where** to use this threadpool

---

### Scheduling

* observeOn(Scheduler)
* subscribeOn(Scheduler)
* Schedulers.computation()
* Schedulers.io()
* Custom schedulers

Note: Explain, but show in next and next:next slide how it works

---

### SubscribeOn

```java
Observable.create(emitter -> {
        String data = slowNetworkCall();
        emitter.onNext(data);
        emitter.onComplete();
})
.subscribeOn(Schedulers.io()) // Can be anywhere
.observeOn(Schedulers.computation()) // Effect downstream
.map(data -> Json.parse(data))
.subscribe(System.out::println)
```

Note: Important to understand how subscribeOn() and observeOn() works

---

### Scheduling example

A more advanced example

```java
fetchFromNetwork()                // returns Observable
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.computation())
    .map (value -> somecomputation()) 
    .flatMap (value -> 
        networkCall()  // returns Observable
            .subscribeOn(Schedulers.io))
    .subscribe()
```
---

## Lab 3

Concurrency

#### Lab 4 

**From blocking two reactive**

Re-write existing blocking imperative program into a
* Reactive and non-blocking
* Handle errors (= not crash, use default)
* Handle retries (=retry, use default if fails)
* Use timeout to prevent long running


BUT first, lets discuss how this could be done without Rx first

---
