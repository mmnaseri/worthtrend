package com.mmnaseri.personal.worthtrend.exec;

import com.mmnaseri.personal.worthtrend.data.model.Transaction;

import java.util.Date;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 11:07)
 */
public interface TransactionCalculator {

    boolean happensOn(Transaction transaction, Date date);

}
