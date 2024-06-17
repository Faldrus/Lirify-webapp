document.addEventListener("DOMContentLoaded", () => {
    const THEME_SWITCH= document.getElementById("theme-switch");

    /** SEARCH bar behavior. **/
    let searchBar = document.getElementById("search-bar");
    searchBar.addEventListener('submit', (event) => {
        event.preventDefault();
        let inputText = document.getElementById("search");
        let input = inputText.value;
        window.location.href = `search.html?q=${input}` ;
    });

    /** THEME switch behavior. **/
    let theme = localStorage.getItem("theme");
    if (theme) { document.body.classList = theme; }
    THEME_SWITCH.addEventListener('click', () => {
        document.body.classList.toggle("dark");
        document.body.classList.toggle("light");
        localStorage.setItem("theme", document.body.classList);
    });

    /** Homepage link. **/
    let homeButton = document.getElementById("nav-homepage");
    homeButton.onclick = () => {
        window.location.href = "homepage.html";
    }

    /** Profile link. **/
    let profileButton = document.getElementById("nav-profile");
    profileButton.addEventListener('click', () => {
        let userToken = checkUserCookie();
        let artistToken = checkArtistCookie();
        if (userToken !== null) {
            window.location.href = `profile.html?id=${userToken}`;
        } else if (artistToken !== null) {
            window.location.href = `profile.html?id=${artistToken}`;
        } else {
            window.location.href = "login.html";
        }
    });

    /** Library link. **/
    let libraryButton = document.getElementById("nav-library");
    libraryButton.addEventListener('click', () => {
        let token = checkUserCookie();
        if (token === null) { window.location.href = "login.html"; }
        window.location.href = `library.html?id=${token}`;
    })

    /** Login link. **/
    let loginButton = document.getElementById("nav-login");
    loginButton.onclick = () => {
        window.location.href = "login.html";
    }

    /** Login name display and logout button **/
    let uToken = checkUserCookie();
    let aToken = checkArtistCookie();
    console.log(uToken ? `users/${uToken}` : `artists/${aToken}`);
    if (uToken !== null || aToken !== null) {
        fetch(BASE_PATH + (uToken ? `users/${uToken}` : `artists/${aToken}`), {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        }).then(response => {
            if (!response.ok) {
                response.json().then(error => console.error("ERROR:", error));
                throw new Error(`HTTP ${response.status} ${response.statusText}`);
            }
            return response.json();
        }).then(data => {
            if (!PRODUCTION) { console.log("DBG : logged in as\n", data); }
            let name = document.createElement("li");
            name.textContent = uToken ? data['Users']['username'] : data['Artists']['name'];
            name.style.display = "inline-block";
            name.style.textAlign = "center";
            name.style.fontSize = "small";
            document.querySelector("nav ul").appendChild(name);

            document.getElementById("nav-login").textContent = "Logout";
            loginButton.onclick = () => {
                document.cookie = "authToken=;expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/;domain=" + window.location.hostname + ";secure;";
                window.location.href = "login.html";
            }

        }).catch(error => console.error(error));
    }
});