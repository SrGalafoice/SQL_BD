--Baseado na Database: https://pastebin.com/zYGCddwv

--Consultar o nome, o logradouro e o número de porta do endereço dos 
--clientes cujos carros são da marca Renault, ordenar a saída por nome ascendente
SELECT nome, Logradouro + ' ' + CAST(Numero AS varchar(5)) AS Endereco_Completo
FROM cliente 
WHERE ID in (
	SELECT ClienteID 
	FROM veiculo
	WHERE Marca = 'Renault')
ORDER BY nome


--Para minimizar falhas de caracteres acentuados, consultar a categoria e o valor hora da categoria, 
--no entanto, caso a categoria seja Nível 1, Nível 2 ou Nível 3, mudar para Categoria 1, Categoria 2 e Categoria 3
SELECT CASE WHEN ISNUMERIC(SUBSTRING(Categoria,7,1)) = 1 AND CAST(SUBSTRING(Categoria,7,1) AS INT) >= 1
 THEN 
 'Categoria ' + SUBSTRING(Categoria,7,1)
 ELSE Categoria
 END AS Categoria, Valor_Hora
FROM categoria 

--Consultar o nome, o cep e o número do endereço dos proprietários de carros cuja diferença entre 
--ano de aquisição e ano de fabricação é superior a 5 (Por serem INT, não usar o DATEDIFF). Ordenação ascendente por nome.

SELECT nome, CEP, Numero
FROM Cliente
WHERE ID in (
	SELECT ClienteID
	FROM veiculo
	WHERE Ano_Fabricacao > 5 AND Data_Aquisicao > 5
)
ORDER BY Nome

--Consultar o nome, o cep (mascarado XXXXX-XXX) e o número do endereço 
--(mascarado sempre com 4 dígitos, completar com zeros quando tiver 1, 2 ou 3 dígitos) dos clientes ordenados por nome.

SELECT nome, 'XXXXX-XXX' AS CEP, Numero
FROM Cliente 
