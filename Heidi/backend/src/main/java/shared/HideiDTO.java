package shared;

import java.io.Serializable;

/**
 * Created by laewoongJang on 2015-05-25.
 */
public class HideiDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private byte[] originalData;
    private byte[] coverData;
    private byte[] hideiData;
    private String password;

    public HideiDTO() {

    }

    public byte[] getOriginalData() {
        return originalData;
    }

    public void setOriginalData(byte[] originalData) {
        this.originalData = originalData;
    }

    public byte[] getCoverData() {
        return coverData;
    }

    public void setCoverData(byte[] coverData) {
        this.coverData = coverData;
    }

    public byte[] getHideiData() {
        return hideiData;
    }

    public void setHideiData(byte[] hideiData) {
        this.hideiData = hideiData;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
