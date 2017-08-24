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
import android.view.View;


public class OpenGLSurfaceView extends GLSurfaceView implements View.OnClickListener {
    private GestureDetector move_gesture;


    OpenGLProjectRenderer renderer;

    private class PointerState {
        int index;
        boolean need_update=true;
        Vector3f world = new Vector3f();
        PointF screen = new PointF();
    }

    private PointerState rotation_point;
    private PointerState rotation_stable_point;
    private PointerState last_coords[] = new PointerState[3];


    //специально для углов
    public OpenGLSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
        for(int i=0; i < 3; i++){
            last_coords[i] = new PointerState();
            last_coords[i].index = i;
        }
        this.requestFocus();
        this.setFocusableInTouchMode(true);
        move_gesture = new GestureDetector(context,new MoveListener());

        Matrix4f matrix = new Matrix4f();
        matrix.setLookAt(new Vector3f(-2.0590067f,-0.23856598f,30.0f), new Vector3f(-2.0590067f,-0.23856598f,0.0f), new Vector3f(0.36845762f,0.9296446f,0.0f));
        Log.w("W", matrix.debug());
    }

    //Работкает :3
    @Override
    public void setRenderer(Renderer r){
        super.setRenderer(r);
        this.renderer = (OpenGLProjectRenderer)r; // Если буду использовать только OpenGLRenderer, иначе -> исключения
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP)
        {
            for(PointerState state: last_coords)
                state.need_update = true;
        }
        return move_gesture.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        Log.w("event","onClick " + view);
    }


    private class MoveListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onContextClick(MotionEvent e) {
            Log.w("event","onContextClick");
            return  true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.w("event","onSingleTapUp");
            return  true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            Log.w("event","event release");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e){
        }

        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            Log.w("event","double click");
            return true;
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            rotation_stable_point = null;
            updateLastCoords(0,motionEvent);
            return true;
        }


        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.w("event","move event press");
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            boolean res=true;
            final int ROTATE_PER_0 = 0x1;
            final int ROTATE_PER_1 = 0x2;

            switch (motionEvent1.getPointerCount()) {
                    case 1:
                        if(!last_coords[0].need_update) {
                            final float x = motionEvent1.getX();
                            final float y = motionEvent1.getY();
                            Vector3f dif = last_coords[0].world.sub(renderer.camera.unprojectPlane(x, y));
                            renderer.camera.translate(dif.x(), dif.y());
                        }
                        updateLastCoords(0, motionEvent1);
                        break;
                    case 2:
                        if(!last_coords[1].need_update){
                            PointF dif[] = new PointF[]{getScreenPositionOffset(0, motionEvent1), getScreenPositionOffset(1, motionEvent1)};

                            int rotation_mask = 0;
                            float whs = renderer.camera.height + renderer.camera.width;
                            rotation_mask |= ((dif[1].length() / whs) > 0.01) ? ROTATE_PER_1 : 0;
                            rotation_mask |= ((dif[0].length() / whs) > 0.01) ? ROTATE_PER_0 : 0;

                            switch (rotation_mask){
                                case  ROTATE_PER_0:
                                    if(rotation_stable_point != last_coords[1]) {
                                        rotation_stable_point = last_coords[1];
                                        rotation_point = last_coords[0];
                                    }
                                    break;
                                case  ROTATE_PER_1:
                                    if(rotation_stable_point != last_coords[0]) {
                                        rotation_stable_point = last_coords[0];
                                        rotation_point = last_coords[1];
                                    }
                                    break;
                                default:
                                    break;
                            }
                            Log.w("W", "innoDB " + rotation_mask + " " + dif[1].length() + " " + dif[0].length());

                            if(rotation_stable_point != null) {
                                renderer.camera.setRotationPoint(rotation_stable_point.world);
                                int rindex = rotation_point.index;
                                int rsindex = rotation_stable_point.index;
                                Vector3f rsPoint = last_coords[rsindex].world;
                                Vector3f now = renderer.camera.unprojectPlane(motionEvent1.getX(rindex), motionEvent1.getY(rindex));
                                Vector3f before = last_coords[rindex].world;
                                float angle = angleBtwLines(
                                        now.x(), now.y(), rsPoint.x(), rsPoint.y(),
                                        before.x(), before.y(), rsPoint.x(), rsPoint.y()
                                );

                                renderer.camera.rotate(-angle);
                            }
                            else return true;
                        }
                        updateLastCoords(0,motionEvent1);
                        updateLastCoords(1,motionEvent1);
                        break;
                    case 3:
                        break;
                    default: res = false;
                        break;
            }
            return  res;
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            Log.e("W", "onRelease");

            //release move event
            return false;
        }

        private float angleBtwLines (float fx1, float fy1, float fx2, float fy2, float sx1, float sy1, float sx2, float sy2){
            float angle1 = (float) Math.atan2(fy1 - fy2, fx1 - fx2);
            float angle2 = (float) Math.atan2(sy1 - sy2, sx1 - sx2);
            return (float) Math.toDegrees((angle1-angle2));
        }

        private PointF getScreenPositionOffset(int index, MotionEvent event){
            return  new PointF(event.getX(index) - last_coords[index].screen.x, event.getY(index) - last_coords[index].screen.y);
        }

        private void updateLastScreenPosition(int index, MotionEvent event){
            last_coords[index].screen = new PointF(event.getX(index), event.getY(index));
        }
        private void updateLastWorldPosition(int index, MotionEvent event){
            last_coords[index].world = renderer.camera.unprojectPlane(event.getX(index),event.getY(index));
            last_coords[index].need_update = false;
        }

        private void updateLastCoords(int index, MotionEvent event){
            updateLastScreenPosition(index, event);
            updateLastWorldPosition(index, event);
        }

    }
}