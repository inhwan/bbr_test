package com.bbr.hidei.engine;

/**
 * Created by laewoongJang on 2015-04-21.
 */
public class EncryptionDecorator extends HideiDecorator {
    private final String password;

    public EncryptionDecorator(HideiData hideiData)
    {
        this(hideiData, "");
    }

    public EncryptionDecorator(HideiData hideiData, String password)
    {
        super(hideiData);
        this.password = password;
        this.hideiData.getHidenList().add("Encryption");
    }

    @Override
    public byte[] getData() {
        return encryption(this.hideiData.getData());
    }

    private byte[] encryption(byte[] data)
    {
        return data;
    }

}
