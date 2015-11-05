package com.mmnaseri.personal.worthtrend.exec.impl;

import com.mmnaseri.personal.worthtrend.data.model.*;
import com.mmnaseri.personal.worthtrend.exec.TransactionCalculator;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 11:09)
 */
public class DefaultTransactionCalculator implements TransactionCalculator {

    @Override
    public boolean happensOn(Transaction transaction, Date date) {
        final Calendar calendar = new GregorianCalendar();
        final Calendar target = new GregorianCalendar();
        target.setTime(date);
        calendar.setTime(transaction.getStartDate());
        //the transaction hasn't started yet
        if (calendar.getTime().after(transaction.getStartDate())) {
            return false;
        }
        if (sameDay(calendar, target)) {
            return true;
        }
        //if it isn't recurrent, the date should be the same as the start date
        final Recurrence recurrence = transaction.getRecurrence();
        if (recurrence == null) {
            return false;
        } else {
            final Termination termination = recurrence.getTermination();
            int iterations = 0;
            while (true) {
                switch (recurrence.getType()) {
                    case DAY:
                        calendar.add(Calendar.DATE, recurrence.getFrequency());
                        break;
                    case WEEK:
                        calendar.add(Calendar.DATE, 7 * recurrence.getFrequency());
                        break;
                    case MONTH:
                        calendar.add(Calendar.MONTH, recurrence.getFrequency());
                        break;
                    case YEAR:
                        calendar.add(Calendar.YEAR, recurrence.getFrequency());
                        break;
                }
                iterations ++;
                if (sameDay(calendar, target)) {
                    return true;
                } else if (calendar.after(target)) {
                    return false;
                } else if (termination != null) {
                    if (termination instanceof IterativeTermination) {
                        final Integer expectedIterations = ((IterativeTermination) termination).getIterations();
                        if (expectedIterations <= iterations) {
                            return false;
                        }
                    } else {
                        final Date endDate = ((TimedTermination) termination).getEndDate();
                        if (endDate.before(date)) {
                            return false;
                        }
                    }
                }
            }
        }
    }

    private boolean sameDay(Calendar calendar, Calendar target) {
        return calendar.get(Calendar.YEAR) == target.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == target.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == target.get(Calendar.DAY_OF_MONTH);
    }

}
