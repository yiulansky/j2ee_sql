create table game_record
(
    id       int auto_increment
        primary key,
    player1  varchar(50)   not null,
    player2  varchar(50)   not null,
    winner   varchar(50)   null,
    duration int default 0 null comment '对局时长（秒）'
);

create table game_save
(
    id          int auto_increment
        primary key,
    save_name   varchar(100)                        not null,
    board_state text                                not null,
    save_time   timestamp default CURRENT_TIMESTAMP null,
    duration    int       default 0                 null comment '对局时长（秒）'
);

create table user
(
    id       bigint auto_increment comment '主键ID'
        primary key,
    username varchar(50)  not null comment '用户名',
    password varchar(255) not null comment '密码（加密存储）',
    constraint uk_username
        unique (username) comment '用户名唯一索引'
)
    comment '用户表';


