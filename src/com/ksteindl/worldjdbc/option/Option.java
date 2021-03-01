package com.ksteindl.worldjdbc.option;

import java.util.Scanner;

public abstract class Option {

    private Integer number;
    private String label;
    protected Scanner scanner;
    private boolean exit;

    public Option(Integer number, String label, Scanner scanner, boolean exit) {
        this.scanner = scanner;
        this.number = number;
        this.label = label;
        this.exit = exit;
    }

    public abstract boolean execute();

    public Integer getNumber() {
        return number;
    }

    public String getLabel() {
        return label;
    }

	public boolean isExit() {
        return exit;
    }





}
