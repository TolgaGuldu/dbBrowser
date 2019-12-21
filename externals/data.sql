-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Anamakine: db
-- Üretim Zamanı: 21 Ara 2019, 02:59:10
-- Sunucu sürümü: 8.0.18
-- PHP Sürümü: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Veritabanı: `dbBrowser`
--

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
('11118', 'ITEC', 1344, 'Database Administration', 3),
('cs482', 'CSEE', 482, 'advanced java', 3);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `Enrollment`
--

CREATE TABLE `Enrollment` (
  `ssn` char(9) NOT NULL,
  `courseID` char(5) NOT NULL,
  `dateReg` date DEFAULT NULL,
  `grade` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Tablo döküm verisi `Enrollment`
--

INSERT INTO `Enrollment` (`ssn`, `courseID`, `dateReg`, `grade`) VALUES
('444111110', '11111', '2004-03-19', 'A'),
('444111110', '11112', '2004-03-19', 'B'),
('444111110', '11113', '2004-03-19', 'C'),
('444111111', '11111', '2004-03-19', 'D'),
('444111111', '11112', '2004-03-19', 'F'),
('444111111', '11113', '2004-03-19', 'A'),
('444111112', '11114', '2004-03-19', 'B'),
('444111112', '11115', '2004-03-19', 'C'),
('444111112', '11116', '2004-03-19', 'D'),
('444111113', '11111', '2004-03-19', 'A'),
('444111113', '11113', '2004-03-19', 'A'),
('444111114', '11115', '2004-03-19', 'B'),
('444111115', '11115', '2004-03-19', 'F'),
('444111115', '11116', '2004-03-19', 'F'),
('444111116', '11111', '2004-03-19', 'D'),
('444111117', '11111', '2004-03-19', 'D'),
('444111118', '11111', '2004-03-19', 'A'),
('444111118', '11112', '2004-03-19', 'D'),
('444111118', '11113', '2004-03-19', 'B');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `Student`
--

CREATE TABLE `Student` (
  `ssn` char(9) NOT NULL,
  `fName` varchar(25) DEFAULT NULL,
  `mi` char(1) DEFAULT NULL,
  `lName` varchar(25) DEFAULT NULL,
  `bDate` date DEFAULT NULL,
  `street` varchar(25) DEFAULT NULL,
  `phone` char(11) DEFAULT NULL,
  `zipCode` char(5) DEFAULT NULL,
  `deptId` char(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Tablo döküm verisi `Student`
--

INSERT INTO `Student` (`ssn`, `fName`, `mi`, `lName`, `bDate`, `street`, `phone`, `zipCode`, `deptId`) VALUES
('444111110', 'Jacob', 'R', 'Smith', '1985-04-09', 'Kingston Street', '9129219434', '31435', 'BIOL'),
('444111111', 'John', 'K', 'Stevenson', NULL, 'app Street', '9129219434', '31411', 'BIOL'),
('444111112', 'George', 'K', 'Smith', '1974-10-10', 'Abercorn St.', '9129213454', '31419', 'CS'),
('444111113', 'Frank', 'E', 'Jones', '1970-09-09', 'app Street', '9125919434', '31411', 'BIOL'),
('444111114', 'Jean', 'K', 'Smith', '1970-02-09', 'app Street', '9129219434', '31411', 'CHEM'),
('444111115', 'Josh', 'R', 'Woo', '1970-02-09', 'Franklin St.', '7075989434', '31411', 'CHEM'),
('444111116', 'Josh', 'R', 'Smith', '1973-02-09', 'app Street', '9129219434', '31411', 'BIOL'),
('444111117', 'Joy', 'P', 'Kennedy', '1974-03-19', 'Bay Street', '9129229434', '31412', 'CS'),
('444111118', 'Toni', 'R', 'Peterson', '1964-04-29', 'Bay Street', '9129229434', '31412', 'MATH'),
('444111119', 'Patrick', 'R', 'Stoneman', '1969-04-29', 'Washington St.', '9129229434', '31435', 'MATH'),
('444111120', 'Rick', 'R', 'Carter', '1986-04-09', 'West Ford St.', '9125919434', '31411', 'BIOL');

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `Course`
--
ALTER TABLE `Course`
  ADD PRIMARY KEY (`courseID`);

--
-- Tablo için indeksler `Enrollment`
--
ALTER TABLE `Enrollment`
  ADD PRIMARY KEY (`ssn`,`courseID`),
  ADD KEY `courseID` (`courseID`);

--
-- Tablo için indeksler `Student`
--
ALTER TABLE `Student`
  ADD PRIMARY KEY (`ssn`);

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `Enrollment`
--
ALTER TABLE `Enrollment`
  ADD CONSTRAINT `Enrollment_ibfk_1` FOREIGN KEY (`ssn`) REFERENCES `Student` (`ssn`),
  ADD CONSTRAINT `Enrollment_ibfk_2` FOREIGN KEY (`courseID`) REFERENCES `Course` (`courseID`);
COMMIT;
