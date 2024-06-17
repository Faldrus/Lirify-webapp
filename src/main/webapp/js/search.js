document.addEventListener('DOMContentLoaded', () => {
    const BASE_PATH = "http://localhost:8080/ranamelone-1.0.0/rest/";
    let url = new URL(window.location.href);
    let SEARCH = url.searchParams.get("q");
    console.log(SEARCH);

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

    const results = document.getElementById("results");
    results.textContent = `Search results for : ${SEARCH}`;
    SEARCH = SEARCH.split(' ').join('.*');
    var empty = true;

    //Artists
    const artistsfetch = fetch(BASE_PATH + `artists/search/${SEARCH}`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json'}
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        data = data['resource-list'];
        console.log(data);
        let container = document.getElementById("artists-search");


        if (data.length > 0){
            let title = document.createElement("h2");
            empty = false;
            title.textContent = "Artists";
            title.className = "title"
            container.appendChild(title);

        }

        data.forEach((Artists) => {
            Artists = Artists['Artists'];
            let card = document.createElement("div");
            card.classList.add("card");

            let title = document.createElement("a");
            title.setAttribute("href", `../html/artist.html?id=${Artists['id']}`);
            title.textContent = Artists['name'];
            card.appendChild(title);
            container.appendChild(card);
        }).catch(error => console.log(error));

    }).catch(error => console.log(error));


    //ALBUMS
    const albumsfetch = fetch(BASE_PATH + `albums/search/${SEARCH}`, {
        method: 'GET',
        headers: {'Content-Type': 'application/json'}
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        data = data['resource-list'];
        console.log(data);
        let container = document.getElementById("albums-search");
        if (data.length > 0){
            let title = document.createElement("h2");
            empty = false;
            title.textContent = "Albums";
            title.className = "title";
            container.appendChild(title);
        }
        data.forEach((Albums) => {
            Albums = Albums['Albums'];
            let card = document.createElement("div");
            card.classList.add("card");

            let title = document.createElement("a");
            title.setAttribute("href", `../html/album.html?id=${Albums['id']}`);
            title.textContent = Albums['title'];
            card.appendChild(title);

            let artist = document.createElement("a");
            artist.style.textDecoration = "none";
            fetch(BASE_PATH + `artists/${Albums["artistId"]}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json'}
            }).then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                }
                return response.json();
            }).then(adata => {
                artist.textContent = "by " + adata['Artists']['name'];
                artist.setAttribute("href", `artist.html?id=${Albums['artistId']}`);
            }).catch(error => { console.error(error); });
            card.appendChild(artist);


            container.appendChild(card);
        }).catch(error => console.log(error));


    }).catch(error => console.log(error));


    //Songs
    const songsfetch = fetch(BASE_PATH + `songs/search/${SEARCH}`, {
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
        let container = document.getElementById("songs-search");
        if (data.length > 0){
            let title = document.createElement("h2");
            empty = false;
            title.textContent = "Songs";
            title.className = "title";
            container.appendChild(title);
        }
        data.forEach((Songs) => {
            Songs = Songs['Songs'];
            let card = document.createElement("div");
            card.classList.add("card");

            let title = document.createElement("a");
            title.id = "title";
            title.setAttribute("href", `../html/song.html?id=${Songs['id']}`);
            title.textContent = Songs['title'];
            card.appendChild(title);

            let artist = document.createElement("a");
            artist.style.textDecoration = "none";
            fetch(BASE_PATH + `artists/${Songs["artistId"]}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json'}
            }).then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                }
                return response.json();
            }).then(adata => {
                artist.textContent = "by " + adata['Artists']['name'];
                artist.setAttribute("href", `artist.html?id=${Songs['artistId']}`);
            }).catch(error => { console.error(error); });
            card.appendChild(artist);

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

                console.log('yes')
            });

            card.appendChild(addbutton);

            container.appendChild(card);

        })
    })

    Promise.all([artistsfetch, albumsfetch, songsfetch]).then(() =>{
        if (empty === true){
            let title = document.createElement("h3");
            title.textContent = "No Results";
            let container = document.getElementById("artists-search");
            container.appendChild(title);
        }
    });

});

