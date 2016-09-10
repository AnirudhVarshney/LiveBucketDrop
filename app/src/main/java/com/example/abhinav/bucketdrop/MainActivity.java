package com.example.abhinav.bucketdrop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.abhinav.bucketdrop.Adapter.AdapterDrop;
import com.example.abhinav.bucketdrop.Adapter.AddListner;
import com.example.abhinav.bucketdrop.Adapter.CompleteListner;
import com.example.abhinav.bucketdrop.Adapter.Divider;
import com.example.abhinav.bucketdrop.Adapter.Filter;
import com.example.abhinav.bucketdrop.Adapter.MarkListner;
import com.example.abhinav.bucketdrop.Adapter.ResetListner;
import com.example.abhinav.bucketdrop.Adapter.SimpleTouchCallBack;
import com.example.abhinav.bucketdrop.beans.Drop;
import com.example.abhinav.bucketdrop.widget.BucketRecyclerView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar mToolbar;
    Button mButton;
    BucketRecyclerView mRecycler;
    Realm mRealm;
    View mEmptyView;
    RealmResults<Drop> mResults;
    AdapterDrop mAdapter;
    private RealmChangeListener mChangeListner = new RealmChangeListener() {
        @Override
        public void onChange() {
            Log.d("ani", "on change");
            mAdapter.update(mResults);
        }
    };
    private AddListner mAddListner = new AddListner() {
        @Override
        public void add() {
            showDilogAdd();

        }
    };
    private MarkListner mMarkListner = new MarkListner() {
        @Override
        public void onMark(int position) {
            showDilogMark(position);
        }
    };
    private CompleteListner mCompleteListner = new CompleteListner() {
        @Override
        public void onComplete(int position) {
            mAdapter.markComplete(position);

        }
    };
    private ResetListner mResetListener = new ResetListner() { //to reset to the default screen if no items to display
        @Override
        public void onReset() {
            AppBucketDrop.save(MainActivity.this, Filter.NONE);
            loadResults(Filter.NONE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mButton = (Button) findViewById(R.id.adddrop);
        mButton.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mEmptyView = findViewById(R.id.empty_drops);
        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(Drop.class).findAllAsync();
        int filterOption = AppBucketDrop.load(this); //
        loadResults(filterOption);
        mRecycler = (BucketRecyclerView) findViewById(R.id.rv_recycler);
        mRecycler.setLayoutManager(manager);
        mAdapter = new AdapterDrop(this, mRealm, mResults,mResetListener);
        mAdapter.setHasStableIds(true);//for recycling animaton and this should be below where u hav called mAdpter only
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        mRecycler.setItemAnimator(new DefaultItemAnimator());//for recycler item animator
        mRecycler.hideifEmpty(mToolbar);
        mRecycler.showifEmpty(mEmptyView);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setAddListner(mAddListner);
        mAdapter.setMarkListner(mMarkListner);

        SimpleTouchCallBack callBack = new SimpleTouchCallBack(mAdapter);//next 3 for delete using swipe
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(mRecycler);
        setSupportActionBar(mToolbar);
        initBackgroundImage();
//        AlarmManager managers = (AlarmManager) getSystemService(ALARM_SERVICE); transfred to util so that we can use this in brodcast reciver
//        Intent intent = new Intent(this, NotificationService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        managers.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, 120000, pendingIntent);
    }

    private void initBackgroundImage() {//used library to load the background image so that app doesnot crash with high reso;ution image
        ImageView background = (ImageView) findViewById(R.id.iv_background);
        Glide.with(this).load(R.drawable.background).centerCrop().into(background);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean handled=true;
        int filterOption = Filter.NONE;//default value
        switch (id){
            case R.id.action_add:
                showDilogAdd();
                break;
            case R.id.action_sort_ascending_date:

                filterOption = Filter.LEAST_TIME_LEFT;
                break;
            case R.id.action_sort_descending_date:

                filterOption = Filter.MOST_TIME_LEFT;
                break;
            case R.id.action_show_complete:

                filterOption = Filter.COMPLETE;
                break;
            case R.id.action_show_incomplete:

                filterOption = Filter.INCOMPLETE;
                break;
            default:
                handled=false;
                break;
        }
        loadResults(filterOption);
        AppBucketDrop.save(this,filterOption); //save method is given in application so that can be used evrywhere

        return handled;
    }
    private void loadResults(int filterOption) {
        switch (filterOption) {
            case Filter.NONE:
                mResults = mRealm.where(Drop.class).findAllAsync();
                break;
            case Filter.LEAST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when");
                break;
            case Filter.MOST_TIME_LEFT:
                mResults = mRealm.where(Drop.class).findAllSortedAsync("when", Sort.DESCENDING);
                break;
            case Filter.COMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", true).findAllAsync();
                break;
            case Filter.INCOMPLETE:
                mResults = mRealm.where(Drop.class).equalTo("completed", false).findAllAsync();
                break;
        }
        mResults.addChangeListener(mChangeListner);
    }
//private void save (int filterOption){ //for sharePrefrence to save sorting values and ways //this all will be moved to application class
//    SharedPreferences pref = getPreferences(MODE_PRIVATE); //to be used in et item count to load data from shared prefrences
//    SharedPreferences.Editor editor = pref.edit();
//    editor.putInt("filter", filterOption);
//    editor.apply();
//}
//    private int load(){
//        SharedPreferences pref=getPreferences(MODE_PRIVATE);
//        int filterOption=pref.getInt("filter",Filter.NONE);
//    return filterOption;
//    }



    @Override
    protected void onStart() {
        super.onStart();
        mResults.addChangeListener(mChangeListner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResults.removeChangeListener(mChangeListner);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        showDilogAdd();

    }

    private void showDilogAdd() {
        DilogAdd dilogAdd=new DilogAdd();
        dilogAdd.show(getSupportFragmentManager(), "Add");//Add is juat a tag to keep track of manager
    }
    private void showDilogMark(int position){ //this methodcannot be created in Adapter class bcoz this all are for activity and dapter for recycler view
        DilogMark dilogMark=new DilogMark();
        Bundle bundle=new Bundle();
        bundle.putInt("POSITION",position);//POSITION IS FOR USING THIS BUNDLE ANYWHERE ELSE LIKE IN VIDEO 80
        dilogMark.setArguments(bundle); //setArguments accept bundle thats why we craeted bundle
        dilogMark.setCompleteListner(mCompleteListner);
        dilogMark.show(getSupportFragmentManager(), "Mark");//Mark is juat a tag to keep track of manager
    }
}
