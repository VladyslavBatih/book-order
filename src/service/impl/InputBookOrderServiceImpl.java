package service.impl;

import model.BookOrder;
import service.InputBookOrderService;
import util.Constant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

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
                        String[] fields = readLine.split(",");
                        int price = Integer.parseInt(fields[1]);
                        int size = Integer.parseInt(fields[2]);
                        String type = fields[3];
                        makeLimitOrder(price, size, type);
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
            saveOutputFile(stringBuilder.toString().trim());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void makeLimitOrder(int price, int size, String type) {
        if (price > 0) {
            if (type.equals(Constant.TYPE_ASK)) {
                bookOrder.getOrderAskList().put(price, size);
            } else {
                bookOrder.getOrderBidList().put(price, size);
            }
        }
    }

    @Override
    public void makeMarketOrder(String action, int size) {
        if (action.equals(Constant.ACTION_BUY)) {
            TreeMap<Integer, Integer> orderAskList = bookOrder.getOrderAskList();
            subtractionSizeFromOrders(orderAskList, size);
        }
        if (action.equals(Constant.ACTION_SELL)) {
            TreeMap<Integer, Integer> orderBidList = bookOrder.getOrderBidList();
            subtractionSizeFromOrders(orderBidList, size);
        }
    }

    @Override
    public String executeQuery(String query) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] queryFields = query.split(",");
        switch (queryFields[1]) {
            case Constant.QUERY_ASK: {
                TreeMap<Integer, Integer> orderAskList = bookOrder.getOrderAskList();
                orderAskList.keySet().stream()
                        .filter(key -> orderAskList.get(key) != 0)
                        .findAny()
                        .ifPresent(key -> stringBuilder.append(key)
                                .append(",").append(orderAskList.get(key))
                                .append(System.lineSeparator()));
                break;
            }
            case Constant.QUERY_BID: {
                TreeMap<Integer, Integer> orderBidList = bookOrder.getOrderBidList();
                orderBidList.keySet().stream()
                        .filter(key -> orderBidList.get(key) != 0)
                        .findAny()
                        .ifPresent(key -> stringBuilder.append(key)
                                .append(",").append(orderBidList.get(key))
                                .append(System.lineSeparator()));
                break;
            }
            case Constant.QUERY_SIZE: {
                int price = Integer.parseInt(queryFields[2]);
                TreeMap<Integer, Integer> orderAskList = bookOrder.getOrderAskList();
                TreeMap<Integer, Integer> orderBidList = bookOrder.getOrderBidList();
                Optional<Integer> optional = Optional.ofNullable(orderAskList.get(price));
                if (optional.isEmpty()) {
                    optional = Optional.ofNullable(orderBidList.get(price));
                }
                if (optional.isEmpty()) {
                    optional = Optional.of(0);
                }
                stringBuilder.append(optional.get()).append(System.lineSeparator());
                break;
            }
            default:
                break;
        }
        return stringBuilder.toString();
    }

    private void subtractionSizeFromOrders(TreeMap<Integer, Integer> orderList, int size) {
        Set<Integer> keySet = orderList.keySet();
        for (Integer integer : keySet) {
            int valueSize = orderList.get(integer);
            if (size > valueSize) {
                size -= valueSize;
                orderList.put(integer, 0);
            } else {
                orderList.put(integer, valueSize - size);
                return;
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