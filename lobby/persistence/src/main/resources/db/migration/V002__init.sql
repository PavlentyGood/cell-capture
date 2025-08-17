
create table outbox
(
    id bigserial primary key,
    aggregate_id varchar not null,
    status varchar not null,
    event_type varchar not null,
    body json not null,
    created timestamp not null,
    updated timestamp not null
);

create index outbox_index on outbox (status, created)
    where status = 'PENDING';
