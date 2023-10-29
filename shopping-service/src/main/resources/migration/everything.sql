-- drop public schema
DROP SCHEMA IF EXISTS public CASCADE;

-- create public schema
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
GRANT ALL ON SCHEMA public TO devuser;


-- create tables
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

/*
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
 */

CREATE TABLE public.product_provider_type
(
    name       VARCHAR(50) PRIMARY KEY,

    created_at timestamp NOT NULL DEFAULT current_timestamp,
    updated_at timestamp NOT NULL DEFAULT current_timestamp,
    deleted_at timestamp NULL,

    CONSTRAINT check_provider_type_in_supported_values CHECK (name in ('RESTAURANT', 'STORE', 'CAFE'))
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
    id                  SERIAL PRIMARY KEY,
    product_type_name   VARCHAR(50)    NOT NULL REFERENCES public.product_type (name),
    product_provider_id INT            NOT NULL REFERENCES public.product_provider (id),
    name                VARCHAR(50)    NOT NULL,
    description         VARCHAR(1000),
    image               VARCHAR(300),
    price               DECIMAL(19, 4) NOT NULL,
    pickup_time         timestamp      NOT NULL,

    created_at          timestamp      NOT NULL DEFAULT current_timestamp,
    updated_at          timestamp      NOT NULL DEFAULT current_timestamp,
    deleted_at          timestamp      NULL,

    CONSTRAINT constraint_id_and_product_type_name UNIQUE (id, product_type_name)
);

/*
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
 */

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

    vegetarian           BOOLEAN       NOT NULL DEFAULT false,
    vegan                BOOLEAN       NOT NULL DEFAULT false,
    gluten_free          BOOLEAN       NOT NULL DEFAULT false,
    nut_free             BOOLEAN       NOT NULL DEFAULT true,
    dairy_free           BOOLEAN       NOT NULL DEFAULT false,
    organic              BOOLEAN       NOT NULL DEFAULT false,

    created_at           timestamp     NOT NULL                                       DEFAULT current_timestamp,
    updated_at           timestamp     NOT NULL                                       DEFAULT current_timestamp,
    deleted_at           timestamp     NULL,

    CONSTRAINT check_product_type_is_food_value CHECK (product_type_name = 'FOOD'),
    CONSTRAINT fk_food_product FOREIGN KEY (product_id, product_type_name) REFERENCES public.product (id, product_type_name)
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


-- insert data
INSERT INTO public.user (first_name, last_name, username, password, email, phone)
VALUES ('John', 'Doe', 'johndoe', 'password123', 'john.doe@example.com', '+15555555555'),
       ('Jane', 'Doe', 'janedoe', 'password123', 'jane.doe@example.com', '+15555555556'),
       ('Peter', 'Parker', 'spiderman', 'password123', 'peter.parker@example.com', '+15555555557'),
       ('Michael', 'Johnson', 'michaelj', 'password789', 'michael.j@example.com', '+15555555566'),
       ('Emily', 'Smith', 'emilys', 'passwordxyz', 'emily.s@example.com', '+15555555567'),
       ('Daniel', 'Williams', 'danw', 'password456', 'daniel.w@example.com', '+15555555568');


INSERT INTO public.user_address (user_id, street_address, city, state, postal_index, country, phone, mobile)
VALUES (1, '123 Main Street', 'Anytown', 'CA', '91234', 'USA', '+15555555555', '+15555555558'),
       (2, '456 Elm Street', 'Anytown', 'CA', '91234', 'USA', '+15555555556', '+15555555559'),
       (3, '789 Oak Street', 'Anytown', 'CA', '91234', 'USA', '+15555555557', '+15555555560'),
       (4, '789 Elm Street', 'Anytown', 'NY', '98765', 'USA', '+15555555569', '+15555555570'),
       (5, '123 Oak Street', 'Anytown', 'TX', '76543', 'USA', '+15555555571', '+15555555572'),
       (6, '456 Birch Street', 'Anytown', 'FL', '54321', 'USA', '+15555555573', '+15555555574');


/*
 INSERT INTO public.payment_provider (name, description)
VALUES ('Visa',
        'Visa is a global payments technology company that provides payment processing services to businesses and consumers.'),
       ('MasterCard',
        'MasterCard is a global financial services corporation that connects consumers, businesses, financial institutions, governments and other organizations with electronic payment methods.'),
       ('PayPal',
        'PayPal is an American financial technology company operating an online payments system in the world that supports online money transfers and serves as an electronic alternative to traditional paper methods like checks and money orders.');

INSERT INTO public.user_payment (user_id, provider_id, payment_type, account_no, expiry_date)
VALUES (1, 1, 'Credit Card', '1234-5678-9012-3456', '2023-12-31'),
       (2, 2, 'Debit Card', '9876-5432-1098-7654', '2024-06-30'),
       (3, 3, 'PayPal', 'johndoe@example.com', NULL);
 */

INSERT INTO public.product_type (name)
VALUES ('FOOD'),
       ('CLOTHES');

INSERT INTO public.clothes_category (name)
VALUES ('Tops'),
       ('Bottoms'),
       ('Dresses'),
       ('Outerwear'),
       ('Shoes');

INSERT INTO public.product_provider_type (name)
VALUES ('RESTAURANT'),
       ('STORE'),
       ('CAFE');

-- dummy data for product_provider table (RESTAURANT and CAFE 1-10)
INSERT INTO public.product_provider (name, product_provider_type, description, address, phone, email, website, image)
VALUES ('McDonalds', 'RESTAURANT', 'Fast food restaurant chain', '456 Elm Street, Anytown, CA 91234', '+15555555556',
        'info@mcdonalds.com', 'https://www.mcdonalds.com/',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500'),
       ('Subway', 'RESTAURANT', 'Submarine sandwich chain', '123 Elm Street, Anytown, CA 91234', '+15555555588',
        'info@subway.com', 'https://www.subway.com/',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500'),
       ('Pizza Hut', 'RESTAURANT', 'Pizza restaurant chain', '456 Oak Street, Anytown, CA 91234', '+15555555589',
        'info@pizzahut.com', 'https://www.pizzahut.com/',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500'),
       ('Chipotle', 'RESTAURANT', 'Fast food restaurant chain', '123 Main Street, Anytown, CA 91234', '+15555555555',
        'info@chipotle.com', 'https://www.chipotle.com/',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500'),
       ('Tiki Taco', 'RESTAURANT', 'Fast food restaurant chain', '123 Main Street, Anytown, CA 91234', '+15555555555',
        'info@tikitaco.com', 'https://www.tikitaco.com/',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500'),
       ('Burger King', 'RESTAURANT', 'Fast food restaurant chain', '789 Oak Street, Anytown, CA 91234', '+15555555557',
        'info@burgerking.com', 'https://www.burgerking.com/',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500'),
       ('Starbucks', 'CAFE', 'Coffeehouse chain', '890 Maple Street, Anytown, CA 91234', '+15555555558',
        'info@starbucks.com', 'https://www.starbucks.com/',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500'),
       ('Dominos Pizza', 'RESTAURANT', 'Pizza delivery and takeout', '345 Cedar Street, Anytown, CA 91234',
        '+15555555590', 'info@dominos.com', 'https://www.dominos.com/',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500'),
       ('KFC', 'RESTAURANT', 'Fast food restaurant chain', '678 Pine Street, Anytown, CA 91234', '+15555555591',
        'info@kfc.com', 'https://www.kfc.com/', 'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500'),
       ('Panera Bread', 'CAFE', 'Bakery-cafe chain', '567 Spruce Street, Anytown, CA 91234', '+15555555592',
        'info@panerabread.com', 'https://www.panerabread.com/',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500');


-- dummy data for product_provider table (STORE 11-20)
INSERT INTO public.product_provider (name, product_provider_type, description, address, phone, email, website, image)
VALUES ('Fashion Avenue', 'STORE', 'Trendy clothing store', '789 Fashion Street, Anytown, CA 91234', '+15555555501',
        'info@fashionavenue.com', 'https://www.fashionavenue.com/', 'https://example.com/fashionavenue.jpg'),
       ('Trendy Threads', 'STORE', 'Chic clothing boutique', '123 Style Avenue, Anytown, CA 91234', '+15555555502',
        'info@trendythreads.com', 'https://www.trendythreads.com/', 'https://example.com/trendythreads.jpg'),
       ('Casual Comforts', 'STORE', 'Relaxed clothing shop', '456 Cozy Street, Anytown, CA 91234', '+15555555503',
        'info@casualcomforts.com', 'https://www.casualcomforts.com/', 'https://example.com/casualcomforts.jpg'),
       ('Shoe Paradise', 'STORE', 'Footwear specialty store', '789 Footwear Avenue, Anytown, CA 91234', '+15555555504',
        'info@shoeparadise.com', 'https://www.shoeparadise.com/', 'https://example.com/shoeparadise.jpg'),
       ('Sports Gear Store', 'STORE', 'Athletic wear and equipment shop', '123 Sporty Street, Anytown, CA 91234',
        '+15555555505', 'info@sportsgearstore.com', 'https://www.sportsgearstore.com/',
        'https://example.com/sportsgearstore.jpg'),
       ('Urban Trends', 'STORE', 'Urban fashion clothing store', '123 Streetwear Avenue, Anytown, CA 91234',
        '+15555555511', 'info@urbantrends.com', 'https://www.urbantrends.com/', 'https://example.com/urbantrends.jpg'),
       ('Chic Couture', 'STORE', 'High-end couture boutique', '456 Couture Lane, Anytown, CA 91234', '+15555555512',
        'info@chiccouture.com', 'https://www.chiccouture.com/', 'https://example.com/chiccouture.jpg'),
       ('Casual Chic', 'STORE', 'Casual and everyday fashion shop', '789 Casual Street, Anytown, CA 91234',
        '+15555555513', 'info@casualchic.com', 'https://www.casualchic.com/', 'https://example.com/casualchic.jpg'),
       ('Denim Delight', 'STORE', 'Denim and jeans specialty store', '234 Denim Avenue, Anytown, CA 91234',
        '+15555555514', 'info@denimdelight.com', 'https://www.denimdelight.com/',
        'https://example.com/denimdelight.jpg'),
       ('Trendy Tots', 'STORE', 'Childrens fashion store', '567 Kids Street, Anytown, CA 91234', '+15555555515',
        'info@trendytots.com', 'https://www.trendytots.com/', 'https://example.com/trendytots.jpg');


/*
 INSERT INTO public.discount (product_id, name, discount_percent, description, active)
VALUES (1, 'Summer Sizzle', 15, 'Get ready for summer with a 15% discount on your favorite burgers!', true),
       (2, 'Pizza Palooza', 20, 'Celebrate with a 20% discount on all pizza varieties.', true),
       (5, 'Dessert Delight', 10, 'Indulge in sweet treats with a 10% discount on desserts.', true),
       (7, 'Meatless Monday', 15, 'Enjoy a 15% discount on all vegetarian food items every Monday.', true),
       (8, 'Weekend Feast', 12, 'Get 12% off your weekend food order to savor the weekend vibes.', true),
       (10, 'Fashion Frenzy', 25, 'Revamp your wardrobe with a 25% discount on clothing and accessories.', true),
       (11, 'Back to School Sale', 30, 'Upgrade your school style with a 30% discount on clothing.', true);
 */

