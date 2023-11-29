## EasyPass
EasyPass is a straight-forward command-line tool that allows you to:
  - Generate new passwords
  - Store existing passwords
  - Check the strength of passwords
  - See if passwords have been leaked in previous data breaches.
## Password Generation
Password generation with EasyPass is made trivial by simply taking in the parameters of the password you want to build.
  - **Length:** Enter the desired length of the password. This can be any number greater than 0 and less than or equal to 1000.
  - **Capitals:** Enter the number of characters in the password that you would like to be capital, as opposed to lowercase. This number may not exceed the number of characters in the password.
  - **Numbers:** Enter the amount of numbers desired in the password. This number may not exceed the length of the password.
  - **Special Characters:** Enter the number of special characters desired in the password. This number may not exceed the length of the password.
## Password Strength Testing
In order to test the strength of the desired password, simply enter it into the entry field and hit *Enter*. This password will be checked against the most well-known password word-lists such as *RockYou* and two of the *SecLists* word-lists. If it is found within any of these lists, you will be notified that your password is extremely weak and is likely to be compromised. Otherwise, if it is not found within these lists, it will be scored according to how complex it is (judging by the randomness, length, and the number of special characters, capitals, and numbers that form it).

## Notice
This application is still under development so if you find any bugs or issues in the source code, please create an issue or reach out!
