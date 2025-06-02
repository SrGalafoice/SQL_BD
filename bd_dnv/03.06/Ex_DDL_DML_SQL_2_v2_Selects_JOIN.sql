--DATABASE: https://github.com/SrGalafoice/SQL_BD/blob/main/bd_dnv/27.05/Ex2.sql

--Consultar, num_cadastro do cliente, nome do cliente, titulo do filme, data_fabricação
--do dvd, valor da locação, dos dvds que tem a maior data de fabricação dentre todos os
--cadastrados.
SELECT cli.num_cadastro, cli.nome, fi.titulo, d.data_fabricacao, loc.valor
FROM Filme fi INNER JOIN DVD d 
ON d.FilmeId = fi.id INNER JOIN Locacao loc
ON loc.DVDnum = d.num INNER JOIN Cliente cli
ON cli.num_cadastro = loc.ClienteNum_Cadastro
WHERE d.data_fabricacao = (
    SELECT MAX(data_fabricacao) FROM DVD
)

--Consultar, num_cadastro do cliente, nome do cliente, data de locação
--(Formato DD/MM/AAAA) e a quantidade de DVD ́s alugados por cliente (Chamar essa
--coluna de qtd), por data de locação

SELECT cli.num_cadastro, cli.nome, CONVERT(CHAR(10), loc.data_locacao, 103) AS data_locacao, COUNT(d.num) AS qtd
FROM Cliente cli INNER JOIN Locacao loc 
ON cli.num_cadastro = loc.ClienteNum_Cadastro INNER JOIN DVD d 
ON d.num = loc.DVDnum
GROUP BY cli.num_cadastro, cli.nome, loc.data_locacao

--Consultar, num_cadastro do cliente, nome do cliente, data de locação
--(Formato DD/MM/AAAA) e a valor total de todos os dvd ́s alugados (Chamar essa
--coluna de valor_total), por data de locação
SELECT cli.num_cadastro, cli.nome, CONVERT(CHAR(10), loc.data_locacao, 103) AS data_locacao, SUM(loc.valor) AS valor_total
FROM Cliente cli INNER JOIN Locacao loc 
ON cli.num_cadastro = loc.ClienteNum_Cadastro 
GROUP BY cli.num_cadastro, cli.nome, loc.data_locacao

--Consultar, num_cadastro do cliente, nome do cliente, Endereço
--concatenado de logradouro e numero como Endereco, data de locação (Formato
--DD/MM/AAAA) dos clientes que alugaram mais de 2 filmes simultaneamente
SELECT cli.num_cadastro, cli.nome, cli.logradouro + ' - ' + CAST(cli.num AS VARCHAR) AS Endereco, CONVERT(CHAR(10), loc.data_locacao, 103) AS data_locacao
FROM Cliente cli INNER JOIN Locacao loc 
ON cli.num_cadastro = loc.ClienteNum_Cadastro INNER JOIN DVD d
ON d.num = loc.DVDnum INNER JOIN Filme fi
ON fi.id = d.FilmeId
GROUP BY cli.num_cadastro, cli.nome, loc.data_locacao, cli.logradouro, cli.num
HAVING COUNT(loc.DVDnum) > 2
