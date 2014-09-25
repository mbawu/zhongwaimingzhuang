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
 * �����������
 * 
 * @author Administrator
 * 
 */
public class PersonForgetPwd extends Activity implements OnClickListener {

	private LinearLayout backBtn;// ���˰�ť
	private TextView getCode;// ��ȡ��֤��
	private Button nextBtn;// ��һ����ť
	private EditText phoneNumTxt;// �ֻ��������
	private EditText codeTxt;// ��֤�������
	private EditText pwdTxt;// ���������
	private EditText repwdTxt;// ȷ�����������
	private TextView backToMain; // ������ҳ��ť
	private int step = 1; // �ж�������ʾ���ǵڼ�����
	private TextView step1Txt; // ����1�ı���
	private TextView step2Txt;// ����2�ı���
	private TextView step3Txt;// ����3�ı���
	private LinearLayout step1Layout;// ����1���ݲ���
	private LinearLayout step2Layout;// ����2���ݲ���
	private LinearLayout step3Layout;// ����3���ݲ���
	private boolean codeSuc = false; // �ֻ���֤���Ƿ���ȷ
	private TextView low; // ���븴�Ӷȵ�
	private TextView normal;// ���븴�Ӷ���
	private TextView high;// ���븴�Ӷȸ�
	int seconds = 61;// �������Ժ����
	private CountSecond countNum;//����ʱ����
	private String codeId;//�����ȡ������֤��ID

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
		pwdTxt.addTextChangedListener(watcher);// ע��������������Ժ�İ�ȫ�ȼ�
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

	// �ı����������
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
		 * ������ʽ�ж�����İ�ȫ�̶�
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
		case R.id.person_forgetpwd_backbtn:// ���˰�ť
			finish();
			break;
		case R.id.person_forgetpwd_getcode:// ��ȡ��֤�밴ť
			sendData(NetworkAction.��ȡ��֤��);
			break;
		case R.id.person_forgetpwd_next:// ��һ����ť
			nextStep();
			break;
		case R.id.person_forgetpwd_backtomain:// ������ҳ��ť

			break;

		default:
			break;
		}
	}

	/**
	 * //�ж��ǵ���һ������ʾ��Ӧ�ı������ı��ı���ɫ��������һ����ť����
	 */
	private void nextStep() {
		switch (step) {
		case 1://����һ�ĵ���¼�
			String phone = phoneNumTxt.getText().toString();
			String code = codeTxt.getText().toString();
			if (phone.equals("") || code.equals("")) {
				Toast.makeText(this, "������������Ϣ", 2000).show();
				return;
			}
			else if(codeId==null)
			{
				Toast.makeText(this, "���Ȼ�ȡ��֤��", 2000).show();
				return;
			}
			sendData(NetworkAction.��֤��֤��);
//			sendData(NetworkAction.��ȡ��֤��);
			// codeValid();//��֤�ֻ���֤��
			
			break;

		case 2:
			String pwd = pwdTxt.getText().toString();
			String repwd = repwdTxt.getText().toString();
			if (pwd.equals("")||repwd.equals("")) {
				Toast.makeText(this, "����������", 2000).show();
				return;
			}
			else if (!pwd.equals(repwd)) {
				Toast.makeText(this, "�������벻һ��", 2000).show();
				return;
			}
			sendData(NetworkAction.�һ�����);
			
			break;
		case 3:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * ���������������
	 */
	private void sendData(final NetworkAction request) {
		String phoneNum = phoneNumTxt.getText().toString();
		String code=codeTxt.getText().toString();
		String pwd=pwdTxt.getText().toString();
		String repwd=repwdTxt.getText().toString();
		String url = null;// �����ַ
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.��ȡ��֤��)) {
			url = Url.URL_VERIFICATION;
			paramter.put("sid", MyApplication.sid);
			paramter.put("mobile", phoneNum);
			paramter.put("SmsContent", "�һ�������֤�룺");
		} else if(request.equals(NetworkAction.�һ�����)){
			url = Url.URL_MEMBER;
			paramter.put("sid", MyApplication.sid);
			paramter.put("act", "setpwd");
			paramter.put("smsid", codeId);
			paramter.put("mobile", phoneNum);
			paramter.put("password", pwd);
			paramter.put("smskey", code);
			paramter.put("username", phoneNum);
		}
		else if(request.equals(NetworkAction.��֤��֤��))
		{
			url = Url.URL_MEMBER;
			paramter.put("act", "iscode");
			paramter.put("sid", MyApplication.sid);
			paramter.put("mobile", phoneNum);
			paramter.put("smskey", code);
			paramter.put("smsid", codeId);
		}
		Log.i(MyApplication.TAG,
				request+"��msgCode-->" + codeId);
		// ��ӡ���ɵ����ӵ�ַ
		Log.i(MyApplication.TAG,
				request+"��url-->" + MyApplication.getUrl(paramter, url));
		/*
		 * ���������������
		 */
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							MyApplication.printLog(request+"result-->",
									"result:" + response.toString());
							int code = response.getInt("code");
							//��ȡ��֤��
							if (request.equals(NetworkAction.��ȡ��֤��)) {
								
								if (code == 1) {
									Toast.makeText(PersonForgetPwd.this, "��֤���ѷ���", 2000).show();
									codeId=response.getString("smsid");
									int width=getCode.getWidth();//��ȡ��ǰ�ؼ����
									getCode.setWidth(width);//���õ�ǰ��Ȳ���
									getCode.setOnClickListener(null);//ȡ����ť�ĵ���¼�
									getCode
											.setBackgroundColor(PersonForgetPwd.this
													.getResources().getColor(
															R.color.lighter_gray));//�ñ�����ɫ���
									handler.post(countNum);//ִ�е���ʱ����
								}  
								else {
									Toast.makeText(PersonForgetPwd.this, response.getString("msg"), 2000).show();
									return;
								}
							}
							else if(request.equals(NetworkAction.�һ�����))
							{
								//�޸�����ɹ�
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
									nextBtn.setText("���");
								}
								else
									Toast.makeText(PersonForgetPwd.this, "����ʧ��", 2000).show();
							}
							else if(request.equals(NetworkAction.��֤��֤��))
							{
								//��֤��֤��ɹ�
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
									Toast.makeText(PersonForgetPwd.this, "��������ȷ����֤��", 2000).show();
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
				getCode.setText("��ȡ��֤��");
				temp=seconds;
			}
			else
			{
				handler.post(countNum);
			}
		};
	};

	//����ʱ����ÿ��ִ�м�1��Ȼ������Ϣ
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
