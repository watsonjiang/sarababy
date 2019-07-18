package com.watson.saralink.codec;

import com.google.gson.Gson;
import com.watson.saralink.msg.Message;
import com.watson.saralink.msg.MessageMeta;
import com.watson.saralink.msg.ScreenCapRsp;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import static com.watson.saralink.msg.Constant.TYPE_SCREENCAP_RSP;

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
        if(type == TYPE_SCREENCAP_RSP) {
            //截图响应带图片内容信息，特别处理
            ScreenCapRsp rsp = new ScreenCapRsp();
            rsp.setData(data);
            return rsp;
        }else {

            Class<? extends Message> clz = MessageMeta.getMessageClz(type);
            if (null == clz) {
                throw new RuntimeException("未知消息类型. type:" + type);
            }
            return gson.fromJson(new String(data), clz);
        }
    }

}
