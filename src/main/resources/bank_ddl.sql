-- MySQL dump 10.13  Distrib 5.6.19, for osx10.7 (i386)
--
-- Host: localhost    Database: springbootdb
-- ------------------------------------------------------
-- Server version	5.7.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `account_id` bigint(20) NOT NULL,
  `account_balance` decimal(19,2) NOT NULL,
  `account_ref` varchar(255) NOT NULL,
  `currency` varchar(255) NOT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `UKr4tbxx0wvxd6awtd9987wkbke` (`account_ref`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `account_transaction`
--

DROP TABLE IF EXISTS `account_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_transaction` (
  `transaction_id` bigint(20) NOT NULL,
  `date` datetime NOT NULL,
  `transaction_ref` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`),
  UNIQUE KEY `UKgk0ya6lgnps4lo106om0xoeld` (`transaction_ref`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `seq_transaction`
--

DROP TABLE IF EXISTS `seq_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seq_transaction` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seq_transaction`
--

LOCK TABLES `seq_transaction` WRITE;
/*!40000 ALTER TABLE `seq_transaction` DISABLE KEYS */;
INSERT INTO `seq_transaction` VALUES (1);
/*!40000 ALTER TABLE `seq_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seq_transaction_leg`
--

DROP TABLE IF EXISTS `seq_transaction_leg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seq_transaction_leg` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seq_transaction_leg`
--

LOCK TABLES `seq_transaction_leg` WRITE;
/*!40000 ALTER TABLE `seq_transaction_leg` DISABLE KEYS */;
INSERT INTO `seq_transaction_leg` VALUES (1);
/*!40000 ALTER TABLE `seq_transaction_leg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sequence_account`
--

DROP TABLE IF EXISTS `sequence_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence_account` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequence_account`
--

LOCK TABLES `sequence_account` WRITE;
/*!40000 ALTER TABLE `sequence_account` DISABLE KEYS */;
INSERT INTO `sequence_account` VALUES (6);
/*!40000 ALTER TABLE `sequence_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_leg`
--

DROP TABLE IF EXISTS `transaction_leg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction_leg` (
  `trans_leg_id` bigint(20) NOT NULL,
  `amount` decimal(19,2) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `account_ref` bigint(20) DEFAULT NULL,
  `transaction_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`trans_leg_id`),
  KEY `FK9oh2cb7nloedc9xie7kaodgn2` (`account_ref`),
  KEY `FKm0voleecex3xeosuet9saqb59` (`transaction_id`),
  CONSTRAINT `FK9oh2cb7nloedc9xie7kaodgn2` FOREIGN KEY (`account_ref`) REFERENCES `account` (`account_id`),
  CONSTRAINT `FKm0voleecex3xeosuet9saqb59` FOREIGN KEY (`transaction_id`) REFERENCES `account_transaction` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;


-- Dump completed on 2017-07-13 23:23:11
