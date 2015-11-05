package com.mmnaseri.personal.worthtrend.cmd.impl;

import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.data.model.Transaction;
import com.mmnaseri.personal.worthtrend.data.repository.TransactionRepository;
import com.mmnaseri.personal.worthtrend.exec.TransactionCalculator;
import com.mmnaseri.personal.worthtrend.io.Console;
import com.mmnaseri.personal.worthtrend.io.OutputManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 12:03)
 */
public class ReportCommand implements Command {

    private final DateFormat dateFormat = new SimpleDateFormat("y/M/d");
    private final TransactionRepository repository;
    private final TransactionCalculator calculator;
    private final OutputManager outputManager;

    @Autowired
    public ReportCommand(TransactionRepository repository, TransactionCalculator calculator, OutputManager outputManager) {
        this.repository = repository;
        this.calculator = calculator;
        this.outputManager = outputManager;
    }

    @Override
    public String getMnemonic() {
        return "report";
    }

    @Override
    public void execute(Console console) {
        console.print("Where do you want to save this report? ");
        final String filePath = console.read();
        final OutputStream outputStream = outputManager.create(filePath);
        console.print("Do you want a (d)etailed report or a dail(y) report? ");
        final String reportType = console.read("[dDyY]").toLowerCase();
        console.print("Starting balance: ");
        Double balance = console.read(Double.class);
        final Calendar from = new GregorianCalendar();
        final Calendar to = new GregorianCalendar();
        //noinspection ConstantConditions
        from.setTime(readDate(console, "Start date: "));
        //noinspection ConstantConditions
        to.setTime(readDate(console, "End date: "));
        if (from.after(to)) {
            console.println("Date range is not valid");
        }
        to.add(Calendar.DATE, 1);
        if (reportType.equals("d")) {
            write(outputStream, "Date", "Transaction", "Amount", "Daily Balance", "Balance");
        } else {
            write(outputStream, "Date", "Balance");
        }
        final List<Transaction> transactions = repository.findAll(new Sort("note"));
        for (; from.before(to); from.add(Calendar.DATE, 1)) {
            final List<Transaction> dailyTransactions = new LinkedList<>();
            for (Transaction transaction : transactions) {
                if (calculator.happensOn(transaction, from.getTime())) {
                    dailyTransactions.add(transaction);
                }
            }
            if (!dailyTransactions.isEmpty()) {
                for (int i = 0; i < dailyTransactions.size(); i++) {
                    final Transaction transaction = dailyTransactions.get(i);
                    balance += transaction.getAmount();
                    if (reportType.equals("d")) {
                        write(outputStream, dateFormat.format(from.getTime()), transaction.getNote(), transaction.getAmount(), i == dailyTransactions.size() - 1 ? balance : "-", balance);
                    }
                }
                if (reportType.equals("y")) {
                    write(outputStream, dateFormat.format(from.getTime()), balance);
                }
            }
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            console.println("Failed to close the stream.");
        }
    }

    private void write(OutputStream outputStream, Object... vars) {
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        boolean first = true;
        for (Object var : vars) {
            try {
                if (!first) {
                    writer.write(",");
                } else {
                    first = false;
                }
                writer.write('"');
                writer.write(String.valueOf(var));
                writer.write('"');
            } catch (IOException e) {
                return;
            }
        }
        try {
            writer.write("\n");
            writer.flush();
        } catch (IOException ignored) {
            ignored.printStackTrace();
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
