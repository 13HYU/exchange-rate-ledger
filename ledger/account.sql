create table account(
	fullname varchar(30) not null,
	username varchar(20) not null,
	password varchar(20) not null,
	email varchar(320) not null,
	question text not null,
	answer varchar(30) not null,
	primary key (username)
);