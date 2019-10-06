package com.snailpong.system;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    int grades = 1;
    String teacher_name[] = new String[]{"Mason", "Wolf", "Cakes", "Chick"};
    String teacher_hp[] = new String[]{"1354753485", "1354853548", "1352198634", "1358892215"};
    public ArrayList<Data> grade1 = new ArrayList<Data>();
    public ArrayList<Data> grade2 = new ArrayList<Data>();
    public ArrayList<Data> grade3 = new ArrayList<Data>();
    public ArrayList<Data> grade4 = new ArrayList<Data>();

    public void setGrade(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        final RecyclerAdapter adapter = new RecyclerAdapter();
        adapter.setItem(grade1);
        recyclerView.setAdapter(adapter);

        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyyMMdd").format(cDate);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("StudentData");
        final DatabaseReference myRef2 = database.getReference("StudentManagement").child(fDate);
        DatabaseReference myRef3 = database.getReference("Notification").child(fDate);

        SeekBar s = findViewById(R.id.seekbar);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ((TextView)findViewById(R.id.seektext)).setText(String.valueOf(i+1));
                ((TextView)findViewById(R.id.teacher_name)).setText(teacher_name[i]);
                ((TextView)findViewById(R.id.teacher_hp)).setText(teacher_hp[i]);

                if(i+1 == 1) {
                    grades = 1;
                    adapter.setItem(grade1);
                    recyclerView.setAdapter(adapter);
                } else if(i+1 == 2){
                    grades = 2;
                    adapter.setItem(grade2);
                    recyclerView.setAdapter(adapter);
                } else if(i+1 == 3){
                    grades = 3;
                    adapter.setItem(grade3);
                    recyclerView.setAdapter(adapter);
                } else if(i+1 == 4){
                    grades = 4;
                    adapter.setItem(grade4);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas : dataSnapshot.getChildren()){
                    if(datas.getKey().equals("Grade1"))   // Grade1의 학생들에 대해서
                        for(DataSnapshot data : datas.getChildren()) grade1.add(data.getValue(Data.class));
                    if(datas.getKey().equals("Grade2"))
                        for(DataSnapshot data : datas.getChildren()) grade2.add(data.getValue(Data.class));
                    if(datas.getKey().equals("Grade3"))
                        for(DataSnapshot data : datas.getChildren()) grade3.add(data.getValue(Data.class));
                    if(datas.getKey().equals("Grade4"))
                        for(DataSnapshot data : datas.getChildren()) grade4.add(data.getValue(Data.class));
                }

                if(grades == 1) adapter.setItem(grade1);
                if(grades == 2) adapter.setItem(grade2);
                if(grades == 3) adapter.setItem(grade3);
                if(grades == 4) adapter.setItem(grade4);
                recyclerView.setAdapter(adapter);

                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("w", String.valueOf(dataSnapshot.getChildrenCount()));
                        for(DataSnapshot datas : dataSnapshot.getChildren()){
                            if(datas.getKey().equals("Grade1"))
                                for(DataSnapshot data : datas.getChildren()){
                                    Map<String, String> map = (Map) data.getValue();  // map은 이름, 부모 정보 등을 저장함
                                    for(Data d : grade1){  // 원래 존재하는 학생을 학생 이름으로 찾는다.
                                        if(d.getId().equals(data.getKey())){
                                            d.setBusnumber(data.child("busnumber").getValue(String.class));
                                            d.setBusdriver(data.child("busdriver").getValue(String.class));
                                            d.setTime(data.child("time").getValue(String.class));
                                            d.setStates(data.child("states").getValue(String.class));
                                        }
                                    }
                                }
                            if(datas.getKey().equals("Grade2"))
                                for(DataSnapshot data : datas.getChildren()) grade2.add(data.getValue(Data.class));
                            if(datas.getKey().equals("Grade3"))
                                for(DataSnapshot data : datas.getChildren()) grade3.add(data.getValue(Data.class));
                            if(datas.getKey().equals("Grade4"))
                                for(DataSnapshot data : datas.getChildren()) grade4.add(data.getValue(Data.class));
                        }

                        if(grades == 1) adapter.setItem(grade1);
                        if(grades == 2) adapter.setItem(grade2);
                        if(grades == 3) adapter.setItem(grade3);
                        if(grades == 4) adapter.setItem(grade4);
                        recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) { }
                });

            }

            @Override
            public void onCancelled(DatabaseError error) { }
        });

        myRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null && dataSnapshot.getValue(Integer.class) == 1){
                    int flag = 0;
                    for(Data data : grade1){
                        if(data.getStates() == null){
                            flag = 1;
                            break;
                        }
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "default");
                    builder.setSmallIcon(R.mipmap.ic_launcher);
                    if(flag == 1)
                        builder.setContentTitle("Someone is missing!");
                    else
                        builder.setContentTitle("Bus left normally.");
                    builder.setContentText("Please check it out the information.");

                    NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                    }
                    notificationManager.notify(1, builder.build());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }
    public class DividerItemDecorator extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public DividerItemDecorator(Drawable divider) {
            mDivider = divider;
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i <= childCount - 2; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivider.draw(canvas);
            }
        }
    }
}
