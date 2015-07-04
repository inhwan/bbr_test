package com.bbr.hidei.engine;

/**
 * Created by laewoongJang on 2015-04-21.
 *
 * 용어 정리 :
 * - originalData	= 사용자가 감추고 싶어하는 원본 데이터
 * - secureData		= originalData를 압축하고 암호화한 데이터
 * - coverData		= secureData를 가려버릴 데이터
 * - hideiData		= coverData와 secureData를 함께 가지고 있는 데이터
 */
public class HideiService {

    private final static byte[] PNG_SIGNATURE = { (byte) 137, (byte) 80, (byte) 78, (byte) 71, (byte) 13, (byte) 10, (byte) 26, (byte) 10 };

    public byte[] helloHidei(byte[] originalData, int coverDataId, String password)
    {
        return null;
    }

    public byte[] helloHidei(byte[] originalData, byte[] coverData, String password)
    {
        return makeHideiData(new CompressDecorator(new EncryptionDecorator(new HideiData(originalData), password)).getData(), coverData);
        //return makeHideiData(new CompressDecorator(new EncryptionDecorator(new HideiData(originalData), password)).getData(), coverData);
        //return makeHideiData(originalData, coverData);
        //return coverData;
    }

    public byte[] byeHidei(byte[] hideiData, String password)
    {
        //byte[] primitiveData = new DecompressDecorator(new DecryptionDecorator(new HideiData(hideiData), password)).process();
        return getSecureData(new DecompressDecorator(new DecryptionDecorator(new HideiData(hideiData), password)).getData());
        //return getSecureData(hideiData);
    }

    private boolean isPngFile(byte[] bytes)
    {
        boolean result = true;

        for (int i = 0; i < PNG_SIGNATURE.length; i++) {

            if (bytes[i] != PNG_SIGNATURE[i]) {
                result = false;
                break;
            }
        }

        return result;
    }

    private int getChunkSize(byte[] bytes) // 빅엔디안
    {
        return ((bytes[0] << 24) & 0xff000000) | ((bytes[1] << 16) & 0x00ff0000) | ((bytes[2] << 8) & 0x0000ff00) | (bytes[3] & 0xff);
    }

    private byte[] getSecureData(byte[] bytes)
    {
        int index = 8; // png 시그네쳐 다음 부터 시작함.

        int chunkSize = 0;

        byte[] chunck = new byte[4];

        while (true) {

            // 청크 길이 정보가 담긴 4바이트 가져와서
            for (int i = 0; i < 4; i++) {
                chunck[i] = bytes[index++];
            }

            // 청크 길이 구함.
            chunkSize = getChunkSize(chunck);

            // 청크 이름 정보가 담긴 4바이트 가져와서
            for (int i = 0; i < 4; i++) {
                chunck[i] = bytes[index++];
            }

            // 청크 이름 정보를 바이트에서 캐릭터형으로 변환 한 후,
            char[] buffer = new char[chunck.length];

            for (int i = 0; i < buffer.length; i++) {
                char c = (char) (chunck[i] & 0xFF);
                buffer[i] = c;

            }

            index = index/*청크 데이터 시작 인덱스*/ + chunkSize/*청크 길이*/ + 4/*청크 crc*/;// => 다음 청크 인덱스를 가리킴

            // 가져온 청크 이름이 마지막 청크를 나타내는 이름이면 종료
            if ('I' == buffer[0] && 'E' == buffer[1] && 'N' == buffer[2] && 'D' == buffer[3]) {
                break;
            }
        }

        int secureSize = bytes.length - index; // 감추어진 이미지의 전체 사이즈를 구함.
        byte[] secureImageByte = new byte[secureSize];

        for(int i=0; i<secureSize; i++)
        {
            secureImageByte[i] = bytes[index++]; // 감춰진 이미지 정보를 복사해 냄
        }

        return secureImageByte;
    }

    public byte[] makeHideiData(final byte[] processedData, final byte[] coverData)
    {
        // todo: originalData를 압축및 암호화
        byte[] secureData = processedData;

        if(isPngFile(coverData) == false) return null;

        byte[] hideiBytes = new byte[secureData.length + coverData.length];

        int index = 0;

//        System.arraycopy(coverData, 0, hideiBytes, 0, coverData.length);
//        System.arraycopy(secureData, 0, hideiBytes, coverData.length+1, secureData.length);
        for(int i=0; i<coverData.length; i++)
        {
            hideiBytes[index++] = coverData[i];
        }

        for(int i=0; i<secureData.length; i++)
        {
            hideiBytes[index++] = secureData[i];
        }

        return hideiBytes;
    }


}
