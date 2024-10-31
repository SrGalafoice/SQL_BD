CREATE DATABASE mecanica
GO
USE mecanica

CREATE TABLE Cliente(
ID	INT	NOT NULL		IDENTITY(3401, 15),
Nome	VARCHAR(100) 	NOT NULL,
Logradouro VARCHAR(200)	NOT NULL,
Numero	INT	NOT NULL	CHECK(Numero > 0),
CEP	CHAR(8)	NOT NULL	CHECK(LEN(CEP) = 8),
Complemento 	VARCHAR(255)	NOT NULL
PRIMARY KEY (ID)
)
GO

CREATE TABLE Telefone_Cliente(
ClienteID	INT	NOT NULL,
Telefone VARCHAR(11)	NOT NULL	CHECK(LEN(Telefone) >= 10)
PRIMARY KEY (Telefone),
FOREIGN KEY (ClienteID) REFERENCES Cliente(ID)
)
GO


CREATE TABLE Peca(
ID	INT	NOT NULL		IDENTITY(3411, 7),
Nome	VARCHAR(30)	NOT NULL	UNIQUE,
Preco	DECIMAL(4,2)	NOT NULL	CHECK(Preco > 0),
Estoque	INT	NOT NULL	CHECK(Estoque >= 10)
PRIMARY KEY (ID)
)
GO

CREATE TABLE Categoria(
ID	INT	NOT NULL	IDENTITY,
Categoria	VARCHAR(10)	NOT NULL CHECK(Categoria IN ('Estagiario', 'Nível 1', 'Nível 2', 'Nível 3')),
Valor_Hora	DECIMAL(4,2)	NOT NULL	CHECK(Valor_Hora > 0)
PRIMARY KEY (ID),
CONSTRAINT chk_Cat_Val
	CHECK((Categoria = 'Estagiario' AND Valor_Hora = 15.00) OR 
	(Categoria = 'Nível 1' AND Valor_Hora = 25.00) OR 
	(Categoria = 'Nível 2' AND Valor_Hora = 35.00) OR 
	(Categoria = 'Nível 3' AND Valor_Hora = 50.00))
)
GO

CREATE TABLE Funcionario(
ID	INT	NOT NULL		IDENTITY(101, 1),
Nome	VARCHAR(100)	NOT NULL,
Logradouro	VARCHAR(200)	NOT NULL,
Numero	INT	NOT NULL	CHECK(Numero > 0),
Telefone	CHAR(11)	NOT NULL	CHECK(LEN(Telefone) >= 10),
Categoria_Habilitacao	VARCHAR(2)	NOT NULL	CHECK(Categoria_Habilitacao IN ('A', 'B', 'C', 'D', 'E')),
CategoriaID	INT	NOT NULL
PRIMARY KEY (ID)
FOREIGN KEY (CategoriaID) REFERENCES Categoria(ID)
)
GO

CREATE TABLE Veiculo (
Placa	CHAR(7)	NOT NULL	CHECK(LEN(Placa) = 7),
Marca	VARCHAR(30)	NOT NULL,
Modelo	VARCHAR(30)	NOT NULL,
Cor	VARCHAR(15)	NOT NULL,
Ano_Fabricacao	INT	NOT NULL	CHECK(Ano_Fabricacao > 1997),
Ano_Modelo	INT	NOT NULL	CHECK(Ano_Modelo > 1997),
Data_Aquisicao	DATE	NOT NULL,
ClienteID	INT	NOT NULL
PRIMARY KEY (Placa),
FOREIGN KEY (ClienteID) REFERENCES Cliente(ID),
CONSTRAINT chk_mod_fab
	CHECK(Ano_Modelo = Ano_Fabricacao OR Ano_Modelo = Ano_Fabricacao + 1)
)
GO

CREATE TABLE Reparo(
Data	DATE	NOT NULL	DEFAULT(GetDate()),
Custo_Total	DECIMAL(4, 2)	NOT NULL	CHECK(Custo_Total > 0),
Tempo	INT	NOT NULL	CHECK(Tempo > 0),
VeiculoPlaca	CHAR(7)	NOT NULL,
FuncionarioID	INT	NOT NULL,
PecaID	INT	NOT NULL,
PRIMARY KEY (Data),
FOREIGN KEY (VeiculoPlaca) REFERENCES Veiculo(Placa),
FOREIGN KEY (FuncionarioID) REFERENCES Funcionario(ID),
FOREIGN KEY (PecaID) REFERENCES Peca(ID)
)
GO

/* Foram delimitadas as seguintes restrições:
- O ID do cliente é auto incremental partindo de 3401 e incrementando de 15 em 15
- O ID do funcionário é auto incremental partindo de 101 e incrementando de 1 em 1
- O ID da peça é auto incremental partindo de 3411 e incrementando de 7 em 7
- O ID da categoria é auto incremental partindo de 1 e incrementando de 1 em 1
- Nenhum número de endereço é negativo
- Todos os CEP de endereços devem ter 8 dígitos
- Se não preenchida, a data do reparo deve ser a data de hoje
- A oficina só trabalha com carros de modelo superior a 1997
- A oficina só trabalha com carros de fabricação superior a 1997
- O ano do modelo deve ser igual ou 1 ano superior ao ano de fabricação
- Não existem preços, custos, tempo ou valores negativos
- Telefones podem ser fixos ou celulares, tendo 10 ou 11 dígitos
- Categorias de Habilitação devem ser A, B, C, D ou E
- Todas as peças devem ter, ao menos, 10 unidades em estoque
- No Brasil, toda placa de veículo tem 7 caracteres
- Cada peça tem nome diferente
- As categorias de funcionários são estagiário, Nível 1, Nível 2 ou Nível 3 e seguem os valores:
- Estagiário: Mais de R$15,00 / hora
- Nível 1: Mais de R$25,00 / hora
- Nível 2: Mais de R$35,00 / hora
- Nível 3: Mais de R$50,00 / hora
*/
