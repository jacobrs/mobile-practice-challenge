package com.csgames.diabetus;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.openalpr.OpenALPR;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    public Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ocr();

        Firebase.setAndroidContext(this);

        myFirebaseRef = new Firebase("https://resplendent-inferno-4136.firebaseio.com/m_numbers");

        String number = "123456";

        login(number);

    }

    public void ocr(){
        Bitmap finalBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.test);

        String file = "";
        try {
            file = saveToInternalStorage(finalBitmap);
        }catch(Exception e){
            e.printStackTrace();
        }

        final String ANDROID_DATA_DIR = this.getApplicationInfo().dataDir;
        final String openAlprConfFile = ANDROID_DATA_DIR +
                File.separatorChar + "runtime_data" + File.separatorChar + "openalpr.conf";

        String image = "android.resource://com.csgames.diabetus/" + R.drawable.test;

        String result = OpenALPR.Factory.create(LoginActivity.this, ANDROID_DATA_DIR).
                recognizeWithCountryRegionNConfig("us", "", file, openAlprConfFile, 10);

        Log.i("test", result);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }

    private void login(final String m_number){
        myFirebaseRef.child(m_number).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
                if(snapshot.getValue() == null){
                    loginFailed();
                }else{
                    Globals.m_number = m_number;
                    Globals.name = snapshot.getValue().toString();
                    proceedToMenu();
                }
            }
            @Override public void onCancelled(FirebaseError error) { }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void proceedToMenu(){
        ((TextView) findViewById(R.id.login_status)).setText(String.format(
                getString(R.string.valid_login), Globals.name));

        Intent next = new Intent(this, MenuActivity.class);
        startActivity(next);
    }

    private void loginFailed(){
        ((TextView) findViewById(R.id.login_status)).setText(getString(R.string.invalid_login));
    }
}
