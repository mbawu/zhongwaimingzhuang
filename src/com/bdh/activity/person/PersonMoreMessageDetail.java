package com.bdh.activity.person;

import com.bdh.model.MyMessage;
import com.bdh.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonMoreMessageDetail extends Activity {
	
	private LinearLayout backBtn;
	private TextView msgSubject;
	private TextView msgContent;
	private TextView msgDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_more_message_detail);
		init();
	}

	private void init() {
		backBtn=(LinearLayout) findViewById(R.id.msg_detail_backbtn);
		backBtn.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		msgSubject=(TextView) findViewById(R.id.msg_detail_subject);
		msgContent=(TextView) findViewById(R.id.msg_detail_content);
		msgDate=(TextView) findViewById(R.id.msg_detail_date);
		
		Intent intent=getIntent();
		MyMessage msg=(MyMessage) intent.getSerializableExtra("msg");
		msgSubject.setText(msg.getSubject());
		msgContent.setText(msg.getContent());
		msgDate.setText(msg.getCreatTime());
	}
}
