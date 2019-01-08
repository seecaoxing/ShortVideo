package com.hazz.kotlinmvp.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.base.BaseActivity
import com.hazz.kotlinmvp.mvp.contract.ReviewContract
import com.hazz.kotlinmvp.mvp.model.bean.ReviewBean
import com.hazz.kotlinmvp.mvp.presenter.HomePresenter
import com.hazz.kotlinmvp.mvp.presenter.ReviewPresenter
import com.hazz.kotlinmvp.ui.adapter.HomeAdapter
import com.hazz.kotlinmvp.ui.adapter.ReviewAdapter
import com.hazz.kotlinmvp.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_recyclerview.*

class ReviewActivity : BaseActivity(), ReviewContract.View {


    private val mPresenter by lazy { ReviewPresenter() }


    private var num: Int = 20
    private var loadingMore = false

    private var mReviewAdapter: ReviewAdapter? = null


    private val linearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    override fun layoutId(): Int = R.layout.activity_review

    override fun initData() {

    }

    override fun initView() {

        mPresenter.attachView(this)

        mPresenter.requestReviewData(num)
        mReviewToolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, mReviewToolbar)

        mReviewList.addOnScrollListener(object : RecyclerView.OnScrollListener() {


            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val childCount = mReviewList.childCount
                    val itemCount = mReviewList.layoutManager.itemCount
                    val firstVisibleItem = (mReviewList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (firstVisibleItem + childCount == itemCount) {
                        if (!loadingMore) {
                            loadingMore = true
                            mPresenter.loadMoreData()
                        }
                    }
                }

            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }

        })


    }

    override fun start() {
    }

    override fun setReviewData(reviewList: ArrayList<ReviewBean>) {
        mReviewAdapter = this?.let { ReviewAdapter(it, reviewList) }
        mReviewList.adapter = mReviewAdapter
        mReviewList.layoutManager = linearLayoutManager


    }

    override fun setMoreData(moreItemList: ArrayList<ReviewBean>) {
        loadingMore = false
        mReviewAdapter?.addItemData(moreItemList)

    }

    override fun showError(msg: String, errorCode: Int) {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()

    }

}