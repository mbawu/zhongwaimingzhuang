package com.zwmz.activity.person;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PersonRegister extends Activity implements OnClickListener {

	private EditText usernameTxt;
	private EditText phoneTxt;
	private EditText validTxt;
	private EditText pwdTxt;
	private EditText pwdAginTxt;
	private EditText emailTxt;
	private LinearLayout backBtn;//后台按钮
	private Button registerbtn;//注册按钮
	private Button getMsgBtn;// 获取验证码按钮
	int seconds = 61;// 多少秒以后可用
	private CountSecond countNum;// 倒计时任务
	private String codeId;// 保存获取到的验证码ID
	private boolean valied=false;//是否已经通过手机验证
	String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_register);
		initView();
	}

	private void initView() {
		MyApplication.registerSuc = false;
		countNum = new CountSecond();
		// 注册的信息
		usernameTxt = (EditText) findViewById(R.id.person_register_username);
		phoneTxt = (EditText) findViewById(R.id.person_register_phone);
		validTxt = (EditText) findViewById(R.id.person_register_validmsg);
		pwdTxt = (EditText) findViewById(R.id.person_register_password);
		pwdAginTxt = (EditText) findViewById(R.id.person_register_pwdagain);
		emailTxt = (EditText) findViewById(R.id.person_register_email);

		// 后退按钮
		backBtn = (LinearLayout) findViewById(R.id.person_register_backbtn);
		backBtn.setOnClickListener(this);
		// 注册按钮
		registerbtn = (Button) findViewById(R.id.person_register_btn);
		registerbtn.setOnClickListener(this);

		getMsgBtn = (Button) findViewById(R.id.person_register_getMsgBtn);
		getMsgBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_register_backbtn:// 后退按钮
			finish();
			break;

		case R.id.person_register_btn:// 注册按钮
			if(codeId==null)
			{
				Toast.makeText(this, "请先获取验证码进行验证", 2000).show();
				return;
			}
			sendData(NetworkAction.验证验证码);//先进行验证验证码，正确以后再去执行注册操作
			break;
		case R.id.person_register_getMsgBtn:// 获取验证码按钮
			sendData(NetworkAction.获取验证码);
			break;
		default:
			break;
		}

	}

	// 向服务器发送数据
	private void sendData(final NetworkAction request) {
		String url = null;
		final String userName = usernameTxt.getText().toString();
		String valid = validTxt.getText().toString();
		final String pwd = pwdTxt.getText().toString();
		String pwdAgain = pwdAginTxt.getText().toString();
		String email = emailTxt.getText().toString();
		phone = phoneTxt.getText().toString();
		if (phone.equals("")) {
			Toast.makeText(this, "请输入手机号！", 2000).show();
			return;
		} 
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.验证验证码))// 执行验证验证码操作
		{
			
			url = Url.URL_MEMBER;
			paramter.put("act", "iscode");
			paramter.put("sid", MyApplication.sid);
			paramter.put("mobile", phone);
			paramter.put("smskey", valid);
			paramter.put("smsid", codeId);
		} else if (request.equals(NetworkAction.获取验证码)) {
			url = Url.URL_VERIFICATION;
			paramter.put("sid", MyApplication.sid);
			paramter.put("mobile", phone);
			paramter.put("SmsContent", "用户注册验证码：");
		}

		// 打印生成的链接地址
		MyApplication.printLog("PersonRegister",
				request+"-->Url:" + MyApplication.getUrl(paramter, url));
		/*
		 * 向服务器发送请求
		 */
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							MyApplication.printLog("PersonRegister", request+"-->result:"
									+ response.toString());
							int code = response.getInt("code");
							MyApplication.printLog("PersonRegister", request+"-->code:"
									+code);
								if (request.equals(NetworkAction.验证验证码)&&code == 1)// 执行验证验证码操作
								{
									register();//成功验证以后再去执行注册操作
								} else if (request.equals(NetworkAction.获取验证码)&&code == 1) {//执行获取验证码操作
										Toast.makeText(PersonRegister.this,
												"验证码已发送", 2000).show();
										codeId = response.getString("smsid");
										int width = getMsgBtn.getWidth();// 获取当前控件宽度
										getMsgBtn.setWidth(width);// 设置当前宽度不变
										getMsgBtn.setOnClickListener(null);// 取消按钮的点击事件
										getMsgBtn
												.setBackgroundColor(PersonRegister.this
														.getResources()
														.getColor(
																R.color.lighter_gray));// 让背景颜色变灰
										handler.post(countNum);// 执行倒计时任务
									}
									 else if (request.equals(NetworkAction.获取验证码)&&code != 1) {//执行获取验证码操作
										 Toast.makeText(PersonRegister.this,
													response.getString("msg"), 2000)
													.show();
									 }
									else {
										
										String result=ErrorMsg.getErrorMsg(request, code);
										Log.i("test", "error-->"+result);
										Toast.makeText(PersonRegister.this,
												ErrorMsg.getErrorMsg(request, code),
												2000).show(); //根据申请的类型判断相应的错误输出
									}
								
						} catch (JSONException e) {
							Log.i("test", "error-->"+e.getMessage());
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("test", "error-->"+error.getMessage());
						error.printStackTrace();
					}
				});

	}

	// 用户注册
	private void register() {
		final String userName = usernameTxt.getText().toString();
		String valid = validTxt.getText().toString();
		final String pwd = pwdTxt.getText().toString();
		String pwdAgain = pwdAginTxt.getText().toString();
		String email = emailTxt.getText().toString();
		phone=phoneTxt.getText().toString();
		if (userName.equals("") || valid.equals("") || pwd.equals("")
				|| pwdAgain.equals("") || email.equals("")) {
			Toast.makeText(this, "请输入完整注册信息！", 2000).show();
			return;
		}

		// 注册
		HashMap<String, String> paramter = new HashMap<String, String>();
		paramter.put("act", "register");
		paramter.put("sid",MyApplication.sid);
		paramter.put("username", userName);
		paramter.put("password", pwd);
		paramter.put("repassword", pwdAgain);
		paramter.put("mobile", phone);
		paramter.put("email", email);
		paramter.put("smsid", codeId);
		paramter.put("smskey", valid);
		// 打印生成的链接地址
		MyApplication.printLog(
				"PersonRegister",
				"Loginurl:"
						+ MyApplication.getUrl(paramter,
								Url.URL_USERS));
		MyApplication.client.postWithURL(Url.URL_USERS, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							MyApplication.printLog("PersonRegister", "result:"
									+ response.toString());
							int code=response.getInt("code");
							if (code== 1) {
								
								try {
									MyApplication.shopCartManager
											.saveProducts(MyApplication.shopCartList);
									MyApplication.shopcart_refresh = true;
								} catch (IOException e) {
									e.printStackTrace();
								}
								/*
								 * 如果返回code1代表注册成功，
								 * 注册成功的时候将用户信息保存到本地SharedPreferences
								 */
								MyApplication.ed
										.putString("username", userName);
								MyApplication.ed.putString("password", pwd);
								MyApplication.seskey = response
										.getString("sessionid");
								MyApplication.ed.putString("uid",
										response.getString("uid"));
								MyApplication.ed.putString("username",
										response.getString("username"));
								MyApplication.ed.putString("nickname",
										response.getString("nickname"));
								MyApplication.ed.putString("photo",
										response.getString("photo"));
								MyApplication.ed.putString("email",
										response.getString("email"));
								MyApplication.ed.putString("createtime",
										response.getString("createtime"));
								MyApplication.ed.putString("birthday",
										response.getString("birthday"));
								MyApplication.ed.putString("address",
										response.getString("address"));
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
								MyApplication.ed.putString("phone",
										phone);
								MyApplication.ed.commit();
								// Toast.makeText(PersonRegister.this,
								// "注册成功", 2000).show();
								MyApplication.registerSuc = true;
								PersonRegister.this.finish();
							} else {
								/*
								 * 如果返回其他代表注册失败， 提示用户注册失败
								 */
								Toast.makeText(PersonRegister.this,
										ErrorMsg.getErrorMsg(NetworkAction.用户注册, code),
										2000).show();
							}
						} catch (JSONException e) {
							Toast.makeText(PersonRegister.this,
									"JSONException错误信息：" + e.getMessage(), 2000)
									.show();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});

	}

	Handler handler = new Handler() {
		int temp = seconds;

		public void handleMessage(Message msg) {
			temp--;
			getMsgBtn.setText(String.valueOf(temp));
			if (seconds == msg.arg1) {
				getMsgBtn.setOnClickListener(PersonRegister.this);
				getMsgBtn.setBackgroundDrawable(PersonRegister.this.getResources()
						.getDrawable(R.drawable.button));
				getMsgBtn.setText("获取验证码");
				temp = seconds;
			} else {
				handler.post(countNum);
			}
		};
	};

	// 倒计时任务，每次执行加1，然后发送消息
	public class CountSecond implements Runnable {
		int count = 0;

		@Override
		public void run() {
//			Log.i(MyApplication.TAG, "run-->" + count);
			count++;
			Message msg = new Message();
			msg.arg1 = count;
			handler.sendMessageDelayed(msg, 1000);
			// handler.postDelayed(new CountSecond(), 1000);
		}

	}
}
