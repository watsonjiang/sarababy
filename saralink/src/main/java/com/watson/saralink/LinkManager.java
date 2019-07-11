package com.watson.saralink;

import java.util.Map;
import java.util.Set;

public class LinkManager {

    private static LinkManager instance;

    Map<String, LinkStateContext> registry;

    public static LinkManager getInstance() {
        return instance;
    }

    public void register(String peerId, LinkStateContext context) {
        registry.put(peerId, context);
    }

    public void unregister(String peerId) {
        registry.remove(peerId);
    }

    public Set<String> getAllPeerId() {
        return null;
    }
}
