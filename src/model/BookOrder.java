package model;

import java.util.ArrayList;
import java.util.List;

public class BookOrder {

   private final List<Order> orderAskList;
   private final List<Order> orderBidList;

    public BookOrder() {
        orderBidList = new ArrayList<>();
        orderAskList = new ArrayList<>();
    }

    public List<Order> getOrderAskList() {
        return orderAskList;
    }

    public List<Order> getOrderBidList() {
        return orderBidList;
    }

    @Override
    public String toString() {
        return "BookOrder {" +
                "orderBidList = " + orderBidList +
                ", orderAskList = " + orderAskList +
                '}';
    }
}