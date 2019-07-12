package com.watson.saralink;

import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LinkManager {

    public interface Listener {
        void onLinkAdd(String peerId);
        void onLinkRemove(String peerId);
    }

    private static LinkManager instance;

    private List<Listener> listenerList = new LinkedList<>();

    Map<String, LinkStateContext> registry = new ConcurrentHashMap<>();

    public static LinkManager getInstance() {
        if(null == instance) {
            instance = new LinkManager();
        }
        return instance;
    }

    public void register(String peerId, LinkStateContext context) {
        registry.put(peerId, context);
        notifyLinkAdd(peerId);

    }

    public void unregister(String peerId) {
        registry.remove(peerId);
        notifyLinkRemove(peerId);
    }

    /**
     * 判断annababy是否已连接。
     * sarababy重连用
     * @return
     */
    public boolean isAnnababyExist() {
        return registry.containsKey("annababy");
    }

    /**
     * 获取sarababy同步调用接口。
     * 短期使用，不要缓存.
     * @param peerId
     * @return 不存在返回null
     */
    public ISarababy getSarababy(String peerId) {
        LinkStateContext context = registry.get(peerId);

        if(null == context) {
            return null;
        }

        SarababyProxy proxy = new SarababyProxy(context);

        return (ISarababy) Proxy.newProxyInstance(LinkManager.class.getClassLoader(), new Class[] {ISarababy.class}, proxy);
    }


    public void registerListener(Listener l) {
        listenerList.add(l);
    }

    void notifyLinkAdd(String peerId) {
        for(Listener l : listenerList) {
            l.onLinkAdd(peerId);
        }
    }

    void notifyLinkRemove(String peerId) {
        for(Listener l : listenerList) {
            l.onLinkRemove(peerId);
        }
    }

}
