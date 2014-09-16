package com.examples.ffmpeg4android_demo;



import com.examples.ffmpeg4android_demo.util.AndroidUtil;
import com.examples.ffmpeg4android_demo.util.Constants;
import com.netcompss.ffmpeg4android_client.BaseWizard;
import com.netcompss.ffmpeg4android_client.Prefs;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

// documentation -->http://androidwarzone.blogspot.com.es/2011/12/ffmpeg4android.html
public class DemoClient extends BaseWizard {

    private static final int ACTION_TAKE_VIDEO = 3;
    private static final String VIDEO_STORAGE_KEY = "viewvideo";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
    private VideoView mVideoView;
    private Uri mVideoUri;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private String videoPath;
    private String videoPathDest;
    private String fileName;

	Button ffmepg;
    Constants constants;
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
        setWorkingFolder(constants.URLOUT);
        copyLicenseAndDemoFilesFromAssetsToSDIfNeeded();
        setContentView(R.layout.ffmpeg_demo_client);
        findView();
        action_ffmepg();

        mVideoView = (VideoView) findViewById(R.id.videoView1);
        mVideoUri = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

	}


    private void findView() {
        ffmepg =  (Button)findViewById(R.id.bkuekiwi);
    }
    private void handleCameraVideo(Intent intent) {
        // documentation -->http://androidwarzone.blogspot.com.es/2011/12/ffmpeg4android.html
        //-y --> Overwrite output files without asking.
        //-i -->filename (input)
        //-ac-->Set the number of audio channels.
        //-ar -->Set the audio sampling frequency.
        //-r-->To force the frame rate of the output file to 24 fps:
        //160x120--> resolution

        String commandStr ="ffmpeg -y -i "+ videoPath +" -strict experimental -r 25 -aspect 4:3 -vcodec mpeg4 -b 2097152 -ab 48000 -ac 2 -ar 22050 "+ constants.URLOUT +""+ fileName;
        setFFmpeg(commandStr);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_VIDEO: {
                if (resultCode == RESULT_OK) {
                    handleCameraVideo(data);
                }
                break;
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
        outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null) );
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.setVisibility(
                savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
                        ImageView.VISIBLE : ImageView.INVISIBLE
        );
    }


    private void dispatchTakeVideoIntent() {
        try{
            Intent takeVideoIntent=null;
            File storagePath;

            storagePath = AndroidUtil.getStoragePath();

            if (storagePath != null) {
                 takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                videoPath = storagePath.getAbsolutePath().concat(File.separator)
                        .concat("bluekiwi").concat(File.separator);
                videoPathDest= videoPath;

                File bkDir = new File(videoPath);
                if (!bkDir.exists()) {
                    bkDir.mkdir();
                }

                SimpleDateFormat sdf = new SimpleDateFormat("ddMMMyyyyHHmmss");
                fileName ="bk_".concat(sdf.format(new Date(System.currentTimeMillis()))).concat(".mp4");

                videoPath = videoPath.concat(fileName);

                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(videoPath)));

            }
            startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);

        }catch(Exception ex){
            Log.d("ERROR_","error " + ex.toString());
        }

    }


    private void action_ffmepg() {
        ffmepg.setOnClickListener(new OnClickListener() {
            public void onClick(View v){

            dispatchTakeVideoIntent();
            }
        });
    }



    private void setFFmpeg(String commandStr){
        setCommand(commandStr);

        ///optional////
        //setOutputFilePath("/sdcard/videokit/out.mp4");
        setProgressDialogTitle("Exporting As MP4 Video");
        setProgressDialogMessage("Depends on your video size, it can take a few minutes");

        setNotificationIcon(R.drawable.icon2);
        setNotificationMessage(constants.NOTIFICATION_MESSAGE);
        setNotificationTitle(constants.NOTIFICATION_TITLE);
        setNotificationfinishedMessageTitle(constants.NOTIFICATION_FINISHED_MESSAGE_TITLE);
        setNotificationfinishedMessageDesc(constants.NOTIFICATION_FINISHED_MESSAGE_DESC);
        setNotificationStoppedMessage(constants.NOTIFICATION_STOPPED_MESSAGE);
        ///////////////

        Log.d(Prefs.TAG, "ffmpeg4android library version: " + Prefs.getLibraryVersionName());
        runTranscoing();
   }





}
