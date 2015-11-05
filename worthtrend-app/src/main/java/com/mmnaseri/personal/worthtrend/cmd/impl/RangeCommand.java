package com.mmnaseri.personal.worthtrend.cmd.impl;

import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.data.model.Transaction;
import com.mmnaseri.personal.worthtrend.data.repository.TransactionRepository;
import com.mmnaseri.personal.worthtrend.exec.TransactionCalculator;
import com.mmnaseri.personal.worthtrend.io.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 11:34)
 */
public class RangeCommand implements Command {

    private final DateFormat dateFormat = new SimpleDateFormat("y/M/d");
    private final TransactionRepository repository;
    private final TransactionCalculator calculator;

    @Autowired
    public RangeCommand(TransactionRepository repository, TransactionCalculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    @Override
    public String getMnemonic() {
        return "range";
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void execute(Console console) {
        console.print("Starting balance: ");
        Double balance = console.read(Double.class);
        final Calendar from = new GregorianCalendar();
        final Calendar to = new GregorianCalendar();
        from.setTime(readDate(console, "Start date: "));
        to.setTime(readDate(console, "End date: "));
        if (from.after(to)) {
            console.println("Date range is not valid");
        }
        to.add(Calendar.DATE, 1);
        final List<Transaction> transactions = repository.findAll(new Sort("note"));
        for (; from.before(to); from.add(Calendar.DATE, 1)) {
            final List<Transaction> dailyTransactions = new LinkedList<>();
            for (Transaction transaction : transactions) {
                if (calculator.happensOn(transaction, from.getTime())) {
                    dailyTransactions.add(transaction);
                }
            }
            if (!dailyTransactions.isEmpty()) {
                console.println("================================");
                console.println(dateFormat.format(from.getTime()));
                console.println("--------------------------------");
                for (Transaction transaction : dailyTransactions) {
                    console.println(transaction);
                    balance += transaction.getAmount();
                }
                console.println("--------------------------------");
                console.println("Balance: " + balance);
                console.println("");
            }
        }
    }

    private Date readDate(Console console, String message) {
        try {
            console.print(message);
            return dateFormat.parse(console.read("\\d+/\\d+/\\d+"));
        } catch (ParseException e) {
            return null;
        }
    }

}
