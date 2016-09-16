package com.musicforall.dto.feed;


import java.util.Date;
import java.util.Objects;

/**
 * @author IliaNik on 03.09.2016.
 */
public class Feed {

    private final Date date;
    private final String content;

    public Feed(String message, String target, Date date) {
        this.date = date;
        this.content = message + " " + target;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feed feed = (Feed) o;
        return Objects.equals(content, feed.content) &&
                Objects.equals(date, feed.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, date);
    }
}