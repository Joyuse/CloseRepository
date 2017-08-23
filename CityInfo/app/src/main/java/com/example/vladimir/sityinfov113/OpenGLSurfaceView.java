package com.example.vladimir.sityinfov113;

/**
 * Created by Vladimir on 23.06.2017.
 */

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


public class OpenGLSurfaceView extends GLSurfaceView implements View.OnClickListener {
    private ScaleGestureDetector scale_gesture;
    private GestureDetector move_gesture;


    OpenGLProjectRenderer renderer;
    int flag =0; //для проверки
    private PointF last_point;
    private Vector3f last_world_pos;

    //специально для углов
    public OpenGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
        scale_gesture = new ScaleGestureDetector(context, new ScaleListener());
        move_gesture = new GestureDetector(context,new MoveListener());
    }

    public void setMyCustomRenderer(OpenGLProjectRenderer r){
        setRenderer(r);
        this.renderer = r; // вроде должно работать для всех, но не факт, я не проверял:D
    }

    //Работкает :3
    @Override
    public void setRenderer(Renderer r){
        super.setRenderer(r);
        this.renderer = (OpenGLProjectRenderer)r; // Если буду использовать только OpenGLRenderer, иначе -> исключения
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(move_gesture.onTouchEvent(event))
            Log.e("Gesture res", "Move ok");
        else if(scale_gesture.onTouchEvent(event))
            Log.e("Gesture res", "Scale ok");
//        //событие
//        int action = event.getAction();
//
//        if (action == MotionEvent.ACTION_DOWN) {
//            final float x = event.getX();   //(NEW)
//            final float y = event.getY();   //(NEW)
//            last_point.set(x,y);    //(NEW)
//            flag =0;
//
//            last_world_pos = renderer.camera.unprojectPlane(last_point.x,last_point.y);
//
////            GLU.gluUnProject(mLastTouchX,mLastTouchY)
//
//            return true;
//        }
//
//        switch (event.getPointerCount()) {
//            case 3:
//                //3 пальца
//                //Log.e("Event", "Пальцев = " +event.getPointerCount());
//                return scale_gesture.onTouchEvent(event);
//            case 2:
//                //2 пальца
//                //Log.e("Event", "Пальцев = " +event.getPointerCount());
//                return doRotationEvent(event);
//            case 1:
//                //1 палец
//                Log.e("Event", "Пальцев = " +event.getPointerCount());
//                return doMoveEvent(event);
//        }
        return true;
    }

    //Передвижение
    private boolean doMoveEvent(MotionEvent event)
    {
        final int action = event.getAction();
        //Пока идет действие
        switch (action) {
            //Если Дейсвтие - движение
            case MotionEvent.ACTION_MOVE: {
                flag ++;
                if (flag > 4) {
                    //Считываем координаты пальца
                    final float x = event.getX();
                    final float y = event.getY();
                    Vector3f dif = last_world_pos.dif(renderer.camera.unprojectPlane(last_point.x,last_point.y));
                    renderer.camera.translate(dif.x(), dif.y());
                    last_world_pos = renderer.camera.unprojectPlane(last_point.x,last_point.y);
                    //Посление касание
                    last_point.set(x,y);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                flag = 0;
                break;
        }
        return true;
    }

    //Повороты
    private boolean doRotationEvent(MotionEvent event) {
        //расчитываем угол менжду двумя пальцами
        float deltaX = event.getX(1) - event.getX(0);
        float deltaY = event.getY(1) - event.getY(0);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                flag = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP: //Если поднят палец №2
                flag = 0;
                break;
            case MotionEvent.ACTION_UP: //Если поднят палец №1
                flag = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                flag ++;
                double radians = Math.atan(deltaY / deltaX);

                if (flag >10) {

                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Log.w("event","onClick " + view);
    }

    //Отдаляет по Z
    private class MoveListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            Log.w("event","release");
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            Log.w("event","double click");

            return true;
        }

//        @Override
//        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
//            return true;
//        }


        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.w("event","move event press");
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            if(motionEvent1.getPointerCount() > 1)
                return false;
            else{
                Log.w("event","move event " + motionEvent1.getPointerCount());
                //move event
                return true;
            }
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.w("event","move event release");
            //release move event
            return false;
        }
    }
    //Отдаляет по Z
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            renderer.camera.zoom(detector.getScaleFactor());
            Log.w("event","scale");
            return false;
        }
    }
}