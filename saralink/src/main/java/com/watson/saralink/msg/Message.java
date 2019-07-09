package com.watson.saralink.msg;

import java.util.UUID;

public class Message {

    String id;

    String genId() {
        return UUID.randomUUID().toString();
    }

}
