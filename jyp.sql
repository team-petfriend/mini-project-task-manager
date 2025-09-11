CREATE DATABASE IF NOT EXISTS `petfriend`;

USE `petfriend`;

-- 댓글 테이블
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
	CONSTRAINT `fk_comment_user_id` FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '댓글';
  
  
  select * from `comments`;
  
-- 옵션: 단순 알림 테이블(읽음 여부)
CREATE TABLE IF NOT EXISTS `notifications` (
	id            BIGINT PRIMARY KEY AUTO_INCREMENT,
	user_id       BIGINT NOT NULL,
	type          VARCHAR(30) NOT NULL,
	ref_type      VARCHAR(20) NOT NULL,
	ref_id        BIGINT NOT NULL,
	message       VARCHAR(255) NOT NULL,
	is_read       TINYINT(1) NOT NULL DEFAULT 0,
	created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT fk_notify_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT chk_notifications_type CHECK(type IN('TASK_ASSIGNED','MENTION','COMMENT','STATUS_CHANGED')),
    CONSTRAINT chk_notifications_ref_type CHECK(ref_type IN('TASK','COMMENT')),
	INDEX idx_notify_user_read (user_id, is_read)
)ENGINE=InnoDB
 DEFAULT CHARSET = utf8mb4
 COLLATE = utf8mb4_unicode_ci
 COMMENT = '알림';
 
 select * from `notifications`