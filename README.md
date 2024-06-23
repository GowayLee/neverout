# NeverOut !
*A brilliant Rouge Game proudly presented by Mamba Studio.*

## System Architecture 🛠
In order to enhance scalability and maintainability, the game is divided into several components, each with its own responsibilities. The architecture follows a specific designed pattern, ensuring separation of concerns and easy integration of new features. 
![Total](docs\images\total.png "total")

### Level Controller
When we launch the application, LevelController will be initialized first. It controlles all the components in View Layer
 - Fixed Menu (Main Menu, Game Over Menu)
 - Dynaic Menus (Level Menu, Pause Menu, In-game HUD)
 - Game View

Also, there is a Context Manager embeded in LevelController, which is used to manage the game context, such as game state, game mode, and game settings. Context Manager will be initialized when the game starts, and will be updated when the game state changes, for example, when the player enter the next level, the difficulty will be increased.
Here, we deploy the Strategy Pattern, different implementation of inteface CtxLogic can be provided in order to change to different game mode.
![LevelController](docs\images\LevelController.png "LevelController")

### Game Engine
After starting the game, the GameEngine will be created. It is responsible for the game logic, including the movement of the player, the generation of monsters and the collision detection. The GameEngine will be initialized with a LevelController, which will provide the necessary information about the game state and settings by passing Context.
In the Engine, AnimationTimer take the responsibility for controlling the game cycle to create frames. In each frame, LogicManager will update the game state.
Here we use Strategy Pattern again, different implementation of inteface Logic can be provided in order to change to different game mode.
Inside LogicManager, there is the Logic Layer, all the game logic are running in this layer. The Logic Layer contains the following components: Collision Detection for monsters for bullets, and Game Over Detection.
![GameEngine](docs\images\GameEngine.png "GameEngine")

### Event Manager
To separate event detection logic and event handler logic, we design an EventManager. Once the Logic Layer determine that some event has happened, it will pass the event to EventManager, and EventManager will handle the event. EventManager will also provide a way for other components to register event listeners.
This feature can make our system more scalable, because we can add new event listeners without modifying the existing code. For example, if we want to add a new feature that when the player collects a coin, the coin will disappear, we can add a new event listener to EventManager, and modify the existing code to pass the event to EventManager.
![EventManager](docs\images\EventManager.png "EventManager")

## Entity Factory
There are numberous objects in the one round game, it will cause a lot of memory allocation and deallocation. To solve this problem, we design an EntityFactory. 
EntityFactory will create certain number of objects in the beginning of the game, and store them in a pool. When we need to use an object, we will get it from the pool, and when we don't need it anymore, we will put it back to the pool. This way, we can reuse the objects, and reduce the memory allocation and deallocation.
![EntityFactory](docs\images\Factory.png "EntityFactory")