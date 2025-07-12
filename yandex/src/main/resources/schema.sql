
create table if not exists items(
                                    id bigint generated always as identity primary key,
                                    title varchar(30) not null,
                                    description varchar(350) not null,
                                    price numeric not null,
                                    count bigint not null,
                                    img_path varchar(256) not null);

create table if not exists cart(
                                    id bigint generated always as identity primary key
                                );

create table if not exists cart_item(
                                   id bigint generated always as identity primary key,
                                   cart_id bigint,
                                   item_id bigint
);

create table if not exists order_item(
                                        id bigint generated always as identity primary key,
                                        order_id bigint,
                                        item_id bigint,
                                        quantity bigint
);

create table if not exists orders(
                                   id bigint generated always as identity primary key,
                                   total_sum bigint
);


insert into items(title, description, price,count,img_path) values ('Футболка 1','классная',3.5,0,'1.jpg');
insert into items(title, description, price,count,img_path) values ('Футболка 2','не очень',2.5,0,'2.jpg');
insert into items(title, description, price,count,img_path) values ('Футболка 3','норм',1.5,0,'3.jpg');
insert into items(title, description, price,count,img_path) values ('Футболка 4','средне',4.5,0,'4.jpg');
insert into items(title, description, price,count,img_path) values ('Футболка 5','так себе',6.5,0,'5.jpg');
insert into items(title, description, price,count,img_path) values ('Футболка 6','подделка',7.5,0,'6.jpg');
insert into items(title, description, price,count,img_path) values ('Футболка 7','эта вообще женская',8.5,0,'7.jpg');

insert into cart ()
values ();