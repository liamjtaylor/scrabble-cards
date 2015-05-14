(ns scrabble-cards.core
  (:require [scrabble-cards.sack :as s]
            [clojure.string :as str]))

(def dictionary-file "src/scrabble_cards/british")
(def all-words (str/split-lines (slurp dictionary-file)))

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

(defn parse-input [word]
  (let [[_ a b c] (re-matches #"(.*)\[(.*)\](.*)" word)]
    {:players (str a c)
     :board b
     :total (str a b c)}))

(defn valid-word? [word cards]
  (let [whole-word (str/lower-case (:total (parse-input word)))]
  (if (= whole-word (some #{whole-word} all-words))
    true false)))

(defn letter-on-board? [word current-word]
  (let [board (str/lower-case (:board (parse-input word)))]
  (if (= true (.contains current-word board))
    true false)))


(defn play-round
  [{:keys [deck players current-word] :as game-state}]
  (println "Current word:" current-word)
  (let [{:keys [player-id cards] :as current} (first players)]
    (println "Current Player:" player-id "Cards:" cards "What is your move?")
    (let [line (read-line)]
      (if (valid-word? (str/upper-case line) cards)
        (if (letter-on-board? (str/upper-case line) current-word)
          (println "hello")
          (println "world"))
        (do
          (println "Not a valid word," (str (:total (parse-input line))) "is not in the dictionary")
          (play-round game-state))))



    (merge game-state {:deck (drop 1 deck) :players (conj (vec (rest players)) current)})))

(defn -main [& args]
  (println "Welcome to scrabble cards!!")
  (println "how many players?")
  (let [no-of-players (Integer/parseInt (read-line))]
      (loop [game-state (initialise-game no-of-players)]
        (when (still-going? game-state)
          (recur (play-round game-state))))
    (println "Game over")))
