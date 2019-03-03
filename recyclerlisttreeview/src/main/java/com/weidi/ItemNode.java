package com.weidi;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weidi on 2019/3/2.
 */

public class ItemNode {

    //实际的数据
    public Object data;
    //本node所使用的layout id
    public int mLayoutResId;
    //是否是展开的(默认都是不展开的)
    public boolean mIsExpanded = true;
    public boolean mNeedToShow = true;
    //mParentNode为null时,表示此节点为root节点
    public ItemNode mParentNode;
    public List<ItemNode> mChildNodes = new ArrayList<>();
    public String mTitle;
    public String mPath = "";
    // 层级
    public int mHierarchy;

    public ItemNode(String title) {
        if (TextUtils.isEmpty(title)) {
            throw new IllegalArgumentException("ItemNode's title is empty!");
        }
        mTitle = title;
    }

}
