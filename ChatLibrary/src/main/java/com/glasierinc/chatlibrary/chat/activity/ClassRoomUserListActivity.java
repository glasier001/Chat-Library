package com.glasierinc.chatlibrary.chat.activity;


import static com.glasierinc.chatlibrary.chat.util.ChatConstance.JOIN_EVENT;
import static com.glasierinc.chatlibrary.chat.util.ChatConstance.ROOM_USERS;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.commonlib.constants.AppUrls;
import com.google.gson.Gson;
import com.glasierinc.chatlibrary.R;
import com.glasierinc.chatlibrary.chat.adapters.ClassRoomUserListAdapter;
import com.glasierinc.chatlibrary.chat.model.RoomUserList;
import com.glasierinc.chatlibrary.chat.model.UserRootResp;
import com.glasierinc.chatlibrary.chat.util.ClassRoomUserListItemClickListener;
import com.glasierinc.chatlibrary.chat.util.Constants;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

public class ClassRoomUserListActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    Activity context = ClassRoomUserListActivity.this;
    Toolbar toolbar_top;
    RecyclerView rcv_testRound;
    CircularFillableLoaders mGeometricProgressView;
    private List<RoomUserList> examTipsList = new ArrayList<>();
    ClassRoomUserListAdapter mExamTipsAdapter;
    String productId, title, time, audio_link;
    String quizId;
    LinearLayout ll_listeningpart;
    boolean isConnected = false;
//    ClassroomGetUserList.RoomUserList mExamTipsModel;
    RoomUserList mExamTipsModel;
    String exam = "";

    TextView toolbarTitle,toolbarSubTitle;
    ImageView back;
    //socketIO
    Socket mSocket;
    String gotUserType ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room_user_list);
        findViewById();
        toolBar();
        gotUserType = getIntent().getStringExtra("userTypeChat");



    }





    @Override
    public void onDestroy() {
//        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    private void toolBar() {
//        setSupportActionBar(toolbar_top);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(getIntent().getStringExtra("titleChat"));
//        toolbar_top.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        toolbarTitle.setText(getIntent().getStringExtra("titleChat"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Constants.isInternetAvailable(context)) {
//            getClassRoomUserList();
            socketInitConnection();
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
    private void socketInitConnection() {
        try {

            IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME};
            mSocket = IO.socket(AppUrls.CHAT_SERVER_URL, opts);
        } catch (URISyntaxException e) {
            Log.e("err", e.getMessage());
        }

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(ROOM_USERS, onRoomUsers);

        mSocket.connect();


    }


    private Emitter.Listener onRoomUsers = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Log.e(TAG, "roomUsersResp" + args[0].toString());


                    // always remember in the gson user_list != userList the key should be same as of response received from the socket.

                    Gson gson = new Gson();
                    UserRootResp obj = gson.fromJson(args[0].toString(), UserRootResp.class);
                    examTipsList.clear();
                    examTipsList.addAll(obj.users_list);


                    Log.e("examTipsListSize",examTipsList.size()+" participants");

                    toolbarSubTitle.setText(examTipsList.size()+" participants");


                    setAdapter();

//                    messageAdapterSocketIO.notifyDataSetChanged();

                }
            });
        }


    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "connecting");

//                    Log.e("appId", SharedPrefs.getSharedPrefString(getApplicationContext(), com.pasupal.android.appconstants.JsonKeys.JK_USER_APPOINTMENT_ID, ""));
//                    Log.e("userId", mUserId);

                    try {


                        mSocket.emit(JOIN_EVENT,getIntent().getStringExtra("roomIdChat"),getIntent().getStringExtra("userIdChat"),gotUserType);
                        Log.e(TAG, "joinRoom");
                        //mSocket.on(REC_ALL_MESSAGE_EVENT, onAllMessage);

                    } catch (Exception e) {
                        Log.e("Exp", e.getMessage());
                        Log.e(TAG, "joinFailed");

                    }

                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");

                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");

                }
            });
        }
    };

    private void findViewById() {
        ll_listeningpart = (LinearLayout) findViewById(R.id.ll_listeningpart);
        rcv_testRound = findViewById(R.id.rcv_testRound);
        toolbar_top = findViewById(R.id.toolbar_top);
        toolbarTitle = findViewById(R.id.message_title);
        toolbarSubTitle = findViewById(R.id.message_subTitle);
        back = findViewById(R.id.imageView4);
        mGeometricProgressView = findViewById(R.id.progressView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv_testRound.setLayoutManager(mLayoutManager);
        rcv_testRound.setItemAnimator(new DefaultItemAnimator());

    }


//    public void getClassRoomUserList() {
//
////        Call<ClassroomGetUserList> call = RetrofitApiClient.getRetrofitClassRoom().create(ApiInterface.class).classroomGetUserLIst(getIntent().getStringExtra("roomIdChat"));
////
////        call.enqueue(new Callback<ClassroomGetUserList>() {
////            @Override
////            public void onResponse(Call<ClassroomGetUserList> call, Response<ClassroomGetUserList> response) {
////
////                if (response.body().getStatus()) {
////                    for (int i = 0; i < response.body().userList.size(); i++) {
////
////                        examTipsList.add(response.body().userList.get(i));
////                    }
////                    toolbarSubTitle.setText(response.body().userList.size()+" participants");
////
////                    setAdapter();
////
////
////                } else {
////                    //dialog.dismiss();
////                    showToast(response.body().getMessage());
////                }
////            }
////
////            @Override
////            public void onFailure(Call<ClassroomGetUserList> call, Throwable t) {
////                Log.v("XXX", "" + t.getMessage());
////                showToast("" + t.getMessage());
////            }
////        });
//
//
//    }



    public void showToast(String message) {
        Toast.makeText(this, message.trim(), Toast.LENGTH_SHORT).show();
    }


    private void setAdapter() {


        mExamTipsAdapter = new ClassRoomUserListAdapter(examTipsList, this, false,getIntent().getStringExtra("userIdChat"), new ClassRoomUserListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean isFromGameBooster,RoomUserList mExamTipsModel) {
                ClassRoomUserListActivity.this.mExamTipsModel = mExamTipsModel;
            }
        });
        for (int pos = 0; pos < examTipsList.size(); pos++) {
            if (examTipsList.get(pos).user_id.equals(getIntent().getStringExtra("userIdChat"))) {
                Collections.swap(examTipsList, pos, 0);
                mExamTipsAdapter.notifyItemMoved(pos, 0);
            }
        }

        rcv_testRound.setAdapter(mExamTipsAdapter);
    }


}
