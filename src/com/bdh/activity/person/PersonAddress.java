package com.bdh.activity.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.bdh.base.MyAdapter;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;
import com.bdh.model.Address;
import com.bdh.model.City;
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;
import com.bdh.R;

import android.app.ProgressDialog;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import android.widget.Toast;

public class PersonAddress extends Fragment implements OnClickListener {

	private ListView addressList;// 地址列表的listView
	private LinearLayout addNew;// 新增收货地址视图
	private View view = null;// 收货地址视图
	private Spinner provinceSp; // 省份列表
	private Spinner citySp;// 城市列表
	private Spinner areaSp;// 区域列表
	private EditText nameTxt; // 收货人姓名
	private EditText detailTxt; // 街道地址详情
	private EditText phoneTxt; // 手机号码
	// private EditText emailTxt; // 邮箱
	private Button saveBtn;// 保存收货按钮
	private String defaultProvice;// 默认省级城市
	private String defaultPID;// 默认省级城市ID
	private MyAdapter adapter;
	ArrayList<Object> dataAddress; // 收货地址集合
	ArrayAdapter<String> provinceAdapter; // 省级列表适配器
	ArrayAdapter<String> cityAdapter;// 市级列表适配器
	ArrayAdapter<String> areaAdapter;// 区域列表适配器
	private String cityName;// 保存的市级名称，用来获取区域列表
	private String cityId;// 保存的市级ID，用来获取区域列表
	private String areaName;// 保存的区域名称
	private String areaId;// 保存的区域ID
	private boolean editFlag = false;// 判断是新增地址还是编辑地址标示
	private String address_id;//保存需要修改的地址的ID

	
	// 创建视图
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null)
			view = inflater.inflate(R.layout.person_address, container, false);
		initView(view);
		initData();
		return view;
	}

	private void initView(View view) {
		// MyApplication.mypDialog=new ProgressDialog(getActivity());
		nameTxt = (EditText) view.findViewById(R.id.person_address_name);// 收货人姓名
		detailTxt = (EditText) view.findViewById(R.id.person_address_detail); // 街道地址详情
		phoneTxt = (EditText) view.findViewById(R.id.person_address_phone); // 手机号码
		// emailTxt = (EditText) view.findViewById(R.id.person_address_email);
		// // 邮箱
		addressList = (ListView) view.findViewById(R.id.person_addressList);// 地址列表listview
		addNew = (LinearLayout) view
				.findViewById(R.id.person_address_add_layout);// 新增收货地址视图

		saveBtn = (Button) view.findViewById(R.id.person_address_savebtn);// 保存收货按钮
		saveBtn.setOnClickListener(this);
		dataAddress = new ArrayList<Object>();
		// 适配器
		adapter = new MyAdapter(this, NetworkAction.获取收货地址列表, dataAddress);
		// MyApplication.getInstance()
		// .getClassName(this.getClass()), data);
		// 初始化地区列表控件
		provinceSp = (Spinner) view.findViewById(R.id.person_address_provice);
		citySp = (Spinner) view.findViewById(R.id.person_address_city);
		areaSp = (Spinner) view.findViewById(R.id.person_address_area);
	}

	private void initData() {

		sendData(NetworkAction.获取收货地址列表);
		// getAddressList();
		addFooterView(); // 先添加底部新增按钮再添加适配器
		addressList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_address_savebtn:// 保存新增收货地址
			String name = nameTxt.getText().toString();
			String detail = detailTxt.getText().toString();
			String phone = phoneTxt.getText().toString();
			if (name.equals("") || detail.equals("") || phone.equals("")) {
				Toast.makeText(getActivity(), "请输入完整信息", 2000).show();
				return;
			}
			if(!isMobileNO(phone))
			{
				Toast.makeText(getActivity(), "请输入正确的手机号码", 2000).show();
				return;
			}
			if (editFlag)
				sendData(NetworkAction.修改收货地址);
			else
				sendData(NetworkAction.新增收货地址);
			// Log.i(MyApplication.TAG," lists-->"+"defaultProvice"+defaultProvice+"  defaultPID"+defaultPID
			// +"  cityName"+cityName+"  cityId"+cityId+"  areaName"+areaName+"  areaId"+areaId);
			break;

		default:
			break;
		}

	}

	
	//验证电话号码
		public static boolean isMobileNO(String mobiles){     
	        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
	        Matcher m = p.matcher(mobiles);     
	        return m.matches();     
	    } 
	/**
	 * 向服务器发送数据
	 */
	private void sendData(final NetworkAction request) {
		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();

		if (request.equals(NetworkAction.省级列表)) {
			MyApplication.progressShow(
					PersonAddress.this.getActivity(),
					 request.toString());
			url = Url.URL_API;
			paramter.put("tpage", "address");
			paramter.put("act", "province");
		} else if (request.equals(NetworkAction.市级列表)) {
			url = Url.URL_API;
			paramter.put("tpage", "address");
			paramter.put("act", "city");
			paramter.put("province_id", defaultPID);
		} else if (request.equals(NetworkAction.区域列表)) {
			url = Url.URL_API;
			paramter.put("tpage", "address");
			paramter.put("act", "county");
			paramter.put("city_id", cityId);
		} else if (request.equals(NetworkAction.新增收货地址)) {
			url = Url.URL_ADDRESS;
			String name = nameTxt.getText().toString();
			String detail = detailTxt.getText().toString();
			String phone = phoneTxt.getText().toString();
			paramter.put("act", "newaddress");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("sid", MyApplication.sid);
			paramter.put("realname", name);
			paramter.put("mobile", phone);
			paramter.put("ProvinceID", defaultPID);
			paramter.put("ProvinceName", defaultProvice);
			paramter.put("CityID", cityId);
			paramter.put("CityName", cityName);
			paramter.put("AreaID", areaId);
			paramter.put("AreaName", areaName);
			paramter.put("address", detail);
			paramter.put("uid", MyApplication.uid);
		} 
		else if ( request.equals(NetworkAction.修改收货地址)) {
			url = Url.URL_ADDRESS;
			String name = nameTxt.getText().toString();
			String detail = detailTxt.getText().toString();
			String phone = phoneTxt.getText().toString();
			paramter.put("act", "EditAdd");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("sid", MyApplication.sid);
			paramter.put("realname", name);
			paramter.put("mobile", phone);
			paramter.put("province_id", defaultPID);
			paramter.put("province_name", defaultProvice);
			paramter.put("city_id", cityId);
			paramter.put("city_name", cityName);
			paramter.put("county_id", areaId);
			paramter.put("county_name", areaName);
			paramter.put("address", detail);
			paramter.put("address_id", address_id);
			paramter.put("uid", MyApplication.uid);
		}else if (request.equals(NetworkAction.获取收货地址列表)) {
			url = Url.URL_USERS;
			paramter.put("act", "addrlist");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
		}
		else if ( request.equals(NetworkAction.删除收货地址)) {
			url = Url.URL_ADDRESS;
			paramter.put("act", "DelAddress");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid",MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
			paramter.put("address_id", address_id);
			
		}
		else if ( request.equals(NetworkAction.设置默认地址)) {
			url = Url.URL_ADDRESS;
			paramter.put("act", "SetDefaultAddress");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid",MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
			paramter.put("address_id", address_id);
			
		}
		// 打印生成的链接地址
		Log.i(MyApplication.TAG, MyApplication.getUrl(paramter, url));
		/*
		 * 向服务器发送请求
		 */
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							Log.i(MyApplication.TAG,
									request + "-->" + response.toString());
							int code = response.getInt("code");
							int position = 0;
							if (code == 1)// 返回成功的代码段
							{

								if (request.equals(NetworkAction.省级列表)) {
									
									JSONArray lists = response
											.getJSONArray("list");
									City.proviceData.clear();
									// Log.i(MyApplication.TAG,
									// "lists.length()-->"+lists.length());
									// 把所有的城市和ID对应保存到全局变量中
									ArrayList list = new ArrayList();
									for (int i = 0; i < lists.length(); i++) {
										JSONObject items = lists
												.getJSONObject(i);
										City.proviceData.put(
												items.getString("province_id"),
												items.getString("province_name"));
										list.add(items
												.getString("province_name"));
										if (i == 0 && editFlag == false)// 设置默认城市初始值
										{
											defaultProvice = items
													.getString("province_name");
											defaultPID = items
													.getString("province_id");
										}
										// 如果是编辑某个地址则查找该城市的索引号并赋值
										else if (defaultProvice.equals(items
												.getString("province_name"))) {
											position = i;
										}

									}
									// 获取所有的城市名称填充到spinner中
									// ArrayList list = City.getProviceList();
									provinceAdapter = new ArrayAdapter<String>(
											view.getContext(),
											android.R.layout.simple_spinner_item,
											list);
									provinceAdapter
											.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
									//simple_spinner_dropdown_item
									provinceSp.setAdapter(provinceAdapter);
									//如果是在编辑状态下默认为用户的城市信息
									if (editFlag)
										provinceSp.setSelection(position, true);
									// MyApplication.progressClose();
									// 获取完省级列表再获取市级列表
									// sendData(NetworkAction.市级列表);
								} else if (request.equals(NetworkAction.市级列表)) {
									JSONArray lists = response
											.getJSONArray("list");
									City.cityData.clear();
									// 获取所有的城市名称填充到spinner中
									ArrayList list = new ArrayList<String>();
									// 把所有的城市和ID对应保存到全局变量中
									for (int i = 0; i < lists.length(); i++) {
										JSONObject items = lists
												.getJSONObject(i);
										City.cityData.put(
												items.getString("city_id"),
												items.getString("city_name"));
										list.add(items.getString("city_name"));
										if (editFlag
												&& cityName.equals(items
														.getString("city_name")))
											
											position = i;
									}

									cityAdapter = new ArrayAdapter<String>(
											view.getContext(),
											android.R.layout.simple_spinner_item,
											list);
									cityAdapter
											.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
									citySp.setAdapter(cityAdapter);
									if (editFlag)
										citySp.setSelection(position, true);
									// MyApplication.progressClose();
									// sendData(NetworkAction.区域列表);
								} else if (request.equals(NetworkAction.区域列表)) {
									JSONArray lists = response
											.getJSONArray("list");
									Log.i(MyApplication.TAG,
											"area lists.length()-->"
													+ lists.length());

									ArrayList list = new ArrayList<String>();
									City.areaData.clear();
									for (int i = 0; i < lists.length(); i++) {
										JSONObject items = lists
												.getJSONObject(i);
										City.areaData.put(
												items.getString("county_id"),
												items.getString("county_name"));
										list.add(items.getString("county_name"));
										if (editFlag
												&& areaName.equals(items
														.getString("county_name")))
											position = i;
									}
									// ArrayList list = City.getAreaList();
									areaAdapter = new ArrayAdapter<String>(
											view.getContext(),
											android.R.layout.simple_spinner_item,
											list);
									areaAdapter
											.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
									areaSp.setAdapter(areaAdapter);
									if (editFlag)
										areaSp.setSelection(position, true);
									// MyApplication.progressClose();
								} else if (request.equals(NetworkAction.新增收货地址)) {
									Toast.makeText(
											PersonAddress.this.getActivity(),
											request + "成功", 2000).show();
									addNew.setVisibility(View.GONE);
									addressList.setVisibility(View.VISIBLE);
									sendData(NetworkAction.获取收货地址列表);

								} else if (request
										.equals(NetworkAction.获取收货地址列表)) {
									// MyApplication.progressShow(PersonAddress.this.getActivity(),
									// "加载", request.toString());
									dataAddress.clear();
									JSONArray lists = response
											.getJSONArray("adr_info");
									for (int i = 0; i < lists.length(); i++) {
										JSONObject items = lists
												.getJSONObject(i);
										Address address = new Address();
										address.setAddressID(items
												.getString("address_id"));
										address.setProvince_id(items
												.getString("province_id"));
										address.setProvince_name(items
												.getString("province_name"));
										address.setCity_id(items
												.getString("city_id"));
										address.setCity_name(items
												.getString("city_name"));
										address.setCounty_id(items
												.getString("county_id"));
										address.setCounty_name(items
												.getString("county_name"));
										address.setRealname(items
												.getString("realname"));
										address.setMobile(items
												.getString("mobile"));
										address.setAddress(items
												.getString("address"));
										address.setTag(items
												.getString("tag"));
										dataAddress.add(address);
//										if(Address.defaultAddress!=null)
									}
									// 更新收货地址列表
									adapter.notifyDataSetChanged();
								} else if (request.equals(NetworkAction.修改收货地址)) {
									
										
									Toast.makeText(
											PersonAddress.this.getActivity(),
											request + "成功", 2000).show();
									addNew.setVisibility(View.GONE);
									addressList.setVisibility(View.VISIBLE);
									// 设置当前为非编辑地址模式
									editFlag = false;
									//离开之前先清空一下收货地址信息页面
									clearAddressEditbox();
									sendData(NetworkAction.获取收货地址列表);
								}
								else if ( request.equals(NetworkAction.删除收货地址)) {
									Toast.makeText(
											PersonAddress.this.getActivity(),
											request + "成功", 2000).show();
									sendData(NetworkAction.获取收货地址列表);
								}
								else if ( request.equals(NetworkAction.设置默认地址)) {
									Toast.makeText(
											PersonAddress.this.getActivity(),
											request + "成功", 2000).show();
									sendData(NetworkAction.获取收货地址列表);
//									// 更新收货地址列表
//									adapter.notifyDataSetChanged();
								}
							} else {
								Toast.makeText(getActivity(),
										ErrorMsg.getErrorMsg(request, code),
										2000).show(); // 根据申请的类型判断相应的错误输出
							}

						} catch (JSONException e) {
							Log.i(MyApplication.TAG,
									request + "-->" + e.getMessage());
							MyApplication.progressClose();
						}

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i(MyApplication.TAG,
								request + "-->" + error.getMessage());
					}
				});
	}

	/**
	 * 在listview底部添加新增按钮
	 */
	private void addFooterView() {
		Button buttonFooter = new Button(view.getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 10, 0, 0);
		buttonFooter.setText("+ 新增收货地址");
		buttonFooter.setBackgroundDrawable(MyApplication.resources
				.getDrawable(R.drawable.button_add_address));
		buttonFooter.setTextColor(MyApplication.resources
				.getColor(R.color.white));
		buttonFooter.setTextSize(18);
//		buttonFooter.setLayoutParams(layoutParams);
		buttonFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击新增收货地址按钮，隐藏listview，打开新增的layout
				// view.findViewById(R.id.person_addressList).setVisibility(View.VISIBLE);//
				// 地址列表listview
				// view.findViewById(R.id.person_address_add_layout).setVisibility(View.GONE);//新增收货地址视图

				getCityList();// 获取城市列表
				addNew.setVisibility(View.VISIBLE);
				addressList.setVisibility(View.GONE);
			}
		});
		// View footerView = LayoutInflater.from(view.getContext()).inflate(
		// R.layout.list_page_load, null);
		addressList.addFooterView(buttonFooter);
	}

	/**
	 * 获取城市列表
	 */
	private void getCityList() {
		sendData(NetworkAction.省级列表);
		provinceSp
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// 根据点击的位置获取城市名称
						defaultProvice = (String) parent
								.getItemAtPosition(position);
						defaultPID = City.getIdFromList("province",
								defaultProvice);
						sendData(NetworkAction.市级列表);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		citySp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// 根据点击的位置获取城市名称
				cityName = (String) parent.getItemAtPosition(position);
				cityId = City.getIdFromList("city", cityName);
				Log.i(MyApplication.TAG, "cityName-->" + cityName);
				Log.i(MyApplication.TAG, "cityId-->" + cityId);
				sendData(NetworkAction.区域列表);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		areaSp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// 根据点击的位置获取城市名称
				areaName = (String) parent.getItemAtPosition(position);
				areaId = City.getIdFromList("area", areaName);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	/**
	 * 删除收货地址
	 * @param addressID
	 */
	public void deleteAddress(String addressID)
	{
		address_id=addressID;
		sendData(NetworkAction.删除收货地址);
	}
	
	/**
	 * 设置默认地址
	 * @param addressID
	 */
	public void defaultAddress(Address address)
	{
		if(!address.getTag().equals("1"))
		{
			address_id=address.getAddressID();
			sendData(NetworkAction.设置默认地址);
		}

	}
	
	public void clearAddressEditbox()
	{
		nameTxt.setText("");
		detailTxt.setText("");
		phoneTxt.setText("");
	}
	
	/**
	 * 编辑收货地址
	 * 
	 * @param address
	 *            单条的地址信息
	 */
	public void editAddress(Address address) {
		// 关闭收货列表，打开收货详情
		addNew.setVisibility(View.VISIBLE);
		addressList.setVisibility(View.GONE);

		// 填入相关的数据
		nameTxt.setText(address.getRealname());
		detailTxt.setText(address.getAddress());
		phoneTxt.setText(address.getMobile());
		defaultPID = address.getProvince_id();
		defaultProvice = address.getProvince_name();
		cityId = address.getCity_id();
		cityName = address.getCity_name();
		areaId = address.getCounty_id();
		areaName = address.getCounty_name();
		address_id=address.getAddressID();
		Log.i(MyApplication.TAG, "address_id-->"+address_id);
		// 设置当前为编辑收货地址模式
		editFlag = true;
		// 获取城市列表并设置该收货地址的城市为默认值
		getCityList();
	}
}
