package com.bdh.activity.product;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bdh.base.MyAdapter;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.model.Comment;
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;
import com.bdh.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class CommentList extends Activity {

	private LinearLayout backBtn;// 后退按钮
	private RadioGroup commentGroup;
	private RadioButton goodRadio;
	private RadioButton normalRadio;
	private RadioButton badRadio;
	private String level = "1";// 评论类型
	private ListView listView;
	private ArrayList<Object> data;
	private MyAdapter adapter;
	private String productID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentlist);
		initView();
		initData();
	}

	private void initView() {
		data = new ArrayList<Object>();
		backBtn = (LinearLayout) findViewById(R.id.commentlist_backbtn);
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		commentGroup = (RadioGroup) findViewById(R.id.comment_group);
		commentGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.comment_tab_good:
					level = "1";
					break;
				case R.id.comment_tab_normal:
					level = "2";
					break;
				case R.id.comment_tab_bad:
					level = "3";
					break;
				}
				sendData(NetworkAction.评论列表);
			}
		});
		goodRadio = (RadioButton) findViewById(R.id.comment_tab_good);
		normalRadio = (RadioButton) findViewById(R.id.comment_tab_normal);
		badRadio = (RadioButton) findViewById(R.id.comment_tab_bad);
		listView = (ListView) findViewById(R.id.comment_listview);
		adapter = new MyAdapter(this, NetworkAction.评论列表, data);
		listView.setAdapter(adapter);
		listView.setDivider(null);
	}

	private void initData() {
		Intent intent = getIntent();
		productID = intent.getStringExtra("productID");
		goodRadio.setChecked(true);
	}

	public void sendData(final NetworkAction request) {
		MyApplication.progressShow(this, request.toString());
		String url = "";
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.评论列表)) {
			url = Url.URL_ORDER;
			paramter.put("act", "comments_list");
			paramter.put("sid", MyApplication.sid);
			paramter.put("ProductID", productID);
			paramter.put("level", level);
			paramter.put("page", "1");
			paramter.put("pagesize", "100000");
		}
		Log.i(MyApplication.TAG, request + MyApplication.getUrl(paramter, url));
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							Log.i(MyApplication.TAG, request + "response-->"
									+ response.toString());
							int code = response.getInt("code");
							if (response.getInt("code") == 1) {
								data.clear();
								goodRadio.setText("好评("+response.getString("level1")+")");
								normalRadio.setText("中评("+response.getString("level2")+")");
								badRadio.setText("差评("+response.getString("level3")+")");
								JSONArray commentArray=response.getJSONArray("list");
								if(commentArray.length()>0)
								{
									for (int i = 0; i < commentArray.length(); i++) {
										JSONObject commentObject=commentArray.getJSONObject(i);
										Comment comment=new Comment();
										comment.setComment_id(commentObject.getString("comment_id"));
										comment.setProduct_id(commentObject.getString("product_id"));
										comment.setUid(commentObject.getString("uid"));
										comment.setUsername(commentObject.getString("username"));
										comment.setComment_subject(commentObject.getString("comment_subject"));
										comment.setComment_content(commentObject.getString("comment_content"));
										comment.setComment_star(commentObject.getString("comment_star"));
										comment.setComment_evaluation(commentObject.getString("comment_evaluation"));
										comment.setCreatetime(commentObject.getString("createtime"));
										comment.setOutDeadLine(commentObject.getString("OutDeadLine"));
										data.add(comment);	
									}
								}
								adapter.notifyDataSetChanged();
							} else {
								Toast.makeText(CommentList.this, request
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
