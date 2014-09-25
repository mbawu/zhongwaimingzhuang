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

	private TextView titleTxt;// 标题文字
	private LinearLayout hotMoreBtn; // 热门商品更多按钮
	private LinearLayout secKillMoreBtn; // 秒杀商品更多按钮
	private ViewFlipper flipper;// 广告容器
	private String netAction = "getAd"; // 向服务器操作的动作
	private MyGridView hotGridView;// 热卖商品
	private MyGridView secKillGridView;// 热卖商品
	private int page = 1; // 当前页码
	private String pageSize = "4"; // 每页显示的数据条数
	private int totalPage = 0; // 总页码
	private ArrayList<Object> hotProduct; // 数据集合
	private ArrayList<Object> secKillProduct; // 数据集合
	private MyAdapter adapterHot; // 热门商品适配器
	private MyAdapter adapterSecKill; // 秒杀商品适配器
	private ScrollView srollView; // 滚动条
	private LinearLayout backBtn;// 后退按钮
	private FrameLayout hotTopModule;// 热门商品上面显示更多和文本的容器
	private FrameLayout secKillTopModule;// 秒杀商品上面显示更多和文本的容器
	private LinearLayout secKillLayout;// 秒杀模块内容
	private LinearLayout hotLayout;// 热门商品模块内容
	private boolean hotModule = false;// 热门商品模块标示
	private boolean secKillModule = false;// 秒杀商品模块标示
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
		//开启倒计时功能
		startChangeTime();
		initView();
		initData();
	}

	//开启倒计时功能
		private void startChangeTime() {
			//如果退出程序以后该标识符为true，再次进入的时候需要重置该标识符才可启动倒计时功能
			if(!ChangeTime.exit)
			{
				ChangeTime.exit=true;
			}
			
		}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 设置滚动条初始位置
//		srollView.smoothScrollTo(0, 0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 该页面被销毁以后监测刷新秒杀倒计时的线程也将随之结束
				ChangeTime.exit=false;
	}

	private void initView() {
		changeTime = new ChangeTime();
		Thread thread = new Thread(changeTime);
		thread.start();
		hotTopModule = (FrameLayout) findViewById(R.id.home_hot_framlayout);//热门商品上面显示更多和文本的容器
		secKillTopModule = (FrameLayout) findViewById(R.id.home_seckill_framlayout);// 秒杀商品上面显示更多和文本的容器
		titleTxt = (TextView) findViewById(R.id.home_title);
		flipper = (ViewFlipper) findViewById(R.id.home_viewFlipper);
		secKillGridView = (MyGridView) findViewById(R.id.home_seckill_gridview);
		hotGridView = (MyGridView) findViewById(R.id.home_hot_gridview);
		hotGridView.setOnItemClickListener(this);
		secKillGridView.setOnItemClickListener(this);
		secKillLayout = (LinearLayout) findViewById(R.id.home_seckill_layout);// 秒杀模块
		hotLayout = (LinearLayout) findViewById(R.id.home_hot_layout);// 热门商品模块
		hotMoreBtn = (LinearLayout) findViewById(R.id.home_hot_more_btn);// 热门商品更多按钮
		hotMoreBtn.setOnClickListener(this);
		secKillMoreBtn = (LinearLayout) findViewById(R.id.home_seckill_more_btn); // 秒杀商品更多按钮
		secKillMoreBtn.setOnClickListener(this);
		backBtn = (LinearLayout) findViewById(R.id.home_backbtn);
		backBtn.setOnClickListener(this);
		srollView = (ScrollView) findViewById(R.id.home_scroll);
		srollView.setOnTouchListener(new TouchListenerImpl());

		hotProduct = new ArrayList<Object>();
		secKillProduct = new ArrayList<Object>();
		// 设置滚动条初始位置
		srollView.smoothScrollTo(0, 0);
	}

	// 滚动条点击和滑动事件监听器
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
				// 如果滑动到顶端的事件
				if (scrollY == 0) {

				}
				// 如果滑动到底部的事件
				if ((scrollY + height) == scrollViewMeasuredHeight) {
					// 如果还需要加载数据的话按照不同的模块获取不同的数据
					if (load) {
						load = false;

						// 热门商品显示模式
						if (hotModule) {
//							MyApplication.progressShow(Home.this, "数据");
							sendDataToServer(NetworkAction.热门商品);// 获取热门商品
						} else if (secKillModule) {
							MyApplication.progressShow(Home.this, "数据");
							sendDataToServer(NetworkAction.秒杀商品);// 获取秒杀商品
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
		sendDataToServer(NetworkAction.首页广告);// 获取首页广告
		sendDataToServer(NetworkAction.热门商品);// 获取热门商品
		sendDataToServer(NetworkAction.秒杀商品);// 获取秒杀商品
		adapterHot = new MyAdapter(this, NetworkAction.热门商品, hotProduct);
		adapterSecKill = new MyAdapter(this, NetworkAction.秒杀商品, secKillProduct);
		hotGridView.setAdapter(adapterHot);
		secKillGridView.setAdapter(adapterSecKill);

		// 每秒刷新秒杀商品时间的handler
		homeHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				// 取出最新的时间
				long time = bundle.getLong("time");
				// 取出索引，根据该索引查询对应的时间和textview
				int index = bundle.getInt("index");
				// 从子线程返回的计算好了的新的时间字符串
				String timeString = bundle.getString("timeString");
				TextView txt = (TextView) ((View) ChangeTime.txtViewList.get(
						index).getTag())
						.findViewById(R.id.home_seckill_outtime);
				// 如果秒杀还没有结束的话，刷新最新的时间，否则显示为秒杀已结束并把相应的时间和textView移除。
				// if(time>0)
				// ChangeTime.txtViewList.get(index).setText(timeString);
				if (time > 0)
					txt.setText(timeString);
				else {
					ChangeTime.txtViewList.get(index).setText("秒杀已结束");
					ChangeTime.txtViewList.remove(index);
					ChangeTime.timeList.remove(index);
				}
			}
		};
	}

	private void sendDataToServer(final NetworkAction request) {

		String url = null;
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.首页广告))// 获取首页广告操作
		{
			url = Url.URL_INDEX;
			paramter.put("act", "img1");
			paramter.put("nowpage", "1");
			paramter.put("pagesize", "100");
			paramter.put("sid", MyApplication.sid);
		} else if (request.equals(NetworkAction.热门商品)) {
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
		} else if (request.equals(NetworkAction.秒杀商品)) {
			Log.i(MyApplication.TAG, "page-->" + page);
			url = Url.URL_SECKILL;
			paramter.put("act", "list");
			paramter.put("sid", MyApplication.sid);
//			paramter.put("sid", "16");
			paramter.put("nowpage", String.valueOf(page));
			paramter.put("pagesize",pageSize);
		}

		// 打印生成的链接地址
		MyApplication.printLog("Home",
				"URL:" + MyApplication.getUrl(paramter, url));
		/*
		 * 向服务器发送请求
		 */
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
//							int code = response.getInt("code");
							Log.i(MyApplication.TAG, request+response.toString());
							if (request.equals(NetworkAction.首页广告))// 获取首页广告操作
							{
								// if (response.getInt("code") == 1) {
								final JSONArray lists = response.getJSONArray("list");
								//如果有首页广告图片并且没有获取到实际网络图片的宽高比例的时候先获取其相对高度
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
												sendDataToServer(NetworkAction.首页广告);
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
							} else if (request.equals(NetworkAction.热门商品)) {// 获取热门商品
								if (response.getInt("code") == 1) {// 如果成功的话
									// objects.clear();
									page++;// 当前页加一
									totalPage = Integer.valueOf(response
											.getString("totalpage"));// 获取总页码

									// 判断是否还要加载数据
									if (page > totalPage)
										load = false;
									else
										load = true;
									JSONArray lists = response
											.getJSONArray("list");// 获取数据集
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
										hotProduct.add(newProduct);// 新获取到的数据添加到数据集合中
									}
									adapterHot.notifyDataSetChanged();// 通知适配器数据发生变化了

								}
							} else if (request.equals(NetworkAction.秒杀商品)) {// 获取秒杀商品
								if (response.getInt("code") == 1) {// 如果成功的话
									// objects.clear();
									page++;// 当前页加一
									totalPage = Integer.valueOf(response
											.getString("totalpage"));// 获取总页码

									// 判断是否还要加载数据
									if (page > totalPage)
										load = false;
									else
										load = true;
									String time = response.getString("time");
									JSONArray lists = response
											.getJSONArray("list");// 获取数据集
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
										secKillProduct.add(newProduct);// 新获取到的数据添加到数据集合中
									}
									adapterSecKill.notifyDataSetChanged();// 通知适配器数据发生变化了

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
		page = 1;// 初始化数据

		switch (v.getId()) {
		case R.id.home_hot_more_btn:// 热门商品更多按钮
			// intent=new Intent().setClass(this, HomeHot.class);
			// 显示后退按钮，更改标题，隐藏秒杀模块，显示更多的热门商品，更改热门商品模块标示，更改秒杀模块标示
			hotMoreBtn.setVisibility(View.GONE);// 隐藏更多按钮
			hotProduct.clear();// 清空数据
			pageSize = "8";// 显示更多热门商品
			backBtn.setVisibility(View.VISIBLE);// 显示后退按钮
			// hotTopModule.setVisibility(View.GONE);// 隐藏热门商品上面的模块
			titleTxt.setText("热门商品");// 标题显示为热门商品
			secKillLayout.setVisibility(View.GONE);// 隐藏秒杀模块
			hotLayout.setVisibility(View.VISIBLE);// 显示热门商品模块
			secKillTopModule.setVisibility(View.GONE);// 隐藏专区上部模块
			hotTopModule.setVisibility(View.GONE);//  隐藏专区上部模块
			hotModule = true;// 更改热门商品模块标示，为了滑动的时候只针对相应的模块进行获取数据
			secKillModule = false;// 更改秒杀模块标示
			sendDataToServer(NetworkAction.热门商品);// 初始化热门商品数据
		
			break;
		case R.id.home_backbtn:// 后退按钮
			// 隐藏后退按钮，更改标题，显示4个秒杀商品，显示4个热门商品，更改热门商品模块标示，更改秒杀模块标示
			hotProduct.clear();// 清空数据
			secKillProduct.clear();// 清空数据
			ChangeTime.timeList.clear();// 清空记录的秒杀商品的时间
			ChangeTime.txtViewList.clear();// 清空记录的秒杀商品的时间对应的文本框
			pageSize = "4";// 显示4条数据
			sendDataToServer(NetworkAction.热门商品);// 初始化热门商品数据
			sendDataToServer(NetworkAction.秒杀商品);// 初始化秒杀商品数据
			// sendDataToServer(getHotProduct);//初始化秒杀商品数据
			backBtn.setVisibility(View.GONE);// 隐藏后退按钮
			titleTxt.setText("恒融汇通");// 标题显示为恒融汇通
			// secKillLayout.setVisibility(View.VISIBLE);//显示秒杀模块
			hotLayout.setVisibility(View.VISIBLE);// 显示热门商品模块
			secKillLayout.setVisibility(View.VISIBLE);// 显示秒杀模块
			secKillTopModule.setVisibility(View.VISIBLE);// 显示专区上部模块
			hotTopModule.setVisibility(View.VISIBLE);//  显示专区上部模块
			hotModule = false;// 更改热门商品模块标示
			secKillModule = false;// 更改秒杀模块标示
			secKillMoreBtn.setVisibility(View.VISIBLE);// 显示秒杀商品更多按钮
			hotMoreBtn.setVisibility(View.VISIBLE);// 显示热门商品更多按钮
			srollView.smoothScrollTo(0, 0);
			break;

		case R.id.home_seckill_more_btn:// 秒杀商品更多按钮
			// 显示后退按钮，更改标题，隐藏热门商品模块，显示更多的秒杀商品，更改热门商品模块标示，更改秒杀模块标示
			secKillMoreBtn.setVisibility(View.GONE);// 隐藏更多按钮
			secKillProduct.clear();// 清空数据
			ChangeTime.timeList.clear();// 清空记录的秒杀商品的时间
			ChangeTime.txtViewList.clear();// 清空记录的秒杀商品的时间对应的文本框
			pageSize = "8";// 显示更多秒杀商品
			sendDataToServer(NetworkAction.秒杀商品);// 初始化秒杀商品数据
			backBtn.setVisibility(View.VISIBLE);// 显示后退按钮
			// hotTopModule.setVisibility(View.GONE);// 隐藏热门商品上面的模块
			titleTxt.setText("秒杀商品");// 标题显示为秒杀商品
			secKillLayout.setVisibility(View.VISIBLE);// 显示秒杀模块
			hotLayout.setVisibility(View.GONE);// 隐藏热门商品模块
			secKillTopModule.setVisibility(View.GONE);// 隐藏专区上部模块
			hotTopModule.setVisibility(View.GONE);//  隐藏专区上部模块
			hotModule = false;// 更改热门商品模块标示，为了滑动的时候只针对相应的模块进行获取数据
			secKillModule = true;// 更改秒杀模块标示
			break;

		default:
			break;
		}

		if (intent != null)
			startActivity(intent);
	}

	/*
	 * gridview的item点击事件
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