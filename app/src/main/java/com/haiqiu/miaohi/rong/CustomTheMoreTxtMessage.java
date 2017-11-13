package com.haiqiu.miaohi.rong;

import android.os.Parcel;
import android.util.Log;

import com.haiqiu.miaohi.utils.MHLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by LiuXiang on 2016/8/1.
 * 融云自定义消息--消息开关控制
 */
@MessageTag(value = "KM:UserTxtMsg", flag = MessageTag.ISCOUNTED)
public class CustomTheMoreTxtMessage extends MessageContent {
    private String content;//消息属性，可随意定义

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 将消息属性封装成 json 串，再将 json 串转成 byte 数组，该方法会在发消息时调用
     *
     * @return byte[]
     */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", "这是一条消息内容");
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            MHLogUtil.e("CustomTheMoreTxtMessage",e);
        }

        return null;
    }

    /**
     * 对收到的消息进行解析
     *
     * @param data byte[]
     */
    public CustomTheMoreTxtMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("content"))
                content = jsonObj.optString("content");

        } catch (JSONException e) {
//            RLog.e(this, "JSONException", e.getMessage());
        }

    }

    //给消息赋值。
    public CustomTheMoreTxtMessage(Parcel in) {
        content = ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
        //这里可继续增加你消息的属性
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<CustomTheMoreTxtMessage> CREATOR = new Creator<CustomTheMoreTxtMessage>() {

        @Override
        public CustomTheMoreTxtMessage createFromParcel(Parcel source) {
            return new CustomTheMoreTxtMessage(source);
        }

        @Override
        public CustomTheMoreTxtMessage[] newArray(int size) {
            return new CustomTheMoreTxtMessage[size];
        }
    };

    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    public int describeContents() {
        return 0;
    }

    /**
     * 将类的数据写入外部提供的 Parcel 中。
     *
     * @param dest  对象被写入的 Parcel。
     * @param flags 对象如何被写入的附加标志。
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);//该类为工具类，对消息中属性进行序列化
        //这里可继续增加你消息的属性
    }
}
