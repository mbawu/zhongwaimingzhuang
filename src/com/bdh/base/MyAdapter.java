package com.bdh.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.android.volley.toolbox.NetworkImageView;
import com.bdh.activity.person.Person;
import com.bdh.activity.person.PersonAddress;
import com.bdh.activity.person.PersonCoupon;
import com.bdh.activity.person.PersonOrder;
import com.bdh.activity.product.OrderEvaluate;
import com.bdh.activity.product.ProductDetail;
import com.bdh.activity.product.ProductList;
import com.bdh.activity.product.ProductListShow;
import com.bdh.activity.product.ShopCart;
import com.bdh.model.Address;
import com.bdh.model.Category;
import com.bdh.model.Comment;
import com.bdh.model.Coupon;
import com.bdh.model.MyMessage;
import com.bdh.model.NetworkAction;
import com.bdh.model.Order;
import com.bdh.model.Product;
import com.bdh.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter implements
		android.view.View.OnClickListener, OnCheckedChangeListener {

	private Object object;
	private ArrayList<Object> data;
	private NetworkAction request;
	private int orderTypeTemp;
	private int orderComment;
	/**
	 * 
	 * @param object
	 *            ��Ҫ��ʾ��activity
	 * @param request
	 *            ��Ҫ��ʾ����������
	 * @param data
	 *            ��Ҫ��ʾ�����ݼ���
	 */
	public MyAdapter(Object object, NetworkAction request,
			ArrayList<Object> data) {
		// MyApplication.printLog("MyAdapter-->classname", module);
		this.object = object;
		this.data = data;
		this.request = request;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// �ó�ÿ��product��ID��ֵ��ÿ��item
		if (request.equals(NetworkAction.������Ʒ)
				|| request.equals(NetworkAction.��ȡ������Ʒ)
				|| request.equals(NetworkAction.������Ʒ)
				|| request.equals(NetworkAction.��ɱ��Ʒ)) {
			Product product = (Product) data.get(position);
			long productId = Long.valueOf(product.getId());
			return productId;
		} else if (request.equals(NetworkAction.һ������)) {
			Category category = (Category) data.get(position);
			long parentId = Long.valueOf(category.getCategory_id());
			return parentId;
		} else if (request.equals(NetworkAction.��������)) {
			Category category = (Category) data.get(position);
			long parentId = Long.valueOf(category.getCategory_id());
			return parentId;
		}

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// if(module.equals("home_hot"))
			// convertView=MyApplication.Inflater.inflate(R.layout.home_hot_item,
			// null);
			if (request.equals(NetworkAction.��ȡ�ջ���ַ�б�))
				convertView = MyApplication.Inflater.inflate(
						R.layout.person_address_item, null);
			else if (request.equals(NetworkAction.������Ʒ)
					|| request.equals(NetworkAction.��ȡ������Ʒ)
					|| request.equals(NetworkAction.������Ʒ)
					|| request.equals(NetworkAction.��ɱ��Ʒ))
				convertView = MyApplication.Inflater.inflate(
						R.layout.home_hot_item, null);
			else if (request.equals(NetworkAction.�ҵ���Ϣ))
				convertView = MyApplication.Inflater.inflate(
						R.layout.person_more_message_item, null);
			else if (request.equals(NetworkAction.һ������))
				convertView = MyApplication.Inflater.inflate(
						R.layout.productlist_first_item, null);
			else if (request.equals(NetworkAction.��������))
				convertView = MyApplication.Inflater.inflate(
						R.layout.productlist_second_item, null);
			else if (request.equals(NetworkAction.��������))
				convertView = MyApplication.Inflater.inflate(
						R.layout.productlist_third_item, null);
			else if (request.equals(NetworkAction.�ύ����)
					|| request.equals(NetworkAction.��������))
				convertView = MyApplication.Inflater.inflate(
						R.layout.submit_product_item, null);
			else if (request.equals(NetworkAction.���ﳵ))
				convertView = MyApplication.Inflater.inflate(
						R.layout.shopcart_item, null);
			else if (request.equals(NetworkAction.�ҵ��Ż�ȯ))
				convertView = MyApplication.Inflater.inflate(
						R.layout.coupon_item, null);
			else if (request.equals(NetworkAction.�����б�))
				convertView = MyApplication.Inflater.inflate(
						R.layout.comment_item, null);
			else if (request.equals(NetworkAction.�����б�))
				convertView = MyApplication.Inflater.inflate(
						R.layout.person_order_item, null);
			else if (request.equals(NetworkAction.���۶���))
				convertView = MyApplication.Inflater.inflate(
						R.layout.orderevaluate_item, null);
			
			
		}
		// �������ɱ��Ʒ�����Ѿ���view�������ػ�ֱ�ӷ��ص�ǰview��Ϊ�˲��õ���ʱ�߳��ظ����view
		else if (convertView != null && request.equals(NetworkAction.��ɱ��Ʒ)) {
			return convertView;
		}

		if (request.equals(NetworkAction.��ȡ�ջ���ַ�б�))// �����ջ���ַitem����
		{
			TextView name = (TextView) convertView
					.findViewById(R.id.person_address_item_name);
			TextView phone = (TextView) convertView
					.findViewById(R.id.person_address_item_phone);
			TextView street = (TextView) convertView
					.findViewById(R.id.person_address_item_street);
			TextView edite = (TextView) convertView
					.findViewById(R.id.person_address_item_edite);
			edite.setOnClickListener(this);
			edite.setTag(data.get(position));
			TextView delete = (TextView) convertView
					.findViewById(R.id.person_address_item_delete);
			delete.setOnClickListener(this);
			Address address = (Address) data.get(position);
			name.setText(address.getRealname());
			phone.setText("(" + address.getMobile() + ")");
			street.setText(address.getStreet());
			delete.setTag(address.getAddressID());

			// �ж��Ƿ�ΪĬ�ϵ�ַ������Ӧ�ĵ���
			FrameLayout defaultLayout = (FrameLayout) convertView
					.findViewById(R.id.person_address_item_default_layout);// ��ΪĬ�ϵ�ַ������
			defaultLayout.setTag(address);
			defaultLayout.setOnClickListener(this);// ע����ΪĬ�ϵ�ַ�ĵ���¼�

			ImageView defaultImg = (ImageView) convertView
					.findViewById(R.id.person_address_item_default_img);// ��ΪĬ�ϵ�ַ��ͼƬ
			TextView defaultTxt = (TextView) convertView
					.findViewById(R.id.person_address_item_default_txt);// ��ΪĬ�ϵ�ַ������
			if (address.getTag().equals("1"))// �����Ĭ�ϵ�ַ�����
			{
				defaultImg.setVisibility(View.VISIBLE);// ��ʾͼ��
				defaultTxt.setTextColor(MyApplication.resources
						.getColor(R.color.gray));
				defaultTxt.setText("Ĭ�ϵ�ַ");
				defaultLayout.setOnClickListener(null);
			} else// �������Ĭ�ϵ�ַ�����
			{
				defaultImg.setVisibility(View.GONE);// ����ͼ��
				defaultTxt.setTextColor(MyApplication.resources
						.getColor(R.color.blue));
				defaultTxt.setText("��ΪĬ�ϵ�ַ");
			}

		} else if (request.equals(NetworkAction.������Ʒ)
				|| request.equals(NetworkAction.��ȡ������Ʒ)
				|| request.equals(NetworkAction.������Ʒ)) {
			Product product = (Product) data.get(position);
			NetworkImageView img = (NetworkImageView) convertView
					.findViewById(R.id.home_hot_img);
			TextView nameTxt = (TextView) convertView
					.findViewById(R.id.home_hot_name);
			TextView priceTxt = (TextView) convertView
					.findViewById(R.id.home_hot_price);
			nameTxt.setText(product.getName());
			priceTxt.setText("�� " + product.getStorePrice());
			// Log.i(MyApplication.TAG,"getImgPath-->"+ product.getImgPath());
			MyApplication.client.getImageForNetImageView(product.getImgPath(),
					img, R.drawable.ic_launcher);
		} else if (request.equals(NetworkAction.�ҵ���Ϣ)) {
			TextView subjectTxt = (TextView) convertView
					.findViewById(R.id.person_msg_subject);
			TextView creatTimeTxt = (TextView) convertView
					.findViewById(R.id.person_msg_creattime);
			TextView contentTxt = (TextView) convertView
					.findViewById(R.id.person_msg_content);
			CheckBox deleteBox = (CheckBox) convertView
					.findViewById(R.id.message_delete);

			MyMessage msg = (MyMessage) data.get(position);
			subjectTxt.setText(msg.getSubject());
			creatTimeTxt.setText(msg.getCreatTime());
			contentTxt.setText(msg.getContent());
			deleteBox.setTag(msg);
			deleteBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					MyMessage msg = (MyMessage) buttonView.getTag();
					msg.setChecked(isChecked);
				}
			});
			if (msg.isShowCheckBox()) {
				deleteBox.setVisibility(View.VISIBLE);
				if (msg.isChecked())
					deleteBox.setChecked(true);
				else
					deleteBox.setChecked(false);
			} else
				deleteBox.setVisibility(View.GONE);
		} else if (request.equals(NetworkAction.һ������)) {
			Category category = (Category) data.get(position);
			TextView firstTxt = (TextView) convertView
					.findViewById(R.id.productlist_firsttxt);
			firstTxt.setText(category.getCategory_name());
		} else if (request.equals(NetworkAction.��������)) {
			Category category = (Category) data.get(position);
			TextView secTxt = (TextView) convertView
					.findViewById(R.id.productlist_sectxt);// ���������ı���
			secTxt.setText(category.getCategory_name());

			// ������������¼�
			RelativeLayout secondItem = (RelativeLayout) convertView
					.findViewById(R.id.productlist_second_itemlayout);
			secondItem.setOnClickListener(this);
			// �ҵ����������б�
			ListView thirdListView = (ListView) convertView
					.findViewById(R.id.productlist_third_listview);// 3���б�listview
			// ��ȡ���е��������༯��
			ArrayList<Object> thridList = ((ProductList) object).thirdLevel;
			// ��ʱ��Ÿö�������ID����Ӧ����������ļ���
			ArrayList<Object> temp = new ArrayList<Object>();
			for (int i = 0; i < thridList.size(); i++) {
				Category thirdCategory = (Category) thridList.get(i);
				if (thirdCategory.getParent_catid().equals(
						category.getCategory_id()))
					temp.add(thirdCategory);
			}
			// �ѻ�ȡ���ĸö��������µ���������װ�ص�����������
			MyAdapter adapter = new MyAdapter(object, NetworkAction.��������, temp);
			thirdListView.setAdapter(adapter);
			// �����б�߶ȣ�ȫ����ʾ�������࣬��Ҫ������
			setListViewHeight(thirdListView);
			secondItem.setTag(thirdListView);
		} else if (request.equals(NetworkAction.��������)) {
			Category category = (Category) data.get(position);
			TextView thirdTxt = (TextView) convertView
					.findViewById(R.id.productlist_thirdtxt);
			thirdTxt.setText(category.getCategory_name());
			// ����������Ŀ
			RelativeLayout thirdLayout = (RelativeLayout) convertView
					.findViewById(R.id.productlist_third_itemlayout);
			thirdLayout.setOnClickListener(this);
			thirdLayout.setTag(category);
		} else if (request.equals(NetworkAction.��ɱ��Ʒ)) {
			Product product = (Product) data.get(position);
			NetworkImageView img = (NetworkImageView) convertView
					.findViewById(R.id.home_hot_img);
			TextView nameTxt = (TextView) convertView
					.findViewById(R.id.home_hot_name);
			TextView priceTxt = (TextView) convertView
					.findViewById(R.id.home_hot_price);
			final TextView outTimeTxt = (TextView) convertView
					.findViewById(R.id.home_seckill_outtime);
			outTimeTxt.setVisibility(View.VISIBLE);

			long outTime = Integer.valueOf(product.getOutEndTime());
			long time = Integer.valueOf(product.getTime());
			long endTime = outTime - time;
			String day = String.valueOf(endTime / 60 / 60 / 24);
			String hour = String.valueOf(endTime / 60 / 60 % 24);
			String min = String.valueOf(endTime / 60 % 60);
			String sec = String.valueOf(endTime % 60);
			String timeString = day + "��" + hour + "ʱ" + min + "��" + sec + "��";
			outTimeTxt.setText(timeString);
			outTimeTxt.setTag(convertView);
			ChangeTime.timeList.add(endTime);
			ChangeTime.txtViewList.add(outTimeTxt);
			nameTxt.setText(product.getName());
			priceTxt.setText("�� " + product.getSKPrice());
			Log.i(MyApplication.TAG, "getView-->adapter");
			MyApplication.client.getImageForNetImageView(product.getImgPath(),
					img, R.drawable.ic_launcher);
		} else if (request.equals(NetworkAction.�ύ����)
				|| request.equals(NetworkAction.��������)) {
			Product product = (Product) data.get(position);
			NetworkImageView img = (NetworkImageView) convertView
					.findViewById(R.id.submit_product_photo);

			TextView nameTxt = (TextView) convertView
					.findViewById(R.id.submit_product_name);
			TextView priceTxt = (TextView) convertView
					.findViewById(R.id.submit_product_price);
			TextView numTxt = (TextView) convertView
					.findViewById(R.id.submit_product_num);
			// ���ж�����ɱ��Ʒ����������Ʒ�����ݲ�ͬ����Ʒ��ʾ��ͬ����Ϣ
			// �������ͣ�1��������2��ɱ
			if (product.getBuy_type().equals("1")) {
				nameTxt.setText(product.getName());
				priceTxt.setText("��" + product.getStorePrice());
			} else {
				nameTxt.setText(product.getSKName());
				priceTxt.setText("��" + product.getSKPrice());
			}
			numTxt.setText(product.getNum());
			MyApplication.client.getImageForNetImageView(
					product.getImgs().get(0), img, R.drawable.ic_launcher);
		} else if (request.equals(NetworkAction.���ﳵ)) {
			Product product = (Product) data.get(position);
			// setListViewHeight(((ShopCart)object).listView);
			// Toast.makeText((Context) object, product.getName(), 2000).show();
			NetworkImageView img = (NetworkImageView) convertView
					.findViewById(R.id.shopcart_product_photo);
			MyApplication.client.getImageForNetImageView(
					product.getImgs().get(0), img, R.drawable.ic_launcher);
			TextView nameTxt = (TextView) convertView
					.findViewById(R.id.shopcart_product_name);
			if (product.getBuy_type().equals("2"))
				nameTxt.setText(product.getSKName());
			else
				nameTxt.setText(product.getName());
			TextView priceNameTxt = (TextView) convertView
					.findViewById(R.id.shopcart_product_pricename);
			if (product.getBuy_type().equals("2"))
				priceNameTxt.setText("��ɱ�ۣ�");
			else
				priceNameTxt.setText("�����ۣ�");
			TextView priceTxt = (TextView) convertView
					.findViewById(R.id.shopcart_product_price);
			priceTxt.setText(product.getStorePrice());
			TextView numTxt = (TextView) convertView
					.findViewById(R.id.shopcart_num_txt);
			numTxt.setText(product.getNum());
			numTxt.setTag(product);
			ImageView subImg = (ImageView) convertView
					.findViewById(R.id.shopcart_num_sub);
			ImageView addImg = (ImageView) convertView
					.findViewById(R.id.shopcart_num_add);
			subImg.setTag(numTxt);
			addImg.setTag(numTxt);
			subImg.setOnClickListener(this);
			addImg.setOnClickListener(this);
			CheckBox checkBox = (CheckBox) convertView
					.findViewById(R.id.shopcart_delete);
			checkBox.setTag(product);
			checkBox.setChecked(product.isChecked());
			checkBox.setOnCheckedChangeListener(this);
		} else if (request.equals(NetworkAction.�ҵ��Ż�ȯ)) {
			Coupon coupon = (Coupon) data.get(position);
			TextView priceTxt = (TextView) convertView
					.findViewById(R.id.coupon_item_price);
			String price = coupon.getPrice();
			int end = price.indexOf(".");
			price = price.substring(0, end);
			priceTxt.setText(price + "Ԫ�Ż�ȯ");
			if ((position + 1) % 3 == 1)
				priceTxt.setBackgroundDrawable(MyApplication.resources
						.getDrawable(R.drawable.coupon_bg_blue));
			else if ((position + 1) % 3 == 2)
				priceTxt.setBackgroundDrawable(MyApplication.resources
						.getDrawable(R.drawable.coupon_bg_green));
			else if ((position + 1) % 3 == 0)
				priceTxt.setBackgroundDrawable(MyApplication.resources
						.getDrawable(R.drawable.coupon_bg_yellow));
			TextView nameTxt = (TextView) convertView
					.findViewById(R.id.coupon_item_name);
			nameTxt.setText(coupon.getProductName());
			TextView priceLineTxt = (TextView) convertView
					.findViewById(R.id.coupon_item_priceline);
			priceLineTxt.setText("��������" + coupon.getPriceLine() + "(�����˷�)");
			TextView dateTxt = (TextView) convertView
					.findViewById(R.id.coupon_item_date);
			dateTxt.setText("��Ч�ڣ�" + coupon.getEnd_time());

			TextView useBtn = (TextView) convertView
					.findViewById(R.id.coupon_item_use);
			useBtn.setTag(coupon);
			if (coupon.getValidity().equals("1")) {
				useBtn.setText("����ʹ��");
				useBtn.setOnClickListener(this);
			} else if (coupon.getValidity().equals("2")) {
				useBtn.setText("�ѹ���");
				useBtn.setBackgroundColor(MyApplication.resources
						.getColor(R.color.darkgray));
			}
		} else if (request.equals(NetworkAction.�����б�)) {
			Comment comment = (Comment) data.get(position);
			TextView nameTxt = (TextView) convertView
					.findViewById(R.id.comment_name);
			RatingBar star = (RatingBar) convertView
					.findViewById(R.id.comment_stars);
			TextView contentTxt = (TextView) convertView
					.findViewById(R.id.comment_content);
			TextView dateTxt = (TextView) convertView
					.findViewById(R.id.comment_date);
			nameTxt.setText(comment.getUsername());
			star.setRating(Float.valueOf(comment.getComment_star()));
			contentTxt.setText(comment.getComment_content());
			dateTxt.setText("����ʱ�� " + comment.getCreatetime());
		} else if (request.equals(NetworkAction.�����б�)) {
			Order order = (Order) data.get(position);
			TextView orderCodeTxt = (TextView) convertView
					.findViewById(R.id.order_code);
			TextView orderDateTxt = (TextView) convertView
					.findViewById(R.id.order_date);
			NetworkImageView img = (NetworkImageView) convertView
					.findViewById(R.id.order_photo);
			TextView orderSubjectTxt = (TextView) convertView
					.findViewById(R.id.order_subject);
			TextView orderNumTxt = (TextView) convertView
					.findViewById(R.id.order_num);
			TextView orderTotalpriceTxt = (TextView) convertView
					.findViewById(R.id.order_totalprice);
			// //�鿴������ť
			// TextView orderViewTxt= (TextView) convertView
			// .findViewById(R.id.order_view);
			// //ȡ��������ť
			// TextView orderCancelTxt= (TextView) convertView
			// .findViewById(R.id.order_cancel);

			// ��ߵİ�ť
			TextView leftBtn = (TextView) convertView
					.findViewById(R.id.order_left_btn);
			// �ұߵİ�ť
			TextView rightBtn = (TextView) convertView
					.findViewById(R.id.order_right_btn);

			// ��ťģ��
			FrameLayout btnLayout = (FrameLayout) convertView
					.findViewById(R.id.order_btn_layout);
			// ������
			orderCodeTxt.setText("�����ţ�" + order.getOrderCode());
			// �µ�ʱ��
			orderDateTxt.setText("����ʱ�䣺" + order.getOutCreateTime());
			// ��ʾ��һ����Ʒ�ĵ�һ��ͼƬ
			ArrayList<Object> products = order.getProducts();
			MyApplication.client.getImageForNetImageView(((Product)products.get(0))
					.getImgs().get(0), img, R.drawable.ic_launcher);
			orderSubjectTxt.setText(order.getOrderSubject());
			// ��Ʒ����
			orderNumTxt.setText(order.getTotalRecord());
			// �������
			orderTotalpriceTxt.setText("��"+order.getTotalPrice());
//			leftBtn.setTag(R.id.tag_first, convertView);
			leftBtn.setTag(order.getOrderID());
//			rightBtn.setTag(order.getOrderID());
			rightBtn.setTag(order);
			// Ҫ�鿴�Ķ������ͣ�1.�����2.��������3.���ջ���4.�����
			int orderType = Integer.valueOf(order.getOrderType());
			orderTypeTemp=orderType;
			orderComment=Integer.valueOf(order.getComments());
//			leftBtn.setTag(R.id.tag_three, orderType);
			leftBtn.setOnClickListener(this);
			rightBtn.setOnClickListener(this);
			switch (orderType) {
			case 1:
				if (order.getIsPay().equals("0")
						&& Integer.valueOf(order.getFlag()) < 3) {
					btnLayout.setVisibility(View.VISIBLE);
					leftBtn.setVisibility(View.VISIBLE);
					leftBtn.setText("ȡ������");
					rightBtn.setVisibility(View.VISIBLE);
					rightBtn.setText("  ��     ��  ");
				}
				else {
					btnLayout.setVisibility(View.GONE);
					leftBtn.setVisibility(View.GONE);
					rightBtn.setVisibility(View.GONE);
				}

				break;
			case 2:
				if (order.getIsPay().equals("0")
						&& Integer.valueOf(order.getFlag()) < 3 ) {
					btnLayout.setVisibility(View.VISIBLE);
					leftBtn.setVisibility(View.VISIBLE);
					leftBtn.setText("ȡ������");
				} else {
					btnLayout.setVisibility(View.GONE);
					leftBtn.setVisibility(View.GONE);
				
				}
				rightBtn.setVisibility(View.GONE);
				break;
			case 3:
				btnLayout.setVisibility(View.VISIBLE);
				leftBtn.setVisibility(View.GONE);
				rightBtn.setVisibility(View.VISIBLE);
				rightBtn.setText("ȷ���ջ� ");
				break;
			case 4:
				btnLayout.setVisibility(View.VISIBLE);
				leftBtn.setVisibility(View.GONE);
				rightBtn.setVisibility(View.VISIBLE);
				// ����״̬ 1�����ۣ�0δ����
				if (orderComment==0)
					rightBtn.setText("  ��     ��  ");
				else
					rightBtn.setText("  ɹ     ��  ");
				break;
			}

		}
		else if (request.equals(NetworkAction.���۶���))
		{
			Product product = (Product) data.get(position);
			NetworkImageView img = (NetworkImageView) convertView
					.findViewById(R.id.order_evaluate_photo);

			TextView nameTxt = (TextView) convertView
					.findViewById(R.id.order_evaluate_name);
			TextView priceTxt = (TextView) convertView
					.findViewById(R.id.order_evaluate_price);
			
			final RatingBar stars = (RatingBar) convertView
					.findViewById(R.id.starBtn);
			
			Button addComment = (Button) convertView
					.findViewById(R.id.addComment);
			final EditText comments = (EditText) convertView
					.findViewById(R.id.addContent);
			stars.setTag(product);
			stars.setTag(R.id.tag_first, comments.getText().toString());
			addComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (comments.getText().toString().equals("")) {
						Toast.makeText((Context) object,
								"����д���ۣ�", 2000).show();
						return;
					}
					MyApplication.comment=false;
					((OrderEvaluate)object).publishComment(stars);
				}
			});
			// ���ж�����ɱ��Ʒ����������Ʒ�����ݲ�ͬ����Ʒ��ʾ��ͬ����Ϣ
			// �������ͣ�1��������2��ɱ
			if (product.getBuy_type().equals("1")) {
				nameTxt.setText(product.getName());
				priceTxt.setText("��" + product.getStorePrice());
			} else {
				nameTxt.setText(product.getSKName());
				priceTxt.setText("��" + product.getSKPrice());
			}
			MyApplication.client.getImageForNetImageView(
					product.getImgs().get(0), img, R.drawable.ic_launcher);
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.person_address_item_edite:// �ջ��б�༭��ť
			Address address = (Address) v.getTag();
			((PersonAddress) object).editAddress(address);
			break;
		case R.id.person_address_item_delete:// �ջ��б�ɾ����ť
			String addressID = (String) v.getTag();
			Log.i(MyApplication.TAG, "addressID-->" + addressID);
			((PersonAddress) object).deleteAddress(addressID);
			break;
		case R.id.person_address_item_default_layout:// �����ΪĬ�ϵ�ַ���¼�
			((PersonAddress) object).defaultAddress((Address) v.getTag());
			break;

		case R.id.productlist_second_itemlayout:// �����б�������¼�
			ListView thirdListView = (ListView) v.getTag();
			ImageView img = (ImageView) v
					.findViewById(R.id.productlist_second_img);
			// ���������б���µ�����������ʾ��ʱ��
			if (thirdListView.getVisibility() == View.VISIBLE) {
				img.setBackgroundDrawable(MyApplication.resources
						.getDrawable(R.drawable.productlist_second_close));
				thirdListView.setVisibility(View.GONE);
			} else// ���������б���µ���������û����ʾ��ʱ��
			{
				img.setBackgroundDrawable(MyApplication.resources
						.getDrawable(R.drawable.productlist_second_open));
				thirdListView.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.productlist_third_itemlayout:// ������������¼�
			// ��ȡ�÷������Ϣ
			Category category = (Category) v.getTag();
			// ����Ʒ����ҳ����ת����Ʒչʾҳ��
			Intent intent = new Intent().setClass((Context) object,
					ProductListShow.class);
			intent.putExtra("Category_id", category.getCategory_id());
			intent.putExtra("CacheID", category.getCacheID());
			((ProductList) object).startActivity(intent);
			// Toast.makeText((Context) object, category.getCacheID(), 2000)
			// .show();
			break;
		case R.id.shopcart_num_sub:// ���ﳵ�ļ��ٰ�ť
			changeShopCartNum(v, "sub");
			break;
		case R.id.shopcart_num_add:// ���ﳵ�����Ӱ�ť
			changeShopCartNum(v, "add");
			break;
		case R.id.coupon_item_use:// �Ż�ȯ��ʹ�ð�ť
			Coupon coupon = (Coupon) v.getTag();
			Intent product_detail_intent = new Intent();
			product_detail_intent.setClass((Context) object,
					ProductDetail.class);
			product_detail_intent.putExtra("productId", coupon.getProductID());
			((Person) object).startActivity(product_detail_intent);
			break;
		case R.id.order_left_btn:// �����б���߰�ť
			String orderID= (String) v.getTag();
			((PersonOrder) object).cancelOrder(orderID);
			break;
		case R.id.order_right_btn:// �����б��ұ߰�ť
			Order order= (Order) v.getTag();
			switch (orderTypeTemp) {
			//����Ǵ��ջ�״̬�ð�ťΪȷ���ջ��Ĺ���
			case 3:
				((PersonOrder) object).confirmReceive(order.getOrderID());
				break;
				//����������״̬�ð�ťΪ���۶����Ĺ���
			case 4:
				//�����δ���۵�״̬��ִ�����۲���
				if (orderComment==0)
				{
					Intent intent2=new Intent();
					intent2.setClass(((PersonOrder)object).getActivity(), OrderEvaluate.class);
					intent2.putExtra("order", order);
					((PersonOrder)object).startActivity(intent2);
				}
				break;
			}
			
			break;
		}

	}

	// �޸Ĺ��ﳵĳ����Ʒ������������ȫ�ֹ��ﳵ���ϲ�ˢ����ʾ
	public void changeShopCartNum(View v, String operation) {
		TextView numTxt = (TextView) v.getTag();
		Product product = (Product) numTxt.getTag();
		int num = Integer.valueOf(numTxt.getText().toString());
		if (operation.equals("sub")) {
			// �������Ϊ1���ܹ��ټ��ٸ���Ʒ
			if (num == 1)
				return;
			else
				num--;
		} else
			num++;
		numTxt.setText(String.valueOf(num));
		product.setNum(String.valueOf(num));
		((ShopCart) object).recalculatePrice();
	}

	/**
	 * ��ȡ������ListView���ݵĸ߶�
	 * 
	 * @param lv
	 */
	private void setListViewHeight(ListView lv) {
		ListAdapter la = lv.getAdapter();
		if (null == la) {
			return;
		}
		// calculate height of all items.
		int h = 0;
		final int cnt = la.getCount();
		for (int i = 0; i < cnt; i++) {
			View item = la.getView(i, null, lv);
			item.measure(0, 0);
			h += item.getMeasuredHeight();
		}
		// reset ListView height
		ViewGroup.LayoutParams lp = lv.getLayoutParams();
		lp.height = h + (lv.getDividerHeight() * (cnt - 1));
		lv.setLayoutParams(lp);
	}

	public final class HomeViewHolder {
		public ImageView img;
		public TextView subject;
		public TextView date;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Product product = (Product) buttonView.getTag();
		Log.i(MyApplication.TAG, "product name-->" + product.getName());
		product.setChecked(isChecked);
		Log.i(MyApplication.TAG, "product checked-->" + product.isChecked());
	}

}
