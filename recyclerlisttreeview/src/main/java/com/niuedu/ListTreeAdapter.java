package com.niuedu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;

import niuedu.com.R;

public abstract class ListTreeAdapter<VH extends ListTreeAdapter.ListTreeViewHolder>
        extends RecyclerView.Adapter<VH> {

    private static final String TAG = ListTreeAdapter.class.getSimpleName();

    protected Context mContext = null;
    protected ListTree tree = null;
    private Bitmap expandIcon = null;
    private Bitmap collapseIcon = null;

    public ListTreeAdapter(Context context, ListTree tree) {
        this.mContext = context;
        this.tree = tree;
    }

    @Override
    final public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (expandIcon == null) {
            expandIcon = BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.expand);
        }
        if (collapseIcon == null) {
            collapseIcon = BitmapFactory.decodeResource(
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
        vh.initView();

        return vh;
    }

    @Override
    final public void onBindViewHolder(VH holder, int position) {
        //get node at the position
        ListTree.TreeNode node = tree.getNodeByPlaneIndex(position);
        if (node.isShowExpandIcon()) {
            if (node.isExpanded()) {
                holder.arrowIcon.setImageBitmap(collapseIcon);
            } else {
                holder.arrowIcon.setImageBitmap(expandIcon);
            }
        } else {
            holder.arrowIcon.setImageBitmap(null);
        }

        ViewGroup.LayoutParams params = holder.arrowIcon.getLayoutParams();

        int layer = tree.getNodeLayerLevel(node);
        int space = layer * 44;
        holder.headSpace.getLayoutParams().width = space;
        space += params.width + 5;
        Log.i(TAG, "onBindViewHolder() space: " + space);

        //给子类机会去绑定行数据
        onBindNodeViewHolder(holder, position, space);
    }

    @Override
    final public int getItemViewType(int position) {
        ListTree.TreeNode node = tree.getNodeByPlaneIndex(position);
        return node.getLayoutResId();
    }

    @Override
    final public int getItemCount() {
        return tree.size();
    }

    protected abstract VH onCreateNodeView(ViewGroup parent, int viewType);

    protected abstract void onBindNodeViewHolder(VH viewHoler, int position, int space);

    public void notifyTreeItemInserted(ListTree.TreeNode parent, ListTree.TreeNode node) {
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
    }

    public class ListTreeViewHolder extends RecyclerView.ViewHolder {

        protected ViewGroup containerView;
        protected ImageView arrowIcon;
        protected Space headSpace;

        public ListTreeViewHolder(View itemView) {
            super(itemView);
            //由于构造方法在子类中调用，不能在这里搞View了，转到initView()中了。
        }

        protected void initView() {
            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setClickable(true);
                    v.setFocusable(true);
                    v.requestFocus();

                    int planePos = getAdapterPosition();
                    ListTree.TreeNode node = tree.getNodeByPlaneIndex(planePos);
                    if (node.isShowExpandIcon()) {
                        int nodePlaneIndex = tree.getNodePlaneIndex(node);
                        if (node.isExpanded()) {
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
                    }
                }
            });
        }
    }
}