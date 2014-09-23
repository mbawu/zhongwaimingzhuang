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
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;
import com.bdh.model.Order;
import com.bdh.model.Product;
import com.bdh.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetail extends Activity {

	private String OrderCode; // 主订单号
	private String order_id; // 主订单ID
	private LinearLayout backBtn;// 后退按钮
	private TextView code;// 订单号
	private TextView date;// 创建时间
	private TextView namePhone;// 收货人和电话
	private TextView address;// 收货地址
	private TextView flag;// 订单状态
	private TextView fregitPrice;// 运费
	private TextView totalprice;// 商品总价
	private ListView listView;
	private ArrayList<Object> data;
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail);
		initView();
		initData();
	}

	private void initView() {
		backBtn = (LinearLayout) findViewById(R.id.order_backbtn);// 后退按钮
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderDetail.this.finish();

			}
		});
		code = (TextView) findViewById(R.id.order_code);// 订单号
		date = (TextView) findViewById(R.id.order_date);// 创建时间
		namePhone = (TextView) findViewById(R.id.order_name_phone);// 收货人和电话
		address = (TextView) findViewById(R.id.order_address);// 收货地址
		flag = (TextView) findViewById(R.id.order_flag);// 订单状态
		fregitPrice = (TextView) findViewById(R.id.order_fregitPrice);//运费
		totalprice = (TextView) findViewById(R.id.order_totalprice);// 商品总价
		data = new ArrayList<Object>();
		listView = (ListView) findViewById(R.id.order_product_listview);
		listView.setDivider(null);
		adapter = new MyAdapter(this, NetworkAction.订单详情, data);
		listView.setAdapter(adapter);
	}

	private void initData() {
		Intent intent = getIntent();
//		OrderCode = intent.getStringExtra("OrderCode");
//		order_id = intent.getStringExtra("order_id");
//		sendData(NetworkAction.订单详情);
		Order order=(Order) intent.getSerializableExtra("order");
		code.setText("订单号："+order.getOrderCode());
		date.setText("创建时间："+order.getOutCreateTime());
		namePhone.setText(order.getRealName()
				+ " ("
				+ order.getMobile() + ")");
		address.setText(order.getAddress());
		int flagInfo = Integer.valueOf(order.getFlag());
		switch (flagInfo) {
		case 1:
			flag.setText("订单状态："+"已下单");
			break;
		case 2:
			flag.setText("订单状态："+"已取消");
			break;
		case 3:
			flag.setText("订单状态："+"派送中");
			break;
		case 4:
			flag.setText("订单状态："+"货物拒收");
			break;
		case 5:
			flag.setText("订单状态："+"交易完成");
			break;
		}

		fregitPrice.setText("￥"+order.getFreight());
		totalprice.setText("￥"+order.getTotalPrice());
		//商品信息
		ArrayList<Object> tempData=order.getProducts();
		for (int i = 0; i < tempData.size(); i++) {
			Product product=(Product) tempData.get(i);
			data.add(product);
		}
		adapter.notifyDataSetChanged();
		refreshListViewHeight();
	}

	//刷新listview的高度
	public void refreshListViewHeight()
	{
		 int totalHeight = 0;   
			for (int index = 0, len = adapter.getCount(); index < len; index++) {     

	            View listViewItem = adapter.getView(index , null, listView);  

	            // 计算子项View 的宽高   

	            listViewItem.measure(0, 0);    

	            // 计算所有子项的高度和

	            totalHeight += listViewItem.getMeasuredHeight()+5;    

	        }   
	        ViewGroup.LayoutParams params = listView.getLayoutParams();   

	        // listView.getDividerHeight()获取子项间分隔符的高度   

	        // params.height设置ListView完全显示需要的高度    


	        params.height = totalHeight+ (listView.getDividerHeight() * (adapter.getCount() - 1));   

	        listView.setLayoutParams(params);   
	}
}
