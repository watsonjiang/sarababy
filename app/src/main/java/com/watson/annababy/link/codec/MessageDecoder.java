package com.watson.annababy.link.codec;

import com.google.gson.Gson;
import com.watson.annababy.link.msg.Message;
import com.watson.annababy.link.msg.MessageMeta;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MessageDecoder extends CumulativeProtocolDecoder {

    private static Gson gson = new Gson();

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        int start = ioBuffer.position();
        if(ioBuffer.remaining() > 8) {
            byte[] typeData = new byte[4];
            byte[] lenData = new byte[4];
            ioBuffer.get(typeData);
            ioBuffer.get(lenData);
            int len = Utils.byteArrayToInt(lenData);
            int type = Utils.byteArrayToInt(typeData);
            if(ioBuffer.remaining() >= len) {
                byte[] data = new byte[len];
                ioBuffer.get(data);
                Message m = decodeMessage(type, data);
                protocolDecoderOutput.write(m);
                ioBuffer.position(ioBuffer.position());
                return true;
            }
        }

        ioBuffer.position(start);
        return false;
    }

    Message decodeMessage(int type, byte[] data) {
        Class<? extends Message> clz = MessageMeta.getMessageClz(type);
        return gson.fromJson(new String(data), clz);
    }

}
