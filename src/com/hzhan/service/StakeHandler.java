package com.hzhan.service;

import java.util.List;
import java.util.Map;

public interface StakeHandler {
    /**
     *
     * @param betOfferId the bet id
     * @param stake the bid price
     * @param customerId id of the customer
     */
    void postStake(int betOfferId, int stake, int customerId);

    /**
     *
     * @param betOfferId the bet id
     * @return a collection of <customerId, stake> pair
     */
    List<Map.Entry<Integer, Integer>> getHighestStakes(int betOfferId);
}
