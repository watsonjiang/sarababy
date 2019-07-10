package com.watson.saralink;

import org.apache.mina.statemachine.annotation.State;

import java.util.Arrays;
import java.util.LinkedList;

public class LinkServerHandler {

    @State
    public static final String ST_CONNECTED = ""

    public LinkServer() {
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
