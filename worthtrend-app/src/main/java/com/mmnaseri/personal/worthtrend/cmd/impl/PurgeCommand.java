package com.mmnaseri.personal.worthtrend.cmd.impl;

import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.data.repository.TransactionRepository;
import com.mmnaseri.personal.worthtrend.io.Console;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 15:25)
 */
public class PurgeCommand implements Command {

    private final TransactionRepository repository;

    @Autowired
    public PurgeCommand(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public String getMnemonic() {
        return "purge";
    }

    @Override
    public void execute(Console console) {
        console.println("Are you sure that you want to PURGE the database? [Yes/No]");
        console.println("This action cannot be undone. You will lose all your data in this process.");
        final String answer = console.read("([yY][eE][sS]|[nN][oO])");
        if (answer.equalsIgnoreCase("yes")) {
            final long count = repository.count();
            repository.deleteAll();
            console.println("You just removed " + count + " transaction(s).");
        }
    }

}
