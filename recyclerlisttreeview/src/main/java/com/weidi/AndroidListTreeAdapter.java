package com.weidi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import niuedu.com.R;

public abstract class AndroidListTreeAdapter<VH extends AndroidListTreeViewHolder>
        extends RecyclerView.Adapter<VH> {

    private static final String TAG = AndroidListTreeAdapter.class.getSimpleName();

    protected Context mContext = null;
    protected AndroidListTree tree = null;
    private Bitmap mExpandIcon = null;
    private Bitmap mCollapseIcon = null;

    public AndroidListTreeAdapter(Context context, AndroidListTree tree) {
        this.mContext = context;
        this.tree = tree;
    }

    @Override
    final public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mCollapseIcon == null) {
            mCollapseIcon = BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.expand);
        }
        if (mExpandIcon == null) {
            mExpandIcon = BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.collapse);
        }

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup container = (ViewGroup) inflater.inflate(
                R.layout.row_container_layout, parent, false);

        ImageView arrowIcon = container.findViewById(R.id.listtree_arrowIcon);
        int w = parent.getMeasuredWidth();
        arrowIcon.getLayoutParams().width = w / 15;
        arrowIcon.getLayoutParams().height = w / 15;

        VH vh = onCreateNodeView(container, viewType);
        if (vh == null) {
            return null;
        }

        vh.containerView = container;
        vh.arrowIcon = arrowIcon;
        vh.headSpace = container.findViewById(R.id.listtree_head_space);
        //vh.initView();

        return vh;
    }

    @Override
    final public void onBindViewHolder(VH holder, int position) {
        //get node at the position
        ItemNode node = tree.getNodeWithPosition(position);
        if (node.mNeedToShow) {
            if (node.mIsExpanded) {
                holder.arrowIcon.setImageBitmap(mExpandIcon);
            } else {
                holder.arrowIcon.setImageBitmap(mCollapseIcon);
            }
        } else {
            holder.arrowIcon.setImageBitmap(null);
        }

        ViewGroup.LayoutParams params = holder.arrowIcon.getLayoutParams();

        int layer = node.mHierarchy;
        int space = layer * 44;
        holder.headSpace.getLayoutParams().width = space;
        space += params.width + 5;
        Log.i(TAG, "onBindViewHolder() space: " + space);

        //给子类机会去绑定行数据
        onBindNodeViewHolder(holder, position, space);
    }

    @Override
    final public int getItemViewType(int position) {
        ItemNode node = tree.getNodeWithPosition(position);
        return node.mLayoutResId;
    }

    @Override
    final public int getItemCount() {
        return tree.getShowedNodes().size();
    }

    protected abstract VH onCreateNodeView(ViewGroup parent, int viewType);

    protected abstract void onBindNodeViewHolder(VH viewHoler, int position, int space);

    /*public void notifyTreeItemInserted(ListTree.TreeNode parent, ListTree.TreeNode node) {
        int parentPlaneIndex = tree.getNodePlaneIndex(parent);
        if (parent.isExpanded()) {
            super.notifyItemInserted(tree.getNodePlaneIndex(node));
        } else {
            //未展开，需展开爸爸
            int count = tree.expandNode(parentPlaneIndex);
            //通知改变爸爸的状态
            super.notifyItemChanged(parentPlaneIndex);
            //通知更新展开的子孙行们
            notifyItemRangeInserted(parentPlaneIndex + 1, parent.getDescendantCount());
        }
    }*/

}