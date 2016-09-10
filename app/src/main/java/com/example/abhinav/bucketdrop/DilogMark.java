package com.example.abhinav.bucketdrop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.abhinav.bucketdrop.Adapter.CompleteListner;

/**
 * Created by ABHINAV on 23-04-2016.
 */
public class DilogMark extends DialogFragment implements View.OnClickListener{
    private ImageButton mBtnClose;
    private Button mBtnCompleted;
    private CompleteListner mListner;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_mark,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnClose= (ImageButton) view.findViewById(R.id.btn_close);
        mBtnCompleted= (Button) view.findViewById(R.id.btn_completed);
        mBtnClose.setOnClickListener(this);
        mBtnCompleted.setOnClickListener(this);
        AppBucketDrop.setRalewayRegular(getActivity(), mBtnCompleted);//for custom font
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);}
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_completed:
                markAsComplete();
                break;
        }
        dismiss();

    }


    private void markAsComplete() {
        Bundle arguments=getArguments();

        if(mListner!=null && arguments!=null){
            int position=arguments.getInt("POSITION");
            mListner.onComplete(position);
        }

    }

    public void setCompleteListner(CompleteListner mCompleteListner) {
        mListner=mCompleteListner;
    }
}

