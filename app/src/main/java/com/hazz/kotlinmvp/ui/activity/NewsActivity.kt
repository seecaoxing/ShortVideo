package com.hazz.kotlinmvp.ui.activity

import android.support.v4.app.Fragment
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.base.BaseActivity
import com.hazz.kotlinmvp.base.BaseFragmentAdapter
import com.hazz.kotlinmvp.ui.fragment.CategoryFragment
import com.hazz.kotlinmvp.ui.fragment.FollowFragment
import com.hazz.kotlinmvp.utils.StatusBarUtil
import com.hazz.kotlinmvp.view.TabLayoutHelper
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * Created by bjcaoxing on 2019/1/24.
 */
class NewsActivity : BaseActivity() {

    private val tabList = ArrayList<String>()

    private val fragments = ArrayList<Fragment>()
    private var mTitle: String? = null
    override fun layoutId(): Int = R.layout.activity_news

    override fun initData() {

    }

    override fun initView() {
        //状态栏透明和间距处理
        this?.let { StatusBarUtil.darkMode(it) }
        this?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }

        tv_header_title.text = mTitle

        tabList.add("关注")
        tabList.add("分类")
        fragments.add(FollowFragment.getInstance("关注"))
        fragments.add(CategoryFragment.getInstance("分类"))

        /**
         * getSupportFragmentManager() 替换为getChildFragmentManager()
         */
        mViewPager.adapter = BaseFragmentAdapter(supportFragmentManager, fragments, tabList)
        mTabLayout.setupWithViewPager(mViewPager)
        TabLayoutHelper.setUpIndicatorWidth(mTabLayout)

    }

    override fun start() {

    }


}