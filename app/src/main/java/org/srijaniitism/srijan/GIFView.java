package org.srijaniitism.srijan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;

public class GIFView extends View {
    private Movie movie;
    private long moviestart;
    public GIFView(Context context) throws IOException {
        super(context);
        movie=Movie.decodeStream(getResources().getAssets().open("preloader.gif"));
    }
    public GIFView(Context context, AttributeSet attrs) throws IOException{
        super(context, attrs);
        movie=Movie.decodeStream(getResources().getAssets().open("preloader.gif"));
    }
    public GIFView(Context context, AttributeSet attrs, int defStyle) throws IOException {
        super(context, attrs, defStyle);
        movie=Movie.decodeStream(getResources().getAssets().open("preloader.gif"));
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long now=android.os.SystemClock.uptimeMillis();
        Paint p = new Paint();
        p.setAntiAlias(true);
        canvas.drawColor(0xFF161519);
        if (moviestart == 0)
            moviestart = now;
        int relTime;
        relTime = (int)((now - moviestart) % movie.duration());
        movie.setTime(relTime);
        movie.draw(canvas, getWidth()/2 - movie.width()/2,
                getHeight()/2 - movie.height()/2);
        this.invalidate();
    }
}
