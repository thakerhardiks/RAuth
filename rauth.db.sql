--
-- Database: `rauth_db`
--
CREATE DATABASE IF NOT EXISTS `rauth_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `rauth_db`;

-- --------------------------------------------------------

--
-- Table structure for table `resources`
--

CREATE TABLE `resources` (
  `res_id` double NOT NULL,
  `res_name` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `role_id` int(255) NOT NULL,
  `role_name` varchar(200) NOT NULL,
  `role_resources` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` double NOT NULL,
  `user_name` varchar(200) DEFAULT NULL,
  `user_email` varchar(200) DEFAULT NULL,
  `user_phone` double DEFAULT NULL,
  `user_password` varchar(1000) NOT NULL,
  `role_id` int(255) NOT NULL,
  `user_status` varchar(15) NOT NULL DEFAULT 'ACTIVATE' COMMENT 'Please refer UserStatus class of framework'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Triggers `user`
--
DELIMITER $$
CREATE TRIGGER `InsertUserCheck` BEFORE INSERT ON `user`
 FOR EACH ROW BEGIN
  IF (NEW.user_name IS NULL AND NEW.user_email IS NULL AND NEW.user_phone IS NULL) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'All the three fields ('user_name', 'user_email', 'user_phone' can not be null at the same time.';
  END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `UpdateUserCheck` BEFORE UPDATE ON `user`
 FOR EACH ROW BEGIN
  IF (NEW.user_name IS NULL AND NEW.user_email IS NULL AND NEW.user_phone IS NULL) THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'All the three fields ('user_name', 'user_email', 'user_phone' can not be null at the same time.';
  END IF;
END
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `resources`
--
ALTER TABLE `resources`
  ADD PRIMARY KEY (`res_id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `user_name` (`user_name`),
  ADD UNIQUE KEY `user_email` (`user_email`),
  ADD UNIQUE KEY `user_phone` (`user_phone`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `role_id` int(255) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` double NOT NULL AUTO_INCREMENT;
