package com.berk.app;
import java.io.ByteArrayOutputStream;
import org.json.JSONException;
import org.json.JSONObject;
import android.R.raw;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//module to drop pictures
public class Camera extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.camera, menu);
        return true;
    }
    //lets the user just leave a text comment
    public void justcomment(View view){
        JSONObject output;
        output = new JSONObject();
        String a = "";
        try {
            output.put("userid", 1);
            output.put("lat", 1);
            output.put("lng", 1);
            output.put("recepients", new int[]{2});
            output.put("image", null);
            a = alert();
            output.put("comment", a);
            AsyncTask t = new MainActivity.Access();
            t.execute("comment" , "addDrop", output);
            t.get();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
    //user takes picture and leaves comment
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            final ImageView im = (ImageView) findViewById(R.id.result);
            final float iny = height/2 - 100;
            final float inx = width/2 - 100;
            im.setY(iny);
            im.setX(inx);
            im.setImageBitmap(imageBitmap);

            String encodedImage = getStringFromBitmap(imageBitmap);
            JSONObject output;
            output = new JSONObject();
            try {
                output.put("userid", 1);
                output.put("lat", 1);
                output.put("lng", 1);
                output.put("recepients", new int[]{2});
                output.put("image", encodedImage);
                output.put("comment", alert());
                AsyncTask t = new MainActivity.Access();
                t.execute("picture" , "addDrop", output);
                t.get();
            }
            catch (Exception e) { e.printStackTrace(); }
            im.setTag("imtag");
            //makes dropping picture exciting
            View v2 = findViewById(R.id.theview);
            v2.setOnDragListener(
                    new OnDragListener(){
                        public boolean onDrag(View v, DragEvent event) {
                            final int action = event.getAction();
                            if(action == DragEvent.ACTION_DROP){
                                //makes the picture fly off the screen
                                final float x = event.getX();
                                final float y = event.getY();
                                final View im2 = findViewById(R.id.result);
                                final double xadd = 50 * (x-inx) / Math.sqrt( Math.pow((x-inx), 2) + Math.pow((y-iny), 2) );
                                final double yadd = 50 * (y-iny) / Math.sqrt( Math.pow((x-inx), 2) + Math.pow((y-iny), 2) );
                                im2.setX(x);
                                im2.setY(y);
                                final MediaPlayer mediaPlayer = MediaPlayer.create(Camera.this, R.raw.ship);
                                mediaPlayer.start();
                                new android.os.CountDownTimer(5000, 10) {
                                    float currx = x;
                                    float curry = y;
                                    public void onTick(long millisUntilFinished) {
                                        currx+= xadd;
                                        curry+= yadd;
                                        im2.setX(currx);
                                        im2.setY(curry);
                                    }

                                    public void onFinish() {
                                        mediaPlayer.stop();
                                        mediaPlayer.release();
                                    }
                                }.start();
                                im2.setVisibility(View.VISIBLE);
                            }
                            return true;
                        }
                    }
            );
            //facilitates dragging
            im.setOnTouchListener(
                    new OnTouchListener(){
                        @Override
                        public boolean onTouch(View v, MotionEvent e) {
                            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                            ClipData dragData = ClipData.newPlainText(v.getTag().toString(), "text/plain");
                            v.setVisibility(View.INVISIBLE);
                            dragData.addItem(item);
                            // Instantiates the drag shadow builder.
                            View.DragShadowBuilder myShadow = new DragShadowBuilder(im);
                            v.startDrag(dragData, myShadow, null, 0 );
                            return true;
                        }
                    }
            );


        }
    }


    //converts bitmap to json to send
    private String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
    //get text from the user
    public String alert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this, 2);
        alert.setTitle("Hello");
        alert.setMessage("What would you like to say?");
        EditText inputpre = new EditText(this);
        inputpre.setTextColor(Color.WHITE);
        final EditText input = inputpre;
        alert.setView(input);
        //alert.se
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Log.d("out1", value);
            }
        });
        alert.show();
        return input.getText().toString();
    }


}
