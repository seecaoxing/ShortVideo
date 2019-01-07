package com.hazz.kotlinmvp.mvp.model

import com.hazz.kotlinmvp.mvp.model.bean.ReviewBean

class ReviewModel {

    fun requestReviewData(num: Int): ArrayList<ReviewBean> {
        var reviewList = ArrayList<ReviewBean>()
        var ints = 0 until num
        for (i: Int in ints) {
            var mReviewBean = ReviewBean(i.toString(), "小明${i}", "我今天评论了${i},", "2019.01.${i}")
            reviewList.add(mReviewBean)
        }
        return reviewList

    }

    fun loadMoreData(): ArrayList<ReviewBean> {

        var reviewList = ArrayList<ReviewBean>()
        var ints = 0 until 10
        for (i: Int in ints) {
            var mReviewBean = ReviewBean(i.toString(), "小明${i}", "我今天评论了${i},", "2019.01.${i}")
            reviewList.add(mReviewBean)
        }
        return reviewList
    }

}