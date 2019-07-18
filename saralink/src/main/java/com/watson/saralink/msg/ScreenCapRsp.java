package com.watson.saralink.msg;

public class ScreenCapRsp extends Message {

    byte[] data;

    public ScreenCapRsp(ScreenCapReq req) {
        id = req.id;
    }

    public ScreenCapRsp() {

    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
