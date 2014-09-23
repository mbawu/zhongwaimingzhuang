package com.bdh.activity.product;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bdh.base.BaseActivity;
import com.bdh.base.MyAdapter;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;
import com.bdh.model.Product;
import com.bdh.R;

public class Search extends BaseActivity implements OnClickListener,OnItemClickListener,OnScrollListener {

	private AutoCompleteTextView searchTxt;// 搜索内容
	private GridView gridView;
	private MyAdapter adapter;
	private ArrayList<Object> data;
	private ImageView searchBtn;
	private int page=1;
	private String pagesize = "5";
	private int totalpage=0;
	private boolean load=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		initView();
	}

	private void initView() {
		searchTxt = (AutoCompleteTextView) findViewById(R.id.searchTxt);
		initAutoComplete("history", searchTxt);  //初始化历史数据
		gridView = (GridView) findViewById(R.id.search_gridview);
		data = new ArrayList<Object>();
		adapter = new MyAdapter(this, NetworkAction.搜索商品, data);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		gridView.setOnScrollListener(this);
		searchBtn = (ImageView) findViewById(R.id.searchbtn);
		searchBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.searchbtn:
			saveHistory("history", searchTxt);  
			data.clear();//先清空数据再从新搜索
			page=1;//重置页码
			sendData(NetworkAction.搜索商品);
			break;

		default:
			break;
		}

	}

	public void sendData(final NetworkAction request) {
		
		String search = searchTxt.getText().toString();
		Log.i(MyApplication.TAG, search);
		if (search.equals("")) {
			Toast.makeText(this, "请输入查询条件", 2000).show();
			return;
		}
		MyApplication.progressShow(this, request.toString());
		HashMap<String, String> paramter = new HashMap<String, String>();
		paramter.put("act", "search");
		paramter.put("sid", MyApplication.sid);
		paramter.put("store_id", MyApplication.sid);
		paramter.put("CacheID", "");
		paramter.put("CacheID1", "0");
		paramter.put("brans", "0");
		paramter.put("cates", "0");
		paramter.put("clear", "0");
		paramter.put("keyname", search);
		paramter.put("keyname1", "0");
		paramter.put("nowpage", String.valueOf(page));
		paramter.put("pagesize", pagesize);
		paramter.put("sort_type", "0");
		Log.i(MyApplication.TAG,
				request + MyApplication.getUrl(paramter, Url.URL_SEARCH));
		MyApplication.client.postWithURL(Url.URL_SEARCH, paramter,
				new Listener<JSONObject>() {
					

					public void onResponse(JSONObject response) {
						try {
							Log.i(MyApplication.TAG, request + "response-->"
									+ response.toString());
							int code = response.getInt("code");
							if (response.getInt("code") == 1) {
								page++;
								totalpage = response.getInt("totalpage");
								//判断是否还要加载数据
								if(page>totalpage)
									load=false;
								else
									load=true;
								// 获取新的分类商品之前先清空之前的数据
								
								JSONArray products = response
										.getJSONArray("list");

								// 判断是否有商品数据
								if (products.length() == 0)
								{
									Toast.makeText(Search.this,
											"没有搜索到任何商品" ,
											2000).show();
									adapter.notifyDataSetChanged();
								}
								else {
									for (int i = 0; i < products.length(); i++) {

										JSONObject product = products
												.getJSONObject(i);
										Product newProduct = new Product();
										newProduct.setId(product
												.getString("product_id"));
										newProduct.setName(product
												.getString("product_name"));
										newProduct.setStorePrice(product
												.getString("store_price"));
										newProduct.setImgPath(product
												.getString("product_photo"));
										data.add(newProduct);
									}
									adapter.notifyDataSetChanged();
								}
							} else {
								Toast.makeText(
										Search.this,
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long productId) {

		Intent intent = new Intent().setClass(this, ProductDetail.class);
		intent.putExtra("productId", String.valueOf(productId));
//		Toast.makeText(this,String.valueOf(productId) , 2000).show();
		startActivity(intent);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		//判断是否需要加载数据并且已经显示到了最后一条数据
		if(load && (firstVisibleItem+visibleItemCount==totalItemCount))
		{
			//重置加载标示
			load=false;
			//搜索后面几页的数据
			sendData(NetworkAction.搜索商品);                                                                                                                                                                                                                                                     
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	/** 

     * 把指定AutoCompleteTextView中内容保存到sharedPreference中指定的字符段 

     *  

     * @param field 

     *            保存在sharedPreference中的字段名 

     * @param autoCompleteTextView 

     *            要操作的AutoCompleteTextView 

     */  

    private void saveHistory(String field,  

            AutoCompleteTextView autoCompleteTextView) {  

        String text = autoCompleteTextView.getText().toString();  

        SharedPreferences sp = getSharedPreferences("network_url", 0);  

        String longhistory = sp.getString(field, "nothing");  

        if (!longhistory.contains(text + ",")) {  

            StringBuilder sb = new StringBuilder(longhistory);  

            sb.insert(0, text + ",");  

            sp.edit().putString("history", sb.toString()).commit();  

        }  

    }
    
    /** 

     * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示 

     *  

     * @param field 

     *            保存在sharedPreference中的字段名 

     * @param autoCompleteTextView 

     *            要操作的AutoCompleteTextView 

     */  

    private void initAutoComplete(String field,  

            AutoCompleteTextView autoCompleteTextView) {  

        SharedPreferences sp = getSharedPreferences("network_url", 0);  

        String longhistory = sp.getString("history", "nothing");  

        String[] histories = longhistory.split(",");  

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  

                android.R.layout.simple_dropdown_item_1line, histories);  

        // 只保留最近的50条的记录  

        if (histories.length > 50) {  

            String[] newHistories = new String[50];  

            System.arraycopy(histories, 0, newHistories, 0, 50);  

            adapter = new ArrayAdapter<String>(this,  

                    android.R.layout.simple_dropdown_item_1line, newHistories);  

        }  

        autoCompleteTextView.setAdapter(adapter);  

        autoCompleteTextView  

                .setOnFocusChangeListener(new OnFocusChangeListener() {  

                    @Override  

                    public void onFocusChange(View v, boolean hasFocus) {  

                        AutoCompleteTextView view = (AutoCompleteTextView) v;  

                        if (hasFocus) {  

//                            view.showDropDown();  

                        }  

                    }  

                });  

    }  
}
