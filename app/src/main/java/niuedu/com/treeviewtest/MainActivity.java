package niuedu.com.treeviewtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.niuedu.ListTree;

public class MainActivity extends AppCompatActivity
        implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    //保存数据的集合
    private ListTree tree = new ListTree();
    //从ListTreeAdapter派生的Adapter
    ExampleListTreeAdapter adapter;
    HorizontalScrollView mHorizontalScrollView;
    RecyclerView mRecyclerView;
    LinearLayout mTestLayout;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        //        setContentView(R.layout.activity_main);
        //        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //        setSupportActionBar(toolbar);

        //使用Android原生的RecyclerView即可
//        mHorizontalScrollView = findViewById(R.id.horizontalscrollview);
//        mHorizontalScrollView.setSmoothScrollingEnabled(true);
        mTestLayout = findViewById(R.id.testlayout);
        mRecyclerView = findViewById(R.id.listview);
        mRecyclerView.setNestedScrollingEnabled(false);

        //创建后台数据：一棵树
        //创建组们，是root node，所有parent为null
        ListTree.TreeNode groupNode1 = tree.addNode(null, "特别关心", R.layout.contacts_group_item);
        ListTree.TreeNode groupNode2 = tree.addNode(null, "我的好友", R.layout.contacts_group_item);
        ListTree.TreeNode groupNode3 = tree.addNode(null, "朋友", R.layout.contacts_group_item);
        ListTree.TreeNode groupNode4 = tree.addNode(null, "家人", R.layout.contacts_group_item);
        ListTree.TreeNode groupNode5 = tree.addNode(null, "同学", R.layout.contacts_group_item);

        //第二层 contact被添加到groupNode2,groupNode5中
        ExampleListTreeAdapter.ContactInfo contact;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
        contact = new ExampleListTreeAdapter.ContactInfo(bitmap, "王二");
        ListTree.TreeNode contactNode1 = tree.addNode(groupNode2, contact, R.layout
                .contacts_contact_item);
        ListTree.TreeNode contactNode2 = tree.addNode(groupNode5, contact, R.layout
                .contacts_contact_item);

        //第三层 contact被添加到contactNode1中
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
        contact = new ExampleListTreeAdapter.ContactInfo(bitmap, "东邪");
        ListTree.TreeNode n = tree.addNode(contactNode1, contact, R.layout.contacts_contact_item);
        n.setShowExpandIcon(false);
        //再添加一个
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
        contact = new ExampleListTreeAdapter.ContactInfo(bitmap, "李圆圆");
        n = tree.addNode(contactNode1, contact, R.layout.contacts_contact_item);
        n.setShowExpandIcon(false);

        //再添加一个
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
        contact = new ExampleListTreeAdapter.ContactInfo(bitmap, "李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆王三");
        ListTree.TreeNode wang3 = tree.addNode(groupNode2, contact, R.layout.contacts_contact_item);
        tree.addNode(groupNode5, contact, R.layout.contacts_contact_item);

        /*bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
        contact = new ExampleListTreeAdapter.ContactInfo(bitmap, "王四");
        ListTree.TreeNode wang4 = tree.addNode(wang3, contact, R.layout.contacts_contact_item);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
        contact = new ExampleListTreeAdapter.ContactInfo(bitmap, "王五");
        ListTree.TreeNode wang5 = tree.addNode(wang4, contact, R.layout.contacts_contact_item);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
        contact = new ExampleListTreeAdapter.ContactInfo(bitmap, "王六");
        ListTree.TreeNode wang6 = tree.addNode(wang5, contact, R.layout.contacts_contact_item);*/

        ListTree.TreeNode tempNode = null;
        for (int i = 4; i < 50; i++) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
            contact = new ExampleListTreeAdapter.ContactInfo(bitmap, "李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆李圆圆王 " + i);
            if (i == 4) {
                tempNode = tree.addNode(wang3, contact, R.layout.contacts_contact_item);
            } else {
                tempNode = tree.addNode(tempNode, contact, R.layout.contacts_contact_item);
            }
        }


        adapter = new ExampleListTreeAdapter(tree,mHorizontalScrollView, mRecyclerView, this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        Log.i(TAG, "onCreate() canHori: " + mLinearLayoutManager.canScrollHorizontally());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setFocusable(true);
        mRecyclerView.requestFocus();

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.scrollToRunningAppPosition();
            }
        }, 5000);*/
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mTestLayout.scrollBy(-30, 0);
//                mRecyclerView.scrollBy(0, 30);
//                mRecyclerView.smoothScrollBy(30, 0);
//                mLinearLayoutManager.scrollHorizontallyBy(30, mRecyclerView.new Recycler(), new RecyclerView.State());
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mTestLayout.scrollBy(30, 0);
//                mRecyclerView.scrollBy(0, -30);
//                mRecyclerView.smoothScrollBy(-30, 0);
//                mLinearLayoutManager.scrollHorizontallyBy(-30, mRecyclerView.new Recycler(), new RecyclerView.State());
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_del_selected) {
            //删除选中的Nodes，删一个Node时会将其子孙一起删掉
            tree.removeCheckedNodes();
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //响应某个Node上的快捷菜单的选择事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_item:
                //向当前行增加一个儿子
                ListTree.TreeNode node = adapter.getCurrentNode();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable
                        .contacts_normal);
                ExampleListTreeAdapter.ContactInfo contact = new ExampleListTreeAdapter.ContactInfo(
                        bitmap, "New contact");
                ListTree.TreeNode childNode = tree.addNode(node, contact, R.layout
                        .contacts_contact_item);
                adapter.notifyTreeItemInserted(node, childNode);
                return true;
            case R.id.action_clear_children:
                //清空所有的儿子们
                node = adapter.getCurrentNode();
                Pair<Integer, Integer> range = tree.clearDescendant(node);
                adapter.notifyItemRangeRemoved(range.first, range.second);
                return true;
            default:
                return false;
        }
    }
}
