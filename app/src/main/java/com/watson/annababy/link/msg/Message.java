package com.watson.annababy.link.msg;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Message {

    String id;

    String genId() {
        return UUID.randomUUID().toString();
    }

}
