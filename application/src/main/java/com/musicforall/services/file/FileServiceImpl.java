package com.musicforall.services.file;

import com.musicforall.files.manager.FileManager;
import com.musicforall.model.Artist;
import com.musicforall.dto.profile.ProfileData;
import com.musicforall.model.SearchArtistRequest;
import com.musicforall.model.Track;
import com.musicforall.services.artist.ArtistService;
import com.musicforall.services.track.TrackService;
import com.musicforall.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Created by Pavel Podgorniy on 9/9/2016.
 */

@Service("fileService")
@Transactional
public class FileServiceImpl implements FileService {

    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    private static final String DEFAULT_PICTURE_DIRECTORY = "/files/picture/";

    @Autowired
    private FileManager manager;

    @Autowired
    private TrackService trackService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<String> uploadTrackFile(Track track, MultipartFile file) {
        final Optional<Path> saved = manager.saveTrack(file);
        Track trackForSaving = track;
        if (saved.isPresent()) {
            trackForSaving.setLocation(file.getOriginalFilename());
            trackForSaving.setSize(file.getSize());

            trackForSaving = updateArtistForTrack(track);
            trackService.save(trackForSaving);
            return new ResponseEntity<>("Song successfully saved", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> uploadPictureFile(Integer userId, MultipartFile file) {
        requireNonNull(userId, "user must not be null");
        final Optional<Path> saved = manager.savePicture(userId, file);
        if (saved.isPresent()) {
            userService.updateUserData(userId, ProfileData.create()
                    .picture(DEFAULT_PICTURE_DIRECTORY + userId + "/" + file.getOriginalFilename())
                    .get());
            return new ResponseEntity<>("Picture successfully saved", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Track updateArtistForTrack(Track track) {
        final List<Artist> listArtists =
                artistService.getAllLike(new SearchArtistRequest(track.getArtist().getName(), null));
        if (listArtists != null && !listArtists.isEmpty()) {
            final Artist existingArtist = listArtists.get(0);
            if (existingArtist != null) {
                if (track.getTags() != null) {
                    existingArtist.extendTags(track.getTags());
                }
                track.setArtist(existingArtist);
            }
        }
        return track;
    }

    @Override
    public ResponseEntity<String> checkFile(MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (manager.getFilePathByName(file.getOriginalFilename()).isPresent()) {
            return new ResponseEntity<>("File exist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("ok", HttpStatus.ACCEPTED);
    }
}
