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

//商品分类列表
public class ProductList extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private LinearLayout backBtn;// 后退按钮
	private ListView firstListView;// 1级列表listview
	private ListView secListView;// 2级列表listview

	private MyAdapter firstAdapter;// 1级分类适配器
	private MyAdapter secAdapter;// 2级分类适配器
	private MyAdapter thirdAdapter;// 3级分类适配器
	private ArrayList<Object> fristLevel;// 1级分类数据
	private ArrayList<Object> secLevel;// 2级分类数据
	public ArrayList<Object> thirdLevel;// 3级分类数据

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
		sendData(NetworkAction.一级分类);
	}

	public void sendData(final NetworkAction request) {
		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.一级分类)) {
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
								// 获取分类操作
								if (request.equals(NetworkAction.一级分类)) {
									// if (1 == 1) {
									// 获取一级分类数据
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
											NetworkAction.一级分类, fristLevel);
									firstListView.setAdapter(firstAdapter);

									// 获取二级分类数据
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

									// 获取三级分类数据
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
		case R.id.productlist_backbtn:// 后退按钮
			backBtn.setVisibility(View.GONE);
			firstListView.setVisibility(View.VISIBLE);
			secListView.setVisibility(View.GONE);
			break;
		// case R.id.productlist_backbtn://后退按钮
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

		case R.id.productlist_first_listview: // 点击一级分类时候的事件
			temp = new ArrayList<Object>(); // 装载临时需要显示的数据
			for (int i = 0; i < secLevel.size(); i++) {
				Category secCategory = (Category) secLevel.get(i);
				// 判断哪些二级分类是属于点击的一级分类的子类并添加在临时集合中
				if (secCategory.getCategory_level().equals("2")
						&& secCategory.getParent_catid().equals("" + parentId)) {
					temp.add(secCategory);
				}
			}
			// 如果有二级分类的时候处理的情况
			if (temp.size() > 0) {
				backBtn.setVisibility(View.VISIBLE); // 显示后退按钮
				firstListView.setVisibility(View.GONE); // 隐藏一级分类视图
				secListView.setVisibility(View.VISIBLE); // 显示二级分类视图
				// 根据点击的父类ID设置相应的adapter
				secAdapter = new MyAdapter(this, NetworkAction.二级分类, temp);
				secListView.setAdapter(secAdapter);
			}
			// 只有一级分类，没有二级分类的情况
			else {
				backBtn.setVisibility(View.GONE); // 显示后退按钮
				firstListView.setVisibility(View.VISIBLE); // 显示一级分类视图
				secListView.setVisibility(View.GONE); // 隐藏二级分类视图
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
