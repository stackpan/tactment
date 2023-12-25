CREATE DATABASE IF NOT EXISTS tactment;

USE tactment;

CREATE TABLE users
(
    username         VARCHAR(100) NOT NULL PRIMARY KEY,
    password         VARCHAR(100) NOT NULL,
    name             VARCHAR(100) NOT NULL,
    token            VARCHAR(100),
    token_expired_at BIGINT,
    UNIQUE (token)
) ENGINE InnoDB;

CREATE TABLE contacts
(
    id         VARCHAR(100) NOT NULL PRIMARY KEY,
    username   VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100),
    phone      VARCHAR(100),
    email      VARCHAR(100),
    FOREIGN KEY fk_contacts_users (username) REFERENCES users (username)
) ENGINE InnoDB;

CREATE TABLE addresses
(
    id          VARCHAR(100) NOT NULL PRIMARY KEY,
    contact_id  VARCHAR(100) NOT NULL,
    street      VARCHAR(200),
    city        VARCHAR(100),
    province    VARCHAR(100),
    country     VARCHAR(100) NOT NULL,
    postal_code VARCHAR(10),
    FOREIGN KEY fk_addresses_contacts (contact_id) REFERENCES contacts (id)
) ENGINE InnoDB;