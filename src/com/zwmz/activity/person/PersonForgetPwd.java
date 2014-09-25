package com.zwmz.activity.person;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.zwmz.activity.person.PersonRegister.CountSecond;
import com.zwmz.base.MyApplication;
import com.zwmz.base.Url;
import com.zwmz.model.NetworkAction;
import com.zwmz.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 忘记密码界面
 * 
 * @author Administrator
 * 
 */
public class PersonForgetPwd extends Activity implements OnClickListener {

	private LinearLayout backBtn;// 后退按钮
	private TextView getCode;// 获取验证码
	private Button nextBtn;// 下一步按钮
	private EditText phoneNumTxt;// 手机号输入框
	private EditText codeTxt;// 验证码输入框
	private EditText pwdTxt;// 密码输入框
	private EditText repwdTxt;// 确认密码输入框
	private TextView backToMain; // 返回首页按钮
	private int step = 1; // 判断正在显示的是第几步骤
	private TextView step1Txt; // 步骤1文本框
	private TextView step2Txt;// 步骤2文本框
	private TextView step3Txt;// 步骤3文本框
	private LinearLayout step1Layout;// 步骤1内容布局
	private LinearLayout step2Layout;// 步骤2内容布局
	private LinearLayout step3Layout;// 步骤3内容布局
	private boolean codeSuc = false; // 手机验证码是否正确
	private TextView low; // 密码复杂度低
	private TextView normal;// 密码复杂度中
	private TextView high;// 密码复杂度高
	int seconds = 61;// 多少秒以后可用
	private CountSecond countNum;//倒计时任务
	private String codeId;//保存获取到的验证码ID

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_forgetpwd);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		countNum=new CountSecond();
		backBtn = (LinearLayout) findViewById(R.id.person_forgetpwd_backbtn);
		backBtn.setOnClickListener(this);
		getCode = (TextView) findViewById(R.id.person_forgetpwd_getcode);
		getCode.setOnClickListener(this);

		nextBtn = (Button) findViewById(R.id.person_forgetpwd_next);
		nextBtn.setOnClickListener(this);
		backToMain = (TextView) findViewById(R.id.person_forgetpwd_backtomain);
		backToMain.setOnClickListener(this);
		phoneNumTxt = (EditText) findViewById(R.id.person_forgetpwd_phonenum);
		codeTxt = (EditText) findViewById(R.id.person_forgetpwd_code);
		pwdTxt = (EditText) findViewById(R.id.person_forgetpwd_newpwd);
		pwdTxt.addTextChangedListener(watcher);// 注册监视密码输入以后的安全等级
		repwdTxt = (EditText) findViewById(R.id.person_forgetpwd_renewpwd);
		step1Txt = (TextView) findViewById(R.id.person_forgetpwd_step1);
		step2Txt = (TextView) findViewById(R.id.person_forgetpwd_step2);
		step3Txt = (TextView) findViewById(R.id.person_forgetpwd_step3);
		step1Layout = (LinearLayout) findViewById(R.id.person_forgetpwd_step1_layout);
		step2Layout = (LinearLayout) findViewById(R.id.person_forgetpwd_step2_layout);
		step3Layout = (LinearLayout) findViewById(R.id.person_forgetpwd_step3_layout);
		low = (TextView) findViewById(R.id.person_forgetpwd_sec_low);
		normal = (TextView) findViewById(R.id.person_forgetpwd_sec_normal);
		high = (TextView) findViewById(R.id.person_forgetpwd_sec_high);
	}

	// 文本输入监听器
	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub

			MyApplication.printLog("person_changepwd", "CharSequence-->" + s);
			checkText(s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		/**
		 * 正则表达式判断输入的安全程度
		 * 
		 * @param text
		 */
		private void checkText(String text) {

			if (text.equals("")) {
				low.setBackgroundColor(MyApplication.resources
						.getColor(R.color.orange));
				normal.setBackgroundColor(MyApplication.resources
						.getColor(R.color.lightgray));
				high.setBackgroundColor(MyApplication.resources
						.getColor(R.color.lightgray));
				return;
			}
			int sec = 1;
			if (text.matches("[0-9]+"))
				sec = 1;
			else if (text.matches("[a-z]+"))
				sec = 1;
			else if (text.matches("[A-Z]+"))
				sec = 1;
			else if (text.matches("[a-z]+[0-9]+"))
				sec = 2;
			else if (text.matches("[0-9]+[a-z]+"))
				sec = 2;
			else if (text.matches("[A-Z]+[0-9]+"))
				sec = 2;
			else if (text.matches("[0-9]+[A-Z]+"))
				sec = 2;
			else if(text.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{6,}$"))
				sec = 3;
			else
				sec = 2;
			switch (sec) {
			case 2:
				low.setBackgroundColor(MyApplication.resources
						.getColor(R.color.lightgray));
				normal.setBackgroundColor(MyApplication.resources
						.getColor(R.color.orange));
				high.setBackgroundColor(MyApplication.resources
						.getColor(R.color.lightgray));
				break;
			case 3:
				low.setBackgroundColor(MyApplication.resources
						.getColor(R.color.lightgray));
				normal.setBackgroundColor(MyApplication.resources
						.getColor(R.color.lightgray));
				high.setBackgroundColor(MyApplication.resources
						.getColor(R.color.orange));
				break;
			default:
				break;
			}

		}

		private void checkText() {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.person_forgetpwd_backbtn:// 后退按钮
			finish();
			break;
		case R.id.person_forgetpwd_getcode:// 获取验证码按钮
			sendData(NetworkAction.获取验证码);
			break;
		case R.id.person_forgetpwd_next:// 下一步按钮
			nextStep();
			break;
		case R.id.person_forgetpwd_backtomain:// 返回首页按钮

			break;

		default:
			break;
		}
	}

	/**
	 * //判断是到哪一步，显示相应的背景，改变文本颜色，设置下一步按钮功能
	 */
	private void nextStep() {
		switch (step) {
		case 1://步骤一的点击事件
			String phone = phoneNumTxt.getText().toString();
			String code = codeTxt.getText().toString();
			if (phone.equals("") || code.equals("")) {
				Toast.makeText(this, "请输入完整信息", 2000).show();
				return;
			}
			else if(codeId==null)
			{
				Toast.makeText(this, "请先获取验证码", 2000).show();
				return;
			}
			sendData(NetworkAction.验证验证码);
//			sendData(NetworkAction.获取验证码);
			// codeValid();//验证手机验证码
			
			break;

		case 2:
			String pwd = pwdTxt.getText().toString();
			String repwd = repwdTxt.getText().toString();
			if (pwd.equals("")||repwd.equals("")) {
				Toast.makeText(this, "请输入密码", 2000).show();
				return;
			}
			else if (!pwd.equals(repwd)) {
				Toast.makeText(this, "两次密码不一致", 2000).show();
				return;
			}
			sendData(NetworkAction.找回密码);
			
			break;
		case 3:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 向服务器发送请求
	 */
	private void sendData(final NetworkAction request) {
		String phoneNum = phoneNumTxt.getText().toString();
		String code=codeTxt.getText().toString();
		String pwd=pwdTxt.getText().toString();
		String repwd=repwdTxt.getText().toString();
		String url = null;// 请求地址
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.获取验证码)) {
			url = Url.URL_VERIFICATION;
			paramter.put("sid", MyApplication.sid);
			paramter.put("mobile", phoneNum);
			paramter.put("SmsContent", "找回密码验证码：");
		} else if(request.equals(NetworkAction.找回密码)){
			url = Url.URL_MEMBER;
			paramter.put("sid", MyApplication.sid);
			paramter.put("act", "setpwd");
			paramter.put("smsid", codeId);
			paramter.put("mobile", phoneNum);
			paramter.put("password", pwd);
			paramter.put("smskey", code);
			paramter.put("username", phoneNum);
		}
		else if(request.equals(NetworkAction.验证验证码))
		{
			url = Url.URL_MEMBER;
			paramter.put("act", "iscode");
			paramter.put("sid", MyApplication.sid);
			paramter.put("mobile", phoneNum);
			paramter.put("smskey", code);
			paramter.put("smsid", codeId);
		}
		Log.i(MyApplication.TAG,
				request+"》msgCode-->" + codeId);
		// 打印生成的链接地址
		Log.i(MyApplication.TAG,
				request+"》url-->" + MyApplication.getUrl(paramter, url));
		/*
		 * 向服务器发送请求
		 */
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							MyApplication.printLog(request+"result-->",
									"result:" + response.toString());
							int code = response.getInt("code");
							//获取验证码
							if (request.equals(NetworkAction.获取验证码)) {
								
								if (code == 1) {
									Toast.makeText(PersonForgetPwd.this, "验证码已发送", 2000).show();
									codeId=response.getString("smsid");
									int width=getCode.getWidth();//获取当前控件宽度
									getCode.setWidth(width);//设置当前宽度不变
									getCode.setOnClickListener(null);//取消按钮的点击事件
									getCode
											.setBackgroundColor(PersonForgetPwd.this
													.getResources().getColor(
															R.color.lighter_gray));//让背景颜色变灰
									handler.post(countNum);//执行倒计时任务
								}  
								else {
									Toast.makeText(PersonForgetPwd.this, response.getString("msg"), 2000).show();
									return;
								}
							}
							else if(request.equals(NetworkAction.找回密码))
							{
								//修改密码成功
								if (code == 1) {
									step1Txt.setTextColor(MyApplication.resources
											.getColor(R.color.black));
									step2Txt.setTextColor(MyApplication.resources
											.getColor(R.color.black));
									step3Txt.setTextColor(MyApplication.resources
											.getColor(R.color.white));
									step1Txt.setBackgroundDrawable(MyApplication.resources
											.getDrawable(R.drawable.p1_hide));
									step2Txt.setBackgroundDrawable(MyApplication.resources
											.getDrawable(R.drawable.p2_hide));
									step3Txt.setBackgroundDrawable(MyApplication.resources
											.getDrawable(R.drawable.p3_show));
									step1Layout.setVisibility(View.GONE);
									step2Layout.setVisibility(View.GONE);
									step3Layout.setVisibility(View.VISIBLE);
									step = 3;
									MyApplication.ed.putString("password", pwdTxt.getText().toString());
									MyApplication.ed.commit();
									nextBtn.setText("完成");
								}
								else
									Toast.makeText(PersonForgetPwd.this, "操作失败", 2000).show();
							}
							else if(request.equals(NetworkAction.验证验证码))
							{
								//验证验证码成功
								if (code== 1) {
									step = 2;
									step1Txt.setTextColor(MyApplication.resources
											.getColor(R.color.black));
									step2Txt.setTextColor(MyApplication.resources
											.getColor(R.color.white));
									step3Txt.setTextColor(MyApplication.resources
											.getColor(R.color.black));
									step1Txt.setBackgroundDrawable(MyApplication.resources
											.getDrawable(R.drawable.p1_hide));
									step2Txt.setBackgroundDrawable(MyApplication.resources
											.getDrawable(R.drawable.p2_show));
									step3Txt.setBackgroundDrawable(MyApplication.resources
											.getDrawable(R.drawable.p3_hide));
									step1Layout.setVisibility(View.GONE);
									step2Layout.setVisibility(View.VISIBLE);
									step3Layout.setVisibility(View.GONE);
								}
								else
								{
									Toast.makeText(PersonForgetPwd.this, "请输入正确的验证码", 2000).show();
								}
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
	Handler handler = new Handler() {
		int temp=seconds;
		public void handleMessage(Message msg) {
			temp--;
			getCode.setText(String.valueOf(temp));
			if (seconds == msg.arg1) {
				getCode.setOnClickListener(PersonForgetPwd.this);
				getCode.setBackgroundDrawable(PersonForgetPwd.this.getResources()
						.getDrawable(R.drawable.button));
				getCode.setText("获取验证码");
				temp=seconds;
			}
			else
			{
				handler.post(countNum);
			}
		};
	};

	//倒计时任务，每次执行加1，然后发送消息
	public class CountSecond implements Runnable {
		int count= 0;

		@Override
		public void run() {
//			Log.i(MyApplication.TAG, "run-->"+count);
			count++;
			Message msg = new Message();
			msg.arg1 = count;
			handler.sendMessageDelayed(msg, 1000);

		}

	}
}
