# PROJECT TITLE:
Love Dramatic Piano Academy DX Ultra Super Game

# TEAM MEMBERS:
- BERNARDINO, ADRIEL O. -  adriel-bernardino
- CLOMA JR. MARK D. - markclomajr
- TOLENTINO AMANDA PARIS DOMINIQUE L.  - AmandaLeeTolentino


# PROBLEM STATEMENT & GOALS:
Rhythm games are often perceived as fast paced and intimidating, especially for beginners with no experience in these kinds of games, discouraging casual players or those who might be interested. Moreover, a lot of rhythm games normally offer limited motivation outside of improving the player’s score, making long term engagement difficult to maintain. 

By simplifying the mechanics and integrating an engaging narrative, there is an opportunity to create a game that is beginner friendly while also offering meaningful incentives to keep progressing.


# TARGET USER:
Teens and adults age 13 and above, looking to learn piano. Visual Novel connoisseurs looking for a new gameplay loop. Anime fans looking to experience playing their favorite soundtracks.

# BRIEF DESCRIPTION:
It is a 2D hybrid web application that combines a visual novel storytelling experience with a simplified rhythm game mechanics system. Designed as a gamified introduction to piano playing, the project limits gameplay to right-hand keys (a single octave) to ensure accessibility for beginners and manageable scope for development. Players progress through a music school narrative where their performance in piano lessons influences their relationships with different characters.

# CORE OOP CONCEPTS:
- Encapsulation: In each song class that has its own unique note arrays, tempo, and difficulty rating.
- Inheritance: In the concrete class implementations of GameCharacter such as PianoTeacherMiriko and ClassmateRinTohsaka.
- Polymorphism: In the abstract GameState class that can will be extended by different types of game state classes such as VisualNovelMode, RhythmGameMode, PauseMenu, and MainMenu,
- Abstraction: In abstract GameCharacter class that defines shared attributes for all characters in visual novels (players, npcs)

# INITIAL CLASS IDEAS:
- GameCharacter: blueprint for all playable and non-playable characters in the game

- NPC: An abstract class that extends GameCharacter, that will be the blueprint for non-playable characters.

- Player: An abstract class that extends GameCharacter, that will be the blueprint for the player, with unique player attributes like experience.

- GameState: An interface or abstract class managing whether the application is in MainMenu, VisualNovelMode, or RhythmGameMode.

- Scene: An abstract class or interface with the framework to read the scripts and access the games assets that are required for each scene.

- Song: An abstract class that will have the base attributes for different songs in the game.

- KeyboardUI: The visual keyboard that the player will see.

- KeyboardListener: The class that actually processes the player’s inputs with methods such as keyPressed(), keyHold(), keyLetGo.

# USER STORIES (Recommended):
- As an asian parent, I want my child to play so that he would enjoy learning piano and become the ultimate prodigy.
- As an adult, I want to play so that learning piano would be much more engaging.
- As an anime fan, I want to play so that I will be able to learn how to play my favorite OST on piano.
- As a seasoned visual novel player, I want to play so that I will be able build connections to my waifus through a unique gameplay experience.
- As a pianist, I think this is awesome sauce! (it's literally just sauce). I want piano to be more accessible to anynone, I want everyone to learn piano so we can have piano battles

# CORE FEATURES (Recommended):
- Visual Novel Mode: A text- and choice-driven narrative set in a music school. Players interact with characters, and their choices branch the story.
- Rhythm/Tutorial Mode: A falling-note rhythm game mapped to standard keyboard keys representing piano notes (e.g., A-S-D-F-G-H-J-K for one octave).
- Affection & Branching System: Successfully mastering specific songs unlocks unique narrative routes, allowing the player to romance different waifus (e.g., the strict Piano Teacher or the competitive Classmate).
