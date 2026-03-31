-- Insérer des ingrédients
INSERT INTO ingredient (name, price, category) VALUES
('Laitue', 800.00, 'VEGETABLE'),
('Tomate', 600.00, 'VEGETABLE'),
('Poulet', 4500.00, 'ANIMAL'),
('Chocolat', 3000.00, 'OTHER'),
('Beurre', 2500.00, 'DAIRY');

-- Insérer des plats
INSERT INTO dish (name, dish_type, selling_price) VALUES
('Salade fraîche', 'START', 3500.00),
('Poulet grillé', 'MAIN', 12000.00),
('Riz aux légumes', 'MAIN', NULL),
('Gâteau au chocolat', 'DESSERT', 8000.00),
('Salade de fruits', 'DESSERT', NULL);