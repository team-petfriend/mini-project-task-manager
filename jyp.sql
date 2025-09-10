-- 댓글 테이블

CREATE DATABASE IF NOT EXISTS `petfriend`;

USE `petfriend`;

CREATE TABLE IF NOT EXISTS `comments` (
	id			BIGINT NOT NULL AUTO_INCREMENT,
    task_id		BIGINT NOT NULL COMMENT 'tasks.id FK',
    user_id 	BIGINT NOT NULL,
    content 	VARCHAR(1000) NOT NULL COMMENT '댓글 내용',
    commenter 	VARCHAR(100) NOT NULL COMMENT '댓글 작성자 표시명 또는 ID',
    created_at	DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at 	DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY `idx_comment_task_id` (task_id),
    KEY `idx_comment_commenter` (commenter),
    CONSTRAINT `fk_comment_task` 	FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk_comment_user_id` FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '댓글';
  
  
  select * from `comments`;