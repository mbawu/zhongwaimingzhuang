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

	private LinearLayout backBtn;//���˰�ť
	private RelativeLayout msgLayout;//�ҵ���Ϣ
	private TextView msgNumTxt;//�ҵ���Ϣ����
	private RelativeLayout sugestLayout;//�������
	private RelativeLayout updateLayout;//�汾����
	private RelativeLayout cacheLayout;//��ջ���
	private RelativeLayout aboutLayout;//��������
	private RelativeLayout telLayout;//�ͷ��绰
	private TextView phoneNum;
	private Handler handler;
	private String msgNum;//�����ҵ���Ϣ������
	private Switch pushBtn;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_more);
		initView();
		getMsgNum(NetworkAction.�ҵ���Ϣ);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getMsgNum(NetworkAction.�ҵ���Ϣ);
	}
	private void initView() {
		handler = new Handler() {
			public void handleMessage(Message arg0) {
				super.handleMessage(arg0);
				Toast.makeText(PersonMore.this, "������", 2000).show();
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
				//����Ϣ���ͷ���
				if (isChecked) {
					JPushInterface.resumePush(MyApplication.context);
				} 
				//�ر���Ϣ����
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
//				//����Ϣ���ͷ���
//				if (checkState) {
//					JPushInterface.resumePush(MyApplication.context);
//				} 
//				//�ر���Ϣ����
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
		case R.id.person_more_backbtn://���˰�ť
			finish();
			break;
		case R.id.person_more_mymessage://�ҵ���Ϣ
			 intent= new Intent().setClass(this, personMoreMessage.class);
			break;
		case R.id.person_more_sugest://�������
			intent=new Intent().setClass(this, PersonMoreSugest.class);
			break;
		case R.id.person_more_update://���°汾
			Toast.makeText(this, "���ڼ�飬���Ժ�", 2000).show();
			new UpdateVersion("",this).startThread();
			break;
		case R.id.person_more_cache://��ջ���
			handler.sendEmptyMessageAtTime(1, 2000);
			break;
		case R.id.person_more_about://��������
			intent=new Intent().setClass(this, PersonMoreAbout.class);
			break;
		case R.id.person_more_tel://�ͷ��绰
			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			builder.setMessage("��ȷ������"+phoneNum.getText().toString()+"��")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent phoneIntent = new Intent("android.intent.action.CALL",
											Uri.parse("tel:" + MyApplication.resources.getString(R.string.person_more_tel_num)));
									startActivity(phoneIntent);
									dialog.cancel();
								}
							})
					.setNegativeButton("����",
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
