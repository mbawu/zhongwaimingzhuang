package com.bdh.activity.product;

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

import com.bdh.activity.MenuBottom;
import com.bdh.activity.person.PersonLogin;
import com.bdh.base.BaseActivity;
import com.bdh.base.CustomDialog;
import com.bdh.base.MyAdapter;
import com.bdh.base.MyApplication;
import com.bdh.model.NetworkAction;
import com.bdh.model.Product;
import com.bdh.R;


public class ShopCart extends BaseActivity implements OnCheckedChangeListener,OnClickListener{

	private LinearLayout emptyLayout;//�չ��ﳵģ��
	private LinearLayout unemptyLayout;//�ǿչ��ﳵģ��
	private LinearLayout deleteBtn;//ɾ����ť
	private CheckBox selectAll;//ȫѡ��ť
	private Button viewBtn;//ȥ��䰴ť
	private TextView totalPriceTxt;//С�Ƽ۸�
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
		emptyLayout=(LinearLayout) findViewById(R.id.shopcart_empty_layout);//�չ��ﳵģ��
		unemptyLayout=(LinearLayout) findViewById(R.id.shopcart_unempty_layout);//�ǿչ��ﳵģ��
		totalPriceTxt=(TextView) findViewById(R.id.shopcart_totalprice);
		deleteBtn=(LinearLayout) findViewById(R.id.shopcart_delete_btn);//ɾ����ť
		deleteBtn.setOnClickListener(this);
		viewBtn=(Button) findViewById(R.id.shopcart_view_btn);//ȥ��䰴ť
		viewBtn.setOnClickListener(this);
		selectAll=(CheckBox) findViewById(R.id.shopcart_deleteall);//ȫѡ��ť
		selectAll.setOnCheckedChangeListener(this);
		listView=(ListView) findViewById(R.id.shopcart_listview);
		listView.setDivider(null);
		adapter=new MyAdapter(this, NetworkAction.���ﳵ, MyApplication.shopCartList);
		listView.setAdapter(adapter);
		submit=(Button) findViewById(R.id.shopcart_pay_btn);//ȥ���㰴ť
		submit.setOnClickListener(this);
	}

	private void initData() {
//		Toast.makeText(this, "num-->"+num, 2000).show();
		//���ﳵΪ��ʱ�����ʾģʽ
		if(MyApplication.shopCartList.isEmpty())
		{
			deleteBtn.setVisibility(View.GONE);//����ɾ����ť
			emptyLayout.setVisibility(View.VISIBLE);//��ʾ�չ��ﳵģ��
			unemptyLayout.setVisibility(View.GONE);//���طǿչ��ﳵģ��
		}
		//���ﳵ�ǿ�ʱ�����ʾģʽ
		else
		{
			deleteBtn.setVisibility(View.VISIBLE);//��ʾɾ����ť
			emptyLayout.setVisibility(View.GONE);//���ؿչ��ﳵģ��
			unemptyLayout.setVisibility(View.VISIBLE);//��ʾ�ǿչ��ﳵģ��
		}
		
		//ˢ��listview�ĸ߶�
		refreshListViewHeight();
		adapter.notifyDataSetChanged();
		recalculatePrice();
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

	            totalHeight += listViewItem.getMeasuredHeight();    

	        }   
	        ViewGroup.LayoutParams params = listView.getLayoutParams();   

	        // listView.getDividerHeight()��ȡ�����ָ����ĸ߶�   

	        // params.height����ListView��ȫ��ʾ��Ҫ�ĸ߶�    


	        params.height = totalHeight+ (listView.getDividerHeight() * (adapter.getCount() - 1));   

	        listView.setLayoutParams(params);   
	}
	
	//�����ﳵ�е���Ʒ��������ɾ���������ı��ʱ�����¼���С�Ƶ����¼۸�ˢ��
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
		//����ȫѡ��ť��״̬���޸Ĺ��ﳵ�����е���Ʒ��ѡ��״̬��Ȼ��ˢ��������
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
		case R.id.shopcart_delete_btn://ɾ����ť
			int count=0;
			//�ж��Ƿ�ѡ��Ʒ
			for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
				Product product=(Product)MyApplication.shopCartList.get(i);
				if(product.isChecked())
					count++;
			}
			if(count==0)
				Toast.makeText(this, "��ѡ��ѡ��Ҫɾ������Ʒ��", 2000).show();
			else
			{
				
				CustomDialog.Builder builder = new CustomDialog.Builder(this);
			    builder.setMessage("��ȷ��Ҫɾ����Щ��Ʒ��")
			      .setPositiveButton("ȷ��",
			        new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog,
			           int id) {
			        	 if(deleteAll)
			        	 {	//���ȫ��ɾ���Ļ�ֱ�����
			        		 MyApplication.shopCartList.clear();
			        		 totalPriceTxt.setText("��0.00");
			        	 }
			        	 else
			        	 {
			        		 	//����й�ѡ����Ʒ����û�е��ȫѡ��ť�Ļ���ѭ��ɾ��
								for (int i = 0; i < MyApplication.shopCartList.size(); i++) {
									Product product=(Product)MyApplication.shopCartList.get(i);
									if(product.isChecked())
										MyApplication.shopCartList.remove(product);
								}
			        	 }
			        	//ˢ��listview�ĸ߶�
			     		refreshListViewHeight();
			        		recalculatePrice();
							adapter.notifyDataSetChanged();
							if(MyApplication.shopCartList.size()==0)
							{
								deleteBtn.setVisibility(View.GONE);//����ɾ����ť
								emptyLayout.setVisibility(View.VISIBLE);//��ʾ�չ��ﳵģ��
								unemptyLayout.setVisibility(View.GONE);//���طǿչ��ﳵģ��
							}
							selectAll.setChecked(false);
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
			break;
		case R.id.shopcart_view_btn://����ȥ��䰴ť
			MenuBottom.tabHost.setCurrentTab(0);
			 MenuBottom.radioGroup.check(R.id.main_tab_home);
			break;
		case R.id.shopcart_pay_btn://ȥ���㰴ť
			Intent intent=new Intent();
			if(MyApplication.loginStat)
			{
				if(!checkProducts())
				{
					Toast.makeText(this, "�빴ѡҪ�������Ʒ", 2000).show();
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
