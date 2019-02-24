package niuedu.com.treeviewtest;

import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.niuedu.ListTree;
import com.niuedu.ListTreeAdapter;

public class ExampleListTreeAdapter extends
        ListTreeAdapter<ExampleListTreeAdapter.BaseViewHolder> {

    private static final String TAG = ExampleListTreeAdapter.class.getSimpleName();

    //行上弹出菜单的侦听器
    private PopupMenu.OnMenuItemClickListener itemMenuClickListener;
    //记录弹出菜单是在哪个行上出现的
    private ListTree.TreeNode currentNode;

    private RecyclerView mRecyclerView;
    private ListTree.TreeNode mCurTreeNode;

    private int selectedPosition = -1;

    //构造方法
    public ExampleListTreeAdapter(ListTree tree,
                                  RecyclerView recyclerView,
                                  PopupMenu.OnMenuItemClickListener listener) {
        super(tree);
        mRecyclerView = recyclerView;
        this.itemMenuClickListener = listener;
    }

    public ListTree.TreeNode getCurrentNode() {
        return currentNode;
    }

    @Override
    protected BaseViewHolder onCreateNodeView(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

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
            return new ContactViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    protected void onBindNodeViewHolder(BaseViewHolder holder, int position) {

        View view = holder.itemView;

        view.setClickable(true);
        view.setFocusable(true);

        if(position==0){
            view.requestFocus();
        }

        //get node at the position
        ListTree.TreeNode node = tree.getNodeByPlaneIndex(position);
        Log.i(TAG, "onBindNodeViewHolder() position: " + position+" title: "+node.getData());
        Log.i(TAG, "onBindNodeViewHolder() selectedPosition: " + selectedPosition);

        if (node.getLayoutResId() == R.layout.contacts_group_item) {
            //group node
            String title = (String) node.getData();

            GroupViewHolder gvh = (GroupViewHolder) holder;
            gvh.textViewTitle.setText(title);
//            gvh.textViewCount.setText("0/" + node.getChildrenCount());
//            gvh.aSwitch.setChecked(node.isChecked());
        } else if (node.getLayoutResId() == R.layout.contacts_contact_item) {
            //child node
            ContactInfo info = (ContactInfo) node.getData();

            ContactViewHolder cvh = (ContactViewHolder) holder;
            cvh.imageViewHead.setImageBitmap(info.getBitmap());
            cvh.textViewTitle.setText(info.getTitle());
//            cvh.aSwitch.setChecked(node.isChecked());
        }

        /*if (selectedPosition != -1) {
            if (selectedPosition == position) {
                holder.itemView.requestFocus();
                selectedPosition = -1;
            }
        }*/
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

    //保存子行信息的类
    public static class ContactInfo {
        //头像,用于设置给ImageView。
        private Bitmap bitmap;
        //标题
        private String title;

        public ContactInfo(Bitmap bitmap, String title) {
            this.bitmap = bitmap;
            this.title = title;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "ContactInfo{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    //组行和联系人行的Holder基类
    class BaseViewHolder extends ListTreeAdapter.ListTreeViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    //将ViewHolder声明为Adapter的内部类，反正外面也用不到
    private class GroupViewHolder extends BaseViewHolder {

        TextView textViewTitle;
        TextView textViewCount;
        Switch aSwitch;
        TextView textViewMenu;

        public GroupViewHolder(final View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
//            textViewCount = itemView.findViewById(R.id.textViewCount);
//            aSwitch = itemView.findViewById(R.id.switchChecked);
//            textViewMenu = itemView.findViewById(R.id.textViewMenu);
//            textViewCount.setVisibility(View.GONE);
//            aSwitch.setVisibility(View.GONE);
//            textViewMenu.setVisibility(View.GONE);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int planeIndex = getAdapterPosition();
                    ListTree.TreeNode node = tree.getNodeByPlaneIndex(planeIndex);
                    if(b){
                        itemView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_red_light));
                        mCurTreeNode = node;
                    }else{
                        itemView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_blue_bright));
                    }
                    Log.i(TAG, "Group onFocusChange() " + b + " " + node.getData());
                }
            });

            /*//应响应点击事件而不是CheckedChange事件，因为那样会引起事件的递归触发
            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int planeIndex = getAdapterPosition();
                    ListTree.TreeNode node = tree.getNodeByPlaneIndex(planeIndex);
                    node.setChecked(!node.isChecked());
                    //改变所有的子孙们的状态
                    int count = tree.setDescendantChecked(planeIndex, node.isChecked());
                    notifyItemRangeChanged(planeIndex, count + 1);
                }
            });

            //点了PopMenu控件，弹出PopMenu
            textViewMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nodePlaneIndex = getAdapterPosition();
                    ListTree.TreeNode node = tree.getNodeByPlaneIndex(nodePlaneIndex);
                    currentNode = node;
                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.setOnMenuItemClickListener(itemMenuClickListener);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_item, popup.getMenu());
                    popup.show();
                }
            });*/
        }
    }

    private class ContactViewHolder extends BaseViewHolder {
        ImageView imageViewHead;
        TextView textViewTitle;
        TextView textViewDetail;
        Switch aSwitch;

        public ContactViewHolder(final View itemView) {
            super(itemView);

            imageViewHead = itemView.findViewById(R.id.imageViewHead);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
//            textViewDetail = itemView.findViewById(R.id.textViewDetail);
//            aSwitch = itemView.findViewById(R.id.switchChecked);
//            textViewDetail.setVisibility(View.GONE);
//            aSwitch.setVisibility(View.GONE);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    int planeIndex = getAdapterPosition();
                    ListTree.TreeNode node = tree.getNodeByPlaneIndex(planeIndex);
                    if(b){
                        itemView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_red_light));
                        mCurTreeNode = node;
                    }else{
                        itemView.setBackgroundColor(view.getContext().getResources().getColor(android.R.color.holo_blue_bright));
                    }
                    Log.i(TAG, "Member onFocusChange() " + b + " " + node.getData());
                }
            });

            /*//应响应点击事件而不是CheckedChange事件，因为那样会引起事件的递归触发
            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int nodePlaneIndex = getAdapterPosition();
                    ListTree.TreeNode node = tree.getNodeByPlaneIndex(nodePlaneIndex);
                    node.setChecked(!node.isChecked());
                    //改变所有的子孙们的状态
                    int count = tree.setDescendantChecked(nodePlaneIndex, node.isChecked());
                    notifyItemRangeChanged(nodePlaneIndex, count + 1);
                }
            });*/
        }
    }
}