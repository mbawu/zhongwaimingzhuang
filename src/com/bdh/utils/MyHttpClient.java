package com.bdh.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * Created with IntelliJ IDEA. User: jimmy Date: 8/6/13 Time: 4:02 PM To change
 * this template use File | Settings | File Templates.
 */
public class MyHttpClient extends Object {
	private RequestQueue mRequestQueue; // ���������������У��ȸ��װ�õġ�
	private BitmapCache mBitmapCache = null;
	private ImageLoader mImageLoader = null;
	private static MyHttpClient client;
	private String TAG="trainticket";

	public static MyHttpClient getInstance(Context con) {
		if (client == null) {
			client = new MyHttpClient(con);
			return client;
		}
		return client;
	}

	private MyHttpClient(Context ctx) {
		super();
		mRequestQueue = Volley.newRequestQueue(ctx);// ���еĳ�ʼ��
		mBitmapCache = new BitmapCache();
		mImageLoader = new ImageLoader(mRequestQueue, mBitmapCache);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	public void addRequest(Request req) { // ���������ȸ�д�õĶ����У��ȸ���Զ�ִ������������������ص����ݻص�������Ļص��ӿ�onResponse��
		mRequestQueue.add(req);
	}

	/**
	 * Converts <code>params</code> into an application/x-www-form-urlencoded
	 * encoded string.
	 */
	private String encodeParameters(Map<String, String> params,
			String paramsEncoding) {
		StringBuilder encodedParams = new StringBuilder();
		try {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				encodedParams.append(URLEncoder.encode(entry.getKey(),
						paramsEncoding));
				encodedParams.append('=');
				encodedParams.append(URLEncoder.encode(entry.getValue(),
						paramsEncoding));
				encodedParams.append('&');
			}
			return encodedParams.toString();
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException("Encoding not supported: "
					+ paramsEncoding, uee);
		}
	}

	// get����
	public void getWithURL(String url, Map<String, String> params,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		if (params != null && params.size() > 0) {
			if (url.contains("?")) {
				url = url + "&" + encodeParameters(params, "UTF-8");
			} else {
				url = url + "?" + encodeParameters(params, "UTF-8");
			}
		}
		MyRequest jsObjRequest = new MyRequest(Request.Method.GET, url, params,
				listener, errorListener);
		this.addRequest(jsObjRequest);
	}

	// post����
	public void postWithURL(String url, Map<String, String> params,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {
		MyRequest jsObjRequest = new MyRequest(Request.Method.POST, url,
				params, listener, errorListener);
		jsObjRequest.setTag(url);
		this.addRequest(jsObjRequest);

	}

	public RequestQueue getmRequestQueue() {
		return mRequestQueue;
	}

	public void setmRequestQueue(RequestQueue mRequestQueue) {
		this.mRequestQueue = mRequestQueue;
	}

	public BitmapCache getmBitmapCache() {
		return mBitmapCache;
	}

	public void setmBitmapCache(BitmapCache mBitmapCache) {
		this.mBitmapCache = mBitmapCache;
	}

	// ��ȡͼƬ
	public NetworkImageView getImageForNetImageView(String url,
			NetworkImageView imageView, int id) {
		imageView.setErrorImageResId(id);
		imageView.setDefaultImageResId(0);
		imageView.setImageUrl(url, mImageLoader);
		return imageView;
	}
}