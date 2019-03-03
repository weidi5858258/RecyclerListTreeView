package com.weidi;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;

import com.niuedu.ListTree;

/**
 * Created by weidi on 2019/3/2.
 */

public class AndroidListTreeViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG =
            AndroidListTreeViewHolder.class.getSimpleName();

    protected ViewGroup containerView;
    protected ImageView arrowIcon;
    protected Space headSpace;

    public AndroidListTreeViewHolder(final AndroidListTree tree, View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(true);
                v.setFocusable(true);
                v.requestFocus();

                Log.i(TAG, "onClick()");

                /*int planePos = getAdapterPosition();
                ItemNode node = tree.getNodeWithPosition(planePos);
                if (node.mNeedToShow) {
                    int nodePlaneIndex = tree.getNodeWithPosition(node);
                    if (node.mIsExpanded) {
                        //收起
                        int count = tree.collapseNode(nodePlaneIndex);
                        notifyItemChanged(nodePlaneIndex);
                        //通知view将相关的行删掉
                        notifyItemRangeRemoved(nodePlaneIndex + 1, count);
                    } else {
                        //展开
                        int count = tree.expandNode(nodePlaneIndex);
                        notifyItemChanged(nodePlaneIndex);
                        //通知view插入相关的行
                        notifyItemRangeInserted(nodePlaneIndex + 1, count);
                    }
                }*/
            }
        });
    }

}
