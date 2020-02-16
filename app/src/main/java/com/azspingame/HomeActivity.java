package com.azspingame;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.azspingame.R;
import ve.com.abicelis.prizewheellib.PrizeWheelView;
import ve.com.abicelis.prizewheellib.model.MarkerPosition;
import ve.com.abicelis.prizewheellib.model.WheelBitmapSection;
import ve.com.abicelis.prizewheellib.model.WheelColorSection;
import ve.com.abicelis.prizewheellib.model.WheelDrawableSection;
import ve.com.abicelis.prizewheellib.model.WheelSection;

/**
 * Created by abicelis on 25/7/2017.
 */

public class HomeActivity extends AppCompatActivity {

    PrizeWheelView wheelView;
    ImageView homeImage;
    ImageView ivBack;
    Button stop;
    ImageView flingCC;
    Button flingCW;
    final List<WheelSection> wheelSections = new ArrayList<>();


    MediaPlayer mPlayer2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acr_home);

        homeImage = (ImageView) findViewById(R.id.home_image);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        stop = (Button) findViewById(R.id.stop_wheel);
        flingCC = (ImageView) findViewById(R.id.fling_wheel_cc);
        flingCW = (Button) findViewById(R.id.fling_wheel_cw);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheelView.stopWheel();
            }
        });
        flingCW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheelView.flingWheel(20000, true);
            }
        });
        flingCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] arr = {20000, 23000, 25000, 28000, 30000, 33000, 35000,  38000, 40000, 43000, 45000, 48000, 50000};
                playWheelSound();
                wheelView.flingWheel(getRandom(arr), false);
            }
        });


        //Bitmap someBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wheel_icon_coins);

        //Init WheelSection list
        //wheelSections.add(new WheelBitmapSection(someBitmap));
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_5)); // 0
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_10));
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_20));
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_50));
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_100));
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_200));
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_500));
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_vip)); // 7
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_vipgold));
        wheelSections.add(new WheelDrawableSection(R.drawable.ic_jackpot)); //9

//        wheelSections.add(new WheelColorSection(R.color.orange));
  //      wheelSections.add(new WheelColorSection(R.color.blue));


        //Init wheelView and set parameters
        wheelView = (PrizeWheelView) findViewById(R.id.home_prize_wheel_view);
        wheelView.setWheelSections(wheelSections);
        wheelView.setMarkerPosition(MarkerPosition.TOP_RIGHT);

        wheelView.setWheelBorderLineColor(R.color.border);
        wheelView.setWheelBorderLineThickness(0);

        wheelView.setWheelSeparatorLineColor(R.color.separator);
        wheelView.setWheelSeparatorLineThickness(0);

        //Set onSettled listener
        wheelView.setWheelEventsListener(new WheelEventsListener());

        //Finally, generate wheel background
        wheelView.generateWheel();

    }



    private class WheelEventsListener implements ve.com.abicelis.prizewheellib.WheelEventsListener {

        @Override
        public void onWheelStopped() {
            if(mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();

            Toast.makeText(HomeActivity.this, "Oops, Please try again..!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWheelFlung() {
          //  Toast.makeText(HomeActivity.this, "Oops, Please try again..!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWheelSettled(final int sectionIndex, double angle) {
          //  Toast.makeText(HomeActivity.this, "Wheel settled at " + angle + "°. Section #" + sectionIndex, Toast.LENGTH_SHORT).show();

            String strWinAmount = "";
            if (sectionIndex == 0)
                strWinAmount = "5 Coins";
            else if (sectionIndex == 1)
                strWinAmount = "10 Coins";
            else if (sectionIndex == 2)
                strWinAmount = "20 Coins";
            else if (sectionIndex == 3)
                strWinAmount = "50 Coins";
            else if (sectionIndex == 4)
                strWinAmount = "100 Coins";
            else if (sectionIndex == 5)
                strWinAmount = "200 Coins";
            else if (sectionIndex == 6)
                strWinAmount = "500 Coins";
            else if (sectionIndex == 7)
                strWinAmount = "VIP";
            else if (sectionIndex == 8)
                strWinAmount = "VIP GOLD";
            else if (sectionIndex == 9)
                strWinAmount = "JACKPOT";

            if(sectionIndex <= 6)
                playCoinSound();
            else
                playVipJackpotSound();

            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("")
                    .setMessage("You win "+strWinAmount+"")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            dialog.dismiss();
                        }
                    })
                    .show();


            /*Animation fadeOut = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_out);
            homeImage.startAnimation(fadeOut);

            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {

                    WheelSection section = wheelSections.get(sectionIndex);
                    switch (section.getType()){
                        case BITMAP:
                            homeImage.setImageBitmap( ((WheelBitmapSection)section).getBitmap() );
                            break;
                        case DRAWABLE:
                            homeImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), ((WheelDrawableSection)section).getDrawableRes()));
                            break;
                        case COLOR:
                            homeImage.setImageDrawable(null);
                            homeImage.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), ((WheelColorSection)section).getColor()));
                            break;
                    }

                    Animation fadeIn = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_in);
                    homeImage.startAnimation(fadeIn);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });*/

        }
    }


    private void playWheelSound() {
        try {
            if(mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();

            mPlayer2= MediaPlayer.create(this, R.raw.spinning);
            mPlayer2.setLooping(true);
            mPlayer2.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void playCoinSound() {
        try {
            if(mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();

            mPlayer2= MediaPlayer.create(this, R.raw.coins);
            mPlayer2.setLooping(false);
            mPlayer2.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void playVipJackpotSound() {
        try {
            if(mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();

            mPlayer2= MediaPlayer.create(this, R.raw.vip_jackpot);
            mPlayer2.setLooping(false);
            mPlayer2.start();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }




    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


}
