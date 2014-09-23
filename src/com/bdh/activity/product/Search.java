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

	private AutoCompleteTextView searchTxt;// ��������
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
		initAutoComplete("history", searchTxt);  //��ʼ����ʷ����
		gridView = (GridView) findViewById(R.id.search_gridview);
		data = new ArrayList<Object>();
		adapter = new MyAdapter(this, NetworkAction.������Ʒ, data);
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
			data.clear();//����������ٴ�������
			page=1;//����ҳ��
			sendData(NetworkAction.������Ʒ);
			break;

		default:
			break;
		}

	}

	public void sendData(final NetworkAction request) {
		
		String search = searchTxt.getText().toString();
		Log.i(MyApplication.TAG, search);
		if (search.equals("")) {
			Toast.makeText(this, "�������ѯ����", 2000).show();
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
								//�ж��Ƿ�Ҫ��������
								if(page>totalpage)
									load=false;
								else
									load=true;
								// ��ȡ�µķ�����Ʒ֮ǰ�����֮ǰ������
								
								JSONArray products = response
										.getJSONArray("list");

								// �ж��Ƿ�����Ʒ����
								if (products.length() == 0)
								{
									Toast.makeText(Search.this,
											"û���������κ���Ʒ" ,
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
		//�ж��Ƿ���Ҫ�������ݲ����Ѿ���ʾ�������һ������
		if(load && (firstVisibleItem+visibleItemCount==totalItemCount))
		{
			//���ü��ر�ʾ
			load=false;
			//�������漸ҳ������
			sendData(NetworkAction.������Ʒ);                                                                                                                                                                                                                                                     
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	/** 

     * ��ָ��AutoCompleteTextView�����ݱ��浽sharedPreference��ָ�����ַ��� 

     *  

     * @param field 

     *            ������sharedPreference�е��ֶ��� 

     * @param autoCompleteTextView 

     *            Ҫ������AutoCompleteTextView 

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

     * ��ʼ��AutoCompleteTextView�������ʾ5����ʾ��ʹ AutoCompleteTextView��һ��ʼ��ý���ʱ�Զ���ʾ 

     *  

     * @param field 

     *            ������sharedPreference�е��ֶ��� 

     * @param autoCompleteTextView 

     *            Ҫ������AutoCompleteTextView 

     */  

    private void initAutoComplete(String field,  

            AutoCompleteTextView autoCompleteTextView) {  

        SharedPreferences sp = getSharedPreferences("network_url", 0);  

        String longhistory = sp.getString("history", "nothing");  

        String[] histories = longhistory.split(",");  

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  

                android.R.layout.simple_dropdown_item_1line, histories);  

        // ֻ���������50���ļ�¼  

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
