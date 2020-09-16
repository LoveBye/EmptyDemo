package com.example.app.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller

import com.chad.library.adapter.base.BaseViewHolder
import com.example.app.R

/**
 * 侧滑删除-自定义recyclerview
 * https://github.com/RuijiePan/SlidingDeleteRecyclerView
 * github源码地址
 */
class SlidingDeleteRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {
    //检测滑动速度
    private val mVelocityTracker: VelocityTracker
    private val mScroller: Scroller
    //4种状态，分别为关闭、正在关闭、正在打开、打开
    private var status = CLOSE
    private var mItemView: View? = null
    //删除图片的宽度
    private var mMaxLength: Int = 0
    //OnTouch点的位置
    private var mLastX: Int = 0
    private var mLastY: Int = 0
    //是否开始滑动
    private var isStartScroll: Boolean = false
    init {
        mScroller = Scroller(context, LinearInterpolator())
        mVelocityTracker = VelocityTracker.obtain()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent): Boolean {
        mVelocityTracker.addMovement(e)
        //获取当前坐标
        val x = e.x.toInt()
        val y = e.y.toInt()
        when (e.action) {
            MotionEvent.ACTION_DOWN ->
                //删除图片还没打开的状态
                if (status == CLOSE) {
                    //寻找对应坐标点下的V
                    val view = findChildViewUnder(x.toFloat(), y.toFloat()) ?: return false
                    //通过baseviewholder获取对应的子View，详情可以看代码
                    val viewHolder = getChildViewHolder(view) as BaseViewHolder
                    mItemView = viewHolder.getView(R.id.item_layout)
                    val mSlidingView = viewHolder.getView<View>(R.id.item_sliding)
                    mMaxLength = mSlidingView.width
                    //当删除图片已经完全显示的时候
                } else if (status == OPEN) {
                    //从当前view的偏移点mItemView.getScrollX()，位移-mMaxLength长度单位
                    // 时间DEFAULT_TIMEms，向左移动为正数
                    mScroller.startScroll(mItemView!!.scrollX, 0, -mMaxLength, 0, DEFAULT_TIME)
                    //刷新下一帧动画
                    invalidate()
                    status = CLOSE
                    return false
                } else {
                    return false
                }
            MotionEvent.ACTION_MOVE -> {
                //获取上次的落点与当前的坐标之间的差值
                val dx = mLastX - x
                val dy = mLastY - y
                val scrollX = mItemView!!.scrollX
                //水平滑动距离大于垂直距离
                if (Math.abs(dx) > Math.abs(dy)) {
                    //向左滑动，直至显示删除图片，向左滑动的最大距离不超过删除图片的宽度
                    if (scrollX + dx >= mMaxLength) {
                        mItemView!!.scrollTo(mMaxLength, 0)
                        return true
                        //向右滑动，直至删除图片不显示，向右滑动的最大距离不超过初始位置
                    } else if (scrollX + dx <= 0) {
                        mItemView!!.scrollTo(0, 0)
                        return true
                    }
                    //如果在图片还未完全显示的状态下，那么手指滑动多少，图片就移动多少
                    mItemView!!.scrollBy(dx, 0)
                }
            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker.computeCurrentVelocity(1000)//计算手指滑动的速度
                val xVelocity = mVelocityTracker.xVelocity//水平方向速度（向左为负）
                val yVelocity = mVelocityTracker.yVelocity//垂直方向速度
                val upScrollX = mItemView!!.scrollX
                var deltaX = 0
                //向右滑动速度为正数
                //滑动速度快的状态下抬起手指，计算所需偏移量
                if (Math.abs(xVelocity) > Math.abs(yVelocity) && Math.abs(xVelocity) >= VELOCITY) {
                    //向右隐藏
                    if (xVelocity >= VELOCITY) {
                        deltaX = -upScrollX
                        status = CLOSING
                    } else if (xVelocity <= -VELOCITY) {
                        deltaX = mMaxLength - upScrollX
                        status = OPENING
                    }
                } else {
                    if (upScrollX >= mMaxLength / 2) {
                        deltaX = mMaxLength - upScrollX
                        status = OPENING
                    } else {
                        deltaX = -upScrollX
                        status = CLOSING
                    }
                }
                mScroller.startScroll(upScrollX, 0, deltaX, 0, DEFAULT_TIME)
                isStartScroll = true
                invalidate()
                mVelocityTracker.clear()
            }
        }
        mLastX = x
        mLastY = y
        return super.onTouchEvent(e)
    }

    override fun computeScroll() {
        //滚动是否完成，true表示还未完成
        if (mScroller.computeScrollOffset()) {
            mItemView!!.scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
            //有滑动，并且在滑动结束的时候
        } else if (isStartScroll) {
            isStartScroll = false
            if (status == CLOSING)
                status = CLOSE
            if (status == OPENING)
                status = OPEN
        }
    }

    override fun onDetachedFromWindow() {
        mVelocityTracker.recycle()
        super.onDetachedFromWindow()
    }

    fun slidingFinish() {
        //返回原点
        mItemView!!.scrollTo(0, 0)
        status = CLOSE
    }

    companion object {
        val CLOSE = 0
        val CLOSING = 1
        val OPENING = 2
        val OPEN = 3
        //滑动速度临界值
        val VELOCITY = 100
        //默认的滑动时间
        val DEFAULT_TIME = 200
    }
}