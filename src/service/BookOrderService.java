package service;

public interface BookOrderService {

    void makeLimitOrder(int price, int size, String type);

    void makeMarketOrder(String action, int size);
}