document.addEventListener('DOMContentLoaded', () => {
    const BASE_PATH = "http://localhost:8080/ranamelone-1.0.0/rest/";
    let url = new URL(window.location.href);
    let ID = url.searchParams.get("id");
    let songArray = [];

    const addSongButton = document.getElementById("add-song-button");

    const modal = document.getElementById('modal');
    const modal2 = document.getElementById('modal2');
    const confirmButton = document.getElementById('confirm');
    const uploadAlbumButton = document.getElementById('upload-album-button');
    const albumConfirmButton = document.getElementById('album-confirm');
    const token = checkArtistCookie();

    addSongButton.addEventListener("click", function () {
        modal.style.display = "block";
    });

    confirmButton.addEventListener("click", function () {
        const songName = document.getElementById('song-name').value;
        const songLink = document.getElementById('song-youtube-link').value;
        const releaseDate = document.getElementById('release-date').value;
        const songGenre = document.getElementById('genre').value;
        const songDuration = document.getElementById('duration').value;
        const songLyrics = document.getElementById('song-lyrics').value;

        songArray.push([songName, songLink, releaseDate, songGenre, songDuration, songLyrics]);
        const songIndex = songArray.length - 1;

        modal.style.display = "none";

        let songContainer = document.getElementById('song-container');

        let songCard =  document.createElement('div');
        songCard.classList.add('card');

        let cardTitle = document.createElement('h3');
        cardTitle.textContent = songName;
        songCard.appendChild(cardTitle);

        let cardDelete = document.createElement('button');
        cardDelete.type = 'button';
        cardDelete.textContent = 'x';
        cardDelete.classList.add('card-delete');

        cardDelete.addEventListener("click", function () {
            songCard.remove();
            songArray[songIndex] = null;
        })

        songCard.appendChild(cardDelete);
        songContainer.appendChild(songCard);

        document.getElementById('song-name').value = '';
        document.getElementById('song-youtube-link').value = '';
        document.getElementById('release-date').value = '';
        document.getElementById('genre').value = '';
        document.getElementById('duration').value = '00:00:00';
        document.getElementById('song-lyrics').value = '';
    });

    uploadAlbumButton.addEventListener("click", function () {
        modal2.style.display = "block";
    })

    albumConfirmButton.addEventListener("click", function () {
        const albumName = document.getElementById('album-name').value;
        const albumReleaseDate = document.getElementById('album-release-date').value;
        const albumGenre = document.getElementById('album-genre').value;

        const ALBUM = {
            Albums: {
                title: albumName,
                coverImage: null,
                releaseDate: albumReleaseDate,
                genre: albumGenre,
                artistId: token
            }
        }

        let albumID;
        const createAlbum = fetch(BASE_PATH + 'albums', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ALBUM)
        }).then(response => {
            if (!response.ok) {
                response.json().then(error => console.log("ERROR:", error));
                throw new Error(`HTTP ${response.status} ${response.statusText}`);
            }
            return response.json();
        }).then(data => {
            console.log(data);
            console.log("ALBUM CREATED");
            albumID = data['Albums']['id'];

            const jsonObjects = [];
            console.log(songArray);
            songArray.forEach(song => {
                if (song !== null) {
                    const SONG = {
                        Songs: {
                            title: song[0],
                            youtubeLink: song[1],
                            releaseDate: song[2],
                            lyrics: song[3],
                            genre: song[5],
                            duration: song[4],
                            artistId: token
                        }
                    };

                    jsonObjects.push(SONG);
                }
            });

            jsonObjects.forEach(SONG => {
                let songID;
                const createSong = fetch(BASE_PATH + `songs`, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(SONG)
                }).then(response => {
                    if (!response.ok) {
                        return response.json().then(error => {
                            console.log("ERROR:", error);
                            throw new Error(`HTTP ${response.status} ${response.statusText}`);
                        });
                    }
                    return response.json();
                }).then(data => {
                    console.log("SONG CREATED");
                    console.log(data);
                    songID = data['Songs']['id'];
                })

                Promise.all([createAlbum, createSong]).then(() => {
                    fetch(BASE_PATH + `songs/${songID}/albums/${albumID}`, {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'}
                    }).then(response => {
                        if (!response.ok) {
                            return response.json().then(error => {
                                console.log("ERROR:", error);
                                throw new Error(`HTTP ${response.status} ${response.statusText}`);
                            });
                        }
                    }).then(() => {
                        console.log("SONGS ADDED TO ALBUM");
                    })
                });


            })

        }).catch(error => console.log(error));

        //window.location.href = `albums/${albumID}.html`;
        modal2.style.display = "none";
    });

});