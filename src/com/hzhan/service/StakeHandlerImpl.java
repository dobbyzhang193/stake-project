package com.hzhan.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StakeHandlerImpl implements StakeHandler {
    //key - bet offer id
    //value - a concurrent hashmap whose key is customer id, and the value is max stake of the customer
    private static final Map<Integer, Map<Integer, Integer>> betOffers= new ConcurrentHashMap<>();
    public StakeHandlerImpl() {}
    @Override
    public void postStake(int betOfferId, int stake, int customerId) {
        //create a new empty concurrent hashmap only if key doesn't exist
        betOffers.computeIfAbsent(betOfferId,
                k -> new ConcurrentHashMap<>())
                //if customer id is null or not existed, put the value into the map
                //if customer id is not null and existed, compare the old value in the
                // map with stake, and put the maximum into the map
                .merge(customerId, stake, Math::max);

        //this is for the test print
//        for(Map.Entry<Integer, Map<Integer, Integer>> entry : betOffers.entrySet()) {
//            int offerId = entry.getKey();
//            Map<Integer, Integer> offer = entry.getValue();
//
//            // this console print is used to test purpose, and it can be removed in production env
//            System.out.println("offerId: {" + offerId);
//            for(Map.Entry<Integer, Integer> item : offer.entrySet()) {
//                System.out.println("customerId: " + item.getKey() + ", stake: " + item.getValue());
//            }
//            System.out.println("}");
//        }
    }

    @Override
    public List<Map.Entry<Integer, Integer>> getHighestStakes(int betOfferId) {
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        Map<Integer, Integer> customerStakes = betOffers.get(betOfferId);

        if(customerStakes == null || customerStakes.isEmpty()) return null;

        for(Map.Entry<Integer, Integer> entry : customerStakes.entrySet()) {
            minHeap.offer(entry);
        }

        List<Map.Entry<Integer, Integer>> result = new ArrayList<>();

        int k = 20;
        while(!minHeap.isEmpty() && k > 0) {
            result.add(minHeap.poll());
            k--;
        }

        return result;
    }

}
