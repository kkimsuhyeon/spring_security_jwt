DROP TABLE IF EXISTS member;

CREATE TABLE member
(
    member_id IDENTITY PRIMARY KEY,
    id            VARCHAR(20)  NOT NULL,
    name          VARCHAR(20)  NOT NULL,
    password      VARCHAR(20)  NOT NULL,
    role          VARCHAR(100) NOT NULL,
    access_token  VARCHAR(255),
    refresh_token VARCHAR(255)
)