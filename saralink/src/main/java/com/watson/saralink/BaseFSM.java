package com.watson.saralink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class BaseFSM implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseFSM.class);

    BlockingQueue<LinkEvent> queue = new LinkedBlockingDeque<>();


    LinkStat stat;

    List<Rule> rules;

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

    public void postEv(LinkEvent ev) {
        try {
            queue.put(ev);
        }catch(InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void onEvent() {

    }

    @Override
    public void run() {
        LOGGER.info("LinkClientFsm enter run loop.");
        while(true) {
            try {
                LinkEvent ev = queue.poll(100, TimeUnit.MILLISECONDS);
                if (null == ev) {
                    continue;
                }
                boolean isRuleMatched = false;
                for (Rule r : rules) {
                    if (stat.equals(r.st1) && r.ev.equals(ev)) {
                        LOGGER.debug("rule matched. st:" + stat + " ev:" + ev);
                        stat = r.st2;
                        if (null != r.ac) {
                            r.ac.run();
                        }
                        isRuleMatched = true;
                        break;
                    }
                }
                if (!isRuleMatched) {
                    LOGGER.warn("no rule matched.!! st:{} ev:{}", stat, ev);
                }
            } catch (InterruptedException e) {
                //ignore
            }
        }
    }
}
