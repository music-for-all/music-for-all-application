package com.musicforall.services.file;

import com.musicforall.model.Track;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Pavel Podgorniy on 9/9/2016.
 */
public interface FileService {
    public ResponseEntity<String> uploadTrackFile(Track trackJson, MultipartFile file);

    public ResponseEntity<String> checkFile(MultipartFile file);
}
