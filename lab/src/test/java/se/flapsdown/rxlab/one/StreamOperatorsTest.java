package se.flapsdown.rxlab.one;

import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import se.flapsdown.rxlab.util.Streams;

import static se.flapsdown.rxlab.util.DogStream.dogs;

public class StreamOperatorsTest {


    TestObserver<String>  stringSubscriber = new TestObserver<>();
    TestObserver<Integer> intSubscriber = new TestObserver<>();


    @Before
    public void prepare() {
        stringSubscriber = new TestObserver<>();
        intSubscriber = new TestObserver<>();
    }


    @Test
    public void test_print_all_names() {
        dogs()
            // print all names in doOnNext()
            .subscribe();
    }


    @Test
    public void test_print_all_names_except_daisy() {
        dogs()
            // 2. Print all except Daisy
            .subscribe();
    }


    @Test
    public void test_take_first_2() {

        dogs()
            .take(2)
            .subscribe(stringSubscriber);
        stringSubscriber.assertResult("Bella", "Buster");
    }


    @Test
    public void test_take_last() {

        dogs()
            .lastElement()
            .subscribe(stringSubscriber);
        stringSubscriber.assertResult("Rocky");
    }


    @Test
    public void test_take_while() {

        dogs()
            .takeWhile( s -> !s.startsWith("P"))
            .subscribe(stringSubscriber);
        stringSubscriber.assertResult("Bella","Buster");
    }

    @Test
    public void test_take_last_with_skip() {

        dogs()
            .skip(4)
            .subscribe(stringSubscriber);
        stringSubscriber.assertResult("Rocky");
    }


    @Test
    public void test_return_default_dogname() {

        dogs()
            .filter(s -> s.equals("santa"))
            .defaultIfEmpty("Rocky")
            .subscribe(stringSubscriber);
        stringSubscriber.assertResult("Rocky");
    }


    @Test
    public void test_scan_or_reduce_to_calculate_total_bytes() {

        // Pick correct function of scan() and reduce() to make sure that
        // the test works
        dogs()
            .map(String::length)
            .reduce((s, s2) -> s + s2)
            .subscribe(intSubscriber);
        intSubscriber.assertResult(26);
        intSubscriber.assertValueCount(1);
    }


    @Test
    public void test_flatmap_streams() {


        dogs()
            .flatMap(s -> dogs())
            .subscribe(stringSubscriber);

        stringSubscriber.assertValueCount(5 * 5);
    }

    @Test
    public void test_merge_streams() {
        // Merging two dog streams into one stream to see what happens
        dogs()
            .mergeWith(dogs())
            .doOnNext(System.out::println)
            .subscribe(stringSubscriber);

        stringSubscriber.assertValueCount(10);
    }

    @Test
    public void test_zip_streams() {
        // Zip two streams using zipWith() or zip()

        // Zip two dog streams into a stream of Streams.Pair
        dogs()

            .zipWith(dogs(), (dog1, dog2) -> Streams.Pair.of(dog1, dog2))
            .map(Streams.Pair::toString)
            .doOnNext(System.out::println)
            .take(1)
            .subscribe(stringSubscriber);

        stringSubscriber.assertValueCount(1);
        stringSubscriber.assertValue("Bella - Bella");
    }

}
