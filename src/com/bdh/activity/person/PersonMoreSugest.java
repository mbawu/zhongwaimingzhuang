package com.bdh.activity.person;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bdh.R;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;


public class PersonMoreSugest extends Activity implements OnClickListener{

	private LinearLayout backBtn;//后退按钮
	private Spinner sugetTypeSN;//意见类型
	private String sugetType="0";
	private ArrayAdapter<String> adapter; 
	 private static final String[] typeItem={"订单反馈","商品反馈","服务反馈","物流反馈","质量反馈","其他"};  
	private EditText contentTxt;//内容
	private EditText phoneTxt;//电话
	private EditText qqTxt;//qq
	private Button submit;//提交按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_more_sugest);
		initView();
		sendData(NetworkAction.意见反馈);
	}

	private void initView() {
		// TODO Auto-generated method stub
		backBtn=(LinearLayout) findViewById(R.id.person_more_sugest_backbtn);
		backBtn.setOnClickListener(this);
		sugetTypeSN=(Spinner) findViewById(R.id.person_more_sugest_spiner);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typeItem);
		//设置下拉列表的风格  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		sugetTypeSN.setAdapter(adapter);
		sugetTypeSN.setOnItemSelectedListener(new SpinnerSelectedListener());  
		contentTxt=(EditText) findViewById(R.id.person_more_sugest_content);
		phoneTxt=(EditText) findViewById(R.id.person_more_sugest_phone);
		qqTxt=(EditText) findViewById(R.id.person_more_sugest_qq);
		submit=(Button) findViewById(R.id.person_more_suget_comitbtn);
		submit.setOnClickListener(this);
	}

	 //使用数组形式操作  
    class SpinnerSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			sugetType=String.valueOf(arg2);
			Toast.makeText(PersonMoreSugest.this, ""+arg2, 2000).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}  
  
      
    }  
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_more_sugest_backbtn:
			finish();
			break;
		case R.id.person_more_suget_comitbtn:
			sendData(NetworkAction.意见反馈);
			break;
		default:
			break;
		}
		
	}
	
	//验证电话号码
			public static boolean isMobileNO(String mobiles){     
		        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
		        Matcher m = p.matcher(mobiles);     
		        return m.matches();     
		    } 
	
	public void sendData(final NetworkAction request)
	 {
		if(contentTxt.getText().toString().equals(""))
		{
			Toast.makeText(this, "请输入留言内容", 2000).show();
			return;
		}
		
		if(!isMobileNO(phoneTxt.getText().toString()))
		{
			Toast.makeText(this, "请输入正确的手机号码", 2000).show();
			return;
		}
	 HashMap<String, String> paramter = new HashMap<String, String>();
	   paramter.put("act", "feedback");
	   paramter.put("sid", MyApplication.sid);
	   paramter.put("uid",  MyApplication.uid);
	   paramter.put("sessionid", MyApplication.seskey);
	   paramter.put("contact",  contentTxt.getText().toString());
	   paramter.put("mobile",  phoneTxt.getText().toString());
	   paramter.put("qq",  qqTxt.getText().toString());
	   paramter.put("msg_type",  sugetType);
	   
	   Log.i(MyApplication.TAG, request+MyApplication.getUrl(paramter, Url.URL_MEMBER));
	   MyApplication.client.postWithURL(Url.URL_MEMBER, paramter,
	     new Listener<JSONObject>() {
	      public void onResponse(JSONObject response) {
	       try {
	       Log.i(MyApplication.TAG, request+"response-->"+response.toString());
	        int code = response.getInt("code");
	        if (response.getInt("code") == 1) {
	        	Toast.makeText(PersonMoreSugest.this, "提交意见成功", 2000).show();
	        } else {
	         Toast.makeText(PersonMoreSugest.this, request+ErrorMsg.getErrorMsg(request, code), 2000).show();
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
