CREATE TABLE user
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    username     VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255) NOT NULL UNIQUE,

    created_at   timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at   timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at   timestamp NULL
);

CREATE TABLE user_address
(
    id             SERIAL PRIMARY KEY,
    user_id        INT          NOT NULL REFERENCES user (id),
    street_address VARCHAR(255) NOT NULL,
    city           VARCHAR(255) NOT NULL,
    state          VARCHAR(255) NOT NULL,
    postal_index   VARCHAR(255) NOT NULL,
    country        VARCHAR(255) NOT NULL,
    phone_number   VARCHAR(255) NOT NULL,
    mobile         VARCHAR(255) NOT NULL,

    created_at     timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at     timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at     timestamp NULL
);

CREATE TABLE user_payment
(
    id           SERIAL PRIMARY KEY,
    user_id      INT          NOT NULL REFERENCES user (id),
    provider_id  INT          NOT NULL REFERENCES payment_provider (id),
    payment_type VARCHAR(255) NOT NULL,
    account_no   VARCHAR(255) NOT NULL,
    expiry_date  DATE         NOT NULL,

    created_at   timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at   timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at   timestamp NULL
);

CREATE TABLE payment_provider
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,

    created_at  timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at  timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at  timestamp NULL
);

CREATE TABLE user_wishlist
(
    id         SERIAL PRIMARY KEY,
    user_id    INT       NOT NULL REFERENCES user (id),

    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL
);

CREATE TABLE user_wishlist_item
(
    id               SERIAL PRIMARY KEY,
    user_wishlist_id INT       NOT NULL REFERENCES user_wishlist (id),
    product_id       INT       NOT NULL REFERENCES product (id),

    created_at       timestamp NOT NULL DEFAULT current_timestamp,
    updated_at       timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at       timestamp NULL
);

CREATE TABLE product
(
    id                  SERIAL PRIMARY KEY,
    product_provider_id INT            NOT NULL REFERENCES product_provider (id),
    inventory_id        INT            NOT NULL REFERENCES inventory (id),
    name                VARCHAR(255)   NOT NULL,
    description         VARCHAR(1000)  NOT NULL,
    price               DECIMAL(19, 4) NOT NULL,
    discount            INT            NOT NULL REFERENCES discount (id),
    image               VARCHAR(255)   NOT NULL,
    pickup_time         DATE           NOT NULL,

    created_at          timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at          timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at          timestamp NULL
);

CREATE TABLE inventory
(
    id         SERIAL PRIMARY KEY,
    quantity   INT       NOT NULL,

    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL
);

CREATE TABLE product_provider
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    address     VARCHAR(255) NOT NULL,
    phone       VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    website     VARCHAR(255) NOT NULL,
    image       VARCHAR(255) NOT NULL,

    created_at  timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at  timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at  timestamp NULL
);

CREATE TABLE product_category
(
    id          SERIAL PRIMARY KEY,
    parent_id   INT REFERENCES product_category (id),
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,

    created_at  timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at  timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at  timestamp NULL
);

CREATE TABLE discount
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    discount_percent DECIMAL      NOT NULL,
    description      VARCHAR(255) NOT NULL,
    active           BOOLEAN      NOT NULL,

    created_at       timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at       timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at       timestamp NULL
);

CREATE TABLE shopping_session
(
    id         SERIAL PRIMARY KEY,
    user_id    INT            NOT NULL REFERENCES user (id),
    total_cost DECIMAL(19, 4) NOT NULL,
    status     VARCHAR(255)   NOT NULL,

    created_at timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL
);

CREATE TABLE cart_item
(
    id                  SERIAL PRIMARY KEY,
    shopping_session_id INT       NOT NULL REFERENCES shopping_session (id),
    product_id          INT       NOT NULL REFERENCES product (id),
    quantity            INT       NOT NULL,

    created_at          timestamp NOT NULL DEFAULT current_timestamp,
    updated_at          timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at          timestamp NULL
);

CREATE TABLE order_items
(
    id         SERIAL PRIMARY KEY,
    order_id   INT       NOT NULL REFERENCES order_details (id),
    product_id INT       NOT NULL REFERENCES product (id),
    quantity   INT       NOT NULL,

    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL
);

CREATE TABLE order_details
(
    id         SERIAL PRIMARY KEY,
    user_id    INT            NOT NULL REFERENCES user (id),
    total_cost DECIMAL(19, 4) NOT NULL,
    payment_id UUID           NOT NULL REFERENCES payment_details (id),

    created_at timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL
);

CREATE TABLE payment_details
(
    id          UUID PRIMARY KEY,
    amount      DECIMAL(19, 4) NOT NULL,
    status      VARCHAR(255)   NOT NULL,
    provider_id INT            NOT NULL REFERENCES payment_provider (id),

    created_at  timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at  timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at  timestamp NULL
);

CREATE TABLE food
(
    id                   SERIAL PRIMARY KEY,
    product_id           INT           NOT NULL REFERENCES product (id),
    food_category_id     INT           NOT NULL REFERENCES food_category (id),
    dietary_restrictions VARCHAR(1000) NOT NULL,

    created_at           timestamp     NOT NULL DEFAULT current_timestamp,
    updated_at           timestamp     NOT NULL DEFAULT current_timestamp,
    deleted_at           timestamp NULL
);

CREATE TABLE food_category
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,

    created_at  timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at  timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at  timestamp NULL
);

CREATE TABLE clothes
(
    id                  SERIAL PRIMARY KEY,
    product_id          INT          NOT NULL REFERENCES product (id),
    clothes_category_id INT          NOT NULL REFERENCES clothes_category (id),
    size                VARCHAR(255) NOT NULL,
    color               VARCHAR(255) NOT NULL,

    created_at          timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at          timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at          timestamp NULL
);

CREATE TABLE clothes_category
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,

    created_at  timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at  timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at  timestamp NULL
);