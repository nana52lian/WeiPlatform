SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `eshop_article`
-- ----------------------------
DROP TABLE IF EXISTS `eshop_article`;
CREATE TABLE `eshop_article` (
  `id` char(36) NOT NULL,
  `section_id` char(36) default NULL,
  `catagory_id` char(36) default NULL,
  `title` varchar(100) default NULL,
  `content` text,
  `summary` varchar(150) default NULL,
  `status` int(1) default '1',
  `writer` varchar(10) default NULL,
  `create_date` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `eshop_article_catagories`
-- ----------------------------
DROP TABLE IF EXISTS `eshop_article_catagories`;
CREATE TABLE `eshop_article_catagories` (
  `id` char(36) NOT NULL,
  `section_id` char(36) default '0',
  `name` varchar(50) default NULL,
  `create_date` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `eshop_article_sections`
-- ----------------------------
DROP TABLE IF EXISTS `eshop_article_sections`;
CREATE TABLE `eshop_article_sections` (
  `id` char(36) NOT NULL,
  `code` varchar(255) default '',
  `name` varchar(255) default '',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;