-- \c restaurant_db

-- Créer les enums
CREATE TYPE category_enum AS ENUM ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
CREATE TYPE dish_type_enum AS ENUM ('START', 'MAIN', 'DESSERT');
CREATE TYPE movement_type_enum AS ENUM ('IN', 'OUT');
CREATE TYPE unit_enum AS ENUM ('KG', 'L', 'PCS');

-- Table ingredient
CREATE TABLE ingredient (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10,2),
    category category_enum
);

-- Table dish
CREATE TABLE dish (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dish_type dish_type_enum,
    selling_price NUMERIC(10,2)
);

-- Table dish_ingredient (jointure ManyToMany)
CREATE TABLE dish_ingredient (
    id SERIAL PRIMARY KEY,
    id_ingredient INTEGER REFERENCES ingredient(id),
    id_dish INTEGER REFERENCES dish(id),
    required_quantity NUMERIC(10,2),
    unit unit_enum
);

-- Table stock_movement
CREATE TABLE stock_movement (
    id SERIAL PRIMARY KEY,
    id_ingredient INTEGER REFERENCES ingredient(id),
    quantity NUMERIC(10,2),
    type movement_type_enum,
    unit unit_enum,
    creation_datetime TIMESTAMP
);