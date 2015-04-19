
package com.seutao.core;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.seutao.entity.PublishedGood;
import com.seutao.entity.PublishedNeed;
import com.seutao.sharedata.ShareData;
public class DelayTimePage extends FragmentActivity
{
	private Context context=null;
	private ViewPager mViewPager=null;
	private FragmentPagerAdapter mAdapter=null;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private Button mBtnGoods;
	private Button mBtnNeeds;
	private Button btnSubmit=null;
	private ImageView goBackIv;
	private TextView topTv;
	private DelayGoodsList mDelayGoodsList=null;
	private DelayNeedsList mDelayNeedsList=null;
	private int currentIndex=0;
	private CheckBox btnSelectAll=null;
	private ImageView belowGoodsIv;
	private ImageView belowNeedsIv;
    private final static String title="选择商品/求购"; 
	private List<PublishedGood>mDelayGoods=new ArrayList<PublishedGood>();
	private List<PublishedNeed> mDelayNeeds=new ArrayList<PublishedNeed>();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delay_time_page);
		context=this;
		goBackIv=(ImageView)findViewById(R.id.listlayout_top_back);
		topTv=(TextView)findViewById(R.id.listlayout_top_text);
		topTv.setText(title);
		belowGoodsIv=(ImageView)findViewById(R.id.delay_page_btn_goods_below);
		belowNeedsIv=(ImageView)findViewById(R.id.delay_page_btn_needs_below);
		goBackIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDelayGoods.clear();
				mDelayNeeds.clear();
				Intent intent = new Intent();
				intent.setAction("android.intent.action.PrivilegePage");
				startActivity(intent);
				((Activity)context).finish();
			}
		});
		btnSubmit=(Button)findViewById(R.id.listlayout_top_btn);
		btnSubmit.setText("完成");
		btnSelectAll=(CheckBox)findViewById(R.id.delay_time_page_cb_selectall);
		mViewPager = (ViewPager)findViewById(R.id.delay_time_page_viewpager);
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
		
			
		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s="";
				Intent intent = new Intent();
				switch (currentIndex) {
					case 0:
						for (int i = 0; i < mDelayGoods.size(); i++) {
							if (mDelayGoods.get(i).getIsSelect()) {
								s=s+mDelayGoods.get(i).getId()+"|";
							}
						}
						intent.putExtra("selectIds", s);
						intent.putExtra("currentIndex", 0);
						intent.setAction("android.intent.action.SelectMonthPage");
						startActivity(intent);
						((Activity)context).finish();
						break;
					case 1:
						for (int i = 0; i < mDelayNeeds.size(); i++) {
							if (mDelayNeeds.get(i).getIsSelect()) {
								s=s+mDelayNeeds.get(i).getId()+"|";
							}
						}
						intent.putExtra("selectIds", s);
						intent.putExtra("currentIndex", 1);
						intent.setAction("android.intent.action.SelectMonthPage");
						startActivity(intent);
						((Activity)context).finish();
						break;
					default:
						break;
					}
				}
			});
		btnSelectAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShareData.delayIsSelectAll=!ShareData.delayIsSelectAll;
				btnSelectAll.setChecked(ShareData.delayIsSelectAll);
				switch (currentIndex) {
				case 0:
					for (int i = 0; i < mDelayGoods.size(); i++) {
						mDelayGoods.get(i).setIsSelect(ShareData.delayIsSelectAll);
					}
					mDelayGoodsList.getAdapter().notifyDataSetChanged();
					break;
				case 1:
					for (int j = 0; j < mDelayNeeds.size(); j++) {
						mDelayNeeds.get(j).setIsSelect(ShareData.delayIsSelectAll);
					}
					mDelayNeedsList.getAdapter().notifyDataSetChanged();
				default:
					break;
				}
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPageSelected(int position)
			{
				resetTabBtn();
				switch (position)
				{
				case 1:
					belowNeedsIv.setImageDrawable(getResources().getDrawable(R.color.yellow));
					for (int i = 0; i < mDelayNeeds.size(); i++) {
						mDelayNeeds.get(i).setIsSelect(false);
					}
					mDelayNeedsList.getAdapter().notifyDataSetChanged();
					break;
				case 0:
					belowGoodsIv.setImageDrawable(getResources().getDrawable(R.color.yellow));
					for (int j = 0; j < mDelayGoods.size(); j++) {
						mDelayGoods.get(j).setIsSelect(false);
					}
					mDelayGoodsList.getAdapter().notifyDataSetChanged();
					break;
				}
				currentIndex = position;
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
		belowGoodsIv.setImageDrawable(getResources().getDrawable(R.color.main_color));
		belowNeedsIv.setImageDrawable(getResources().getDrawable(R.color.main_color));
		
	}

	private void initView()
	{

		mBtnGoods=(Button) findViewById(R.id.delay_time_page_btn_goods);
		mBtnGoods.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(0);
			}
		});
		mBtnNeeds=(Button) findViewById(R.id.delay_time_page_btn_needs);
		mBtnNeeds.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(1);
			}
		});
		DelayGoodsList.initContext(this);
		mDelayGoodsList=new DelayGoodsList();
		mFragments.add(mDelayGoodsList);
		DelayNeedsList.initContext(this);
		mDelayNeedsList=new DelayNeedsList();
		mFragments.add(mDelayNeedsList);

	}
	
}

