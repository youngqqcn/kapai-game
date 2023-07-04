# create database if not exists `game_card` default character set utf8mb4 collate utf8mb4_unicode_ci;

create table if not exists `card_model`
(
    `id`          bigint unsigned not null auto_increment primary key,
    `name`        varchar(100)    not null comment '卡牌名称',
    `unit_price`  int             not null default 0 comment '单价',
    `power`       int             not null default 0 comment '铸造卡牌赠送的算力',
    `output`      int             not null default 0 comment '铸造卡牌产出',
    `ep`          int             not null default 0 comment '铸造需要的ep数量',
    `zt_power`    int             not null default 0 comment '直推赠送的算力',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_time` timestamp       not null default current_timestamp comment '创建时间'
) engine = innodb
  default charset = utf8mb4 comment '卡牌模型';

create table if not exists `level_model`
(
    `id`            bigint unsigned not null primary key,
    `name`          varchar(100)    not null comment '名称',
    `card_model_id` bigint unsigned not null comment '此等级需要持有的卡牌id',
    `small_power`   bigint          not null default 0 comment '此等级需要的小区算力',
    `weight`        decimal(2, 2)   not null default 0 comment '加权',
    `upper_limit`   int             not null default 0 comment '日产出上限',
    `update_time`   timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_time`   timestamp       not null default current_timestamp comment '创建时间',
    foreign key (`card_model_id`) references `card_model` (`id`)
) engine = innodb
  default charset = utf8mb4 comment '等级模型';

create table if not exists `wallet`
(
    `id`           bigint unsigned not null auto_increment primary key,
    `wallet`       varchar(100)    not null comment '钱包地址',
    `zt_power`     bigint          not null default 0 comment '直推算力',
    `status`       tinyint         not null default 0 not null comment '1:禁用',
    `lock_token_a` decimal(38, 18) not null default 0 comment '锁仓A代币数量',
    `node`         tinyint         not null default 0 not null comment '节点人身份 1:小节点 2:大节点 3:超级节点',
    `update_time`  timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_time`  timestamp       not null default current_timestamp comment '创建时间'
) engine = innodb
  default charset = utf8mb4 comment '钱包';
create index index_wallet_wallet on `wallet` (`wallet`);

insert into `wallet`(`id`, `wallet`)
values (1, '0x0101010101010101010101010101010101010101');
insert into `wallet`(`id`, `wallet`)
values (2, '0x1010101010101010101010101010101010101010');

create table if not exists `wallet_power`
(
    `wallet_id`   bigint unsigned not null primary key comment '钱包id',
    `small_power` bigint          not null default 0 comment '小区业绩',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_time` timestamp       not null default current_timestamp comment '创建时间'
) engine = innodb
  default charset = utf8mb4 comment '用户算力';

create table if not exists `wallet_card`
(
    `id`            bigint unsigned not null auto_increment primary key,
    `wallet_id`     bigint unsigned not null comment '钱包id',
    `card_model_id` bigint unsigned not null comment '卡牌模型id',
    `power`         bigint          not null default 0 comment '算力',
    `output`        decimal(38, 18) not null default 0 comment '产出',
    `days`          int             not null default 0 comment '持有天数',
    `status`        tinyint         not null default 0 not null comment '0:正在释放 1:释放完毕',
    `update_time`   timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_time`   timestamp       not null default current_timestamp comment '创建时间',
    foreign key (`wallet_id`) references `wallet` (`id`),
    foreign key (`card_model_id`) references `card_model` (`id`)
) engine = innodb
  default charset = utf8mb4 comment '钱包持有的卡牌';

create table if not exists `relation`
(
    `wallet_id`     bigint unsigned not null primary key default 0 comment '钱包id',
    `superior_id`   bigint unsigned not null             default 0 comment '上级',
    `relation_path` text            not null comment '关系链',
    `update_time`   timestamp       not null             default current_timestamp on update current_timestamp comment '更新时间',
    `create_time`   timestamp       not null             default current_timestamp comment '创建时间',
    foreign key (`wallet_id`) references `wallet` (`id`),
    foreign key (`superior_id`) references `wallet` (`id`)
) engine = innodb
  default charset = utf8mb4 comment '推荐关系';

insert into `relation`(`wallet_id`, `superior_id`, `relation_path`)
values (2, 1, '1');

create table if not exists `mold_order`
(
    `id`            bigint unsigned not null auto_increment primary key,
    `wallet_id`     bigint unsigned not null comment '钱包id',
    `card_model_id` bigint unsigned not null comment '卡牌模型id',
    `type`          tinyint         not null comment '类型 0: 不使用EP  1: 使用EP',
    `price`         decimal(38, 18) not null default 0 comment '价格',
    `token_a`       decimal(38, 18) not null default 0 comment 'A代币数量',
    `ep`            decimal(38, 18) not null default 0 comment 'ep数量',
    `tx_hash`       varchar(100)    not null default '' comment '交易hash',
    `status`        tinyint         not null default 0 not null comment '状态 -1失败 0交易中 1已完成',
    `card_data`     text comment '卡牌数据',
    `update_time`   timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_time`   timestamp       not null default current_timestamp comment '创建时间',
    foreign key (`wallet_id`) references `wallet` (`id`),
    foreign key (`card_model_id`) references `card_model` (`id`)
) engine = innodb
  default charset = utf8mb4 comment '铸造订单';

create table if not exists `withdraw_order`
(
    `id`          bigint unsigned not null auto_increment primary key,
    `wallet_id`   bigint unsigned not null comment '钱包id',
    `token`       varchar(100)    not null default '' comment 'token',
    `token_name`  varchar(100)    not null default '' comment '名称',
    `amount`      decimal(38, 18) not null default 0 comment '数量',
    `tx_hash`     varchar(100)    not null default '' comment '交易hash',
    `status`      tinyint         not null default 0 not null comment '状态 -1失败 0交易中 1已完成',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    foreign key (`wallet_id`) references `wallet` (`id`)
) engine = innodb
  default charset = utf8mb4 comment '签到提现订单';

create table if not exists `asset_log`
(
    `id`            bigint unsigned not null auto_increment primary key,
    `wallet_id`     bigint unsigned not null comment '钱包id',
    `type`          tinyint         not null default 0 comment '资产类型 1:算力 2:a代币 3:b代币 4:业绩 5:ep',
    `card_model_id` bigint unsigned not null default 0 comment '卡牌id',
    `before_amount` decimal(38, 18) not null default 0 comment '变更前数量',
    `after_amount`  decimal(38, 18) not null default 0 comment '变更后数量',
    `amount`        decimal(38, 18) not null default 0 comment '变更数量',
    `source`        tinyint         not null default 0 comment '[{"name":"算力","type":1,"source":{"1":"铸造","2":"下级铸造获得奖励"}},{"name":"a代币","type":2,"source":{"1":"","2":""}},{"name":"b代币","type":3,"source":{"1":"","2":""}},{"name":"业绩","type":4,"source":{"1":"","2":""}},{"name":"ep","type":5,"source":{"1":"兑换","2":"铸造","3":"铸造失败回退"}}]',
    `extra`         text comment '附带信息',
    `create_time`   timestamp       not null default current_timestamp comment '创建时间',
    foreign key (`wallet_id`) references `wallet` (`id`)
) engine = innodb
  default charset = utf8mb4 comment '资产变更记录';
create index index_asset_log_link_id on `asset_log` (`card_model_id`);

create table if not exists `check_in`
(
    `id`        bigint unsigned not null auto_increment primary key,
    `wallet_id` bigint unsigned not null comment '钱包id',
    `type`      tinyint         not null default 0 comment '类型 1:tokena 2:tokenb',
    `amount`    decimal(38, 18) not null default 0 comment '数量',
    `status`    tinyint         not null default 0 comment '状态 0未签到 1已签到',
    `date`      date            not null comment '日期',
    foreign key (`wallet_id`) references `wallet` (`id`)
) engine = innodb
  default charset = utf8mb4 comment '签到';

create table if not exists `token_destroy`
(
    `id`             bigint unsigned not null auto_increment primary key,
    `token`          varchar(100)    not null comment 'token',
    `day`            date            not null comment '日期',
    `last_amount`    decimal(38, 18) not null default 0 comment '变更前数量',
    `current_amount` decimal(38, 18) not null default 0 comment '变更后数量',
    `create_time`    timestamp       not null default current_timestamp comment '创建时间'
) engine = innodb
  default charset = utf8mb4 comment '资产变更记录';

create table if not exists `buy_node_order`
(
    `id`          bigint unsigned not null auto_increment primary key,
    `wallet_id`   bigint unsigned not null comment '钱包id',
    `node`        tinyint         not null default 0 comment '节点',
    `price`       int             not null default 0 comment '购买金额',
    `period`      int             not null default 0 comment '购买期数',
    `ep`          int             not null default 0 comment '赠送EP数量',
    `tx_hash`     varchar(100)    not null default '' comment '交易hash',
    `status`      tinyint         not null default 0 not null comment '状态 -1失败 0交易中 1已完成',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    foreign key (`wallet_id`) references `wallet` (`id`)
) engine = innodb
  default charset = utf8mb4 comment '购买节点订单';

insert into `card_model` (`id`, `name`, `unit_price`, `power`, `output`, `ep`, `zt_power`)
values (1, '青铜令牌', 100, 100, 200, 34, 20);
insert into `card_model` (`id`, `name`, `unit_price`, `power`, `output`, `ep`, `zt_power`)
values (2, '白银令牌', 500, 500, 1250, 165, 125);
insert into `card_model` (`id`, `name`, `unit_price`, `power`, `output`, `ep`, `zt_power`)
values (3, '黄金令牌', 2000, 2000, 6000, 660, 600);
insert into `card_model` (`id`, `name`, `unit_price`, `power`, `output`, `ep`, `zt_power`)
values (4, '钻石令牌', 5000, 5000, 17500, 1650, 1750);
insert into `card_model` (`id`, `name`, `unit_price`, `power`, `output`, `ep`, `zt_power`)
values (5, '荣耀令牌', 10000, 10000, 40000, 3300, 4000);

insert into `level_model` (`id`, `name`, `card_model_id`, `small_power`, `weight`, `upper_limit`)
values (1, '士兵', 1, 5000, 0.10, 200);
insert into `level_model` (`id`, `name`, `card_model_id`, `small_power`, `weight`, `upper_limit`)
values (2, '骑士', 2, 20000, 0.15, 1250);
insert into `level_model` (`id`, `name`, `card_model_id`, `small_power`, `weight`, `upper_limit`)
values (3, '将军', 3, 80000, 0.20, 6000);
insert into `level_model` (`id`, `name`, `card_model_id`, `small_power`, `weight`, `upper_limit`)
values (4, '伯爵', 4, 500000, 0.25, 17500);
insert into `level_model` (`id`, `name`, `card_model_id`, `small_power`, `weight`, `upper_limit`)
values (5, '国王', 5, 4000000, 0.30, 40000);

create table if not exists `swap_ep_order`
(
    `id`          bigint unsigned not null auto_increment primary key,
    `wallet_id`   bigint unsigned not null comment '钱包id',
    `order_id`    varchar(100)    not null default '' comment '订单ID',
    `amount`      decimal(38, 18) not null default 0 comment '数量',
    `tx_hash`     varchar(100)    not null default '' comment '交易hash',
    `status`      tinyint         not null default 0 not null comment '状态 -1失败 0交易中 1已完成',
    `update_time` timestamp       not null default current_timestamp on update current_timestamp comment '更新时间',
    `create_time` timestamp       not null default current_timestamp comment '创建时间',
    foreign key (`wallet_id`) references `wallet` (`id`)
) engine = innodb
  default charset = utf8mb4 comment 'EP兑换订单';
create index index_swap_ep_order_order_id on `swap_ep_order` (`order_id`);

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin`
(
    `id`           int unsigned                                                 NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`     varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT '用户名',
    `nickname`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT '昵称',
    `password`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT '密码',
    `salt`         varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT '密码盐',
    `avatar`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT '' COMMENT '头像',
    `email`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         DEFAULT '' COMMENT '电子邮箱',
    `mobile`       varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT '手机号码',
    `loginfailure` tinyint unsigned                                             NOT NULL DEFAULT '0' COMMENT '失败次数',
    `logintime`    bigint                                                                DEFAULT NULL COMMENT '登录时间',
    `loginip`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT NULL COMMENT '登录IP',
    `createtime`   bigint                                                                DEFAULT NULL COMMENT '创建时间',
    `updatetime`   bigint                                                                DEFAULT NULL COMMENT '更新时间',
    `token`        varchar(59) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT 'Session标识',
    `status`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'normal' COMMENT '状态',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin`
    DISABLE KEYS */;
INSERT INTO `admin`
VALUES (1, 'admin', 'Admin', '532adf8a6525b246562b196b4383e0a2', '787d92', 'http://kapai.codbtoken.cn/assets/img/avatar.png', 'admin@admin.com', '', 0, 1687865363, '106.92.242.73', 1491635035, 1687865363, 'f000e758-65e0-4341-b40c-37635eddd477', 'normal');
/*!40000 ALTER TABLE `admin`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_log`
--

DROP TABLE IF EXISTS `admin_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_log`
(
    `id`         int unsigned                                              NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `admin_id`   int unsigned                                              NOT NULL DEFAULT '0' COMMENT '管理员ID',
    `username`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci       DEFAULT '' COMMENT '管理员名字',
    `url`        varchar(1500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     DEFAULT '' COMMENT '操作页面',
    `title`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      DEFAULT '' COMMENT '日志标题',
    `content`    longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
    `ip`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci       DEFAULT '' COMMENT 'IP',
    `useragent`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci      DEFAULT '' COMMENT 'User-Agent',
    `createtime` bigint                                                             DEFAULT NULL COMMENT '操作时间',
    PRIMARY KEY (`id`),
    KEY `name` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='管理员日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_log`
--

LOCK TABLES `admin_log` WRITE;
/*!40000 ALTER TABLE `admin_log`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_log`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attachment`
(
    `id`          int unsigned                                                  NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `category`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT '' COMMENT '类别',
    `admin_id`    int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '管理员ID',
    `user_id`     int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '会员ID',
    `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT '物理路径',
    `imagewidth`  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT '' COMMENT '宽度',
    `imageheight` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT '' COMMENT '高度',
    `imagetype`   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT '' COMMENT '图片类型',
    `imageframes` int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '图片帧数',
    `filename`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT '文件名称',
    `filesize`    int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '文件大小',
    `mimetype`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT 'mime类型',
    `extparam`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci          DEFAULT '' COMMENT '透传数据',
    `createtime`  bigint                                                                 DEFAULT NULL COMMENT '创建日期',
    `updatetime`  bigint                                                                 DEFAULT NULL COMMENT '更新时间',
    `uploadtime`  bigint                                                                 DEFAULT NULL COMMENT '上传时间',
    `storage`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'local' COMMENT '存储位置',
    `sha1`        varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci           DEFAULT '' COMMENT '文件 sha1编码',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='附件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment`
    DISABLE KEYS */;
INSERT INTO `attachment`
VALUES (1, '', 1, 0, '/assets/img/qrcode.png', '150', '150', 'png', 0, 'qrcode.png', 21859, 'image/png', '', 1491635035, 1491635035, 1491635035, 'local', '17163603d0263e4838b9387ff2cd4877e8b018f6');
/*!40000 ALTER TABLE `attachment`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_group`
--

DROP TABLE IF EXISTS `auth_group`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_group`
(
    `id`         int unsigned                                          NOT NULL AUTO_INCREMENT,
    `pid`        int unsigned                                          NOT NULL DEFAULT '0' COMMENT '父组别',
    `name`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT '' COMMENT '组名',
    `rules`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规则ID',
    `createtime` bigint                                                         DEFAULT NULL COMMENT '创建时间',
    `updatetime` bigint                                                         DEFAULT NULL COMMENT '更新时间',
    `status`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci   DEFAULT '' COMMENT '状态',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='分组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_group`
--

LOCK TABLES `auth_group` WRITE;
/*!40000 ALTER TABLE `auth_group`
    DISABLE KEYS */;
INSERT INTO `auth_group`
VALUES (1, 0, 'Admin group', '*', 1491635035, 1491635035, 'normal');
/*!40000 ALTER TABLE `auth_group`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_group_access`
--

DROP TABLE IF EXISTS `auth_group_access`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_group_access`
(
    `uid`      int unsigned NOT NULL COMMENT '会员ID',
    `group_id` int unsigned NOT NULL COMMENT '级别ID',
    UNIQUE KEY `uid_group_id` (`uid`, `group_id`),
    KEY `uid` (`uid`),
    KEY `group_id` (`group_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='权限分组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_group_access`
--

LOCK TABLES `auth_group_access` WRITE;
/*!40000 ALTER TABLE `auth_group_access`
    DISABLE KEYS */;
INSERT INTO `auth_group_access`
VALUES (1, 1);
/*!40000 ALTER TABLE `auth_group_access`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auth_rule`
--

DROP TABLE IF EXISTS `auth_rule`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auth_rule`
(
    `id`         int unsigned                                                          NOT NULL AUTO_INCREMENT,
    `type`       enum ('menu','file') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL            DEFAULT 'file' COMMENT 'menu为菜单,file为权限节点',
    `pid`        int unsigned                                                          NOT NULL            DEFAULT '0' COMMENT '父ID',
    `name`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                             DEFAULT '' COMMENT '规则名称',
    `title`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                              DEFAULT '' COMMENT '规则名称',
    `icon`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                              DEFAULT '' COMMENT '图标',
    `url`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                             DEFAULT '' COMMENT '规则URL',
    `condition`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                             DEFAULT '' COMMENT '条件',
    `remark`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                             DEFAULT '' COMMENT '备注',
    `ismenu`     tinyint unsigned                                                      NOT NULL            DEFAULT '0' COMMENT '是否为菜单',
    `menutype`   enum ('addtabs','blank','dialog','ajax') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单类型',
    `extend`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                             DEFAULT '' COMMENT '扩展属性',
    `py`         varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                              DEFAULT '' COMMENT '拼音首字母',
    `pinyin`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                             DEFAULT '' COMMENT '拼音',
    `createtime` bigint                                                                                    DEFAULT NULL COMMENT '创建时间',
    `updatetime` bigint                                                                                    DEFAULT NULL COMMENT '更新时间',
    `weigh`      int                                                                   NOT NULL            DEFAULT '0' COMMENT '权重',
    `status`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci                              DEFAULT '' COMMENT '状态',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`) USING BTREE,
    KEY `pid` (`pid`),
    KEY `weigh` (`weigh`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='节点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auth_rule`
--

LOCK TABLES `auth_rule` WRITE;
/*!40000 ALTER TABLE `auth_rule`
    DISABLE KEYS */;
INSERT INTO `auth_rule`
VALUES (5, 'file', 0, 'auth', 'Auth', 'fa fa-group', '', '', '', 1, NULL, '', 'qxgl', 'quanxianguanli', 1491635035, 1491635035, 99, 'normal'),
       (9, 'file', 5, 'auth/admin', 'Admin', 'fa fa-user', '', '', 'Admin tips', 1, NULL, '', 'glygl', 'guanliyuanguanli', 1491635035, 1491635035, 118, 'normal'),
       (10, 'file', 5, 'auth/adminlog', 'Admin log', 'fa fa-list-alt', '', '', 'Admin log tips', 1, NULL, '', 'glyrz', 'guanliyuanrizhi', 1491635035, 1491635035, 113, 'normal'),
       (11, 'file', 5, 'auth/group', 'Group', 'fa fa-group', '', '', 'Group tips', 1, NULL, '', 'jsz', 'juesezu', 1491635035, 1491635035, 109, 'normal'),
       (12, 'file', 5, 'auth/rule', 'Rule', 'fa fa-bars', '', '', 'Rule tips', 1, NULL, '', 'cdgz', 'caidanguize', 1491635035, 1491635035, 104, 'normal'),
       (40, 'file', 9, 'auth/admin/index', 'View', 'fa fa-circle-o', '', '', 'Admin tips', 0, NULL, '', '', '', 1491635035, 1491635035, 117, 'normal'),
       (41, 'file', 9, 'auth/admin/add', 'Add', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 116, 'normal'),
       (42, 'file', 9, 'auth/admin/edit', 'Edit', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 115, 'normal'),
       (43, 'file', 9, 'auth/admin/del', 'Delete', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 114, 'normal'),
       (44, 'file', 10, 'auth/adminlog/index', 'View', 'fa fa-circle-o', '', '', 'Admin log tips', 0, NULL, '', '', '', 1491635035, 1491635035, 112, 'normal'),
       (45, 'file', 10, 'auth/adminlog/detail', 'Detail', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 111, 'normal'),
       (46, 'file', 10, 'auth/adminlog/del', 'Delete', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 110, 'normal'),
       (47, 'file', 11, 'auth/group/index', 'View', 'fa fa-circle-o', '', '', 'Group tips', 0, NULL, '', '', '', 1491635035, 1491635035, 108, 'normal'),
       (48, 'file', 11, 'auth/group/add', 'Add', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 107, 'normal'),
       (49, 'file', 11, 'auth/group/edit', 'Edit', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 106, 'normal'),
       (50, 'file', 11, 'auth/group/del', 'Delete', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 105, 'normal'),
       (51, 'file', 12, 'auth/rule/index', 'View', 'fa fa-circle-o', '', '', 'Rule tips', 0, NULL, '', '', '', 1491635035, 1491635035, 103, 'normal'),
       (52, 'file', 12, 'auth/rule/add', 'Add', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 102, 'normal'),
       (53, 'file', 12, 'auth/rule/edit', 'Edit', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 101, 'normal'),
       (54, 'file', 12, 'auth/rule/del', 'Delete', 'fa fa-circle-o', '', '', '', 0, NULL, '', '', '', 1491635035, 1491635035, 100, 'normal'),
       (85, 'file', 0, 'new_wallet', '会员管理', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'hygl', 'huiyuanguanli', 1685693555, 1685720119, 98, 'normal'),
       (86, 'file', 85, 'new_wallet/index', '会员列表', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'hylb', 'huiyuanliebiao', 1685693905, 1685720256, 99, 'normal'),
       (87, 'file', 85, 'wallet_card/index', '会员卡牌', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'hykp', 'huiyuankapai', 1685694791, 1685720264, 98, 'normal'),
       (88, 'file', 117, 'asset_log/index', '数据变更记录', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'sjbgjl', 'shujubiangengjilu', 1685694972, 1685806572, 0, 'normal'),
       (89, 'file', 0, 'card_model', '卡牌管理', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'kpgl', 'kapaiguanli', 1685695023, 1685720134, 96, 'normal'),
       (90, 'file', 89, 'card_model/index', '卡牌列表', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'kplb', 'kapailiebiao', 1685695046, 1685720330, 99, 'normal'),
       (91, 'file', 85, 'check_in/index', '签到管理', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'qdgl', 'qiandaoguanli', 1685695119, 1685720290, 96, 'normal'),
       (92, 'file', 0, 'level_model', '等级管理', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'djgl', 'dengjiguanli', 1685695199, 1685720142, 95, 'normal'),
       (93, 'file', 92, 'level_model/index', '等级列表', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'djlb', 'dengjiliebiao', 1685695218, 1685695218, 0, 'normal'),
       (94, 'file', 89, 'mold_order/index', '铸造订单', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'zzdd', 'zhuzaodingdan', 1685695307, 1685720336, 98, 'normal'),
       (95, 'file', 85, 'new_relation/index', '推荐关系', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'tjgx', 'tuijianguanxi', 1685695358, 1685720279, 97, 'normal'),
       (96, 'file', 0, 'withdraw_order', '提现管理', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'txgl', 'tixianguanli', 1685695409, 1685720217, 93, 'normal'),
       (97, 'file', 96, 'withdraw_order/index', '提现列表', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'txlb', 'tixianliebiao', 1685695418, 1685695432, 0, 'normal'),
       (98, 'file', 0, 'buy_node_order', '节点管理', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'jdgl', 'jiedianguanli', 1685695723, 1685720127, 97, 'normal'),
       (99, 'file', 98, 'buy_node_order/index', '节点订单', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'jddd', 'jiediandingdan', 1685695723, 1685720312, 98, 'normal'),
       (110, 'file', 0, 'system_config', '系统管理', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'xtgl', 'xitongguanli', 1685704554, 1685720240, 92, 'normal'),
       (111, 'file', 110, 'system_config/edit', '系统配置', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'xtpz', 'xitongpeizhi', 1685704554, 1685704750, 0, 'normal'),
       (116, 'file', 0, 'data_statistics/index', '数据统计', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'sjtj', 'shujutongji', 1685706159, 1685706159, 0, 'normal'),
       (117, 'file', 0, 'asset_log', '记录管理', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'jlgl', 'jiluguanli', 1685708768, 1685720159, 94, 'normal'),
       (119, 'file', 117, 'asset_log/index?identifying=1', 'ART锁仓释放记录', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'Ascsfjl', 'ARTsuocangshifangjilu', 1685708878, 1685806464, 0, 'normal'),
       (120, 'file', 117, 'asset_log/ka_chan_log', '令牌产出记录', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'lpccjl', 'lingpaichanchujilu', 1685708919, 1686042134, 0, 'normal'),
       (121, 'file', 98, 'new_node', '节点列表', 'fa fa-circle-o', '', '', '', 0, 'addtabs', '', 'jdlb', 'jiedianliebiao', 1685710963, 1687866728, 99, 'normal'),
       (122, 'file', 121, 'new_node/index', '查看', 'fa fa-circle-o', '', '', '', 0, NULL, '', 'zk', 'zhakan', 1685710963, 1685711211, 0, 'normal'),
       (124, 'file', 121, 'new_node/edit', '编辑', 'fa fa-circle-o', '', '', '', 0, NULL, '', 'bj', 'bianji', 1685710963, 1685710963, 0, 'normal'),
       (128, 'file', 117, 'asset_log/art_log_list', 'ART记录', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'Ajl', 'ARTjilu', 1685806702, 1685806702, 0, 'normal'),
       (129, 'file', 117, 'asset_log/soul_log_list', 'SOUL记录', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'Sjl', 'SOULjilu', 1685806732, 1685806732, 0, 'normal'),
       (130, 'file', 0, 'swap_ep_order', 'IOT兑换订单', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'Idhdd', 'IOTduihuandingdan', 1686296766, 1687865091, 92, 'normal'),
       (131, 'file', 130, 'swap_ep_order/index', '订单列表', 'fa fa-circle-o', '', '', '', 1, 'addtabs', '', 'ddlb', 'dingdanliebiao', 1686296766, 1686296911, 0, 'normal');
/*!40000 ALTER TABLE `auth_rule`
    ENABLE KEYS */;
UNLOCK TABLES;


DROP TABLE IF EXISTS `system_config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_config`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `equity_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '权益说明',
    `level_desc`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '等级升级说明',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更换时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='系统配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_config`
--

LOCK TABLES `system_config` WRITE;
/*!40000 ALTER TABLE `system_config`
    DISABLE KEYS */;
INSERT INTO `system_config`
VALUES (1, '这是\r\n权益\r\n说明', '这是\r\n登记升级\r\n说明1', '2023-06-02 19:21:40');
/*!40000 ALTER TABLE `system_config`
    ENABLE KEYS */;
UNLOCK TABLES;
