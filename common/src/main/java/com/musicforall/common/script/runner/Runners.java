package com.musicforall.common.script.runner;

/**
 * @author Evgeniy on 11.10.2016.
 */
public final class Runners {

    private Runners() {
    }

    public static ScriptRunner getRunner(final Type type) {
        switch (type) {
            case GROOVY:
                return new GroovyRunner();
            default:
                return null;
        }
    }

    public enum Type {
        GROOVY
    }
}
