package com.bdh.activity.person;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bdh.base.BaseFragmentActivity;
import com.bdh.base.CustomDialog;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.R;

//会员中心页面
public class Person extends BaseFragmentActivity {

	private LinearLayout personMore; // 更多按钮
	private int tabContent; // 选项卡内容容器ID
	private Fragment tabFragment; // 需要对应到的选项卡内容
	private TextView loginTxt; // 显示登录或注销文本框
	private ImageView loginImg; //登录或注销上方的图片
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person);
		MyApplication.printLog("person", "onCreate"+MyApplication.loginStat);
		initView();
		initData();
		
	}

//	@Override
//	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(arg0, arg1, arg2);
//		MyApplication.printLog("person", "startActivityForResult");
//	}

	
	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
		MyApplication.printLog("person", "onResumeFragments"+MyApplication.loginStat);
		// 登录成功以后返回到该页面判断登录状态并改变文本内容
		if (!MyApplication.loginStat) {
			MyApplication.printLog("person", "onResumeFragments-->if");
			Intent intent=new Intent().setClass(this,PersonLogin.class);
			startActivity(intent);
		}
		else
			MyApplication.printLog("person", "onResumeFragments-->else");


	}

	private void initView() {
		// TODO Auto-generated method stub
		// 注销，登录按钮
		loginTxt = (TextView) findViewById(R.id.person_loginout_txt);
		loginTxt.setOnClickListener(this);
		// 注销，登录图片
		loginImg = (ImageView) findViewById(R.id.person_loginout_img);
		loginImg.setOnClickListener(this);

	}


	private void initData() {
		
		// 进入会员中心前先检查是否已经登录如果没有登录先跳转到登录页面
//		if(!MyApplication.loginStat)
//		{
//			Toast.makeText(this, "请先登录", 2000).show();
//			Intent intent=new Intent().setClass(this,PersonLogin.class);
//			startActivityForResult(intent, 1);
//		}
		personMore = (LinearLayout) findViewById(R.id.person_more_btn);
		personMore.setOnClickListener(this);

		fragmentManager = getSupportFragmentManager(); // 获取管理器
		tabContent = R.id.person_subtab;// 需要替换选项卡的内容容器
		// 初始化为订单详情内容
		fragmentTransaction = fragmentManager.beginTransaction();
		tabFragment = new PersonOrder();
		fragmentTransaction.add(tabContent, tabFragment, "order");
		fragmentTransaction.commit();
		// 初始化选项卡标签
		RadioGroup radioGroup = (RadioGroup) this
				.findViewById(R.id.person_tab_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroupCheckedChange());
	}

	// 选项卡标签点击事件
	public class RadioGroupCheckedChange implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			fragmentTransaction = fragmentManager.beginTransaction();
			switch (checkedId) {
			// 订单详情的选中事件
			case R.id.person_tab_order:
				tabFragment = new PersonOrder();
				break;
			// 优惠券
			case R.id.person_tab_coupon:
				tabFragment = new PersonCoupon();

				break;
			// 个人信息
			case R.id.person_tab_info:
				tabFragment = new PersonInfo();
				break;
			// 收货地址
			case R.id.person_tab_address:
				tabFragment = new PersonAddress();
				break;
			default:
				break;
			}
			fragmentTransaction.replace(tabContent, tabFragment, "content");
			fragmentTransaction.commit();
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.person_more_btn:// 进入更多的页面
			 if(MyApplication.loginStat)
			 {
			intent = new Intent().setClass(this, PersonMore.class);
			 }
			 else
			Toast.makeText(this, "请先登录", 2000).show();
			break;

		default:
			// 点击登录与注销按钮和图片，如果未登录跳转到登录页面
			if (!MyApplication.loginStat) {
				intent = new Intent().setClass(this, PersonLogin.class);
			} else // 如果已登录注销登录状态
			{
				CustomDialog.Builder builder = new CustomDialog.Builder(this);
			    builder.setMessage("确定注销吗？")
			      .setPositiveButton("确定",
			        new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog,
			           int id) {
							loginOut(); 
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
			}

			break;

		}
		if (intent != null)
			startActivity(intent);

	}

	/**
	 * 注销登录
	 */
	private void loginOut() {
		// TODO Auto-generated method stub
		HashMap<String, String> paramter = new HashMap<String, String>();
		paramter.put("act", "logout");
		paramter.put("sessionid", MyApplication.seskey);
		// 打印生成的链接地址
		MyApplication.printLog(
				"Person--loginout",
				"Loginurl:"
						+ MyApplication.getUrl(paramter,
								Url.URL_USERS));
		/*
		 * 向服务器发送请求
		 */
		MyApplication.client.postWithURL(Url.URL_USERS, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							MyApplication.printLog("Person--loginout", "result:"
									+ response.toString());
							int code = response.getInt("code");
							if (response.getInt("code") == 1) {
								Toast.makeText(Person.this, "注销成功", 2000).show();
								MyApplication.loginStat=false;
								Intent intent=new Intent().setClass(Person.this, PersonLogin.class);
								startActivity(intent);
							} else {
								Toast.makeText(Person.this, "注销失败", 2000).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Log.i(MyApplication.TAG, "Person statu-->onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Log.i(MyApplication.TAG, "Person statu-->onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// Log.i(MyApplication.TAG, "Person statu-->onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// Log.i(MyApplication.TAG, "Person statu-->onDestroy");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// Log.i(MyApplication.TAG, "Person statu-->onRestart");
		// if(MyApplication.loginStat)
		// Toast.makeText(this, "登录成功", 2000).show();

	}


}
