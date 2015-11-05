package com.mmnaseri.personal.worthtrend.io.impl;

import com.mmnaseri.personal.worthtrend.cmd.Command;
import com.mmnaseri.personal.worthtrend.io.Console;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.NumberUtils;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 09:37)
 */
public class ConsoleImpl implements Console, ApplicationContextAware {

    private final PrintStream out;
    private final Scanner in;
    private final Map<String, Command> commands;

    public ConsoleImpl(PrintStream out, InputStream in) {
        this.out = out;
        this.in = new Scanner(in);
        this.commands = new ConcurrentHashMap<>();
    }

    @Override
    public void registerCommand(Command command) {
        commands.put(command.getMnemonic(), command);
    }

    @Override
    public Command getCommand(String mnemonic) {
        return commands.get(mnemonic);
    }

    @Override
    public void print(Object message) {
        out.print(message);
    }

    @Override
    public void println(Object message) {
        out.println(message);
    }

    @Override
    public String read() {
        return in.nextLine();
    }

    @Override
    public <T extends Number> T read(Class<T> numberType) {
        return NumberUtils.parseNumber(in.nextLine(), numberType);
    }

    @Override
    public String read(String pattern) {
        final Pattern regexp = Pattern.compile("^" + pattern + "$", Pattern.DOTALL);
        do {
            final String value = read();
            if (regexp.matcher(value).matches()) {
                return value;
            } else {
                println("Invalid input format. Expected " + pattern);
            }
        } while (true);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        final Collection<Command> commands = applicationContext.getBeansOfType(Command.class).values();
        for (Command command : commands) {
            registerCommand(command);
        }
    }

}
