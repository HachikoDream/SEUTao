package com.seutao.core;

import java.util.ArrayList;
import java.util.List;

import com.seutao.sharedata.ShareData;

import android.content.Context;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class PublishedPage extends FragmentActivity
{
	private   Context context=null;
	private ViewPager mViewPager=null;
	private FragmentPagerAdapter mAdapter=null;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private Button mBtnGoods;
	private Button mBtnNeeds;
	private ImageView goBackIv;
	private TextView topTv;
	private Button topBtn;
	private ImageView belowGoodsIv;
	private ImageView belowNeedsIv;
	private int uid;
    private final static String title="发布列表";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_published_page);
		context=this;
		uid=getIntent().getIntExtra("uid", -1);
		goBackIv=(ImageView)findViewById(R.id.listlayout_top_back);
		topTv=(TextView)findViewById(R.id.listlayout_top_text);
		topBtn=(Button)findViewById(R.id.listlayout_top_btn);
		belowGoodsIv=(ImageView)findViewById(R.id.published_page_btn_goods_below);
		belowNeedsIv=(ImageView)findViewById(R.id.published_page_btn_needs_below);
		topTv.setText(title);
		topBtn.setVisibility(View.GONE);
		goBackIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity)context).finish();
			}
		});
		mViewPager = (ViewPager)findViewById(R.id.published_page_viewpager);
		initView();
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{
			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mFragments.get(arg0);
			}
		};
		
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			//private int currentIndex;
			@Override
			public void onPageSelected(int position)
			{
				resetTabBtn();
				switch (position)
				{
				case 0:
					belowGoodsIv.setImageDrawable(getResources().getDrawable(R.color.yellow));
					break;
				case 1:
					belowNeedsIv.setImageDrawable(getResources().getDrawable(R.color.yellow));
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		});

	}
	protected void resetTabBtn()
	{
		mBtnGoods.setBackgroundColor(getResources().getColor(R.color.main_color));
		mBtnNeeds.setBackgroundColor(getResources().getColor(R.color.main_color));
		belowGoodsIv.setImageDrawable(getResources().getDrawable(R.color.main_color));
		belowNeedsIv.setImageDrawable(getResources().getDrawable(R.color.main_color));
	}

	private void initView()
	{

		mBtnGoods=(Button) findViewById(R.id.published_page_btn_goods);
		mBtnGoods.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(0);
			}
		});
		mBtnNeeds=(Button) findViewById(R.id.published_page_btn_needs);
		mBtnNeeds.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(1);
			}
		});
		PublishedGoodsList.initContext(this);
		PublishedGoodsList mPublishedGoodsList=new PublishedGoodsList(uid);
		mFragments.add(mPublishedGoodsList);
		PublishedNeedsList.initContext(this);
		PublishedNeedsList mPublishedNeedsList=new PublishedNeedsList(uid);
		mFragments.add(mPublishedNeedsList);
	}

}

