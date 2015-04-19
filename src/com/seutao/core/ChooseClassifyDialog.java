package com.seutao.core;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seutao.adapter.ClassifyAdapter;
import com.seutao.sharedata.ShareData;

public class ChooseClassifyDialog extends Dialog {
	
   private Button commitButton;
   private Button cancelButton;
   public  String classifyName="选择分类";
   private TextView b;

	public ChooseClassifyDialog(Context context,TextView b) {
		super(context);
		this.b=b;
		// TODO Auto-generated constructor stub
	}

	// Scrolling flag
	private boolean scrolling = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.up_goods_page_two_selectclassify_layout);
		final WheelView FirstClassify = (WheelView) findViewById(R.id.up_goods_FirstClassify);
		FirstClassify.setVisibleItems(6);
		FirstClassify.setViewAdapter(new ClassifyAdapter(getContext()));
		final WheelView SecondClassify = (WheelView) findViewById(R.id.up_goods_SecondClassify);
		SecondClassify.setVisibleItems(6);
		FirstClassify.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!scrolling) {
					updateCities(SecondClassify, ShareData.SecondClassify, newValue);
				}
			}
		});

		FirstClassify.addScrollingListener( new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}
			@Override
			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				updateCities(SecondClassify, ShareData.SecondClassify, FirstClassify.getCurrentItem());
			}
		});

		FirstClassify.setCurrentItem(1);
		commitButton=(Button)findViewById(R.id.up_goods_chooseClassify_commit);
		cancelButton=(Button)findViewById(R.id.up_goods_chooseClassify_quit);
		commitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				classifyName=ShareData.FirstClassify[FirstClassify.getCurrentItem()]+"-"+ShareData.SecondClassify[FirstClassify.getCurrentItem()][SecondClassify.getCurrentItem()];
				System.out.println(classifyName);
				b.setText(classifyName);
				dismiss();
			}
		});
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	/**
	 * Updates the city wheel
	 */
	private void updateCities(WheelView city, String cities[][], int index) {
		ArrayWheelAdapter<String> adapter =
				new ArrayWheelAdapter<String>(getContext(), cities[index]);
		adapter.setTextSize(18);
		city.setViewAdapter(adapter);
		city.setCurrentItem(cities[index].length / 2);
	}


}
