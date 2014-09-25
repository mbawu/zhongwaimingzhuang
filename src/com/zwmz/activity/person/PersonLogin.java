package com.zwmz.activity.person;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.zwmz.activity.MenuBottom;
import com.zwmz.base.MyApplication;
import com.zwmz.base.Url;
import com.zwmz.model.ErrorMsg;
import com.zwmz.model.NetworkAction;
import com.zwmz.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//登录页面
public class PersonLogin extends Activity implements OnClickListener {

	private TextView userNameTxt;// 用户名
	private TextView passwordTxt;// 密码
	private Button loginBtn;// 验证码
	private TextView forgetPwdTxt;// 忘记密码
	private LinearLayout backBtn; // 返回按钮
	private LinearLayout registerBtn;// 注册按钮
	boolean isExit; // 判断是否需要退出程序
	Handler mHandler; // 重置退出程序标识

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_login);
		initView();
		initData();
	}

	// 从注册页面返回来以后判断是否注册成功并读取用户名和密码放入输入框中
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*
		 * 如果注册成功通知需要刷新登录页面，当注册页面关闭以后运行到这里将新注册的用户名和密码显示出来。
		 */
		if (MyApplication.registerSuc) {
			Toast.makeText(this, "注册成功！", 2000).show();
			userNameTxt.setText(MyApplication.sp.getString("username", ""));
			passwordTxt.setText(MyApplication.sp.getString("password", ""));
			MyApplication.registerSuc=false;
			login();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	// 判断用户是否在两秒内连续点击两次返回按钮
	private void exit() {
		// TODO Auto-generated method stub
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			MyApplication.getInstance().exit();
			finish();
		}

	}

	private void initView() {
		userNameTxt = (TextView) findViewById(R.id.person_login_username);
		passwordTxt = (TextView) findViewById(R.id.person_login_password);
		// 后退按钮
		backBtn = (LinearLayout) findViewById(R.id.title_icon_left_layout);
		backBtn.setOnClickListener(this);
		// 忘记密码
		forgetPwdTxt = (TextView) findViewById(R.id.person_login_forgetPwd);
		forgetPwdTxt.setOnClickListener(this);
		// 登录按钮
		loginBtn = (Button) findViewById(R.id.person_login_btn);
		loginBtn.setOnClickListener(this);
		// 注册按钮
		registerBtn = (LinearLayout) findViewById(R.id.title_txt_right_layout);
		registerBtn.setOnClickListener(this);

		// 如果用户在两秒内没有点击返回按钮则重置为不退出标记
		mHandler = new Handler() {

			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				isExit = false;
			}

		};

	}

	private void initData() {
		// TODO Auto-generated method stub

		if (MyApplication.sp.getString("username", "") != null)
		{
			userNameTxt.setText(MyApplication.sp.getString("username", ""));
			passwordTxt.setText(MyApplication.sp.getString("password", ""));
			Log.i(MyApplication.TAG, "pwd-->"+passwordTxt.getText().toString());
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.person_login_btn: // 登录按钮
			login();
			break;
		case R.id.person_login_forgetPwd: // 忘记密码按钮
			intent = new Intent().setClass(this, PersonForgetPwd.class);
			break;
		case R.id.title_icon_left_layout: // 后退按钮
			// case R.id.person_login_backbtn: // 后退按钮
			if (!MyApplication.loginStat) {
				Toast.makeText(this, "请先登录再返回个人中心", 2000).show();
			}
			break;
		case R.id.title_txt_right_layout: // 注册按钮
			intent = new Intent().setClass(this, PersonRegister.class);
			break;
		default:
			break;
		}
		if (intent != null)
			startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(MyApplication.mypDialog!=null)
			MyApplication.mypDialog.dismiss();
	}
	// 登录
	private void login() {
		String username = "";
		String password = "";
		username = userNameTxt.getText().toString();
		password = passwordTxt.getText().toString();
		// 不允许为空
		if (username.equals("") || password.equals("")) {
			Toast.makeText(this, "请输入用户名和密码！", 2000).show();
			return;
		}
		MyApplication.progressShow(PersonLogin.this,  "登录");
		HashMap<String, String> paramter = new HashMap<String, String>();
		paramter.put("act", "login");
		paramter.put("sid", MyApplication.sid);
		paramter.put("username", username);
		paramter.put("password", password);
		// 打印生成的链接地址
		MyApplication.printLog(
				"PersonLogin",
				"Loginurl:"
						+ MyApplication.getUrl(paramter,
								Url.URL_USERS));
		/*
		 * 向服务器发送登录请求
		 */
		MyApplication.client.postWithURL(Url.URL_USERS, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							MyApplication.printLog("PersonLogin", "result:"
									+ response.toString());
							
							int code = response.getInt("code");
							if (response.getInt("code") == 1) {
								//检查是否是更换了用户，如果是的话清空购物车信息
								if (!MyApplication.sp.getString(
										"username", "").equals(
												response.getString("username"))) {
									MyApplication.shopCartList
											.clear();
									try {
										MyApplication.shopCartManager
												.saveProducts(MyApplication.shopCartList);
										MyApplication.shopcart_refresh = true;
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
								
								MyApplication.loginStat = true;
								MyApplication.seskey = response
										.getString("sessionid");
								MyApplication.uid = response
										.getString("uid");
								MyApplication.ed.putString("password",passwordTxt.getText().toString());
								MyApplication.ed.putString("username",
										response.getString("username"));

								MyApplication.ed.putString("name",
										response.getString("nickname"));
								MyApplication.ed.putString("address",
										response.getString("address"));

								MyApplication.ed.putString("photo",
										response.getString("photo"));
								MyApplication.ed.putString("email",
										response.getString("email"));
								MyApplication.ed.putString("createtime",
										response.getString("createtime"));
								MyApplication.ed.putString("birthday",
										response.getString("birthday"));
								MyApplication.ed.putString("sex",
										response.getString("sex"));
								MyApplication.ed.putString("credit",
										response.getString("credit"));
								MyApplication.ed.putString("newfriends",
										response.getString("newfriends"));
								MyApplication.ed.putString("province_name",
										response.getString("province_name"));
								MyApplication.ed.putString("city_name",
										response.getString("city_name"));
								MyApplication.ed.putString("area_name",
										response.getString("area_name"));
								MyApplication.ed.putString("province_id",
										response.getString("province_id"));
								MyApplication.ed.putString("city_id",
										response.getString("city_id"));
								MyApplication.ed.putString("area_id",
										response.getString("area_id"));
								MyApplication.ed.commit();
								// Intent intent = new Intent();
								// intent.setClass(PersonLogin.this,
								// Person.class);
								// intent.putExtra("comment", "suc");
								// setResult(Activity.RESULT_OK, intent);
								MyApplication.loginStat = true;
								PersonLogin.this.finish();
								Toast.makeText(PersonLogin.this, "登录成功", 2000)
										.show();

							} else {
//								Log.i(MyApplication.TAG, "LoginError-->"+ErrorMsg.getErrorMsg(NetworkAction.登录, code));
								 Toast.makeText(PersonLogin.this, ErrorMsg.getErrorMsg(NetworkAction.登录, code), 2000).show();
								MyApplication.loginStat = false;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});

	}

}
