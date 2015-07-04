package com.bbr.hidei.engine;

/**
 * Created by laewoongJang on 2015-04-21.
 */
public class CompressDecorator extends HideiDecorator {

    private final int rate;

    public CompressDecorator(HideiData hideiData)
    {
        this(hideiData, 0);
    }

    public CompressDecorator(HideiData hideiData, int rate)
    {
        super(hideiData);
        this.rate = rate;
        this.hideiData.getHidenList().add("Compress");
    }

    @Override
    public byte[] getData() {
        return compress(hideiData.getData());
    }

    private byte[] compress(byte[] data)
    {
        return data;
    }


}
