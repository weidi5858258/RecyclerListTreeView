package com.niuedu;

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

/**
 * 为RecyclerView提供数据
 */
public abstract class ListTreeAdapter<VH extends ListTreeAdapter.ListTreeViewHolder>
        extends RecyclerView.Adapter<VH> {

    private static final String TAG = ListTreeAdapter.class.getSimpleName();
    protected ListTree tree;

    //展开和收起图标的Drawable资源id
    private Bitmap expandIcon = null;
    private Bitmap collapseIcon = null;

    //构造方法
    public ListTreeAdapter(ListTree tree) {
        this.tree = tree;
    }

    public ListTreeAdapter(ListTree tree, Bitmap expandIcon, Bitmap collapseIcon) {
        this.tree = tree;

        this.expandIcon = expandIcon;
        this.collapseIcon = collapseIcon;
    }

    @Override
    final public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (expandIcon == null) {
            expandIcon = BitmapFactory.decodeResource(
                    parent.getContext().getResources(), R.drawable.expand);
        }

        if (collapseIcon == null) {
            collapseIcon = BitmapFactory.decodeResource(
                    parent.getContext().getResources(), R.drawable.collapse);
        }

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup container = (ViewGroup) inflater.inflate(
                R.layout.row_container_layout, parent, false);

        //响应在Arrow上的点击事件，执行收缩或展开
        ImageView arrowIcon = container.findViewById(R.id.listtree_arrowIcon);
        //跟据列表控件的宽度为它计算一个合适的大小
        int w = parent.getMeasuredWidth();
        arrowIcon.getLayoutParams().width = w / 15;
        arrowIcon.getLayoutParams().height = w / 15;

        //子类创建自己的row view
        VH vh = onCreateNodeView(container, viewType);
        if (vh == null) {
            return null;
        }

        vh.containerView = container;
        vh.arrowIcon = arrowIcon;
        vh.headSpace = container.findViewById(R.id.listtree_head_space);

        //不能在构造方法中设置各View，只能另搞一个方法了
        vh.initView();

        //container.addView(vh.itemView);
        return vh;
    }

    @Override
    final public void onBindViewHolder(VH holder, int position) {
        //get node at the position
        ListTree.TreeNode node = tree.getNodeByPlaneIndex(position);
        if (node.isShowExpandIcon()) {
            if (node.isExpand()) {
                holder.arrowIcon.setImageBitmap(collapseIcon);
            } else {
                holder.arrowIcon.setImageBitmap(expandIcon);
            }
        } else {
            //不需要显示图标
            holder.arrowIcon.setImageBitmap(null);
        }

        ViewGroup.LayoutParams params = holder.arrowIcon.getLayoutParams();

        //跟据node的层深，改变缩进距离,从0开始计
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
        int count = 0;
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
        if (parent.isExpand()) {
            //已展开
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

    /***
     只有空格和展开收缩的图标
     */
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
                        if (node.isExpand()) {
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