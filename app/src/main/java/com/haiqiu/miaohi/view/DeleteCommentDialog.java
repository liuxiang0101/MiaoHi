package com.haiqiu.miaohi.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.utils.ScreenUtils;

/**
 * 视频详情/图片详情删除dialog
 * Created by ningl on 17/1/13.
 */

public class DeleteCommentDialog extends Dialog {

    private IDeleteComment iDeleteComment;

    public DeleteCommentDialog(Context context) {
        super(context, R.style.DeleteCommentDialog);
        View view = View.inflate(context, R.layout.dialog_deletecomment, null);
        setContentView(view);
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = ScreenUtils.getScreenSize(context).x;
        win.setAttributes(lp);
        view.findViewById(R.id.tv_deletecomment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iDeleteComment != null) iDeleteComment.onDeleteComment();
            }
        });
    }

    public interface IDeleteComment{
        void onDeleteComment();
    }

    public void setOnDeleteCommentListener(IDeleteComment iDeleteComment){
        this.iDeleteComment = iDeleteComment;
    }
}
