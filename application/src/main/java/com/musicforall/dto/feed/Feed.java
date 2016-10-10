package com.musicforall.dto.feed;


import java.util.Objects;

/**
 * @author IliaNik on 03.09.2016.
 */
public class Feed {

    private final String date;
    private final String content;

    public Feed(String content, String date) {
        this.date = date;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) return false;
        final Feed feed = (Feed) o;
        return Objects.equals(content, feed.content) &&
                Objects.equals(date, feed.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, date);
    }
}