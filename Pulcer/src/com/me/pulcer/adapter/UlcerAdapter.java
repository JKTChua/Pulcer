package com.me.pulcer.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pulcer.R;
import com.me.pulcer.common.PApp;
import com.me.pulcer.component.PTextView;
import com.me.pulcer.entity.Reminder;
import com.me.pulcer.entity.UlcerGroup;
import com.me.pulcer.util.PLogger;
import com.me.pulcer.util.Util;

public class UlcerAdapter extends BaseAdapter
{

	public ArrayList<UlcerGroup> list;
	private LayoutInflater lf;
	public boolean isLocked=false;
	public int selectedId=-1;
	Context context;
	
	static class ViewHolder
	{
        PTextView ulcerStage;
        ImageView ulcerImg;
        PTextView ulcerLocation;
    }
	
	public UlcerAdapter(Context context,ArrayList<UlcerGroup> ulcerList)
	{
		this.list = ulcerList;
		this.context = context;
		lf=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public UlcerGroup getItem(int index)
	{
		return list.get(index);
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
//		PLogger.getLogger().info("getView Called:"+position+" locked:"+isLocked);
		
		if(convertView==null)
		{
			convertView=lf.inflate(R.layout.ulcer_row, null);
			holder = new ViewHolder();
			holder.ulcerImg=(ImageView) convertView.findViewById(R.id.ulcer_imgvw);
			holder.ulcerStage=(PTextView) convertView.findViewById(R.id.ulcer_stage);
			holder.ulcerLocation=(PTextView) convertView.findViewById(R.id.ulcer_location);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		UlcerGroup ulcer = (UlcerGroup) list.get(position);
		
		if(ulcer!=null)
		{
			holder.ulcerStage.setText("Stage " + ulcer.stage);
		}
		
//		if(ulcer!=null){
//			infoLog("Name:"+ulcer.medication+" status:"+ulcer.status);
//			if(ulcer.reminderId==selectedId){
//				convertView.setBackgroundColor(context.getResources().getColor(R.color.list_upcomming_color));
//			}else if(ulcer.status>0){
//				convertView.setBackgroundColor(context.getResources().getColor(R.color.list_disable_bg));
//			}else{
//				convertView.setBackgroundColor(Color.TRANSPARENT);
//			}
//			
//			if(Util.validateStr(ulcer.medication)){
//				holder.pillName.setText(ulcer.medication);
//			}else{
//				holder.pillName.setText("");
//			}
//			if(Util.validateStr(ulcer.dosage)){
//				holder.pillContent.setText(ulcer.dosage+" mg");
//			}else{
//				holder.pillContent.setText("");
//			}
//			
//			if(Util.validateStr(ulcer.days)){
//				String day[]=ulcer.days.split(",");
//				holder.dayContainer.setVisibility(View.GONE);
//				holder.pillSchedule.setVisibility(View.VISIBLE);
//				if(ulcer.days.contains("6") && ulcer.days.contains("7") && day.length==2){
//					holder.pillSchedule.setText("Weekends");
//				}else 
//					if(day.length==5 && ulcer.days.contains("6")!=true && ulcer.days.contains("7")!=true){
//						holder.pillSchedule.setText("Weekdays");
//					}
//				else if(day.length==7){
//					holder.pillSchedule.setText("Daily");
//				}else{
//					holder.pillSchedule.setVisibility(View.GONE);
//					holder.dayContainer.setVisibility(View.VISIBLE);
//					Util.showDays(holder.dayContainer, ulcer.days);
//				}
//				
//			}else{
//				holder.pillSchedule.setText("");
//			}
//			infoLog("Name:"+ulcer.medication+" image:"+ulcer.imageFile+" status:"+ulcer.status);
//			if(Util.validateStr(ulcer.imageFile)){
//				
//				if(ulcer.status==Reminder.STATE_SCHEDULE){
//					if(ulcer.imageFile.equalsIgnoreCase(PApp.SHAPE_CAPSULE)){
//						holder.pillImg.setImageResource(capsule);
//					}else if(ulcer.imageFile.equalsIgnoreCase(PApp.SHAPE_TABLET)){
//						holder.pillImg.setImageResource(tablet);
//					}else{
//						//TODO need to change as per image url
//						holder.pillImg.setImageResource(capsule);
//					}
//				}else{
//					if(ulcer.imageFile.equalsIgnoreCase(PApp.SHAPE_CAPSULE)){
//						holder.pillImg.setImageResource(capsule_disable);
//					}else if(ulcer.imageFile.equalsIgnoreCase(PApp.SHAPE_TABLET)){
//						holder.pillImg.setImageResource(tablet);
//					}else{
//						//TODO need to change as per image url
//						holder.pillImg.setImageResource(capsule_disable);
//					}
//				}
//				
//				
//			}else{
//				//TODO there should be default image for this
//				holder.pillImg.setImageResource(capsule);
//			}
//			if(ulcer.status!=Reminder.STATE_SCHEDULE){
//				if(ulcer.status==Reminder.STATE_TAKEN){
//					holder.rowIndicator.setBackgroundResource(R.drawable.green_corner);
//				}else if(ulcer.status==Reminder.STATE_MISSED){
//					holder.rowIndicator.setBackgroundResource(R.drawable.orange_corner);
//				}
//				holder.rowIndicator.setVisibility(View.VISIBLE);
//			}else{
//				holder.rowIndicator.setVisibility(View.GONE);
//			}
//			
//			
//			if(Util.validateStr(Util.getTweelHour(ulcer.time))){
//				String str=Util.getTweelHour(ulcer.time);
//				String sub=str.substring(str.length()-2, str.length());
//				str=str.substring(0, str.length()-3);
//				holder.pillTime.setText(Html.fromHtml(str+"<small>"+sub+"</small>"));
//			}else{
//				holder.pillTime.setText("");
//			}
//			
//			
//		}
		return convertView;
	}
	private void infoLog(String message)
	{
//		PLogger.getLogger().info("UserPillAdapter:"+message);
	}

}
