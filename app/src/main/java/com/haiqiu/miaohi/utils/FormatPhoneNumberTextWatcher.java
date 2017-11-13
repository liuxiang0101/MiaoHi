package com.haiqiu.miaohi.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;

/**
 * Created by LiuXiang on 2016/9/2.
 */
public class FormatPhoneNumberTextWatcher implements TextWatcher {
    private final String TAG = "FormatPhoneNumberTextWatcher";
    private EditText et_phone_number;
    private TextView tv_next;
    private ImageView imageView;
    private Context context;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        MHLogUtil.i(TAG, "beforeTextChanged");
    }

    public FormatPhoneNumberTextWatcher(EditText et_phone_number, TextView tv_next, ImageView imageView, Context context) {
        this.et_phone_number = et_phone_number;
        this.tv_next = tv_next;
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        MHLogUtil.i(TAG, "onTextChanged");
        if (s == null || s.length() == 0) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        if (!sb.toString().equals(s.toString())) {
            int index = start + 1;
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            et_phone_number.setText(sb.toString());
            et_phone_number.setSelection(index);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        MHLogUtil.i(TAG, "afterTextChanged");
        if (s.length() <= 0 || s == null) {
            imageView.setVisibility(View.INVISIBLE);
        } else {
            imageView.setVisibility(View.VISIBLE);
        }
        if (null != s && s.length() >= 13) {
            tv_next.setBackgroundResource(R.drawable.selector_button_black);
            tv_next.setClickable(true);
        } else {
            tv_next.setBackgroundColor(context.getResources().getColor(R.color.color_df));
            tv_next.setClickable(false);
        }
    }
};
