package com.randyr.macysscanner.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.randyr.macysscanner.R;

public class ProgressDia extends Dialog
{
    private final long DURATION=1000;
    private Context context;
    private int screenHeight;
    private int current;
    private int[] incr;

    private TextView indicator;

    public ProgressDia(Context context)
    {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dia);
        initUi();
    }

    private void initUi()
    {
        indicator=(TextView)findViewById(R.id.progress_dia_ind);
        buildSteps();
    }

    private void buildSteps()
    {
        current=0;
        screenHeight=this.getWindow().getAttributes().height;
        incr=new int[]{screenHeight/25,screenHeight/50,screenHeight/75,this.getWindow().getAttributes().height};
    }

    public void takeStep(int step)
    {
        final LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (step)
        {
            case 0:
                for(int a=0;a<=step;a++)
                {
                    indicator.setText(current+1);
                    current++;

                    Animation animation=new Animation()
                    {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t)
                        {
                            params.setMargins(0,0,0,incr[0]);
                            indicator.setLayoutParams(params);
                        }
                    };

                    animation.setDuration(DURATION);
                    indicator.startAnimation(animation);
                }
                break;
            case 1:
                for(int a=50;a<=step;a++)
                {
                    indicator.setText(current+1);
                    current++;

                    Animation animation=new Animation()
                    {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t)
                        {
                            params.setMargins(0,0,0,incr[1]);
                            indicator.setLayoutParams(params);
                        }
                    };
                    animation.setDuration(DURATION);
                    indicator.startAnimation(animation);
                }
                break;
            case 2:
                for(int a=75;a<=step;a++)
                {
                    indicator.setText(current+1);
                    current++;

                    Animation animation=new Animation()
                    {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t)
                        {
                            params.setMargins(0,0,0,incr[2]);
                            indicator.setLayoutParams(params);
                        }
                    };
                    animation.setDuration(DURATION);
                    indicator.startAnimation(animation);
                }
                break;
            case 3:
                for(int a=100;a<=step;a++)
                {
                    indicator.setText(current+1);
                    current++;

                    Animation animation=new Animation()
                    {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t)
                        {
                            params.setMargins(0,0,0,incr[3]);
                            indicator.setLayoutParams(params);
                        }
                    };
                    animation.setDuration(DURATION);
                    indicator.startAnimation(animation);
                    this.dismiss();
                }
                break;
        }
    }
}
