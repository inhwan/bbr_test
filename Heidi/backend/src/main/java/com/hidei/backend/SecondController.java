package com.hidei.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shared.HideiDTO;

import java.util.logging.Logger;
/**
 * Created by laewoongJang on 2015-04-21.
 */
public class SecondController {

    private static final Logger log = Logger.getLogger(HideiService.class.getName());

    public void helloHidei(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        try {
            ObjectInputStream in = new ObjectInputStream(req.getInputStream());
            HideiDTO receive = null;
            try {
                receive = (HideiDTO) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            in.close();

            HideiDTO send = new HideiDTO();

            byte[] hideiData = new HideiService().helloHidei(receive.getOriginalData(), receive.getCoverData(), receive.getPassword());

            send.setHideiData(hideiData);
            send.setPassword("cover success!!! : ");

            ObjectOutputStream oos = new ObjectOutputStream(resp.getOutputStream());
            oos.writeObject(send);
            oos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void byeHidei(HttpServletRequest req, HttpServletResponse resp)
    {
        try {
            ObjectInputStream in = new ObjectInputStream(req.getInputStream());
            HideiDTO receive = null;
            try {
                receive = (HideiDTO) in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            in.close();

            HideiDTO send = new HideiDTO();

            byte[] originalData =  new HideiService().byeHidei(receive.getHideiData(), receive.getPassword());

            send.setOriginalData(originalData);
            send.setPassword("restore success!!!");

            ObjectOutputStream oos = new ObjectOutputStream(resp.getOutputStream());
            oos.writeObject(send);
            oos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
