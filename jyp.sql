-- 댓글 테이블

CREATE DATABASE IF NOT EXISTS `petfriend`;

USE `petfriend`;

CREATE TABLE IF NOT EXISTS `comments` (
	`id`		BIGINT NOT NULL AUTO_INCREMENT,
    `task_id`	BIGINT NOT NULL COMMENT 'tasks.id FK',
    `content` 	varchar(1000) not null comment '댓글 내용',
    `commenter` varchar(100) not null comment '댓글 작성자 표시명 또는 ID',
    primary key (`id`),
    key `idx_comment_task_id` (`task_id`),
    key `idx_comment_commenter` (`commenter`),
    constraint `fk_comment_post`
		foreign key (`task_id`) references `tasks` (`id`)
        on delete cascade
        on update cascade
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '댓글';
  
  
  select * from `comments`;