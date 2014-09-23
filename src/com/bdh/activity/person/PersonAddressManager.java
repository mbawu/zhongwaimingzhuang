package com.bdh.activity.person;

import com.bdh.base.BaseFragmentActivity;
import com.bdh.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

//��ʾ��ַ����Ĵ��з��ذ�ť��ҳ��
public class PersonAddressManager extends FragmentActivity implements OnClickListener{

	private LinearLayout backBtn;//���˰�ť
	
	private LinearLayout addressLayout;
	private android.support.v4.app.FragmentTransaction fragmentTransaction;
	private android.support.v4.app.FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_address_manager);
		initView();
	}

	private void initView() {
		
		backBtn=(LinearLayout) findViewById(R.id.address_manager_backbtn)	;
		backBtn.setOnClickListener(this);
		fragmentManager = getSupportFragmentManager(); // ��ȡ������
		fragmentTransaction = fragmentManager.beginTransaction();
		//ֱ�ӵ���address�Ĺ���ҳ��
		android.support.v4.app.Fragment newfragment = new PersonAddress();
		fragmentTransaction.replace(R.id.address_layout, newfragment).commit();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.address_manager_backbtn:
			finish();
			break;

		default:
			break;
		}
		
	}
}
