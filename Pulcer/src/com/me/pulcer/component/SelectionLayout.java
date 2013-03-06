package com.getplusapp.mobile.android.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.getplusapp.mobile.android.util.PLogger;

public class SelectionLayout extends LinearLayout{

	public PButton btn[];
	public boolean isSingleSelected=true;
	public boolean isLock=false;
	public SelectionChangeListener selectionListener=null;
	public SelectionLayout(Context context) {
		super(context);
	}
	public SelectionLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public void init(){
		PLogger.getLogger().info("init:"+this.getChildCount());
		
		
		int btnCnt=0;
		for(int i=0;i<this.getChildCount();i++){
			View temp=this.getChildAt(i);
			if(temp instanceof PButton){
				btnCnt++;
			}
			
		}
		btn=new PButton[btnCnt];
		
		int arrayCnt=0;
		
		
		for(int i=0;i<this.getChildCount();i++){
			View temp=this.getChildAt(i);
			if(temp instanceof PButton){
				btn[arrayCnt]=(PButton) this.getChildAt(i);
				btn[arrayCnt].setOnClickListener(btnClick);
				btn[arrayCnt].setTag(btn[arrayCnt].getText());
				arrayCnt++;
				
			}
			
		}
		
		
	}
	public interface SelectionChangeListener{
		public void onSelection();
	}
	
	public int getSelectedBtnPosition(){
		for(int i=0;i<btn.length;i++){
			if(btn[i].isSelected()){
				return i;
			}
		}
		return -1;
	}
	
	public String getMultiselectPosition(){
		String str="";
		for(int i=0;i<this.getChildCount();i++){
			View temp=this.getChildAt(i);
			if(temp.isSelected())
				str=str+""+i+",";
		}
		return str;
	}
	
	public void setMode(boolean isSingleSelected){
		this.isSingleSelected=isSingleSelected;
	}
	View.OnClickListener btnClick=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(isLock!=true){
				if(isSingleSelected){
					setDefault();
					v.setSelected(true);
					if(selectionListener!=null){
						selectionListener.onSelection();
					}
				}else{
					if(v.isSelected()){
						v.setSelected(false);
					}else{
						v.setSelected(true);
					}
				}
			}
			
			
			PLogger.getLogger().info("SElection onCLick"+v.getId());
			
			
			
		}
	};
	
	public void selectAll(){
		for(int i=0;i<btn.length;i++){
			btn[i].setSelected(true);
		}
	}
	public void deSelecteAll(){
		for(int i=0;i<btn.length;i++){
			btn[i].setSelected(false);
		}
	}
	
	
	private void setDefault(){
		for(int i=0;i<btn.length;i++){
			btn[i].setSelected(false);
		}
	}
	
	public void setSelected(int index){
		if(isSingleSelected)
			deSelecteAll();
		
		btn[index].setSelected(true);
	}
	
	
}
