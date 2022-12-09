# Документация к API

- [Отделения](#отделения)
    - [Все отделения](#все-отделения)
    - [Поиск](#поиск-отделения)
- [Группы](#группы)
    - [Все группы](#все-группы)
    - [Поиск по ID](#поиск-группы)
- [Расписание](#расписание)
    - [Расписание группы](#расписание-группы)
    - [Расписание группы с заменами](#расписание-группы-с-заменами)
- [Замены в расписании](#замены-в-расписании)
- [Тип недели](#тип-недели)
- [Расписание звонков](#расписание-звонков)

## Отделения

### Все отделения

`curl -X GET /api/departments` - получение всех отделений.

**Пример ответа**

```json
[
  {
    "id": "a19dced47c9224424b0ac966f5fa147c",
    "name": "09.02.01",
    "groups": [
      {
        "id": "96ce33329c197edd4d3a0da7b3f01d52",
        "name": "Э-1-19, Э-11/1-20",
        "department_id": "a19dced47c9224424b0ac966f5fa147c"
      }
    ]
  },
  {
    "id": "a8def7fcfc1932705d763fc48b81c9e8",
    "name": "09.02.06",
    "groups": [
      {
        "id": "758b9e1473fe209a2f94b0626c641794",
        "name": "СА50-1-19, СА50-11/1-20",
        "department_id": "a8def7fcfc1932705d763fc48b81c9e8"
      }
    ]
  }
]
```

### Поиск отделения

`curl -X GET /api/departments?department={id_or_name}` - получение отделения по id или названию.

**Пример ответа**

```json
{
  "id": "f7d56bc1e8c0415bc1a88b8bf4982a61",
  "name": "09.02.07",
  "groups": [
    {
      "id": "f72b283f2d58193d9c1f5814b13384e3",
      "name": "БД50-1-19, БД50-11/1-20",
      "department_id": "f7d56bc1e8c0415bc1a88b8bf4982a61"
    }
  ]
}
```

**Пример ответа с ошибкой**

```
Status code: 400 (BAD REQUEST)
Department not found
```

## Группы

### Все группы

`curl -X GET /api/groups` - получение всех групп.

**Пример ответа**

```json
[
  {
    "id": "96ce33329c197edd4d3a0da7b3f01d52",
    "name": "Э-1-19, Э-11/1-20",
    "department_id": "a19dced47c9224424b0ac966f5fa147c"
  },
  {
    "id": "c9b8fc80cb5cb97cb5d053754f366f80",
    "name": "Э-1-20",
    "department_id": "a19dced47c9224424b0ac966f5fa147c"
  }
]
```

### Поиск группы

`curl -X GET /api/groups?group={id_or_name}` - получение группы по id или названию.

**Пример ответа**

```json
{
  "id": "62b622b043c1ef5fcf6ae11b647f3b17",
  "name": "ВД50-2-21",
  "department_id": "f7d56bc1e8c0415bc1a88b8bf4982a61"
}
```

**Пример ответа с ошибкой**

```
Status code: 400 (BAD REQUEST)
Group not found
```

## Расписание

### Расписание группы

`curl -X GET /api/schedule/{group_id_or_name}` - получение расписания по id или названию группы.

**Пример ответа**

```json
{
  "week_label": "Знаменатель",
  "group_id": "c0c0f0c0e9bfd5e3521de71ef4a36e74",
  "days": [
    {
      "day_index": 5,
      "day_name": "Пятница",
      "branch": "Нахимовский",
      "rows": [
        {
          "type": "single",
          "lesson_number": 1,
          "lesson": "Физическая культура",
          "teacher": "А.Б. Вгдейкин",
          "default": true,
          "time_table": {
            "lesson_number": 1,
            "start_time": {
              "hour": 8,
              "minute": 30
            },
            "end_time": {
              "hour": 10,
              "minute": 0
            }
          }
        },
        {
          "type": "single",
          "lesson_number": 2,
          "lesson": "Экономика отрасли",
          "teacher": "Ж.З. Ийкин",
          "default": true,
          "time_table": {
            "lesson_number": 2,
            "start_time": {
              "hour": 10,
              "minute": 10
            },
            "end_time": {
              "hour": 11,
              "minute": 40
            }
          }
        },
        {
          "type": "divided",
          "lesson_number": 3,
          "numerator": {
            "lesson": "Технология разработки и защиты баз данных",
            "teacher": "А.Б. Вгдейкин",
            "default": true
          },
          "denominator": {
            "lesson": "Обеспечение качества функционирования КС",
            "teacher": "Ж.З Ийкин",
            "default": true
          },
          "default": true,
          "time_table": {
            "lesson_number": 3,
            "start_time": {
              "hour": 12,
              "minute": 0
            },
            "end_time": {
              "hour": 13,
              "minute": 30
            }
          }
        },
        {
          "type": "single",
          "lesson_number": 4,
          "lesson": "Обеспечение качества функционирования КС",
          "teacher": "А.Б. Вгдейкин",
          "default": true,
          "time_table": {
            "lesson_number": 4,
            "start_time": {
              "hour": 14,
              "minute": 0
            },
            "end_time": {
              "hour": 15,
              "minute": 30
            }
          }
        }
      ]
    }
  ]
}
```

**Пример ответа с ошибкой**

```
Status code: 400 (BAD REQUEST)
Schedule not found
```

### Расписание группы с заменами

`curl -X GET /api/schedule/{group_id_or_name}?merge=true` - получение расписания с
заменами по id или названию группы.

**Пример ответа**

```json
{
  "week_label": "Знаменатель",
  "group_id": "c0c0f0c0e9bfd5e3521de71ef4a36e74",
  "days": [
    {
      "day_index": 6,
      "day_name": "Суббота",
      "branch": "Нежинская",
      "rows": [
        {
          "type": "single",
          "lesson_number": 3,
          "lesson": "Технологии физического уровня передачи данных",
          "teacher": "А.Б. Вгдейкин",
          "default": false,
          "time_table": {
            "lesson_number": 3,
            "start_time": {
              "hour": 12,
              "minute": 0
            },
            "end_time": {
              "hour": 13,
              "minute": 30
            }
          }
        },
        {
          "type": "single",
          "lesson_number": 4,
          "lesson": "Элементы высшей математики",
          "teacher": "Ж.З. Ийкин",
          "default": false,
          "time_table": {
            "lesson_number": 4,
            "start_time": {
              "hour": 14,
              "minute": 0
            },
            "end_time": {
              "hour": 15,
              "minute": 30
            }
          }
        }
      ]
    }
  ]
}
```

**Пример ответа с ошибкой**

```
Status code: 400 (BAD REQUEST)
Schedule not found
```

## Замены в расписании

`curl -X GET /api/changes/{group_id_or_name}` - получение замен в расписании по id или названию
группы.

**Пример ответа**

```json
{
  "group_id": "БД50-1-21",
  "days": [
    {
      "timestamp": 1670619600000,
      "day_index": 6,
      "day_name": "Суббота",
      "rows": [
        {
          "type": "canceled",
          "lesson_number": 3,
          "lesson": "Отмененная пара",
          "teacher": "А.Б. Вгдейкин",
          "cause": "Reworked",
          "insert_timestamp": 1670580038000
        },
        {
          "type": "additional",
          "lesson_number": 3,
          "lesson": "Добавленная пара",
          "teacher": "Ж.З. Ийкин",
          "insert_timestamp": 1670579421000
        },
        {
          "type": "replace",
          "lesson_number": 4,
          "replaced_lesson": "Что заменили",
          "replaced_teacher": "А.Б. Вгдейкин",
          "replacement_lesson": "На что заменили",
          "replacement_teacher": "Ж.З. Ийкин",
          "insert_timestamp": 1670579421000
        },
        {
          "type": "moved",
          "lesson": "Переставленная пара",
          "teacher": "А.Б. Вгдейкин",
          "moved_from": 5,
          "moved_to": 3,
          "insert_timestamp": 1670579421000
        }
      ]
    }
  ]
}
```

**Пример ответа с ошибкой**

```
Status code: 400 (BAD REQUEST)
Changes not found
```

## Тип недели

`curl -X GET /api/week-label` - получение текущего типа недели.

**Пример ответа**

```json
{
  "name": "Numerator",
  "display_name": "Числитель"
}
```

## Расписание звонков

`curl -X GET /api/timetable` - получение расписания звонков.

**Пример ответа**

```json
{
  "rows": [
    {
      "lesson_number": 1,
      "start_time": {
        "hour": 8,
        "minute": 30
      },
      "end_time": {
        "hour": 10,
        "minute": 0
      }
    },
    {
      "lesson_number": 2,
      "start_time": {
        "hour": 10,
        "minute": 10
      },
      "end_time": {
        "hour": 11,
        "minute": 40
      }
    },
    {
      "lesson_number": 3,
      "start_time": {
        "hour": 12,
        "minute": 0
      },
      "end_time": {
        "hour": 13,
        "minute": 30
      }
    }
  ]
}
```