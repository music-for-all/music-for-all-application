var player = new MediaElementPlayer('audio', {
        audioWidth: 400,
        audioHeight: 30,
        pauseOtherPlayers: true,
        enableKeyboard: true,
        startVolume: 0.8
    });

function play(location) {
    player.setSrc(location);
    player.play();
}
     
