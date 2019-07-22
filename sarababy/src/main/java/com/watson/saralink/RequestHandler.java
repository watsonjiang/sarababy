package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.ScreenCapReq;
import com.watson.saralink.msg.ScreenCapRsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 职责，处理命令
 */
public class RequestHandler implements IRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

    private Process suProc;

    public CmdExecRsp onCmdExec(CmdExecReq req) {
        LOGGER.info("----onCmdExec. cmd:{}", req.cmdLine);
        CmdExecRsp rsp = new CmdExecRsp(req);
        rsp.output = "hello anna";
        return rsp;
    }

    Process getSuProc() {
        if(null == suProc) {
            try {
                ProcessBuilder builder = new ProcessBuilder("su");
                suProc = builder.start();
            }catch (IOException e) {
                ExceptionUtils.throwException(e);
            }
        }
        return suProc;
    }

    @Override
    public ScreenCapRsp onScreenCap(ScreenCapReq req) {
        try {
            ProcessBuilder builder = new ProcessBuilder("su", "0", "screencap", "-p");
            Process proc = builder.start();
            //Process proc = getSuProc();
            //proc.getOutputStream().write("screencap -p\n".getBytes());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while(-1 != (len = proc.getInputStream().read(buf))) {
                bos.write(buf, 0, len);
            }
            int exitCode = proc.waitFor();
            proc.destroy();
            LOGGER.info("exit code:{}", exitCode);
            byte[] data = bos.toByteArray();
            LOGGER.info("data size:{}", data.length);
            ScreenCapRsp rsp = new ScreenCapRsp(req);
            rsp.setData(data);
            return rsp;
        }catch (Exception e) {
            LOGGER.error("oops!", e);
            ExceptionUtils.throwException(e);
        }
        return null;
   }
}
