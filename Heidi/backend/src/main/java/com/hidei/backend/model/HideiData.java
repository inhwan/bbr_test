package com.hidei.backend.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laewoongJang on 2015-04-21.
 */
public class HideiData {
    private byte[] data;
    private List<String> hidenList;

    public HideiData()
    {

    }

    public HideiData(byte[] data)
    {
        if(data == null)
        {
            throw new NullPointerException("data is null!");
        }

        this.data = data;
        hidenList = new ArrayList<String>();
    }

    public HideiData(HideiData obj)
    {
        this(obj.data);
    }

    public byte[] getData()
    {
        return this.data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }

    public List<String> getHidenList()
    {
        return hidenList;
    }

    public String toString()
    {
        return "[data] " + data;
    }
}
