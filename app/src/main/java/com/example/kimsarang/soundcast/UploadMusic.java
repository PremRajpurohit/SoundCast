package com.example.kimsarang.soundcast;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseFileUtils;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class UploadMusic extends AppCompatActivity {

    EditText mTitle;
    TextView mMusicName,mThumbName;
    ImageView mThumb;
    Uri mUri,tUri;
    File musicFile,thumbFile;

    String musicFileName,thumbFileName;

    private static final int M_CODE = 100;
    private static final int T_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_music);

        mTitle = findViewById(R.id.mTitle);
        mThumb = findViewById(R.id.mThumb);
        mMusicName = findViewById(R.id.mMusicName);
        mThumbName = findViewById(R.id.mThumbName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {


            switch (requestCode)
            {
                case M_CODE:
                    mUri = data.getData();
                    musicFile = new File(mUri.getPath());
                    musicFileName = musicFile.getName();
                    mMusicName.setText(musicFileName);
                    break;
                case T_CODE:
                    tUri = data.getData();
                    thumbFile = new File(tUri.getPath());
                    thumbFileName = thumbFile.getName();
                    mThumbName.setText(thumbFileName);
                    try {
                        mThumb.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(tUri)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void onMusicUpload(View view) {
        openFileChooser(M_CODE);
    }

    public void onThumbUpload(View view) {
        openFileChooser(T_CODE);
    }

    public void openFileChooser(int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"), code);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void onFinish(View view) throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseObject entity = new ParseObject("songs_library");

                ParseFile mFile = new ParseFile(String.valueOf(Calendar.getInstance().getTime().getSeconds()) + ".mp3",getFileBytes(mUri));
                ParseFile tFile = new ParseFile(String.valueOf(Calendar.getInstance().getTime().getSeconds()) + ".jpeg",getFileBytes(tUri));

                entity.put("title", mTitle.getText().toString());
                entity.put("link", saveFile(mFile));
                entity.put("thumbnail", saveFile(tFile));
                entity.put("music_file", mFile);
                entity.put("thumbnail_file", tFile);

                // Saves the new object.
                // Notice that the SaveCallback is totally optional!
                try {
                    entity.save();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadMusic.this, "Uploaded",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public byte[] getFileBytes(Uri uri) {
        byte[] buffer = new byte[1024000];
        try {
            InputStream stream = getContentResolver().openInputStream(uri);
            stream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  buffer;
    }

    public String saveFile(ParseFile parseFile) {
        try {
            parseFile.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parseFile.getUrl();
    }
}
