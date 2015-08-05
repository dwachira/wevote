# JSON Structure #
```
{
 "id": 1,
 "title": "title of the question",
 "pool": "name of the pool",
 "answers":
  [
   {
    "age": [0,0,0,0,0,0],
    "answer": "first answer",
    "gender": [0,0],
    "rating": 0
   },
   {
    "age": [0,0,0,0,0,0],
    "answer": "secont answer",
    "gender": [0,0],
    "rating": 0
   }
  ],
 "question": "actual text of the question",
 "date": "YYYY-MM-DD HH:MM:SS.0"
}
```

# Explanation #

All following parameters are use in flash charts: ByRating, ByGender and ByAge.

**rating** — amount of users, which votes for current answer.

**gender** — same as rating, except it contains separate cells for genders:
  * 0 — male
  * 1 — female

**age** — same as rating, except it contains separate cells for ages:
  * 0 — +17
  * 1 — 18..24
  * 2 — 25..34
  * 3 — 35..44
  * 4 — 45..54
  * 5 — 55+