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

INSERT INTO public.product_provider_type (name)
VALUES ('RESTAURANT'),
       ('STORE');

-- dummy data for product_provider table (RESTAURANT)
INSERT INTO public.product_provider (name, product_provider_type, description, address, phone, email, website, image)
VALUES ('McDonalds', 'RESTAURANT', 'Fast food restaurant chain', '456 Elm Street, Anytown, CA 91234', '+15555555556',
        'info@mcdonalds.com', 'https://www.mcdonalds.com/', 'https://example.com/mcdonalds.jpg'),
       ('Subway', 'RESTAURANT', 'Submarine sandwich chain', '123 Elm Street, Anytown, CA 91234', '+15555555588',
        'info@subway.com', 'https://www.subway.com/', 'https://example.com/subway.jpg'),
       ('Pizza Mc Hut', 'RESTAURANT', 'Pizza restaurant chain', '456 Oak Street, Anytown, CA 91234', '+15555555589',
        'info@pizzahut.com', 'https://www.pizzahut.com/', 'https://example.com/pizzahut.jpg'),
       ('Chipotle Mc', 'RESTAURANT', 'Fast food restaurant chain', '123 Main Street, Anytown, CA 91234', '+15555555555',
        'info@chipotle.com', 'https://www.chipotle.com/', 'https://example.com/chipotle.jpg'),
       ('Tiki Taco', 'RESTAURANT', 'Fast food restaurant chain', '123 Main Street, Anytown, CA 91234', '+15555555555',
        'info@tikitaco.com', 'https://www.tikitaco.com/', 'https://example.com/tikitaco.jpg');

-- dummy data for product_provider table (STORE)
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
        'https://example.com/sportsgearstore.jpg');

-- dummy data for product table (FOOD)
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 1, 'Hamburger', 'A classic American hamburger with lettuce, tomato, onion, and cheese.',
        'https://example.com/hamburger.jpg', 9.99, '2023-09-22 12:00:00', null),
       ('FOOD', 3, 'Pizza', 'A delicious pepperoni pizza with a crispy crust and melted cheese.',
        'https://example.com/pizza.jpg', 12.99, '2023-09-22 13:30:00', null),
       ('FOOD', 2, 'Pasta', 'A hearty spaghetti dish with tomato sauce and meatballs.', 'https://example.com/pasta.jpg',
        14.99, '2023-09-23 13:00:00', null),
       ('FOOD', 1, 'Ice cream', 'A bowl of cold, delicious ice cream.', 'https://example.com/icecream.jpg', 4.99,
        '2023-09-23 13:30:00', null),
       ('FOOD', 1, 'Tiramisu',
        'A classic Italian dessert made with coffee-dipped ladyfingers and a creamy mascarpone filling.',
        'https://example.com/tiramisu.jpg', 6.99, '2023-09-24 14:00:00', null),
       ('FOOD', 4, 'Fried rice', 'A dish of rice fried with vegetables and your choice of protein.',
        'https://example.com/friedrice.jpg', 11.99, '2023-09-25 14:30:00', null),
       ('FOOD', 2, 'Cheeseburger', 'A classic cheeseburger with lettuce, tomato, onion, and a juicy patty.',
        'https://example.com/cheeseburger.jpg', 8.99, '2023-10-25 12:15:00', null),
       ('FOOD', 3, 'Margherita Pizza', 'A traditional Margherita pizza with fresh mozzarella, basil, and tomato sauce.',
        'https://example.com/margheritapizza.jpg', 11.99, '2024-10-25 12:45:00', null),
       ('FOOD', 4, 'General Tso Chicken', 'Crispy chicken in a sweet and spicy General Tso sauce with steamed rice.',
        'https://example.com/generaltsochicken.jpg', 13.99, '2024-11-25 13:15:00', null),
       ('FOOD', 5, 'Tacos', 'Delicious street-style tacos with your choice of fillings.',
        'https://example.com/tacos.jpg', 9.99, '2025-01-01 13:45:00', null);

-- dummy data for product table (CLOTHES)
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time)
VALUES ('CLOTHES', 6, 'T-shirt', 'A classic black cotton t-shirt.', 'https://example.com/tshirt.jpg', 19.99,
        '2023-09-22 15:00:00'),
       ('CLOTHES', 6, 'Jeans', 'A pair of blue denim jeans.', 'https://example.com/jeans.jpg', 29.99,
        '2023-09-22 15:30:00'),
       ('CLOTHES', 7, 'Dress', 'A black and white striped dress with a belted waist.', 'https://example.com/dress.jpg',
        39.99, '2023-09-22 16:00:00'),
       ('CLOTHES', 7, 'Jacket', 'A denim jacket with a button-front closure and two pockets.',
        'https://example.com/jacket.jpg', 49.99, '2023-09-22 16:30:00'),
       ('CLOTHES', 8, 'Sneakers', 'A pair of white sneakers with a rubber sole.', 'https://example.com/sneakers.jpg',
        59.99, '2023-09-22 17:00:00'),
       ('CLOTHES', 8, 'Hoodie', 'A cozy hooded sweatshirt in charcoal gray.', 'https://example.com/hoodie.jpg', 34.99,
        '2023-09-22 17:30:00'),
       ('CLOTHES', 9, 'Skirt', 'A floral-print skirt with an elastic waistband.', 'https://example.com/skirt.jpg',
        24.99, '2023-09-22 18:00:00'),
       ('CLOTHES', 9, 'Blouse', 'A stylish blouse with a v-neck and three-quarter sleeves.',
        'https://example.com/blouse.jpg', 27.99, '2023-09-22 18:30:00'),
       ('CLOTHES', 10, 'Sweatpants', 'Comfortable sweatpants in heather gray for lounging.',
        'https://example.com/sweatpants.jpg', 19.99, '2023-09-22 19:00:00'),
       ('CLOTHES', 10, 'Sandals', 'Casual sandals with a cushioned sole for walking.',
        'https://example.com/sandals.jpg', 39.99, '2023-09-22 19:30:00');

-- dummy data for product table (FOOD, deleted)
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 1, 'DELETED - Hamburger', 'A classic American hamburger with lettuce, tomato, onion, and cheese.',
        'https://example.com/hamburger.jpg', 9.99, '2023-09-22 12:00:00', '2023-09-22 12:00:00'),
       ('FOOD', 3, 'DELETED - Pizza', 'A delicious pepperoni pizza with a crispy crust and melted cheese.',
        'https://example.com/pizza.jpg', 12.99, '2023-09-22 13:30:00', '2023-09-22 12:00:00'),
       ('FOOD', 2, 'DELETED - Pasta', 'A hearty spaghetti dish with tomato sauce and meatballs.',
        'https://example.com/pasta.jpg', 14.99, '2023-09-23 13:00:00', '2023-09-22 12:00:00'),
       ('FOOD', 1, 'DELETED - Ice cream', 'A bowl of cold, delicious ice cream.', 'https://example.com/icecream.jpg',
        4.99, '2023-09-23 13:30:00', '2023-09-22 12:00:00'),
       ('FOOD', 1, 'DELETED - Tiramisu',
        'A classic Italian dessert made with coffee-dipped ladyfingers and a creamy mascarpone filling.',
        'https://example.com/tiramisu.jpg',
        6.99, '2023-09-24 14:00:00', '2023-09-22 12:00:00');


-- food_category table
INSERT INTO public.food_category (name)
VALUES ('Fast food'),
       ('Dessert'),
       ('American'),
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
INSERT INTO public.food (product_id, dietary_restrictions)
VALUES (1, 'None'),
       (2, 'None'),
       (3, 'None'),
       (4, 'Contains dairy.'),
       (5, 'Contains dairy and gluten.'),
       (6, 'None'),
       (7, 'None'),
       (8, 'None'),
       (9, 'Contains gluten.'),
       (10, 'None'),
       (21, 'None'),
       (22, 'None'),
       (23, 'None'),
       (24, 'Contains dairy.'),
       (25, 'Contains dairy and gluten.');


INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (1, 1),  -- Hamburger is Fast Food
       (1, 3),  -- Hamburger is American
       (2, 1),  -- Pizza is Fast Food
       (2, 3),  -- Pizza is American
       (2, 4),  -- Pizza is Italian
       (3, 4),  -- Pasta is Italian
       (3, 5),  -- Pasta is Chinese
       (4, 2),  -- Ice cream is Dessert
       (4, 7),  -- Ice cream is Indian
       (5, 2),  -- Tiramisu is Dessert
       (5, 4),  -- Tiramisu is Italian
       (6, 5),  -- Fried rice is Chinese
       (6, 7),  -- Fried rice is Indian
       (7, 1),  -- Cheeseburger is Fast Food
       (7, 3),  -- Cheeseburger is American
       (8, 1),  -- Margherita Pizza is Fast Food
       (8, 3),  -- Margherita Pizza is American
       (8, 4),  -- Margherita Pizza is Italian
       (9, 1),  -- General Tso Chicken is Fast Food
       (9, 5),  -- General Tso Chicken is Chinese
       (10, 1), -- Tacos is Fast Food
       (10, 6), -- Tacos is Mexican
       (11, 1), -- DELETED - Hamburger is Fast Food
       (11, 3), -- DELETED - Hamburger is American
       (12, 1), -- DELETED - Pizza is Fast Food
       (12, 3), -- DELETED - Pizza is American
       (12, 4), -- DELETED - Pizza is Italian
       (13, 4), -- DELETED - Pasta is Italian
       (13, 5), -- DELETED - Pasta is Chinese
       (14, 2), -- DELETED - Ice cream is Dessert
       (14, 7), -- DELETED - Ice cream is Indian
       (15, 2), -- DELETED - Tiramisu is Dessert
       (15, 4); -- DELETED - Tiramisu is Italian


INSERT INTO public.clothes_category (name)
VALUES ('Tops'),
       ('Bottoms'),
       ('Dresses'),
       ('Outerwear'),
       ('Shoes'),
       ('Accessories'),
       ('Athletic wear'),
       ('Swimwear'),
       ('Underwear'),
       ('Sleepwear'),
       ('Socks'),
       ('Hats'),
       ('Belts'),
       ('Jewelry'),
       ('Bags'),
       ('Sunglasses'),
       ('Scarves'),
       ('Gloves'),
       ('Wallets'),
       ('Watches');

INSERT INTO public.clothes (product_id, size, color)
VALUES (11, 'Medium', 'Black'),
       (12, '32x32', 'Blue'),
       (13, 'Small', 'Black and White'),
       (14, 'Large', 'Denim Blue'),
       (15, '10', 'White'),
       (16, 'Large', 'Charcoal Gray'),
       (17, 'Medium', 'Floral Print'),
       (18, 'Small', 'Navy Blue'),
       (19, 'Medium', 'Heather Gray'),
       (20, '8', 'Brown');

INSERT INTO public.clothes_clothes_category (clothes_id, clothes_category_id)
VALUES (1, 1), -- T-shirt is Tops
       (2, 2), -- Jeans is Bottoms
       (3, 3), -- Dress is Dresses
       (4, 4), -- Jacket is Outerwear
       (5, 5), -- Sneakers is Shoes
       (5, 7), -- Sneakers is Athletic wear
       (6, 1), -- Hoodie is Tops
       (6, 4), -- Hoodie is Outerwear
       (7, 3), -- Skirt is Dresses
       (8, 1), -- Blouse is Tops
       (9, 2), -- Sweatpants is Bottoms
       (10, 5); -- Sandals is Shoes


INSERT INTO public.discount (product_id, name, discount_percent, description, active)
VALUES (1, 'Summer Sizzle', 15, 'Get ready for summer with a 15% discount on your favorite burgers!', true),
       (2, 'Pizza Palooza', 20, 'Celebrate with a 20% discount on all pizza varieties.', true),
       (5, 'Dessert Delight', 10, 'Indulge in sweet treats with a 10% discount on desserts.', true),
       (7, 'Meatless Monday', 15, 'Enjoy a 15% discount on all vegetarian food items every Monday.', true),
       (8, 'Weekend Feast', 12, 'Get 12% off your weekend food order to savor the weekend vibes.', true),
       (10, 'Fashion Frenzy', 25, 'Revamp your wardrobe with a 25% discount on clothing and accessories.', true),
       (11, 'Back to School Sale', 30, 'Upgrade your school style with a 30% discount on clothing.', true);

