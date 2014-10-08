package com.zwmz.activity.product;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.zwmz.R;
import com.zwmz.base.BaseActivity;
import com.zwmz.base.MyAdapter;
import com.zwmz.base.MyApplication;
import com.zwmz.base.Url;
import com.zwmz.model.Category;
import com.zwmz.model.ErrorMsg;
import com.zwmz.model.NetworkAction;

//��Ʒ�����б�
public class ProductList extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private LinearLayout backBtn;// ���˰�ť
	private ListView firstListView;// 1���б�listview
	private ListView secListView;// 2���б�listview

	private MyAdapter firstAdapter;// 1������������
	private MyAdapter secAdapter;// 2������������
	private MyAdapter thirdAdapter;// 3������������
	private ArrayList<Object> fristLevel;// 1����������
	private ArrayList<Object> secLevel;// 2����������
	public ArrayList<Object> thirdLevel;// 3����������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productlist);
		initView();
		initData();
	}

	private void initView() {
		backBtn = (LinearLayout) findViewById(R.id.productlist_backbtn);
		backBtn.setOnClickListener(this);
		firstListView = (ListView) findViewById(R.id.productlist_first_listview);
		firstListView.setDivider(null);
		firstListView.setOnItemClickListener(this);
		secListView = (ListView) findViewById(R.id.productlist_second_listview);
		secListView.setDivider(null);
		secListView.setOnItemClickListener(this);
		// thirdListView = (ListView)
		// findViewById(R.id.productlist_third_listview);
		// thirdListView.setDivider(null);
		// thirdListView.setOnItemClickListener(this);
		fristLevel = new ArrayList<Object>();
		secLevel = new ArrayList<Object>();
		thirdLevel = new ArrayList<Object>();
	}

	private void initData() {
		// TODO Auto-generated method stub
		sendData(NetworkAction.һ������);
	}

	public void sendData(final NetworkAction request) {
		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.һ������)) {
			MyApplication.progressShow(this, request.toString());
			url = Url.URL_INDEX;
			paramter.put("act", "category");
			paramter.put("sid", MyApplication.sid);
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
								// ��ȡ�������
								if (request.equals(NetworkAction.һ������)) {
									// if (1 == 1) {
									// ��ȡһ����������
									JSONArray firstList = response
											.getJSONArray("catlist1");
									for (int i = 0; i < firstList.length(); i++) {
										Category category = new Category();
										JSONObject item = firstList
												.getJSONObject(i);
										category.setCategory_id(item
												.getString("category_id"));
										category.setCacheID(item
												.getString("CacheID"));
										category.setParent_catid(item
												.getString("parent_catid"));
										category.setCategory_level(item
												.getString("category_level"));

										category.setCategory_name(item
												.getString("category_name"));
										fristLevel.add(i, category);
									}
									firstAdapter = new MyAdapter(
											ProductList.this,
											NetworkAction.һ������, fristLevel);
									firstListView.setAdapter(firstAdapter);

									// ��ȡ������������
									JSONArray secList = response
											.getJSONArray("catlist2");
									for (int i = 0; i < secList.length(); i++) {
										Category category = new Category();
										JSONObject item = secList
												.getJSONObject(i);
										category.setCategory_id(item
												.getString("category_id"));
										category.setCacheID(item
												.getString("CacheID"));
										category.setParent_catid(item
												.getString("parent_catid"));
										category.setCategory_level(item
												.getString("category_level"));

										category.setCategory_name(item
												.getString("category_name"));
										secLevel.add(i, category);
									}

									// ��ȡ������������
									JSONArray thirdList = response
											.getJSONArray("catlist3");
									for (int i = 0; i < thirdList.length(); i++) {
										Category category = new Category();
										JSONObject item = thirdList
												.getJSONObject(i);
										category.setCategory_id(item
												.getString("category_id"));
										category.setCacheID(item
												.getString("CacheID"));
										category.setParent_catid(item
												.getString("parent_catid"));
										category.setCategory_level(item
												.getString("category_level"));
										category.setCategory_name(item
												.getString("category_name"));
										thirdLevel.add(i, category);
									}
									Log.i(MyApplication.TAG, request
											+ "load-->" + thirdLevel.size());
								}

							} else {
								Toast.makeText(
										ProductList.this,
										request
												+ ErrorMsg.getErrorMsg(request,
														code), 2000).show();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.productlist_backbtn:// ���˰�ť
			backBtn.setVisibility(View.GONE);
			firstListView.setVisibility(View.VISIBLE);
			secListView.setVisibility(View.GONE);
			break;
		// case R.id.productlist_backbtn://���˰�ť
		//
		// break;

		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long parentId) {
		Log.i(MyApplication.TAG, "VISIBLEtest");
		ArrayList<Object> temp;
		switch (parent.getId()) {

		case R.id.productlist_first_listview: // ���һ������ʱ����¼�
			temp = new ArrayList<Object>(); // װ����ʱ��Ҫ��ʾ������
			for (int i = 0; i < secLevel.size(); i++) {
				Category secCategory = (Category) secLevel.get(i);
				// �ж���Щ�������������ڵ����һ����������ಢ�������ʱ������
				if (secCategory.getCategory_level().equals("2")
						&& secCategory.getParent_catid().equals("" + parentId)) {
					temp.add(secCategory);
				}
			}
			// ����ж��������ʱ��������
			if (temp.size() > 0) {
				backBtn.setVisibility(View.VISIBLE); // ��ʾ���˰�ť
				firstListView.setVisibility(View.GONE); // ����һ��������ͼ
				secListView.setVisibility(View.VISIBLE); // ��ʾ����������ͼ
				// ���ݵ���ĸ���ID������Ӧ��adapter
				secAdapter = new MyAdapter(this, NetworkAction.��������, temp);
				secListView.setAdapter(secAdapter);
			}
			// ֻ��һ�����࣬û�ж�����������
			else {
				backBtn.setVisibility(View.GONE); // ��ʾ���˰�ť
				firstListView.setVisibility(View.VISIBLE); // ��ʾһ��������ͼ
				secListView.setVisibility(View.GONE); // ���ض���������ͼ
				Intent intent = new Intent().setClass(this,
						ProductListShow.class);
				intent.putExtra("Category_id",
						((Category) fristLevel.get(position)).getCategory_id());
				intent.putExtra("CacheID",
						((Category) fristLevel.get(position)).getCacheID());
				startActivity(intent);
			}
			break;
		}

	}

}
