package com.viewcore.test;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baofeng.mojing.MojingSDK;
import com.bfmj.viewcore.view.BaseViewActivity;
import com.bfmj.viewcore.view.GLModelView;
import com.bfmj.viewcore.view.GLRootView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Model3dActivity extends BaseViewActivity {

    private GLRootView rootView;
    private MyModelView modelView;

    public class MyModelView extends GLModelView {
        public boolean groyEnable = false;

        public MyModelView(Context context) {
            super(context);
            float[] prjMatrix = new float[16];
            float[] mvpMatrix = new float[16];
            float[] VMatrix = new float[16];
            Matrix.setLookAtM(VMatrix, 0,
                    0, 0, 15,
                    0, 0, 0,
                    0, 1, 0);
            Matrix.frustumM(prjMatrix, 0,
                    -1, 1,
                    -1, 1,
                    3, 2000);

            Matrix.multiplyMM(mvpMatrix, 0,
                    prjMatrix, 0,
                    VMatrix, 0);
            Matrix.rotateM(mvpMatrix, 0,
                    135, 0.0f, 1.0f, 0);
            this.setMatrix(mvpMatrix);
        }

        @Override
        public void draw() {
            if (groyEnable) {
                float[] tmpMatrix = new float[16];
                MojingSDK.getLastHeadView(tmpMatrix);
                float[] prjMatrix = new float[16];
                float[] mvpMatrix = new float[16];
                float[] VMatrix = new float[16];
                Matrix.setLookAtM(VMatrix, 0,
                        0, 0, 15,
                        0, 0, 0,
                        0, 1, 0);
                Matrix.frustumM(prjMatrix, 0,
                        -1, 1,
                        -1, 1,
                        3, 2000);

                Matrix.multiplyMM(mvpMatrix, 0,
                        VMatrix, 0, tmpMatrix, 0);

                Matrix.multiplyMM(mvpMatrix, 0,
                        prjMatrix, 0,
                        mvpMatrix, 0);
                this.setMatrix(mvpMatrix);
            }
            super.draw();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_model3d);

        rootView = getRootView();

        modelView = new MyModelView(this);
        if (modelView != null) {
            modelView.groyEnable = isGroyEnable();

            String objPath = "amenemhat";
            String filePath = getFilesDir().getAbsolutePath() + "/" + objPath;
            String fileName = filePath + "/amenemhat.obj";
            copyFilesFassets(this, objPath, filePath);

//            modelView.loadModel("/sdcard/model/amenemhat/amenemhat.obj");
            modelView.loadModel(fileName);
            rootView.addView(modelView);
        }

        rootView.onResume();
        setDistortionEnable(true);
    }


    String getFileName(String fileName) {
        String onlyName = "";
        int slashIndex = fileName.lastIndexOf('/');

        if (slashIndex != -1) {
            onlyName = fileName.substring(slashIndex + 1);
        }
        return onlyName;
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
