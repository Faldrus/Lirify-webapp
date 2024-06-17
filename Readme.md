# Webapp 2024 - RanaMelone - Lirify

---

University of Padua, Master's degree in Computer Engineering.\
Web Applications course, A.Y. 2023/2024, group Ranamelone, project Lirify. \
Lirify is an application for song lyrics, that provides users and artists functionalities for the management of songs, albums and playlists.

---

## Notes

- The DB is pre-filled with 3 user accounts + artists and their relative songs/albums for quick debugging.
- By default `webapp/js/utils.js` has a `PRODUCTION` variable flag set to `false`. This allows the JavaScript scripts to print useful debugging info in the browser console, but it's definitely not 'safe'.
- Filters are implemented with `authToken=user-ID` and `authToken=artist-ID` 12h cookies issued by `webapp/js/login.js`.
- The light/dark theme functionality uses `localStorage` to save the preference.
- Passwords are NOT encrypted in the DB.


## REST

| request                           | method | description                                     |
|:----------------------------------|:-------|:------------------------------------------------|
| `rest/albums`                     | POST   | create Album entry                              |
| `rest/albums/artists/{ID}`        | GET    | list albums by Artist with ID                   |
| `rest/albums/pickalgorithm/{NUM}` | GET    | randomly list up to NUM Albums within last year |
| `rest/albums/{ID}`                | DELETE | delete Album with ID                            |
| `rest/albums/{ID}`                | GET    | info about Album with ID                        |
| `rest/albums/search/{QUERY}`      | GET    | QUERY search Albums                             |
| `rest/artists`                    | POST   | create Artist entry                             |
| `rest/artists/{ID}`               | DELETE | delete Artist with ID                           |
| `rest/artists/{ID}`               | GET    | info about Artist with ID                       |
| `rest/artists/{ID}`               | PUT    | update info about Artist with ID                |
| `rest/artists/login`              | POST   | Artist login                                    |
| `rest/artists/search/{QUERY}`     | GET    | QUERY search Artists                            |
| `rest/playlists`                  | POST   | create Playlist entry                           |
| `rest/playlists/users/{ID}`       | GET    | list Playlist by User with ID                   |
| `rest/playlists/{ID}`             | DELETE | delete Playlist with ID                         |
| `rest/playlists/{ID}`             | GET    | info about Playlist with ID                     |
| `rest/songs`                      | POST   | create Song entry                               |
| `rest/songs/pickalgorithm/{NUM}`  | GET    | randomly list up to NUM Songs within last year  |
| `rest/songs/albums/{ID}`          | GET    | list Songs in Album with ID                     |
| `rest/songs/albums/{ID}`          | POST   | add Song to Album with ID                       |
| `rest/songs/{ID}/playlists/{ID}`  | DELETE | delete Song with ID from Playlist with ID       |
| `rest/songs/playlists/{ID}`       | GET    | list Songs in Playlist with ID                  |
| `rest/songs/{ID}/playlists/{ID}`  | POST   | add Song with ID to Playlist with ID            |
| `rest/songs/{ID}`                 | DELETE | delete Song with ID                             |
| `rest/songs/{ID}`                 | GET    | info about Song with ID                         |
| `rest/songs/search/{QUERY}`       | GET    | QUERY search Songs                              |
| `rest/users`                      | POST   | create User entry                               |
| `rest/users/{ID}/albums/{ID}`     | DELETE | User with ID unlikes Album with ID              |
| `rest/users/{ID}/albums`          | GET    | list Albums liked by User with ID               |
| `rest/users/{ID}/albums/{ID}`     | POST   | User with ID likes Album with ID                |
| `rest/users/{ID}/songs/{ID}`      | DELETE | User with ID unlikes Song with ID               |
| `rest/users/{ID}/songs`           | GET    | list Songs liked by User with ID                |
| `rest/users/{ID}/songs/{ID}`      | POST   | User with ID likes Song with ID                 |
| `rest/users/{ID}`                 | DELETE | delete User with ID                             |
| `rest/users/{ID}`                 | GET    | info about User with ID                         |
| `rest/users/{ID}`                 | PUT    | update info about User with ID                  |
| `rest/users/login`                | POST   | User login                                      |