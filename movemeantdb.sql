-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 12, 2017 at 07:43 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `movemeantdb`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`studentweb`@`localhost` PROCEDURE `NewVisit` (IN `placeID` VARCHAR(100), IN `userID` VARCHAR(50))  NO SQL
IF (now() > (SELECT last_visit from places_users WHERE place_id=placeId and user_id= userId)+ INTERVAL 1 DAY)
THEN UPDATE places_users SET last_visit= now(),num_of_vis=num_of_vis+1 WHERE place_id=placeId and user_id= userId;
END IF$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `places_users`
--

CREATE TABLE `places_users` (
  `user_id` varchar(50) COLLATE utf32_german2_ci NOT NULL,
  `place_id` varchar(100) COLLATE utf32_german2_ci NOT NULL,
  `reveal` tinyint(1) NOT NULL,
  `last_visit` datetime NOT NULL,
  `num_of_vis` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf32 COLLATE=utf32_german2_ci;

--
-- Dumping data for table `places_users`
--

INSERT INTO `places_users` (`user_id`, `place_id`, `reveal`, `last_visit`, `num_of_vis`) VALUES
('1', '00000000000', 0, '2017-08-12 18:14:11', 0),
('1', '123', 0, '0000-00-00 00:00:00', 0),
('1', '1234', 0, '0000-00-00 00:00:00', 0),
('1', '12345', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJ27ixEMQapEcRGXlLsRv8Sik', 0, '2017-08-12 19:36:26', 3),
('1', 'ChIJ4eQphL8apEcRwq7V25dOG_0', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJ6_AG8cMapEcRpLsjKv65Y-g', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJ6XrzD8QapEcRqOWloBRQmF4', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJ70wQfcMapEcR2vWKcjCShdU', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJa8bkg8MapEcR-N1K2VW9CQE', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJAQDAOcIapEcRZT8pDT20fx8', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJazKdGcUapEcRIy1IeqnzG-4', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJb0aY5MMapEcRpySd_xROTVQ', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJb2MBrcEapEcRYisTzYdPBM0', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJDQIvfcMapEcRuPYQS2mAKUg', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJFQyXC8MapEcRdizMeDYaED0', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJfw2HfdIapEcR4Mr_a9ZC5aw', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJG8dhWmIapEcRWAoD3gMFG-E', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJi2BzhcIapEcRhovt0zYFrT0', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJk3nJ3W8bpEcRkyNbaPvne3U', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJl_BdeN4apEcRWkMYx0HBCuw', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJl0PTXMMapEcRU3rkfgQN3Qw', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJmyDFacEapEcRma8DF3yxlo8', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJp5LZRZUapEcRp5FGGAWhLyQ', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJpzr2gsMapEcRIJmIpOYK6Ks', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJQcPMWdsapEcR_LShk1ul4xg', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJqf5n9cMapEcRpZCPaoDdClI', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJr4EVnC1RwokRXwH3tAxCNqg', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJr4YQIMAapEcRjtALxFf0Fv4', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJrfiUlMMapEcRM4Rq-pprrWg', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJsVqQWcIapEcRdKE7cF6J1kk', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJsYWSO8UapEcRfBn25PvpRwk', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJSZRxOsIapEcRXY6BAPkg1Ck', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJTcDpj8QapEcRJXyRFreMW3o', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJteViGMcapEcRSo8qfpTNZ0g', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJub0hy8MapEcRGvH7PQXuDis', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJuUxBr8EapEcRXmtkvX_SNI0', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJuZSnZcQapEcR6ZAFpiHN8eg', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJuZSnZcQapEcRxzTvLa35P9s', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJW2IKzMMapEcR995OxKY0f_o', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJx0GvyMEapEcRkfFZbPGONd0', 0, '0000-00-00 00:00:00', 0),
('1', 'ChIJy0mE_ekapEcRi_3tyae26hU', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJ3ZQirusapEcRvI7S_p0JCDU', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJ50-kz-sapEcRpHC8gpplWII', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJ6_AG8cMapEcRpLsjKv65Y-g', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJAaqwC-wapEcRvZqy4bUNnz8', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJb2MBrcEapEcRYisTzYdPBM0', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJdd9C2-sapEcRX63ZmxxyiOU', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJeXLN78MapEcR9ePNRwhnQGY', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJG6OF7usapEcRSFLH24I6Tmw', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJuUxBr8EapEcRXmtkvX_SNI0', 0, '0000-00-00 00:00:00', 0),
('2', 'ChIJuZSnZcQapEcR6ZAFpiHN8eg', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJ_x868dIapEcR1GSQ8vchIjk', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJ3ZQirusapEcRvI7S_p0JCDU', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJ50-kz-sapEcRpHC8gpplWII', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJAaqwC-wapEcRvZqy4bUNnz8', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJb2MBrcEapEcRYisTzYdPBM0', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJbU63WisFpEcRXOiisE4L1Dc', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJdd9C2-sapEcRX63ZmxxyiOU', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJeUu7GNMapEcRDtQxOoRN-mw', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJfw2HfdIapEcR4Mr_a9ZC5aw', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJH1cA_OsapEcRAaSG7Dv7KpY', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJi2BzhcIapEcRhovt0zYFrT0', 0, '0000-00-00 00:00:00', 0),
('3', 'ChIJuUxBr8EapEcRXmtkvX_SNI0', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJ4eQphL8apEcRwq7V25dOG_0', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJ6_AG8cMapEcRpLsjKv65Y-g', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJb2MBrcEapEcRYisTzYdPBM0', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJbbnMoMUapEcRzRlpMuJ6dL4', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJBWAFtcQapEcRVJPEK-Ek12Q', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJeXLN78MapEcRPjbGio0fap0', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJi2BzhcIapEcRhovt0zYFrT0', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJl0PTXMMapEcRU3rkfgQN3Qw', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJlz8eB8UapEcR7i0hNy6vToc', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJmagEusEapEcRd123Jr1zvkQ', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJPZQS1toapEcRkyYYZN7G7PM', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJQcPMWdsapEcR_LShk1ul4xg', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJSZRxOsIapEcRXY6BAPkg1Ck', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJuUxBr8EapEcRXmtkvX_SNI0', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJuZSnZcQapEcR6ZAFpiHN8eg', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJvUagEdsapEcRE1CWMkbSNtI', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJy0mE_ekapEcRi_3tyae26hU', 0, '0000-00-00 00:00:00', 0),
('4', 'ChIJZ7nx38QapEcRpTPDVH7b6wE', 0, '0000-00-00 00:00:00', 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` varchar(50) CHARACTER SET utf32 COLLATE utf32_german2_ci NOT NULL,
  `user_name` varchar(30) CHARACTER SET utf32 COLLATE utf32_german2_ci NOT NULL,
  `password` varchar(30) CHARACTER SET latin1 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_german2_ci ROW_FORMAT=COMPACT;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `user_name`, `password`) VALUES
('1', 'anas', '12345'),
('2', 'saeed', '12345'),
('3', 'john', '12345'),
('4', 'sam', '12345');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `places_users`
--
ALTER TABLE `places_users`
  ADD PRIMARY KEY (`user_id`,`place_id`),
  ADD KEY `user_id` (`user_id`) USING BTREE;

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `places_users`
--
ALTER TABLE `places_users`
  ADD CONSTRAINT `places_users_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
