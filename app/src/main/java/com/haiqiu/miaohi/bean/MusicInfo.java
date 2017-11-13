package com.haiqiu.miaohi.bean;

/**
 * Created by zhandalin on 2016-12-05 16:49.
 * 说明:
 */
public class MusicInfo {
    public final static int FROM_NETWORK = 0;
    public final static int FROM_LOCAL = 1;

    public final static int MUSIC_STATE_WAIT_DOWNLOAD = 0;
    public final static int MUSIC_STATE_DOWNLOADING = 1;
    public final static int MUSIC_STATE_DOWNLOAD_COMPLETE = 2;
    public final static int MUSIC_STATE_DOWNLOAD_FAIL = 3;

    private String music_id;
    private String music_name;
    private String music_uri;
    private long music_duration;
    private String imagePath;

    private String downloadProgressInfo;
    private String downloadMusicPath;

    private int type;
    private boolean isSelected;

    private int music_state;//音乐状态

    public String getMusic_name() {
        return music_name;
    }

    public String getMusic_uri() {
        return music_uri;
    }

    public long getMusic_duration() {
        return music_duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getMusic_state() {
        return music_state;
    }

    public String getMusic_id() {
        return music_id;
    }

    public void setMusic_state(int music_state) {
        this.music_state = music_state;
    }

    public String getDownloadMusicPath() {
        return downloadMusicPath;
    }

    public void setDownloadMusicPath(String downloadMusicPath) {
        this.downloadMusicPath = downloadMusicPath;
    }

    public String getDownloadProgressInfo() {
        return downloadProgressInfo;
    }

    public void setDownloadProgressInfo(String downloadProgressInfo) {
        this.downloadProgressInfo = downloadProgressInfo;
    }
}
