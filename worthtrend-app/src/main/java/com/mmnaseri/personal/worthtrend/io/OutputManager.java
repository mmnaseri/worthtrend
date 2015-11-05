package com.mmnaseri.personal.worthtrend.io;

import java.io.OutputStream;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/05, 11:52)
 */
public interface OutputManager {

    OutputStream create(String channel);

}
