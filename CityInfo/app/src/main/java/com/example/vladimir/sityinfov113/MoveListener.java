package com.example.vladimir.sityinfov113;

import android.graphics.PointF;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Vladimir on 28.08.2017.
 */

public class MoveListener extends GestureDetector.SimpleOnGestureListener{
    private static final int FIRST_POINTER_COMMON = 0;
    private static final int SECOND_POINTER_COMMON = 1;
    private static final int TOTAL_POINTERS = 2;

    private Camera camera;
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

    public MoveListener(Camera camera){
        for(int i=0; i < TOTAL_POINTERS; i++){
            last_coords[i] = new PointerState();
            last_coords[i].index = i;
        }
        this.camera = camera;
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
    public boolean onDoubleTap(MotionEvent event) {
        Vector3f unp1 = camera.unprojectPlane(event.getX(FIRST_POINTER_COMMON), event.getY(FIRST_POINTER_COMMON));
        final float ratio = 0.25f;
        camera.zoom(ratio);
        Vector3f unp1a = camera.unprojectPlane(event.getX(FIRST_POINTER_COMMON), event.getY(FIRST_POINTER_COMMON));
        Vector3f dif = unp1.subed(unp1a);
        camera.translate(dif.x(), dif.y());

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
                    Vector3f dif = fp.world.subed(camera.unprojectPlane(x, y));
                    camera.translate(dif.x(), dif.y());
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
                    rotation_helper.scale(motionEvent1);
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
        last_coords[index].world = camera.unprojectPlane(event.getX(index),event.getY(index));
        last_coords[index].need_update = false;
    }

    private void updateLastCoords(int index, MotionEvent event){
        updateLastScreenPosition(index, event);
        updateLastWorldPosition(index, event);
    }

    private class RotationHelper{
        private PointerState rotation_point;
        private PointerState rotation_stable_point;

        private PointerState last_pointers_copy[] = new PointerState[TOTAL_POINTERS];

        public void reset(){
            rotation_stable_point = null;
        }

        public void startRotation(PointerState fs, PointerState ss){
            last_pointers_copy[FIRST_POINTER_COMMON] = new PointerState(fs);
            last_pointers_copy[SECOND_POINTER_COMMON] = new PointerState(ss);
        }

        public void scale(MotionEvent event){
            PointerState fp = last_coords[FIRST_POINTER_COMMON];
            PointerState sp = last_coords[SECOND_POINTER_COMMON];
            if(last_pointers_copy[FIRST_POINTER_COMMON] == fp){
                Vector3f unp1 = camera.unprojectPlane(event.getX(FIRST_POINTER_COMMON), event.getY(FIRST_POINTER_COMMON));
                Vector3f unp2 = camera.unprojectPlane(event.getX(SECOND_POINTER_COMMON), event.getY(SECOND_POINTER_COMMON));
                double new_distance = (double)unp2.subed(unp1).toPointF().length();
                double old_distance = (double)fp.world.subed(sp.world).toPointF().length();

                Vector3f old_center = fp.world.added(sp.world).dived(2f);

                float ratio = (float)(old_distance / new_distance);
                camera.zoom(ratio);
                Vector3f unp1a = camera.unprojectPlane(event.getX(FIRST_POINTER_COMMON), event.getY(FIRST_POINTER_COMMON));
                Vector3f unp2a = camera.unprojectPlane(event.getX(SECOND_POINTER_COMMON), event.getY(SECOND_POINTER_COMMON));
                Vector3f new_center2 = unp1a.added(unp2a).dived(2f);
                Vector3f dif = old_center.subed(new_center2);
                camera.translate(dif.x(), dif.y());
            }
            else
                Log.w("W", "scale ignored!");
        }

        public void rotate(MotionEvent event) {
            final int ROTATE_PER_0 = 0x1;
            final int ROTATE_PER_1 = 0x2;
            final int ROTATE_BOTH = ROTATE_PER_0 | ROTATE_PER_1;

            PointerState fp = last_coords[FIRST_POINTER_COMMON];
            PointerState sp = last_coords[SECOND_POINTER_COMMON];
            if (!sp.need_update) {
                PointF dif[] = new PointF[]{getScreenPositionOffset(FIRST_POINTER_COMMON, event, last_pointers_copy), getScreenPositionOffset(SECOND_POINTER_COMMON, event, last_pointers_copy)};
                int rotation_mask = 0;
                float whs = camera.height + camera.width;
                rotation_mask |= ((dif[SECOND_POINTER_COMMON].length() / whs) > 0.01) ? ROTATE_PER_1 : 0;
                rotation_mask |= ((dif[FIRST_POINTER_COMMON].length() / whs) > 0.01) ? ROTATE_PER_0 : 0;

                Log.e("W", "difs " + (dif[SECOND_POINTER_COMMON].length() / whs) + " " + (dif[FIRST_POINTER_COMMON].length() / whs));

                switch (rotation_mask) {
                    case ROTATE_PER_0:
                        if (rotation_stable_point != sp) {
                            rotation_stable_point = sp;
                            rotation_point = fp;
                        }
                        break;
                    case ROTATE_PER_1:
                    case ROTATE_BOTH:
                        if (rotation_stable_point != fp) {
                            rotation_stable_point = fp;
                            rotation_point = sp;
                        }
                        break;
                    default: Log.w("W", "default called!");
                        break;
                }

                if (rotation_stable_point != null) {
                    last_pointers_copy[FIRST_POINTER_COMMON] = last_coords[FIRST_POINTER_COMMON];
                    last_pointers_copy[SECOND_POINTER_COMMON] = last_coords[SECOND_POINTER_COMMON];
                    camera.setRotationPoint(rotation_stable_point.world);
                    int rindex = rotation_point.index;
                    int rsindex = rotation_stable_point.index;
                    Vector3f rsPoint = last_coords[rsindex].world;
                    Vector3f now = camera.unprojectPlane(event.getX(rindex), event.getY(rindex));
                    Vector3f before = last_coords[rindex].world;
                    float angle = angleBtwLines(
                            now.x(), now.y(), rsPoint.x(), rsPoint.y(),
                            before.x(), before.y(), rsPoint.x(), rsPoint.y()
                    );
                    camera.rotate(-angle);
                }
            }
        }
    }


}