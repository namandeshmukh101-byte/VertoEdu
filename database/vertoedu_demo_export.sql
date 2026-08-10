/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: academic_years
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `academic_years` (
  `end_date` date DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `start_date` date DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK5cbe6cdpkcenvfsah496sxq3y` (`school_id`, `name`),
  CONSTRAINT `FKeh3xckk0s8t44khlgba0o4t3j` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 2 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: ai_reviews
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `ai_reviews` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ocr_result_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `suggested_data_json` json NOT NULL,
  `suggestions_text` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKg7sghqbfehmx2e8rx2texvobq` (`ocr_result_id`),
  CONSTRAINT `FKnnoiswsddgjkgd15q3i1i046l` FOREIGN KEY (`ocr_result_id`) REFERENCES `ocr_results` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 17 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: approval_logs
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `approval_logs` (
  `is_approved` bit(1) NOT NULL,
  `admin_user_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `document_upload_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `final_approved_data_json` json DEFAULT NULL,
  `rejection_reason` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK2a4lhq12aeh5h5uvx69w2u0i9` (`document_upload_id`),
  KEY `FK5gfg0rbox6qhkscdpvbt29evl` (`admin_user_id`),
  CONSTRAINT `FK1kcyruchy7l9oi16386ggxyfc` FOREIGN KEY (`document_upload_id`) REFERENCES `document_uploads` (`id`),
  CONSTRAINT `FK5gfg0rbox6qhkscdpvbt29evl` FOREIGN KEY (`admin_user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: attendance_records
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `attendance_records` (
  `date` date NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recorded_by_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKds62wkr9c84fblmc74p7eouac` (`student_id`, `date`),
  KEY `FKtoaedas5c77u7eepgy96nssrt` (`recorded_by_id`),
  CONSTRAINT `FKb5ijilkgrgx66qn66iajdkyb9` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  CONSTRAINT `FKtoaedas5c77u7eepgy96nssrt` FOREIGN KEY (`recorded_by_id`) REFERENCES `teachers` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 4 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: document_uploads
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `document_uploads` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_id` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `uploaded_by_user_id` bigint NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `document_type` enum(
  'ADMISSION_FORM',
  'BIRTH_CERTIFICATE',
  'MARK_SHEET',
  'TRANSFER_CERTIFICATE'
  ) NOT NULL,
  `status` enum(
  'AI_COMPLETED',
  'APPROVED',
  'OCR_COMPLETED',
  'REJECTED',
  'UPLOADED'
  ) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKppexeghld7e6k96jrvlxr05pd` (`school_id`),
  KEY `FK36ahjmtwg420lpijuue4d7vyb` (`uploaded_by_user_id`),
  CONSTRAINT `FK36ahjmtwg420lpijuue4d7vyb` FOREIGN KEY (`uploaded_by_user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKppexeghld7e6k96jrvlxr05pd` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 66 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: exam_results
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `exam_results` (
  `marks_obtained` double NOT NULL,
  `max_marks` double NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `exam_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `recorded_by_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `subject_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKet9gqpwsoo4oj9g2vnjjclvs1` (`student_id`, `subject_id`, `exam_id`),
  KEY `FKtf85ht7yquiorwjx2xbdx3fxw` (`exam_id`),
  KEY `FK9lpnyr5oto7gf0kdbpijrn9pg` (`recorded_by_id`),
  KEY `FK1h1qupqh86nvnjvuui13tv89u` (`subject_id`),
  CONSTRAINT `FK1h1qupqh86nvnjvuui13tv89u` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`),
  CONSTRAINT `FK9lpnyr5oto7gf0kdbpijrn9pg` FOREIGN KEY (`recorded_by_id`) REFERENCES `teachers` (`id`),
  CONSTRAINT `FKr7qgl670f47u65kkdm8ex5119` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  CONSTRAINT `FKtf85ht7yquiorwjx2xbdx3fxw` FOREIGN KEY (`exam_id`) REFERENCES `exams` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 4 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: exams
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `exams` (
  `academic_year_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkjpygans57taw97kuko0ocpba` (`academic_year_id`, `name`),
  KEY `FK58snu3x30ly9owm86j8bqbrd2` (`school_id`),
  CONSTRAINT `FK58snu3x30ly9owm86j8bqbrd2` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`),
  CONSTRAINT `FKddehgdbvhn56aeo9hempt82qq` FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 2 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: notices
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `notices` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `content` text NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK98pwy06fm1aicue0jqqloin89` (`school_id`),
  CONSTRAINT `FK98pwy06fm1aicue0jqqloin89` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: ocr_results
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `ocr_results` (
  `created_at` datetime(6) NOT NULL,
  `document_upload_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `extracted_data_json` json NOT NULL,
  `raw_text` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfvexo6xg13r5t322mpxibxwo4` (`document_upload_id`),
  CONSTRAINT `FKgrlh8sl5mmoc3v3lpwes19kgc` FOREIGN KEY (`document_upload_id`) REFERENCES `document_uploads` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 55 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: parent_student_link_requests
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `parent_student_link_requests` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `scholar_number` varchar(255) NOT NULL,
  `status` enum('APPROVED', 'PENDING', 'REJECTED') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqd3dop118hw2oe730v2lvsndc` (`parent_id`),
  CONSTRAINT `FKqd3dop118hw2oe730v2lvsndc` FOREIGN KEY (`parent_id`) REFERENCES `parents` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: parents
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `parents` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `alternate_contact` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKc1t2v6wf187l8w0yew9sph3l4` (`user_id`),
  KEY `FKatq0lg3m5wavlfe3p7kfkvwpj` (`school_id`),
  CONSTRAINT `FKatq0lg3m5wavlfe3p7kfkvwpj` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`),
  CONSTRAINT `FKchh8tf8w072tapgqoijrahojk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 142 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: roles
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `roles` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) NOT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 4 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: school_classes
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `school_classes` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `level` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlme3kvo01xfmaedqrkyii5njy` (`school_id`, `name`),
  CONSTRAINT `FK5pyw0mtl3c0033cwb8v2grg9c` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 4 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: schools
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `schools` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contact_email` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKehwqlfa7xseucba45p6wlqfgn` (`name`)
) ENGINE = InnoDB AUTO_INCREMENT = 56 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: sections
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `sections` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_class_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3o44g36cxfp2nlu1ejwt4u6lm` (`school_class_id`, `name`),
  CONSTRAINT `FKpxpbk38aktd71xg1vkq69cvhj` FOREIGN KEY (`school_class_id`) REFERENCES `school_classes` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 6 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: student_history
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `student_history` (
  `academic_year_id` bigint NOT NULL,
  `class_id` bigint NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `section_id` bigint DEFAULT NULL,
  `student_id` bigint NOT NULL,
  `roll_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKj6rjovasjri9321ycu8fnk3k2` (`academic_year_id`),
  KEY `FKoec0gq8pkcauq9c0gkji0q41y` (`class_id`),
  KEY `FKqdk7olvrnprvfi4008twdeurb` (`section_id`),
  KEY `FK6y8hvjnp9qcmo4epcbnjxe70r` (`student_id`),
  CONSTRAINT `FK6y8hvjnp9qcmo4epcbnjxe70r` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`),
  CONSTRAINT `FKj6rjovasjri9321ycu8fnk3k2` FOREIGN KEY (`academic_year_id`) REFERENCES `academic_years` (`id`),
  CONSTRAINT `FKoec0gq8pkcauq9c0gkji0q41y` FOREIGN KEY (`class_id`) REFERENCES `school_classes` (`id`),
  CONSTRAINT `FKqdk7olvrnprvfi4008twdeurb` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: students
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `students` (
  `dob` date DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL,
  `school_id` bigint NOT NULL,
  `section_id` bigint DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `scholar_number` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1in8qp99flw2hq1vctoq1j0gn` (`school_id`, `scholar_number`),
  KEY `FK7bbpphkk8f0aoav3iiih3mh4e` (`parent_id`),
  KEY `FKbu72kq4xd8qjcemytgfxel71l` (`section_id`),
  CONSTRAINT `FK7bbpphkk8f0aoav3iiih3mh4e` FOREIGN KEY (`parent_id`) REFERENCES `parents` (`id`),
  CONSTRAINT `FKbu72kq4xd8qjcemytgfxel71l` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`),
  CONSTRAINT `FKdojmg8v3rw2ow4dev2b8q5oqq` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 176 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: subjects
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `subjects` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKfalygpi28hqjdas693n3euayl` (`school_id`, `code`),
  CONSTRAINT `FKmuktvnrq4ft25nduvev1wseqd` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 2 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: teacher_sections
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `teacher_sections` (
  `section_id` bigint NOT NULL,
  `teacher_id` bigint NOT NULL,
  PRIMARY KEY (`section_id`, `teacher_id`),
  KEY `FKnwt3kd9rvpypym8bno8tcgtfl` (`teacher_id`),
  CONSTRAINT `FK2ww7518xigahlwvxywbajt06l` FOREIGN KEY (`section_id`) REFERENCES `sections` (`id`),
  CONSTRAINT `FKnwt3kd9rvpypym8bno8tcgtfl` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: teacher_subjects
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `teacher_subjects` (
  `subject_id` bigint NOT NULL,
  `teacher_id` bigint NOT NULL,
  PRIMARY KEY (`subject_id`, `teacher_id`),
  KEY `FK6dcl3ihufp4v0j1fuxlw4ksoj` (`teacher_id`),
  CONSTRAINT `FK6dcl3ihufp4v0j1fuxlw4ksoj` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`id`),
  CONSTRAINT `FKdweqkwxroox2u7pbmksehx04i` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: teachers
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `teachers` (
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `school_id` bigint NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `employee_id` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKruivy0hschywhf1uptbt687ts` (`school_id`, `employee_id`),
  UNIQUE KEY `UKcd1k6xwg9jqtiwx9ybnxpmoh9` (`user_id`),
  CONSTRAINT `FK25tvrvw3ww2p7mbt62abrbwev` FOREIGN KEY (`school_id`) REFERENCES `schools` (`id`),
  CONSTRAINT `FKb8dct7w2j1vl1r2bpstw5isc0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 4 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# SCHEMA DUMP FOR TABLE: users
# ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `users` (
  `active` bit(1) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `profile_image` varchar(512) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `google_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKovh8xmu9ac27t18m56gri58i1` (`google_id`),
  KEY `idx_user_email` (`email`),
  KEY `idx_user_google_id` (`google_id`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 75 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: academic_years
# ------------------------------------------------------------

INSERT INTO
  `academic_years` (
    `end_date`,
    `is_active`,
    `start_date`,
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `name`
  )
VALUES
  (
    '2027-06-30',
    b'1',
    '2026-09-01',
    '2026-08-08 20:28:09.223087',
    1,
    1,
    '2026-08-08 20:28:09.223087',
    '2026-2027'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: ai_reviews
# ------------------------------------------------------------


# ------------------------------------------------------------
# DATA DUMP FOR TABLE: approval_logs
# ------------------------------------------------------------


# ------------------------------------------------------------
# DATA DUMP FOR TABLE: attendance_records
# ------------------------------------------------------------

INSERT INTO
  `attendance_records` (
    `date`,
    `created_at`,
    `id`,
    `recorded_by_id`,
    `student_id`,
    `updated_at`,
    `remarks`,
    `status`
  )
VALUES
  (
    '2026-08-09',
    '2026-08-09 17:04:31.778065',
    1,
    2,
    132,
    '2026-08-09 17:04:31.778065',
    NULL,
    'ABSENT'
  );
INSERT INTO
  `attendance_records` (
    `date`,
    `created_at`,
    `id`,
    `recorded_by_id`,
    `student_id`,
    `updated_at`,
    `remarks`,
    `status`
  )
VALUES
  (
    '2026-08-09',
    '2026-08-09 17:04:31.778065',
    2,
    2,
    133,
    '2026-08-09 17:04:31.778065',
    NULL,
    'PRESENT'
  );
INSERT INTO
  `attendance_records` (
    `date`,
    `created_at`,
    `id`,
    `recorded_by_id`,
    `student_id`,
    `updated_at`,
    `remarks`,
    `status`
  )
VALUES
  (
    '2026-08-09',
    '2026-08-09 17:04:31.778065',
    3,
    2,
    131,
    '2026-08-09 17:04:31.778065',
    NULL,
    'PRESENT'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: document_uploads
# ------------------------------------------------------------

INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-08 20:41:40.381433',
    1,
    NULL,
    '2026-08-08 20:41:41.158222',
    3,
    'test_image.jpg',
    'uploads\\ocr\\c29ae035-92c7-4112-ac4e-3d27c4b22841.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-08 20:41:41.290038',
    2,
    NULL,
    '2026-08-08 20:41:41.763168',
    3,
    'test_document.pdf',
    'uploads\\ocr\\c0271049-5256-495f-b50b-d617c6b925d1.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-08 20:41:41.816218',
    3,
    NULL,
    '2026-08-08 20:41:42.131710',
    3,
    'test_image.png',
    'uploads\\ocr\\2b53bf37-9321-43a4-a33a-c95505299b62.png',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-08 20:51:20.999114',
    4,
    NULL,
    '2026-08-08 20:51:21.729192',
    3,
    'test_image.jpg',
    'uploads\\ocr\\6ae9d0f4-7cba-4083-80bd-99f8acaee8cc.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-08 20:51:21.832085',
    5,
    NULL,
    '2026-08-08 20:51:22.284429',
    3,
    'test_document.pdf',
    'uploads\\ocr\\a290aad2-3464-49cd-af1e-142bc754b5ab.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-08 20:51:22.345323',
    6,
    NULL,
    '2026-08-08 20:51:22.992956',
    3,
    'test_image.png',
    'uploads\\ocr\\c017e728-826e-4ee2-8cd5-edb59488d092.png',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 11:59:48.063185',
    7,
    NULL,
    '2026-08-09 11:59:51.596925',
    3,
    'test_image.jpg',
    'uploads\\ocr\\6362726a-f6e2-4011-8472-94c4fa710901.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 11:59:51.736634',
    8,
    NULL,
    '2026-08-09 11:59:52.559814',
    3,
    'test_document.pdf',
    'uploads\\ocr\\e7ac6c94-34d6-4184-8f1d-5c541676109c.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 11:59:52.883811',
    9,
    NULL,
    '2026-08-09 11:59:53.926197',
    3,
    'test_image.png',
    'uploads\\ocr\\f6d4f858-cb50-49c1-ad0d-e500daf8a38a.png',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 12:01:00.409482',
    10,
    NULL,
    '2026-08-09 12:01:01.376935',
    3,
    'test_image.jpg',
    'uploads\\ocr\\29a6ba68-6890-477b-9217-e18e769686eb.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 12:01:01.512307',
    11,
    NULL,
    '2026-08-09 12:01:03.720635',
    3,
    'test_document.pdf',
    'uploads\\ocr\\ef0230d9-9932-4ae9-a36b-8a273fc73da7.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 12:01:03.800450',
    12,
    NULL,
    '2026-08-09 12:01:05.362927',
    3,
    'test_image.png',
    'uploads\\ocr\\578d9089-60d8-4fde-aec2-dd3de1ef9959.png',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 13:31:50.267417',
    22,
    NULL,
    '2026-08-09 13:31:53.644613',
    3,
    'test_image.jpg',
    'uploads\\ocr\\80fd3694-4dbd-4b06-9126-b317ce72e2fd.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 13:31:53.754931',
    23,
    NULL,
    '2026-08-09 13:31:55.206901',
    3,
    'test_document.pdf',
    'uploads\\ocr\\d440f3c8-cafc-4d3e-9879-641845aafe91.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 13:31:55.363899',
    24,
    NULL,
    '2026-08-09 13:31:56.818811',
    3,
    'test_image.png',
    'uploads\\ocr\\8cbbb960-a179-4547-84d8-309a8a5f1ea3.png',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 14:05:55.347179',
    29,
    NULL,
    '2026-08-09 14:05:55.347179',
    3,
    'test_image.jpg',
    'uploads\\ocr\\6f8d3f06-f314-4fcb-a8ec-980b6ab7677f.jpg',
    'ADMISSION_FORM',
    'UPLOADED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 14:05:55.659859',
    30,
    NULL,
    '2026-08-09 14:05:55.659859',
    3,
    'test_document.pdf',
    'uploads\\ocr\\a806b8cd-f9e1-447c-b886-b2ed2ec70cb1.pdf',
    'ADMISSION_FORM',
    'UPLOADED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 14:05:55.791327',
    31,
    NULL,
    '2026-08-09 14:05:55.791327',
    3,
    'test_image.png',
    'uploads\\ocr\\72c88057-fa1d-4904-b742-cf6274b4010a.png',
    'ADMISSION_FORM',
    'UPLOADED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 14:43:06.982107',
    32,
    NULL,
    '2026-08-09 14:43:09.398935',
    4,
    'Antigravity_coursePDF.pdf',
    'uploads\\ocr\\41ec59fe-ea94-4e3a-a469-240411477f8c.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 15:20:19.426745',
    33,
    NULL,
    '2026-08-09 15:20:20.389502',
    4,
    'test_doc.jpg',
    'uploads\\ocr\\9aa7b3e1-a25e-4de6-8a0b-686f72a1ae14.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 16:46:11.918877',
    42,
    NULL,
    '2026-08-09 16:46:14.342692',
    3,
    'test_image.jpg',
    'uploads\\ocr\\6826c10f-2271-4693-a349-1732e15b9203.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 16:46:14.480185',
    43,
    NULL,
    '2026-08-09 16:46:15.827769',
    3,
    'test_document.pdf',
    'uploads\\ocr\\e1747e03-1cb3-465a-ae02-515030414f12.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 16:46:15.891996',
    44,
    NULL,
    '2026-08-09 16:46:17.368207',
    3,
    'test_image.png',
    'uploads\\ocr\\5b0d6598-e317-4854-a810-0b9485df4e8a.png',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 18:29:24.068463',
    49,
    NULL,
    '2026-08-09 18:29:25.881910',
    3,
    'test_image.jpg',
    'uploads\\ocr\\28dca90e-9e98-4e49-96e3-30a0493e40fe.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 18:29:25.962910',
    50,
    NULL,
    '2026-08-09 18:29:26.701939',
    3,
    'test_document.pdf',
    'uploads\\ocr\\ad435a01-28e3-4ef9-aa8b-ac9523ce5d49.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 18:29:26.751472',
    51,
    NULL,
    '2026-08-09 18:29:27.393585',
    3,
    'test_image.png',
    'uploads\\ocr\\83bda66b-4288-4fbb-a218-2ae3ae7c597b.png',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 18:31:28.095206',
    56,
    NULL,
    '2026-08-09 18:31:29.913169',
    3,
    'test_image.jpg',
    'uploads\\ocr\\1431755b-572a-42dd-b30f-ed19d64418c4.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 18:31:30.008455',
    57,
    NULL,
    '2026-08-09 18:31:30.972036',
    3,
    'test_document.pdf',
    'uploads\\ocr\\4a9a4f79-c602-4dc4-8a84-f74e5e9875f5.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 18:31:31.041089',
    58,
    NULL,
    '2026-08-09 18:31:31.765155',
    3,
    'test_image.png',
    'uploads\\ocr\\07fb43bc-0120-42c0-82d7-8da7b7fe8fdb.png',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 18:33:39.617814',
    63,
    NULL,
    '2026-08-09 18:33:40.640449',
    3,
    'test_image.jpg',
    'uploads\\ocr\\505884e1-5621-4ae9-9209-d19f674cfaf2.jpg',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 18:33:40.704928',
    64,
    NULL,
    '2026-08-09 18:33:41.264072',
    3,
    'test_document.pdf',
    'uploads\\ocr\\0461cd17-700f-4832-9f7a-dfe06da31397.pdf',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );
INSERT INTO
  `document_uploads` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `uploaded_by_user_id`,
    `file_name`,
    `file_path`,
    `document_type`,
    `status`
  )
VALUES
  (
    '2026-08-09 18:33:41.317593',
    65,
    NULL,
    '2026-08-09 18:33:41.742364',
    3,
    'test_image.png',
    'uploads\\ocr\\0a47688e-6cf6-460e-aec7-6577906bc89e.png',
    'ADMISSION_FORM',
    'OCR_COMPLETED'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: exam_results
# ------------------------------------------------------------

INSERT INTO
  `exam_results` (
    `marks_obtained`,
    `max_marks`,
    `created_at`,
    `exam_id`,
    `id`,
    `recorded_by_id`,
    `student_id`,
    `subject_id`,
    `updated_at`,
    `remarks`
  )
VALUES
  (
    15,
    20,
    '2026-08-09 17:04:08.103354',
    1,
    1,
    2,
    131,
    1,
    '2026-08-09 17:04:08.103354',
    NULL
  );
INSERT INTO
  `exam_results` (
    `marks_obtained`,
    `max_marks`,
    `created_at`,
    `exam_id`,
    `id`,
    `recorded_by_id`,
    `student_id`,
    `subject_id`,
    `updated_at`,
    `remarks`
  )
VALUES
  (
    17,
    20,
    '2026-08-09 17:04:08.103354',
    1,
    2,
    2,
    132,
    1,
    '2026-08-09 17:04:08.103354',
    NULL
  );
INSERT INTO
  `exam_results` (
    `marks_obtained`,
    `max_marks`,
    `created_at`,
    `exam_id`,
    `id`,
    `recorded_by_id`,
    `student_id`,
    `subject_id`,
    `updated_at`,
    `remarks`
  )
VALUES
  (
    11,
    20,
    '2026-08-09 17:04:08.103354',
    1,
    3,
    2,
    133,
    1,
    '2026-08-09 17:04:08.103354',
    NULL
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: exams
# ------------------------------------------------------------

INSERT INTO
  `exams` (
    `academic_year_id`,
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `name`
  )
VALUES
  (
    1,
    '2026-08-08 20:28:09.325559',
    1,
    1,
    '2026-08-08 20:28:09.325559',
    'PT-1'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: notices
# ------------------------------------------------------------


# ------------------------------------------------------------
# DATA DUMP FOR TABLE: ocr_results
# ------------------------------------------------------------

INSERT INTO
  `ocr_results` (
    `created_at`,
    `document_upload_id`,
    `id`,
    `updated_at`,
    `extracted_data_json`,
    `raw_text`
  )
VALUES
  (
    '2026-08-09 18:33:40.631739',
    63,
    52,
    '2026-08-09 18:33:40.631739',
    '{}',
    'Thiaisa test JPES decumentor OCR,'
  );
INSERT INTO
  `ocr_results` (
    `created_at`,
    `document_upload_id`,
    `id`,
    `updated_at`,
    `extracted_data_json`,
    `raw_text`
  )
VALUES
  (
    '2026-08-09 18:33:41.261264',
    64,
    53,
    '2026-08-09 18:33:41.261264',
    '{}',
    'This isa test PDF document for OCR'
  );
INSERT INTO
  `ocr_results` (
    `created_at`,
    `document_upload_id`,
    `id`,
    `updated_at`,
    `extracted_data_json`,
    `raw_text`
  )
VALUES
  (
    '2026-08-09 18:33:41.737720',
    65,
    54,
    '2026-08-09 18:33:41.737720',
    '{}',
    'This isa test PNG document for OCR'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: parent_student_link_requests
# ------------------------------------------------------------


# ------------------------------------------------------------
# DATA DUMP FOR TABLE: parents
# ------------------------------------------------------------

INSERT INTO
  `parents` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `address`,
    `alternate_contact`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-09 11:40:27.924636',
    5,
    1,
    '2026-08-09 11:40:27.924636',
    2,
    '456 Parent St.',
    NULL,
    'Bob',
    'Parent',
    '123-456-7890'
  );
INSERT INTO
  `parents` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `address`,
    `alternate_contact`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-09 12:25:29.429981',
    46,
    12,
    '2026-08-09 12:25:29.429981',
    10,
    NULL,
    NULL,
    'Test',
    'Parent',
    NULL
  );
INSERT INTO
  `parents` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `address`,
    `alternate_contact`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-09 12:25:29.685714',
    47,
    13,
    '2026-08-09 12:25:29.685714',
    11,
    NULL,
    NULL,
    'Test',
    'Parent',
    NULL
  );
INSERT INTO
  `parents` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `address`,
    `alternate_contact`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-09 12:25:29.771549',
    48,
    14,
    '2026-08-09 12:25:29.771549',
    12,
    NULL,
    NULL,
    'Test',
    'Parent',
    NULL
  );
INSERT INTO
  `parents` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `address`,
    `alternate_contact`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-09 12:25:29.903820',
    49,
    15,
    '2026-08-09 12:25:29.903820',
    13,
    NULL,
    NULL,
    'Test',
    'Parent',
    NULL
  );
INSERT INTO
  `parents` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `address`,
    `alternate_contact`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-09 13:21:56.864421',
    55,
    1,
    '2026-08-09 13:21:56.864421',
    24,
    NULL,
    NULL,
    'P1',
    'One',
    NULL
  );
INSERT INTO
  `parents` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `address`,
    `alternate_contact`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-09 13:21:56.880341',
    56,
    1,
    '2026-08-09 13:21:56.880341',
    25,
    NULL,
    NULL,
    'P2',
    'Two',
    NULL
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: roles
# ------------------------------------------------------------

INSERT INTO
  `roles` (
    `created_at`,
    `id`,
    `updated_at`,
    `name`,
    `description`
  )
VALUES
  (
    '2026-08-08 20:28:08.471885',
    1,
    '2026-08-08 20:28:08.471885',
    'ADMIN',
    'School Administrator with full platform access'
  );
INSERT INTO
  `roles` (
    `created_at`,
    `id`,
    `updated_at`,
    `name`,
    `description`
  )
VALUES
  (
    '2026-08-08 20:28:08.560408',
    2,
    '2026-08-08 20:28:08.560408',
    'TEACHER',
    'Teacher with access to assigned classes and subjects'
  );
INSERT INTO
  `roles` (
    `created_at`,
    `id`,
    `updated_at`,
    `name`,
    `description`
  )
VALUES
  (
    '2026-08-08 20:28:08.573995',
    3,
    '2026-08-08 20:28:08.573995',
    'PARENT',
    'Parent with read-only access to student information'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: school_classes
# ------------------------------------------------------------

INSERT INTO
  `school_classes` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `level`,
    `name`
  )
VALUES
  (
    '2026-08-08 20:28:09.248786',
    1,
    1,
    '2026-08-08 20:28:09.248786',
    NULL,
    'Grade 10'
  );
INSERT INTO
  `school_classes` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `level`,
    `name`
  )
VALUES
  (
    '2026-08-09 13:21:56.698959',
    2,
    1,
    '2026-08-09 13:21:56.698959',
    NULL,
    '10'
  );
INSERT INTO
  `school_classes` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `level`,
    `name`
  )
VALUES
  (
    '2026-08-09 13:21:56.721489',
    3,
    1,
    '2026-08-09 13:21:56.721489',
    NULL,
    '9'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: schools
# ------------------------------------------------------------

INSERT INTO
  `schools` (
    `created_at`,
    `id`,
    `updated_at`,
    `address`,
    `contact_email`,
    `name`
  )
VALUES
  (
    '2026-08-08 20:28:09.127229',
    1,
    '2026-08-08 20:28:09.127229',
    '123 Education Lane, Learning City',
    'admin@vertoedu.com',
    'VertoEdu Default School'
  );
INSERT INTO
  `schools` (
    `created_at`,
    `id`,
    `updated_at`,
    `address`,
    `contact_email`,
    `name`
  )
VALUES
  (
    '2026-08-09 11:36:08.872469',
    3,
    '2026-08-09 11:36:08.872469',
    NULL,
    NULL,
    'Security Test School'
  );
INSERT INTO
  `schools` (
    `created_at`,
    `id`,
    `updated_at`,
    `address`,
    `contact_email`,
    `name`
  )
VALUES
  (
    '2026-08-09 12:24:02.539698',
    9,
    '2026-08-09 12:24:02.539698',
    NULL,
    NULL,
    'Integrity Test School 288a0097-8b71-4f09-b1e9-26ae2568f841'
  );
INSERT INTO
  `schools` (
    `created_at`,
    `id`,
    `updated_at`,
    `address`,
    `contact_email`,
    `name`
  )
VALUES
  (
    '2026-08-09 12:24:02.782463',
    10,
    '2026-08-09 12:24:02.782463',
    NULL,
    NULL,
    'Integrity Test School fe3271ec-f995-4488-bed4-578cfc773e4a'
  );
INSERT INTO
  `schools` (
    `created_at`,
    `id`,
    `updated_at`,
    `address`,
    `contact_email`,
    `name`
  )
VALUES
  (
    '2026-08-09 12:24:02.840087',
    11,
    '2026-08-09 12:24:02.840087',
    NULL,
    NULL,
    'Integrity Test School 3ddbe4cb-59f3-4d41-9f0c-6bc7d8520d18'
  );
INSERT INTO
  `schools` (
    `created_at`,
    `id`,
    `updated_at`,
    `address`,
    `contact_email`,
    `name`
  )
VALUES
  (
    '2026-08-09 12:25:29.254294',
    12,
    '2026-08-09 12:25:29.254294',
    NULL,
    NULL,
    'Integrity Test School c79d7aa3-0202-473a-a711-2af298bd1aed'
  );
INSERT INTO
  `schools` (
    `created_at`,
    `id`,
    `updated_at`,
    `address`,
    `contact_email`,
    `name`
  )
VALUES
  (
    '2026-08-09 12:25:29.649687',
    13,
    '2026-08-09 12:25:29.649687',
    NULL,
    NULL,
    'Integrity Test School 1ebe54ea-3e82-432e-a5fd-792e5a0da600'
  );
INSERT INTO
  `schools` (
    `created_at`,
    `id`,
    `updated_at`,
    `address`,
    `contact_email`,
    `name`
  )
VALUES
  (
    '2026-08-09 12:25:29.744120',
    14,
    '2026-08-09 12:25:29.744120',
    NULL,
    NULL,
    'Integrity Test School b07f7d21-03d8-4c4a-86a0-ed128be29c99'
  );
INSERT INTO
  `schools` (
    `created_at`,
    `id`,
    `updated_at`,
    `address`,
    `contact_email`,
    `name`
  )
VALUES
  (
    '2026-08-09 12:25:29.865795',
    15,
    '2026-08-09 12:25:29.865795',
    NULL,
    NULL,
    'Integrity Test School 1b2880e5-589e-487c-8a61-5eb03f4a5f97'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: sections
# ------------------------------------------------------------

INSERT INTO
  `sections` (
    `created_at`,
    `id`,
    `school_class_id`,
    `updated_at`,
    `name`
  )
VALUES
  (
    '2026-08-08 20:28:09.272013',
    1,
    1,
    '2026-08-08 20:28:09.272013',
    'A'
  );
INSERT INTO
  `sections` (
    `created_at`,
    `id`,
    `school_class_id`,
    `updated_at`,
    `name`
  )
VALUES
  (
    '2026-08-09 13:21:56.738196',
    2,
    2,
    '2026-08-09 13:21:56.738196',
    'A'
  );
INSERT INTO
  `sections` (
    `created_at`,
    `id`,
    `school_class_id`,
    `updated_at`,
    `name`
  )
VALUES
  (
    '2026-08-09 13:21:56.752179',
    3,
    2,
    '2026-08-09 13:21:56.752179',
    'B'
  );
INSERT INTO
  `sections` (
    `created_at`,
    `id`,
    `school_class_id`,
    `updated_at`,
    `name`
  )
VALUES
  (
    '2026-08-09 13:21:56.763896',
    4,
    3,
    '2026-08-09 13:21:56.763896',
    'A'
  );
INSERT INTO
  `sections` (
    `created_at`,
    `id`,
    `school_class_id`,
    `updated_at`,
    `name`
  )
VALUES
  (
    '2026-08-09 13:21:56.774035',
    5,
    3,
    '2026-08-09 13:21:56.774035',
    'B'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: student_history
# ------------------------------------------------------------


# ------------------------------------------------------------
# DATA DUMP FOR TABLE: students
# ------------------------------------------------------------

INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.549226',
    131,
    55,
    1,
    2,
    '2026-08-09 18:29:16.007440',
    'Aarav',
    'Sharma',
    'TEST-SCH-10001'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.586536',
    132,
    55,
    1,
    2,
    '2026-08-09 18:29:16.023916',
    'Vivaan',
    'Patel',
    'TEST-SCH-10002'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.594707',
    133,
    55,
    1,
    2,
    '2026-08-09 18:29:16.035933',
    'Aditya',
    'Verma',
    'TEST-SCH-10003'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.602690',
    134,
    56,
    1,
    3,
    '2026-08-09 18:29:16.045014',
    'Arjun',
    'Mehta',
    'TEST-SCH-10004'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.611174',
    135,
    56,
    1,
    3,
    '2026-08-09 18:29:16.064676',
    'Rohan',
    'Gupta',
    'TEST-SCH-10005'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.620452',
    136,
    NULL,
    1,
    3,
    '2026-08-09 18:29:16.078594',
    'Kabir',
    'Joshi',
    'TEST-SCH-10006'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.629151',
    137,
    NULL,
    1,
    4,
    '2026-08-09 18:29:16.093848',
    'Ishaan',
    'Singh',
    'TEST-SCH-10007'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.638617',
    138,
    NULL,
    1,
    4,
    '2026-08-09 18:29:16.110911',
    'Reyansh',
    'Shah',
    'TEST-SCH-10008'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.646875',
    139,
    NULL,
    1,
    5,
    '2026-08-09 18:29:16.126234',
    'Atharv',
    'Jain',
    'TEST-SCH-10009'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.656759',
    140,
    NULL,
    1,
    5,
    '2026-08-09 18:29:16.140689',
    'Vihaan',
    'Desai',
    'TEST-SCH-10010'
  );
INSERT INTO
  `students` (
    `dob`,
    `created_at`,
    `id`,
    `parent_id`,
    `school_id`,
    `section_id`,
    `updated_at`,
    `first_name`,
    `last_name`,
    `scholar_number`
  )
VALUES
  (
    NULL,
    '2026-08-09 16:53:30.664247',
    141,
    NULL,
    1,
    5,
    '2026-08-09 18:29:16.158222',
    'Dhruv',
    'Kulkarni',
    'TEST-SCH-10011'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: subjects
# ------------------------------------------------------------

INSERT INTO
  `subjects` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `code`,
    `description`,
    `name`
  )
VALUES
  (
    '2026-08-08 20:28:09.290211',
    1,
    1,
    '2026-08-08 20:28:09.290211',
    'MATH101',
    NULL,
    'Mathematics'
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: teacher_sections
# ------------------------------------------------------------

INSERT INTO
  `teacher_sections` (`section_id`, `teacher_id`)
VALUES
  (1, 1);
INSERT INTO
  `teacher_sections` (`section_id`, `teacher_id`)
VALUES
  (2, 2);
INSERT INTO
  `teacher_sections` (`section_id`, `teacher_id`)
VALUES
  (5, 2);
INSERT INTO
  `teacher_sections` (`section_id`, `teacher_id`)
VALUES
  (3, 3);
INSERT INTO
  `teacher_sections` (`section_id`, `teacher_id`)
VALUES
  (4, 3);

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: teacher_subjects
# ------------------------------------------------------------

INSERT INTO
  `teacher_subjects` (`subject_id`, `teacher_id`)
VALUES
  (1, 1);
INSERT INTO
  `teacher_subjects` (`subject_id`, `teacher_id`)
VALUES
  (1, 2);
INSERT INTO
  `teacher_subjects` (`subject_id`, `teacher_id`)
VALUES
  (1, 3);

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: teachers
# ------------------------------------------------------------

INSERT INTO
  `teachers` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `employee_id`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-08 20:28:09.389344',
    1,
    1,
    '2026-08-08 20:28:09.389344',
    1,
    'EMP-T01',
    'John',
    'Teacher',
    NULL
  );
INSERT INTO
  `teachers` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `employee_id`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-09 13:21:56.791679',
    2,
    1,
    '2026-08-09 18:33:39.385672',
    22,
    NULL,
    'T1',
    'One',
    NULL
  );
INSERT INTO
  `teachers` (
    `created_at`,
    `id`,
    `school_id`,
    `updated_at`,
    `user_id`,
    `employee_id`,
    `first_name`,
    `last_name`,
    `phone`
  )
VALUES
  (
    '2026-08-09 13:21:56.852249',
    3,
    1,
    '2026-08-09 18:33:39.517588',
    23,
    NULL,
    'T2',
    'Two',
    NULL
  );

# ------------------------------------------------------------
# DATA DUMP FOR TABLE: users
# ------------------------------------------------------------

INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-08 20:28:09.362996',
    1,
    2,
    '2026-08-08 20:28:09.362996',
    NULL,
    'teacher@vertoedu.com',
    'John Teacher',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-08 20:28:09.413418',
    2,
    3,
    '2026-08-08 20:28:09.413418',
    NULL,
    'parent@vertoedu.com',
    'Bob Parent',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-08 20:41:38.447229',
    3,
    1,
    '2026-08-08 20:41:38.447229',
    NULL,
    'admin@vertoedu.com',
    'Admin User',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-08 20:47:13.663162',
    4,
    1,
    '2026-08-08 20:58:32.963579',
    'https://lh3.googleusercontent.com/a/ACg8ocJmjs0KlKwd0efl-asmJSf6fBDKWfzjVqAVKRa-JqIi4B_NiA=s96-c',
    'clashclasher1124@gmail.com',
    'Clashclasher11',
    '109187373133218865997'
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 11:37:37.244956',
    5,
    3,
    '2026-08-09 11:37:37.244956',
    NULL,
    'parent1@test.com',
    'Parent One',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 11:37:37.268361',
    6,
    3,
    '2026-08-09 11:37:37.268361',
    NULL,
    'parent2@test.com',
    'Parent Two',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 12:25:29.409598',
    10,
    3,
    '2026-08-09 12:25:29.409598',
    NULL,
    'integritya111bc4f-f9fc-4978-823f-589dc6902149@test.com',
    'Test Integrity User',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 12:25:29.674249',
    11,
    3,
    '2026-08-09 12:25:29.674249',
    NULL,
    'integrity8fd028fc-c129-4764-8e85-a8b64b1e0ced@test.com',
    'Test Integrity User',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 12:25:29.762236',
    12,
    3,
    '2026-08-09 12:25:29.762236',
    NULL,
    'integrity99d25211-ca74-4e89-883e-2afbffcd1627@test.com',
    'Test Integrity User',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 12:25:29.893383',
    13,
    3,
    '2026-08-09 12:25:29.893383',
    NULL,
    'integrity666b9270-0b32-4130-a741-e26842256e63@test.com',
    'Test Integrity User',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 13:21:56.584542',
    22,
    2,
    '2026-08-09 17:03:38.731573',
    'https://lh3.googleusercontent.com/a/ACg8ocKZBpWnOkxI1I_Z7KuHcCpjP7qitPiiuBLiWLS3fBA1MDiO4g=s96-c',
    'clashclasher1102@gmail.com',
    'Clasher',
    '101475510265258760659'
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 13:21:56.629923',
    23,
    2,
    '2026-08-09 17:11:15.308262',
    'https://lh3.googleusercontent.com/a/ACg8ocJCi7C00oYwd2TTl6ekvWrgFaM3Wl2izUQwMuhUAiOYAV87lPA=s96-c',
    'parthdeshmukh167@gmail.com',
    'Parth Deshmukh',
    '105081423276822058617'
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 13:21:56.639545',
    24,
    3,
    '2026-08-09 13:21:56.639545',
    NULL,
    'workgpt678@gmail.com',
    'Parent One',
    NULL
  );
INSERT INTO
  `users` (
    `active`,
    `created_at`,
    `id`,
    `role_id`,
    `updated_at`,
    `profile_image`,
    `email`,
    `full_name`,
    `google_id`
  )
VALUES
  (
    b'1',
    '2026-08-09 13:21:56.647613',
    25,
    3,
    '2026-08-09 17:15:07.679692',
    'https://lh3.googleusercontent.com/a/ACg8ocJX9fi9fz9Qx8AbashQk39PGNz9Z2VK_UH44ir89uwyUK00Bg=s96-c',
    'regaltashwal@gmail.com',
    'Regal',
    '109485756401055520083'
  );

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
