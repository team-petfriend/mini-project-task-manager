CREATE TABLE IF NOT EXISTS `users` (
	id BIGINT NOT NULL AUTO_INCREMENT,
    login_id VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT `uk_users_login_id` UNIQUE (login_id),
    CONSTRAINT `uk_users_email` UNIQUE (email),
    CONSTRAINT `uk_users_nickname` UNIQUE (nickname),
    CONSTRAINT `chk_users_gender` CHECK(gender IN ('MALE', 'FEMALE'))
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '사용자';
  
  CREATE TABLE tasks (
	id           BIGINT PRIMARY KEY AUTO_INCREMENT,
	project_id   BIGINT NOT NULL,
	title        VARCHAR(200) NOT NULL,
	description  TEXT,
	status       VARCHAR(250) NOT NULL DEFAULT'TODO',
	priority     VARCHAR(250) NOT NULL DEFAULT'MEDIUM',
	assignee_id  BIGINT NULL,
	due_date     DATE NULL,
	created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	CONSTRAINT fk_task_project  CHECK (status IN ('TODO','IN_PROGRESS','DONE')),
	CONSTRAINT fk_task_assignee CHECK (priority IN ('LOW','MEDIUM','HIGH')),

	INDEX idx_task_project_status (project_id, status),
	INDEX idx_task_assignee_due (assignee_id, due_date)
);

CREATE TABLE tags (
	id           BIGINT PRIMARY KEY AUTO_INCREMENT,
	name         VARCHAR(50) NOT NULL UNIQUE,
	color        VARCHAR(20) NULL,
	created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE task_tag (
	task_id  BIGINT NOT NULL,
	tag_id   BIGINT NOT NULL,
	PRIMARY KEY (task_id, tag_id),
	CONSTRAINT fk_tt_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
	CONSTRAINT fk_tt_tag  FOREIGN KEY (tag_id)  REFERENCES tags(id)  ON DELETE CASCADE
);