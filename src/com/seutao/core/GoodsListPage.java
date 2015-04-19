package com.seutao.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.seutao.sharedata.ShareData;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsListPage extends FragmentActivity {

	private ViewPager mViewPager = null;
	private TextView goods_list_page_zonghe = null;
	private TextView goods_list_page_time = null;
	private TextView goods_list_page_price = null;
	private TextView goods_list_page_redu = null;
	private ZongheList zongheList = null;
	private TimeList timeList = null;
	private PriceList priceList = null;
	private ReduList reduList = null;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private List<Map<String, Object>> goods = new ArrayList<Map<String, Object>>();
	private int lowOrHigh = 0;
	private int pageKind = 0;
	private int kind = 0;
	private TextView topTv;
	private ImageView goBackIv;
	private final static String title="商品列表";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_goods_list_page);
		pageKind = getIntent().getIntExtra("pageKind", 0);
		kind = getIntent().getIntExtra("kind", 0);
		mViewPager = (ViewPager) findViewById(R.id.goods_list_page_list);
		goods_list_page_zonghe = (TextView) findViewById(R.id.goods_list_page_zonghe);
		goods_list_page_time = (TextView) findViewById(R.id.goods_list_page_time);
		goods_list_page_price = (TextView) findViewById(R.id.goods_list_page_price);
		goods_list_page_redu = (TextView) findViewById(R.id.goods_list_page_redu);
		topTv=(TextView)findViewById(R.id.allayout_top_text);
		goBackIv=(ImageView)findViewById(R.id.alllayout_top_back);
		topTv.setText(title);
		goBackIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		initView();
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};

		mViewPager.setAdapter(mAdapter);

		goods_list_page_zonghe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mViewPager.setCurrentItem(0);
			}
		});

		goods_list_page_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mViewPager.setCurrentItem(1);
			}
		});
		goods_list_page_price.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mViewPager.setCurrentItem(2);
//				priceList.sortByPrice(goods, lowOrHigh);

				if (lowOrHigh == 0) {
					lowOrHigh = 1;//价格由高到低
				} else {
					lowOrHigh = 0;//价格由低到高
				}

				priceList.setCheck(lowOrHigh);
				priceList.getGoodsByPrice(ShareData.List_init,lowOrHigh);
			}
		});
		goods_list_page_redu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mViewPager.setCurrentItem(3);
			}
		});

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				resetTab();
				switch (position) {
				case 0:
					goods_list_page_zonghe.setBackgroundColor(getResources()
							.getColor(R.color.tabs_e_b_color));
					goods_list_page_zonghe.setTextColor(getResources()
							.getColor(R.color.tabs_e_t_color));
					break;
				case 1:
					goods_list_page_time.setBackgroundColor(getResources()
							.getColor(R.color.tabs_e_b_color));
					goods_list_page_time.setTextColor(getResources().getColor(
							R.color.tabs_e_t_color));
					break;
				case 2:
					goods_list_page_price.setBackgroundColor(getResources()
							.getColor(R.color.tabs_e_b_color));
					goods_list_page_price.setTextColor(getResources().getColor(
							R.color.tabs_e_t_color));
					break;
				case 3:
					goods_list_page_redu.setBackgroundColor(getResources()
							.getColor(R.color.tabs_e_b_color));
					goods_list_page_redu.setTextColor(getResources().getColor(
							R.color.tabs_e_t_color));
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	protected void resetTab() {
		goods_list_page_zonghe.setBackgroundColor(getResources().getColor(
				R.color.tabs_s_b_color));
		goods_list_page_zonghe.setTextColor(getResources().getColor(
				R.color.tabs_s_t_color));

		goods_list_page_time.setBackgroundColor(getResources().getColor(
				R.color.tabs_s_b_color));
		goods_list_page_time.setTextColor(getResources().getColor(
				R.color.tabs_s_t_color));

		goods_list_page_price.setBackgroundColor(getResources().getColor(
				R.color.tabs_s_b_color));
		goods_list_page_price.setTextColor(getResources().getColor(
				R.color.tabs_s_t_color));

		goods_list_page_redu.setBackgroundColor(getResources().getColor(
				R.color.tabs_s_b_color));
		goods_list_page_redu.setTextColor(getResources().getColor(
				R.color.tabs_s_t_color));
	}

	private void initView() {
		zongheList = new ZongheList(getApplicationContext(), pageKind, kind);
		timeList = new TimeList(getApplicationContext(), pageKind, kind);
		priceList = new PriceList(goods, getApplicationContext(), pageKind,
				kind);
		reduList = new ReduList(getApplicationContext(), pageKind, kind);
		mFragments.add(zongheList);
		mFragments.add(timeList);
		mFragments.add(priceList);
		mFragments.add(reduList);
	}

}
