-- Drop the table in case it exists
   DROP TABLE IF EXISTS xyz CASCADE;
   CREATE TABLE xyz
   -- This table will contain numeric x, y, and z values
   (
     x numeric,
     y numeric,
     z numeric
   )
   WITH (OIDS=FALSE);
   -- We will be disciplined and ensure we have a primary key
   ALTER TABLE xyz ADD COLUMN gid serial;
   ALTER TABLE xyz ADD PRIMARY KEY (gid);

   -- populate the data for testing to use in the query:

   INSERT INTO xyz (x, y, z)
     VALUES (random()*3, random()*10, random()*100);
   INSERT INTO xyz (x, y, z)
     VALUES (random()*3, random()*10, random()*100);
   INSERT INTO xyz (x, y, z)
     VALUES (random()*3, random()*10, random()*100);
   INSERT INTO xyz (x, y, z)
     VALUES (random()*3, random()*10, random()*100);

     -- Ensure we don't try to duplicate the view
   DROP VIEW IF EXISTS viewXyz;
   -- Retain original attributes, but also create a point and y
   CREATE VIEW viewXyz AS
   -- // add SRID for the point
   SELECT x, y, z, ST_SetSRID(ST_MakePoint(x,y), 4326)
   FROM xyz;
