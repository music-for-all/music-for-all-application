"use strict";

function ChunksPlayer() {

    /**
     * The ArrayBuffer that will have the new chunks appended
     *
     * @private
     * @type {ArrayBuffer}
     */
    var _activeBuffer;

    var _totalChunksLoaded = 0;

    var _track;

    var _audioContext;

    var _audioBuffer;

    var _paused = false;

    /**
     * The audio source is responsible for playing the music
     *
     * @private
     * @type {AudioBufferSourceNode}
     */
    var _audioSource;

    var _analyser;

    var _request = new XMLHttpRequest();
    _request.responseType = "arraybuffer";
    _request.addEventListener("load", _onChunkLoaded, false);

    /**
     * Creates a new Uint8Array based on two different ArrayBuffers
     *
     * @private
     * @param {ArrayBuffers} buffer1 The first buffer.
     * @param {ArrayBuffers} buffer2 The second buffer.
     * @return {ArrayBuffers} The new ArrayBuffer created out of the two.
     */
    function _appendBuffer(buffer1, buffer2) {
        var tmp = new Uint8Array(buffer1.byteLength + buffer2.byteLength);
        tmp.set(new Uint8Array(buffer1), 0);
        tmp.set(new Uint8Array(buffer2), buffer1.byteLength);
        return tmp.buffer;
    }

    function _initializeWebAudio() {
        _audioContext = new AudioContext();
        _analyser = _audioContext.createAnalyser();
        _analyser.fftSize = 2048;
    }

    function initAudioSource() {
        var audioSource = _audioContext.createBufferSource();
        audioSource.buffer = _audioBuffer;
        audioSource.connect(_analyser);
        audioSource.connect(_audioContext.destination);
        audioSource.playbackRate.value = 1;
        return audioSource;
    }

    /**
     * Plays the music from the point that it currently is.
     */
    function _play() {
        try {
            _audioSource.stop();
        } catch (e) {
            console.log(e);
        }
        _audioSource = initAudioSource();
        var currentTime = _audioContext.currentTime || 0;

        _audioSource.start(0, currentTime, _audioBuffer.duration - currentTime);
    }

    function isFullyLoaded() {
        return _activeBuffer && _activeBuffer.byteLength >= _track.size;
    }

    function _loadChunk(index) {
        if (isFullyLoaded()) return;
        _request.open("GET", "files/" + _track.id + "/" + index, true);
        _request.send();
    }

    function _onChunkLoaded() {
        if (_totalChunksLoaded === 0) {
            _initializeWebAudio();
            _activeBuffer = _request.response;
        } else {
            _activeBuffer = _appendBuffer(_activeBuffer, _request.response);
        }

        _audioContext.decodeAudioData(_activeBuffer, function (buf) {
            _audioBuffer = buf;
            _play();
        });

        _totalChunksLoaded++;
        setTimeout(function () {
            if (!_paused) {
                _loadChunk(_totalChunksLoaded);
            }
        }, 800);
    }

    this.play = function (track) {
        _totalChunksLoaded = 0;
        _audioBuffer = {};
        _track = track;
        _loadChunk(_totalChunksLoaded);
    };

    this.pause = function () {
        _paused = true;
        if (_audioContext.state === "running") {
            _audioContext.suspend()
        }
    };

    this.resume = function () {
        if (_totalChunksLoaded > 0 && _paused) {
            _paused = false;
            if (_audioContext.state === "suspended") {
                _audioContext.resume()
            }
            _loadChunk(_totalChunksLoaded);
        }
    };
};