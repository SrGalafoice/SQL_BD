--DATABASE: https://github.com/SrGalafoice/SQL_BD/blob/main/bd_dnv/13.05/Ex_DDL_DML_SQL.sql

--a) Adicionar User
--(6; Joao; Ti_joao; 123mudar; joao@empresa.com)
INSERT INTO users VALUES('Joao', 'Ti_joao', '123mudar', 'joao@empresa.com')

--b) Adicionar Project
--(10004; Atualização de Sistemas; Modificação de Sistemas Operacionais nos PC's; 12/09/2014)
INSERT INTO projects VALUES('Atualização de Sistemas', 'Modificação de Sistemas Operacionais nos PC''s', '2014-09-12')

--Quantos projetos não tem usuários associados a ele. A coluna deve chamar
--qty_projects_no_users

SELECT COUNT(*) AS qty_project_no_users 
FROM projects pro LEFT OUTER JOIN users_has_projects upo
ON pro.ID = upo.projects_ID
WHERE upo.users_ID IS NULL

--Id do projeto, nome do projeto, qty_users_project (quantidade de usuários por
--projeto) em ordem alfabética crescente pelo nome do projeto
SELECT pro.ID, pro.nome, COUNT(upo.users_ID) AS qty_users_project
FROM projects pro INNER JOIN users_has_projects upo
ON pro.ID = upo.projects_ID
GROUP BY pro.ID, pro.nome
ORDER BY pro.nome
