# "Счета и деньги" - проект, реализованный на Scala

## Требования

### Функциональные требования

#### Основные

- состоит из счетов пользователей (деньги = целое число)
- пользователи могут пополнять счёт
- пользователи могут снимать со счёта
- пользователи могут переводить деньги между счетами

#### Дополнительные

- у переводов может быть указана категория перевода
- в зависимости от категории может начисляться различный кэшбэк

### Технические требования

- Scala
- PostgreSQL
- Docker
- приложение должно разворачиваться одной командой
- интерфейс взаимодействия с пользователем - REST API ([Akka HTTP](https://doc.akka.io/))
- сдача проекта как репозитория на GitHub

### Сущности (таблицы в БД)

- Счёт
- Категория
- ???

---

## Разработка

### Перед началом работы

- В корне проекта необходимо создать файл `.env` по формату `.env.example` и заполнить переменные окружения

### Необходимые команды

- `sbt new scala/scala-seed.g8` - создание нового проекта
- `sbt run` - запуск проекта

### TODO

|   № |    Модель     | Задача                                | Стр-ра | API | БД  | Обр. ошибок |
| --: | :-----------: | ------------------------------------- | :----: | :-: | :-: | :---------: |
|   1 |    Account    | Создание счёта                        |   ✅   | ✅  | ✅  |     ✅      |
|   2 |    Account    | Список счётов                         |   ✅   | ✅  | ✅  |     ✅      |
|   3 |    Account    | Детализация по счёту                  |   ✅   | ✅  | ✅  |     ✅      |
|   4 |    Account    | Редактирование счёта                  |   ✅   | ✅  | ✅  |     ✅      |
|   5 |    Account    | Удаление счёта                        |   ✅   | ✅  | ✅  |     ✅      |
|   6 |    Account    | Пополнение счёта                      |   ✅   | ✅  | ✅  |     ✅      |
|   7 |    Account    | Обналичить со счёта                   |   ✅   | ✅  | ✅  |     ✅      |
|   8 |    Account    | Перевести деньги по ID счёта          |   ✅   | ✅  | ✅  |     ✅      |
|  -- |    ------     | ----------------v2---------------     |  ----  | --- | --- | ----------- |
|   9 |    Account    | Перевести деньги по номеру телефона   |   ✅   | 🕑  | 🕑  |     🕑      |
|  10 |    Account    | Выбрать приоритетный счёт             |   ✅   | 🕑  | 🕑  |     🕑      |
|  11 |   Category    | Просмотр списка всех категорий        |   ❔   | ❔  | ❔  |     ❔      |
|  12 |   Category    | Создать новую категорию               |   ❔   | ❔  | ❔  |     ❔      |
|  13 |   Category    | Изменить процент кэшбэка на категории |   ❔   | ❔  | ❔  |     ❔      |
|  14 |     User      | Просмотр списка пользователей         |   ❔   | ❔  | ❔  |     ❔      |
|  15 |     User      | Создать нового пользователя           |   ❔   | ❔  | ❔  |     ❔      |
|  16 | CashOperation | История операций со счётом            |   ❔   | ❔  | ❔  |     ❔      |
|  17 |   Transfer    | История переводов                     |   ❔   | ❔  | ❔  |     ❔      |
