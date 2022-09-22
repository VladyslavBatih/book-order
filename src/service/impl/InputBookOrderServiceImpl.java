package service.impl;

import model.BookOrder;
import model.Order;
import service.InputBookOrderService;
import util.Constant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InputBookOrderServiceImpl implements InputBookOrderService {

    private final BookOrder bookOrder;

    public InputBookOrderServiceImpl(BookOrder bookOrder) {
        this.bookOrder = bookOrder;
    }

    @Override
    public void readInputFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Constant.FILE_INPUT));
            StringBuilder stringBuilder = new StringBuilder();
            String readLine = reader.readLine();

            while (readLine != null) {
                if (readLine.startsWith("u")) {
                    makeLimitOrder(createOrderFromInput(readLine));

                } else if (readLine.startsWith("o")) {
                    String[] fields = readLine.split(",");
                    String action = fields[1];
                    int size = Integer.parseInt(fields[2]);
                    makeMarketOrder(action, size);

                } else if (readLine.startsWith("q")) {
                    stringBuilder.append(executeQuery(readLine));

                } else {
                    throw new IllegalArgumentException("Invalid line");
                }
                readLine = reader.readLine();
            }
            saveOutputFile(stringBuilder.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void makeLimitOrder(Order order) {
        if (order.getType().equals(Constant.TYPE_ASK)) {
            bookOrder.getOrderAskList().add(order);
        }
        if (order.getType().equals(Constant.TYPE_BID)) {
            bookOrder.getOrderBidList().add(order);
        }
    }

    @Override
    public void makeMarketOrder(String action, int size) {
        if (action.equals(Constant.ACTION_BUY)) {
            bookOrder.getOrderAskList().sort(Comparator.comparingInt(Order::getPrice));
            removeOrderFromList(bookOrder.getOrderAskList(), size);
        }
        if (action.equals(Constant.ACTION_SELL)) {
            bookOrder.getOrderBidList().sort(Comparator.comparingInt(Order::getPrice).reversed());
            removeOrderFromList(bookOrder.getOrderBidList(), size);
        }
    }

    @Override
    public String executeQuery(String query) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] queryFields = query.split(",");
        switch (queryFields[1]) {
            case Constant.QUERY_BID: {
                bookOrder.getOrderBidList().sort(Comparator.comparingInt(Order::getPrice).reversed());
                Order order = bookOrder.getOrderBidList().get(Constant.INDEX_BEST_PRICE);
                stringBuilder.append(order.getPrice()).append(",").append(order.getSize()).append("\n");
                break;
            }
            case Constant.QUERY_ASK: {
                bookOrder.getOrderAskList().sort(Comparator.comparingInt(Order::getPrice));
                Order order = bookOrder.getOrderAskList().get(Constant.INDEX_BEST_PRICE);
                stringBuilder.append(order.getPrice()).append(",").append(order.getSize()).append("\n");
                break;
            }
            case Constant.QUERY_SIZE: {
                int price = Integer.parseInt(queryFields[2]);
                bookOrder.getOrderAskList().addAll(bookOrder.getOrderBidList());
                Order order = bookOrder.getOrderAskList().stream()
                        .filter(o -> o.getPrice() == price)
                        .findAny()
                        .orElse(new Order());
                stringBuilder.append(order.getSize()).append("\n");
                break;
            }
            default: break;
        }
        return stringBuilder.toString();
    }

    private Order createOrderFromInput(String line) {
        String[] fields = line.split(",");
        Order order = new Order();
        order.setPrice(Integer.parseInt(fields[1]));
        order.setSize(Integer.parseInt(fields[2]));
        order.setType(fields[3]);
        return order;
    }

    private void removeOrderFromList(List<Order> orderList, int size) {
        List<Order> removeOrderList = new ArrayList<>();
        for (Order order : orderList) {
            while (size != 0) {
                if (size >= order.getSize()) {
                    size -= order.getSize();
                    removeOrderList.add(order);
                } else {
                    int currentOrderSize = order.getSize() - size;
                    order.setSize(currentOrderSize);
                    size = 0;
                }
            }
        }
        orderList.removeAll(removeOrderList);
    }

    private void saveOutputFile(String content) {
        try (PrintWriter outPut = new PrintWriter(Constant.FILE_OUTPUT)) {
            outPut.print(content);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}