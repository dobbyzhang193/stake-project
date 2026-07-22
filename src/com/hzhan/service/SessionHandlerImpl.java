package com.hzhan.service;

import com.hzhan.model.Session;
import com.hzhan.util.SessionGenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandlerImpl implements SessionHandler {
    private final int EXPIRE_TIME_IN_MINUTE = 10;
    //<CustomerId, Session> map
    private final Map<Integer, Session> sessions = new ConcurrentHashMap<>();
    //<SessionKey, CustomerId> map
    //this map is used to retrieve the id associated with the provided session
    //with this map added, the retrieve time complexity optimized from O(N) to O(1)
    private final Map<String, Integer> sessionIndex = new ConcurrentHashMap<>();
    public SessionHandlerImpl() {}


    @Override
    public Session getOrCreatSession(int customerId) {
         return sessions.compute(customerId, (id, oldSession) -> {
            if(oldSession != null && !oldSession.isExpired()) {
                return oldSession;
            }

            Session newSession = createSession(id);
            sessionIndex.put(newSession.getKey(), id);

            if(oldSession != null) {
                sessionIndex.remove(oldSession.getKey(), id);
            }
            return newSession;
        });
    }

    /**
     *
     * @param key - session key
     *
     * @return customer id associated with session key
     */
    @Override
    public int getCustomerIdBySessionKey(String key) {
        return sessionIndex.get(key);
    }

    @Override
    public boolean isValidSession(String sessionId) {

        if(sessionId == null || sessionId.isBlank()) return false;
        if(!sessionIndex.containsKey(sessionId)) return false;
        if(sessions.get(sessionIndex.get(sessionId)).isExpired()) return false;

        return true;
    }

    private Session createSession(int customerId) {
        String sessionKey = SessionGenerator.generateSessionKey();
        Long expireTime = System.currentTimeMillis() + EXPIRE_TIME_IN_MINUTE * 60 * 1000;
        return new Session(customerId, sessionKey, expireTime);
    }

    public void testPrint() {
        System.out.println("sessions{");
        for(Map.Entry<Integer, Session> entry : sessions.entrySet()) {
            int id = entry.getKey();
            Session session = entry.getValue();
            System.out.println("id = " + id + ", sessionKey = " + session.getKey() + ", expire = " + session.getExpireTime());
        }
        System.out.println("}");

        System.out.println("sessionIndex{");
        for(Map.Entry<Integer, Session> entry : sessions.entrySet()) {
            System.out.println("sessionKey = " + entry.getValue().getKey() + ", id = " + entry.getKey());
        }
        System.out.println("}");
    }
}
