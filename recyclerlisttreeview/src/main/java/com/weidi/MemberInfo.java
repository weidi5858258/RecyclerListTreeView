package com.weidi;

import android.graphics.Bitmap;

/**
 * Created by weidi on 2019/3/3.
 */

public class MemberInfo {

    public Bitmap bitmap;
    public String title;

    public MemberInfo() {
    }

    public MemberInfo(Bitmap bitmap, String title) {
        this.bitmap = bitmap;
        this.title = title;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "title='" + title + '\'' +
                '}';
    }

}
