package com.haiqiu.miaohi.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.haiqiu.miaohi.bean.Comment;
import com.haiqiu.miaohi.bean.Notify_user_result;
import com.haiqiu.miaohi.bean.TextInfo;
import com.haiqiu.miaohi.bean.VideoItemPageResult;
import com.qiniu.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文本处理工具
 * Created by ningl on 16/10/9.
 * 如果要使用点击功能 必须设置 setMovementMethod(LinkMovementMethod.getInstance())
 */
public class TextUtil {
    
    public static TextUtil getInstance(){
        return new TextUtil();
    }

    /**
     * 获取SpannableStringBuilder
     * @return
     */
    private SpannableStringBuilder getBaseSpan(TextInfo textInfo){
        if(textInfo ==null||textInfo.getOriginalStr()==null) return null;
        SpannableStringBuilder span = null;
        if(!StringUtils.isNullOrEmpty(textInfo.getOriginalStr())
                || textInfo.getEnd()> textInfo.getEnd()
                || textInfo.getEnd()< textInfo.getOriginalStr().length()){
            span = new SpannableStringBuilder(textInfo.getOriginalStr());
        }
        return span;
    }

    /**
     * 制定位置字体添加点击事件 调用前需设置
     * #setMovementMethod(LinkMovementMethod.getInstance())#
     * @param textInfo 字体信息
     * @return SpannableStringBuilder
     */
    public SpannableStringBuilder setClick(TextInfo textInfo, AbstractTextUtil iClickText){
        SpannableStringBuilder span = getBaseSpan(textInfo);
        if(span != null) span.setSpan(new TextClickSpan(textInfo, iClickText), textInfo.getStart(), textInfo.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 指定位置字体颜色设置
     * @param textInfo
     * @return
     */
    public SpannableStringBuilder setColor(TextInfo textInfo){
        SpannableStringBuilder span = getBaseSpan(textInfo);
        if(span != null) span.setSpan(new ForegroundColorSpan(textInfo.getColor()), textInfo.getStart(), textInfo.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 指定位置设置点击和颜色 调用前需设置
     * #setMovementMethod(LinkMovementMethod.getInstance())#
     * @param textInfo
     * @param iClickText
     * @return
     */
    public SpannableStringBuilder setClickAndColor(TextInfo textInfo, AbstractTextUtil iClickText){
        SpannableStringBuilder span = getBaseSpan(textInfo);
        if(span != null) {
            span.setSpan(new ForegroundColorSpan(textInfo.getColor()), textInfo.getStart(), textInfo.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new TextClickSpan(textInfo, iClickText), textInfo.getStart(), textInfo.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    /**
     * 指定位置设置点击和颜色 调用前需设置
     * #setMovementMethod(LinkMovementMethod.getInstance())#
     * @param textInfos
     * @param iClickText
     * @return
     */
    public SpannableStringBuilder setClickAndColor(String OriginalStr, List<TextInfo> textInfos, AbstractTextUtil iClickText){
        if(textInfos!=null){
            SpannableStringBuilder span = null;
            if(OriginalStr == null) return null;
            span = new SpannableStringBuilder(OriginalStr);
            for (int i = 0; i < textInfos.size(); i++) {
                TextInfo textInfo = textInfos.get(i);
                if(textInfo ==null||textInfo.getOriginalStr()==null) break;
                if(!StringUtils.isNullOrEmpty(textInfo.getOriginalStr())
                        || textInfo.getEnd()> textInfo.getEnd()
                        || textInfo.getEnd()< textInfo.getOriginalStr().length()){
                    span.setSpan(new TextClickSpan(textInfo, iClickText), textInfo.getStart(), textInfo.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    span.setSpan(new ForegroundColorSpan(textInfo.getColor()), textInfo.getStart(), textInfo.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            return span;
        }
        return null;
    }

    /**
     * 处理@好友变色和点击
     * @param pageResult
     */
    public SpannableStringBuilder handleVideoDescribe(VideoItemPageResult pageResult, AbstractTextUtil iClickText){
        if(pageResult == null
                ||pageResult.getVideo_note()==null
                ||pageResult.getNotify_user_result()==null)
            return new SpannableStringBuilder("");
//        String videoNote = pageResult.getVideo_note();
//        List<Notify_user_result> notify_user_results = pageResult.getNotify_user_result();
//        Map<String, Integer> stageMap = new HashMap<>();
//        List<TextInfo> textInfos = new ArrayList<>();
//        int index = 0;
//        for (int i = 0; i < notify_user_results.size(); i++) {
//            Notify_user_result notify_user_result = notify_user_results.get(i);
//            if(notify_user_result==null
//                    ||notify_user_result.getNotify_user_id()==null
//                    ||notify_user_result.getNotify_user_name()==null) return new SpannableStringBuilder("");
//            //map中是否存在重名
//            if(stageMap.containsKey(notify_user_result.getNotify_user_name())){//存在
//                index = videoNote.indexOf("@"+notify_user_result.getNotify_user_name(), stageMap.get(notify_user_result.getNotify_user_name()));
//            } else {
//                index = videoNote.indexOf("@"+notify_user_result.getNotify_user_name());
//            }
//            stageMap.put(notify_user_result.getNotify_user_name(), index+notify_user_result.getNotify_user_name().length());
//            if(index!=-1){
//                TextInfo textInfo = new TextInfo();
//                textInfo.setStart(index);
//                textInfo.setEnd(index+1+notify_user_result.getNotify_user_name().length());
//                textInfo.setTarget(notify_user_result.getNotify_user_id());
//                textInfo.setOriginalStr(pageResult.getVideo_note());
//                textInfo.setColor(Color.parseColor("#00a0e9"));
//                textInfos.add(textInfo);
//            }
//        }
//        return TextUtil.getInstance().setClickAndColor(pageResult.getVideo_note(), textInfos, iClickText);
        return handlerString(pageResult.getVideo_note(), pageResult.getNotify_user_result(), iClickText);
    }

    /**
     * 处理含有@的字符串变色和点击
     * @param text
     * @param notify_user_results
     * @param iClickText
     * @return
     */
    public SpannableStringBuilder handlerString(String text, List<Notify_user_result> notify_user_results, AbstractTextUtil iClickText){
        if(text == null
                ||notify_user_results==null)
            return new SpannableStringBuilder("");
        String videoNote = text;
        Map<String, Integer> stageMap = new HashMap<>();
        List<TextInfo> textInfos = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < notify_user_results.size(); i++) {
            Notify_user_result notify_user_result = notify_user_results.get(i);
            if(notify_user_result==null
                    ||notify_user_result.getNotify_user_id()==null
                    ||notify_user_result.getNotify_user_name()==null) return new SpannableStringBuilder("");
            //map中是否存在重名
            if(stageMap.containsKey(notify_user_result.getNotify_user_name())){//存在
                index = videoNote.indexOf("@"+notify_user_result.getNotify_user_name(), stageMap.get(notify_user_result.getNotify_user_name()));
            } else {
                index = videoNote.indexOf("@"+notify_user_result.getNotify_user_name());
            }
            stageMap.put(notify_user_result.getNotify_user_name(), index+notify_user_result.getNotify_user_name().length());
            if(index!=-1){
                TextInfo textInfo = new TextInfo();
                textInfo.setStart(index);
                textInfo.setEnd(index+1+notify_user_result.getNotify_user_name().length());
                textInfo.setTarget(notify_user_result.getNotify_user_id());
                textInfo.setOriginalStr(text);
                textInfo.setColor(Color.parseColor("#00a0e9"));
                textInfos.add(textInfo);
            }
        }
        if(iClickText!=null) iClickText.getTextInfos(textInfos);
        return TextUtil.getInstance().setClickAndColor(text, textInfos, iClickText);

    }

    /**
     * 处理含有@的字符串变色和点击
     * @param text
     * @param notify_user_results
     * @param iClickText
     * @return
     */
    public SpannableStringBuilder handlerStringBlack(String text, List<Notify_user_result> notify_user_results, AbstractTextUtil iClickText){
        if(text == null
                ||notify_user_results==null)
            return new SpannableStringBuilder("");
        String videoNote = text;
        Map<String, Integer> stageMap = new HashMap<>();
        List<TextInfo> textInfos = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < notify_user_results.size(); i++) {
            Notify_user_result notify_user_result = notify_user_results.get(i);
            if(notify_user_result==null
                    ||notify_user_result.getNotify_user_id()==null
                    ||notify_user_result.getNotify_user_name()==null) return new SpannableStringBuilder("");
            //map中是否存在重名
            if(stageMap.containsKey(notify_user_result.getNotify_user_name())){//存在
                index = videoNote.indexOf("@"+notify_user_result.getNotify_user_name(), stageMap.get(notify_user_result.getNotify_user_name()));
            } else {
                index = videoNote.indexOf("@"+notify_user_result.getNotify_user_name());
            }
            stageMap.put(notify_user_result.getNotify_user_name(), index+notify_user_result.getNotify_user_name().length());
            if(index!=-1){
                TextInfo textInfo = new TextInfo();
                textInfo.setStart(index);
                textInfo.setEnd(index+1+notify_user_result.getNotify_user_name().length());
                textInfo.setTarget(notify_user_result.getNotify_user_id());
                textInfo.setOriginalStr(text);
                textInfo.setColor(Color.parseColor("#1d1d1d"));
                textInfos.add(textInfo);
            }
        }
        if(iClickText!=null) iClickText.getTextInfos(textInfos);
        return TextUtil.getInstance().setClickAndColor(text, textInfos, iClickText);

    }

    public class TextClickSpan extends ClickableSpan {

        private TextInfo textInfo;
        private AbstractTextUtil iClickText;

        public TextClickSpan(TextInfo textInfo, AbstractTextUtil iClickText) {
            this.textInfo = textInfo;
            this.iClickText = iClickText;
        }

        @Override
        public void onClick(View view) {
            if(iClickText!=null) iClickText.onClickSpan(textInfo);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
            if(textInfo!=null&&textInfo.getColor()!=0){
                ds.setColor(textInfo.getColor());
            }
        }
    }

    /**
     * 设置评论人
     * @param comment
     * @return
     */
    public SpannableStringBuilder getCommontSpan(Comment comment, AbstractTextUtil abstractTextUtil){
        SpannableStringBuilder span = handlerString(comment.getComment_user_name()+"   "+comment.getComment_text(), comment.getNotify_user_result(), abstractTextUtil);
        span.setSpan(new StyleSpan(Typeface.BOLD), 0, comment.getComment_user_name().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.parseColor("#1d1d1d")), 0, comment.getComment_user_name().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextInfo textInfo = new TextInfo();
        textInfo.setColor(Color.parseColor("#1d1d1d"));
        textInfo.setEnd(comment.getComment_user_name().length());
        textInfo.setStart(0);
        textInfo.setTarget(comment.getComment_user_id());
        span.setSpan(new TextClickSpan(textInfo, abstractTextUtil), 0, comment.getComment_user_name().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 设置制定位置字体加粗
     * @param text
     * @param start
     * @param end
     */
    public static SpannableStringBuilder setBold(String text, int start, int end){
        if(text == null) return new SpannableStringBuilder("");
        SpannableStringBuilder span = new SpannableStringBuilder(text);
        span.setSpan(new StyleSpan(Typeface.BOLD), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

}
