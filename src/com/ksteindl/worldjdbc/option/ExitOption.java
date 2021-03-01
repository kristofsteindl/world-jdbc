package com.ksteindl.worldjdbc.option;

import java.util.Scanner;

public class ExitOption extends Option {

	public ExitOption(Integer number, String label, Scanner scanner) {
		super(number, label, scanner,true);
	}

	@Override
	public boolean execute() {
		return true;
	}

}
