package com.hazz.kotlinmvp.mvp.presenter

import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.mvp.contract.HomeContract
import com.hazz.kotlinmvp.mvp.contract.ReviewContract
import com.hazz.kotlinmvp.mvp.model.HomeModel
import com.hazz.kotlinmvp.mvp.model.ReviewModel
import com.hazz.kotlinmvp.mvp.model.bean.ReviewBean

class ReviewPresenter : BasePresenter<ReviewContract.View>(), ReviewContract.Presenter {

    private val reviewModel: ReviewModel by lazy {

        ReviewModel()
    }

    override fun requestReviewData(num: Int) {

        var revewList = reviewModel.requestReviewData(20)

        mRootView?.apply {
            setReviewData(revewList)
        }

    }

    override fun loadMoreData() {
        var revewList = reviewModel.loadMoreData()
        mRootView?.apply {
            setMoreData(revewList)
        }
    }

    override fun addReviewDta(mReview: ReviewBean) {
        var mReviewData = reviewModel.addReviewDta(mReview)
        mRootView?.apply {
            addReview(mReviewData)
        }
    }
}