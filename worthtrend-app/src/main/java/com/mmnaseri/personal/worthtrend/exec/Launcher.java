package com.mmnaseri.personal.worthtrend.exec;

import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.io.Console;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 09:41)
 */
public class Launcher implements Runnable {

    private final Console console;

    public Launcher(Console console) {
        this.console = console;
    }

    @Override
    public void run() {
        do {
            console.print("wt > ");
            final String mnemonic = console.read().trim();
            if (mnemonic.equals("exit")) {
                break;
            }
            final Command command = console.getCommand(mnemonic);
            if (command == null) {
                console.println("No such command: " + mnemonic);
            } else {
                command.execute(console);
            }
        } while (true);
    }

}
