package com.flash.light.free.good.fashioncallflash.tool;

import android.text.TextUtils;

import com.flash.light.free.good.fashioncallflash.content.NotiContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 通知管理类
 */
public class MessageManager {

    private static MessageManager messageManager;

    private List<NotiContent> showList;//展示通知集合
    private List<String> notiKey;//key集合
    private Map<String, Integer> notiContnet;//展示内容集合
    private List<String> noNotifyList;

    private MessageManager() {
        noNotifyList = new ArrayList<>();
        showList = new ArrayList<>();
        notiKey = new ArrayList<>();
        notiContnet = new HashMap<>();
        noNotifyList.add("android");
    }

    public static MessageManager getInstence() {
        if (messageManager == null) {
            synchronized (MessageManager.class) {
                if (messageManager == null) {
                    messageManager = new MessageManager();
                }
            }
        }
        return messageManager;
    }

    public static void destroy() {
        messageManager = null;
    }

    /**
     * 获取不管理通知app包名
     */
    public List<String> getNoNotifyAPPs() {
        return noNotifyList;
    }

    /**
     * 获取所有通知
     */
    public List<NotiContent> getAllNotify() {
        return showList;
    }

    /**
     * 更新展示通知
     */
    public void refreshShowList(List<NotiContent> list) {
        boolean refreshView = false;
        for (NotiContent notiContent : list) {
            String key = notiContent.getKey();
            if (!notiKey.contains(key)) {
                refreshView = true;
                showList.add(notiContent);
                notiKey.add(key);
                notiContnet.put(key, 1);
            } else {
                //更新内容
                if (!notiContnet.containsKey(key)) {
                    notiContnet.put(key, 1);
                } else {
                    int newNum = notiContnet.get(key) + 1;
                    notiContnet.put(key, newNum);
//                    String newContent = String.format(CallApplication.getContext().getResources().getString(R.string.notify_new_content), newNum + "");
                    for (NotiContent content : showList) {
                        if (TextUtils.equals(content.getKey(), key)) {
                            refreshView = true;
//                            content.setContent(newContent);
                        }
                    }
                }
            }
        }

        if (refreshView) {
            //刷新界面
//            ScreenLockWindow.getInstence().refreshNotify(showList);
        }
    }

    /**
     * 删除通知
     *
     * @param content 通知
     */
    public void deleteNotify(NotiContent content) {
        showList.remove(content);
        notiKey.remove(content.getKey());
        notiContnet.remove(content.getKey());
    }

    /**
     * 清除所有通知
     */
    public void deleteAllNotify() {
        showList.clear();
        notiKey.clear();
        notiContnet.clear();
    }

}
