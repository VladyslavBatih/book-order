package model;

public class Order {

    private String type;
    private int price;
    private int size;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Order {" +
                "type = '" + type + '\'' +
                ", price = '" + price + '\'' +
                ", size = '" + size + '\'' +
                '}';
    }
}