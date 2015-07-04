package com.bbr.hidei.engine;

import java.util.List;

/**
 * Created by laewoongJang on 2015-04-21.
 */
public abstract class HideiDecorator extends HideiData {

    protected HideiData hideiData;

    public HideiDecorator(HideiData hideiData)
    {
        this.hideiData = hideiData;
    }

    @Override
    public List<String> getHidenList() {
        return hideiData.getHidenList();
    }
}
