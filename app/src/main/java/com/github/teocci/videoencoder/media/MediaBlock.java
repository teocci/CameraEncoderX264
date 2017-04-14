package com.github.teocci.videoencoder.media;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017/Apr/14
 */

public class MediaBlock
{
    public int videoCount = 0;

    public int flag = 0;

    private byte[] buffer;
    private int bufferLength;
    private int currentLength;

    public MediaBlock(int maxSize)
    {
        super();
        buffer = new byte[maxSize];
        bufferLength = maxSize;
        currentLength = 0;
    }

    public void reset()
    {
        synchronized (this) {
            currentLength = 0;
            videoCount = 0;
            flag = 0;
        }
    }

    public int length()
    {
        return currentLength;
    }

    public byte[] data()
    {
        return buffer;
    }

    public int writeVideo(byte[] data, int length)
    {
        if (currentLength + length >= bufferLength) {
            return 0;
        }

        for (int i = 0; i < length; i++) {
            buffer[currentLength] = data[i];
            currentLength++;
        }
        videoCount++;
        return length;
    }

    public int write(byte[] data, int length)
    {
        if (currentLength + length >= bufferLength) {
            return 0;
        }

        for (int i = 0; i < length; i++) {
            buffer[currentLength] = data[i];
            currentLength++;
        }

        return length;
    }
}
