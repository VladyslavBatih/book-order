package service;

public interface InputBookOrderService extends BookOrderService {

    void readInputFile();

    String executeQuery(String query);
}