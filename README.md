# Online Shop

### Initialization of the project.

SQL to be executed on the PostgreSQL service.

```
CREATE USER onlineshop WITH PASSWORD 'onlineshop';
```
Create user.

```
CREATE DATABASE "OnlineShop"
    WITH 
    OWNER = onlineshop
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
```
Create database.