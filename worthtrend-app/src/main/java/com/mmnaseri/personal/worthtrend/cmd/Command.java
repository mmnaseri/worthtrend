package com.mmnaseri.personal.worthtrend.cmd;

import com.mmnaseri.personal.worthtrend.io.Console;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 09:48)
 */
public interface Command {

    String getMnemonic();

    void execute(Console console);

}
