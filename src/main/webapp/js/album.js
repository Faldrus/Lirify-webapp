document.addEventListener('DOMContentLoaded', () => {
    const PARAMS = new URLSearchParams(window.location.search);
    const ID = PARAMS.get('id');
    if (ID === null) { window.location.href = "homepage.html"; }

    /** INFO display about the Album. **/
    fetch(BASE_PATH + `albums/${ID}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json'}
    }).then(response => {
        if(!response.ok) {
            response.json().then(error => console.error("ERROR:", error));
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(async data => {
        if (!PRODUCTION) { console.log("DBG : album\n", data); }
        document.getElementById('album-name').textContent = data['Albums']['title'];
        document.getElementById('album-release').textContent = (new Date(data['Albums']['releaseDate'])).toLocaleDateString("en-US", { year: "numeric", month: "long", day: "numeric" });
        document.getElementById('album-genre').textContent = data['Albums']['genre'];

        try { /** Adding behavior to the Artist's name link. **/
            const response = await fetch(BASE_PATH + `artists/${data['Albums']['artistId']}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });
            const artist = await response.json();
            if (!PRODUCTION) { console.log("DBG : album's artist\n", artist); }
            let artistA = document.getElementById("album-artist");
            artistA.textContent = "by " + artist['Artists']['name'];
            artistA.setAttribute("href", `artist.html?id=${data['Albums']['artistId']}`);
        } catch (error) { console.error(error); }

        /** DELETE behavior if Artist login (owner). HERE because data['Albums']['artistId'] is needed. **/
        const aToken = checkArtistCookie();
        if (aToken === data['Albums']['artistId'].toString()) { /** Add 'Delete' button. **/
            if (!PRODUCTION) { console.log("DBG : current login allowed to perform deletion"); }
            let deleteButton = document.createElement("button");
            deleteButton.id = "delete";
            deleteButton.textContent = "Delete";
            deleteButton.type = "button";
            function warnListener() { /** Warning. **/
                deleteButton.textContent = "Confirm?";
                if (!PRODUCTION) { console.log("ACT : confirmation required"); }

                deleteButton.removeEventListener('click', warnListener);
                deleteButton.addEventListener('click', confirmListener);
            }
            function confirmListener() { /** Actual deletion. **/
                fetch(BASE_PATH + `albums/${ID}`, {
                    method: 'DELETE',
                    headers: {'Content-Type': 'application/json'}
                }).then(response =>{
                    if (!response.ok) {
                        response.json().then(err => console.error("ERROR:", err));
                        throw new Error(`HTTP ${response.status} ${response.statusText}`);
                    }
                    return response.json();
                }).then(dt => {
                    if (!PRODUCTION) { console.log("ACT : album deleted\n", dt); }
                    deleteButton.style.color = "var(--md-sys-color-primary)";
                    window.location.href = "homepage.html";
                }).catch(err => { console.error(err); });

                deleteButton.removeEventListener('click', confirmListener);
            }
            /** Set button to warn at first, then actually delete. **/
            deleteButton.addEventListener('click', warnListener);
            document.getElementById('album-actions').appendChild(deleteButton);
        }
    }).catch(error => console.error(error));

    /** LIKE & UNLIKE dynamic button. **/
    const uToken = checkUserCookie();
    let likeButton = document.getElementById("like");
    function listenerUnlike() { /** Unlike behavior. **/
        fetch(BASE_PATH + `users/${uToken}/albums/${ID}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(rsp => {
            if (!rsp.ok) {
                rsp.json().then(error => console.error("ERROR:", error));
                throw new Error(`HTTP ${rsp.status} ${rsp.statusText}`);
            }
        }).then(() => {
            if (!PRODUCTION) { console.log("ACT : album unliked"); }
            likeButton.textContent = "Like";
        }).catch(err => console.error(err));

        likeButton.removeEventListener('click', listenerUnlike);
        likeButton.addEventListener('click', listenerLike);
    }
    function listenerLike() { /** Like behavior. **/
        fetch(BASE_PATH + `users/${uToken}/albums/${ID}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        }).then(rsp => {
            if (!rsp.ok) {
                rsp.json().then(error => console.error("ERROR:", error));
                throw new Error(`HTTP ${rsp.status} ${rsp.statusText}`);
            }
        }).then(() => {
            if (!PRODUCTION) { console.log("ACT : album liked"); }
            likeButton.textContent = "Unlike";
        }).catch(err => console.error(err));

        likeButton.removeEventListener('click', listenerLike);
        likeButton.addEventListener('click', listenerUnlike);
    }
    if (uToken === null) { /** No User logged in => click: login.html **/
        likeButton.addEventListener('click', () => { window.location.href = "login.html"; });
    } else { /** User logged in => check if album was liked or not. **/
        fetch(BASE_PATH + `users/${uToken}/albums`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        }).then(response => {
            if (!response.ok) {
                response.json().then(error => console.error("ERROR:", error));
                throw new Error(`HTTP ${response.status} ${response.statusText}`);
            }
            return response.json();
        }).then(data => {
            let wasLiked = false;
            data = data['resource-list'];
            data.forEach(album => { if (album['Albums']['id'].toString() === ID) { wasLiked = true; } });
            if (!PRODUCTION) { console.log("DBG : album already liked?\n", wasLiked); }

            if (wasLiked) { /** Album was liked => Unlike behavior. **/
                likeButton.textContent = "Unlike";
                likeButton.addEventListener('click', listenerUnlike);
            } else { /** Album was not liked => Like behavior. **/
                likeButton.textContent = "Like";
                likeButton.addEventListener('click', listenerLike);
            }
        }).catch(error => console.error(error));
    }

    /** DISPLAY list of Songs in Album. **/
    fetch(BASE_PATH + `songs/albums/${ID}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    }).then(response => {
        if (!response.ok) {
            response.json().then(error => console.error("ERROR:", error));
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        if (!PRODUCTION) { console.log("DBG : album's songs\n", data); }
        data = data['resource-list'];
        document.getElementById('album-capacity').textContent = Object.keys(data).length + " Song(s)";
        data.forEach(song => {
            let card = document.createElement("div");
            card.classList.add("card");

            let songName = document.createElement("a");
            songName.href = `song.html?id=${song['Songs']['id']}`;
            songName.textContent = song['Songs']['title'];
            card.appendChild(songName);

            let cardActions = document.createElement("div");
            cardActions.classList.add("card-actions");

            let releaseDate = document.createElement("p");
            releaseDate.textContent = (new Date(song['Songs']['releaseDate'])).toLocaleDateString("en-US", { year: "numeric", month: "long", day: "numeric" });
            cardActions.appendChild(releaseDate);
            let duration = document.createElement("p");
            duration.textContent = song['Songs']['duration'];
            cardActions.appendChild(duration);
            let genre = document.createElement("p");
            genre.textContent = song['Songs']['genre'];
            cardActions.appendChild(genre);

            let songLike = document.createElement("button");
            songLike.textContent = "Like";
            songLike.type = "button";
            function listenerSongUnlike() { /** Unlike behavior. **/
                fetch(BASE_PATH + `users/${uToken}/songs/${song['Songs']['id']}`, {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json' }
                }).then(rsp => {
                    if (!rsp.ok) {
                        rsp.json().then(err => console.error("ERROR:", err));
                        throw new Error(`HTTP ${rsp.status} ${rsp.statusText}`);
                    }
                }).then(() => {
                    if (!PRODUCTION) { console.log("ACT : song unliked\n"); }
                    songLike.textContent = "Like";
                }).catch(err => console.error(err));

                songLike.removeEventListener('click', listenerSongUnlike);
                songLike.addEventListener('click', listenerSongLike);
            }
            function listenerSongLike() { /** Like behavior. **/
                fetch(BASE_PATH + `users/${uToken}/songs/${song['Songs']['id']}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                }).then(rsp => {
                    if (!rsp.ok) {
                        rsp.json().then(err => console.error("ERROR:", err));
                        throw new Error(`HTTP ${rsp.status} ${rsp.statusText}`);
                    }
                }).then(() => {
                    if (!PRODUCTION) { console.log("ACT : song liked\n"); }
                    songLike.textContent = "Unlike";
                }).catch(err => console.error(err));

                songLike.removeEventListener('click', listenerSongLike);
                songLike.addEventListener('click', listenerSongUnlike);
            }
            if (uToken === null) { /** No User logged in => click: login.html **/
                songLike.addEventListener('click', () => { window.location.href = "login.html"; });
            } else { /** User logged in => check if song was liked or not. **/
                fetch(BASE_PATH + `users/${uToken}/songs`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' }
                }).then(rsp => {
                    if (!rsp.ok) {
                        rsp.json().then(err => console.error("ERROR:", err));
                        throw new Error(`HTTP ${rsp.status} ${rsp.statusText}`);
                    }
                    return rsp.json();
                }).then(dt => {
                    let wasLiked = false;
                    dt = dt['resource-list'];
                    dt.forEach(userLikedSong => { if (userLikedSong['Songs']['id'] === song['Songs']['id']) { wasLiked = true; } });

                    if (wasLiked) { /** Song was liked => Unlike behavior. **/
                        songLike.textContent = "Unlike";
                        songLike.addEventListener('click', listenerSongUnlike);
                    } else { /** Song was not liked => Like behavior. **/
                        songLike.textContent = "Like";
                        songLike.addEventListener('click', listenerSongLike);
                    }
                }).catch(err => console.error(err));
            }
            cardActions.appendChild(songLike);

            card.appendChild(cardActions);
            document.getElementById("album-songs").appendChild(card);
        });
    }).catch(error => console.error(error));
});