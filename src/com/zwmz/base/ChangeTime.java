package com.zwmz.base;

import java.util.ArrayList;

import com.zwmz.activity.product.Home;
import com.zwmz.activity.product.ProductDetail;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;


//ÿ��ˢ����ɱ��Ʒ�ĵ���ʱʱ��
public class ChangeTime  implements Runnable{

	
	public static  ArrayList<Long> timeList;
	public static ArrayList<TextView> txtViewList;
	public static boolean exit=true;
	public static  long secKillTime=-1;
	
	public ChangeTime()
	{
		timeList=new ArrayList<Long>();
		txtViewList=new ArrayList<TextView>();
	}
	
	
	@Override
	public void run() {
		while(exit)
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//��ɱ��Ʒ����ҳ�浹��ʱ�ı���ˢ��
			if(secKillTime!=-1)
			{
				Bundle bundle=new Bundle();
				secKillTime--;
				String day = String.valueOf(secKillTime / 60 / 60 / 24);
				String hour = String.valueOf(secKillTime / 60 / 60 % 24);
				String min = String.valueOf(secKillTime / 60 % 60);
				String sec = String.valueOf(secKillTime % 60);
				String timeString = day + "��" + hour + "ʱ" + min + "��" + sec + "��";
				bundle.putLong("time", secKillTime);
				bundle.putString("timeString", timeString);
				if(ProductDetail.secKillHandler!=null)
				{
					Message msg=new Message();
					msg.setData(bundle);
					ProductDetail.secKillHandler.sendMessage(msg);
				}
			}
			
			//��ɱ��Ʒ�б���ʱˢ��
			if(timeList.size()>0&&!MyApplication.exit)
			{
//				Log.i(MyApplication.TAG, "timeList-->"+timeList.size());
//				Log.i(MyApplication.TAG, "txtViewList-->"+txtViewList.size());
				for (int i = 0; i < timeList.size(); i++) {
					long time=timeList.get(i);
					Bundle bundle=new Bundle();
					if(time>0)
					{
						time--;
						String day = String.valueOf(time / 60 / 60 / 24);
						String hour = String.valueOf(time / 60 / 60 % 24);
						String min = String.valueOf(time / 60 % 60);
						String sec = String.valueOf(time % 60);
						String timeString = day + "��" + hour + "ʱ" + min + "��" + sec + "��";
						bundle.putLong("time", time);
						bundle.putString("timeString", timeString);
						bundle.putInt("index", i);
						timeList.remove(i);
						timeList.add(i, time);
						if(Home.homeHandler!=null)
						{
							Message msg=new Message();
							msg.setData(bundle);
							Home.homeHandler.sendMessage(msg);
						}
					}
				}
			}
			
		}
		
	}
	
	
	
}
