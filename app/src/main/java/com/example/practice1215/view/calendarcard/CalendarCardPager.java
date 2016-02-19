package com.example.practice1215.view.calendarcard;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CalendarCardPager extends ViewPager {
	
	private CardPagerAdapter mCardPagerAdapter;
	private OnCellItemClick mOnCellItemClick;
	
	public CalendarCardPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init(context);
	}

	public CalendarCardPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public CalendarCardPager(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		 mCardPagerAdapter = new CardPagerAdapter(context);
		 setAdapter(mCardPagerAdapter);
	}
	
	public CardPagerAdapter getCardPagerAdapter() {
		return mCardPagerAdapter;
	}

	public OnCellItemClick getOnCellItemClick() {
		return mOnCellItemClick;
	}

	public void setOnCellItemClick(OnCellItemClick mOnCellItemClick) {
		this.mOnCellItemClick = mOnCellItemClick;
		mCardPagerAdapter.setDefaultOnCellItemClick(this.mOnCellItemClick);
		if (getChildCount() > 0) {
			for(int i=0; i<getChildCount(); i++) {
				View v = getChildAt(i);
				if (v instanceof CalendarCard) {
					((CalendarCard) v).setOnCellItemClick(this.mOnCellItemClick);
				}
			}
		}
	}

}
