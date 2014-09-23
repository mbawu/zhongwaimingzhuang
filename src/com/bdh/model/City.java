package com.bdh.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.bdh.activity.person.PersonAddress;
import com.bdh.base.MyApplication;
import com.bdh.base.Url;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

/**
 * �����б������
 * 
 * @author Administrator
 * 
 */
public class City {
	public static HashMap<String, String> proviceData = new HashMap<String, String>();// ʡ���б�
	public static HashMap<String, String> cityData = new HashMap<String, String>();// �м��б�
	public static HashMap<String, String> areaData = new HashMap<String, String>();// �����б�
	
	
	/**
	 * ��ȡʡ���б�
	 * 
	 * @return
	 */
	public static ArrayList getProviceList() {
		
		
		ArrayList list = new ArrayList<String>();
		
		Object[] values=proviceData.values().toArray();
		for (int i = 0; i < values.length; i++) {
			Log.i(MyApplication.TAG, "i-->"+values[i].toString());
			list.add(values[i].toString());
		}
//		Log.i(MyApplication.TAG, "proviceData-->" + proviceData.size());
//		ArrayList list = new ArrayList<String>();
//		Iterator iterator = proviceData.keySet().iterator();
//		while (iterator.hasNext()) {
//			// value = hashmap.get(iterator.next());
//			
//			list.add(proviceData.get(iterator.next()));
//		}
		return list;
	}

	/**
	 * ������Ҫ��ȡ���б������Լ����������г���Ӧ�ĳ���ID
	 * @param listName  ��Ҫ��ȡ�����м��б��������б�
	 * @param cityName  ��Ҫ�ĳ��л����������
	 * @return
	 */
	public static String getIdFromList(String listName, String cityName) {
		String cityId = null;
		Iterator iter = null;
		if (listName.equals("province")) {
			iter = proviceData.entrySet().iterator();
		}
		else if(listName.equals("city"))
			iter = cityData.entrySet().iterator();
		else if(listName.equals("area"))
			iter = areaData.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			if (val.equals(cityName)) {
				cityId=key.toString();
			}
//			Log.i(MyApplication.TAG, "val-->"+val.toString()+"  key-->"+key.toString());
//			Log.i(MyApplication.TAG, "equals-->"+cityName.equals(val)+" cityId--> "+cityId);
		}
		return cityId;
	}

	/**
	 * ��ȡ�м��б�
	 * 
	 * @return
	 */
	public static ArrayList getCityList() {
		ArrayList list = new ArrayList<String>();
		Iterator iterator = cityData.keySet().iterator();
		while (iterator.hasNext()) {
			// value = hashmap.get(iterator.next());
			list.add(cityData.get(iterator.next()));
		}
		return list;
	}

	
	/**
	 * ��ȡ�����б�
	 * 
	 * @return
	 */
	public static ArrayList getAreaList() {
		ArrayList list = new ArrayList<String>();
		Iterator iterator = areaData.keySet().iterator();
		while (iterator.hasNext()) {
			// value = hashmap.get(iterator.next());
			list.add(areaData.get(iterator.next()));
		}
		return list;
	}
}
