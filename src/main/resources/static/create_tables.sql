create table tests
(
    test_id     serial
        constraint tests_pk
            primary key,
    title       varchar not null,
    description varchar,
    category    varchar not null
);

create unique index tests_test_id_uindex
    on tests (test_id);

create table test_questions
(
    question_id serial
        constraint test_questions_pk
            primary key,
    title       varchar not null,
    description varchar,
    test_id     int
        constraint test_questions_tests_test_id_fk
            references tests
            on update cascade on delete cascade
);

create unique index test_questions_question_id_uindex
    on test_questions (question_id);

create table question_options
(
    option_id    serial
        constraint question_options_pk
            primary key,
    option_value varchar not null,
    is_correct   boolean not null,
    question_id  int
        constraint question_options_test_questions_question_id_fk
            references test_questions
            on update cascade on delete cascade
);

create unique index question_options_option_id_uindex
    on question_options (option_id);

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
