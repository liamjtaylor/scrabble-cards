(ns scrabble-cards.core)

(defn create-game [no-of-players]
  {:deck []
   :current-word ""
   :players []})

(defn initialise-game [no-of-players]
  (println "initialising" no-of-players "player game")
  (let [game-state (create-game no-of-players)]
    (println game-state)))

(defn -main [& args]
  (println "Welcome to scrabble cards!!")
  (println "how many players?")
  (let [no-of-players (Integer/parseInt (read-line))]
    (initialise-game no-of-players)))
