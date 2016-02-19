package com.example.practice1215.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.practice1215.R;
import com.example.practice1215.view.calendarcard.CalendarCard;
import com.example.practice1215.view.calendarcard.CardGridItem;
import com.example.practice1215.view.calendarcard.OnCellItemClick;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Sample1 extends Activity {
	
	private CalendarCard mCalendarCard;
	private TextView mTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample1);
		mCalendarCard = (CalendarCard)findViewById(R.id.calendarCard1);
		mCalendarCard.setOnCellItemClick(new OnCellItemClick() {
			@Override
			public void onCellClick(View v, CardGridItem item) {
				mTextView.setText(getResources().getString(R.string.sel_date, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(item.getDate().getTime())));
			}
		});
		
		
		mTextView = (TextView)findViewById(R.id.textView1);
	}

}
