
create table parties
(
    id UUID primary key,
    completed boolean not null,
    first_dice integer,
    second_dice integer,
    owner_id integer not null,
    current_player_id integer not null,
    created timestamp not null default current_timestamp,
    updated timestamp not null default current_timestamp
);

create table players
(
    id integer primary key,
    name varchar(20) not null,
    party_id UUID not null references parties (id)
);

create table cells
(
    player_id integer not null references players (id),
    party_id UUID not null references parties (id),
    x integer not null,
    y integer not null,
    primary key (player_id, x, y)
);
