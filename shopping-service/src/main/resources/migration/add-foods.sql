-- food_category table (countries)
INSERT INTO public.food_category (name)
VALUES ('American'), -- 1
       ('Italian'),
       ('Chinese'),
       ('Mexican'),
       ('Indian'), -- 5
       ('Mediterranean'),
       ('Korean'),
       ('Japanese'), -- 8
       ('Thai'),
       ('Vietnamese'), -- 10
       ('Greek'),
       ('French'), -- 12
       ('Spanish'),
       ('German'); -- 14

-- food_category table (fast food)
INSERT INTO public.food_category (name)
VALUES ('Fast Food'), -- 15
       ('Fried'),
       ('Burger'),
       ('Pizza'), -- 18
       ('Fries'),
       ('Chicken'), -- 20
       ('Tacos'),
       ('Burritos'),
       ('Sandwiches'),
       ('Hot Dogs'),
       ('Wings'), -- 25
       ('Salads'),
       ('Wraps'),
       ('Subs'); -- 28

-- food_category table (dessert)
INSERT INTO public.food_category (name)
VALUES ('Ice Cream'), -- 29
       ('Frozen Yogurt'), -- 30
       ('Dessert'),
       ('Donuts'), -- 32
       ('Cakes'),
       ('Cupcakes'),
       ('Cookies'), -- 35
       ('Pies'),
       ('Pastries'), -- 37
       ('Bread'),
       ('Brownies'),
       ('Candy'), -- 40
       ('Chocolate'),
       ('Milkshakes'),
       ('Smoothies'), -- 43
       ('Juice'),
       ('Coffee'),
       ('Tea'),
       ('Bubble Tea'),
       ('Boba'); -- 48

-- food_category table (warm meals)
INSERT INTO public.food_category (name)
VALUES ('Pasta'), -- 49
       ('Soup'), -- 50
       ('Stew'),
       ('Rice'),
       ('Noodles'), -- 53
       ('Curry'),
       ('Stir Fry'),
       ('Dumplings'), -- 56
       ('Ramen'),
       ('Pho'), -- 58
       ('Sushi'),
       ('Sashimi'),
       ('Teriyaki'), -- 61
       ('Katsu'),
       ('Soba'),
       ('Udon'), -- 64
       ('Pad Thai'),
       ('Fried Rice'),
       ('Spring Rolls'), -- 67
       ('Egg Rolls'),
       ('Tofu'),
       ('Meat'), -- 70
       ('Seafood'), -- 71
       ('Vegetarian'), -- 72
       ('Vegan'); -- 73

-- dummy data for product table (FOOD 1-10, PRODUCTS 1-10)
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 1, 'Hamburger', 'A classic American hamburger with lettuce, tomato, onion, and cheese.',
        'https://images.unsplash.com/photo-1551615593-ef5fe247e8f7?w=500', 9.99, '2023-09-22 12:00:00', null),
       ('FOOD', 3, 'Pizza', 'A delicious pepperoni pizza with a crispy crust and melted cheese.',
        'https://hips.hearstapps.com/hmg-prod/images/classic-cheese-pizza-recipe-2-64429a0cb408b.jpg?crop=0.6666666666666667xw:1xh;center,top&resize=1200:*',
        12.99, '2023-09-22 13:30:00', null),
       ('FOOD', 2, 'Pasta', 'A hearty spaghetti dish with tomato sauce and meatballs.',
        'https://foodhub.scene7.com/is/image/woolworthsltdprod/Easy-chicken-and-bacon-pasta:Mobile-1300x1150',
        14.99, '2023-09-23 13:00:00', null),
       ('FOOD', 7, 'Ice cream', 'A bowl of cold, delicious ice cream.',
        'https://sugarspunrun.com/wp-content/uploads/2023/03/ice-cream-cone-cupcakes-1-of-1-3.jpg', 4.99,
        '2023-09-23 13:30:00', null),
       ('FOOD', 10, 'Tiramisu',
        'A classic Italian dessert made with coffee-dipped ladyfingers and a creamy mascarpone filling.',
        'https://static01.nyt.com/images/2017/04/05/dining/05COOKING-TIRAMISU1/05COOKING-TIRAMISU1-threeByTwoMediumAt2X-v2.jpg',
        6.99, '2023-09-24 14:00:00', null),
       ('FOOD', 4, 'Fried rice', 'A dish of rice fried with vegetables and your choice of protein.',
        'https://www.australianeggs.org.au/assets/Uploads/Egg-fried-rice-2.jpg', 11.99, '2023-09-25 14:30:00', null),
       ('FOOD', 2, 'Cheeseburger', 'A classic cheeseburger with lettuce, tomato, onion, and a juicy patty.',
        'https://people.com/thmb/7xiBWLcomIVvVYHWuHRDXVv97Vo=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc():focal(669x478:671x480)/mcdonalds-free-cheeseburger-day-091423-01-baca4b5e5fdf47feb4afac36b659c26e.jpg',
        8.99, '2023-10-25 12:15:00', null),
       ('FOOD', 3, 'Margherita Pizza', 'A traditional Margherita pizza with fresh mozzarella, basil, and tomato sauce.',
        'https://images.prismic.io/eataly-us/ed3fcec7-7994-426d-a5e4-a24be5a95afd_pizza-recipe-main.jpg?auto=compress,format',
        11.99, '2024-10-25 12:45:00', null),
       ('FOOD', 4, 'General Tso Chicken', 'Crispy chicken in a sweet and spicy General Tso sauce with steamed rice.',
        'https://www.thecountrycook.net/wp-content/uploads/2021/08/thumbnail-General-Tsos-Chicken-scaled.jpg', 13.99,
        '2024-11-25 13:15:00', null),
       ('FOOD', 5, 'Tacos', 'Delicious street-style tacos with your choice of fillings.',
        'https://cdn.vox-cdn.com/thumbor/0-SMli6Ir1f6qG-bpDgeHIXAUU4=/1400x1400/filters:format(png)/cdn.vox-cdn.com/uploads/chorus_asset/file/10426267/Tiki_Taco_DC_food.png',
        9.99, '2025-01-01 13:45:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Hamburger
    (1, false, false, true, true, true, false),
    -- Pizza
    (2, false, false, false, false, true, false),
    -- Pasta
    (3, true, false, false, false, true, false),
    -- Ice cream
    (4, true, false, false, false, true, false),
    -- Tiramisu
    (5, true, false, false, false, true, false),
    -- Fried rice
    (6, true, false, false, true, true, false),
    -- Cheeseburger
    (7, false, false, true, false, true, false),
    -- Margherita Pizza
    (8, true, false, false, false, true, false),
    -- General Tso Chicken
    (9, false, false, false, true, true, false),
    -- Tacos
    (10, false, false, false, true, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (1, 15),  -- Hamburger is Fast Food
       (1, 17),  -- Hamburger is Burger
       (1, 1),   -- Hamburger is American
       (2, 18),  -- Pizza is Pizza
       (2, 2),   -- Pizza is Italian
       (3, 2),   -- Pasta is Italian
       (4, 29),  -- Ice cream is Ice Cream
       (4, 31),  -- Ice cream is Dessert
       (5, 2),   -- Tiramisu is Italian (Italian dessert)
       (5, 31),  -- Tiramisu is Dessert
       (6, 4),   -- Fried rice is Chinese
       (6, 19),  -- Fried rice is Fried
       (6, 29),  -- Fried rice is Fast Food
       (6, 1),   -- Fried rice is American (American-Chinese fusion)
       (7, 17),  -- Cheeseburger is Burger
       (7, 15),  -- Cheeseburger is Fast Food
       (7, 1),   -- Cheeseburger is American
       (8, 18),  -- Margherita Pizza is Pizza
       (8, 2),   -- Margherita Pizza is Italian
       (9, 2),   -- General Tso Chicken is Italian (Italian-inspired Chinese dish)
       (9, 4),   -- General Tso Chicken is Chinese
       (9, 29),  -- General Tso Chicken is Fast Food
       (9, 6),   -- General Tso Chicken is Chicken
       (10, 7),  -- Tacos is Tacos
       (10, 15), -- Tacos is Fast Food (Fast food-style tacos)
       (10, 4),  -- Tacos is Mexican
       (10, 6);  -- Tacos is Chicken


-- Adding 5 more rows with product_provider_type 1
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 1, 'Chicken Sandwich', 'A delicious chicken sandwich with lettuce, tomato, and special sauce.',
        'https://example.com/chickensandwich.jpg', 8.99, '2023-09-22 14:00:00', null),
       ('FOOD', 1, 'Double Cheeseburger',
        'A mouthwatering double cheeseburger with two juicy patties and all the fixings.',
        'https://example.com/doublecheeseburger.jpg', 10.99, '2023-09-23 14:30:00', null),
       ('FOOD', 1, 'Fish Fillet Sandwich', 'A crispy fish fillet sandwich with tartar sauce and a soft bun.',
        'https://example.com/fishfilletsandwich.jpg', 7.99, '2023-09-24 15:00:00', null),
       ('FOOD', 1, 'Salad with nuts and olives', 'A fresh garden salad with mixed greens, tomatoes, and your choice of dressing.',
        'https://example.com/salad.jpg', 6.99, '2023-09-25 15:30:00', null),
       ('FOOD', 1, 'Milkshake', 'A creamy and indulgent milkshake available in various flavors.',
        'https://example.com/milkshake.jpg', 4.99, '2023-09-26 16:00:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Chicken Sandwich
    (11, false, false, false, true, true, false),
    -- Double Cheeseburger
    (12, false, false, false, false, true, false),
    -- Fish Fillet Sandwich
    (13, false, false, false, true, true, false),
    -- Salad with nuts and olives
    (14, true, true, true, true, true, false),
    -- Milkshake
    (15, false, false, false, false, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (11, 17), -- Chicken Sandwich is Burger
       (11, 15), -- Chicken Sandwich is Fast Food
       (11, 1),  -- Chicken Sandwich is American
       (12, 17), -- Double Cheeseburger is Burger
       (12, 15), -- Double Cheeseburger is Fast Food
       (12, 1),  -- Double Cheeseburger is American
       (13, 17), -- Fish Fillet Sandwich is Burger
       (13, 15), -- Fish Fillet Sandwich is Fast Food
       (13, 1),  -- Fish Fillet Sandwich is American
       (14, 25), -- Salad with nuts and olives is Salads
       (14, 15), -- Salad with nuts and olives is Fast Food
       (14, 1),  -- Salad with nuts and olives is American
       (15, 29), -- Milkshake is Ice Cream
       (15, 31), -- Milkshake is Dessert
       (15, 15), -- Milkshake is Fast Food
       (15, 1);  -- Milkshake is American

-- Adding 5 more rows with product_provider_type 2
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 2, 'Vegan Lasagna', 'A hearty lasagna dish with layers of pasta, cheese, and meat sauce.',
        'https://example.com/lasagna.jpg', 15.99, '2023-10-02 14:30:00', null),
       ('FOOD', 2, 'Meatball Sub', 'A meatball sub sandwich loaded with meatballs, marinara sauce, and melted cheese.',
        'https://example.com/meatballsub.jpg', 9.99, '2023-10-03 15:00:00', null),
       ('FOOD', 2, 'Chicken Alfredo', 'Creamy chicken Alfredo pasta with a rich and savory sauce.',
        'https://example.com/chickenalfredo.jpg', 13.99, '2023-10-04 15:30:00', null),
       ('FOOD', 2, 'Caprese Salad', 'A classic Caprese salad with fresh mozzarella, tomatoes, and basil.',
        'https://example.com/capresesalad.jpg', 7.99, '2023-10-05 16:00:00', null),
       ('FOOD', 2, 'Espresso', 'A strong and aromatic espresso shot for coffee lovers.',
        'https://example.com/espresso.jpg', 2.99, '2023-10-06 16:30:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Vegan Lasagna
    (16, true, true, false, false, true, false),
    -- Meatball Sub
    (17, false, false, false, false, true, false),
    -- Chicken Alfredo
    (18, false, false, false, false, true, false),
    -- Caprese Salad
    (19, true, true, true, true, true, false),
    -- Espresso
    (20, true, true, true, true, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (16, 49), -- Vegan Lasagna is Pasta
       (16, 15), -- Vegan Lasagna is Fast Food
       (16, 1),  -- Vegan Lasagna is American
       (16, 72), -- Vegan Lasagna is Vegetarian
       (16, 73), -- Vegan Lasagna is Vegan
       (17, 28), -- Meatball Sub is Subs
       (17, 15), -- Meatball Sub is Fast Food
       (17, 1),  -- Meatball Sub is American
       (17, 6),  -- Meatball Sub is Chicken
       (18, 49), -- Chicken Alfredo is Pasta
       (18, 15), -- Chicken Alfredo is Fast Food
       (18, 1),  -- Chicken Alfredo is American
       (18, 6),  -- Chicken Alfredo is Chicken
       (19, 25), -- Caprese Salad is Salads
       (19, 15), -- Caprese Salad is Fast Food
       (19, 1),  -- Caprese Salad is American
       (19, 72), -- Caprese Salad is Vegetarian
       (19, 73), -- Caprese Salad is Vegan
       (20, 48), -- Espresso is Coffee
       (20, 15), -- Espresso is Fast Food
       (20, 1),  -- Espresso is American
       (20, 72), -- Espresso is Vegetarian
       (20, 73); -- Espresso is Vegan

-- Adding 5 more rows with product_provider_type 3
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 3, 'Vegetarian Pizza', 'A vegetarian pizza with fresh vegetables and a cheesy blend.',
        'https://example.com/vegetarianpizza.jpg', 11.99, '2023-09-27 12:00:00', null),
       ('FOOD', 3, 'Supreme Pizza',
        'A supreme pizza loaded with all the toppings including pepperoni, sausage, and more.',
        'https://example.com/supremepizza.jpg', 13.99, '2023-09-28 12:30:00', null),
       ('FOOD', 3, 'Hawaiian Pizza', 'A Hawaiian pizza with pineapple, ham, and a delicious tomato sauce.',
        'https://example.com/hawaiianpizza.jpg', 12.99, '2023-09-29 13:00:00', null),
       ('FOOD', 3, 'BBQ Chicken Pizza', 'A BBQ chicken pizza with tender chicken, BBQ sauce, and red onions.',
        'https://example.com/bbqchickenpizza.jpg', 14.99, '2023-09-30 13:30:00', null),
       ('FOOD', 3, 'Veggie Pizza', 'A veggie-packed pizza with bell peppers, mushrooms, and olives.',
        'https://example.com/veggiepizza.jpg', 12.99, '2023-10-01 14:00:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Vegetarian Pizza
    (21, true, true, false, false, true, false),
    -- Supreme Pizza
    (22, false, false, false, false, true, false),
    -- Hawaiian Pizza
    (23, false, false, false, false, true, false),
    -- BBQ Chicken Pizza
    (24, false, false, false, false, true, false),
    -- Veggie Pizza
    (25, true, true, false, false, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (21, 18), -- Vegetarian Pizza is Pizza
       (21, 2),  -- Vegetarian Pizza is Italian
       (21, 15), -- Vegetarian Pizza is Fast Food
       (21, 72), -- Vegetarian Pizza is Vegetarian
       (21, 73), -- Vegetarian Pizza is Vegan
       (22, 18), -- Supreme Pizza is Pizza
       (22, 2),  -- Supreme Pizza is Italian
       (22, 15), -- Supreme Pizza is Fast Food
       (23, 18), -- Hawaiian Pizza is Pizza
       (23, 2),  -- Hawaiian Pizza is Italian
       (23, 15), -- Hawaiian Pizza is Fast Food
       (24, 18), -- BBQ Chicken Pizza is Pizza
       (24, 2),  -- BBQ Chicken Pizza is Italian
       (24, 15), -- BBQ Chicken Pizza is Fast Food
       (24, 6),  -- BBQ Chicken Pizza is Chicken
       (25, 18), -- Veggie Pizza is Pizza
       (25, 2),  -- Veggie Pizza is Italian
       (25, 15), -- Veggie Pizza is Fast Food
       (25, 72), -- Veggie Pizza is Vegetarian
       (25, 73); -- Veggie Pizza is Vegan

-- Adding 5 more rows with product_provider_type 4
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 4, 'Orange Chicken', 'Tender orange chicken with a sweet and tangy sauce, served with steamed rice.',
        'https://example.com/orangechicken.jpg', 11.99, '2023-10-07 17:00:00', null),
       ('FOOD', 4, 'Kung Pao Shrimp', 'Spicy Kung Pao shrimp with peanuts and vegetables, served over noodles.',
        'https://example.com/kungpaoshrimp.jpg', 12.99, '2023-10-08 17:30:00', null),
       ('FOOD', 4, 'Beef and Broccoli', 'Beef and broccoli stir-fry with a savory brown sauce, served with rice.',
        'https://example.com/beefandbroccoli.jpg', 10.99, '2023-10-09 18:00:00', null),
       ('FOOD', 4, 'Egg Drop Soup', 'A comforting bowl of egg drop soup with silky egg ribbons in a flavorful broth.',
        'https://example.com/eggdropsoup.jpg', 4.99, '2023-10-10 18:30:00', null),
       ('FOOD', 4, 'Spring Rolls', 'Crispy spring rolls filled with vegetables and served with dipping sauce.',
        'https://example.com/springrolls.jpg', 6.99, '2023-10-11 19:00:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Orange Chicken
    (26, false, false, false, false, true, false),
    -- Kung Pao Shrimp
    (27, false, false, false, false, true, false),
    -- Beef and Broccoli
    (28, false, false, false, false, true, false),
    -- Egg Drop Soup
    (29, true, true, false, false, true, false),
    -- Spring Rolls
    (30, true, true, false, false, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (26, 4),  -- Orange Chicken is Chinese
       (26, 15), -- Orange Chicken is Fast Food
       (26, 6),  -- Orange Chicken is Chicken
       (27, 4),  -- Kung Pao Shrimp is Chinese
       (27, 15), -- Kung Pao Shrimp is Fast Food
       (27, 72), -- Kung Pao Shrimp is Vegan
       (28, 4),  -- Beef and Broccoli is Chinese
       (28, 15), -- Beef and Broccoli is Fast Food
       (28, 6),  -- Beef and Broccoli is Chicken
       (29, 4),  -- Egg Drop Soup is Chinese
       (29, 15), -- Egg Drop Soup is Fast Food
       (29, 72), -- Egg Drop Soup is Vegetarian
       (29, 73), -- Egg Drop Soup is Vegan
       (30, 4),  -- Spring Rolls is Chinese
       (30, 15), -- Spring Rolls is Fast Food
       (30, 72), -- Spring Rolls is Vegetarian
       (30, 73); -- Spring Rolls is Vegan

-- Adding 5 more rows with product_provider_type 5
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 5, 'Beef Tacos', 'Savory beef tacos with your choice of toppings and salsa.',
        'https://example.com/beeftacos.jpg', 8.99, '2023-10-12 12:00:00', null),
       ('FOOD', 5, 'Chicken Quesadilla', 'A cheesy chicken quesadilla with peppers and onions.',
        'https://example.com/chickenquesadilla.jpg', 7.99, '2023-10-13 12:30:00', null),
       ('FOOD', 5, 'Guacamole', 'Freshly made guacamole with ripe avocados, tomatoes, and spices.',
        'https://example.com/guacamole.jpg', 4.99, '2023-10-14 13:00:00', null),
       ('FOOD', 5, 'Churros', 'Delicious churros with cinnamon sugar, perfect for dessert.',
        'https://example.com/churros.jpg', 5.99, '2023-10-15 13:30:00', null),
       ('FOOD', 5, 'Salsa Verde', 'A tangy and spicy salsa verde made with tomatillos and chili peppers.',
        'https://example.com/salsaverde.jpg', 3.99, '2023-10-16 14:00:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Beef Tacos
    (31, false, false, false, false, true, false),
    -- Chicken Quesadilla
    (32, false, false, false, false, true, false),
    -- Guacamole
    (33, true, true, false, false, true, false),
    -- Churros
    (34, true, true, false, false, true, false),
    -- Salsa Verde
    (35, true, true, false, false, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (31, 10), -- Beef Tacos is Mexican
       (31, 15), -- Beef Tacos is Fast Food
       (31, 4),  -- Beef Tacos is Chinese (Chinese-Mexican fusion)
       (31, 6),  -- Beef Tacos is Chicken
       (32, 10), -- Chicken Quesadilla is Mexican
       (32, 15), -- Chicken Quesadilla is Fast Food
       (32, 4),  -- Chicken Quesadilla is Chinese (Chinese-Mexican fusion)
       (32, 6),  -- Chicken Quesadilla is Chicken
       (33, 10), -- Guacamole is Mexican
       (33, 15), -- Guacamole is Fast Food
       (33, 72), -- Guacamole is Vegetarian
       (33, 73), -- Guacamole is Vegan
       (34, 10), -- Churros is Mexican
       (34, 15), -- Churros is Fast Food
       (34, 72), -- Churros is Vegetarian
       (34, 73), -- Churros is Vegan
       (35, 10), -- Salsa Verde is Mexican
       (35, 15), -- Salsa Verde is Fast Food
       (35, 72), -- Salsa Verde is Vegetarian
       (35, 73); -- Salsa Verde is Vegan

-- Adding 5 more rows with product_provider_type 6
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 6, 'Croissant', 'A flaky and buttery croissant, perfect for breakfast.',
        'https://example.com/croissant.jpg', 2.99, '2023-10-17 14:30:00', null),
       ('FOOD', 6, 'Cappuccino', 'A frothy and rich cappuccino made with espresso and steamed milk.',
        'https://example.com/cappuccino.jpg', 4.99, '2023-10-18 15:00:00', null),
       ('FOOD', 6, 'Chocolate Eclair', 'A decadent chocolate eclair filled with cream.',
        'https://example.com/chocolateeclair.jpg', 3.99, '2023-10-19 15:30:00', null),
       ('FOOD', 6, 'Ham and Cheese Croissant', 'A savory ham and cheese croissant with a flaky crust.',
        'https://example.com/hamandcheesecroissant.jpg', 3.99, '2023-10-20 16:00:00', null),
       ('FOOD', 6, 'Macaron Assortment', 'A delightful assortment of colorful macarons in various flavors.',
        'https://example.com/macarons.jpg', 6.99, '2023-10-21 16:30:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Croissant
    (36, true, true, false, false, true, false),
    -- Cappuccino
    (37, true, true, false, false, true, false),
    -- Chocolate Eclair
    (38, true, true, false, false, true, false),
    -- Ham and Cheese Croissant
    (39, false, false, false, false, true, false),
    -- Macaron Assortment
    (40, true, true, false, false, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (36, 31), -- Croissant is Dessert
       (36, 38), -- Croissant is Bread
       (37, 48), -- Cappuccino is Coffee
       (37, 31), -- Cappuccino is Dessert
       (38, 31), -- Chocolate Eclair is Dessert
       (38, 38), -- Chocolate Eclair is Bread
       (39, 31), -- Ham and Cheese Croissant is Dessert
       (39, 38), -- Ham and Cheese Croissant is Bread
       (40, 31), -- Macaron Assortment is Dessert
       (40, 38); -- Macaron Assortment is Bread

-- Adding 5 new rows for product_provider_id 7 (Cafeteria-style)
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 7, 'Blueberry Muffin', 'A freshly baked blueberry muffin, a perfect morning treat in the cafeteria.',
        'https://example.com/blueberrymuffin.jpg', 2.49, '2023-10-27 19:00:00', null),
       ('FOOD', 7, 'Cinnamon Roll', 'A warm and gooey cinnamon roll, a delightful choice for a cafeteria breakfast.',
        'https://example.com/cinnamonroll.jpg', 3.99, '2023-10-27 19:30:00', null),
       ('FOOD', 7, 'Chocolate Cake',
        'A rich and decadent chocolate cake, perfect for satisfying your sweet tooth in the cafeteria.',
        'https://example.com/chocolatecake.jpg', 4.99, '2023-10-27 20:00:00', null),
       ('FOOD', 7, 'Fruit Salad',
        'A refreshing fruit salad with a mix of seasonal fruits, a healthy option in the cafeteria.',
        'https://example.com/fruitsalad.jpg', 3.49, '2023-10-27 20:30:00', null),
       ('FOOD', 7, 'Croissant', 'A buttery and flaky croissant, a classic choice for a quick bite in the cafeteria.',
        'https://example.com/croissant.jpg', 1.99, '2023-10-27 21:00:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Blueberry Muffin
    (41, true, true, false, false, true, false),
    -- Cinnamon Roll
    (42, true, true, false, false, true, false),
    -- Chocolate Cake
    (43, true, true, false, false, true, false),
    -- Fruit Salad
    (44, true, true, false, false, true, false),
    -- Croissant
    (45, true, true, false, false, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (41, 16), -- Blueberry Muffin is Bakery
       (41, 15), -- Blueberry Muffin is Fast Food
       (41, 1),  -- Blueberry Muffin is American
       (42, 16), -- Cinnamon Roll is Bakery
       (42, 15), -- Cinnamon Roll is Fast Food
       (42, 1),  -- Cinnamon Roll is American
       (43, 16), -- Chocolate Cake is Bakery
       (43, 15), -- Chocolate Cake is Fast Food
       (43, 1),  -- Chocolate Cake is American
       (44, 25), -- Fruit Salad is Salads
       (44, 15), -- Fruit Salad is Fast Food
       (44, 1),  -- Fruit Salad is American
       (44, 72), -- Fruit Salad is Vegetarian
       (44, 73), -- Fruit Salad is Vegan
       (45, 16), -- Croissant is Bakery
       (45, 15), -- Croissant is Fast Food
       (45, 1);  -- Croissant is American

-- Adding 5 more rows with product_provider_type 8
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 8, 'Spaghetti Carbonara', 'Creamy spaghetti carbonara with bacon and Parmesan cheese.',
        'https://example.com/spaghetticarbonara.jpg', 12.99, '2023-10-27 19:30:00', null),
       ('FOOD', 8, 'Margherita Pizza', 'A classic Margherita pizza with fresh tomatoes, mozzarella, and basil.',
        'https://example.com/margheritapizza.jpg', 11.99, '2023-10-28 20:00:00', null),
       ('FOOD', 8, 'Tiramisu', 'A delightful tiramisu dessert with layers of coffee-soaked ladyfingers and mascarpone.',
        'https://example.com/tiramisu.jpg', 7.99, '2023-10-29 20:30:00', null),
       ('FOOD', 8, 'Minestrone Soup', 'A hearty minestrone soup with vegetables, beans, and pasta.',
        'https://example.com/minestronesoup.jpg', 6.99, '2023-10-30 21:00:00', null),
       ('FOOD', 8, 'Chicken Alfredo', 'Creamy chicken Alfredo pasta with a rich Parmesan sauce.',
        'https://example.com/chickenalfredo.jpg', 9.99, '2023-10-31 21:30:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Spaghetti Carbonara
    (46, false, false, false, false, true, false),
    -- Margherita Pizza
    (47, true, true, false, false, true, false),
    -- Tiramisu
    (48, true, true, false, false, true, false),
    -- Minestrone Soup
    (49, true, true, false, false, true, false),
    -- Chicken Alfredo
    (50, false, false, false, false, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (46, 49), -- Spaghetti Carbonara is Pasta
       (46, 15), -- Spaghetti Carbonara is Fast Food
       (46, 1),  -- Spaghetti Carbonara is American
       (47, 18), -- Margherita Pizza is Pizza
       (47, 2),  -- Margherita Pizza is Italian
       (47, 15), -- Margherita Pizza is Fast Food
       (48, 31), -- Tiramisu is Dessert
       (48, 15), -- Tiramisu is Fast Food
       (48, 1),  -- Tiramisu is American
       (48, 72), -- Tiramisu is Vegetarian
       (48, 73), -- Tiramisu is Vegan
       (49, 49), -- Minestrone Soup is Pasta
       (49, 15), -- Minestrone Soup is Fast Food
       (49, 1),  -- Minestrone Soup is American
       (49, 72), -- Minestrone Soup is Vegetarian
       (49, 73), -- Minestrone Soup is Vegan
       (50, 49), -- Chicken Alfredo is Pasta
       (50, 15), -- Chicken Alfredo is Fast Food
       (50, 1),  -- Chicken Alfredo is American
       (50, 6);  -- Chicken Alfredo is Chicken

-- Adding 5 more rows with product_provider_type 9
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 9, 'Sushi Roll Combo', 'An assortment of delicious sushi rolls, perfect for sushi lovers.',
        'https://example.com/sushi.jpg', 14.99, '2023-11-01 22:00:00', null),
       ('FOOD', 9, 'Sashimi Platter', 'A fresh sashimi platter with a variety of raw fish slices.',
        'https://example.com/sashimi.jpg', 16.99, '2023-11-02 22:30:00', null),
       ('FOOD', 9, 'Tempura Shrimp', 'Crispy tempura shrimp served with dipping sauce.',
        'https://example.com/tempurashrimp.jpg', 10.99, '2023-11-03 23:00:00', null),
       ('FOOD', 9, 'Miso Soup', 'Traditional miso soup with tofu, seaweed, and green onions.',
        'https://example.com/misosoup.jpg', 4.99, '2023-11-04 23:30:00', null),
       ('FOOD', 9, 'Dragon Roll', 'A specialty dragon roll sushi with eel and avocado.',
        'https://example.com/dragonroll.jpg', 12.99, '2023-11-05 00:00:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Sushi Roll Combo
    (51, false, false, false, false, true, false),
    -- Sashimi Platter
    (52, false, false, false, false, true, false),
    -- Tempura Shrimp
    (53, false, false, false, false, true, false),
    -- Miso Soup
    (54, true, true, false, false, true, false),
    -- Dragon Roll
    (55, false, false, false, false, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (51, 5),  -- Sushi Roll Combo is Japanese
       (51, 15), -- Sushi Roll Combo is Fast Food
       (51, 72), -- Sushi Roll Combo is Vegetarian
       (51, 73), -- Sushi Roll Combo is Vegan
       (52, 5),  -- Sashimi Platter is Japanese
       (52, 15), -- Sashimi Platter is Fast Food
       (52, 72), -- Sashimi Platter is Vegetarian
       (52, 73), -- Sashimi Platter is Vegan
       (53, 5),  -- Tempura Shrimp is Japanese
       (53, 15), -- Tempura Shrimp is Fast Food
       (53, 72), -- Tempura Shrimp is Vegetarian
       (53, 73), -- Tempura Shrimp is Vegan
       (54, 5),  -- Miso Soup is Japanese
       (54, 15), -- Miso Soup is Fast Food
       (54, 72), -- Miso Soup is Vegetarian
       (54, 73), -- Miso Soup is Vegan
       (55, 5),  -- Dragon Roll is Japanese
       (55, 15), -- Dragon Roll is Fast Food
       (55, 72), -- Dragon Roll is Vegetarian
       (55, 73); -- Dragon Roll is Vegan

-- Adding 5 new rows for product_provider_id 10 (Cafeteria-style)
INSERT INTO public.product (product_type_name, product_provider_id, name, description, image, price, pickup_time,
                            deleted_at)
VALUES ('FOOD', 10, 'Baguette', 'A fresh and crusty baguette, perfect for sandwiches and snacks in the cafeteria.',
        'https://example.com/baguette.jpg', 2.99, '2023-11-01 22:00:00', null),
       ('FOOD', 10, 'Fruit Tart',
        'A delightful fruit tart with a buttery pastry crust and a mix of fresh fruits, a cafeteria favorite.',
        'https://example.com/fruittart.jpg', 4.49, '2023-11-01 22:30:00', null),
       ('FOOD', 10, 'Lemonade', 'A refreshing glass of homemade lemonade, a perfect beverage choice in the cafeteria.',
        'https://example.com/lemonade.jpg', 1.99, '2023-11-01 23:00:00', null),
       ('FOOD', 10, 'Club Sandwich',
        'A classic club sandwich with turkey, bacon, lettuce, and tomato, a popular cafeteria option.',
        'https://example.com/clubsandwich.jpg', 7.99, '2023-11-01 23:30:00', null),
       ('FOOD', 10, 'Chocolate Eclair',
        'A delicious chocolate eclair filled with cream, a sweet treat in the cafeteria.',
        'https://example.com/chocolateeclair.jpg', 3.99, '2023-11-02 00:00:00', null);

INSERT INTO public.food (product_id, vegetarian, vegan, gluten_free, dairy_free, nut_free, organic)
VALUES
    -- Baguette
    (56, true, true, false, false, true, false),
    -- Fruit Tart
    (57, true, true, false, false, true, false),
    -- Lemonade
    (58, true, true, false, false, true, false),
    -- Club Sandwich
    (59, false, false, false, false, true, false),
    -- Chocolate Eclair
    (60, true, true, false, false, true, false);

INSERT INTO public.food_food_category (food_id, food_category_id)
VALUES (56, 22), -- Baguette is Sandwiches
       (56, 15), -- Baguette is Fast Food
       (56, 1),  -- Baguette is American
       (57, 31), -- Fruit Tart is Dessert
       (57, 15), -- Fruit Tart is Fast Food
       (57, 1),  -- Fruit Tart is American
       (58, 15), -- Lemonade is Fast Food
       (58, 1),  -- Lemonade is American
       (59, 22), -- Club Sandwich is Sandwiches
       (59, 15), -- Club Sandwich is Fast Food
       (59, 1),  -- Club Sandwich is American
       (60, 31), -- Chocolate Eclair is Dessert
       (60, 15), -- Chocolate Eclair is Fast Food
       (60, 1);  -- Chocolate Eclair is American




