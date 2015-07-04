package com.bbr.hidei.engine;

/**
 * Created by laewoongJang on 2015-04-21.
 */
public class DecryptionDecorator extends HideiDecorator {

    private final String password;

    public DecryptionDecorator(HideiData hideiData)
    {
        this(hideiData, "");
    }

    public DecryptionDecorator(HideiData hideiData, String password)
    {
        super(hideiData);
        this.password = password;
        this.hideiData.getHidenList().add("Decryption");
    }

    @Override
    public byte[] getData() {
        return decryption(this.hideiData.getData());
    }

    private byte[] decryption(byte[] data)
    {
        return data;
    }
}
