CREATE DATABASE IF NOT EXISTS `petfriend`;

USE `petfriend`;

DROP TABLE IF EXISTS `task_tag`;
DROP TABLE IF EXISTS `tags`;
DROP TABLE IF EXISTS `projects`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `task_assignees`;
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `users`;


SELECT * FROM users;
-- 사용자 테이블
CREATE TABLE IF NOT EXISTS `users` (
    id 					BIGINT NOT NULL AUTO_INCREMENT  PRIMARY KEY,
    login_id 			VARCHAR(50) NOT NULL,
    password 			VARCHAR(255) NOT NULL,
    email 				VARCHAR(255) NOT NULL,
    nickname 			VARCHAR(50) NOT NULL,
    gender 				VARCHAR(10),
    created_at 			DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at 			DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT `uk_users_login_id` UNIQUE (login_id),
    CONSTRAINT `uk_users_email` UNIQUE (email),
    CONSTRAINT `uk_users_nickname` UNIQUE (nickname),
    CONSTRAINT `chk_users_gender` CHECK (gender IN ('MALE' , 'FEMALE'))
)  ENGINE=INNODB 
   DEFAULT CHARSET=UTF8MB4 
   COLLATE = UTF8MB4_UNICODE_CI 
   COMMENT='사용자';

-- 사용자 권한 테이블
   CREATE TABLE IF NOT EXISTS `user_roles`(
    user_id 				BIGINT NOT NULL,
    role 					VARCHAR(30) NOT NULL,
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_roles 	  UNIQUE (user_id, role),
    CONSTRAINT chk_user_roles_role CHECK (role IN ('USER','ADMIN'))
)ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '사용자 권한';
  

-- 프로젝트 테이블
-- owerId -> 관리자
CREATE TABLE IF NOT EXISTS `projects`(
    id 				BIGINT PRIMARY KEY AUTO_INCREMENT,
    owner_id  		BIGINT NOT NULL,
    tag_id			BIGINT NOT NULL,
    name 			VARCHAR(100) NOT NULL,
    created_at		DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at 		DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT `uk_projects_name` UNIQUE (name),
    CONSTRAINT `fk_projects_user_id` FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT `fk_projects_tag_id` FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE,
    INDEX idx_projects_name (name),
    INDEX idx_projects_createdAt (created_at)
)ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '프로젝트';


-- 담당자
CREATE TABLE IF NOT EXISTS `task_assignees` (
	task_id 			BIGINT NOT NULL,
	user_id 			BIGINT NOT NULL,
    PRIMARY KEY(task_id, user_id),
	CONSTRAINT fk_task_assignees_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_assignees_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
)ENGINE=InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_unicode_ci
COMMENT = '담당자';



-- 할 일 테이블
--  `task_assignees`  수정필요 
CREATE TABLE IF NOT EXISTS `tasks` (
	id           		BIGINT PRIMARY KEY AUTO_INCREMENT,
	project_id   		BIGINT NOT NULL,
	title        		VARCHAR(200) NOT NULL,
	description  		TEXT,
	task_status       	VARCHAR(250) NOT NULL DEFAULT'TODO',
	priority     		VARCHAR(250) NOT NULL DEFAULT'MEDIUM',
	due_date     		DATE NULL,
	created_at	        DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at 			DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
	CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_project FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT chk_tasks_status CHECK (status IN ('TODO','IN_PROGRESS','DONE')),
	CONSTRAINT chk_tasks_assignees CHECK (priority IN ('LOW','MEDIUM','HIGH')),
	INDEX idx_tasks_projects_status (project_id, status),
	INDEX idx_tasks_assignees_due (due_date)
)ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '할일';	

-- task history
CREATE TABLE IF NOT EXISTS `task_history`(
	id			BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id 	BIGINT NOT NULL,
    actor_id	BIGINT NOT NULL,
    field		ENUM('STATUS','ASSIGNEE','DUE_DATE','TITLE','PRIORITY','TAGS') NOT NULL,
    old_value	VARCHAR(255),
    new_value	VARCHAR(255),
    created_at	DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	CONSTRAINT fk_task_history_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_history_actor FOREIGN KEY (actor_id) REFERENCES users(id)
);

-- 태그 테이블
CREATE TABLE IF NOT EXISTS `tags` (
	id          	BIGINT PRIMARY KEY AUTO_INCREMENT,
	name        	VARCHAR(50) NOT NULL UNIQUE,
	color        	VARCHAR(20) NOT NULL NULL,
    CONSTRAINT `uk_tags_name` UNIQUE (name)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '태그';

-- 할일 태그
CREATE TABLE IF NOT EXISTS `task_tag` (
	id 				bigint auto_increment,
	task_id  		BIGINT,
	tag_id   		BIGINT,
	PRIMARY KEY (id),
	CONSTRAINT fk_task_tag_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
	CONSTRAINT fk_task_tag_tag  FOREIGN KEY (tag_id)  REFERENCES tags(id)  ON DELETE CASCADE,
    constraint uk_task_tag_task unique (task_id),
    constraint uk_task_tag_task unique (tag_id),
	task_id  		BIGINT NOT NULL,
	tag_id   		BIGINT NOT NULL,
	PRIMARY KEY (task_id, tag_id),
	CONSTRAINT fk_task_tag_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
	CONSTRAINT fk_task_tag_tag  FOREIGN KEY (tag_id)  REFERENCES tags(id)  ON DELETE CASCADE
)ENGINE=InnoDB
 DEFAULT CHARSET = utf8mb4
 COLLATE = utf8mb4_unicode_ci
 COMMENT = '할일 태그';


SELECT * FROM `task_tag`;
SELECT * FROM `tags`;
SELECT * FROM `tasks`;
SELECT * FROM `projects`;
SELECT * FROM `user_roles`;
SELECT * FROM `users`;

CREATE DATABASE IF NOT EXISTS `petfriend`;

USE `petfriend`;

-- 댓글 테이블
CREATE TABLE IF NOT EXISTS `comments` (
	id			BIGINT NOT NULL AUTO_INCREMENT,
    task_id		BIGINT NOT NULL COMMENT 'tasks.id FK',
    content 	VARCHAR(500) NOT NULL COMMENT '댓글 내용',
    commenter 	BIGINT NOT NULL COMMENT '댓글 작성자 표시명 또는 ID',
    created_at	DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at 	DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY `idx_comment_task_id` (task_id),
    KEY `idx_comment_commenter` (commenter),
    CONSTRAINT `fk_comments_task` 	FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE ON UPDATE CASCADE
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