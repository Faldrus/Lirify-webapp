document.addEventListener('DOMContentLoaded', () => {
    const BASE_PATH = "http://localhost:8080/ranamelone-1.0.0/rest/";
    let url = new URL(window.location.href);
    let ID = url.searchParams.get("id");

    function checkUserCookie() {
        let token = null;
        const searchTerm = "authToken=user-"
        document.cookie.split(';').forEach(cookie => {
            if (cookie.trim().startsWith(searchTerm)) {
                token = cookie.trim().substring(searchTerm.length);
            }
        });
        return token;
    }


   //Artist
    fetch(BASE_PATH + `artists/${ID}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json'}
    }).then(response => {
        if(!response.ok) {
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        console.log(data);
        document.getElementById('artist-name').textContent = data['Artists']['name'];
        document.getElementById('artist-bio').textContent = data['Artists']['biography'];

        document.getElementById('artist-pic').src = data['Artists']['image'] !== "NOT FOUND" ? data['Artists']['image'] : "data:image/jpeg;base64,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";
    })

    //Songs
    fetch(BASE_PATH + `songs/artists/${ID}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json'}
    }).then(response => {
        if(!response.ok) {
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        data = data['resource-list'];
        console.log(data);
        let container = document.getElementById("songs-list");
        data.forEach((Songs) => {
            Songs = Songs['Songs'];
            let card = document.createElement("div");
            card.classList.add("card");

            let title = document.createElement("a");
            title.setAttribute("href", `../html/song.html?id=${Songs['id']}`);
            title.textContent = Songs['title'];
            card.appendChild(title);

            let duration = document.createElement("p");
            duration.textContent = Songs['duration'];
            card.appendChild(duration);

            let genre = document.createElement("p");
            genre.textContent = Songs['genre'];
            card.appendChild(genre);

            let ytbutton = document.createElement("button");
            ytbutton.textContent = 'Youtube';
            ytbutton.addEventListener('click', function(){
                window.location.href = `${Songs['youtubeLink']}`;
            });
            card.appendChild(ytbutton);

            let likebutton = document.createElement("button");
            likebutton.textContent = "Like";
            const token = checkUserCookie();
            let SID = Songs['id'];
            likebutton.id = SID;

            function listenerlike() {
                fetch(BASE_PATH + `users/${token}/songs/${likebutton.id}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                }).then(rsp => {
                    if (!rsp.ok) {
                        rsp.json().then(error => console.error("ERROR:", error));
                        throw new Error(`HTTP ${rsp.status} ${rsp.statusText}`);
                    }
                }).then(() => {
                    console.log("Action: song liked\n");
                    likebutton.textContent = "Unlike";
                }).catch(err => console.error(err));

                likebutton.removeEventListener('click', listenerlike);
                likebutton.addEventListener('click', listenerunlike);
            }

            function listenerunlike() {
                fetch(BASE_PATH + `users/${token}/songs/${likebutton.id}`, {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json' }
                }).then(rsp => {
                    if (!rsp.ok) {
                        rsp.json().then(error => console.error("ERROR:", error));
                        throw new Error(`HTTP ${rsp.status} ${rsp.statusText}`);
                    }
                }).then(() => {
                    console.log("Action: song unliked\n");
                    likebutton.textContent = "Like";
                }).catch(err => console.error(err));

                likebutton.removeEventListener('click', listenerunlike);
                likebutton.addEventListener('click', listenerlike);
            }

            if (token === null) { /** No User logged in => click: login.html **/
            likebutton.addEventListener('click', () => { window.location.href = "login.html"; });
            } else { /** User logged in => check if song was liked or not. **/
            fetch(BASE_PATH + `users/${token}/songs`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            }).then(response => {
                if (!response.ok) {
                    response.json().then(error => console.error("ERROR:", error));
                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                }
                return response.json();
            }).then(data => {
                let wasliked = false;
                data = data['resource-list'];
                data.forEach(song => {
                    if (song['Songs']['id'].toString() === likebutton.id) { wasliked = true; } });
                console.log("Debug: song liked?\n", wasliked);

                if (wasliked) { /** Song was liked => Unlike behavior. **/
                likebutton.textContent = "Unlike";
                    likebutton.addEventListener('click', listenerunlike);
                } else { /** Song was not liked => Like behavior. **/
                likebutton.textContent = "Like";
                    likebutton.addEventListener('click', listenerlike);
                }
            }).catch(error => console.error(error));
            }
            card.appendChild(likebutton);

            let addbutton = document.createElement("button");
            let modal = document.getElementById("modal");

            window.onclick = function(event) {
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }

            addbutton.textContent = 'Add';
            addbutton.addEventListener('click', function(){
                let token = checkUserCookie();
                if (token === null) { window.location.href = "login.html"; }

                modal.style.display = "block";

                const list = document.getElementById('list')
                list.innerHTML = '' ;

                //Users Playlist fetch
                fetch(BASE_PATH + `playlists/users/${token}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' }
                }).then(response => {
                    if (!response.ok) {
                        response.json().then(error => console.log("ERROR:", error));
                        throw new Error(`HTTP ${response.status} ${response.statusText}`);
                    }
                    return response.json();
                }).then(data => {
                    data = data['resource-list'];
                    console.log("LOGGED USER'S PLAYLISTS:", data);
                    data.forEach((playlist) => {
                        let option = document.createElement("button");
                        console.log(playlist);
                        option.value = playlist['Playlists']['id'];
                        option.textContent = playlist['Playlists']['title'];

                        option.addEventListener('click', () => {
                            let SID = Songs['id'];
                            const SINP = {
                                SongsInPlaylist:{
                                    songID: SID,
                                    playlistID: option.value
                                }
                            }

                            fetch(BASE_PATH + `songs/${SID}/playlists/${option.value}`, {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                                body: JSON.stringify(SINP)
                            }).then(response => {
                                if (!response.ok) {
                                    response.json().then(error => console.log("ERROR:", error));
                                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                                }
                            }).then(() => {
                                console.log("ADDED");
                            }).catch(error => console.log(error));
                            modal.style.display = "none";
                        });

                        list.appendChild(option);
                    });
                }).catch(error => console.log(error));

            });

            card.appendChild(addbutton);

            container.appendChild(card);
        })
    }).catch(error => console.log(error));

    //Albums
    fetch(BASE_PATH + `albums/artists/${ID}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json'}
    }).then(response => {
        if(!response.ok) {
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        data = data['resource-list'];
        console.log(data);
        let container = document.getElementById("albums-list");
        data.forEach((Albums) => {
            Albums = Albums['Albums'];
            let card = document.createElement("div");
            card.classList.add("card");

            let title = document.createElement("a");
            title.setAttribute("href", `../html/album.html?id=${Albums['id']}`);
            title.textContent = Albums['title'];
            card.appendChild(title);
            container.appendChild(card);


        }).catch(error => console.log(error));
    }).catch(error => console.log(error));

});