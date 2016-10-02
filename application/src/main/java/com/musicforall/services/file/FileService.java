package com.musicforall.services.file;

import com.musicforall.model.Track;
import com.musicforall.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Pavel Podgorniy on 9/9/2016.
 */
public interface FileService {
    ResponseEntity<String> uploadTrackFile(Track trackJson, MultipartFile file);

    ResponseEntity<String> uploadPictureFile(User user, MultipartFile file);

    ResponseEntity<String> checkFile(MultipartFile file);
}
