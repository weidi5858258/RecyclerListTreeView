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
import com.weidi.AndroidListTreeAdapter;
import com.weidi.ItemNode;
import com.weidi.MemberInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    //保存数据的集合
    //private ListTree tree = new ListTree();
    private AndroidListTree tree = new AndroidListTree();
    //从ListTreeAdapter派生的Adapter
    //ExampleListTreeAdapter adapter;
    public static AndroidListTreeAdapter adapter;
    RecyclerView mRecyclerView;
    LinearLayout mTestLayout;
    LinearLayoutManager mLinearLayoutManager;
    RecyclerView.Recycler mRecycler;
    RecyclerView.State mState;

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
                //mTestLayout.scrollBy(-30, 0);
                /*try {
                    Class clazz = Class.forName("android.support.v7.widget.RecyclerView");
                    Field field = clazz.getDeclaredField("mViewFlinger");
                    field.setAccessible(true);
                    Object object = field.get(mRecyclerView);

                    clazz = Class.forName("android.support.v7.widget.RecyclerView$ViewFlinger");
                    Method method = clazz.getDeclaredMethod("smoothScrollBy", int.class,int.class);
                    method.setAccessible(true);
                    method.invoke(object, 30,30);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }*/

                //                mRecyclerView.startNestedScroll(30, 1);
                //                mRecyclerView.scrollBy(300, 30);
                //                mRecyclerView.smoothScrollBy(300, 10);

                /*try {
                    Class clazz = Class.forName("android.support.v7.widget.RecyclerView");
                    Field field = clazz.getDeclaredField("mRecycler");
                    field.setAccessible(true);
                    mRecycler = (RecyclerView.Recycler) field.get(mRecyclerView);

                    field = clazz.getDeclaredField("mState");
                    field.setAccessible(true);
                    mState = (RecyclerView.State) field.get(mRecyclerView);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                mLinearLayoutManager.scrollHorizontallyBy(30, mRecycler, mState);*/

                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                //mTestLayout.scrollBy(30, 0);
                /*try {
                    Class clazz = Class.forName("android.support.v7.widget.RecyclerView");
                    Field field = clazz.getDeclaredField("mViewFlinger");
                    field.setAccessible(true);
                    Object object = field.get(mRecyclerView);

                    clazz = Class.forName("android.support.v7.widget.RecyclerView$ViewFlinger");
                    Method method = clazz.getDeclaredMethod("smoothScrollBy", int.class,int.class);
                    method.setAccessible(true);
                    method.invoke(object, -30,-30);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }*/

                //                mRecyclerView.startNestedScroll(30, 1);
                //                mRecyclerView.scrollBy(300, -30);
                //                mRecyclerView.smoothScrollBy(-300, 10);

                /*try {
                    Class clazz = Class.forName("android.support.v7.widget.RecyclerView");
                    Field field = clazz.getDeclaredField("mRecycler");
                    field.setAccessible(true);
                    mRecycler = (RecyclerView.Recycler) field.get(mRecyclerView);

                    field = clazz.getDeclaredField("mState");
                    field.setAccessible(true);
                    mState = (RecyclerView.State) field.get(mRecyclerView);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                mLinearLayoutManager.scrollHorizontallyBy(-30, mRecycler, mState);*/

                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mTestLayout.scrollBy(-30, 0);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mTestLayout.scrollBy(30, 0);
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void init() {
        mTestLayout = findViewById(R.id.testlayout);
        mRecyclerView = findViewById(R.id.listview);
        mRecyclerView.setNestedScrollingEnabled(false);

        MemberInfo memberInfo = new MemberInfo();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.contacts_normal);
        memberInfo.bitmap = bitmap;
        memberInfo.title = "storage/37C8-3904";
        ItemNode storage_37C8_3904 = new ItemNode(memberInfo.title);
        storage_37C8_3904.data = memberInfo;
        storage_37C8_3904.mLayoutResId = R.layout.row_container_layout;

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "storage/47C8-3904";
        ItemNode storage_47C8_3904 = new ItemNode(memberInfo.title);
        storage_47C8_3904.data = memberInfo;
        storage_47C8_3904.mLayoutResId = R.layout.row_container_layout;

        tree.addRootNode(storage_37C8_3904);
        tree.addRootNode(storage_47C8_3904);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "myfiles";
        ItemNode myfiles = new ItemNode(memberInfo.title);
        myfiles.data = memberInfo;
        myfiles.mLayoutResId = R.layout.row_container_layout;
        tree.addChildNode(storage_37C8_3904, myfiles);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "APITestApp";
        ItemNode APITestApp = new ItemNode(memberInfo.title);
        APITestApp.data = memberInfo;
        APITestApp.mLayoutResId = R.layout.row_container_layout;
        tree.addChildNode(myfiles, APITestApp);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "Artifact";
        ItemNode Artifact = new ItemNode(memberInfo.title);
        Artifact.data = memberInfo;
        Artifact.mLayoutResId = R.layout.row_container_layout;
        tree.addChildNode(myfiles, Artifact);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "app";
        ItemNode app = new ItemNode(memberInfo.title);
        app.data = memberInfo;
        app.mLayoutResId = R.layout.row_container_layout;
        tree.addChildNode(APITestApp, app);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "src";
        ItemNode src = new ItemNode(memberInfo.title);
        src.data = memberInfo;
        src.mLayoutResId = R.layout.row_container_layout;
        tree.addChildNode(app, src);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "main";
        ItemNode main = new ItemNode(memberInfo.title);
        main.data = memberInfo;
        main.mLayoutResId = R.layout.row_container_layout;
        tree.addChildNode(src, main);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "java";
        ItemNode java = new ItemNode(memberInfo.title);
        java.data = memberInfo;
        java.mLayoutResId = R.layout.row_container_layout;
        tree.addChildNode(main, java);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "com";
        ItemNode com = new ItemNode(memberInfo.title);
        com.data = memberInfo;
        com.mLayoutResId = R.layout.row_container_layout;
        tree.addChildNode(java, com);

        memberInfo = new MemberInfo();
        memberInfo.bitmap = bitmap;
        memberInfo.title = "tvrecyclerview";
        ItemNode tvrecyclerview = new ItemNode(memberInfo.title);
        tvrecyclerview.data = memberInfo;
        tvrecyclerview.mLayoutResId = R.layout.row_container_layout;
        tree.addChildNode(com, tvrecyclerview);


        tree.handleSort();


        adapter = new AndroidListTreeAdapter(getApplicationContext(), tree);
        mLinearLayoutManager = new LinearLayoutManager(this);
        Log.i(TAG, "onCreate() canHori: " + mLinearLayoutManager.canScrollHorizontally());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.mTestLayout = mTestLayout;

        mRecyclerView.setFocusable(true);
        mRecyclerView.requestFocus();
    }

}
