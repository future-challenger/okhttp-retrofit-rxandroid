package okhttp.demo.com.okhttpdemo.RxJavaDemo

import android.app.Activity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.jakewharton.rxbinding.view.RxView
import okhttp.demo.com.okhttpdemo.R
import okhttp3.OkHttpClient
import okhttp3.Request
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.io.IOException

class RxJavaActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_java)

        findViewById(R.id.rxjava_button).setOnClickListener {
            val theObservable = Observable.just("hello world!")
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
            val theAction = Action1<kotlin.String> { s -> Log.d(TAG, "action1 data:- " + s) }

            val theSubscription = theObservable.subscribe(theAction)
        }

        findViewById(R.id.from_observable_button).setOnClickListener {
            val listObservable = Observable.from(arrayOf(1, 2, 3, 4, 5))
            listObservable.subscribe { integer -> Log.d(TAG, "data:- " + integer.toString()) }

            //map
            listObservable.map { integer -> // 1
                integer!! * integer // 2
            }

            // skip & filter
            listObservable.skip(2) // 1
                    .filter { integer ->
                        integer!! % 2 == 0 // 2 }.subscribe { integer -> Log.d(TAG, "skip & filter data:- " + integer.toString()) }
                    }

            findViewById(R.id.async_button).setOnClickListener {
                val asyncObservable = Observable.create(Observable.OnSubscribe<kotlin.String> { subscriber ->
                    try {
                        val content = fetchData("https://api.github.com/orgs/octokit/repos")
                        subscriber.onStart()
                        subscriber.onNext(content)
                        subscriber.onCompleted()
                    } catch (e: Exception) {
                        subscriber.onError(e)
                    }
                })

                asyncObservable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe { s ->
                    Log.d(TAG, "async data:- " + s)
                    Toast.makeText(this@RxJavaActivity, "async data:- " + s, Toast.LENGTH_SHORT).show()
                }
            }

            findViewById(R.id.scenario_1_button).setOnClickListener { scenarioHttpRequests() }

            findViewById(R.id.scenario_2_button).setOnClickListener { senarioThreadDependency() }

            val eventButton = findViewById(R.id.event_button) as Button
            //        RxView.clicks(eventButton).subscribe(new Action1<Void>() {
            //            @Override
            //            public void call(Void aVoid) {
            //                Toast.makeText(RxJavaActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            //            }
            //        });

            RxView.clicks(eventButton).skip(3).subscribe { Toast.makeText(this@RxJavaActivity, "Clicked", Toast.LENGTH_SHORT).show() }


        }
    }

    /**
     * 场景一：两个以上的Http请求
     */
    fun scenarioHttpRequests() {
        val yahooObservable = Observable.create(Observable.OnSubscribe<kotlin.String> { subscriber ->
            try {
                val content = fetchData2("https://www.yahoo.com")
                subscriber.onStart()
                subscriber.onNext(content)
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        })

        val googleObservable = Observable.create(Observable.OnSubscribe<kotlin.String> { subscriber ->
            try {
                val content = fetchData2("https://www.google.com")
                subscriber.onStart()
                subscriber.onNext(content)
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        })

        yahooObservable.subscribeOn(Schedulers.newThread())
        googleObservable.subscribeOn(Schedulers.newThread())

        val zippedObservable = Observable.zip(yahooObservable, googleObservable) { yahooString, appleString ->
            val result = yahooString + "\n" + appleString
            Log.d(TAG, result)

            if (Looper.getMainLooper().thread === Thread.currentThread()) {
                Log.d(TAG, "Main Thread")
            } else {
                Log.d(TAG, "Not Main Thread")
            }

            Toast.makeText(this@RxJavaActivity, result, Toast.LENGTH_SHORT).show()
            result
        }

        zippedObservable.subscribe { s -> Toast.makeText(this@RxJavaActivity, s, Toast.LENGTH_SHORT).show() }
    }

    fun senarioThreadDependency() {
        val yahooObservable = Observable.create(Observable.OnSubscribe<kotlin.String> { subscriber ->
            try {
                val content = fetchData2("https://www.yahoo.com")
                subscriber.onStart()
                subscriber.onNext(content)
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        })

        val googleObservable = Observable.create(Observable.OnSubscribe<kotlin.String> { subscriber ->
            try {
                val content = fetchData2("https://www.google.com")
                subscriber.onStart()
                subscriber.onNext(content)
                subscriber.onCompleted()
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        })

        yahooObservable.subscribeOn(Schedulers.newThread())
        googleObservable.subscribeOn(Schedulers.newThread())

        Observable.concat(yahooObservable, googleObservable).subscribe { s ->
            Log.d(TAG, "concat data: " + s)
            Toast.makeText(this@RxJavaActivity, "concat data: " + s, Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchData(urlString: String): String {
        var result = ""
        try {
            val client = OkHttpClient()

            val request = Request.Builder().url(urlString).build()
            val call = client.newCall(request)
            val response = call.execute()

            if (!response.isSuccessful) {
                result = "REQUEST_FAIL"
            } else {
                result = response.body().string()
            }
        } catch (e: IOException) {
            result = "REQUEST_FAIL"
        } finally {
            return result
        }
    }

    fun fetchData2(urlString: String): String {
        try {
            Thread.sleep(1000)
        } catch (ex: Exception) {

        } finally {
            return "You requested: " + urlString
        }
    }

    companion object {

        private val TAG = RxJavaActivity::class.java.simpleName
    }
}
