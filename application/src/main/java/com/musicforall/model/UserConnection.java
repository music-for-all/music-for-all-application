package com.musicforall.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Andrey on 8/11/16.
 */

@Entity
@Table(name = "UserConnection")
public class UserConnection implements Serializable {

    private static final long serialVersionUID = 1957893141381799674L;

    @Column
    private final String userId;

    @Column
    private final String providerId;

    @Column
    private final String providerUserId;

    @Column
    private final int rank;

    @Column
    private final String displayName;

    @Id
    @Column(unique = true)
    private final String profileUrl;

    @Column(unique = true)
    private final String imageUrl;

    @Column
    private final String accessToken;

    @Column
    private final String secret;

    @Column
    private final String refreshToken;

    @Column
    private final Long expireTime;

    public UserConnection(String userId, String providerId, String providerUserId,
                          int rank, String displayName, String profileUrl,
                          String imageUrl, String accessToken, String secret,
                          String refreshToken, Long expireTime) {
        this.userId = userId;
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.rank = rank;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.secret = secret;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public int getRank() {
        return rank;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public String toString() {
        return
                "userId = " + userId +
                        ", providerId = " + providerId +
                        ", providerUserId = " + providerUserId +
                        ", rank = " + rank +
                        ", displayName = " + displayName +
                        ", profileUrl = " + profileUrl +
                        ", imageUrl = " + imageUrl +
                        ", accessToken = " + accessToken +
                        ", secret = " + secret +
                        ", refreshToken = " + refreshToken +
                        ", expireTime = " + expireTime;
    }
}

