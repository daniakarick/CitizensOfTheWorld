package com.example.danie.comcet325bg46ic.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.danie.comcet325bg46ic.AddLocation;
import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.helpers.SQLDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    GestureDetector detector;
    ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cover = (ImageView)findViewById(R.id.coverPhoto);
        detector = new GestureDetector(this,this);

        //Populate();
    }

    public void OpenActivity(View v){
        Intent intent = new Intent(this,AddLocation.class);
        startActivity(intent);
    }

    public void OpenBudgetPlanner(View v){
        Intent budgetPlanIntent = new Intent(this,BudgetPlanner.class);
        startActivity(budgetPlanIntent);
    }

    public boolean onTouchEvent(MotionEvent event){
        return detector.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        Intent intent = new Intent(getApplicationContext(),LocationsList.class);
        startActivity(intent);
        overridePendingTransition(R.xml.slide_up,R.xml.slide_down);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    public void Populate(){
        SQLDatabase db = new SQLDatabase(this);

        Location MtFuji = new Location();
        MtFuji.Favorite = true;
        MtFuji.Name = "Mount Fuji";
        MtFuji.Location = "Kitayama, Japan";
        MtFuji.Description = "Active volcano";
        MtFuji.GeoLocation[0] = 35.3605555;
        MtFuji.GeoLocation[1] = 138.725589;
        MtFuji.Notes = "Here are some notes";

        Location imperialPalace = new Location();
        imperialPalace.Name = "Imperial Palace";
        imperialPalace.Location = "Tokyo, Japan";
        imperialPalace.Description = "Primary residence of the Emperor of Japan";
        imperialPalace.Favorite = true;
        imperialPalace.GeoLocation[0] = 35.685175;
        imperialPalace.GeoLocation[1] = 139.7506108;
        imperialPalace.Notes = "here are some notes";

        Location museum = new Location();
        museum.Name = "National Museum of Nature and Science";
        museum.Location = "Taito, Tokyo, Japan";
        museum.Description = "Opened in 1871";
        museum.GeoLocation[0] = 35.716357;
        museum.GeoLocation[1] = 139.7741939;
        museum.Notes = "here are some notes";

        db.addLocation(MtFuji);
        db.addLocation(museum);
        db.addLocation(imperialPalace);

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
