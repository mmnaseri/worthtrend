package com.mmnaseri.personal.worthtrend.cmd.impl;

import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.data.model.*;
import com.mmnaseri.personal.worthtrend.data.repository.TransactionRepository;
import com.mmnaseri.personal.worthtrend.io.Console;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 10:07)
 */
public class ScheduleTransactionCommand implements Command {

    private final TransactionRepository repository;
    private final DateFormat dateFormat = new SimpleDateFormat("y/M/d");

    @Autowired
    public ScheduleTransactionCommand(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getMnemonic() {
        return "schedule";
    }

    @Override
    public void execute(Console console) {
        final Transaction transaction = new Transaction();
        console.print("Note: ");
        transaction.setNote(console.read("(\\s*\\S+)+\\s*"));
        if (transaction.getNote().equals("-")) {
            return;
        }
        console.print("Amount: ");
        transaction.setAmount(console.read(Double.class));
        if (transaction.getAmount().equals(0d)) {
            return;
        }
        console.print("Start Date: ");
        try {
            transaction.setStartDate(dateFormat.parse(console.read("\\d+/\\d+/\\d+")));
        } catch (ParseException e) {
            console.println("Invalid date format");
            return;
        }
        console.print("Recurrent: [N/Y] ");
        if (console.read("[yYnN]").equalsIgnoreCase("y")) {
            final Recurrence recurrence = readRecurrence(console);
            if (recurrence == null) return;
            transaction.setRecurrence(recurrence);
        }
        final Transaction saved = repository.save(transaction);
        final String id = saved.getId();
        saved.setCode(id.substring(id.length() - 6));
        repository.save(saved);
        console.println("Saved transaction " + saved.getCode());
    }

    private Recurrence readRecurrence(Console console) {
        console.println("How often does this transaction occur?");
        console.println("D) Daily");
        console.println("W) Weekly");
        console.println("M) Monthly");
        console.println("Y) Yearly");
        final String type = console.read("[dwmyDWMY]");
        final RecurrenceType recurrenceType;
        if (type.equalsIgnoreCase("d")) {
            recurrenceType = RecurrenceType.DAY;
        } else if (type.equalsIgnoreCase("w")) {
            recurrenceType = RecurrenceType.WEEK;
        } else if (type.equalsIgnoreCase("m")) {
            recurrenceType = RecurrenceType.MONTH;
        } else if (type.equalsIgnoreCase("y")) {
            recurrenceType = RecurrenceType.YEAR;
        } else {
            console.println("Invalid recurrence type");
            return null;
        }
        final Recurrence recurrence = new Recurrence();
        recurrence.setType(recurrenceType);
        console.print("How many " + recurrenceType.name().toLowerCase() + "s should pass between two occurrences? ");
        recurrence.setFrequency(Math.max(1, console.read(Integer.class)));
        console.print("Does the recurrence end? [Y/N] ");
        if (console.read("[yYnN]").equalsIgnoreCase("y")) {
            final Termination termination = readTermination(console);
            recurrence.setTermination(termination);
        }
        return recurrence;
    }

    private Termination readTermination(Console console) {
        console.print("Do you want this to finish after a particular (n)umber of times or on a (d)ate? ");
        final Termination termination;
        if (console.read("[nNdD]").equalsIgnoreCase("n")) {
            termination = new IterativeTermination();
            console.print("How many times should this transaction run? ");
            ((IterativeTermination) termination).setIterations(console.read(Integer.class));
        } else {
            termination =  new TimedTermination();
            console.print("When should this transaction end? ");
            try {
                ((TimedTermination) termination).setEndDate(dateFormat.parse(console.read("\\d+/\\d+/\\d+")));
            } catch (ParseException e) {
                console.println("Invalid date format");
                return null;
            }
        }
        return termination;
    }

}
