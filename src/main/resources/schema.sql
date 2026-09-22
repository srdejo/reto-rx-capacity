CREATE TABLE IF NOT EXISTS capacity (
    capacity_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    description VARCHAR(90)
);

CREATE TABLE IF NOT EXISTS bootcamp_capacity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    capacity_id BIGINT,
    bootcamp_id BIGINT
)
