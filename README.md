# API
- [Отделения](#отделения)
    - [Все отделения](#все-отделения)
    - [Поиск по ID](#поиск-отделения-по-id)
    - [Поиск по названию](#поиск-отделения-по-названию)
- [Группы](#группы)
    - [Все группы](#все-группы)
    - [Поиск по ID](#поиск-группы-по-id)
    - [Поиск по названию](#поиск-группы-по-названию)
- [Расписание](#расписание)
    - [Расписание группы по ее ID](#расписание-группы-по-ее-id)
    - [Расписание группы по ее названию](#расписание-группы-по-ее-названию)
- [Изменения в расписании](#изменения-в-расписании)
    - [Изменения в расписании группы по ее ID](#изменения-в-расписании-группы-по-ее-id)
    - [Изменения в расписании группы по ее названию](#изменения-в-расписании-группы-по-ее-названию)

## Отделения
### Все отделения
`curl -X GET /api/departments/all` - получение всех отделений

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
            },
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
            },
        ]
    },
]
```

### Поиск отделения по ID
`curl -X GET /api/departments/byId/{department_id}` - получение отделения по его id

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
        },
    ]
}
```

**Пример ответа с ошибкой**
```
Status code: 400 (BAD REQUEST)
Department not found
```

### Поиск отделения по названию
`curl -X GET /api/departments/byName/{name}` - получение отделения по его названию

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
        },
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
`curl -X GET /api/groups/all` - получение всех групп

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
    },
]
```

### Поиск группы по ID
`curl -X GET /api/groups/byId/{group_id}` - получение группы по ее id

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

### Поиск группы по названию
`curl -X GET /api/groups/byName/{name}` - получение группы по ее названию

**Пример ответа**
```json
{
    "id": "c0c0f0c0e9bfd5e3521de71ef4a36e74",
    "name": "П50-4-19, П50-11/4-20",
    "department_id": "f7d56bc1e8c0415bc1a88b8bf4982a61"
}
```

**Пример ответа с ошибкой**
```
Status code: 400 (BAD REQUEST)
Group not found
```

## Расписание
### Расписание группы по ее ID
`curl -X GET /api/schedule/byId/{group_id}` - получение расписания группы по ее id

**Пример ответа**
```json
{
    "group_id": "c0c0f0c0e9bfd5e3521de71ef4a36e74",
    "days": [
        {
            "day_index": 1,
            "day_name": "Понедельник",
            "branch": "Нахимовский",
            "rows": [
                {
                    "type": "single",
                    "lesson_number": 1,
                    "lesson": "Физическая культура",
                    "teacher": "А.В. Андрюков"
                },
                {
                    "type": "single",
                    "lesson_number": 2,
                    "lesson": "Экономика отрасли",
                    "teacher": "Л.Ю. Попова"
                },
                {
                    "type": "divided",
                    "lesson_number": 3,
                    "numerator": {
                        "lesson": "Технология разработки и защиты баз данных",
                        "teacher": "А.А. Комаров"
                    },
                    "denominator": {
                        "lesson": "Обеспечение качества функционирования КС",
                        "teacher": "А.Д. Горбунов"
                    }
                },
                {
                    "type": "single",
                    "lesson_number": 4,
                    "lesson": "Обеспечение качества функционирования КС",
                    "teacher": "А.Д. Горбунов"
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

### Расписание группы по ее названию
`curl -X GET /api/schedule/byName/{name}` - получение расписания группы по ее названию

**Пример ответа**
```json
{
    "group_id": "c0c0f0c0e9bfd5e3521de71ef4a36e74",
    "days": [
        {
            "day_index": 1,
            "day_name": "Понедельник",
            "branch": "Нахимовский",
            "rows": [
                {
                    "type": "single",
                    "lesson_number": 1,
                    "lesson": "Физическая культура",
                    "teacher": "А.В. Андрюков"
                },
                {
                    "type": "single",
                    "lesson_number": 2,
                    "lesson": "Экономика отрасли",
                    "teacher": "Л.Ю. Попова"
                },
                {
                    "type": "divided",
                    "lesson_number": 3,
                    "numerator": {
                        "lesson": "Технология разработки и защиты баз данных",
                        "teacher": "А.А. Комаров"
                    },
                    "denominator": {
                        "lesson": "Обеспечение качества функционирования КС",
                        "teacher": "А.Д. Горбунов"
                    }
                },
                {
                    "type": "single",
                    "lesson_number": 4,
                    "lesson": "Обеспечение качества функционирования КС",
                    "teacher": "А.Д. Горбунов"
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

## Изменения в расписании
### Изменения в расписании группы по ее ID
`curl -X GET /api/changes/byId/{group_id}` - получение изменений в расписании группы по ее id

**Пример ответа**
```json
{
    "group_id": "62b622b043c1ef5fcf6ae11b647f3b17",
    "days": [
        {
            "timestamp": 1666386000000,
            "day_index": 6,
            "day_name": "Суббота",
            "rows": [
                {
                    "type": "additional",
                    "lesson_number": 3,
                    "replacement_lesson": "Дискретная математика с элементами математической логики Ю.А. Калашникова",
                    "insert_timestamp": 1666344657000
                },
                {
                    "type": "replace",
                    "lesson_number": 5,
                    "replaced_lesson": "Астрономия А.Е. Шахбазян",
                    "replacement_lesson": "Занятие отменено с последующей отработкой",
                    "insert_timestamp": 1666344752000
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

### Изменения в расписании группы по ее названию
`curl -X GET /api/changes/byName/{name}` - получение изменений в расписании группы по ее названию

**Пример ответа**
```json
{
    "group_id": "62b622b043c1ef5fcf6ae11b647f3b17",
    "days": [
        {
            "timestamp": 1666386000000,
            "day_index": 6,
            "day_name": "Суббота",
            "rows": [
                {
                    "type": "additional",
                    "lesson_number": 3,
                    "replacement_lesson": "Дискретная математика с элементами математической логики Ю.А. Калашникова",
                    "insert_timestamp": 1666344657000
                },
                {
                    "type": "replace",
                    "lesson_number": 5,
                    "replaced_lesson": "Астрономия А.Е. Шахбазян",
                    "replacement_lesson": "Занятие отменено с последующей отработкой",
                    "insert_timestamp": 1666344752000
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
