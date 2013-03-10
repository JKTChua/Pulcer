package com.me.pulcer.component;

/**
 * Created by IntelliJ IDEA.
 * User: hetalpatel
 * Date: 11/01/12
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.CheckBox;

/**
 * CheckBox that does not react to any user event in order to let the container handle them.
 */
public class ICheckbox extends CheckBox {

    // Provide the same constructors as the superclass
    public ICheckbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Provide the same constructors as the superclass
    public ICheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Provide the same constructors as the superclass
    public ICheckbox(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Make the checkbox not respond to any user event
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Make the checkbox not respond to any user event
        return false;
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        // Make the checkbox not respond to any user event
        return false;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        // Make the checkbox not respond to any user event
        return false;
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        // Make the checkbox not respond to any user event
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // Make the checkbox not respond to any user event
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        // Make the checkbox not respond to any user event
        return false;
    }
}

