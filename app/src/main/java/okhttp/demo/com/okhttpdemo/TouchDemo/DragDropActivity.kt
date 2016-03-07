package okhttp.demo.com.okhttpdemo.TouchDemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.MotionEventCompat
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import okhttp.demo.com.okhttpdemo.R

class DragDropActivity : Activity() {
    private val INVALID_POINTER_ID = -1
    private val TAG = DragDropActivity::class.java.simpleName

    private var mImageView: ImageView? = null
    private var mActivePointerId = INVALID_POINTER_ID;

    private var mScaleDetector: ScaleGestureDetector? = null
    private var mLastTouchX: Float? = null
    private var mLastTouchY: Float? = null

    private var mPosX: Float? = null
    private var mPosY: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_drag_drop)

        mImageView = findViewById(R.id.drag_drop_image_view) as ImageView

        findViewById(R.id.another_drag_drop_button).setOnClickListener {
            val i = Intent(this@DragDropActivity, DragDropScrollActivity::class.java)
            startActivity(i)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var action = MotionEventCompat.getActionMasked(event)

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val pointerIndex = MotionEventCompat.getActionIndex(event!!)
                val x = MotionEventCompat.getX(event!!, pointerIndex)
                val y = MotionEventCompat.getY(event!!, pointerIndex)

                mLastTouchX = x
                mLastTouchY = y

                mActivePointerId = MotionEventCompat.getPointerId(event, 0)
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = MotionEventCompat.findPointerIndex(event!!, mActivePointerId)
                val x = MotionEventCompat.getX(event!!, pointerIndex)
                val y = MotionEventCompat.getY(event!!, pointerIndex)

                val dx = x - mLastTouchX!!
                val dy = y - mLastTouchY!!

                mPosX = mPosX ?: 0.0f + dx
                mPosY = mPosY ?: 0.0f + dy

                //                mImageView?.x = mPosX!!
                //                mImageView?.y = mPosY!!
                mImageView?.x = x
                mImageView?.y = y

                Log.d("##DRAG", "Pointer x- $x | y- $y")
                Log.d("##DRAG", "View x- ${mImageView?.x} y- ${mImageView?.y}")

                mLastTouchX = x
                mLastTouchY = y


            }
            MotionEvent.ACTION_UP -> {
                mActivePointerId = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = MotionEventCompat.getActionIndex(event!!)
                val pointerId = MotionEventCompat.getPointerId(event!!, pointerIndex)

                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mLastTouchX = MotionEventCompat.getX(event!!, newPointerIndex)
                    mLastTouchY = MotionEventCompat.getY(event!!, newPointerIndex)
                    mActivePointerId = MotionEventCompat.getPointerId(event!!, newPointerIndex)
                }
            }
        }

        return true
    }
}
