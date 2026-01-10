-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 10, 2026 at 06:21 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gym`
--

-- --------------------------------------------------------

--
-- Table structure for table `coaches`
--

CREATE TABLE `coaches` (
  `c_name` varchar(50) NOT NULL,
  `c_phoneno` varchar(15) DEFAULT NULL,
  `c_age` int(11) DEFAULT NULL,
  `c_adress` varchar(100) DEFAULT NULL,
  `c_gender` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `coaches`
--

INSERT INTO `coaches` (`c_name`, `c_phoneno`, `c_age`, `c_adress`, `c_gender`) VALUES
('AMIT', '9988776655', 35, 'Mumbai', 'Male'),
('NEHA', '9123456780', 28, 'Pune', 'Female'),
('RAHUL', '9876543210', 30, 'Nagpur', 'Male');

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `mem_name` varchar(50) NOT NULL,
  `m_phone_no` varchar(15) DEFAULT NULL,
  `m_age` int(11) DEFAULT NULL,
  `m_amount` int(11) DEFAULT NULL,
  `m_timing` varchar(50) DEFAULT NULL,
  `m_coach` varchar(50) DEFAULT NULL,
  `m_gender` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `members`
--

INSERT INTO `members` (`mem_name`, `m_phone_no`, `m_age`, `m_amount`, `m_timing`, `m_coach`, `m_gender`) VALUES
('AARAV', '8012345678', 22, 1000, '6AM to 8AM', 'RAHUL', 'Male'),
('GAURAV', '7887783533', 22, 1000, '6PM to 8PM', 'RAHUL', 'Male'),
('PRIYA', '9012345678', 25, 1200, '6PM to 8PM', 'NEHA', 'Female'),
('RUSHI', '8485863058', 20, 200, '4PM to 6PM', 'RAHUL', 'Male'),
('VIKRAM', '7712345678', 29, 900, '6PM to 8PM', 'AMIT', 'Male');

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `p_date` varchar(20) DEFAULT NULL,
  `mem_name` varchar(50) DEFAULT NULL,
  `p_amount` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `coaches`
--
ALTER TABLE `coaches`
  ADD PRIMARY KEY (`c_name`);

--
-- Indexes for table `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`mem_name`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD KEY `mem_name` (`mem_name`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`mem_name`) REFERENCES `members` (`mem_name`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
