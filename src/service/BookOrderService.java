package service;

import model.Order;

public interface BookOrderService {

    void makeLimitOrder(Order order);

    void makeMarketOrder(String action, int size);
}