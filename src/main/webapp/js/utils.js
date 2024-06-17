/** Logs with sensitive data are disabled in production. **/
const PRODUCTION = true;

/** Defines the base path for REST calls. **/
const BASE_PATH = "http://localhost:8080/ranamelone-1.0.0/rest/";

/**
 * @returns {null} if there is no User logged in.
 * @returns {String} if there is a User logged in.
 */
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

/**
 * @returns {null} if there is no Artist logged in.
 * @returns {String} if there is an Artist logged in.
 */
function checkArtistCookie() {
    let token = null;
    const searchTerm = "authToken=artist-"
    document.cookie.split(';').forEach(cookie => {
        if (cookie.trim().startsWith(searchTerm)) {
            token = cookie.trim().substring(searchTerm.length);
        }
    });
    return token;
}