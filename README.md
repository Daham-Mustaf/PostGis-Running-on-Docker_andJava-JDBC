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
We can find the version installed by issuing a `select PostGIS_full_version();` query with psql or another tool.<br /> 
### Importing nonspatial tabular data (CSV) using GDA:<br /> 
Data Abstraction Library [`GDAL`](https://gdal.org/gdal.pdf) The Geospatial Data Abstraction Library (GDAL) is a collection of tools that allow you to read, manipulate and write both raster and vector data using GDAL and the Simple Feature Library (OGR). we will import a CSV file to PostGIS using the `ogr2ogr` GDAL command and the GDAL OGR virtual format. <br /> 
`ogr2ogr` can be used to convert simple features data between file formats. It can also perform various operations during
the process, such as spatial or attribute selection, reducing the set of attributes, setting the output coordinate system or
even reprojecting the features during translation.<br />
This file represents the active hotspots in the world detected by the Moderate Resolution Imaging Spectroradiometer (MODIS) satellites in the last 24 hours. For each row, there are the coordinates of the hotspot (latitude, longitude) in decimal degrees (in the WGS 84 spatial reference system, SRID = 4326), and a series of useful fields such as the acquisition date, acquisition time, and satellite type, just to name a few.

1. Analyze the structure of the Global_24h.csv file<br />
 ```bash
 $ cd ~/Desktop/PostGis/Data
 $ head -n 5 Global_24h.csv
 ```
 2. Create a GDAL virtual data source in the same directory where the CSV file is composed of just one layer derived from the Global_24h.csv file. edit the file global_24h.vrt.

