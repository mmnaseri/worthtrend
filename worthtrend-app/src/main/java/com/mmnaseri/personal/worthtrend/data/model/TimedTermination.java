package com.mmnaseri.personal.worthtrend.data.model;

import java.util.Date;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 10:49)
 */
public class TimedTermination extends Termination {

    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
