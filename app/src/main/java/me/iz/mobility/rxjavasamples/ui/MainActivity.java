package me.iz.mobility.rxjavasamples.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iz.mobility.rxjavasamples.R;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        helloWorld();

        simplerHelloWorld();

        otherHelloWorld();

        transformationHelloWorld();

        helloWorldUsingLambda();
    }


    /**
     * Method for hello world
     */
    private void helloWorld() {

//        Emits Hello world and then completes
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("Hello, world!");
                        sub.onCompleted();
                    }
                }
        );

//        Subscriber just prints what observable sends
        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Timber.d(s);
            }

            @Override
            public void onCompleted() {
                Timber.d("Completed!!");
            }

            @Override
            public void onError(Throwable e) {
                Timber.w(e.getMessage());
            }
        };

        myObservable.subscribe(mySubscriber);
    }


    /**
     * Simpler way of sending hello world
     */
    private void simplerHelloWorld() {

        Observable<String> myObservable = Observable.just("Simpler Hello, World! ");

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Timber.d(s);
            }
        };

        myObservable.subscribe(onNextAction);

    }


    private void otherHelloWorld() {

        Observable.just("Simplest Hello, World")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Timber.d(s);
                    }
                });
    }


    /* Observable.just("Hello, world!")
            .map(s -> s.hashCode())
            .map(i -> Integer.toString(i))
            .subscribe(s -> System.out.println(s)); */


    private void transformationHelloWorld() {

        Observable.just("Hello, World")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.hashCode();
                    }
                })
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer s) {
                        return s.toString() + "- Basit";

                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String integer) {
                        Timber.d(integer);
                    }
                });
    }

    private void helloWorldUsingLambda() {
        Observable.just("Hello, world!")
                .map(String::hashCode)
                .map(i -> Integer.toString(i))
                .subscribe(s -> Timber.d(s));
    }

    @OnClick(R.id.btnWithoutRx)
    public void withoutRx() {
        Intent intent = new Intent();
        intent.setClass(this,UsingRetrofitWithoutRxActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btnWithRx)
    public void withRx() {
        Intent intent = new Intent();
        intent.setClass(this,RetrofitWithRx.class);
        startActivity(intent);
    }

}
