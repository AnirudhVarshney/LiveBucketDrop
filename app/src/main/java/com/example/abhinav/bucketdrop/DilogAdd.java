package com.example.abhinav.bucketdrop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.abhinav.bucketdrop.beans.Drop;
import com.example.abhinav.bucketdrop.widget.BucketPickerView;

import io.realm.Realm;

/**
 * Created by ABHINAV on 20-04-2016.
 */
public class DilogAdd extends DialogFragment implements View.OnClickListener {
    private ImageButton mButtonClose;
    private EditText mInputWhat;
    private BucketPickerView mInputWhen;
    private Button mButtonAdd;
    public DilogAdd() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dilog_add,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogTheme);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) { //we cannot directly call findviewby because we ae now in fragment we need context
                                                                                    //the above contains the xml file we can use view hence
        super.onViewCreated(view, savedInstanceState);
        mInputWhat= (EditText) view.findViewById(R.id.et_drop);
       mInputWhen = (BucketPickerView) view.findViewById(R.id.datepicker);
       mButtonAdd= (Button) view.findViewById(R.id.AddIt);
       mButtonClose= (ImageButton) view.findViewById(R.id.btn_close);
        mButtonClose.setOnClickListener(this);
        mButtonAdd.setOnClickListener(this);
        AppBucketDrop.setRalewayRegular(getActivity(), mInputWhat, mButtonAdd);//custom font

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.AddIt:
                addAction();
                break;
        }

        dismiss();
    }

    private void addAction() {
        String what=mInputWhat.getText().toString();
       // String date=mInputWhen.getDayOfMonth()+"/"+mInputWhen.getMonth()+"/"+mInputWhen.getYear();
        long now=System.currentTimeMillis();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, mInputWhen.getDayOfMonth());
//        calendar.set(Calendar.MONTH, mInputWhen.getMonth());
//        calendar.set(Calendar.YEAR, mInputWhen.getYear());                we dont need this bcoz we have created now our own widget
//        calendar.set(Calendar.HOUR, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
        Realm realm=Realm.getDefaultInstance();
        Drop drop=new Drop(what,now,mInputWhen.getTime(),false); //whole time is converted into millisecond
        realm.beginTransaction();
        realm.copyToRealm(drop);
        realm.commitTransaction();
        realm.close();
    }
}
