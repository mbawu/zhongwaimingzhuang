package com.bdh.base;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

//开机自启极光推送服务
public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.i(MyApplication.TAG, "BroadcastReceiver");
        // 在这里干你想干的事
    	MyApplication.jPush=MyApplication.sp.getBoolean("push", true);
		//如果打开消息推送功能才初始化该功能
		if(MyApplication.jPush){
			JPushInterface.setDebugMode(true);
	        JPushInterface.init(MyApplication.context);
		}
    }
}
