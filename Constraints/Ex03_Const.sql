CREATE DATABASE Maternidade
GO
USE Maternidade

CREATE TABLE Mae (
ID_Mae		INT				NOT NULL 	IDENTITY(1001,1),
nome		VARCHAR(60)		NOT NULL,
logradouro_endereco	VARCHAR(100)	NOT NULL,
numero_endereco		INT				NOT NULL 	CHECK(numero_endereco > 0),
cep_endereco			CHAR(8)			NOT NULL 	CHECK(LEN(cep_endereco) = 8),
complemento_endereco	VARCHAR(200)	NOT NULL,
telefone	CHAR(10)		NOT NULL 	CHECK(LEN(telefone) = 10),
data_nasc	DATE			NOT NULL
PRIMARY KEY (ID_Mae)
)
GO

CREATE TABLE Medico (
CRM_numero		INT				NOT NULL,
CRM_UF			CHAR(2)			NOT NULL,
nome			VARCHAR(60)		NOT NULL,
celular			CHAR(11)		NOT NULL 	UNIQUE 	CHECK(LEN(celular) = 11),
especialidade	VARCHAR(30)		NOT NULL
PRIMARY KEY (CRM_numero, CRM_UF)
)
GO

CREATE TABLE Bebe(
ID_Bebe	INT	NOT NULL		IDENTITY,
nome	VARCHAR(60)	NOT NULL,
data_nasc		DATE	NOT NULL		DEFAULT(GETDATE()),
altura	DECIMAL(7,2)	NOT NULL 	CHECK(altura  > 0),
peso	DECIMAL(4,3)	NOT NULL		CHECK(peso > 0),
ID_Mae	INT	NOT NULL
PRIMARY KEY (ID_Bebe)
FOREIGN KEY (ID_Mae) REFERENCES Mae(ID_Mae)
)
GO

/* Para a tabela mãe:
- O ID é auto incremental, iniciando em 1001 e indo de 1 em 1
- Número de porta não pode ser negativo
- CEP deve ter 8 dígitos
- Telefone deve ter 10 dígitos
Para a tabela medico:
- Celular deve ter 11 dígitos
- Celular não pode repetir
Para a tabela bebe:
- O ID é auto incremental, iniciando em 1 e indo de 1 em 1
- Se não preenchida, a data de nascimento é o dia de hoje
- Altura não pode ser negativa
- Peso não pode ser negativo
*/
