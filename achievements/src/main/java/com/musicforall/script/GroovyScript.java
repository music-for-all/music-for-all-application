package com.musicforall.script;

import com.musicforall.runner.ScriptRunner;

import java.util.Map;

/**
 * @author Evgeniy on 09.10.2016.
 */
public class GroovyScript<T> {
    private final String text;
    private final Map<String, Object> variables;

    public GroovyScript(String text, Map<String, Object> variables) {
        this.text = text;
        this.variables = variables;
    }

    public T run() {
        return ScriptRunner.get().eval(this);
    }

    public String getText() {
        return text;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
