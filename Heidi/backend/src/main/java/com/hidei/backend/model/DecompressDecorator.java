package com.hidei.backend.model;

/**
 * Created by laewoongJang on 2015-04-21.
 */
public class DecompressDecorator extends HideiDecorator {

    private final int rate;

    public DecompressDecorator(HideiData hideiData)
    {
        this(hideiData, 0);
    }

    public DecompressDecorator(HideiData hideiData, int rate)
    {
        super(hideiData);
        this.rate = rate;
        this.hideiData.getHidenList().add("Decompress");
    }

    @Override
    public byte[] getData() {
        return decompress(this.hideiData.getData());
    }

    private byte[] decompress(byte[] data)
    {
        return data;
    }
}
