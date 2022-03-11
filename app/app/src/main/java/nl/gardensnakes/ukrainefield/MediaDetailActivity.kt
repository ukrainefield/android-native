package nl.gardensnakes.ukrainefield

import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.github.piasy.biv.view.BigImageView
import com.google.firebase.analytics.FirebaseAnalytics
import nl.gardensnakes.ukrainefield.data.remote.HttpRoutes
import nl.gardensnakes.ukrainefield.helper.PreferenceHelper

class MediaDetailActivity : AppCompatActivity() {
    private lateinit var mediaURL: String
    private lateinit var mediaType: String

    private lateinit var imageView: BigImageView
    private lateinit var videoView: VideoView

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        BigImageViewer.initialize(GlideImageLoader.with(this));
        setContentView(R.layout.activity_media_detail)
        BigImageViewer.initialize(GlideImageLoader.with(this));
        mediaURL = intent.getStringExtra("MEDIA_URL").toString()
        mediaType = intent.getStringExtra("MEDIA_TYPE").toString()

        imageView = findViewById(R.id.media_detail_image)
        videoView = findViewById(R.id.media_detail_video)


        if (mediaType == "image") {
            imageView.showImage(Uri.parse(mediaURL))
            imageView.clipToOutline = true
        } else {
            imageView.visibility = View.GONE
            videoView.visibility = View.VISIBLE
            videoView.setVideoPath(mediaURL)
            val mediaController = MediaController(this)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.seekTo(30)
            if (PreferenceHelper.shouldAutoPlayVideos(this)) {
                videoView.start()
            }
        }
    }
}