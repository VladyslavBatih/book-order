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
import java.util.Collections;
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
                switch (readLine.charAt(0)) {
                    case 'u': {
                        makeLimitOrder(createOrderFromInput(readLine));
                        break;
                    }
                    case 'o': {
                        String[] fields = readLine.split(",");
                        String action = fields[1];
                        int size = Integer.parseInt(fields[2]);
                        makeMarketOrder(action, size);
                        break;
                    }
                    case 'q': {
                        stringBuilder.append(executeQuery(readLine));
                        break;
                    }
                    default:
                        break;
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
            List<Order> orderAskList = bookOrder.getOrderAskList();
            addOrderToOrderList(orderAskList, order);
        } else {
            List<Order> orderBidList = bookOrder.getOrderBidList();
            addOrderToOrderList(orderBidList, order);
        }
    }

    @Override
    public void makeMarketOrder(String action, int size) {
        if (action.equals(Constant.ACTION_BUY)) {
            List<Order> orderAskList = bookOrder.getOrderAskList();
            sortOrderList(orderAskList, Constant.TYPE_ASK);
            subtractionSizeFromOrders(orderAskList, size);
        }
        if (action.equals(Constant.ACTION_SELL)) {
            List<Order> orderBidList = bookOrder.getOrderBidList();
            sortOrderList(orderBidList, Constant.TYPE_BID);
            subtractionSizeFromOrders(orderBidList, size);
        }
    }

    @Override
    public String executeQuery(String query) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] queryFields = query.split(",");
        switch (queryFields[1]) {
            case Constant.QUERY_ASK: {
                List<Order> orderAskList = bookOrder.getOrderAskList();
                sortOrderList(orderAskList, Constant.TYPE_ASK);
                orderAskList.stream()
                        .filter(o -> o.getSize() != 0)
                        .findAny()
                        .ifPresent(order -> stringBuilder.append(order.getPrice())
                                        .append(",").append(order.getSize()).append(System.lineSeparator()));
                break;
            }
            case Constant.QUERY_BID: {
                List<Order> orderBidList = bookOrder.getOrderBidList();
                sortOrderList(orderBidList, Constant.TYPE_BID);
                orderBidList.stream()
                        .filter(o -> o.getSize() != 0)
                        .findAny()
                        .ifPresent(order -> stringBuilder.append(order.getPrice())
                                .append(",").append(order.getSize()).append(System.lineSeparator()));
                break;
            }
            case Constant.QUERY_SIZE: {
                int price = Integer.parseInt(queryFields[2]);
                bookOrder.getOrderAskList().addAll(bookOrder.getOrderBidList());
                Order order = bookOrder.getOrderAskList().stream()
                        .filter(o -> o.getPrice() == price)
                        .findAny()
                        .orElse(new Order());
                stringBuilder.append(order.getSize()).append(System.lineSeparator());
                break;
            }
            default:
                break;
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

    private void sortOrderList(List<Order> orderList, String listType) {
        orderList.sort(Comparator.comparingInt(Order::getPrice));
        if (listType.equals(Constant.TYPE_BID)) {
            Collections.reverse(orderList);
        }
    }

    private void addOrderToOrderList(List<Order> orderList, Order order) {
        boolean priceWasFound = false;
        for (Order currentOrder : orderList) {
            if (currentOrder.getPrice() == order.getPrice()) {
                currentOrder.setSize(order.getSize());
                priceWasFound = true;
            }
        }
        if (!priceWasFound) {
            orderList.add(order);
        }
    }

    private void subtractionSizeFromOrders(List<Order> orderList, int size) {
        for (Order order : orderList) {
            if (size >= order.getSize()) {
                size -= order.getSize();
                order.setSize(0);
            } else {
                order.setSize(order.getSize() - size);
            }
        }
    }

    private void saveOutputFile(String content) {
        try (PrintWriter outPut = new PrintWriter(Constant.FILE_OUTPUT)) {
            outPut.print(content);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}