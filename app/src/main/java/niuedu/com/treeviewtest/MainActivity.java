package niuedu.com.treeviewtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.weidi.AndroidListTree;
import com.weidi.ItemNode;
import com.weidi.MemberInfo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //保存数据的集合
    //private ListTree tree = new ListTree();
    private AndroidListTree tree = new AndroidListTree();
    //从ListTreeAdapter派生的Adapter
    //ExampleListTreeAdapter adapter;
    AndroidExampleListTreeAdapter adapter;
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

        init();

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

    private void init() {
        mTestLayout = findViewById(R.id.testlayout);
        mRecyclerView = findViewById(R.id.listview);
        mRecyclerView.setNestedScrollingEnabled(false);

        MemberInfo memberInfo = new MemberInfo();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
        memberInfo.bitmap = bitmap;
        memberInfo.title = "特别关心";
        ItemNode groupNode1 = new ItemNode("特别关心");
        groupNode1.data = memberInfo;
        groupNode1.mLayoutResId = R.layout.contacts_group_item;

        memberInfo = new MemberInfo();
        memberInfo.title = "我的好友";
        ItemNode groupNode2 = new ItemNode("我的好友");
        groupNode2.data = memberInfo;
        groupNode2.mLayoutResId = R.layout.contacts_group_item;

        tree.addRootNode(groupNode1);
        tree.addRootNode(groupNode2);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "王二";
        ItemNode memberNode1 = new ItemNode("王二");
        memberNode1.data = memberInfo;
        memberNode1.mLayoutResId = R.layout.contacts_contact_item;
        tree.addChildNode(groupNode1, memberNode1);

        adapter = new AndroidExampleListTreeAdapter(getApplicationContext(), tree, mRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        Log.i(TAG, "onCreate() canHori: " + mLinearLayoutManager.canScrollHorizontally());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setFocusable(true);
        mRecyclerView.requestFocus();
    }

}
