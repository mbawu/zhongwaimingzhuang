package com.bdh.activity.product;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bdh.R;
import com.bdh.base.MyAdapter;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;
import com.bdh.model.Product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ProductListShow extends Activity implements OnClickListener,OnItemClickListener {

	private LinearLayout backBtn;// ���˰�ť
	private String Category_id;// ��ȡ������Ʒ�ķ���ID
	private String CacheID;// ��ȡ������Ʒ��CaheID
	private int page = 1; // ��Ҫ����鿴�����ݵ�ҳ��
	private String pageSize="10";
	private int totalpage=0;
	private GridView gridView;// ��ʾ���ݵ�����
	private MyAdapter adapter; // ������
	private ArrayList<Object> data;// ��Ʒ����
	private String sortType = "0"; //����ʽ
	private boolean load=true;//�Ƿ���Ҫ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productlist_show);
		initView();
		initData();
	}

	private void initView() {
		backBtn = (LinearLayout) findViewById(R.id.productlist_show_backbtn);
		backBtn.setOnClickListener(this);
		gridView = (GridView) findViewById(R.id.productlist_show_gridview);
		gridView.setOnItemClickListener(this);
		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//�ж��Ƿ���Ҫ�������ݲ����Ѿ���ʾ�������һ������
				if(load && (firstVisibleItem+visibleItemCount==(page-1)*Integer.valueOf(pageSize)) &&page>1)
				{
					//���ü��ر�ʾ
					load=false;
					//�������漸ҳ������
					sendData(NetworkAction.��ȡ������Ʒ);                                                                                                                                                                                                                                                     
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

		});
		data = new ArrayList<Object>();
		adapter = new MyAdapter(this, NetworkAction.��ȡ������Ʒ, data);
		gridView.setAdapter(adapter);
		RadioGroup radioGroup = (RadioGroup) this
				.findViewById(R.id.product_tab_group);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.product_tab_default://Ĭ������
					sortType="3";
					break;
				case R.id.product_tab_sale://��������
					sortType="0";
					break;
				case R.id.product_tab_comment://��������
					sortType="5";
					break;
				case R.id.product_tab_price://�۸�����
					sortType="2";
					break;
				case R.id.product_tab_new://��Ʒ����
					sortType="4";
					break;
				}
				// ��ȡ�µķ�����Ʒ֮ǰ�����֮ǰ������
				data.clear();
				page=1;
				sendData(NetworkAction.��ȡ������Ʒ);
			}

		});
	}

	private void initData() {
		// �Ȼ�ȡ�������ķ���ID
		Intent intent = getIntent();
		Category_id = intent.getStringExtra("Category_id");
		CacheID = intent.getStringExtra("CacheID");
		// ��ȡ�÷����µ�������Ʒ
		sendData(NetworkAction.��ȡ������Ʒ);
	}

	public void sendData(final NetworkAction request) {
		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.��ȡ������Ʒ)) {
			MyApplication.progressShow(this, request.toString());
			url = Url.URL_INDEX;
			paramter.put("act", "product");
			paramter.put("sid", MyApplication.sid);
			paramter.put("store_id", MyApplication.sid);
			paramter.put("nowpage", String.valueOf(page));
			paramter.put("pagesize", pageSize);
			paramter.put("keyname", Category_id);
			paramter.put("CacheID", CacheID);
			paramter.put("keyname1", "0");
			paramter.put("CacheID1", "0");
			paramter.put("brans", "0");
			paramter.put("clears", "0");
			paramter.put("start_price", "0");
			paramter.put("end_price", "0");
			paramter.put("sort_type", sortType);
			paramter.put("cates", "0");
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
								if (request.equals(NetworkAction.��ȡ������Ʒ)) {
									page++;
									totalpage = response.getInt("totalpage");
									
									JSONArray products = response
											.getJSONArray("list");
									//�ж��Ƿ�Ҫ��������
									if(page>totalpage)
										load=false;
									else
										load=true;
									// �ж��Ƿ�����Ʒ����
									if (products.length() == 0)
										Toast.makeText(ProductListShow.this,
												"�÷��໹û����Ʒ"+products.length(), 2000).show();
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
								}
								
							} else {
								Toast.makeText(ProductListShow.this, request
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.productlist_show_backbtn:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long productId) {

		Intent intent = new Intent().setClass(this, ProductDetail.class);
		intent.putExtra("productId", String.valueOf(productId));
		intent.putExtra("skid", "null");
//		Toast.makeText(this,String.valueOf(productId) , 2000).show();
		startActivity(intent);

	}
}
