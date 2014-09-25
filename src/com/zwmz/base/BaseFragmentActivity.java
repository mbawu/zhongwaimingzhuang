package com.zwmz.base;

import java.io.IOException;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseFragmentActivity extends FragmentActivity implements OnClickListener{

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		MyApplication.getInstance().addActivity(this);
	}
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub

			if (keyCode == KeyEvent.KEYCODE_BACK) {

				CustomDialog.Builder builder = new CustomDialog.Builder(this);
				builder.setMessage("��ȷ���˳���")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										//�˳�ǰ�ȱ��湺�ﳵ��Ϣ
										try {
											MyApplication.shopCartManager
													.saveProducts(MyApplication.shopCartList);
										} catch (IOException e) {

											e.printStackTrace();
										}
										dialog.cancel();
										finish();
										MyApplication.getInstance().exit();
									}
								})
						.setNegativeButton("����",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				CustomDialog alert = builder.create();
				alert.show();
				return true;
			}

			return super.onKeyDown(keyCode, event);
		}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
