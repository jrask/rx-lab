package se.flapsdown.rxlab;


import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Observables {



    public static <T> Observable<T> fromIterable(Iterable<T> iterable) {
        return Observable.create(emitter -> {

            for (T t : iterable) {
                emitter.onNext(t);
            }
            emitter.onComplete();
        });
    }


    public static void showFiles(AtomicInteger cnt, File... files) {
        for (File file : files) {
            if (file.isDirectory()) {
                //System.out.println("Directory: " + file.getName());
                if (file.listFiles() == null) {
                    System.out.println(file + " was null");
                } else {
                    showFiles(cnt, file.listFiles()); // Calls same method again.
                }
            } else {
                cnt.incrementAndGet();
                Path path = Paths.get(file.toURI());
                try {
                    byte[] bytes = Files.readAllBytes(path);

                    //System.out.println("File: " + file.getName() + " size: " + bytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    static Observable<File> listFiles(File f) {
        if(f.isDirectory())
            return Observable.fromIterable(Arrays.asList(f.listFiles()))
                .flatMap(Observables::listFiles);
        return Observable.just(f);
    }



    public static void main(String args[]) {
        long start = System.currentTimeMillis();
        AtomicInteger cnt = new AtomicInteger(0);
        showFiles(cnt, new File("/Users/jrask/Documents"));
        //showFilesRx(new File("/Users/jrask/Documents"));
        System.out.println(cnt.get());
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();

        Single<Long> count = Observable.just(new File("/Users/jrask/"))
            .flatMap(Observables::listFiles)
            .map(file -> Paths.get(file.toURI()))
            .flatMap(path -> Observable.just(path).subscribeOn(Schedulers.io()).map(path1 -> Files.readAllBytes(path1)))
            .map(bytes -> bytes.length)
            //.onErrorReturnItem(0)
            .doOnError(throwable -> throwable.printStackTrace())
            .count();

        System.out.println("count " + count.blockingGet());
        System.out.println(System.currentTimeMillis() - start);
    }
}
