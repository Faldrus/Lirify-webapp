document.addEventListener('DOMContentLoaded', () => {
    const BASE_PATH = "http://localhost:8080/ranamelone-1.0.0/rest/";
    let url = new URL(window.location.href);
    let ID = url.searchParams.get("id");

    function isArtist() {
        let artist = false;
        const searchTerm = "authToken=artist-";
        document.cookie.split(';').forEach(cookie => {
            if (cookie.trim().startsWith(searchTerm)) {
                artist = true;
            }
        });
        return artist;
    }

    if (isArtist()) {
        const CONFIRM = document.getElementById('confirm');

        CONFIRM.addEventListener('click', (event) => {
            event.preventDefault();

            const songName = document.getElementById('song-name').value;
            const songLink = document.getElementById('song-youtube-link').value;
            const releaseDate = document.getElementById('release-date').value;
            const songGenre = document.getElementById('genre').value;
            const songDuration = document.getElementById('duration').value;
            const songLyrics = document.getElementById('song-lyrics').value;

            console.log("Song Name:", songName);
            console.log("Song Link:", songLink);
            console.log("Release Date:", releaseDate);
            console.log("Song Genre:", songGenre);
            console.log("Song Duration:", songDuration);
            console.log("Song Lyrics:", songLyrics);

            const artistID = checkArtistCookie();

            const SONG = {
                Songs: {
                    title: songName,
                    youtubeLink: songLink,
                    releaseDate: releaseDate,
                    lyrics: songLyrics,
                    genre: songGenre,
                    duration: songDuration,
                    artistId: artistID
                }
            };

            console.log("Artist ID:", artistID);
            console.log("SONG JSON:", JSON.stringify(SONG));

            fetch(BASE_PATH + `songs/`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(SONG)
            }).then(response => {
                if (!response.ok) {
                    response.json().then(error => console.log("ERROR:", error));
                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                }
            }).then(() => {
                console.log("SONG CREATED");
                window.location.href = "homepage.html";
            }).catch(error => console.log(error));
        });
    }
});