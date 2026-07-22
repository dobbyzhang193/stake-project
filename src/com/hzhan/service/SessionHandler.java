package com.hzhan.service;

import com.hzhan.model.Session;

public interface SessionHandler {
    /**
     *
     * @param customerId id of customer
     * @return new session associate with customer id
     */
    Session getOrCreatSession(int customerId);

    /**
     *
     * @param sessionId the key of the session
     * @return boolean
     *         true - valid session
     *         false - invalid session
     */
    boolean isValidSession(String sessionId);

    /**
     *
     * @param key - session key
     * @return customer id associated with session key
     */
    int getCustomerIdBySessionKey(String key);

    //for test purpose
    void testPrint();

}
