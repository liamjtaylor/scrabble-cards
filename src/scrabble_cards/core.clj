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
  (create-game no-of-players))

(defn still-going? [game-state]
  (-> game-state :deck empty? not))

(defn valid-word? [word player]
  true)

(defn parse-input [word]
  (let [[_ a b c] (re-matches #"(.*)\[(.*)\](.*)" word)]
    {:players (str a c)
     :board b})

(defn play-round
  [{:keys [deck players current-word] :as game-state}]
  (println "Current word:" current-word)
  (let [{:keys [player-id cards] :as current} (first players)]
    (println "Current Player:" player-id "Cards:" cards "What is your move?")
    (let [line (read-line)
          line (clojure.string/uppercase)
          ]
      (if (valid-word? (clojure.string/uppercase (read-line)))
        (parse-input line)
        )



    (merge game-state {:deck (drop 1 deck) :players (conj (vec (rest players)) current)})))

(defn -main [& args]
  (println "Welcome to scrabble cards!!")
  (println "how many players?")
  (let [no-of-players (Integer/parseInt (read-line))]
      (loop [game-state (initialise-game no-of-players)]
        (when (still-going? game-state)
          (recur (play-round game-state))))
    (println "Game over")))
