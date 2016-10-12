package com.musicforall.common.script;

import com.musicforall.common.script.runner.Runners;

import java.util.Map;

import static com.musicforall.common.script.runner.Runners.Type.GROOVY;

/**
 * @author Evgeniy on 09.10.2016.
 */
public final class Script<T> {
    private final String text;
    private final Map<String, Object> variables;
    private Runners.Type type;

    private Script(String text, Map<String, Object> variables) {
        this.text = text;
        this.variables = variables;
        this.type = GROOVY;
    }

    public static <T> Script<T> create(String text, Map<String, Object> variables) {
        return new Script<T>(text, variables);
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
