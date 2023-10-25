CREATE TABLE public.user
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    phone      VARCHAR(25)  NOT NULL,

    created_at timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp    NULL
);

CREATE TABLE public.user_address
(
    id             SERIAL PRIMARY KEY,
    user_id        INT          NOT NULL REFERENCES public.user (id),
    street_address VARCHAR(100) NOT NULL,
    city           VARCHAR(100) NOT NULL,
    state          VARCHAR(100) NOT NULL,
    postal_index   VARCHAR(25)  NOT NULL,
    country        VARCHAR(100) NOT NULL,
    phone          VARCHAR(25)  NOT NULL,
    mobile         VARCHAR(25)  NOT NULL,

    created_at     timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at     timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at     timestamp    NULL
);

CREATE TABLE public.payment_provider
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(255) NOT NULL,

    created_at  timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at  timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at  timestamp    NULL
);

CREATE TABLE public.user_payment
(
    id           SERIAL PRIMARY KEY,
    user_id      INT         NOT NULL REFERENCES public.user (id),
    provider_id  INT         NOT NULL REFERENCES public.payment_provider (id),
    payment_type VARCHAR(50) NOT NULL,
    account_no   VARCHAR(50) NOT NULL,
    expiry_date  DATE,

    created_at   timestamp   NOT NULL DEFAULT current_timestamp,
    updated_at   timestamp   NOT NULL DEFAULT current_timestamp,
    deleted_at   timestamp   NULL
);

CREATE TABLE public.product_provider_type
(
    name       VARCHAR(50) PRIMARY KEY,

    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL,

    CONSTRAINT check_provider_type_in_supported_values CHECK (name in ('RESTAURANT', 'STORE'))
);

CREATE TABLE public.product_provider
(
    id                    SERIAL PRIMARY KEY,
    product_provider_type VARCHAR(50)  NOT NULL REFERENCES public.product_provider_type (name),
    name                  VARCHAR(50)  NOT NULL,
    description           VARCHAR(255),
    address               VARCHAR(50)  NOT NULL,
    phone                 VARCHAR(25)  NOT NULL,
    email                 VARCHAR(255) NOT NULL,
    website               VARCHAR(255),
    image                 VARCHAR(255),

    created_at            timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at            timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at            timestamp    NULL
);

CREATE TABLE public.product_type
(
    name       VARCHAR(50) PRIMARY KEY,

    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL,

    CONSTRAINT check_product_type_in_supported_values CHECK (name in ('FOOD', 'CLOTHES'))
);

CREATE TABLE public.product
(
    id                  SERIAL PRIMARY KEY NOT NULL,
    product_type_name   VARCHAR(50)        NOT NULL REFERENCES public.product_type (name),
    product_provider_id INT                NOT NULL REFERENCES public.product_provider (id),
    name                VARCHAR(50)        NOT NULL,
    description         VARCHAR(1000),
    image               VARCHAR(300),
    price               DECIMAL(19, 4)     NOT NULL,
    pickup_time         timestamp          NOT NULL,

    created_at          timestamp          NOT NULL DEFAULT current_timestamp,
    updated_at          timestamp          NOT NULL DEFAULT current_timestamp,
    deleted_at          timestamp          NULL,

    CONSTRAINT constraint_id_and_product_type_name UNIQUE (id, product_type_name)
);

CREATE TABLE public.discount
(
    id               SERIAL PRIMARY KEY,
    product_id       INT          NOT NULL REFERENCES public.product (id),
    name             VARCHAR(50)  NOT NULL,
    discount_percent DECIMAL      NOT NULL,
    description      VARCHAR(255) NOT NULL,
    active           BOOLEAN      NOT NULL,

    created_at       timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at       timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at       timestamp    NULL
);

CREATE TABLE public.user_favorite_providers
(
    id                  SERIAL PRIMARY KEY,
    user_id             INT       NOT NULL REFERENCES public.user (id),
    product_provider_id INT       NOT NULL REFERENCES public.product_provider (id),

    created_at          timestamp NOT NULL DEFAULT current_timestamp,
    updated_at          timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at          timestamp NULL
);

CREATE TABLE public.shopping_session
(
    id         SERIAL PRIMARY KEY,
    user_id    INT            NOT NULL REFERENCES public.user (id),
    total_cost DECIMAL(19, 4) NOT NULL,
    status     VARCHAR(25)    NOT NULL,

    created_at timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp      NULL
);

CREATE TABLE public.cart_item
(
    id                  SERIAL PRIMARY KEY,
    shopping_session_id INT       NOT NULL REFERENCES public.shopping_session (id),
    product_id          INT       NOT NULL REFERENCES public.product (id),
    quantity            INT       NOT NULL,

    created_at          timestamp NOT NULL DEFAULT current_timestamp,
    updated_at          timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at          timestamp NULL
);

CREATE TABLE public.order_details
(
    id         SERIAL PRIMARY KEY,
    user_id    INT            NOT NULL REFERENCES public.user (id),
    total_cost DECIMAL(19, 4) NOT NULL,

    created_at timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp      NULL
);

CREATE TABLE public.order_items
(
    id         SERIAL PRIMARY KEY,
    order_id   INT       NOT NULL REFERENCES public.order_details (id),
    product_id INT       NOT NULL REFERENCES public.product (id),
    quantity   INT       NOT NULL,

    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL
);

CREATE TABLE public.payment_details
(
    id               UUID PRIMARY KEY,
    amount           DECIMAL(19, 4) NOT NULL,
    status           VARCHAR(25)    NOT NULL,
    provider_id      INT            NOT NULL REFERENCES public.payment_provider (id),
    order_details_id INT            NOT NULL REFERENCES public.order_details (id),

    created_at       timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at       timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at       timestamp      NULL
);

CREATE TABLE public.food_category
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50),

    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL
);

CREATE TABLE public.food
(
    id                   SERIAL PRIMARY KEY,
    product_id           INT           NOT NULL REFERENCES public.product (id) UNIQUE,
    product_type_name    VARCHAR(10)   NOT NULL REFERENCES public.product_type (name) DEFAULT 'FOOD',
    dietary_restrictions VARCHAR(1000) NOT NULL,

    created_at           timestamp     NOT NULL                                       DEFAULT current_timestamp,
    updated_at           timestamp     NOT NULL                                       DEFAULT current_timestamp,
    deleted_at           timestamp     NULL,

    CONSTRAINT check_product_type_is_food_value CHECK (product_type_name = 'FOOD'),
    CONSTRAINT fk_food_product FOREIGN KEY (product_id, product_type_name)
        REFERENCES public.product (id, product_type_name)
);

CREATE TABLE public.food_food_category
(
    food_id          INT       NOT NULL REFERENCES public.food (id),
    food_category_id INT       NOT NULL REFERENCES public.food_category (id),
    PRIMARY KEY (food_id, food_category_id),

    created_at       timestamp NOT NULL DEFAULT current_timestamp,
    updated_at       timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at       timestamp NULL
);

CREATE TABLE public.clothes_category
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(25),

    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL
);

CREATE TABLE public.clothes
(
    id                SERIAL PRIMARY KEY,
    product_id        INT          NOT NULL REFERENCES public.product (id) UNIQUE,
    product_type_name VARCHAR(25)  NOT NULL REFERENCES public.product_type (name) DEFAULT 'CLOTHES',
    size              VARCHAR(255) NOT NULL,
    color             VARCHAR(255) NOT NULL,

    created_at        timestamp    NOT NULL                                       DEFAULT current_timestamp,
    updated_at        timestamp    NOT NULL                                       DEFAULT current_timestamp,
    deleted_at        timestamp    NULL,

    CONSTRAINT check_product_type_is_clothes_value CHECK (product_type_name = 'CLOTHES'),
    CONSTRAINT fk_clothes_product FOREIGN KEY (product_id, product_type_name)
        REFERENCES public.product (id, product_type_name)
);

CREATE TABLE public.clothes_clothes_category
(
    clothes_id          INT       NOT NULL REFERENCES public.clothes (id),
    clothes_category_id INT       NOT NULL REFERENCES public.clothes_category (id),
    PRIMARY KEY (clothes_id, clothes_category_id),

    created_at          timestamp NOT NULL DEFAULT current_timestamp,
    updated_at          timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at          timestamp NULL
);