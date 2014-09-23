package com.bdh.activity.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.bdh.base.MyAdapter;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;
import com.bdh.model.Order;
import com.bdh.model.Product;
import com.bdh.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class OrderEvaluate extends Activity implements OnClickListener {



	private LinearLayout backBtn;
	private ListView listView;
	private MyAdapter adapter;
	private ArrayList<Object> data;
	private Product productComment;
	private String Evaluation;//评论等级。1、2、3级分别发1、2、3好中差评
	private String evaluation_star;//评论星级
	private String OrderItemID;
	private String commentContent;
	private String product_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_evaluate);
		initView();
		initData();
	}

	private void initData() {
		Intent intent=getIntent();
		Order order=(Order) intent.getSerializableExtra("order");

		ArrayList<Object> tempData=order.getProducts();
		for (int i = 0; i < tempData.size(); i++) {
			data.add(tempData.get(i));
		}
		adapter.notifyDataSetChanged();
		
	}

	private void initView() {
		backBtn = (LinearLayout) findViewById(R.id.order_evaluate_backbtn);
		backBtn.setOnClickListener(this);
		listView=(ListView) findViewById(R.id.order_evaluate_listView);
		data=new ArrayList<Object>();
		adapter=new MyAdapter(this, NetworkAction.评论订单, data);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.order_evaluate_backbtn:
			finish();
			break;

		default:
			break;
		}
	}

	public void publishComment(View view)
	{
		productComment=(Product) view.getTag();
		float stars=((RatingBar)view).getRating();
		evaluation_star=String.valueOf(stars);
		if(stars==1)
			Evaluation="3";
		else if(stars>1&&stars<=3)
			Evaluation="2";
		else if(stars>3&&stars<=5)
			Evaluation="1";
		commentContent=(String) view.getTag(R.id.tag_first);
		product_id=productComment.getId();
		OrderItemID=productComment.getOrderItemID();
		sendData(NetworkAction.评论订单);
	}
	
	public void sendData(final NetworkAction request)
	 {
	 HashMap<String, String> paramter = new HashMap<String, String>();
	   paramter.put("act", "comments");
	   paramter.put("sid", MyApplication.sid);
	   paramter.put("uid", MyApplication.uid);
	   paramter.put("sessionid", MyApplication.seskey);
	   paramter.put("Evaluation",Evaluation);
	   paramter.put("OrderItemID", OrderItemID);
	   paramter.put("commentContent", commentContent);
	   paramter.put("evaluation_star", evaluation_star);
	   paramter.put("product_id", product_id);
	   paramter.put("username", MyApplication.sp.getString("username", ""));
	   Log.i(MyApplication.TAG, request+MyApplication.getUrl(paramter, Url.URL_ORDER));
	   MyApplication.client.postWithURL(Url.URL_ORDER, paramter,
	     new Listener<JSONObject>() {
	      public void onResponse(JSONObject response) {
	       try {
	       Log.i(MyApplication.TAG, request+"response-->"+response.toString());
	        int code = response.getInt("code");
	        if (response.getInt("code") == 1) {
	        	 Toast.makeText(OrderEvaluate.this, "评论商品成功", 2000).show();
	        	 MyApplication.comment=true;
	        	 for (int i = 0; i < data.size(); i++) {
					Product product=(Product) data.get(i);
					if(product.getId().equals(product_id))
					{
						data.remove(product);
					}
				}
	        	 adapter.notifyDataSetChanged();
	        	 if(data.size()==0)
	        	 {
	        		 OrderEvaluate.this.finish();
	        	 }
	        } else {
	         Toast.makeText(OrderEvaluate.this, request+ErrorMsg.getErrorMsg(request, code), 2000).show();
	        }
	       } catch (JSONException e) {
	        Log.i(MyApplication.TAG, request+"JSONException-->"+e.getMessage());
	       }
	      }
	     }, new ErrorListener() {
	      @Override
	      public void onErrorResponse(VolleyError error) {
	       Log.i(MyApplication.TAG, request+"onErrorResponse-->"+error.getMessage());
	      }
	     });
	 }

	

	
}
