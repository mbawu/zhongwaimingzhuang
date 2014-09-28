package com.zwmz.activity.person;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.zwmz.activity.product.Home;
import com.zwmz.base.MyAdapter;
import com.zwmz.base.MyApplication;
import com.zwmz.base.Url;
import com.zwmz.model.City;
import com.zwmz.model.ErrorMsg;
import com.zwmz.model.NetworkAction;
import com.zwmz.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//会员信息页面
public class PersonInfo extends Fragment implements OnClickListener {

	private View view;
	private LinearLayout changeInfoLy;// 修改信息的内容
	private LinearLayout changePwdLy;// 修改密码的内容
	private TextView changePwd;// 修改密码
	private TextView saveInfo;// 修改密码
	private TextView confirmChange;// 确认修改密码
	private EditText nameTxt;// 用户昵称
	private EditText addressTxt;// 地址
	private EditText emailTxt;// 邮箱
//	private EditText phoneTxt;// 电话
	private EditText oldPwdTxt;// 原密码
	private EditText newPwdTxt;// 新密码
	private EditText reNewPwdTxt;// 确认密码
	private TextView changeBrithdayBtn;// 修改生日的按钮
	private DatePicker dataPicker;// 日期选择器
	private TextView showBrithdayTxt;// 显示生日日期
	private String sex = "0";//
	private RadioGroup sexGroup;
	private RadioButton man;
	private RadioButton women;
	private String defaultProvice;// 默认省级城市
	private String defaultPID;// 默认省级城市ID
	private String cityName;// 保存的市级名称，用来获取区域列表
	private String cityId;// 保存的市级ID，用来获取区域列表
	private String areaName;// 保存的区域名称
	private String areaId;// 保存的区域ID
	private Spinner provinceSp; // 省份列表
	private Spinner citySp;// 城市列表
	private Spinner areaSp;// 区域列表
	ArrayList<Object> dataAddress; // 收货地址集合
	ArrayAdapter<String> provinceAdapter; // 省级列表适配器
	ArrayAdapter<String> cityAdapter;// 市级列表适配器
	ArrayAdapter<String> areaAdapter;// 区域列表适配器
	private MyAdapter adapter;
	private boolean editFlag = false;// 判断是新增地址还是编辑地址标示

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null)
			view = inflater.inflate(R.layout.person_info, container, false);
		Log.i(MyApplication.TAG, "onCreateView");
		initView();
		initData();
		return view;
	}

	private void initView() {
		man= (RadioButton) view.findViewById(R.id.person_info_sexman);
		women= (RadioButton) view.findViewById(R.id.person_info_sexwomen);
		sexGroup = (RadioGroup) view.findViewById(R.id.person_info_sexmod);
		sexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.person_info_sexman:
					sex = "0";
					
					break;
				case R.id.person_info_sexwomen:
					sex = "1";
					break;
				}

			}
		});
		dataPicker = (DatePicker) view
				.findViewById(R.id.person_info_brithdaymod);// 日期选择器
		showBrithdayTxt = (TextView) view
				.findViewById(R.id.person_info_showbirthday);// 显示生日日期的文本框
		oldPwdTxt = (EditText) view.findViewById(R.id.person_info_oldpwd);// 原密码
		newPwdTxt = (EditText) view.findViewById(R.id.person_info_newpwd);// 新密码
		reNewPwdTxt = (EditText) view.findViewById(R.id.person_info_renewpwd);// 确认密码
		nameTxt = (EditText) view.findViewById(R.id.person_info_namemod);
		emailTxt = (EditText) view.findViewById(R.id.person_info_emailmod);
//		phoneTxt = (EditText) view.findViewById(R.id.person_info_phonemod);
		changePwdLy = (LinearLayout) view
				.findViewById(R.id.person_info_changepwd_layout);
		changeInfoLy = (LinearLayout) view
				.findViewById(R.id.person_info_changeinfo_layout);
		changePwd = (TextView) view.findViewById(R.id.person_info_changepwd_btn);
		changePwd.setOnClickListener(this);
		saveInfo = (TextView) view.findViewById(R.id.person_info_saveinfo);
		saveInfo.setOnClickListener(this);
		confirmChange = (TextView) view
				.findViewById(R.id.person_info_confirmchange);
		confirmChange.setOnClickListener(this);
		changeBrithdayBtn = (TextView) view
				.findViewById(R.id.person_info_changebirthday);
		changeBrithdayBtn.setOnClickListener(this);
		// 地址相关
		dataAddress = new ArrayList<Object>();

		// 适配器
		adapter = new MyAdapter(this, NetworkAction.获取收货地址列表, dataAddress);
		// MyApplication.getInstance()
		// .getClassName(this.getClass()), data);
		// 初始化地区列表控件
		provinceSp = (Spinner) view.findViewById(R.id.person_info_provice);
		citySp = (Spinner) view.findViewById(R.id.person_info_city);
		areaSp = (Spinner) view.findViewById(R.id.person_info_area);
		addressTxt = (EditText) view.findViewById(R.id.person_info_detail);
	}

	private void initData() {
		// 取出个人信息
		nameTxt.setText(MyApplication.sp.getString("nickname", ""));
		defaultProvice = MyApplication.sp.getString("ProvinceName", "");// 默认省级城市
		defaultPID = MyApplication.sp.getString("ProvinceID", "");// 默认省级城市ID
		cityName = MyApplication.sp.getString("CityName", "");// 保存的市级名称，用来获取区域列表
		cityId = MyApplication.sp.getString("CityID", "");// 保存的市级ID，用来获取区域列表
		areaName = MyApplication.sp.getString("AreaName", "");// 保存的区域名称
		areaId = MyApplication.sp.getString("AreaID", "");// 保存的区域ID
		String detailAddress = MyApplication.sp.getString("address", "");// 详细地址
		addressTxt.setText(detailAddress);
		emailTxt.setText(MyApplication.sp.getString("email", ""));
//		phoneTxt.setText(MyApplication.sp.getString("phone", ""));
		showBrithdayTxt.setText(MyApplication.sp.getString("birthday", ""));
		sex= MyApplication.sp.getString("sex", "");
		if(sex.equals("0"))
			man.setChecked(true);
		else
			women.setChecked(true);
		if (!defaultProvice.equals(""))
			editFlag=true;
			getCityList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_info_changepwd_btn:// 显示修改密码界面
			changeInfoLy.setVisibility(View.GONE);
			changePwdLy.setVisibility(View.VISIBLE);
			break;
		case R.id.person_info_confirmchange:// 保存修改的密码并打开会员信息页面，关闭修改密码页面
			String oldPwd = oldPwdTxt.getText().toString();
			String newPwd = newPwdTxt.getText().toString();
			String reNewPwd = reNewPwdTxt.getText().toString();
			if (oldPwd.equals("") || newPwd.equals("") || reNewPwd.equals("")) {
				Toast.makeText(getActivity(), "请填入完整信息", 2000).show();
				return;
			} else if (!newPwd.equals(reNewPwd)) {
				Toast.makeText(getActivity(), "两次输入的密码不一致", 2000).show();
				return;
			}
			sendData(NetworkAction.修改密码);
			break;
		case R.id.person_info_saveinfo:// 保存修改的信息
			sendData(NetworkAction.保存个人信息);
			break;

		case R.id.person_info_changebirthday:// 修改生日按钮
			String text = changeBrithdayBtn.getText().toString();
			if (text.equals("修改日期")) {
				changeBrithdayBtn.setText("保存");
				dataPicker.setVisibility(View.VISIBLE);
			} else if (text.equals("保存")) {
				// 显示修改以后的日期
				String newData = dataPicker.getYear() + "-"
						+ (dataPicker.getMonth() + 1) + "-"
						+ dataPicker.getDayOfMonth();
				showBrithdayTxt.setText(newData);
				changeBrithdayBtn.setText("修改日期");
				dataPicker.setVisibility(View.GONE);
				MyApplication.ed.putString("birthday", newData);
				MyApplication.ed.commit();
			}
			break;
		default:
			break;
		}

	}

	// 保存修改的信息
	private void saveInfo() {
		MyApplication.ed.putString("nickname", nameTxt.getText().toString());
		MyApplication.ed.putString("sex", sex);
		MyApplication.ed.putString("ProvinceName", defaultProvice);
		MyApplication.ed.putString("ProvinceID", defaultPID);
		MyApplication.ed.putString("CityName", cityName);
		MyApplication.ed.putString("CityID", cityId);
		MyApplication.ed.putString("AreaName", areaName);
		MyApplication.ed.putString("AreaID", areaId);
		MyApplication.ed.putString("birthday", showBrithdayTxt.getText()
				.toString());
		MyApplication.ed.putString("address", addressTxt.getText().toString());
		MyApplication.ed.putString("email", emailTxt.getText().toString());
//		MyApplication.ed.putString("phone", phoneTxt.getText().toString());
		MyApplication.ed.putString("AreaID", areaId);
		MyApplication.ed.commit();
	}

	private void sendData(final NetworkAction request) {
		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();

		// 获取修改密码操作
		if (request.equals(NetworkAction.修改密码)) {
			String oldPwd = oldPwdTxt.getText().toString();
			String newPwd = newPwdTxt.getText().toString();
			String reNewPwd = reNewPwdTxt.getText().toString();
			url = Url.URL_MEMBER;
			paramter.put("act", "ChgPwd");
			paramter.put("oldpassword", oldPwd);
			paramter.put("newpassword1", newPwd);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("sid", MyApplication.sid);
		} else if (request.equals(NetworkAction.省级列表)) {
			MyApplication.progressShow(PersonInfo.this.getActivity(),
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
		} else if (request.equals(NetworkAction.保存个人信息)) {
			url = Url.URL_MEMBER;
			paramter.put("act", "MemberInfo");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
			paramter.put("nickname", nameTxt.getText().toString());
			paramter.put("email", emailTxt.getText().toString());
			paramter.put("sex",sex);
			String birthday = showBrithdayTxt.getText().toString();
			if (!birthday.equals("null")) {
				String year = birthday.substring(0, 4);
				int monthChar = birthday.indexOf("-");
				int dayChar = birthday.lastIndexOf("-");
				String month = birthday.substring(monthChar + 1, dayChar);
				String day = birthday.substring(dayChar + 1);
				paramter.put("YYYY", year);
				paramter.put("MM", month);
				paramter.put("DD", day);
			}else
			{
				paramter.put("YYYY", "");
				paramter.put("MM", "");
				paramter.put("DD", "");
			}
			paramter.put("ProvinceID", defaultPID);
			paramter.put("CityID", cityId);
			paramter.put("AreaID", areaId);
			paramter.put("ProvinceName", defaultProvice);
			paramter.put("CityName", cityName);
			paramter.put("AreaName", areaName);
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
							MyApplication.printLog("changePwd", "result:"
									+ response.toString());
							// int code = response.getInt("code");
							int code = response.getInt("code");
							int position = 0;
							if (code == 1) {
								if (request.equals(NetworkAction.修改密码))// 获取修改密码操作
								{
									MyApplication.ed.putString("password",
											newPwdTxt.getText().toString());
									Toast.makeText(getActivity(), "修改密码成功",
											2000).show();
									changeInfoLy.setVisibility(View.VISIBLE);
									changePwdLy.setVisibility(View.GONE);
									MyApplication.ed.commit();
								} else if (request.equals(NetworkAction.省级列表)) {

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
									// simple_spinner_dropdown_item
									provinceSp.setAdapter(provinceAdapter);
									// 如果是在编辑状态下默认为用户的城市信息
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
								} else if (request.equals(NetworkAction.保存个人信息)) {
									Toast.makeText(getActivity(), "保存个人信息成功",
											2000).show();
									saveInfo();
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
						MyApplication.progressClose();
					}
				});
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
}
