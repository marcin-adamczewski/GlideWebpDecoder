package com.bumptech.glide.samples.webp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.integration.webp.decoder.WebpDrawable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SeekActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek);

        final ImageView image = (ImageView) findViewById(R.id.seekable_image);
        loadImage(image, "https://www.gstatic.com/webp/animated/1.webp");

        final ExecutorService executorService = Executors.newCachedThreadPool();

        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WebpDrawable) image.getDrawable()).start();
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WebpDrawable) image.getDrawable()).stop();
            }
        });

        ((SeekBar) findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                WebpDrawable drawable = (WebpDrawable) image.getDrawable();
                float index = progress / 100f * drawable.getFrameCount();
                drawable.seekTo(Math.round(index));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ((SeekBar) findViewById(R.id.seek_bar_blocking)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                final WebpDrawable drawable = (WebpDrawable) image.getDrawable();
                final float index = progress / 100f * drawable.getFrameCount();
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        drawable.seekToBlocking(Math.round(index));
                    }
                });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void loadImage(ImageView imageView, String url) {
        GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(imageView);
    }

}
