package com.bdh.activity.person;

import com.bdh.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class PersonMoreAbout extends Activity implements OnClickListener{

	private LinearLayout backBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_more_about);
		backBtn=(LinearLayout) findViewById(R.id.person_more_about_backbtn);
		backBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.person_more_about_backbtn:
			finish();
			break;

		default:
			break;
		}
	}
}
