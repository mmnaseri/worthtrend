package com.mmnaseri.personal.worthtrend.io.impl;

import com.mmnaseri.personal.worthtrend.io.OutputManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 11:52)
 */
public class FileOutputManager implements OutputManager {

    @Override
    public OutputStream create(String channel) {
        final File file = new File(channel);
        if (file.exists()) {
            if (!file.delete()) {
                throw new IllegalStateException("Failed to delete existing file: " + channel);
            }
        }
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        final FileOutputStream stream;
        try {
            stream = new FileOutputStream(file, false);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Could not create output file", e);
        }
        return stream;
    }

}
