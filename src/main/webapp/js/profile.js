document.addEventListener('DOMContentLoaded', () => {
    const BASE_PATH = "http://localhost:8080/ranamelone-1.0.0/rest/";
    let url = new URL(window.location.href);
    let ID = url.searchParams.get("id");
/*
    let uToken = checkUserCookie();
    if (uToken === null) { window.location.href = "login.html"; }
    else if (uToken !== ID) {
        alert("Access Denied");
        window.location.href = "homepage.html";
    }

    let aToken = checkArtistCookie();
    if (aToken === null) { window.location.href = "login.html"; }
    else if (aToken !== ID) {
        alert("Access Denied");
        window.location.href = "homepage.html";
    }
*/
    function isArtist() {
        let artist = false;
        const searchTerm = "authToken=artist-"
        document.cookie.split(';').forEach(cookie => {
            if (cookie.trim().startsWith(searchTerm)) {
                artist = true;
            }
        });
        return artist;
    }

    if (isArtist()) {
        let artistName;
        let artistEmail;
        let artistPassword;
        let artistImage;
        let artistBio;
        let artistVerified;
        let artistLanguage;


        fetch(BASE_PATH + `artists/${ID}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP ${response.status} ${response.statusText}`);
            }
            return response.json();
        }).then(data => {
            console.log(data);
            document.getElementById('profile-name').textContent = data['Artists']['name'];
            document.getElementById('profile-email').textContent = data['Artists']['email'];
            document.getElementById('profile-bio').style.display = "block";
            document.getElementById('profile-bio').textContent = data['Artists']['biography'];

            artistName = data['Artists']['name'];
            artistEmail = data['Artists']['email'];
            artistPassword = data['Artists']['password'];
            artistImage = data['Artists']['artist_image'];
            artistBio = data['Artists']['biography'];
            artistVerified = data['Artists']['verified'];
            artistLanguage = data['Artists']['language'];
        });

        document.getElementById("new-biography").style.display = "block";

        const CONFIRM = document.getElementById('confirm');
        const DELETE = document.getElementById('delete');

        let uploadSongButton = document.getElementById('upload-song');
        uploadSongButton.addEventListener('click', () => {
            let token = checkArtistCookie();
            if (token == null) { window.location.href = "login.html"; }
            window.location.href = `upload_song.html?id=${token}`;
        });

        let uploadAlbumButton = document.getElementById('upload-album');
        uploadAlbumButton.addEventListener('click', () => {
            let token = checkArtistCookie();
            if (token == null) { window.location.href = "login.html"; }
            window.location.href = `upload_album.html?id=${token}`;
        });

        CONFIRM.addEventListener('click', (event) => {
            const newUsername = document.getElementById('new-username').value;
            const newEmail = document.getElementById('new-email').value;
            const newBiography = document.getElementById('new-biography').value;
            const oldPassword = document.getElementById('old-password').value;
            const newPassword = document.getElementById('new-password').value;
            const confirmPassword = document.getElementById('confirm-password').value;

            let token = checkArtistCookie()

            let artistNewValues = {
                Artists: {
                    id: token,
                    name: artistName,
                    email: artistEmail,
                    password: artistPassword,
                    artist_image: artistImage,
                    biography: artistBio,
                    verified: artistVerified,
                    language: artistLanguage
                }
            };

            var modal = document.getElementById('modal');
            var alertText = document.getElementById('settings-alert');

            window.onclick = function (event) {
                if (event.target === modal) {
                    modal.style.display = "none";
                }
            }

            if (newUsername) {
                artistNewValues.Users["username"] = newUsername;
            }

            if (newEmail) {
                artistNewValues.Users["email"] = newEmail;
            }

            if (newPassword) {
                if (newPassword === confirmPassword) {
                    if (oldPassword === artistPassword) {
                        artistNewValues.Users["password"] = newPassword;
                        alertText = "Settings changed";
                    } else {
                        alertText = "Old password is invalid!";
                    }
                } else {
                    alertText = "Passwords don't match!";
                }
            }

            console.log(artistNewValues);
            document.getElementById('settings-alert').textContent = alertText;
            modal.style.display = "block";

            fetch(BASE_PATH + `artists/${token}`, {
                method: 'PUT',  // Updated to 'PUT' for consistency
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(artistNewValues)
            }).then(response => {
                if (!response.ok) {
                    response.json().then(error => console.log("ERROR:", error));
                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                }
                return response.json();
            }).then(() => {
                console.log("VALUES CHANGED");
            }).catch(error => console.log(error));
        });

        function deleteArtistAccount() {
            const token = checkArtistCookie()

            fetch(BASE_PATH + `artists/${token}`, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' }
            }).then(response => {
                if (!response.ok) {
                    return response.json().then(error => {
                        console.error('ERROR:', error);
                        throw new Error(`HTTP ${response.status} ${response.statusText}`);
                    });
                }
                return response.json();
            }).then(() => {
                document.cookie = "authToken=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/;domain=" + window.location.hostname + ";secure;";
                window.location.href = "login.html";
            }).catch(error => {
                console.error(error);
            });
        }

        DELETE.addEventListener('click', deleteArtistAccount);

    } else {
        let userUsername;
        let userEmail;
        let userPassword;
        let userAvatar;

        fetch(BASE_PATH + `users/${ID}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP ${response.status} ${response.statusText}`);
            }
            return response.json();
        }).then(data => {
            console.log(data);
            document.getElementById('profile-name').textContent = data['Users']['username'];
            document.getElementById('profile-email').textContent = data['Users']['email'];
            document.getElementById('profile-bio').style.display = "none";
            document.getElementById('profile-buttons').style.display = "none";
            document.getElementById('new-biography').style.display = "none";
            document.getElementById('bio-label').style.display = "none";

            userUsername = data['Users']['username']
            userEmail = data['Users']['email'];
            userPassword = data['Users']['password'];
            userAvatar = data['Users']['avatar'];
        });

        const CONFIRM = document.getElementById('confirm');
        const DELETE = document.getElementById('delete');

        let uploadSongButton = document.getElementById('upload-song');
        uploadSongButton.addEventListener('click', () => {
            let token = checkUserCookie();
            if (token == null) { window.location.href = "login.html"; }
            window.location.href = `upload_song.html?id=${token}`;
        });

        let uploadAlbumButton = document.getElementById('upload-album');
        uploadAlbumButton.addEventListener('click', () => {
            let token = checkUserCookie();
            if (token == null) { window.location.href = "login.html"; }
            window.location.href = `upload_album.html?id=${token}`;
        });

        CONFIRM.addEventListener('click', (event) => {
            const newUsername = document.getElementById('new-username').value;
            const newEmail = document.getElementById('new-email').value;
            const oldPassword = document.getElementById('old-password').value;
            const newPassword = document.getElementById('new-password').value;
            const confirmPassword = document.getElementById('confirm-password').value;

            let token = checkUserCookie()

            let userNewValues = {
                Users: {
                    id: token,
                    username: userUsername,
                    email: userEmail,
                    password: userPassword,
                    avatar: userAvatar
                }
            };

            let modal = document.getElementById('modal');
            let alertText = document.getElementById('settings-alert');

            window.onclick = function (event) {
                if (event.target === modal) {
                    modal.style.display = "none";
                }
            }

            if (newUsername) {
                userNewValues.Users["username"] = newUsername;
            }

            if (newEmail) {
                userNewValues.Users["email"] = newEmail;
            }

            if (newPassword) {
                if (newPassword === confirmPassword) {
                    if (oldPassword === userPassword) {
                        userNewValues.Users["password"] = newPassword;
                        alertText = "Settings changed";
                    } else {
                        alertText = "Old password is invalid!";
                    }
                } else {
                    alertText = "Passwords don't match!";
                }
            }

            console.log("USER JSON: ", JSON.stringify(userNewValues));
            document.getElementById('settings-alert').textContent = alertText;
            modal.style.display = "block";

            fetch(BASE_PATH + `users/${token}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userNewValues)
            }).then(response => {
                if (!response.ok) {
                    response.json().then(error => console.log("ERROR:", error));
                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                }
                return response.json();
            }).then(() => {
                console.log("VALUES CHANGED");
            }).catch(error => console.log(error));
        });

        function deleteUserAccount() {
            const token = checkUserCookie()

            fetch(BASE_PATH + `users/${token}`, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' }
            }).then(response => {
                if (!response.ok) {
                    return response.json().then(error => {
                        console.error('ERROR:', error);
                        throw new Error(`HTTP ${response.status} ${response.statusText}`);
                    });
                }
                return response.json();
            }).then(() => {
                document.cookie = "authToken=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/;domain=" + window.location.hostname + ";secure;";
                window.location.href = "login.html";
            }).catch(error => {
                console.error(error);
            });
        }


        DELETE.addEventListener('click', deleteUserAccount);
    }
});
