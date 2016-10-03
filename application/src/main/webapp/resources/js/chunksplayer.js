"use strict";


function ChunksPlayer() {

    var self = this;

    var lastChunkId;

    var track;

    var audioContext;

    var audioBufferQueue = [];

    var stream = new Stream();

    var sequencePlay = function () {
        stream.start(track.id);
        setTimeout(function () {
            if (self.isPlaying()) {
                loadChunk(lastChunkId);
            }
        }, 15000);
    };

    var singlePlay = function () {
    };

    var playingStrategy = sequencePlay;

    /**
     * The audio source is responsible for playing the music
     *
     * @private
     * @type {AudioBufferSourceNode}
     */
    var audioSource;

    var request = new XMLHttpRequest();
    request.responseType = "arraybuffer";
    request.addEventListener("load", onChunkLoaded, false);

    /**
     * Creates a new Uint8Array based on two different ArrayBuffers
     *
     * @private
     * @param {ArrayBuffers} buffer1 The first buffer.
     * @param {ArrayBuffers} buffer2 The second buffer.
     * @return {ArrayBuffers} The new ArrayBuffer created out of the two.
     */
    function appendBuffer(buffer1, buffer2) {
        var tmp = new Uint8Array(buffer1.byteLength + buffer2.byteLength);
        tmp.set(new Uint8Array(buffer1), 0);
        tmp.set(new Uint8Array(buffer2), buffer1.byteLength);
        return tmp.buffer;
    }

    function initAudioSource() {
        var audioSource = audioContext.createBufferSource();
        audioSource.buffer = audioBufferQueue.shift();
        audioSource.connect(audioContext.destination);
        audioSource.playbackRate.value = 1;
        audioSource.onended = function () {
            play();
        };
        return audioSource;
    }

    function play() {
        audioSource = initAudioSource();
        audioSource.start();
    }

    function loadChunk(index) {
        request.open("GET", dict.contextPath + "/files/" + track.id + "/" + index, true);
        request.send();
    }

    function onChunkLoaded() {
        const startPlaying = !audioContext;
        if (startPlaying) {
            audioContext = new AudioContext();
        }

        if (request.response.byteLength <= 0) {
            return;
        }
        audioContext.decodeAudioData(request.response, function (buf) {
            audioBufferQueue.push(buf);
            if (startPlaying) {
                play();
            }
        });

        lastChunkId++;
        playingStrategy();
    }

    this.reset = function () {
        if (audioContext && audioContext.state !== "closed") {
            audioContext.close();
            audioContext = null;
        }
        stream.stop();
        audioBufferQueue = [];
    };

    this.playChunk = function (trackToPlay, partId) {
        if (audioContext && audioContext.state === "suspended") {
            audioContext.resume();
        }
        playingStrategy = singlePlay;
        track = trackToPlay;
        loadChunk(partId);
    };

    this.play = function (trackToPlay, partId) {
        playingStrategy = sequencePlay;
        lastChunkId = partId;
        self.reset();
        track = trackToPlay;
        loadChunk(lastChunkId);
    };

    this.isPlaying = function () {
        return audioContext && audioContext.state === "running";
    };

    this.pause = function () {
        if (self.isPlaying()) {
            audioContext.suspend();
        }
        stream.stop();
    };

    this.resume = function () {
        if (!self.isPlaying()) {
            if (audioContext && audioContext.state === "suspended") {
                audioContext.resume();
            }
            loadChunk(lastChunkId);
        }
    };
}