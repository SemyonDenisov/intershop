
create table if not exists items(
                                    id bigint generated always as identity primary key,
                                    title varchar(30) not null,
                                    description varchar(350) not null,
                                    price numeric not null,
                                    count bigint not null,
                                    img_path varchar(256) not null);


insert into items(title, description, price,count,img_path) values ('title','description',3.5,0,'none');
insert into items(title, description, price,count,img_path) values ('title','description',2.5,0,'none');
insert into items(title, description, price,count,img_path) values ('title','description',1.5,0,'none');
insert into items(title, description, price,count,img_path) values ('title','description',4.5,0,'none');
insert into items(title, description, price,count,img_path) values ('title','description',6.5,0,'none');
insert into items(title, description, price,count,img_path) values ('title','description',7.5,0,'none');
insert into items(title, description, price,count,img_path) values ('title','description',8.5,0,'none');


