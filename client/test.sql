use test;
show tables;
select * from test_table;
drop table test_table;
show tables;
create table test_table(
clon1 int,clon2 varchar(8),clon3 int
);
select * from test_table;
select con4 from test_table;
select * from test;
select *,*,con1,con2 from test_table;
insert into test_table (con3,con2,con1)values(7,asd,1);
insert into test_table values (2,'zxc',8);
insert into test_table values (3,"qaz",5);
insert into test_table values
(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),
(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),
(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),
(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),
(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),
(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),
(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),
(2,"zxc",8),(2,"zxc",8),(2,"zxc",8),(2,"zxc",8);
insert into test values (1,'sdf');
insert into test_table values(a,"sxc",3);
select * from test_table;
select con1 from test_table;
select *,con1 from test_table;
select con1,con1 from test_table;
select con1 from test;
select * from test_table where con1=2;
select * from test_table where con2='asd';
update test_table set clon2 ='wsx'  where clon1=1 ;
select * from test_table;
update test_table set clo='wsx' where clon1=1;
update test set clon2='wsx' where clon1=1;
update test_table set clon1='w';
update test set clon2='wer' where clon2='wsx';
select * from test_table;
delete from test_table where clon1=2;
select * from test_table;
quit
