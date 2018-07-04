package com.example.app.widget

interface OnItemCallbackListener {
    /**
     * @param fromPosition 起始位置
     * @param toPosition   移动的位置
     */
    fun onMove(fromPosition: Int, toPosition: Int)

    fun onSwipe(position: Int)
}