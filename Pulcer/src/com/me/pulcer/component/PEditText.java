package com.me.pulcer.component;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.me.pulcer.component.PEditText.DrawableClickListener.DrawablePosition;

public class PEditText extends EditText {
	
	private static final String FONT_PATH="fonts/HelveticaNeueLTPro-Bd.otf";
	private Typeface tf;
	private Drawable leftDrwd;
	private Drawable rightDrwd;
	private DrawableClickListener listener;
	
	
	public PEditText(Context context){
		super(context);
//		setFont(context);
		init();
	}
	
	public PEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		setFont(context);
		init();
	}
	
	public PEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
//		setFont(context);
		init();
		
	}
	private void setFont(Context context){
		if(!isInEditMode()){
			tf=Typeface.createFromAsset(context.getAssets(), FONT_PATH);
			this.setTypeface(tf);
		}
	}
	
	private void init(){
		if(rightDrwd!=null){
			rightDrwd.setBounds(0, 0, rightDrwd.getIntrinsicWidth(), rightDrwd.getIntrinsicHeight());
		}
		
	}
	
	@Override
	public void setCompoundDrawables(Drawable left, Drawable top,Drawable right, Drawable bottom) {
		
		if(left!=null){
			leftDrwd=left;
		}
		if(right!=null){
			rightDrwd=right;
		}
		super.setCompoundDrawables(left, top, right, bottom);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		PEditText et=PEditText.this;
		if(rightDrwd!=null){
			if (event.getX() > et.getWidth() - et.getPaddingRight() - rightDrwd.getIntrinsicWidth()) {
				if(listener!=null)
					listener.onClick(DrawablePosition.RIGHT);
				this.setText("");
				handleText();
			
			}
		}
	    
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onTextChanged(CharSequence text, int start,int lengthBefore, int lengthAfter) {
		handleText();
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}
	
	private void handleText(){
		if(this.getText().toString().equalsIgnoreCase("")){
			this.setCompoundDrawables(leftDrwd, null,null, null);
		}else{
			this.setCompoundDrawables(leftDrwd, null,rightDrwd, null);
		}
	}
	
	public interface DrawableClickListener {
		public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
		public void onClick(DrawablePosition target); 
	}
	
	@Override
	protected void finalize() throws Throwable {
		leftDrwd=null;
		rightDrwd=null;
		super.finalize();
	}
	
	
}
