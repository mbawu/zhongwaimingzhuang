package com.zwmz.activity.person;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.zwmz.activity.person.Person.RadioGroupCheckedChange;
import com.zwmz.activity.product.OrderDetail;
import com.zwmz.base.CustomDialog;
import com.zwmz.base.MyAdapter;
import com.zwmz.base.MyApplication;
import com.zwmz.base.Url;
import com.zwmz.model.ErrorMsg;
import com.zwmz.model.NetworkAction;
import com.zwmz.model.Order;
import com.zwmz.model.Product;
import com.zwmz.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PersonOrder extends Fragment {
	private View view;
	private RadioGroup radioGroup;// ����״̬ѡ�
	private ListView listView;
	private ArrayList<Object> data;
	private MyAdapter adapter;
	public static String orderType = "1";// Ҫ�鿴�Ķ������ͣ�1.�����2.��������3.���ջ���4.�����
	private String orderflag;
	private String payway;
	private String ispay;
	private RadioButton waitPayRadio;
	private RadioButton waitSendRadio;
	private RadioButton waitReceiveRadio;
	private RadioButton doneRadio;
	private boolean firstCreate = true;
	private String orderID;// ȡ������ʱ���ID��

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(MyApplication.TAG, "PersonOrder->onCreateView");
		if (view == null)
			view = inflater.inflate(R.layout.person_order, container, false);
		initView();
		return view;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//������۳ɹ��Ժ�ˢ�¶����б�
		if (MyApplication.comment) {
			sendData(NetworkAction.�����б�);
		}
		if(MyApplication.loginStat&&!MyApplication.comment)
		{
			Log.i(MyApplication.TAG, "PersonOrder->onResume");
			switch (Integer.valueOf(orderType)) {
			case 1:
				waitPayRadio.setChecked(true);
				break;
			case 2:
				waitSendRadio.setChecked(true);
				break;
			case 3:
				waitReceiveRadio.setChecked(true);
				break;
			case 4:
				doneRadio.setChecked(true);
				break;
			}
		}
	}

	private void initView() {
		radioGroup = (RadioGroup) view.findViewById(R.id.person_order_tab);
		radioGroup.setOnCheckedChangeListener(new RadioGroupCheckedChange());
		waitPayRadio = (RadioButton) view
				.findViewById(R.id.person_order_waitpay);
		waitSendRadio = (RadioButton) view
				.findViewById(R.id.person_order_waitsend);
		waitReceiveRadio = (RadioButton) view
				.findViewById(R.id.person_order_waitreceive);
		doneRadio = (RadioButton) view
				.findViewById(R.id.person_order_done);
//		waitPayRadio.setChecked(true);
		listView = (ListView) view.findViewById(R.id.person_order_listView);
		listView.setDivider(null);
		data = new ArrayList<Object>();
		adapter = new MyAdapter(this, NetworkAction.�����б�, data);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Order order = (Order) data.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), OrderDetail.class);
				intent.putExtra("order", order);
//				intent.putExtra("OrderCode", order.getOrderCode());
//				intent.putExtra("order_id", order.getOrderID());
				startActivity(intent);
			}
		});
	}

	// ѡ���ǩ����¼�
	public class RadioGroupCheckedChange implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			// ������
			case R.id.person_order_waitpay:
				orderflag = "100";
				payway = "1";
				ispay = "0";
				orderType = "1";
				break;
			// ������
			case R.id.person_order_waitsend:
				orderflag = "101";
				payway = "0";
				ispay = "0";
				orderType = "2";
				break;
			// ���ջ�
			case R.id.person_order_waitreceive:
				orderflag = "3";
				payway = "0";
				ispay = "0";
				orderType = "3";
				break;
			// �����
			case R.id.person_order_done:
				orderflag = "5";
				payway = "0";
				ispay = "0";
				orderType = "4";
				break;
			}
			Log.i(MyApplication.TAG, "OnCheckedChangeListener");
			sendData(NetworkAction.�����б�);
		}

	}

	// ȷ���ջ�
	public void confirmReceive(String orderID) {
		this.orderID = orderID;
		sendData(NetworkAction.ȷ���ջ�);
	}

	// ȡ������
	public void cancelOrder(String orderID) {
		this.orderID = orderID;
		
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
	    builder.setMessage("ȷ��ȡ��������")
	      .setPositiveButton("ȷ��",
	        new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog,
	           int id) {
	        	 sendData(NetworkAction.ȡ������);
	        	 dialog.cancel();
	         }
	        })
	      .setNegativeButton("����",
	        new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog,
	           int id) {
	          dialog.cancel();
	         }
	        });
	    CustomDialog alert = builder.create();
	    alert.show();
		
	}

	public void sendData(final NetworkAction request) {
		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.�����б�)) {
			MyApplication.progressShow(getActivity(), request.toString());
			url = Url.URL_ORDER;
			paramter.put("act", "OrderList");
			paramter.put("sid", MyApplication.sid);
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid", MyApplication.uid);
			paramter.put("page", "1");
			paramter.put("pagesize", "10000");
			paramter.put("orderflag", orderflag);
			paramter.put("payway", payway);
			paramter.put("ispay", ispay);
		} else if (request.equals(NetworkAction.ȡ������)) {
			url = Url.URL_ORDER;
			paramter.put("act", "CancelOrder");
			paramter.put("sid", MyApplication.sid);
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid", MyApplication.uid);
			paramter.put("order_id", orderID);
		} else if (request.equals(NetworkAction.ȷ���ջ�)) {
			url = Url.URL_ORDER;
			paramter.put("act", "receipt");
			paramter.put("sid", MyApplication.sid);
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid", MyApplication.uid);
			paramter.put("order_id", orderID);
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
								if (request.equals(NetworkAction.�����б�)) {
									data.clear();
									JSONArray orderArray = response
											.getJSONArray("list");
									for (int i = 0; i < orderArray.length(); i++) {
										JSONObject orderObject = orderArray
												.getJSONObject(i);
										Order order = new Order();
										order.setOrderID(orderObject
												.getString("OrderID"));
										order.setUserName(orderObject
												.getString("UserName"));
										order.setFreight(orderObject
												.getString("Freight"));
										order.setOrderSubject(orderObject
												.getString("OrderSubject"));
										order.setProductNum(orderObject
												.getString("ProductNum"));
										order.setTotalPrice(orderObject
												.getString("TotalPrice"));
										order.setOrderCode(orderObject
												.getString("OrderCode"));
										order.setRealName(orderObject
												.getString("RealName"));
										order.setMobile(orderObject
												.getString("Mobile"));
										order.setAddress(orderObject
												.getString("Address"));
										order.setFlag(orderObject
												.getString("Flag"));
										order.setOutCreateTime(orderObject
												.getString("OutCreateTime"));
										order.setIsPay(orderObject
												.getString("isPay"));
										order.setPayWay(orderObject
												.getString("PayWay"));
										order.setComments(orderObject
												.getString("comments"));
										order.setTotalRecord(orderObject
												.getString("ProductNum"));
										order.setOrderType(orderType);
										JSONArray productArray = orderObject
												.getJSONArray("ProductList");
										for (int j = 0; j < productArray
												.length(); j++) {
											JSONObject productObject = productArray
													.getJSONObject(j);
											Product product = new Product();
											product.setId(productObject
													.getString("ProductID"));
											
											String OrderItemID=null;
											try {
												OrderItemID=productObject
														.getString("OrderItemID");
											} catch (Exception e) {
											}
											if(OrderItemID!=null)
											product.setOrderItemID(productObject
													.getString("OrderItemID"));
											product.setOrderCode(productObject
													.getString("OrderCode"));
											product.setNum(productObject
													.getString("ProductNum"));
											product.setStorePrice(productObject
													.getString("ProductPrice"));
											product.setTotalPrice(productObject
													.getString("ProductTotalprice"));
											product.setNature(productObject
													.getString("ProductAttribute"));
											product.setImgPath(productObject
													.getString("Image"));
											product.setAttribute(productObject
													.getString("attribute"));
											product.setName(productObject
													.getString("ProductName"));
											product.setBuy_type("1");
											order.addProduct(product);
										}
										data.add(order);
									}
									if(data.size()==0)
									{
										Toast.makeText(getActivity(), "���޶���",
												2000).show();
									}
									adapter.notifyDataSetChanged();
								} else if (request.equals(NetworkAction.ȡ������)) {
									Toast.makeText(getActivity(), "ȡ�������ɹ�",
											2000).show();
									sendData(NetworkAction.�����б�);
								}
								else if (request.equals(NetworkAction.ȷ���ջ�)) {
									Toast.makeText(getActivity(), "ȷ�ϳɹ�",
											2000).show();
									sendData(NetworkAction.�����б�);
								}
							} else {
								Toast.makeText(
										getActivity(),
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
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(MyApplication.TAG, "PersonOrder->onDestroy");
	}
}
