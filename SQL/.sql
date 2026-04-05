-- =========================
-- DATABASE
-- =========================
create database Banking_system;


-- =========================
-- TABLES
-- =========================

create table customer(
        customer_id serial primary key,
        name text,
        city text,
        password text default '1234'
);

create table account(
        account_id serial primary key,
        customer_id int references customer(customer_id),
        type_of_account text,
        balance numeric,
        limit_of_credit numeric default 100000,
        used_credit numeric default 0
);

create table transaction_table(
        transaction_id serial primary key,
        account_id int references account(account_id),
        amount numeric,
        type_of_transaction text,
        date_of_transaction timestamp default current_timestamp,
        level_of_risk text
);

alter table transaction_table add constraint check_type_of_transaction check (type_of_transaction in ('DEBIT','CREDIT'));


-- =========================
-- FRAUD DETECTION
-- =========================

-- Evaluates each new transaction before insertion and assigns a risk level (LOW, MEDIUM, HIGH) based on the transaction amount
create or replace function fraud_detection_trigger()
returns trigger as $$
begin
	if new.amount > 100000 then
		new.level_of_risk := 'HIGH';
	elsif new.amount > 50000 then
		new.level_of_risk := 'MEDIUM';
else
		new.level_of_risk := 'LOW';
end if;
return new;
end;
$$ language plpgsql;

-- ===============================================================================
-- PL/pgSQL stands for: Procedural Language / PostgreSQL Structured Query Language
-- ===============================================================================


-- Trigger to detect and prevent fraudulent transactions before inserting into transaction_table
create trigger prevent_fraud_trigger
before insert on transaction_table
for each row
execute function fraud_detection_trigger();

-- =========================
-- Sample Data Insertion
-- =========================

insert into customer(name, city, password) values
        ('Shivani', 'Nizamabad', 'pass1'),
        ('Akshay', 'Hyderabad', 'pass2'),
        ('Siddarth', 'Warangal', 'pass3'),
        ('Sathvik', 'Coimbatore', 'pass4'),
        ('Anu', 'Rajawaram', 'pass5');

insert into account(customer_id, type_of_account, balance, limit_of_credit, used_credit) values
            (1, 'Savings', 55000, 100000, 100020),
            (2, 'Current', 70000, 135000, 30000),
            (3, 'Savings', 35000, 120000, 8000),
            (4, 'Current', 135000, 200000, 45000),
            (5, 'Savings', 50000, 100000, 0);

insert into transaction_table(account_id, amount, type_of_transaction) values
            (1, 135000, 'CREDIT'),
            (2, 50000, 'DEBIT'),
            (3, 55000, 'CREDIT'),
            (4, 45000, 'DEBIT'),
            (5, 50000, 'CREDIT');



