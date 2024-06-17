document.addEventListener('DOMContentLoaded', () => {
    const R_SUBMIT = document.getElementById('r-submit');
    const LU_SUBMIT = document.getElementById('lu-submit');
    const LA_SUBMIT = document.getElementById('la-submit');

    const R_FORM = document.getElementById('r-form');
    const LU_FORM = document.getElementById('lu-form');
    const LA_FORM = document.getElementById('la-form');

    function deleteError(form) {
        if (form.lastElementChild.className === 'input-error') {
            form.lastElementChild.remove();
        }
    }
    function customError(form, message) {
        let errorP = document.createElement('p');
        errorP.textContent = message;
        errorP.classList.add('input-error');
        form.appendChild(errorP);
    }

    /** REGISTER behavior. **/
    R_SUBMIT.addEventListener('click', (event) => {
        if (!R_FORM.checkValidity()) {
            R_FORM.reportValidity();
            return;
        }
        const DATA = new FormData(R_FORM);
        deleteError(R_FORM);
        if (DATA.get("r-email") !== DATA.get("r-email-confirm")) {
            customError(R_FORM, "emails don't match");
            event.preventDefault();
        }
        if (DATA.get("r-password") !== DATA.get("r-password-confirm")) {
            customError(R_FORM, "passwords don't match");
            event.preventDefault();
        }
        if (DATA.get("r-email").trim() === "" ||
            DATA.get("r-email-confirm").trim() === "" ||
            DATA.get("r-password").trim() === "" ||
            DATA.get("r-password-confirm").trim() === "") {
            customError(R_FORM, "no empty/whitespace strings");
            event.preventDefault();
        }
        const IS_ARTIST = DATA.get("r-is-artist");
        if (IS_ARTIST) { /** Artist registration. **/
            const ARTIST = {
                Artists: {
                    name: DATA.get("r-username").trim(),
                    email: DATA.get("r-email").trim(),
                    password: DATA.get("r-password").trim()
                }
            };
            fetch(BASE_PATH + "artists", {
                method: "POST",
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(ARTIST)
            }).then(response => {
                if (!response.ok) {
                    response.json().then(error => console.error("ERROR:", error));
                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                }
                return response.json();
            }).then(data => {
                const DATE = new Date();
                const HOURS = 12;
                DATE.setTime(DATE.getTime() + (HOURS * 60 * 60 * 1000));
                document.cookie =
                    "authToken=artist-" + data['Artists']['id'] +
                    ";expires=" + DATE.toUTCString() +
                    ";path=/" +
                    ";domain=" + window.location.hostname +
                    ";secure;";
                window.location.href = "homepage.html";
            }).catch(error => {
                console.error(error);
                customError(R_FORM, error);
            });
        } else { /** User registration. **/
            const USER = {
                Users: {
                    username: DATA.get("r-username").trim(),
                    email: DATA.get("r-email").trim(),
                    password: DATA.get("r-password").trim()
                }
            };
            fetch(BASE_PATH + "users", {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(USER)
            }).then(response => {
                if (!response.ok) {
                    response.json().then(error => console.error("ERROR:", error));
                    throw new Error(`HTTP ${response.status} ${response.statusText}`);
                }
                return response.json();
            }).then(data => {
                const DATE = new Date();
                const HOURS = 12;
                DATE.setTime(DATE.getTime() + (HOURS * 60 * 60 * 1000));
                document.cookie =
                    "authToken=user-" + data['Users']['id'] +
                    ";expires=" + DATE.toUTCString() +
                    ";path=/" +
                    ";domain=" + window.location.hostname +
                    ";secure;";
                window.location.href = "homepage.html";
            }).catch(error => {
                console.error(error);
                customError(R_FORM, error);
            });
        }
    });

    /** LOGIN User behavior. **/
    LU_SUBMIT.addEventListener('click', (event) => {
        if (!LU_FORM.checkValidity()) {
            LU_FORM.reportValidity();
            return;
        }
        const DATA = new FormData(LU_FORM);
        deleteError(LU_FORM);
        const USER = {
            Users: {
                username: null,
                email: DATA.get("lu-email").trim(),
                password: DATA.get("lu-password").trim()
            }
        };
        fetch(BASE_PATH + "users/login", {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(USER)
        }).then(response => {
            if (!response.ok) {
                response.json().then(error => console.error("ERROR:", error));
                throw new Error(`HTTP ${response.status} ${response.statusText}`);
            }
            return response.json();
        }).then(data => {
            const DATE = new Date();
            const HOURS = 12;
            DATE.setTime(DATE.getTime() + (HOURS * 60 * 60 * 1000));
            document.cookie =
                "authToken=user-" + data['Users']['id'] +
                ";expires=" + DATE.toUTCString() +
                ";path=/" +
                ";domain=" + window.location.hostname +
                ";secure;";
            window.location.href = "homepage.html";
        }).catch(error => {
            console.error(error);
            customError(LU_FORM, error);
        });
    });

    /** LOGIN Artist behavior. **/
    LA_SUBMIT.addEventListener('click', (event) => {
        if (!LA_FORM.checkValidity()) {
            LA_FORM.reportValidity();
            return;
        }
        const DATA = new FormData(LA_FORM);
        deleteError(LA_FORM);
        const ARTIST = {
            Artists: {
                name: null,
                email: DATA.get("la-email").trim(),
                password: DATA.get("la-password").trim()
            }
        };
        fetch(BASE_PATH + "artists/login", {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(ARTIST)
        }).then(response => {
            if (!response.ok) {
                response.json().then(error => console.error("ERROR:", error));
                throw new Error(`HTTP ${response.status} ${response.statusText}`);
            }
            return response.json();
        }).then(data => {
            const DATE = new Date();
            const HOURS = 12;
            DATE.setTime(DATE.getTime() + (HOURS * 60 * 60 * 1000));
            document.cookie =
                "authToken=artist-" + data['Artists']['id'] +
                ";expires=" + DATE.toUTCString() +
                ";path=/" +
                ";domain=" + window.location.hostname +
                ";secure;";
            window.location.href = "homepage.html";
        }).catch(error => {
            console.error(error);
            customError(LA_FORM, error);
        });
    });
});