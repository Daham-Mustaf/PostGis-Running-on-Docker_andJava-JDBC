# PostGis
PostGIS Setup<br /> 
Setting up PostGIS functions will allow to access spatial functions from within PostgreSQL.<br /> 
Installing under Ubuntu from terminal.
```bash
$ sudo add-apt-repository ppa:ubuntugis/ppa
$ sudo apt-get update
```
Install PostGIS with apt-get.
```bash
$ sudo apt-get install postgis postgresql-10-postgis-2.5
```
After successful installation open psql.
```bash
$ sudo -u postgres psql
```
```bash
List all databases
```
```bash
postgres=# \l
```
Connect to specific database
```bash
postgres=# \c DATABASE_NAME
```
Once PostGIS is installed, you will need to configure your database to use the extensions.
```bash
CREATE EXTENSION postgis;
CREATE EXTENSION postgis_topology;
```
We can find the version installed by issuing a `select PostGIS_full_version();` query with psql or another tool.
