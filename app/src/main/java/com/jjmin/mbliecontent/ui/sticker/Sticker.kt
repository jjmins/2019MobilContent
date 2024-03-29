package com.jjmin.mbliecontent.ui.sticker

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PointF
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.jjmin.mbliecontent.ui.food.FoodInfoActivity

class Sticker(context: Context, bitmap: Bitmap, color: Int, id: Int, num: Int,x : Float?, y : Float?) :
    BaseSticker(context, bitmap,x,y) {

    private val mLastSinglePoint = PointF()//스크린의 커튼을 가리키는 점
    private val mLastDistanceVector = PointF()//백테계산
    private val mDistanceVector = PointF()//두손가락 사이의 백터
    private var mLastDistance: Float = 0.toFloat()//손가락 사이의 길이
    var num : Int = num
    var color = color
    var id = id

    private val mFirstPoint = PointF()
    private val mSecondPoint = PointF()

    /**
     * 초기화
     */
    fun reset() {
        mLastSinglePoint.set(0f, 0f)
        mLastDistanceVector.set(0f, 0f)
        mDistanceVector.set(0f, 0f)
        mLastDistance = 0f
        mMode = BaseSticker.MODE_NONE
    }

    /**
     */
    fun calculateDistance(firstPointF: PointF, secondPointF: PointF): Float {
        val x = firstPointF.x - secondPointF.x
        val y = firstPointF.y - secondPointF.y
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }


    /**
     *
     * @param lastVector
     * @param currentVector
     * @return
     */
    fun calculateDegrees(lastVector: PointF, currentVector: PointF): Float {
        val lastDegrees = Math.atan2(lastVector.y.toDouble(), lastVector.x.toDouble()).toFloat()
        val currentDegrees = Math.atan2(currentVector.y.toDouble(), currentVector.x.toDouble()).toFloat()
        return Math.toDegrees((currentDegrees - lastDegrees).toDouble()).toFloat()
    }

    var gestureDetector = GestureDetector(object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            var intent =  Intent(context, FoodInfoActivity::class.java)
            intent.putExtra("id",id)
            context?.startActivity(intent)
            return true
        }
    })

    /**
     *
     * @param event
     */
    override fun onTouch(event: MotionEvent) {
        gestureDetector.onTouchEvent(event)
        when (event.action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN -> {
                //스티커 터치
                mMode = MODE_SINGLE
                //누른 위치 기록
                mLastSinglePoint.set(event.x, event.y)
            }
            MotionEvent.ACTION_POINTER_DOWN -> if (event.pointerCount == 2) {

                mMode = MODE_MULTIPLE
                //더블 클릭 위치 기록
                mFirstPoint.set(event.getX(0), event.getY(0))
                mSecondPoint.set(event.getX(1), event.getY(1))
                //두 손가락 사이의 벡터를 계산하다
                mLastDistanceVector.set(mFirstPoint.x - mSecondPoint.x, mFirstPoint.y - mSecondPoint.y)
                //두 손가락 사이의 거리를 계산하다
                mLastDistance = calculateDistance(mFirstPoint, mSecondPoint)
            }
            MotionEvent.ACTION_MOVE -> {
                if (mMode == BaseSticker.MODE_SINGLE) {
                    translate(event.x - mLastSinglePoint.x, event.y - mLastSinglePoint.y)
                    mLastSinglePoint.set(event.x, event.y)
                    x = event.x
                    y = event.y
                    Log.e("asdf", "x : ${event.x}  y : ${event.y}")
                }
                if (mMode == BaseSticker.MODE_MULTIPLE && event.pointerCount == 2) {
                    //더블 핑거의 점 위치 기록
                    mFirstPoint.set(event.getX(0), event.getY(0))
                    mSecondPoint.set(event.getX(1), event.getY(1))
                    //조작 자유
                    val distance = calculateDistance(mFirstPoint, mSecondPoint)
                    //두 손가락으로 이동한 거리에 따른 축척 인자 획득
                    val scale = distance / mLastDistance
                    scale(scale, scale)
                    mLastDistance = distance
                    //조작 자유 회전
//                    mDistanceVector.set(mFirstPoint.x - mSecondPoint.x, mFirstPoint.y - mSecondPoint.y)
//                    rotate(calculateDegrees(mLastDistanceVector, mDistanceVector))
//                    mLastDistanceVector.set(mDistanceVector.x, mDistanceVector.y)
                }
            }
            MotionEvent.ACTION_UP -> reset()
        }

    }
}
