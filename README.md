# GameDiff

An Android trivia-style game where you compare two game cards and try to keep your streak going. The app includes user accounts and saves your **best score (high score)** locally using **Room** so your top score persists across app restarts.

---

## What the app does

### Core gameplay
- The app shows **two game cards** (top and bottom).
- You make a guess by tapping one of the cards (based on the rule shown in the game screen).
- If your guess is correct:
  - Your **score increases**
  - The game continues with the next round
- If your guess is wrong:
  - The game ends (**Game Over**)
  - You can retry or quit

### Accounts and saved high scores
- Users can **create an account** (sign up) and **log in**
- The app stores each user's **best score** in a local database using **Room**
- Only the **highest score ever achieved** is saved
  - If you score lower than your best score, it will **not overwrite**
- The best score is displayed on the **top-left of the game screen**

---

## How to use the app

### 1) Sign up / Log in
1. Open the app
2. If you don’t have an account, go to **Sign Up**
3. Create a username + password
4. Log in with the same credentials

### 2) Start playing
1. From the **Main Menu**, press **Play**
2. The game screen will open:
   - Your current **Score** is shown
   - Your saved **Best Score** is shown (top-left)

### 3) Make guesses
- Tap one of the game cards to submit your choice
- Each correct guess adds +1 to your score
- A wrong guess ends the run

### 4) Retry / Quit
- **Retry** starts a new run (score resets to 0)
- **Quit** returns you to the main menu

### 5) High score behavior
- Your best score saves automatically when the game ends
- It persists even after closing/restarting the app
- Only a higher score overwrites the previous best score

---

## Tech stack / features used

- **Kotlin**
- **Jetpack Compose** (UI)
- **Room** (local database for users + bestScore)
- **RetroFit** (Used to make interactions with RAWG API)
- **MVVM** (ViewModel manages game state)

---
