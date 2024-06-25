create table genre (
    id varchar(36) not null PRIMARY KEY,
    name varchar(255) not null,
    active boolean not null default true,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    deleted_at datetime(6)
);

create table genre_category (
    genre_id varchar(36) not null,
    category_id varchar(36) not null,

    CONSTRAINT idx_genre_category UNIQUE (genre_id, category_id),
    CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genre (id) ON DELETE CASCADE,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE,
    PRIMARY KEY (genre_id, category_id)
);