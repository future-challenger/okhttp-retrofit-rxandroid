package okhttp.demo.com.okhttpdemo.TouchDemo

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ViewGroup
import android.widget.ImageView
import okhttp.demo.com.okhttpdemo.R

class ScaleActivity : Activity() {

    private var mImageView: ImageView? = null

    private var mDetector: ScaleGestureDetector? = null
    private var mScaleFactor: Float = 1.0f

    private var mOriginalLayoutParams: ViewGroup.LayoutParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)

        mImageView = findViewById(R.id.drag_drop_image_view) as ImageView
        mDetector = ScaleGestureDetector(this@ScaleActivity, MyScaleListener())
        mOriginalLayoutParams = mImageView?.layoutParams
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDetector?.onTouchEvent(event)
        return true
    }

    inner class MyScaleListener() : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            mScaleFactor *= (detector?.scaleFactor ?: 1.0f)

            // 不要变太小或者太大
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f))

            var width = mOriginalLayoutParams!!.width * mScaleFactor
            var height = mOriginalLayoutParams!!.height * mScaleFactor

            mImageView?.layoutParams?.width = width as Int
            mImageView?.layoutParams?.height = height as Int

            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return super.onScaleBegin(detector)
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            super.onScaleEnd(detector)
        }
    }
}
