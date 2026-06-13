
alter table users
    add column user_type varchar(50) default  'CUSTOMER';

alter table company
    add column base_url varchar(255) default 'http://localhost:8080/';

