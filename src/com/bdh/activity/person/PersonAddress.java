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

	private ListView addressList;// ��ַ�б��listView
	private LinearLayout addNew;// �����ջ���ַ��ͼ
	private View view = null;// �ջ���ַ��ͼ
	private Spinner provinceSp; // ʡ���б�
	private Spinner citySp;// �����б�
	private Spinner areaSp;// �����б�
	private EditText nameTxt; // �ջ�������
	private EditText detailTxt; // �ֵ���ַ����
	private EditText phoneTxt; // �ֻ�����
	// private EditText emailTxt; // ����
	private Button saveBtn;// �����ջ���ť
	private String defaultProvice;// Ĭ��ʡ������
	private String defaultPID;// Ĭ��ʡ������ID
	private MyAdapter adapter;
	ArrayList<Object> dataAddress; // �ջ���ַ����
	ArrayAdapter<String> provinceAdapter; // ʡ���б�������
	ArrayAdapter<String> cityAdapter;// �м��б�������
	ArrayAdapter<String> areaAdapter;// �����б�������
	private String cityName;// ������м����ƣ�������ȡ�����б�
	private String cityId;// ������м�ID��������ȡ�����б�
	private String areaName;// �������������
	private String areaId;// ���������ID
	private boolean editFlag = false;// �ж���������ַ���Ǳ༭��ַ��ʾ
	private String address_id;//������Ҫ�޸ĵĵ�ַ��ID

	
	// ������ͼ
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
		nameTxt = (EditText) view.findViewById(R.id.person_address_name);// �ջ�������
		detailTxt = (EditText) view.findViewById(R.id.person_address_detail); // �ֵ���ַ����
		phoneTxt = (EditText) view.findViewById(R.id.person_address_phone); // �ֻ�����
		// emailTxt = (EditText) view.findViewById(R.id.person_address_email);
		// // ����
		addressList = (ListView) view.findViewById(R.id.person_addressList);// ��ַ�б�listview
		addNew = (LinearLayout) view
				.findViewById(R.id.person_address_add_layout);// �����ջ���ַ��ͼ

		saveBtn = (Button) view.findViewById(R.id.person_address_savebtn);// �����ջ���ť
		saveBtn.setOnClickListener(this);
		dataAddress = new ArrayList<Object>();
		// ������
		adapter = new MyAdapter(this, NetworkAction.��ȡ�ջ���ַ�б�, dataAddress);
		// MyApplication.getInstance()
		// .getClassName(this.getClass()), data);
		// ��ʼ�������б�ؼ�
		provinceSp = (Spinner) view.findViewById(R.id.person_address_provice);
		citySp = (Spinner) view.findViewById(R.id.person_address_city);
		areaSp = (Spinner) view.findViewById(R.id.person_address_area);
	}

	private void initData() {

		sendData(NetworkAction.��ȡ�ջ���ַ�б�);
		// getAddressList();
		addFooterView(); // ����ӵײ�������ť�����������
		addressList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_address_savebtn:// ���������ջ���ַ
			String name = nameTxt.getText().toString();
			String detail = detailTxt.getText().toString();
			String phone = phoneTxt.getText().toString();
			if (name.equals("") || detail.equals("") || phone.equals("")) {
				Toast.makeText(getActivity(), "������������Ϣ", 2000).show();
				return;
			}
			if(!isMobileNO(phone))
			{
				Toast.makeText(getActivity(), "��������ȷ���ֻ�����", 2000).show();
				return;
			}
			if (editFlag)
				sendData(NetworkAction.�޸��ջ���ַ);
			else
				sendData(NetworkAction.�����ջ���ַ);
			// Log.i(MyApplication.TAG," lists-->"+"defaultProvice"+defaultProvice+"  defaultPID"+defaultPID
			// +"  cityName"+cityName+"  cityId"+cityId+"  areaName"+areaName+"  areaId"+areaId);
			break;

		default:
			break;
		}

	}

	
	//��֤�绰����
		public static boolean isMobileNO(String mobiles){     
	        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
	        Matcher m = p.matcher(mobiles);     
	        return m.matches();     
	    } 
	/**
	 * ���������������
	 */
	private void sendData(final NetworkAction request) {
		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();

		if (request.equals(NetworkAction.ʡ���б�)) {
			MyApplication.progressShow(
					PersonAddress.this.getActivity(),
					 request.toString());
			url = Url.URL_API;
			paramter.put("tpage", "address");
			paramter.put("act", "province");
		} else if (request.equals(NetworkAction.�м��б�)) {
			url = Url.URL_API;
			paramter.put("tpage", "address");
			paramter.put("act", "city");
			paramter.put("province_id", defaultPID);
		} else if (request.equals(NetworkAction.�����б�)) {
			url = Url.URL_API;
			paramter.put("tpage", "address");
			paramter.put("act", "county");
			paramter.put("city_id", cityId);
		} else if (request.equals(NetworkAction.�����ջ���ַ)) {
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
		else if ( request.equals(NetworkAction.�޸��ջ���ַ)) {
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
		}else if (request.equals(NetworkAction.��ȡ�ջ���ַ�б�)) {
			url = Url.URL_USERS;
			paramter.put("act", "addrlist");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid", MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
		}
		else if ( request.equals(NetworkAction.ɾ���ջ���ַ)) {
			url = Url.URL_ADDRESS;
			paramter.put("act", "DelAddress");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid",MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
			paramter.put("address_id", address_id);
			
		}
		else if ( request.equals(NetworkAction.����Ĭ�ϵ�ַ)) {
			url = Url.URL_ADDRESS;
			paramter.put("act", "SetDefaultAddress");
			paramter.put("sessionid", MyApplication.seskey);
			paramter.put("uid",MyApplication.uid);
			paramter.put("sid", MyApplication.sid);
			paramter.put("address_id", address_id);
			
		}
		// ��ӡ���ɵ����ӵ�ַ
		Log.i(MyApplication.TAG, MyApplication.getUrl(paramter, url));
		/*
		 * ���������������
		 */
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							Log.i(MyApplication.TAG,
									request + "-->" + response.toString());
							int code = response.getInt("code");
							int position = 0;
							if (code == 1)// ���سɹ��Ĵ����
							{

								if (request.equals(NetworkAction.ʡ���б�)) {
									
									JSONArray lists = response
											.getJSONArray("list");
									City.proviceData.clear();
									// Log.i(MyApplication.TAG,
									// "lists.length()-->"+lists.length());
									// �����еĳ��к�ID��Ӧ���浽ȫ�ֱ�����
									ArrayList list = new ArrayList();
									for (int i = 0; i < lists.length(); i++) {
										JSONObject items = lists
												.getJSONObject(i);
										City.proviceData.put(
												items.getString("province_id"),
												items.getString("province_name"));
										list.add(items
												.getString("province_name"));
										if (i == 0 && editFlag == false)// ����Ĭ�ϳ��г�ʼֵ
										{
											defaultProvice = items
													.getString("province_name");
											defaultPID = items
													.getString("province_id");
										}
										// ����Ǳ༭ĳ����ַ����Ҹó��е������Ų���ֵ
										else if (defaultProvice.equals(items
												.getString("province_name"))) {
											position = i;
										}

									}
									// ��ȡ���еĳ���������䵽spinner��
									// ArrayList list = City.getProviceList();
									provinceAdapter = new ArrayAdapter<String>(
											view.getContext(),
											android.R.layout.simple_spinner_item,
											list);
									provinceAdapter
											.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
									//simple_spinner_dropdown_item
									provinceSp.setAdapter(provinceAdapter);
									//������ڱ༭״̬��Ĭ��Ϊ�û��ĳ�����Ϣ
									if (editFlag)
										provinceSp.setSelection(position, true);
									// MyApplication.progressClose();
									// ��ȡ��ʡ���б��ٻ�ȡ�м��б�
									// sendData(NetworkAction.�м��б�);
								} else if (request.equals(NetworkAction.�м��б�)) {
									JSONArray lists = response
											.getJSONArray("list");
									City.cityData.clear();
									// ��ȡ���еĳ���������䵽spinner��
									ArrayList list = new ArrayList<String>();
									// �����еĳ��к�ID��Ӧ���浽ȫ�ֱ�����
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
									// sendData(NetworkAction.�����б�);
								} else if (request.equals(NetworkAction.�����б�)) {
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
								} else if (request.equals(NetworkAction.�����ջ���ַ)) {
									Toast.makeText(
											PersonAddress.this.getActivity(),
											request + "�ɹ�", 2000).show();
									addNew.setVisibility(View.GONE);
									addressList.setVisibility(View.VISIBLE);
									sendData(NetworkAction.��ȡ�ջ���ַ�б�);

								} else if (request
										.equals(NetworkAction.��ȡ�ջ���ַ�б�)) {
									// MyApplication.progressShow(PersonAddress.this.getActivity(),
									// "����", request.toString());
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
									// �����ջ���ַ�б�
									adapter.notifyDataSetChanged();
								} else if (request.equals(NetworkAction.�޸��ջ���ַ)) {
									
										
									Toast.makeText(
											PersonAddress.this.getActivity(),
											request + "�ɹ�", 2000).show();
									addNew.setVisibility(View.GONE);
									addressList.setVisibility(View.VISIBLE);
									// ���õ�ǰΪ�Ǳ༭��ַģʽ
									editFlag = false;
									//�뿪֮ǰ�����һ���ջ���ַ��Ϣҳ��
									clearAddressEditbox();
									sendData(NetworkAction.��ȡ�ջ���ַ�б�);
								}
								else if ( request.equals(NetworkAction.ɾ���ջ���ַ)) {
									Toast.makeText(
											PersonAddress.this.getActivity(),
											request + "�ɹ�", 2000).show();
									sendData(NetworkAction.��ȡ�ջ���ַ�б�);
								}
								else if ( request.equals(NetworkAction.����Ĭ�ϵ�ַ)) {
									Toast.makeText(
											PersonAddress.this.getActivity(),
											request + "�ɹ�", 2000).show();
									sendData(NetworkAction.��ȡ�ջ���ַ�б�);
//									// �����ջ���ַ�б�
//									adapter.notifyDataSetChanged();
								}
							} else {
								Toast.makeText(getActivity(),
										ErrorMsg.getErrorMsg(request, code),
										2000).show(); // ��������������ж���Ӧ�Ĵ������
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
	 * ��listview�ײ����������ť
	 */
	private void addFooterView() {
		Button buttonFooter = new Button(view.getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(0, 10, 0, 0);
		buttonFooter.setText("+ �����ջ���ַ");
		buttonFooter.setBackgroundDrawable(MyApplication.resources
				.getDrawable(R.drawable.button_add_address));
		buttonFooter.setTextColor(MyApplication.resources
				.getColor(R.color.white));
		buttonFooter.setTextSize(18);
//		buttonFooter.setLayoutParams(layoutParams);
		buttonFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// ��������ջ���ַ��ť������listview����������layout
				// view.findViewById(R.id.person_addressList).setVisibility(View.VISIBLE);//
				// ��ַ�б�listview
				// view.findViewById(R.id.person_address_add_layout).setVisibility(View.GONE);//�����ջ���ַ��ͼ

				getCityList();// ��ȡ�����б�
				addNew.setVisibility(View.VISIBLE);
				addressList.setVisibility(View.GONE);
			}
		});
		// View footerView = LayoutInflater.from(view.getContext()).inflate(
		// R.layout.list_page_load, null);
		addressList.addFooterView(buttonFooter);
	}

	/**
	 * ��ȡ�����б�
	 */
	private void getCityList() {
		sendData(NetworkAction.ʡ���б�);
		provinceSp
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// ���ݵ����λ�û�ȡ��������
						defaultProvice = (String) parent
								.getItemAtPosition(position);
						defaultPID = City.getIdFromList("province",
								defaultProvice);
						sendData(NetworkAction.�м��б�);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		citySp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// ���ݵ����λ�û�ȡ��������
				cityName = (String) parent.getItemAtPosition(position);
				cityId = City.getIdFromList("city", cityName);
				Log.i(MyApplication.TAG, "cityName-->" + cityName);
				Log.i(MyApplication.TAG, "cityId-->" + cityId);
				sendData(NetworkAction.�����б�);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		areaSp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// ���ݵ����λ�û�ȡ��������
				areaName = (String) parent.getItemAtPosition(position);
				areaId = City.getIdFromList("area", areaName);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	/**
	 * ɾ���ջ���ַ
	 * @param addressID
	 */
	public void deleteAddress(String addressID)
	{
		address_id=addressID;
		sendData(NetworkAction.ɾ���ջ���ַ);
	}
	
	/**
	 * ����Ĭ�ϵ�ַ
	 * @param addressID
	 */
	public void defaultAddress(Address address)
	{
		if(!address.getTag().equals("1"))
		{
			address_id=address.getAddressID();
			sendData(NetworkAction.����Ĭ�ϵ�ַ);
		}

	}
	
	public void clearAddressEditbox()
	{
		nameTxt.setText("");
		detailTxt.setText("");
		phoneTxt.setText("");
	}
	
	/**
	 * �༭�ջ���ַ
	 * 
	 * @param address
	 *            �����ĵ�ַ��Ϣ
	 */
	public void editAddress(Address address) {
		// �ر��ջ��б����ջ�����
		addNew.setVisibility(View.VISIBLE);
		addressList.setVisibility(View.GONE);

		// ������ص�����
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
		// ���õ�ǰΪ�༭�ջ���ַģʽ
		editFlag = true;
		// ��ȡ�����б����ø��ջ���ַ�ĳ���ΪĬ��ֵ
		getCityList();
	}
}
