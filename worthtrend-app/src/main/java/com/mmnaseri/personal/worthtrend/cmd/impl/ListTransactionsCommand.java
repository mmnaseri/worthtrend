package com.mmnaseri.personal.worthtrend.cmd.impl;

import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.data.model.Transaction;
import com.mmnaseri.personal.worthtrend.data.repository.TransactionRepository;
import com.mmnaseri.personal.worthtrend.io.Console;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 09:52)
 */
public class ListTransactionsCommand implements Command {

    private final TransactionRepository repository;

    @Autowired
    public ListTransactionsCommand(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getMnemonic() {
        return "list";
    }

    @Override
    public void execute(Console console) {
        final List<Transaction> transactions = repository.findAll(new Sort(Sort.Direction.ASC, "note"));
        if (transactions.isEmpty()) {
            console.println("You do not have any transactions scheduled (:");
        }
        for (Transaction transaction : transactions) {
            console.print(transaction.getCode());
            console.print(": ");
            console.print(transaction.getNote());
            console.print(" (");
            console.print(transaction.getAmount());
            console.println(")");
        }
    }

}
