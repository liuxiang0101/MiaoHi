/*
 * Android-FFmpegExecutor
 * https://github.com/crust87/Android-FFmpegExecutor
 *
 * Mabi
 * crust87@gmail.com
 * last modify 2016-01-13
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haiqiu.miaohi.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.haiqiu.miaohi.ConstantsValue;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by zhandalin on 2016-05-19 14:48.
 * 说明:主要思路就是把打包好的FFmpeg拷贝到File里面,然后改变文件权限,
 * 在添加一些执行命令,ffmpeg是在init里面添加上的
 * 感谢强大的github
 */
public class FFmpegExecutorUtil {
    private static final String TAG="FFmpegExecutorUtil";
    private Context mContext;
    private Handler mHandler;
    private ArrayList<String> mCommands;
    private ArrayList<ProcessBuilder> mProcessQueue;

    private FFmepgExecuteListener mFFmepgExecuteListener;
    private FFmepgProcessListener mFFmepgProcessListener;
    // Attributes
    private String mFFmpegPath;
    // Working Components
    private Process mCurrentProcess;

    /**
     * 使用现有的文件,要注意的是这个文件要有执行权限
     *
     * @param context    the context of application
     * @param ffmpegPath the String of ffmpegPath, this path must be internal storage
     */
    public FFmpegExecutorUtil(Context context, String ffmpegPath) {
        mContext = context;
        mCommands = new ArrayList<>();
        mProcessQueue = new ArrayList<>();

        mHandler = new Handler();
        mFFmpegPath = ffmpegPath;
    }

    /**
     * Constructor
     * Copy ffmpeg to internal storage and change file permission
     *
     * @param context           the context of application
     * @param ffmpegInputStream the InputStream of ffmpeg binary
     */
    public FFmpegExecutorUtil(Context context, InputStream ffmpegInputStream) {
        try {
            mContext = context;
            mCommands = new ArrayList<>();
            mProcessQueue = new ArrayList<>();

            File ffmpegDirPath = new File(mContext.getFilesDir().getAbsolutePath() + "/ffmpeg");
            if (!ffmpegDirPath.exists()) {
                ffmpegDirPath.mkdir();
            }
            mHandler = new Handler();
            mFFmpegPath = ffmpegDirPath.getAbsolutePath() + "/ffmpeg";

            File ffmpeg = new File(mFFmpegPath);
            if (!ffmpeg.exists()) {
                moveToFile(ffmpegInputStream, ffmpeg);
            }

            String[] args = {"/system/bin/chmod", "755", mFFmpegPath};
            Process process = new ProcessBuilder(args).start();
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    /**
     * 重置,以便下次执行
     */
    public void reset() {
        mCommands.clear();
        mCommands.add(mFFmpegPath);

        mProcessQueue.clear();
    }

    /**
     * @param command the string command for FFmpeg
     * @return this
     */
    public FFmpegExecutorUtil putCommand(String command) {
        mCommands.add(command);

        return this;
    }

    private FFmpegExecutorUtil addQueue() {
        mProcessQueue.add(new ProcessBuilder(mCommands));
        return this;
    }

    private FFmpegExecutorUtil clearQueue() {
        mProcessQueue.clear();

        return this;
    }


    private void executeProcessQueue() throws IOException {
        mHandler.post(mPreExecuteRunnable);

        for (ProcessBuilder builder : mProcessQueue) {
            mHandler.post(mPreProcessRunnable);
            executeProcess(builder);
            mHandler.post(mPostProcessRunnable);
        }

        mHandler.post(mPostExecuteRunnable);

        mProcessQueue.clear();
    }

    private void executeProcessQueueAsync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                onStartExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    for (ProcessBuilder builder : mProcessQueue) {
                        mHandler.post(mPreProcessRunnable);
                        executeProcess(builder);
                        mHandler.post(mPostProcessRunnable);
                    }

                    mProcessQueue.clear();
                } catch (IOException e) {
                    MHLogUtil.e(TAG,e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                onFinishExecute();
            }

        }.execute();
    }

    private void executeProcess() throws IOException {
        executeProcess(new ProcessBuilder(mCommands));
    }

    private void executeProcess(ProcessBuilder builder) throws IOException {
        mCurrentProcess = builder.redirectErrorStream(true).start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(mCurrentProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (mFFmepgExecuteListener != null) {
                mFFmepgExecuteListener.onReadProcessLine(line);
            } else {
                Log.v("FFmpegExecutor", line);
            }
        }
        destroy();
    }

    /**
     * @throws IOException if Process can not start
     */
    public void executeCommand() {
        try {
            mHandler.post(mPreExecuteRunnable);

            executeProcess();

            mHandler.post(mPostExecuteRunnable);
        } catch (Exception e) {
            MHLogUtil.e(TAG,e);
        }
    }

    public void executeCommandAsync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                onStartExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    executeProcess();
                } catch (IOException e) {
                    MHLogUtil.e(TAG,e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                onFinishExecute();
            }

        }.execute();
    }

    private void onStartExecute() {
        if (mFFmepgExecuteListener != null) {
            mFFmepgExecuteListener.onStartExecute();
        }
    }

    private void onFinishExecute() {
        if (mFFmepgExecuteListener != null) {
            mFFmepgExecuteListener.onFinishExecute();
        }
    }

    private Runnable mPreExecuteRunnable = new Runnable() {
        @Override
        public void run() {
            onStartExecute();
        }
    };

    private Runnable mPostExecuteRunnable = new Runnable() {
        @Override
        public void run() {
            onFinishExecute();
        }
    };

    private void onStartProcess() {
        if (mFFmepgProcessListener != null) {
            mFFmepgProcessListener.onStartProcess();
        }
    }

    private void onFinishProcess() {
        if (mFFmepgProcessListener != null) {
            mFFmepgProcessListener.onFinishProcess();
        }
    }

    private Runnable mPreProcessRunnable = new Runnable() {
        @Override
        public void run() {
            onStartProcess();
        }
    };

    private Runnable mPostProcessRunnable = new Runnable() {
        @Override
        public void run() {
            onFinishProcess();
        }
    };

    /*
     * I'm not sure it's work
     */
    public void destroy() {
        if (mCurrentProcess != null) {
            mCurrentProcess.destroy();
            mCurrentProcess = null;
        }
    }

    /**
     * @param ffmepgExecuteListener listener
     */
    public void setFFmepgExecuteListener(FFmepgExecuteListener ffmepgExecuteListener) {
        mFFmepgExecuteListener = ffmepgExecuteListener;
    }

    public interface FFmepgExecuteListener {
        void onStartExecute();

        void onReadProcessLine(String line);

        void onFinishExecute();
    }

    private void setFFmepgProcessListener(FFmepgProcessListener ffmepgProcessListener) {
        mFFmepgProcessListener = ffmepgProcessListener;
    }

    public interface FFmepgProcessListener {
        void onStartProcess();

        void onFinishProcess();
    }

    public static String getArchitecture() {
        return System.getProperty("os.arch");
    }

    public void moveToFile(InputStream mInputStream, File mDestination) throws IOException {
//        boolean ffmpegHasInit = (boolean) SpUtils.get("ffmpegHasInit", false);
        boolean ffmpegHasInit = (boolean) SpUtils.get(ConstantsValue.Sp.FFMPEG_HAS_INIT, false);
        if (ffmpegHasInit) return;

        OutputStream destinationOut = new BufferedOutputStream(new FileOutputStream(mDestination));
        int numRead;
        byte[] buf = new byte[1024];
        while ((numRead = mInputStream.read(buf)) >= 0) {
            destinationOut.write(buf, 0, numRead);
        }
        destinationOut.flush();
        destinationOut.close();
//        SpUtils.put("ffmpegHasInit", true);
        SpUtils.put(ConstantsValue.Sp.FFMPEG_HAS_INIT, true);
    }
}
