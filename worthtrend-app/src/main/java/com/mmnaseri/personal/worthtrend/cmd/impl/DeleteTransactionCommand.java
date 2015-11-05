package com.mmnaseri.personal.worthtrend.cmd.impl;

import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.data.model.Transaction;
import com.mmnaseri.personal.worthtrend.data.repository.TransactionRepository;
import com.mmnaseri.personal.worthtrend.io.Console;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 10:16)
 */
public class DeleteTransactionCommand implements Command {

    private final TransactionRepository repository;

    @Autowired
    public DeleteTransactionCommand(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getMnemonic() {
        return "delete";
    }

    @Override
    public void execute(Console console) {
        console.print("Enter transaction id: ");
        final String id = console.read("\\S{6}");
        final List<Transaction> transactions = repository.findByCodeEndsWith(id);
        if (transactions.isEmpty()) {
            console.println("Could not find a transaction with this id");
        } else if (transactions.size() > 1) {
            console.println("There are multiple transactions with this code: " + id);
            for (int i = 0; i < transactions.size(); i++) {
                final Transaction transaction = transactions.get(i);
                console.print(i + 1);
                console.print(": ");
                console.print(transaction.getNote());
                console.print(" (");
                console.print(transaction.getAmount());
                console.println(")");
            }
            console.print("Which one did you mean? ");
            final Integer number = console.read(Integer.class);
            if (number < 1 || number > transactions.size()) {
                console.println("You chose to NOT delete anything");
                return;
            }
            delete(console, transactions.get(number - 1));
        } else {
            delete(console, transactions.get(0));
        }
    }

    private void delete(Console console, Transaction transaction) {
        console.print("You chose to delete " + transaction + ". Is that correct? [Y//N] ");
        final String answer = console.read("[YyNn]").toLowerCase();
        if (answer.equals("y")) {
            repository.delete(transaction);
            console.println("Deleted transaction: " + transaction);
        }
    }

}
