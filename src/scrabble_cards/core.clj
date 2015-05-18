(ns scrabble-cards.core
  (:require [scrabble-cards.sack :as s]
            [clojure.string :as str]
            [clojure.set :as st]))

(def dictionary-file "src/scrabble_cards/british")
(def all-words (str/split-lines (slurp dictionary-file)))

(def select-values (comp vals select-keys))

(defn build-hand [memo card]
  (if (> (first (select-values memo card)) 0)
    (assoc memo (keyword card) (+ (first (select-values memo card)) 1))
    (assoc memo (keyword card) 1)))

(defn deal-hand [deck players]
  (reduce {} (fn [memo card] (build-hand memo card)) (take 7 deck)))


; {\A 0 :b 0 :c 0 :d 0 :e 0 :f 0 :g 0 :h 0 :i 0 :j 0 :k 0 :l 0 :m 0 :n 0 :o 0 :p 0 :q 0 :r 0 :s 0 :t 0 :u 0 :v 0 :w 0 :x 0 :y 0 :z 0}

(defn add-player [{:keys [deck players] :as game-state} player-id]
  (let [new-player {:player-id player-id :cards (deal-hand deck players) :score 0}]
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

(defn has-letters-for-word? [word users-letters]
  (empty? (st/difference (set word) (set users-letters))))

(defn occurrences-in-str [character string]
  (count (filter #{character} string)))

(defn has-enough-letters? [word users-letters]
 (every? (fn [character] (>= (occurrences-in-str character users-letters) (occurrences-in-str character word))) word))

(defn player-has-cards? [word users-letters]
  (and (has-letters-for-word? word users-letters) (has-enough-letters? word users-letters)))


(defn play-round
  [{:keys [deck players current-word] :as game-state}]
  (println "Current word:" current-word players)
  (let [{:keys [player-id cards] :as current} (first players)]
    (println "Current Player:" player-id "Cards:" cards "What is your move?")
    (let [line (read-line)]
      (println line)
      (if (valid-word? (str/upper-case line) cards)
        (if (letter-on-board? (str/upper-case line) current-word)
          (if (player-has-cards? (str/upper-case (:players (parse-input line))) (str/upper-case (str cards)))
            (println "has letters")
            (println "does not have letters"))
          (println "letters not in current word"))
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
