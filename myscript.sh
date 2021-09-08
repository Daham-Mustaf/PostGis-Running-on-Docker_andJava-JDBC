sudo add-apt-repository ppa:ubuntugis/ppa

sudo apt-get update
sudo apt-get install postgis postgresql-10-postgis-2.5

CREATE EXTENSION postgis
CREATE EXTENSION postgis_topology

postgres=# \q