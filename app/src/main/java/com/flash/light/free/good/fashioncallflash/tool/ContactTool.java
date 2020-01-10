package com.flash.light.free.good.fashioncallflash.tool;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.flash.light.free.good.fashioncallflash.CallApplication;
import com.flash.light.free.good.fashioncallflash.content.ContactContent;
import com.flash.light.free.good.fashioncallflash.util.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 联系人工具类
 */
public class ContactTool {

    private static ContactTool contactTool;
    private Map<String, ContactContent> contactContentList;
    private ContentResolver contentResolver;

    private ContactTool() {
        contactContentList = new HashMap<>();
        contentResolver = CallApplication.getContext().getContentResolver();
    }

    public static ContactTool getInstence() {
        if (contactTool == null) {
            synchronized (ContactTool.class) {
                if (contactTool == null) {
                    contactTool = new ContactTool();
                }
            }
        }
        return contactTool;
    }

    public static void destroy() {
        contactTool = null;
    }

    /**
     * 获取所有联系人信息
     */
    public void getAllContact() {
        contactContentList.clear();
        new Thread(new getAllContactThr()).start();
    }

    private class getAllContactThr implements Runnable{

        @Override
        public void run() {
            try {
                //获取联系人信息的Uri
                Uri uri = ContactsContract.Contacts.CONTENT_URI;
                //查询数据，返回Cursor
                Cursor cursor = contentResolver.query(uri, null, null, null, null);

                while (cursor.moveToNext()) {
                    ContactContent contactContent = new ContactContent();
                    //获取联系人的ID
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract
                            .Contacts._ID));
                    contactContent.setId(contactId);
                    //获取联系人的姓名
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts
                            .DISPLAY_NAME));
                    contactContent.setName(name);
                    //查询电话类型的数据操作
                    Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone
                            .CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                            "" + " = " + contactId, null, null);

                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract
                                .CommonDataKinds.Phone.NUMBER));
                        phoneNumber = phoneNumber.replaceAll(" ","");
                        phoneNumber = phoneNumber.replaceAll("\\+","");
                        phoneNumber = phoneNumber.replaceAll("-","");
                        contactContent.setPhone(phoneNumber);
                        contactContentList.put(phoneNumber, contactContent);
                    }
                    phones.close();
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Logger.INSTANCE.d( "联系人获取完成" + contactContentList.size());
        }
    }

    /**
     * 获取联系人的图片
     */
    private Bitmap getPhoto(String contactId) {
        Bitmap photo = null;
        Cursor dataCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, new
                String[]{"data15"}, ContactsContract.Data.CONTACT_ID + "=?" + " AND " +
                ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Photo
                .CONTENT_ITEM_TYPE + "'", new String[]{String.valueOf(contactId)}, null);
        if (dataCursor != null) {
            if (dataCursor.getCount() > 0) {
                dataCursor.moveToFirst();
                byte[] bytes = dataCursor.getBlob(dataCursor.getColumnIndex("data15"));
                if (bytes != null) {
                    photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                }
            }
            dataCursor.close();
        }
        return photo;
    }

    /**
     * 判断联系人
     */
    public ContactContent checkContact(String phoneNum) {
        try {
            if (contactContentList.containsKey(phoneNum)) {
                ContactContent contactContent = contactContentList.get(phoneNum);
                contactContent.setIcon(getPhoto(contactContent.getId()));
                return contactContent;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
