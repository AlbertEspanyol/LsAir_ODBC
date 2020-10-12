USE Local_LsAir;

DROP USER IF EXISTS 'analytic_user'@'localhost','manager_user'@'localhost','rrhh_user'@'localhost';
CREATE	USER 'analytic_user'@'localhost' IDENTIFIED BY 'password_analytic'	,
  'manager_user'@'localhost'	IDENTIFIED BY 'password_manager',
'rrhh_user'@'localhost' IDENTIFIED BY 'password_rrhh';

GRANT SELECT, SHOW VIEW, CREATE VIEW
ON OLAP_db.*
TO 'analytic_user'@'localhost';

GRANT UPDATE
ON *.*
TO 'manager_user'@'localhost';

GRANT CREATE USER
ON *.*
TO 'rrhh_user'@'localhost';

SELECT * FROM mysql.user;