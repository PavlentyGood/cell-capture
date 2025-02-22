
create sequence player_id_seq;

create table parties
(
    id uuid primary key,
    started boolean not null,
    owner_id integer not null,
    player_limit integer not null,
    created timestamp not null default current_timestamp,
    updated timestamp not null default current_timestamp
);

create table players
(
    id integer primary key,
    name varchar(20) not null,
    party_id uuid not null references parties (id)
);
