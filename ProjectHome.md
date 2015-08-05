# What does this system do? #
Mobile Voting System allows to provide voting among multiple users using mobile phone SMS technology and publish outcome to the web-server.

In order to get system work you need:
  * GSM modem and driver for it
  * JDK 6 or latest
  * WeVote program

In downloading tab you can find drivers for mc35 GSM modem, which was used as an example, documentation, WeVote program and putty program for testing connections with modem.

# What does this system do for a user? #
  * Users sends SMS to GSM modem
    1. if not registered — containing age/birthday and (or) gender (e.g. "M16", "f17/04/1989")
    1. if registered — empty
  * GSM modem veryfies users
    1. if not registered & sms contains age/birthday and (or) gender : register to the system
    1. if registered — pick user from database
  * Admin creates a poll(topic) and questions(polls) — multiple choice
  * GSM modem sends question sms to the users
  * Users replies to questions — "a"/"B" etc.
  * Program collects sms into database

# What does the system do for an admin? #
  * Login
  * Settings for GSM modem
    1. SIM-card code
    1. Port number
  * Collects list of users
  * Checks amount of money in SIM-card
  * Creates pool and questions
  * Received answers
    1. correct — accept & upload into database
    1. incorrect — resubmit question
  * Logout

# Problems with modem? #
Testing the connections is easy:
  * plugin **GSM modem** and run **putty**
  * choose **"Serial"** option and **COM-port**
  * in opened terminal type **"AT"**
  * correct responds from modem must be **"OK"**

WeVoteClient is able to search for COM ports and pick the one that has a pre-defined modem connected to it.