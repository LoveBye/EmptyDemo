package com.example.app.adapter

import android.app.Dialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.Window
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.example.app.R
import com.example.app.utils.LogUtils
import com.example.app.widget.OnItemCallbackListener
import java.util.*
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


/**
 * 基本用法：https://www.jianshu.com/p/1e20f301272e
 *
 * @param <T>
 * @param <K>
</K></T> */
open class BaseAdapter<T, K : BaseViewHolder> : BaseQuickAdapter<T, K>, OnItemCallbackListener {
    var mAlertDialog: Dialog? = null
    val callback = OnItemCallbackHelper()
    var mCanSwipe = false
    /**
     * 实例化ItemTouchHelper对象,然后添加到RecyclerView
     */
    val mItemTouchHelper = ItemTouchHelper(callback)

    constructor(layoutResId: Int, data: List<T>?) : super(layoutResId, data) {
        initAdapterDuang()
    }

    constructor(data: List<T>?) : super(data) {
        initAdapterDuang()
    }

    constructor(layoutResId: Int) : super(layoutResId) {
        initAdapterDuang()
    }

    override fun convert(helper: K, item: T) {

    }

    private fun initAdapterDuang() {
        /**
         * openLoadAnimation()
         * 5种动画效果（渐显、缩放、从下到上，从左到右、从右到左）
         * ALPHAIN = 0x00000001;
         * SCALEIN = 0x00000002;
         * SLIDEIN_BOTTOM = 0x00000003;
         * SLIDEIN_LEFT = 0x00000004;
         * SLIDEIN_RIGHT = 0x00000005;
         */
        openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        /**
         * 如果需要在子控件事件中获取其他子控件可以使用：
         * getViewByPosition(RecyclerView recyclerView, int position, @IdRes int viewId)
         */
        /**
         * 默认第一次加载会进入加载更多的回调，如果不需要可以配置：
         * disableLoadMoreIfNotFullPage();
         */
        /**
         * 加载完成（注意不是加载结束，而是本次数据加载结束并且还有下页数据）
         * mQuickAdapter.loadMoreComplete();
         * 加载失败
         * mQuickAdapter.loadMoreFail();
         * 加载结束
         * mQuickAdapter.loadMoreEnd();
         */
        /**
         * 注意：如果上拉结束后，下拉刷新需要再次开启上拉监听，需要使用setNewData方法填充数据。
         * 打开或关闭加载（一般用于下拉的时候做处理，因为上拉下拉不能同时操作）
         * mQuickAdapter.setEnableLoadMore(boolean);
         */
        /**
         * 预加载(这个功能屌炸天)
         * 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
         * mQuickAdapter.setPreLoadNumber(int);
         */
        setPreLoadNumber(1)
        /**
         * 设置自定义加载更多的布局
         * mQuickAdapter.setLoadMoreView(new CustomLoadMoreView());
         */
        setLoadMoreView(SimpleLoadMoreView())
        /**
         * 下拉加载（符合聊天软件下拉历史数据需求）
         * 1、这里的下拉加载我个人觉得不好用，并不像我们常用的那种下拉加载（当然有可能是我没有合理使用）
         * 2、所以如果是需要下拉刷新的功能，我个人会使用SwipeRefreshLayout
         * 设置开启开关
         * mAdapter.setUpFetchEnable(true);
         * 开始加载的位置
         * mAdapter.setStartUpFetchPosition(2);
         */
//        adater.helper.attachToRecyclerView(rlv)//设置可拖动
//        canSwipe = true//设置可滑动删除
    }


    //拖动
    override fun onMove(fromPosition: Int, toPosition: Int) {
        try {
            /**
             * 在这里进行给原数组数据的移动
             */
            Collections.swap(mData, fromPosition, toPosition)
            /**
             * 通知数据移动
             */
            notifyItemMoved(fromPosition, toPosition)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSwipe(position: Int) {
        if (mCanSwipe) {
            showAlertDialog(position)
        } else {
//            notifyItemChanged(position)//加这句，不然就滑没了
            notifyDataSetChanged()
        }
    }

    inner class OnItemCallbackHelper : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlag = ItemTouchHelper.LEFT or ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.RIGHT
            val swipeFlag = ItemTouchHelper.START or ItemTouchHelper.END
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlag, swipeFlag)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            onMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            onSwipe(viewHolder.adapterPosition)
        }
    }

    /**
     * 显示提示框
     */
    fun showAlertDialog(position: Int) {
        mAlertDialog = Dialog(mContext)
        val window = mAlertDialog!!.window
        mAlertDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)//设置Dialog没有标题。需在setContentView之前设置，在之后设置会报错
        window!!.setContentView(R.layout.base_dialog)
        val tv_hint = window.findViewById<TextView>(R.id.tv_dialog_hint)
        val tvConfirm = window.findViewById<TextView>(R.id.tv_dialog_confirm)
        val tvCancel = window.findViewById<TextView>(R.id.tv_dialog_cancel)
        tv_hint.setText("确定删除？")
        tvCancel.setOnClickListener {
            mAlertDialog!!.dismiss()
//            notifyItemChanged(position)
            notifyDataSetChanged()
        }
        tvConfirm.setOnClickListener {
            mAlertDialog!!.dismiss()
            remove(position)
//            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
        mAlertDialog!!.show()
    }
}