package com.example.luoxiao.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.luoxiao.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by luoxiao on 2015/10/16.
 */
public class MyApplication extends Application {
    private static final String TAG = "MyAPP";
    private static Application mApplication;
    private CityDB mCityDB;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyApplication->onCreate");
        mApplication = this;
        mCityDB = openCityDB();
    }

    public static Application getInstance() {
        return mApplication;
    }

    private CityDB openCityDB() {
        String dir_path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases"
                + File.separator;
        String db_path = dir_path + CityDB.CITY_DB_NAME;
        File dir = new File(dir_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File db = new File(db_path);
        if (!db.exists()) {
            Log.i("MyAPP", "db does not exist");
            // 如果不存在city2.db，新建一个，
            // 再把存在assets文件夹下的city.db里的内容复制到city2.db中。
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    int size = buffer.length;
                    fos.write(buffer, 0, size);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, db_path);
    }

    public CityDB getCityDB() {
        return mCityDB;
    }
}
