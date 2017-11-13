package com.haiqiu.miaohi.utils;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by ningl on 16/10/18.
 */
public class LongClickableLinkMovementMethod extends LinkMovementMethod{

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN
                ||action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, TextUtil.TextClickSpan.class);
            if(link.length!=0){
                ClickableSpan l = link[0];
                int ls = buffer.getSpanStart(l);
                int le = buffer.getSpanEnd(l);
                if (off > layout.getOffsetToLeftOf(ls) && off < layout.getOffsetToLeftOf(le + 1)) {
                    Selection.removeSelection(buffer);
                    return true;
                }
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

}
