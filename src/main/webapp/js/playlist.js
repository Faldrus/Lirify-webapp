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

    //Playlist
    fetch(BASE_PATH + `playlists/${ID}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json'}
    }).then(response => {
        if(!response.ok) {
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        console.log(data);
        if (token !== data['Playlists']['userId'].toString()) {
            alert("Access Denied");
            window.location.href = "homepage.html";
        }
        document.getElementById('playlist-name').textContent = data['Playlists']['title'];
    })


    //Delete Playlist
    let deletebutton = document.getElementById("deleteBtn");
    var modal = document.getElementById("modal");

    deletebutton.onclick = function() {
        modal.style.display = "block";
    }
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    let cancelbutton = document.getElementById("cancelBtn")
    cancelbutton.onclick = function() {
        modal.style.display = "none";
    }

    let confirmbutton = document.getElementById("confirmBtn")
    confirmbutton.addEventListener('click', () =>{
        fetch(BASE_PATH + `playlists/${ID}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json'}
        }).then(response => {
            if(!response.ok) {
                throw new Error(`HTTP ${response.status} ${response.statusText}`);
            }
            return response.json();
        }).then(data => {
            console.log(data);
            window.location.href = `../html/library.html?id=${token}`
        })
    })

    //Songs
    fetch(BASE_PATH + `songs/playlists/${ID}`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json'}
    }).then(response => {
        if(!response.ok) {
            throw new Error(`HTTP ${response.status} ${response.statusText}`);
        }
        return response.json();
    }).then(data => {
        data = data['resource-list'];
        document.getElementById("playlist-capacity").textContent = data.length + " Song(s)";
        console.log(data);
        let container = document.getElementById("playlist-songs");
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

            let removebutton = document.createElement("button");
            removebutton.textContent = 'Remove';
            removebutton.addEventListener('click', function(){

                let SID = Songs['id'];
                let url = new URL(window.location.href);
                let PID = url.searchParams.get("id");

                fetch(BASE_PATH + `songs/${SID}/playlists/${PID}`, {
                    method: 'DELETE',
                    headers: {'Content-Type': 'application/json'}
                }).then(response =>{
                    if (!response.ok) {
                        throw new Error(`HTTP ${response.status} ${response.statusText}`);
                    }
                    return response.json();
                }).then(data => console.log(data)) .catch((error) => console.error('Error:', error));

                console.log('Remove');
                location.reload();

            });
            card.appendChild(removebutton);

            container.appendChild(card);

        })
    })

});