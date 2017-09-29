package com.scarecrow.root.orchardapp.activties;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.scarecrow.root.orchardapp.R;

/**
 * Created by root on 17-9-13.
 */

public class ActivityWelcomePage extends AppCompatActivity {
    ImageView mWelcImageView;

    @Override
    public void onCreate(Bundle onSaveInstante) {
        super.onCreate(onSaveInstante);
        setContentView(R.layout.activity_welcome);
        mWelcImageView = (ImageView) findViewById(R.id.welcome_img_view);
        AlphaAnimation anim = new AlphaAnimation(0.2f, 0.8f);
        anim.setDuration(2000);
        anim.setFillAfter(true);
        mWelcImageView.setAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent in = new Intent(getBaseContext(), ActivityMainInterface.class);
                startActivity(in);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
