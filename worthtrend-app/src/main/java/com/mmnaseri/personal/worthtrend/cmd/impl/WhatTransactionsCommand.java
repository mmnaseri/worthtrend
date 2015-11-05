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
import java.util.Date;
import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 11:20)
 */
public class WhatTransactionsCommand implements Command {

    private final TransactionRepository repository;
    private final TransactionCalculator calculator;
    private final DateFormat dateFormat = new SimpleDateFormat("y/M/d");

    @Autowired
    public WhatTransactionsCommand(TransactionRepository repository, TransactionCalculator calculator) {
        this.repository = repository;
        this.calculator = calculator;
    }

    @Override
    public String getMnemonic() {
        return "what";
    }

    @Override
    public void execute(Console console) {
        console.print("Enter date: ");
        final Date date;
        try {
            date = dateFormat.parse(console.read("\\d+/\\d+/\\d+"));
        } catch (ParseException e) {
            console.println("Invalid date format");
            return;
        }
        final List<Transaction> transactions = repository.findAll(new Sort("note"));
        int count = 0;
        Double total = 0d;
        for (Transaction transaction : transactions) {
            if (calculator.happensOn(transaction, date)) {
                count ++;
                console.println(transaction);
                total += transaction.getAmount();
            }
        }
        if (count == 0) {
            console.println("Looks like nothing is going to happen on this day! E-:");
        } else if (count > 1) {
            console.println("------------------------");
            console.println("Total: " + total);
        }
    }

}
