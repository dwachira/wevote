# History Token Structure #
```
 http://wevote.com/#/question/45 — load Question number 45 (if does not exists — load Error tab)
 http://wevote.com/#/system — load System tab
```

# Explanation #

Supports history token by
  * opening proper tab
  * expanding proper folder
  * back & forward browser buttons
  * if requested tab is already opened, no need to load it form server, instead show it & update history

History is separated on following sectors:

**/#/question** — explains number of question to load<br>
<b>/#/system</b> — uses for entry system tab