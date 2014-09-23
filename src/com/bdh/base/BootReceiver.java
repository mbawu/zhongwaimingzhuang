package com.bdh.base;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//���������������ͷ���
public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.i(MyApplication.TAG, "BroadcastReceiver");
        // �����������ɵ���
    	MyApplication.jPush=MyApplication.sp.getBoolean("push", true);
		//�������Ϣ���͹��ܲų�ʼ���ù���
		if(MyApplication.jPush){
			JPushInterface.setDebugMode(true);
	        JPushInterface.init(MyApplication.context);
		}
    }
}
