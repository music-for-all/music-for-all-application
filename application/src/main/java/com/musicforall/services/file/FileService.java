package com.musicforall.services.file;

import com.musicforall.model.Track;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Pavel Podgorniy on 9/9/2016.
 */
public interface FileService {
    ResponseEntity<String> uploadTrackFile(Track trackJson, MultipartFile file);

    ResponseEntity<String> uploadPictureFile(Integer userId, MultipartFile file);

    ResponseEntity<String> checkFile(MultipartFile file);
}
