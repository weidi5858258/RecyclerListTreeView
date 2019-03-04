package com.weidi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import niuedu.com.treeviewtest.MainActivity;
import niuedu.com.treeviewtest.R;

public class AndroidListTreeAdapter extends RecyclerView.Adapter {

    private static final String TAG = AndroidListTreeAdapter.class.getSimpleName();

    private Context mContext = null;
    private AndroidListTree tree = null;
    public LinearLayout mTestLayout = null;
    private Bitmap mExpandIcon = null;
    private Bitmap mCollapseIcon = null;
    private ItemNode mCurrentNode;

    private int mFocusedIndex = -1;

    public AndroidListTreeAdapter(Context context, AndroidListTree tree) {
        this.mContext = context;
        this.tree = tree;
        if (mCollapseIcon == null) {
            mCollapseIcon = BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.expand);
        }
        if (mExpandIcon == null) {
            mExpandIcon = BitmapFactory.decodeResource(
                    mContext.getResources(), R.drawable.collapse);
        }
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup container = (ViewGroup) inflater.inflate(
                R.layout.row_container_layout, parent, false);

        return new MemberViewHolder(container);
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holer, int position) {
        Log.i(TAG, "onBindViewHolder() position: " + position);
        if (holer == null) {
            return;
        }
        MemberViewHolder viewHoler = null;
        if (holer instanceof MemberViewHolder) {
            viewHoler = (MemberViewHolder) holer;
        }

        View view = viewHoler.itemView;
        view.setClickable(true);
        view.setFocusable(true);

        ItemNode node = tree.getNodeWithPosition(position);
        if (node.mNeedToShow) {
            if (node.mIsExpanded) {
                viewHoler.arrowIcon.setImageBitmap(mExpandIcon);
            } else {
                viewHoler.arrowIcon.setImageBitmap(mCollapseIcon);
            }
        } else {
            viewHoler.arrowIcon.setImageBitmap(null);
        }

        ViewGroup.LayoutParams params = viewHoler.arrowIcon.getLayoutParams();

        int layer = node.mHierarchy;
        int space = layer * 44;
        viewHoler.headSpace.getLayoutParams().width = space;
        space += params.width + 5;
        //Log.i(TAG, "onBindViewHolder() space: " + space);

        if (node.mLayoutResId == R.layout.row_container_layout) {
            //child node
            MemberInfo info = (MemberInfo) node.data;

            MemberViewHolder cvh = (MemberViewHolder) viewHoler;
            cvh.imageViewHead.setImageBitmap(info.bitmap);
            cvh.textViewTitle.setText(info.title);
            TextPaint textPaint = cvh.textViewTitle.getPaint();
            int textPaintWidth = (int) textPaint.measureText(
                    cvh.textViewTitle.getText().toString());

            params = cvh.imageViewHead.getLayoutParams();
            Log.i(TAG, "onBindNodeViewHolder() title: " + info.title);
            //            Log.i(TAG, "onBindNodeViewHolder() params.width: " + params.width);
            //            Log.i(TAG, "onBindNodeViewHolder() textPaintWidth: " + textPaintWidth);
            int totalWidth = space + params.width + 20 + textPaintWidth;
            Log.i(TAG, "onBindNodeViewHolder() totalWidth: " + totalWidth);

            if (524 - totalWidth <= 44) {
                Log.i(TAG, "onBindNodeViewHolder() (524 - totalWidth): " + (524 - totalWidth));

                mTestLayout.scrollBy(30, 0);
            } else {

            }

        }

        if (mFocusedIndex != -1) {
            if (mFocusedIndex == position) {
                view.requestFocus();
            }
        }
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

    public ItemNode getCurrentNode() {
        return mCurrentNode;
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {

        public Space headSpace;
        public ImageView arrowIcon;
        public ImageView imageViewHead;
        public TextView textViewTitle;

        public MemberViewHolder(final View itemView) {
            super(itemView);

            headSpace = itemView.findViewById(niuedu.com.treeviewtest.R.id.listtree_head_space);
            arrowIcon = itemView.findViewById(niuedu.com.treeviewtest.R.id.listtree_arrowIcon);
            imageViewHead = itemView.findViewById(niuedu.com.treeviewtest.R.id.imageViewHead);
            textViewTitle = itemView.findViewById(niuedu.com.treeviewtest.R.id.textViewTitle);

            //            ImageView arrowIcon = itemView.findViewById(R.id.listtree_arrowIcon);
            //            int w = itemView.getMeasuredWidth();
            //            arrowIcon.getLayoutParams().width = w / 15;
            //            arrowIcon.getLayoutParams().height = w / 15;

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mCurrentNode == null) {
                                return;
                            }
                            Log.i(TAG, "onClick() start mCurrentNode.mPath: " + mCurrentNode.mPath);
                            if (mCurrentNode.mIsExpanded) {
                                tree.handleCollapseNode(mCurrentNode);
                            } else {
                                tree.handleExpandNode(mCurrentNode);
                            }
                            tree.handleSort();

                            MainActivity.adapter.notifyDataSetChanged();

                            v.setClickable(true);
                            v.setFocusable(true);
                            v.requestFocus();

                            mFocusedIndex = tree.getFocusedIndex(mCurrentNode);
                            Log.i(TAG, "onClick() end mCurrentNode.mPath: " + mCurrentNode.mPath);
                        }
                    });

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    int planeIndex = getAdapterPosition();
                    ItemNode node = tree.getNodeWithPosition(planeIndex);
                    if (hasFocus) {
                        itemView.setBackgroundColor(view.getContext().getResources().getColor
                                (android.R.color.holo_red_light));
                        mCurrentNode = node;
                        mFocusedIndex = tree.getFocusedIndex(mCurrentNode);
                        Log.i(TAG, "Member onFocusChange() " + hasFocus + " " + node.mPath);
                    } else {
                        itemView.setBackgroundColor(view.getContext().getResources().getColor
                                (android.R.color.holo_green_light));
                    }
                }
            });
        }

    }

}