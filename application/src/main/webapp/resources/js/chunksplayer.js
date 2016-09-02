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

    var audioBufferQueue;

    var paused = false;

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

    function isFullyLoaded() {
        return activeBuffer && activeBuffer.byteLength >= track.size;
    }

    function loadChunk(index) {
        if (isFullyLoaded()) {
            return;
        }
        request.open("GET", dict.contextPath + "/files/" + track.id + "/" + index, true);
        request.send();
    }

    function onChunkLoaded() {
        if (totalChunksLoaded === 0) {
            audioContext = new AudioContext();
            activeBuffer = request.response;
        } else {
            activeBuffer = appendBuffer(activeBuffer, request.response);
        }

        const startPlaying = (totalChunksLoaded === 0);
        audioContext.decodeAudioData(request.response, function (buf) {
            audioBufferQueue.push(buf);
            if (startPlaying) {
                play();
            }
        });

        totalChunksLoaded++;
        setTimeout(function () {
            if (!paused) {
                loadChunk(totalChunksLoaded);
            }
        }, 15000);
    }

    this.play = function (trackToPlay) {
        totalChunksLoaded = 0;
        audioBufferQueue = [];
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