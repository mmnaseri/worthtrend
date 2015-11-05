package com.mmnaseri.personal.worthtrend.io;

import com.mmnaseri.personal.worthtrend.cmd.Command;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 09:06)
 */
public interface Console {

    void registerCommand(Command command);

    Command getCommand(String mnemonic);

    void print(Object message);

    void println(Object message);

    String read();

    <T extends Number> T read(Class<T> numberType);

    String read(String pattern);
}
