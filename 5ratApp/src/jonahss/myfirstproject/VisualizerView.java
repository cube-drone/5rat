package jonahss.myfirstproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

/**
 * A simple class that draws waveform data received from a
 */
class VisualizerView extends View {
  private byte[] mBytes;
  private float[] mPoints;
  private Rect mRect = new Rect();

  private Paint mForePaint = new Paint();

  public VisualizerView(Context context) {
    super(context);
    init();
  }

  private void init() {
    mBytes = null;

    mForePaint.setStrokeWidth(15f);
    mForePaint.setAntiAlias(true);
    mForePaint.setColor(Color.rgb(255, 0, 0));
  }

  public void updateVisualizer(byte[] bytes) {
    mBytes = bytes;
    Log.d("updateVisualizer", Integer.toString(bytes.length));
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    Log.d("canvas ondraw", Boolean.toString(mBytes == null));

    if (mBytes == null) {
      return;
    }

    Log.d("canvas ondraw", Integer.toString(mBytes.length));

    if (mPoints == null || mPoints.length < mBytes.length * 4) {
      mPoints = new float[mBytes.length * 4];
    }

    mRect.set(0, 0, getWidth(), getHeight());

    for (int i = 0; i < mBytes.length - 1; i++) {
      mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
      mPoints[i * 4 + 1] = mRect.height() / 2
              + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
      mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
      mPoints[i * 4 + 3] = mRect.height() / 2
              + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2) / 128;
    }

    Log.d("canvas ondraw", Float.toString(mBytes[0]));
    canvas.drawLines(mPoints, mForePaint);
  }
}
