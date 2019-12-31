-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Anamakine: db
-- Üretim Zamanı: 24 Ara 2019, 23:46:11
-- Sunucu sürümü: 8.0.18
-- PHP Sürümü: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Veritabanı: `dbBrowser`
--
CREATE DATABASE IF NOT EXISTS `dbBrowser` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `dbBrowser`;
-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `Course`
--

CREATE TABLE `Course` (
  `courseID` char(5) NOT NULL,
  `subjectID` char(4) NOT NULL,
  `courseNum` int(11) DEFAULT NULL,
  `title` varchar(50) NOT NULL,
  `numCredit` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Tablo döküm verisi `Course`
--

INSERT INTO `Course` (`courseID`, `subjectID`, `courseNum`, `title`, `numCredit`) VALUES
('11111', 'CSCI', 1301, 'Introduction to Java I', 4),
('11112', 'CSCI', 1302, 'Introduction to Java II', 3),
('11113', 'CSCI', 3720, 'Database Systems', 3),
('11114', 'CSCI', 4750, 'Rapid Java Application', 3),
('11115', 'MATH', 2750, 'Calculus I', 5),
('11116', 'MATH', 3750, 'Calculus II', 5),
('11117', 'EDUC', 1111, 'Reading', 3),
('11118', 'ITEC', 1344, 'Database Administration', 3);

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `Course`
--
ALTER TABLE `Course`
  ADD PRIMARY KEY (`courseID`);
COMMIT;
