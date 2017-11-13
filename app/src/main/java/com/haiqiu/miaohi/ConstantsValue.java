package com.haiqiu.miaohi;

import android.content.Context;
import android.os.Environment;

import com.haiqiu.miaohi.utils.ToastUtils;

import java.io.File;
import java.util.HashMap;

/**
 * 常量
 */
public class ConstantsValue {
        //SERVER_URL 很多地方通过app来判断逻辑了,如果连接中没有app字样了,那就要注意逻辑更改
//    public static String SERVER_URL = "https://app.miaohi.com/mhv/v1/vsmhvfora.do";
//
//    public static  String SERVER_URL = "http://192.168.1.64:8080/vsmhvfora.do";                //王宏ip
//    public static  String SERVER_URL = "http://192.168.1.60:8080/mvserver/vsmhvfora.do";       //王健ip

    //    public static String SERVER_URL = "https://test.miaohi.com/mhv/v1/vsmhvfora.do";
    public static String SERVER_URL = "https://dev.miaohi.com/mhv/v1/vsmhvfora.do";


    public static String BUSINESS = "haiqiu";
    public static final String android = "AN";


    /**
     * @return 当前的模式, 如果是开发者模式, 则会弹提示, 防止被坑
     */
    public static boolean isDeveloperMode(Context context) {
        if (null != context && BuildConfig.DEBUG)
            ToastUtils.showToastAtCenter(context, "请注意,当前操作是在开发者模式下");
        return BuildConfig.DEBUG;
    }

    /**
     * 所有网络请求地址
     */
    public static class Url {
        public static final String HOME_PAGE_HOT = "homepagehot";//首页热门
        public static final String LOGIN = "login";//登陆
        public static final String LOGINEX = "loginex";//第三方登陆
        public static final String VERIFY_LOGINEX = "verifyloginex";//验证三方登陆是否已注册或绑定
        public static final String GIFT_RECEIVED = "giftreceived";
        public static final String GET_FIND = "getfind";
        public static final String HOME_PAGE_NEW = "homepagenew";//首页最新
        public static final String HOME_PAGE_VIP = "homepagevip";//首页大咖
        public static final String HOME_PAGE_ATTENTION = "homepageattention";
        public static final String SMS_CODE = "smscode";//获取短信验证码
        public static final String FORGET_PASSWORD = "forgetpassword";//忘记密码
        public static final String REGISTER = "register";//注册
        public static final String BIND_ACCOUNT = "bindaccount";//绑定账号
        public static final String GET_USERINFO = "getuserinfo";//获取用户信息
        public static final String SET_USERINFO = "setuserinfo";//设置用户信息
        public static final String VERIFY_REGISTER_INFO = "verifyregisterinfo";//验证注册信息有效性
        public static final String RECOMMEND_USER_FOR_ATTENTION = "recommenduserforattention";//首次注册后推荐用户进行关注
        public static final String BATCH_ATTENTION_DO = "batchattentiondo";//批量关注
        public static final String UPLOAD_VIDEO = "uploadvideo";
        public static final String USERACHIEVEMENT = "userachievement";
        public static final String ATTENTIONDO = "attentiondo";//关注
        public static final String TOP_AMAZE = "topamaze";
        public static final String TOP_ATTRACT = "topattract";
        public static final String TOP_LIKE = "toplike";
        public static final String TOP_VIP = "topvip";
        public static final String TOP_HOT = "tophot";
        public static final String FEEDBACK = "feedback";//反馈
        public static final String QUESTION_AUTHORIZATION = "questionAuthorization";//开通映答权限
        public static final String USER_FANS = "userfans";
        public static final String QUESTION_OBSERVE_USER_LIST = "questionobserveuserlist";  //围观列表
        public static final String GET_PRAISED_USER_LIST = "getpraiseduserlist";            //点赞列表
        public static final String GET_RONG_VALID_TOKEN = "getrongvalidtoken";//获取融云有效token
        public static final String USER_ATTENTION = "userattention";
        public static final String FACESENT = "facesent";
        public static final String GET_IMAGE_REPLACE = "getimagereplace";
        public static final String USER_MONEY_BALANCE = "usermoneybalance";//用户的累积收支情况
        public static final String GET_QUESTION_INFO = "getquestioninfo";//映答详情
        public static final String GET_PUSHED_MSG = "getpushedmsg";
        public static final String DISCOVERY = "discovery";//发现
        public static final String GET_OBJECT_LIST_BY_LABEL = "getobjectlistbylabel";//获取分类内容信息
        public static final String GET_DISCOVER_INTEREST_USERS = "getdiscoveryinterestusers";//发现-获取更多感兴趣的人
        public static final String GET_ATTENTION_INTEREST_USERS = "getattentioninterestusers";//关注-获取更多感兴趣的人
        public static final String GET_USERS_ATTENTION_STATUS = "getusersattentionstatus";//获取多个用户的关注状态
        public static final String SET_PUSHED_MSG_STSTE = "setpushedmsgstate";//修改消息状态
        public static final String SET_PUSHED_MSG_STSTE_BY_TIME = "setpushedmsgstatebytime";//按时间修改消息状态
        public static final String SEARCH_USER = "searchuser";
        public static final String SEARCH_ACTIVITY = "searchactivity";
        public static final String SEARCH_ENGINES = "searchengines";//搜索引擎
        public static final String SELECT_PREFERENCE_LABEL = "selectpreferencelabel";//选择用户喜好标签
        public static final String CONFIRM_PREFERENCE_LABEL = "confirmpreferencelabel";//选择喜好运动标签
        public static final String RECOMMEND = "recommend";
        public static final String GET_ACTIVITY_INFO = "getactivityinfo";
        public static final String GET_ACTIVITY_DETAIL = "getactivitydetail";
        public static final String SEARCH_FRIEND = "searchfriend";
        public static final String HOPE_ATTENTION = "hopeattention";
        public static final String GETVIDEOINFO = "getvideoinfo";//获取视频详情
        public static final String GETUSERCOMMENTINFO = "getusercommentinfo";//获取视频总评论
        public static final String PRAISEVIDEODO = "praisevideodo";//点赞&取消点赞
        public static final String GETVIPCOMMENTINFO = "getvipcommentinfo";//获取大咖评论
        public static final String GIFTRECEIVEDBYVIDEO = "giftreceivedbyvideo";//获取礼物
        public static final String PRAISE_VIDEODO = "praisevideodo";//视频点赞
        public static final String ADD_VIDEO_PLAYCOUNT = "addvideoplaycount";//视频播放统计
        public static final String GIFTSPECIFICATION = "giftspecification";//大咖获取可发送的表情
        public static final String ON_LOOK_ANSWER = "onlookanswer";
        public static final String SENDGIFT = "sendgift";//发送礼物
        public static final String DOCOMMENT = "docomment";//发送评论
        public static final String REPORTVIDEO = "reportvideo";//举报视频
        public static final String REPORT_QUESTION = "reportquestion";//举报视频
        public static final String REPORT_USER = "reportuser";//举报用户
        public static final String DELETEVIDEO = "deletevideo";//删除视频
        public static final String DELETECOMMENT = "deletecomment";//删除评论
        public static final String PRAISESENT = "praisesent";//送出的赞
        public static final String CHECK_UPDATE = "getclientversion";//检测更新
        public static final String GET_HOMEPAGE_TOOLBAR = "gethomepagetoolbar";//获取toolbar信息
        public static final String SET_HOMEPAGE_TOOLBAR = "sethomepagetoolbar";//更新toolbar信息
        public static final String GET_VIDEO_BY_KINDTAG = "getvideobykindtag";
        public static final String GETQUESTIONFROMUSER = "getquestionfromuser";//获取别人问我的问题
        public static final String GETQUESTIONTOVIP = "getquestiontovip";//获取我问的问题
        public static final String GETQUESTIONWATCHED = "getquestionbywatched";//我看过的问题
        public static final String TOP_QUESTION_RECOMMEND = "topquestionrecommend";//精选映答
        public static final String TOP_QUESTION_VIP = "topquestionvip";//热门答主
        public static final String GET_USER_ANSWER = "useranswer";//获取用户的映答
        public static final String SUBMIT_QUESTION_TOVIP = "submitquestiontovip";//提问
        public static final String CONFIRM_PAYMENT = "confirmpayment";//支付结果确认
        public static final String UPLOAD_ANSWER_VIDEO = "uploadanswervideo";//上传映答视频
        public static final String TOPQUESTION_RECOMMEND = "topquestionrecommend";//映答精选榜
        public static final String TOPQUESTION_NEW = "topquestionnew";//新映答
        public static final String TOPQUESTION_VIP = "topquestionvip";//新映答
        public static final String VALIDATEAUTHORIZATION = "validateauthorization";//检测是否绑定微信
        public static final String BINDINGWECHAT = "bindingwechat";//绑定微信
        public static final String UPLOADANSWERVIDEO = "uploadanswervideo";//上传信息
        public static final String GETWALLETINFO = "getwalletinfo";//钱包信息
        public static final String GETDEPOSITINFO = "getdepositinfo";//充值页面
        public static final String GETTRANSACTIONRECORD = "gettransactionrecord";//交易记录

        public static final String CALL_DEPOSITPAY = "calldepositpay";//调起充值
        public static final String CONFIRM_DEPOSIT = "confirmdeposit";//确认充值结果
        public static final String CONFIRMPAYSECONDREQUEST = "confirmpaysecondrequest";//确认二次充值结果

        public static final String GET_DEPOSITRECORD = "getdepositrecord";//充值记录
        public static final String SEARCHBINDACCOUNT = "searchbindaccount";//查询绑定账号
        public static final String GIFT_SPECIFICATION = "allgiftspecification";//获取所有礼物
        public static final String BINDACCOUNT = "bindaccount";//绑定账号
        public static final String REMOVEBINDACCOUNT = "removebindaccount";//解除绑定
        public static final String GIFTRECEIVEBYVIDEO = "giftreceivedbyvideo";//视频详情获取礼物
        public static final String GET_SWITCH_FOR_PUSH_NOTIFY = "getswitchforpushnotify";//推送消息开关状态
        public static final String SET_SWITCH_FOR_PUSH_NOTIFY = "setswitchforpushnotify";//设置推送消息开关状态
        public static final String GETALLUSERPHONTSANDVIDEOS = "getalluserphotosandvideos";//获取用户作品列表
        public static final String UPLOADBACKGROUNDIMG = "uploadbackgroundimg";
        public static final String GETALLMYANSEREDQUESTION = "getallmyansweredquestion";//我的回答
        public static final String GETALLMYASKQUESTION = "getallmyaskedquestion";//我的提问
        public static final String GETALLMYOBSERVEDQUESTION = "getallmyobservedquestion";//我的围观
        public static final String GETUSERANSWEREDQUESTION = "getuseransweredquestion";//获取费当前用户的映答
        public static final String ATTENTION = "attention";//获取关注列表
        public static final String UPLOADPHOTO = "uploadphoto";//上传图片
        public static final String PRAISEPHOTODO = "praisephotodo";//图片点赞
        public static final String DELETEPHOTO = "deletephoto";//删除图片
        public static final String GETPHOTOINFO = "getphotoinfo";//获取图片详情
        public static final String UNINTERESTOBJECT = "uninterestobject";//不感兴趣的内容
        public static final String REPORTPHOTO = "reportphoto";//举报图片

        public static final String USER_FANS_OWN = "userfansown";//用户粉丝
        public static final String USER_ATTENTION_OWN = "userattentionown";//用户关注


        public static final String GET_VIDEO_STICKERS = "getvideostickers";//贴纸
        public static final String GET_ALL_BACKGROUND_MUSIC = "getallbackgroundmusic";//配乐
        public static final String GETTHIRDPARTYTOKEN = "getthirdpartytoken";//获取第三方token参数

        public static final String IS_SELECT_LABEL = "isselectlabel";//当前用户是否选择了标签

        public static final String BE_HAVIOUR_STATISTICS = "behaviourstatistics";//用户行为统计

    }

    /**
     * 消息commend
     */
    public class MessageCommend {
        public static final String MSG_RECEIVE_ZAN = "pmsgreceivepraisevideo";          //消息-收到的赞
        public static final String MSG_SYSTEM = "pmsgreceivesystem";                    //消息-系统消息
        public static final String MSG_BE_INVITATE = "pmsgreceiveinvitation";           //消息-受邀活动
        public static final String MSG_AT_ME = "pmsgreceiveatme";                       //消息-@我的
        public static final String MSG_RECEIVE_COMMENT = "pmsgreceivecomment";          //消息-评论消息
        public static final String MSG_NEW_FRIEND = "pmsgreceiveattention";             //消息-新的好友
        public static final String MSG_RECEIVE_GIFT = "pmsgreceivegift";                //消息-收到礼物

        public static final String MSG_VIP_RECEIVE_QUESTION = "pmsgvipreceivequestion"; //消息-大咖收到提问
        public static final String MSG_VIP_ANSWER_USER = "pmsgvipansweruser";           //消息-大咖回答普通用户
        public static final String MSG_VIP_ANSWER_VIP = "pmsgvipanswervip";             //消息-大咖回答大咖
        public static final String MSG_OBSERVE_VIDEO = "pmsgobservevideo";              //消息-围观了我的视频

        public static final String MSG_RECEIVE_NOTIFY = "pmsgreceivenotify";            //消息-定时发送消息
        public static final String MSG_RECEIVE_VIDEO_KIND = "pmsgreceivevideokind";     //消息-收到通知更新
        public static final String MSG_RECEIVE_ATTENTION_NEW = "pmsgattentionnews";     //消息-收到关注页有更新
    }

    public class PayType {
        public static final int PAY_TYPE_WEIXIN = 10;
        public static final int PAY_TYPE_ALIPAY = 20;
        public static final int PAY_TYPE_UNIPAY = 2;//待确定
    }

    /**
     * 视频状态
     */
    public class VideoState {

        /**
         * 状态类字段对应的状态类型：正常使用
         */
        public static final int STATE_TYPE_NORMAL = 10;

        /**
         * 状态类字段对应的状态类型：没有定义
         */
        public static final int STATE_TYPE_UNDEFINE = 8;

        /**
         * 状态类字段对应的状态类型：冻结
         */
        public static final int STATE_TYPE_FREEZE = 6;

        /**
         * 状态类字段对应的状态类型：隐藏
         */
        public static final int STATE_TYPE_HIDE = 4;

        /**
         * 状态类字段对应的状态类型：删除
         */
        public static final int STATE_TYPE_DELETE = -1;

        /**
         * 状态类字段对应的状态类型：失败
         */
        public static final int STATE_TYPE_FAIL = -2;

        /**
         * 状态类字段对应的状态类型：过期
         */
        public static final int STATE_TYPE_OUTDATE = -3;

        /**
         * 状态类字段对应的状态类型：被举报
         */
        public static final int STATE_TYPE_REPORT = 12;

        /**
         * 状态类字段对应的状态类型：马甲
         */
        public static final int STATE_TYPE_VEST = 16;

        /**
         * 状态类字段对应的状态类型：已读
         */
        public static final int STATE_TYPE_READ = 20;
    }

    /**
     * 广播所用的Action
     */
    public static class IntentFilterAction {

        //支付状态同步
        public static final String PAY_SUCCESS_ACTION = "pay_success_action";
        public static final String PAY_SUCCESS_QUESTION_ID_KEY = "pay_success_question_id";

        //视频点赞同步
        public static final String VIDEO_ID_KEY = "video_id_key";

        //评论数量同步
        public static final String VIDEO_COMMIT_COUNT_ACTION = "video_commit_count_action";
        public static final String VIDEO_COMMIT_COUNT_KEY = "video_commit_count_key";

        //礼物数量同步
        public static final String VIDEO_GIFT_COUNT_ACTION = "video_gift_count_action";

        //删除视频同步
        public static final String VIDEO_DELETE_ACTION = "video_delete_action";

        //登陆成功
        public static final String LOGIN_SUCCESS_ACTION = "login_success";

        //退出登陆
        public static final String LOGOUT_ACTION = "logout_success";

        //同步粉丝
        public static final String SYNC_FANS_DATA_SUCCESS = "sync_fans_data_success";

        //同步关注
        public static final String SYNC_ATTENTION_DATA_SUCCESS = "sync_attention_data_success";

        //上传东西
        public static final String MEDIA_UPLOAD_ACTION = "media_upload_action";
    }

    /**
     * 第三方分享和登陆
     */
    public static class Shared {
        //视频详情
        public static final String SHARED_VIDEO_BASEURL = SERVER_URL.contains("app") ? "http://app.miaohi.com/miaohih5/shi.html?id=" : SERVER_URL.contains("apr") ? "http://apr.miaohi.com/miaohih5/shi.html?id=" : "http://apt.miaohi.com/miaohih5/shi.html?id=";
        //映答详情
        public static final String SHARED_QADETAIL_BASEURL = SERVER_URL.contains("app") ? "http://app.miaohi.com/miaohih5/answer_share.html?id=" : SERVER_URL.contains("apr") ? "http://apr.miaohi.com/miaohih5/answer_share.html?id=" : "http://apt.miaohi.com/miaohih5/answer_share.html?id=";
        //个人主页
        public static final String SHARED_PERSONALPAGE_BASEURL = SERVER_URL.contains("app") ? "http://app.miaohi.com/miaohih5/body.html?userid=" : SERVER_URL.contains("apr") ? "http://apr.miaohi.com/miaohih5/body.html?userid=" : "http://apt.miaohi.com/miaohih5/body.html?userid=";
        //专题详情
        public static final String SHARED_SUBJECTDETAIL_BASEURL = SERVER_URL.contains("app") ? "http://app.miaohi.com/miaohih5/topic.html?id=" : SERVER_URL.contains("apr") ? "http://apr.miaohi.com/miaohih5/topic.html?id=" : "http://apt.miaohi.com/miaohih5/topic.html?id=";

        public static final String SHARED_TITLE = "运动精彩，即刻秒嗨!";

        public static final String ANDROID_DEFAULT_SHARE_IMAGE = "http://res.release.miaohi.com/android_default_share_image.png";
        public static final String ANDROID_DEFAULT_SHARE_IMAGE1 = "http://image.release.miaohi.com/shareempty20170111.png";

        public static final String APPDOWNLOAD = "http://res.release.miaohi.com/main/index.html";

    }

    /**
     * 视频编辑常量
     */
    public static class VideoEdit {
        public static final String MUSIC_DIR_NAME = "Music";
        public static final String MUSIC_FILE_INFO_PATH = MUSIC_DIR_NAME + "/info.json";
        public static final String PASTER_DIR_NAME = "Decal";
        public static final String PASTER_FILE_INFO_PATH = PASTER_DIR_NAME + "/info.json";
    }

    /**
     * Sharedpreference
     */
    public class Sp {
        // 秒嗨token
        public static final String TOKEN_MIAOHI = "miaohi_token";
        // 七牛上传头像图片token
        public static final String TOKEN_QINIU_UPLOAD_ICON = "qiniu_upload_icon_token";
        // 七牛上传普通头像token
        public static final String TOKEN_QINIU_UPLOAD_IMAGE = "qiniu_upload_image_token";
        // 七牛上传视频token
        public static final String TOKEN_QINIU_UPLOAD_VIDEO = "qiniu_upload_video_token";
        // 融云token
        public static final String RONG_TOKEN = "rong_token";
        //七牛头像域名
        public static final String QINIU_WEB_ICON_BASE = "qiniu_web_icon_base";
        //七牛视频缩略图域名
        public static final String QINIU_WEB_IMAGE_BASE = "qiniu_web_image_base";
        //七牛视频域名
        public static final String QINIU_WEB_VIDEO_BASE = "qiniu_web_video_base";
        //秒嗨用户类型
        public static final String USER_TYPE = "user_type";
        //用户id
        public static final String USER_ID = "user_id";
        //用户昵称
        public static final String USER_NAME = "user_name";
        public static final String PORTRAIT_URI = "portrait_uri";
        public static final String UNDEFINE = "undefine";
        public static final String USER_LABEL_SELECTED = "label_selected";

        public static final String ANSER_DROIT = "answer_droit";

        public static final String LAST_GIFT = "last_gift";
        public static final String LAST_FANS = "last_fans";
        public static final String LAST_VERSION_CODE = "last_version_code";

        public static final String FIRST_USE_APP = "first_use_app";
        public static final String FIRST_INTO_NEW_VERSION_APP = "isFirstIntoNewEditionApp";
        public static final String HAS_CLICKED_MAKE_VIDEO = "hasClickedMakeVideo";
        public static final String HAS_CLICKED_OPEN_SORT = "hasClickedOpenSort";
        public static final String IS_SHOW_MSG_INFO = "is_show_msg_info";
        public static final String THE_PATH_SAVE_SPLASH_IMAGE = "the_path_save_splash_image";
        public static final String QA_TYPE = "qa_type";//映答列表-类型
        public static final String IS_APP_ON_FOREGROUND = "isAppOnForeground";//当前程序是否运行在前台

        public static final String USER_SOFT_CHECK_UPDATE = "user_soft_check_update";// 存储下次是否检查更新
        public static final String USER_PHONE_NUMBER = "USER_NAME";           //用户登录过一次自动存储的手机号
        public static final String IS_LOGIN_SUCCESS = "is_login_success";     //记录是否登录成功
        public static final String IS_LOGINOUT_APP = "isLoginoutApp";         //记录是否登出账户
        public static final String LAST_INTO_TIME = "last_into_time";         //消息-新的好友--记录上次进入的时间
        public static final String PATCH_VERSION_CODE = "patch_version_code"; //记录补丁版本号
        public static final String FFMPEG_HAS_INIT = "ffmpegHasInit";         //记录ffmpeg是否初始化
        public static final String LAST_DELETE_VIDEO_RECORD_DIR = "lastDeleteVideoRecordDir";         //记录上次删除的录制视频文件夹

        public static final String COPY_FILE_FLAG = "copy_file_flag";

        public static final String CHOOSE_LABEL_FLAG = "choose_label_flag";
    }

    /**
     * 一些视频目录的描述
     */
    public static class Video {
        public static final String VIDEO_BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MiaoHi";
        public final static String ROOT_PATH = VIDEO_BASE_PATH + File.separator + "VideoRecord";
        public final static String VIDEO_TEMP_PATH = VIDEO_BASE_PATH + File.separator + "VideoRecord" + File.separator + "VideoTemp";
    }

    public static class Other {
        //补丁名字
        public static final String PATCH_FILE_NAME = "miaohi.apatch";

        public static final String CHANNEL_NAME_NEW = "channel_name_new.data";

        //七牛获取头像后缀参数
        public static final String USER_HEAD_PARAM = "?imageView2/0/w/720/h/720";
        //七牛获取视频小封面参数
        public static final String VIDEO_MIN_PREVIEW_PICTURE_PARAM = "?imageView2/0/w/320/h/320";
        //七牛获取视频大封面参数
        public static final String VIDEO_MAX_PREVIEW_PICTURE_PARAM = "?imageView2/0/w/720/h/720";

        public static final String HEADER_PARAM = "imageView2/0/w/50/h/50";

        //七牛获取视频小封面参数--从视频帧里面截取
        public static final String VIDEO_MIN_PREVIEW_PICTURE_PARAM_FRAME = "|imageMogr2/thumbnail/320x320";
        //七牛获取视频大封面参数--从视频帧里面截取
        public static final String VIDEO_MAX_PREVIEW_PICTURE_PARAM_FRAME = "|imageMogr2/thumbnail/720x720";

        public static final String NETWORK_ERROR_TIP_MSG = "好像断网了";


        /**
         * 二次确认类型
         */
        public static final int CONFIRM_PAYSECONDRE_BALANCE = 10;//余额充足
        public static final int CONFIRM_PAYSECONDRE_NOBALANCE = 20;//余额不足
        public static final int CONFIRM_PAYSECONDRE_ASK = 30;//提问映答金额改变
        public static final int CONFIRM_PAYSECONDRE_CIRCUSEE = 40;//围观映答金额改变

        public static final String PAGESIZE = "20";

        //是否已经围观过
        public static final int PAYQARESULT_YES = 0; //未围观过
        public static final int PAYQARESULT_NO = 1;  //围观过

        //删除视频和图片的位置
        public static final int DELETEVIDEOANDIMG_FROMATTENTION = 1;        //在关注页删除视频或者图片
        public static final int DELETEVIDEOANDIMG_FROMAPERSONALHOME = 2;    //在个人主页删除视频或者图片
        public static final int DELETEVIDEOANDIMG_FROMVIDEOANDIMGDETAIL = 3;//在视频/图片详情删除视频或者图片

        //映答提问成功
        public static final int ASKQUESTION_RESULT = 112;


    }

    /**
     * banner
     */
    public static class BANNER {
        public static final String BANNER_TARGET_HTML5 = "html5";//h5
        public static final String BANNER_TARGET_VIDEODETAIL = "video";//视频详情
        public static final String BANNER_TARGET_ANSERDETAIL = "answer";//应答详情
        public static final String BANNER_TARGET_PERSONALPAGE = "user";//个人主页
        public static final String BANNER_TARGET_ACTIVITYDETAIL = "activity";//活动详情
    }

    public static class DB {
        public static final String TABLE_NAME_ATTENTION = "attention";
        public static final String TABLE_NAME_FANS = "fans";
    }

    public static class ErrorCode {//错误码
        public static final int ERROR_CODE_NORMAL = 0;//一切正常
        public static final int ERROR_CODE_CHOOSE_LABEL = 800000;//强制选择标签
    }

    /**
     * 渠道相关的,这个变了app build.gradle里面也要对应着变
     */
    public final static HashMap<String, String> CHANNEL_MAP = new HashMap<String, String>() {{
        put("yingyongbao", "miaohi-mhv-android-tencent");//key是我们工程里面配置的渠道名字,value是服务端要求的名字,做一下转换
        put("luodiye", "miaohi-mhv-android-landingh5");
        put("haiqiu", "miaohi-mhv-android-haiqiu");
        put("sinaweibo", "miaohi-mhv-android-sina-weibo");
        put("qihu360", "miaohi-mhv-android-qihu360");
        put("xiaomi", "miaohi-mhv-android-xiaomi");
        put("baidu", "miaohi-mhv-android-baidu");
        put("huawei", "miaohi-mhv-android-huawei");
        put("wandoujia", "miaohi-mhv-android-wandoujia");
        put("meizu", "miaohi-mhv-android-meizu");
        put("vivo", "miaohi-mhv-android-vivo");
        put("oppo", "miaohi-mhv-android-oppo");
        put("gfan", "miaohi-mhv-android-gfan");
        put("pp", "miaohi-mhv-android-pp");
        put("letv", "miaohi-mhv-android-letv");
        put("lenovo", "miaohi-mhv-android-lenovo");
        put("samsung", "miaohi-mhv-android-samsung");
        put("google", "miaohi-mhv-android-google");
        put("smartisan", "miaohi-mhv-android-smartisan");
        put("xian", "miaohi-mhv-android-activity-xian");
        put("guanwang", "miaohi-mhv-android-miaohi");
        put("baidu_tieba", "miaohi-mhv-android-baidu-tieba");
        put("baidu_tieba_1", "miaohi-mhv-android-baidu-tieba_1");
        put("baidu_tieba_2", "miaohi-mhv-android-baidu-tieba_2");
        put("baidu_tieba_3", "miaohi-mhv-android-baidu-tieba_3");
        put("baidu_tieba_4", "miaohi-mhv-android-baidu-tieba_4");
        put("baidu_tieba_5", "miaohi-mhv-android-baidu-tieba_5");
        put("baidu_tieba_6", "miaohi-mhv-android-baidu-tieba_6");
        put("baidu_tieba_7", "miaohi-mhv-android-baidu-tieba_7");
        put("baidu_tieba_8", "miaohi-mhv-android-baidu-tieba_8");
        put("baidu_tieba_9", "miaohi-mhv-android-baidu-tieba_9");
        put("baidu_tieba_10", "miaohi-mhv-android-baidu-tieba_10");
        put("today_toutiao", "miaohi-mhv-android-today-toutiao");
        put("today_toutiao_1", "miaohi-mhv-android-today-toutiao_1");
        put("today_toutiao_2", "miaohi-mhv-android-today-toutiao_2");
        put("today_toutiao_3", "miaohi-mhv-android-today-toutiao_3");
        put("guangdiantong", "miaohi-mhv-android-guangdiantong");
        put("jinli", "miaohi-mhv-android-jinli");
        put("anzhi", "miaohi-mhv-android-anzhi");
        put("sogou", "miaohi-mhv-android-sogou");
        put("yingyonghui", "miaohi-mhv-android-yingyonghui");
        put("ali", "miaohi-mhv-android-ali");
    }};

}
