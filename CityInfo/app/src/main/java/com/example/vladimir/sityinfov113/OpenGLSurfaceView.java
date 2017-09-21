package com.example.vladimir.sityinfov113;

/**
 * Created by Vladimir on 23.06.2017.
 */

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OpenGLSurfaceView extends GLSurfaceView implements View.OnClickListener {

    private GestureDetector move_gesture;
    private MoveListener move_listener;
    OpenGLProjectRenderer renderer;

    //специально для углов
    public OpenGLSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
    }

    //Работкает :3
    @Override
    public void setRenderer(Renderer r){
        super.setRenderer(r);
        super.setRenderMode(RENDERMODE_CONTINUOUSLY);
        this.renderer = (OpenGLProjectRenderer)r;
        move_gesture = new GestureDetector(this.getContext(),move_listener = new MoveListener(renderer.camera));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(move_listener != null) {
            if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
                move_listener.resetStates();
            }
            return move_gesture.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        Log.w("event","onClick " + view);
    }
}