-- init-db.sql
-- Script d'initialisation de la base de données BarApp

-- Create database if not exists (already handled by docker-compose)
-- CREATE DATABASE IF NOT EXISTS barapp;

-- Use the database
\c barapp;

-- Insert default admin user (password: Admin123!)
INSERT INTO "user" (email, password, role)
VALUES ('admin@barapp.com', '$2a$12$rq.8cZV2B.XgN8Ut0nB8ouHZXZ.e1r7y1gJz8Z.YoQQyQz9Q8w9Ge', 'ROLE_ADMIN')
ON CONFLICT (email) DO NOTHING;

-- Insert default barmaker (password: Barmaker123!)
INSERT INTO "user" (email, password, role)
VALUES ('barmaker@barapp.com', '$2a$12$mX9.5tZB.XgN8Ut0nB8ouHZXZ.e1r7y1gJz8Z.YoQQyQz9Q8w9Ge', 'ROLE_BARMAKER')
ON CONFLICT (email) DO NOTHING;

-- Insert test user (password: Test123!)
INSERT INTO "user" (email, password, role)
VALUES ('test@barapp.com', '$2a$12$kY8.3vAB.XgN8Ut0nB8ouHZXZ.e1r7y1gJz8Z.YoQQyQz9Q8w9Ge', 'ROLE_USER')
ON CONFLICT (email) DO NOTHING;

-- Insert categories
INSERT INTO category (name, parent_id) VALUES
                                           ('Cocktails Classiques', NULL),
                                           ('Cocktails Modernes', NULL),
                                           ('Shots', NULL),
                                           ('Sans Alcool', NULL)
ON CONFLICT DO NOTHING;

-- Insert subcategories
INSERT INTO category (name, parent_id) VALUES
                                           ('Base Rhum', 1),
                                           ('Base Vodka', 1),
                                           ('Base Gin', 1),
                                           ('Base Whisky', 1),
                                           ('Cocktails Fruités', 2),
                                           ('Cocktails Épicés', 2)
ON CONFLICT DO NOTHING;

-- Insert ingredients
INSERT INTO ingredient (name, price, is_available) VALUES
    ('Rhum blanc', 300, true),
    ('Rhum ambré', 350, true),
    ('Vodka', 250, true),
    ('Gin', 300, true),
    ('Whisky', 400, true),
    ('Citron vert', 50, true),
    ('Citron jaune', 50, true),
    ('Orange', 60, true),
    ('Menthe fraîche', 30, true),
    ('Sucre de canne', 20, true),
    ('Sirop de sucre', 40, true),
    ('Angostura', 80, true),
    ('Eau gazeuse', 30, true),
    ('Tonic', 40, true),
    ('Coca Cola', 35, true),
    ('Jus d''orange', 60, true),
    ('Jus de cranberry', 55, true),
    ('Grenadine', 45, true)
ON CONFLICT DO NOTHING;
-- Insert cocktails
INSERT INTO cocktail (name, description, price, is_visible, is_available, is_discount, discount_price, category_id) VALUES
('Mojito', 'Cocktail cubain à base de rhum, menthe et citron vert', 950, true, true, false, NULL, 5),
('Caipirinha', 'Cocktail brésilien à base de cachaça et citron vert', 850, true, true, false, NULL, 5),
('Piña Colada', 'Cocktail tropical à base de rhum, coco et ananas', 1200, true, true, true, 1000, 5),
('Vodka Tonic', 'Classique vodka avec tonic et citron', 750, true, true, false, NULL, 6),
('Gin Tonic', 'Gin premium avec tonic et citron', 800, true, true, false, NULL, 7),
('Whisky Sour', 'Whisky avec citron et sucre', 1100, true, true, false, NULL, 8),
('Cosmopolitan', 'Vodka, cranberry et citron vert', 950, true, true, false, NULL, 2),
('Sex on the Beach', 'Cocktail fruité multicolore', 1050, true, true, true, 900, 2),
('Bloody Mary', 'Vodka épicée avec jus de tomate', 900, true, true, false, NULL, 6),
('Virgin Mojito', 'Mojito sans alcool', 650, true, true, false, NULL, 4)
ON CONFLICT DO NOTHING;

-- Insert recipes
INSERT INTO recipe (name, description, cocktail_id) VALUES
('Mojito Classic', 'La recette traditionnelle du mojito', 1),
('Caipirinha Traditional', 'Recette authentique brésilienne', 2),
('Piña Colada Tropical', 'Version crémeuse et tropicale', 3),
('Vodka Tonic Simple', 'Recette simple et rafraîchissante', 4),
('Gin Tonic Premium', 'Version premium avec botaniques', 5),
('Whisky Sour Classic', 'Recette classique équilibrée', 6),
('Cosmopolitan Chic', 'Version élégante et colorée', 7),
('Sex on the Beach Fun', 'Cocktail festif et coloré', 8),
('Bloody Mary Spicy', 'Version épicée et savoureuse', 9),
('Virgin Mojito Fresh', 'Version sans alcool rafraîchissante', 10)
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Mojito)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(1, 1, 6), -- Rhum blanc 6cl
(1, 6, 1), -- Citron vert 1 unité
(1, 9, 10), -- Menthe fraîche 10 feuilles
(1, 10, 2), -- Sucre de canne 2 cuillères
(1, 13, 10) -- Eau gazeuse 10cl
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Caipirinha)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(2, 1, 6), -- Rhum blanc 6cl (remplace cachaça)
(2, 6, 1), -- Citron vert 1 unité
(2, 10, 2) -- Sucre de canne 2 cuillères
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Piña Colada)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(3, 1, 6), -- Rhum blanc 6cl
(3, 16, 10), -- Jus d'orange 10cl (remplace jus d'ananas)
(3, 11, 2) -- Sirop de sucre 2cl (remplace crème de coco)
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Vodka Tonic)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(4, 3, 4), -- Vodka 4cl
(4, 14, 12), -- Tonic 12cl
(4, 7, 1) -- Citron jaune 1 tranche
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Gin Tonic)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(5, 4, 4), -- Gin 4cl
(5, 14, 12), -- Tonic 12cl
(5, 7, 1) -- Citron jaune 1 tranche
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Whisky Sour)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(6, 5, 6), -- Whisky 6cl
(6, 7, 1), -- Citron jaune 1 unité
(6, 11, 2), -- Sirop de sucre 2cl
(6, 12, 2) -- Angostura 2 traits
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Cosmopolitan)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(7, 3, 4), -- Vodka 4cl
(7, 17, 6), -- Jus de cranberry 6cl
(7, 6, 1), -- Citron vert 1 trait
(7, 11, 1) -- Sirop de sucre 1cl
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Sex on the Beach)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(8, 3, 2), -- Vodka 2cl
(8, 1, 2), -- Rhum blanc 2cl
(8, 16, 8), -- Jus d'orange 8cl
(8, 17, 4), -- Jus de cranberry 4cl
(8, 18, 1) -- Grenadine 1cl
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Bloody Mary)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(9, 3, 4), -- Vodka 4cl
(9, 17, 10), -- Jus de cranberry 10cl (remplace jus de tomate)
(9, 7, 1), -- Citron jaune 1 trait
(9, 12, 3) -- Angostura 3 traits (épices)
ON CONFLICT DO NOTHING;

-- Insert recipe ingredients (Virgin Mojito)
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity) VALUES
(10, 6, 1), -- Citron vert 1 unité
(10, 9, 10), -- Menthe fraîche 10 feuilles
(10, 10, 2), -- Sucre de canne 2 cuillères
(10, 13, 15) -- Eau gazeuse 15cl
ON CONFLICT DO NOTHING;

-- Insert different size and price for cocktails
-- Mojito (id = 1)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(1, 'S', 750),
(1, 'M', 950),
(1, 'L', 1150);

-- Caipirinha (id = 2)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(2, 'S', 650),
(2, 'M', 850),
(2, 'L', 1050);

-- Piña Colada (id = 3)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(3, 'S', 900),
(3, 'M', 1200),
(3, 'L', 1450);

-- Vodka Tonic (id = 4)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(4, 'S', 600),
(4, 'M', 750),
(4, 'L', 900);

-- Gin Tonic (id = 5)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(5, 'S', 650),
(5, 'M', 800),
(5, 'L', 950);

-- Whisky Sour (id = 6)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(6, 'S', 900),
(6, 'M', 1100),
(6, 'L', 1300);

-- Cosmopolitan (id = 7)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(7, 'S', 750),
(7, 'M', 950),
(7, 'L', 1150);

-- Sex on the Beach (id = 8)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(8, 'S', 850),
(8, 'M', 1050),
(8, 'L', 1250);

-- Bloody Mary (id = 9)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(9, 'S', 700),
(9, 'M', 900),
(9, 'L', 1100);

-- Virgin Mojito (id = 10)
INSERT INTO cocktail_size (cocktail_id, size, price) VALUES
(10, 'S', 500),
(10, 'M', 650),
(10, 'L', 800);
