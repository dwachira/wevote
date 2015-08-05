# JSON Structure #
```
[
 {
  "folder": "first folder",
  "item":
   [
    {
     "id": 1,
     "title": "first item"
    },
    {
     "id": 2,
     "title": "second item"
    }
   ]
 },
 {
  "folder": "second folder",
  "item":
   [
    {
     "id": 3,
     "title": "first item"
    },
    {
     "id": 4,
     "title": "second item"
    }
   ]
 }
]
```

# Explanation #

JSONMenu sends at once, as soon as page was loaded. It collects array of folders, which contains array of items.

**id** — number of question, which uses in RPC request for loading chart information.