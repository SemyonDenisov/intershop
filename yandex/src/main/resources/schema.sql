
create table if not exists items(
                                    id bigint generated always as identity primary key,
                                    title varchar(30) not null,
                                    description varchar(350) not null,
                                    price numeric not null,
                                    count bigint not null,
                                    img_path varchar(256) not null);


insert into items(title, description, price,count,img_path) values ('title','description',3.5,0,'1.jpg');
insert into items(title, description, price,count,img_path) values ('title','description',2.5,0,'2.jpg');
insert into items(title, description, price,count,img_path) values ('title','description',1.5,0,'3.jpg');
insert into items(title, description, price,count,img_path) values ('title','description',4.5,0,'4.jpg');
insert into items(title, description, price,count,img_path) values ('title','description',6.5,0,'5.jpg');
insert into items(title, description, price,count,img_path) values ('title','description',7.5,0,'6.jpg');
insert into items(title, description, price,count,img_path) values ('title','description',8.5,0,'7.jpg');


create table if not exists orders(
                                    id bigint generated always as identity primary key,
                                    total_sum numeric not null);

