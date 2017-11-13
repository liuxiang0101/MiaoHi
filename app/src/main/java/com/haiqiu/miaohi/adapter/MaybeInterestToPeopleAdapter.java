package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiqiu.miaohi.ConstantsValue;
import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.activity.MaybeInterestToPeopleActivity;
import com.haiqiu.miaohi.activity.PersonalHomeActivity;
import com.haiqiu.miaohi.activity.VideoAndImgActivity;
import com.haiqiu.miaohi.base.BaseActivity;
import com.haiqiu.miaohi.bean.MaybeInterestObjList;
import com.haiqiu.miaohi.bean.MaybeInterestUserList;
import com.haiqiu.miaohi.bean.VideoAndImg;
import com.haiqiu.miaohi.fragment.LoginDialogFragment;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpHandler;
import com.haiqiu.miaohi.okhttp.MHRequestParams;
import com.haiqiu.miaohi.response.BaseResponse;
import com.haiqiu.miaohi.utils.ChangeAttentionStateUtil;
import com.haiqiu.miaohi.utils.DisplayOptionsUtils;
import com.haiqiu.miaohi.utils.MHStateSyncUtil;
import com.haiqiu.miaohi.utils.MHStringUtils;
import com.haiqiu.miaohi.utils.ScreenUtils;
import com.haiqiu.miaohi.utils.SpUtils;
import com.haiqiu.miaohi.utils.callback.ChangeAttentionStateCallBack;
import com.haiqiu.miaohi.view.MyCircleView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ningl on 16/11/29.
 * 可能感兴趣的人适配器
 */
public class MaybeInterestToPeopleAdapter extends RecyclerView.Adapter<MaybeInterestToPeopleAdapter.ViewHolder> {
    private int screenWith;
    private Context context;
    private ImageLoader imageLoader;
    private List<MaybeInterestUserList> data;

    public MaybeInterestToPeopleAdapter(List<MaybeInterestUserList> data, Context context) {
        this.imageLoader = ImageLoader.getInstance();
        this.data = data;
        this.context = context;
        screenWith = ScreenUtils.getScreenWidth(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_maybeinterestpeople, null));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MaybeInterestUserList obj = data.get(position);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height = screenWith / 3;
        holder.ll_maybeinterest.setLayoutParams(lp);
        //感兴趣的人名称
        holder.tv_maybeinterest_name.setText(obj.getUser_name());
        //是否已关注显示
        MHStateSyncUtil.State syncState = MHStateSyncUtil.getSyncState(obj.getUser_id());
        if (MHStateSyncUtil.State.ATTENTION_STATE_NOT_FOUND != syncState) {
            obj.setAttention_state(MHStateSyncUtil.State.ATTENTION_STATE_IS_TRUE == syncState);
        }
        if (obj.isAttention_state()) {
            holder.tv_maybeinterest_attention.setText("已关注");
            holder.tv_maybeinterest_attention.setTextColor(context.getResources().getColor(R.color.color_c4));
            holder.tv_maybeinterest_nolike.setVisibility(View.INVISIBLE);
            holder.tv_maybeinterest_attention.setBackgroundResource(R.drawable.tag_bg);
        } else {
            holder.tv_maybeinterest_attention.setText("关注");
            holder.tv_maybeinterest_attention.setTextColor(context.getResources().getColor(R.color.fontblue));
            holder.tv_maybeinterest_attention.setBackgroundResource(R.drawable.shape_attention_blue_selector);
            holder.tv_maybeinterest_nolike.setVisibility(View.VISIBLE);
        }
        //头像显示
        imageLoader.displayImage(obj.getPortrait_uri(), holder.mcv_maybeinterest, DisplayOptionsUtils.getSilenceDisplayBuilder());
        //性别
        holder.iv_gender.setImageResource(obj.getUser_gender() == 1 ? R.drawable.gender_man : R.drawable.gender_women);
        //是否开通映答
        holder.iv_qa.setVisibility(obj.getAnswer_auth() == 1 ? View.VISIBLE : View.INVISIBLE);
        //是否VIP
        holder.iv_vip_mark.setVisibility(obj.getUser_type() > 10 ? View.VISIBLE : View.INVISIBLE);
        //用户头衔显示
        if (MHStringUtils.isEmpty(obj.getUser_note())) {
            holder.tv_maybeinterest_user_title.setVisibility(View.GONE);
        } else {
            holder.tv_maybeinterest_user_title.setVisibility(View.VISIBLE);
            holder.tv_maybeinterest_user_title.setText(obj.getUser_note());
        }

        //设置用户作品，最多展示3个
        final List<MaybeInterestObjList> listObj = obj.getObject_list();
        holder.iv_maybeinterest_middle.setVisibility(View.INVISIBLE);
        holder.iv_maybeinterest_middlevideo.setVisibility(View.INVISIBLE);
        holder.iv_maybeinterest_right.setVisibility(View.INVISIBLE);
        holder.iv_maybeinterest_rightvideo.setVisibility(View.INVISIBLE);
        //用户是否有作品
        if (listObj != null) {
            if (listObj.size() == 0)
                holder.ll_maybeinterest.setVisibility(View.GONE);
            else
                holder.ll_maybeinterest.setVisibility(View.VISIBLE);
        } else
            holder.ll_maybeinterest.setVisibility(View.GONE);
        //用户的精选的三个作品的展示逻辑
        holder.iv_maybeinterest_left.setVisibility(View.INVISIBLE);
        holder.iv_maybeinterest_middle.setVisibility(View.INVISIBLE);
        holder.iv_maybeinterest_right.setVisibility(View.INVISIBLE);
        holder.iv_maybeinterest_leftvideo.setVisibility(View.INVISIBLE);
        holder.iv_maybeinterest_middlevideo.setVisibility(View.INVISIBLE);
        holder.iv_maybeinterest_rightvideo.setVisibility(View.INVISIBLE);
        if (listObj != null)
            if (listObj.size() >= 0) {
                switch (listObj.size()) {
                    case 0:
                        break;
                    default:
                    case 3:
                        holder.iv_maybeinterest_right.setVisibility(View.VISIBLE);
                        if (listObj.get(2).getObject_type() == 1) {
                            holder.iv_maybeinterest_rightvideo.setVisibility(View.VISIBLE);
                        } else {
                            holder.iv_maybeinterest_rightvideo.setVisibility(View.INVISIBLE);
                        }
                        if (listObj.get(2).getObject_img_uri_state() == 10)
                            imageLoader.displayImage(listObj.get(2).getObject_img_uri(), holder.iv_maybeinterest_right);
                        holder.iv_maybeinterest_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enterDetail(listObj.get(2).getObject_type(), listObj.get(2).getObject_id());
                            }
                        });
                    case 2:
                        holder.iv_maybeinterest_middle.setVisibility(View.VISIBLE);
                        if (listObj.get(1).getObject_type() == 1) {
                            holder.iv_maybeinterest_middlevideo.setVisibility(View.VISIBLE);
                        } else {
                            holder.iv_maybeinterest_middlevideo.setVisibility(View.INVISIBLE);
                        }
                        if (listObj.get(1).getObject_img_uri_state() == 10)
                            imageLoader.displayImage(listObj.get(1).getObject_img_uri(), holder.iv_maybeinterest_middle, DisplayOptionsUtils.getSilenceDisplayBuilder());
                        holder.iv_maybeinterest_middle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enterDetail(listObj.get(1).getObject_type(), listObj.get(1).getObject_id());
                            }
                        });
                    case 1:
                        holder.iv_maybeinterest_left.setVisibility(View.VISIBLE);
                        if (listObj.get(0).getObject_type() == 1) {
                            holder.iv_maybeinterest_leftvideo.setVisibility(View.VISIBLE);
                        } else {
                            holder.iv_maybeinterest_leftvideo.setVisibility(View.INVISIBLE);
                        }
                        if (listObj.get(0).getObject_img_uri_state() == 10)
                            imageLoader.displayImage(listObj.get(0).getObject_img_uri(), holder.iv_maybeinterest_left, DisplayOptionsUtils.getSilenceDisplayBuilder());
                        holder.iv_maybeinterest_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enterDetail(listObj.get(0).getObject_type(), listObj.get(0).getObject_id());
                            }
                        });
                        break;
                }
            }
        //点击整个条目跳转
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogined()) {
                    Intent intent = new Intent(context, PersonalHomeActivity.class);
                    intent.putExtra("userId", obj.getUser_id());
                    context.startActivity(intent);
                }
            }
        });
        //点击头像跳转
        holder.mcv_maybeinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogined())
                    context.startActivity(new Intent(context, PersonalHomeActivity.class).putExtra("userId", obj.getUser_id()));
            }
        });
        //点击不喜欢按钮
        holder.tv_maybeinterest_nolike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogined()) {
                    unLikeThis(data.get(position).getUser_id());

                    data.remove(position);
                    notifyItemRemoved(position);
                    if (position != data.size())
                        notifyItemRangeChanged(position, data.size() - position);
                }
                TCAgent.onEvent(context, "推人不喜欢" + ConstantsValue.android);
            }
        });
        //点击关注按钮
        holder.tv_maybeinterest_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAttentionStateUtil stateUtil = new ChangeAttentionStateUtil((BaseActivity) context);
                stateUtil.changeAttentionState(obj.getUser_id(), obj.getAttention_state() != 1, v, holder.progress_bar, new ChangeAttentionStateCallBack() {
                    @Override
                    public void callBackInfo(boolean attentionState) {
                        obj.setAttention_state(attentionState);
                        notifyDataSetChanged();
                    }
                });
                TCAgent.onEvent(context, "推人关注" + ConstantsValue.android);
            }
        });
    }

    private void enterDetail(int type, String objectId) {
        ArrayList<VideoAndImg> data = new ArrayList<>();
        VideoAndImg obj = new VideoAndImg();
        obj.setElement_type(type);
        obj.setContent_type(type);
        obj.setPhoto_id(objectId);
        obj.setVideo_id(objectId);
        data.add(obj);
        Intent intent = new Intent(context, VideoAndImgActivity.class);
        intent.putParcelableArrayListExtra("data", data)
                .putExtra("currentIndex", 0)
                .putExtra("userId", objectId)
                .putExtra("pageIndex", 0)
                .putExtra("command", ConstantsValue.Url.GETALLUSERPHONTSANDVIDEOS);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MyCircleView mcv_maybeinterest;
        private TextView tv_maybeinterest_name;
        private TextView tv_maybeinterest_nolike;
        private TextView tv_maybeinterest_attention;
        private TextView tv_maybeinterest_user_title;
        private ImageView iv_gender;
        private ImageView iv_qa;
        private ImageView iv_vip_mark;
        private ImageView iv_maybeinterest_middle;
        private ImageView iv_maybeinterest_leftvideo;
        private ImageView iv_maybeinterest_left;
        private ImageView iv_maybeinterest_middlevideo;
        private ImageView iv_maybeinterest_right;
        private ImageView iv_maybeinterest_rightvideo;
        private LinearLayout ll_maybeinterest;
        private View progress_bar;

        public ViewHolder(View itemView) {
            super(itemView);
            mcv_maybeinterest = (MyCircleView) itemView.findViewById(R.id.mcv_maybeinterest);
            tv_maybeinterest_name = (TextView) itemView.findViewById(R.id.tv_maybeinterest_name);
            tv_maybeinterest_user_title = (TextView) itemView.findViewById(R.id.tv_maybeinterest_user_title);
            tv_maybeinterest_nolike = (TextView) itemView.findViewById(R.id.tv_maybeinterest_nolike);
            tv_maybeinterest_attention = (TextView) itemView.findViewById(R.id.tv_maybeinterest_attention);
            iv_gender = (ImageView) itemView.findViewById(R.id.iv_gender);
            iv_qa = (ImageView) itemView.findViewById(R.id.iv_qa);
            iv_vip_mark = (ImageView) itemView.findViewById(R.id.iv_vip_mark);
            iv_maybeinterest_middle = (ImageView) itemView.findViewById(R.id.iv_maybeinterest_middle);
            iv_maybeinterest_leftvideo = (ImageView) itemView.findViewById(R.id.iv_maybeinterest_leftvideo);
            iv_maybeinterest_left = (ImageView) itemView.findViewById(R.id.iv_maybeinterest_left);
            iv_maybeinterest_middlevideo = (ImageView) itemView.findViewById(R.id.iv_maybeinterest_middlevideo);
            iv_maybeinterest_right = (ImageView) itemView.findViewById(R.id.iv_maybeinterest_right);
            iv_maybeinterest_rightvideo = (ImageView) itemView.findViewById(R.id.iv_maybeinterest_rightvideo);
            ll_maybeinterest = (LinearLayout) itemView.findViewById(R.id.ll_maybeinterest);
            progress_bar = itemView.findViewById(R.id.progress_bar);
        }
    }

    //点击不喜欢按钮
    private void unLikeThis(String object_id) {
        MHRequestParams requestParams = new MHRequestParams();
        requestParams.addParams("object_id", object_id);
        requestParams.addParams("object_type", "1");//类型  1:人物 2：视频 3：映答 4：图片
        MHHttpClient.getInstance().post(BaseResponse.class, context, ConstantsValue.Url.UNINTERESTOBJECT, requestParams, new MHHttpHandler<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
//                Toast.makeText(context, "提交成功,我们会减少此类内容的推荐", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String content) {

            }

        });
    }

    private boolean isLogined() {
        if (MHStringUtils.isEmpty(SpUtils.getString(ConstantsValue.Sp.TOKEN_MIAOHI))) {
            LoginDialogFragment loginDialog = new LoginDialogFragment();
            loginDialog.show(((MaybeInterestToPeopleActivity) context).getSupportFragmentManager(), "loginDialog");
            return false;
        }
        return true;
    }

}
