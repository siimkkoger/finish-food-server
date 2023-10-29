-- dummy data for product table (CLOTHES 1-10, PRODUCTS 11-20)
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