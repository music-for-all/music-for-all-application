package com.musicforall.runner;

import com.musicforall.script.Script;

/**
 * @author Evgeniy on 11.10.2016.
 */
public interface ScriptRunner {
    <T> T eval(Script<T> script);

    <T> boolean validate(Script<T> script);
}
