package okhttp.demo.com.okhttpdemo.TouchDemo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView

import okhttp.demo.com.okhttpdemo.R

class DragDropScrollActivity : Activity() {
    private val TAG: String = DragDropScrollActivity::class.java.simpleName

    val mPanGesture: GestureDetector by lazy {
        GestureDetector(this@DragDropScrollActivity, MySimpleOnGestureListener())
    }

    var mImageView: ImageView? = null
    var mPosX: Float? = null
    var mPosY: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_drop_scroll)

        mImageView = findViewById(R.id.drag_drop_image_view) as ImageView
        mPosX = mImageView?.x
        mPosY = mImageView?.y
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mPanGesture.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    inner class MySimpleOnGestureListener() : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            mImageView?.x = mPosX!! - distanceX
            mImageView?.y = mPosY!! - distanceY

            mPosX = mImageView?.x
            mPosY = mImageView?.y

            Log.d(TAG, "distance X: $distanceX, Y: $distanceY")
            Log.d(TAG, "position X: ${mImageView!!.x} Y: ${mImageView!!.y}")

            return true
        }
    }
}
