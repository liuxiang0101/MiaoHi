package com.haiqiu.miaohi.utils;

import java.util.regex.Pattern;

/**
 * Created by Guoy on 2016/5/12.
 */
public class MobileUtils {
    private static Pattern mobilePattern;
    /**
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * 联通：130、131、132、145、155、156、175、176、185、186
     * 电信：133、153、173、177、180、181、189
     * 全球星：1349
     * 虚拟运营商：170
     *
     * @param mobileNo
     * @return
     */


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        //        String telRegex = "[1][234578]\\d{9}";//"[1]"代表第1位为数字1，"[234578]"代表第二位可以为2、3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        //        if (TextUtils.isEmpty(mobiles)) return false;
        //        else return mobiles.matches(telRegex);
        if (mobilePattern == null) {
            mobilePattern = Pattern.compile("^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$");
        }
        return mobilePattern.matcher(mobiles).matches();
    }

}

