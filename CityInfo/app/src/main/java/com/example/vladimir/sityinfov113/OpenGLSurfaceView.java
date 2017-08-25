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
    private static final int FIRST_POINTER_COMMON = 0;
    private static final int SECOND_POINTER_COMMON = 1;
    private static final int TOTAL_POINTERS = 2;


    private GestureDetector move_gesture;
    private MoveListener move_listener;

    OpenGLProjectRenderer renderer;



    //специально для углов
    public OpenGLSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);

        this.requestFocus();
        this.setFocusableInTouchMode(true);
        move_gesture = new GestureDetector(context,move_listener = new MoveListener());

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
            move_listener.resetStates();
        }
        return move_gesture.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        Log.w("event","onClick " + view);
    }


    private class MoveListener extends GestureDetector.SimpleOnGestureListener{
        private RotationHelper rotation_helper = new RotationHelper();
        private class PointerState {
            int index;
            boolean need_update=true;
            Vector3f world = new Vector3f();
            PointF screen = new PointF();

            public PointerState(){}
            private PointerState(PointerState other){
                this.index = other.index;
                this.need_update = other.need_update;
                this.world.set(other.world.x(), other.world.y(), other.world.z());
                this.screen.set(other.screen.x, other.screen.y);
            }
        }
        private PointerState last_coords[] = new PointerState[TOTAL_POINTERS];

        public MoveListener(){
            for(int i=0; i < TOTAL_POINTERS; i++){
                last_coords[i] = new PointerState();
                last_coords[i].index = i;
            }
        }

        public void resetStates(){
            for(PointerState state: last_coords)
                state.need_update = true;
            Log.e("FF", "" + last_coords[SECOND_POINTER_COMMON].need_update);
        }

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
            rotation_helper.reset();
            resetStates();
            return true;
        }


        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.w("event","move event press");
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            boolean res=true;
            PointerState fp = last_coords[FIRST_POINTER_COMMON];
            PointerState sp = last_coords[SECOND_POINTER_COMMON];

            switch (motionEvent1.getPointerCount()) {
                case 1:
                    if(!fp.need_update) {
                        final float x = motionEvent1.getX();
                        final float y = motionEvent1.getY();
                        Vector3f dif = fp.world.subed(renderer.camera.unprojectPlane(x, y));
                        renderer.camera.translate(dif.x(), dif.y());
                    }
                    updateLastCoords(FIRST_POINTER_COMMON, motionEvent1);
                    break;
                case 2:
                    Log.w("W", "sp " + sp.need_update);

                    if(sp.need_update){
                        updateLastCoords(FIRST_POINTER_COMMON,motionEvent1);
                        updateLastCoords(SECOND_POINTER_COMMON,motionEvent1);
                        rotation_helper.startRotation(fp,sp);
                    }
                    else{
                        rotation_helper.rotate(motionEvent1);
                        updateLastCoords(FIRST_POINTER_COMMON,motionEvent1);
                        updateLastCoords(SECOND_POINTER_COMMON,motionEvent1);
                    }
                    break;
                case 3:
                    //renderer.camera.zoom(dif[1].length() - dif[0].length());
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

        private PointF getScreenPositionOffset(int index, MotionEvent event, PointerState  pointers[]){
            return  new PointF(event.getX(index) - pointers[index].screen.x, event.getY(index) - pointers[index].screen.y);
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


        private class RotationHelper{
            private PointerState rotation_point;
            private PointerState rotation_stable_point;

            private PointerState rotation_start_pointers_copy[] = new PointerState[TOTAL_POINTERS];

            final int ROTATE_PER_0 = 0x1;
            final int ROTATE_PER_1 = 0x2;

            public void reset(){
                rotation_stable_point = null;
            }

            public void startRotation(PointerState fs, PointerState ss){
                rotation_start_pointers_copy[FIRST_POINTER_COMMON] = new PointerState(fs);
                rotation_start_pointers_copy[SECOND_POINTER_COMMON] = new PointerState(ss);
            }

            public void rotate(MotionEvent event) {
                PointerState fp = last_coords[FIRST_POINTER_COMMON];
                PointerState sp = last_coords[SECOND_POINTER_COMMON];
                if (!sp.need_update) {
                    PointF dif[] = new PointF[]{getScreenPositionOffset(FIRST_POINTER_COMMON, event, rotation_start_pointers_copy), getScreenPositionOffset(SECOND_POINTER_COMMON, event,rotation_start_pointers_copy)};
                    Log.e("W", "" + dif[0].x + " " + dif[0].y + " " + dif[1].x + " " + dif[1].y);
                    int rotation_mask = 0;
                    float whs = renderer.camera.height + renderer.camera.width;
                    rotation_mask |= ((dif[SECOND_POINTER_COMMON].length() / whs) > 0.01) ? ROTATE_PER_1 : 0;
                    rotation_mask |= ((dif[FIRST_POINTER_COMMON].length() / whs) > 0.01) ? ROTATE_PER_0 : 0;

                    switch (rotation_mask) {
                        case ROTATE_PER_0:
                            if (rotation_stable_point != sp) {
                                rotation_stable_point = sp;
                                rotation_point = fp;
                            }
                            break;
                        case ROTATE_PER_1:
                            if (rotation_stable_point != fp) {
                                rotation_stable_point = fp;
                                rotation_point = sp;
                            }
                            break;
                        default:
                            break;
                    }

                    if (rotation_stable_point != null) {
                        rotation_start_pointers_copy[FIRST_POINTER_COMMON] = last_coords[FIRST_POINTER_COMMON];
                        rotation_start_pointers_copy[SECOND_POINTER_COMMON] = last_coords[SECOND_POINTER_COMMON];
                        renderer.camera.setRotationPoint(rotation_stable_point.world);
                        int rindex = rotation_point.index;
                        int rsindex = rotation_stable_point.index;
                        Vector3f rsPoint = last_coords[rsindex].world;
                        Vector3f now = renderer.camera.unprojectPlane(event.getX(rindex), event.getY(rindex));
                        Vector3f before = last_coords[rindex].world;
                        float angle = angleBtwLines(
                                now.x(), now.y(), rsPoint.x(), rsPoint.y(),
                                before.x(), before.y(), rsPoint.x(), rsPoint.y()
                        );
                        renderer.camera.rotate(-angle);
                    }
                }
            }
        }


    }
}