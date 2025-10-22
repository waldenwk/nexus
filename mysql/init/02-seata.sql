-- 创建Seata数据库
CREATE DATABASE IF NOT EXISTS seata DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE seata;

-- 全局事务表
CREATE TABLE IF NOT EXISTS `global_table` (
  `xid` VARCHAR(128) NOT NULL,
  `transaction_id` BIGINT DEFAULT NULL,
  `status` TINYINT NOT NULL,
  `application_id` VARCHAR(32) DEFAULT NULL,
  `transaction_service_group` VARCHAR(32) DEFAULT NULL,
  `transaction_name` VARCHAR(128) DEFAULT NULL,
  `timeout` INT DEFAULT NULL,
  `begin_time` BIGINT DEFAULT NULL,
  `application_data` VARCHAR(2000) DEFAULT NULL,
  `gmt_create` DATETIME DEFAULT NULL,
  `gmt_modified` DATETIME DEFAULT NULL,
  PRIMARY KEY (`xid`),
  KEY `idx_gmt_modified_status` (`gmt_modified`, `status`),
  KEY `idx_transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 分支事务表
CREATE TABLE IF NOT EXISTS `branch_table` (
  `branch_id` BIGINT NOT NULL,
  `xid` VARCHAR(128) NOT NULL,
  `transaction_id` BIGINT DEFAULT NULL,
  `resource_group_id` VARCHAR(32) DEFAULT NULL,
  `resource_id` VARCHAR(256) DEFAULT NULL,
  `branch_type` VARCHAR(8) DEFAULT NULL,
  `status` TINYINT DEFAULT NULL,
  `client_id` VARCHAR(64) DEFAULT NULL,
  `application_data` VARCHAR(2000) DEFAULT NULL,
  `gmt_create` DATETIME(6) DEFAULT NULL,
  `gmt_modified` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`branch_id`),
  KEY `idx_xid` (`xid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 全局锁表
CREATE TABLE IF NOT EXISTS `lock_table` (
  `row_key` VARCHAR(128) NOT NULL,
  `xid` VARCHAR(96) DEFAULT NULL,
  `transaction_id` BIGINT DEFAULT NULL,
  `branch_id` BIGINT NOT NULL,
  `resource_id` VARCHAR(256) DEFAULT NULL,
  `table_name` VARCHAR(32) DEFAULT NULL,
  `pk` VARCHAR(36) DEFAULT NULL,
  `gmt_create` DATETIME DEFAULT NULL,
  `gmt_modified` DATETIME DEFAULT NULL,
  PRIMARY KEY (`row_key`),
  KEY `idx_branch_id` (`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;