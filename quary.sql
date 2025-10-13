CREATE DATABASE IF NOT EXISTS `mini_project_task_manager`;
DROP DATABASE `mini_project_task_manager`;
USE `mini_project_task_manager`;

DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `projects`;
DROP TABLE IF EXISTS `tags`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `task_assignees`;
DROP TABLE IF EXISTS `task_history`;
DROP TABLE IF EXISTS `task_tag`;
DROP TABLE IF EXISTS `notifications`;

SELECT * FROM `users`;
SELECT * FROM `roles`;
SELECT * FROM `user_roles`;
SELECT * FROM `projects`;
SELECT * FROM `tags`;
SELECT * FROM `tasks`;
SELECT * FROM `task_assignees`;
SELECT * FROM `task_history`;
SELECT * FROM `task_tag`;
SELECT * FROM `comments`;
SELECT * FROM `notifications`;

SELECT 
    tg.project_id,  -- tg.project.id
    tg.name,
    tg.color,
    ta.title,
    ta.description,
    ta.task_status,
    ta.due_date,
    ta.created_at,
    ta.updated_at
FROM task_tag tt
INNER JOIN tags tg ON tt.tag_id = tg.id
INNER JOIN tasks ta ON tt.task_id = ta.id
WHERE tg.project_id = 3;  


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
   
SELECT * FROM `users`;




-- 권한 코드 테이블
CREATE TABLE IF NOT EXISTS `roles` (
	role_name VARCHAR(30) PRIMARY KEY
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '권한 코드(USER, MANAGER, OWNER 등)';
  
SELECT * FROM `roles`;
  


-- 사용자 권한 테이블
CREATE TABLE IF NOT EXISTS `user_roles` (
	user_id 	BIGINT NOT NULL,
    role_name 	VARCHAR(30) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_name) REFERENCES roles(role_name)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '사용자 권한 매핑';

SELECT * FROM `user_roles`;



-- 프로젝트 테이블
-- ownerId -> 관리자
CREATE TABLE IF NOT EXISTS `projects`(
    id 				BIGINT PRIMARY KEY AUTO_INCREMENT,
    owner_id  		BIGINT NOT NULL,
    name 			VARCHAR(100) NOT NULL,
    created_at		DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at 		DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT `uk_projects_name` UNIQUE (name),
    CONSTRAINT `fk_projects_user_id` FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE,
    INDEX idx_projects_name (name),
    INDEX idx_projects_createdAt (created_at)
)ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '프로젝트';
  
SELECT * FROM `projects`;



-- 태그 테이블
CREATE TABLE IF NOT EXISTS `tags` (
	id          	BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id		BIGINT NOT NULL,
	name        	VARCHAR(50) NOT NULL,
	color        	VARCHAR(20) NOT NULL,
    CONSTRAINT fk_tag_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
	 CONSTRAINT uk_tags_name UNIQUE (name, project_id)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '태그';
  
SELECT * FROM `tags`;



-- 할 일 테이블
CREATE TABLE IF NOT EXISTS `tasks` (
	id           		BIGINT PRIMARY KEY AUTO_INCREMENT,
	project_id   		BIGINT NOT NULL,
	title        		VARCHAR(200) NOT NULL,
	description  		LONGTEXT,
	task_status       	VARCHAR(250) NOT NULL DEFAULT'TODO',
	priority     		VARCHAR(250) NOT NULL DEFAULT'MEDIUM',
	due_date     		DATE NULL,
	created_at	        DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at 			DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
	CONSTRAINT fk_tasks_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
	CONSTRAINT chk_tasks_status CHECK (task_status IN ('TODO','IN_PROGRESS','DONE')),
	CONSTRAINT chk_tasks_assignees CHECK (priority IN ('LOW','MEDIUM','HIGH')),
	INDEX idx_tasks_projects_status (project_id, task_status),
	INDEX idx_tasks_assignees_due (due_date)
)ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '할일';
  
SELECT * FROM `tasks`;



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

SELECT * FROM `task_assignees`;



-- task history
CREATE TABLE IF NOT EXISTS `task_history`(
	id			BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id 	BIGINT NOT NULL,
    actor_id	BIGINT NOT NULL,
    field		ENUM('STATUS','ASSIGNEE','DUE_DATE','TITLE','PRIORITY','TAGS') NOT NULL,
    old_value	VARCHAR(255),
    new_value	VARCHAR(255),
    created_at	DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at 	DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
	CONSTRAINT fk_task_history_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_history_actor FOREIGN KEY (actor_id) REFERENCES users(id)
)ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = 'Task History(logs)';
drop table task_history;
SELECT * FROM `task_history`;




-- 할일 태그
CREATE TABLE IF NOT EXISTS `task_tag` (
	id 				bigint auto_increment,
	task_id  		BIGINT NOT NULL,
	tag_id   		BIGINT NOT NULL,
	PRIMARY KEY (id),
	CONSTRAINT fk_task_tag_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
	CONSTRAINT fk_task_tag_tag  FOREIGN KEY (tag_id)  REFERENCES tags(id)  ON DELETE CASCADE
)ENGINE=InnoDB
 DEFAULT CHARSET = utf8mb4
 COLLATE = utf8mb4_unicode_ci
 COMMENT = '할일 태그';
 
SELECT * FROM `task_tag`;



-- 댓글 테이블
CREATE TABLE IF NOT EXISTS `comments` (
	id			BIGINT NOT NULL AUTO_INCREMENT,
    task_id		BIGINT NOT NULL COMMENT 'tasks.id FK',
    content 	VARCHAR(500) NOT NULL COMMENT '댓글 내용',
    commenter 	BIGINT NOT NULL COMMENT '댓글 작성자 표시명 또는 ID',
    created_at	DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at 	DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    INDEX `idx_comment_task_id` (task_id),
    INDEX `idx_comment_commenter` (commenter),
    CONSTRAINT `fk_comments_task` 	FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_comments_user` 	FOREIGN KEY (commenter) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '댓글';
  
SELECT * FROM `comments`;



-- 옵션: 단순 알림 테이블(읽음 여부) 
CREATE TABLE IF NOT EXISTS `notifications` (
	id            BIGINT PRIMARY KEY AUTO_INCREMENT,
	user_id       BIGINT NOT NULL,
	type          VARCHAR(30) NOT NULL,
	ref_type      VARCHAR(20) NOT NULL,
	ref_id        BIGINT NOT NULL,
	message       VARCHAR(255) NOT NULL,
	is_read       TINYINT(1) NOT NULL DEFAULT 0,
	created_at    DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	CONSTRAINT fk_notify_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT chk_notifications_type CHECK(type IN('TASK_ASSIGNED','MENTION','COMMENT','STATUS_CHANGED')),
    CONSTRAINT chk_notifications_ref_type CHECK(ref_type IN('TASK','COMMENT')),
	INDEX idx_notify_user_read (user_id, is_read)
)ENGINE=InnoDB
 DEFAULT CHARSET = utf8mb4
 COLLATE = utf8mb4_unicode_ci
 COMMENT = '알림';
 
SELECT * FROM `notifications`;




SELECT * FROM `users`;
SELECT * FROM `roles`;
SELECT * FROM `user_roles`;
SELECT * FROM `projects`;
SELECT * FROM `tags`;
SELECT * FROM `tasks`;
SELECT * FROM `task_assignees`;
SELECT * FROM `task_history`;
SELECT * FROM `task_tag`;
SELECT * FROM `comments`;
SELECT * FROM `notifications`;

SELECT * FROM `tasks` where project_id = 1;
SELECT * FROM `tags` where project_id = 2;

SELECT * FROM `task_tag`;


CREATE OR REPLACE VIEW `front_view` AS
SELECT distinct task_id, actor_id, field, old_value, new_value, created_at, updated_at 
from `task_history`
group by task_id
order by updated_at desc;

select * from`front_view`;





SHOW TABLES;
SHOW CREATE TABLE comments;

 # ==========================================================Sample===========================================================================
INSERT INTO `users` (login_id, password, email, nickname, gender) VALUES
    ('user123', 'user123123', 'user123@example.com', 'user123', NULL),
	('manager123', 'manager123123', 'manager123@example.com', 'manager123', NULL),
	('admin123', 'admin123123', 'admin123@example.com', 'admin123', NULL),
    ('user456', 'user456456', 'user456@example.com', 'user456', 'MALE'),
	('manager456', 'manager456456', 'manager456@example.com', 'manager456', 'FEMALE'),
	('admin456', 'admin456456', 'admin456@example.com', 'admin456', 'MALE');

 
INSERT INTO roles (role_name) VALUES
	('USER'),
    ('MANAGER'),
    ('ADMIN')
    # 이미 값이 있는 경우(DUPLICATE, 중복)
    # , 에러 대신 그대로 유지할 것을 설정 
	ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);
 
 
INSERT INTO user_roles (user_id, role_name) VALUES
	(1, 'USER'),
    (2, 'MANAGER'),
    (3, 'ADMIN'),
    (4, 'USER'),
    (5, 'MANAGER'),
    (6, 'ADMIN')
	ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);
    
INSERT INTO `projects` (owner_id, name) VALUES
	(3, 'TEST123'),
    (6, 'TEST456')
    

