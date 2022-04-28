create table tests
(
    test_id     serial  not null
        constraint tests_pk
            primary key,
    title       varchar not null,
    description varchar,
    category    varchar not null
);

create unique index tests_test_id_uindex
    on tests (test_id);


create table test_options
(
    option_id    serial  not null
        constraint test_options_pk
            primary key,
    option_value varchar not null,
    is_correct   boolean not null,
    test_id      int     not null
        constraint test_options_tests_test_id_fk
            references tests
            on update cascade on delete cascade
);

create unique index test_options_option_id_uindex
    on test_options (option_id);

create table test_tags
(
    tag_id    serial  not null
        constraint test_tags_pk
            primary key,
    tag_value varchar not null,
    test_id   int     not null
        constraint test_tags_tests_test_id_fk
            references tests
            on update cascade on delete cascade
);

create unique index test_tags_tag_id_uindex
    on test_tags (tag_id);

