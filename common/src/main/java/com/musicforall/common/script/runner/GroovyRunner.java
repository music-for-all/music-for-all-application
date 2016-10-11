package com.musicforall.common.script.runner;

import com.musicforall.common.script.Script;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilationFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Evgeniy on 09.10.2016.
 */
public final class GroovyRunner implements ScriptRunner {
    private static final Logger LOG = LoggerFactory.getLogger(GroovyRunner.class);

    @Override
    public <T> T eval(Script<T> script) {
        final GroovyShell shell = createShell(script.getVariables());

        final Object result = shell.evaluate(script.getText());
        return (T) result;
    }

    @Override
    public <T> boolean validate(Script<T> script) {
        final GroovyShell shell = createShell(script.getVariables());

        try {
            shell.parse(script.getText());
            return true;
        } catch (CompilationFailedException e) {
            LOG.error("Compilation of script '{}' failed", script.getText());
            return false;
        }
    }

    private GroovyShell createShell(Map<String, Object> variables) {
        final GroovyShell shell = new GroovyShell();
        variables.entrySet().stream()
                .forEach(e -> shell.setVariable(e.getKey(), e.getValue()));
        return shell;
    }
}
