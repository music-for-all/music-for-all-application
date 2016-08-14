var MP3ChunksPlayer = function () {

    var _self = this;

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

    /**
     * The audio source is responsible for playing the music
     *
     * @private
     * @type {AudioBufferSourceNode}
     */
    var _audioSource;

    var _analyser;

    var _request = new XMLHttpRequest();
    _request.responseType = 'arraybuffer';
    _request.addEventListener('load', _onChunkLoaded, false);

    /**
     * Creates a new Uint8Array based on two different ArrayBuffers
     *
     * @private
     * @param {ArrayBuffers} buffer1 The first buffer.
     * @param {ArrayBuffers} buffer2 The second buffer.
     * @return {ArrayBuffers} The new ArrayBuffer created out of the two.
     */
    var _appendBuffer = function (buffer1, buffer2) {
        var tmp = new Uint8Array(buffer1.byteLength + buffer2.byteLength);
        tmp.set(new Uint8Array(buffer1), 0);
        tmp.set(new Uint8Array(buffer2), buffer1.byteLength);
        return tmp.buffer;
    };

    var _initializeWebAudio = function () {
        _audioContext = new AudioContext();
        _analyser = _audioContext.createAnalyser();
        _analyser.fftSize = 2048;
    };

    /**
     * Plays the music from the point that it currently is.
     *
     * @private
     */
    var _play = function () {
        // Adding a bit of  scheduling so that we won't have single digit milisecond overlaps.
        // Thanks to Chris Wilson for his suggestion.
        var scheduledTime = 0.010;

        try {
            _audioSource.stop(scheduledTime);
        } catch (e) {
        }

        _audioSource = _audioContext.createBufferSource();
        _audioSource.buffer = _audioBuffer;
        _audioSource.connect(_analyser);
        _audioSource.connect(_audioContext.destination);
        var currentTime = _audioContext.currentTime + 0.005 || 0;
        _audioSource.start(scheduledTime - 0.005, currentTime, _audioBuffer.duration - currentTime);
        _audioSource.playbackRate.value = 1;
    };

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
        if (_activeBuffer.byteLength < _track.size) {
            setTimeout(function () {
                _loadChunk(_totalChunksLoaded);
            }, 1000);
        }
    }

    var _loadChunk = function (index) {
        _request.open('GET', 'files/' + _track.id + '/' + index, true);
        _request.send();
    };

    /**
     * Initializes the class by loading the first chunk
     *
     * @return {MP3ChunksPlayer} Returns a reference to this instance.
     */
    this.play = function (track) {
        _totalChunksLoaded = 0;
        _audioBuffer = {};
        _track = track;
        _loadChunk(_totalChunksLoaded);
        return this;
    };
};