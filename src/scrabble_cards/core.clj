(ns scrabble-cards.core
  (:require [scrabble-cards.sack :as s]))

(defn add-player [{:keys [deck players] :as game-state} player-id]
  (let [new-player {:player-id player-id :cards (take 7 deck) :score 0}]
    (merge game-state {:deck (drop 7 deck) :players (conj players new-player)})))

(defn create-game [no-of-players]
  (let [initial-state {:deck (s/sack) :current-word "" :players []}]
    (reduce add-player initial-state (range no-of-players))))

(defn initialise-game [no-of-players]
  (println "initialising" no-of-players "player game")
  (let [game-state (create-game no-of-players)]
    (println game-state)))

(defn -main [& args]
  (println "Welcome to scrabble cards!!")
  (println "how many players?")
  (let [no-of-players (Integer/parseInt (read-line))]
    (initialise-game no-of-players)))
