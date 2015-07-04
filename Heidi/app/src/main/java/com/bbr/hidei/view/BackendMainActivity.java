package com.bbr.hidei.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbr.hidei.heidi.R;


public class BackendMainActivity extends Activity {

    private final int REQ_CODE_GALLERY = 100;
    private final int REQ_CODE_FILE = 200;

    private Button mOpenHideFileButton;
    private Button mOpenHideImageButton;
    private Button mRequestButton;
    private TextView mOpenhideFileSrcTextView;
    private TextView mOpenhideImageSrcTextView;

    private ImageView mResultImageView;

    private String imgPath;
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backend_activity_main);

        mOpenHideFileButton     = (Button)findViewById(R.id.button_open_hide_file);
        mOpenHideImageButton    = (Button)findViewById(R.id.button_open_cover_image);
        mRequestButton      = (Button)findViewById(R.id.button_request);
        mOpenhideFileSrcTextView      = (TextView)findViewById(R.id.textview_hide_file_src);
        mOpenhideImageSrcTextView   = (TextView)findViewById(R.id.textview_cover_image_src);
        mResultImageView               = (ImageView)findViewById(R.id.imageview_result);

        initOnClickEvent();

    }

    private void initOnClickEvent()
    {
        mOpenHideFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file*//**//**//**//*");
                startActivityForResult(intent, REQ_CODE_FILE);*/
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_CODE_FILE);
            }
        });
        mOpenHideImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_CODE_GALLERY);
            }
        });
        mRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServletPostAsyncTask task = new ServletPostAsyncTask();
                task.setImageView(mResultImageView);
                task.execute(new Pair<Context, String[]>(BackendMainActivity.this, new String[]{filePath, imgPath, "mr.jack"}));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getImagePath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_GALLERY) {
            if (resultCode == RESULT_OK) {
                Log.i("ㄹㄹㄹ","갤러리 성공");
                Uri uri = data.getData(); // Received Data from intent.
                imgPath = uri.getPath();
                imgPath = getImagePath(uri);

                mOpenhideImageSrcTextView.setText(imgPath);
            }
        }
        else if(requestCode == REQ_CODE_FILE){
            if(resultCode == RESULT_OK){
                Log.i("ㄹㄹㄹ","파일 성공");
                Uri uri = data.getData();
                filePath = getImagePath(uri);
                mOpenhideFileSrcTextView.setText(filePath);
            }
        }
    }
}
