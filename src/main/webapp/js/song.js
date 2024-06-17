document.addEventListener('DOMContentLoaded', () => {
    const PARAMS = new URLSearchParams(window.location.search);
    const ID = PARAMS.get('id');
    if (ID === null) { window.location.href = "homepage.html"; }

    /** INFO display about the Song. **/
    fetch(BASE_PATH + `songs/${ID}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    }).then(response => {
        if (!response.ok) {
            response.json().then(error => console.error("ERROR:", error));
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(async data => {
        console.log("Debug: song\n", data);
        document.getElementById("song-name").textContent = data['Songs']['title'];
        document.getElementById("song-release").textContent = (new Date(data['Songs']['releaseDate'])).toLocaleDateString("en-US", { year: "numeric", month: "long", day: "numeric" });
        document.getElementById("song-duration").textContent = data['Songs']['duration'];
        document.getElementById("song-genre").textContent = data['Songs']['genre'];
        document.querySelector("#song-lyrics > p").innerText = data['Songs']['lyrics'].replace(/\\n/g, "<br>");
        document.getElementById("song-link").src = data['Songs']['youtubeLink'];

        try { /** Adding behavior to the Artist's name link. **/
            const response = await fetch(BASE_PATH + `artists/${data['Songs']['artistId']}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });
            const artist = await response.json();
            if (!PRODUCTION) { console.log("DBG : song's artist\n", artist); }
            let artistA = document.getElementById("song-artist");
            artistA.textContent = "by " + artist['Artists']['name'];
            artistA.setAttribute("href", `artist.html?id=${data['Songs']['artistId']}`);
        } catch (error) { console.error(error); }

        /** DELETE behavior if Artist login (owner). HERE because data['Songs']['artistId'] is needed. **/
        const aToken = checkArtistCookie();
        if (aToken === data['Songs']['artistId'].toString()) { /** Add 'Delete' button. **/
            console.log("Debug: current login allowed to perform deletion");
            let deleteButton = document.createElement("button");
            deleteButton.id = "album-delete";
            deleteButton.textContent = "Delete";
            deleteButton.type = "button";
            function warnListener() { /** Warning. **/
                deleteButton.textContent = "Confirm?";
                if (!PRODUCTION) { console.log("ACT : confirmation required"); }

                deleteButton.removeEventListener('click', warnListener);
                deleteButton.addEventListener('click', confirmListener);
            }
            function confirmListener() { /** Actual deletion. **/
                fetch(BASE_PATH + `songs/${ID}`, {
                    method: 'DELETE',
                    headers: {'Content-Type': 'application/json'}
                }).then(response =>{
                    if (!response.ok) {
                        response.json().then(err => console.error("ERROR:", err));
                        throw new Error(`HTTP ${response.status} ${response.statusText}`);
                    }
                    return response.json();
                }).then(dt => {
                    if (!PRODUCTION) { console.log("ACT : song deleted\n", dt); }
                    deleteButton.style.color = "var(--md-sys-color-primary)";
                    window.location.href = "homepage.html";
                }).catch(err => { console.error(err); });

                deleteButton.removeEventListener('click', confirmListener);
            }
            /** Set button to warn at first, then actually delete. **/
            deleteButton.addEventListener('click', warnListener);
            document.getElementById('song-actions').appendChild(deleteButton);
        }
    }).catch(error => { console.error(error); });

    /** LIKE & UNLIKE dynamic button. **/
    const uToken = checkUserCookie();
    let likeButton = document.getElementById("like");
    function listenerUnlike() { /** Unlike behavior. **/
        fetch(BASE_PATH + `users/${uToken}/songs/${ID}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(rsp => {
            if (!rsp.ok) {
                rsp.json().then(error => console.error("ERROR:", error));
                throw new Error(`HTTP ${rsp.status} ${rsp.statusText}`);
            }
        }).then(() => {
            if (!PRODUCTION) { console.log("ACT : song unliked\n"); }
            likeButton.textContent = "Like";
        }).catch(err => console.error(err));

        likeButton.removeEventListener('click', listenerUnlike);
        likeButton.addEventListener('click', listenerLike);
    }
    function listenerLike() { /** Like behavior. **/
        fetch(BASE_PATH + `users/${uToken}/songs/${ID}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        }).then(rsp => {
            if (!rsp.ok) {
                rsp.json().then(error => console.error("ERROR:", error));
                throw new Error(`HTTP ${rsp.status} ${rsp.statusText}`);
            }
        }).then(() => {
            if (!PRODUCTION) { console.log("ACT : song liked\n"); }
            likeButton.textContent = "Unlike";
        }).catch(err => console.error(err));

        likeButton.removeEventListener('click', listenerLike);
        likeButton.addEventListener('click', listenerUnlike);
    }
    if (uToken === null) { /** No User logged in => click: login.html **/
        likeButton.addEventListener('click', () => { window.location.href = "login.html"; });
    } else { /** User logged in => check if song was liked or not. **/
        fetch(BASE_PATH + `users/${uToken}/songs`, {
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
            data.forEach(song => { if (song['Songs']['id'].toString() === ID) { wasLiked = true; } });
            if (!PRODUCTION) { console.log("DBG : song already liked?\n", wasLiked); }

            if (wasLiked) { /** Song was liked => Unlike behavior. **/
                likeButton.textContent = "Unlike";
                likeButton.addEventListener('click', listenerUnlike);
            } else { /** Song was not liked => Like behavior. **/
                likeButton.textContent = "Like";
                likeButton.addEventListener('click', listenerLike);
            }
        }).catch(error => console.error(error));
    }

    /** ADD button dynamic menu behavior. **/
    document.getElementById("add").addEventListener('click', (event) => {
        let token = checkUserCookie();
        if (token === null) {
            window.location.href = "login.html";
            document.getElementById("add").removeEventListener('click', arguments.callee);
        }

        let mainElement = document.querySelector("main");
        if (mainElement.children.length === 4) { /** Menu status: ON => remove it. **/
            mainElement.removeChild(document.getElementById("adding"));
        } else { /** Menu status: OFF => create it. **/
            let adding = document.createElement("div");
            adding.id = "adding";

            /** Form: add to existing playlist **/
            let songPlaylists = document.createElement("form");
            songPlaylists.id = "song-playlists";

            let label = document.createElement("label");
            label.setAttribute("for", "dropdown");
            label.textContent = "ADD";
            songPlaylists.appendChild(label);

            let dropdown = document.createElement("select");
            dropdown.setAttribute("id", "dropdown");
            dropdown.name = "dropdown";
            dropdown.required = true;

            let optionExample = document.createElement("option");
            optionExample.value = "";
            optionExample.textContent = "- Your Playlists -";
            dropdown.appendChild(optionExample);

            /** Fetch user's playlist to insert in the dropdown menu. **/
            fetch(BASE_PATH + `playlists/users/${token}`, {
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
                if (!PRODUCTION) { console.log("DBG : user's playlists\n", data); }
                data.forEach((playlist) => {
                    let option = document.createElement("option");
                    option.value = playlist['Playlists']['id'];
                    option.textContent = playlist['Playlists']['title'];
                    dropdown.appendChild(option);
                });
            }).catch(error => console.error(error));
            songPlaylists.appendChild(dropdown);

            let songSend = document.createElement("button");
            songSend.id = "song-send";
            songSend.textContent = "+";
            songSend.type = "button";
            /** Send form to add song to selected playlist. **/
            songSend.addEventListener('click', (event) => {
                const FORM = document.getElementById("song-playlists");
                if (!FORM.checkValidity()) {
                    FORM.reportValidity();
                    return;
                }
                const DATA = new FormData(FORM);
                fetch(BASE_PATH + `songs/${ID}/playlists/${DATA.get("dropdown")}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' }
                }).then(response => {
                    if (!response.ok) {
                        document.getElementById("song-send").style.color = "var(--md-sys-color-error-container)";
                        document.getElementById("song-send").style.fontWeight = "bold";
                        response.json().then(error => console.error("ERROR:", error));
                        throw new Error(`HTTP ${response.status} ${response.statusText}`);
                    }
                }).then(() => {
                    if (!PRODUCTION) { console.log("ACT : song added to playlist"); }
                    document.getElementById("song-send").style.color = "var(--md-sys-color-primary)";
                }).catch(error => console.error(error));
            });
            songPlaylists.appendChild(songSend);
            adding.appendChild(songPlaylists);

            /** Form: add to newly created playlist **/
            let songNewPlaylist = document.createElement("form");
            songNewPlaylist.id = "song-new-playlist";

            let label2 = document.createElement("label");
            label2.setAttribute("for", "text");
            label2.textContent = "NEW";
            songNewPlaylist.appendChild(label2);

            let text = document.createElement("input");
            text.setAttribute("type", "text");
            text.name = "text";
            text.id = "text";
            text.maxLength = 50;
            text.required = true;
            songNewPlaylist.appendChild(text);

            let songSendNew = document.createElement("button");
            songSendNew.id = "song-send-new";
            songSendNew.textContent = "+";
            songSendNew.type = "button";

            songSendNew.addEventListener('click', (event) => {
                const FORM = document.getElementById("song-new-playlist");
                if (!FORM.checkValidity()) {
                    FORM.reportValidity();
                    return;
                }
                if (text.value.trim() === "") {
                    document.getElementById("song-send-new").style.color = "var(--md-sys-error-container)";
                    console.error("ERR : no empty/whitespace strings");
                    event.preventDefault();
                }
                const PLAYLIST = {
                    Playlists: {
                        title: text.value.trim(),
                        userId: token
                    }
                };
                fetch(BASE_PATH + `playlists`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(PLAYLIST)
                }).then(response => {
                    if (!response.ok) {
                        response.json().then(error => console.error("ERROR:", error));
                        throw new Error(`HTTP ${response.status} ${response.statusText}`);
                    }
                    return response.json();
                }).then(async data => {
                    if (!PRODUCTION) { console.log("ACT : playlist created\n", data); }
                    try {
                        const response = await fetch(BASE_PATH + `songs/${ID}/playlists/${data['Playlists']['id']}`, {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' }
                        });
                        if (!response.ok) {
                            document.getElementById("song-send-new").style.color = "var(--md-sys-color-error-container)";
                            document.getElementById("song-send-new").style.fontWeight = "bold";
                            const err = await response.json();
                            console.error("ERROR:", err);
                            throw new Error(`HTTP ${response.status} ${response.statusText}`);
                        }
                        if (!PRODUCTION) { console.log("ACT : song added\n", data); }
                        document.getElementById("song-send-new").style.color = "var(--md-sys-color-primary)";
                    } catch (e) { console.error(e); }
                }).catch(error => console.error(error));
            });
            songNewPlaylist.appendChild(songSendNew);
            adding.appendChild(songNewPlaylist);
            mainElement.insertBefore(adding, document.getElementById("song-video"));
        }
    });
});