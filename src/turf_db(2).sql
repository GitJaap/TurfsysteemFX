-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Nov 22, 2014 at 07:17 PM
-- Server version: 5.6.20
-- PHP Version: 5.5.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `turf_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE IF NOT EXISTS `accounts` (
`account_id` mediumint(8) unsigned NOT NULL,
  `account_name` varchar(40) NOT NULL,
  `balance` mediumint(9) NOT NULL,
  `pincode` char(40) DEFAULT NULL,
  `account_creation_date` datetime NOT NULL,
  `credit_limit` mediumint(8) unsigned NOT NULL DEFAULT '0'
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`account_id`, `account_name`, `balance`, `pincode`, `account_creation_date`, `credit_limit`) VALUES
(1, 'PINACCOUNT', -190, NULL, '2014-09-27 13:22:33', 0),
(2, 'CASHACCOUNT', -9570, NULL, '2014-10-23 11:32:58', 0),
(3, 'SCHRAPACCOUNT', -4870, NULL, '2014-10-23 11:32:58', 0),
(4, 'Jaap', 0, NULL, '2014-10-23 11:32:58', 0);

-- --------------------------------------------------------

--
-- Table structure for table `admins`
--

CREATE TABLE IF NOT EXISTS `admins` (
`admin_id` mediumint(8) unsigned NOT NULL,
  `user_id` mediumint(8) unsigned DEFAULT NULL,
  `login` varchar(45) NOT NULL,
  `pass` char(40) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `admins`
--

INSERT INTO `admins` (`admin_id`, `user_id`, `login`, `pass`) VALUES
(1, NULL, 'default', '7505d64a54e061b7acd54ccd58b49dc43500b635'),
(2, NULL, 'kasper', '789b49606c321c8cf228d17942608eff0ccc4171');

-- --------------------------------------------------------

--
-- Table structure for table `admin_changes`
--

CREATE TABLE IF NOT EXISTS `admin_changes` (
`admin_change_id` mediumint(8) unsigned NOT NULL,
  `current_product_price_class_id` mediumint(8) unsigned NOT NULL,
  `admin_id` mediumint(8) unsigned NOT NULL,
  `admin_change_date` datetime NOT NULL,
  `admin_change_description` varchar(45) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `admin_changes`
--

INSERT INTO `admin_changes` (`admin_change_id`, `current_product_price_class_id`, `admin_id`, `admin_change_date`, `admin_change_description`) VALUES
(1, 1, 1, '2014-09-29 22:35:29', NULL),
(2, 1, 1, '2014-10-08 23:19:49', NULL),
(3, 1, 1, '2014-10-08 23:21:04', NULL),
(4, 1, 1, '2014-10-08 23:23:27', NULL),
(5, 1, 1, '2014-10-08 23:44:24', NULL),
(6, 1, 1, '2014-10-09 00:17:53', NULL),
(7, 1, 1, '2014-10-09 01:03:15', NULL),
(8, 1, 1, '2014-10-09 01:04:17', NULL),
(9, 1, 1, '2014-10-09 01:15:16', NULL),
(10, 1, 1, '2014-10-09 01:15:41', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `admin_logs`
--

CREATE TABLE IF NOT EXISTS `admin_logs` (
`admin_log_id` mediumint(8) unsigned NOT NULL,
  `admin_log_date` datetime NOT NULL,
  `admin_log_type` tinyint(1) NOT NULL DEFAULT '1',
  `bar_id` mediumint(8) unsigned NOT NULL,
  `flow_meter_readout` float NOT NULL,
  `admin_id` mediumint(8) unsigned NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `admin_logs`
--

INSERT INTO `admin_logs` (`admin_log_id`, `admin_log_date`, `admin_log_type`, `bar_id`, `flow_meter_readout`, `admin_id`) VALUES
(1, '2014-09-28 01:27:31', 1, 1, 1000, 2);

-- --------------------------------------------------------

--
-- Table structure for table `bars`
--

CREATE TABLE IF NOT EXISTS `bars` (
`bar_id` mediumint(8) unsigned NOT NULL,
  `bar_name` varchar(45) NOT NULL,
  `current_bar_cash` mediumint(8) unsigned NOT NULL,
  `bar_visibility` tinyint(1) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `bars`
--

INSERT INTO `bars` (`bar_id`, `bar_name`, `current_bar_cash`, `bar_visibility`) VALUES
(1, 'Bovenbar', 0, 1),
(2, 'Benedenbar', 0, 1);

-- --------------------------------------------------------

--
-- Table structure for table `cards`
--

CREATE TABLE IF NOT EXISTS `cards` (
`card_id` mediumint(8) unsigned NOT NULL,
  `account_id` mediumint(8) unsigned NOT NULL,
  `card_uid` varchar(50) NOT NULL,
  `card_atr` varchar(50) DEFAULT NULL,
  `card_name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `clients`
--

CREATE TABLE IF NOT EXISTS `clients` (
`client_id` mediumint(8) unsigned NOT NULL,
  `client_name` varchar(45) NOT NULL,
  `client_visibility` tinyint(1) NOT NULL,
  `bar_id` mediumint(8) unsigned NOT NULL,
  `client_is_active` tinyint(1) NOT NULL DEFAULT '0',
  `last_client_update` datetime NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Dumping data for table `clients`
--

INSERT INTO `clients` (`client_id`, `client_name`, `client_visibility`, `bar_id`, `client_is_active`, `last_client_update`) VALUES
(1, 'PClinks', 1, 1, 1, '2014-11-22 19:09:01'),
(2, 'PCrechts', 1, 1, 1, '2014-11-22 19:10:52'),
(3, 'PCbeheer', 1, 1, 1, '2014-11-22 19:12:01'),
(4, 'PClinks', 1, 2, 1, '2014-11-22 19:13:22'),
(5, 'PCrechts', 1, 2, 0, '2014-10-08 23:44:49'),
(6, 'PCBeheer', 1, 2, 0, '2014-10-08 23:44:50');

-- --------------------------------------------------------

--
-- Table structure for table `client_logs`
--

CREATE TABLE IF NOT EXISTS `client_logs` (
`client_log_id` mediumint(8) unsigned NOT NULL,
  `client_id` mediumint(8) unsigned NOT NULL,
  `client_log_type` tinyint(1) DEFAULT NULL,
  `log_date` datetime NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=568 ;

--
-- Dumping data for table `client_logs`
--

INSERT INTO `client_logs` (`client_log_id`, `client_id`, `client_log_type`, `log_date`) VALUES
(189, 1, 1, '2014-10-04 00:26:14'),
(190, 1, 0, '2014-10-04 00:26:19'),
(191, 1, 1, '2014-10-04 00:26:46'),
(192, 1, 0, '2014-10-04 00:26:57'),
(193, 1, 1, '2014-10-04 00:41:43'),
(194, 1, 1, '2014-10-04 00:44:34'),
(195, 1, 0, '2014-10-04 00:44:38'),
(196, 1, 1, '2014-10-04 00:44:50'),
(197, 1, 0, '2014-10-04 00:45:07'),
(198, 1, 1, '2014-10-04 01:01:23'),
(199, 1, 0, '2014-10-04 01:01:46'),
(200, 1, 1, '2014-10-04 01:03:52'),
(201, 1, 0, '2014-10-04 01:03:58'),
(202, 1, 1, '2014-10-04 01:06:34'),
(203, 1, 0, '2014-10-04 01:07:10'),
(204, 1, 1, '2014-10-04 01:08:43'),
(205, 1, 0, '2014-10-04 01:08:55'),
(206, 1, 0, '2014-10-04 01:08:59'),
(207, 1, 1, '2014-10-04 01:09:04'),
(208, 3, 1, '2014-10-04 01:09:12'),
(209, 3, 1, '2014-10-04 01:11:17'),
(210, 3, 0, '2014-10-04 01:11:23'),
(211, 1, 0, '2014-10-04 01:11:24'),
(212, 1, 1, '2014-10-04 01:12:04'),
(213, 1, 1, '2014-10-04 01:14:38'),
(214, 1, 0, '2014-10-04 01:19:50'),
(215, 1, 0, '2014-10-04 01:19:53'),
(216, 3, 0, '2014-10-04 01:19:54'),
(217, 1, 1, '2014-10-04 01:20:00'),
(218, 1, 0, '2014-10-04 01:20:06'),
(219, 1, 1, '2014-10-04 01:21:34'),
(220, 1, 0, '2014-10-04 01:21:53'),
(221, 1, 1, '2014-10-04 01:24:35'),
(222, 1, 0, '2014-10-04 01:25:03'),
(223, 1, 1, '2014-10-04 01:25:55'),
(224, 1, 0, '2014-10-04 01:26:01'),
(225, 1, 1, '2014-10-04 01:26:19'),
(226, 1, 0, '2014-10-04 01:26:21'),
(227, 1, 1, '2014-10-04 01:27:00'),
(228, 1, 0, '2014-10-04 01:28:20'),
(229, 1, 1, '2014-10-04 01:28:24'),
(230, 2, 1, '2014-10-04 01:28:52'),
(231, 1, 0, '2014-10-04 01:28:59'),
(232, 1, 1, '2014-10-04 01:29:14'),
(233, 1, 0, '2014-10-04 01:29:30'),
(234, 2, 0, '2014-10-04 01:30:15'),
(235, 1, 1, '2014-10-04 01:40:32'),
(236, 2, 1, '2014-10-04 01:40:42'),
(237, 2, 0, '2014-10-04 01:40:49'),
(238, 2, 1, '2014-10-04 01:41:00'),
(239, 2, 0, '2014-10-04 01:41:01'),
(240, 2, 1, '2014-10-04 01:41:08'),
(241, 2, 0, '2014-10-04 01:41:09'),
(242, 2, 1, '2014-10-04 01:41:13'),
(243, 2, 0, '2014-10-04 01:41:15'),
(244, 1, 0, '0000-00-00 00:00:00'),
(245, 1, 1, '2014-10-04 01:43:14'),
(246, 1, 0, '2014-10-04 01:43:21'),
(247, 1, 1, '2014-10-04 16:24:25'),
(248, 1, 0, '2014-10-04 16:25:27'),
(249, 1, 1, '2014-10-04 16:26:28'),
(250, 1, 0, '2014-10-04 16:26:35'),
(251, 1, 1, '2014-10-04 16:30:31'),
(252, 1, 0, '2014-10-04 16:30:42'),
(253, 1, 1, '2014-10-04 16:32:52'),
(254, 1, 0, '2014-10-04 16:32:55'),
(255, 1, 1, '2014-10-04 16:33:51'),
(256, 1, 0, '2014-10-04 16:33:53'),
(257, 1, 1, '2014-10-04 16:34:16'),
(258, 1, 0, '2014-10-04 16:34:26'),
(259, 1, 1, '2014-10-04 16:39:32'),
(260, 1, 0, '2014-10-04 16:39:54'),
(261, 5, 1, '2014-10-04 16:40:25'),
(262, 5, 0, '2014-10-04 16:41:05'),
(263, 4, 1, '2014-10-04 16:43:29'),
(264, 4, 0, '2014-10-04 16:43:31'),
(265, 1, 1, '2014-10-04 16:43:36'),
(266, 1, 0, '2014-10-04 16:43:44'),
(267, 1, 1, '2014-10-04 16:45:33'),
(268, 1, 0, '2014-10-04 16:46:10'),
(269, 1, 1, '2014-10-04 16:49:09'),
(270, 1, 0, '0000-00-00 00:00:00'),
(271, 1, 1, '2014-10-05 11:27:50'),
(272, 1, 0, '2014-10-05 11:27:57'),
(273, 1, 1, '2014-10-05 11:35:22'),
(274, 1, 0, '2014-10-05 11:35:40'),
(275, 1, 1, '2014-10-05 11:43:01'),
(276, 1, 0, '2014-10-05 11:43:29'),
(277, 1, 1, '2014-10-05 11:43:59'),
(278, 1, 0, '2014-10-05 11:48:16'),
(279, 4, 1, '2014-10-05 11:48:28'),
(280, 4, 0, '2014-10-05 11:48:34'),
(281, 1, 1, '2014-10-05 11:51:11'),
(282, 1, 0, '2014-10-05 11:51:32'),
(283, 1, 1, '2014-10-05 12:37:31'),
(284, 1, 0, '2014-10-05 13:41:18'),
(285, 1, 1, '2014-10-05 14:44:32'),
(286, 1, 0, '2014-10-05 14:45:37'),
(287, 1, 1, '2014-10-05 14:46:38'),
(288, 1, 0, '2014-10-05 14:47:34'),
(289, 1, 1, '2014-10-05 14:53:48'),
(290, 1, 0, '0000-00-00 00:00:00'),
(291, 1, 1, '2014-10-05 14:58:35'),
(292, 1, 0, '2014-10-05 14:58:37'),
(293, 1, 1, '2014-10-05 14:59:13'),
(294, 1, 0, '2014-10-05 14:59:24'),
(295, 4, 1, '2014-10-05 15:01:14'),
(296, 4, 0, '2014-10-05 15:01:24'),
(297, 1, 1, '2014-10-05 15:01:31'),
(298, 1, 0, '2014-10-05 15:02:15'),
(299, 1, 1, '2014-10-05 15:20:24'),
(300, 1, 0, '2014-10-05 15:20:25'),
(301, 1, 1, '2014-10-05 15:22:01'),
(302, 1, 0, '2014-10-05 15:22:05'),
(303, 1, 1, '2014-10-05 15:22:44'),
(304, 1, 0, '2014-10-05 15:23:15'),
(305, 1, 1, '2014-10-05 15:23:28'),
(306, 1, 0, '2014-10-05 15:23:49'),
(307, 1, 1, '2014-10-05 15:31:25'),
(308, 1, 0, '0000-00-00 00:00:00'),
(309, 1, 1, '2014-10-05 15:38:46'),
(310, 1, 0, '2014-10-05 15:38:48'),
(311, 1, 1, '2014-10-05 15:39:21'),
(312, 1, 0, '2014-10-05 15:41:37'),
(313, 1, 1, '2014-10-05 15:41:59'),
(314, 1, 0, '2014-10-05 15:42:52'),
(315, 1, 1, '2014-10-05 15:50:14'),
(316, 1, 0, '2014-10-05 15:50:19'),
(317, 1, 1, '2014-10-05 15:50:48'),
(318, 1, 0, '2014-10-05 15:51:01'),
(319, 1, 1, '2014-10-05 15:51:26'),
(320, 1, 0, '2014-10-05 15:51:29'),
(321, 1, 1, '2014-10-05 15:52:29'),
(322, 1, 0, '2014-10-05 15:53:51'),
(323, 1, 0, '2014-10-05 15:53:53'),
(324, 1, 0, '2014-10-05 15:53:54'),
(325, 1, 1, '2014-10-05 15:55:15'),
(326, 1, 0, '2014-10-05 15:55:43'),
(327, 1, 1, '2014-10-05 15:55:49'),
(328, 1, 0, '0000-00-00 00:00:00'),
(329, 1, 1, '2014-10-05 16:00:31'),
(330, 1, 0, '0000-00-00 00:00:00'),
(331, 1, 1, '2014-10-05 16:03:48'),
(332, 1, 0, '0000-00-00 00:00:00'),
(333, 1, 1, '2014-10-05 16:06:29'),
(334, 1, 0, '2014-10-05 16:10:43'),
(335, 1, 1, '2014-10-05 16:11:41'),
(336, 1, 0, '2014-10-05 16:14:09'),
(337, 1, 0, '2014-10-05 16:14:10'),
(338, 1, 0, '2014-10-05 16:14:11'),
(339, 1, 0, '2014-10-05 16:14:11'),
(340, 1, 1, '2014-10-05 16:15:27'),
(341, 1, 0, '2014-10-05 16:15:31'),
(342, 1, 1, '2014-10-05 16:16:31'),
(343, 1, 0, '2014-10-05 16:18:03'),
(344, 1, 1, '2014-10-05 16:18:49'),
(345, 1, 0, '2014-10-05 16:39:40'),
(346, 1, 1, '2014-10-05 16:55:40'),
(347, 1, 0, '2014-10-05 16:56:24'),
(348, 1, 1, '2014-10-05 16:56:36'),
(349, 1, 0, '2014-10-05 16:57:13'),
(350, 4, 1, '2014-10-05 17:03:34'),
(351, 4, 0, '2014-10-05 17:04:15'),
(352, 1, 1, '2014-10-05 17:04:21'),
(353, 1, 0, '2014-10-05 17:04:47'),
(354, 1, 1, '2014-10-05 17:05:10'),
(355, 1, 0, '2014-10-05 17:09:49'),
(356, 1, 1, '2014-10-05 17:10:06'),
(357, 1, 0, '2014-10-05 17:10:43'),
(358, 1, 1, '2014-10-05 17:11:55'),
(359, 1, 0, '2014-10-05 17:12:29'),
(360, 4, 1, '2014-10-05 17:12:36'),
(361, 4, 0, '2014-10-05 17:13:33'),
(362, 1, 1, '2014-10-05 17:16:18'),
(363, 1, 0, '2014-10-05 17:16:20'),
(364, 1, 1, '2014-10-05 17:16:53'),
(365, 1, 0, '2014-10-05 17:16:58'),
(366, 1, 1, '2014-10-05 17:17:37'),
(367, 1, 0, '2014-10-05 17:17:39'),
(368, 4, 1, '2014-10-05 17:18:11'),
(369, 4, 0, '2014-10-05 17:18:15'),
(370, 1, 1, '2014-10-05 17:32:44'),
(371, 1, 0, '2014-10-05 17:36:29'),
(372, 1, 1, '2014-10-06 10:46:22'),
(373, 1, 0, '2014-10-06 10:50:18'),
(374, 1, 1, '2014-10-07 16:21:53'),
(375, 1, 0, '2014-10-07 16:24:26'),
(376, 1, 1, '2014-10-08 21:48:26'),
(377, 1, 0, '2014-10-08 21:48:39'),
(378, 1, 1, '2014-10-08 21:53:09'),
(379, 1, 0, '0000-00-00 00:00:00'),
(380, 1, 1, '2014-10-08 21:55:03'),
(381, 1, 0, '0000-00-00 00:00:00'),
(382, 1, 1, '2014-10-08 22:04:01'),
(383, 2, 1, '2014-10-08 22:04:50'),
(384, 1, 0, '0000-00-00 00:00:00'),
(385, 2, 0, '0000-00-00 00:00:00'),
(386, 1, 1, '2014-10-08 22:11:03'),
(387, 1, 0, '2014-10-08 22:11:15'),
(388, 2, 0, '2014-10-08 22:11:17'),
(389, 1, 0, '2014-10-08 22:11:18'),
(390, 1, 0, '2014-10-08 22:11:21'),
(391, 1, 0, '2014-10-08 22:11:21'),
(392, 1, 1, '2014-10-08 22:17:11'),
(393, 1, 0, '0000-00-00 00:00:00'),
(394, 1, 1, '2014-10-08 22:19:20'),
(395, 1, 0, '2014-10-08 22:21:47'),
(396, 1, 0, '2014-10-08 22:21:47'),
(397, 4, 1, '2014-10-08 22:22:37'),
(398, 4, 0, '0000-00-00 00:00:00'),
(399, 1, 1, '2014-10-08 22:24:01'),
(400, 1, 0, '0000-00-00 00:00:00'),
(401, 1, 1, '2014-10-08 22:26:59'),
(402, 1, 0, '0000-00-00 00:00:00'),
(403, 4, 1, '2014-10-08 22:29:01'),
(404, 4, 0, '2014-10-08 22:29:25'),
(405, 1, 0, '2014-10-08 22:29:26'),
(406, 1, 0, '2014-10-08 22:29:26'),
(407, 4, 0, '2014-10-08 22:29:27'),
(408, 1, 1, '2014-10-08 22:29:29'),
(409, 4, 1, '2014-10-08 22:30:02'),
(410, 4, 0, '2014-10-08 22:30:07'),
(411, 1, 0, '0000-00-00 00:00:00'),
(412, 1, 1, '2014-10-08 22:34:28'),
(413, 1, 0, '0000-00-00 00:00:00'),
(414, 1, 1, '2014-10-08 22:36:17'),
(415, 1, 0, '0000-00-00 00:00:00'),
(416, 1, 1, '2014-10-08 22:37:44'),
(417, 1, 0, '2014-10-08 22:38:05'),
(418, 1, 0, '2014-10-08 22:38:06'),
(419, 1, 0, '2014-10-08 22:38:07'),
(420, 1, 0, '2014-10-08 22:38:07'),
(421, 1, 1, '2014-10-08 22:39:31'),
(422, 1, 0, '0000-00-00 00:00:00'),
(423, 1, 1, '2014-10-08 22:41:44'),
(424, 1, 0, '2014-10-08 22:44:47'),
(425, 1, 1, '2014-10-08 22:45:17'),
(426, 1, 0, '0000-00-00 00:00:00'),
(427, 1, 1, '2014-10-08 22:48:08'),
(428, 2, 1, '2014-10-08 22:48:24'),
(429, 1, 0, '0000-00-00 00:00:00'),
(430, 2, 0, '0000-00-00 00:00:00'),
(431, 1, 1, '2014-10-08 22:56:57'),
(432, 1, 0, '0000-00-00 00:00:00'),
(433, 1, 1, '2014-10-08 22:59:01'),
(434, 1, 0, '2014-10-08 23:00:04'),
(435, 2, 0, '2014-10-08 23:00:07'),
(436, 1, 0, '2014-10-08 23:00:09'),
(437, 1, 0, '2014-10-08 23:00:11'),
(438, 1, 0, '2014-10-08 23:00:13'),
(439, 1, 1, '2014-10-08 23:16:33'),
(440, 1, 0, '0000-00-00 00:00:00'),
(441, 1, 1, '2014-10-08 23:20:41'),
(442, 2, 1, '2014-10-08 23:20:47'),
(443, 2, 0, '2014-10-08 23:22:57'),
(444, 1, 0, '2014-10-08 23:22:58'),
(445, 1, 0, '2014-10-08 23:23:02'),
(446, 1, 1, '2014-10-08 23:23:07'),
(447, 2, 1, '2014-10-08 23:23:12'),
(448, 3, 1, '2014-10-08 23:23:18'),
(449, 1, 0, '0000-00-00 00:00:00'),
(450, 2, 0, '0000-00-00 00:00:00'),
(451, 3, 0, '0000-00-00 00:00:00'),
(452, 1, 1, '2014-10-08 23:24:59'),
(453, 3, 0, '2014-10-08 23:25:41'),
(454, 2, 0, '2014-10-08 23:25:44'),
(455, 1, 0, '2014-10-08 23:25:46'),
(456, 1, 1, '2014-10-08 23:31:46'),
(457, 2, 1, '2014-10-08 23:32:05'),
(458, 2, 0, '2014-10-08 23:32:22'),
(459, 1, 0, '2014-10-08 23:32:23'),
(460, 1, 1, '2014-10-08 23:39:42'),
(461, 2, 1, '2014-10-08 23:40:15'),
(462, 3, 1, '2014-10-08 23:40:34'),
(463, 4, 1, '2014-10-08 23:41:07'),
(464, 5, 1, '2014-10-08 23:42:01'),
(465, 6, 1, '2014-10-08 23:42:08'),
(466, 1, 0, '2014-10-08 23:44:46'),
(467, 2, 0, '2014-10-08 23:44:48'),
(468, 3, 0, '2014-10-08 23:44:48'),
(469, 4, 0, '2014-10-08 23:44:49'),
(470, 5, 0, '2014-10-08 23:44:49'),
(471, 6, 0, '2014-10-08 23:44:50'),
(472, 1, 1, '2014-10-08 23:45:02'),
(473, 2, 1, '2014-10-08 23:45:12'),
(474, 2, 0, '2014-10-08 23:46:51'),
(475, 1, 0, '2014-10-08 23:46:52'),
(476, 1, 1, '2014-10-08 23:46:58'),
(477, 2, 1, '2014-10-08 23:47:22'),
(478, 1, 0, '2014-10-08 23:47:33'),
(479, 1, 1, '2014-10-08 23:48:29'),
(480, 3, 1, '2014-10-08 23:48:51'),
(481, 1, 0, '2014-10-08 23:49:42'),
(482, 3, 0, '2014-10-08 23:49:44'),
(483, 2, 0, '2014-10-08 23:51:25'),
(484, 1, 1, '2014-10-08 23:51:38'),
(485, 1, 0, '0000-00-00 00:00:00'),
(486, 1, 1, '2014-10-08 23:52:20'),
(487, 1, 0, '2014-10-08 23:52:42'),
(488, 1, 1, '2014-10-08 23:56:14'),
(489, 1, 0, '2014-10-08 23:58:39'),
(490, 1, 1, '2014-10-09 00:00:32'),
(491, 1, 0, '2014-10-09 00:00:35'),
(492, 1, 1, '2014-10-09 00:05:37'),
(493, 1, 0, '0000-00-00 00:00:00'),
(494, 1, 1, '2014-10-09 00:16:00'),
(495, 1, 0, '2014-10-09 00:18:16'),
(496, 1, 1, '2014-10-09 00:23:44'),
(497, 1, 0, '2014-10-09 00:23:51'),
(498, 1, 1, '2014-10-09 00:25:16'),
(499, 1, 0, '2014-10-09 00:25:25'),
(500, 1, 1, '2014-10-09 00:28:07'),
(501, 1, 0, '2014-10-09 00:28:09'),
(502, 4, 1, '2014-10-09 00:34:17'),
(503, 4, 0, '0000-00-00 00:00:00'),
(504, 1, 1, '2014-10-09 00:42:59'),
(505, 1, 0, '2014-10-09 00:43:10'),
(506, 1, 1, '2014-10-09 00:45:06'),
(507, 1, 0, '2014-10-09 00:45:11'),
(508, 1, 1, '2014-10-09 00:48:52'),
(509, 1, 0, '2014-10-09 00:49:03'),
(510, 1, 1, '2014-10-09 00:59:27'),
(511, 1, 0, '2014-10-09 00:59:35'),
(512, 1, 1, '2014-10-09 01:00:38'),
(513, 1, 0, '2014-10-09 01:00:45'),
(514, 1, 1, '2014-10-09 01:02:47'),
(515, 1, 0, '2014-10-09 01:03:34'),
(516, 1, 1, '2014-10-09 01:04:08'),
(517, 1, 0, '2014-10-09 01:04:31'),
(518, 1, 1, '2014-10-09 01:12:41'),
(519, 1, 0, '0000-00-00 00:00:00'),
(520, 1, 1, '2014-10-09 01:13:31'),
(521, 1, 0, '2014-10-09 01:13:33'),
(522, 1, 1, '2014-10-09 01:14:24'),
(523, 1, 0, '2014-10-09 01:14:27'),
(524, 1, 1, '2014-10-09 01:15:02'),
(525, 1, 0, '0000-00-00 00:00:00'),
(526, 4, 1, '2014-10-09 01:16:39'),
(527, 1, 1, '2014-10-09 01:16:53'),
(528, 2, 1, '2014-10-09 01:16:59'),
(529, 3, 1, '2014-10-09 01:17:05'),
(530, 3, 0, '2014-10-09 01:17:44'),
(531, 2, 0, '2014-10-09 01:17:45'),
(532, 1, 0, '2014-10-09 01:17:46'),
(533, 4, 0, '2014-10-09 01:17:47'),
(534, 1, 1, '2014-10-09 01:21:02'),
(535, 1, 0, '2014-10-09 01:21:32'),
(536, 1, 1, '2014-10-09 01:21:59'),
(537, 1, 0, '2014-10-09 01:22:02'),
(538, 1, 1, '2014-11-13 20:33:34'),
(539, 1, 0, '2014-11-13 20:33:48'),
(540, 1, 1, '2014-11-20 20:26:46'),
(541, 1, 0, '0000-00-00 00:00:00'),
(542, 1, 1, '2014-11-20 20:28:50'),
(543, 1, 0, '0000-00-00 00:00:00'),
(544, 1, 1, '2014-11-20 20:30:37'),
(545, 1, 0, '0000-00-00 00:00:00'),
(546, 1, 1, '2014-11-20 20:36:11'),
(547, 1, 0, '0000-00-00 00:00:00'),
(548, 1, 1, '2014-11-20 20:38:38'),
(549, 1, 0, '0000-00-00 00:00:00'),
(550, 1, 1, '2014-11-20 20:44:27'),
(551, 1, 0, '0000-00-00 00:00:00'),
(552, 1, 1, '2014-11-20 20:46:05'),
(553, 2, 1, '2014-11-20 20:46:16'),
(554, 1, 0, '0000-00-00 00:00:00'),
(555, 2, 0, '0000-00-00 00:00:00'),
(556, 1, 1, '2014-11-22 12:02:03'),
(557, 1, 0, '0000-00-00 00:00:00'),
(558, 1, 1, '2014-11-22 17:08:46'),
(559, 2, 1, '2014-11-22 17:15:46'),
(560, 1, 0, '0000-00-00 00:00:00'),
(561, 1, 1, '2014-11-22 17:19:34'),
(562, 1, 0, '0000-00-00 00:00:00'),
(563, 2, 0, '0000-00-00 00:00:00'),
(564, 1, 1, '2014-11-22 19:09:01'),
(565, 2, 1, '2014-11-22 19:10:52'),
(566, 3, 1, '2014-11-22 19:12:01'),
(567, 4, 1, '2014-11-22 19:13:22');

-- --------------------------------------------------------

--
-- Table structure for table `event_dates`
--

CREATE TABLE IF NOT EXISTS `event_dates` (
`event_id` mediumint(8) unsigned NOT NULL,
  `event_name` varchar(20) NOT NULL,
  `event_start` datetime NOT NULL,
  `event_ending` datetime NOT NULL,
  `event_description` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `product_bar_visibility`
--

CREATE TABLE IF NOT EXISTS `product_bar_visibility` (
  `product_version_id` mediumint(8) unsigned NOT NULL,
  `product_type_id` mediumint(8) unsigned NOT NULL,
  `bar_id` mediumint(8) unsigned NOT NULL,
  `product_bar_visibility` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `product_bar_visibility`
--

INSERT INTO `product_bar_visibility` (`product_version_id`, `product_type_id`, `bar_id`, `product_bar_visibility`) VALUES
(1, 1, 1, 1),
(1, 2, 1, 1),
(1, 3, 1, 1),
(1, 4, 1, 1),
(1, 5, 1, 1),
(1, 6, 1, 1),
(1, 7, 1, 1),
(1, 8, 1, 1),
(1, 9, 1, 1),
(1, 10, 1, 1),
(1, 11, 1, 1),
(1, 14, 1, 1),
(1, 15, 1, 1),
(1, 16, 1, 1),
(1, 17, 1, 1),
(1, 18, 1, 1),
(1, 19, 1, 1),
(1, 19, 2, 1),
(2, 1, 1, 1),
(2, 2, 1, 1),
(2, 3, 1, 1),
(2, 4, 1, 1),
(2, 5, 1, 1),
(2, 6, 1, 1),
(2, 7, 1, 1),
(2, 8, 1, 1),
(2, 16, 1, 1),
(3, 1, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `product_class`
--

CREATE TABLE IF NOT EXISTS `product_class` (
`product_class_id` mediumint(8) unsigned NOT NULL,
  `class_name` varchar(45) NOT NULL,
  `class_color_hex` char(6) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `product_class`
--

INSERT INTO `product_class` (`product_class_id`, `class_name`, `class_color_hex`) VALUES
(1, 'Fris', 'DB8C0D'),
(2, 'Pils', 'FFFF33'),
(3, 'Speciaal Bier', '33ffff');

-- --------------------------------------------------------

--
-- Table structure for table `product_price_class`
--

CREATE TABLE IF NOT EXISTS `product_price_class` (
`product_price_class_id` mediumint(8) unsigned NOT NULL,
  `class_name` varchar(45) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `product_price_class`
--

INSERT INTO `product_price_class` (`product_price_class_id`, `class_name`) VALUES
(1, 'Normaal'),
(2, 'Evenement'),
(3, 'Extern');

-- --------------------------------------------------------

--
-- Table structure for table `product_transactions`
--

CREATE TABLE IF NOT EXISTS `product_transactions` (
`product_transaction_id` mediumint(8) unsigned NOT NULL,
  `product_version_id` mediumint(8) unsigned NOT NULL,
  `product_type_id` mediumint(8) unsigned NOT NULL,
  `transaction_credit_id` mediumint(8) unsigned NOT NULL,
  `product_amount` smallint(5) unsigned NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=110 ;

--
-- Dumping data for table `product_transactions`
--

INSERT INTO `product_transactions` (`product_transaction_id`, `product_version_id`, `product_type_id`, `transaction_credit_id`, `product_amount`) VALUES
(67, 2, 8, 2, 1),
(68, 2, 16, 2, 1),
(69, 1, 14, 2, 1),
(70, 1, 15, 2, 1),
(71, 3, 1, 3, 2),
(72, 2, 2, 3, 3),
(73, 2, 7, 3, 2),
(74, 2, 8, 3, 1),
(75, 1, 11, 3, 1),
(76, 1, 14, 3, 1),
(77, 1, 17, 3, 1),
(78, 2, 4, 4, 1),
(79, 2, 3, 6, 1),
(80, 1, 15, 7, 25),
(81, 2, 8, 8, 2),
(82, 1, 11, 9, 2),
(83, 2, 7, 10, 1),
(84, 2, 8, 10, 1),
(85, 2, 16, 10, 1),
(86, 1, 10, 10, 1),
(87, 1, 11, 10, 1),
(88, 1, 14, 10, 1),
(89, 1, 15, 10, 1),
(90, 1, 19, 10, 1),
(91, 2, 6, 13, 1),
(92, 2, 7, 13, 1),
(93, 1, 10, 13, 2),
(94, 1, 11, 13, 1),
(95, 1, 11, 14, 1),
(96, 2, 8, 20, 2),
(97, 2, 16, 20, 1),
(98, 2, 2, 22, 1),
(99, 2, 3, 22, 2),
(100, 2, 6, 22, 1),
(101, 2, 7, 22, 1),
(102, 2, 8, 22, 1),
(103, 1, 10, 22, 2),
(104, 1, 11, 22, 1),
(105, 1, 14, 22, 1),
(106, 1, 18, 22, 2),
(107, 1, 19, 22, 2),
(108, 1, 9, 23, 3),
(109, 1, 19, 24, 1);

-- --------------------------------------------------------

--
-- Table structure for table `product_types`
--

CREATE TABLE IF NOT EXISTS `product_types` (
  `product_version_id` mediumint(8) unsigned NOT NULL,
  `product_type_id` mediumint(8) unsigned NOT NULL,
  `product_class_id` mediumint(8) unsigned NOT NULL,
  `product_price_class_id` mediumint(8) unsigned NOT NULL,
  `product_price` mediumint(8) unsigned NOT NULL,
  `product_name` varchar(20) NOT NULL,
  `creation_date` datetime NOT NULL,
  `supply_product_id` mediumint(8) unsigned NOT NULL,
  `supply_product_percentage` float unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `product_types`
--

INSERT INTO `product_types` (`product_version_id`, `product_type_id`, `product_class_id`, `product_price_class_id`, `product_price`, `product_name`, `creation_date`, `supply_product_id`, `supply_product_percentage`) VALUES
(1, 1, 1, 1, 90, 'Cola', '2014-09-14 17:19:33', 1, 100),
(1, 2, 1, 1, 90, 'Fanta', '2014-09-14 17:19:33', 1, 100),
(1, 3, 1, 1, 100, 'Bier', '2014-09-14 17:13:34', 1, 100),
(1, 4, 1, 1, 90, 'Cassis', '2014-09-14 17:19:33', 1, 100),
(1, 5, 1, 1, 500, 'Wijn Fles', '2014-09-14 17:19:56', 1, 100),
(1, 6, 1, 1, 190, 'Hoegaarden', '2014-09-14 17:20:18', 1, 100),
(1, 7, 1, 1, 160, 'HJ Pils', '2014-09-14 17:20:36', 1, 100),
(1, 8, 1, 1, 260, 'Kwakje', '2014-09-14 17:21:00', 1, 100),
(1, 9, 2, 1, 150, 'Biba''s', '2014-09-16 00:31:59', 1, 100),
(1, 10, 2, 1, 150, 'Loempia''s', '2014-09-16 00:32:38', 1, 100),
(1, 11, 2, 1, 200, 'Vlammetjes', '2014-09-16 00:32:55', 1, 100),
(1, 14, 2, 1, 150, 'Chips', '2014-09-16 13:05:38', 1, 100),
(1, 15, 2, 1, 50, 'Koek', '2014-09-16 21:08:17', 1, 100),
(1, 16, 1, 1, 90, 'Icetea', '2014-09-16 21:08:17', 1, 100),
(1, 17, 3, 1, 0, 'Shift Bier', '2014-09-16 21:08:17', 1, 100),
(1, 18, 3, 1, 0, 'Shift Fanta', '2014-09-16 21:08:17', 1, 100),
(1, 19, 3, 1, 0, 'Shift Cola', '2014-09-16 21:08:17', 1, 100),
(2, 1, 1, 1, 90, 'Cola', '2014-09-14 17:19:33', 1, 100),
(2, 2, 1, 1, 90, 'Fanta', '2014-09-14 17:19:33', 1, 100),
(2, 3, 1, 1, 100, 'Bier', '2014-09-14 17:13:34', 1, 100),
(2, 4, 1, 1, 90, 'Cassis', '2014-09-14 17:19:33', 1, 100),
(2, 5, 1, 1, 500, 'Wijn Fles', '2014-09-14 17:19:56', 1, 100),
(2, 6, 1, 1, 180, 'Hoegaarden', '2014-09-14 17:20:18', 1, 100),
(2, 7, 1, 1, 160, 'HJ Pils', '2014-09-14 17:20:36', 1, 100),
(2, 8, 1, 1, 260, 'Kwakje', '2014-09-14 17:21:00', 1, 100),
(2, 16, 1, 1, 90, 'Icetea', '2014-09-16 21:08:17', 1, 100),
(3, 1, 1, 1, 100, 'Cola', '2014-09-14 17:19:33', 1, 100);

-- --------------------------------------------------------

--
-- Table structure for table `supply_orders`
--

CREATE TABLE IF NOT EXISTS `supply_orders` (
  `supply_order_id` mediumint(8) unsigned NOT NULL,
  `order_date` datetime NOT NULL,
  `order_price` mediumint(8) unsigned NOT NULL,
  `admin_id` mediumint(8) unsigned NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `supply_order_list`
--

CREATE TABLE IF NOT EXISTS `supply_order_list` (
`supply_order_list_id` mediumint(8) unsigned NOT NULL,
  `supply_order_id` mediumint(8) unsigned NOT NULL,
  `supply_product_id` mediumint(8) unsigned NOT NULL,
  `supply_product_amount` smallint(6) NOT NULL,
  `current_supply_product_price` mediumint(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `supply_products`
--

CREATE TABLE IF NOT EXISTS `supply_products` (
`supply_product_id` mediumint(8) unsigned NOT NULL,
  `product_name` varchar(45) NOT NULL,
  `current_supply_amount` float unsigned NOT NULL,
  `critical_supply_amount` float unsigned NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `supply_products`
--

INSERT INTO `supply_products` (`supply_product_id`, `product_name`, `current_supply_amount`, `critical_supply_amount`) VALUES
(1, 'TEST', 10000, 10);

-- --------------------------------------------------------

--
-- Table structure for table `transactions_credit`
--

CREATE TABLE IF NOT EXISTS `transactions_credit` (
`transaction_credit_id` mediumint(8) unsigned NOT NULL,
  `account_id` mediumint(8) unsigned NOT NULL,
  `admin_id` mediumint(8) unsigned NOT NULL,
  `client_id` mediumint(8) unsigned NOT NULL,
  `transaction_type_id` mediumint(8) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL,
  `transaction_amount` mediumint(9) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=25 ;

--
-- Dumping data for table `transactions_credit`
--

INSERT INTO `transactions_credit` (`transaction_credit_id`, `account_id`, `admin_id`, `client_id`, `transaction_type_id`, `transaction_date`, `transaction_amount`) VALUES
(1, 1, 1, 1, 2, '2014-10-23 11:39:05', 0),
(2, 3, 1, 1, 4, '2014-10-23 11:42:30', 550),
(3, 3, 1, 1, 4, '2014-10-23 12:20:00', 1400),
(4, 1, 1, 1, 2, '2014-10-23 12:20:11', 90),
(5, 1, 1, 1, 2, '2014-10-23 12:20:23', 0),
(6, 1, 1, 1, 2, '2014-10-23 12:20:25', 100),
(7, 2, 1, 1, 3, '2014-10-23 12:23:00', 1250),
(8, 2, 1, 1, 3, '2014-11-13 22:18:35', 520),
(9, 3, 1, 1, 4, '2014-11-13 22:18:56', 400),
(10, 2, 1, 1, 3, '2014-11-13 22:23:27', 1060),
(11, 2, 1, 1, 3, '2014-11-13 22:23:28', 0),
(12, 2, 1, 1, 3, '2014-11-13 22:23:57', 0),
(13, 2, 1, 1, 3, '2014-11-17 23:08:49', 840),
(14, 2, 1, 1, 3, '2014-11-17 23:09:01', 200),
(15, 2, 1, 1, 3, '2014-11-17 23:09:02', 0),
(16, 2, 1, 1, 3, '2014-11-17 23:09:04', 0),
(17, 2, 1, 1, 3, '2014-11-17 23:09:06', 0),
(18, 3, 1, 1, 4, '2014-11-17 23:35:04', 0),
(19, 3, 1, 1, 4, '2014-11-17 23:35:06', 0),
(20, 3, 1, 1, 4, '2014-11-17 23:35:22', 610),
(21, 1, 1, 1, 2, '2014-11-18 00:05:01', 0),
(22, 2, 1, 1, 3, '2014-11-20 18:37:45', 1540),
(23, 3, 1, 1, 4, '2014-11-20 18:53:48', 450),
(24, 2, 1, 1, 3, '2014-11-20 20:03:08', 0);

-- --------------------------------------------------------

--
-- Table structure for table `transactions_debit`
--

CREATE TABLE IF NOT EXISTS `transactions_debit` (
`transaction_debit_id` mediumint(8) unsigned NOT NULL,
  `account_id` mediumint(8) unsigned NOT NULL,
  `admin_id` mediumint(8) unsigned NOT NULL,
  `client_id` mediumint(8) unsigned NOT NULL,
  `transaction_type_id` mediumint(8) unsigned NOT NULL,
  `transaction_date` datetime NOT NULL,
  `transaction_amount` mediumint(9) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

--
-- Dumping data for table `transactions_debit`
--

INSERT INTO `transactions_debit` (`transaction_debit_id`, `account_id`, `admin_id`, `client_id`, `transaction_type_id`, `transaction_date`, `transaction_amount`) VALUES
(1, 2, 1, 1, 3, '2014-11-13 22:18:35', 520),
(2, 3, 1, 1, 4, '2014-11-13 22:18:56', 400),
(3, 2, 1, 1, 3, '2014-11-13 22:23:27', 1060),
(4, 2, 1, 1, 3, '2014-11-13 22:23:28', 0),
(5, 2, 1, 1, 3, '2014-11-13 22:23:57', 0),
(6, 2, 1, 1, 3, '2014-11-17 23:08:49', 840),
(7, 2, 1, 1, 3, '2014-11-17 23:09:01', 200),
(8, 2, 1, 1, 3, '2014-11-17 23:09:02', 0),
(9, 2, 1, 1, 3, '2014-11-17 23:09:04', 0),
(10, 2, 1, 1, 3, '2014-11-17 23:09:06', 0),
(11, 3, 1, 1, 4, '2014-11-17 23:35:04', 0),
(12, 3, 1, 1, 4, '2014-11-17 23:35:06', 0),
(13, 3, 1, 1, 4, '2014-11-17 23:35:22', 610),
(14, 1, 1, 1, 2, '2014-11-18 00:05:01', 0),
(15, 2, 1, 1, 3, '2014-11-20 18:37:45', 1540),
(16, 3, 1, 1, 4, '2014-11-20 18:53:47', 450),
(17, 2, 1, 1, 3, '2014-11-20 20:03:08', 0);

-- --------------------------------------------------------

--
-- Table structure for table `transaction_types`
--

CREATE TABLE IF NOT EXISTS `transaction_types` (
`transaction_type_id` mediumint(8) unsigned NOT NULL,
  `transaction_type_name` varchar(45) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `transaction_types`
--

INSERT INTO `transaction_types` (`transaction_type_id`, `transaction_type_name`) VALUES
(1, 'barpas'),
(2, 'pin'),
(3, 'contant'),
(4, 'schrapkaart');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
`user_id` mediumint(8) unsigned NOT NULL,
  `account_id` mediumint(8) unsigned NOT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(40) NOT NULL,
  `email` varchar(40) NOT NULL,
  `pass` char(40) NOT NULL,
  `registration_date` datetime NOT NULL,
  `birth_date` date NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `account_id`, `first_name`, `last_name`, `email`, `pass`, `registration_date`, `birth_date`) VALUES
(1, 1, 'Jaap', 'Wesdorp', 'Jaapwesdorp@gmail.com', '789b49606c321c8cf228d17942608eff0ccc4171', '2014-09-20 23:51:17', '1993-03-25');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
 ADD PRIMARY KEY (`account_id`);

--
-- Indexes for table `admins`
--
ALTER TABLE `admins`
 ADD PRIMARY KEY (`admin_id`), ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `admin_changes`
--
ALTER TABLE `admin_changes`
 ADD PRIMARY KEY (`admin_change_id`), ADD KEY `current_product_price_class_id` (`current_product_price_class_id`), ADD KEY `admin_id` (`admin_id`);

--
-- Indexes for table `admin_logs`
--
ALTER TABLE `admin_logs`
 ADD PRIMARY KEY (`admin_log_id`), ADD KEY `bar_id` (`bar_id`), ADD KEY `admin_id` (`admin_id`);

--
-- Indexes for table `bars`
--
ALTER TABLE `bars`
 ADD PRIMARY KEY (`bar_id`);

--
-- Indexes for table `cards`
--
ALTER TABLE `cards`
 ADD PRIMARY KEY (`card_id`), ADD KEY `account_id` (`account_id`);

--
-- Indexes for table `clients`
--
ALTER TABLE `clients`
 ADD PRIMARY KEY (`client_id`), ADD KEY `bar_id` (`bar_id`);

--
-- Indexes for table `client_logs`
--
ALTER TABLE `client_logs`
 ADD PRIMARY KEY (`client_log_id`), ADD KEY `client_id` (`client_id`);

--
-- Indexes for table `event_dates`
--
ALTER TABLE `event_dates`
 ADD PRIMARY KEY (`event_id`);

--
-- Indexes for table `product_bar_visibility`
--
ALTER TABLE `product_bar_visibility`
 ADD PRIMARY KEY (`product_version_id`,`product_type_id`,`bar_id`), ADD KEY `bar_id` (`bar_id`);

--
-- Indexes for table `product_class`
--
ALTER TABLE `product_class`
 ADD PRIMARY KEY (`product_class_id`);

--
-- Indexes for table `product_price_class`
--
ALTER TABLE `product_price_class`
 ADD PRIMARY KEY (`product_price_class_id`);

--
-- Indexes for table `product_transactions`
--
ALTER TABLE `product_transactions`
 ADD PRIMARY KEY (`product_transaction_id`), ADD KEY `product_version_id` (`product_version_id`,`product_type_id`), ADD KEY `transaction_credit_id` (`transaction_credit_id`);

--
-- Indexes for table `product_types`
--
ALTER TABLE `product_types`
 ADD PRIMARY KEY (`product_version_id`,`product_type_id`), ADD KEY `product_class_id` (`product_class_id`), ADD KEY `product_price_class_id` (`product_price_class_id`), ADD KEY `supply_product_id` (`supply_product_id`);

--
-- Indexes for table `supply_orders`
--
ALTER TABLE `supply_orders`
 ADD PRIMARY KEY (`supply_order_id`), ADD KEY `admin_id` (`admin_id`);

--
-- Indexes for table `supply_order_list`
--
ALTER TABLE `supply_order_list`
 ADD PRIMARY KEY (`supply_order_list_id`), ADD KEY `supply_order_id` (`supply_order_id`), ADD KEY `supply_product_id` (`supply_product_id`);

--
-- Indexes for table `supply_products`
--
ALTER TABLE `supply_products`
 ADD PRIMARY KEY (`supply_product_id`);

--
-- Indexes for table `transactions_credit`
--
ALTER TABLE `transactions_credit`
 ADD PRIMARY KEY (`transaction_credit_id`), ADD KEY `account_id` (`account_id`), ADD KEY `admin_id` (`admin_id`), ADD KEY `client_id` (`client_id`), ADD KEY `transaction_type_id` (`transaction_type_id`);

--
-- Indexes for table `transactions_debit`
--
ALTER TABLE `transactions_debit`
 ADD PRIMARY KEY (`transaction_debit_id`), ADD KEY `account_id` (`account_id`), ADD KEY `admin_id` (`admin_id`), ADD KEY `client_id` (`client_id`), ADD KEY `transaction_type_id` (`transaction_type_id`);

--
-- Indexes for table `transaction_types`
--
ALTER TABLE `transaction_types`
 ADD PRIMARY KEY (`transaction_type_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
 ADD PRIMARY KEY (`user_id`), ADD KEY `account_id` (`account_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
MODIFY `account_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `admins`
--
ALTER TABLE `admins`
MODIFY `admin_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `admin_changes`
--
ALTER TABLE `admin_changes`
MODIFY `admin_change_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `admin_logs`
--
ALTER TABLE `admin_logs`
MODIFY `admin_log_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `bars`
--
ALTER TABLE `bars`
MODIFY `bar_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `cards`
--
ALTER TABLE `cards`
MODIFY `card_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `clients`
--
ALTER TABLE `clients`
MODIFY `client_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `client_logs`
--
ALTER TABLE `client_logs`
MODIFY `client_log_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=568;
--
-- AUTO_INCREMENT for table `event_dates`
--
ALTER TABLE `event_dates`
MODIFY `event_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `product_class`
--
ALTER TABLE `product_class`
MODIFY `product_class_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `product_price_class`
--
ALTER TABLE `product_price_class`
MODIFY `product_price_class_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `product_transactions`
--
ALTER TABLE `product_transactions`
MODIFY `product_transaction_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=110;
--
-- AUTO_INCREMENT for table `supply_order_list`
--
ALTER TABLE `supply_order_list`
MODIFY `supply_order_list_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `supply_products`
--
ALTER TABLE `supply_products`
MODIFY `supply_product_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `transactions_credit`
--
ALTER TABLE `transactions_credit`
MODIFY `transaction_credit_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=25;
--
-- AUTO_INCREMENT for table `transactions_debit`
--
ALTER TABLE `transactions_debit`
MODIFY `transaction_debit_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT for table `transaction_types`
--
ALTER TABLE `transaction_types`
MODIFY `transaction_type_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
MODIFY `user_id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `admins`
--
ALTER TABLE `admins`
ADD CONSTRAINT `admins_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `admin_changes`
--
ALTER TABLE `admin_changes`
ADD CONSTRAINT `admin_changes_ibfk_1` FOREIGN KEY (`current_product_price_class_id`) REFERENCES `product_price_class` (`product_price_class_id`),
ADD CONSTRAINT `admin_changes_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`admin_id`);

--
-- Constraints for table `admin_logs`
--
ALTER TABLE `admin_logs`
ADD CONSTRAINT `admin_logs_ibfk_1` FOREIGN KEY (`bar_id`) REFERENCES `bars` (`bar_id`),
ADD CONSTRAINT `admin_logs_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`admin_id`);

--
-- Constraints for table `cards`
--
ALTER TABLE `cards`
ADD CONSTRAINT `cards_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`);

--
-- Constraints for table `clients`
--
ALTER TABLE `clients`
ADD CONSTRAINT `clients_ibfk_1` FOREIGN KEY (`bar_id`) REFERENCES `bars` (`bar_id`);

--
-- Constraints for table `client_logs`
--
ALTER TABLE `client_logs`
ADD CONSTRAINT `client_logs_ibfk_1` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`);

--
-- Constraints for table `product_bar_visibility`
--
ALTER TABLE `product_bar_visibility`
ADD CONSTRAINT `product_bar_visibility_ibfk_1` FOREIGN KEY (`product_version_id`, `product_type_id`) REFERENCES `product_types` (`product_version_id`, `product_type_id`),
ADD CONSTRAINT `product_bar_visibility_ibfk_2` FOREIGN KEY (`bar_id`) REFERENCES `bars` (`bar_id`);

--
-- Constraints for table `product_transactions`
--
ALTER TABLE `product_transactions`
ADD CONSTRAINT `product_transactions_ibfk_1` FOREIGN KEY (`product_version_id`, `product_type_id`) REFERENCES `product_types` (`product_version_id`, `product_type_id`),
ADD CONSTRAINT `product_transactions_ibfk_2` FOREIGN KEY (`transaction_credit_id`) REFERENCES `transactions_credit` (`transaction_credit_id`);

--
-- Constraints for table `product_types`
--
ALTER TABLE `product_types`
ADD CONSTRAINT `product_types_ibfk_1` FOREIGN KEY (`product_class_id`) REFERENCES `product_class` (`product_class_id`),
ADD CONSTRAINT `product_types_ibfk_2` FOREIGN KEY (`product_price_class_id`) REFERENCES `product_price_class` (`product_price_class_id`),
ADD CONSTRAINT `product_types_ibfk_3` FOREIGN KEY (`supply_product_id`) REFERENCES `supply_products` (`supply_product_id`);

--
-- Constraints for table `supply_orders`
--
ALTER TABLE `supply_orders`
ADD CONSTRAINT `supply_orders_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`admin_id`);

--
-- Constraints for table `supply_order_list`
--
ALTER TABLE `supply_order_list`
ADD CONSTRAINT `supply_order_list_ibfk_1` FOREIGN KEY (`supply_order_id`) REFERENCES `supply_orders` (`supply_order_id`),
ADD CONSTRAINT `supply_order_list_ibfk_2` FOREIGN KEY (`supply_product_id`) REFERENCES `supply_products` (`supply_product_id`);

--
-- Constraints for table `transactions_credit`
--
ALTER TABLE `transactions_credit`
ADD CONSTRAINT `transactions_credit_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
ADD CONSTRAINT `transactions_credit_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`admin_id`),
ADD CONSTRAINT `transactions_credit_ibfk_3` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`),
ADD CONSTRAINT `transactions_credit_ibfk_4` FOREIGN KEY (`transaction_type_id`) REFERENCES `transaction_types` (`transaction_type_id`);

--
-- Constraints for table `transactions_debit`
--
ALTER TABLE `transactions_debit`
ADD CONSTRAINT `transactions_debit_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
ADD CONSTRAINT `transactions_debit_ibfk_2` FOREIGN KEY (`admin_id`) REFERENCES `admins` (`admin_id`),
ADD CONSTRAINT `transactions_debit_ibfk_3` FOREIGN KEY (`client_id`) REFERENCES `clients` (`client_id`),
ADD CONSTRAINT `transactions_debit_ibfk_4` FOREIGN KEY (`transaction_type_id`) REFERENCES `transaction_types` (`transaction_type_id`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
