# Project 01 Retrospective and overview

[Video Walkthrough]
<!-- Ads have really ruined rick-rolling. -->
[Github Repo](https://github.com/ayaayethan/project-1-trivia-game.git)

## Overview
This is a sports betting assitant that makes use of an API we found [here](https://github.com/public-apis/public-apis?tab=readme-ov-file).

## Introduction

* How was communication managed?
 + Coms were primarily managed on Discord and in-person meeting during class.
* How many stories/issues were initially considered?
  + Initially a total of 20 User stories were considered.
* How many stories/issues were completed
  + A total of 15 user stories and 43 issues were completed.

## Team Retrospective

### Ethan Ayaay
1. Ethan's Github PRs are [here](https://github.com/ayaayethan/project-1-trivia-game/pulls?q=is%3Apr+is%3Aclosed+author%3Aayaayethan)
2. Ethan's Github issues are [here](https://github.com/ayaayethan/project-1-trivia-game/issues?q=is%3Aissue%20state%3Aclosed%20author%3Aayaayethan)

#### What was your role / which stories did you work on
I primarily worked on setting up our API by using the RetroFit library to fetch the data that we needed. I also created the Retrofit instance, repositories, data classes, as well as the GamesViewModel which contained methods used to fetch games, initialize gameplay, make guesses, and swap games from the stage.
+ What was the biggest challenge? 
+ Why was it a challenge?
 +  The biggest challenge was figuring out how to properly query games from the API in the most efficient way. Since we would need to swap out a game with a new one after a user makes a guess, we thought that querying a new game from the API after each guess might make the gameplay feel slow and less responsive.
+ How was the challenge addressed?
 +  Instead of querying games one at a time, we decided to query games in bulks of 10. These games were then stored in a queue, and games were pulled from this queue after each guess. Once the queue only had 2 items remaining, we would fetch another 10 games from the API and reload the queue. This way, we were able effectively paginate our data, allowing us to make requests less frequently, and speed up the response time of swapping out old games with a new one.
+ Favorite / most interesting part of this project?
 + My favorite part of the project was getting to use and learn more about Composable UI. Compared to Java, using Composable allowed us to create modular components that were reusable across the entire app. This was especially useful when creating the game cards you see during gameplay. 
+ If you could do it over, what would you change?
  + I would utilize Github issues more as well as the project kanban board to keep myself more organized. I also would have documented my code and changes more thoroughly, as well as making more thorough PR descriptions.
+ What is the most valuable thing you learned?
  + The most valuable thing I learned was effectively working in a team—managing priorities and tasks, and conducting meaningful code reviews to ensure incoming pull requests aligned with our project standards


### Justin Martinez
1. Justin's pull requests are [here](https://github.com/ayaayethan/project-1-trivia-game/pulls?q=is%3Apr+is%3Aclosed+author%3Awhosteenie)
2. Justin's Github issues are [here](https://github.com/ayaayethan/project-1-trivia-game/issues?q=is%3Aissue%20state%3Aclosed%20author%3Awhosteenie)

#### What was your role / which stories did you work on
I built the base skeleton for the app screen flow and tracking score. I also implemented preloading of images to improve load times in the app.
+ What was the biggest challenge? 
+ Biggest challenge was learning Kotlin and Composable UI
+ Why was it a challenge?
  + I have used Java plenty before, but the structure of the Composable UI is something I have never done before. Having that learning curve while also working in a group was a new challenge.
+ How was the challenge addressed?
 + Reviewing examples and iterating to see what worked allowed me to get an idea of what was expected.
+ Favorite / most interesting part of this project
  + The final product was very satisfying, it plays like a real game. Being able to see how it all works under the hood is a great insight.
+ If you could do it over, what would you change?
  + I would use GitHub issues a lot more, to make sure I always had something to do.
+ What is the most valuable thing you learned?
  + I learned that merge conflicts can very easily present a brick wall, and that it's important to communicate with your teammates to avoid breaking their changes.

### Bhavishya Dash
1. Bhavishya's pull requests are [here](https://github.com/ayaayethan/project-1-trivia-game/pulls?q=is%3Apr+is%3Aclosed+author%3Abhavishyadash)
2. Bhavishya's Github issues are [here](https://github.com/ayaayethan/project-1-trivia-game/issues?q=is%3Aissue%20state%3Aclosed%20author%3Abhavishyadash)

#### What was your role / which stories did you work on
I led the Room database implementation for user accounts and persistence, improved the app’s UI, added full dark mode theming, and implemented the high score system that saves and displays each user’s best score from the database.

+ What was the biggest challenge? 
  + Fixing the Gradle Sync errors.
+ Why was it a challenge?
  + Diffrent platforms, caused some gradle issues that I have never seen before. It caused me to merge my first PR dark.
  + How was the challenge addressed?
  + I used ChatGPT for help, and also asked Roberto once.
+ Favorite / most interesting part of this project
  + To be honest nothing, but to answer this question I would say trying diffrent UI themes was somewhat "interesting".
+ If you could do it over, what would you change?
  + Not take Gradle Sync issues lightly, and try to be the first one to commit to the Repo so everyone else would follow.
+ What is the most valuable thing you learned?
  + Do the work early and document everything simultaenously. As we have already finished our project and tomorrow is submission, its sort of hard to recall where i found dificulty for the stuff I did 3 weeks ago.

### Anthony Brunetto
1. Anthony's pull requests are [here](https://github.com/ayaayethan/project-1-trivia-game/pulls?q=is%3Apr+is%3Aclosed+author%3AAnthony-Brunetto)
2. Anthony's Github issues are [here](https://github.com/ayaayethan/project-1-trivia-game/issues?q=is%3Aissue%20state%3Aclosed%20author%3AAnthony-Brunetto)

#### What was your role / which stories did you work on
I mainly worked on the gameplay screen UI providing feedback for correct and incorrect guesses, smoothly transitioning between rounds, and the help screen providing gameplay instructions.

+ What was the biggest challenge? 
  + Learning how to properly review pull requests
+ Why was it a challenge?
  +  This was my first time using pull requests
  +Initially I didn't know how to properly review a pull request
+ How was the challenge addressed?
  + I went to the TA for help and used ChatGPT and web resources to get more comfortable with git.
+ Favorite / most interesting part of this project
  + In class lectures about how to be a good reviewer
  + Researching online best practices
+ If you could do it over, what would you change?
  + I would probably use only one activity rather than two
+ What is the most valuable thing you learned?
  + Definitely learning GitHub issues + pull requests and how to be a code reviewer

## Conclusion
- How successful was the project?
  - I think the project was a complete success. We met all deadlines efficiently, all of the team members were competent and focused to understand the situation properly. Overall team dynamics were very high and smooth and there was no communication gaps in between. 
- What was the largest victory?
 - The Larget VICTORY was when we got the main game functionality working. When the images were properly showing up and it was actually recording our scores and saving it to the database, we all felt so much accomplished.
- Final assessment of the project
 - This project was very helpful in preparing us for both capstone and real-life projects. This project taught us that communication and documentation is key. Though we succeeded at communication, we never thought of the later. The project is finished and right now while filling this Readme we are scravenging around our previous submissions to look for our orginal plans and user stories. Documenting changes and enhancement is key. As a conclusion of this assesment, we would like to say that overall, our actions were very quick and decisive while fixing bugs and errors, overall Team cooporation was very high. And if we get a chance, we will do it again.
