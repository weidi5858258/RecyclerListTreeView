package niuedu.com.treeviewtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.AndroidListTree;
import com.weidi.AndroidListTreeAdapter;
import com.weidi.AndroidListTreeViewHolder;
import com.weidi.ItemNode;
import com.weidi.MemberInfo;

public class AndroidExampleListTreeAdapter extends
        AndroidListTreeAdapter<AndroidListTreeViewHolder> {

    private static final String TAG =
            AndroidExampleListTreeAdapter.class.getSimpleName();

    //记录弹出菜单是在哪个行上出现的
    private RecyclerView mRecyclerView;
    private ItemNode mCurrentNode;
    private int selectedPosition = -1;

    //构造方法
    public AndroidExampleListTreeAdapter(Context context,
                                         AndroidListTree tree,
                                         RecyclerView recyclerView) {
        super(context, tree);
        mRecyclerView = recyclerView;
    }

    @Override
    protected AndroidListTreeViewHolder onCreateNodeView(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        //创建不同的行View
        if (viewType == R.layout.contacts_group_item) {
            //注意！此处有一个不同！最后一个参数必须传true！
            View view = inflater.inflate(viewType, parent, true);
            //用不同的ViewHolder包装
            return new GroupViewHolder(view);
        } else if (viewType == R.layout.contacts_contact_item) {
            //注意！此处有一个不同！最后一个参数必须传true！
            View view = inflater.inflate(viewType, parent, true);
            //用不同的ViewHolder包装
            return new MemberViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    protected void onBindNodeViewHolder(AndroidListTreeViewHolder viewHoler,
                                        int position,
                                        int space) {
        View view = viewHoler.itemView;
        view.setClickable(true);
        view.setFocusable(true);
        if (position == 0) {
            view.requestFocus();
        }

        //get node at the position
        ItemNode node = tree.getNodeWithPosition(position);
//        Log.i(TAG, "onBindNodeViewHolder() position: " + position+" title: "+node.getData());
//        Log.i(TAG, "onBindNodeViewHolder() selectedPosition: " + selectedPosition);

        if (node.mLayoutResId == R.layout.contacts_group_item) {
            //group node
            MemberInfo memberInfo = (MemberInfo) node.data;

            GroupViewHolder gvh = (GroupViewHolder) viewHoler;
            gvh.textViewTitle.setText(memberInfo.title);
        } else if (node.mLayoutResId == R.layout.contacts_contact_item) {
            //child node
            MemberInfo info = (MemberInfo) node.data;

            MemberViewHolder cvh = (MemberViewHolder) viewHoler;
            cvh.imageViewHead.setImageBitmap(info.bitmap);
            cvh.textViewTitle.setText(info.title);
            TextPaint textPaint = cvh.textViewTitle.getPaint();
            int textPaintWidth = (int) textPaint.measureText(cvh.textViewTitle.getText().toString());

            ViewGroup.LayoutParams params = cvh.imageViewHead.getLayoutParams();
            Log.i(TAG, "onBindNodeViewHolder() title: " + info.title);
//            Log.i(TAG, "onBindNodeViewHolder() params.width: " + params.width);
            //            Log.i(TAG, "onBindNodeViewHolder() textPaintWidth: " + textPaintWidth);
            int totalWidth = space + params.width + 20 + textPaintWidth;
            Log.i(TAG, "onBindNodeViewHolder() totalWidth: " + totalWidth);

            if (524 - totalWidth <= 44) {
                // 移动
//                mRecyclerView.scrollBy(0, -110);
//                mRecyclerView.smoothScrollBy(550, 110);
//                mHorizontalScrollView.smoothScrollBy(1500, 0);
            }

//            cvh.aSwitch.setChecked(node.isChecked());
        }

        /*if (selectedPosition != -1) {
            if (selectedPosition == position) {
                holder.itemView.requestFocus();
                selectedPosition = -1;
            }
        }*/
    }

    public ItemNode getCurrentNode() {
        return mCurrentNode;
    }

    public void scrollToRunningAppPosition() {
        mRecyclerView.scrollToPosition(0);
        setSelection(0);
        /*if (mCurTreeNode == null) {
            mRecyclerView.scrollToPosition(2);
            setSelection(2);
            return;
        }*/
    }

    public synchronized void setSelection(int position) {
        // MLog.i(TAG, "setSelection position = " + position);
        selectedPosition = position;
        notifyDataSetChanged();
    }

    class GroupViewHolder extends AndroidListTreeViewHolder {

        public TextView textViewTitle;

        public GroupViewHolder(final View itemView) {
            super(tree, itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    int planeIndex = getAdapterPosition();
                    ItemNode node = tree.getNodeWithPosition(planeIndex);
                    if (hasFocus) {
                        itemView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_red_light));
                        mCurrentNode = node;
                    } else {
                        itemView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_green_light));
                    }
//                    Log.i(TAG, "Group onFocusChange() " + b + " " + node.getData());
                }
            });
        }

    }

    class MemberViewHolder extends AndroidListTreeViewHolder {

        public ImageView imageViewHead;
        public TextView textViewTitle;

        public MemberViewHolder(final View itemView) {
            super(tree, itemView);

            imageViewHead = itemView.findViewById(R.id.imageViewHead);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    int planeIndex = getAdapterPosition();
                    ItemNode node = tree.getNodeWithPosition(planeIndex);
                    if (hasFocus) {
                        itemView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_red_light));
                        mCurrentNode = node;
                    } else {
                        itemView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_green_light));
                    }
//                    Log.i(TAG, "Member onFocusChange() " + b + " " + node.getData());
                }
            });
        }

    }

}