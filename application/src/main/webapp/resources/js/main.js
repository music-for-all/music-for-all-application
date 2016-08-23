"use strict";

function updateLikeCount(id) {

    track.getLikeCount(id)
        .then(function(likeCount) {
            $("#tracks #" + id + " .num-likes").text(likeCount);
            $("#recommendations #" + id + " .num-likes").text(likeCount);
        });
}

/**
 * Retrieves tracks recommended for the current user.
 */
function displayRecommendedTracks() {

    track.getRecommendedTracks()
        .then(function (tracks) {
            $.each(tracks, function (i, track) {
                addRecommendedTrack(track);
                updateLikeCount(track.id);
            });
        });
}
