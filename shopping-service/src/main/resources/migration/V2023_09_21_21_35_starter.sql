CREATE TABLE public.user
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    username     VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255) NOT NULL,

    created_at   timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at   timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at   timestamp    NULL
);

CREATE TABLE public.user_address
(
    id             SERIAL PRIMARY KEY,
    user_id        INT          NOT NULL REFERENCES public.user (id),
    street_address VARCHAR(255) NOT NULL,
    city           VARCHAR(255) NOT NULL,
    state          VARCHAR(255) NOT NULL,
    postal_index   VARCHAR(255) NOT NULL,
    country        VARCHAR(255) NOT NULL,
    phone_number   VARCHAR(255) NOT NULL,
    mobile         VARCHAR(255) NOT NULL,

    created_at     timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at     timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at     timestamp    NULL
);

CREATE TABLE public.payment_provider
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,

    created_at  timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at  timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at  timestamp    NULL
);

CREATE TABLE public.user_payment
(
    id           SERIAL PRIMARY KEY,
    user_id      INT          NOT NULL REFERENCES public.user (id),
    provider_id  INT          NOT NULL REFERENCES public.payment_provider (id),
    payment_type VARCHAR(255) NOT NULL,
    account_no   VARCHAR(255) NOT NULL,
    expiry_date  DATE,

    created_at   timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at   timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at   timestamp    NULL
);

CREATE TABLE public.product_provider_type
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,

    created_at timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp    NULL
);

CREATE TABLE public.product_provider
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    product_provider_type_id INT          NOT NULL REFERENCES public.product_provider_type (id),
    description      VARCHAR(255),
    address          VARCHAR(255) NOT NULL,
    phone            VARCHAR(255) NOT NULL,
    email            VARCHAR(255) NOT NULL,
    website          VARCHAR(255),
    image            VARCHAR(255),

    created_at       timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at       timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at       timestamp    NULL
);

CREATE TABLE public.product_category
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,

    created_at timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp    NULL
);

CREATE TABLE public.product
(
    id                  SERIAL PRIMARY KEY,
    product_category_id INT            NOT NULL REFERENCES public.product_category (id),
    product_provider_id INT            NOT NULL REFERENCES public.product_provider (id),
    price               DECIMAL(19, 4) NOT NULL,
    pickup_time         DATE           NOT NULL,

    created_at          timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at          timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at          timestamp      NULL
);

CREATE TABLE public.discount
(
    id               SERIAL PRIMARY KEY,
    product_id       INT          NOT NULL REFERENCES public.product (id),
    name             VARCHAR(255) NOT NULL,
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
    status     VARCHAR(255)   NOT NULL,

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
    status           VARCHAR(255)   NOT NULL,
    provider_id      INT            NOT NULL REFERENCES public.payment_provider (id),
    order_details_id INT            NOT NULL REFERENCES public.order_details (id),

    created_at       timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at       timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at       timestamp      NULL
);

CREATE TABLE public.food_category
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,

    created_at timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp    NULL
);

CREATE TABLE public.food
(
    id                   SERIAL PRIMARY KEY,
    product_id           INT           NOT NULL REFERENCES public.product (id) UNIQUE,
    name                 VARCHAR(255)  NOT NULL,
    description          VARCHAR(1000),
    image                VARCHAR(255),
    dietary_restrictions VARCHAR(1000) NOT NULL,

    created_at           timestamp     NOT NULL DEFAULT current_timestamp,
    updated_at           timestamp     NOT NULL DEFAULT current_timestamp,
    deleted_at           timestamp     NULL
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
    name       VARCHAR(255) NOT NULL,

    created_at timestamp    NOT NULL DEFAULT current_timestamp,
    updated_at timestamp    NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp    NULL
);

CREATE TABLE public.clothes
(
    id          SERIAL PRIMARY KEY,
    product_id  INT           NOT NULL REFERENCES public.product (id) UNIQUE,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(1000),
    image       VARCHAR(255),
    size        VARCHAR(255)  NOT NULL,
    color       VARCHAR(255)  NOT NULL,

    created_at  timestamp     NOT NULL DEFAULT current_timestamp,
    updated_at  timestamp     NOT NULL DEFAULT current_timestamp,
    deleted_at  timestamp     NULL
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