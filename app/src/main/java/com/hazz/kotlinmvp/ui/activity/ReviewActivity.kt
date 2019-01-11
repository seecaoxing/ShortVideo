package com.hazz.kotlinmvp.ui.activity

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.base.BaseActivity
import com.hazz.kotlinmvp.mvp.contract.ReviewContract
import com.hazz.kotlinmvp.mvp.model.bean.ReviewBean
import com.hazz.kotlinmvp.mvp.presenter.ReviewPresenter
import com.hazz.kotlinmvp.ui.adapter.ReviewAdapter
import com.hazz.kotlinmvp.utils.StatusBarUtil
import com.utils.baseutils.DateTimeUtil
import com.utils.baseutils.DateTimeUtil.LONG_DATE_TIME_FORMAT_SECOND
import kotlinx.android.synthetic.main.activity_review.*

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


        val decorView = this.getWindow().getDecorView()
        val contentView = this.findViewById<View>(Window.ID_ANDROID_CONTENT)

        decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            decorView.getWindowVisibleDisplayFrame(r)

            val height = decorView.getContext().getResources().getDisplayMetrics().heightPixels
            val diff = height - r.bottom

            if (diff != 0) {
                if (contentView.getPaddingBottom() !== diff) {
                    contentView.setPadding(0, 0, 0, diff)
                }
            } else {
                if (contentView.getPaddingBottom() !== 0) {
                    contentView.setPadding(0, 0, 0, 0)
                }
            }
        }

        sendReviewBtn.setOnClickListener {
            var reviewText = editReview.text.toString()
            if (!TextUtils.isEmpty(reviewText.trim())) {
                var reviewBean = ReviewBean("111", "小李", reviewText, DateTimeUtil.getDateFormat(LONG_DATE_TIME_FORMAT_SECOND))
                mPresenter.addReviewDta(reviewBean)
            }
        }


    }

    override fun start() {

    }

    override fun addReview(mReview: ReviewBean) {
        loadingMore = false
        mReviewAdapter?.addItemData(mReview)
        mReviewList.smoothScrollToPosition(0)
        editReview.text.clear()

        closeSoftKeyBoard(sendReviewBtn)

    }

    fun closeSoftKeyBoard(view: View) {
        //隐藏软键盘
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0) //强制隐藏键盘
    }

    override fun setReviewData(reviewList: ArrayList<ReviewBean>) {
        mReviewAdapter = this?.let { ReviewAdapter(it, reviewList) }
        mReviewList.adapter = mReviewAdapter
        mReviewList.layoutManager = linearLayoutManager


    }

    override fun setMoreData(moreItemList: ArrayList<ReviewBean>) {
        loadingMore = false
        mReviewAdapter?.addMoreData(moreItemList)

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