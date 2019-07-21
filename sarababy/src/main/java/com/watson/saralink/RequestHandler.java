package com.watson.saralink;

import com.watson.saralink.msg.CmdExecReq;
import com.watson.saralink.msg.CmdExecRsp;
import com.watson.saralink.msg.ScreenCapReq;
import com.watson.saralink.msg.ScreenCapRsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
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
            Process proc = getSuProc();
            proc.getOutputStream().write("screencap\n".getBytes());
            int i = 0;
            int j = 0;
            while(-1 != (j = proc.getInputStream().read())) {
                i++;
                LOGGER.info("i:{} j:{}", i, j);
            }

            ScreenCapRsp rsp = new ScreenCapRsp(req);
            rsp.setData("test".getBytes());
            return rsp;
        }catch (Exception e) {
            LOGGER.error("oops!", e);
            ExceptionUtils.throwException(e);
        }
        return null;
   }
}
