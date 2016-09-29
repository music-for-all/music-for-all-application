package com.musicforall.model.user;

import java.util.Objects;

/**
 * @author ENikolskiy.
 */
public class UserConfig {

    private boolean isPublicRadio;

    public UserConfig(boolean isPublicRadio) {
        this.isPublicRadio = isPublicRadio;
    }

    public boolean isPublicRadio() {
        return isPublicRadio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPublicRadio);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final UserConfig other = (UserConfig) obj;
        return Objects.equals(this.isPublicRadio, other.isPublicRadio);
    }
}
