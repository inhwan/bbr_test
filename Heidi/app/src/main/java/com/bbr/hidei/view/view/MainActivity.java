package com.bbr.hidei.view.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.bbr.hidei.heidi.R;
import com.dd.CircularProgressButton;
import com.soundcloud.android.crop.Crop;

import java.io.File;

public class MainActivity extends Activity {

    private final int REQ_CODE_GALLERY = 100;
    private final int REQ_CODE_FILE = 200;

    private CircularProgressButton acceptButton;
    private CircularProgressButton subButton;
//    private ImageView resultView;

    private String imgPath;
    private Uri imgUri;

  //  WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        resultView = (ImageView) findViewById(R.id.result_image);

//        browser = (WebView) findViewById(R.id.webView);
//        browser.getSettings().getJavaScriptEnabled();
//        browser.loadUrl("file:///android_asset/map/index.html");
//        browser.getSettings().setJavaScriptEnabled(true);    // �ڹٽ�ũ��Ʈ ��� �� ����

        if( SplashActivity.ACTIVITY_INITIALIZED == false) {
            startActivity(new Intent(this, SplashActivity.class));
            SplashActivity.ACTIVITY_INITIALIZED = true;
        }
        acceptButton = (CircularProgressButton) findViewById(R.id.acceptButton);
        acceptButton.setIndeterminateProgressMode(true);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                갤러리
                 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_CODE_GALLERY);

                /*
                파일탐색기
                 */
/*                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file*//**//*");
                startActivityForResult(intent, REQ_CODE_FILE);*/
            }
        });

        subButton = (CircularProgressButton) findViewById(R.id.subButton);
        subButton.setIndeterminateProgressMode(true);
        subButton.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_GALLERY) {
            if (resultCode == RESULT_OK) {
                imgUri = data.getData(); // Received Data from intent.
                imgPath = imgUri.getPath();
                imgPath = getImagePath(imgUri);

                Log.i("테스트 imgPath::" , imgPath);

                //resultView.setImageDrawable(getDrawableFromBitmap(imgPath));



                subButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        beginCrop(imgUri);
                    }
                });
                //resultView.setBackgroundResource(R.drawable.texture);
                subButton.setText("CROP");
                subButton.setVisibility(View.VISIBLE);
                subButton.invalidate();
                acceptButton.invalidate();

/*                Canvas canvas = new Canvas();
                canvas.drawBitmap(BitmapFactory.decodeFile(imgPath), 100, 100, null);
                customViewpager.
                customViewpager.invalidate();
                customIndicator.setViewPager(customViewpager);*/
//                customIndicator.setBackground(getDrawableFromBitmap(imgPath));

                /*customViewpager.setBackground(getDrawableFromBitmap(imgPath));
                customIndicator.setViewPager(customViewpager);*/

                Handler hd = new Handler();
                hd.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        acceptButton.setCompleteText("NEXT");
                        acceptButton.setProgress(100);
                        acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                acceptButton.setProgress(0);
                                Intent intent = new Intent();
                                intent.setAction("com.sec.android.app.myfiles.PICK_DATA");
                                intent.putExtra("CONTENT_TYPE", "*/*");
                                startActivityForResult(intent, REQ_CODE_FILE);       // 2초짜리

                                if (acceptButton.getProgress() == 0) {
                                    acceptButton.setProgress(50);
                                } else if (acceptButton.getProgress() == 100) {
                                    acceptButton.setProgress(0);
                                } else {
                                    acceptButton.setProgress(99);
                                }
                            }
                        });
                    }
                }, 2000);
            }
        }
        else if(requestCode == REQ_CODE_FILE){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();

                String filePath = uri.getPath();

                Log.i("테스트 filePath::" , filePath);

                Handler hd = new Handler();
                hd.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        acceptButton.setCompleteText("SUCCESS");
                        acceptButton.setProgress(100);       // 2초짜리

                        acceptButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                            }
                        });
                    }
                }, 2000);
            }
        }
        else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        /*else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void selected() {
//            resultView.setImageDrawable(null);
        //Crop.pickImage(this);
    }

    private Drawable getDrawableFromBitmap(String filePath){
        Drawable d = new BitmapDrawable(getResources(), filePath);

        return d;
    }

    private String getImagePath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
//            resultView.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
