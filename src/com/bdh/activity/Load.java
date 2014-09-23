package com.bdh.activity;

import cn.jpush.android.api.JPushInterface;

import com.bdh.base.BaseActivity;
import com.bdh.base.MyApplication;
import com.bdh.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;

/*���Activity 
 ��������������*/
public class Load extends BaseActivity {
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load);
		/* �����ʼ����ʱ������Ļ��� */
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		MyApplication.width = metric.widthPixels;
		MyApplication.height = metric.heightPixels;
		
		handler = new Handler();
		handler.postDelayed(new startMainActivity(), 2000);
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}
	class startMainActivity implements Runnable{

		@Override
		public void run() {
			//��ʼ��JPUSH
			Intent intent = new Intent().setClass(Load.this, MenuBottom.class);
			startActivity(intent);
			//��������ļ��Ƿ����Ϣ���͹���
			MyApplication.jPush=MyApplication.sp.getBoolean("push", true);
			//�������Ϣ���͹��ܲų�ʼ���ù���
			if(MyApplication.jPush){
				JPushInterface.setDebugMode(true);
		        JPushInterface.init(MyApplication.context);
			}
			Load.this.finish();
		}
		
	}
}
