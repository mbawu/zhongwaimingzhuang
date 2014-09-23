package com.bdh.activity.person;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bdh.activity.person.PersonOrder.RadioGroupCheckedChange;
import com.bdh.base.MyAdapter;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.model.Coupon;
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;
import com.bdh.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PersonCoupon extends Fragment {

	private View view;
	private RadioGroup radioGroup;// 订单状态选项卡
	private ListView listView;
	private String type = "0";// 查询类型。0全部，1有效，2无效
	private MyAdapter adapter;
	private ArrayList<Object> data;
	private RadioButton allCoupon;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if (view == null)
			view = inflater.inflate(R.layout.person_coupon, container, false);
		initView();
		return view;

	}

	private void initView() {
		data = new ArrayList<Object>();
		radioGroup = (RadioGroup) view.findViewById(R.id.person_coupon_tab);
		radioGroup.setOnCheckedChangeListener(new RadioGroupCheckedChange());
//		radioGroup.check(R.id.person_coupon_all);
		listView = (ListView) view.findViewById(R.id.person_coupon_listView);
		listView.setDivider(null);
		adapter = new MyAdapter(getActivity(), NetworkAction.我的优惠券, data);
		listView.setAdapter(adapter);
		allCoupon=(RadioButton) view.findViewById(R.id.person_coupon_all);
		allCoupon.setChecked(true);
	}

	// 选项卡标签点击事件
	public class RadioGroupCheckedChange implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			// 全部
			case R.id.person_coupon_all:
				type = "0";

				break;
			// 有效
			case R.id.person_coupon_use:
				type = "1";
				break;
			// 无效
			case R.id.person_coupon_unuse:
				type = "2";
				break;
			default:
				break;
			}
			data.clear();
			sendData(NetworkAction.我的优惠券);
		}

	}

	public void sendData(final NetworkAction request) {
		String url = "";
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.我的优惠券)) {
			MyApplication.progressShow(getActivity(), request.toString());
			url = Url.URL_MEMBER;
			paramter.put("act", "coupon");
			paramter.put("sid", MyApplication.sid);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("nowpage", "1");
			paramter.put("pagesize", "10000");
			paramter.put("type", type);
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
								if (request.equals(NetworkAction.我的优惠券)) {
									JSONArray lists = response
											.getJSONArray("list");
									for (int i = 0; i < lists.length(); i++) {
										Coupon coupon = new Coupon();
										JSONObject object = lists
												.getJSONObject(i);
										coupon.setCouponID(object
												.getString("coupon_id"));
										coupon.setProductID(object
												.getString("product_id"));
										coupon.setProductName(object
												.getString("product_name"));
										coupon.setOrderID(object
												.getString("order_id"));
										coupon.setPriceLine(object
												.getString("price_line"));
										coupon.setPrice(object
												.getString("price"));
										coupon.setStart_time(object
												.getString("start_time"));
										coupon.setEnd_time(object
												.getString("end_time"));
										coupon.setFlag(object.getString("flag"));
										coupon.setValidity(object
												.getString("validity"));
										data.add(coupon);
									}
									adapter.notifyDataSetChanged();
								}

							} else {
								Toast.makeText(getActivity(), request
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
