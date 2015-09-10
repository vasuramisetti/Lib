-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Sep 10, 2015 at 03:25 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `lms`
--

-- --------------------------------------------------------

--
-- Table structure for table `author`
--

CREATE TABLE IF NOT EXISTS `author` (
  `author_id` int(11) NOT NULL AUTO_INCREMENT,
  `credentials` varchar(255) NOT NULL,
  `shortBio` varchar(255) NOT NULL,
  `person_id` int(11) NOT NULL,
  PRIMARY KEY (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE IF NOT EXISTS `book` (
  `isbn` varchar(20) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author_id` int(11) NOT NULL,
  PRIMARY KEY (`isbn`),
  UNIQUE KEY `isbn` (`isbn`),
  KEY `isbn_2` (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `bookcopy`
--

CREATE TABLE IF NOT EXISTS `bookcopy` (
  `copy_id` int(11) NOT NULL AUTO_INCREMENT,
  `isbn` varchar(11) NOT NULL,
  `copyNumber` int(11) NOT NULL,
  PRIMARY KEY (`copy_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `bookcopy`
--

INSERT INTO `bookcopy` (`copy_id`, `isbn`, `copyNumber`) VALUES
(1, '14545', 1),
(2, '874', 1);

-- --------------------------------------------------------

--
-- Table structure for table `checkinout`
--

CREATE TABLE IF NOT EXISTS `checkinout` (
  `checkInOut_id` int(11) NOT NULL AUTO_INCREMENT,
  `dueDate` varchar(255) NOT NULL,
  `checkOutDate` varchar(255) NOT NULL,
  `returnDate` varchar(255) NOT NULL,
  `copy_id` int(11) NOT NULL,
  `memeber_id` int(11) NOT NULL,
  PRIMARY KEY (`checkInOut_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `checkinout`
--

INSERT INTO `checkinout` (`checkInOut_id`, `dueDate`, `checkOutDate`, `returnDate`, `copy_id`, `memeber_id`) VALUES
(1, '01/10/2013', '01/11/2013', '01/12/2013', 0, 0),
(2, '01/10/2012', '01/11/2012', '01/12/2012', 1, 1),
(4, '01/10/2011', '01/12/2011', '', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE IF NOT EXISTS `employee` (
  `emp_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`emp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `employeerole`
--

CREATE TABLE IF NOT EXISTS `employeerole` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `employee_id` int(11) NOT NULL,
  `roleName` varchar(255) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

CREATE TABLE IF NOT EXISTS `member` (
  `member_id` int(11) NOT NULL AUTO_INCREMENT,
  `person_id` int(11) NOT NULL,
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `member`
--

INSERT INTO `member` (`member_id`, `person_id`) VALUES
(1, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `person`
--

CREATE TABLE IF NOT EXISTS `person` (
  `person_id` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `phoneNo` varchar(255) NOT NULL,
  `street` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `state` varchar(2) NOT NULL,
  `zip` int(5) NOT NULL,
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `person`
--

INSERT INTO `person` (`person_id`, `firstName`, `lastName`, `phoneNo`, `street`, `city`, `state`, `zip`) VALUES
(1, 'Bidhut', 'Karki', '655-242-233', '1000 N', 'fairfield', 'IA', 54224),
(2, 'Pritivi', 'Khatri', '655-767-321', 'Sinamangal', 'Kathmandu', 'BG', 44600),
(3, 'Pritivi', 'Khatri2', '655-767-321', 'Sinamangal', 'Kathmandu', 'BG', 44600);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
