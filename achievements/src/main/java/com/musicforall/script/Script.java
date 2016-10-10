package com.musicforall.script;

import com.musicforall.runner.Runners;

import java.util.Map;

/**
 * @author Evgeniy on 09.10.2016.
 */
public class Script<T> {
    private final String text;
    private final Map<String, Object> variables;
    private Runners.Type type;

    public Script(String text, Map<String, Object> variables, Runners.Type type) {
        this.text = text;
        this.variables = variables;
        this.type = type;
    }

    public Script<T> type(Runners.Type type) {
        this.type = type;
        return this;
    }

    public T run() {
        return Runners.getRunner(type).eval(this);
    }

    public boolean isValid() {
        return Runners.getRunner(type).validate(this);
    }

    public Runners.Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
