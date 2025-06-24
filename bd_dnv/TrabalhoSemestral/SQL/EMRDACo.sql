CREATE DATABASE mercado;
GO
USE mercado;
GO
-- Tabela Empresa
CREATE TABLE Empresa (
   CNPJ CHAR(11) PRIMARY KEY,
   nomeEmpresa VARCHAR(255),
   CEP INT NOT NULL,
    Logradouro VARCHAR(255) NOT NULL,
    NumeroLogradouro INT NOT NULL,
    ComplementoLogradouro VARCHAR(255),
    CidadeLogradouro VARCHAR(255) NOT NULL,
    BairroLogradouro VARCHAR(255) NOT NULL
);
-- Tabela Email
CREATE TABLE Email (
   Endereco_email VARCHAR(255) PRIMARY KEY,
   EmpresaCNPJ CHAR(11),
   FOREIGN KEY (EmpresaCNPJ) REFERENCES Empresa(CNPJ)
);
-- Tabela Telefone
CREATE TABLE Telefone (
   Numero_telefone CHAR(11) PRIMARY KEY,
   EmpresaCNPJ CHAR(11),
   FOREIGN KEY (EmpresaCNPJ) REFERENCES Empresa(CNPJ)
);

-- Tabela Produto
CREATE TABLE Produto (
   ID INT PRIMARY KEY,
   valorUnitario DECIMAL(7,2),
   nome VARCHAR(255)
);
-- Tabela Entrada
CREATE TABLE Entrada (
   ID INT PRIMARY KEY IDENTITY (1001, 1),
   valorTotal DECIMAL(7,2),
   data DATE,
   quantidade INT,
   valorUnitarioEntrada DECIMAL(7,2)
);
-- Tabela Produto_entrada (relacionamento N:N entre Produto e Entrada)
CREATE TABLE Produto_entrada (
   ProdutoID INT,
   entradaID INT,
   PRIMARY KEY (ProdutoID, entradaID),
   FOREIGN KEY (ProdutoID) REFERENCES Produto(ID),
   FOREIGN KEY (entradaID) REFERENCES Entrada(ID)
);
-- Tabela Venda
CREATE TABLE Venda (
   ID INT PRIMARY KEY,
   data DATE,
   quantidadeProduto INT,
   valorUnitarioSaida DECIMAL(7,2),
   ProdutoID INT,
   EmpresaCNPJ CHAR(11),
   FOREIGN KEY (ProdutoID) REFERENCES Produto(ID),
   FOREIGN KEY (EmpresaCNPJ) REFERENCES Empresa(CNPJ)
);
-- Tabela Produto_Venda (relacionamento N:N entre Produto e Venda)
CREATE TABLE Produto_Venda (
   ProdutoID INT,
   VendaID INT,
   PRIMARY KEY (ProdutoID, VendaID),
   FOREIGN KEY (ProdutoID) REFERENCES Produto(ID),
   FOREIGN KEY (VendaID) REFERENCES Venda(ID)
);
-- Tabela Prateleira
CREATE TABLE Prateleira (
   ID INT PRIMARY KEY,
   Corredor INT
);
-- Tabela Produto_Prateleira (relacionamento N:N entre Produto e Prateleira)
CREATE TABLE Produto_Prateleira (
   quantidade INT,
   ProdutoID INT,
   prateleiraID INT,
   PRIMARY KEY (ProdutoID, prateleiraID),
   FOREIGN KEY (ProdutoID) REFERENCES Produto(ID),
   FOREIGN KEY (prateleiraID) REFERENCES Prateleira(ID)
);

INSERT INTO Prateleira VALUES(10, 1),(9,1),(8,1),(7,1),(6,1),(5,1),(4,1),(3,1),(2,1),(1,1)

SELECT * FROM Entrada
SELECT * FROM Produto_Prateleira
SELECT * FROM Produto_Entrada
SELECT * FROM Produto
SELECT * FROM Empresa
SELECT * FROM Email
SELECT * FROM Venda
SELECT * FROM Produto_Venda
SELECT * FROM Telefone

