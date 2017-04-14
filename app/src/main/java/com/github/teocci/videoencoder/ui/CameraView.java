package com.github.teocci.videoencoder.ui;

import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.github.teocci.videoencoder.interfaces.CameraReadyCallback;

import java.util.*;

public class CameraView implements SurfaceHolder.Callback
{
    public static final String TAG = CameraView.class.getSimpleName();

    private Camera camera = null;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    CameraReadyCallback cameraReadyCallback;

    private List<int[]> supportedFrameRate;
    private List<Camera.Size> supportedSizes;
    private Camera.Size currentSize;

    public CameraView(SurfaceView sv)
    {
        camera = null;
        cameraReadyCallback = null;

        surfaceView = sv;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);
    }

    public List<Camera.Size> getSupportedPreviewSize()
    {
        return supportedSizes;
    }

    public int Width()
    {
        return currentSize.width;
    }

    public int Height()
    {
        return currentSize.height;
    }

    public void setCameraReadyCallback(CameraReadyCallback cb)
    {
        cameraReadyCallback = cb;
    }

    public void StartPreview()
    {
        if (camera == null)
            return;
        camera.startPreview();
    }

    public void StopPreview()
    {
        if (camera == null)
            return;
        camera.stopPreview();
    }

    public void AutoFocus()
    {
        camera.autoFocus(afcb);
    }

    public void Release()
    {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void setupCamera(int width, int height, int bufNumber, double fps, PreviewCallback cb)
    {
        double diff = Math.abs(supportedSizes.get(0).width * supportedSizes.get(0).height - width * height);
        int targetIndex = 0;
        int index = 0;
        for (Camera.Size size : supportedSizes) {
            Log.e(TAG, "supportedSize = " + size.width + "x" + size.height);
            double newDiff = Math.abs(size.width * size.height - width * height);
            if (newDiff < diff) {
                diff = newDiff;
                targetIndex = index;
            }
            index++;
        }

        currentSize.width = supportedSizes.get(targetIndex).width;
        currentSize.height = supportedSizes.get(targetIndex).height;

        diff = Math.abs(supportedFrameRate.get(0)[0] * supportedFrameRate.get(0)[1] - fps * fps * 1000 * 1000);
        targetIndex = 0;
        index = 0;
        for (int[] frameRate : supportedFrameRate) {
            Log.e(TAG, "frameRate = " + frameRate[0] + " " + frameRate[1]);
            double newDiff = Math.abs(frameRate[0] * frameRate[1] - fps * fps * 1000 * 1000);
            if (newDiff < diff) {
                diff = newDiff;
                targetIndex = index;
            }
            index++;
        }
        int targetMaxFrameRate = supportedFrameRate.get(targetIndex)[0];
        int targetMinFrameRate = supportedFrameRate.get(targetIndex)[1];

        Camera.Parameters p = camera.getParameters();
        p.setPreviewSize(currentSize.width, currentSize.height);
        p.setPreviewFormat(ImageFormat.NV21);
        p.setPreviewFpsRange(targetMaxFrameRate, targetMinFrameRate);
        camera.setParameters(p);

        PixelFormat pixelFormat = new PixelFormat();
        PixelFormat.getPixelFormatInfo(ImageFormat.NV21, pixelFormat);
        int bufSize = currentSize.width * currentSize.height * pixelFormat.bitsPerPixel / 8;
        byte[] buffer;
        for (int i = 0; i < bufNumber; i++) {
            buffer = new byte[bufSize];
            camera.addCallbackBuffer(buffer);
        }
        camera.setPreviewCallbackWithBuffer(cb);
    }

    private void initCamera()
    {
        camera = Camera.open();
        currentSize = camera.new Size(0, 0);
        Camera.Parameters p = camera.getParameters();

        supportedFrameRate = p.getSupportedPreviewFpsRange();

        supportedSizes = p.getSupportedPreviewSizes();
        currentSize = supportedSizes.get(supportedSizes.size() / 2);
        p.setPreviewSize(currentSize.width, currentSize.height);

        camera.setParameters(p);
        //camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        camera.setPreviewCallbackWithBuffer(null);

        camera.startPreview();
    }

    private Camera.AutoFocusCallback afcb = new Camera.AutoFocusCallback()
    {
        @Override
        public void onAutoFocus(boolean success, Camera camera)
        {
        }
    };

    @Override
    public void surfaceChanged(SurfaceHolder sh, int format, int w, int h)
    {
    }

    @Override
    public void surfaceCreated(SurfaceHolder sh)
    {
        initCamera();
        if (cameraReadyCallback != null)
            cameraReadyCallback.onCameraReady();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder sh)
    {
        Release();
    }
}
