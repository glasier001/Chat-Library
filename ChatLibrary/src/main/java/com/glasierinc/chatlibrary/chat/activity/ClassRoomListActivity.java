package com.glasierinc.chatlibrary.chat.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.glasierinc.chatlibrary.R;
import com.glasierinc.chatlibrary.chat.adapters.ClassRoomListAdapter;
import com.glasierinc.chatlibrary.chat.util.ClassRooomListItemClickListener;
import com.glasierinc.chatlibrary.chat.util.Constants;
import com.glasierinc.chatlibrary.webservice.MentalHealthWebService;
import com.glasierinc.chatlibrary.webservice.RetrofitClientInstance;
import com.glasierinc.chatlibrary.webservice.responsepojo.ClassroomListResp;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassRoomListActivity extends AppCompatActivity  {

    String TAG = getClass().getSimpleName();
    Activity context = ClassRoomListActivity.this;
    Toolbar toolbar_top;
    RecyclerView rcv_testRound;
    CircularFillableLoaders mGeometricProgressView;
    private List<ClassroomListResp.RoomList> examTipsList = new ArrayList<>();
    ClassRoomListAdapter mExamTipsAdapter;
    String productId, title, time, audio_link;
    String quizId;
    LinearLayout ll_listeningpart;
    boolean isConnected = false;
    ClassroomListResp.RoomList mExamTipsModel;
    String exam = "";
    String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room_list);
        findViewById();
        toolBar();
        type = getIntent().getStringExtra("classuserType");
    }

    @Override
    public void onDestroy() {
//        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void toolBar() {
        setSupportActionBar(toolbar_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Class Room List");
        toolbar_top.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Constants.isInternetAvailable(context)) {
            getClassRoomList();
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void findViewById() {
        ll_listeningpart = (LinearLayout) findViewById(R.id.ll_listeningpart);
        rcv_testRound = findViewById(R.id.rcv_testRound);
        toolbar_top = findViewById(R.id.toolbar_top);
        mGeometricProgressView = findViewById(R.id.progressView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv_testRound.setLayoutManager(mLayoutManager);
        rcv_testRound.setItemAnimator(new DefaultItemAnimator());

    }



    public void getClassRoomList() {

        Call<ClassroomListResp> call = RetrofitClientInstance.getRetrofitClassRoom().create(MentalHealthWebService.class).getClassroomList(getIntent().getStringExtra("classuserId"),getIntent().getStringExtra("classuserType"));

        call.enqueue(new Callback<ClassroomListResp>() {
            @Override
            public void onResponse(Call<ClassroomListResp> call, Response<ClassroomListResp> response) {

                if ( response.body().getStatus()) {
                    for (int i = 0 ; i<response.body().room.size();i++) {
                        examTipsList.clear();
                        examTipsList.add(response.body().room.get(i));
                    }

                    setAdapter();



                } else {
                    //dialog.dismiss();
                    showToast(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ClassroomListResp> call, Throwable t) {
                Log.v("XXX", "" + t.getMessage());
                showToast("" + t.getMessage());
            }
        });


    }


    public void showToast(String message) {
        Toast.makeText(this, message.trim(), Toast.LENGTH_SHORT).show();
    }



    private void setAdapter() {


        mExamTipsAdapter = new ClassRoomListAdapter(examTipsList, this, false, new ClassRooomListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean isFromGameBooster, ClassroomListResp.RoomList mExamTipsModel) {
                ClassRoomListActivity.this.mExamTipsModel = mExamTipsModel;
                Log.e("roomggjbn",mExamTipsModel.room_id);

               // Intent intent = new Intent(ClassRoomListActivity.this, ClassRoomUserListActivity.class);
                Intent intent = new Intent(ClassRoomListActivity.this, ClassRoomChatActivity.class);
                intent.putExtra("userId", getIntent().getStringExtra("classuserId"));
//                intent.putExtra("userEmail", getIntent().getStringExtra("classUserEmail"));
                intent.putExtra("title", mExamTipsModel.room_name);
                intent.putExtra("roomId", mExamTipsModel.room_id);
                intent.putExtra("userType",type);
                startActivity(intent);
            }
        });



                rcv_testRound.setAdapter(mExamTipsAdapter);
    }


}
