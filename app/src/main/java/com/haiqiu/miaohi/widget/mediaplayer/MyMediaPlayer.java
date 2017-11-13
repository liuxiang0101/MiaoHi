package com.haiqiu.miaohi.widget.mediaplayer;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by zhandalin on 2016-06-06 15:41.
 * 说明:用于获取播放器的各种事件,用于通知控制器,改变状态
 */
public class MyMediaPlayer extends IjkMediaPlayer {

    private MyPlayerEventListener playerEventListener;

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        if (null != playerEventListener) playerEventListener.mediaPlayerPause();
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        if (null != playerEventListener) playerEventListener.mediaPlayerStart();
    }


    @Override
    public void reset() {
        super.reset();
        if (null != playerEventListener) playerEventListener.mediaPlayerReset();
    }

    @Override
    public void release() {
        super.release();
        if (null != playerEventListener) playerEventListener.mediaPlayerRelease();
    }

    public interface MyPlayerEventListener {
        void mediaPlayerPause();

        void mediaPlayerStart();

        void mediaPlayerReset();

        void mediaPlayerRelease();
    }

    public void setPlayerEventListener(MyPlayerEventListener mediaPlayerEventListener) {
        this.playerEventListener = mediaPlayerEventListener;
    }
}
