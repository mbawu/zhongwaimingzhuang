package com.bdh.activity.product;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.NetworkImageView;
import com.bdh.activity.MenuBottom;
import com.bdh.activity.person.PersonLogin;
import com.bdh.base.ChangeTime;
import com.bdh.base.CustomDialog;
import com.bdh.base.MyApplication;
import com.bdh.base.MyViewFlipper;
import com.bdh.base.MyWebView;
import com.bdh.base.Url;
import com.bdh.model.Coupon;
import com.bdh.model.ErrorMsg;
import com.bdh.model.NetworkAction;
import com.bdh.model.Product;
import com.bdh.utils.EulaWebView;
import com.bdh.R;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class ProductDetail extends Activity implements OnClickListener,
		OnGestureListener {

	private Resources resources;
	private RatingBar stars;// �����Ǽ�
	private TextView secKillTime;// ��ɱ��Ʒʱ��
	private TextView pNum;// ������Ʒ������
	private TextView imgNum;// ��ƷͼƬ������
	private TextView commentNumTxt;//��������
	private TextView storePriceTxt;// ��ʾ����ɱ�ۻ��Ǵ����۵��ı���
	private String num = "1";// ��¼��Ʒ����
	private ProgressDialog progressBar;// ������Ʒ����ʱ��Ľ�����
	private AlertDialog alertDialog;// ������Ʒ�������ʱ�����ʾ��
	private WebView webView;// ��ʾ��Ʒ�����webView
	String productUrl = "http://api2.xinlingmingdeng.com/products/"; // ��Ʒ������ַ
	private String productId;// ����������ƷID
	private String skid;// ��ɱ��ƷID
	private TextView name;// ��Ʒ����
	private TextView referencePrice;// ��Ʒ�ο��۸�
	private TextView storePrice;// ��Ʒ�����۸�
	private TextView discountTxt;// ��Ʒ�ۿ��ı���
	private String discount;// ��Ʒ�ۿ�
	private TextView cashBack;// ����
	private Product product;// ��Ʒʵ����
	private LinearLayout backBtn;// ���˰�ť
	private ImageView addImg;// ����������ť
	private ImageView subImg;// ����������ť
	private LinearLayout call;// ��ϵ�ͷ���ť
	private LinearLayout comment;// �鿴���۰�ť
	private Button buyNow;// ��������ť
	private Button addShopcart;// ���빺�ﳵ��ť
	private LinearLayout attributeLayout;// ��Ʒ����
	private int viewWidth;
	private int viewHeight;
	public static Handler secKillHandler;// ��ɱ����ʱhandler
	private MyViewFlipper viewFlipper;
	private GestureDetector gesture;
	private int photopage = 1;
	private int photototalpage;
	private LinearLayout.LayoutParams layoutParams;
	private String buyType = "1"; // Buy_type �������ͣ�1��������2��ɱ
	private String freight;// �˷�
	private LinearLayout discountLayout;// ������Ϣģ��
	private LinearLayout giftLayout;// ��Ʒģ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail);
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		commentNumTxt=(TextView) findViewById(R.id.product_comment_num);
		stars = (RatingBar) findViewById(R.id.product_stars);// �����Ǽ�
		discountLayout = (LinearLayout) findViewById(R.id.product_discount_layout);// ������Ϣģ��
		giftLayout = (LinearLayout) findViewById(R.id.product_gift_layout);// ��Ʒģ��
		layoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		gesture = new GestureDetector(this);
		viewFlipper = (MyViewFlipper) findViewById(R.id.product_viewflipper);
		viewFlipper.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gesture.onTouchEvent(event);
			}
		});
		imgNum = (TextView) findViewById(R.id.product_imgnum);
		storePriceTxt = (TextView) findViewById(R.id.product_store_pricetxt);
		secKillTime = (TextView) findViewById(R.id.product_seckill_outtime);
		viewWidth = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		viewHeight = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		pNum = (TextView) findViewById(R.id.product_num_txt);
		progressBar = new ProgressDialog(this);// ��ʼ��������
		webView = (WebView) findViewById(R.id.product_webview);// ��ʼ����Ʒ������ҳ����
		
		name = (TextView) findViewById(R.id.product_name);// ��Ʒ����
		referencePrice = (TextView) findViewById(R.id.product_reference_price);// ��Ʒ�۸�
		referencePrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // �м�Ӻ���
		storePrice = (TextView) findViewById(R.id.product_store_price);// ��Ʒ�����۸�
		discountTxt = (TextView) findViewById(R.id.product_discount);// ��Ʒ�ۿ�
		attributeLayout = (LinearLayout) findViewById(R.id.product_attribute_layout);// ��Ʒ����
		backBtn = (LinearLayout) findViewById(R.id.product_detail_backbtn);// ���˰�ť
		backBtn.setOnClickListener(this);
		addImg = (ImageView) findViewById(R.id.product_num_add);// ����������ť
		addImg.setOnClickListener(this);
		subImg = (ImageView) findViewById(R.id.product_num_sub);// ����������ť
		subImg.setOnClickListener(this);
		call = (LinearLayout) findViewById(R.id.product_call);// ��ϵ�ͷ���ť
		call.setOnClickListener(this);
		comment = (LinearLayout) findViewById(R.id.product_comment);// �鿴���۰�ť
		comment.setOnClickListener(this);
		buyNow = (Button) findViewById(R.id.product_buynow);// ��������ť
		buyNow.setOnClickListener(this);
		addShopcart = (Button) findViewById(R.id.product_add_shopcart);// ���빺�ﳵ��ť
		addShopcart.setOnClickListener(this);
		resources = ProductDetail.this.getResources();

		secKillHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				// ȡ�����µ�ʱ��
				long time = bundle.getLong("time");
				// �����̷߳��صļ�����˵��µ�ʱ���ַ���
				String timeString = bundle.getString("timeString");
				if (time > 0)
					secKillTime.setText(timeString);
				else {
					// ������ɱ��Ʒ����ҳ��ĵ���ʱʱ��
					ChangeTime.secKillTime = -1;
					secKillTime.setText("��ɱ�ѽ���");
				}
			}
		};
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// ������ɱ��Ʒ����ҳ��ĵ���ʱʱ��
		ChangeTime.secKillTime = -1;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (MyApplication.goToOrder) {
			MyApplication.goToOrder = false;
			this.finish();
		}
	}

	private void initData() {
		// ��ȡ����������ƷID�������Ϊ�����ȡ��Ʒ����ϸ��Ϣ
		Intent intent = getIntent();
		productId = intent.getStringExtra("productId");
		skid = intent.getStringExtra("skid");
		Log.i(MyApplication.TAG, "skid-->" + skid);
		if (skid == null || skid.equals("null"))
			sendData(NetworkAction.��Ʒ����);
		else {
			buyType = "2";
			sendData(NetworkAction.��ɱ��Ʒ����);
		}

		sendData(NetworkAction.�����б�);
		//��ȡ��Ʒ������ҳ����ַ
		productUrl=productUrl+"?act=detail&sid="+MyApplication.sid+"&product_id="+productId;
//		Log.i(MyApplication.TAG, "url-->"+productUrl);
		initWebView();
	}

	private void sendData(final NetworkAction request) {

		String url="";
		HashMap<String, String> paramter = new HashMap<String, String>();
		if (request.equals(NetworkAction.��Ʒ����)) {
			url = Url.URL_DETAILS;
			paramter.put("act", "ProductInto");
			paramter.put("product_id", productId);
			paramter.put("sid", MyApplication.sid);
		} else if (request.equals(NetworkAction.��ɱ��Ʒ����)) {
			url = Url.URL_SECKILL;
			paramter.put("act", "seckill");
			paramter.put("product_id", productId);
			paramter.put("sid", MyApplication.sid);
			paramter.put("SKID", skid);
		} else if (request.equals(NetworkAction.�����б�)) {
			url = Url.URL_ORDER;
			paramter.put("act", "comments_list");
			paramter.put("sid", MyApplication.sid);
			paramter.put("ProductID", productId);
			paramter.put("level", "0");
			paramter.put("page", "1");
			paramter.put("pagesize", "100000");
		}
		Log.i(MyApplication.TAG, request + MyApplication.getUrl(paramter, url));
		/*
		 * ���������������
		 */
		MyApplication.client.postWithURL(url, paramter,
				new Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						try {
							Log.i(MyApplication.TAG, request + "response-->"
									+ response.toString());
							int code = response.getInt("code");
							
							if (response.getInt("code") == 1) {
								
								if (request.equals(NetworkAction.��ɱ��Ʒ����)
										|| request.equals(NetworkAction.��Ʒ����)) {
									product = new Product();
									JSONObject normalInfo = response
											.getJSONObject("info");
									JSONArray imgList = null;
									if (request.equals(NetworkAction.��ɱ��Ʒ����)) {

										buyType = "2";
										// ��ȡ�ؼ���Ϣ����
										long startTime = response
												.getLong("date");// ��ȡ��ʼʱ���
										Log.i(MyApplication.TAG, request
												+ " startTime-->" + startTime);
										JSONObject info = response
												.getJSONObject("SecondKill");
										// ��ȡ����ʱ���
										long outTime = info
												.getLong("OutEndTime");
										// �õ�����ɱ��Ʒ����ʣ���ʱ��
										long time = outTime - startTime;
										// ��ʾ��ɱ��Ʒ����ʱ�ı���
										secKillTime.setVisibility(View.VISIBLE);
										secKillTime.setTextSize(15);
										// ��ʼˢ�µ���ʱ�ı���
										ChangeTime.secKillTime = time;
										name.setText(info.getString("SKName"));
										referencePrice.setText("��"+normalInfo
												.getString("ReferencePrice"));
										Log.i(MyApplication.TAG,"getStorePrice()->"+ info
												.getString("SKPrice"));										
										storePriceTxt.setText("��ɱ�ۣ�");
										storePrice.setText("��"+info
												.getString("SKPrice"));
										
										// ��ʼ��ͼƬ����
										imgList = info.getJSONArray("Images");

										// --------------����Ϊ��Ҫ���ݵ��¸�ҳ������ݼ��ϣ�����Ϊҳ����ʾ���ݼ���-------------

										product.setStorePrice(info
												.getString("SKPrice"));
										product.setSkID(skid);
										product.setInventory(info
												.getString("SKLeftNum"));// ���ÿ����
										product.setSKName(info
												.getString("SKName"));
										product.setSKPrice(info
												.getString("SKPrice"));
										//�����ɱ��Ʒ������ID
										try {
											String priceID=info.getString("PriceID");
											if(priceID.equals("null"))
												product.setPriceID("0");
											else
												product.setPriceID(priceID);
										} catch (Exception e) {
											product.setPriceID("0");
										}
									} else if (request
											.equals(NetworkAction.��Ʒ����)) {
										buyType = "1";
										String tempname=normalInfo
												.getString("SubProductName");
										if(tempname.equals(""))
											name.setText(normalInfo
													.getString("ProductName"));
										else
											name.setText(tempname);
										referencePrice.setText("��"+normalInfo
												.getString("ReferencePrice"));
										storePrice.setText("��"+normalInfo
												.getString("StorePrice"));
										// ��ʼ��ͼƬ����
										imgList = response
												.getJSONArray("ImgInfo");
										// ��ʾ������Ϣģ��
										discountLayout
												.setVisibility(View.VISIBLE);
										// �ۿ���Ϣ
										JSONArray discounts = response
												.getJSONArray("discount");
										if (discounts.length() > 0) {
											for (int i = 0; i < discounts
													.length(); i++) {
												JSONObject discountObject = discounts
														.getJSONObject(i);
												discount = discountObject
														.getString("Per");

												product.setDiscount(discountObject
														.getString("Per"));
												product.setDiscountCash(discountObject
														.getString("OffsetPrice"));
												discountTxt.setText(product
														.getDiscount()
														+ "��   �֣�"
														+ product
																.getDiscountCash());

											}
										}
										// ��̬����������Ϣ
										JSONArray coupons = response
												.getJSONArray("coupons");
									
										if (coupons.length() > 0) {
											// ��̬����������Ϣ
											for (int i = 0; i < coupons
													.length(); i++) {
												JSONObject couponObject = coupons
														.getJSONObject(i);
												Coupon coupon = new Coupon();
												coupon.setCouponID(couponObject
														.getString("CouponID"));
												coupon.setStoreID(couponObject
														.getString("StoreID"));
												coupon.setProductID(couponObject
														.getString("ProductID"));
												coupon.setPrice(couponObject
														.getString("Price"));
												coupon.setStart_time(couponObject
														.getString("StartTime"));
												coupon.setEnd_time(couponObject
														.getString("EndTime"));
												coupon.setPriceLine(couponObject
														.getString("PriceLine"));
												product.addCoupon(coupon);
												// �¼�һ��
												LinearLayout layout = new LinearLayout(
														discountLayout
																.getContext());
												LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
														LayoutParams.MATCH_PARENT,
														LayoutParams.WRAP_CONTENT);
												layoutParams.setMargins(0, 6,
														0, 0);
												if(i==0)
												{
												// ������Ϣ������
												TextView t1 = new TextView(
														layout.getContext());
												t1.setText("������Ϣ��     ");
												t1.setVisibility(View.INVISIBLE);
												layout.addView(t1);
												// ��ɫ��������������
												TextView t2 = new TextView(
														layout.getContext());
												t2.setText("����");
												t2.setTextColor(MyApplication.resources
														.getColor(R.color.white));
												t2.setBackgroundColor(MyApplication.resources
														.getColor(R.color.red));
												t2.setTextSize(15);
												t2.setPadding(8, 0, 8, 0);
												layout.addView(t2);
												// ��Ч��
												String time = couponObject
														.getString("StartTime")
														.substring(5, 7)
														+ "��"
														+ couponObject
																.getString(
																		"StartTime")
																.substring(8,
																		10)
														+ "�� ~ "
														+ couponObject
																.getString(
																		"EndTime")
																.substring(5, 7)
														+ "��"
														+ couponObject
																.getString(
																		"EndTime")
																.substring(8,
																		10)
														+ "��";
												Log.i(MyApplication.TAG,
														"time-->" + time);
												TextView t3 = new TextView(
														layout.getContext());
												t3.setText(time);
												t3.setTextColor(MyApplication.resources
														.getColor(R.color.red));
												t3.setTextSize(15);
												layout.addView(t3);
												}
												LinearLayout layoutNext = new LinearLayout(
														discountLayout
																.getContext());
												// ������Ϣ������
												TextView t4 = new TextView(
														layoutNext.getContext());
												t4.setText("������Ϣ��     ");
												t4.setVisibility(View.INVISIBLE);
												layoutNext.addView(t4);
												
												// ��ʾ������
												TextView t5 = new TextView(
														layoutNext.getContext());
												t5.setText("����"
														+ couponObject
																.getString("PriceLine")
														+ "Ԫ����"
														+ couponObject
																.getString("Price")
														+ "Ԫ�Ż�ȯ");
												t5.setTextColor(MyApplication.resources
														.getColor(R.color.red));
												t5.setTextSize(13);
												layoutNext.addView(t5);
												discountLayout.addView(layout,
														layoutParams);
												discountLayout.addView(
														layoutNext,
														layoutParams);
											}
										}

										// ��Ʒ����
										JSONArray attributes = response
												.getJSONArray("PriceList");
										Log.i(MyApplication.TAG, "attributes-->"
												+ attributes);
										if (attributes.length() >= 1) {
											attributeLayout.setVisibility(View.VISIBLE);
											LinearLayout layout = new LinearLayout(
													attributeLayout.getContext());
											RadioGroup attributeGroup = new RadioGroup(
													layout.getContext());
											attributeGroup.setOrientation(1);
											
											for (int i = 0; i < attributes.length(); i++) {
												RadioGroup.LayoutParams layoutParams1 = new RadioGroup.LayoutParams(
														LayoutParams.WRAP_CONTENT,
														LayoutParams.WRAP_CONTENT);
												layoutParams1.setMargins(0, 7, 0, 0);
												
												// �������Ե�ѡ��ť
//												RadioButton attribute = new RadioButton(
//														attributeGroup.getContext());
												RadioButton attribute =  (RadioButton)LayoutInflater.from(attributeGroup.getContext()).inflate(R.layout.radio_item, null);
												// ��ʾ��������
												JSONObject item = attributes
														.getJSONObject(i);
												// ��ȡ�����Ե���Ʒ����
												int num = item.getInt("PriceNum");
												// �������Ʒ�������㣬����ѡ��
												if (num <= 0)
													attribute.setEnabled(false);
												Log.i(MyApplication.TAG, "num-->"
														+ num);
												attribute.setText(item
														.getString("PriceName"));
												attribute.setTextColor(resources
														.getColor(R.color.gray));
												attribute.setId(i);
												attribute.setTextSize(14);
												attribute.setTag(R.id.tag_first,
														item.getString("Price"));
												attribute.setTag(R.id.tag_second,
														item.getString("PriceNum"));
												attribute.setTag(R.id.tag_three,
														item.getString("PriceID"));
												attributeGroup.addView(attribute,layoutParams1);
												if (i == 0) {
													//�����Ʒ��priceID
													product.setPriceID(item.getString("PriceID"));
													// ��������ı���
													TextView attributeTxt = new TextView(
															layout.getContext());
													attributeTxt.setText("��Ʒ���ԣ�  ");
													attributeTxt.setTextColor(resources
															.getColor(R.color.gray));
													attributeTxt.setPadding(0, 15,
															0, 0);
													layout.addView(attributeTxt);
													// Ĭ��ѡ����Ʒ���Եĵ�һ�����ԣ������ø����Եļ۸���ۿ���Ϣ
													if (buyType.equals("1")
															&& discount != null) {
														attribute.setChecked(true);
														product.setAttribute(attribute
																.getText()
																.toString());
														String Price = attribute
																.getTag(R.id.tag_first)
																.toString();
														String newPrice = String.valueOf(Double
																.valueOf(Price)
																* (Double
																		.valueOf(discount) / 100));
														DecimalFormat df = new DecimalFormat(
																".00");
														String lessPrice = String.valueOf(df.format(Double
																.valueOf(Price)
																- Double.valueOf(newPrice)));
														discountTxt
																.setText(discount
																		+ "��   �֣�"
																		+ lessPrice);
														storePrice
																.setText("��"+newPrice);
														// ���øò�Ʒ��������Լ۸�
														product.setStorePrice(newPrice);
														product.setInventory(item
																.getString("PriceNum"));
													}
												}

											}
											attributeGroup
													.setOnCheckedChangeListener(new OnCheckedChangeListener() {

														@Override
														public void onCheckedChanged(
																RadioGroup group,
																int checkedId) {
															RadioButton btn = (RadioButton) group
																	.getChildAt(checkedId);
															String priceID= btn
																	.getTag(R.id.tag_three)
																	.toString();
															product.setPriceID(priceID);
															// ������ͨ��Ʒ��ʱ���Ҹ���Ʒ���ۿ�ʱʹ�������Եļ۸���ۿۼ������µļ۸���Żݼ�
															if (buyType.equals("1")
																	&& discount != null) {
																String Price = btn
																		.getTag(R.id.tag_first)
																		.toString();
																String newPrice = String.valueOf(Double
																		.valueOf(Price)
																		* (Double
																				.valueOf(discount) / 100));
																DecimalFormat df = new DecimalFormat(
																		".00");
																String lessPrice = String.valueOf(df.format(Double
																		.valueOf(Price)
																		- Double.valueOf(newPrice)));
																discountTxt
																		.setText(discount
																				+ "��   �֣�"
																				+ lessPrice);
																storePrice
																		.setText("��"+newPrice);
																product.setAttribute(btn
																		.getText()
																		.toString());
																product.setStorePrice(newPrice);
																product.setInventory(btn
																		.getTag(R.id.tag_second)
																		.toString());
															}
															//
														}
													});
											layout.addView(attributeGroup);
											attributeLayout.addView(layout);
										}
										else
											product.setPriceID("0");
										
										// --------------------------��ͨ��Ʒ����Ʒ��Ϣ���-----------------------------------
										product.setInventory(normalInfo
												.getString("ProductLeft"));// �����
										product.setStorePrice(normalInfo
												.getString("StorePrice"));
										if(coupons.length()==0&&discounts.length()==0)
											discountLayout.setVisibility(View.GONE);
									}

									// --------------------------��ͨ��Ʒ����ɱ��Ʒ��ͬ���Ե���Ϣ���---------------------------------
									product.setId(normalInfo
											.getString("ProductID"));// ��ƷID
									product.setFreight(normalInfo
											.getString("Freight"));// ��Ʒ�˷�
									product.setName(normalInfo
											.getString("ProductName"));// ��Ʒ����
									product.setBuy_type(buyType);
									product.setReferencePrice(normalInfo
											.getString("ReferencePrice"));
									product.setDate(String.valueOf(new Date()
											.getTime()));// ��¼���빺�ﳵ��ʱ��
									product.setNature(normalInfo
											.getString("Nature"));
									// ----------------------��ͬ����----------------------------------
									// ��Ʒ�����Ǽ�
									String commentStars = normalInfo
											.getString("CommentStars");
									Log.i(MyApplication.TAG, "commentStars-->"
											+ commentStars);
									stars.setRating(Float.valueOf(commentStars));
//									stars.setEnabled(false);
									// ͼƬ����
									for (int i = 0; i < imgList.length(); i++) {
										String m = Url.URL_IMGPATH
												+ ((JSONObject) imgList.get(i))
														.getString("FilePath");
										String imgPath = ((JSONObject) imgList
												.get(i)).getString("FilePath");
										LinearLayout ll = (LinearLayout) MyApplication.Inflater
												.inflate(R.layout.img_item,
														null);
										product.setImgPath(imgPath);
										NetworkImageView l = (NetworkImageView) ll
												.findViewById(R.id.product_photo);
										MyApplication.client
												.getImageForNetImageView(m, l,
														R.drawable.ic_launcher);
										viewFlipper.addView(ll, layoutParams);
									}
									photototalpage = imgList.length();
									imgNum.setText("" + photopage + "/"
											+ photototalpage);

									

									// ��Ʒ
									JSONArray gifts = response
											.getJSONArray("GiftList");
									if (gifts.length() >= 1) {

										LinearLayout layout = new LinearLayout(
												giftLayout.getContext());
										RadioGroup giftGroup = new RadioGroup(
												layout.getContext());
										giftGroup.setOrientation(1);

										for (int i = 0; i < gifts.length(); i++) {
											RadioGroup.LayoutParams layoutParams1 = new RadioGroup.LayoutParams(
													LayoutParams.WRAP_CONTENT,
													LayoutParams.WRAP_CONTENT);
											layoutParams1.setMargins(0, 7, 0, 0);
											// �������Ե�ѡ��ť
											RadioButton giftRB = (RadioButton)LayoutInflater.from(giftGroup.getContext()).inflate(R.layout.radio_item, null);
											// ��ʾ��������
											JSONObject item = gifts
													.getJSONObject(i);
											giftRB.setText(item
													.getString("GiftName"));
											giftRB.setTextColor(resources
													.getColor(R.color.gray));
											giftRB.setId(i);
											giftRB.setTextSize(12);
											// giftRB.setTag(item
											// .getString("Price"));
											giftGroup.addView(giftRB,layoutParams1);
											if (i == 0) {
												// ��������ı���
												TextView giftTxt = new TextView(
														layout.getContext());
												giftTxt.setText("��Ʒ��          ");
												giftTxt.setTextColor(resources
														.getColor(R.color.gray));
												giftTxt.setPadding(0, 15, 0, 0);
												giftRB.setChecked(true);
												layout.addView(giftTxt);
											}
										}
										layout.addView(giftGroup);
										giftLayout.addView(layout);
									}
									// ���û����Ʒ����ʾ��ģ��
									else
										giftLayout.setVisibility(View.GONE);
									// giftGroup
									// .setOnCheckedChangeListener(new
									// OnCheckedChangeListener() {
									//
									// @Override
									// public void onCheckedChanged(
									// RadioGroup group,
									// int checkedId) {
									// RadioButton btn = (RadioButton) group
									// .getChildAt(checkedId);
									// //
									// ������ͨ��Ʒ��ʱ���Ҹ���Ʒ���ۿ�ʱʹ�������Եļ۸���ۿۼ������µļ۸���Żݼ�
									// if (buyType.equals("1")
									// && discount != null) {
									// String Price = (String) btn
									// .getTag();
									// String newPrice = String.valueOf(Double
									// .valueOf(Price)
									// * (Double
									// .valueOf(discount) / 100));
									// DecimalFormat df = new DecimalFormat(
									// ".00");
									// String lessPrice =
									// String.valueOf(df.format(Double
									// .valueOf(Price)
									// - Double.valueOf(newPrice)));
									// discountTxt
									// .setText(discount
									// + "��   �֣�"
									// + lessPrice);
									// storePrice
									// .setText(newPrice);
									// }
									// //
									// }
									// });
									// layout.addView(attributeGroup);
									// attributeLayout.addView(layout);

								}
								else if (request.equals(NetworkAction.�����б�)) {
									commentNumTxt.setText("("+response.getString("total_number")+")");
								}
								
							} else {
								Toast.makeText(ProductDetail.this, request
										+ ErrorMsg.getErrorMsg(request, code),
										2000).show();
							}

						} catch (JSONException e) {
							Log.i(MyApplication.TAG, request
									+ "JSONException-->" + e.getMessage());
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i(MyApplication.TAG, request + "onErrorResponse-->"
								+ error.getMessage());
					}
				});
	}

	/*
	 * ��ʼ��webview
	 */
	protected void initWebView() {
		// ��ƽ�����
		progressBar = ProgressDialog.show(ProductDetail.this, null,
				"���ڼ�����Ʒ���飬���Ժ�");
		// ���WebView���
		webView.getSettings().setJavaScriptEnabled(true);
		WebSettings settings = webView.getSettings(); 
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
//		webView.getSettings().setSupportZoom(true);
//		webView.getSettings().setBuiltInZoomControls(true);
//		Log.i(MyApplication.TAG, "url-->"+url);
		webView.loadUrl(productUrl);
		alertDialog = new AlertDialog.Builder(this).create();
		// ������ͼ�ͻ���
		webView.setWebViewClient(new MyWebViewClient());
	}

	/*
	 * ���ü�����ҳʱ��ʾ���������޷�����ʱ����������ʾ
	 */
	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			progressBar.show();
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (progressBar.isShowing()) {
				progressBar.dismiss();
			}
		}

		
		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Toast.makeText(ProductDetail.this, "��ҳ���س���", Toast.LENGTH_LONG);
			alertDialog.setTitle("ERROR");
			alertDialog.setMessage(description);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
			alertDialog.show();
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.product_detail_backbtn:// ���˰�ť
			finish();
			break;

		case R.id.product_num_add:// ����������ť
			num = pNum.getText().toString();
			num = String.valueOf(Integer.valueOf(num) + 1);
			pNum.setText(num);
			product.setNum(num);// ������Ʒ������Ϊ��ǰѡ�������

			break;
		case R.id.product_num_sub:// ����������ť
			if (Integer.valueOf(num) > 1) {
				num = pNum.getText().toString();
				num = String.valueOf(Integer.valueOf(num) - 1);
				pNum.setText(num);
				product.setNum(num);// ������Ʒ������Ϊ��ǰѡ�������
				// double pNumSub=Double.valueOf(product.getNum());
				// double priceSub=Double.valueOf(product.getStorePrice());
				// product.setTotalPrice(String.valueOf(pNumSub*priceSub));//���ø���Ʒ���ܼ�
			}
			break;
		case R.id.product_call:// ��ϵ�ͷ���ť
			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			builder.setMessage("\t\tȷ������ͷ��绰?")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// �����ȷ�ϡ���Ĳ���
									Intent intent = new Intent(
											Intent.ACTION_CALL, Uri
													.parse("tel:"
															+ "4000838310"));
									ProductDetail.this.startActivity(intent);
									dialog.cancel();
								}
							})
					.setNegativeButton("����",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
			 CustomDialog alert = builder.create();
			    alert.show();
			    
			break;
		case R.id.product_comment:// �鿴���۰�ť
			Intent commentIntent=new Intent();
			commentIntent.setClass(this, CommentList.class);
			commentIntent.putExtra("productID", product.getId());
			startActivity(commentIntent);
			break;
		case R.id.product_buynow:// ��������ť
			// product.setBuy_type(buyType);// ��¼�������Ʒ����
			product.setNum(num);// ������Ʒ������Ϊ��ǰѡ�������
			double pNumSub = Double.valueOf(product.getNum());
			double priceSub = Double.valueOf(product.getStorePrice());
			product.setTotalPrice(String.valueOf(pNumSub * priceSub));// ���ø���Ʒ���ܼ�
			if (MyApplication.loginStat) {
				intent = new Intent(this, SubmitOrder.class);
				ArrayList<Object> plist = new ArrayList<Object>();
				plist.add(product);
				intent.putExtra("products", plist);
			} else {
				intent = new Intent(this, PersonLogin.class);
			}
			startActivity(intent);
			break;
		case R.id.product_add_shopcart:// ���빺�ﳵ��ť
			// product.setBuy_type(buyType);// ��¼�������Ʒ����
			product.setNum(num);// ������Ʒ������Ϊ��ǰѡ�������
			double p1NumSub = Double.valueOf(product.getNum());
			Log.i(MyApplication.TAG,"product.getStorePrice()->"+ product.getStorePrice());
			double p1riceSub = Double.valueOf(product.getStorePrice());
			product.setTotalPrice(String.valueOf(p1NumSub * p1riceSub));// ���ø���Ʒ���ܼ�
			if (MyApplication.loginStat) {
				MyApplication.shopCartManager.add(product);

				CustomDialog.Builder builder1 = new CustomDialog.Builder(this);
				builder1.setMessage("�ɹ����빺�ﳵ��")
			      .setPositiveButton("�ٹ��",
			        new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog,
			           int id) {
			        	 dialog.cancel();
			         }
			        })
			      .setNegativeButton("ȥ���ﳵ",
			        new DialogInterface.OnClickListener() {
			         public void onClick(DialogInterface dialog,
			           int id) {
			        	 MenuBottom.tabHost.setCurrentTab(3);
			 			MenuBottom.radioGroup.check(R.id.main_tab_shopcart);
			 			ProductDetail.this.finish();
			 			dialog.cancel();
			         }
			        });
			    CustomDialog alert1 = builder1.create();
			    alert1.show();
				ArrayList<Object> products = null;
				try {
					products = MyApplication.shopCartManager.readShopCart();
				} catch (StreamCorruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				intent = new Intent(this, PersonLogin.class);
				startActivity(intent);
			}

			break;
		case 0:// ��̬��ӵ���Ʒ���԰�ť�ĵ���¼�
				// Toast.makeText(this, v.getTag().toString(), 2000).show();
			break;
		default:
			break;
		}

	}

	// ����ͼƬ�ķ���
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() > e2.getX()) {
			if (photopage < photototalpage) {
				viewFlipper.setInAnimation(ProductDetail.this,
						R.anim.view_in_from_right);
				viewFlipper.setOutAnimation(ProductDetail.this,
						R.anim.view_out_to_left);
				viewFlipper.showNext();
				photopage = photopage + 1;
				imgNum.setText("" + photopage + "/" + photototalpage);
			}
		} else {
			if (photopage == 1) {

			} else {
				viewFlipper.setInAnimation(ProductDetail.this,
						R.anim.view_in_from_left);
				viewFlipper.setOutAnimation(ProductDetail.this,
						R.anim.view_out_to_right);
				viewFlipper.showPrevious();
				photopage = photopage - 1;
				imgNum.setText("" + photopage + "/" + photototalpage);
			}
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
