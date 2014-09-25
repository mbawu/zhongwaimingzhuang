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

//��¼ҳ��
public class PersonLogin extends Activity implements OnClickListener {

	private TextView userNameTxt;// �û���
	private TextView passwordTxt;// ����
	private Button loginBtn;// ��֤��
	private TextView forgetPwdTxt;// ��������
	private LinearLayout backBtn; // ���ذ�ť
	private LinearLayout registerBtn;// ע�ᰴť
	boolean isExit; // �ж��Ƿ���Ҫ�˳�����
	Handler mHandler; // �����˳������ʶ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_login);
		initView();
		initData();
	}

	// ��ע��ҳ�淵�����Ժ��ж��Ƿ�ע��ɹ�����ȡ�û�������������������
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*
		 * ���ע��ɹ�֪ͨ��Ҫˢ�µ�¼ҳ�棬��ע��ҳ��ر��Ժ����е����ｫ��ע����û�����������ʾ������
		 */
		if (MyApplication.registerSuc) {
			Toast.makeText(this, "ע��ɹ���", 2000).show();
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

	// �ж��û��Ƿ�������������������η��ذ�ť
	private void exit() {
		// TODO Auto-generated method stub
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
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
		// ���˰�ť
		backBtn = (LinearLayout) findViewById(R.id.title_icon_left_layout);
		backBtn.setOnClickListener(this);
		// ��������
		forgetPwdTxt = (TextView) findViewById(R.id.person_login_forgetPwd);
		forgetPwdTxt.setOnClickListener(this);
		// ��¼��ť
		loginBtn = (Button) findViewById(R.id.person_login_btn);
		loginBtn.setOnClickListener(this);
		// ע�ᰴť
		registerBtn = (LinearLayout) findViewById(R.id.title_txt_right_layout);
		registerBtn.setOnClickListener(this);

		// ����û���������û�е�����ذ�ť������Ϊ���˳����
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
		case R.id.person_login_btn: // ��¼��ť
			login();
			break;
		case R.id.person_login_forgetPwd: // �������밴ť
			intent = new Intent().setClass(this, PersonForgetPwd.class);
			break;
		case R.id.title_icon_left_layout: // ���˰�ť
			// case R.id.person_login_backbtn: // ���˰�ť
			if (!MyApplication.loginStat) {
				Toast.makeText(this, "���ȵ�¼�ٷ��ظ�������", 2000).show();
			}
			break;
		case R.id.title_txt_right_layout: // ע�ᰴť
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
	// ��¼
	private void login() {
		String username = "";
		String password = "";
		username = userNameTxt.getText().toString();
		password = passwordTxt.getText().toString();
		// ������Ϊ��
		if (username.equals("") || password.equals("")) {
			Toast.makeText(this, "�������û��������룡", 2000).show();
			return;
		}
		MyApplication.progressShow(PersonLogin.this,  "��¼");
		HashMap<String, String> paramter = new HashMap<String, String>();
		paramter.put("act", "login");
		paramter.put("sid", MyApplication.sid);
		paramter.put("username", username);
		paramter.put("password", password);
		// ��ӡ���ɵ����ӵ�ַ
		MyApplication.printLog(
				"PersonLogin",
				"Loginurl:"
						+ MyApplication.getUrl(paramter,
								Url.URL_USERS));
		/*
		 * ����������͵�¼����
		 */
		MyApplication.client.postWithURL(Url.URL_USERS, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							MyApplication.printLog("PersonLogin", "result:"
									+ response.toString());
							
							int code = response.getInt("code");
							if (response.getInt("code") == 1) {
								//����Ƿ��Ǹ������û�������ǵĻ���չ��ﳵ��Ϣ
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
								Toast.makeText(PersonLogin.this, "��¼�ɹ�", 2000)
										.show();

							} else {
//								Log.i(MyApplication.TAG, "LoginError-->"+ErrorMsg.getErrorMsg(NetworkAction.��¼, code));
								 Toast.makeText(PersonLogin.this, ErrorMsg.getErrorMsg(NetworkAction.��¼, code), 2000).show();
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
