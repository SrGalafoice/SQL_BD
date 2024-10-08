CREATE DATABASE Livraria
GO
USE Livraria 
GO

CREATE TABLE Livro(
	Codigo INT NOT NULL IDENTITY(100001, 100),
	Nome VARCHAR(200) NOT NULL UNIQUE CHECK (Len(Nome) > 3),
	Lingua VARCHAR(10) NOT NULL DEFAULT('PT-BR'),
	Ano INT NOT NULL CHECK(Ano > 1990)
	PRIMARY KEY (Codigo)
)
GO

CREATE TABLE Editora(
ID_Editora INT NOT NULL IDENTITY(491,16),
Nome VARCHAR(70) NOT NULL UNIQUE CHECK (Len(Nome) > 3),
Telefone VARCHAR(11) NOT NULL CHECK(Len(Telefone) >= 10),
Logradouro_Endereco VARCHAR(200) NOT NULL,
Numero_Endereco INT NOT NULL CHECK (Numero_Endereco > 0),
CEP_Endereco char(8) NOT NULL CHECK (len(CEP_Endereco) = 10),
Complemento_Endereco VARCHAR(255) NOT NULL

PRIMARY KEY(ID_Editora)
)
GO

CREATE TABLE Autor(
ID_Autor INT NOT NULL IDENTITY(2351, 1),
Nome VARCHAR(100) NOT NULL UNIQUE CHECK(len(nome) > 3),
Data_Nasc date NOT NULL,
Pais_Nasc VARCHAR(50) NOT NULL CHECK(Pais_Nasc = 'Brasil' OR Pais_Nasc =  'Estados Unidos' OR Pais_Nasc = 'Inglaterra' OR Pais_Nasc = 'Alemanha'),
Biografia VARCHAR(255) NOT NULL

PRIMARY KEY (ID_Autor)
)
GO

CREATE TABLE Edicao(
ISBN char(13) NOT NULL,
Preco decimal(4,2) NOT NULL CHECK (Preco > 0),
Ano INT NOT NULL CHECK(Ano > 1993),
Numero_Paginas INT NOT NULL CHECK(Numero_Paginas > 15),
Qtd_Estoque INT NOT NULL,

PRIMARY KEY (ISBN)
)
GO

CREATE TABLE Livro_Autor(
LivroCodigo INT NOT NULL,
AutorID_Autor INT NOT NULL

PRIMARY KEY(LivroCodigo, AutorID_Autor)

FOREIGN KEY(LivroCodigo) REFERENCES Livro(Codigo),
FOREIGN KEY (AutorID_Autor) REFERENCES Autor(ID_Autor)
)
GO

CREATE TABLE Editora_Edicao_Livro(
LivroCodigo INT NOT NULL,
EdicaoISBN char(13) NOT NULL,
EditoraID_Editora INT NOT NULL

PRIMARY KEY(LivroCodigo, EdicaoISBN, EditoraID_Editora),

FOREIGN KEY(LivroCodigo) REFERENCES Livro(Codigo),
FOREIGN KEY (EdicaoISBN) REFERENCES Edicao(ISBN),
FOREIGN KEY (EditoraID_Editora) REFERENCES Editora(ID_Editora)
)
