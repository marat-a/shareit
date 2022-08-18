DROP TABLE IF EXISTS users, items, item_requests cascade ;
CREATE TABLE IF NOT EXISTS users (
                                     user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     email VARCHAR(512) NOT NULL,
                                     CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
                                     item_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     owner INTEGER NOT NULL,
                                     name  VARCHAR(255) NOT NULL,
                                     description TEXT,
                                     available    BOOLEAN,
                                     item_request INTEGER,
                                     constraint ITEMS_USERS_USER_ID_FK
                                         foreign key (owner) references USERS (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS item_requests (
                                     request_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     description TEXT,
                                     requestor INTEGER,
                                     created date,
                                     constraint ITEMS_REQUEST_USERS_USER_ID_FK
                                         foreign key (requestor) references USERS (user_id) ON DELETE CASCADE
);