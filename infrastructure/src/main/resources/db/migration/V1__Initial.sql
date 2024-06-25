create table category (
    id varchar(36) not null PRIMARY KEY,
    name varchar(255) not null,
    description varchar(4000),
    active boolean not null default true,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    deleted_at datetime(6)
)