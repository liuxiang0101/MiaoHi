package com.haiqiu.miaohi.view;

import android.content.Context;

import com.haiqiu.miaohi.widget.ActionSheetDialog;

/**
 * 选择dialog
 * Created by ningl on 2016/5/26.
 */
public class MHActionSheetDialog extends ActionSheetDialog{

    private ActionSheetDialog dialog;
    private SheetItemColor color;

    public MHActionSheetDialog(Context context, final IMHActionSheetDialogListener iMHActionSheetDialogListener, final String... itemStr) {
        super(context);
        color = SheetItemColor.Blue;
        dialog = new ActionSheetDialog(context).builder();
        for (int i=0; i<itemStr.length; i++){
            final int index = i;
            dialog.addSheetItem(itemStr[i], color, new OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    iMHActionSheetDialogListener.onSelect(itemStr[index], index);
                }
            });
        }
        dialog.setCancelable(true);
        dialog.show();
    }

    public interface IMHActionSheetDialogListener{
        public void onSelect(String select, int index);
    }
}
