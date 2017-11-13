package com.haiqiu.miaohi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ningl on 16/10/18.
 */
public class NoteEditText extends EditText{

    private OnCursorChange onCursorChange;

    public NoteEditText(Context context) {
        super(context);
    }

    public NoteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if(onCursorChange!=null) onCursorChange.change(selStart, selEnd);
    }

    public interface OnCursorChange {
        void change(int selStart, int selEnd);
    }

    public void setOnCursorChangeListener(OnCursorChange onCursorChange){
        this.onCursorChange = onCursorChange;
    }
}
