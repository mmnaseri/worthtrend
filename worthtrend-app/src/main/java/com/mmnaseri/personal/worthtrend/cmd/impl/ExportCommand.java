package com.mmnaseri.personal.worthtrend.cmd.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.data.model.Transaction;
import com.mmnaseri.personal.worthtrend.data.repository.TransactionRepository;
import com.mmnaseri.personal.worthtrend.io.Console;
import com.mmnaseri.personal.worthtrend.io.OutputManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 15:03)
 */
public class ExportCommand implements Command {

    private final TransactionRepository repository;
    private final OutputManager outputManager;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExportCommand(TransactionRepository repository, OutputManager outputManager, ObjectMapper objectMapper) {
        this.repository = repository;
        this.outputManager = outputManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getMnemonic() {
        return "export";
    }

    @Override
    public void execute(Console console) {
        console.print("Where do you want to export to? ");
        final String location = console.read();
        final OutputStream stream = outputManager.create(location);
        try {
            final List<Transaction> transactions = repository.findAll();
            for (Transaction transaction : transactions) {
                transaction.setCode(null);
                transaction.setId(null);
            }
            objectMapper.writeValue(stream, transactions);
        } catch (IOException e) {
            console.println("Could not write to the output");
        }
        try {
            stream.close();
        } catch (IOException e) {
            console.println("Failed to close the output");
        }
    }

}
