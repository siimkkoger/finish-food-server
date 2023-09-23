INSERT INTO public.user (first_name, last_name, username, password, email, phone_number)
VALUES
    ('John', 'Doe', 'johndoe', 'password123', 'john.doe@example.com', '+15555555555'),
    ('Jane', 'Doe', 'janedoe', 'password123', 'jane.doe@example.com', '+15555555556'),
    ('Peter', 'Parker', 'spiderman', 'password123', 'peter.parker@example.com', '+15555555557');

INSERT INTO public.user_address (user_id, street_address, city, state, postal_index, country, phone_number, mobile)
VALUES
    (1, '123 Main Street', 'Anytown', 'CA', '91234', 'USA', '+15555555555', '+15555555558'),
    (2, '456 Elm Street', 'Anytown', 'CA', '91234', 'USA', '+15555555556', '+15555555559'),
    (3, '789 Oak Street', 'Anytown', 'CA', '91234', 'USA', '+15555555557', '+15555555560');

INSERT INTO public.payment_provider (name, description)
VALUES
    ('Visa', 'Visa is a global payments technology company that provides payment processing services to businesses and consumers.'),
    ('MasterCard', 'MasterCard is a global financial services corporation that connects consumers, businesses, financial institutions, governments and other organizations with electronic payment methods.'),
    ('PayPal', 'PayPal is an American financial technology company operating an online payments system in the world that supports online money transfers and serves as an electronic alternative to traditional paper methods like checks and money orders.');

INSERT INTO public.user_payment (user_id, provider_id, payment_type, account_no, expiry_date)
VALUES
    (1, 1, 'Credit Card', '1234-5678-9012-3456', '2023-12-31'),
    (2, 2, 'Debit Card', '9876-5432-1098-7654', '2024-06-30'),
    (3, 3, 'PayPal', 'johndoe@example.com', NULL);

INSERT INTO public.product_category (name)
VALUES
    ('FOOD'),
    ('CLOTHES');

INSERT INTO public.food_category (name)
VALUES
    ('American'),
    ('Italian'),
    ('Chinese'),
    ('Mexican'),
    ('Indian');

INSERT INTO public.clothes_category (name)
VALUES
    ('Tops'),
    ('Bottoms'),
    ('Dresses'),
    ('Outerwear'),
    ('Shoes');

INSERT INTO product_provider_type (name)
VALUES
    ('RESTAURANT'),
    ('STORE');

INSERT INTO product_provider (name, provider_type_id, description, address, phone, email, website, image)
VALUES
    ('Chipotle', 1, 'Fast food restaurant chain', '123 Main Street, Anytown, CA 91234', '+15555555555', 'info@chipotle.com', 'https://www.chipotle.com/', 'https://example.com/chipotle.jpg'),
    ('McDonalds', 1, 'Fast food restaurant chain', '456 Elm Street, Anytown, CA 91234', '+15555555556', 'info@mcdonalds.com', 'https://www.mcdonalds.com/', 'https://example.com/mcdonalds.jpg'),
    ('Walmart', 2, 'Department store chain', '456 Elm Street, Anytown, CA 91234', '+15555555556', 'info@walmart.com', 'https://www.walmart.com/', 'https://example.com/walmart.jpg'),
    ('Target', 2, 'Discount store chain', '789 Oak Street, Anytown, CA 91234', '+15555555557', 'info@target.com', 'https://www.target.com/', 'https://example.com/target.jpg');

-- product table
INSERT INTO product (product_category_id, product_provider_id, price, pickup_time)
VALUES
    (1, 1, 9.99, '2023-09-22 12:00:00'),
    (1, 1, 12.99, '2023-09-22 12:30:00'),
    (1, 2, 14.99, '2023-09-22 13:00:00'),
    (1, 2, 4.99, '2023-09-22 13:30:00'),
    (1, 2, 6.99, '2023-09-22 14:00:00'),
    (1, 3, 11.99, '2023-09-22 14:30:00'),
    (2, 3, 19.99, '2023-09-22 15:00:00'),
    (2, 3, 29.99, '2023-09-22 15:30:00'),
    (2, 4, 39.99, '2023-09-22 16:00:00'),
    (2, 4, 49.99, '2023-09-22 16:30:00'),
    (2, 4, 59.99, '2023-09-22 17:00:00');


-- food_category table
INSERT INTO food_category (name)
VALUES
    ('American'),
    ('Italian'),
    ('Chinese'),
    ('Mexican'),
    ('Indian');

-- food table
INSERT INTO food (product_id, name, description, image, dietary_restrictions)
VALUES
    (1, 'Hamburger', 'A classic American hamburger with lettuce, tomato, onion, and cheese.', 'https://example.com/hamburger.jpg', 'None'),
    (2, 'Pizza', 'A delicious pepperoni pizza with a crispy crust and melted cheese.', 'https://example.com/pizza.jpg', 'Pork'),
    (3, 'Pasta', 'A hearty spaghetti dish with tomato sauce and meatballs.', 'https://example.com/pasta.jpg', 'None'),
    (4, 'Ice cream', 'A bowl of cold, delicious ice cream.', 'https://example.com/icecream.jpg', 'Dairy'),
    (5, 'Tiramisu', 'A classic Italian dessert made with coffee-dipped ladyfingers and a creamy mascarpone filling.', 'https://example.com/tiramisu.jpg', 'Eggs, Dairy'),
    (6, 'Fried rice', 'A dish of rice fried with vegetables and your choice of protein.', 'https://example.com/friedrice.jpg', 'None');

INSERT INTO food_foods_category (food_id, food_category_id)
VALUES (1, 1), (2, 1), (3, 1), (4, 2), (5, 2), (6, 3);

-- clothes_category table
INSERT INTO clothes_category (name)
VALUES
    ('Tops'),
    ('Bottoms'),
    ('Dresses'),
    ('Outerwear'),
    ('Shoes');

-- clothes table
INSERT INTO clothes (product_id, name, description, image, size, color)
VALUES
    (7, 'T-shirt', 'A classic black cotton t-shirt.', 'https://example.com/tshirt.jpg', 'Medium', 'Black'),
    (8, 'Jeans', 'A pair of blue denim jeans.', 'https://example.com/jeans.jpg', '32W32L', 'Blue'),
    (9, 'Dress', 'A black and white striped dress with a belted waist.', 'https://example.com/dress.jpg', 'Small', 'Black and white'),
    (10, 'Jacket', 'A denim jacket with a button-front closure and two pockets.', 'https://example.com/jacket.jpg', 'Medium', 'Denim'),
    (11, 'Sneakers', 'A pair of white sneakers with a rubber sole.', 'https://example.com/sneakers.jpg', '8', 'White');

INSERT INTO clothes_clothes_category (clothes_id, clothes_category_id)
VALUES (1, 1), (2, 2), (3, 3), (4, 4), (5, 5);

-- discount table
INSERT INTO discount (product_id, name, discount_percent, description, active)
VALUES
    (1, 'Labor Day Sale', 10, 'Get 10% off your purchase of hamburgers on Labor Day.', true),
    (3, 'Pasta Night', 20, 'Get 20% off your purchase of pasta on Wednesday nights.', true),
    (4, 'Ice Cream Sundae Special', 15, 'Get 15% off your purchase of ice cream sundaes on Sundays.', true);
