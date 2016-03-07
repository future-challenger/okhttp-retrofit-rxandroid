package okhttp.demo.com.okhttpdemo.TouchDemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.VelocityTracker

import okhttp.demo.com.okhttpdemo.R

class TouchDemoActivity : Activity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    val TAG: String = TouchDemoActivity::class.java.simpleName
    val GES_TAG: String = "##GestureDetectorTAG"

    // do not use this keyword `lateinit`, if you forget to init it, something bad happens!!!
    //    lateinit var mDetector: GestureDetector
    lateinit var mSimpleDetector: GestureDetector

    var velocityTracker: VelocityTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch_demo)

        findViewById(R.id.touch_test_view).setOnTouchListener { view, event ->
            Log.d("View tag", "view touched")
            this@TouchDemoActivity.getTouchEventInfo(event)
        }

        //        mDetector = GestureDetector(this@TouchDemoActivity, this@TouchDemoActivity)
        //        mDetector.setOnDoubleTapListener(this@TouchDemoActivity)

        mSimpleDetector = GestureDetector(this@TouchDemoActivity, MyGestureListener())

        // navigation button
        findViewById(R.id.multitouch_button).setOnClickListener { v ->
            val i = Intent(this@TouchDemoActivity, MultiTouchActivity::class.java)
            startActivity(i)
        }
        findViewById(R.id.drag_drop_button).setOnClickListener { v ->
            val i = Intent(this@TouchDemoActivity, DragDropActivity::class.java)
            startActivity(i)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //        return getTouchEventInfo(event)
        //        this.mDetector.onTouchEvent(event)

        //        this.mSimpleDetector.onTouchEvent(event)

        getTouchEventInfo(event)
        return super.onTouchEvent(event)
    }

    private fun getTouchEventInfo(event: MotionEvent?): Boolean {

        var index = event!!.actionIndex
        var pointerId = event!!.getPointerId(index)
        val action = event!!.actionMasked

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "action down")

                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain()
                } else {
                    velocityTracker?.clear()
                }
                velocityTracker?.addMovement(event)

                //                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "action up")
                Log.d(TAG, "if you calculate velocity here, all values are ZERO")
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d(TAG, "action move")

                velocityTracker?.addMovement(event)
                velocityTracker?.computeCurrentVelocity(1000)   // `1000` time unit

                Log.d(TAG, "X velocity: " + velocityTracker?.getXVelocity(pointerId))
                Log.d(TAG, "Y velocity: " + velocityTracker?.getYVelocity(pointerId))

                //                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d(TAG, "action cancel")
                velocityTracker?.recycle()
                //                return true
            }
            MotionEvent.ACTION_OUTSIDE -> {
                Log.d(TAG, "action outside")
                return true
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                Log.d(TAG, "action pointer down")
                return true
            }
            MotionEvent.ACTION_POINTER_UP -> {
                Log.d(TAG, "action pointer up")
                return true
            }
            else -> return super.onTouchEvent(event)
        }
        return true
    }

    override fun onSingleTapUp(event: MotionEvent?): Boolean {
        Log.d(GES_TAG, "onSingleTapUp: " + event?.toString())
        return true
    }

    override fun onLongPress(event: MotionEvent?) {
        Log.d(GES_TAG, "onLongPress: " + event?.toString())
    }

    override fun onScroll(event1: MotionEvent?, event2: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.d(GES_TAG, "onScroll: " + event1!!.toString() + event2!!.toString())
        return true
    }

    override fun onDown(event: MotionEvent?): Boolean {
        Log.d(GES_TAG, "onDown: " + event!!.toString())
        return true
    }

    override fun onFling(event1: MotionEvent?, event2: MotionEvent?, p2: Float, p3: Float): Boolean {
        Log.d(GES_TAG, "onFling: " + event1!!.toString() + event2!!.toString())
        return true
    }

    override fun onShowPress(event: MotionEvent?) {
        Log.d(GES_TAG, "onShowPress: " + event!!.toString())
    }

    override fun onDoubleTapEvent(event: MotionEvent?): Boolean {
        Log.d(GES_TAG, "onDoubleTapEvent: " + event!!.toString())
        return true
    }

    override fun onDoubleTap(event: MotionEvent?): Boolean {
        Log.d(GES_TAG, "onDoubleTap: " + event!!.toString())
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
        Log.d(GES_TAG, "onSingleTapConfirmed: " + event!!.toString())
        return true
    }

    class MyGestureListener() : GestureDetector.SimpleOnGestureListener() {
        init {

        }

        override fun onDown(e: MotionEvent?): Boolean {

            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Log.d(MyGestureListener::class.java.simpleName, "onFling")
            return true
        }
    }
}
