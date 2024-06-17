-- Data
INSERT INTO Users (username, email, password, avatar)
VALUES
    ('Andrea Valentinuzzi', 'andrea@email.it', 'andreaval1', 'avatar1.png'),
    ('Giovanni Brejc', 'giovanni@email.it', 'giovanni1', NULL),
    ('Maria Rossi', 'maria@email.com', 'maria123', 'avatar3.png'),
    ('Luca Bianchi', 'luca@email.com', 'luca456', 'avatar4.png'),
    ('Chiara Verdi', 'chiara@email.com', 'chiara789', NULL),
    ('Marco Moretti', 'marco@email.com', 'marco321', 'avatar6.png'),
    ('Alessia Ferrari', 'alessia@email.com', 'alessia789', NULL),
    ('Davide Russo', 'davide@email.com', 'davide123', 'avatar8.png'),
    ('Elena Martini', 'elena@email.com', 'elena456', 'avatar9.png'),
    ('Fabio Esposito', 'fabio@email.com', 'fabio789', NULL),
    ('Giulia Romano', 'giulia@email.com', 'giulia123', 'avatar11.png'),
    ('Paolo Santoro', 'paolo@email.com', 'paolo456', 'avatar12.png'),
    ('Sara Conti', 'sara@email.com', 'sara789', NULL),
    ('Simone Gatti', 'simone@email.com', 'simone123', 'avatar14.png'),
    ('Valentina De Luca', 'valentina@email.com', 'valentina456', 'avatar15.png'),
    ('Filippo Bianco', 'filippo@email.com', 'filippo789', NULL),
    ('Laura Galli', 'laura@email.com', 'laura123', 'avatar17.png'),
    ('Stefano Rizzo', 'stefano@email.com', 'stefano456', 'avatar18.png'),
    ('Veronica Costa', 'veronica@email.com', 'veronica789', NULL);

INSERT INTO Playlists (Title, UserID)
VALUES
        ('My Favorite Songs', 1),     -- Playlist owned by user with ID 1
        ('Workout Mix', 1),           -- Playlist owned by user with ID 1
        ('Chill Vibes', 3),           -- Playlist owned by user with ID 3
        ('Road Trip Playlist', 4),    -- Playlist owned by user with ID 4
        ('Study Focus', 5),           -- Playlist owned by user with ID 5
        ('Throwback Jams', 6),        -- Playlist owned by user with ID 6
        ('Party Hits', 7),            -- Playlist owned by user with ID 7
        ('Relaxing Piano', 1),        -- Playlist owned by user with ID 1
        ('Hip Hop Essentials', 9),    -- Playlist owned by user with ID 9
        ('Indie Rock Favorites', 10), -- Playlist owned by user with ID 10
        ('Jazz Classics', 2),         -- Playlist owned by user with ID 2
        ('Rock Anthems', 8),          -- Playlist owned by user with ID 8
        ('Electronic Vibes', 11);     -- Playlist owned by user with ID 11

INSERT INTO Songs (Title, Youtube_Link, Release_Date, Genre, Duration, ArtistID)
VALUES
--1
    ('Shape of You', 'https://www.youtube.com/watch?v=JGwWNGJdvx8', '2017-01-06', 'Pop', '00:03:53', 1),
    ('Uptown Funk', 'https://www.youtube.com/watch?v=OPf0YbXqDm0', '2014-11-14', 'Funk', '00:04:32', 2),
    ('Despacito', 'https://www.youtube.com/watch?v=kJQP7kiw5Fk', '2017-01-12', 'Latin', '00:04:41', 3),
    ('Roar', 'https://www.youtube.com/watch?v=CevxZvSJLk8', '2013-09-05', 'Pop', '00:04:30', 4),
  --5
    ('Havana', 'https://www.youtube.com/watch?v=BQ0mxQXmLsk', '2017-08-03', 'Pop', '00:03:39', 5),
    ('Someone Like You', 'https://www.youtube.com/watch?v=hLQl3WQQoQ0', '2011-09-29', 'Pop', '00:04:45', 6),
    ('Billie Jean', 'https://www.youtube.com/watch?v=Zi_XLOBDo_Y', '1983-01-02', 'Pop', '00:04:54', 7),
    ('Sweet Child o'' Mine', 'https://www.youtube.com/watch?v=1w7OgIMMRc4', '1987-07-21', 'Rock', '00:05:55', 8),
    ('Bohemian Rhapsody', 'https://www.youtube.com/watch?v=fJ9rUzIMcZQ', '1975-10-31', 'Rock', '00:06:07', 9),
    --10
    ('Hotel California', 'https://www.youtube.com/watch?v=lrfhf1Gv4Tw', '1976-12-08', 'Rock', '00:06:30', 10),
    ('Smells Like Teen Spirit', 'https://www.youtube.com/watch?v=hTWKbfoikeg', '1991-09-10', 'Grunge', '00:04:38', 11),
    ('Thriller', 'https://www.youtube.com/watch?v=sOnqjkJTMaA', '1983-11-30', 'Pop', '00:13:43', 12),
    ('Don''t Stop Believin''', 'https://www.youtube.com/watch?v=1k8craCGpgs', '1981-06-03', 'Rock', '00:04:11', 13),
    ('Every Breath You Take', 'https://www.youtube.com/watch?v=OMOGaugKpzs', '1983-05-20', 'Pop', '00:04:13', 14),
    --15
    ('Livin'' on a Prayer', 'https://www.youtube.com/watch?v=lDK9QqIzhwk', '1986-10-31', 'Rock', '00:04:09', 15),
    ('Firework', 'https://www.youtube.com/watch?v=QGJuMBdaqIw', '2010-10-26', 'Pop', '00:03:55', 4),
    ('Every Little Thing She Does Is Magic', 'https://www.youtube.com/watch?v=aENX1Sf3fgQ', '1981-09-01', 'Rock', '00:04:20', 14),
    ('You Give Love a Bad Name', 'https://www.youtube.com/watch?v=KrZHPOeOxQQ', '1986-07-18', 'Rock', '00:03:48', 15),
    ('Separate Ways (Worlds Apart)', 'https://www.youtube.com/watch?v=LatorN4P9aA', '1983-01-05', 'Rock', '00:04:22', 13),
       --20
    ('Come As You Are', 'https://www.youtube.com/watch?v=vabnZ9-ex7o', '1991-09-24', 'Grunge', '00:03:38', 11),
    ('Take It Easy', 'https://www.youtube.com/watch?v=DIoKr9VDg3A', '1972-05-01', 'Rock', '00:03:31', 10),
    ('Another One Bites the Dust', 'https://www.youtube.com/watch?v=rY0WxgSXdEE', '1980-06-30', 'Rock', '00:03:36', 9),
    ('November Rain', 'https://www.youtube.com/watch?v=8SbUC-UaAxE', '1992-06-17', 'Rock', '00:08:57', 8),
    ('Beat It', 'https://www.youtube.com/watch?v=oRdxUFDoQe0', '1983-02-14', 'Pop', '00:04:56', 7),
    --25
    ('Rolling in the Deep', 'https://www.youtube.com/watch?v=rYEDA3JcQqw', '2010-11-29', 'Pop', '00:03:54', 6),
    ('Never Be the Same', 'https://www.youtube.com/watch?v=Ph54wQG8ynk', '2017-12-07', 'Pop', '00:04:02', 5),
    ('Échame La Culpa', 'https://www.youtube.com/watch?v=TyHvyGVs42U', '2017-11-17', 'Latin', '00:03:31', 3),
    ('Nothing Breaks Like a Heart', 'https://www.youtube.com/watch?v=A9hcJgtnm6Q', '2018-11-29', 'Pop', '00:03:49', 2),
    ('Castle on the Hill', 'https://www.youtube.com/watch?v=K0ibBPhiaG0', '2017-01-06', 'Pop', '00:04:21', 1),
    --30
     ('Eraser', 'https://www.youtube.com/watch?v=I0hXGOfvYi4', '2017-03-03', 'Pop', 1),
     ('Castle on the Hill', 'https://www.youtube.com/watch?v=K0ibBPhiaG0', '2017-01-06', 'Pop', 1),
     ('Dive', 'https://www.youtube.com/watch?v=WjKFQjOynfA', '2017-03-03', 'Pop', 1),
     ('Perfect', 'https://www.youtube.com/watch?v=2Vv-BfVoq4g', '2017-03-03', 'Pop', 1),
     ('Galway Girl', 'https://www.youtube.com/watch?v=87gWaABqGYs', '2017-03-03', 'Pop', 1),
    -- 35
    ('Happier', 'https://www.youtube.com/watch?v=8BzVMB5J-jI', '2017-03-03', 'Pop', 1),
     ('New Man', 'https://www.youtube.com/watch?v=BjwBdFCN4VI', '2017-03-03', 'Pop', 1),
     ('Hearts Don''t Break Around Here', 'https://www.youtube.com/watch?v=e1dgNkZfFys', '2017-03-03', 'Pop', 1),
     ('What Do I Know?', 'https://www.youtube.com/watch?v=QqGWWBaWLDY', '2017-03-03', 'Pop', 1),
     ('How Would You Feel (Paean)', 'https://www.youtube.com/watch?v=wY473jAptyw', '2017-02-17', 'Pop', 1),
    --40
     ('Supermarket Flowers', 'https://www.youtube.com/watch?v=K0ibBPhiaG0', '2017-03-03', 'Pop', 1);

INSERT INTO Artists (Name, Artist_Image, Biography, Verified, Language)
VALUES
    ('Ed Sheeran', 'ed_sheeran.jpg', 'Biography of Ed Sheeran...', TRUE, 'English'),                -- Artist ID 1 for 'Shape of You'
    ('Mark Ronson', 'mark_ronson.jpg', 'Biography of Mark Ronson...', TRUE, 'English'),             -- Artist ID 2 for 'Uptown Funk'
    ('Luis Fonsi', 'luis_fonsi.jpg', 'Biography of Luis Fonsi...', TRUE, 'Spanish'),                -- Artist ID 3 for 'Despacito'
    ('Katy Perry', 'katy_perry.jpg', 'Biography of Katy Perry...', TRUE, 'English'),                -- Artist ID 4 for 'Roar'
    ('Camila Cabello', 'camila_cabello.jpg', 'Biography of Camila Cabello...', TRUE, 'English'),    -- Artist ID 5 for 'Havana'
    ('Adele', 'adele.jpg', 'Biography of Adele...', TRUE, 'English'),                               -- Artist ID 6 for 'Someone Like You'
    ('Michael Jackson', 'michael_jackson.jpg', 'Biography of Michael Jackson...', TRUE, 'English'), -- Artist ID 7 for 'Billie Jean'
    ('Guns N'' Roses', 'guns_n_roses.jpg', 'Biography of Guns N'' Roses...', TRUE, 'English'),      -- Artist ID 8 for 'Sweet Child o' Mine'
    ('Queen', 'queen.jpg', 'Biography of Queen...', TRUE, 'English'),                               -- Artist ID 9 for 'Bohemian Rhapsody'
    ('Eagles', 'eagles.jpg', 'Biography of Eagles...', TRUE, 'English'),                            -- Artist ID 10 for 'Hotel California'
    ('Nirvana', 'nirvana.jpg', 'Biography of Nirvana...', TRUE, 'English'),                         -- Artist ID 11 for 'Smells Like Teen Spirit'
    ('Michael Jackson', 'michael_jackson.jpg', 'Biography of Michael Jackson...', TRUE, 'English'), -- Artist ID 12 for 'Thriller'
    ('Journey', 'journey.jpg', 'Biography of Journey...', TRUE, 'English'),                         -- Artist ID 13 for 'Don't Stop Believin''
    ('The Police', 'the_police.jpg', 'Biography of The Police...', TRUE, 'English'),                -- Artist ID 14 for 'Every Breath You Take'
    ('Bon Jovi', 'bon_jovi.jpg', 'Biography of Bon Jovi...', TRUE, 'English'),                      -- Artist ID 15 for 'Livin' on a Prayer'
    ('Bruno Mars', 'bruno_mars.jpg', 'Biography of Bruno Mars...', TRUE, 'English'),
    ('Coldplay', 'coldplay.jpg', 'Biography of Coldplay...', TRUE, 'English'),
    ('Taylor Swift', 'taylor_swift.jpg', 'Biography of Taylor Swift...', TRUE, 'English'),
    ('Ariana Grande', 'ariana_grande.jpg', 'Biography of Ariana Grande...', TRUE, 'English'),
    ('Drake', 'drake.jpg', 'Biography of Drake...', TRUE, 'English');

INSERT INTO Albums (Title, Album_Cover_Image, Release_Date, Genre, ArtistID)
VALUES
    ('Divide', 'divide_album_cover.jpg', '2017-03-03', 'Pop', 1),
    ('Thriller', 'thriller_album_cover.jpg', '1982-11-30', 'Pop', 7),
    ('21', '21_album_cover.jpg', '2011-01-24', 'Pop', 6);


INSERT INTO SongsInAlbums (SongID, AlbumID) VALUES
    (1,1),(30,1),(31,1),(32,1),(33,1),(34,1),(35,1),(36,1),(37,1),(38,1),(39,1),(40,1);
    -- CREATES THE RELATIONSHIPS BETWEEN THE SONGS OF EDSHERAN AND THE ALBUM DIVIDE


INSERT INTO LikedSongs (UserID, SongID) VALUES
    (14,3),(4,25),(16,32),(18,17),
    (1,3),(2,12),(3,18),(4,5),
    (5,9),(6,22),(7,13),(8,19),
    (9,4),(10,8),(11,10),(12,16),
    (13,21),(14,15),(15,7),(16,1),
    (17,23),(18,6),(19,2),(20,11);

INSERT INTO LikedAlbums (UserID, AlbumID) VALUES
    (1,1),(4,1),(1,2),(1,3),
    (1,1),(2,2),(3,3),(4,3),
    (5,1),(6,2),(7,3),(8,3),
    (9,1),(10,2),(11,3),(12,1),
    (13,1),(14,2),(15,3),(16,2),
    (17,1),(18,2),(19,3),(20,1);

INSERT INTO SongsInPlaylists (SongID, PlaylistID) VALUES
    (1,1),(2,1),(15,2),(1,1),
    (2,1),(3,2),(4,2),(2,1),
    (5,3),(6,3),(7,4),(8,4),
    (9,5),(10,5),(11,6),(12,6),
    (13,7),(14,7),(15,8),(16,8),
    (17,9),(18,9),(19,10),(20,10),
    (21,11),(22,11),(23,12),(24,12),
    (25,13),(26,13);