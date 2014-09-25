package com.zwmz.activity.product;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.zwmz.R;
import com.zwmz.base.BaseActivity;
import com.zwmz.base.ChangeTime;
import com.zwmz.base.MyAdapter;
import com.zwmz.base.MyApplication;
import com.zwmz.base.MyGridView;
import com.zwmz.base.Url;
import com.zwmz.model.NetworkAction;
import com.zwmz.model.Product;

public class Home extends BaseActivity implements OnClickListener,
		OnItemClickListener {

	private TextView titleTxt;// ��������
	private LinearLayout hotMoreBtn; // ������Ʒ���ఴť
	private LinearLayout secKillMoreBtn; // ��ɱ��Ʒ���ఴť
	private ViewFlipper flipper;// �������
	private String netAction = "getAd"; // ������������Ķ���
	private MyGridView hotGridView;// ������Ʒ
	private MyGridView secKillGridView;// ������Ʒ
	private int page = 1; // ��ǰҳ��
	private String pageSize = "4"; // ÿҳ��ʾ����������
	private int totalPage = 0; // ��ҳ��
	private ArrayList<Object> hotProduct; // ���ݼ���
	private ArrayList<Object> secKillProduct; // ���ݼ���
	private MyAdapter adapterHot; // ������Ʒ������
	private MyAdapter adapterSecKill; // ��ɱ��Ʒ������
	private ScrollView srollView; // ������
	private LinearLayout backBtn;// ���˰�ť
	private FrameLayout hotTopModule;// ������Ʒ������ʾ������ı�������
	private FrameLayout secKillTopModule;// ��ɱ��Ʒ������ʾ������ı�������
	private LinearLayout secKillLayout;// ��ɱģ������
	private LinearLayout hotLayout;// ������Ʒģ������
	private boolean hotModule = false;// ������Ʒģ���ʾ
	private boolean secKillModule = false;// ��ɱ��Ʒģ���ʾ
	private boolean load;
	public static Handler homeHandler;
	public ChangeTime changeTime;

	private boolean getHeight=false;
	private int newHeight=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		//��������ʱ����
		startChangeTime();
		initView();
		initData();
	}

	//��������ʱ����
		private void startChangeTime() {
			//����˳������Ժ�ñ�ʶ��Ϊtrue���ٴν����ʱ����Ҫ���øñ�ʶ���ſ���������ʱ����
			if(!ChangeTime.exit)
			{
				ChangeTime.exit=true;
			}
			
		}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// ���ù�������ʼλ��
//		srollView.smoothScrollTo(0, 0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// ��ҳ�汻�����Ժ���ˢ����ɱ����ʱ���߳�Ҳ����֮����
				ChangeTime.exit=false;
	}

	private void initView() {
		changeTime = new ChangeTime();
		Thread thread = new Thread(changeTime);
		thread.start();
		hotTopModule = (FrameLayout) findViewById(R.id.home_hot_framlayout);//������Ʒ������ʾ������ı�������
		secKillTopModule = (FrameLayout) findViewById(R.id.home_seckill_framlayout);// ��ɱ��Ʒ������ʾ������ı�������
		titleTxt = (TextView) findViewById(R.id.home_title);
		flipper = (ViewFlipper) findViewById(R.id.home_viewFlipper);
		secKillGridView = (MyGridView) findViewById(R.id.home_seckill_gridview);
		hotGridView = (MyGridView) findViewById(R.id.home_hot_gridview);
		hotGridView.setOnItemClickListener(this);
		secKillGridView.setOnItemClickListener(this);
		secKillLayout = (LinearLayout) findViewById(R.id.home_seckill_layout);// ��ɱģ��
		hotLayout = (LinearLayout) findViewById(R.id.home_hot_layout);// ������Ʒģ��
		hotMoreBtn = (LinearLayout) findViewById(R.id.home_hot_more_btn);// ������Ʒ���ఴť
		hotMoreBtn.setOnClickListener(this);
		secKillMoreBtn = (LinearLayout) findViewById(R.id.home_seckill_more_btn); // ��ɱ��Ʒ���ఴť
		secKillMoreBtn.setOnClickListener(this);
		backBtn = (LinearLayout) findViewById(R.id.home_backbtn);
		backBtn.setOnClickListener(this);
		srollView = (ScrollView) findViewById(R.id.home_scroll);
		srollView.setOnTouchListener(new TouchListenerImpl());

		hotProduct = new ArrayList<Object>();
		secKillProduct = new ArrayList<Object>();
		// ���ù�������ʼλ��
		srollView.smoothScrollTo(0, 0);
	}

	// ����������ͻ����¼�������
	private class TouchListenerImpl implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:
				int scrollY = view.getScrollY();
				int height = view.getHeight();
				int scrollViewMeasuredHeight = srollView.getChildAt(0)
						.getMeasuredHeight();
				// ������������˵��¼�
				if (scrollY == 0) {

				}
				// ����������ײ����¼�
				if ((scrollY + height) == scrollViewMeasuredHeight) {
					// �������Ҫ�������ݵĻ����ղ�ͬ��ģ���ȡ��ͬ������
					if (load) {
						load = false;

						// ������Ʒ��ʾģʽ
						if (hotModule) {
//							MyApplication.progressShow(Home.this, "����");
							sendDataToServer(NetworkAction.������Ʒ);// ��ȡ������Ʒ
						} else if (secKillModule) {
							MyApplication.progressShow(Home.this, "����");
							sendDataToServer(NetworkAction.��ɱ��Ʒ);// ��ȡ��ɱ��Ʒ
						}
					}

				}
				break;

			default:
				break;
			}
			return false;
		}

	};

	private void initData() {
		// TODO Auto-generated method stub
		sendDataToServer(NetworkAction.��ҳ���);// ��ȡ��ҳ���
		sendDataToServer(NetworkAction.������Ʒ);// ��ȡ������Ʒ
		sendDataToServer(NetworkAction.��ɱ��Ʒ);// ��ȡ��ɱ��Ʒ
		adapterHot = new MyAdapter(this, NetworkAction.������Ʒ, hotProduct);
		adapterSecKill = new MyAdapter(this, NetworkAction.��ɱ��Ʒ, secKillProduct);
		hotGridView.setAdapter(adapterHot);
		secKillGridView.setAdapter(adapterSecKill);

		// ÿ��ˢ����ɱ��Ʒʱ���handler
		homeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				// ȡ�����µ�ʱ��
				long time = bundle.getLong("time");
				// ȡ�����������ݸ�������ѯ��Ӧ��ʱ���textview
				int index = bundle.getInt("index");
				// �����̷߳��صļ�����˵��µ�ʱ���ַ���
				String timeString = bundle.getString("timeString");
				TextView txt = (TextView) ((View) ChangeTime.txtViewList.get(
						index).getTag())
						.findViewById(R.id.home_seckill_outtime);
				// �����ɱ��û�н����Ļ���ˢ�����µ�ʱ�䣬������ʾΪ��ɱ�ѽ���������Ӧ��ʱ���textView�Ƴ���
				// if(time>0)
				// ChangeTime.txtViewList.get(index).setText(timeString);
				if (time > 0)
					txt.setText(timeString);
				else {
					ChangeTime.txtViewList.get(index).setText("��ɱ�ѽ���");
					ChangeTime.txtViewList.remove(index);
					ChangeTime.timeList.remove(index);
				}
			}
		};
	}

	private void sendDataToServer(final NetworkAction request) {

		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.��ҳ���))// ��ȡ��ҳ������
		{
			url = Url.URL_INDEX;
			paramter.put("act", "img1");
			paramter.put("nowpage", "1");
			paramter.put("pagesize", "100");
			paramter.put("sid", MyApplication.sid);
		} else if (request.equals(NetworkAction.������Ʒ)) {
			// MyApplication.progressShow(this, request.toString());
			MyApplication.progressShow(this, request.toString());
			Log.i(MyApplication.TAG, "page-->" + page);
			url = Url.URL_HOTSALE;
			paramter.put("act", "hotsale");
			paramter.put("CacheID", "");
			paramter.put("CacheID1", "0");
			paramter.put("brans", "0");
			paramter.put("cates", "0");
			paramter.put("clears", "0");
			paramter.put("keyname", "");
			paramter.put("keyname1", "");
			paramter.put("nowpage", String.valueOf(page));
			paramter.put("pagesize", pageSize);
			paramter.put("sort_type", "0");
			paramter.put("sid", MyApplication.sid);
		} else if (request.equals(NetworkAction.��ɱ��Ʒ)) {
			Log.i(MyApplication.TAG, "page-->" + page);
			url = Url.URL_SECKILL;
			paramter.put("act", "list");
			paramter.put("sid", MyApplication.sid);
//			paramter.put("sid", "16");
			paramter.put("nowpage", String.valueOf(page));
			paramter.put("pagesize",pageSize);
		}

		// ��ӡ���ɵ����ӵ�ַ
		MyApplication.printLog("Home",
				"URL:" + MyApplication.getUrl(paramter, url));
		/*
		 * ���������������
		 */
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
//							int code = response.getInt("code");
							Log.i(MyApplication.TAG, request+response.toString());
							if (request.equals(NetworkAction.��ҳ���))// ��ȡ��ҳ������
							{
								// if (response.getInt("code") == 1) {
								final JSONArray lists = response.getJSONArray("list");
								//�������ҳ���ͼƬ����û�л�ȡ��ʵ������ͼƬ�Ŀ�߱�����ʱ���Ȼ�ȡ����Ը߶�
								if(lists.length()>0 && !getHeight)
								{
									Thread thread=new Thread(new Runnable() {
										
										@Override
										public void run() {
											JSONObject item;
											try {
												item = lists.getJSONObject(0);
												String path = Url.URL_IMGPATH
														+ item.getString("attachments_path");
												URL url = new URL(path);
												String responseCode = url.openConnection().getHeaderField(0);
												Bitmap map = BitmapFactory.decodeStream(url.openStream());
												int height = map.getHeight();
												int width = map.getWidth();
												newHeight = (int) (MyApplication.width * ((double)height / width));
												getHeight=true;
												sendDataToServer(NetworkAction.��ҳ���);
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} 
											
											
										}
									});
									thread.start();
									return;
								}
								for (int i = 0; i < lists.length(); i++) {
									JSONObject item = lists.getJSONObject(i);
									NetworkImageView netView = new NetworkImageView(
											Home.this);
									String path = Url.URL_IMGPATH
											+ item.getString("attachments_path");
									Log.i(MyApplication.TAG, "path-->" + path);
									netView.setAdjustViewBounds(false);
									LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											newHeight);
									netView.setLayoutParams(layoutParams);
									MyApplication.client
											.getImageForNetImageView(path,
													netView,
													R.drawable.ic_launcher);
									flipper.addView(netView);
									flipper.setInAnimation(Home.this,
											R.anim.view_in_from_right);
									flipper.setOutAnimation(Home.this,
											R.anim.view_out_to_left);
									flipper.startFlipping();
								}
							} else if (request.equals(NetworkAction.������Ʒ)) {// ��ȡ������Ʒ
								if (response.getInt("code") == 1) {// ����ɹ��Ļ�
									// objects.clear();
									page++;// ��ǰҳ��һ
									totalPage = Integer.valueOf(response
											.getString("totalpage"));// ��ȡ��ҳ��

									// �ж��Ƿ�Ҫ��������
									if (page > totalPage)
										load = false;
									else
										load = true;
									JSONArray lists = response
											.getJSONArray("list");// ��ȡ���ݼ�
									for (int i = 0; i < lists.length(); i++) {
										JSONObject product = lists
												.getJSONObject(i);
										Product newProduct = new Product();
										newProduct.setId(product
												.getString("product_id"));
										newProduct.setName(product
												.getString("product_name"));
										newProduct.setStorePrice(product
												.getString("store_price"));
										newProduct.setImgPath(product
												.getString("product_photo"));
										hotProduct.add(newProduct);// �»�ȡ����������ӵ����ݼ�����
									}
									adapterHot.notifyDataSetChanged();// ֪ͨ���������ݷ����仯��

								}
							} else if (request.equals(NetworkAction.��ɱ��Ʒ)) {// ��ȡ��ɱ��Ʒ
								if (response.getInt("code") == 1) {// ����ɹ��Ļ�
									// objects.clear();
									page++;// ��ǰҳ��һ
									totalPage = Integer.valueOf(response
											.getString("totalpage"));// ��ȡ��ҳ��

									// �ж��Ƿ�Ҫ��������
									if (page > totalPage)
										load = false;
									else
										load = true;
									String time = response.getString("time");
									JSONArray lists = response
											.getJSONArray("list");// ��ȡ���ݼ�
									for (int i = 0; i < lists.length(); i++) {
										JSONObject product = lists
												.getJSONObject(i);
										Product newProduct = new Product();
										newProduct.setSkID(product
												.getString("SKID"));
										newProduct.setId(product
												.getString("ProductID"));
										newProduct.setName(product
												.getString("SKName"));
										newProduct.setSKPrice(product
												.getString("SKPrice"));
										newProduct.setOutEndTime(product
												.getString("OutEndTime"));
										newProduct.setImgPath(product
												.getString("Images"));
										newProduct.setTime(time);
										secKillProduct.add(newProduct);// �»�ȡ����������ӵ����ݼ�����
									}
									adapterSecKill.notifyDataSetChanged();// ֪ͨ���������ݷ����仯��

								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		page = 1;// ��ʼ������

		switch (v.getId()) {
		case R.id.home_hot_more_btn:// ������Ʒ���ఴť
			// intent=new Intent().setClass(this, HomeHot.class);
			// ��ʾ���˰�ť�����ı��⣬������ɱģ�飬��ʾ�����������Ʒ������������Ʒģ���ʾ��������ɱģ���ʾ
			hotMoreBtn.setVisibility(View.GONE);// ���ظ��ఴť
			hotProduct.clear();// �������
			pageSize = "8";// ��ʾ����������Ʒ
			backBtn.setVisibility(View.VISIBLE);// ��ʾ���˰�ť
			// hotTopModule.setVisibility(View.GONE);// ����������Ʒ�����ģ��
			titleTxt.setText("������Ʒ");// ������ʾΪ������Ʒ
			secKillLayout.setVisibility(View.GONE);// ������ɱģ��
			hotLayout.setVisibility(View.VISIBLE);// ��ʾ������Ʒģ��
			secKillTopModule.setVisibility(View.GONE);// ����ר���ϲ�ģ��
			hotTopModule.setVisibility(View.GONE);//  ����ר���ϲ�ģ��
			hotModule = true;// ����������Ʒģ���ʾ��Ϊ�˻�����ʱ��ֻ�����Ӧ��ģ����л�ȡ����
			secKillModule = false;// ������ɱģ���ʾ
			sendDataToServer(NetworkAction.������Ʒ);// ��ʼ��������Ʒ����
		
			break;
		case R.id.home_backbtn:// ���˰�ť
			// ���غ��˰�ť�����ı��⣬��ʾ4����ɱ��Ʒ����ʾ4��������Ʒ������������Ʒģ���ʾ��������ɱģ���ʾ
			hotProduct.clear();// �������
			secKillProduct.clear();// �������
			ChangeTime.timeList.clear();// ��ռ�¼����ɱ��Ʒ��ʱ��
			ChangeTime.txtViewList.clear();// ��ռ�¼����ɱ��Ʒ��ʱ���Ӧ���ı���
			pageSize = "4";// ��ʾ4������
			sendDataToServer(NetworkAction.������Ʒ);// ��ʼ��������Ʒ����
			sendDataToServer(NetworkAction.��ɱ��Ʒ);// ��ʼ����ɱ��Ʒ����
			// sendDataToServer(getHotProduct);//��ʼ����ɱ��Ʒ����
			backBtn.setVisibility(View.GONE);// ���غ��˰�ť
			titleTxt.setText("���ڻ�ͨ");// ������ʾΪ���ڻ�ͨ
			// secKillLayout.setVisibility(View.VISIBLE);//��ʾ��ɱģ��
			hotLayout.setVisibility(View.VISIBLE);// ��ʾ������Ʒģ��
			secKillLayout.setVisibility(View.VISIBLE);// ��ʾ��ɱģ��
			secKillTopModule.setVisibility(View.VISIBLE);// ��ʾר���ϲ�ģ��
			hotTopModule.setVisibility(View.VISIBLE);//  ��ʾר���ϲ�ģ��
			hotModule = false;// ����������Ʒģ���ʾ
			secKillModule = false;// ������ɱģ���ʾ
			secKillMoreBtn.setVisibility(View.VISIBLE);// ��ʾ��ɱ��Ʒ���ఴť
			hotMoreBtn.setVisibility(View.VISIBLE);// ��ʾ������Ʒ���ఴť
			srollView.smoothScrollTo(0, 0);
			break;

		case R.id.home_seckill_more_btn:// ��ɱ��Ʒ���ఴť
			// ��ʾ���˰�ť�����ı��⣬����������Ʒģ�飬��ʾ�������ɱ��Ʒ������������Ʒģ���ʾ��������ɱģ���ʾ
			secKillMoreBtn.setVisibility(View.GONE);// ���ظ��ఴť
			secKillProduct.clear();// �������
			ChangeTime.timeList.clear();// ��ռ�¼����ɱ��Ʒ��ʱ��
			ChangeTime.txtViewList.clear();// ��ռ�¼����ɱ��Ʒ��ʱ���Ӧ���ı���
			pageSize = "8";// ��ʾ������ɱ��Ʒ
			sendDataToServer(NetworkAction.��ɱ��Ʒ);// ��ʼ����ɱ��Ʒ����
			backBtn.setVisibility(View.VISIBLE);// ��ʾ���˰�ť
			// hotTopModule.setVisibility(View.GONE);// ����������Ʒ�����ģ��
			titleTxt.setText("��ɱ��Ʒ");// ������ʾΪ��ɱ��Ʒ
			secKillLayout.setVisibility(View.VISIBLE);// ��ʾ��ɱģ��
			hotLayout.setVisibility(View.GONE);// ����������Ʒģ��
			secKillTopModule.setVisibility(View.GONE);// ����ר���ϲ�ģ��
			hotTopModule.setVisibility(View.GONE);//  ����ר���ϲ�ģ��
			hotModule = false;// ����������Ʒģ���ʾ��Ϊ�˻�����ʱ��ֻ�����Ӧ��ģ����л�ȡ����
			secKillModule = true;// ������ɱģ���ʾ
			break;

		default:
			break;
		}

		if (intent != null)
			startActivity(intent);
	}

	/*
	 * gridview��item����¼�
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long productId) {

		Intent intent = new Intent().setClass(this, ProductDetail.class);
		
		if (parent.getId() == R.id.home_seckill_gridview) {
			Product product = (Product) secKillProduct.get(position);
			intent.putExtra("productId",product.getId());
			intent.putExtra("skid",product.getSkID());
//			Log.i(MyApplication.TAG,"productId-->"+product.getId()+"  skid-->"+product.getSkID());
		}
		else if(parent.getId() == R.id.home_hot_gridview)
		{
			intent.putExtra("productId", String.valueOf(productId));
		}
//		Toast.makeText(this, String.valueOf(productId), 2000).show();
		 startActivity(intent);

	}
}