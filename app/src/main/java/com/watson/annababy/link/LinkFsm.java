package com.watson.annababy.link;

import android.util.Log;

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
public class LinkFsm implements Runnable{

    final String TAG = "LINK_FSM";

    public static class Rule{
        LinkStat st1;
        LinkEvent ev;
        Runnable ac;
        LinkStat st2;

        public Rule(LinkStat st1, LinkEvent ev, Runnable ac, LinkStat st2) {
            this.st1 = st1;
            this.ev = ev;
            this.ac = ac;
            this.st2 = st2;
        }
    }

    BlockingQueue<LinkEvent> queue = new LinkedBlockingDeque<>();

    LinkStat stat;

    List<Rule> rules;

    public LinkFsm(Runnable acConnect, Runnable acLogin) {
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

    public void postEv(LinkEvent ev) {
        try {
            queue.put(ev);
        }catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        Log.i(TAG, "LinkFsm enter run loop.");
        while(true) {
            try {
                LinkEvent ev = queue.poll(100, TimeUnit.MILLISECONDS);
                if (null == ev) {
                    continue;
                }
                boolean isRuleMatched = false;
                for (Rule r : rules) {
                    if (stat.equals(r.st1) && r.ev.equals(ev)) {
                        Log.d(TAG, "rule matched. st:" + stat + " ev:" + ev);
                        stat = r.st2;
                        if (null != r.ac) {
                            r.ac.run();
                        }
                        isRuleMatched = true;
                        break;
                    }
                }
                if (!isRuleMatched) {
                    Log.w(TAG, "no rule matched.!! st:" + stat + " ev:" + ev);
                }
            } catch (InterruptedException e) {
                //ignore
            }
        }
    }

}
