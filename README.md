# FinalProject

Randy, I just wanted to let you know that for some reason, we couldn't commit using the command line like we were doing this whole year. So we panicked and tried all we could. Pulling, refreshing, nothing helped. So as the last straw we used the upload files button on the website. That's why you'll probably notice the commits are recent, since this is our fifth or sixth attempt at a normal command line committed repository. Also, the first commit with the src folder is not updated, the up to date files are the files outside of it obviously you can tell by the dates. Again, it's unexplicable as to why the first commit works (hence the src folder) but then it refuses to push and update the src folder. Also, near the end, we both met up and did the final commit, but since we weren't that confident committing the normal way anyway, we decided to commit from Ammar's account. The source files are on time, the Gradle file however will only be a few minutes later than 12am. It's totally up to you as how you mark our participation, but if my words mean anything, I'd like to say we both did relatively the same amount of work. The rest is up to you sir. Now onto the rules...


--Rules--
This is a game of Foxes and Hounds

Hounds are placed onto fields b8, d8, f8, and h8. Fox starts set onto e1. Fox moves first.

Hounds hunting the fox. The fox moves diagonally one field range forward or backward onto an empty adjacent square per turn. Hounds can only move diagonally one field forward (towards row 1) onto an empty adjacent square.

Since only diagonal moves are allowed the game is played on the 32 dark fields of the chess board only.

There is no capturing by jumping and no stacking of checkers. Neither by jumping the opponent as in Checkers nor by moving onto its field replacing the opponent.

Fox wins by reaching the other side of the board (8th row where hounds started their hunt).
Hounds win by blocking the fox such that it cannot move anymore (when fox has no legal move left).

If moving perfect then hounds will win.

The ideal run is for the Fox client and Hound client to send and receive move information(X and Y coordinates of their pieces) 
through the server. This would allow each player to view the other players moves. The sending of information from client to server
was working however the clients would give a pointer exception when trying to read from the server. Due to this the final build does 
not allow the player to play against another player. What happens instead is the player client will just allow the player an infinite 
number of moves(since the the other player is not moving). On their own each client works as expected, albeit missing a few functionalities
due to them not being able to know the other players moves.

The game cannot be won because the Hound player will not update on the Fox screen to allow the Fox to get past, leaving the Fox 
to just move around the board.
