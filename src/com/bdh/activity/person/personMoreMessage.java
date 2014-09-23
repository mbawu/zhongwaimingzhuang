package com.bdh.activity.person;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.bdh.base.MyAdapter;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.model.ErrorMsg;
import com.bdh.model.MyMessage;
import com.bdh.model.NetworkAction;
import com.bdh.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class personMoreMessage extends Activity implements OnClickListener {

	private LinearLayout backBtn;
	private ListView listView;
	private MyAdapter myAdapter;
	private ArrayList<Object> data;
	private boolean deleteModel;// 是否在删除状态下
	private LinearLayout deleteBtn;
	private ImageView deleteImg;
	private TextView deleteTxt;
	private LinearLayout deleteLayout;// 全选按钮模块
	private LinearLayout emptyLayout;// 没有消息时候的显示模块
	private CheckBox deleteAll;
	private boolean deleteAllModel = false;// 是否是全选删除的状态

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_more_message);
		initView();
		sendData(NetworkAction.我的消息, null);
	}

	private void initView() {
		emptyLayout = (LinearLayout) findViewById(R.id.message_empty);
		deleteLayout = (LinearLayout) findViewById(R.id.message_delete_layout);
		deleteTxt = (TextView) findViewById(R.id.message_delete_txt);
		deleteImg = (ImageView) findViewById(R.id.message_delete_img);
		deleteBtn = (LinearLayout) findViewById(R.id.message_delete_btn);
		deleteBtn.setOnClickListener(this);
		backBtn = (LinearLayout) findViewById(R.id.person_more_msg_backbtn);
		backBtn.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.person_more_msg_listview);
		listView.setDivider(null);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(personMoreMessage.this, PersonMoreMessageDetail.class);
				intent.putExtra("msg", (MyMessage)data.get(position));
				startActivity(intent);
			}
		});
		data = new ArrayList<Object>();
		myAdapter = new MyAdapter(this, NetworkAction.我的消息, data);
		listView.setAdapter(myAdapter);
		deleteAll = (CheckBox) findViewById(R.id.message_deleteall);
		deleteAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				deleteAllModel = isChecked;
				sendData(NetworkAction.我的消息, null);
			}
		});
	}

	
	
	// 检查最新的消息是否为空
	public void isEmpty() {
		if (data.isEmpty()) {
			emptyLayout.setVisibility(View.VISIBLE);
			deleteLayout.setVisibility(View.GONE);
			deleteTxt.setVisibility(View.GONE);
			deleteImg.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_more_msg_backbtn:
			finish();
			break;
		case R.id.message_delete_btn:
			// 第一次点击删除按钮
			if (!deleteModel) {
				deleteTxt.setVisibility(View.VISIBLE);
				deleteImg.setVisibility(View.GONE);
				deleteLayout.setVisibility(View.VISIBLE);
				deleteModel = true;
				sendData(NetworkAction.我的消息, null);
			} else {
				if (data.size() > 0) {
					for (int i = 0; i < data.size(); i++) {
						MyMessage msg = (MyMessage) data.get(i);
						if (!deleteAllModel) {
							if (msg.isChecked())
								sendData(NetworkAction.删除消息,
										msg.getMessStateID());
						} else {
							sendData(NetworkAction.删除消息, msg.getMessStateID());
							Toast.makeText(personMoreMessage.this,
									"消息已全部清空", 2000).show();
						}
					}
				}
			}

			break;
		}

	}

	public void sendData(final NetworkAction request, String deleteMsgID) {
		
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.我的消息)) {
			MyApplication.progressShow(this, request.toString());
			paramter.put("act", "Message");
			paramter.put("sid", MyApplication.sid);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("nowpage", "1");
			paramter.put("pagesize", "10000");
		} else if (request.equals(NetworkAction.删除消息)) {
			paramter.put("act", "MessageDel");
			paramter.put("sid", MyApplication.sid);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("MessStateID", deleteMsgID);
		}

		Log.i(MyApplication.TAG,
				request + MyApplication.getUrl(paramter, Url.URL_MEMBER));
		MyApplication.client.postWithURL(Url.URL_MEMBER, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							Log.i(MyApplication.TAG, request + "response-->"
									+ response.toString());
							int code = response.getInt("code");
							if (response.getInt("code") == 1) {
								if (request.equals(NetworkAction.我的消息)) {
									data.clear();
									JSONArray msgAll = response
											.getJSONArray("list");
									for (int i = 0; i < msgAll.length(); i++) {
										JSONObject msg = msgAll
												.getJSONObject(i);
										MyMessage message = new MyMessage();
										message.setMessStateID(msg
												.getString("MessStateID"));
										message.setSubject(msg
												.getString("MessageSubject"));
										message.setContent(msg
												.getString("MessageContent"));
										message.setCreatTime(msg
												.getString("OutCreateTime"));
										if (deleteModel)
										{
											message.setShowCheckBox(true);
											if(deleteAllModel)
												message.setChecked(true);
											else
												message.setChecked(false);
										}
										else
											message.setShowCheckBox(false);
										data.add(message);
									}
									Log.i(MyApplication.TAG, "data.isEmpty->"+data.isEmpty());
									// 检查最新的消息是否为空
									isEmpty();
									myAdapter.notifyDataSetChanged();
								} else if (request.equals(NetworkAction.删除消息)) {
									if(!deleteAllModel)
										Toast.makeText(personMoreMessage.this,
											"删除成功", 2000).show();
									sendData(NetworkAction.我的消息, null);
								}
							} else {
								Toast.makeText(personMoreMessage.this, request
										+ ErrorMsg.getErrorMsg(request, code),
										2000).show();
							}
						} catch (JSONException e) {
							Log.i(MyApplication.TAG, request
									+ "JSONException-->" + e.getMessage());
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i(MyApplication.TAG, request + "onErrorResponse-->"
								+ error.getMessage());
					}
				});
	}
}
