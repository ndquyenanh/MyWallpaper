package wall.example.demo.mywallpaper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by sev_user on 04-Dec-14.
 */
public class MyService extends WallpaperService {

    int x, y;

    /**
     * Must be implemented to return a new instance of the wallpaper's engine.
     * Note that multiple instances may be active at the same time, such as
     * when the wallpaper is currently set as the active wallpaper and the user
     * is in the wallpaper picker viewing a preview of it as well.
     */
    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    private class MyEngine extends Engine {

        SurfaceHolder mSurfaceHolder;
        Paint mPaint;

        // fish and sea
        boolean mVisible = true;
        Bitmap fishBitmap, seaBitmap;

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private MyEngine() {
            this.mSurfaceHolder = getSurfaceHolder();

            mPaint = new Paint();
            mPaint.setStrokeWidth(20);
            mPaint.setColor(Color.MAGENTA);

            // fish and sea
            fishBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fish);
            seaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sea);
            x = -130;
            y = 200;
        }

        /**
         * Called to inform you of the wallpaper's offsets changing
         * within its contain, corresponding to the container's
         * call to {@link WallpaperManager#setWallpaperOffsets(IBinder, float, float)
         * WallpaperManager.setWallpaperOffsets()}.
         *
         * @param xOffset
         * @param yOffset
         * @param xOffsetStep
         * @param yOffsetStep
         * @param xPixelOffset
         * @param yPixelOffset
         */
        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            draw();
        }

        void draw() {
            Canvas canvas = null;

            try {

                canvas = mSurfaceHolder.lockCanvas();

                if (canvas != null) {

                    canvas.drawColor(Color.BLACK);
                    canvas.drawBitmap(seaBitmap, 0, 0, null);
                    canvas.drawBitmap(fishBitmap, x, y, null);

                    int width = canvas.getWidth();
                    if (x > width + 100) {
                        x = -130;
                    }

                    x++;
                }
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            handler.removeCallbacks(runnable);
            if (mVisible) {
                handler.postDelayed(runnable, 10);
            }
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

            yourTouch(event);
        }

        void yourTouch(MotionEvent event) {
            Canvas canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas();

               // canvas.drawColor(Color.WHITE);

                canvas.drawRect(event.getX(), event.getY(), event.getX() + 20, event.getY() + 50, mPaint);
            } finally {
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }

        /**
         * Convenience for {@link android.view.SurfaceHolder.Callback#surfaceCreated
         * SurfaceHolder.Callback.surfaceCreated()}.
         *
         * @param holder
         */
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        /**
         * Convenience for {@link android.view.SurfaceHolder.Callback#surfaceChanged
         * SurfaceHolder.Callback.surfaceChanged()}.
         *
         * @param holder
         * @param format
         * @param width
         * @param height
         */
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        /**
         * Convenience for {@link android.view.SurfaceHolder.Callback#surfaceDestroyed
         * SurfaceHolder.Callback.surfaceDestroyed()}.
         *
         * @param holder
         */
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);

            mVisible = false;
            handler.removeCallbacks(runnable);
        }

        /**
         * Called to inform you of the wallpaper becoming visible or
         * hidden.  <em>It is very important that a wallpaper only use
         * CPU while it is visible.</em>.
         *
         * @param visible
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (mVisible) {
                handler.post(runnable);
            } else {
                handler.removeCallbacks(runnable);
            }
        }
    }
}
