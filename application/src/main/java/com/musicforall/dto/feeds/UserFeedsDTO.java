package com.musicforall.dto.feeds;

import com.musicforall.dto.feed.Feed;
import com.musicforall.model.user.User;

import java.util.Collection;
import java.util.Objects;

/**
 * @author IliaNik on 09.10.2016.
 */
public class UserFeedsDTO {
    private final User user;
    private final Collection<Feed> feeds;

    public UserFeedsDTO(User user, Collection<Feed> feeds) {
        this.user = user;
        this.feeds = feeds;
    }

    public User getUser() {
        return user;
    }

    public Collection<Feed> getFeeds() {
        return feeds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserFeedsDTO)) return false;
        final UserFeedsDTO that = (UserFeedsDTO) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(feeds, that.feeds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, feeds);
    }
}
