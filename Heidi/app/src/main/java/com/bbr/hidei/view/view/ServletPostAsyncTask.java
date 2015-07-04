package com.bbr.hidei.view.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import shared.HideiDTO;


/**
 * Created by laewoongJang on 2015-05-16.
 */
class ServletPostAsyncTask extends AsyncTask<Pair<Context, String[]>, Void, byte[]> {
    private Context context;
    private Activity mActivity;

    private String res;
    @Override
    protected byte[] doInBackground(Pair<Context, String[]>... params) {
        context = params[0].first;
        mActivity = (Activity)context;
        final String[] files = params[0].second;
        final String hideFile = files[0];
        final String coverImage = files[1];
        final String password = files[2];

        int i=0;

        Log.i("fff", "hideFile = " + hideFile);
        Log.i("fff", "coverImage = " + coverImage);
        Log.i("fff", "password = " + password);

        byte[] hideFileByte = readFile(hideFile);
        byte[] coverFileByte = readFile(coverImage);

        final String SERVLET_URL = "http://hidei-968.appspot.com/hello";
        HideiDTO receive = null;

        InputStream in = null;
        ObjectInputStream oin = null;
        ObjectOutputStream oout = null;

        HideiDTO info = new HideiDTO();
        info.setOriginalData(hideFileByte);
        info.setCoverData(coverFileByte);
        info.setPassword("hello, Hide Image~!");

        try{
            URL url = new URL(SERVLET_URL);
            URLConnection conn = url.openConnection(); // 서버의 url로부터 연결 생성

            HttpURLConnection httpConn = (HttpURLConnection)conn; // url 연결로부터 http 연결 생성
            //httpConn.setRequestMethod("POST"); // request 메소드를 결정해준다.  위 서버의 서블릿에서 doPost에 작성하였기에 여기서도 POST.
            httpConn.setDoInput(true);   // http 커넥션 설정. input output 할것인지.
            httpConn.setDoOutput(true);
            //httpConn.connect(); // 연결.

            oout = new ObjectOutputStream(httpConn.getOutputStream());
            oout.writeObject(info);
            oout.close();

            Log.d("WEB","Client: Sent Done ");

            // 서버가 제대로 응답을 했을 경우에 200 이 넘어온다. HTTP_OK = 200
            int responseCode = httpConn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                Log.d("WEB","Response HTTP OK : " + responseCode);

                ObjectInputStream oin2 = new ObjectInputStream(httpConn.getInputStream());

                try {
                    receive = (HideiDTO) oin2.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                oin2.close();
                Log.i("fff", "서버로 응답온 비밀번호 : " + receive.getPassword());
            }

        } catch (Exception ex){

            Log.e("ERR", "message : " + ex.getMessage());
            ex.printStackTrace();
            Log.e("ERR", "Connecting error", ex);
        }
        finally {

        }

        return receive.getHideiData();
    }
    private ImageView mImageView;
    public void setImageView(ImageView view)
    {
        mImageView = view;
    }

    @Override
    protected void onPostExecute(byte[] result) {

        if(result == null)
        {
            Toast.makeText(context, "resut : 널이 왔어!!" + res, Toast.LENGTH_LONG).show();
        }
        else {

            byte[] decodedResult = result;

            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedResult, 0, decodedResult.length);
            final Drawable d = new BitmapDrawable(context.getResources(), bitmap);

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageDrawable(d);
                }
            });

            Toast.makeText(context, "resut : " + result.length + " // decoded result : " + decodedResult.length, Toast.LENGTH_LONG).show();
        }

    }

    private byte[] readFile(final String path) {
        File file = new File(path);

        int size = (int) file.length();

        byte[] bytes = new byte[size];

        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bytes;
    }

    private final static byte[] pngSignature = { (byte) 137, (byte) 80, (byte) 78, (byte) 71, (byte) 13, (byte) 10, (byte) 26, (byte) 10 };

    private boolean isPngFile(byte[] bytes)
    {
        boolean result = true;

        for (int i = 0; i < pngSignature.length; i++) {

            if (bytes[i] != pngSignature[i]) {
                result = false;
                break;
            }
        }

        return result;
    }

    private void saveSecureImage(byte[] secureImage, String secureImageName) {

        File f = new File(Environment.getExternalStorageDirectory().toString() + "/" + secureImageName + ".png");

        try {
            f.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } // your mistake was at here

        try
        {
            FileOutputStream fos = new FileOutputStream(f);

            //write your byteArray here
            fos.write(secureImage);
            fos.flush();
            fos.close();

        }catch (IOException e){

            e.printStackTrace();
        }
    }
}