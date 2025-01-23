CREATE DATABASE UsuProj
GO

USE UsuProj
GO

CREATE TABLE users(
ID INT NOT NULL IDENTITY(1,1),
nome VARCHAR(45) NOT NULL,
username VARCHAR(10) NOT NULL UNIQUE,
senha VARCHAR(8) NOT NULL DEFAULT('123mudar'),
email VARCHAR(45) NOT NULL

PRIMARY KEY (ID)
)
GO

CREATE TABLE projects(
ID INT NOT NULL IDENTITY(10001, 1),
nome VARCHAR(45) NOT NULL,
descricao VARCHAR(45),
date DATE NOT NULL CHECK(date > '2014-09-01')

PRIMARY KEY (ID)
)
GO


CREATE TABLE users_has_projects(
users_ID INT NOT NULL,
projects_ID INT NOT NULL

PRIMARY KEY(users_ID, projects_ID),
FOREIGN KEY (users_ID) REFERENCES users(ID),
FOREIGN KEY (projects_ID) REFERENCES projects(ID)
)

INSERT INTO users(nome, username, senha, email) VALUES
('Maria', 'Rh_maria',DEFAULT,'maria@empresa.com'),
('Paulo', 'Ti_paulo','123@456','paulo@empresa.com'),
('Ana', 'Rh_ana',DEFAULT,'ana@empresa.com'),
('Clara', 'Ti_clara', DEFAULT,'clara@empresa.com'),
('Aparecido', 'Rh_apareci', '55@!cido', 'aparecido@empresa.com')

SELECT * FROM users
--DELETE users
--DBCC CHECKIDENT ('Users', RESEED, 0)


INSERT INTO projects(nome, descricao, date) VALUES 
('Re-folha', 'Refatoração das Folhas','2014-09-05'),
('Manutenção PC''s', 'Manutenção PC''s','2014-09-06'),
('Auditoria', NULL, '2014-09-07') 
SELECT * FROM projects

INSERT INTO users_has_projects (users_ID, projects_ID)  VALUES
(1 , 10001),
(5, 10001),
(3, 10003),
(4, 10002),
(2, 10002)
SELECT * FROM users_has_projects


DELETE users
TRUNCATE TABLE users
DELETE PROJECTS
TRUNCATE TABLE projects
-- O projeto de Manutenção atrasou, mudar a data para 12/09/2014
UPDATE projects SET date = '2014-09-12'
WHERE nome = 'Manutenção PC''s'

-- O username de aparecido (usar o nome como condição de mudança) está feio, mudar para Rh_cido
UPDATE users set username = 'Rh_cido' 
WHERE nome = 'Aparecido'

-- Mudar o password do username Rh_maria (usar o username como condição de mudança)
-- para 888@*, mas a condição deve verificar se o password dela ainda é 123mudar
UPDATE users SET senha = '888@'
WHERE username = 'Rh_maria' AND senha = '123mudar'

--O user de id 2 não participa mais do projeto 10002, removê-lo da associativa
DELETE users_has_projects 
WHERE users_id = 2 AND projects_id = 10002
