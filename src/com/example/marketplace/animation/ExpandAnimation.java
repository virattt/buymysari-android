package com.example.marketplace.animation;

import android.view.ViewGroup.LayoutParams;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

public abstract class ExpandAnimation extends Animation implements Animation.AnimationListener {
	 private View view;
	    private static int ANIMATION_DURATION;
	    private int LastWidth;
	    private int FromWidth;
	    private int ToWidth;
	    private static int STEP_SIZE=30;
    
    public ExpandAnimation(View v, int FromWidth, int ToWidth, int Duration) {
        
    	 this.view = v;
         ANIMATION_DURATION = 1;
         this.FromWidth = FromWidth;
         this.ToWidth = ToWidth;
         setDuration(ANIMATION_DURATION);
         setRepeatCount(20);
         setFillAfter(false);
         setInterpolator(new AccelerateInterpolator());
         setAnimationListener(this);
    }
    
    public void onAnimationend(Animation anim) {
        // TODO Auto-generated method stub
         
    }
 
    public void onAnimationrepeat(Animation anim) {
        // TODO Auto-generated method stub
        LayoutParams lyp =  view.getLayoutParams();
        lyp.width = LastWidth +=ToWidth/20;
        view.setLayoutParams(lyp);
    }
 
    public void onAnimationstart(Animation anim) {
        // TODO Auto-generated method stub
        LayoutParams lyp =  view.getLayoutParams();
        lyp.width = 0;
        view.setLayoutParams(lyp);
        LastWidth = 0;
    }
}