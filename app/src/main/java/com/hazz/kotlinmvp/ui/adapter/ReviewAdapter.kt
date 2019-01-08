package com.hazz.kotlinmvp.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.mvp.model.bean.ReviewBean
import com.hazz.kotlinmvp.view.recyclerview.ViewHolder
import com.hazz.kotlinmvp.view.recyclerview.adapter.CommonAdapter

class ReviewAdapter(context: Context, data: ArrayList<ReviewBean>)
    : CommonAdapter<ReviewBean>(context, data, -1) {


    /**
     * 加载布局
     */
    private fun inflaterView(mLayoutId: Int, parent: ViewGroup): View {
        //创建view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return view ?: View(parent.context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder(inflaterView(R.layout.item_review_layout, parent))

    }

    override fun bindData(holder: ViewHolder, data: ReviewBean, position: Int) {

        holder.setText(R.id.mReviewAutorName, data.reviewAutorName)
        holder.setText(R.id.mReviewContent, data.reviewContent)
        holder.setText(R.id.mReviewTime, data.reviewTime)

    }


    override fun getItemCount(): Int {

        if (mData == null || mData.size == 0) {
            return 0
        } else {
            return mData.size
        }


    }

    /**
     * 添加更多数据
     */
    fun addMoreData(itemList: ArrayList<ReviewBean>) {
        this.mData.addAll(itemList)
        notifyDataSetChanged()
    }

    fun addItemData(mReviewData: ReviewBean) {
        this.mData.add(0, mReviewData)
        notifyDataSetChanged()

    }
}