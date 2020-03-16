# Totally-not-a Scrabble Cheater
### Written by Leo Belyi
##### (shhhhh)

### Why?
Why not

### How do I use it?
So this is a maven project so if you want to run it on
your machine you can clone the project and then run `mvn package`.
In the `target/` folder there will be a `.jar` file that you
can run with `java -jar MY_JAR.jar`. Make sure you actually have
both maven and a JVM installed. This will run the program on
a test scrabble game that I verified on another website.

### Configuring it?
So I make use of CSV files because that's the best way I've found
to make this work and it makes it slightly easier to visualize.
Hopefully I've written it in a way that you could export an
excel spreadsheet (fingers crossed). But I haven't tested that
sooooo best of luck.

The main things to configure are the `NUM_MOVES` variable in
`Main.java` if you prefer to view more than 5 best moves and
in order to actually put in your scrabble board and tile-set
you can go into the `src/main/resources/letterBoards/` folder
to see all of the available CSVs. My suggestion is to copy
the contents from the one named `scrabbleOUTLINE.csv` and
making that how you need it. It should be fairly intuitive to
create, just replacing the spaces with the letters where they
are applicable. View my test CSVs for the examples. Then you
just go into `Main.java` again and update the
`letterBoardResourcePath` variable to reflect the new file you
added. After that, you can just go through the same process of 
running it as before and VOILA your best moves are printed!

### Current Problems:
* I think the word file I got was bogus... Has some words
that at very least don't work on my Scrabble Go app. Maybe
I should find another file for it.
* Not as fast as it could be... A while back I wrote a bad
version for Python and this is like 10x faster than that
was, but that's not saying much.

### Questions and Comments
You can leave an issue here or feel free to email me at
leonid@ac93.org. Send me a meme or something to and I'll
definitely read it.