-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.40-community


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema wevote
--

CREATE DATABASE IF NOT EXISTS wevote;
USE wevote;

--
-- Definition of table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nickname` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=cp1251;

--
-- Dumping data for table `administrator`
--

/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` (`id`,`nickname`,`password`) VALUES 
 (1,'NorthernDemon','lol'),
 (2,'Pavlo','Ukrane');
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;


--
-- Definition of table `pool`
--

DROP TABLE IF EXISTS `pool`;
CREATE TABLE `pool` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(30) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=cp1251 ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `pool`
--

/*!40000 ALTER TABLE `pool` DISABLE KEYS */;
INSERT INTO `pool` (`id`,`title`,`date`) VALUES 
 (1,'Vancouver 2010 Question Pack','2010-02-28 15:16:45'),
 (2,'Unix vs Windows Holywars','2010-03-08 23:46:03'),
 (3,'Questions for Master Students','2011-03-08 23:46:03'),
 (4,'Test Pack for Charts','2011-10-10 09:50:00');
/*!40000 ALTER TABLE `pool` ENABLE KEYS */;


--
-- Definition of table `pool_answer`
--

DROP TABLE IF EXISTS `pool_answer`;
CREATE TABLE `pool_answer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `question_id` int(10) unsigned NOT NULL,
  `answer` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `question_id` (`question_id`),
  CONSTRAINT `question_id` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=cp1251;

--
-- Dumping data for table `pool_answer`
--

/*!40000 ALTER TABLE `pool_answer` DISABLE KEYS */;
INSERT INTO `pool_answer` (`id`,`question_id`,`answer`) VALUES 
 (1,1,'I don\'t know yet!'),
 (2,1,'Stop asking me!'),
 (3,2,'None'),
 (4,2,'May be 15'),
 (5,3,'UNIX'),
 (6,4,'server'),
 (7,5,'no'),
 (8,5,'yes'),
 (9,6,'yeah'),
 (10,7,'Bold'),
 (11,7,'Fat'),
 (12,7,'Funny'),
 (13,7,'Exhausted'),
 (14,7,'Leader'),
 (15,7,'None of list'),
 (16,7,'Who?'),
 (17,7,'What?');
/*!40000 ALTER TABLE `pool_answer` ENABLE KEYS */;


--
-- Definition of table `question`
--

DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `pool_id` int(10) unsigned NOT NULL,
  `question` tinytext NOT NULL,
  `title` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pool_id` (`pool_id`),
  CONSTRAINT `pool_id` FOREIGN KEY (`pool_id`) REFERENCES `pool` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=cp1251;

--
-- Dumping data for table `question`
--

/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` (`id`,`pool_id`,`question`,`title`) VALUES 
 (1,1,'With whome Finland will play final hockey game?','Finland Hockey'),
 (2,1,'How many medals will win USA?','USA medals'),
 (3,2,'Which Operation System is better? We know the answer, what about you? =)','Holy Wars'),
 (4,2,'What is the main profit from using Unix Operation System?','Unix Operating System'),
 (5,3,'Would you like to became a doctor?','Are you a Doctor?'),
 (6,3,'Would you like to became a worker?','Are you a Worker?'),
 (7,4,'What do you think of me?','Personal Question');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;


--
-- Definition of table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `mobile_number` varchar(45) NOT NULL,
  `age` datetime NOT NULL,
  `gender` varchar(45) NOT NULL DEFAULT 'unknown',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=cp1251 ROW_FORMAT=DYNAMIC;

--
-- Dumping data for table `user`
--

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`,`mobile_number`,`age`,`gender`) VALUES 
 (1,'+79219899375','1989-00-00 00:00:00','male'),
 (2,'+358468151909','1985-00-00 00:00:00','male'),
 (3,'+911','1980-00-00 00:00:00','female'),
 (4,'+1','1978-00-00 00:00:00','female'),
 (5,'+2','1965-00-00 00:00:00','female'),
 (6,'+3','1994-00-00 00:00:00','male'),
 (7,'+4','1995-00-00 00:00:00','female'),
 (8,'+5','1990-00-00 00:00:00','male'),
 (9,'+6','1975-00-00 00:00:00','female'),
 (10,'+7','1935-00-00 00:00:00','male'),
 (11,'+8','1965-00-00 00:00:00','female'),
 (12,'+9','1930-00-00 00:00:00','male');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;


--
-- Definition of table `user_answer`
--

DROP TABLE IF EXISTS `user_answer`;
CREATE TABLE `user_answer` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `answer_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `answer_id` (`answer_id`),
  CONSTRAINT `answer_id` FOREIGN KEY (`answer_id`) REFERENCES `pool_answer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=cp1251;

--
-- Dumping data for table `user_answer`
--

/*!40000 ALTER TABLE `user_answer` DISABLE KEYS */;
INSERT INTO `user_answer` (`id`,`user_id`,`answer_id`) VALUES 
 (1,1,2),
 (2,1,9),
 (3,1,8),
 (4,2,3),
 (5,2,4),
 (6,2,1),
 (7,2,5),
 (8,1,6),
 (9,1,7),
 (10,1,3),
 (11,2,2),
 (12,3,2),
 (13,1,10),
 (14,2,11),
 (15,3,12),
 (16,4,13),
 (17,5,14),
 (18,6,15),
 (19,7,16),
 (20,8,17),
 (21,9,16),
 (22,10,15),
 (23,11,14),
 (24,12,15);
/*!40000 ALTER TABLE `user_answer` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
