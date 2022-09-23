package model;

import java.util.Collections;
import java.util.TreeMap;

public class BookOrder {

    private final TreeMap<Integer, Integer> orderAskList;
    private final TreeMap<Integer, Integer> orderBidList;

    public BookOrder() {
        orderAskList = new TreeMap<>();
        orderBidList = new TreeMap<>(Collections.reverseOrder());
    }

    public TreeMap<Integer, Integer> getOrderAskList() {
        return orderAskList;
    }

    public TreeMap<Integer, Integer> getOrderBidList() {
        return orderBidList;
    }

    @Override
    public String toString() {
        return "BookOrder {" +
                "orderAskList = " + orderAskList +
                ", orderBidList = " + orderBidList +
                '}';
    }
}