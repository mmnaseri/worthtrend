package com.mmnaseri.personal.worthtrend.data.repository;

import com.mmnaseri.personal.worthtrend.data.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 09:05)
 */
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByCodeEndsWith(String suffix);

}
