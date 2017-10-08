package wall.example.demo.mywallpaper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by sev_user on 04-Dec-14.
 */
public class MyPaint extends WallpaperService implements MainActivity.OnChangeColor {

    MyEngine myEngine = new MyEngine();
    Context mContext;

    /**
     * Must be implemented to return a new instance of the wallpaper's engine.
     * Note that multiple instances may be active at the same time, such as
     * when the wallpaper is currently set as the active wallpaper and the user
     * is in the wallpaper picker viewing a preview of it as well.
     */
    @Override
    public Engine onCreateEngine() {

        mContext = this;
        MainActivity mainActivity;
        if (mContext instanceof MainActivity) {

            mainActivity = (MainActivity) mContext;
            mainActivity.setChangeColorListener(myEngine);
        }

        return myEngine;
    }

    @Override
    public void changeColor(int color) {

    }

    private class MyEngine extends Engine implements MainActivity.OnChangeColor {

        Paint mPaint;
        Path mPath;
        SurfaceHolder mSurfaceHolder;

        public MyEngine() {

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeWidth(10);

            mPath = new Path();

            mSurfaceHolder = getSurfaceHolder();
        }

        /**
         * Called as the user performs touch-screen interaction with the
         * window that is currently showing this wallpaper.  Note that the
         * events you receive here are driven by the actual application the
         * user is interacting with, so if it is slow you will get fewer
         * move events.
         *
         * @param event
         */
        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            drawing(event);

//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                mPath = new Path();
//                mPath.moveTo(event.getX(), event.getY());
//            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//                mPath.lineTo(event.getX(), event.getY());
//            } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                mPath.lineTo(event.getX(), event.getY());
//            }
//
//            if (mPath != null) {
//                Canvas canvas = mSurfaceHolder.lockCanvas();
//                canvas.drawPath(mPath, mPaint);
//                mSurfaceHolder.unlockCanvasAndPost(canvas);
//            }
        }

        void drawing(MotionEvent event) {

            Canvas canvas = null;

            try {

                canvas = mSurfaceHolder.lockCanvas();
                if (canvas != null) {

                    //canvas.drawColor(Color.BLACK);
                    touching(event, canvas);
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        void touching(MotionEvent event, Canvas canvas) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    mPath.moveTo(x, y);
                    break;

                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;

                case MotionEvent.ACTION_UP:
                    mPath.lineTo(x, y);
                    break;

                default:
                    break;
            }

            canvas.drawPath(mPath, mPaint);
            //mPath.reset();
        }

        @Override
        public void changeColor(int color) {
            mPaint.setColor(color);
        }
    }
}
