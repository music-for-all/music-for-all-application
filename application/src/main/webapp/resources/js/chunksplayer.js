"use strict";


function ChunksPlayer() {

    /**
     * The ArrayBuffer that will have the new chunks appended
     *
     * @private
     * @type {ArrayBuffer}
     */
    var activeBuffer;

    var lastChunkId;

    var initialChunkId = 0;

    var track;

    var audioContext;

    var audioBufferQueue;

    var paused = false;

    var stream = new Stream();

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
        if (lastChunkId === initialChunkId) {
            audioContext = new AudioContext();
            activeBuffer = request.response;
        } else {
            activeBuffer = appendBuffer(activeBuffer, request.response);
        }

        const startPlaying = (lastChunkId === initialChunkId);
        audioContext.decodeAudioData(request.response, function (buf) {
            audioBufferQueue.push(buf);
            if (startPlaying) {
                play();
            }
        });

        lastChunkId++;
        setTimeout(function () {
            if (!paused) {
                loadChunk(lastChunkId);
            }
        }, 15000);
    }

    function reset() {
        if (audioContext && audioContext.state !== "closed") {
            audioContext.close();
        }
        stream.stop();
        lastChunkId = initialChunkId;
        paused = false;
        audioBufferQueue = [];
    }

    this.playFrom = function (trackToPlay, partId) {
        initialChunkId = partId;
        reset();
        track = trackToPlay;
        loadChunk(lastChunkId);
        stream.start(track.id);
    };

    this.play = function (trackToPlay) {
        this.playFrom(trackToPlay, 0);
    };

    this.pause = function () {
        if (audioContext && audioContext.state === "running") {
            audioContext.suspend();
        }
        paused = true;
        stream.stop();
    };

    this.resume = function () {
        if (lastChunkId >= initialChunkId && paused) {
            if (audioContext && audioContext.state === "suspended") {
                audioContext.resume();
            }
            paused = false;
            loadChunk(lastChunkId);
            stream.start(track.id);
        }
    };
}