package com.example.androidlab5.likes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.androidlab5.R;

public class ActivityForLikes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);
        RecyclerView view = findViewById(R.id.liked_recycler);
        view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerAdapterForLikes adapter = new RecyclerAdapterForLikes(getApplicationContext());
        view.setAdapter(adapter);
    }
}
