package com.watson.saralink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;


/**
 * 连接状态机
 *
 */
public class LinkClientFsm extends BaseFSM {

    static private final Logger LOGGER = LoggerFactory.getLogger(LinkClientFsm.class);

    public LinkClientFsm(Runnable acConnect, Runnable acLogin) {
        stat = LinkStat.ST_DISCONNECTED;

        rules = new LinkedList<>(Arrays.asList(new Rule[]{
                new Rule(LinkStat.ST_DISCONNECTED, LinkEvent.EV_START, acConnect, LinkStat.ST_CONNECTING),
                new Rule(LinkStat.ST_CONNECTING, LinkEvent.EV_EXCEPTION, acConnect, LinkStat.ST_CONNECTING),
                new Rule(LinkStat.ST_CONNECTING, LinkEvent.EV_CONN_SUCC, acLogin, LinkStat.ST_CONNECTED),
                new Rule(LinkStat.ST_CONNECTED, LinkEvent.EV_EXCEPTION, acConnect, LinkStat.ST_CONNECTING),
                new Rule(LinkStat.ST_CONNECTED, LinkEvent.EV_LOGIN_START, null, LinkStat.ST_LOGINING),
                new Rule(LinkStat.ST_LOGINING, LinkEvent.EV_EXCEPTION, acConnect, LinkStat.ST_CONNECTING),
                new Rule(LinkStat.ST_LOGINING, LinkEvent.EV_LOGIN_SUCC, null, LinkStat.ST_LOGIN)
        }));

        postEv(LinkEvent.EV_START);
    }

}
