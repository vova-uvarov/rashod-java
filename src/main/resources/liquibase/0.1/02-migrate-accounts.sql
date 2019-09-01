--liquibase formatted sql
--changeset vuvarov:migrateAccounts
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'нал',  'Наличные', 'SIMPLE', 0, '#de5709', 'ACTIVE', true, true,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Зарплатная из СГ уже закрыта',  'СБ7643', 'SIMPLE', 0, '#1ef71e', 'INACTIVE', false, true,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  '', 'Тинькофф', 'SIMPLE', 0, '#2b2525', 'ACTIVE', false, true,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Саша для получения компенсации за садик', 'Моментум', 'SIMPLE', 0, '#11cfd9', 'ACTIVE', false, true, 'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Долг за машину бабушке',  'Бабушке', 'DEBT', 250685, '', 'INACTIVE', false, true,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Долг за машину, ноутбук, телевизор', 'Долги Маме', 'DEBT', 232000, '#e0b714', 'INACTIVE', false, true,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES ( 'Для новой работы',  'Сбербанк', 'SIMPLE', 0, '#16ad1e', 'ACTIVE', false, true,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES ( 'Серебро на пенсию',  'ОМС Серебро', 'GOAL', 1000, '#73797d', 'ACTIVE', false, true,  'ARGENTUM',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  '--', 'Стабфонд', 'GOAL', 500000, '#b96686', 'INACTIVE', false, true,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Расчетный счет ИП', 'UBRR Р/С', 'GOAL', 120000, '#eb0e93', 'INACTIVE', false, true, 'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  '-', 'СБ Вклад Стабфонд', 'GOAL', 800000, '#19ff3f', 'ACTIVE', false, false, 'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'В сбере', 'ОМС Золото', 'GOAL', 100, '#e0b714', 'ACTIVE', false, false , 'AURUM',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Просто доллары, возможно на квартиру',  'СБ Доллары', 'GOAL', 90000, '#5b8f66', 'INACTIVE', false, false, 'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Основной Расчетный счет',  'Тинькоф Р/C', 'GOAL', 999000, '#a02714', 'ACTIVE', false, false, 'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Фиктивный счет. На самом деле это все лежит на карте',  'Тинькофф (Цель)', 'GOAL', 3000000, '#541697', 'ACTIVE', false, false, 'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'то что мы занимаем',  'Должны НАМ', 'GOAL', 135019, '#cd2339', 'ACTIVE', false, false,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Тинькофф Вклад квартира',  'Тинькофф Вклад квартира', 'GOAL', 2000000, '#e7e584', 'INACTIVE', false, false,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Акции яндекса',  'YANDEX', 'GOAL', 100, '#ff0037', 'INACTIVE', false, false,  'YANDEX',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'ETF Китайских компаний',  'ETF-CH', 'GOAL', 100, '#683024', 'INACTIVE', false, false,  'ETF_CH',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'СБ Вклад Простой',  'СБ Вклад Простой', 'GOAL', 500000, '#7ba2cb', 'INACTIVE', false, false, 'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'ВТБ Мультикарта',  'ВТБ Мультикарта', 'SIMPLE', 200000, '#6775bf', 'ACTIVE', false, true, 'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'ВТБ Накопительный Счет',  'ВТБ Накопительный Счет', 'GOAL', 2500000, '#96BFAB', 'ACTIVE', false, false,  'RUB',now(), now());
INSERT INTO account (  description,  name, account_type, target_cost, color, status, round, is_balance,  currency, ins_time, modif_time ) VALUES (  'Для погашения Ипотеки',  'Ипотека', 'DEBT', 2000000, '#93bf84', 'ACTIVE', false, false, 'RUB',now(), now());
