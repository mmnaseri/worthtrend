package com.mmnaseri.personal.worthtrend.cmd.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.data.model.Transaction;
import com.mmnaseri.personal.worthtrend.data.repository.TransactionRepository;
import com.mmnaseri.personal.worthtrend.io.Console;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 15:09)
 */
public class ImportCommand implements Command {

    private final TransactionRepository repository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ImportCommand(TransactionRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getMnemonic() {
        return "import";
    }

    @Override
    public void execute(Console console) {
        Object value;
        console.print("Where is the data file? ");
        try {
            value = objectMapper.readValue(new File(console.read()), CollectionType.construct(LinkedList.class, SimpleType.construct(Transaction.class)));
        } catch (IOException e) {
            console.println("Failed to read from the input");
            return;
        }
        //noinspection unchecked
        final List<Transaction> transactions = (List<Transaction>) value;
        for (Transaction transaction : transactions) {
            transaction.setId(null);
            transaction.setCode(null);
            final Transaction saved = repository.save(transaction);
            saved.setCode(saved.getId().substring(saved.getId().length() - 6));
            repository.save(saved);
            console.println("Imported transaction " + saved.getCode());
        }
    }

}
