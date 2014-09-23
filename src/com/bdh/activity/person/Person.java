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

//��Ա����ҳ��
public class Person extends BaseFragmentActivity {

	private LinearLayout personMore; // ���ఴť
	private int tabContent; // ѡ���������ID
	private Fragment tabFragment; // ��Ҫ��Ӧ����ѡ�����
	private TextView loginTxt; // ��ʾ��¼��ע���ı���
	private ImageView loginImg; //��¼��ע���Ϸ���ͼƬ
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
		// ��¼�ɹ��Ժ󷵻ص���ҳ���жϵ�¼״̬���ı��ı�����
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
		// ע������¼��ť
		loginTxt = (TextView) findViewById(R.id.person_loginout_txt);
		loginTxt.setOnClickListener(this);
		// ע������¼ͼƬ
		loginImg = (ImageView) findViewById(R.id.person_loginout_img);
		loginImg.setOnClickListener(this);

	}


	private void initData() {
		
		// �����Ա����ǰ�ȼ���Ƿ��Ѿ���¼���û�е�¼����ת����¼ҳ��
//		if(!MyApplication.loginStat)
//		{
//			Toast.makeText(this, "���ȵ�¼", 2000).show();
//			Intent intent=new Intent().setClass(this,PersonLogin.class);
//			startActivityForResult(intent, 1);
//		}
		personMore = (LinearLayout) findViewById(R.id.person_more_btn);
		personMore.setOnClickListener(this);

		fragmentManager = getSupportFragmentManager(); // ��ȡ������
		tabContent = R.id.person_subtab;// ��Ҫ�滻ѡ�����������
		// ��ʼ��Ϊ������������
		fragmentTransaction = fragmentManager.beginTransaction();
		tabFragment = new PersonOrder();
		fragmentTransaction.add(tabContent, tabFragment, "order");
		fragmentTransaction.commit();
		// ��ʼ��ѡ���ǩ
		RadioGroup radioGroup = (RadioGroup) this
				.findViewById(R.id.person_tab_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroupCheckedChange());
	}

	// ѡ���ǩ����¼�
	public class RadioGroupCheckedChange implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			fragmentTransaction = fragmentManager.beginTransaction();
			switch (checkedId) {
			// ���������ѡ���¼�
			case R.id.person_tab_order:
				tabFragment = new PersonOrder();
				break;
			// �Ż�ȯ
			case R.id.person_tab_coupon:
				tabFragment = new PersonCoupon();

				break;
			// ������Ϣ
			case R.id.person_tab_info:
				tabFragment = new PersonInfo();
				break;
			// �ջ���ַ
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
		case R.id.person_more_btn:// ��������ҳ��
			 if(MyApplication.loginStat)
			 {
			intent = new Intent().setClass(this, PersonMore.class);
			 }
			 else
			Toast.makeText(this, "���ȵ�¼", 2000).show();
			break;

		default:
			// �����¼��ע����ť��ͼƬ�����δ��¼��ת����¼ҳ��
			if (!MyApplication.loginStat) {
				intent = new Intent().setClass(this, PersonLogin.class);
			} else // ����ѵ�¼ע����¼״̬
			{
				CustomDialog.Builder builder = new CustomDialog.Builder(this);
			    builder.setMessage("ȷ��ע����")
			      .setPositiveButton("ȷ��",
			        new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog,
			           int id) {
							loginOut(); 
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
			}

			break;

		}
		if (intent != null)
			startActivity(intent);

	}

	/**
	 * ע����¼
	 */
	private void loginOut() {
		// TODO Auto-generated method stub
		HashMap<String, String> paramter = new HashMap<String, String>();
		paramter.put("act", "logout");
		paramter.put("sessionid", MyApplication.seskey);
		// ��ӡ���ɵ����ӵ�ַ
		MyApplication.printLog(
				"Person--loginout",
				"Loginurl:"
						+ MyApplication.getUrl(paramter,
								Url.URL_USERS));
		/*
		 * ���������������
		 */
		MyApplication.client.postWithURL(Url.URL_USERS, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							MyApplication.printLog("Person--loginout", "result:"
									+ response.toString());
							int code = response.getInt("code");
							if (response.getInt("code") == 1) {
								Toast.makeText(Person.this, "ע���ɹ�", 2000).show();
								MyApplication.loginStat=false;
								Intent intent=new Intent().setClass(Person.this, PersonLogin.class);
								startActivity(intent);
							} else {
								Toast.makeText(Person.this, "ע��ʧ��", 2000).show();
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
		// Toast.makeText(this, "��¼�ɹ�", 2000).show();

	}


}
