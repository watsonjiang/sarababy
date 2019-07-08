package com.watson.annababy.link;

import org.junit.Test;

public class LinkManagerIT {
    //测试连接
    @Test
    public void testConnect() {
        LinkManager m = new LinkManager();

        while(true) {
            try{
                Thread.sleep(10000);
            }catch (InterruptedException e) {
                //ignore
            }
        }
    }
}
