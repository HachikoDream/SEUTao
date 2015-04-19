package com.seutao.core;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ImageView;

public class HomePage extends FragmentActivity {
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments ;
	/**
	 * 顶部控件
	 */
	private Button introduceButton;
	private Button latestButton;
	private ImageView introduceIv;
	private ImageView latestIv;
	private ImageView backIv;
	private Button searchBtn;
	private final int INTRODUCE_BUTTON_PRESSED=0;
	private final int LATEST_BUTTON_PRESSED=1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		mViewPager=(ViewPager)findViewById(R.id.home_page_viewpager);
		initTopView();
		setIconOnTop(INTRODUCE_BUTTON_PRESSED);
		mFragments= new ArrayList<Fragment>();
		HomePagePartOne hpo=new HomePagePartOne();
		HomePagePartTwo hpt=new HomePagePartTwo();
		mFragments.add(hpo);
		mFragments.add(hpt);
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{

			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int positon)
			{
				return mFragments.get(positon);
			}
		};
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int positon) {
				// TODO Auto-generated method stub
				System.out.println("onPageSelected!");
				setIconOnTop(positon);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				System.out.println("onPageScrolled!");
			}
			
			@Override
			public void onPageScrollStateChanged(int arg1) {
				// TODO Auto-generated method stub
				System.out.println("onPageScrollStateChanged!  positon:"+arg1);
			}
		});

	}
	private void initTopView(){
		introduceButton=(Button)findViewById(R.id.home_page_introduce_goods_button);
		latestButton=(Button)findViewById(R.id.home_page_latest_needs_button);
		introduceIv=(ImageView)findViewById(R.id.home_page_introduce_goods_img);
		latestIv=(ImageView)findViewById(R.id.home_page_latest_needs_img);
		backIv=(ImageView)findViewById(R.id.alllayout_top_back);
		backIv.setVisibility(View.INVISIBLE);
		searchBtn=(Button)findViewById(R.id.alllayout_top_btn);
		searchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent =new Intent(HomePage.this,SearchResultPage.class);
				startActivity(intent);
			}
		});
		introduceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIconOnTop(0);
				mViewPager.setCurrentItem(0, true);
			}
		});
		latestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setIconOnTop(1);
				mViewPager.setCurrentItem(1,true);
			}
		});
	}
	private void setIconOnTop(int event){
		switch (event) {
		case INTRODUCE_BUTTON_PRESSED:
			backtoCommonOnTop();
			introduceIv.setImageDrawable(getResources().getDrawable(R.color.yellow));
			latestButton.setTextColor(getResources().getColor(R.color.nearwhite));
			break;
        case LATEST_BUTTON_PRESSED:
			backtoCommonOnTop();
			latestIv.setImageDrawable(getResources().getDrawable(R.color.yellow));
			introduceButton.setTextColor(getResources().getColor(R.color.nearwhite));
			break;
		
		}
	}
	private void backtoCommonOnTop(){
		introduceButton.setTextColor(getResources().getColor(R.color.white));
		latestButton.setTextColor(getResources().getColor(R.color.white));
		introduceIv.setImageDrawable(getResources().getDrawable(R.color.main_color));
		latestIv.setImageDrawable(getResources().getDrawable(R.color.main_color));
	}
	
	}
	
	

