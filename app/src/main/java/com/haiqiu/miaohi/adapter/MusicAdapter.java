package com.haiqiu.miaohi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiqiu.miaohi.R;
import com.haiqiu.miaohi.bean.MusicInfo;
import com.haiqiu.miaohi.okhttp.MHHttpClient;
import com.haiqiu.miaohi.okhttp.MHHttpProgressHandler;
import com.haiqiu.miaohi.widget.OnItemClickListener;

import java.io.File;
import java.util.List;

/**
 * Created by zhandalin on 2016-09-04 20:47.
 * 说明:配乐adapter
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private Context context;
    private List<MusicInfo> musicInfos;
    private OnItemClickListener onItemClickListener;
    private int lastSelectedPosition;
    private int lastDownloadPosition;

    public MusicAdapter(Context context, List<MusicInfo> musicInfos) {
        this.context = context;
        this.musicInfos = musicInfos;
        musicInfos.get(0).setSelected(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_choose_music, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MusicInfo musicInfo = musicInfos.get(position);
        if (position == 0) {
            holder.iv_filter_preview.setImageResource(R.drawable.xuanzewu);
        } else {
            holder.iv_filter_preview.setImageDrawable(context.getResources().getDrawable(context.getResources().getIdentifier("yinyue" + ((position + 1) % 15), "drawable", context.getPackageName())));
        }

        holder.tv_filter_name.setSelected(musicInfo.isSelected());
        holder.tv_filter_name.setText(musicInfo.getMusic_name());
        if (musicInfo.isSelected()) {
            holder.iv_xuanze.setVisibility(View.VISIBLE);
            holder.iv_white.setVisibility(View.VISIBLE);
            holder.tv_filter_name.setBackgroundResource(0);
        } else {
            holder.iv_xuanze.setVisibility(View.GONE);
            holder.iv_white.setVisibility(View.GONE);
            holder.tv_filter_name.setBackgroundResource(R.color.transparent_80);
        }

        if (musicInfo.getType() == MusicInfo.FROM_NETWORK) {
            holder.iv_cover.setVisibility(musicInfo.getMusic_state() != MusicInfo.MUSIC_STATE_DOWNLOAD_COMPLETE ? View.VISIBLE : View.GONE);
            if (musicInfo.getMusic_state() == MusicInfo.MUSIC_STATE_DOWNLOADING || musicInfo.getMusic_state() == MusicInfo.MUSIC_STATE_DOWNLOAD_FAIL) {
                holder.tv_download_progress.setVisibility(View.VISIBLE);
            } else {
                holder.tv_download_progress.setVisibility(View.GONE);
            }
            holder.tv_download_progress.setText(musicInfo.getDownloadProgressInfo());
        } else {
            holder.iv_cover.setVisibility(View.GONE);
            holder.tv_download_progress.setVisibility(View.GONE);
        }

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return musicInfos.size();
    }

    public MusicInfo getMusicInfo(int position) {
        if (null != musicInfos && musicInfos.size() > position) {
            return musicInfos.get(position);
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_filter_preview;
        public TextView tv_filter_name;
        public View rl_filter_image;
        public View iv_xuanze;
        public View iv_white;
        public View iv_cover;
        public TextView tv_download_progress;
        public int position;

        public ViewHolder(View rootView) {
            super(rootView);
            this.iv_filter_preview = (ImageView) rootView.findViewById(R.id.iv_filter_preview);
            this.tv_filter_name = (TextView) rootView.findViewById(R.id.tv_filter_name);
            this.rl_filter_image = rootView.findViewById(R.id.rl_filter_image);
            this.iv_xuanze = rootView.findViewById(R.id.iv_xuanze);
            this.iv_white = rootView.findViewById(R.id.iv_white);
            this.iv_cover = rootView.findViewById(R.id.iv_cover);
            this.tv_download_progress = (TextView) rootView.findViewById(R.id.tv_download_progress);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MusicInfo musicInfo = musicInfos.get(position);
                    //这个时候需要下载
                    if (musicInfo.getType() == MusicInfo.FROM_NETWORK && musicInfo.getMusic_state() != MusicInfo.MUSIC_STATE_DOWNLOAD_COMPLETE) {
                        downloadMusic(musicInfo, position);
                        return;
                    }
                    selectedPosition(position);
                }
            });
        }

        private void downloadMusic(final MusicInfo musicInfo, final int position) {
            if (lastDownloadPosition == position) return;

            lastDownloadPosition = position;
            final String path = context.getFilesDir().getAbsolutePath() + "/" + musicInfo.getMusic_id() + ".mp3";
            MHHttpClient.getInstance().downloadAsyn(musicInfo.getMusic_uri(), path, new MHHttpProgressHandler() {
                @Override
                public void onProgress(float progress) {
                    musicInfo.setDownloadProgressInfo(String.format("%.1f", 100 * progress) + "%");
                    musicInfo.setMusic_state(MusicInfo.MUSIC_STATE_DOWNLOADING);
                    notifyItemChanged(position);
                }

                @Override
                public void onSuccess(String content) {
                    musicInfo.setDownloadMusicPath(path);
                    musicInfo.setMusic_state(MusicInfo.MUSIC_STATE_DOWNLOAD_COMPLETE);
                    musicInfo.setDownloadProgressInfo("0.0%");
                    notifyItemChanged(position);
                    if (lastDownloadPosition == position) {
                        selectedPosition(position);
                    }
                }

                @Override
                public void onFailure(String content) {
                    super.onFailure(content);
                    musicInfo.setMusic_state(MusicInfo.MUSIC_STATE_DOWNLOAD_FAIL);
                    musicInfo.setDownloadProgressInfo("下载失败");
                    //删除防止下次判断出错
                    File file = new File(path);
                    if (file.exists()) file.delete();
                    notifyItemChanged(position);
                }
            });
        }

        private void selectedPosition(int position) {
            if (lastSelectedPosition == position) return;

            musicInfos.get(lastSelectedPosition).setSelected(false);
            notifyItemChanged(lastSelectedPosition);
            lastSelectedPosition = position;
            musicInfos.get(position).setSelected(true);
            notifyItemChanged(position);
            if (null != onItemClickListener)
                onItemClickListener.OnItemClick(null, position);
        }
    }

    public void addData(List<MusicInfo> musicInfos) {
        if (null == musicInfos || musicInfos.size() == 0) return;
        this.musicInfos.addAll(musicInfos);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (null != musicInfos) musicInfos.clear();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
