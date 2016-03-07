package okhttp.demo.com.okhttpdemo.TouchDemo

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import okhttp.demo.com.okhttpdemo.R

class ScaleActivity : Activity() {

    private var mImageView: ImageView? = null

    private var mDetector: ScaleGestureDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)

        mImageView = findViewById(R.id.drag_drop_image_view) as ImageView
        mDetector = ScaleGestureDetector(this@ScaleActivity, MyScaleListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    inner class MyScaleListener() : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            return super.onScale(detector)
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return super.onScaleBegin(detector)
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            super.onScaleEnd(detector)
        }
    }
}
