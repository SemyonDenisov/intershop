
create table if not exists balance(
                                    id bigint generated always as identity primary key,
                                    user_id bigint not null,
                                    amount bigint not null
                                    );


insert into balance(user_id,amount) values (1,20);
insert into balance(user_id,amount) values (2,777);
