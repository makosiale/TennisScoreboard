# Tennis Scoreboard

Веб-приложение, реализующее табло счёта теннисного матча.

## Что нужно знать

- [Java] - коллекции, ООП
- [Паттерн MVC(S)]) 
- [Maven/Gradle]
- [Backend]
  - Java Servlets, JSP
  - HTTP - GET и POST запросы, формы
- [Базы данных] - SQL, Hibernate, H2 (in-memory SQL database)
- [Frontend] - HTML/CSS, блочная вёрстка
- [Тесты] - юнит тестирование, JUnit 5
- [Деплой] - облачный хостинг, командная строка Linux, Tomcat

## Мотивация проекта

- Создать клиент-серверное приложение с веб-интерфейсом
- Получить практический опыт работы с ORM Hibernate
- Сверстать простой веб-интерфейс без сторонних библиотек
- Познакомиться с архитектурным паттерном MVC(S)

## Функционал приложения

Работа с матчами:

- Создание нового матча
- Просмотр законченных матчей
- Подсчёт очков в текущем матче

## Подсчёт очков в теннисном матче

В теннисе особая система подсчёта очков - https://www.gotennis.ru/read/world_of_tennis/pravila.html

Для упрощения, допустим что каждый матч играется по следующим правилам:
- Матч играется до двух сетов (best of 3)
- При счёте 6/6 в сете, играется тай-брейк до 7 очков

## Интерфейс приложения

### Главная страница

- Ссылки, ведущие на страницы нового матча и списка завершенных матчей

### Страница нового матча

Адрес - `/new-match`.

Интерфейс:
- HTML форма с полями “Имя первого игрока”, “Имя второго игрока” и кнопкой “начать”. Для упрощения допустим, что имена игроков уникальны. Игрок не может играть сам с собой.
- Нажатие кнопки “начать” приводить к POST запросу по адресу `/new-match`

Обработчик POST запроса:
- Проверяет существование игроков в таблице `Players`. Если игрока с таким именем не существует, создаём
- Создаём экземпляр класса, содержащего айди игроков и текущий счёт, и кладём в коллекцию текущих матчей (существующую только в памяти приложения, либо в key-value storage). Ключом коллекции является UUID, значением - счёт в матче
- Редирект на страницу `/match-score?uuid=$match_id`

### Страница счёта матча - `/match-score`

Адрес - `/match-score?uuid=$match_id`. GET параметр `uuid` содержит UUID матча.

Интерфейс:
- Таблица с именами игроков, текущим счётом
- Формы и кнопки для действий - "Первый", "Второй", которые начисляют очки в случае забитого первому или второму игроку.
- Нажатие кнопок приводит к POST запросу по адресу `/match-score?uuid=$match_id`, в полях отправленной формы содержится айди выигравшего очко игрока

Обработчик POST запроса:
- Извлекает из коллекции экземпляр класса Match
- В соответствии с тем, какой игрок выиграл очко, обновляет счёт матча
- Если матч не закончился - рендерится таблица счёта матча с кнопками, описанными выше
- Если матч закончился:
    - Удаляем матч из коллекции текущих матчей
    - Записываем законченный матч в SQL базу данных
    - Рендерим финальный счёт

### Страница сыгранных матчей - `/matches`

Адрес - `/matches?page=$page_number&filter_by_player_name=$player_name`. GET параметры:
- `page` - номер страницы. Если параметр не задан, подразумевается первая страница

Постранично отображает список сыгранных матчей. Для постраничного отображения потребуется реализация пагинации.

Интерфейс:
- Список найденных матчей
- Переключатель страниц, если матчей найдено больше, чем влезает на одну страницу

## Фронтенд

Был реализован мной с помощью html и css стилей, без использования фреймворка Bootstrap.

## База данных

В качестве базы данных используется H2. Это in-memory SQL база для Java. In-memory означает то, что движок БД и сами таблицы существуют только внутри памяти Java приложения. При использовании in-memory хранилища необходимо инициализировать таблицы базы данных при каждом старте приложения.
Также позднее я перестроил проект с использованием фреймворка Hibernate и в качестве базы данных использовал PostreSQL.
#### Таблица `Players` - игроки

| Имя колонки | Тип     | Комментарий                   |
|-------------|---------|-------------------------------|
| id          | Int     | Первичный ключ, автоинкремент |
| name        | Varchar | Имя игрока                    |

Индексы:
- Уникальный индекс колонки `Name` для эффективности поиска игроков по имени и запрета повторяющихся имён

### Таблица `Matches` - завершенные матчи

Для упрощения, в БД сохраняются только доигранные матчи в момент их завершения.

| Имя колонки | Тип | Комментарий                                     |
|-------------|-----|-------------------------------------------------|
| id          | Int | Первичный ключ, автоинкремент                   |
| player1     | Int | Айди первого игрока, внешний ключ на Players.ID |
| player2     | Int | Айди второго игрока, внешний ключ на Players.ID |
| winner      | Int | Айди победителя, внешний ключ на Players.ID     |

## MVCS

MVCS - архитектурный паттерн, особенно хорошо подходящий под реализацию подобных приложений.

## Описание
Это веб-приложение содержит несколько сервлетов и фильтров, обеспечивающих работу с матчами и базой данных H2.

## Учёт счёта матча
Приложение использует 3 сервлета для работы с матчами.

## Сервлеты и их ручки

### 1. H2 Console
- **Класс**: `org.h2.server.web.WebServlet`
- **URL**: `/h2/*`
- **Методы**: `GET`
- **Назначение**: Веб-консоль для работы с базой данных H2.

### 2. New Match
- **Класс**: `servlets.NewMatchServlet`
- **URL**: `/new-match`
- **Методы**: `GET`, `POST`
- **Назначение**: 
  - `GET` — отображает страницу создания нового матча.
  - `POST` — создаёт новый матч и перенаправляет на страницу его счёта.

### 3. Match Score
- **Класс**: `servlets.MatchScoreServlet`
- **URL**: `/match-score`
- **Методы**: `GET`, `POST`
- **Назначение**: 
  - `GET` — отображает текущий счёт матча.
  - `POST` — обновляет счёт матча, определяет победителя и сохраняет завершённый матч.

### 4. Match View
- **Класс**: `servlets.MatchViewServlet`
- **URL**: `/matches`
- **Методы**: `GET`
- **Назначение**: Выводит список всех завершённых матчей с возможностью фильтрации.

## Фильтры

### 1. Charset Filter
- **Класс**: `filters.CharsetFilter`
- **URL**: `/*`
- **Назначение**: Устанавливает кодировку для всех запросов.

### 2. CORS Filter
- **Класс**: `filters.CORSFilter`
- **URL**: `/*`
- **Назначение**: Разрешает междоменные запросы (CORS).

## Как работает приложение
1. Обрабатывает **POST-запросы** к `/match-score`.
2. Через `OngoingMatchesService` получает экземпляр класса `Match` для текущего матча, который является частью модели `MatchScoreModel`.
3. Через `MatchScoreCalculationService` обновляет счёт в матче.
4. Если матч закончился, через `FinishedMatchesPersistenceService` сохраняет его в базу данных.
5. С помощью **JSP-шаблона** отображает `MatchScoreModel` в виде отрендеренного HTML.

## Основные сервисы
- **`OngoingMatchesService`** — хранит текущие матчи и позволяет их записывать/читать.
- **`MatchScoreCalculationService`** — реализует логику подсчёта счёта матча по очкам/геймам/сетам.
- **`FinishedMatchesPersistenceService`** — инкапсулирует чтение и запись законченных матчей в базу данных.

## Тесты

Данный проект включает набор юнит-тестов для проверки логики расчета счета в теннисных матчах. Тестируются различные сценарии, включая стандартные игры, тай-брейки и определение победителя матча.

В проекте используется библиотека JUnit для написания и выполнения тестов. Приведенные ниже тесты проверяют различные аспекты работы сервиса расчета счета.

### 1. `testPlayer1WinsPointAtDeuce`

Проверяет, что игрок 1 выигрывает очко при счете на уровне "дьюс" (40-40).

### 2. `testPlayer1WinsGameFromForty`

Проверяет, что игрок 1 выигрывает игру, если у игрока 2 ноль, а у игрока 1 — 40.

### 3. `testTaiBreakIsTrue`

Проверяет, что переменная `taiBreak` становится `true`, когда оба игрока достигают 6 игр в сете.

### 4. `testPlayer1WinsTieBreak`

Проверяет, что игрок 1 выигрывает сет в тай-брейке, если его счет составляет 7, а у второго игрока — 4.

### 5. `testPlayer1WinstPointInTieBreak`

Проверяет, что игрок 1 выигрывает очко в тай-брейке.

### 6. `testPlayer1WinsGameAfterDeuceAndAdvantage`

Проверяет, что игрок 1 выигрывает игру после "дьюса" и "преимущества".

### 7. `testPlayer1WinSet`

Проверяет, что игрок 1 выигрывает сет, если его счет 5-3 в игре и 40-30 в текущем гейме.

### 8. `testPlayer2WinsSet`

Проверяет, что игрок 2 выигрывает сет при аналогичных условиях для второго игрока.

### 9. `testMatchWinnerPlayer1`

Проверяет, что матч выигрывает игрок 1, если он выигрывает все сеты.

### 10. `testMatchWinnerPlayer2`

Проверяет, что матч выигрывает игрок 2, если он выигрывает все сеты.

## Как запустить тесты

Для запуска тестов можно использовать любой инструмент для работы с JUnit, например, `Maven` или `Gradle`.

### Maven

```bash
mvn test
```

## План работы над приложением

- Классы-модели Hibernate для таблиц БД
- Страница создания нового матча
- Сервисы для хранения текущих матчей и подсчета очков в матче, юнит тесты для подсчёта очков
- Страница счёта матча
- Сервис для сохранения законченного матча в БД
- Сервис поиска законченных матчей по имени игрока
- Страница отображения законченных матчей, поиска матчей по имени игрока
- Деплой на удалённый сервер
