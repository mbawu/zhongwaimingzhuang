package com.bdh.activity;

import cn.jpush.android.api.JPushInterface;

import com.bdh.activity.person.Person;
import com.bdh.activity.product.Home;
import com.bdh.activity.product.ProductList;
import com.bdh.activity.product.Search;
import com.bdh.activity.product.ShopCart;
import com.bdh.base.MyApplication;
import com.bdh.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

public class MenuBottom extends TabActivity {
	/** Called when the activity is first created. */
	public static TabHost tabHost; // �ײ��˵���
	public static RadioGroup radioGroup;
	private Resources resources; //��ȡ��Դ�ļ�

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		resources = getResources();
		initData();
	}

	
	private void initData() {
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		// ��ҳ�˵�
		intent = new Intent().setClass(this, Home.class);
		spec = tabHost.newTabSpec(resources.getString(R.string.main_menu_home))
				.setIndicator(resources.getString(R.string.main_menu_home))
				.setContent(intent);
		tabHost.addTab(spec);
		// �����˵�
		intent = new Intent().setClass(this, Search.class);
		spec = tabHost
				.newTabSpec(resources.getString(R.string.main_menu_search))
				.setIndicator(resources.getString(R.string.main_menu_search))
				.setContent(intent);
		tabHost.addTab(spec);

		// �б�˵�
		intent = new Intent().setClass(this, ProductList.class);
		spec = tabHost.newTabSpec(resources.getString(R.string.main_menu_list))
				.setIndicator(resources.getString(R.string.main_menu_list))
				.setContent(intent);
		tabHost.addTab(spec);

		// ���ﳵ�˵�
		intent = new Intent().setClass(this, ShopCart.class);
		spec = tabHost
				.newTabSpec(resources.getString(R.string.main_menu_shopcart))
				.setIndicator(resources.getString(R.string.main_menu_shopcart))
				.setContent(intent);
		tabHost.addTab(spec);

		// �������Ĳ˵�
		intent = new Intent().setClass(this, Person.class);
		spec = tabHost
				.newTabSpec(
						resources.getString(R.string.main_menu_personcenter))
				.setIndicator(
						resources.getString(R.string.main_menu_personcenter))
				.setContent(intent);
		tabHost.addTab(spec);

//		// ��¼����
//					intent = new Intent().setClass(this, PersonLogin.class);
//					spec = tabHost
//							.newTabSpec(resources.getString(R.string.person_login))
//							.setIndicator(resources.getString(R.string.person_login))
//							.setContent(intent);
//					tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		radioGroup = (RadioGroup) this
				.findViewById(R.id.main_tab_group);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.main_tab_home:
					tabHost.setCurrentTabByTag(resources
							.getString(R.string.main_menu_home));
					break;
				case R.id.main_tab_search:
					tabHost.setCurrentTabByTag(resources
							.getString(R.string.main_menu_search));
					break;
				case R.id.main_tab_list:
					tabHost.setCurrentTabByTag(resources
							.getString(R.string.main_menu_list));
					break;
				case R.id.main_tab_shopcart:
					tabHost.setCurrentTabByTag(resources
							.getString(R.string.main_menu_shopcart));
					break;
				case R.id.main_tab_personcenter:
					tabHost.setCurrentTabByTag(resources
							.getString(R.string.main_menu_personcenter));
					break;
				default:
					break;
				}
			}
		});
	}


}