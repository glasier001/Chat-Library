package com.glasierinc.chatmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

//import com.glasierinc.chatlibrary.chat.activity.ClassRoomListActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, ClassRoomListActivity.class);
//        intent.putExtra("classuserId", "4200");
//        intent.putExtra("classUserEmail", "studentdeveloper1@test.com");
//        intent.putExtra("classuserType","learner");
//        startActivity(intent);
    }
}