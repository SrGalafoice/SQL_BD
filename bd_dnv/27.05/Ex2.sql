CREATE DATABASE Locadora;
GO

USE Locadora;
GO

CREATE TABLE Filme (
    id INT PRIMARY KEY,
    titulo VARCHAR(40) NOT NULL,
    ano INT CHECK (ano <= 2021)
);

CREATE TABLE Estrela (
    id INT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL
);

CREATE TABLE Filme_Estrela (
    FilmeId INT,
    EstrelaId INT,
    PRIMARY KEY (FilmeId, EstrelaId),
    FOREIGN KEY (FilmeId) REFERENCES Filme(id),
    FOREIGN KEY (EstrelaId) REFERENCES Estrela(id)
);

CREATE TABLE DVD (
    num INT PRIMARY KEY,
    data_fabricacao DATE CHECK (data_fabricacao < GETDATE()),
    FilmeId INT,
    FOREIGN KEY (FilmeId) REFERENCES Filme(id)
);

CREATE TABLE Cliente (
    num_cadastro INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    logradouro VARCHAR(100) NOT NULL,
    num INT CHECK (num > 0),
    cep CHAR(8) CHECK (LEN(cep) = 8)
);

CREATE TABLE Locacao (
    DVDnum INT,
    ClienteNum_Cadastro INT,
    data_locacao DATE DEFAULT CAST(GETDATE() AS DATE),
    data_devolucao DATE,
    valor DECIMAL(7, 2) CHECK (valor > 0),
    PRIMARY KEY (DVDnum, ClienteNum_Cadastro, data_locacao),
    FOREIGN KEY (DVDnum) REFERENCES DVD(num),
    FOREIGN KEY (ClienteNum_Cadastro) REFERENCES Cliente(num_cadastro),
    CHECK (data_devolucao > data_locacao)
);

--A entidade estrela deveria ter o nome real da estrela, com 50 caracteres
ALTER TABLE Estrela
ADD nome_real VARCHAR(50) 

--Verificando um dos nomes de filme, percebeu-se que o nome do filme deveria ser um atributo
--com 80 caracteres
ALTER TABLE Filme
ALTER COLUMN titulo VARCHAR(80) NOT NULL

INSERT INTO Filme VALUES
(1001, 'Whiplash', 2015),
(1002, 'Birdman', 2015),
(1003, 'Interestelar', 2014),
(1004, 'A Culpa é das estrelas', 2014),
(1005, 'Alexandre e o Dia Terrível, Horrível, Espantoso e Horroroso', 2014),
(1006, 'Sing', 2016);

INSERT INTO Estrela VALUES
(9901, 'Michael Keaton', 'Michael John Douglas'),
(9902, 'Emma Stone', 'Emily Jean Stone'),
(9903, 'Miles Teller', NULL),
(9904, 'Steve Carell','Steven John Carell'),
(9905, 'Jennifer Garner','Jennifer Anne Garner');

INSERT INTO Filme_Estrela VALUES
(1002, 9901),
(1002, 9902),
(1001, 9903),
(1005, 9904),
(1005, 9905);

INSERT INTO DVD VALUES
(10001, '2020-12-02', 1001),
(10002, '2019-10-18', 1002),
(10003, '2020-04-03', 1003),
(10004, '2020-12-02', 1001),
(10005, '2019-10-18', 1004),
(10006, '2020-04-03', 1002),
(10007, '2020-12-02', 1005),
(10008, '2019-10-18', 1002),
(10009, '2020-04-03', 1003);


INSERT INTO Cliente (num_cadastro, nome, logradouro, num, cep) VALUES
(5501, 'Matilde Luz', 'Rua Síria', 150, '03086040'),
(5502, 'Carlos Carreiro', 'Rua Bartolomeu Aires', 1250, '04419110'),
(5503, 'Daniel Ramalho', 'Rua Itajutiba', 169, NULL),
(5504, 'Roberta Bento', 'Rua Jayme Von Rosenburg', 36, NULL),
(5505, 'Rosa Cerqueira', 'Rua Arnaldo Simões Pinto', 235, '02917110');

INSERT INTO Locacao (DVDnum, ClienteNum_Cadastro, data_locacao, data_devolucao, valor) VALUES
(10001, 5502, '2021-02-18', '2021-02-21', 3.50),
(10009, 5502, '2021-02-18', '2021-02-21', 3.50),
(10002, 5503, '2021-02-18', '2021-02-19', 3.50),
(10002, 5505, '2021-02-20', '2021-02-23', 3.00),
(10004, 5505, '2021-02-20', '2021-02-23', 3.00),
(10005, 5505, '2021-02-20', '2021-02-23', 3.00),
(10001, 5501, '2021-02-24', '2021-02-26', 3.50),
(10008, 5501, '2021-02-24', '2021-02-26', 3.50);

-- Os CEP dos clientes 5503 e 5504 são 08411150 e 02918190 respectivamente
UPDATE Cliente 
SET CEP = '08411150'
WHERE num_cadastro = 5503

UPDATE Cliente 
SET CEP = '02918190'
WHERE num_cadastro = 5503

--A locação de 2021-02-18 do cliente 5502 teve o valor de 3.25 para cada DVD alugado
UPDATE Locacao 
SET valor = 3.25
WHERE ClienteNum_Cadastro = 5502 AND data_locacao = '2021-02-18'

--A locação de 2021-02-24 do cliente 5501 teve o valor de 3.10 para cada DVD alugado
UPDATE Locacao 
SET valor = 3.10
WHERE ClienteNum_Cadastro = 5501 AND data_locacao = '2021-02-24'

--O DVD 10005 foi fabricado em 2019-07-14
UPDATE DVD
SET data_fabricacao = '2019-07-14'
WHERE num = 10005

--O nome real de Miles Teller é Miles Alexander Teller
UPDATE Estrela
SET nome_real = 'Miles Alexander Teller'
WHERE nome = 'Miles Teller'

--O filme Sing não tem DVD cadastrado e deve ser excluído
DELETE FROM Filme
WHERE titulo = 'Sing'

--Consultar num_cadastro do cliente, nome do cliente, data_locacao (Formato
--dd/mm/aaaa), Qtd_dias_alugado (total de dias que o filme ficou alugado), titulo do
--filme, ano do filme da locação do cliente cujo nome inicia com Matilde
SELECT cl.num_cadastro, cl.nome, 
CONVERT(CHAR(10), loc.data_locacao, 103) AS Data_locacao, 
DATEDIFF(DAY, loc.data_locacao, loc.data_devolucao) AS Qtd_Dias_alugado,
fi.titulo, fi.ano 
FROM Cliente cl INNER JOIN Locacao loc 
ON cl.num_cadastro = loc.ClienteNum_Cadastro INNER JOIN DVD
ON loc.DVDnum = DVD.num INNER JOIN Filme fi 
ON fi.id = DVD.FilmeId
WHERE cl.nome LIKE 'Matilde%'

--Consultar nome da estrela, nome_real da estrela, título dos filmes
--cadastrados do ano de 2015
SELECT es.nome, es.nome_real, fi.titulo 
FROM Estrela es INNER JOIN Filme_Estrela
ON es.id = Filme_Estrela.EstrelaId INNER JOIN Filme fi
ON fi.id = Filme_Estrela.FilmeId
WHERE fi.ano = 2015

--Consultar título do filme, data_fabricação do dvd (formato dd/mm/aaaa), caso a
--diferença do ano do filme com o ano atual seja maior que 6, deve aparecer a diferença
--do ano com o ano atual concatenado com a palavra anos (Exemplo: 7 anos), caso
--contrário só a diferença (Exemplo: 4).
SELECT DISTINCT fi.titulo, CONVERT(CHAR(10), DVD.data_fabricacao, 103) AS Data_Fabricacao,
	CASE WHEN (YEAR(GETDATE()) - fi.ano) >= 6 THEN
		CAST((YEAR(GETDATE()) - fi.ano) AS VARCHAR) + ' anos' 
		ELSE
		CAST((YEAR(GETDATE()) - fi.ano) AS VARCHAR)
		END AS Ano
FROM Filme fi INNER JOIN DVD
ON fi.id = DVD.FilmeId
