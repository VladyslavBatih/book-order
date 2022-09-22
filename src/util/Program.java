package util;

import model.BookOrder;
import service.InputBookOrderService;
import service.impl.InputBookOrderServiceImpl;

public class Program {

    public static void start(){
        InputBookOrderService inputBookOrderService = new InputBookOrderServiceImpl(new BookOrder());
        inputBookOrderService.readInputFile();
    }

    private Program(){
    }
}