"use strict";

function ChunksPlayer() {

    /**
     * The ArrayBuffer that will have the new chunks appended
     *
     * @private
     * @type {ArrayBuffer}
     */    
    var activeBuffer;

    var totalChunksLoaded = 0;

    var track;

    var audioContext;

    var audioBuffer;

    var paused = false;

    /**
     * The audio source is responsible for playing the music
     *
     * @private
     * @type {AudioBufferSourceNode}
     */
    var audioSource;

    var analyser;

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

    function initializeWebAudio() {
        audioContext = new AudioContext();
        analyser = audioContext.createAnalyser();
        analyser.fftSize = 2048;
    }

    function initAudioSource() {
        var audioSource = audioContext.createBufferSource();
        audioSource.buffer = audioBuffer;
        audioSource.connect(analyser);
        audioSource.connect(audioContext.destination);
        audioSource.playbackRate.value = 1;
        return audioSource;
    }

    /**
     * Plays the music from the point that it currently is.
     */
    function play() {
        try {
            audioSource.stop();
        } catch (e) {
            console.log(e);
        }
        audioSource = initAudioSource();
        var currentTime = audioContext.currentTime || 0;

        audioSource.start(0, currentTime, audioBuffer.duration - currentTime);
    }

    function isFullyLoaded() {
        return activeBuffer && activeBuffer.byteLength >= track.size;
    }

    function loadChunk(index) {
        if (isFullyLoaded()) {
            return;
        }
        request.open("GET", "files/" + track.id + "/" + index, true);
        request.send();
    }

    function onChunkLoaded() {
        if (totalChunksLoaded === 0) {
            initializeWebAudio();
            activeBuffer = request.response;
        } else {
            activeBuffer = appendBuffer(activeBuffer, request.response);
        }

        audioContext.decodeAudioData(activeBuffer, function (buf) {
            audioBuffer = buf;
            play();
        });

        totalChunksLoaded++;
        setTimeout(function () {
            if (!paused) {
                loadChunk(totalChunksLoaded);
            }
        }, 10000);
    }

    this.play = function (trackToPlay) {
        totalChunksLoaded = 0;
        audioBuffer = {};
        track = trackToPlay;
        loadChunk(totalChunksLoaded);
    };

    this.pause = function () {
        if (audioContext.state === "running") {
            audioContext.suspend();
        }
        paused = true;
    };

    this.resume = function () {
        if (totalChunksLoaded > 0 && paused) {
            if (audioContext.state === "suspended") {
                audioContext.resume();
            }
            paused = false;
            loadChunk(totalChunksLoaded);
        }
    };
}