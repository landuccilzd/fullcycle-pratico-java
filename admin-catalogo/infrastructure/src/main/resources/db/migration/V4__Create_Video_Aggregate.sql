CREATE TABLE video_video_media (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    checksum VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    encoded_path VARCHAR(500) NOT NULL,
    media_status VARCHAR(50) NOT NULL
);

CREATE TABLE video_image_media (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    checksum VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL
);

CREATE TABLE video (
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    video_media_id VARCHAR(36),
    trailer_media_id VARCHAR(36),
    banner_media_id VARCHAR(36),
    thumbnail_media_id VARCHAR(36),
    thumbnail_half_media_id VARCHAR(36),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000) NOT NULL,
    year_launched SMALLINT NOT NULL,
    opened BOOLEAN NOT NULL DEFAULT FALSE,
    published BOOLEAN NOT NULL DEFAULT FALSE,
    rating VARCHAR(5),
    duration DECIMAL(5,2) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT FK_VIDEO_MEDIA FOREIGN KEY (video_media_id) REFERENCES video_video_media (id) ON DELETE CASCADE,
    CONSTRAINT FK_TRAILER_MEDIA FOREIGN KEY (trailer_media_id) REFERENCES video_video_media (id) ON DELETE CASCADE,
    CONSTRAINT FK_BANNER_MEDIA FOREIGN KEY (banner_media_id) REFERENCES video_image_media (id) ON DELETE CASCADE,
    CONSTRAINT FK_THUMBAIL_MEDIA FOREIGN KEY (thumbnail_media_id) REFERENCES video_image_media (id) ON DELETE CASCADE,
    CONSTRAINT FK_THUMBNAIL_HALF_MEDIA FOREIGN KEY (thumbnail_half_media_id) REFERENCES video_image_media (id) ON DELETE CASCADE
);

CREATE TABLE video_category (
    video_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    CONSTRAINT UX_VIDEO_CATEGORY_UNIQUE UNIQUE (video_id, category_id),
    CONSTRAINT PK_VIDEO_CATEGORY PRIMARY KEY (video_id, category_id),
    CONSTRAINT FK_VIDEO_CATEGORY_VIDEO FOREIGN KEY (video_id) REFERENCES video (id) ON DELETE CASCADE,
    CONSTRAINT FK_VIDEO_CATEGORY_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE
);

CREATE TABLE video_genre (
    video_id VARCHAR(36) NOT NULL,
    genre_id VARCHAR(36) NOT NULL,
    CONSTRAINT UX_VIDEO_GENRE_UNIQUE UNIQUE (video_id, genre_id),
    CONSTRAINT PK_VIDEO_GENRE PRIMARY KEY (video_id, genre_id),
    CONSTRAINT FK_VIDEO_GENRE_VIDEO FOREIGN KEY (video_id) REFERENCES video (id) ON DELETE CASCADE,
    CONSTRAINT FK_VIDEO_GENRE_GENRE FOREIGN KEY (genre_id) REFERENCES genre (id) ON DELETE CASCADE
);

CREATE TABLE video_cast_member (
    video_id VARCHAR(36) NOT NULL,
    cast_member_id VARCHAR(36) NOT NULL,
    CONSTRAINT UX_VIDEO_CAST_MEMBER_UNIQUE UNIQUE (video_id, cast_member_id),
    CONSTRAINT PK_VIDEO_CAST_MEMBER PRIMARY KEY (video_id, cast_member_id),
    CONSTRAINT FK_VIDEO_CAST_MEMBER_VIDEO FOREIGN KEY (video_id) REFERENCES video (id) ON DELETE CASCADE,
    CONSTRAINT FK_VIDEO_CAST_MEMBER_CAST_MEMBER FOREIGN KEY (cast_member_id) REFERENCES cast_member (id) ON DELETE CASCADE
);