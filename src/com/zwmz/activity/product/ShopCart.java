package com.zwmz.activity.product;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zwmz.activity.MenuBottom;
import com.zwmz.activity.person.PersonLogin;
import com.zwmz.base.BaseActivity;
import com.zwmz.base.CustomDialog;
import com.zwmz.base.MyAdapter;
import com.zwmz.base.MyApplication;
import com.zwmz.model.NetworkAction;
import com.zwmz.model.Product;
import com.zwmz.R;


public class ShopCart extends BaseActivity implements OnCheckedChangeListener,OnClickListener{

	private LinearLayout emptyLayout;//空购物车模块
	private LinearLayout unemptyLayout;//非空购物车模块
	private LinearLayout deleteBtn;//删除按钮
	private CheckBox selectAll;//全选按钮
	private Button viewBtn;//去逛逛按钮
	private TextView totalPriceTxt;//小计价格
	private MyAdapter adapter;
	public ListView listView;
	private boolean deleteAll=false;
	private Button submit;
	private ArrayList<Object> tempSubmitProduct;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopcart);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(MyApplication.TAG, "onResume-->shopcart->"+MyApplication.shopCartList.size());
		initData();
	}
	
	private void initView() {
		tempSubmitProduct=new ArrayList<Object>();
		emptyLayout=(LinearLayout) findViewById(R.id.shopcart_empty_layout);//空购物车模块
		unemptyLayout=(LinearLayout) findViewById(R.id.shopcart_unempty_layout);//非空购物车模块
		totalPriceTxt=(TextView) findViewById(R.id.shopcart_totalprice);
		deleteBtn=(LinearLayout) findViewById(R.id.shopcart_delete_btn);//删除按钮
		deleteBtn.setOnClickListener(this);
		viewBtn=(Button) findViewById(R.id.shopcart_view_btn);//去逛逛按钮
		viewBtn.setOnClickListener(this);
		selectAll=(CheckBox) findViewById(R.id.shopcart_deleteall);//全选按钮
		selectAll.setOnCheckedChangeListener(this);
		listView=(ListView) findViewById(R.id.shopcart_listview);
		listView.setDivider(null);
		adapter=new MyAdapter(this, NetworkAction.购物车, MyApplication.shopCartList);
		listView.setAdapter(adapter);
		submit=(Button) findViewById(R.id.shopcart_pay_btn);//去结算按钮
		submit.setOnClickListener(this);
	}

	private void initData() {
//		Toast.makeText(this, "num-->"+num, 2000).show();
		//购物车为空时候的显示模式
		if(MyApplication.shopCartList.isEmpty())
		{
			deleteBtn.setVisibility(View.GONE);//隐藏删除按钮
			emptyLayout.setVisibility(View.VISIBLE);//显示空购物车模块
			unemptyLayout.setVisibility(View.GONE);//隐藏非空购物车模块
		}
		//购物车非空时候的显示模式
		else
		{
			deleteBtn.setVisibility(View.VISIBLE);//显示删除按钮
			emptyLayout.setVisibility(View.GONE);//隐藏空购物车模块
			unemptyLayout.setVisibility(View.VISIBLE);//显示非空购物车模块
		}
		
		//刷新listview的高度
		refreshListViewHeight();
		adapter.notifyDataSetChanged();
		recalculatePrice();
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

	            totalHeight += listViewItem.getMeasuredHeight();    

	        }   
	        ViewGroup.LayoutParams params = listView.getLayoutParams();   

	        // listView.getDividerHeight()获取子项间分隔符的高度   

	        // params.height设置ListView完全显示需要的高度    


	        params.height = totalHeight+ (listView.getDividerHeight() * (adapter.getCount() - 1));   

	        listView.setLayoutParams(params);   
	}
	
	//当购物车中的商品发生增加删除，数量改变的时候重新计算小计的最新价格并刷新
	public void recalculatePrice()
	{
		if(!MyApplication.shopCartList.isEmpty())
		{
			double totalPrice=0;
			for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
				Product product=(Product)MyApplication.shopCartList.get(i);
				double price=Double.valueOf(product.getStorePrice());
				int num=Integer.valueOf(product.getNum());
				double siglePrice=num*price;
				product.setTotalPrice(String.valueOf(siglePrice));
				totalPrice=totalPrice+siglePrice;
			}
			DecimalFormat df = new DecimalFormat(
					 ".00");
			df.format(totalPrice);
			totalPriceTxt.setText(String.valueOf(totalPrice));
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.i(MyApplication.TAG, "size"+MyApplication.shopCartList.size());
		//更改全选按钮的状态，修改购物车内所有的商品的选中状态，然后刷新适配器
		for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
			Product product=(Product)MyApplication.shopCartList.get(i);
			if(product.isChecked()!=isChecked)
				product.setChecked(isChecked);
			Log.i(MyApplication.TAG, "isChecked"+product.isChecked());
		}
		adapter.notifyDataSetChanged();
		if(isChecked)
			deleteAll=true;
		else
			deleteAll=false;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shopcart_delete_btn://删除按钮
			int count=0;
			//判断是否勾选商品
			for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
				Product product=(Product)MyApplication.shopCartList.get(i);
				if(product.isChecked())
					count++;
			}
			if(count==0)
				Toast.makeText(this, "请选勾选需要删除的商品！", 2000).show();
			else
			{
				
				CustomDialog.Builder builder = new CustomDialog.Builder(this);
			    builder.setMessage("你确定要删除这些商品吗？")
			      .setPositiveButton("确定",
			        new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog,
			           int id) {
			        	 if(deleteAll)
			        	 {	//如果全部删除的话直接清空
			        		 MyApplication.shopCartList.clear();
			        		 totalPriceTxt.setText("￥0.00");
			        	 }
			        	 else
			        	 {
			        		 	//如果有勾选的商品但是没有点击全选按钮的话再循环删除
								for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
									Product product=(Product)MyApplication.shopCartList.get(i);
									if(product.isChecked())
										MyApplication.shopCartList.remove(product);
								}
			        	 }
			        	//刷新listview的高度
			     		refreshListViewHeight();
			        		recalculatePrice();
							adapter.notifyDataSetChanged();
							if(MyApplication.shopCartList.size()==0)
							{
								deleteBtn.setVisibility(View.GONE);//隐藏删除按钮
								emptyLayout.setVisibility(View.VISIBLE);//显示空购物车模块
								unemptyLayout.setVisibility(View.GONE);//隐藏非空购物车模块
							}
							selectAll.setChecked(false);
							   dialog.cancel();
			         }
			        })
			      .setNegativeButton("返回",
			        new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog,
			           int id) {
			          dialog.cancel();
			         }
			        });
			    CustomDialog alert = builder.create();
			    alert.show();
			
			}
			break;
		case R.id.shopcart_view_btn://马上去逛逛按钮
			MenuBottom.tabHost.setCurrentTab(0);
			 MenuBottom.radioGroup.check(R.id.main_tab_home);
			break;
		case R.id.shopcart_pay_btn://去结算按钮
			Intent intent=new Intent();
			if(MyApplication.loginStat)
			{
				if(!checkProducts())
				{
					Toast.makeText(this, "请勾选要购买的商品", 2000).show();
					return;
				}
			intent.setClass(this, SubmitOrder.class);
			intent.putExtra("products", tempSubmitProduct);
			try {
				MyApplication.shopCartManager.saveProducts(MyApplication.shopCartList);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			else
			{
				intent.setClass(this, PersonLogin.class);
			}
			startActivity(intent);
			break;
		}
		
	}

	private boolean checkProducts() {
		tempSubmitProduct.clear();
		for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
			Product product=(Product) MyApplication.shopCartList.get(i);
			if(product.isChecked())
				tempSubmitProduct.add(product);
		}
		
		if(tempSubmitProduct.size()>0)
			return true;
		else
			return false;
	}
}
