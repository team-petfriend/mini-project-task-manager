CREATE DATABASE IF NOT EXISTS `petfriend`;

USE `petfriend`;

DROP TABLE IF EXISTS `task_tag`;
DROP TABLE IF EXISTS `tags`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `projects`;
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
CREATE TABLE IF NOT EXISTS `projects`(
    id 				BIGINT PRIMARY KEY AUTO_INCREMENT,
    owner_id  		BIGINT NOT NULL,
    name 			VARCHAR(100) NOT NULL,
    created_at		DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at 		DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT `uk_projects_name` UNIQUE (name),
    CONSTRAINT `fk_project_user_id` FOREIGN KEY (owner_id) REFERENCES users (id),
    INDEX idx_project_name (name),
    INDEX idx_project_createdAt (created_at)
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
	status       		VARCHAR(250) NOT NULL DEFAULT'TODO',
	priority     		VARCHAR(250) NOT NULL DEFAULT'MEDIUM',
	assignee_id  		BIGINT NOT NULL,
	due_date     		DATE NULL,
	created_at	        DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at 			DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
	CONSTRAINT fk_task_projects FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_projects FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT chk_task_status CHECK (status IN ('TODO','IN_PROGRESS','DONE')),
	CONSTRAINT chk_task_assignee CHECK (priority IN ('LOW','MEDIUM','HIGH')),
	INDEX idx_task_project_status (project_id, status),
	INDEX idx_task_assignee_due (assignee_id, due_date)
)ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '할일';

-- 태그 테이블
CREATE TABLE IF NOT EXISTS `tags` (
	id          	BIGINT PRIMARY KEY AUTO_INCREMENT,
	name        	VARCHAR(50) NOT NULL UNIQUE,
	color        	VARCHAR(20) NULL,
	created_at		DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
	updated_at 		DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '태그';

-- 할일 태그
CREATE TABLE IF NOT EXISTS `task_tag` (
	task_id  		BIGINT NOT NULL,
	tag_id   		BIGINT NOT NULL,
	PRIMARY KEY (task_id, tag_id),
	CONSTRAINT fk_tt_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
	CONSTRAINT fk_tt_tag  FOREIGN KEY (tag_id)  REFERENCES tags(id)  ON DELETE CASCADE
)ENGINE=InnoDB
 DEFAULT CHARSET = utf8mb4
 COLLATE = utf8mb4_unicode_ci
 COMMENT = '할일 태그';
