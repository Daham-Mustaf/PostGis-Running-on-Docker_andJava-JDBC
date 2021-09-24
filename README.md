# PostGis

# Table of contents
1. [PostGIS Introduction](#introduction)
- [Docker:](#dock)
- [Docker and PostgreSQL:](#dp)
- [Installation:](#install)
- [ Check which version](#version)
- [extensions:](#extensions)
- [`ogrinfo`: ](#datasource)
- [`--help` getting help: ](#help)
- [`--long-usage` full help:](#fullhelp)
3. [Working with shape file](#paragraph1)


## PostGIS Introduction <a name="introduction"></a>


PostGIS Setup<br /> 
Setting up PostGIS functions will allow to access spatial functions from within PostgreSQL.<br /> 
## Docker Introduction <a name="dock"></a>
Docker is a set of platform as a service (PaaS) products that use OS-level virtualization to deliver software in packages called containers. Containers are isolated from one another and bundle their own software, libraries and configuration files; they can communicate with each other through well-defined channels. Because all of the containers share the services of a single operating system kernel, they use fewer resources than virtual machines.

Docker and PostgreSQL:<a name="dp"></a>
- run the commadn to donwload the image:
```bash
docker pull postgres
```
- start a postgres instance:
```bash
docker run --name postgres-0 -e POSTGRES_PASSWORD=postgres -d  -p 5432:5432 postgres:alpine
```
- check the docker which it is ruunnig 
```bash
Docker ps
```
- Open a shell into a running docker container:
```bash
docker exec -it postgres-0 bash
```
- Let’s stop the container / instance
```bash
docker stop postgres-0
```
- And let’s also throw away any data generated and stored by our instance
 ```bash
docker rm postgres-0
```

## PostGIS Instollation: <a name="install"></a>
Installing under Ubuntu from terminal.
```bash
$ sudo add-apt-repository ppa:ubuntugis/ppa
$ sudo apt-get update
```
Install PostGIS with apt-get.
```bash
$ sudo apt-get install postgis postgresql-10-postgis-2.5
```
## check the version: <a name="version"></a>
After a PostGIS install, do a quick verification of the version to make sure the installa- tion succeeded. Execute the following query:
```sql
SELECT postgis_full_version();
```
## add extenstions:<a name="extensions"></a>
Once PostGIS is installed, you will need to configure your database to use the extensions.
```j
CREATE EXTENSION postgis;
CREATE EXTENSION postgis_topology;
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

We can find the version installed by issuing a `select PostGIS_full_version();` query with psql or another tool.<br /> 
### Importing nonspatial tabular data (CSV) using GDA:<br /> 
Data Abstraction Library [`GDAL`](https://gdal.org/gdal.pdf) The Geospatial Data Abstraction Library (GDAL) is a collection of tools that allow you to read, manipulate and write both raster and vector data using GDAL and the Simple Feature Library (OGR). we will import a CSV file to PostGIS using the `ogr2ogr` GDAL command and the GDAL OGR virtual format. <br /> 
`ogr2ogr` can be used to convert simple features data between file formats. It can also perform various operations during
the process, such as spatial or attribute selection, reducing the set of attributes, setting the output coordinate system or
even reprojecting the features during translation.<br />
This file represents the active hotspots in the world detected by the Moderate Resolution Imaging Spectroradiometer (MODIS) satellites in the last 24 hours. For each row, there are the coordinates of the hotspot (latitude, longitude) in decimal degrees (in the WGS 84 spatial reference system, SRID = 4326), and a series of useful fields such as the acquisition date, acquisition time, and satellite type, just to name a few.

1. Analyze the structure of the [`Global_24h.csv`](https://github.com/Daham-Mustaf/PostGis/blob/main/Data/Global_24h.csv)<br />
 ```bash
 $ cd ~/Desktop/PostGis/Data
 $ head -n 5 Global_24h.csv
 ```
 2. Create a GDAL virtual data source in the same directory where the [`Global_24h.csv`](https://github.com/Daham-Mustaf/PostGis/blob/main/Data/Global_24h.csv). edit the file [`global_24h.vrt`](https://github.com/Daham-Mustaf/PostGis/blob/main/Data/global_24h.vrt).<br />
 3. With the `ogrinfo` command, The `ogrinfo` program lists various information about an OGR-supported data source to stdout (the terminal). By
executing SQL statements it is also possible to edit data.
With the `ogrinfo` command, check if the virtual layer is correctly recognized by GDAL.

`ogrinfo` <a name="datasource"></a>
```bash
$ ogrinfo global_24h.vrt Global_24h -fid 1
 ```
`-fid` If provided, only the feature with this feature id will be reported. Operates exclusive of the spatial or attribute
queries

### Creating a spatial view in PostgreSQL:
You can use SQL to create a view and include the spatial column in the view definition.
1- we will creat some random points, which could be real data.
```js
 //Drop the table in case it exists
   DROP TABLE IF EXISTS xyz CASCADE;
   CREATE TABLE xyz
   //This table contains numeric x, y, and z values
   (
     x numeric,
     y numeric,
     z numeric
   )
   WITH (OIDS=FALSE);
   // We will be disciplined and ensure we have a primary key
   ALTER TABLE xyz ADD COLUMN gid serial;
   ALTER TABLE xyz ADD PRIMARY KEY (gid);

   // populate the data for testing to use in the query:

   INSERT INTO xyz (x, y, z)
     VALUES (random()*3, random()*10, random()*100);
   INSERT INTO xyz (x, y, z)
     VALUES (random()*3, random()*10, random()*100);
   INSERT INTO xyz (x, y, z)
     VALUES (random()*3, random()*10, random()*100);
   INSERT INTO xyz (x, y, z)
     VALUES (random()*3, random()*10, random()*100);

     // Ensure we don't try to duplicate the view
   DROP VIEW IF EXISTS viewXyz;
   // Retain original attributes, but also create a point and y
   CREATE VIEW viewXyz AS
   SELECT x, y, z, ST_SetSRID(ST_MakePoint(x,y), 4326)
   FROM xyz;
```
The `ST_MakePoint()` function takes the input of two numbers to create a PostGIS point Any time there is an update to the table to add a new record with x and y values, the view will populate a point, which is really useful for data that is constantly being updated.
with `ST_SetSRID()` We can declare our spatial reference system in the view, The spatial reference system identifier (SRID) allows us to specify the coordinate system for a given dataset.
```js
SELECT AddGeometryColumn ('xyz','geom',3734,'POINT',2);
UPDATE xyz
 // Add ST_SetSRID
     SET geom = ST_SetSRID(ST_MakePoint(x,y), 3734);
SELECT *, ST_AsText(geom) as points FROM xyz;
```
with fucntion `AddGeometryColumn()` We now have a column called geom with an SRID of 3734; that is, a point geometry type in two dimensions.
`ST_AsText(geom)`Returns the Well-Known Text representation of the geometry/geography.
