package okhttp.demo.com.okhttpdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp.demo.com.okhttpdemo.KotlinDemo.KotlinActivity;
import okhttp.demo.com.okhttpdemo.RxJavaDemo.RxJavaActivity;
import okhttp.demo.com.okhttpdemo.retrofitDemo.RetrofitActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private final static String TAG = "###okHttp";

    private final static int REQUEST_FAIL = 10101010;
    private final static int REQUEST_SUCCESS = 2001010101;

    private Handler requestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    Toast.makeText(MainActivity.this, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_FAIL:
                    Toast.makeText(MainActivity.this, "request failed", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView sectionListView = (ListView) findViewById(R.id.section_listview);
        List<String> ds = new ArrayList<>();
        ds.add("sync request");
        ds.add("async request");
        ds.add("retrofit");
        ds.add("RxJava in Android");
        ds.add("Kotlin demo");
        sectionListView.setAdapter(new ItemAdapter(MainActivity.this, android.R.layout.simple_list_item_1, ds));
        sectionListView.setOnItemClickListener(this);

        ExecutorService cachedExecutor = Executors.newCachedThreadPool();
        cachedExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            performSyncHttpRequest();
        } else if (position == 1) {
            performAsyncHttpRequest();
        } else if (position == 2) {
            Intent i = new Intent(MainActivity.this, RetrofitActivity.class);
            startActivity(i);
        } else if (position == 3){
            Intent i = new Intent(MainActivity.this, RxJavaActivity.class);
            startActivity(i);
        }else if(position == 4) {
            Intent i = new Intent(MainActivity.this, KotlinActivity.class);
            startActivity(i);
        }
    }

    private void performSyncHttpRequest() {

        Runnable requestTask = new Runnable() {
            @Override
            public void run() {
                Message msg = requestHandler.obtainMessage();
                try {
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("http://www.baidu.com")
                            .build();
                    Call call = client.newCall(request);
                    Response response = call.execute();

                    if (!response.isSuccessful()) {
                        msg.what = REQUEST_FAIL;
                    } else {
                        Log.d(TAG, response.body().string());
                        msg.what = REQUEST_SUCCESS;
                    }
                } catch (IOException ex) {
                    msg.what = REQUEST_FAIL;
                } finally {
                    // send the message
                    msg.sendToTarget();
                }
            }
        };

        Thread requestThread = new Thread(requestTask);
        requestThread.start();
    }

    private void performAsyncHttpRequest() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String errorMMessage = e.getMessage();

                if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                    Log.d(TAG, "Main Thread");
                } else {
                    Log.d(TAG, "Not Main Thread");
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, errorMMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                    Log.d(TAG, "Main Thread");
                } else {
                    Log.d(TAG, "Not Main Thread");
                }

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "code: ");

                        Toast.makeText(MainActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    }
                });

                MainActivity.this.mView.post(new Runnable() {
                    public void run() {
                        Log.d("UI thread", "I am the UI thread");
                    }
                });
            }
        });
    }

    private static class ItemAdapter extends ArrayAdapter<String> {

        public ItemAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }
    }
}
