package com.ksteindl.worldjdbc.option;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class OptionBuiilder {

    private Class optionImpl;
    private Integer number;
    private Scanner scanner;
    private String label;

    public OptionBuiilder setOptionImpl(Class optionImpl) {
        this.optionImpl = optionImpl;
        return this;
    }

    public OptionBuiilder setNumber(Integer number) {
        this.number = number;
        return this;
    }

    public OptionBuiilder setScanner(Scanner scanner) {
        this.scanner = scanner;
        return this;
    }

    public OptionBuiilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public Option createOption() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = optionImpl.getDeclaredConstructor( new Class[] {Integer.class, String.class, Scanner.class});
        Option option = (Option)constructor.newInstance(new Object[]{this.number, this.label, this.scanner});
        return option;
    }
}