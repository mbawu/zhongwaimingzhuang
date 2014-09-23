package com.bdh.activity.person;


import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bdh.base.CustomDialog;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.base.WiperSwitch;
import com.bdh.base.WiperSwitch.OnChangedListener;
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;
import com.bdh.utils.UpdateVersion;
import com.bdh.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class PersonMore extends Activity implements OnClickListener{

	private LinearLayout backBtn;//后退按钮
	private RelativeLayout msgLayout;//我的消息
	private TextView msgNumTxt;//我的消息数量
	private RelativeLayout sugestLayout;//意见反馈
	private RelativeLayout updateLayout;//版本升级
	private RelativeLayout cacheLayout;//清空缓存
	private RelativeLayout aboutLayout;//关于我们
	private RelativeLayout telLayout;//客服电话
	private TextView phoneNum;
	private Handler handler;
	private String msgNum;//保存我的消息的数量
	private Switch pushBtn;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_more);
		initView();
		getMsgNum(NetworkAction.我的消息);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getMsgNum(NetworkAction.我的消息);
	}
	private void initView() {
		handler = new Handler() {
			public void handleMessage(Message arg0) {
				super.handleMessage(arg0);
				Toast.makeText(PersonMore.this, "清除完毕", 2000).show();
			}
		};
		phoneNum=(TextView) findViewById(R.id.person_more_phonetxt);
		msgNumTxt=(TextView) findViewById(R.id.person_more_msg_num);
		backBtn=(LinearLayout) findViewById(R.id.person_more_backbtn);
		backBtn.setOnClickListener(this);
		msgLayout=(RelativeLayout) findViewById(R.id.person_more_mymessage);
		msgLayout.setOnClickListener(this);
		sugestLayout=(RelativeLayout) findViewById(R.id.person_more_sugest);
		sugestLayout.setOnClickListener(this);
		updateLayout=(RelativeLayout) findViewById(R.id.person_more_update);
		updateLayout.setOnClickListener(this);
		cacheLayout=(RelativeLayout) findViewById(R.id.person_more_cache);
		cacheLayout.setOnClickListener(this);
		aboutLayout=(RelativeLayout) findViewById(R.id.person_more_about);
		aboutLayout.setOnClickListener(this);
		telLayout=(RelativeLayout) findViewById(R.id.person_more_tel);
		telLayout.setOnClickListener(this);
		
		pushBtn=(Switch) findViewById(R.id.person_more_switchbtn);
		pushBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Log.i(MyApplication.TAG, "isChecked-->"+isChecked);
				//打开消息推送服务
				if (isChecked) {
					JPushInterface.resumePush(MyApplication.context);
				} 
				//关闭消息推送
				else {
					JPushInterface.stopPush(MyApplication.context);
				}
				MyApplication.jPush=isChecked;
				MyApplication.ed.putBoolean("push", isChecked);
				MyApplication.ed.commit();
			}
		});
//		pushBtn.setOnChangedListener(new OnChangedListener() {
//			
//			@Override
//			public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
//				// TODO Auto-generated method stub
//				Log.i(MyApplication.TAG, "isChecked-->"+checkState);
//				//打开消息推送服务
//				if (checkState) {
//					JPushInterface.resumePush(MyApplication.context);
//				} 
//				//关闭消息推送
//				else {
//					JPushInterface.stopPush(MyApplication.context);
//				}
//				MyApplication.jPush=checkState;
//				MyApplication.ed.putBoolean("push", checkState);
//				MyApplication.ed.commit();
//			}
//		});
		Log.i(MyApplication.TAG, "push->"+MyApplication.sp.getBoolean("push", true));
		MyApplication.jPush=MyApplication.sp.getBoolean("push", true);
		pushBtn.setChecked(MyApplication.jPush);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.person_more_backbtn://后退按钮
			finish();
			break;
		case R.id.person_more_mymessage://我的消息
			 intent= new Intent().setClass(this, personMoreMessage.class);
			break;
		case R.id.person_more_sugest://意见反馈
			intent=new Intent().setClass(this, PersonMoreSugest.class);
			break;
		case R.id.person_more_update://更新版本
			Toast.makeText(this, "正在检查，请稍后", 2000).show();
			new UpdateVersion("",this).startThread();
			break;
		case R.id.person_more_cache://清空缓存
			handler.sendEmptyMessageAtTime(1, 2000);
			break;
		case R.id.person_more_about://关于我们
			intent=new Intent().setClass(this, PersonMoreAbout.class);
			break;
		case R.id.person_more_tel://客服电话
			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			builder.setMessage("你确定拨打"+phoneNum.getText().toString()+"吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent phoneIntent = new Intent("android.intent.action.CALL",
											Uri.parse("tel:" + MyApplication.resources.getString(R.string.person_more_tel_num)));
									startActivity(phoneIntent);
									dialog.cancel();
								}
							})
					.setNegativeButton("返回",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			CustomDialog alert = builder.create();
			alert.show();
			
			break;
		default:
			break;
		}
		if(intent!=null)
			startActivity(intent);
		
	}
	
	public void getMsgNum(final NetworkAction request)
	{
		
	
	HashMap<String, String> paramter = new HashMap<String, String>();
	  paramter.put("act", "Message");
	  paramter.put("sid", MyApplication.sid);
	  paramter.put("uid", MyApplication.uid);
	  paramter.put("sessionid", MyApplication.seskey);
	  paramter.put("nowpage", "1");
	  paramter.put("pagesize", "1000");
	  Log.i(MyApplication.TAG, request+MyApplication.getUrl(paramter, Url.URL_MEMBER));
	  MyApplication.client.postWithURL(Url.URL_MEMBER, paramter,
	    new Listener<JSONObject>() {
	     public void onResponse(JSONObject response) {
	      try {
	    	  Log.i(MyApplication.TAG, request+"response-->"+response.toString());
	       int code = response.getInt("code");
	       if (response.getInt("code") == 1) {
	       msgNum=response.getString("totalnum");
	       msgNumTxt.setText(msgNum);
	       } else {
	    	   Toast.makeText(PersonMore.this, request+ErrorMsg.getErrorMsg(request, code), 2000).show();
	       }
	      } catch (JSONException e) {
	       Log.i(MyApplication.TAG, request+"JSONException-->"+e.getMessage());
	      }
	     }
	    }, new ErrorListener() {
	     @Override
	     public void onErrorResponse(VolleyError error) {
	    	 Log.i(MyApplication.TAG, request+"onErrorResponse-->"+error.getMessage());
	     }
	    });
	}
}
