package com.flash.light.free.good.fashioncallflash.net;

import com.flash.light.free.good.fashioncallflash.db.DataBaseTool;
import com.flash.light.free.good.fashioncallflash.db.ThemeContent;
import com.flash.light.free.good.fashioncallflash.util.Logger;
import com.flash.light.free.good.fashioncallflash.util.UtilsKt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetTool {

    public static void downloadWallPaper(String downloadUrl, DownloadCallBack callBack) {

        String imageName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);

        FileOutputStream fops = null;
        InputStream inStream = null;
        try {
            ThemeContent themeContent = DataBaseTool.Companion.getInstance().find(downloadUrl);
            if (themeContent == null) {
                callBack.downloadError();
                return;
            }

            File floadFile = new File(UtilsKt.getSaveFloadAddress());
            if (!floadFile.exists()) {
                if (!floadFile.mkdirs()) {
                    Logger.INSTANCE.d("创建文件夹失败");
                }
            } else {
                File[] listFile = floadFile.listFiles();
                if (listFile == null || listFile.length == 0) {
                    floadFile.delete();
                    File newFload = new File(UtilsKt.getSaveFloadAddress());
                    newFload.mkdirs();
                }
            }
            File file = new File(UtilsKt.getSaveFloadAddress(), imageName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Logger.INSTANCE.d("创建文件失败");
                }
            } else {
                if (themeContent.isDownload()){
                    callBack.downloadSuccess(file.getAbsolutePath());
                    return;
                }
            }

            URL url = new URL(downloadUrl);
            //得到connection对象。
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            //连接
            connection.connect();
            //得到响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                Long imageLength = Long.parseLong(connection.getHeaderField("Content-Length"));

                inStream = connection.getInputStream();//通过输入流获取图片数据

                byte[] buffer = new byte[1024 * 5];
                int len;

                fops = new FileOutputStream(file);

                Long saveData = 0L;

                while ((len = inStream.read(buffer)) != -1) {
                    saveData += len;
                    fops.write(buffer, 0, len);
                }

                if (imageLength.longValue() == saveData.longValue()) {
                    themeContent.setPath(file.getAbsolutePath());
                    themeContent.setDownload(true);
                    DataBaseTool.Companion.getInstance().updateWords(themeContent);
                    callBack.downloadSuccess(file.getAbsolutePath());
                } else {
                    callBack.downloadError();
                }
            } else {
                callBack.downloadError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            callBack.downloadError();
        } finally {
            try {
                assert inStream != null;
                inStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                assert fops != null;
                fops.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
