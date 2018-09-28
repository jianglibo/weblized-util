CREATE TABLE reuseable_cron (
  id INT AUTO_INCREMENT PRIMARY KEY,
  description     VARCHAR(200),
  expression      VARCHAR(200)  NOT NULL,
  created_at TIMESTAMP(2),
  CONSTRAINT unique_rc_expression UNIQUE (expression)
) ENGINE=InnoDB;

 CREATE TABLE user_account (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(200)  NOT NULL,
  mobile VARCHAR(64)  NOT NULL,
  email VARCHAR(200)  NOT NULL,
  password VARCHAR(200)  NOT NULL,
  account_non_expired BOOLEAN NOT NULL,
  account_non_locked BOOLEAN NOT NULL,
  credentials_non_expired BOOLEAN NOT NULL,
  enabled BOOLEAN NOT NULL,
  description VARCHAR(200),
  created_at TIMESTAMP(2),
  CONSTRAINT unique_ua_email UNIQUE (email),
  CONSTRAINT unique_ua_name UNIQUE (username),
  CONSTRAINT unique_ua_mobile UNIQUE (mobile)
) ENGINE=InnoDB;

 CREATE TABLE user_role (
  id INT AUTO_INCREMENT PRIMARY KEY,
  role VARCHAR(200)  NOT NULL,
  description VARCHAR(200),
  created_at TIMESTAMP(2),
  CONSTRAINT unique_ur_role UNIQUE (role)
) ENGINE=InnoDB;

CREATE TABLE user_and_role (
  user_id INT  NOT NULL,
  role_id INT  NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES user_account (id),
  CONSTRAINT fk_ur_role FOREIGN KEY (role_id)  REFERENCES user_role (id)
) ENGINE=InnoDB;


CREATE TABLE user_grp
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  ename VARCHAR(256),
  msgkey VARCHAR(256),
  created_at TIMESTAMP(2),
  CONSTRAINT unique_usergrp_ename UNIQUE (ename)
) ENGINE=InnoDB;

CREATE TABLE usergrp_and_user (
  user_id INT  NOT NULL,
  grp_id    INT  NOT NULL,
  PRIMARY KEY (user_id, grp_id),
  CONSTRAINT fk_ugu_user     FOREIGN KEY (user_id)  REFERENCES user_account (id),
  CONSTRAINT fk_ugu_grp       FOREIGN KEY (grp_id)    REFERENCES user_grp   (id)
) ENGINE=InnoDB;

CREATE TABLE big_ob
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARChAR(256) NOT NULL,
  description VARCHAR(256),
  content BLOB,
  created_at TIMESTAMP(2),
  CONSTRAINT unique_big_ob_name UNIQUE (name)
) ENGINE=InnoDB;

CREATE TABLE job_log
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  job_class VARCHAR(128),
  ctx VARCHAR(256),
  exp BLOB,
  created_at TIMESTAMP(2)
) ENGINE=InnoDB;

CREATE TABLE key_value
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  item_key VARCHAR(256) NOT NULL,
  item_value VARCHAR(256) NOT NULL,
  created_at TIMESTAMP(2),
  CONSTRAINT unique_key_value_key UNIQUE (item_key)
) ENGINE=InnoDB;

CREATE INDEX job_log_created_at_idx ON job_log(created_at);