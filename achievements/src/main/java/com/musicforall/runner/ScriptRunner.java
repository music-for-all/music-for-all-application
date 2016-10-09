package com.musicforall.runner;

import com.musicforall.script.GroovyScript;
import groovy.lang.GroovyShell;

import java.util.Map;

/**
 * @author Evgeniy on 09.10.2016.
 */
public class ScriptRunner {
    private final GroovyShell shell = new GroovyShell();

    public static ScriptRunner get() {
        return new ScriptRunner();
    }

    public <T> T eval(GroovyScript<T> script) {
        final Map<String, Object> variables = script.getVariables();
        prepareShell(variables);

        final Object result = shell.evaluate(script.getText());

        cleanUpShell(variables);
        return (T) result;
    }

    private void prepareShell(Map<String, Object> variables) {
        variables.entrySet().stream()
                .forEach(e -> shell.setVariable(e.getKey(), e.getValue()));
    }

    private void cleanUpShell(Map<String, Object> variables) {
        for (final String key : variables.keySet()) {
            shell.getContext().getVariables().remove(key);
        }
    }
}
