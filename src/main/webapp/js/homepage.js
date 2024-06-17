document.addEventListener('DOMContentLoaded', () => {
    const COLS = 5;
    const NUM_ALBUM = COLS;
    const NUM_SONG = COLS * 3;

    /** DISPLAY Albums behavior. **/
    fetch(BASE_PATH + `albums/pickalgorithm/${NUM_ALBUM}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    }).then(response => {
        if (!response.ok) {
            response.json().then(error => console.error("ERROR:", error));
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        data = data['resource-list'];
        if (!PRODUCTION) { console.log("DBG : albums picked\n", data); }

        let container = document.getElementById("album-algorithm");
        data.forEach((description) => {
            description = description['AlbumDescription'];

            let card = document.createElement("div");
            card.classList.add("card");

            let title = document.createElement("a");
            title.setAttribute("href", `album.html?id=${description['album']['id']}`);
            title.textContent = description['album']['title'];
            card.appendChild(title);

            let artist = document.createElement("a");
            artist.setAttribute("href", `artist.html?id=${description['artist']['id']}`);
            artist.textContent = "by " + description['artist']['name'];
            card.appendChild(artist);

            container.appendChild(card);
        });
    }).catch(error => console.error(error));

    /** DISPLAY Songs behavior. **/
    fetch(BASE_PATH + `songs/pickalgorithm/${NUM_SONG}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    }).then(response => {
        if (!response.ok) {
            response.json().then(error => console.error("ERROR:", error));
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        data = data['resource-list'];
        if (!PRODUCTION) { console.log("DBG : songs picked\n", data); }
        let container = document.getElementById("song-algorithm");
        data.forEach((description) => {
            description = description['SongDescription'];

            let card = document.createElement("div");
            card.classList.add("card");

            let title = document.createElement("a");
            title.setAttribute("href", `song.html?id=${description['song']['id']}`);
            title.textContent = description['song']['title'];
            card.appendChild(title);

            let artist = document.createElement("a");
            artist.setAttribute("href", `artist.html?id=${description['artist']['id']}`);
            artist.textContent = "by " + description['artist']['name'];
            card.appendChild(artist);

            container.appendChild(card);
        });
    }).catch(error => console.error(error));
});