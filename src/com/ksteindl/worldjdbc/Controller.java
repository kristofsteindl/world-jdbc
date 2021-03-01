package com.ksteindl.worldjdbc;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ksteindl.worldjdbc.option.*;
import com.ksteindl.worldjdbc.option.modify.*;
import com.ksteindl.worldjdbc.option.select.*;

// TODO introducing Service layer
public class Controller implements AutoCloseable{
	
    private final Scanner scanner;
	
	public Controller() {
		this.scanner =  new Scanner(System.in);
	}
	
    @Override
    public void close() {
        scanner.close();
    }

    public void start() {
        int mainChoose = 0;
        List<Option> menu = createMenu(scanner);
        int exitOption = menu.stream().filter(element -> element.isExit()).findAny().get().getNumber();
        do {
            menu.forEach(element -> System.out.println(element.getNumber() + " " + element.getLabel()));
            try {
                mainChoose = Integer.parseInt(scanner.next());
                if (mainChoose > 0 && mainChoose <= menu.size() && mainChoose != exitOption) {
                    Integer finInt = mainChoose;
                    menu.stream().filter(element -> element.getNumber().equals(finInt)).findAny().get().execute();
                }
            } catch (NumberFormatException ex) {
                System.out.println("Input must be a valid number!");
                mainChoose = -1;
            }
        } while (mainChoose != exitOption);
        System.out.println("Thank you using this app, hope you return!\n");
    }



    private List<Option> createMenu(Scanner scanner) {
        List<OptionBuiilder> builders = new ArrayList<>();
        builders.add(new OptionBuiilder()
                .setOptionImpl(CitiesOfCountryOption.class)
        		.setLabel("Cities of country")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(CountriesOfContinentOption.class)
        		.setLabel("Countries of continent(s)")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(CitiesLikeOption.class)
        		.setLabel("Info of city(s)")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(CountriesOfLanguageOption.class)
        		.setLabel("Countries where a certain language is spoken")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(LanguagesOverThresholdOption.class)
        		.setLabel("Languages spoken by more than treshold")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(ContinentGnpOption.class)
        		.setLabel("Continents order by GNP")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(CreateCityOption.class)
                .setLabel("Add new city")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(ModifyCityOption.class)
                .setLabel("Modify existing city")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(DeleteCityOption.class)
                .setLabel("Delete existing city")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(CreateLanguageOption.class)
                .setLabel("Add language for a country")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(ModifyLanguageOption.class)
                .setLabel("Modify existing language of a country")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(DeleteLanguageOption.class)
                .setLabel("Delete language from country")
                .setScanner(scanner));
        builders.add(new OptionBuiilder()
                .setOptionImpl(ExitOption.class)
        		.setLabel("Exit"));
        return Stream
                .iterate(0, i -> i + 1)
                .limit(builders.size())
                .map(i -> {
                    try {
                        return builders.get(i).setNumber(i + 1).createOption();
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

}
