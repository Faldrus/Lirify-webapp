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

    let token = checkUserCookie();
    if (token === null) { window.location.href = "login.html"; }
    else if (token !== ID) {
        alert("Access Denied");
        window.location.href = "homepage.html";
    }

    //Create Playlist
    let modal = document.getElementById("modal");
    let btn = document.getElementById("create");
    let submitBtn = document.getElementById("submitBtn");

    btn.onclick = function() {
        modal.style.display = "block";
    }
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
        if (event.target == modal2) {
            modal2.style.display = "none";
        }
    }

    window.onload = function() {
        document.getElementById("form").addEventListener("keydown", function(event) {
            if (event.keyCode === 13) {
                event.preventDefault();

            }
        });
    };

    submitBtn.onclick = function() {
        var name = document.getElementById("nameInput").value;
        if (name.trim() === '') {return;}
        console.log(name);
        console.log(ID);
        if (name != null) {
            const PLAYLIST = {
                Playlists:{
                    title: name,
                    userId: ID
                }
            };
            fetch(BASE_PATH + "playlists", {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(PLAYLIST)
            }).then(response => {
                if (!response.ok) {
                    response.json().then(error => console.log("ERROR:", error));
                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                }
                return response.json();
            }).then(data => {
                console.log(data);
                console.log('gne')
                location.reload();
            }).catch(error => console.log(error));

        }
        modal.style.display = "none";
    }

    //Playlist
    fetch(BASE_PATH + `playlists/users/${ID}`, {
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
        let container = document.getElementById("playlist-list");
        data.forEach((Playlists) => {
            Playlists = Playlists['Playlists'];
            let card = document.createElement("div");
            card.classList.add("card");

            let title = document.createElement("a");
            title.setAttribute("href", `../html/playlist.html?id=${Playlists['id']}`);
            title.textContent = Playlists['title'];
            card.appendChild(title);
            container.appendChild(card);
        })
    })

    //Songs
    fetch(BASE_PATH + `users/${ID}/songs`, {
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
            title.id = "title";
            card.appendChild(title);

            let artist = document.createElement("a");
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

            let addbutton = document.createElement("button");
            var modal2 = document.getElementById("modal2");
            addbutton.onclick = function() {
                modal2.style.display = "block";
            }

            addbutton.textContent = 'Add';
            addbutton.addEventListener('click', function(){
                let token = checkUserCookie();
                if (token === null) { window.location.href = "login.html"; }

                const list = document.getElementById('list2')
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

                            fetch(BASE_PATH + `songs/${SID}/playlists/${option.value}`, {
                                method: 'POST',
                                headers: { 'Content-Type': 'application/json' },
                            }).then(response => {
                                if (!response.ok) {
                                    response.json().then(error => console.log("ERROR:", error));
                                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                                }
                            }).then(() => {
                                console.log("ADDED");
                            }).catch(error => console.log(error));
                            modal2.style.display = "none";
                        });

                        list.appendChild(option);
                    });
                }).catch(error => console.log(error));
            });

            card.appendChild(addbutton);

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

            container.appendChild(card);

        })
    })

    //Albums
    fetch(BASE_PATH + `users/${ID}/albums`, {
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


        })
    })

});