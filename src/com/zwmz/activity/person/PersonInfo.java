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

//��Ա��Ϣҳ��
public class PersonInfo extends Fragment implements OnClickListener {

	private View view;
	private LinearLayout changeInfoLy;// �޸���Ϣ������
	private LinearLayout changePwdLy;// �޸����������
	private TextView changePwd;// �޸�����
	private TextView saveInfo;// �޸�����
	private TextView confirmChange;// ȷ���޸�����
	private EditText nameTxt;// �û��ǳ�
	private EditText addressTxt;// ��ַ
	private EditText emailTxt;// ����
//	private EditText phoneTxt;// �绰
	private EditText oldPwdTxt;// ԭ����
	private EditText newPwdTxt;// ������
	private EditText reNewPwdTxt;// ȷ������
	private TextView changeBrithdayBtn;// �޸����յİ�ť
	private DatePicker dataPicker;// ����ѡ����
	private TextView showBrithdayTxt;// ��ʾ��������
	private String sex = "0";//
	private RadioGroup sexGroup;
	private RadioButton man;
	private RadioButton women;
	private String defaultProvice;// Ĭ��ʡ������
	private String defaultPID;// Ĭ��ʡ������ID
	private String cityName;// ������м����ƣ�������ȡ�����б�
	private String cityId;// ������м�ID��������ȡ�����б�
	private String areaName;// �������������
	private String areaId;// ���������ID
	private Spinner provinceSp; // ʡ���б�
	private Spinner citySp;// �����б�
	private Spinner areaSp;// �����б�
	ArrayList<Object> dataAddress; // �ջ���ַ����
	ArrayAdapter<String> provinceAdapter; // ʡ���б�������
	ArrayAdapter<String> cityAdapter;// �м��б�������
	ArrayAdapter<String> areaAdapter;// �����б�������
	private MyAdapter adapter;
	private boolean editFlag = false;// �ж���������ַ���Ǳ༭��ַ��ʾ

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
				.findViewById(R.id.person_info_brithdaymod);// ����ѡ����
		showBrithdayTxt = (TextView) view
				.findViewById(R.id.person_info_showbirthday);// ��ʾ�������ڵ��ı���
		oldPwdTxt = (EditText) view.findViewById(R.id.person_info_oldpwd);// ԭ����
		newPwdTxt = (EditText) view.findViewById(R.id.person_info_newpwd);// ������
		reNewPwdTxt = (EditText) view.findViewById(R.id.person_info_renewpwd);// ȷ������
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
		// ��ַ���
		dataAddress = new ArrayList<Object>();

		// ������
		adapter = new MyAdapter(this, NetworkAction.��ȡ�ջ���ַ�б�, dataAddress);
		// MyApplication.getInstance()
		// .getClassName(this.getClass()), data);
		// ��ʼ�������б�ؼ�
		provinceSp = (Spinner) view.findViewById(R.id.person_info_provice);
		citySp = (Spinner) view.findViewById(R.id.person_info_city);
		areaSp = (Spinner) view.findViewById(R.id.person_info_area);
		addressTxt = (EditText) view.findViewById(R.id.person_info_detail);
	}

	private void initData() {
		// ȡ��������Ϣ
		nameTxt.setText(MyApplication.sp.getString("nickname", ""));
		defaultProvice = MyApplication.sp.getString("ProvinceName", "");// Ĭ��ʡ������
		defaultPID = MyApplication.sp.getString("ProvinceID", "");// Ĭ��ʡ������ID
		cityName = MyApplication.sp.getString("CityName", "");// ������м����ƣ�������ȡ�����б�
		cityId = MyApplication.sp.getString("CityID", "");// ������м�ID��������ȡ�����б�
		areaName = MyApplication.sp.getString("AreaName", "");// �������������
		areaId = MyApplication.sp.getString("AreaID", "");// ���������ID
		String detailAddress = MyApplication.sp.getString("address", "");// ��ϸ��ַ
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
		case R.id.person_info_changepwd_btn:// ��ʾ�޸��������
			changeInfoLy.setVisibility(View.GONE);
			changePwdLy.setVisibility(View.VISIBLE);
			break;
		case R.id.person_info_confirmchange:// �����޸ĵ����벢�򿪻�Ա��Ϣҳ�棬�ر��޸�����ҳ��
			String oldPwd = oldPwdTxt.getText().toString();
			String newPwd = newPwdTxt.getText().toString();
			String reNewPwd = reNewPwdTxt.getText().toString();
			if (oldPwd.equals("") || newPwd.equals("") || reNewPwd.equals("")) {
				Toast.makeText(getActivity(), "������������Ϣ", 2000).show();
				return;
			} else if (!newPwd.equals(reNewPwd)) {
				Toast.makeText(getActivity(), "������������벻һ��", 2000).show();
				return;
			}
			sendData(NetworkAction.�޸�����);
			break;
		case R.id.person_info_saveinfo:// �����޸ĵ���Ϣ
			sendData(NetworkAction.���������Ϣ);
			break;

		case R.id.person_info_changebirthday:// �޸����հ�ť
			String text = changeBrithdayBtn.getText().toString();
			if (text.equals("�޸�����")) {
				changeBrithdayBtn.setText("����");
				dataPicker.setVisibility(View.VISIBLE);
			} else if (text.equals("����")) {
				// ��ʾ�޸��Ժ������
				String newData = dataPicker.getYear() + "-"
						+ (dataPicker.getMonth() + 1) + "-"
						+ dataPicker.getDayOfMonth();
				showBrithdayTxt.setText(newData);
				changeBrithdayBtn.setText("�޸�����");
				dataPicker.setVisibility(View.GONE);
				MyApplication.ed.putString("birthday", newData);
				MyApplication.ed.commit();
			}
			break;
		default:
			break;
		}

	}

	// �����޸ĵ���Ϣ
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

		// ��ȡ�޸��������
		if (request.equals(NetworkAction.�޸�����)) {
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
		} else if (request.equals(NetworkAction.ʡ���б�)) {
			MyApplication.progressShow(PersonInfo.this.getActivity(),
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
		} else if (request.equals(NetworkAction.���������Ϣ)) {
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
		// ��ӡ���ɵ����ӵ�ַ
		Log.i(MyApplication.TAG, MyApplication.getUrl(paramter, url));
		/*
		 * ���������������
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
								if (request.equals(NetworkAction.�޸�����))// ��ȡ�޸��������
								{
									MyApplication.ed.putString("password",
											newPwdTxt.getText().toString());
									Toast.makeText(getActivity(), "�޸�����ɹ�",
											2000).show();
									changeInfoLy.setVisibility(View.VISIBLE);
									changePwdLy.setVisibility(View.GONE);
									MyApplication.ed.commit();
								} else if (request.equals(NetworkAction.ʡ���б�)) {

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
									// simple_spinner_dropdown_item
									provinceSp.setAdapter(provinceAdapter);
									// ������ڱ༭״̬��Ĭ��Ϊ�û��ĳ�����Ϣ
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
								} else if (request.equals(NetworkAction.���������Ϣ)) {
									Toast.makeText(getActivity(), "���������Ϣ�ɹ�",
											2000).show();
									saveInfo();
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
						MyApplication.progressClose();
					}
				});
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
}
