-- drop everything
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
GRANT ALL ON SCHEMA public TO testuser;


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
    pickup_time         timestamp               NOT NULL,

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

INSERT INTO public.product_type (name)
VALUES ('FOOD'),
       ('CLOTHES');

INSERT INTO public.clothes_category (name)
VALUES ('Tops'),
       ('Bottoms'),
       ('Dresses'),
       ('Outerwear'),
       ('Shoes');

INSERT INTO product_provider_type (name)
VALUES ('RESTAURANT'),
       ('STORE');

INSERT INTO product_provider (name, product_provider_type, description, address, phone, email, website, image)
VALUES ('Chipotle', 'RESTAURANT', 'Fast food restaurant chain', '123 Main Street, Anytown, CA 91234', '+15555555555',
        'info@chipotle.com', 'https://www.chipotle.com/', 'https://example.com/chipotle.jpg'),
       ('McDonalds', 'RESTAURANT', 'Fast food restaurant chain', '456 Elm Street, Anytown, CA 91234', '+15555555556',
        'info@mcdonalds.com', 'https://www.mcdonalds.com/', 'https://example.com/mcdonalds.jpg'),
       ('Walmart', 'STORE', 'Department store chain', '456 Elm Street, Anytown, CA 91234', '+15555555556',
        'info@walmart.com', 'https://www.walmart.com/', 'https://example.com/walmart.jpg'),
       ('Target', 'STORE', 'Discount store chain', '789 Oak Street, Anytown, CA 91234', '+15555555557',
        'info@target.com', 'https://www.target.com/', 'https://example.com/target.jpg'),
       ('Subway', 'RESTAURANT', 'Submarine sandwich chain', '123 Elm Street, Anytown, CA 91234', '+15555555588',
        'info@subway.com', 'https://www.subway.com/', 'https://example.com/subway.jpg'),
       ('Pizza Hut', 'RESTAURANT', 'Pizza restaurant chain', '456 Oak Street, Anytown, CA 91234', '+15555555589',
        'info@pizzahut.com', 'https://www.pizzahut.com/', 'https://example.com/pizzahut.jpg'),
       ('Fisherman Catch', 'RESTAURANT', 'Seafood restaurant with a variety of fresh catches',
        '789 Pine Street, Anytown, CA 91234', '+15555555590', 'info@fishermanscatch.com',
        'https://www.fishermanscatch.com/', 'https://example.com/fishermanscatch.jpg');

INSERT INTO product (product_type_name, product_provider_id, name, description, image, price, pickup_time)
VALUES ('FOOD', 2, 'Hamburger', 'A classic American hamburger with lettuce, tomato, onion, and cheese.',
        'https://example.com/hamburger.jpg', 9.99, '2023-09-22 12:30:00'),
       ('FOOD', 1, 'Pizza', 'A delicious pepperoni pizza with a crispy crust and melted cheese.',
        'https://example.com/pizza.jpg', 12.99, '2023-09-22 12:30:00'),
       ('FOOD', 2, 'Pasta', 'A hearty spaghetti dish with tomato sauce and meatballs.', 'https://example.com/pasta.jpg',
        14.99, '2023-09-22 13:00:00'),
       ('FOOD', 2, 'Ice cream', 'A bowl of cold, delicious ice cream.', 'https://example.com/icecream.jpg', 4.99,
        '2023-09-22 13:30:00'),
       ('FOOD', 2, 'Tiramisu',
        'A classic Italian dessert made with coffee-dipped ladyfingers and a creamy mascarpone filling.',
        'https://example.com/tiramisu.jpg', 6.99, '2023-09-22 14:00:00'),
       ('FOOD', 3, 'Fried rice', 'A dish of rice fried with vegetables and your choice of protein.',
        'https://example.com/friedrice.jpg', 11.99, '2023-09-22 14:30:00'),

       ('FOOD', 2, 'Cheeseburger', 'A classic cheeseburger with lettuce, tomato, onion, and a juicy patty.',
        'https://example.com/cheeseburger.jpg', 8.99, '2023-09-22 12:15:00'),
       ('FOOD', 6, 'Margherita Pizza', 'A traditional Margherita pizza with fresh mozzarella, basil, and tomato sauce.',
        'https://example.com/margheritapizza.jpg', 11.99, '2023-09-22 12:45:00'),
       ('FOOD', 7, 'General Tso Chicken', 'Crispy chicken in a sweet and spicy General Tso sauce with steamed rice.',
        'https://example.com/generaltsochicken.jpg', 13.99, '2023-09-22 13:15:00'),
       ('FOOD', 5, 'Tacos', 'Delicious street-style tacos with your choice of fillings.',
        'https://example.com/tacos.jpg', 9.99, '2023-09-22 13:45:00'),
       ('FOOD', 5, 'Butter Chicken', 'A rich and creamy Indian butter chicken dish served with naan bread.',
        'https://example.com/butterchicken.jpg', 14.99, '2023-09-22 14:15:00'),
       ('FOOD', 5, 'Sushi Platter', 'A variety of fresh sushi rolls served with soy sauce and wasabi.',
        'https://example.com/sushi.jpg', 16.99, '2023-09-22 14:45:00'),

       ('CLOTHES', 3, 'T-shirt', 'A classic black cotton t-shirt.', 'https://example.com/tshirt.jpg', 19.99,
        '2023-09-22 15:00:00'),
       ('CLOTHES', 3, 'Jeans', 'A pair of blue denim jeans.', 'https://example.com/jeans.jpg', 29.99,
        '2023-09-22 15:30:00'),
       ('CLOTHES', 4, 'Dress', 'A black and white striped dress with a belted waist.', 'https://example.com/dress.jpg',
        39.99, '2023-09-22 16:00:00'),
       ('CLOTHES', 4, 'Jacket', 'A denim jacket with a button-front closure and two pockets.',
        'https://example.com/jacket.jpg', 49.99, '2023-09-22 16:30:00'),
       ('CLOTHES', 4, 'Sneakers', 'A pair of white sneakers with a rubber sole.', 'https://example.com/sneakers.jpg',
        59.99, '2023-09-22 17:00:00');


-- food_category table
INSERT INTO food_category (name)
VALUES ('American'),
       ('Italian'),
       ('Chinese'),
       ('Mexican'),
       ('Indian'),
       ('Vegetarian'),
       ('Vegan'),
       ('Seafood'),
       ('Mediterranean'),
       ('BBQ');

-- food table
INSERT INTO food (product_id, dietary_restrictions)
VALUES (1, 'Contains beef and dairy.'),
       (2, 'Pork'),
       (3, 'None'),
       (4, 'Dairy'),
       (5, 'Eggs, Dairy'),
       (6, 'None'),
       (7, 'Contains beef and dairy.'),
       (8, 'None'),
       (9, 'None'),
       (10, 'None'),
       (11, 'None'),
       (12, 'None');

INSERT INTO food_food_category (food_id, food_category_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 2),
       (5, 2),
       (6, 3),
       (1, 2),
       (2, 3),
       (3, 4),
       (4, 1),
       (5, 3),
       (1, 3);

INSERT INTO clothes_category (name)
VALUES ('Tops'),
       ('Bottoms'),
       ('Dresses'),
       ('Outerwear'),
       ('Shoes');

INSERT INTO clothes (product_id, size, color)
VALUES (13, 'Medium', 'Black'),
       (14, '32W32L', 'Blue'),
       (15, 'Small', 'Black and white'),
       (16, 'Medium', 'Denim'),
       (17, '8', 'White');

INSERT INTO clothes_clothes_category (clothes_id, clothes_category_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 5);

INSERT INTO discount (product_id, name, discount_percent, description, active)
VALUES (1, 'Labor Day Sale', 10, 'Get 10% off your purchase of hamburgers on Labor Day.', true),
       (3, 'Pasta Night', 20, 'Get 20% off your purchase of pasta on Wednesday nights.', true),
       (4, 'Ice Cream Sundae Special', 15, 'Get 15% off your purchase of ice cream sundaes on Sundays.', true),
       (7, 'Meatless Monday', 15, 'Get 15% off vegetarian food on Mondays.', true),
       (8, 'Pizza Night', 10, 'Get 10% off all pizzas on Friday nights.', true),
       (10, 'Weekend Special', 12, 'Get 12% off your weekend food order.', true);
