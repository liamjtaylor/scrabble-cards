(ns scrabble-cards.sack
  (:require [clojure.string :as string]))

(def letters
  "Scrabble letter distribution data"
  [[:blank 2 0]
   [\E 12 1]
   [\A 9 1]
   [\I 9 1]
   [\O 8 1]
   [\N 6 1]
   [\R 6 1]
   [\T 6 1]
   [\L 4 1]
   [\S 4 1]
   [\U 4 1]
   [\D 4 2]
   [\G 3 2]
   [\B 2 3]
   [\C 2 3]
   [\M 2 3]
   [\P 2 3]
   [\F 2 3]
   [\H 2 3]
   [\V 2 3]
   [\W 2 3]
   [\Y 2 3]
   [\K 1 5]
   [\J 1 8]
   [\X 1 8]
   [\Q 1 10]
   [\Z 1 10]])

(def sack
  (shuffle (flatten (map #(repeat (second %) (first %)) scrabble-letters))))

(def points
  (into {} (map #(vec [(first %) (last %)]) scrabble-letters)))

