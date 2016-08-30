package com.musicforall.services.message;

/**
 * Created by kgavrylchenko on 25.08.16.
 */

public final class Mail {

    private String text;
    private String from;
    private String to;
    private String subject;

    private Mail(Builder builder) {
        text = builder.text;
        from = builder.from;
        to = builder.to;
        subject = builder.subject;
    }

    public static Builder build() {
        return new Builder();
    }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public static final class Builder {
        private String text;
        private String from;
        private String to;
        private String subject;

        private Builder() {
        }

        public Builder text(String val) {
            text = val;
            return this;
        }

        public Builder from(String val) {
            from = val;
            return this;
        }

        public Builder to(String val) {
            to = val;
            return this;
        }

        public Builder subject(String val) {
            subject = val;
            return this;
        }

        public Mail get() {
            return new Mail(this);
        }
    }
}
