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
	private LinearLayout backBtn;//��̨��ť
	private Button registerbtn;//ע�ᰴť
	private Button getMsgBtn;// ��ȡ��֤�밴ť
	int seconds = 61;// �������Ժ����
	private CountSecond countNum;// ����ʱ����
	private String codeId;// �����ȡ������֤��ID
	private boolean valied=false;//�Ƿ��Ѿ�ͨ���ֻ���֤
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
		// ע�����Ϣ
		usernameTxt = (EditText) findViewById(R.id.person_register_username);
		phoneTxt = (EditText) findViewById(R.id.person_register_phone);
		validTxt = (EditText) findViewById(R.id.person_register_validmsg);
		pwdTxt = (EditText) findViewById(R.id.person_register_password);
		pwdAginTxt = (EditText) findViewById(R.id.person_register_pwdagain);
		emailTxt = (EditText) findViewById(R.id.person_register_email);

		// ���˰�ť
		backBtn = (LinearLayout) findViewById(R.id.person_register_backbtn);
		backBtn.setOnClickListener(this);
		// ע�ᰴť
		registerbtn = (Button) findViewById(R.id.person_register_btn);
		registerbtn.setOnClickListener(this);

		getMsgBtn = (Button) findViewById(R.id.person_register_getMsgBtn);
		getMsgBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_register_backbtn:// ���˰�ť
			finish();
			break;

		case R.id.person_register_btn:// ע�ᰴť
			if(codeId==null)
			{
				Toast.makeText(this, "���Ȼ�ȡ��֤�������֤", 2000).show();
				return;
			}
			sendData(NetworkAction.��֤��֤��);//�Ƚ�����֤��֤�룬��ȷ�Ժ���ȥִ��ע�����
			break;
		case R.id.person_register_getMsgBtn:// ��ȡ��֤�밴ť
			sendData(NetworkAction.��ȡ��֤��);
			break;
		default:
			break;
		}

	}

	// ���������������
	private void sendData(final NetworkAction request) {
		String url = null;
		final String userName = usernameTxt.getText().toString();
		String valid = validTxt.getText().toString();
		final String pwd = pwdTxt.getText().toString();
		String pwdAgain = pwdAginTxt.getText().toString();
		String email = emailTxt.getText().toString();
		phone = phoneTxt.getText().toString();
		if (phone.equals("")) {
			Toast.makeText(this, "�������ֻ��ţ�", 2000).show();
			return;
		} 
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.��֤��֤��))// ִ����֤��֤�����
		{
			
			url = Url.URL_MEMBER;
			paramter.put("act", "iscode");
			paramter.put("sid", MyApplication.sid);
			paramter.put("mobile", phone);
			paramter.put("smskey", valid);
			paramter.put("smsid", codeId);
		} else if (request.equals(NetworkAction.��ȡ��֤��)) {
			url = Url.URL_VERIFICATION;
			paramter.put("sid", MyApplication.sid);
			paramter.put("mobile", phone);
			paramter.put("SmsContent", "�û�ע����֤�룺");
		}

		// ��ӡ���ɵ����ӵ�ַ
		MyApplication.printLog("PersonRegister",
				request+"-->Url:" + MyApplication.getUrl(paramter, url));
		/*
		 * ���������������
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
								if (request.equals(NetworkAction.��֤��֤��)&&code == 1)// ִ����֤��֤�����
								{
									register();//�ɹ���֤�Ժ���ȥִ��ע�����
								} else if (request.equals(NetworkAction.��ȡ��֤��)&&code == 1) {//ִ�л�ȡ��֤�����
										Toast.makeText(PersonRegister.this,
												"��֤���ѷ���", 2000).show();
										codeId = response.getString("smsid");
										int width = getMsgBtn.getWidth();// ��ȡ��ǰ�ؼ����
										getMsgBtn.setWidth(width);// ���õ�ǰ��Ȳ���
										getMsgBtn.setOnClickListener(null);// ȡ����ť�ĵ���¼�
										getMsgBtn
												.setBackgroundColor(PersonRegister.this
														.getResources()
														.getColor(
																R.color.lighter_gray));// �ñ�����ɫ���
										handler.post(countNum);// ִ�е���ʱ����
									}
									 else if (request.equals(NetworkAction.��ȡ��֤��)&&code != 1) {//ִ�л�ȡ��֤�����
										 Toast.makeText(PersonRegister.this,
													response.getString("msg"), 2000)
													.show();
									 }
									else {
										
										String result=ErrorMsg.getErrorMsg(request, code);
										Log.i("test", "error-->"+result);
										Toast.makeText(PersonRegister.this,
												ErrorMsg.getErrorMsg(request, code),
												2000).show(); //��������������ж���Ӧ�Ĵ������
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

	// �û�ע��
	private void register() {
		final String userName = usernameTxt.getText().toString();
		String valid = validTxt.getText().toString();
		final String pwd = pwdTxt.getText().toString();
		String pwdAgain = pwdAginTxt.getText().toString();
		String email = emailTxt.getText().toString();
		phone=phoneTxt.getText().toString();
		if (userName.equals("") || valid.equals("") || pwd.equals("")
				|| pwdAgain.equals("") || email.equals("")) {
			Toast.makeText(this, "����������ע����Ϣ��", 2000).show();
			return;
		}

		// ע��
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
		// ��ӡ���ɵ����ӵ�ַ
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
								 * �������code1����ע��ɹ���
								 * ע��ɹ���ʱ���û���Ϣ���浽����SharedPreferences
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
								// "ע��ɹ�", 2000).show();
								MyApplication.registerSuc = true;
								PersonRegister.this.finish();
							} else {
								/*
								 * ���������������ע��ʧ�ܣ� ��ʾ�û�ע��ʧ��
								 */
								Toast.makeText(PersonRegister.this,
										ErrorMsg.getErrorMsg(NetworkAction.�û�ע��, code),
										2000).show();
							}
						} catch (JSONException e) {
							Toast.makeText(PersonRegister.this,
									"JSONException������Ϣ��" + e.getMessage(), 2000)
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
				getMsgBtn.setText("��ȡ��֤��");
				temp = seconds;
			} else {
				handler.post(countNum);
			}
		};
	};

	// ����ʱ����ÿ��ִ�м�1��Ȼ������Ϣ
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
