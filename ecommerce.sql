CREATE DATABASE ecommerce;
USE ecommerce;

CREATE TABLE Produtos(
Id INT PRIMARY KEY,
Nome VARCHAR(40),
Preco DOUBLE,
Quantidade INT); 

CREATE TABLE Carrinho(
Cod INT PRIMARY KEY auto_increment,
Quantidade INT,
IdProd INT,
constraint fk_ProdCar foreign key (IdProd) references Produtos (Id));

INSERT INTO Produtos(Id, Nome, Preco, Quantidade)
VALUES
    (1, 'Camiseta', 29.90, 10),
    (2, 'Calça Jeans', 89.90, 5),
    (3, 'Tênis Esportivo', 159.90, 7),
    (4, 'Relógio de Pulso', 199.90, 3),
    (5, 'Perfume Feminino', 129.90, 8),
    (6, 'Smartphone', 999.90, 2),
    (7, 'Fones de Ouvido Bluetooth', 79.90, 6),
    (8, 'Mochila', 69.90, 4);

