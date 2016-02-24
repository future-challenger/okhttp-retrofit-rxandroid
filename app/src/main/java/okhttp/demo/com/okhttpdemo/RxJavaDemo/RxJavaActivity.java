package okhttp.demo.com.okhttpdemo.RxJavaDemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.io.IOException;

import okhttp.demo.com.okhttpdemo.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RxJavaActivity extends Activity {

    private final static String TAG = RxJavaActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        findViewById(R.id.rxjava_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable<String> theObservable = Observable.just("hello world!");
// with observer
//        Observer<String> theObserver = new Observer<String>() {
//            @Override
//            public void onCompleted() {
//                Log.d(TAG, "completed");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "error");
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.d(TAG, "data:- " + s);
//            }
//        };
//        Subscription theSubscription = theObservable.subscribe(theObserver);

// with action1<>
                Action1<String> theAction = new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "action1 data:- " + s);
                    }
                };

                Subscription theSubscription = theObservable.subscribe(theAction);
            }
        });

        findViewById(R.id.from_observable_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable<Integer> listObservable = Observable.from(new Integer[]{1, 2, 3, 4, 5});
                listObservable.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "data:- " + String.valueOf(integer));
                    }
                });

                //map
                listObservable.map(new Func1<Integer, Integer>() { // 1
                    @Override
                    public Integer call(Integer integer) {
                        return integer * integer; // 2
                    }
                });

                // skip & filter
                listObservable
                        .skip(2) // 1
                        .filter(new Func1<Integer, Boolean>() {
                            @Override
                            public Boolean call(Integer integer) {
                                return integer % 2 == 0; // 2
                            }
                        })
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                Log.d(TAG, "skip & filter data:- " + String.valueOf(integer));
                            }
                        });
            }
        });

        findViewById(R.id.async_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable<String> asyncObservable = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            String content = fetchData("https://api.github.com/orgs/octokit/repos");
                            subscriber.onStart();
                            subscriber.onNext(content);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }

                    }
                });

                asyncObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Log.d(TAG, "async data:- " + s);
                                Toast.makeText(RxJavaActivity.this, "async data:- " + s, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        findViewById(R.id.scenario_1_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scenarioHttpRequests();
            }
        });

        findViewById(R.id.scenario_2_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senarioThreadDependency();
            }
        });

        Button eventButton = (Button) findViewById(R.id.event_button);
//        RxView.clicks(eventButton).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                Toast.makeText(RxJavaActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

        RxView.clicks(eventButton).skip(3).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(RxJavaActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 场景一：两个以上的Http请求
     */
    private void scenarioHttpRequests() {
        Observable<String> yahooObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String content = fetchData2("https://www.yahoo.com");
                    subscriber.onStart();
                    subscriber.onNext(content);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }

            }
        });

        Observable<String> googleObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String content = fetchData2("https://www.google.com");
                    subscriber.onStart();
                    subscriber.onNext(content);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }

            }
        });

        yahooObservable.subscribeOn(Schedulers.newThread());
        googleObservable.subscribeOn(Schedulers.newThread());

        Observable<String> zippedObservable = Observable.zip(yahooObservable, googleObservable, new Func2<String, String, String>() {
            @Override
            public String call(String yahooString, String appleString) {
                String result = yahooString + "\n" + appleString;
                Log.d(TAG, result);

                if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                    Log.d(TAG, "Main Thread");
                } else {
                    Log.d(TAG, "Not Main Thread");
                }

                Toast.makeText(RxJavaActivity.this, result, Toast.LENGTH_SHORT).show();
                return result;
            }
        });

        zippedObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Toast.makeText(RxJavaActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void senarioThreadDependency() {
        Observable<String> yahooObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String content = fetchData2("https://www.yahoo.com");
                    subscriber.onStart();
                    subscriber.onNext(content);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }

            }
        });

        Observable<String> googleObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    String content = fetchData2("https://www.google.com");
                    subscriber.onStart();
                    subscriber.onNext(content);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }

            }
        });

        yahooObservable.subscribeOn(Schedulers.newThread());
        googleObservable.subscribeOn(Schedulers.newThread());

        Observable.concat(yahooObservable, googleObservable)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "concat data: " + s);
                        Toast.makeText(RxJavaActivity.this, "concat data: " + s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String fetchData(String urlString) {
        String result = "";
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urlString)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();

            if (!response.isSuccessful()) {
                result = "REQUEST_FAIL";
            } else {
                result = response.body().string();
            }
        } catch (IOException e) {
            result = "REQUEST_FAIL";
        } finally {
            return result;
        }
    }

    private String fetchData2(String urlString) {
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {

        } finally {
            return "You requested: " + urlString;
        }
    }
}
