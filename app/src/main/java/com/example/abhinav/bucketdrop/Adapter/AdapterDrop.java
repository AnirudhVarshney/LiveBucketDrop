package com.example.abhinav.bucketdrop.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.abhinav.bucketdrop.AppBucketDrop;
import com.example.abhinav.bucketdrop.R;
import com.example.abhinav.bucketdrop.beans.Drop;
import com.example.abhinav.bucketdrop.extras.Util;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ABHINAV on 21-04-2016.
 */
//public class AdapterDrop extends RecyclerView.Adapter<AdapterDrop.DropHolder> {// first create Drophoder class then implement method of AdapterDrops
public class AdapterDrop extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListner{//we remover zbove bcoz we have teo hoders now o we kept parent
    private LayoutInflater minflater;
    private RealmResults<Drop> mResults;
    private Realm mRealm;
    public static final int ITEM=0;
    public static final int NO_ITEM = 1;
    public static final int FOOTER=2;
    public static final int COUNT_NO_ITEMS = 1;
    public static final int COUNT_FOOTER = 1;
    private AddListner mAddListner;
    private MarkListner mMarkListner;
private ResetListner mResetListner;
    private int mFilterOption;
    private Context mContext;
    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @param mResetListner
     * @param mResetListener
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    public AdapterDrop(Context context, Realm realm, RealmResults<Drop> results, ResetListner resetListner){ //For recycler view
        minflater=LayoutInflater.from(context);
        mRealm=realm;
        mContext=context;
        mResetListner=resetListner;
        update(results);
    }
    public void update(RealmResults<Drop> results){
        mResults=results;
        mFilterOption= AppBucketDrop.load(mContext);
        notifyDataSetChanged();
    }
    public void setAddListner(AddListner listner){ //instead of maiking this method u can also pass this as a third parameter in AdapterDrop constructor like
        //and then we do not need to call this in main activity by calling set like
        mAddListner=listner;
        //        public AdapterDrops(Context context, RealmResults<Drop> results, AddListener listener) {
        //            mInflater = LayoutInflater.from(context);
        //            update(results);
        //            mAddListener = listener;
        //        }
    }
    public void setMarkListner(MarkListner markListner){
        mMarkListner=markListner;
    }
    @Override
    public long getItemId(int position) { //ad anmation to recycler view
        if (position < mResults.size()) {
            return mResults.get(position).getAdded(); //get added becoz its unique..the time when its added
        }
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemViewType(int position) { //for sectioned recycler view
//       if(mResults==null || position<mResults.size()){ //see previous video to check this condition
//           return ITEM;   basic when no shared prefrences
//       }
//        else {
//           return FOOTER;
//       }
//    }

        if (!mResults.isEmpty()) {
            if (position < mResults.size()) {
                return ITEM;
            } else {
                return FOOTER;
            }
        } else {
            if (mFilterOption == Filter.COMPLETE ||
                    mFilterOption == Filter.INCOMPLETE) {
                if (position == 0) {
                    return NO_ITEM;
                } else {
                    return FOOTER;
                }
            } else {
                return ITEM;
            }
        }
    }

    @Override
    // public DropHolder onCreateViewHolder(ViewGroup parent, int viewType) { //for recycler view //here we are removing DropHolder and placing recyclerview.VewHolder bcoz now we have two holder so we place parent bcoz

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            View view = minflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view);
        }
        else if (viewType == NO_ITEM) {
            View view = minflater.inflate(R.layout.no_item, parent, false);
            return new NoItemsHolder(view);}
        else {
            View view = minflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view);
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) { //to put the data into thesingle row in recycler view
        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;

            Drop drop = mResults.get(position);


            // dropHolder.mTextwhat.setText(drop.getWhat());//u can use this method also and also we can craete method
            dropHolder.setWhat(drop.getWhat()); //all 3 to set data in row
            dropHolder.setWhen(drop.getWhen());
            dropHolder.setBackground(drop.isCompleted());
            //Log.d("av", "in" + position);
        }
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        if (!mResults.isEmpty()) {
            return mResults.size() +COUNT_FOOTER;
        } else {
            if (mFilterOption == Filter.LEAST_TIME_LEFT
                    || mFilterOption == Filter.MOST_TIME_LEFT
                    || mFilterOption == Filter.NONE) {
                return 0;
            } else {
                return COUNT_NO_ITEMS + COUNT_FOOTER;
            }
        }
    }

    @Override
    public void onSwipe(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).removeFromRealm();
            mRealm.commitTransaction();
            notifyItemRemoved(position);

        }
        resetFilterIfEmpty();
    }
    private void resetFilterIfEmpty() {
        if (mResults.isEmpty() && (mFilterOption == Filter.COMPLETE ||
                mFilterOption == Filter.INCOMPLETE)){
           mResetListner.onReset();
        }
    }

    public void markComplete(int position) {
        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();
            notifyItemChanged(position);
        }
    }


    public  class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //we have removed static after public so that
        //so that we can use mMarklistner from above otherewise u can check video 80 to knw what to do
        TextView mTextwhat;
        TextView mTextwhen;
        Context mContext;
        View mItemView;

        public DropHolder(View itemView) {
            super(itemView);
            mItemView=itemView;
            mContext=itemView.getContext();
            itemView.setOnClickListener(this);//for whole view
            mTextwhat= (TextView) itemView.findViewById(R.id.tv_what);
            mTextwhen= (TextView) itemView.findViewById(R.id.when);
        }
        public void setWhat(String what){
            mTextwhat.setText(what);
        }
        public void setWhen(long when) {
            mTextwhen.setText(DateUtils.getRelativeTimeSpanString(when,System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS,DateUtils.FORMAT_ABBREV_ALL));//last is to format the abbrevation like october to oct and all
        }


        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            mMarkListner.onMark(getAdapterPosition());
        }

        public void setBackground(boolean completed) {
            Drawable drawable;
            if(completed){
                drawable=ContextCompat.getDrawable(mContext,R.color.bg_drop_complete);}
            else{
                drawable=ContextCompat.getDrawable(mContext,R.drawable.row_drop_selector);
            }
            // mItemView.setBackground(drawable);//this version is supported for 16 and above so we are checking version check
            Util.setBackground(mItemView, drawable);
        }


    }
    public   class NoItemsHolder extends RecyclerView.ViewHolder {

        public NoItemsHolder(View itemView) {
            super(itemView);
        }
    }
    public  class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button mBtnAdd;
        public FooterHolder(View itemView) {
            super(itemView);
            mBtnAdd= (Button) itemView.findViewById(R.id.btn_footer); //here we can not use method of main activity to show dilog bcoz dilog is created by activity not adapter
            //and giving a reference s a bad idea bcoz activity is create and destroyed

            mBtnAdd.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            mAddListner.add();
        }
    }
}
