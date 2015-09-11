-- phpMyAdmin SQL Dump
-- version 4.4.12
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 11, 2015 at 11:28 AM
-- Server version: 5.6.25
-- PHP Version: 5.5.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lms`
--

-- --------------------------------------------------------

--
-- Table structure for table `Author`
--

CREATE TABLE IF NOT EXISTS `Author` (
  `author_id` int(11) NOT NULL,
  `credentials` varchar(255) NOT NULL,
  `shortBio` varchar(255) NOT NULL,
  `person_id` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Author`
--

INSERT INTO `Author` (`author_id`, `credentials`, `shortBio`, `person_id`) VALUES
(6, '5 Star', 'Very good', 17),
(7, '5 Sat', 'short', 18),
(8, 'sdf', 'science start', 19),
(9, 'sdfsdf', 'science', 20);

-- --------------------------------------------------------

--
-- Table structure for table `Book`
--

CREATE TABLE IF NOT EXISTS `Book` (
  `isbn` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `max_checkout` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=123457 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Book`
--

INSERT INTO `Book` (`isbn`, `title`, `max_checkout`) VALUES
(1233, 'Head First C#', 21),
(1234, 'Head First Java', 7),
(2344, 'Death by black hole', 21),
(34556, 'Brief History of Time', 7);

-- --------------------------------------------------------

--
-- Table structure for table `BookCopy`
--

CREATE TABLE IF NOT EXISTS `BookCopy` (
  `copy_id` int(11) NOT NULL,
  `isbn` int(11) NOT NULL,
  `copyNumber` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `BookCopy`
--

INSERT INTO `BookCopy` (`copy_id`, `isbn`, `copyNumber`) VALUES
(1, 34556, 1);

-- --------------------------------------------------------

--
-- Table structure for table `book_author`
--

CREATE TABLE IF NOT EXISTS `book_author` (
  `book_id` int(11) DEFAULT NULL,
  `author_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `book_author`
--

INSERT INTO `book_author` (`book_id`, `author_id`) VALUES
(1234, 6),
(1233, 7),
(2344, 8),
(34556, 9);

-- --------------------------------------------------------

--
-- Table structure for table `CheckInOut`
--

CREATE TABLE IF NOT EXISTS `CheckInOut` (
  `checkInOut_id` int(11) NOT NULL,
  `dueDate` varchar(255) NOT NULL,
  `checkOutDate` varchar(255) NOT NULL,
  `returnDate` varchar(255) DEFAULT NULL,
  `copy_id` int(11) NOT NULL,
  `memeber_id` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `CheckInOut`
--

INSERT INTO `CheckInOut` (`checkInOut_id`, `dueDate`, `checkOutDate`, `returnDate`, `copy_id`, `memeber_id`) VALUES
(1, '09/18/2015', '09/11/2015', '09/18/2015', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `Employee`
--

CREATE TABLE IF NOT EXISTS `Employee` (
  `emp_id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `person_id` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Employee`
--

INSERT INTO `Employee` (`emp_id`, `username`, `password`, `person_id`) VALUES
(1, 'bidhutkarki', 'kabindra', 15),
(2, 'admin', 'admin', 21);

-- --------------------------------------------------------

--
-- Table structure for table `EmployeeRole`
--

CREATE TABLE IF NOT EXISTS `EmployeeRole` (
  `role_id` int(11) NOT NULL,
  `emp_id` int(11) NOT NULL,
  `roleName` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `EmployeeRole`
--

INSERT INTO `EmployeeRole` (`role_id`, `emp_id`, `roleName`) VALUES
(2, 1, 'Librarian'),
(3, 2, 'Administrator');

-- --------------------------------------------------------

--
-- Table structure for table `Member`
--

CREATE TABLE IF NOT EXISTS `Member` (
  `member_id` int(11) NOT NULL,
  `person_id` int(11) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Member`
--

INSERT INTO `Member` (`member_id`, `person_id`) VALUES
(1, 16);

-- --------------------------------------------------------

--
-- Table structure for table `Person`
--

CREATE TABLE IF NOT EXISTS `Person` (
  `person_id` int(11) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `phoneNo` varchar(255) NOT NULL,
  `street` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `state` varchar(2) NOT NULL,
  `zip` int(5) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Person`
--

INSERT INTO `Person` (`person_id`, `firstName`, `lastName`, `phoneNo`, `street`, `city`, `state`, `zip`) VALUES
(2, 'Bidhut', 'Karki', '655-242-233', '1000 N', 'fairfield', 'IA', 54224),
(4, 'Bidhut', 'Karki', '655-242-233', '1000 N', 'fairfield', 'IA', 54224),
(10, 'Pritivi', 'Narayan', '655-767-321', 'Sinamangal', 'Kathmandu', 'BG', 44600),
(11, 'Pritivi', 'Khatri', '655-767-321', 'Sinamangal', 'Kathmandu', 'BG', 44600),
(12, 'Bidhut', 'KARKI', '655-333-768', '1000 N', 'Farifield', 'IA', 54657),
(13, 'Bidhut', 'Karki', '9804656672', 'Sinamangal', 'Kathmandu', 'IA', 46000),
(14, 'Bidhut', 'Karki', '9804656672', 'Sinamangal', 'Kathmandu', 'IA', 46000),
(15, 'Bidhut', 'Karki', '9804656672', 'Sinamangal', 'Kathmandu', 'IA', 4600),
(16, 'Prithivi', 'Khatari', '655-243-421', '1000 N', 'Fairfield', 'IA', 52667),
(17, 'Pradip', 'Khajbuja', 'Phone', '100 N', 'Fairfiled', 'IA', 56757),
(18, 'Jocob', 'Berlin', 'Phone', '6575 N', 'LOL', 'CA', 7686),
(19, 'Neil', 'Tyson', 'Phone', 'sdf', 'sdf', 'CA', 3434),
(20, 'Steven', 'Hawking', 'Phone', 'sdf', 'sdf', 'IA', 1234),
(21, 'Admin', 'Admin', '87979685', 'dsfjl', 'sdkfhlk', 'IA', 9897);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Author`
--
ALTER TABLE `Author`
  ADD PRIMARY KEY (`author_id`);

--
-- Indexes for table `Book`
--
ALTER TABLE `Book`
  ADD PRIMARY KEY (`isbn`);

--
-- Indexes for table `BookCopy`
--
ALTER TABLE `BookCopy`
  ADD PRIMARY KEY (`copy_id`);

--
-- Indexes for table `book_author`
--
ALTER TABLE `book_author`
  ADD KEY `book_id` (`book_id`),
  ADD KEY `author_id` (`author_id`);

--
-- Indexes for table `CheckInOut`
--
ALTER TABLE `CheckInOut`
  ADD PRIMARY KEY (`checkInOut_id`);

--
-- Indexes for table `Employee`
--
ALTER TABLE `Employee`
  ADD PRIMARY KEY (`emp_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `username_2` (`username`);

--
-- Indexes for table `EmployeeRole`
--
ALTER TABLE `EmployeeRole`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `Member`
--
ALTER TABLE `Member`
  ADD PRIMARY KEY (`member_id`);

--
-- Indexes for table `Person`
--
ALTER TABLE `Person`
  ADD PRIMARY KEY (`person_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Author`
--
ALTER TABLE `Author`
  MODIFY `author_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `Book`
--
ALTER TABLE `Book`
  MODIFY `isbn` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=123457;
--
-- AUTO_INCREMENT for table `BookCopy`
--
ALTER TABLE `BookCopy`
  MODIFY `copy_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `CheckInOut`
--
ALTER TABLE `CheckInOut`
  MODIFY `checkInOut_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `Employee`
--
ALTER TABLE `Employee`
  MODIFY `emp_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `EmployeeRole`
--
ALTER TABLE `EmployeeRole`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `Member`
--
ALTER TABLE `Member`
  MODIFY `member_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `Person`
--
ALTER TABLE `Person`
  MODIFY `person_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=22;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `book_author`
--
ALTER TABLE `book_author`
  ADD CONSTRAINT `book_author_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `Book` (`isbn`),
  ADD CONSTRAINT `book_author_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `Author` (`author_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
