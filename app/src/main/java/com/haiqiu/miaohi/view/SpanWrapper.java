package com.haiqiu.miaohi.view;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ningl on 2016/6/21.
 */
public class SpanWrapper extends ClickableSpan {

    protected Context mContext;
    protected TextView mTextView;

    public SpanWrapper(Context mContext, TextView mTextView, String str) {
        this.mContext = mContext;
        this.mTextView = mTextView;
        updateClickSpan(mTextView, str);
    }


    public void updateClickSpan(TextView tv, String str) {
        // 这里拼接超链接, 我在前后加了一个井号,可自行去除
//        String htmlLinkText = "bbbbbbbbbbbb"+"<a href='https://souly.cn' style='text-decoration:none; color:#0000FF'> \n" +
//                "          html超链接测试</a>" + tv.getText().toString() + "</a>";
        String htmlLinkText = str;

        tv.setText(Html.fromHtml(htmlLinkText));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) tv.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans(); // should clear old spans
            for (URLSpan url : urls) {
                // 设置Span
                style.setSpan(this, sp.getSpanStart(url), sp.getSpanEnd(url),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv.setText(style);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        Toast.makeText(mContext, "aaa", Toast.LENGTH_SHORT).show();
    }
}
