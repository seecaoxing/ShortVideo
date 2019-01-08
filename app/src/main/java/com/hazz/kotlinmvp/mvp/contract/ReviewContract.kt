package com.hazz.kotlinmvp.mvp.contract

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.mvp.model.bean.ReviewBean

class ReviewContract {


    interface View : IBaseView {

        /**
         * 设置第一次请求的数据
         */
        fun setReviewData(reviewList: ArrayList<ReviewBean>)

        /**
         * 设置加载更多的数据
         */
        fun setMoreData(moreItemList: ArrayList<ReviewBean>)

        fun addReview(mReview: ReviewBean)

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)


    }

    interface Presenter : IPresenter<View> {

        /**
         * 获取首页精选数据
         */
        fun requestReviewData(num: Int)

        /**
         * 加载更多数据
         */
        fun loadMoreData()

        fun addReviewDta(mReview: ReviewBean)


    }
}