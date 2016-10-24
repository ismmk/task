CREATE TABLE  entry
(user_id text,client_id text,entry_type text, action_time timestamp,params map<text,text>, primary key(entry_type, client_id, user_id, action_time));

insert into entry(user_id, client_id, entry_type, action_time, params)
values ('user1', 'client1', 'type1', dateof(now()), {'v' : 't0'});

insert into entry(user_id, client_id, entry_type, action_time, params)
values ('user2', 'client2', 'type1', dateof(now()), {'v' : 't1'});

insert into entry(user_id, client_id, entry_type, action_time, params)
values ('user3', 'client1', 'type1', dateof(now()), {'v' : 't2'});