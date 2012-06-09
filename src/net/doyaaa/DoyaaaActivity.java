package net.doyaaa;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;


public class DoyaaaActivity extends Activity {

  private static final String TAG = "DOYAA";

  private static final String FILE_NAME = "Doyaa.jpg";
  private static final int CAMERA_RESULT = 1000;

  private static final int MAX_SIZE = 640;

  Uri mImageUri = null;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    onExtraClick();

  }

  public void onExtraClick() {

    Intent intent = new Intent();
    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
    File mTmpFile = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
    startActivityForResult(intent, CAMERA_RESULT);

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == CAMERA_RESULT) {
      Log.d(TAG, "onresult");

      File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);

      BitmapFactory.Options option = new BitmapFactory.Options();

      //サイズだけロードするように設定して
      option.inJustDecodeBounds = true;

      //サイズ情報だけロード
      BitmapFactory.decodeFile(file.getAbsolutePath(), option);

      int w = option.outWidth;
      int h = option.outHeight;

      //大きい辺に合わせて読み込みスケールを調整
      if( MAX_SIZE < Math.max(w,h)){
        int scale = Math.max(w,h)/MAX_SIZE + 1;
        option.inSampleSize = scale;
      }

      //今度こそ読み込む
      option.inJustDecodeBounds = false;
      Bitmap pic = BitmapFactory.decodeFile(file.getAbsolutePath(), option);

      Log.d(TAG, "pic:" + pic.getWidth() + ":" + pic.getHeight() );

      Bitmap doyaadBitmap = makeDoyaa(pic);

      ImageView iv = ((ImageView) findViewById(R.id.imageView1) );
      iv.setImageDrawable(new BitmapDrawable(doyaadBitmap));


    }

  }

  private final Bitmap makeDoyaa(Bitmap baseBitmap){

    //TODO bitmapをランダムに読み込む
    Bitmap overlayBitmap =
      BitmapFactory.decodeResource(getResources(), R.drawable.sample);

    baseBitmap = baseBitmap.copy(Bitmap.Config.ARGB_8888, true);

    Log.d(TAG, "ol:" + overlayBitmap.getWidth() + ":" + overlayBitmap.getHeight() );

    Paint paint = new Paint();
    paint.setAntiAlias(true);
    Canvas c = new Canvas(baseBitmap);

    c.drawBitmap(
        overlayBitmap,
        new Rect(0, 0, overlayBitmap.getWidth(), overlayBitmap.getHeight() ),
        new Rect(0, 0, baseBitmap.getWidth(), baseBitmap.getHeight() ),
        paint
    );

    return baseBitmap;
  }


}