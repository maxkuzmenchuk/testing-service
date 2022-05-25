create table tests
(
    test_id       serial
        constraint tests_pk
            primary key,
    test_title    varchar   not null,
    description   varchar,
    category      varchar   not null,
    creating_date date      not null,
    creator_id    int       not null,
    is_archived   boolean   not null,
    updating_date timestamp not null
);

create unique index tests_test_id_uindex
    on tests (test_id);

create table test_questions
(
    question_id    serial
        constraint test_questions_pk
            primary key,
    question_title varchar   not null,
    description    varchar,
    updating_date  timestamp not null,
    test_id        int
        constraint test_questions_tests_test_id_fk
            references tests
            on update cascade on delete cascade
);

create unique index test_questions_question_id_uindex
    on test_questions (question_id);

create table question_options
(
    option_id     serial
        constraint question_options_pk
            primary key,
    option_value  varchar   not null,
    is_correct    boolean   not null,
    updating_date timestamp not null,
    question_id   int
        constraint question_options_test_questions_question_id_fk
            references test_questions
            on update cascade on delete cascade
);

create unique index question_options_option_id_uindex
    on question_options (option_id);

create table test_tags
(
    tag_id        serial    not null
        constraint test_tags_pk
            primary key,
    tag_value     varchar   not null,
    updating_date timestamp not null,
    test_id       int       not null
        constraint test_tags_tests_test_id_fk
            references tests
            on update cascade on delete cascade
);

create unique index test_tags_tag_id_uindex
    on test_tags (tag_id);

create table test_results
(
    result_id                  serial
        constraint test_results_pk
            primary key,
    user_id                    int       not null,
    test_id                    int       not null
        constraint test_results_tests_test_id_fk
            references tests
            on update cascade on delete cascade,
    correct_answers_count      int       not null,
    correct_answers_percentage int       not null,
    testing_date               timestamp not null
);

create unique index test_results_result_id_uindex
    on test_results (result_id);

create table update_log
(
    updating_log_id  serial
        constraint update_log_pk
            primary key,
    entity_id        int        not null,
    entity_type      varchar(9) not null,
    operation_type   char(9)    not null,
    updated_field    varchar    not null,
    old_value        varchar    not null,
    new_value        varchar    not null,
    updating_date    timestamp  not null,
    updating_user_id int        not null
);

create unique index update_log_update_log_id_uindex
    on update_log (updating_log_id);

create table test_archive
(
    archive_id        serial
        constraint test_archive_pk
            primary key,
    archiving_date    date,
    archiving_user_id int not null,
    archiving_test_id int not null
);

create unique index test_archive_archive_id_uindex
    on test_archive (archive_id);

create table files
(
    file_id         serial
        constraint files_pk
            primary key,
    file_kind       varchar(6) not null,
    test_id         int        not null,
    file_name       varchar    not null,
    file_type       varchar    not null,
    file_source_url varchar    not null,
    creating_date   timestamp  not null
);

create unique index files_file_id_uindex
    on files (file_id);






