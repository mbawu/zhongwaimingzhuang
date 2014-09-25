package com.zwmz.activity.product;


import com.zwmz.R;
import com.zwmz.activity.MenuBottom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class SubmitSuccess extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.submit_success);
	LinearLayout backBtn=(LinearLayout) findViewById(R.id.submit_backbtn);
	backBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
			
		}
	});
	Button viewBtn=(Button) findViewById(R.id.submit_view_btn);
	viewBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			finish();
		}
	});
}
}
