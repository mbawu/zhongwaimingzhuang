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

	private String OrderCode; // ��������
	private String order_id; // ������ID
	private LinearLayout backBtn;// ���˰�ť
	private TextView code;// ������
	private TextView date;// ����ʱ��
	private TextView namePhone;// �ջ��˺͵绰
	private TextView address;// �ջ���ַ
	private TextView flag;// ����״̬
	private TextView fregitPrice;// �˷�
	private TextView totalprice;// ��Ʒ�ܼ�
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
		backBtn = (LinearLayout) findViewById(R.id.order_backbtn);// ���˰�ť
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderDetail.this.finish();

			}
		});
		code = (TextView) findViewById(R.id.order_code);// ������
		date = (TextView) findViewById(R.id.order_date);// ����ʱ��
		namePhone = (TextView) findViewById(R.id.order_name_phone);// �ջ��˺͵绰
		address = (TextView) findViewById(R.id.order_address);// �ջ���ַ
		flag = (TextView) findViewById(R.id.order_flag);// ����״̬
		fregitPrice = (TextView) findViewById(R.id.order_fregitPrice);//�˷�
		totalprice = (TextView) findViewById(R.id.order_totalprice);// ��Ʒ�ܼ�
		data = new ArrayList<Object>();
		listView = (ListView) findViewById(R.id.order_product_listview);
		listView.setDivider(null);
		adapter = new MyAdapter(this, NetworkAction.��������, data);
		listView.setAdapter(adapter);
	}

	private void initData() {
		Intent intent = getIntent();
//		OrderCode = intent.getStringExtra("OrderCode");
//		order_id = intent.getStringExtra("order_id");
//		sendData(NetworkAction.��������);
		Order order=(Order) intent.getSerializableExtra("order");
		code.setText("�����ţ�"+order.getOrderCode());
		date.setText("����ʱ�䣺"+order.getOutCreateTime());
		namePhone.setText(order.getRealName()
				+ " ("
				+ order.getMobile() + ")");
		address.setText(order.getAddress());
		int flagInfo = Integer.valueOf(order.getFlag());
		switch (flagInfo) {
		case 1:
			flag.setText("����״̬��"+"���µ�");
			break;
		case 2:
			flag.setText("����״̬��"+"��ȡ��");
			break;
		case 3:
			flag.setText("����״̬��"+"������");
			break;
		case 4:
			flag.setText("����״̬��"+"�������");
			break;
		case 5:
			flag.setText("����״̬��"+"�������");
			break;
		}

		fregitPrice.setText("��"+order.getFreight());
		totalprice.setText("��"+order.getTotalPrice());
		//��Ʒ��Ϣ
		ArrayList<Object> tempData=order.getProducts();
		for (int i = 0; i < tempData.size(); i++) {
			Product product=(Product) tempData.get(i);
			data.add(product);
		}
		adapter.notifyDataSetChanged();
		refreshListViewHeight();
	}

	//ˢ��listview�ĸ߶�
	public void refreshListViewHeight()
	{
		 int totalHeight = 0;   
			for (int index = 0, len = adapter.getCount(); index < len; index++) {     

	            View listViewItem = adapter.getView(index , null, listView);  

	            // ��������View �Ŀ��   

	            listViewItem.measure(0, 0);    

	            // ������������ĸ߶Ⱥ�

	            totalHeight += listViewItem.getMeasuredHeight()+5;    

	        }   
	        ViewGroup.LayoutParams params = listView.getLayoutParams();   

	        // listView.getDividerHeight()��ȡ�����ָ����ĸ߶�   

	        // params.height����ListView��ȫ��ʾ��Ҫ�ĸ߶�    


	        params.height = totalHeight+ (listView.getDividerHeight() * (adapter.getCount() - 1));   

	        listView.setLayoutParams(params);   
	}
}
