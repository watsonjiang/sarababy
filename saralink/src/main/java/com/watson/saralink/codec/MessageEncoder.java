package com.watson.saralink.codec;

import com.google.gson.Gson;
import com.watson.saralink.msg.Message;
import com.watson.saralink.msg.MessageMeta;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MessageEncoder implements ProtocolEncoder {

    private Gson gson = new Gson();

    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        Message m = (Message)o;
        int msgType = MessageMeta.getMessageType(m.getClass());
        if(-1 == msgType) {
            throw new RuntimeException("未知消息类型. class:" + m.getClass());
        }
        byte[] type = Utils.intToByteArray(msgType);
        byte[] data = gson.toJson(o).getBytes();
        byte[] len = Utils.intToByteArray(data.length);
        IoBuffer buf = IoBuffer.allocate(8 + data.length);
        buf.put(type);
        buf.put(len);
        buf.put(data);
        buf.flip();
        protocolEncoderOutput.write(buf);
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }

}
